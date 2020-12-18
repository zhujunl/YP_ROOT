package com.o2o_jiangchen.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.o2o_jiangchen.activity.HomeSearchActivity;
import com.o2o_jiangchen.activity.MyCaptureActivity;
import com.yunpeng_chuankou.R;


public class HomeTitleBarFragment extends BaseFragment {
	@ViewInject(R.id.search_rl)
	private RelativeLayout search;
	@ViewInject(R.id.frag_home_title_bar_ll_earn)
	private LinearLayout frag_home_title_bar_ll_earn;
	private Intent intent;

	@Override
	protected View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		return setContentView(R.layout.frag_home_title_bar);
	}

	@Override
	protected void init() {
		registeClick();
	}



	private void registeClick() {
		search.setOnClickListener(this);
		frag_home_title_bar_ll_earn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_rl:
			clickSearch();
			break;
			case R.id.frag_home_title_bar_ll_earn:
			Intent i = new Intent(getActivity(), MyCaptureActivity.class);
				startActivity(i);
				break;
		default:
			break;
		}
	}

	

	/**
	 * 点击搜索
	 */
	private void clickSearch() {
		intent = new Intent(getActivity(),HomeSearchActivity.class);
		startActivity(intent);
		
		
	}


}