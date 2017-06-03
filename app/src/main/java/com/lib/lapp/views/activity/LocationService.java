package com.lib.lapp.views.activity;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;
import com.lib.lapp.net.utils.WiFiDataManager;

/**
 * @author wxx
 * @Date 2017/5/30 23:07
 * @Description
 */

public class LocationService extends Service {
    public static LocationService locationService;
    private int count = 0;
    private boolean threadDisable = false;
    //本地广播管理器
    private LocalBroadcastManager mLocalBroadcastManager;
    //广播接收器
    private BroadcastReceiver myServiceReceiver;

    @Override
    public void onCreate() {
        initializeService();
        locationService = this;
        WiFiDataManager.getInstance().initWifi(this);
        WiFiDataManager.getInstance().startScanWifi(this);
        super.onCreate();
    }


    /**
     * 扫描信号不满足指纹库的热点数量要求时发送提醒
     *
     * @param winfo
     */
    private void sendScanResult(String winfo) {
        Intent warnningIntent = new Intent("com.location.service.WARNNING");
        warnningIntent.putExtra("warnning", winfo);
        mLocalBroadcastManager.sendBroadcast(warnningIntent);
    }

    private void initializeService() {
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        myServiceReceiver = new myServiceReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.location.service.WARNNING");
        mLocalBroadcastManager.registerReceiver(myServiceReceiver, intentFilter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void uiUpdate(){
        String scanWarnning = "";
        if (WiFiDataManager.getInstance().isNormal) {
            scanWarnning = "WIFI热点个数符合定位条件";
        } else {
            scanWarnning = "WIFI热点个数不足,无法定位";
        }
        sendScanResult(scanWarnning);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        count = 0;
        threadDisable = true;
        mLocalBroadcastManager.unregisterReceiver(myServiceReceiver);
        WiFiDataManager.getInstance().endCollecting(this);
    }

    class myServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
