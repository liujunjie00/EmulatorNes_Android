package com.ritchie.bickmodule.window;

import static android.content.Context.WINDOW_SERVICE;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


import com.ritchie.bickmodule.R;
import com.ritchie.bickmodule.ui.VerticalProgress;

import java.util.Random;

public class TankeWindow implements View.OnClickListener {
    private Context context;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private DisplayMetrics displayMetrics;
    private View view;
    private Button button1,button2,button3,button4;
    private VerticalProgress verticalProgress;
    private Handler handler;
    private Random random;
    private View fragment;
    private int pid;
    private String starAddr,endAddr;



    public TankeWindow(Context context) {
        this.context = context;
        initWindow();

    }

    public void initWindow(){
            if (Settings.canDrawOverlays(context)) {
                windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
                layoutParams = new WindowManager.LayoutParams();
                displayMetrics = new DisplayMetrics();
                windowManager.getDefaultDisplay().getMetrics(displayMetrics);
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                layoutParams.format = PixelFormat.RGB_565;
                layoutParams.alpha = 0.8f;
                layoutParams.width = 300;
                layoutParams.height = 700;
                layoutParams.gravity = Gravity.LEFT;
                layoutParams.x = 10;
                layoutParams.y = 10;
                view = LayoutInflater.from(context).inflate(R.layout.tankelayout, null);

                verticalProgress = view.findViewById(R.id.tan_ke_vertical);
                //fragment = view.findViewById(R.id.control_view);
                windowManager.addView(view, layoutParams);
                verticalProgress = view.findViewById(R.id.vp_progress);
                random = new Random();
                handler = new Handler(Looper.myLooper());

                view.setOnTouchListener(new View.OnTouchListener() {
                    final WindowManager.LayoutParams floatWindowLayoutUpdateParam = layoutParams;
                    double x;
                    double y;
                    double px;
                    double py;
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                x = floatWindowLayoutUpdateParam.x;
                                y = floatWindowLayoutUpdateParam.y;
                                px = event.getRawX();
                                py = event.getRawY();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                floatWindowLayoutUpdateParam.x = (int) ((x + event.getRawX()) - px);
                                floatWindowLayoutUpdateParam.y = (int) ((y + event.getRawY()) - py);
                                windowManager.updateViewLayout(view, floatWindowLayoutUpdateParam);
                                break;
                        }
                        return false;
                    }
                });
            }

    }

    public void click(){
        button1.setOnClickListener(this::onClick);

    }
    @Override
    public void onClick(View v) {
        button1Event();
    }

    private void button1Event() {
    }
}
