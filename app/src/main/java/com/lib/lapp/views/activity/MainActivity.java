package com.lib.lapp.views.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lib.lapp.R;
import com.lib.lapp.adapter.MyFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener {

    //UI Objects
    private RadioGroup rg_tab_bar; //切换按钮所在的容器
    private RadioButton rb_map_info; //地图界面切换按钮
    private RadioButton rb_navication_info; //导航信息界面切换按钮
    private RadioButton rb_person_info;  //个人信息界面切换按钮
    private ViewPager vpager;  //承载切换Fragment的容器

    private MyFragmentPagerAdapter mAdapter; //Fragment适配器

    //几个代表页面的常量
    public static final int PAGE_ONE = 0; //地图界面标记
    public static final int PAGE_TWO = 1; //导航信息界面标记
    public static final int PAGE_THREE = 2; //个人信息界面标记


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        bindViews();
        rb_map_info.setChecked(true);
    }

    /**
     * 初始化控件，以及将Fragment适配器绑定到ViewPage控件上，并设置监听事件
     * 以此来实现页面的切换
     */
    private void bindViews() {
        rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rb_map_info = (RadioButton) findViewById(R.id.rb_map_info);
        rb_navication_info = (RadioButton) findViewById(R.id.rb_navication_info);
        rb_person_info = (RadioButton) findViewById(R.id.rb_person_info);
        rg_tab_bar.setOnCheckedChangeListener(this);   //设置切换监听
        vpager = (ViewPager) findViewById(R.id.vpager); //获取ViewPage资源
        vpager.setAdapter(mAdapter);  //绑定fragment适配器
        vpager .setOffscreenPageLimit(2);
        vpager.setCurrentItem(0);     //设置初始的Fragment页面为第0页
        vpager.addOnPageChangeListener(this); //设置ViewPage切换监听，即页面切换改变时的监听事件
    }

    /**
     * 重载RadioGroup的监听事件方法，实现对切换按钮的事件响应
     * @param group
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_map_info:
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.rb_navication_info:
                vpager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.rb_person_info:
                vpager.setCurrentItem(PAGE_THREE);
                break;
        }
    }

    //重写ViewPager页面切换的处理方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (vpager.getCurrentItem()) {
                case PAGE_ONE:
                    rb_map_info.setChecked(true);
                    break;
                case PAGE_TWO:
                    rb_navication_info.setChecked(true);
                    break;
                case PAGE_THREE:
                    rb_person_info.setChecked(true);
                    break;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState);
    }
}
