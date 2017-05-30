package com.lib.lapp.views.activity;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * @author wxx
 * @Date 2017/5/30 23:07
 * @Description
 */

public class LocationService extends Service {
    private int count = 0;
    private boolean threadDisable = false;

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!threadDisable) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count++;
                    Log.v("LocationService", "Count is " + count);

                    //发送广播
                    Intent intent = new Intent();
                    intent.putExtra("count", count);
                    intent.setAction("com.lib.lapp.views.activity.LocationService");
                    sendBroadcast(intent);
                }
            }
        }).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        count = 0;
        threadDisable = true;
        Log.v("CountService", "on destroy");
    }
}
