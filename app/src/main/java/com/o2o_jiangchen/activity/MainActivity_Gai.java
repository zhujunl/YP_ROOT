package com.o2o_jiangchen.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.hardware.usb.UsbManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.utils.SDToast;
import com.google.gson.Gson;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.o2o_jiangchen.adapter.MyPagerAdapter;
import com.o2o_jiangchen.app.MyApplication;
import com.o2o_jiangchen.constant.Constant;
import com.o2o_jiangchen.customview.EyeView;
import com.o2o_jiangchen.fragment.BaseFragment;
import com.o2o_jiangchen.fragment.ViewPagerListViewFragment;
import com.o2o_jiangchen.model.MarkModel;
import com.o2o_jiangchen.utils.Jc_Utils;
import com.yunpeng_chuankou.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ai.yunji.water.callback.RobotMoveCallback;
import ai.yunji.water.constant.Notification;
import ai.yunji.water.entity.Marker;
import ai.yunji.water.entity.MarkersReponse;
import ai.yunji.water.entity.NotificationResponse;
import ai.yunji.water.entity.RobotPowerStatusResponse;
import ai.yunji.water.entity.RobotStatusReponse;
import ai.yunji.water.task.RobotConnectAction;
import cn.wch.ch34xuartdriver.CH34xUARTDriver;

import static com.o2o_jiangchen.utils.Jc_Utils.toByteArray2;


public class MainActivity_Gai extends BaseActivity {
    private static final String ACTION_USB_PERMISSION = "cn.wch.wchusbdriver.USB_PERMISSION";
    public byte[] writeBuffer;
    public byte[] readBuffer;
    private boolean isOpen;

    @ViewInject(R.id.tablayout)
    private TabLayout tabLayout;
    @ViewInject(R.id.view_pager)
    private ViewPager view_pager;
    @ViewInject(R.id.tv_open_close)
    TextView tv_open_close;

    private MyPagerAdapter viewpagerFragmentAdapter;
    private List<BaseFragment> list = new ArrayList<BaseFragment>();
    private int currentIndex;

    private boolean songcan = false;
    private boolean click_flag=true;//可点击
    private boolean f=true;

    private CountDownTimer mTimer;
    private int remainTime;
    private Dialog mPreviewDialog = null;

    //开门时候的眼睛
    private Dialog eyedialog;

    //电量
    @ViewInject(R.id.batter_txt)
    private TextView battertxt;
    @ViewInject(R.id.batter_img)
    private ImageView batterimg;
    private RobotPowerStatusResponse ro;
    private boolean notice;
    private int batter;

    //时间
    private Timer timer;
    private Timer timer2;
    @ViewInject(R.id.time)
    private TextView timeview;
    private long mill;

    private RobotStatusReponse robotStatusReponse;
    private RobotStatusReponse.ResultsBean resultsBean;

    //关机
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(Constant.TitleType.TTITLE_NONE);
        setContentView(R.layout.act_main_gai);
        mPreviewDialog = new Dialog(MainActivity_Gai.this, R.style.unlogin_dialog);
        init();

        battertxt=findViewById(R.id.batter_txt);
        batterimg=findViewById(R.id.batter_img);



        IntentFilter timeFilter=new IntentFilter();
        timeFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(timeReceiver,timeFilter);

        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {//获取电量计时器
                // (1) 使用handler发送消息
                Message message=new Message();
                message.what=0;
                mHandler.sendMessage(message);
            }
        },0,60000);


        timer2=new Timer();
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {//获取地址计时器
                Message message=new Message();
                message.what=1;
                mHandler2.sendMessage(message);
            }
        },0,1000);
    }

    private void init() {
        initCh340Driver();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openCh340();
                configCh340();
            }
        }, 2000);
        initViewPager();
        initTabLayout();
        registerClick();
        updateTime();

    }

    HashMap<String, Marker> map;


    private void getMarkerData() {
        MarkersReponse markersReponse = RobotConnectAction.init(MainActivity_Gai.this).getMarkers();
        map = markersReponse.results;
        if (map == null) {
            SDToast.showToast("map is null");
        } else {
            solveMapData(map);
        }
    }

    private void getBatter(){
        ro=RobotConnectAction.init(MainActivity_Gai.this).getPowerStatus();
        if(ro.results==null){

        }else {
            setBatter();
        }
    }
    //充电桩位置信息：（x=-22.44,y=1.47,theta=-1.47159）
    private void getPosition(){
        robotStatusReponse=RobotConnectAction.init(MainActivity_Gai.this).getRobotStatus();
        if(robotStatusReponse.results!=null){
            resultsBean=robotStatusReponse.results;
            double x=resultsBean.current_pose.x;
            double y=resultsBean.current_pose.y;
            double theta=resultsBean.current_pose.theta;
            if(x<0){
                x=-x;
            }
            if(y<0){
                y=-y;
            }
            if(theta<0){
                theta=-theta;
            }
            double error_x=x-22.44;
            double error_y=y-1.47;
            double error_theta=theta-1.47159;
            if((-0.1)<=error_x&&error_x<=0.1&&(-0.1)<=error_y&&error_y<=0.1&&(-0.1)<=error_theta&&error_theta<=0.1){
                batterimg.setImageResource(R.drawable.batter_charge);
                if (mPreviewDialog != null) {
                    mPreviewDialog.dismiss();
                    hideBottomMenu0();
                }
            }
        }
    }

    private void setBatter(){
        notice=ro.results.charger_connected_notice;
        batter=ro.results.battery_capacity;
        battertxt.setText(batter+"%");
        if (notice == true) {
            batterimg.setImageResource(R.drawable.batter_charge);
        } else {
            if (80 <= batter && batter <= 100) {
                batterimg.setImageResource(R.drawable.batter_full);
            } else if (40 <= batter && batter < 80) {
                batterimg.setImageResource(R.drawable.batter_half);
            } else if (10 <= batter && batter < 40) {
                batterimg.setImageResource(R.drawable.batter_low);
            } else {
                batterimg.setImageResource(R.drawable.batter_zero);
            }
            if(batter<=20){
                RobotConnectAction.init(MainActivity_Gai.this).sendMoveMarker(mList.get(4), new RobotMoveCallback() {
                    @Override
                    public void robotNotificationResult(NotificationResponse info) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                notice=true;
                            }
                        },5000);
                    }
                });
            }
        }
        if(batter==50&&notice==true){
            RobotConnectAction.init(MainActivity_Gai.this).sendMoveMarker(mList.get(8),new RobotMoveCallback(){
                @Override
                public void robotMoveResult(String code) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            notice=false;
                        }
                    },5000);
                }
            });
        }
    }


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){
                getBatter();
            }
        }
    };

    private Handler mHandler2=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                getPosition();
            }
        }
    };

    private void initTabLayout() {
        tabLayout.setSelectedTabIndicatorColor(MainActivity_Gai.this.getResources().getColor(R.color.main_color));
        tabLayout.setSelectedTabIndicatorHeight(6);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabTextColors(MainActivity_Gai.this.getResources().getColor(R.color.black_d), MainActivity_Gai.this.getResources().getColor(R.color.main_color));
        tabLayout.setHorizontalScrollBarEnabled(false);//添加数据之后，不会自动滑动到第一个
        tabLayout.setupWithViewPager(view_pager);
        //设置选中字体变粗
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null == view) {
                    tab.setCustomView(R.layout.custom_tab_layout_text);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTextColor(tabLayout.getTabTextColors());
                textView.setTypeface(Typeface.DEFAULT_BOLD);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null == view) {
                    tab.setCustomView(R.layout.custom_tab_layout_text);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTypeface(Typeface.DEFAULT);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initViewPager() {
        viewpagerFragmentAdapter = new MyPagerAdapter(MainActivity_Gai.this.getSupportFragmentManager(), list);
        view_pager.setAdapter(viewpagerFragmentAdapter);
        view_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void registerClick() {
        tv_open_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.configButton:
                configCh340();
                break;
            case R.id.tv_open_close:
                clickOpenOrClose();
                break;
        }
    }

    private Marker marker;

    private void clickOpenOrClose() {
        if (map == null) {
            SDToast.showToast("map is null");
            return;
        }

        marker = getMarkerFromList();


        if (songcan) {//点击关门送餐
            if (marker == null) {
                SDToast.showToast("请先选择相应的房间送餐");
                return;
            }
            if(click_flag==false){
                SDToast.showToast("请稍后");
            }else {
                songcan = false;
                tv_open_close.setText("开门");
                sendToDanPianJi("2\r\n");
                showCountDownDialog(6, marker, false);
                f=true;
            }
        } else {//点击开门后按钮名称改变为关门送餐
            if(f){
                f=false;
                songcan = true;
                click_flag=false;//不可点击
                tv_open_close.setText("关门送餐");
                sendToDanPianJi("1\r\n");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        click_flag=true;
                    }
                },4500);
            }
        }
    }

    /**
     * 开门禁操作
     */
    private synchronized void openDoor(){
        if(notice!=true){//表示没有在充电
            //打开眼睛动画
            if (eyedialog != null) {
                eyedialog.dismiss();
                hideBottomMenu0();
            }
            View alertReimderView = View.inflate(mPreviewDialog.getContext(), R.layout.eyelayout, null);
            eyedialog = new Dialog(MainActivity_Gai.this, R.style.unlogin_dialog);
            final EyeView eyeView = alertReimderView.findViewById(R.id.eyelayout_eye);
            eyeView.startTiming();
            eyedialog.setContentView(alertReimderView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            eyedialog.setCancelable(false);
            if (eyedialog != null) {
                eyedialog.show();
                hideBottomMenu0();
            }
            RobotConnectAction.init(MainActivity_Gai.this).sendMoveMarker(mList.get(0), new RobotMoveCallback() {
                @Override
                public void robotMoveResult(String code) {
                    if (code.equals(Notification.MOVE_FINISHED)) {
                        //走到了台前开始开门
                        sendToDanPianJi("3\r\n");
                        playMusic(3);
                        //回台前
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                RobotConnectAction.init(MainActivity_Gai.this).sendMoveMarker(mList.get(8), new RobotMoveCallback() {
                                    @Override
                                    public void robotMoveResult(String code) {
                                        if (code.equals(Notification.MOVE_FINISHED)) {
                                            if (eyedialog != null) {
                                                eyeView.stopTiming();
                                                eyedialog.dismiss();
                                                hideBottomMenu0();
                                            }
                                        }
                                    }
                                });
                            }
                        },2000);
                    } else if (code.equals(Notification.MOVE_FAILED)) {
                        SDToast.showToast("移动失败");
                    }
                }
            });
        }
    }



    BroadcastReceiver timeReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.ACTION_TIME_TICK.equals(intent.getAction())){
                updateTime();
            }
        }
    };

    public String updateTime() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        mill=hour*60*60*1000+minute*60*1000;
        boolean is24hFormart = true;
        if (!is24hFormart && hour >= 12) {
            hour = hour - 12;
        }

        String time = "";
        if (hour >= 10) {
            time += Integer.toString(hour);
        }
        else {
            time += "0" + Integer.toString(hour);
        }
        time += ":";

        if (minute >= 10) {
            time += Integer.toString(minute);
        }
        else {
            time += "0" + Integer.toString(minute);
        }
        timeview.setText(time);
        if(mill==36000000 +2400000 ){
            RobotConnectAction.init(MainActivity_Gai.this).sendMoveMarker(mList.get(4),new RobotMoveCallback(){
                @Override
                public void robotMoveResult(String code) {
                    notice=true;
                    batterimg.setImageResource(R.drawable.batter_charge);
                }
            });
        }

        return time;
    }

    private Dialog secondDialog;
    private TextView tv_second;


    /**
     * @param sencond
     * @param marker
     * @param isFirstPoint
     */
    private void showCountDownDialog(int sencond, final Marker marker, final boolean isFirstPoint) {
        if (secondDialog != null) {
            secondDialog.dismiss();
            hideBottomMenu0();
        }

        secondDialog = new Dialog(MainActivity_Gai.this, R.style.unlogin_dialog);
        View alertReimderView = View.inflate(MainActivity_Gai.this, R.layout.item_dialog_second, null);
        tv_second = alertReimderView.findViewById(R.id.tv_content);

        secondDialog.setContentView(alertReimderView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        secondDialog.setCancelable(false);
        if (secondDialog != null) {
            secondDialog.show();
            hideBottomMenu0();
        }


        mTimer = new CountDownTimer((long) (sencond * 1000), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!MainActivity_Gai.this.isFinishing()) {
                    remainTime = (int) (millisUntilFinished / 1000L);
                    if (isFirstPoint) {
                        tv_second.setText(remainTime + "秒后将返回");
                    } else {
                        tv_second.setText(remainTime + "秒后将开始送餐");
                    }
                }
            }

            @Override
            public void onFinish() {
                if (secondDialog != null) {
                    secondDialog.cancel();
                    secondDialog.dismiss();
                    hideBottomMenu0();
                }
                if (isFirstPoint) {
                    closeDoor = false;
                    moveToFirstMarker(marker);
                } else {
                    moveToPoint(marker);
                }
            }
        };
        mTimer.start();

    }

    private void moveToPoint(Marker marker) {
        RobotConnectAction.init(MainActivity_Gai.this).sendMoveMarker(marker, new RobotMoveCallback() {
            @Override
            public void robotMoveResult(String code) {
                if (code.equals(Notification.MOVE_START)) {
                    showTipDialog(true);
                } else if (code.equals(Notification.MOVE_FINISHED)) {
                    if (secondDialog != null) {
                        secondDialog.dismiss();
                        hideBottomMenu0();
                    }
                    playMusic(1);
                    showTipDialog(false);
                } else if (code.equals(Notification.MOVE_FAILED)) {
                    SDToast.showToast("移动失败，请再次点击关门送餐");
                }
            }
        });
    }

    private Marker getMarkerFromList() {
        for (int i = 0; i < mList.size(); i++) {
            String markName = mList.get(i).marker_name;
            for (int j = 0; j < mapList.size(); j++) {
                if (mapList.get(j).isSelect()) {
                    if (mapList.get(j).marker_name.equals(markName)) {
                        return mList.get(i);
                    }
                }
            }
        }
        return null;
    }

    private void sendToDanPianJi(String flag) {
//        byte[] to_send = Jc_Utils.toByteArray(writeText.getText().toString());		//以16进制发送
        byte[] to_send = toByteArray2(flag);        //以字符串方式发送
        int retval = MyApplication.driver.WriteData(to_send, to_send.length);//写数据，第一个参数为需要发送的字节数组，第二个参数为需要发送的字节长度，返回实际发送的字节长度
        if (retval < 0)
            SDToast.showToast("指令发送失败");
    }

    private void clickCancleMove() {
        RobotConnectAction.init(MainActivity_Gai.this).moveCancel();
    }

    private void clickDanMove() {
        MarkersReponse markersReponse = RobotConnectAction.init(MainActivity_Gai.this).getMarkers();
        HashMap<String, Marker> map = markersReponse.results;
        List<Marker> list = new ArrayList<Marker>(map.values());
        Marker mark = map.get("工位");
        SDToast.showToast("mark:" + new Gson().toJson(mark));
        RobotConnectAction.init(MainActivity_Gai.this).sendMoveMarker(mark, new RobotMoveCallback() {
            @Override
            public void robotMoveResult(String Code) {
                SDToast.showToast("robotMoveResult:" + Code);
            }
        });
    }

    private List<MarkModel> mapList;
    private List<Marker> mList;

    private void solveMapData(HashMap<String, Marker> map) {
        if (map == null)
            return;
        List<String> keyList = new ArrayList(map.keySet());
        mList = new ArrayList(map.values());

        mapList = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            int floor = mList.get(i).floor;
            String name = mList.get(i).marker_name;
            MarkModel markModel = new MarkModel();
            markModel.floor = floor;
            markModel.marker_name = name;
            mapList.add(markModel);
        }

        setViewPagerFragmentData(mapList);
        setTabLayoutData(mapList);
        refreshCurrentTabData();
    }

    private void refreshCurrentTabData() {
        ViewPagerListViewFragment viewPagerListViewFragment = ((ViewPagerListViewFragment) viewpagerFragmentAdapter.getItem(currentIndex));
        viewPagerListViewFragment.refreshData();
    }

    private void setTabLayoutData(List<MarkModel> mapList) {
        List<MarkModel> mMapList = mapList;
        List<Integer> floorList = new ArrayList<>();
        for (int i = 0; i < mMapList.size(); i++) {
            int floor = mMapList.get(i).floor;
            floorList.add(floor);
        }
        List<Integer> mFloorList = Jc_Utils.removeDuplicate(floorList);
        for (int i = 0; i < mFloorList.size(); i++) {
            tabLayout.getTabAt(i).setText("第" + mFloorList.get(i) + "层");
        }
    }

    private void setViewPagerFragmentData(List<MarkModel> mapList) {
        List<MarkModel> mMapList = mapList;
        List<Integer> floorList = new ArrayList<>();
        for (int i = 0; i < mMapList.size(); i++) {
            int floor = mMapList.get(i).floor;
            floorList.add(floor);
        }
        List<Integer> mFloorList = Jc_Utils.removeDuplicate(floorList);


        view_pager.setOffscreenPageLimit(mFloorList.size());
        list.clear();
        for (int i = 0; i < mFloorList.size(); i++) {
            ViewPagerListViewFragment viewPagerListViewFragment = new ViewPagerListViewFragment();
            viewPagerListViewFragment.setData(mFloorList.get(i), mMapList);
            list.add(viewPagerListViewFragment);
        }
        viewpagerFragmentAdapter.notifyDataSetChanged();
    }

    private void configCh340() {
        if (MyApplication.driver.mDeviceConnection == null) {
            SDToast.showToast("串口硬件配置失败");
            return;
        }
        int baudRate = 9600;
        byte stopBit = 1;
        byte dataBit = 8;
        byte parity = 0;
        byte flowControl = 0;
        if (MyApplication.driver.SetConfig(baudRate, dataBit, stopBit, parity, flowControl)) {
//            mTitle.setMiddleTextTop("配置成功");
            SDToast.showToast("串口配置成功");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getMarkerData();
                    getBatter();
                    getPosition();
                }
            }, 200);
        } else {
            SDToast.showToast("串口配置失败");
//            mTitle.setMiddleTextTop("配置失败");
        }

    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String returnStr = (String) msg.obj;
            if (TextUtils.isEmpty(returnStr))
                return;
            if (returnStr.equals("o")) {
                if (mList == null)
                    return;
                if (mList.size() <= 0)
                    return;
                openDoor();
//                showCountDownDialog(1, firstMarker, true, true);
            }
        }
    };



    private TextView dialog_custom_tv_title = null;
    private TextView tv_content = null;
    private EyeView eyeView;
    private LinearLayout ll_parent;
    boolean isQucan = false;
    boolean closeDoor = false;
    String Back_Name="前台";

    private void showTipDialog(boolean isEye) {
        if (mPreviewDialog != null) {
            mPreviewDialog.dismiss();
            hideBottomMenu0();
        }


        View alertReimderView = View.inflate(mPreviewDialog.getContext(), R.layout.item_dialog, null);
        dialog_custom_tv_title = alertReimderView.findViewById(R.id.dialog_custom_tv_title);
        eyeView = alertReimderView.findViewById(R.id.eyeview);
        ll_parent = alertReimderView.findViewById(R.id.ll_parent);
        if (eyeView.isStartBlinkAnim()) {
            eyeView.stopBlinkAnim();
        }
        if (isEye) {//显示眼睛
            eyeView.setVisibility(View.VISIBLE);
            ll_parent.setVisibility(View.GONE);
        } else {//显示开门取餐 关门送餐
            eyeView.setVisibility(View.GONE);
            ll_parent.setVisibility(View.VISIBLE);
        }
        if (marker != null) {
            dialog_custom_tv_title.setText(marker.marker_name);
        }
        tv_content = alertReimderView.findViewById(R.id.tv_content);
        tv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isQucan) {//点击关门返回后变成开门取餐
                    if(click_flag!=true){
                        SDToast.showToast("请稍后");
                    }else {
                        isQucan=false;
                        f=true;
                        tv_content.setText("开门取餐");
                        closeDoor = true;
                        sendToDanPianJi("2\r\n");
                        mPreviewDialog.dismiss();
                        hideBottomMenu0();
                        //Marker firstMarker = mList.get(8);
                        Marker firstMarker = new Marker();
                        for (Marker m :mList){
                            if(m.marker_name.equals(Back_Name)){
                                firstMarker=m;
                            }
                        }
                        showCountDownDialog(6, firstMarker, true);
                    }
                } else {//点击后变成关门返回
                    if(f==true){
                        isQucan=true;
                        f=false;
                        click_flag=false;
                        tv_content.setText("关门返回");
                        sendToDanPianJi("1\r\n");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                click_flag=true;
                            }
                        },4500);
                    }
                }
            }
        });
        mPreviewDialog.setContentView(alertReimderView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mPreviewDialog.setCancelable(false);
        if (mPreviewDialog != null) {
            eyeView.startBlinkAnim();
            mPreviewDialog.show();
            hideBottomMenu0();
        }
    }


    private void moveToFirstMarker(Marker firstMarker) {
        RobotConnectAction.init(MainActivity_Gai.this).sendMoveMarker(firstMarker, new RobotMoveCallback() {
            @Override
            public void robotMoveResult(String code) {
                if (code.equals(Notification.MOVE_START)) {
                    showTipDialog(true);
                } else if (code.equals(Notification.MOVE_FINISHED)) {
                    if (secondDialog != null) {
                        secondDialog.cancel();
                        secondDialog.dismiss();
                        hideBottomMenu0();
                    }
                    if (mPreviewDialog != null) {
                        mPreviewDialog.cancel();
                        mPreviewDialog.dismiss();
                        initAllListSelectData();
                        hideBottomMenu0();
                    }
                } else if (code.equals(Notification.MOVE_FAILED)) {
                    SDToast.showToast("移动失败");
                }
            }
        });
    }

    private void initAllListSelectData() {
        getMarkerData();
        getBatter();
        getPosition();
        view_pager.setCurrentItem(currentIndex);
    }

    private void openCh340() {
        if (!isOpen) {
            int retval = MyApplication.driver.ResumeUsbPermission();
            if (retval == 0) {
                //Resume usb device list
                retval = MyApplication.driver.ResumeUsbList();
                if (retval == -1)// ResumeUsbList方法用于枚举CH34X设备以及打开相关设备
                {
                    SDToast.showToast("Open failed!");
                    MyApplication.driver.CloseDevice();
                } else if (retval == 0) {
                    if (MyApplication.driver.mDeviceConnection != null) {
                        if (!MyApplication.driver.UartInit()) {//对串口设备进行初始化操作
                            SDToast.showToast("Initialization failed!");
                            return;
                        }
                      //  SDToast.showToast("Device opened");
                        isOpen = true;
                        new MainActivity_Gai.readThread().start();//开启读线程读取串口接收的数据
                    } else {
                        SDToast.showToast("Open failed!");
                    }
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_Gai.this);
                    builder.setIcon(R.drawable.icon);
                    builder.setTitle("未授权限");
                    builder.setMessage("确认退出吗？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    });
                    builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                        }
                    });
                    builder.show();

                }
            }
        } else {
            isOpen = false;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            MyApplication.driver.CloseDevice();
        }
    }

    public void selectMarkName(String marker_name) {
        for (int i = 0; i < list.size(); i++) {
            ViewPagerListViewFragment viewPagerListViewFragment = ((ViewPagerListViewFragment) viewpagerFragmentAdapter.getItem(i));
            viewPagerListViewFragment.initSelectStatus();
        }
        for (int i = 0; i < mapList.size(); i++) {
            mapList.get(i).setSelect(false);
            if (mapList.get(i).marker_name.equals(marker_name)) {
                mapList.get(i).setSelect(true);
            }
        }
        ViewPagerListViewFragment viewPagerListViewFragment = ((ViewPagerListViewFragment) viewpagerFragmentAdapter.getItem(currentIndex));
        viewPagerListViewFragment.dealDataToUi();
    }

    private class readThread extends Thread {

        public void run() {
            byte[] buffer = new byte[4096];
            while (true) {
                Message msg = Message.obtain();
                if (!isOpen) {
                    break;
                }
                int length = MyApplication.driver.ReadData(buffer, 4096);
                if (length > 0) {
//                    String recv = Jc_Utils.toHexString(buffer, length);		//以16进制输出
                    String recv = new String(buffer, 0, length);        //以字符串形式输出
                    msg.obj = recv;
                    handler.sendMessage(msg);
                }
            }
        }
    }

    private void initCh340Driver() {
        MyApplication.driver = new CH34xUARTDriver(
                (UsbManager) getSystemService(Context.USB_SERVICE), this,
                ACTION_USB_PERMISSION);
        if (!MyApplication.driver.UsbFeatureSupported())// 判断系统是否支持USB HOST
        {
            Dialog dialog = new AlertDialog.Builder(MainActivity_Gai.this)
                    .setTitle("提示")
                    .setMessage("您的手机不支持USB HOST，请更换其他手机再试！")
                    .setPositiveButton("确认",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    System.exit(0);
                                }
                            }).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持常亮的屏幕的状态
        writeBuffer = new byte[512];
        readBuffer = new byte[512];
        isOpen = false;
    }


    MediaPlayer mediaPlayer;

    void playMusic(int type) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
        }
        mediaPlayer.reset();
        if (type == 1) {//  -
            try {
                mediaPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.open_door));
                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (type == 0) {
            try {
                mediaPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.close_door));
                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (type == 3) {
            try {
                mediaPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.hycgy));
                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        unregisterReceiver(timeReceiver);
    }
}
