package com.guk2zzada.armetro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    ListView list;
    Button btnZoom;
    Button btnSave;
    Button btnScan;
    Button btnLocation;
    Button btnAuto;
    EditText edtMyLoc;

    ArrayList<SSIDItem> arrayList;
    WifiManager wifiManager;
    List<ScanResult> scanResults;
    SSIDAdapter adapter = new SSIDAdapter();

    StaticVar sv;

    //Location Permission을 위한 필드
    public static final int MULTIPLE_PERMISSIONS = 10;

    //원하는 권한을 배열로
    String[] permissions = new String[] {
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    protected void onStart() {
        super.onStart();
        if(Build.VERSION.SDK_INT >= 23) {
            if(!checkPermissions()) {
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        edtMyLoc.setText(sv.getStation());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.list);
        btnZoom = findViewById(R.id.btnZoom);
        btnSave = findViewById(R.id.btnSave);
        btnScan = findViewById(R.id.btnScan);
        btnLocation = findViewById(R.id.btnLocation);
        btnAuto = findViewById(R.id.btnAuto);
        edtMyLoc = findViewById(R.id.edtMyLoc);

        sv = StaticVar.getInstance();
        edtMyLoc.setText(sv.getStation());

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        final BroadcastReceiver wifiScannerReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);

                if(success) {
                    scanSuccess();
                } else {
                    scanFailure();
                }
            }
        };

        btnZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BlacklistActivity.class);
                startActivity(intent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SaveActivity.class);
                sv.setList(adapter.getItems());
                startActivity(intent);
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
                getApplicationContext().registerReceiver(wifiScannerReceiver, intentFilter);

                boolean success = wifiManager.startScan();
                if(!success) {
                    scanFailure();
                }
            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<ScanResult> results = wifiManager.getScanResults();

                Log.v("Scan Result", "Success");

                adapter.clearItem();

                for(ScanResult result : results) {
                    adapter.addItem(result.SSID, result.BSSID, result.level + "");
                    adapter.notifyDataSetChanged();
                }

                sv.setStation(edtMyLoc.getText().toString());
                sv.setList(adapter.getItems());

                Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                startActivity(intent);
            }
        });

        btnAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AutoActivity.class);
                startActivity(intent);
            }
        });

        dataSetting();
    }

    private void scanSuccess() {
        List<ScanResult> results = wifiManager.getScanResults();

        Log.v("Scan Result", "Success");

        adapter.clearItem();

        for(ScanResult result : results) {
            Log.v("Scan Result", result.SSID);
            Log.v("Scan Result", result.BSSID);
            //Log.v("Scan Result", result.level + "");


            adapter.addItem(result.SSID, result.BSSID, result.level + "");
            adapter.notifyDataSetChanged();
        }
    }

    private void scanFailure() {
        List<ScanResult> results = wifiManager.getScanResults();
        Log.v("Scan Result", "Failure");
    }

    private void dataSetting() {
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                StaticVar sv = StaticVar.getInstance();
                SSIDItem item = adapter.getItem(position);
                sv.addBlackList(item);
                Toast.makeText(getApplicationContext(), item.name + " 블랙리스트 추가", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for(String p : permissions) {
            result = ContextCompat.checkSelfPermission(MainActivity.this, p);
            if(result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }

        if(!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(MainActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }

        return true;
    }
}
