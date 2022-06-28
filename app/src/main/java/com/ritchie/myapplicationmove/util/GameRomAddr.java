package com.ritchie.myapplicationmove.util;

public class GameRomAddr {

    public static void rmAddrFile(){
        SystemUtil.execShellCmd("rm /data/data/com.ritchie.myapplicationmove/romAddr");

    }
    public static long getRomAddr(){
        String ddr = SystemUtil.execShellCmd("cat rm /data/data/com.ritchie.myapplicationmove/romAddr");
        String[] addrs = ddr.split("/n");
        long addr1 = Long.parseLong(addrs[0]);
        return addr1;
    }
}
