package com.o2o_jiangchen.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;

import com.fanwe.library.activity.SDBaseActivity;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.title.SDTitleSimple;
import com.fanwe.library.title.SDTitleSimple.SDTitleSimpleListener;
import com.fanwe.library.utils.SDResourcesUtil;
import com.lidroid.xutils.ViewUtils;
import com.o2o_jiangchen.app.MyApplication;
import com.o2o_jiangchen.constant.Constant;
import com.yunpeng_chuankou.R;


public class BaseActivity extends SDBaseActivity implements SDTitleSimpleListener{
	/** 是否是当作广告页面启动 (boolean) */
	public static final String EXTRA_IS_ADVS = "extra_is_advs";
	private Constant.TitleType mTitleType = Constant.TitleType.TTITLE_NONE;
	protected SDTitleSimple mTitle;
	
	public Constant.TitleType getmTitleType()
	{
		return mTitleType;
	}

	public void setmTitleType(Constant.TitleType mTitleType)
	{
		this.mTitleType = mTitleType;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		hideBottomMenu0();
	}
	public void hideBottomMenu0() {
		Window _window = getWindow();
		WindowManager.LayoutParams params = _window.getAttributes();
		params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
		_window.setAttributes(params);
	}


	@Override
	protected View onCreateTitleView()
	{
		View viewTitle = null;
		switch (getmTitleType())
		{
		case TITLE:
			mTitle = new SDTitleSimple(MyApplication.getApplication());
			mTitle.setLeftImageLeft(R.drawable.ic_arrow_left_white);
			mTitle.setmListener(this);
			viewTitle = mTitle;
			break;
		default:
			break;
		}
		return viewTitle;
	}

	@Override
	protected LayoutParams generateTitleViewLayoutParams()
	{
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, SDResourcesUtil.getDimensionPixelSize(R.dimen.height_title_bar));
		return params;
	}

	@Override
	public void setContentView(View view)
	{
		super.setContentView(view);
		ViewUtils.inject(this);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	public void finish()
	{
		super.finish();
	}



	@Override
	public void onCLickLeft_SDTitleSimple(SDTitleItem v)
	{
		finish();
	}

	@Override
	public void onCLickMiddle_SDTitleSimple(SDTitleItem v) {
	}

	@Override
	public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
	}

}
