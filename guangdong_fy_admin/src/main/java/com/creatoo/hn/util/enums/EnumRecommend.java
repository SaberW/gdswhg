package com.creatoo.hn.util.enums;

/**
 * 对应表的公共推荐状态isrecommend
 * Created by Administrator on 2017/3/16.
 */
public enum EnumRecommend {
    RECOMMEND_NO(0, "不推荐"),

    RECOMMEND_YES(1, "推荐");

    private int value;
    private String name;

    EnumRecommend(int value, String name){
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
