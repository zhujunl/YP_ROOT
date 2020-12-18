package com.o2o_jiangchen.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.fanwe.library.pulltorefresh.SDPullToRefresh;
import com.fanwe.library.pulltorefresh.SDPullToRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.yunpeng_chuankou.R;

public class BasePullToRefreshScrollViewFragment extends BaseFragment implements SDPullToRefresh,
		SDPullToRefreshListener<PullToRefreshScrollView>
		{
		
		protected PullToRefreshScrollView mPullView;
		private ScrollView scrollView;
		
		@Override
		protected View setContentView(View view)
		{
		View viewScrollParent = LayoutInflater.from(getActivity()).inflate(R.layout.view_pull_to_refresh_scrollview, null);
		mPullView = (PullToRefreshScrollView) viewScrollParent.findViewById(R.id.ptrsv_all);
		mPullView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>()
		{
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView)
			{
				BasePullToRefreshScrollViewFragment.this.onPullDownToRefresh(mPullView);
			}
		
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView)
			{
				BasePullToRefreshScrollViewFragment.this.onPullUpToRefresh(mPullView);
			}
		});
		
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		mPullView.getRefreshableView().addView(view, params);
		return super.setContentView(viewScrollParent);
		}
		@Override
		public void setRefreshing()
		{
		
		scrollView = mPullView.getRefreshableView();
		scrollView.post(new Runnable() {
			public void run() {
				scrollView.fullScroll(ScrollView.FOCUS_UP);
				//scrollView.fullScroll(ScrollView.FOCUS_DOWN);
				}
				});
		mPullView.setRefreshing();
		}
		
		@Override
		public void setModePullFromStart()
		{
		mPullView.setMode(Mode.PULL_FROM_START);
		}
		
		@Override
		public void setModePullFromEnd()
		{
		mPullView.setMode(Mode.PULL_FROM_END);
		}
		
		@Override
		public void setModeBoth()
		{
		mPullView.setMode(Mode.BOTH);
		}
		
		@Override
		public void setModeDisabled()
		{
		mPullView.setMode(Mode.DISABLED);
		}
		
		@Override
		public void onRefreshComplete()
		{
		mPullView.onRefreshComplete();
		}
		
		@Override
		public void onPullDownToRefresh(PullToRefreshScrollView view)
		{
		
		}
		
		@Override
		public void onPullUpToRefresh(PullToRefreshScrollView view)
		{
		
		}
		}
