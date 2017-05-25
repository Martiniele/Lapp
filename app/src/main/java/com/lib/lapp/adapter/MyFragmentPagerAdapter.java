package com.lib.lapp.adapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import com.lib.lapp.views.activity.MainActivity;
import com.lib.lapp.views.fragment.MapFragment;
import com.lib.lapp.views.fragment.NavigationFragment;
import com.lib.lapp.views.fragment.PersonFragment;

/**
 * @author wxx
 * @Description Fragment 适配器
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int PAGER_COUNT = 3;
    private MapFragment mapFragment = null;
    private NavigationFragment navigationFragment = null;
    private PersonFragment personFragment = null;

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        mapFragment = new MapFragment();
        navigationFragment = new NavigationFragment();
        personFragment = new PersonFragment();
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case MainActivity.PAGE_ONE:
                fragment = mapFragment;
                break;
            case MainActivity.PAGE_TWO:
                fragment = navigationFragment;
                break;
            case MainActivity.PAGE_THREE:
                fragment = personFragment;
                break;
        }
        return fragment;
    }
}

