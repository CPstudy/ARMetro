package com.guk2zzada.armetro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AutosaveActivity extends AppCompatActivity {

    StaticVar sv = StaticVar.getInstance();

    WifiManager wifiManager;
    private ArrayList<SSIDItem> arrayList = new ArrayList<>();
    int loop = sv.getLoop();
    int count = 0;
    boolean isFind = false;

    String resultLog = "";
    String sendString = "";

    RelativeLayout layPre;
    TextView txtLog;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autosave);

        layPre = findViewById(R.id.layPre);
        txtLog = findViewById(R.id.txtLog);
        scrollView = findViewById(R.id.scrollView);

        resultLog += "역: " + sv.getStation() + "\n";
        resultLog += "위치: " + sv.getLocation() + "\n";
        resultLog += "그룹: " + sv.getGroup() + "\n";
        resultLog += "메모: " + sv.getMemo() + "\n";
        resultLog += "반복: " + sv.getLoop() + "\n";
        resultLog += "========================================\n";

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        new SaveAsync().execute();

    }

    private void scanSuccess() {
        List<ScanResult> results = wifiManager.getScanResults();

        Log.v("Scan Result", "Success");
        resultLog += "[성공]\n";

        arrayList.clear();

        for(ScanResult result : results) {
            Log.v("Scan Result", result.SSID);
            Log.v("Scan Result", result.BSSID);
            //Log.v("Scan Result", result.level + "");

            SSIDItem si = new SSIDItem(result.SSID, result.BSSID, result.level + "");
            arrayList.add(si);

            resultLog += result.SSID + " | " + result.BSSID + " | " + result.level + "\n";
        }

        createContent();
        sendData();
    }

    private void scanFailure() {
        List<ScanResult> results = wifiManager.getScanResults();
        Log.v("Scan Result", "Failure");

        resultLog += "[실패]\n";
        resultLog += "----------------------------------------\n";
        resultLog += "반복 횟수: " + (count + 1) + "회\n";

        isFind = false;
    }

    private void createContent() {
        sendString = "";

        for(SSIDItem item : arrayList) {
            if(!sv.containsSSID(item.ssid)) {
                sendString += item.name + "||" + item.ssid + "||" + item.signal + "\n";
            }
        }

        sendString = sendString.substring(0, sendString.length() - 1);
        Log.v("Send String", sendString);
    }

    private void sendData() {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("station", sv.getStation())
                .add("group_id", sv.getGroup())
                .add("location", sv.getLocation())
                .add("memo", sv.getMemo())
                .add("list", sendString)
                .build();

        Request request = new Request.Builder()
                .url("https://guide94.cafe24.com/ssid/insert-ssid.php")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(insertCallback);
    }

    private Callback insertCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("TEST", "ERROR Message : " + e.getMessage());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    resultLog += "[실패]\n";
                    resultLog += "----------------------------------------\n";
                    resultLog += "반복 횟수: " + (count + 1) + "회\n";
                    isFind = false;
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            final String responseData = response.body().string();
            Log.d("TEST", "responseDatae : " + responseData);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    count++;

                    if(count < loop) {
                        resultLog += "----------------------------------------\n";
                        resultLog += "반복 횟수: " + (count + 1) + "회\n";
                    } else if(count == loop) {
                        resultLog += "----------------------------------------\n";
                        resultLog += "종료";
                    }
                    isFind = false;
                }
            });
        }
    };

    class SaveAsync extends AsyncTask<String, String, String> {


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

        @Override
        protected void onPreExecute() {
            resultLog += "반복 횟수: " + (count + 1) + "회\n";
            txtLog.setText(resultLog);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            txtLog.setText(resultLog);
            scrollView.fullScroll(View.FOCUS_DOWN);
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            while(count < loop) {
                if(!isFind) {
                    isFind = true;
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
                    getApplicationContext().registerReceiver(wifiScannerReceiver, intentFilter);

                    boolean success = wifiManager.startScan();
                    publishProgress();
                    if (!success) {
                        scanFailure();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            layPre.setVisibility(View.GONE);
            txtLog.setText(resultLog);
            scrollView.fullScroll(View.FOCUS_DOWN);
            super.onPostExecute(s);
        }

    }
}
