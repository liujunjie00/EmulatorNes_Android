package com.ritchie.myapplicationmove.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.ritchie.nativelib.NativeLib;

import java.io.File;
import java.util.Timer;

public class NesGameWindows extends SurfaceView implements SurfaceHolder.Callback {
    private final Bitmap vram = Bitmap.createBitmap(1080, 945, Bitmap.Config.ARGB_8888);
    private final Rect vramRect = new Rect(0, 0, 1080, 945);
    private final Object locker = new Object();
    private Rect viewRect = null;
    private final Paint paint = new Paint();
    private String TAG = "liujunjie";
    private NativeLib nativeLib = new NativeLib();


    public void init(){
        android.util.Log.d("liujunjie","初始化成功");
        getHolder().addCallback(this);
        paint.setAntiAlias(false);

    }
    public void tick() {
        synchronized (locker) {
           boolean uuuu = nativeLib.render(vram);
        }

        SurfaceHolder holder = getHolder();
        if (null == holder) {
            return;
        }
        Canvas canvas = holder.lockCanvas();
        if (null == canvas) {

            return;
        }
        canvas.drawColor(0xff000000);
        if (null == viewRect) {
            return;
        }
        canvas.drawBitmap(vram, vramRect, viewRect, paint);
        holder.unlockCanvasAndPost(canvas);
    }


    public NesGameWindows(Context context) {
        super(context);
    }

    public NesGameWindows(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NesGameWindows(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    public void initBaseDir(Context context) {
        File file = context.getExternalCacheDir();
        String path = file.getAbsolutePath();
        nativeLib.setBaseDir(path);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int width, int height) {
        double xDiv = width / 256.0;
        double yDiv = height / 240.0;
        if (xDiv < yDiv) {
            int h = (int) (240 * xDiv);
            int y = (height - h) / 2;
            viewRect = new Rect(0, y, width, y + h);
        } else {
            int w = (int) (256 * yDiv);
            int x = (width - w) / 2;
            viewRect = new Rect(x, 0, x + w, height);
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        viewRect = null;
    }




}
