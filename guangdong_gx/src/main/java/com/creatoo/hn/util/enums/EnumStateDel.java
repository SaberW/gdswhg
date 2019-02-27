package com.creatoo.hn.util.enums;

/**
 * 对应表的delstate状态
 * Created by Administrator on 2017/3/16.
 */
public enum EnumStateDel {
    STATE_DEL_NO(0, "未删除"),

    STATE_DEL_YES(1, "已删除");

    private int value;
    private String name;

    EnumStateDel(int value, String name){
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
