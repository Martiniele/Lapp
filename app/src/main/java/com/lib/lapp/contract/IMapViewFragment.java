package com.lib.lapp.contract;

import android.view.View;

/**
 * Created by wxx on 2017/3/30.
 */

public interface IMapViewFragment {
    public void openMapByPath(View view);
    public void openMapById(View view);
    public void initZoomComponent();
    public void init3DControllerComponent();
}
