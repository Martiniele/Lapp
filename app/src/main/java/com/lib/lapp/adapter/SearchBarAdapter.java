package com.lib.lapp.adapter;

import android.content.Context;
import com.fengmap.android.map.marker.FMModel;
import com.lib.lapp.R;
import java.util.ArrayList;

/**
 * @author wxx
 * @Date 2017/04/13
 * @Description 搜索列表适配 地图搜索框的搜索适配器
 */
public class SearchBarAdapter extends CommonFilterAdapter<FMModel> {

    public SearchBarAdapter(Context context, ArrayList<FMModel> mapModels) {
        super(context, mapModels, R.layout.search_layout_item_model);
    }

    @Override
    public void convert(ViewHolder viewHolder, FMModel mapNode, int position) {
        viewHolder.setText(R.id.txt_model_name, mapNode.getName());
    }
}