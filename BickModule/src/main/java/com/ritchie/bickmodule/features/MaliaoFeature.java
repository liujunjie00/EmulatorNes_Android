package com.ritchie.mapsandftms.features;

import java.util.ArrayList;
import java.util.List;

public class MaliaoFeature extends Feature{
    SkillData goldenBody; //金身
    SkillData stealth;  //隐身
    SkillData bullet;// 发子弹
    SkillData getBigger; // 变大
    SkillData becomeSmaller; // 变小
    SkillData throughTheWall; // 穿墙
    SkillData airSwimming; // 空中游泳
    SkillData bigJumpInWater; // 水中大跳
    SkillData walkHard; // 步履艰难
    SkillData highJump_halfScreen; //  跳高 半屏
    SkillData highJump_fullScreen; //  跳高
    SkillData[] skillDataList;
    public MaliaoFeature(long mem,long i) {
/*
        goldenBody = new SkillData(mem+sizeof+1720+(0x79f-0x79f),9,1);
        stealth = new SkillData(mem+sizeof+1720+0x79f-0x79E,3,1);
        bullet = new SkillData(mem+sizeof+1720+0x79f-0x756,2,1);
        getBigger = new SkillData(mem+sizeof+1720+0x79f-0x754,1,0);
        becomeSmaller = new SkillData(mem+sizeof+1720+0x79f-0x754,0,0);
        throughTheWall = new SkillData(mem+sizeof+1720+0x79f-0x754,2,0);
        airSwimming = new SkillData(mem+sizeof+1720+0x79f-0x704,1,0);
        bigJumpInWater = new SkillData(mem+sizeof+1720+0x79f-0x704,0,0);
*/
        goldenBody = new SkillData(0, (mem + i - 0xE7) + 0x079F, 15, 1,"金身");
        stealth = new SkillData(1, (mem + i - 0xE7) + 0x79E, 15, 1,"隐身");
        bullet = new SkillData(2, (mem + i - 0xE7) + 0x756, 2, 1,"发子弹");
        getBigger = new SkillData(3, (mem + i - 0xE7) + 0x754, 0, 1,"变大");
        becomeSmaller = new SkillData(4, (mem + i - 0xE7) + 0x754, 1, 1,"变小");
        throughTheWall = new SkillData(5, (mem + i - 0xE7) + 0x754, 2, 0,"穿墙");
        airSwimming = new SkillData(6, (mem + i - 0xE7) + 0x704, 1, 0,"空中游泳");
        bigJumpInWater = new SkillData(7, (mem + i - 0xE7) + 0x704, 0, 0,"水中大跳");
        walkHard = new SkillData(8, (mem + i - 0xe7) + 0x45, 0, 1,"步履艰难");
        highJump_halfScreen = new SkillData(8, (mem + i - 0xe7) + 0x709, 0x20, 0x30,"跳高半屏");
        highJump_fullScreen = new SkillData(9, (mem + i - 0xe7) + 0x709, 0x10, 0x30,"跳高全屏");
        skillDataList = new SkillData[]{goldenBody,
                stealth,
                bullet,
                getBigger,
                becomeSmaller,
                throughTheWall,
                airSwimming,
                bigJumpInWater,
                walkHard,
                highJump_fullScreen,
                highJump_fullScreen};
    }

    public MaliaoFeature(SkillData[] skillData) {
    this.skillDataList = skillData;
    }

    public MaliaoFeature() {
    }

    public SkillData getGoldenBody() {
        return goldenBody;
    }

    public void setGoldenBody(SkillData goldenBody) {
        this.goldenBody = goldenBody;
    }

    public SkillData getStealth() {
        return stealth;
    }

    public void setStealth(SkillData stealth) {
        this.stealth = stealth;
    }

    public SkillData getBullet() {
        return bullet;
    }

    public void setBullet(SkillData bullet) {
        this.bullet = bullet;
    }

    public SkillData getGetBigger() {
        return getBigger;
    }

    public void setGetBigger(SkillData getBigger) {
        this.getBigger = getBigger;
    }

    public SkillData getBecomeSmaller() {
        return becomeSmaller;
    }

    public void setBecomeSmaller(SkillData becomeSmaller) {
        this.becomeSmaller = becomeSmaller;
    }

    public SkillData getThroughTheWall() {
        return throughTheWall;
    }

    public void setThroughTheWall(SkillData throughTheWall) {
        this.throughTheWall = throughTheWall;
    }

    public SkillData getAirSwimming() {
        return airSwimming;
    }

    public void setAirSwimming(SkillData airSwimming) {
        this.airSwimming = airSwimming;
    }

    public SkillData getBigJumpInWater() {
        return bigJumpInWater;
    }

    public void setBigJumpInWater(SkillData bigJumpInWater) {
        this.bigJumpInWater = bigJumpInWater;
    }

    public SkillData getWalkHard() {
        return walkHard;
    }

    public void setWalkHard(SkillData walkHard) {
        this.walkHard = walkHard;
    }

    public SkillData getHighJump_halfScreen() {
        return highJump_halfScreen;
    }

    public void setHighJump_halfScreen(SkillData highJump_halfScreen) {
        this.highJump_halfScreen = highJump_halfScreen;
    }

    public SkillData getHighJump_fullScreen() {
        return highJump_fullScreen;
    }

    public void setHighJump_fullScreen(SkillData highJump_fullScreen) {
        this.highJump_fullScreen = highJump_fullScreen;
    }

    public SkillData[] getSkillDataList() {
        return skillDataList;
    }
}
