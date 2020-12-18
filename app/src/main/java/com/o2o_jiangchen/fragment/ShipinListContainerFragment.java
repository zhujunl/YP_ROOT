package com.o2o_jiangchen.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.o2o_jiangchen.activity.MainActivity;
import com.o2o_jiangchen.constant.Constant;
import com.yunpeng_chuankou.R;


public class ShipinListContainerFragment extends BaseFragment {
	private ShipinListFragment mFragment;

	@Override
	protected View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		setmTitleType(Constant.TitleType.TITLE);
		return setContentView(R.layout.view_container);
	}

	@Override
	protected void init() {
		initTitle();
		addFragments();
	}

	private void addFragments() {

		mFragment = new ShipinListFragment();
		getSDFragmentManager().replace(R.id.view_container_fl_content,
				mFragment);
	}
	
	private void initTitle() {

		String title = "视频";
		mTitle.setMiddleTextTop(title);
		mTitle.setBackgroundColor(getResources().getColor(R.color.main_color));
		if (getActivity() instanceof MainActivity) {
			mTitle.setLeftImageLeft(0);
		} else {
			mTitle.setLeftImageLeft(R.drawable.ic_arrow_left_white);
		}

	}
}
