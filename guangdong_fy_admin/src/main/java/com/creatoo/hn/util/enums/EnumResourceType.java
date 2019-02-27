package com.creatoo.hn.util.enums;

/**
 * 资源管理对应类型
 * Created by wangxl on 2017/4/25.
 */
public enum EnumResourceType {
    TRA(1, "非遗培训"),ACT(2, "非遗活动"),VEN(3, "文化场馆"),ROOM(4, "场馆活动室"),WHPP(5,"文化品牌配置"),
    NOTICE(6,"资讯公告"),MINGLU(7,"非遗名录"),SUCCOR(8,"传承人"),
    ORG(9,"优秀组织"),PERSON(10,"先进个人"),PROJECT(11,"项目示范"),ONLINE(12,"非遗大讲坛"),
    EXHI(13,"非遗展览"),SUBJECT(14,"非遗专题");

    private int value;
    private String name;

    private EnumResourceType(int value, String name){
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
