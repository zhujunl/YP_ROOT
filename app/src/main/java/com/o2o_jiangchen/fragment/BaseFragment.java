package com.o2o_jiangchen.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;

import com.fanwe.library.common.SDActivityManager;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.dialog.SDDialogCustom.SDDialogCustomListener;
import com.fanwe.library.fragment.SDBaseFragment;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.title.SDTitleSimple;
import com.fanwe.library.title.SDTitleSimple.SDTitleSimpleListener;
import com.fanwe.library.utils.SDResourcesUtil;
import com.lidroid.xutils.ViewUtils;
import com.o2o_jiangchen.activity.BaseActivity;
import com.o2o_jiangchen.app.MyApplication;
import com.o2o_jiangchen.constant.Constant;
import com.yunpeng_chuankou.R;


public class BaseFragment extends SDBaseFragment implements
		SDTitleSimpleListener {
		
		protected SDTitleSimple mTitle;
		private Constant.TitleType mTitleType = Constant.TitleType.TTITLE_NONE;
		private boolean once = false;
		public Constant.TitleType getmTitleType() {
		return mTitleType;
		}
		
		public void setmTitleType(Constant.TitleType mTitleType) {
		this.mTitleType = mTitleType;
		}
		
		@Override
		protected View onCreateTitleView() {
		View viewTitle = null;
		switch (getmTitleType()) {
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
		protected LayoutParams generateTitleViewLayoutParams() {
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				SDResourcesUtil.getDimensionPixelSize(R.dimen.height_title_bar));
		return params;
		}
		
		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
		ViewUtils.inject(this, view);
		init();
		super.onViewCreated(view, savedInstanceState);
		}
		
		protected void init() {
		
		}
		
		public BaseActivity getBaseActivity() {
		Activity activity = getActivity();
		if (activity != null && activity instanceof BaseActivity) {
			return (BaseActivity) activity;
		}
		return null;
		}
		
		@Override
		public void onCLickLeft_SDTitleSimple(SDTitleItem v) {
		getActivity().finish();
		}
		
		@Override
		public void onCLickMiddle_SDTitleSimple(SDTitleItem v) {
		
		}
		
		@Override
		public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
		
		}
		
		@Override
		protected int onCreateContentView() {
		// TODO Auto-generated method stub
		return 0;
		}
		
		@Override
		public void onResume() {
		
			if(!once){
				 SharedPreferences sp =getActivity().getSharedPreferences("close", Context.MODE_PRIVATE);
				 int isClose = sp.getInt("shop_close", 0);
				 String info = sp.getString("close_info", "");
				 int showfra= sp.getInt("showfra", 1);
					if(isClose == 1){
						startDialog(info,showfra);
					}
				once = true;
			}
		
		super.onResume();
		}
		private  void startDialog(String info,int showfra)
		{ 
		if(showfra == 1){
			SDDialogConfirm dialog = new SDDialogConfirm();
			dialog.setTextTitle("通知");
			dialog.mTvCancel.setVisibility(View.GONE);
			dialog.setOnKeyListener(new OnKeyListener() {
				
				@Override
				public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
					 if (arg1 == KeyEvent.KEYCODE_BACK && arg2.getRepeatCount() == 0) {  
			                return true;  
			            } else {  
			                return false;  
			            }  
				}
			});
			dialog.setTextContent(info).setmListener(new SDDialogCustomListener()
			{
		
				@Override
				public void onDismiss(SDDialogCustom dialog)
				{
				}
		
				@Override
				public void onClickConfirm(View v, SDDialogCustom dialog)
				{
					SDActivityManager.getInstance().finishAllActivity();
					System.exit(0);
				}
		
				@Override
				public void onClickCancel(View v, SDDialogCustom dialog)
				{
				}
			}).show();
			showfra = 0;
			SharedPreferences sp =getActivity().getSharedPreferences("close", Context.MODE_PRIVATE);
			 Editor et = sp.edit();
			 et.putInt("showfra", showfra);
			 et.commit();
		}
		}
		
		}
		
