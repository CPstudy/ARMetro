package com.guk2zzada.armetro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class ZoomActivity extends AppCompatActivity {

    RelativeLayout layout;

    RelativeLayout.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);

        layout = findViewById(R.id.layout);

        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(Units.dp(736), Units.dp(213), 0, 0);

        Log.e("Units", "dp = " + Units.dp(70));

        Button button = new Button(this);
        button.setText("apple");
        button.setLayoutParams(params);
        button.setBackgroundResource(0);
        button.setBackgroundColor(0xffff0000);
        layout.addView(button);

    }
}
