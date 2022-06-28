package com.ritchie.bickmodule.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;

import android.os.IBinder;

import android.util.Log;

import androidx.annotation.Nullable;

import com.ritchie.bickmodule.features.BaseCharacter;
import com.ritchie.bickmodule.proFile.ListParcelable;
import com.ritchie.bickmodule.util.MapsTools;

import java.util.List;

/**
 * 这个服务的主要动作是去加载maps
 * */
public class SearchMapsServices extends IntentService {
    private Integer pid = null;
    private List<String[]> test66s = null;
    private  List<String[]> list = null;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SearchMapsServices(String name) {
        super(name);
    }

    @Override
    public void setIntentRedelivery(boolean enabled) {
        super.setIntentRedelivery(enabled);
    }

    public SearchMapsServices(){

        super("");
    }



    @Override
    public void onCreate() {
        Log.d("fuemde", "onCreate: ");

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initMaps();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("liujunjjie", "onBind: ");
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    /**
     * 初始化maps的相关信息*/
    private void initMaps() {
        pid = MapsTools.getPid("com.ritchie.myapplicationmove");
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
        stopSelf();

    }
    private void sendMessage(int i){
        Intent intent1 = new Intent();
        if (i == -1){
            intent1.putExtra("pid",-1);
        }else {
            intent1.putExtra("pid",pid);
            intent1.putExtra("list",new ListParcelable(list));
            intent1.putExtra("test66s",new ListParcelable(test66s));

        }
        intent1.setAction("searcherMaps");

        this.sendBroadcast(intent1);

    }
    class MyAsyncTask extends AsyncTask<List,Integer,Boolean>{


        public MyAsyncTask() {
        }

        @Override
        protected Boolean doInBackground(List... lists) {
            int ex = 0;
            try {
                list = MapsTools.getStartAndEnd(pid);
                // 这里的大数据需要延时处理
                test66s = MapsTools.getTest66(pid, list,new BaseCharacter());
                if (pid!=null && test66s != null && list != null){
                    ex = 1;
                }else {
                    ex = -1;
                }
            }catch (Exception e){
                ex = -1;
            }finally {
                sendMessage(ex);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.d("engg", "onProgressUpdate: "+values);
            super.onProgressUpdate(values);
        }
    }


}
