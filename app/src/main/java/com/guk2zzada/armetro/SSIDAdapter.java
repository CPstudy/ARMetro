package com.guk2zzada.armetro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SSIDAdapter extends BaseAdapter {

    public static final String SEPERATOR = "||";
    public static final String LINE = "\n";

    private ArrayList<SSIDItem> arrayList = new ArrayList<>();

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public SSIDItem getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_ssid, parent, false);
        }

        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtSSID = convertView.findViewById(R.id.txtSSID);
        TextView txtSignal = convertView.findViewById(R.id.txtSignal);

        SSIDItem si = getItem(position);

        txtName.setText(si.name);
        txtSSID.setText(si.ssid);
        txtSignal.setText(si.signal);

        return convertView;
    }

    public void clearItem() {
        arrayList.clear();
    }

    public void addItem(String name, String ssid, String signal) {
        SSIDItem si = new SSIDItem(name, ssid, signal);
        arrayList.add(si);
    }

    public ArrayList<SSIDItem> getItems() {
        return arrayList;
    }
}
