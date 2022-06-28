package com.ritchie.mapsandftms.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.ritchie.mapsandftms.features.BaseCharacter;
import com.ritchie.mapsandftms.features.MaliaoFeature;
import com.ritchie.mapsandftms.features.SkillData;
import com.ritchie.mapsandftms.features.TankeFeature;
import com.ritchie.mapsandftms.games.GameModel;
import com.ritchie.mapsandftms.window.FloatingWindow;

public class ReadandwriteService extends Service {
    public ReadandwriteService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*FloatingWindow floatingWindow = new FloatingWindow(this);
        floatingWindow.initWindow();
        floatingWindow.setAllViewOnClick();*/
        //切换游戏管理器
        String gameName = "坦克大战";
        BaseCharacter baseCharacter = new BaseCharacter("0x52","0x59","0x4f","0x55","0x49",0x110);
        SkillData skillData1 = new SkillData(0,0xa8,0x20,0x00,"模式切换1");
        SkillData skillData2 = new SkillData(1,0xa8,0x40,0x00,"模式切换2");
        SkillData skillData3 = new SkillData(2,0xa8,0x60,0x00,"模式切换3");
        SkillData skillData4 = new SkillData(3,0x45,0x1f,0x00,"铁墙");
        SkillData skillData5 = new SkillData(4,0x100,0x2,0x00,"禁止不动");
        SkillData skillData6 = new SkillData(5,0x89,0x2,0x00,"保护模式");
        SkillData[] skillData = new SkillData[]{
                skillData1,
                skillData2,
                skillData3,
                skillData4,
                skillData5,
                skillData6

        };
        TankeFeature tankeFeature = new TankeFeature(gameName,skillData,baseCharacter);
        GameModel gameModel = new GameModel(tankeFeature,this,2);
        gameModel.initWindow();
        gameModel.setAllViewOnClick();
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
        //
    }
}