package com.lib.lapp.net.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wxx on 2017/4/30.
 */

public class WiFiDataManager {
    public static final long WIFI_SCAN_DELAY = 1000;
    private Context context;
    private WifiManager wifiManager;
    public List<ScanResult> scanResults = null;
    public List<ScanResult> scantmps = null;
    private Timer wifiScanTimer;
    private TimerTask wifiScanTimerTask;
    public float rssScan[];
    public boolean isNormal = true;
    public ArrayList<HashMap<Integer, Integer>> dataRssi = new ArrayList<HashMap<Integer, Integer>>(); // 每行代表一个Wifi热点，对应一个map，map的第一个值是数据的index，第二个值是rssi
    public HashMap<String, Integer> dataBssid = new HashMap<String, Integer>();
    public ArrayList<String> dataWifiNames = new ArrayList<String>();
    public int dataCount = 0;

    private volatile static WiFiDataManager wiFiDataManager = null;

    public static WiFiDataManager getInstance(Context ctx) {
        if (wiFiDataManager == null) {
            synchronized (WiFiDataManager.class) {
                if (wiFiDataManager == null) {
                    wiFiDataManager = new WiFiDataManager(ctx);
                }
            }
        }
        return wiFiDataManager;
    }

    private WiFiDataManager(Context context) {
        this.context = context;
    }

    // 初始化WIFI，开启
    public void initWifi() {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        Toast.makeText(context, "正在开启WiFi...", Toast.LENGTH_SHORT).show();
        wifiManager.setWifiEnabled(true);
        while (wifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLED) {
            // TipsTextView.setText("正在开启WiFi，请稍候");
        }
        // TipsTextView.setText("WiFi已开启");
    }

    // 设置Timer任务，开始Wifi扫描
    public void startScanWifi() {
        context.registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiScanTimer = new Timer();
        wifiScanTimerTask = new TimerTask() {
            public void run() {
                wifiManager.startScan();
            }
        };
        wifiScanTimer.schedule(wifiScanTimerTask, 0, WIFI_SCAN_DELAY);
    }

    private final BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            scantmps = wifiManager.getScanResults();
            scanResults = new ArrayList<ScanResult>();
            for (ScanResult result : scantmps) {
                if (result.SSID.equals("Library")) {
                    scanResults.add(result);
                } else {
                    continue;
                }
            }
            for (int i = 0; i < scanResults.size(); i++) {
                if (!dataBssid.containsKey(scanResults.get(i).BSSID)) { // 新增一个wifi热点
                    dataBssid.put(scanResults.get(i).BSSID, dataBssid.size());
                    dataWifiNames.add(scanResults.get(i).SSID);
                    HashMap<Integer, Integer> tmp = new HashMap<Integer, Integer>();
                    tmp.put(dataCount, scanResults.get(i).level);
                    dataRssi.add(tmp);
                } else { // wifi热点已存在
                    dataRssi.get(dataBssid.get(scanResults.get(i).BSSID)).put(
                            dataCount, scanResults.get(i).level);
                }
            }
            dataCount++;
        }
    };
}
