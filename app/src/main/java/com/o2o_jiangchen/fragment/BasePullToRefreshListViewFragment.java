package com.o2o_jiangchen.fragment;

import android.widget.ListView;

import com.fanwe.library.pulltorefresh.SDPullToRefresh;
import com.fanwe.library.pulltorefresh.SDPullToRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class BasePullToRefreshListViewFragment extends BaseFragment implements SDPullToRefresh, SDPullToRefreshListener<PullToRefreshListView>
{

    protected PullToRefreshListView mPullView;

    protected void initPullToRefreshListView(PullToRefreshListView listView)
    {
        if (listView != null)
        {
            this.mPullView = listView;
            setModeBoth();
            listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>()
            {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
                {
                    BasePullToRefreshListViewFragment.this.onPullDownToRefresh(mPullView);
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
                {
                    BasePullToRefreshListViewFragment.this.onPullUpToRefresh(mPullView);
                }
            });
        }
    }

    @Override
    public void setRefreshing()
    {
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
    public void onPullDownToRefresh(PullToRefreshListView view)
    {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshListView view)
    {

    }

    @Override
    protected int onCreateContentView()
    {
        return 0;
    }
}

