package com.ritchie.mapsandftms.features;

public class BaseCharacter {
    private String[] strings;
    private long index;

    public BaseCharacter() {
    }


    public BaseCharacter( String s, String s1, String s2, String s3, String s4,long indexForComputer) {
        this.strings = new String[]{s,s1,s2,s3,s4};
        this.index = indexForComputer;

    }

    public String[] getStrings() {
        return strings;
    }

    public void setStrings(String[] strings) {
        this.strings = strings;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }
}
