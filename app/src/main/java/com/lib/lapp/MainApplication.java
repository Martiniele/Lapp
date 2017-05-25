package com.lib.lapp;

import android.app.Application;
import com.fengmap.android.FMMapSDK;

/**
 * Created by WXX on 2017/3/10.
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FMMapSDK.init(this);
    }
}
