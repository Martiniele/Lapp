package com.lib.lapp.net.utils;

import java.util.HashMap;

/**
 * @author wxx
 * @Date 2017/5/31 14:34
 * @Description
 */

public class ApConfig {

    private static HashMap<String, Integer> radiobssidMap = null;

    public static HashMap<String, Integer> getRadioMapData() {
        radiobssidMap = new HashMap<String, Integer>();
        radiobssidMap.put("e6:14:4b:14:94:2f", 1);
        radiobssidMap.put("66:14:4b:55:b9:27", 2);
        radiobssidMap.put("a6:14:4b:14:94:db", 3);
        radiobssidMap.put("66:14:4b:55:ac:e7", 4);
        radiobssidMap.put("26:14:4b:55:8c:23", 5);
        radiobssidMap.put("a6:14:4b:14:92:fb", 6);
        radiobssidMap.put("66:14:4b:55:b3:97", 7);
        radiobssidMap.put("a6:14:4b:55:88:cb", 8);
        radiobssidMap.put("e6:14:4b:55:b6:ef", 9);
        radiobssidMap.put("66:14:4b:14:92:e7", 10);
        radiobssidMap.put("66:14:4b:55:b5:07", 11);
        radiobssidMap.put("26:14:4b:55:ac:d3", 12);
        radiobssidMap.put("a6:14:4b:14:92:db", 13);
        radiobssidMap.put("86:14:4b:54:80:59", 14);
        radiobssidMap.put("26:14:4b:55:ac:c3", 15);
        radiobssidMap.put("66:14:4b:14:93:e7", 16);
        radiobssidMap.put("66:14:4b:14:94:27", 17);
        radiobssidMap.put("e6:14:4b:55:b9:cf", 18);
        radiobssidMap.put("66:14:4b:55:b4:27", 19);
        radiobssidMap.put("e6:14:4b:55:ad:0f", 20);
        radiobssidMap.put("66:14:4b:14:94:37", 21);
        radiobssidMap.put("66:14:4b:14:94:77", 22);
        radiobssidMap.put("26:14:4b:14:92:83", 23);
        radiobssidMap.put("a6:14:4b:14:94:2b", 24);
        radiobssidMap.put("26:14:4b:55:ac:63", 25);
        radiobssidMap.put("66:14:4b:55:ba:47", 26);
        return radiobssidMap;
    }
}
