package com.ritchie.myapplicationmove;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;
import android.util.Log;

import android.view.KeyEvent;
import android.view.WindowManager;


import com.ritchie.bickmodule.service.ReadandwriteService;
import com.ritchie.myapplicationmove.Keyboard.MyKeyBoardProFile;
import com.ritchie.myapplicationmove.Keyboard.NesKeyDate;
import com.ritchie.myapplicationmove.gameFile.GameFileInit;
import com.ritchie.myapplicationmove.runtime.GameRuntimeInfo;
import com.ritchie.myapplicationmove.ui.NesGameWindows;
import com.ritchie.myapplicationmove.util.GameRomAddr;
import com.ritchie.nativelib.NativeLib;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "liujunjie";
    private NativeLib nativeLib;
    private boolean off = true;
    private NesGameWindows nesGameWindows;
    private String[] perms;
    private GameFileInit gameFileInit;
    private GameRuntimeInfo gameRuntimeInfo;
    private  NesKeyDate gameKey;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GameRomAddr.rmAddrFile();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * 获取弹窗权限
         * */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !Settings.canDrawOverlays(this)) {
            getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivity(intent);
        }
        //检查权限

        perms = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        checkPermission(perms);

        //启动悬浮窗
        Intent intent1 = new Intent(this, ReadandwriteService.class);
        startService(intent1);
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

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyUp: "+keyCode);
        sendkey(keyCode,false);
        //gameRuntimeInfo.resetStTurbo();
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyUp down : "+keyCode);
        sendkey(keyCode,true);
        return true;
    }
    /**
     * 键盘转化器
     * 1 10 1
     * */
    private void sendkey(int keyCode,boolean DownUP) {

        switch (keyCode){
            case MyKeyBoardProFile.KEY_A:
                keypossess(1,1,-1,DownUP); // 1 1
                break;
            case MyKeyBoardProFile.KEY_B:
                keypossess(1,2,-1,DownUP);// 10 1
                break;
            case MyKeyBoardProFile.KEY_A_TURBO:
                keypossess(2,1,-2,DownUP);// 1  10
                break;
            case MyKeyBoardProFile.KEY_B_TURBO:
                keypossess(2,1,-3,DownUP);// 1 11
                break;
            case MyKeyBoardProFile.KEY_SELECT:
                keypossess(3,4,-1,DownUP);// 100
                break;
            case MyKeyBoardProFile.KEY_START:
                keypossess(3,8,-1,DownUP);// 1000
                break;
            case MyKeyBoardProFile.KEY_LEFT:
                keypossess(0,64,-1,DownUP);// 1000000
                break;
            case MyKeyBoardProFile.KEY_RIGHT:
                keypossess(0,128,-1,DownUP);// 10000000
                break;
            case MyKeyBoardProFile.KEY_UP:
                keypossess(0,16,-1,DownUP);// 10000
                break;
            case MyKeyBoardProFile.KEY_DOWN:
                keypossess(0,32,-1,DownUP);// 100000
                break;
            case MyKeyBoardProFile.KEY_L1:

                break;
            case MyKeyBoardProFile.KEY_R1:
                break;

        }


    }

    /**
     * @param category 表示键盘的类型,是方向键还是
     * @param keys 表示需要输入键盘的值
     * @param trubo 表示是否连发
     * @param downOrUp  表示是否是按下还是抬起
     * */
    private void keypossess(int category, int keys, int trubo,boolean downOrUp) {

        if (!downOrUp){
            gameRuntimeInfo.resetStKey(keys,trubo);
            Log.d(TAG, "keypossess: up:::::" + keys);

        }else {
            Log.d(TAG, "keypossess: down::::" + keys);
            gameRuntimeInfo.setStKey(keys,trubo);
        }


    }


    private void checkPermission(String[] perms) {
        if (!EasyPermissions.hasPermissions(this, perms)) {

            ActivityCompat.requestPermissions(MainActivity.this, perms, 1);
        }else {

        }
    }

}