package com.o2o_jiangchen.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.o2o_jiangchen.fragment.BaseFragment;

import java.util.List;

/**
 * Created by Administrator on 2020/5/12.
 */

public class MyPagerAdapter   extends FragmentStatePagerAdapter {

    private final FragmentManager mFragmentManager;
    private List<BaseFragment> list;
    public MyPagerAdapter(FragmentManager fm,List<BaseFragment> list) {
        super(fm);
        this.mFragmentManager = fm;
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getItemPosition(Object object) {//最主要就是加了这个方法。
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return list.size();
    }


}