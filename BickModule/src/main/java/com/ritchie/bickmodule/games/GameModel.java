package com.ritchie.bickmodule.games;

import static android.content.Context.WINDOW_SERVICE;


import static com.ritchie.bickmodule.window.FloatingWindow.bikeData;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ritchie.bickmodule.R;
import com.ritchie.bickmodule.features.BaseCharacter;
import com.ritchie.bickmodule.features.MaliaoFeature;
import com.ritchie.bickmodule.features.SkillData;
import com.ritchie.bickmodule.features.TankeFeature;
import com.ritchie.bickmodule.proFile.Bike;
import com.ritchie.bickmodule.proFile.BikeData1;
import com.ritchie.bickmodule.proFile.ListParcelable;
import com.ritchie.bickmodule.service.RuningService;
import com.ritchie.bickmodule.service.SearchMapsServices;
import com.ritchie.bickmodule.ui.DashboardView4;
import com.ritchie.bickmodule.ui.VerticalProgress;
import com.ritchie.bickmodule.util.MapsTools;
import com.ritchie.bickmodule.util.SystemUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 这个一个游戏配置类
 * */
public class GameModel implements View.OnClickListener{
    private List<Map<String, Object>> deviceShow;
    private WindowManager windowManager,windowManager2;
    private WindowManager.LayoutParams layoutParams;
    private  DisplayMetrics displayMetrics;
    private View view,SpeedShowView;
    private Button button1,button2,button3,button4,button5,textView3;
    private ListView listView1;
    private TextView textViewMain,textView2;
    private VerticalProgress verticalProgress;
    private EditText editText3,editText4;
    private Random random;
    private Handler handler;
    private String gameName;
    private BaseCharacter baseCharacter;            // 这个表示坦克大战的字符串
    private SkillData[] skillDataList;         // 这个是技能数组
    private Context context;
    private Integer pid = null ;
    private List<String[]> test66s = null;
    private List<String[]> list = null;
    private SimpleAdapter simpleAdapter;
    private String[] onClickMapsItem;
    private String onClickStar;
    private String onClickEnd;
    private boolean superKey = false;
    private long lenForI;
    private MaliaoFeature maliaoFeature;

    private long head;
    private TankeFeature tankeFeature;
    private boolean starGame = false;
    private int test;
    private int countCad = 0;
    private int countCad1 = 0;
    private Handler handlerSearchMaps;
    private int[] countCadStack ={0,0,0};
    private DashboardView4 dashboardView4;
    private boolean isAnimFinished = true;


    /**
     * 这是一个广播接收器，接受处理maps的数据
     * “searcherMaps”
     * */
    private BroadcastReceiver broadcastReceiverMaps = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getExtras();
            int id = bundle.getInt("pid");
            if (id != -1) {
                ListParcelable listPar = bundle.getParcelable("list");
                ListParcelable test66sPar = bundle.getParcelable("test66s");
                pid = id;
                list = listPar.getList();
                test66s = test66sPar.getList();

                refreshListView();

            }else {

                textViewMain.setText("没有找到test66");
            }
        }
    };
    /**
     * 刷新listView布局
     * */
    private void refreshListView(){
        deviceShow = new ArrayList<>();
        for (int i = 0; i < test66s.size(); i++) {
            HashMap<String, Object> hashMap = new HashMap<String,Object>();
            hashMap.put("star", test66s.get(i)[0]);
            hashMap.put("end", test66s.get(i)[1]);
            deviceShow.add(hashMap);
        }
        textViewMain.setText("执行完成");
        listView1.setVisibility(View.VISIBLE);
        simpleAdapter = new SimpleAdapter(context, deviceShow, R.layout.list_view_item_maps, new String[]{"star", "end"}, new int[]{R.id.star_addr, R.id.end_addr});
        listView1.setAdapter(simpleAdapter);
        listView1.setOnItemClickListener(new GameModel.OnItemClick());
    }
    /**
     * 蓝牙数据的广播接收机
     * */
    private BroadcastReceiver broadcastReceiverBle = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            int state = bundle.getInt("state");
            if (state != -1) {
                BikeData1 bikeData1p = bundle.getParcelable("data");
                bikeData = bikeData1p;

                Log.d("888888", "onReceive: "+isAnimFinished);
                if (isAnimFinished){
                    @SuppressLint("ObjectAnimatorBinding") ObjectAnimator animator = ObjectAnimator.ofInt(dashboardView4, "mRealTimeValue",
                            dashboardView4.getVelocity(),bikeData1p.getInstantaneousCadence());
                    animator.setDuration(400).setInterpolator(new LinearInterpolator());
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            isAnimFinished = false;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isAnimFinished = true;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            isAnimFinished = true;
                        }
                    });
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int value = (int) animation.getAnimatedValue();
                            dashboardView4.setVelocity(value);
                        }
                    });
                    animator.start();
                }
                handler.post(tankeFastRunable);

            }else {
                textViewMain.setText("没有找到单车");
            }

        }
    };
    /**
     * maps广播处理的动态注册
     * */
    public void initBroadcastReceiverMaps(){
        IntentFilter filter = new IntentFilter();
        filter.addAction("searcherMaps");
        context.registerReceiver(broadcastReceiverMaps,filter);
    }
    /**
     * 需要弄一个蓝牙的动态注册接受*/
    public void initBroadcastReceiverBle(){
        IntentFilter filter = new IntentFilter();
        filter.addAction("FindBluetoothDevices");
        context.registerReceiver(broadcastReceiverBle,filter);
    }

    public void initWindow(){
        initBroadcastReceiverMaps();
        initBroadcastReceiverBle();
        if (Settings.canDrawOverlays(context)) {
            windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
            layoutParams = new WindowManager.LayoutParams();
            displayMetrics = context.getResources().getDisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            layoutParams.format = PixelFormat.RGB_565;
            layoutParams.alpha = 0.8f;
            layoutParams.width = 330;
            layoutParams.height = 600;
            layoutParams.gravity = Gravity.LEFT;
            layoutParams.x = 10;
            layoutParams.y = 10;
            view = LayoutInflater.from(context).inflate(R.layout.floating_window_layout, null);
            windowManager.addView(view, layoutParams);
            button1 = view.findViewById(R.id.button1);
            button2 = view.findViewById(R.id.button2);
            button3 = view.findViewById(R.id.button3);
            listView1 = view.findViewById(R.id.list_view1);
            textViewMain = view.findViewById(R.id.text_view_main);
            verticalProgress = view.findViewById(R.id.vp_progress);
            button4 = view.findViewById(R.id.button4);
            editText3 = view.findViewById(R.id.edit_text3);
            editText4 = view.findViewById(R.id.edit_text4);
            button5 = view.findViewById(R.id.button5);
            textView2 = view.findViewById(R.id.test_view2);
            textView3 = view.findViewById(R.id.test_view3);

            random = new Random();
            handler = new Handler(Looper.myLooper());
        }
    }

    @SuppressLint("RtlHardcoded")
    public void speedShow(){
        windowManager2 = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParamsSpeedShow = new WindowManager.LayoutParams();
        DisplayMetrics displayMetricsSpeedShow = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetricsSpeedShow);
        layoutParamsSpeedShow.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        layoutParamsSpeedShow.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParamsSpeedShow.format = PixelFormat.RGB_565;
        layoutParamsSpeedShow.alpha = 0.8f;
        layoutParamsSpeedShow.width = 330;
        layoutParamsSpeedShow.height = 330;
        layoutParamsSpeedShow.gravity = Gravity.RIGHT;
        layoutParamsSpeedShow.x = 0;
        layoutParamsSpeedShow.y = 0;
        SpeedShowView = LayoutInflater.from(context).inflate(R.layout.board_view, null);
        dashboardView4 = SpeedShowView.findViewById(R.id.board_view_progress);
        windowManager2.addView(SpeedShowView,layoutParamsSpeedShow);
        SpeedShowView.setOnTouchListener(new View.OnTouchListener() {
            final WindowManager.LayoutParams floatWindowLayoutUpdateParamww = layoutParamsSpeedShow;
            double x;
            double y;
            double px;
            double py;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = floatWindowLayoutUpdateParamww.x;
                        y = floatWindowLayoutUpdateParamww.y;
                        px = event.getRawX();
                        py = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 拿到 视图的现在的显示位置  + （动画移动的位置（坐标） - 第一次触摸的位置（坐标））
                        floatWindowLayoutUpdateParamww.x = (int) (x + (px - event.getRawX()));
                        floatWindowLayoutUpdateParamww.y = (int) ((y + event.getRawY()) - py);
                        windowManager2.updateViewLayout(SpeedShowView, floatWindowLayoutUpdateParamww);
                        break;
                }
                return false;
            }
        });
    }
    /**
     * 注册所有的点击事件*/
    public void setAllViewOnClick() {
        button1.setOnClickListener(this::onClick);
        button2.setOnClickListener(this::onClick);
        button3.setOnClickListener(this::onClick);
        button4.setOnClickListener(this::onClick);
        button5.setOnClickListener(this::onClick);
        view.setOnClickListener(this::onClick);
        textView3.setOnClickListener(this::onClick);
        view.setOnTouchListener(new View.OnTouchListener() {
            final WindowManager.LayoutParams floatWindowLayoutUpdateParam = layoutParams;
            double x;
            double y;
            double px;
            double py;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = floatWindowLayoutUpdateParam.x;
                        y = floatWindowLayoutUpdateParam.y;
                        px = event.getRawX();
                        py = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        floatWindowLayoutUpdateParam.x = (int) ((x + event.getRawX()) - px);
                        floatWindowLayoutUpdateParam.y = (int) ((y + event.getRawY()) - py);
                        windowManager.updateViewLayout(view, floatWindowLayoutUpdateParam);
                        break;
                }
                return false;
            }
        });
    }
    /**
     * 实现所有的点击事件
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                button1EventService2();
                break;
            case R.id.button2:
                button2Evemt();
                break;
            case R.id.button3:
                button3Event();
                break;
            case R.id.button4:
                button4Event();
                break;
            case R.id.button5:
                button5Event();
                break;
            case R.id.test_view3:
                shoutdown();
                break;
        }
        updateViewLayoutForKey(false);


    }

    private void shoutdown() {
        int mypid = android.os.Process.myPid();
        SystemUtil.KillPid(mypid);
    }

    /**
     * 这个是一个键盘切换处理器
     */
    public void updateViewLayoutForKey(boolean trueOrFalse) {
        if (trueOrFalse == superKey) return;
        if (trueOrFalse) {
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        } else {
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        }
        windowManager.updateViewLayout(view, layoutParams);
        superKey = trueOrFalse;
    }




    private void button5Event() {
       int[] pids =  MapsTools.getPids("com.ritchie.myapplicationmove");
       if (pids == null){
           return;
       }
        for (int i = 0; i < pids.length; i++) {
            SystemUtil.KillPid(pids[i]);
        }

    }

    private void button4Event() {
        String ss = SystemUtil.execShellCmd("su root echo 9999 > /sdcard/Download/ccc.txt");
        try {
            Runtime.getRuntime().exec("su root echo 9696969 > /sdcard/Download/ccc.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void button3Event() {
    }
    /**
     * 开始游戏按钮
     * */
    private void button2Evemt() {
            tankeFeature.init(head);
            Intent intent2 = new Intent(context, RuningService.class);
            context.startService(intent2);
            textViewMain.setText("正在寻找单车");
            if (windowManager2 == null){
                speedShow();
            }

    }
    /*private void button2Evemt() {
        if (!starGame){
            tankeFeature.init(head);
            Intent intent2 = new Intent(context, RuningService.class);
            context.startService(intent2);
            textViewMain.setText("正在寻找单车");
            starGame=true;

        }
    }*/
    //开始扫描maps ，这里需要对新建的参数进行判断
    private void button1Event() {
        textViewMain.setText("正在查找");
        // 开辟一条线程
        if (deviceShow != null) {
            deviceShow.clear();
            listView1.clearAnimation();
        }
        if (simpleAdapter != null) {

            simpleAdapter = null;
        }
        listView1.setVisibility(View.GONE);

        handlerSearchMaps = new Handler();
        /**
         * 就是这里在加载视图的时候导致线程堵塞，应该要考虑拉起一个服务去执行加载动作
         * */
        final boolean post = handlerSearchMaps.post(new Runnable() {
            @Override
            public void run() {
                textViewMain.setText("正在读取maps");
                try {
                    pid = MapsTools.getPid("com.ritchie.myapplicationmove");
                    list = MapsTools.getStartAndEnd(pid);
                    test66s = MapsTools.getTest66(pid, list, baseCharacter);
                    deviceShow = new ArrayList<>();
                    for (int i = 0; i < test66s.size(); i++) {
                        HashMap<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("star", test66s.get(i)[0]);
                        hashMap.put("end", test66s.get(i)[1]);
                        deviceShow.add(hashMap);
                    }
                    textViewMain.setText("执行完成");
                    listView1.setVisibility(View.VISIBLE);
                    simpleAdapter = new SimpleAdapter(context, deviceShow, R.layout.list_view_item_maps, new String[]{"star", "end"}, new int[]{R.id.star_addr, R.id.end_addr});
                    listView1.setAdapter(simpleAdapter);

                } catch (RuntimeException e) {
                    if (list != null) {
                        textViewMain.setText("找到符号规则的maps的条数 " + list.size() + e.getMessage());
                    } else {
                        textViewMain.setText(e.getMessage());
                    }

                }

            }
        });
        listView1.setOnItemClickListener(new GameModel.OnItemClick());
    }


    private void button1EventService() {
        handlerSearchMaps = new Handler();
        textViewMain.setText("正在查找");
        if (deviceShow != null) {
            deviceShow.clear();
            listView1.clearAnimation();
        }
        if (simpleAdapter != null) {

            simpleAdapter = null;
        }
        listView1.setVisibility(View.GONE);

        Intent intent3 = new Intent(context, SearchMapsServices.class);
        context.startService(intent3);


        handlerSearchMaps.postDelayed(new Runnable() {
            int timeoutSearch = 0;
            @Override
            public void run() {
                if (test66s != null){
                    deviceShow = new ArrayList<>();
                    for (int i = 0; i < test66s.size(); i++) {
                        HashMap<String, Object> hashMap = new HashMap();
                        hashMap.put("star", test66s.get(i)[0]);
                        hashMap.put("end", test66s.get(i)[1]);
                        deviceShow.add(hashMap);
                    }
                    textViewMain.setText("执行完成");
                    listView1.setVisibility(View.VISIBLE);
                    simpleAdapter = new SimpleAdapter(context, deviceShow, R.layout.list_view_item_maps, new String[]{"star", "end"}, new int[]{R.id.star_addr, R.id.end_addr});
                    listView1.setAdapter(simpleAdapter);
                }else {
                    timeoutSearch++;
                    if (timeoutSearch>5){
                        textViewMain.setText("耐心等待一会");
                    }
                    if (timeoutSearch>15){
                        textViewMain.setText("已经超时");
                    }else {
                        handlerSearchMaps.postDelayed(this::run,1*1000);
                    }
                }
            }
        },5*1000);
        listView1.setOnItemClickListener(new GameModel.OnItemClick());
    }


    /**
     * 新开一个线程去查找map
     * */
    private void button1EventService2() {
        handlerSearchMaps = new Handler();
        textViewMain.setText("正在查找");
        if (deviceShow != null) {
            deviceShow.clear();
            listView1.clearAnimation();
        }
        if (simpleAdapter != null) {

            simpleAdapter = null;
        }
        listView1.setVisibility(View.GONE);
        handlerSearchMaps.post(new Runnable() {
            @Override
            public void run() {
                Intent intent3 = new Intent(context, SearchMapsServices.class);
                context.startService(intent3);
            }
        });
    }
    /**
     * listview 的点击事件类
     * */
    class OnItemClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            TextView textViewStar = view.findViewById(R.id.star_addr);
            String starAddr = textViewStar.getText().toString();
            TextView textViewEnd = view.findViewById(R.id.end_addr);
            String endAddr = textViewEnd.getText().toString();
            textViewMain.setText("请判断");
            onClickMapsItem = new String[]{starAddr, endAddr};
            long start = Long.parseLong(test66s.get(i)[0],16);
            long sizeOff = Long.parseLong(test66s.get(i)[2]);
            head = start+sizeOff-tankeFeature.getBaseCharacter().getIndex(); //这个是绝对的不变的字符的数
            long addrLong = head+tankeFeature.getSkillData()[test].getOffSize();
            String addr1 = String.format("%016x",addrLong);
            MapsTools.fastWrite1(pid,"0x"+addr1,tankeFeature.getSkillData()[test].getDefaultValue());
            MapsTools.fastWrite1(pid,"0x"+addr1,tankeFeature.getSkillData()[test].getValue());

            if (starAddr != null) {
                onClickStar = starAddr;
            }
            if (endAddr != null) {

                onClickEnd = endAddr;
            }
            textViewMain.setText(" 0x" + onClickStar + " 0x" + onClickEnd);
        }
    }
    /**
     * 这个是马里奥的线程监听器*/
    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            if (bikeData != null && bikeData instanceof BikeData1) {
                BikeData1 bikeData1 = (BikeData1) bikeData;
                int Cadence = bikeData1.getInstantaneousCadence();
                Log.d("liujunjie", "runnable1线程正在刷新数据: " + Cadence);
                int countKM = bikeData1.getTotalDistancePresentUint();
                if (verticalProgress.getmProgress() > (countKM % 100)) {
                    vertical(countKM, true);
                } else {
                    vertical(countKM, false);
                }
                textViewMain.setText("当前踏频:" + Cadence);
            }
            handler.postDelayed(runnable1, 1 * 1000);

        }
        public void vertical(int countKM, boolean iso) {
            int variable = countKM % 100; //这个是需要添加到的值
            if (iso) {
                for (int k = verticalProgress.getmProgress(); k <100 ; k++) {
                    verticalProgress.setProgress(k);
                }
                for (int i = 0; i < variable; i++) {
                    verticalProgress.setProgress(i);
                }
                //刷新技能
                goldenBody(00);
            } else {
                int star = verticalProgress.getmProgress();
                for (int j = 0; j < variable - star; j++) {
                    verticalProgress.setProgress(star + j);
                }
            }
        }
    };

    int timer = 0;
    /**
     * 这个值是三秒刷新一次
     * 这个数据处理需要处理脏数据的需求，就是需要
     * 第一种方法就是拿出五个值 去掉最大和最小的的两个数值 再拿三个进行判断  优点：比较稳 ，但是延迟比较高 ，
     * 第二种方法 拿三个值
     * */
    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
                BikeData1 bikeData1 = (BikeData1) bikeData;
                int Cadence = bikeData1.getInstantaneousCadence();
                int countKM = bikeData1.getTotalDistancePresentUint();
                if (countCad1 < 3) {
                    countCad += Cadence;
                    countCad1++;
                    if (countCad1 == 3) {
                        if (countCad < 25 * 3) {
                            SkillData[] skillData = tankeFeature.getSkillData();
                            long addr = skillData[0].getPhysicalAddress();
                            String ssss = String.format("%016x",addr);
                            String addrdd = "0x" + ssss;
                            MapsTools.fastWrite1(pid, addrdd, 0x00);
                            textView2.setText("正常模式");
                        }
                        if (countCad > 25 * 3 && countCad < 50 *3) {
                            SkillData[] skillData = tankeFeature.getSkillData();
                            long addr = skillData[0].getPhysicalAddress();
                            String ssss = String.format("%016x",addr);
                            String addrdd = "0x" + ssss;
                            MapsTools.fastWrite1(pid, addrdd, 0x20);
                            textView2.setText("模式1");
                        }

                        if (countCad > 50 * 3 && countCad <75 * 3 ) {
                            SkillData[] skillData = tankeFeature.getSkillData();
                            long addr = skillData[0].getPhysicalAddress();
                            String ssss = String.format("%016x",addr);
                            String addrdd = "0x" + ssss;
                            MapsTools.fastWrite1(pid, addrdd, 0x40);
                            textView2.setText("模式2");
                        }
                        if (countCad > 75 * 3 && countCad <80 * 3) {
                            SkillData[] skillData = tankeFeature.getSkillData();
                            long addr = skillData[0].getPhysicalAddress();
                            String ssss = String.format("%016x",addr);
                            String addrdd = "0x" + ssss;
                            MapsTools.fastWrite1(pid, addrdd, 0x60);
                            textView2.setText("模式3");
                        }
                        if (countCad > 80 * 3) {
                            SkillData[] skillData = tankeFeature.getSkillData();
                            long addr = skillData[4].getPhysicalAddress();
                            String ssss = String.format("%016x",addr);
                            String addrdd = "0x" + ssss;
                            MapsTools.fastWrite1(pid, addrdd, 0x03);
                            textView2.setText("敌方静止不动");
                        }
                    }
                } else {
                    countCad = 0;
                    countCad1 = 0;
                }
                double ss = 100/60;
                int speed = (int) (ss*Cadence);
                verticalProgress.setProgress(speed);
                textViewMain.setText("现在的踏频是："+Cadence);
            }

    };
    /**
     * 要写一个一秒刷新的线程
     * 搞一个数据池，一共三个元素，先进后出，递归
     * 从第4个开始砍掉前面1个
     * 然后进行判断
     * 之后的每一个都开始判断
     */
    final Runnable tankeFastRunable = new Runnable() {
        @Override
        public void run() {
            BikeData1 bikeData1 = (BikeData1) bikeData;
            int Cadence = bikeData1.getInstantaneousCadence();

            if ((Cadence - countCadStack[2]) + (Cadence - countCadStack[1]) + (Cadence - countCadStack[0]) < -60) {
                SkillData[] skillData = tankeFeature.getSkillData();
                long addr = skillData[0].getPhysicalAddress();
                String ssss = String.format("%016x", addr);
                String addrdd = "0x" + ssss;
                MapsTools.fastWrite1(pid, addrdd, 0x00);
                textView2.setText("正常模式");
                countCadStack[0] = countCadStack[1];
                countCadStack[1] = countCadStack[2];
                countCadStack[2] = Cadence;
                int countKM = bikeData1.getTotalDistancePresentUint();
                //textView3.setText(countKM);
                double ss = 100 / 60;
                int speed = (int) (ss * Cadence);
                verticalProgress.setProgress(speed);
                textViewMain.setText("现在的踏频是：" + Cadence);
                return;
            }
            countCadStack[0] = countCadStack[1];
            countCadStack[1] = countCadStack[2];
            countCadStack[2] = Cadence;
            for (int k = 0; k < countCadStack.length; k++) {
                countCad += countCadStack[k];
            }
            if (countCad < 30 * 3) {
                SkillData[] skillData = tankeFeature.getSkillData();
                long addr = skillData[0].getPhysicalAddress();
                String ssss = String.format("%016x", addr);
                String addrdd = "0x" + ssss;
                MapsTools.fastWrite1(pid, addrdd, 0x00);
                textView2.setText("正常模式");
            }
            if (countCad > 30 * 3 && countCad < 60 * 3) {
                SkillData[] skillData = tankeFeature.getSkillData();
                long addr = skillData[0].getPhysicalAddress();
                String ssss = String.format("%016x", addr);
                String addrdd = "0x" + ssss;
                MapsTools.fastWrite1(pid, addrdd, 0x20);
                textView2.setText("模式1");
            }

            if (countCad > 60 * 3 && countCad < 90 * 3) {
                SkillData[] skillData = tankeFeature.getSkillData();
                long addr = skillData[0].getPhysicalAddress();
                String ssss = String.format("%016x", addr);
                String addrdd = "0x" + ssss;
                MapsTools.fastWrite1(pid, addrdd, 0x40);
                textView2.setText("模式2");
            }
            if (countCad > 90 * 3) {
                SkillData[] skillData = tankeFeature.getSkillData();
                long addr = skillData[0].getPhysicalAddress();
                String ssss = String.format("%016x", addr);
                String addrdd = "0x" + ssss;
                MapsTools.fastWrite1(pid, addrdd, 0x60);
                textView2.setText("模式3");
            }
            if (countCad > 100 * 3) {
                SkillData[] skillData = tankeFeature.getSkillData();
                long addr = skillData[5].getPhysicalAddress();
                String ssss = String.format("%016x", addr);
                String addrdd = "0x" + ssss;
                MapsTools.fastWrite1(pid, addrdd, 0x2);
                textView2.setText("金身");
            }
            countCad = 0;
            int countKM = bikeData1.getTotalDistancePresentUint();
            //textView3.setText(countKM);
            double ss = 100 / 60;
            int speed = (int) (ss * Cadence);
            verticalProgress.setProgress(speed);
            textViewMain.setText("现在的踏频是：" + Cadence);
            //这里更新指针的弹跳速度
                /*handler.post(new Runnable() {

                    @Override
                    public void run() {
                        int dateForDashboardView4 = dashboardView4.getVelocity();

                        if ( dateForDashboardView4 == 0 ){
                            dashboardView4.setVelocity(Cadence);
                        }else {
                            int differenceData = (Cadence>dateForDashboardView4) ? Cadence-dateForDashboardView4 : dateForDashboardView4-Cadence;
                            if (dateForDashboardView4 > Cadence){
                                float time = 500f/(float)differenceData;
                                for (int T = 0; T < differenceData; T++) {
                                    dashboardView4.setVelocity(dateForDashboardView4--);
                                    try {
                                        Thread.sleep((long) time);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }else {
                                float time = 500f/(float)differenceData;
                                for (int K = 0; K < differenceData; K++) {
                                    dashboardView4.setVelocity(dateForDashboardView4++);
                                    try {
                                        Thread.sleep((long) time);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                }


                            }

                        }

                    }
                });*/


        }
    };

    /**
     * 这个方法是测试用到的，过时了*/
    @Deprecated
    public void goldenBody() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int ppp1 = random.nextInt(tankeFeature.getSkillData().length);
                SkillData skillData = tankeFeature.getSkillData()[ppp1];
                long addr = skillData.getPhysicalAddress();
                String addr1 = String.format("%016x",addr);
                MapsTools.fastWrite1(pid,addr1 , skillData.getValue());
                Log.d("技能", "goldenBody:新增 "+skillData.getName());
                textView2.setText("获得：" + skillData.getName() + "技能10秒");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MapsTools.fastWrite1(pid, addr1, skillData.getDefaultValue());
                        Log.d("技能", "run: 消失"+skillData.getName());
                        textView2.setText("技能消失");
                    }
                }, 5 * 1000);
                handler.postDelayed(this,6*1000);
            }

        },10*1000);

    }
    public void goldenBody(int ppp) {
        int ppp1 = random.nextInt(tankeFeature.getSkillData().length);
        SkillData skillData = tankeFeature.getSkillData()[ppp1];
        long addr = skillData.getPhysicalAddress();
        String addr1 = String.format("%016x",addr);
        MapsTools.fastWrite1(pid,addr1 , skillData.getValue());
        Log.d("技能", "goldenBody:新增 "+skillData.getName());
        textView2.setText("获得：" + skillData.getName() + "技能10秒");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MapsTools.fastWrite1(pid, addr1, skillData.getDefaultValue());
                Log.d("技能", "run: 消失"+skillData.getName());
                textView2.setText("技能消失");
            }
        }, 10 * 1000);
    }

    public static void upData(Bike c) {
        bikeData = c;
    }


    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public BaseCharacter getBaseCharacter() {
        return baseCharacter;
    }

    public void setBaseCharacter(BaseCharacter baseCharacter) {
        this.baseCharacter = baseCharacter;
    }

    public SkillData[] getSkillDataList() {
        return skillDataList;
    }

    public void setSkillDataList(SkillData[] skillDataList) {
        this.skillDataList = skillDataList;
    }


    public GameModel(String gameName, BaseCharacter baseCharacter, SkillData[] skillDataList,Context context) {
        this.gameName = gameName;
        this.baseCharacter = baseCharacter;
        this.skillDataList = skillDataList;
        this.context = context;

    }
    public GameModel(TankeFeature tankeFeature,Context context,int test){

        this.tankeFeature = tankeFeature;
        this.context = context;
        this.test = test;

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }



}

