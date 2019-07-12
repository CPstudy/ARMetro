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
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    ListView list;
    Button btnScan;

    ArrayList<SSIDItem> arrayList;
    WifiManager wifiManager;
    List<ScanResult> scanResults;
    SSIDAdapter adapter = new SSIDAdapter();

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.list);
        btnScan = findViewById(R.id.btnScan);

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

        for(int i = 0; i < 5; i++) {
            adapter.addItem("Name" + i, "SSID" + i, "Signal" + i);
        }

        list.setAdapter(adapter);
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
