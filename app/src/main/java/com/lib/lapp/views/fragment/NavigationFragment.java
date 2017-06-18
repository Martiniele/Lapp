package com.lib.lapp.views.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.lapp.R;
import com.lib.lapp.adapter.LvBaseAdapter;
import com.lib.lapp.widget.RefreshableView;
import com.lib.lapp.widget.mylistview.SimpleFooter;
import com.lib.lapp.widget.mylistview.SimpleHeader;
import com.lib.lapp.widget.mylistview.ZrcListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NavigationFragment extends BaseFragment {
    private ZrcListView zrc_listview;
    private ZrcListView listView;

    private Handler handler;
    private ArrayList<String> msgs;
    private int pageId = -1;

    private MyAdapter adapter;

    private static final String[][] names = new String[][]{
            {"1层B区第1排", "1层B区第2排", "1层B区第3排", "1层B区第4排", "1层B区第5排", "1层B区第6排", "1层B区第7排", "1层B区第8排", "1层B区第9排", "1层B区第10排", "1层B区第11排", "1层B区第12排", "1层B区第13排", "1层B区第14排", "1层B区第15排"},
            {"1层B区第16排", "1层B区第17排", "1层B区第18排", "1层B区第19排", "1层B区第20排", "1层B区第21排", "1层B区第22排", "1层B区第23排", "1层B区第24排", "1层B区第25排", "1层B区第26排", "1层B区第27排", "1层B区第28排", "1层B区第29排", "1层B区第30排"},
            {"1层B区第31排", "1层B区第32排", "1层B区第33排", "1层B区第34排", "1层B区第35排", "1层B区第36排", "1层B区第37排", "1层B区第38排", "1层B区第39排", "1层B区第40排", "1层B区第41排", "1层B区第42排"},
            {"1层A区第1排", "1层A区第2排", "1层A区第3排", "1层A区第4排", "1层A区第5排", "1层A区第6排", "1层A区第7排", "1层A区第8排", "1层A区第9排", "1层A区第10排", "1层A区第11排"}
    };

    public NavigationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_mapdata_content, container, false);
        listView = (ZrcListView) view.findViewById(R.id.zListView);

        handler = new Handler();
        // 设置下拉刷新的样式（可选，但如果没有Header则无法下拉刷新）
        SimpleHeader header = new SimpleHeader(getActivity());
        header.setTextColor(R.color.txt_color);
        header.setCircleColor(R.color.h_circle_color);
        listView.setHeadable(header);

        // 设置加载更多的样式（可选）
        SimpleFooter footer = new SimpleFooter(getActivity());
        footer.setCircleColor(R.color.f_circle_color);
        listView.setFootable(footer);
        listView.setOnItemClickListener(new ZrcListView.OnItemClickListener() {

            @Override
            public void onItemClick(ZrcListView parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                String resultString = (String) parent.getAdapter().getItem(position);
                Toast.makeText(getActivity(), resultString, Toast.LENGTH_SHORT).show();
            }
        });
        // 设置列表项出现动画（可选）
        listView.setItemAnimForTopIn(R.anim.topitem_in);
        listView.setItemAnimForBottomIn(R.anim.bottomitem_in);


        // 下拉刷新事件回调（可选）
        listView.setOnRefreshStartListener(new ZrcListView.OnStartListener() {
            @Override
            public void onStart() {
                refresh();
            }

        });

        // 加载更多事件回调（可选）
        listView.setOnLoadMoreStartListener(new ZrcListView.OnStartListener() {
            @Override
            public void onStart() {
                loadMore();
            }
        });

        adapter = new MyAdapter();
        listView.setAdapter(adapter);
        listView.refresh(); // 主动下拉刷新
        return view;
    }

    private void refresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int rand = (int) (Math.random() * 2); // 随机数模拟成功失败。这里从有数据开始。
                if (rand == 0 || pageId == -1) {
                    pageId = 0;
                    msgs = new ArrayList<String>();
                    for (String name : names[0]) {
                        msgs.add(name);
                    }
                    adapter.notifyDataSetChanged();
                    listView.setRefreshSuccess("加载成功"); // 通知加载成功
                    listView.startLoadMore(); // 开启LoadingMore功能
                } else {
                    listView.setRefreshFail("加载失败");
                }
            }
        }, 2 * 1000);
    }

    private void loadMore() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pageId++;
                if (pageId < names.length) {
                    for (String name : names[pageId]) {
                        msgs.add(name);
                    }
                    adapter.notifyDataSetChanged();
                    listView.setLoadMoreSuccess();
                } else {
                    listView.stopLoadMore();
                }
            }
        }, 2 * 1000);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void fetchData() {

    }

    @Override
    public void updateLocateGroupView() {

    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return msgs == null ? 0 : msgs.size();
        }

        @Override
        public Object getItem(int position) {
            return msgs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView;
            if (convertView == null) {
                textView = (TextView) getActivity().getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
                textView.setTextColor(R.color.bg_black);
            } else {
                textView = (TextView) convertView;
            }
            textView.setText(msgs.get(position));
            return textView;
        }
    }
}
