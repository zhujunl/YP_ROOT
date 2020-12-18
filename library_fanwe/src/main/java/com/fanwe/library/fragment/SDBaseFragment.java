package com.fanwe.library.fragment;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.fanwe.library.activity.SDBaseActivity;
import com.fanwe.library.common.SDFragmentManager;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.sunday.eventbus.SDBaseEvent;
import com.sunday.eventbus.SDEventManager;
import com.sunday.eventbus.SDEventObserver;

public abstract class SDBaseFragment extends Fragment implements SDEventObserver, OnClickListener
{

	private SDFragmentManager mFragmentManager;
	private SDFragmentLifeCircleListener mListenerLifeCircle;
	private SDFragmentOnActivityResultListener mListenerOnActivityResult;
	private boolean mIsNeedRefreshOnResume = false;
	private View mTitleView;
	private ViewGroup mContentView;
	private LayoutInflater mInflater;
	private ViewGroup mContainer;
	private boolean mIsRemovedFromViewPager = false;

	public boolean isRemovedFromViewPager()
	{
		return mIsRemovedFromViewPager;
	}

	public void setIsRemovedFromViewPager(boolean isRemovedFromViewPager)
	{
		this.mIsRemovedFromViewPager = isRemovedFromViewPager;
	}

	public SDBaseFragment()
	{
		ensureArgumentsNotNull();
	}

	private void ensureArgumentsNotNull()
	{
		if (getArguments() == null)
		{
			super.setArguments(new Bundle());
		}
	}

	@Override
	public void setArguments(Bundle args)
	{
		ensureArgumentsNotNull();
		if (args != null)
		{
			getArguments().putAll(args);
		}
	}

	/**
	 * 用public void setIsNeedRefreshOnResume(boolean isNeedRefreshOnResume)替代
	 * 
	 * @param mIsNeedRefreshOnResume
	 */
	@Deprecated
	public void setmIsNeedRefreshOnResume(boolean mIsNeedRefreshOnResume)
	{
		this.mIsNeedRefreshOnResume = mIsNeedRefreshOnResume;
	}

	/**
	 * 设置是否下次onResume被调用的时候调用onNeedRefreshOnResume方法
	 * 
	 * @param mIsNeedRefreshOnResume
	 */
	public void setIsNeedRefreshOnResume(boolean isNeedRefreshOnResume)
	{
		this.mIsNeedRefreshOnResume = isNeedRefreshOnResume;
	}

	public void setListenerLifeCircle(SDFragmentLifeCircleListener listenerLifeCircle)
	{
		this.mListenerLifeCircle = listenerLifeCircle;
	}

	public SDFragmentLifeCircleListener getListenerLifeCircle()
	{
		return mListenerLifeCircle;
	}

	public void setListenerOnActivityResult(SDFragmentOnActivityResultListener listenerOnActivityResult)
	{
		this.mListenerOnActivityResult = listenerOnActivityResult;
	}

	public SDFragmentOnActivityResultListener getListenerOnActivityResult()
	{
		return mListenerOnActivityResult;
	}

	public SDFragmentManager getSDFragmentManager()
	{
		if (mFragmentManager == null)
		{
			mFragmentManager = new SDFragmentManager(getChildFragmentManager());
		}
		return mFragmentManager;
	}

	@Override
	public void onAttach(Activity activity)
	{
		if (mListenerLifeCircle != null)
		{
			mListenerLifeCircle.onAttach(activity, this);
		}
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		if (mListenerLifeCircle != null)
		{
			mListenerLifeCircle.onCreate(savedInstanceState, this);
		}
		SDEventManager.register(this);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (mListenerLifeCircle != null)
		{
			mListenerLifeCircle.onCreateView(inflater, container, savedInstanceState, this);
		}
		this.mInflater = inflater;
		this.mContainer = container;

		View contentView = onCreateContentView(inflater, container, savedInstanceState);
		if (contentView == null)
		{
			int layoutId = onCreateContentView();
			if (layoutId != 0)
			{
				View layoutView = inflater.inflate(layoutId, container, false);
				contentView = setContentView(layoutView);
			}
		}
		return contentView;
	}

	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return null;
	}

	/**
	 * 此方法用于被重写返回fragment布局id
	 * 
	 * @return
	 */
	protected abstract int onCreateContentView();

	@Deprecated
	protected View setContentView(int layoutResID)
	{
		View contentView = null;
		if (mInflater != null && mContainer != null)
		{
			contentView = mInflater.inflate(layoutResID, mContainer, false);
		}
		return setContentView(contentView);
	}

	/**
	 * 设置fragment布局
	 * 
	 * @param view
	 *            布局
	 * @return 
	 *         经过包裹后的布局(如果onCreateTitleView方法有返回布局，则作为标题栏添加在view布局的上面，外层套一层Linearlayout
	 *         ，否则返回原布局)
	 */
	protected View setContentView(View view)
	{
		mTitleView = createTitleView();
		if (mTitleView != null)
		{
			LinearLayout linAll = new LinearLayout(getActivity());
			linAll.setOrientation(LinearLayout.VERTICAL);
			linAll.addView(mTitleView, generateTitleViewLayoutParams());
			linAll.addView(view, generateContentViewLayoutParams());
			mContentView = linAll;
		} else
		{
			mContentView = (ViewGroup) view;
		}
		return mContentView;
	}

	/**
	 * 此方法用于被重写，创建标题布局，调用setContentView方法时候才会触发
	 * 
	 * @return 标题view
	 */
	protected View onCreateTitleView()
	{
		return null;
	}

	protected int onCreateTitleViewResId()
	{
		return 0;
	}

	private View createTitleView()
	{
		View view = null;
		int resId = onCreateTitleViewResId();
		if (resId != 0)
		{
			view = LayoutInflater.from(getActivity()).inflate(resId, null);
		} else
		{
			view = onCreateTitleView();
		}
		return view;
	}

	protected LinearLayout.LayoutParams generateTitleViewLayoutParams()
	{
		return new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

	protected LinearLayout.LayoutParams generateContentViewLayoutParams()
	{
		return new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}

	public SDBaseActivity getSDBaseActivity()
	{
		SDBaseActivity sdBaseActivity = null;
		Activity activity = getActivity();
		if (activity != null && activity instanceof SDBaseActivity)
		{
			sdBaseActivity = (SDBaseActivity) activity;
		}
		return sdBaseActivity;
	}

	/**
	 * 获得fragment布局
	 * 
	 * @return
	 */
	public View getContentView()
	{
		return mContentView;
	}

	/**
	 * 获得标题布局(onCreateTitleView方法有被重写时有效)
	 * 
	 * @return
	 */
	public View getTitleView()
	{
		return mTitleView;
	}

	/**
	 * 移除标题布局(onCreateTitleView方法有被重写时且返回不为null时有效)
	 */
	public void removeTileView()
	{
		if (mTitleView != null && mContentView != null)
		{
			mContentView.removeView(mTitleView);
		}
	}

	/**
	 * 改变标题布局(onCreateTitleView方法有被重写且返回不为null时有效)
	 * 
	 * @param view
	 */
	public void changeTitleView(View view)
	{
		if (mTitleView != null && view != null && mContentView != null)
		{
			mContentView.removeView(mTitleView);
			mContentView.addView(view, 0, generateTitleViewLayoutParams());
			mTitleView = view;
		}
	}

	public boolean isEmpty(CharSequence content)
	{
		return TextUtils.isEmpty(content);
	}

	public boolean isEmpty(List<?> list)
	{
		return SDCollectionUtil.isEmpty(list);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		if (mListenerLifeCircle != null)
		{
			mListenerLifeCircle.onViewCreated(view, savedInstanceState, this);
		}
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		if (mListenerLifeCircle != null)
		{
			mListenerLifeCircle.onActivityCreated(savedInstanceState, this);
		}
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart()
	{
		if (mListenerLifeCircle != null)
		{
			mListenerLifeCircle.onStart(this);
		}
		super.onStart();
	}

	@Override
	public void onResume()
	{
		if (mListenerLifeCircle != null)
		{
			mListenerLifeCircle.onResume(this);
		}
		if (mIsNeedRefreshOnResume)
		{
			mIsNeedRefreshOnResume = false;
			onNeedRefreshOnResume();
		}
		super.onResume();
	}

	protected void onNeedRefreshOnResume()
	{

	}

	@Override
	public void onPause()
	{
		if (mListenerLifeCircle != null)
		{
			mListenerLifeCircle.onPause(this);
		}
		super.onPause();
	}

	@Override
	public void onStop()
	{
		if (mListenerLifeCircle != null)
		{
			mListenerLifeCircle.onStop(this);
		}
		super.onStop();
	}

	@Override
	public void onDestroyView()
	{
		if (mListenerLifeCircle != null)
		{
			mListenerLifeCircle.onDestroyView(this);
		}
		super.onDestroyView();
	}

	@Override
	public void onDestroy()
	{
		if (mListenerLifeCircle != null)
		{
			mListenerLifeCircle.onDestroy(this);
		}
		SDEventManager.unregister(this);
		super.onDestroy();
	}

	@Override
	public void onDetach()
	{
		if (mListenerLifeCircle != null)
		{
			mListenerLifeCircle.onDetach(this);
		}
		super.onDetach();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (mListenerOnActivityResult != null)
		{
			mListenerOnActivityResult.onActivityResult(requestCode, resultCode, data, this);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 调用此方法会触发onRefreshData()方法
	 */
	public void refreshData()
	{
		if (this.isAdded())
		{
			onRefreshData();
		}
	}

	/**
	 * 调用refreshData()方法后触发此方法
	 */
	protected void onRefreshData()
	{

	}

	public void hideFragmentView()
	{
		SDViewUtil.hide(getView());
	}

	public void showFragmentView()
	{
		SDViewUtil.show(getView());
	}

	public void invisibleFragmentView()
	{
		SDViewUtil.invisible(getView());
	}

	public boolean toggleFragmentView(List<?> list)
	{
		if (list != null && !list.isEmpty())
		{
			showFragmentView();
			return true;
		} else
		{
			hideFragmentView();
			return false;
		}
	}

	public boolean toggleFragmentView(Object obj)
	{
		if (obj != null)
		{
			showFragmentView();
			return true;
		} else
		{
			hideFragmentView();
			return false;
		}
	}

	public boolean toggleFragmentView(String content)
	{
		if (!TextUtils.isEmpty(content))
		{
			showFragmentView();
			return true;
		} else
		{
			hideFragmentView();
			return false;
		}
	}

	public boolean toggleFragmentView(int show)
	{
		if (show == 1)
		{
			showFragmentView();
			return true;
		} else
		{
			hideFragmentView();
			return false;
		}
	}

	public boolean toggleFragmentView(boolean show)
	{
		if (show)
		{
			showFragmentView();
			return true;
		} else
		{
			hideFragmentView();
			return false;
		}
	}

	public View findViewById(int id)
	{
		View view = null;
		View viewFragment = getView();
		if (viewFragment != null)
		{
			view = viewFragment.findViewById(id);
		}
		return view;
	}

	@SuppressWarnings("unchecked")
	public <V extends View> V find(int id)
	{
		View view = null;
		View viewFragment = getView();
		if (viewFragment != null)
		{
			view = viewFragment.findViewById(id);
		}
		return (V) view;
	}

	@Override
	public void onClick(View v)
	{

	}

	@Override
	public void onEvent(SDBaseEvent event)
	{

	}

	@Override
	public void onEventMainThread(SDBaseEvent event)
	{

	}

	@Override
	public void onEventBackgroundThread(SDBaseEvent event)
	{

	}

	@Override
	public void onEventAsync(SDBaseEvent event)
	{

	}

	public interface SDFragmentOnActivityResultListener
	{
		public void onActivityResult(int requestCode, int resultCode, Intent data, Fragment fragment);
	}

	public interface SDFragmentLifeCircleListener
	{
		public void onAttach(Activity activity, Fragment fragment);

		public void onCreate(Bundle savedInstanceState, Fragment fragment);

		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, Fragment fragment);

		public void onViewCreated(View view, Bundle savedInstanceState, Fragment fragment);

		public void onActivityCreated(Bundle savedInstanceState, Fragment fragment);

		public void onStart(Fragment fragment);

		public void onResume(Fragment fragment);

		public void onPause(Fragment fragment);

		public void onStop(Fragment fragment);

		public void onDestroyView(Fragment fragment);

		public void onDestroy(Fragment fragment);

		public void onDetach(Fragment fragment);
	}
}
