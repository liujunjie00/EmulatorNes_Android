package com.ritchie.bickmodule.util;

import android.util.Log;


import com.ritchie.bickmodule.features.BaseCharacter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FastSearch {
    private static final String TAG = "liujunjie";
    private StringBuilder stringBuilder = new StringBuilder();

    /**
     * 这里消耗了太多的时间需要优化一下性能*/
    public List<String[]> getTest66(int pid, List<String[]> list, BaseCharacter baseCharacter, int speed) {
        List<String[]> test66 = new ArrayList<>(50);
        for (int i = 0; i < list.size(); i++) {
            // 这里查找到的返回值必须是带有test66字段的
            StringBuilder stringBuilder = execShellCmd2("su root /system/bin/MapsAndFTMS/tanKeReadOnceReturn.out "+pid+" "+list.get(i)[0]+" "+list.get(i)[1]+" "+88);
            String result = stringBuilder.toString();
            if (result.length()>1 && result.contains("test66")){
                Log.d(TAG, "查找到的结果是"+result);
                String offset = result.substring(result.indexOf("offset:")+7,result.length()).trim();
                String[] ttr = new String[]{list.get(i)[0],list.get(i)[1],offset};
                test66.add(ttr);
            }

        }
        if (test66.size()<1){

            throw new RuntimeException("没有查找到test66");
        }
        return test66;
    }

    public static StringBuilder execShellCmd2(String command) {
        StringBuilder stringBuffer = new StringBuilder();
        //Log.i("execShellCmd", command);
        try {
            Process process = Runtime.getRuntime().exec(command + "\n");
            DataOutputStream stdin = new DataOutputStream(
                    process.getOutputStream());
            DataInputStream stdout = new DataInputStream(
                    process.getInputStream());
            DataInputStream stderr = new DataInputStream(
                    process.getErrorStream());
            String line;
            while ((line = stdout.readLine()) != null) {
                stringBuffer.append(line+"\n");
            }
            if (stringBuffer.length() > 0) {
                String string1 = stringBuffer.substring(0, stringBuffer.length() - 1);
            }
            while ((line = stderr.readLine()) != null) {
                Log.e("EXEC", line);
            }
            process.waitFor();
        } catch (Exception e) {
            e.getMessage();
        }
        return stringBuffer;
    }
    public StringBuilder execShellCmd3(String command){
            if (stringBuilder.length()>=1){
             //   stringBuilder.
            }

        return null;
    }
}
