package com.guk2zzada.armetro;

import java.util.HashMap;
import java.util.Map;

public class SSIDResult {

    private String ssid;
    private HashMap<String, Integer> map;
    private String location = "";
    private int count = 0;
    private int distance;

    public SSIDResult() {
        map = new HashMap<>();
        distance = 0;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getCalcLocation() {
        Map.Entry<String, Integer> maxEntry = null;

        for(Map.Entry<String, Integer> entry : map.entrySet()) {
            if(maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }

        count = maxEntry.getValue();

        return maxEntry.getKey();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        if(map.containsKey(location)) {
            int l = map.get(location);
            map.put(location, l + 1);
        } else {
            map.put(location, 1);
        }

        this.location = getCalcLocation();
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getCount() {
        return count;
    }
}
