package com.o2o_jiangchen.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.o2o_jiangchen.adapter.Grid0Adapter;
import com.o2o_jiangchen.adapter.Grid1Adapter;
import com.o2o_jiangchen.adapter.Grid2Adapter;
import com.o2o_jiangchen.adapter.Grid3Adapter;
import com.o2o_jiangchen.customview.MyGridView;
import com.o2o_jiangchen.model.Grid0model;
import com.o2o_jiangchen.utils.DisplayUtil;
import com.yunpeng_chuankou.R;

import java.util.ArrayList;
import java.util.List;



public class ShipinListFragment  extends BasePullToRefreshScrollViewFragment {
	@ViewInject(R.id.gridView0)
	private MyGridView gridView0 = null;

	@ViewInject(R.id.gridView1)
	private MyGridView gridView1 = null;

	@ViewInject(R.id.gridView2)
	private MyGridView gridView2 = null;

	@ViewInject(R.id.gridView3)
	private MyGridView gridView3 = null;
	
	private Grid0Adapter adapter0;
	private Grid1Adapter adapter1;
	private Grid2Adapter adapter2;
	private Grid3Adapter adapter3;
	private List<Grid0model> model0List = new ArrayList<Grid0model>();
	@Override
	protected View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		return setContentView(R.layout.shipinlist_frag);

	}
	
	@Override
	protected void init() {
		gridView0.setColumnWidth(DisplayUtil.getScreenMetrics(getActivity()).x / 3);

		adapter0 = new Grid0Adapter(getActivity(),model0List);
		gridView0.setAdapter(adapter0);
		
		
		gridView1
				.setColumnWidth(DisplayUtil.getScreenMetrics(getActivity()).x / 3);
		adapter1 = new Grid1Adapter(getActivity(),model0List);
		gridView1.setAdapter(adapter1);
		
		
		gridView2
				.setColumnWidth(DisplayUtil.getScreenMetrics(getActivity()).x / 3);
		adapter2 = new Grid2Adapter(getActivity(),model0List);
		gridView2.setAdapter(adapter2);
		
		
		gridView3
				.setColumnWidth(DisplayUtil.getScreenMetrics(getActivity()).x / 3);
		adapter3 = new Grid3Adapter(getActivity(),model0List);
		gridView3.setAdapter(adapter3);
		setRefreshing();
		
	}
	@Override
	public void onPullDownToRefresh(PullToRefreshScrollView view) {
		model0List.clear();
		requestIndex();
	}
	private void requestIndex()
	{
		for (int i = 0; i < 8; i++) {
			Grid0model model0 = new Grid0model();
			model0.setImg(R.drawable.my_orders);
			model0.setName("课程"+i);
			model0List.add(model0);
		}
		adapter0.notifyDataSetChanged();
		adapter1.notifyDataSetChanged();
		adapter2.notifyDataSetChanged();
		adapter3.notifyDataSetChanged();
		onRefreshComplete();
	}
}
