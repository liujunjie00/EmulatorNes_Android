package com.ritchie.mapsandftms.proFile;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class IconsoleProFile {
    private byte[] data;



    public IconsoleProFile() {
    }
    public IconsoleProFile(byte[] data) {
        // data FE 1F 06 27 00 00 3C 02 00 00 33 0A 00 05 00 8A 02 00 00 2B 00 00 00 00 00 00 A5 03 00 00
        this.data = data;
    }
    public String  initialize(byte[] data1){
    //先处理data的前两位数

    String ssr = byte2Hex(data1);
    Log.d("liujunjie",ssr);

    return ssr;
    }
    public static String processor(byte[] bytes){
        String oo = byte2Hex(bytes);
        processTheFirsTwo(bytes);

        //需要小段处理数据
        return "nhao1";


    }
    public static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }
    public static String byte2Hex1(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            if (i<bytes.length-1){
                stringBuffer.append(temp+":");
            }else {
                stringBuffer.append(temp);
            }

        }
        return stringBuffer.toString();
    }

    public static String stringHexTo2jinzi(String hexString){


        return null;
    }
    public static String stringHexTo10jinzi(String hexString){
        int num = 0;
        for(int i=2; i<hexString.length();i++){ //i=2,用来跳过“0x”
            if (hexString.charAt(i) >= 'A' && hexString.charAt(i) <= 'F') {
                num = num * 16 + hexString.charAt(i) - 'A' + 10;
            }else {
                num = num * 16 +hexString.charAt(i) - '0';
            }
        }
        System.out.println(num);

        return null;
    }
    //处理前两位变成二进制的模式
    public static List<String> processTheFirsTwo(byte[] ssr){
        List<String> strings = new ArrayList<>();
        for (int i = 0; i <= 1; i++) {
            String ssss;
            StringBuilder oos = null;
            int ss = ssr[i]&0xff+0x100;
            if (ssr[i]<0){
                ssss= Integer.toBinaryString(ss).substring(1);

            }else {
                ssss = Integer.toBinaryString(ss);
            }
            //进行前面一位补零操作
            if (ssss.length()<8){
                StringBuilder zero = new StringBuilder();
                for (int j = 0; j < 8-ssss.length(); j++) {
                    zero.append(0+"");
                }
                ssss=zero+ssss;
                //将小段模式改成大段模式
                StringBuilder oor = new StringBuilder(ssss);
                oos = oor.reverse();
            }
            Log.d("liujunjie数据是"+i,ssss);
            strings.add(oos.toString());
        }
        return strings;
    }
    public static String processTheFirs(byte[] ssr){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= 1; i++) {
            String ssss;
            StringBuilder oos = null;
            int ss = ssr[i]&0xff+0x100;
            if (ssr[i]<0){
                ssss= Integer.toBinaryString(ss).substring(1);

            }else {
                ssss = Integer.toBinaryString(ss);
            }
            //进行前面一位补零操作
            if (ssss.length()<8){
                StringBuilder zero = new StringBuilder();
                for (int j = 0; j < 8-ssss.length(); j++) {
                    zero.append(0+"");
                }
                ssss=zero+ssss;
                //将小段模式改成大段模式
                StringBuilder oor = new StringBuilder(ssss);
                oos = oor.reverse();
            }
            Log.d("liujunjie数据是"+i,ssss);
           // strings.add(oos.toString());
            stringBuilder.append(oos);
        }
        return stringBuilder.toString();
    }
    public static BikeData2 getBikeData(byte[] bytes){
        BikeData2 bikeData2 = new BikeData2();
        //fe瞬间速度：06 27 平均速度：00 00 瞬时踏频：82 03 平均踏频 00 00 里程：86 06 00 训练等级：01 00 瞬间功率：21 03 平均功率： 00 00 总消耗能量：0D 00 每小时消耗的能量：00 00 每分钟消耗的能量：00 00 00 消耗的时间：52 00 剩余的时间：00 00"
        //先将数据处理 不处理前面两位数据

        String ppp = byte2Hex(bytes);
        //瞬间速度
        int ss =(bytes[2]&0xff)|((bytes[3]&0xff)<<8);
        bikeData2.setInstantaneousSpeed_Uint16(ss);
        //平均速度
        int ss1 =(bytes[4]&0xff)|((bytes[5]&0xff)<<8);
        bikeData2.setAverageSpeedPresent_Uint16(ss1);
        //瞬时踏频
        int ss2 =(bytes[6]&0xff)|((bytes[7]&0xff)<<8);
        bikeData2.setInstantaneousCadencePresent_Uint16(ss2);
        //平均踏频
        int ss3 =(bytes[8]&0xff)|((bytes[9]&0xff)<<8);
        bikeData2.setAverageCadencePresent_Uint16(ss3);
        //里程
        int ss4 =((bytes[10]&0xff)|((bytes[11]&0xff)<<8))|((bytes[12]&0xff)<<16);
        bikeData2.setTotalDistancePresent_(ss4);
        //训练等级
        int ss5 =(bytes[13]&0xff)|((bytes[14]&0xff)<<8);
        bikeData2.setResistanceLevelPresent(ss5);
        //瞬间功率
        int ss6 =(bytes[15]&0xff)|((bytes[16]&0xff)<<8);
        bikeData2.setInstantaneousPower(ss6);
        //平均功率
        int ss7 =(bytes[17]&0xff)|((bytes[18]&0xff)<<8);
        bikeData2.setAveragePowerPresent(ss7);
        //总消耗能量
        int ss8 =(bytes[19]&0xff)|((bytes[20]&0xff)<<8);
        bikeData2.setTotalEnergyField(ss8);
        //每小时消耗的能量
        int ss9 =(bytes[21]&0xff)|((bytes[22]&0xff)<<8);
        bikeData2.setEnergyPerHour(ss9);
        //每分钟消耗的能量
        int EnergyPerMinute = bytes[23]&0xff;
        bikeData2.setEnergyPerMinute(EnergyPerMinute);
        //心率
        int HeartRatePresent = bytes[24]&0xff;
        bikeData2.setHeartRatePresent(HeartRatePresent);
        //耗氧量
        int MetabolicEquivalentPresent = bytes[25]&0xff;
        bikeData2.setMetabolicEquivalentPresent(MetabolicEquivalentPresent);
        //消耗的时间
        int SS10 =(bytes[26]&0xff)|((bytes[27]&0xff)<<8);
        bikeData2.setElapsedTimePresent(SS10);
        //剩余时间
        int SS11 =(bytes[28]&0xff)|((bytes[29]&0xff)<<8);
        bikeData2.setRemainingTimePresent(SS11);

        return bikeData2;
    }

    public static BikeData1 getBikeData2(byte[] bytes){
        BikeData1 bikeData = new BikeData1();
        //fe瞬间速度：06 27 平均速度：00 00 瞬时踏频：82 03 平均踏频 00 00 里程：86 06 00 训练等级：01 00 瞬间功率：21 03 平均功率： 00 00 总消耗能量：0D 00 每小时消耗的能量：00 00 每分钟消耗的能量：00 00 00 消耗的时间：52 00 剩余的时间：00 00"
        //先将数据处理 不处理前面两位数据
        //瞬间速度
        int ss =(bytes[3]&0xff);
        bikeData.setMoreDataInstantaneousSpeed(ss);
        int ss2 = (bytes[9]&0xff)|((bytes[10]&0xff)<<8)|((bytes[11]&0xff)<<16);
        bikeData.setTotalDistancePresentUint(ss2);
        //9 10 11
        bikeData.setInstantaneousCadence(ss);
        int ss3 =(bytes[18]&0xff);
        bikeData.setHeartRatePresent(ss3);


        return bikeData;
    }



    public static BikeData2 getBikeData1(byte[] bytes){
        BikeData2 bikeData2 = new BikeData2();
        //fe瞬间速度：06 27 平均速度：00 00 瞬时踏频：82 03 平均踏频 00 00 里程：86 06 00 训练等级：01 00 瞬间功率：21 03 平均功率： 00 00 总消耗能量：0D 00 每小时消耗的能量：00 00 每分钟消耗的能量：00 00 00 消耗的时间：52 00 剩余的时间：00 00"
        //先将数据处理 不处理前面两位数据
        String ppp = byte2Hex1(bytes);
        String[] strr = ppp.split(":");
        //瞬间速度

       // bikeData.setInstantaneousSpeed_Uint16(InstantaneousSpeed_Uint162*100+InstantaneousSpeed_Uint161);
        //平均速度

        int AverageSpeedPresent_Uint16 = bytes[4]&0xff+0x100;
        int AverageSpeedPresent_Uint161 = bytes[5]&0xff+0x100;
        bikeData2.setAverageSpeedPresent_Uint16(AverageSpeedPresent_Uint161*100+AverageSpeedPresent_Uint16);
        //瞬时踏频
        int InstantaneousCadencePresent_Uint16 = bytes[6]&0xff+0x100;
        int InstantaneousCadencePresent_Uint161 = bytes[7]&0xff+0x100;
        bikeData2.setInstantaneousCadencePresent_Uint16(InstantaneousCadencePresent_Uint161*100+InstantaneousCadencePresent_Uint16);
        //平均踏频
        int AverageCadencePresent_Uint16 = bytes[8]&0xff+0x100;
        int AverageCadencePresent_Uint161 = bytes[9]&0xff+0x100;
        bikeData2.setAverageCadencePresent_Uint16(AverageCadencePresent_Uint161*100+AverageCadencePresent_Uint16);
        //里程
        int TotalDistancePresent_ = bytes[10]&0xff+0x100;
        int TotalDistancePresent_1 = bytes[11]&0xff+0x100;
        int TotalDistancePresent_2 = bytes[12]&0xff+0x100;
        Log.d("liujunjie2","个位"+TotalDistancePresent_+"百位"+TotalDistancePresent_1+"千位"+TotalDistancePresent_2);
        bikeData2.setTotalDistancePresent_((TotalDistancePresent_2*10000)+(TotalDistancePresent_1*100)+TotalDistancePresent_);
        //训练等级
        int ResistanceLevelPresent = bytes[13]&0xff+0x100;
        int ResistanceLevelPresent1 = bytes[14]&0xff+0x100;
        bikeData2.setResistanceLevelPresent(ResistanceLevelPresent1*100+ResistanceLevelPresent);
        //瞬间功率
        int InstantaneousPower = bytes[15]&0xff+0x100;
        int InstantaneousPower1 = bytes[16]&0xff+0x100;
        bikeData2.setInstantaneousPower(InstantaneousPower1*100+InstantaneousPower);
        //平均功率
        int AveragePowerPresent = bytes[17]&0xff+0x100;
        int AveragePowerPresent1 = bytes[18]&0xff+0x100;
        bikeData2.setAveragePowerPresent(AveragePowerPresent1*100+AveragePowerPresent);
        //总消耗能量
        int TotalEnergyField = bytes[19]&0xff+0x100;
        int TotalEnergyField1 = bytes[20]&0xff+0x100;
        bikeData2.setTotalEnergyField(TotalEnergyField1*100+TotalEnergyField);
        //每小时消耗的能量
        int EnergyPerHour = bytes[21]&0xff+0x100;
        int EnergyPerHour1 = bytes[22]&0xff+0x100;
        bikeData2.setEnergyPerHour(EnergyPerHour1*100+EnergyPerHour);
        //每分钟消耗的能量
        int EnergyPerMinute = bytes[23]&0xff+0x100;
        bikeData2.setEnergyPerMinute(EnergyPerMinute);
        //心率
        int HeartRatePresent = bytes[24]&0xff+0x100;
        bikeData2.setHeartRatePresent(HeartRatePresent);
        //耗氧量
        int MetabolicEquivalentPresent = bytes[25]&0xff+0x100;
        bikeData2.setMetabolicEquivalentPresent(MetabolicEquivalentPresent);
        //消耗的时间
        int ElapsedTimePresent = bytes[26]&0xff+0x100;
        int ElapsedTimePresent1 = bytes[27]&0xff+0x100;
        bikeData2.setElapsedTimePresent(ElapsedTimePresent1*100+ElapsedTimePresent);
        //剩余时间
        int RemainingTimePresent = bytes[28]&0xff+0x100;
        int RemainingTimePresent1 = bytes[29]&0xff+0x100;
        bikeData2.setRemainingTimePresent(RemainingTimePresent1*100+RemainingTimePresent);

        return bikeData2;
    }
    public static int dataProcessing(String str1,String str2,String str3){
        String[] bbb = new String[]{"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};


        return 0;
    }

    public static int dataProcessing(String str1,String str2){
        String[] bbb = new String[]{"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
        int shiweishu = 0;
        int geweishu = 0;
        for (int i = 0; i <bbb.length ; i++) {
            for (int j = 0; j < str1.length(); j++) {
                if (bbb[i].equals(str1.charAt(0))){
                    if (j == 0){
                        shiweishu = i;
                    }
                    if (j == 1){
                        geweishu = i;
                    }

                }
            }
        }


        return 0;
    }

    public static int dataProcessing(String str1){



        return 0;
    }
    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        // 由高位到低位
        for (int i = 0; i < 4; i++) {
        int shift = (4 - 1 - i) * 8;
        value += (bytes[i] & 0x000000FF) << shift;// 往高位游
        }
        return value;
    }


}
