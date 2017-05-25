package com.lib.lapp.view.utils;

/**
 * Created by WXX on 2017/3/22.
 * 用于展示地图的控件
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.fengmap.android.map.FMMapView;

public class MapViewPagerFixed extends FMMapView {

    public MapViewPagerFixed(Context context) {
        super(context);
    }

    public MapViewPagerFixed(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
