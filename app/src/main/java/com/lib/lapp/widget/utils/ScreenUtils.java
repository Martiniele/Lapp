package com.lib.lapp.widget.utils;

import android.content.Context;
import android.view.View;

/**
 * Created by wxx on 2017/4/24.
 */

public class ScreenUtils {
    /**
     * 获取屏幕的高度(px)
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context){
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕的宽度(px)
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示, x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己加入额外的偏移来修正
     * @param anchorView    呼出window的View
     * @param contentView   window的内容布局
     * @return    window显示左上角的xoff,yoff坐标
     */
    private static int[] caculatePopWindowPos(final View anchorView, final View contentView){
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];

        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();

        //获取屏幕的高宽
        final int screenHeght  = ScreenUtils.getScreenHeight(anchorView.getContext());
        final int screenWidth = ScreenUtils.getScreenWidth(anchorView.getContext());

        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        //计算ontentView 的宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();

        //判断需要向上弹出还是向下弹出
        final boolean isNeedShowUp = (screenHeght - anchorLoc[1] - anchorHeight < windowHeight);
        if(isNeedShowUp){
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] - windowHeight;
        }else {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }
        return windowPos;
    }
}
