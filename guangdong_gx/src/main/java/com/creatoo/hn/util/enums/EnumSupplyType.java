package com.creatoo.hn.util.enums;

/**
 * Created by rbg on 2017/8/23.
 */
public enum EnumSupplyType {
    TYPE_CG("1", "场馆"),
    TYPE_PXKC("2", "培训"),
    TYPE_YCJM("4", "文艺演出"),
    TYPE_ZL("5", "展览展示"),
    TYPE_WYZJ("6", "文艺专家"),
    TYPE_WYPC("3", "文艺辅材");

    private String name;
    private String value;

    EnumSupplyType(String value, String name){
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
