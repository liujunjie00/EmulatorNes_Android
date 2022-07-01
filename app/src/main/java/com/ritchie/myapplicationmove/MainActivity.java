package com.ritchie.myapplicationmove;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;
import android.util.Log;

import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


import com.ritchie.bickmodule.features.BaseCharacter;
import com.ritchie.bickmodule.features.SkillData;
import com.ritchie.bickmodule.features.TankeFeature;
import com.ritchie.bickmodule.service.ReadandwriteService;
import com.ritchie.bickmodule.ui.DashboardView4;
import com.ritchie.myapplicationmove.Keyboard.MyKeyBoardProFile;
import com.ritchie.myapplicationmove.Keyboard.NesKeyDate;
import com.ritchie.myapplicationmove.cadenceReceiver.BleReceiver;
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
    private GameRomAddr gameRomAddr;
    private Button button1;
    private BleReceiver bleReceiver;
    private DashboardView4 dashboardView4;
    private TextView textViewMain;
    private TankeFeature tankeFeature;
    private int requestCode = 0;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dashboardView4 = findViewById(R.id.board_view_progress_left);
        textViewMain = findViewById(R.id.test_view5);
        String gameName = "坦克大战";
        BaseCharacter baseCharacter = new BaseCharacter("0x52","0x59","0x4f","0x55","0x49",0x110);
        SkillData skillData1 = new SkillData(0,0xa8,0x20,0x00,"模式切换1");
        SkillData skillData2 = new SkillData(1,0xa8,0x40,0x00,"模式切换2");
        SkillData skillData3 = new SkillData(2,0xa8,0x60,0x00,"模式切换3");
        SkillData skillData4 = new SkillData(3,0x45,0x1f,0x00,"铁墙");
        SkillData skillData5 = new SkillData(4,0x100,0x2,0x00,"禁止不动");
        SkillData skillData6 = new SkillData(5,0x89,0x2,0x00,"保护模式");
        SkillData[] skillData = new SkillData[]{
                skillData1,
                skillData2,
                skillData3,
                skillData4,
                skillData5,
                skillData6

        };
        tankeFeature = new TankeFeature(gameName,skillData,baseCharacter);
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

        //设置强制横屏
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        button1 = findViewById(R.id.write_rom_addr);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameRomAddr.setAddrValue(0xa8, 0x60);
            }
        });

        //启动悬浮窗
        //Intent intent1 = new Intent(this, ReadandwriteService.class);
        //startService(intent1);
        //实例化窗口
        nativeLib = new NativeLib();
        nesGameWindows = findViewById(R.id.surface_view_game);
        nesGameWindows.init();
        nesGameWindows.initBaseDir(getApplicationContext());
        //0x7fc54b8cd0
        //0x7fc54b8cd0

        //启动窗口模式
        nativeLib.start(0,102,11100);

        //初始化文件结构
        gameFileInit = new GameFileInit(nativeLib);
        gameFileInit.setFindNamePath();

        //运行模拟器
        gameRuntimeInfo = new GameRuntimeInfo(nesGameWindows,nativeLib,getApplicationContext());
        gameRuntimeInfo.startGame();

        gameKey = new NesKeyDate(nativeLib,getApplicationContext());
        gameRomAddr = new GameRomAddr(nativeLib);

        //
        bleReceiver = new BleReceiver(dashboardView4,textViewMain,nativeLib,tankeFeature);
        bleReceiver.ConnectTheDevice(getApplicationContext());





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
        }else {
            gameRuntimeInfo.setStKey(keys,trubo);
        }
    }


    private void checkPermission(String[] perms) {
        for (int i = 0; i < perms.length; i++) {
            int status = ContextCompat.checkSelfPermission(getApplicationContext(), perms[i]);
            if (status != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, perms, requestCode);
                return;
            }

        }
    }

}