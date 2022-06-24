package com.ritchie.myapplicationmove;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;


import com.ritchie.myapplicationmove.Keyboard.NesKeyDate;
import com.ritchie.myapplicationmove.gameFile.GameFileInit;
import com.ritchie.myapplicationmove.runtime.GameRuntimeInfo;
import com.ritchie.myapplicationmove.ui.NesGameWindows;
import com.ritchie.nativelib.NativeLib;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "liujunjie";
    private NativeLib nativeLib;
    private boolean off = true;
    private NesGameWindows nesGameWindows;
    private String[] perms;
    private int i =0;
    private GameFileInit gameFileInit;
    private GameRuntimeInfo gameRuntimeInfo;
    private  NesKeyDate gameKey;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //检查权限
        perms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        checkPermission(perms);
        //实例化窗口
        nativeLib = new NativeLib();
        nesGameWindows = findViewById(R.id.surface_view_game);
        nesGameWindows.init();
        nesGameWindows.initBaseDir(getApplicationContext());

        //启动窗口模式
        nativeLib.start(0,102,11100);

        //初始化文件结构
        gameFileInit = new GameFileInit(nativeLib);
        gameFileInit.setFindNamePath();

        //运行模拟器
        gameRuntimeInfo = new GameRuntimeInfo(nesGameWindows,nativeLib,getApplicationContext());
        gameRuntimeInfo.startGame();

        gameKey = new NesKeyDate(nativeLib,getApplicationContext());




    }

    /**
     * 按键处理器*/
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        String keyName = "";
            gameKey.sendkey(event.getKeyCode());

        Log.d(TAG, "dispatchKeyEvent: "+keyName+"键盘码:"+event.getKeyCode());

        return true;


    }



    private void checkPermission(String[] perms) {
        if (!EasyPermissions.hasPermissions(this, perms)) {

            ActivityCompat.requestPermissions(MainActivity.this, perms, 1);
        }else {

        }
    }

}