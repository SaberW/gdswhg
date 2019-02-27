package com.creatoo.hn.util.enums;

/**
 * 权限适应范围
 * 做为whg_sys_pms_scope.type的值
 * Created by wangxl on 2018/1/16.
 */
public enum EnumPmsScope {
    /**
     * 适应文化馆
     */
    Site("Site","适应文化馆"),

    /**
     * 适应省市区等区域
     */
    Area("Area", "适应区域");

    private String value;
    private String name;
    EnumPmsScope(String  value, String name){
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
