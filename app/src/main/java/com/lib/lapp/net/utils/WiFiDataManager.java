package com.lib.lapp.net.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.lib.lapp.location.Klocation;
import com.lib.lapp.location.TransUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author wxx
 * @Date 2017/04/12
 * @Description WIFI 信号扫描
 */

public class WiFiDataManager {

    public static final long WIFI_SCAN_DELAY = 1500;
    private WifiManager wifiManager;  //WIFI管理器，用于获取和管理WIFI信号的采集
    private TransUtils transUtils = null;
    public List<ScanResult> scanResults = null; //存放过滤后扫描到的WIFI信号
    public List<ScanResult> scantemps = null; //存放原始扫描到的所有WIFI信号
    private Timer wifiScanTimer;    //扫描定时器
    private TimerTask wifiScanTimerTask; //定时器任务
    private int[] rssiArray;   //存放对应AP的RSSI信号强度值
    public  boolean isNormal = true;

    public int[] getRssiArray(){
        return this.rssiArray;
    }
    private volatile static WiFiDataManager wiFiDataManager = null;

    public static WiFiDataManager getInstance() {
        if (wiFiDataManager == null) {
            synchronized (WiFiDataManager.class) {
                if (wiFiDataManager == null) {
                    wiFiDataManager = new WiFiDataManager();
                }
            }
        }
        return wiFiDataManager;
    }

    private WiFiDataManager() {
        transUtils = new TransUtils();
    }

    // 初始化WIFI，开启
    public void initWifi(Context context) {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        Toast.makeText(context, "正在开启WiFi...", Toast.LENGTH_SHORT).show();
        wifiManager.setWifiEnabled(true);
        while (wifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLED) {
            Toast.makeText(context, "正在开启WiFi...", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(context, "WiFi已开启", Toast.LENGTH_SHORT).show();
    }

    // 设置Timer任务，开始Wifi扫描
    public void startScanWifi(Context context) {
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
            scantemps = wifiManager.getScanResults();
            scanResults = new ArrayList<ScanResult>();
            for (ScanResult result : scantemps) {
                if (result.SSID.equals("Library")) {
                    scanResults.add(result);
                } else {
                    continue;
                }
            }
            rssiArray = transUtils.scanResults2vector(scanResults, ApConfig.getRadioMapData());
            new Klocation().start();
        }
    };

    public void endCollecting(Context context) {
        context.unregisterReceiver(wifiReceiver); // 取消监听
    }
}
