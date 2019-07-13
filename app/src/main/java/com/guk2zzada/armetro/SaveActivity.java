package com.guk2zzada.armetro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SaveActivity extends AppCompatActivity {

    EditText edtStation;
    EditText edtGroup;
    EditText edtContent;
    EditText edtLocation;
    EditText edtMemo;
    Button btnSend;

    String jsonString = "";

    StaticVar sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        edtStation = findViewById(R.id.edtStation);
        edtGroup = findViewById(R.id.edtGroup);
        edtContent = findViewById(R.id.edtContent);
        edtLocation = findViewById(R.id.edtLocation);
        edtMemo = findViewById(R.id.edtMemo);
        btnSend = findViewById(R.id.btnSend);

        sv = StaticVar.getInstance();

        edtStation.setText(sv.getStation());
        edtGroup.setText(sv.getGroup());
        edtLocation.setText(sv.getLocation());

        String content = "";

        for(SSIDItem item : sv.getList()) {
            if(!sv.containsSSID(item.ssid)) {
                content += item.name + " | " + item.ssid + " | " + item.signal + "\n";
            }
        }

        edtContent.setText(content.substring(0, content.length() - 1));

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sv.setStation(edtStation.getText().toString());
                sv.setGroup(edtGroup.getText().toString());
                sv.setLocation(edtLocation.getText().toString());
                createContent();
                sendData();
            }
        });
    }

    private void createContent() {

        for(SSIDItem item : sv.getList()) {
            if(!sv.containsSSID(item.ssid)) {
                jsonString += item.name + "||" + item.ssid + "||" + item.signal + "\n";
            }
        }

        jsonString = jsonString.substring(0, jsonString.length() - 1);
    }

    private void sendData() {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("station", sv.getStation())
                .add("group_id", sv.getGroup())
                .add("location", sv.getLocation())
                .add("memo", edtMemo.getText().toString())
                .add("list", jsonString)
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
                }
            });
            finish();
        }
    };
}
