package com.lib.lapp.widget;

import android.view.View;

import com.lib.lapp.widget.utils.TaskFilter;


/**
 * @author wxx
 * @Description 重复单击监听
 */
public abstract class OnSingleClickListener implements View.OnClickListener {

    @Override
    public void onClick(View view) {
        if (TaskFilter.filter()) {
            onSingleClick(view);
        }
    }

    /**
     * 用于为外部提供的覆写方法，以实现点击事件
     */
    public abstract void onSingleClick(View view);
}
