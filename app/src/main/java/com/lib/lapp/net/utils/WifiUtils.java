package com.lib.lapp.net.utils;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wxx
 * @Date 2017/03/13
 * @Description 用于扫描WIFI信号
 */

public class WifiUtils {
    private WifiManager wifiManager;
    private List<ScanResult> scanResults = null;
    private static HashMap<String, Object> map = null;
    private static List<HashMap<String, Object>> list = null;

    public WifiUtils(Context context) {
        this.wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * 判断WIFI 是否启动
     *
     * @param context 传入当前的内容
     */
    public void initWifi(Context context) {
        if (wifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLED) {
            Toast.makeText(context, "正在开启wifi，请稍后...", Toast.LENGTH_SHORT).show();
            if (wifiManager == null) {
                wifiManager = (WifiManager) context
                        .getSystemService(Context.WIFI_SERVICE);
            }
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
        }
    }

    /**
     * 获取扫描到的图书馆WIFI数据信息，用于后续的定位及信号采集处理
     *
     * @return List<HashMap<String,Object>>存储扫描到的所有信号数据的封装
     */
    public List<HashMap<String, Object>> getScanResults() {
        scanResults = wifiManager.getScanResults();
        List<String> w_ssid = new LinkedList<String>();
        List<String> w_bssid = new LinkedList<String>();
        List<Integer> w_rssi = new LinkedList<Integer>();
        List<Float> w_dis = new LinkedList<Float>();
        for (ScanResult result : scanResults) {
            if (result.SSID.equals("library")) {
                int rrssi = Math.abs(result.level);
                float rpower = (float) ((rrssi - 43) / (10 * 2.5)); //计算WIFI热点到达采集点的距离
                float rd = (float) Math.pow(10, rpower);
                w_ssid.add(result.SSID);
                w_bssid.add(result.BSSID);
                w_rssi.add(result.level);
                w_dis.add(rd);
            }
        }
        for (int i = 0; i < w_ssid.size(); i++) {
            map = new HashMap<String, Object>();
            map.put("SSID", w_ssid.get(i));
            map.put("BSSID", w_bssid.get(i));
            map.put("RSSI", String.valueOf(w_rssi.get(i)));
            map.put("DIS", String.valueOf(w_dis.get(i)));
            list.add(map);
        }
        return list;
    }
}
