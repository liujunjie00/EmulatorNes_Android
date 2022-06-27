package com.ritchie.myapplicationmove.runtime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;

import com.ritchie.myapplicationmove.ui.NesGameWindows;
import com.ritchie.nativelib.NativeLib;

/**
 * 有一个难题就是如何把键盘的监听事件做好
 * 1.避免出现死机的情况
 * 2.单独开始一个线程去执行正常更新
 * 3.创建一个键位缓存池去保存一定时间内的所有键位
 * 4.当键盘接受输出的时候,正常更新wait
 * 5.键位线程计算上一次正常更新的时间进行等待,如何更新键位,在去判断键位缓存池里面有没有新的键位增加
 * 6.有的话就等待时间继续更新,没有就唤醒正常更新线程*/
public class GameRuntimeInfo {
    private NesGameWindows nesGameWindows;
    private NativeLib nativeLib;
    private int i = 0;
    private Context context;
    private long startTime = 0;
    private Object lock = new Object();
    private Object lock2 = new Object();
    private Object lock3 = new Object();
    int[] keys = new int[]{0,0};
    int keysint;
    int turbosint;

    /**
     * 按键监听广播
     * */
    private BroadcastReceiver broadcastReceiverEmulateDate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            keysint = bundle.getInt("key");
            putKey(keysint);


            turbosint = bundle.getInt("turbos");

            SkillsAndGamesRunning(keysint,turbosint,50);

        }
    };
    /**
     * 无参构造
     * */
    public GameRuntimeInfo() {
        nativeLib = new NativeLib();
    }
    /**
     * 有参构造
     * */
    public GameRuntimeInfo(NesGameWindows nesGameWindows, NativeLib nativeLib, Context context) {
        this.nesGameWindows = nesGameWindows;
        this.nativeLib = nativeLib;
        this.context = context;
    }
    /**
     * 开始游戏方法
     * */
    public boolean startGame() {
        startTime = System.currentTimeMillis();
        IntentFilter filter = new IntentFilter();
        filter.addAction("keyboard");
        GameAnsyTask gameAnsyTask = new GameAnsyTask();
        gameAnsyTask.execute();
        context.registerReceiver(broadcastReceiverEmulateDate, filter);
        return true;
    }
    /**
     * 所有的耗时操作都在这个类里面
     * */
    class GameAnsyTask extends AsyncTask<Integer, Integer, Boolean> {
        public GameAnsyTask() {
        }
        @Override
        protected Boolean doInBackground(Integer... integers) {
            nativeLib.setViewPortSize(1080, 945);
            screenRefreshThread.start();
            SkillsAndGamesRunning(0,-1,0);
            gameRunningThread.start();


            return null;
        }

    }
    /**
     * 获取屏幕图像刷新线程
     * */
    Thread screenRefreshThread = new Thread(new Runnable() {
        long uutime  = System.currentTimeMillis();
        @Override
        public void run() {
            try {
                Thread.sleep(1*1000);
                while (true){
                    if (System.currentTimeMillis()-uutime>=60){
                        uutime = System.currentTimeMillis();
                        //刷新屏幕
                        nesGameWindows.tick();
                    }

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });
    /**
     * 正常运行的游戏线程
     * */
    Thread gameRunningThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                SkillsAndGamesRunning(0,-1,0);

            }
        }
    });

    /**
     * 键盘刷新线程
     * */
    Thread keyboardRefreshThread = new Thread(new Runnable() {
        @Override
        public void run() {

        }
    });
    /**
     * 这个正常的技能调用流程和游戏运行的卡口
     * 需要确保底层不被执行的挂掉
     * 为了确保按键的效率,必须优先执行键盘事件
     * 按理来说这个应该可以不被多线程运行,因为他本身就是wait掉了
     * */
    public void SkillsAndGamesRunning(int keys,int turbos,int numframes){

        /*if (keys != -1){
            //虽然优先执行,也要确保多线程下不被蹦掉


        }*/
      synchronized (lock){
          if (numframes == 50){
              //获取当前时间
                  long skilltime = System.currentTimeMillis();
                  //如果开始的时间和现在的时间差没到30秒
                  long offSizeTime = skilltime - startTime;
                  if (offSizeTime <= 30){
                      try {
                          Thread.sleep(30-offSizeTime);
                          nativeLib.emulate(keys,turbos,0);
                         /* while (putKey(-1)){
                              wait(30);
                              nativeLib.emulate(keys,turbos,0);
                          }*/

                          startTime = startTime+30;
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }
                  }else {
                      nativeLib.emulate(keys,turbos,0);
                      startTime = skilltime;
                      
                  }
                  //获取他列表中现在的状态
                  boolean Allbbo = putKey(-1);
                  while (Allbbo){
                          startTime=System.currentTimeMillis();
                          while (System.currentTimeMillis()-startTime>40){
                              startTime = System.currentTimeMillis();

                              nativeLib.emulate(keysint,turbosint,0);
                              Allbbo = putKey(-1);
                              return;
                          }



                  }

            return;
          }
          long lasTime = System.currentTimeMillis();
          if(lasTime-startTime > 30 ){
              startTime = lasTime;
              nativeLib.emulate(keys,turbos,0);
              putKey(0);

          }

        }



    }
    public boolean putKey(int keyW){
        synchronized (lock2){

            if (keyW != -1 ){
                keys[0] = keys[1];
                keys[1] = keyW;
            }

            return keys[0] == keys[1];
        }
    }
}
