package com.guk2zzada.armetro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AutoActivity extends AppCompatActivity {

    EditText edtStation;
    EditText edtGroup;
    EditText edtLocation;
    EditText edtMemo;
    EditText edtLoop;
    Button btnStart;

    StaticVar sv = StaticVar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto);

        edtStation = findViewById(R.id.edtStation);
        edtGroup = findViewById(R.id.edtGroup);
        edtLocation = findViewById(R.id.edtLocation);
        edtMemo = findViewById(R.id.edtMemo);
        edtLoop = findViewById(R.id.edtLoop);
        btnStart = findViewById(R.id.btnStart);

        edtLoop.setText("0");

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sv.setStation(edtStation.getText().toString());
                sv.setGroup(edtGroup.getText().toString());
                sv.setLocation(edtLocation.getText().toString());
                sv.setMemo(edtMemo.getText().toString());
                sv.setLoop(Integer.parseInt(edtLoop.getText().toString()));
                Intent intent = new Intent(AutoActivity.this, AutosaveActivity.class);
                startActivity(intent);
            }
        });
    }
}
