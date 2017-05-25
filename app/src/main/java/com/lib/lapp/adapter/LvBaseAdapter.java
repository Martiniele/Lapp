package com.lib.lapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lib.lapp.R;

import java.util.List;
import java.util.Map;

/**
 * Created by wxx on 2017/4/16.
 *
 * @Description ListView 适配器
 */

public class LvBaseAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, Object>> listItems; //存放列表信息的集合
    private LayoutInflater listContainer;  //视图容器
    private boolean[] hasChecked;

    /**
     * Listview items 控件列表内部类
     */
    public final class ListItemView {
        public ImageView image;
        public TextView title;
        public TextView info;
        public CheckBox check;
        public Button detail;
    }

    public LvBaseAdapter(Context context, List<Map<String, Object>> listItems) {
        this.context = context;
        listContainer = LayoutInflater.from(context); //创建视图容器并设置上下文
        this.listItems = listItems;
        hasChecked = new boolean[getCount()];
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 记录勾选了哪个物品
     *
     * @param checkedID
     */
    private void checkedChange(int checkedID) {
        hasChecked[checkedID] = !hasChecked[checkedID];
    }

    /**
     * 判断物品是否选择
     *
     * @param checkedID
     * @return
     */
    public boolean hasChecked(int checkedID) {
        return hasChecked[checkedID];
    }

    /**
     * 显示详情
     *
     * @param checkID
     */
    private void showDetailInfo(int checkID) {
        new AlertDialog.Builder(context).setTitle("书架详情：" + listItems.get(checkID).get("info"))
                .setMessage(listItems.get(checkID).get("detail").toString())
                .setPositiveButton("确定", null)
                .show();
    }

    /**
     * ListView Items设置
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int selectID = position;
        ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemView();
            //获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.fg_mapdata_list_item, null);
            listItemView.image = (ImageView) convertView.findViewById(R.id.imageItem);
            listItemView.title = (TextView) convertView.findViewById(R.id.titleItem);
            listItemView.detail = (Button) convertView.findViewById(R.id.detailItem);
            listItemView.info = (TextView) convertView.findViewById(R.id.infoItem);
            listItemView.check = (CheckBox) convertView.findViewById(R.id.checkItem);
            //设置控件集到convertView
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        listItemView.image.setBackgroundResource((Integer) listItems.get(position).get("image"));
        listItemView.title.setText((String) listItems.get(position).get("title"));
        listItemView.info.setText((String) listItems.get(position).get("info"));
        listItemView.detail.setText("书架详情");
        //注册按钮点击时间
        listItemView.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示设施详情
                showDetailInfo(selectID);
            }
        });
        // 注册多选框状态事件处理
        listItemView.check.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                //记录物品选中状态
                checkedChange(selectID);
            }
        });
        return convertView;
    }
}
