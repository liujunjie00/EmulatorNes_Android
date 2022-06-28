package com.ritchie.bickmodule.util;

import android.util.Log;

import com.ritchie.mapsandftms.features.BaseCharacter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsTools {
    public static final String TAG = "MapsTools";

    /**
     * 获取app的包名
     * @param appName 需要填写app的类名
     * @return 返回值是app的pid*/
    public static int getPid(String appName){
        int pid ;
        String ssr = SystemUtil.execShellCmd("su root ps -A | grep -v restart | grep "+appName).trim();
        if (ssr.length()<1){
            return 0;
        }
        String ssr1 = ssr.substring(ssr.indexOf(" "),ssr.length()).trim();
        ssr = ssr1.substring(0,ssr1.indexOf(" ")).trim();
        pid = Integer.parseInt(ssr);
        return pid;
    }

    public static int[] getPids(String appName){
        int[] pids = new int[1];
        String ssr = SystemUtil.execShellCmd("su root ps -A | grep "+appName).trim();
        if (ssr.length()<1){
            return null;
        }
        if (ssr.contains("\n")){
            String[] pisStrs = ssr.split("\n");
            for (int i = 0; i < pisStrs.length; i++) {
                String sssy = pisStrs[i];
                String ssr1 = sssy.substring(ssr.indexOf(" "),sssy.length()).trim();
                ssr = ssr1.substring(0,ssr1.indexOf(" ")).trim();
                int pid = Integer.parseInt(ssr);
                pids[i] = pid;
            }
        }
        return pids;
    }
    /**
     * 这个函数就是筛选动作，这个数据搜索所有的关卡 ，*/
    public static List<String[]> getTest66(int pid, List<String[]> list) {
        List<String[]> test66 = new ArrayList<>(50);
        for (int i = 0; i < list.size(); i++) {
            // 这里查找到的返回值必须是带有test66字段的
            StringBuilder stringBuilder = SystemUtil.execShellCmd2("su root /system/bin/MapsAndFTMS/search90a6019f13.out "+pid+" "+list.get(i)[0]+" "+list.get(i)[1]+" "+88);
            String result = stringBuilder.toString();
            if (result.length()>1 && result.contains("test66")){
                Log.d(TAG, "查找到的结果是"+result);
                String[] ttr = new String[]{list.get(i)[0],list.get(i)[1],result};
                test66.add(ttr);
            }

        }
        if (test66.size()<1){

            throw new RuntimeException("没有查找到test66");
        }
        return test66;
    }
    /**
     * 性能优化前
     * */
    public static List<String[]> getTest66(int pid, List<String[]> list, BaseCharacter baseCharacter) {
        List<String[]> test66 = new ArrayList<>(50);
        for (int i = 0; i < list.size(); i++) {
            // 这里查找到的返回值必须是带有test66字段的
            StringBuilder stringBuilder = SystemUtil.execShellCmd2("su root /system/bin/MapsAndFTMS/tanKeReadOnceReturn.out "+pid+" "+list.get(i)[0]+" "+list.get(i)[1]+" "+88);
            String result = stringBuilder.toString();
            if (result.length()>1 && result.contains("test66")){
                Log.d(TAG, "查找到的结果是"+result);
                //这里应该处理三个值 ，一个开始的内存的地址，一个结束的内存的地址，还有一个就是i offSize
                //查找到的结果是pidaa: 1968, start-addr:0xd1b80000,end-addr:0xd1c00000,value:88
                //                  he_test666:get end addr:0xd1b80000 , end:0xd1c00000,offset:137600
                String offset = result.substring(result.indexOf("offset:")+7,result.length()).trim();
                String[] ttr = new String[]{list.get(i)[0],list.get(i)[1],offset};
                test66.add(ttr);
            }

        }
        if (test66.size()<1){

            return null;
        }
        return test66;
    }



    /**
     *
     * 这个函数是全部修改test66的值全部的offSize都会被修改，效率第*/
    public static String allWrite(int pid,String starAddr,String endAddr) {
            // 这里查找到的返回值必须是带有test66字段的
            StringBuilder stringBuilder = SystemUtil.execShellCmd2("su root /system/bin/MapsAndFTMS/b.out "+pid+" "+starAddr+" "+endAddr+" "+00);
            String result = stringBuilder.toString();
            //这里一个还要处理sizeOff的
            Log.d(TAG, "oneSearch: 全部修改的结果"+result);
            return result;
    }
    /**
     * 这个这个oncewrite.out这个文件只会写入第一个offSize,效率搞 */
    public static String onceWrite(int pid,String starAddr,String endAddr) {
        // 这里查找到的返回值必须是带有test66字段的
        StringBuilder stringBuilder = SystemUtil.execShellCmd2("su root /system/bin/MapsAndFTMS/oncewrite.out "+pid+" "+starAddr+" "+endAddr+" "+00);
        String result = stringBuilder.toString();

        Log.d(TAG, "oneSearch: 单个查询的结果是"+result);
        return result;
    }

    /**
     * 这个函数就牛逼了
     * 金身的后面两位是 1720 09*/
    public static String onceWrite(int pid,String starAddr,String endAddr,int offSize,int value) {
        // 这里查找到的返回值必须是带有test66字段的
        StringBuilder stringBuilder = SystemUtil.execShellCmd2("su root /system/bin/MapsAndFTMS/readMemReturnOffset90a601.out "+pid+" "+starAddr+" "+endAddr+" "+offSize+" "+value);
        String result = stringBuilder.toString();

        Log.d(TAG, "oneSearch: 单个查询的结果是"+result);
        return result;
    }
    /**
     * 这个是关于坦克大战的搜索函数*/
    public static List<String[]> getTest66fortanKe(int pid,List<String[]> list) {
        List<String[]> test66 = new ArrayList<>(50);
        for (int i = 0; i < list.size(); i++) {
            // 这里查找到的返回值必须是带有test66字段的
            StringBuilder stringBuilder = SystemUtil.execShellCmd2("su root /system/bin/MapsAndFTMS/tanKeReadOnceReturn.out "+pid+" "+list.get(i)[0]+" "+list.get(i)[1]+" "+88);
            String result = stringBuilder.toString();
            if (result.length()>1 && result.contains("test66")){
                Log.d(TAG, "查找到的结果是"+result);
                String[] ttr = new String[]{list.get(i)[0],list.get(i)[1],result};
                test66.add(ttr);
            }
        }
        if (test66.size()<1){
            throw new RuntimeException("没有查找到test66");
        }
        return test66;
    }





    /**
     * 这个是返回值是maps 的long类型，不用了因为底层c语言实现了输入16位字符串直接转数字*/
    @Deprecated
    public List<long[]> getStartAndEndForLong(int pid ){
        String ff  = SystemUtil.execShellCmd("su root grep -v deleted -rsn proc/"+pid+"/maps | grep rw-p | grep -A5 /lib/arm/libnes.so ");
        List<long[]> list = new ArrayList<>();
        //数据处理
        String[] maps = ff.split("\n");
        for (int i = 0; i < maps.length; i++) {
            String tt = maps[i];
            String starTt = tt.substring(tt.indexOf(":")+1,tt.indexOf("-"));
            starTt = starTt.substring(starTt.indexOf(":")+1,starTt.length());
            long statr = Long.parseLong(starTt,16);
            String endStr  = tt.substring(tt.indexOf("-")+1,tt.indexOf(" "));
            long end = Long.parseLong(endStr,16);
            long[]  mapsLong = new long[]{statr,end};
            list.add(mapsLong);
        }
        return list;
    }
    /**
     * @param pid 需要获取对应程序的pid
     * @return 返回值是String数组的list集合 String[0] 是开始地址 String[1]是结束地址 都不带0x
     *
     * /lib/arm/libnes.so
     * */
    public static List<String[]> getStartAndEnd(int pid){
        //String ff  = SystemUtil.execShellCmd("su root grep -v deleted -rsn proc/"+pid+"/maps | grep rw-p | grep -A200 /lib/arm/libnes.so");
       // String ff  = SystemUtil.execShellCmd("su root grep -v deleted -rsn proc/"+pid+"/maps | grep rw-p | grep -A200 /lib/arm/libnes.so");
        String ff  = SystemUtil.execShellCmd("su root grep -v deleted -rsn proc/"+pid+"/maps | grep rw-p ");
        //String ff  = SystemUtil.execShellCmd("su root grep -v deleted -rsn proc/"+pid+"/maps | grep -A500 /lib/arm/libnes.so");
        if (ff.length()<2){
            throw new RuntimeException("没有搜到任何maps数据，请检查马里奥是否运行");
        }else {
            List<String[]> list = new ArrayList<>();
            String[] maps = ff.split("\n");
            for (int i = 0; i < maps.length; i++) {
                String tt = maps[i];
                String starTt = tt.substring(tt.indexOf(":")+1,tt.indexOf("-"));
                starTt = starTt.substring(starTt.indexOf(":")+1,starTt.length());
                String endStr  = tt.substring(tt.indexOf("-")+1,tt.indexOf(" "));
                String[]  mapsLong = new String[]{starTt,endStr};
                list.add(mapsLong);
            }
            return list;
        }
    }
    /**
     * 大概率不准 不要用*/
    @Deprecated
    public static List<String[]> getStartAndEnd(int pid,int size ){
        String ff  = SystemUtil.execShellCmd("su root grep -v deleted -rsn proc/"+pid+"/maps | grep rw-p | grep -A100 /lib/arm/libnes.so");
        if (ff.length()<2){
            throw new RuntimeException("没有搜到任何maps数据，请检查马里奥是否运行");
        }else {
            List<String[]> list = new ArrayList<>();
            String[] maps = ff.split("\n");
            for (int i = 0; i < maps.length; i++) {
                String tt = maps[i];
                String starTt = tt.substring(tt.indexOf(":")+1,tt.indexOf("-"));
                starTt = starTt.substring(starTt.indexOf(":")+1,starTt.length());
                String endStr  = tt.substring(tt.indexOf("-")+1,tt.indexOf(" "));
                long star = Long.parseLong(starTt,16);
                long end = Long.parseLong(endStr,16);
                Log.d(TAG, "getStartAndEnd: 减法"+star+"-"+end +" = "+(end-star)+"");
                if ((end - star) ==size){
                    String[]  mapsLong = new String[]{starTt,endStr};
                    list.add(mapsLong);
                }
            }
            return list;
        }
    }
    /** 不准确 最好不要用
     * @param size 这个搜索开辟空间的大小 ，现在就是1024*512 和 1024* 1024
     * @param libnesLen 这个参数是填写 -A 后面的数的 */
    public static List<String[]> getStartAndEnd(int pid,int size,int libnesLen ){
        String ff  = SystemUtil.execShellCmd("su root grep -v deleted -rsn proc/"+pid+"/maps | grep rw-p | grep -A100 /lib/arm/libnes.so");
        if (ff.length()<2){
            throw new RuntimeException("没有搜到任何maps数据，请检查马里奥是否运行");
        }else {
            List<String[]> list = new ArrayList<>();
            String[] maps = ff.split("\n");
            for (int i = 0; i < maps.length; i++) {
                String tt = maps[i];
                String starTt = tt.substring(tt.indexOf(":")+1,tt.indexOf("-"));
                starTt = starTt.substring(starTt.indexOf(":")+1,starTt.length());
                String endStr  = tt.substring(tt.indexOf("-")+1,tt.indexOf(" "));
                long star = Long.parseLong(starTt,16);
                long end = Long.parseLong(endStr,16);
                Log.d(TAG, "getStartAndEnd: 减法"+star+"-"+end +" = "+(end-star)+"");
                if ((end - star) ==size){
                    String[]  mapsLong = new String[]{starTt,endStr};
                    list.add(mapsLong);
                }

            }
            return list;
        }
    }
    /**
     * 这里不能直接打log 打log会卡死程序 */
    public static void printfAllMem (int pid,String star,String end){
       SystemUtil.execShellCmd("su root /system/bin/MapsAndFTMS/printAllMemChar.out "+pid+" "+star+" "+end+" "+88+" > /sdcard/test.txt ");

    }


    public static void fastWrite1 (int pid,String addr,int value){
        String ssr = SystemUtil.execShellCmd("su root /system/bin/MapsAndFTMS/fastAddrWrite2.out "+pid+" "+addr+" "+value);
        Log.d(TAG, "fastWrite1: "+ssr);

    }
}
