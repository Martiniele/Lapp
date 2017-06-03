package com.lib.lapp.views.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lib.lapp.R;
import com.lib.lapp.adapter.LvBaseAdapter;
import com.lib.lapp.widget.RefreshableView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NavigationFragment extends BaseFragment {
    private RefreshableView refreshableView;
    private ListView listView;
    private LvBaseAdapter listViewAdapter;
    private List<Map<String, Object>> listItems;
    private Integer[] imgeIDs = {R.drawable.ic_laulogo,
            R.drawable.ic_laulogo, R.drawable.ic_laulogo,
            R.drawable.ic_laulogo, R.drawable.ic_laulogo,
            R.drawable.ic_laulogo, R.drawable.ic_laulogo};
    private String[] goodsNames = {"B区第一排", "B区第二排",
            "B区第三排", "B区第四排", "B区第五排", "B区第六排", "B区第七排"};
    private String[] goodsDetails = {
            "B区第一排：TP类图书",
            "B区第二排：TP类图书",
            "B区第三排：TP类图书",
            "B区第四排：TP类图书",
            "B区第五排：TP类图书",
            "B区第六排：TP类图书",
            "B区第七排：TP类图书"
    };

    public NavigationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_mapdatalv_content, container, false);
        Log.e("INFO", "第二个页面加载");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView) getActivity().findViewById(R.id.list_goods);
        refreshableView = (RefreshableView) getActivity().findViewById(R.id.refreshable_view);
        listItems = getListItems();
        listViewAdapter = new LvBaseAdapter(getActivity(), listItems); //创建适配器
        listView.setAdapter(listViewAdapter);
        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                refreshableView.finishRefreshing();
            }
        }, 0);
    }

    /**
     * 初始化商品信息
     */
    private List<Map<String, Object>> getListItems() {
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < goodsNames.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", imgeIDs[i]);               //图片资源
            map.put("title", "书架编号：");              //物品标题
            map.put("info", goodsNames[i]);     //物品名称
            map.put("detail", goodsDetails[i]); //物品详情
            listItems.add(map);
        }
        return listItems;
    }

    @Override
    public void fetchData() {

    }

    @Override
    public void updateLocateGroupView() {

    }
}
