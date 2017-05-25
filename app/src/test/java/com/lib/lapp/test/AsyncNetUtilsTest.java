package com.lib.lapp.test;

import com.lib.lapp.model.WifiInfo;
import com.lib.lapp.net.utils.AsyncNetUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

/**
 * Created by wxx on 2017/3/31.
 */
@RunWith(MockitoJUnitRunner.class)
public class AsyncNetUtilsTest {

    @Test
    public void asyncJsonStringByURL() throws Exception {

    }

    @Test
    public void asyncJsonObjectByURL() throws Exception {

    }

    @Test
    public void asyncGetByteByURL() throws Exception {

    }

    @Test
    public void sendStringByPost() throws Exception {

    }

    @Test
    public void sendJsonStringByPost() throws Exception {

    }

    @Test
    public void sendFormByPost() throws  Exception{
        String url = "http://127.0.0.1:8080/LibServer/rssiaction.action";
        WifiInfo info = new WifiInfo();
        info.setPos_x(1278878);
        info.setPos_y(288554);
        info.setWifi_ssid("wxxxxx");
        info.setWifi_bssid("as:21:e3:88:3j:e0");
        info.setWifi_rssi(-54);
        AsyncNetUtils.getInstance().sendFormByPost(url,info, new AsyncNetUtils.StringCallBack() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                assertEquals("111","111");
            }
        });
    }
}