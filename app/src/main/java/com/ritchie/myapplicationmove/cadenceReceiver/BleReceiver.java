package com.ritchie.myapplicationmove.cadenceReceiver;

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
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.ritchie.bickmodule.features.SkillData;
import com.ritchie.bickmodule.features.TankeFeature;
import com.ritchie.bickmodule.proFile.BikeData1;
import com.ritchie.bickmodule.service.RuningService;
import com.ritchie.bickmodule.ui.DashboardView4;
import com.ritchie.bickmodule.util.MapsTools;
import com.ritchie.myapplicationmove.util.GameRomAddr;
import com.ritchie.nativelib.NativeLib;

public class BleReceiver {
    private boolean isAnimFinished = true;
    private DashboardView4 dashboardView4;
    private TextView textViewMain;
    private Handler handler;
    private NativeLib nativeLib;
    private int[] countCadStack={0,0,0};
    private int countCad = 0;
    private TankeFeature tankeFeature;
    // 接受到蓝牙发送的数据
    private BroadcastReceiver bike = new BroadcastReceiver() {
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
                // 后台运行别的线程
                handler.post(tankeFastRunable);

            }else {
                textViewMain.setText("没有找到单车");
            }
        }
    };
    public BleReceiver() {
    }

    public BleReceiver(DashboardView4 dashboardView4, TextView textViewMain, NativeLib nativeLib, TankeFeature tankeFeature) {
        this.dashboardView4 = dashboardView4;
        this.textViewMain = textViewMain;
        this.nativeLib = nativeLib;
        this.tankeFeature = tankeFeature;
    }

    public void ConnectTheDevice(Context context){
        //启动单车服务
        Intent intent2 = new Intent(context, RuningService.class);
        context.startService(intent2);
        // 注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("FindBluetoothDevices");
        context.registerReceiver(bike,filter);
        //
        handler = new Handler();

        // 初始化
        GameRomAddr gameRomAddr = new GameRomAddr(nativeLib);
        tankeFeature.init(gameRomAddr.getRomAddr());

    }



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
            SkillData[] skillData = tankeFeature.getSkillData();
            BikeData1 bikeData1 = (BikeData1) bikeData;
            int Cadence = bikeData1.getInstantaneousCadence();

            if ((Cadence - countCadStack[2]) + (Cadence - countCadStack[1]) + (Cadence - countCadStack[0]) < -60) {

                long addr = skillData[0].getPhysicalAddress();
                nativeLib.setAddrValue(addr, 0x00);
                countCadStack[0] = countCadStack[1];
                countCadStack[1] = countCadStack[2];
                countCadStack[2] = Cadence;
                int countKM = bikeData1.getTotalDistancePresentUint();

                double ss = 100 / 60;
                int speed = (int) (ss * Cadence);

                textViewMain.setText("现在的踏频是：" + Cadence);
                return;
            }
            countCadStack[0] = countCadStack[1];
            countCadStack[1] = countCadStack[2];
            countCadStack[2] = Cadence;

            // 把三个数相加在一起
            for (int k = 0; k < countCadStack.length; k++) {
                countCad += countCadStack[k];
            }
            if (countCad < 30 * 3) {

                long addr = skillData[0].getPhysicalAddress();
                nativeLib.setAddrValue(addr, 0x00);


            }
            if (countCad > 30 * 3 && countCad < 60 * 3) {

                long addr = skillData[0].getPhysicalAddress();
                nativeLib.setAddrValue(addr, 0x20);


            }

            if (countCad > 60 * 3 && countCad < 90 * 3) {

                long addr = skillData[0].getPhysicalAddress();
                nativeLib.setAddrValue(addr, 0x40);


            }
            if (countCad > 90 * 3) {

                long addr = skillData[0].getPhysicalAddress();
                nativeLib.setAddrValue(addr, 0x60);

            }
            if (countCad > 100 * 3) {

                long addr = skillData[5].getPhysicalAddress();
                nativeLib.setAddrValue(addr, 0x2);;

            }
            countCad = 0;
            int countKM = bikeData1.getTotalDistancePresentUint();
            double ss = 100 / 60;
            int speed = (int) (ss * Cadence);

            textViewMain.setText("现在的踏频是：" + Cadence);

        }
    };
}
