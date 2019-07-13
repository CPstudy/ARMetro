package com.guk2zzada.armetro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class BlacklistActivity extends AppCompatActivity {

    ListView list;

    ArrayList<SSIDItem> arrayList = new ArrayList<>();
    BlacklistAdapter adapter = new BlacklistAdapter();
    StaticVar sv = StaticVar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklist);

        list = findViewById(R.id.list);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sv.removeBlackList(i);
                adapter.notifyDataSetChanged();
            }
        });

    }
}
