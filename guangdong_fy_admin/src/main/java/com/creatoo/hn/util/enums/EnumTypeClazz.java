package com.creatoo.hn.util.enums;

/**
 * 系统分类类别常量
 * Created by wangxl on 2017/3/17.
 */
public enum EnumTypeClazz {
    TYPE_ART("1", "艺术分类"), TYPE_VENUE("2", "场馆分类"), TYPE_ROOM("3", "活动室分类"), TYPE_ACTIVITY("4", "活动分类"),
    TYPE_TRAIN("5", "培训分类"), TYPE_AREA("6", "区域") , TYPE_ROOM_SHEBEI("7", "活动室设备分类"),
    TYPE_GENRE("8", "类别"),TYPE_BATCH("9", "批次"),TYPE_LEVEL("10", "级别"),TYPE_TEA_SPE("11", "老师专长"),
    TYPE_VOL_TRAIN("12", "志愿培训类型"),TYPE_VOL_ACT("13", "志愿活动类型"),TYPE_ZYFL("14", "资源分类"),TYPE_CUL("15","文化展类型"),TYPE_LIVE("16","直播类型"),
    TYPE_GOODS("20", "商品类型"),TYPE_SHOWGOODS("21", "展演商品类型"),TYPE_EXHGOODS("22", "展品类型"),TYPE_EXH("23", "展览类商品类型"),TYPE_SHOWORGAN("24", "组织机构类型"),TYPE_PER("25", "人才类型"),
    TYPE_SUP("26", "供需类型"),TYPE_SUP_BRAND("27", "供需品牌类型"),
    TYPE_GAT("28", "众筹类型"),TYPE_GAT_BRAND("29", "众筹品牌类型"),
    TYPE_TEA("31", "培训师资"),TYPE_DRSC("32", "培训资源"),TYPE_LIVEDRSC("33", "直播视频"),TYPE_MAJOR("34", "微专业类型"),TYPE_PINPAI("35", "文化品牌类型");

    private String value;
    private String name;


    EnumTypeClazz(String value, String name){
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
