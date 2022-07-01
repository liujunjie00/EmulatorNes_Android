package com.ritchie.myapplicationmove.util;

import com.ritchie.nativelib.NativeLib;

public class GameRomAddr {
    private NativeLib nativeLib;


    public GameRomAddr(NativeLib nativeLib) {
        this.nativeLib = nativeLib;
    }

    /**
     * 这个不需要做了,在c部分已经实现了
     * */
    public void rmAddrFile(){
        SystemUtil.execShellCmd("rm /data/data/com.ritchie.myapplicationmove/RAMAddr");

    }
    public long getRomAddr(){
        long addr1;
        String ddr = SystemUtil.execShellCmd("cat /data/data/com.ritchie.myapplicationmove/RAMAddr");
        if (ddr == "" ) return 0;
        if (ddr.contains("\n")){
            String[] addrs = ddr.split("\n");
            addr1= Long.parseLong(addrs[0]);
        }else {
            ddr = ddr.substring(ddr.indexOf("0x")+2);
            addr1 = Long.parseLong(ddr,16);
        }

        return addr1;
    }
    public boolean setAddrValue(long offsize, int value){
        if (nativeLib != null){

            nativeLib.setAddrValue(getRomAddr()+offsize,value);
        }
        return true;
    }
}
