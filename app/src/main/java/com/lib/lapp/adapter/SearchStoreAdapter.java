package com.lib.lapp.adapter;

import android.content.Context;

import com.lib.lapp.R;
import com.lib.lapp.model.Store;
import java.util.ArrayList;

/**
 * @author wxx
 * @Date 2017/04/13
 * @Description 搜索列表适配  用于业务相关搜索的适配器
 */
public class SearchStoreAdapter extends CommonFilterAdapter<Store> {

    public SearchStoreAdapter(Context context, ArrayList<Store> goods) {
        super(context, goods, R.layout.search_layout_item_model);
    }

    @Override
    public void convert(ViewHolder viewHolder, Store store, int position) {
        viewHolder.setText(R.id.txt_model_name, store.NAME);
    }
}