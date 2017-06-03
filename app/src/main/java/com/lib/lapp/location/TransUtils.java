package com.lib.lapp.location;

import android.net.wifi.ScanResult;
import android.util.Log;

import com.lib.lapp.net.utils.WiFiDataManager;

import java.util.HashMap;
import java.util.List;

/**
 * @author wxx
 * @Date 2017/5/31 14:19
 * @Description 工具类 将扫描的信号封装成特征向量格式
 */
public class TransUtils {
    /**
     * 定位时最少扫描到WIFI信号才能定位
     */
    public int WIFI_NUM_MIN = 3;

    /**
     * 用于实时定位时特征向量的规范化
     *
     * @param scanResults 初始扫描结果
     * @param bssids      指纹库中存在的AP集合
     * @return
     */
    public synchronized int[] scanResults2vector(List<ScanResult> scanResults, HashMap<String, Integer> bssids) {
        // 生成rssScan数组 按指纹数据库中的
        int rssScan[] = new int[100];
        int bsscount = 0;  //统计实时扫描到的AP数量
        if (scanResults != null) {
            // 初始化
            for (int i = 0; i < rssScan.length; i++) {
                rssScan[i] = 0;
            }
            for (int j = 0; j < scanResults.size(); j++) {
                String bssid = scanResults.get(j).BSSID;
                if (bssids.containsKey(bssid)) {
                    int idx = bssids.get(bssid);        //保证指纹与指纹数据库的特征向量格式一致
                    rssScan[idx] = scanResults.get(j).level;
                    bsscount++;
                }
            }
        }

        if (bsscount < 3) {
            WiFiDataManager.getInstance().isNormal = false;
        } else {
            WiFiDataManager.getInstance().isNormal = true;
        }
        return rssScan;
    }
}
