package com.o2o_jiangchen.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.o2o_jiangchen.activity.MainActivity_Gai;
import com.o2o_jiangchen.adapter.FloorGridAdapter;
import com.o2o_jiangchen.constant.Constant;
import com.o2o_jiangchen.customview.MyGridView;
import com.o2o_jiangchen.model.MarkModel;
import com.o2o_jiangchen.my_interface.SwitchSelectInterface;
import com.yunpeng_chuankou.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2020/5/12.
 */

public class ViewPagerListViewFragment extends BaseFragment {
    private boolean flag = false;
    private List<MarkModel> resultsBeans = new ArrayList<>();
    @ViewInject(R.id.gridView)
    MyGridView gridView;
    private FloorGridAdapter floorGridAdapter;
    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        setmTitleType(Constant.TitleType.TTITLE_NONE);
        return setContentView(R.layout.frag_viewpager_listview);
    }

    @Override
    protected void init() {
        initGridView();
    }

    private void initGridView() {
        floorGridAdapter = new FloorGridAdapter(getActivity(), resultsBeans, new SwitchSelectInterface() {
            @Override
            public void select(int position) {
                MainActivity_Gai parentActivity = (MainActivity_Gai) getActivity();
                parentActivity.selectMarkName(resultsBeans.get(position).marker_name);
            }
        });
        gridView.setAdapter(floorGridAdapter);
    }

    public void setData(int floor, List<MarkModel> mapList){
        List<MarkModel> markerList = new ArrayList<>();
        markerList.clear();

        for (int i = 0; i < mapList.size(); i++) {
            if (floor == mapList.get(i).floor){
                markerList.add(mapList.get(i));
            }
        }
        resultsBeans.addAll(markerList);

    }

    public void refreshData(){
        if (!flag){
            dealDataToUi();
            flag = true;
        }
    }
    public void dealDataToUi() {
        if (resultsBeans.size() == 0)
            return;
        floorGridAdapter.notifyDataSetChanged();
    }

    public void initSelectStatus(){
        if (resultsBeans.size() == 0)
            return;
        for (int i = 0; i < resultsBeans.size(); i++) {
            resultsBeans.get(i).setSelect(false);
        }
        dealDataToUi();
    }
}
