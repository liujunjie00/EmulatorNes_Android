package com.ritchie.myapplicationmove.runtime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;

import com.ritchie.myapplicationmove.ui.NesGameWindows;
import com.ritchie.nativelib.NativeLib;


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

    public GameRuntimeInfo() {
    }

    public GameRuntimeInfo(NesGameWindows nesGameWindows, NativeLib nativeLib, Context context) {
        this.nesGameWindows = nesGameWindows;
        this.nativeLib = nativeLib;
        this.context = context;
    }

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
     * 屏幕刷新函数*/
    class GameAnsyTask extends AsyncTask<Integer, Integer, Boolean> {
        Thread thread = new Thread(new Runnable() {
            long uutime  = System.currentTimeMillis();
            @Override
            public void run() {
                try {
                    Thread.sleep(1*1000);
                    while (true){
                        if (System.currentTimeMillis()-uutime>=60){
                            uutime = System.currentTimeMillis();
                            nesGameWindows.tick();
                        }

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        public GameAnsyTask() {
        }
        @Override
        protected Boolean doInBackground(Integer... integers) {
            nativeLib.setViewPortSize(1080, 945);
            thread.start();
            SkillsAndGamesRunning(0,-1,0);

            /**
             * 这个是主函数处理和技能处理模块
             * */
            while (true) {
               SkillsAndGamesRunning(0,-1,0);

            }
        }

    }
    public void SkillsAndGamesRunning(int keys,int turbos,int numframes){

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
