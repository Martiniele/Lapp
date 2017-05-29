package com.lib.lapp.adapter;

import android.content.Context;

import com.lib.lapp.R;

import java.util.List;

/**
 * @author wxx
 * @Date 2017/5/29 15:29
 * @Description
 */

public class BookSearchAdapter extends CommonFilterAdapter<String> {

    public BookSearchAdapter(Context context, List<String> datas) {
        super(context, datas, R.layout.search_layout_item_model);
    }

    @Override
    public void convert(ViewHolder viewHolder, String item, int position) {
        viewHolder.setText(R.id.txt_model_name, item);
    }
}
