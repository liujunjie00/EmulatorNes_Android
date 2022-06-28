package com.ritchie.myapplicationmove.runtime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.ritchie.myapplicationmove.sound.SoundMusic;
import com.ritchie.myapplicationmove.ui.NesGameWindows;
import com.ritchie.nativelib.NativeLib;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 经过我们的测试
 * 需要当个线程执行游戏的操作
 * 需要使用键盘值代入的问题
 * */
public class GameRuntimeInfo {
    private NesGameWindows nesGameWindows;
    private NativeLib nativeLib;
    private long startTime = 0;
    private Object lock = new Object();
    private AtomicInteger stKey = new AtomicInteger(0);
    private AtomicInteger stTurbo = new AtomicInteger(0);
    private SoundMusic soundMusic;
    private short[] musicBuff = new short[8192*8];
   // private short[] musicBuffCopy = new short[8192*8];
    private int musicI=0;
    private long musicTime = 0;

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
    }
    /**
     * 开始游戏方法
     * */
    public boolean startGame() {
        startTime = System.currentTimeMillis();
        GameAnsyTask gameAnsyTask = new GameAnsyTask();
        soundMusic = new SoundMusic();
        soundMusic.initSound();
        gameAnsyTask.execute();
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
                    if (System.currentTimeMillis()-uutime>=20){
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
     * 音乐刷新线程
     * */
    private void readSfxBuffer(){

        soundMusic.play();
        if (System.currentTimeMillis() - musicTime >= 80){
            musicTime = System.currentTimeMillis();
            boolean non = true;
            int slen = nativeLib.readSfxBuffer(musicBuff);
            for (int k = 0; k < 10; k++) {
                non = musicBuff[k]==musicBuff[k+1];
            }
            if (non) return;
            //System.arraycopy(musicBuff,0,musicBuffCopy,0,slen);
            Log.d("liujunjie", "run:slen "+slen);
            soundMusic.starMusic(musicBuff,null,slen);
        }

    }

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

    public void setStKey(int stKey,int trubo) {
        //将两个值相加到一起
        int intKry = this.stKey.get() | stKey;
        this.stKey.set(intKry);
        int intTurbo = stTurbo.get() | stKey;
        stTurbo.set(intTurbo);

    }
    public void resetStKey(int keycode,int turbo) {

        this.stKey.set(stKey.get() & (~keycode));
        stTurbo.set(stKey.get() & (~ turbo));
    }
    public void resetStTurbo() {
        this.stTurbo.set(-1);
    }

    /**
     *这个就是主要的游戏刷新方法,包括游戏的正常运行的状态
     * */
    public void SkillsAndGamesRunning(int keys,int turbos,int numframes){

        synchronized (lock){
            long tiii = System.currentTimeMillis();
            if (tiii- startTime >=20 ){
                startTime =tiii;
                Log.d("SkillsAndGamesRunning", "SkillsAndGamesRunning: " + stKey.get());
                nativeLib.emulate(stKey.get(),-(stTurbo.get()),0);
                musicI++;
                if (musicI == 2){
                    musicI=0;
                    readSfxBuffer();
                }
            }
        }

    }

}
