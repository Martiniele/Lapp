package com.lib.lapp.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.fengmap.android.map.FMGroupInfo;
import com.fengmap.android.map.FMMap;
import com.fengmap.android.map.FMMapInfo;
import com.fengmap.android.map.marker.FMModel;
import com.lib.lapp.R;
import com.lib.lapp.widget.OnSingleClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wxx
 * @Description 搜索结果  基础设施搜索适配器
 */
public class AnalysisListAdapter extends CommonAdapter<FMModel> {

    private FMModel mLastClicked;
    private FMMap mFMMap;

    public AnalysisListAdapter(Context context, FMMap map, List<FMModel> datas) {
        super(context, datas, R.layout.search_item_analysis_result);
        this.mFMMap = map;
    }

    @Override
    public void convert(ViewHolder viewHolder, final FMModel model, int position) {
        String modelName = model.getName();
        if (TextUtils.isEmpty(modelName)) {
            modelName = "空";
        }
        String name = (position + 1) + "、" + modelName;
        viewHolder.setText(R.id.title, name);
        viewHolder.getConvertView().setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                onItemClick(model);
            }
        });
        String groupName = convertToGroupName(model.getGroupId());
        viewHolder.setText(R.id.group, groupName);
    }


    /**
     * 点击时间处理
     *
     * @param model 模型
     */
    private void onItemClick(FMModel model) {
        //清除上次聚焦效果
        if (!model.equals(mLastClicked)) {
            if (mLastClicked != null) {
                mLastClicked.setSelected(false);
            }
            this.mLastClicked = model;
            this.mLastClicked.setSelected(true);
        }

        //切换楼层并居中
        if (mFMMap.getFocusGroupId() != model.getGroupId()) {
            mFMMap.setFocusByGroupId(model.getGroupId(), null);
        }
        mFMMap.moveToCenter(model.getCenterMapCoord(), true);
    }

    /**
     * 将groupId转换为楼层名
     *
     * @param groupId 楼层id
     * @return
     */
    private String convertToGroupName(int groupId) {
        FMMapInfo mapInfo = mFMMap.getFMMapInfo();

        ArrayList<FMGroupInfo> groups = mapInfo.getGroups();
        for (int i = 0; i < groups.size(); i++) {
            FMGroupInfo groupInfo = groups.get(i);
            if (groupInfo.getGroupId() == groupId) {
                return groupInfo.getGroupName().toUpperCase();
            }
        }
        return null;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

        if (mLastClicked != null) {
            mLastClicked.setSelected(false);
            mLastClicked = null;
        }
    }

}

