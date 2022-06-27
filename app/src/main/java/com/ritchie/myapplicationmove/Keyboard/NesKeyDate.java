package com.ritchie.myapplicationmove.Keyboard;
import android.content.Context;
import android.content.Intent;
import android.view.InputDevice;

import com.ritchie.myapplicationmove.runtime.GameRuntimeInfo;
import com.ritchie.nativelib.NativeLib;

import java.util.ArrayList;

public class NesKeyDate {
    private NativeLib nativeLib;
    private Intent intentSendkeyS;
    private Context context;
    private long startTime = 0;
    Object lock = new Object();
    private GameRuntimeInfo gameRuntimeInfo;


    public NesKeyDate(NativeLib nativeLib,Context context) {
        this.nativeLib = nativeLib;
        intentSendkeyS = new Intent();
        this.context = context;
        gameRuntimeInfo = new GameRuntimeInfo();
        intentSendkeyS.setAction("keyboard");
    }

    public NesKeyDate() {
    }



    public void sendkey(int keyCode) {

            switch (keyCode){
                case MyKeyBoardProFile.KEY_A:
                    keypossess(0,1,-1);
                    break;
                case MyKeyBoardProFile.KEY_B:
                    keypossess(0,2,-1);
                    break;
                case MyKeyBoardProFile.KEY_A_TURBO:
                    keypossess(0,1,-2);
                    break;
                case MyKeyBoardProFile.KEY_B_TURBO:
                    keypossess(0,1,-3);
                    break;
                case MyKeyBoardProFile.KEY_SELECT:
                    keypossess(2,4,-1);
                    break;
                case MyKeyBoardProFile.KEY_START:
                    keypossess(2,8,-1);


                    break;
                case MyKeyBoardProFile.KEY_LEFT:
                    keypossess(1,64,-1);
                    break;
                case MyKeyBoardProFile.KEY_RIGHT:
                    keypossess(1,128,-1);
                    break;
                case MyKeyBoardProFile.KEY_UP:
                    keypossess(1,16,-1);
                    break;
                case MyKeyBoardProFile.KEY_DOWN:
                    keypossess(1,32,-1);
                    break;
                case MyKeyBoardProFile.KEY_L1:

                    break;
                case MyKeyBoardProFile.KEY_R1:

                    break;

            }


    }

    private void keypossess(int keyAttribute, int key, int turbos) {
        long lasTime = System.currentTimeMillis();
        if (lasTime - startTime > 20){
            startTime = lasTime;
            gameRuntimeInfo.SkillsAndGamesRunning(key,turbos,50);
        }



       /* intentSendkeyS.putExtra("key",key);
        intentSendkeyS.putExtra("turbos",turbos);

        synchronized (lock){
                    context.sendBroadcast(intentSendkeyS);

        }*/




    }


    //获取支持的游戏设备
    public ArrayList<Integer> getGameControllerIds() {
        ArrayList<Integer> gameControllerDeviceIds = new ArrayList<Integer>();
        int[] deviceIds = InputDevice.getDeviceIds();
        for (int deviceId : deviceIds) {
            InputDevice dev = InputDevice.getDevice(deviceId);
            int sources = dev.getSources();
            if (((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD)
                    || ((sources & InputDevice.SOURCE_JOYSTICK)
                    == InputDevice.SOURCE_JOYSTICK)) {
                if (!gameControllerDeviceIds.contains(deviceId)) {
                    gameControllerDeviceIds.add(deviceId);
                }
            }
        }
        return gameControllerDeviceIds;
    }
}
