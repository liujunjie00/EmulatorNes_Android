package com.ritchie.bickmodule;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ritchie.bickmodule.service.ReadandwriteService;


public class MainActivity extends Activity {
    private String[] perms;
    private int requestCode=0;
    private ImageView imageView1,imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        /***
         * 获取权限 */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !Settings.canDrawOverlays(this)){
            getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivity(intent);
        }
        checkPermission();

        // 开启maps服务
        Intent intent1 = new Intent(this, ReadandwriteService.class);
        startService(intent1);

       finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    public void checkPermission() {
        perms = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
        };
        for (int i = 0; i < perms.length; i++) {
            int status = ContextCompat.checkSelfPermission(getApplicationContext(), perms[i]);
            if (status != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, perms, requestCode);
                return;
            }

        }

    }
}