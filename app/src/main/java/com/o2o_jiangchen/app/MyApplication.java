package com.o2o_jiangchen.app;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.fanwe.library.SDLibrary;
import com.fanwe.library.command.SDCommandManager;
import com.fanwe.library.config.SDLibraryConfig;
import com.fanwe.library.utils.SDViewUtil;
import com.o2o_jiangchen.common.ImageLoaderManager;
import com.sunday.eventbus.SDBaseEvent;
import com.sunday.eventbus.SDEventManager;
import com.sunday.eventbus.SDEventObserver;
import com.yunpeng_chuankou.R;

import org.xutils.x;

import ai.yunji.water.task.RobotConnectAction;
import cn.wch.ch34xuartdriver.CH34xUARTDriver;


public class MyApplication extends MultiDexApplication implements SDEventObserver {
    public static CH34xUARTDriver driver;// 需要将CH34x的驱动类写在APP类下面，使得帮助类的生命周期与整个应用程序的生命周期是相同的
    private static MyApplication mApp = null;

    public Intent mPushIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void init() {
        mApp = this;
        x.Ext.init(this);
        ImageLoaderManager.initImageLoader();
        initSDLibrary();
        SDEventManager.register(this);
        SDCommandManager.getInstance().initialize();
        RobotConnectAction.init(mApp);
    }

    private void initSDLibrary() {
        SDLibrary.getInstance().init(getApplication());

        SDLibraryConfig config = new SDLibraryConfig();

        config.setmMainColor(getResources().getColor(R.color.main_color));
        config.setmMainColorPress(getResources().getColor(
                R.color.main_color_press));

        config.setmTitleColor(getResources().getColor(R.color.bg_title_bar));
        config.setmTitleColorPressed(getResources().getColor(
                R.color.bg_title_bar_pressed));
        config.setmTitleHeight(getResources().getDimensionPixelOffset(
                R.dimen.height_title_bar));

        config.setmStrokeColor(getResources().getColor(R.color.stroke));
        config.setmStrokeWidth(SDViewUtil.dp2px(1));

        config.setmCornerRadius(getResources().getDimensionPixelOffset(
                R.dimen.corner));
        config.setmGrayPressColor(getResources().getColor(R.color.gray_press));

        SDLibrary.getInstance().setConfig(config);
    }


    public static MyApplication getApplication() {
        return mApp;
    }


    public static String getStringById(int resId) {
        return getApplication().getString(resId);
    }

    @Override
    public void onTerminate() {
        SDEventManager.unregister(this);
        super.onTerminate();
    }

    @Override
    public void onEvent(SDBaseEvent event) {

    }

    @Override
    public void onEventBackgroundThread(SDBaseEvent event) {

    }

    @Override
    public void onEventAsync(SDBaseEvent event) {

    }

    @Override
    public void onEventMainThread(SDBaseEvent event) {

    }

}

