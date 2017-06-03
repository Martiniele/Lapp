package com.lib.lapp.net.utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.lib.lapp.model.WifiInfo;

/**
 * @author wxx
 * @Date 2017/04/12 9:30
 * @Description 网络请求相关的方法封装
 */
public class AsyncNetUtils {
    public static final String TAG = "AsyncNetUtils";
    private OkHttpClient client;
    private Handler handler;
    //提交字符串数据
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");
    //提交JSON数据
    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");

    private volatile static AsyncNetUtils asyncNetUtils = null; // 确保线程安全

    private AsyncNetUtils() {
        client = new OkHttpClient();
        handler = new Handler(Looper.getMainLooper());
    }

    /**
     * 单例模式
     *
     * @return
     */
    public static AsyncNetUtils getInstance() {
        if (asyncNetUtils == null) {
            synchronized (AsyncNetUtils.class) {
                if (asyncNetUtils == null) {
                    asyncNetUtils = new AsyncNetUtils();
                }
            }
        }
        return asyncNetUtils;
    }

    /**
     * 请求返回的是JSON字符串
     *
     * @param jsonValue
     * @param callBack
     */
    private void onSuccessJsonStringMethod(final String jsonValue, final StringCallBack callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.onResponse(jsonValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 请求结果返回相应的JSON对象
     *
     * @param jsonValue
     * @param callBack
     */
    private void onSuccessJsonObjectMethod(final String jsonValue,
                                           final JsonCallBack callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.onResponse(new JSONObject(jsonValue));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 返回响应的对象是一个字节数组
     *
     * @param data
     * @param callBack
     */
    private void onSuccessByteMethod(final byte[] data,
                                     final ByteCallBack callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.onResponse(data);
                    } catch (Exception e) {

                    }
                }
            }
        });
    }

    /**
     * 图片请求
     *
     * @param bitmap
     * @param callBack
     */
    private void onSuccessImgMethod(final Bitmap bitmap,
                                    final BitmapCallBack callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.onResponse(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /***
     *
     * @author WXX
     *
     * 2017 2017-3-17
     *
     * OKHTTP 请求操作
     */

    /**
     * 异步请求，请求返回Json字符串
     *
     * @param url
     * @param callback
     */
    public void asyncJsonStringByURL(String url, final StringCallBack callback) {
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonStringMethod(response.body().string(), callback);
                }
            }
        });
    }

    /**
     * 异步请求，请求返回JSON对象
     *
     * @param url
     * @param callback
     */
    public void asyncJsonObjectByURL(final String url, final JsonCallBack callback) {
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonObjectMethod(response.body().string(), callback);
                }
            }
        });
    }


    /**
     * 异步请求，请求返回Byte 字节数组
     *
     * @param url
     * @param callback
     */
    public void asyncGetByteByURL(final String url, final ByteCallBack callback) {
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessByteMethod(response.body().bytes(), callback);
                }
            }
        });
    }

    /**
     * 异步请求，返回图片
     *
     * @param url
     * @param callback
     */
    public void asyncDownloadImgByURL(String url, final BitmapCallBack callback) {
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    byte[] data = response.body().bytes();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    onSuccessImgMethod(bitmap, callback);
                    //防止溢出异常
                    System.out.println(data.length);
                }
            }
        });
    }

    /**
     * 向服务器提交String请求
     *
     * @param url
     * @param content
     * @param callback
     */
    public void sendStringByPost(String url, String content, final JsonCallBack callback) {
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_MARKDOWN, content);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonObjectMethod(response.body().string(), callback);
                }
            }
        });
    }

    /**
     * 向服务器提交json字符串格式的数据
     *
     * @param url
     * @param json
     * @param callback
     */
    public void sendJsonStringByPost(String url, String json, final StringCallBack callback) {
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onSuccessJsonStringMethod(response.body().string(), callback);
            }

            @Override
            public void onFailure(Call call, IOException exp) {
                exp.printStackTrace();
            }
        });
    }

    /**
     * 提交表单
     *
     * @param url
     * @param wifiInfo
     * @param callback
     */
    public void sendFormByPost(String url, WifiInfo wifiInfo, final StringCallBack callback) {
        RequestBody requestBody = new FormBody.Builder()
                .add("x", String.valueOf(wifiInfo.getPos_x()))
                .add("y", String.valueOf(wifiInfo.getPos_y()))
                .add("bssid", wifiInfo.getWifi_bssid())
                .add("ssid", wifiInfo.getWifi_ssid())
                .add("rssi", String.valueOf(wifiInfo.getWifi_rssi()))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("content-type", String.valueOf(JSON))
                .addHeader("Home", "China")
                .addHeader("user-agent", "Android")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onSuccessJsonStringMethod(response.body().string(), callback);
            }
        });
    }

    //网络访问字符串或者Json字符串回调接口
    public interface StringCallBack {
        void onResponse(String response);
    }

    //网络访问字符数组回调接口
    public interface ByteCallBack {
        void onResponse(byte[] result);
    }

    //网络访问图片回调接口
    public interface BitmapCallBack {
        void onResponse(Bitmap bitmap);
    }

    //网络访问 Json对象回调接口
    public interface JsonCallBack {
        void onResponse(JSONObject jsonObject);
    }
}
