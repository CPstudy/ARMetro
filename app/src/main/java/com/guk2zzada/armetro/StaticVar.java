package com.guk2zzada.armetro;

import java.util.ArrayList;

public class StaticVar {
    private static StaticVar instance = null;

    private ArrayList<SSIDItem> blackList;
    private ArrayList<SSIDItem> list;
    private String station = "";
    private String group = "";
    private String location = "";
    private String memo = "";

    private int loop = 0;

    private StaticVar() {

    }

    public static StaticVar getInstance() {
        if(instance == null) {
            instance = new StaticVar();
        }

        return instance;
    }

    public ArrayList<SSIDItem> getBlackList() {

        if(blackList == null) {
            blackList = new ArrayList<>();
        }

        return blackList;
    }

    public boolean containsSSID(String ssid) {
        if(blackList == null) {
            blackList = new ArrayList<>();
            return false;
        }

        for(SSIDItem item : blackList) {
            if(item.ssid.equals(ssid)) {
                return true;
            }
        }

        return false;
    }

    public void addBlackList(SSIDItem item) {
        if(blackList == null) {
            blackList = new ArrayList<>();
        }
        blackList.add(item);
    }

    public void removeBlackList(int position) {
        blackList.remove(position);
    }

    public ArrayList<SSIDItem> getList() {
        return list;
    }

    public void setList(ArrayList<SSIDItem> list) {
        this.list = list;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getLoop() {
        return loop;
    }

    public void setLoop(int loop) {
        this.loop = loop;
    }
}
