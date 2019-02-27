package com.creatoo.hn.util.enums;

/**
 * 管理系统类别
 * Created by wangxl on 2018/1/16.
 */
public enum EnumConsoleSystem {
    /**
     * 适应文化馆
     */
    sysmgr("sysmgr","总分馆管理系统"),

    /**
     * 适应省市区等区域
     */
    bizmgr("bizmgr", "业务管理系统"),

    /**
     * 群文资源库
     */
    masmgr("masmgr", "群文用户");


    private String value;
    private String name;
    EnumConsoleSystem(String  value, String name){
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
