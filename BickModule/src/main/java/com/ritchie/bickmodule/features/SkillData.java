package com.ritchie.mapsandftms.features;

public class SkillData {
    private int index;
    private long computerOffSize;
    private int Value;
    private int defaultValue;
    private String name;

    public long getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(long physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    private long physicalAddress;

    public SkillData(int index,long offSize, int value, int defaultValue,String name) {
        this.computerOffSize = offSize;
        Value = value;
        this.defaultValue = defaultValue;
        this.index = index;
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public long getOffSize() {
        return computerOffSize;
    }

    public int getValue() {
        return Value;
    }

    public int getDefaultValue() {
        return defaultValue;
    }

    public void setOffSize(long offSize) {
        this.computerOffSize = offSize;
    }

    public String getName() {
        return name;
    }
}
