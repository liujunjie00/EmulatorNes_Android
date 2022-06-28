package com.ritchie.mapsandftms.service;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.clj.fastble.BleManager;
import com.clj.fastble.conn.BleCharacterCallback;
import com.clj.fastble.conn.BleGattCallback;
import com.clj.fastble.exception.BleException;
import com.ritchie.mapsandftms.games.GameModel;
import com.ritchie.mapsandftms.proFile.BikeData1;
import com.ritchie.mapsandftms.proFile.BikeData2;
import com.ritchie.mapsandftms.proFile.IconsoleProFile;
import com.ritchie.mapsandftms.window.FloatingWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个是获取踏频传感器的链接数据的信息
 * 通过广播的方式发送数据
 *
 * */
public class RuningService extends Service {

    private BleManager bleManager;
    private List<String[]> lists;
    private String bikeName = "Smart47466500";  //5
    private String bikeName2 = "iConsole+105"; //13
    private static final String TAG= "liujunjie";
    private Context context;
    private Intent intent = new Intent();
    public RuningService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initBT(this,bikeName,5);
        context = this;

        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    private void initBT(Context context,String Name,int index) {
        intent.setAction("FindBluetoothDevices");
        lists = new ArrayList<>();
        bleManager = new BleManager(context);
        bleManager.scanNameAndConnect(Name, 20 * 1000, true, new BleGattCallback() {
            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
               List<BluetoothGattService> services = gatt.getServices();
                for (int i = 0; i < services.size(); i++) {
                    String uuidService = services.get(i).getUuid().toString();
                    List<BluetoothGattCharacteristic> bluetoothGattCharacteristicList = services.get(i).getCharacteristics();
                    for (int j = 0; j < bluetoothGattCharacteristicList.size(); j++) {
                        String uuidItem = bluetoothGattCharacteristicList.get(j).getUuid().toString();
                        String[] itemIndex = new String[]{uuidService,uuidItem};
                        lists.add(itemIndex);
                    }
                }
                notifyBle(lists,index);
            }
            @Override
            public void onNotFoundDevice() {
                stopService("没有找到设备");
                Log.d(TAG, "onNotFoundDevice: 没有找到设备 ");
            }

            @Override
            public void onFoundDevice(BluetoothDevice device) {

            }

            @Override
            public void onConnectSuccess(BluetoothGatt gatt, int status) {
                //发现服务
                gatt.discoverServices();
            }

            @Override
            public void onConnectFailure(BleException exception) {
                Log.d(TAG, "onConnectFailure: 链接超时");
                stopService("连接超时了");
            }
        });
    }
    public void notifyBle(List<String[]> list,int index) {
        Looper.prepare();
        boolean ss = bleManager.notify(list.get(index)[0], list.get(index)[1], new BleCharacterCallback() {
            @Override
            public void onSuccess(BluetoothGattCharacteristic characteristic) {
                byte[] dataBytes = characteristic.getValue();
                BikeData1 c = IconsoleProFile.getBikeData2(dataBytes);

                intent.putExtra("state",1);
                intent.putExtra("data",c);
                context.sendBroadcast(intent);

                Log.d(TAG, "onSuccess: "+dataBytes.toString());
            }
            @Override
            public void onFailure(BleException exception) {
                stopService("找到设备了但是连接不上");

            }
        });

    }
    private void stopService(String msg){
        intent.putExtra("state",-1);
        intent.putExtra("msg",msg);
        context.sendBroadcast(intent);
        stopSelf();
    }

}