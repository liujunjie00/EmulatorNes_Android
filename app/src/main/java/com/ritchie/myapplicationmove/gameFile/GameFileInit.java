package com.ritchie.myapplicationmove.gameFile;

import android.content.Context;

import com.ritchie.nativelib.NativeLib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class GameFileInit {
    private NativeLib nativeLib;
    public GameFileInit() {
    }

    public GameFileInit(NativeLib nativeLib) {
        this.nativeLib = nativeLib;

    }
    public boolean setFindNamePath(){

        nativeLib.loadGame("/mnt/sdcard/Download/rom/tanke.nes","/mnt/sdcard/Download/rom",
                "/mnt/sdcard/Download/rom/tanke.sav");


        return false;
    }

    /**
     *  从assets目录中复制整个文件夹内容
     *  @param  context  Context 使用CopyFiles类的Activity
     *  @param  oldPath  String  原文件路径  如：/aa
     *  @param  newPath  String  复制后路径  如：xx:/bb/cc
     */
    /*public void copyFilesFassets(Context context, String oldPath, String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {//如果是目录
                File file = new File(newPath);
                file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFassets(context,oldPath + "/" + fileName,newPath+"/"+fileName);
                }
            } else {//如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath,fileName));
                byte[] buffer = new byte[1024];
                int byteCount=0;
                while((byteCount=is.read(buffer))!=-1) {//循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {

            e.printStackTrace();

        }
    }*/
}
