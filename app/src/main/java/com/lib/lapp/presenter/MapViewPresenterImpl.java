package com.lib.lapp.presenter;

import com.lib.lapp.contract.IMapViewFragment;
import com.lib.lapp.model.WifiInfo;

/**
 * @author wxx
 * @Date 2017/03/30
 * @Description 地图页面的相关逻辑操作
 */

public class MapViewPresenterImpl implements IMapViewPresenter {
    private IMapViewFragment iMapViewFragment;
    private WifiInfo wifiInfo;

    public MapViewPresenterImpl(IMapViewFragment iMapviewfragment) {
        this.iMapViewFragment = iMapviewfragment;
    }
}
