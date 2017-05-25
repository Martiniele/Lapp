package com.lib.lapp.model;

/**
 * Created by wxx on 2017/3/30.
 */

public class WifiInfo {
    private float pos_x;
    private float pos_y;
    private int wifi_rssi;
    private String wifi_ssid;
    private String wifi_bssid;
    private float wifi_distance;

    public float getPos_x() {
        return pos_x;
    }

    public void setPos_x(float pos_x) {
        this.pos_x = pos_x;
    }

    public float getPos_y() {
        return pos_y;
    }

    public void setPos_y(float pos_y) {
        this.pos_y = pos_y;
    }

    public int getWifi_rssi() {
        return wifi_rssi;
    }

    public void setWifi_rssi(int wifi_rssi) {
        this.wifi_rssi = wifi_rssi;
    }

    public String getWifi_ssid() {
        return wifi_ssid;
    }

    public void setWifi_ssid(String wifi_ssid) {
        this.wifi_ssid = wifi_ssid;
    }

    public String getWifi_bssid() {
        return wifi_bssid;
    }

    public void setWifi_bssid(String wifi_bssid) {
        this.wifi_bssid = wifi_bssid;
    }

    public float getWifi_distance() {
        return wifi_distance;
    }

    public void setWifi_distance(float wifi_distance) {
        this.wifi_distance = wifi_distance;
    }
}
