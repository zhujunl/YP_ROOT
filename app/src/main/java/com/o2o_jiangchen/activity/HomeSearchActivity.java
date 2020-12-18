package com.o2o_jiangchen.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.fanwe.library.customview.ClearEditText;
import com.fanwe.library.utils.SDToast;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.o2o_jiangchen.constant.Constant;
import com.yunpeng_chuankou.R;


public class HomeSearchActivity extends BaseActivity
{

	@ViewInject(R.id.act_home_search_rl_search_bar)
	private RelativeLayout mRlSearchBar = null;

	@ViewInject(R.id.act_home_search_et_search_text)
	private ClearEditText mEtSearchText = null;

	@ViewInject(R.id.act_home_search_btn_search)
	private Button mBtnSearch = null;




	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setmTitleType(Constant.TitleType.TITLE);
		setContentView(R.layout.act_home_search);
		init();
	}

	private void init()
	{
		initTitle();
		registeClick();
	}




	private void initTitle()
	{
		mTitle.setMiddleTextTop("搜索");
	}

	private void registeClick()
	{
		mBtnSearch.setOnClickListener(this);

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.act_home_search_btn_search:
			clickSearchBtn();
			break;

		default:
			break;
		}
	}

	/**
	 * 搜索
	 */
	private void clickSearchBtn()
	{
		SDToast.showToast("搜索");
	}

}
