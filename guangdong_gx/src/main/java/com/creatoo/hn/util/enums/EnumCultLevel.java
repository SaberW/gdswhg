package com.creatoo.hn.util.enums;

import io.swagger.models.auth.In;

/**
 * 文化馆级别
 * Created by wangxl on 2018/1/17.
 */
public enum EnumCultLevel {
    Level_Province(1, "省级"),
    Level_City(2, "市级"),
    Level_Area(3, "区级");

    private Integer value;
    private String name;
    EnumCultLevel(Integer  value, String name){
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
