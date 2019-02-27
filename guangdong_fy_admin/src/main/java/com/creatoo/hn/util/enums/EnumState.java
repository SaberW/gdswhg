package com.creatoo.hn.util.enums;

/**
 * 对应表的公共状态state
 * Created by Administrator on 2017/3/16.
 */
public enum EnumState {
    STATE_NO(0, "停用"),

    STATE_YES(1, "启用");

    private int value;
    private String name;

    EnumState(int value, String name){
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
