package com.ritchie.mapsandftms.features;

public class TankeFeature extends Feature{
    private String Name; //游戏名字
    private long head;   //有效头地址
    private SkillData[] skillData; // 技能数组
    private boolean isInit = false;
    private BaseCharacter baseCharacter;//绝对字符，虽然现在没用

    public TankeFeature() {
    }

    public boolean isInit() {
        return isInit;
    }

    public void setInit(boolean init) {
        isInit = init;
    }

    public BaseCharacter getBaseCharacter() {
        return baseCharacter;
    }

    public void setBaseCharacter(BaseCharacter baseCharacter) {
        this.baseCharacter = baseCharacter;
    }

    public TankeFeature(String name, SkillData[] skillData, BaseCharacter baseCharacter) {
        Name = name;
        this.skillData = skillData;
        this.baseCharacter = baseCharacter;
    }
    /**
     * 将所有的相对头路径的地址初始化为绝对路径
     * */
    public void init(long head){
        isInit = true;
        this.head = head;
        if (skillData != null){
            for (int i = 0; i < skillData.length; i++) {
                long computerAddress =  skillData[i].getOffSize();
                long absoluteAddress = computerAddress+head;
                skillData[i].setPhysicalAddress(absoluteAddress);
            }


        }
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public long getHead() {
        return head;
    }

    public void setHead(long head) {
        this.head = head;
    }

    public SkillData[] getSkillData() {
        return skillData;
    }

    public void setSkillData(SkillData[] skillData) {
        this.skillData = skillData;
    }
}
