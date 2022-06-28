package com.ritchie.mapsandftms.proFile;

public class BikeData2 extends Bike {
   /* 0 More Data/Instantaneous Speed    Uint16   2位
1 Average Speed present  Uint16
1 Instantaneous Cadence  Uint16
1 Average Cadence present  Uint16  33 0A
1 Total Distance Present  Uint24   00 05 00
1 Resistance Level Present Uint8	8A
1 Instantaneous Power Present
1 Average Power Present       Uint40

"FE 1F 06 27 00 00 3C 02 00 00 33 0A 00 05 00 8A 02 00 00 2B 00 00 00 00 00 00 A5 03 00 00
[0000fff1] Notify: "55 14 00 00 00 00 00 00 00 05 01 00 33 0F 2B 00 00 00 73 AA"



-2, 31, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0

00011111

1 Total Energy Field      uint16
    Energy per Hour         uint16
    Energy per Minute       uint8
1 Heart Rate Present      uint8
1 Metabolic Equivalent Present  uint8
1 Elapsed Time Present    uint16
1 Remaining Time Present  uint16

" 瞬间速度：06 27 平均速度：00 00 瞬时踏频：82 03 平均踏频 00 00 里程：86 06 00 训练等级：01 00 瞬间功率：21 03 平均功率： 00 00 总消耗能量：0D 00 每小时消耗的能量：00 00 每分钟消耗的能量：00 心率：00 耗氧量：00 消耗的时间：52 00 剩余的时间：00 00"
" 瞬间速度：06 27 平均速度：00 00 瞬时踏频：82 03 平均踏频 00 00 里程：86 06 00 训练等级：01 00 瞬间功率：21 03 平均功率： 00 00 总消耗能量：0D 00 每小时消耗的能量：00 00 每分钟消耗的能量：00 心率：00 耗氧量：00 消耗的时间：52 00 剩余的时间：00 00"
" 瞬间速度：06 27 平均速度：00 00 瞬时踏频：82 03 平均踏频 00 00 里程：A2 06 00 训练等级：01 00 瞬间功率：21 03 平均功率： 00 00 总消耗能量：0E 00 每小时消耗的能量：00 00 每分钟消耗的能量：00 心率：00 耗氧量：00 消耗的时间：53 00 剩余的时间：00 00"
" 瞬间速度：06 27 平均速度：00 00 瞬时踏频：82 03 平均踏频 00 00 里程：A2 06 00 训练等级：01 00 瞬间功率：21 03 平均功率： 00 00 总消耗能量：0E 00 每小时消耗的能量：00 00 每分钟消耗的能量：00 心率：00 耗氧量：00 消耗的时间：53 00 剩余的时间：00 00"
" 瞬间速度：06 27 平均速度：00 00 瞬时踏频：0E 03 平均踏频 00 00 里程：BE 06 00 训练等级：01 00 瞬间功率：B3 02 平均功率： 00 00 总消耗能量：0E 00 每小时消耗的能量：00 00 每分钟消耗的能量：00 心率：00 耗氧量：00 消耗的时间：54 00 剩余的时间：00 00"
" 瞬间速度：06 27 平均速度：00 00 瞬时踏频：0E 03 平均踏频 00 00 里程：BE 06 00 训练等级：01 00 瞬间功率：B3 02 平均功率： 00 00 总消耗能量：0E 00 每小时消耗的能量：00 00 每分钟消耗的能量：00 心率：00 耗氧量：00 消耗的时间：54 00 剩余的时间：00 00"
" 瞬间速度：06 27 平均速度：00 00 瞬时踏频：6A 02 平均踏频 00 00 里程：D9 06 00 训练等级：01 00 瞬间功率：16 02 平均功率： 00 00 总消耗能量：0E 00 每小时消耗的能量：00 00 每分钟消耗的能量：00 心率：00 耗氧量：00 消耗的时间：55 00 剩余的时间：00 00"*/
 private int InstantaneousSpeed_Uint16;           //瞬间速度
 private int AverageSpeedPresent_Uint16;          //当前平均速度
 private int InstantaneousCadencePresent_Uint16;  //瞬间踏频
 private int AverageCadencePresent_Uint16;        //平均踏频
 private int TotalDistancePresent_;               //总里程
 private int ResistanceLevelPresent;              //训练等级

 private int InstantaneousPower;                  //瞬间功率
 private int AveragePowerPresent;                 //平均功率
 private int TotalEnergyField;                    //总消耗能量16
 private int EnergyPerHour;                       //每小时消耗的能量16
 private int EnergyPerMinute;                     //每分钟消耗的能量8
 private int HeartRatePresent;                    //心率8
 private int MetabolicEquivalentPresent;          // 消耗的氧气 耗氧量 8    0.1 meta
 private int ElapsedTimePresent;                  //消耗的时间
 private int RemainingTimePresent;                //剩余的时间

 public BikeData2() {
 }

 public int getInstantaneousSpeed_Uint16() {
  return InstantaneousSpeed_Uint16;
 }

 public void setInstantaneousSpeed_Uint16(int instantaneousSpeed_Uint16) {
  InstantaneousSpeed_Uint16 = instantaneousSpeed_Uint16;
 }

 public int getAverageSpeedPresent_Uint16() {
  return AverageSpeedPresent_Uint16;
 }

 public void setAverageSpeedPresent_Uint16(int averageSpeedPresent_Uint16) {
  AverageSpeedPresent_Uint16 = averageSpeedPresent_Uint16;
 }

 public int getInstantaneousCadencePresent_Uint16() {
  return InstantaneousCadencePresent_Uint16;
 }

 public void setInstantaneousCadencePresent_Uint16(int instantaneousCadencePresent_Uint16) {
  InstantaneousCadencePresent_Uint16 = instantaneousCadencePresent_Uint16;
 }

 public int getAverageCadencePresent_Uint16() {
  return AverageCadencePresent_Uint16;
 }

 public void setAverageCadencePresent_Uint16(int averageCadencePresent_Uint16) {
  AverageCadencePresent_Uint16 = averageCadencePresent_Uint16;
 }

 public int getTotalDistancePresent_() {
  return TotalDistancePresent_;
 }

 public void setTotalDistancePresent_(int totalDistancePresent_) {
  TotalDistancePresent_ = totalDistancePresent_;
 }

 public int getResistanceLevelPresent() {
  return ResistanceLevelPresent;
 }

 public void setResistanceLevelPresent(int resistanceLevelPresent) {
  ResistanceLevelPresent = resistanceLevelPresent;
 }

 public int getInstantaneousPower() {
  return InstantaneousPower;
 }

 public void setInstantaneousPower(int instantaneousPower) {
  InstantaneousPower = instantaneousPower;
 }

 public int getAveragePowerPresent() {
  return AveragePowerPresent;
 }

 public void setAveragePowerPresent(int averagePowerPresent) {
  AveragePowerPresent = averagePowerPresent;
 }

 public int getTotalEnergyField() {
  return TotalEnergyField;
 }

 public void setTotalEnergyField(int totalEnergyField) {
  TotalEnergyField = totalEnergyField;
 }

 public int getEnergyPerHour() {
  return EnergyPerHour;
 }

 public void setEnergyPerHour(int energyPerHour) {
  EnergyPerHour = energyPerHour;
 }

 public int getEnergyPerMinute() {
  return EnergyPerMinute;
 }

 public void setEnergyPerMinute(int energyPerMinute) {
  EnergyPerMinute = energyPerMinute;
 }

 public int getHeartRatePresent() {
  return HeartRatePresent;
 }

 public void setHeartRatePresent(int heartRatePresent) {
  HeartRatePresent = heartRatePresent;
 }

 public int getMetabolicEquivalentPresent() {
  return MetabolicEquivalentPresent;
 }

 public void setMetabolicEquivalentPresent(int metabolicEquivalentPresent) {
  MetabolicEquivalentPresent = metabolicEquivalentPresent;
 }

 public int getElapsedTimePresent() {
  return ElapsedTimePresent;
 }

 public void setElapsedTimePresent(int elapsedTimePresent) {
  ElapsedTimePresent = elapsedTimePresent;
 }

 public int getRemainingTimePresent() {
  return RemainingTimePresent;
 }

 public void setRemainingTimePresent(int remainingTimePresent) {
  RemainingTimePresent = remainingTimePresent;
 }

 @Override
 public String toString() {
  return "跑步机的数据是{\n" +
          "瞬间速度：=" + InstantaneousSpeed_Uint16 +
          ", \n当前平均速度：=" + AverageSpeedPresent_Uint16 +
          ", \n瞬间踏频=" + InstantaneousCadencePresent_Uint16 +
          ", \n平均踏频=" + AverageCadencePresent_Uint16 +
          ", \n总里程=" + TotalDistancePresent_ +
          ", \n训练等级=" + ResistanceLevelPresent +
          ", \n瞬间功率=" + InstantaneousPower +
          ", \n平均功率=" + AveragePowerPresent +
          ", \n总消耗能量=" + TotalEnergyField +
          ", \n每小时消耗的能量=" + EnergyPerHour +
          ", \n每分钟消耗的能量=" + EnergyPerMinute +
          ", \n心率=" + HeartRatePresent +
          ", \n消耗的氧气：=" + MetabolicEquivalentPresent +
          ", \n消耗的时间：=" + ElapsedTimePresent +
          ", \n剩余的时间：=" + RemainingTimePresent +
          '}';
 }
}
