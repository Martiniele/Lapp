package com.lib.lapp.views.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lib.lapp.R;
import com.lib.lapp.model.WifiInfo;
import com.lib.lapp.net.utils.AsyncNetUtils;
import com.lib.lapp.net.utils.ConfigUrl;
import com.lib.lapp.views.activity.LocationService;


import java.util.Map;

import me.leefeng.promptlibrary.PromptDialog;


public class PersonFragment extends BaseFragment {
    public static final int NET_MSG = 1;
    private Button net_btn;
    private Toast toast;
    private TextView txt_content;
    private PromptDialog promptDialog;

    //创建对象
    private MyFragmentReceiver receiver = null;
    //本地广播管理器
    private LocalBroadcastManager fLocalBroadcastManager;
    public PersonFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_persondata_content, container, false);
        net_btn = (Button) view.findViewById(R.id.net_btn);
        txt_content = (TextView) view.findViewById(R.id.txt_service);
        promptDialog = new PromptDialog(getActivity());

        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        fLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
        //注册广播接收器
        receiver = new MyFragmentReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.location.service.WARNNING");
        fLocalBroadcastManager.registerReceiver(receiver, intentFilter);
        super.onAttach(context);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NET_MSG:
                    Bundle data = msg.getData();
                    String val = data.getString("value");
                    Gson gson = new Gson();
                    Map<String, String> map = gson.fromJson(val, Map.class);
                    String result = null;
                    result = map.get("message");
                    toast = Toast.makeText(getContext(), result, 1000);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                    break;
            }
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String url = ConfigUrl.RSSIACTION_URL;
            WifiInfo info = new WifiInfo();
            info.setPos_x(1278878);
            info.setPos_y(288554);
            info.setWifi_ssid("Library");
            info.setWifi_bssid("as:21:e3:88:3j:e0");
            info.setWifi_rssi(-54);
            AsyncNetUtils.getInstance().sendFormByPost(url, info, new AsyncNetUtils.StringCallBack() {
                @Override
                public void onResponse(String response) {
                    Message msg = new Message();
                    msg.what = NET_MSG;
                    Bundle data = new Bundle();
                    data.putString("value", response);
                    msg.setData(data);
                    handler.sendMessage(msg);
                }
            });
        }
    };

    @Override
    public void fetchData() {
        net_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(runnable).start();
            }
        });
    }

    @Override
    public void updateLocateGroupView() {

    }

    @Override
    public void onDestroyView() {
        fLocalBroadcastManager.unregisterReceiver(receiver);
        super.onDestroyView();
    }

    /**
     * 获取广播数据
     *
     * @author wxx
     */
    public class MyFragmentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "com.location.service.WARNNING":
                    Bundle wBundle = intent.getExtras();
                    String result = wBundle.getString("warnning");
                    txt_content.setText(result);
                    break;
            }
        }
    }
}
