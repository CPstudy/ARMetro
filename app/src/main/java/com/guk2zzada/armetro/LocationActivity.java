package com.guk2zzada.armetro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LocationActivity extends AppCompatActivity {

    TextView txtResult;
    HashMap<String, SSIDResult> map;
    HashMap<String, Integer> resultMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        txtResult = findViewById(R.id.txtResult);

        sendData();
    }

    private void sendData() {
        StaticVar sv = StaticVar.getInstance();
        String result = "";

        for(SSIDItem item : sv.getList()) {
            result += item.name + "||" + item.ssid + "||" + item.signal + "\n";
        }

        result = result.substring(0, result.length() - 1);
        Log.v("Result String", result);

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("station", sv.getStation())
                .add("list", result)
                .build();

        Request request = new Request.Builder()
                .url("https://guide94.cafe24.com/ssid/find-location.php")
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
                    Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    map = new HashMap<>();

                    String[] strings = responseData.split("\n");

                    try {
                        for (String s : strings) {
                            String[] ss = s.split("\t");
                            String ssid = ss[0];
                            String location = ss[1];
                            int distance = Integer.parseInt(ss[2]);

                            if (!map.containsKey(ssid)) {
                                SSIDResult sr = new SSIDResult();
                                sr.setSsid(ssid);
                                sr.setLocation(location);
                                sr.setDistance(distance);
                                map.put(ssid, sr);
                            } else {
                                SSIDResult sr = map.get(ssid);
                                sr.setLocation(location);
                                sr.setDistance(sr.getDistance() + distance);
                                map.put(ssid, sr);
                            }
                        }

                        String testResult = "";
                        resultMap = new HashMap<>();

                        for (String key : map.keySet()) {
                            testResult += key + "\t" + map.get(key).getLocation() + "\n";

                            int count = map.get(key).getCount();
                            String loc = map.get(key).getLocation();

                            if (resultMap.containsKey(loc)) {
                                resultMap.put(loc, resultMap.get(loc) + count);
                            } else {
                                resultMap.put(loc, count);
                            }
                        }

                        Map.Entry<String, Integer> maxEntry = null;

                        for (Map.Entry<String, Integer> entry : resultMap.entrySet()) {
                            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                                maxEntry = entry;
                            }
                        }

                        testResult += "\n\n내 위치: " + maxEntry.getKey();

                        txtResult.setText(testResult);
                    } catch (Exception e) {
                        txtResult.setText(e.getMessage());
                    }
                }
            });
        }
    };
}
