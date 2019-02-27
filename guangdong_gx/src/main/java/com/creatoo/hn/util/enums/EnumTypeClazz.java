package com.creatoo.hn.util.enums;

/**
 * 系统分类类别常量
 * Created by wangxl on 2017/3/17.
 */
public enum EnumTypeClazz {
    TYPE_ART("1", "艺术分类"),
    TYPE_VENUE("2", "场馆类型"),
    TYPE_ROOM("3", "活动室类型"),
    TYPE_ACTIVITY("4", "活动分类"),
    TYPE_TRAIN("5", "培训分类"),
    TYPE_AREA("6", "区域") ,
    TYPE_ROOM_SHEBEI("7", "活动室设备分类"),
    TYPE_GENRE("8", "类别"),
    TYPE_BATCH("9", "批次"),
    TYPE_LEVEL("10", "级别"),
    TYPE_TEA_SPE("11", "老师专长"),
    TYPE_VOL_TRAIN("12", "志愿培训类型"),
    TYPE_VOL_ACT("13", "志愿活动类型"),
    TYPE_ZYFL("14", "资源分类"),
    TYPE_CUL("15","文化展类型"),
    TYPE_LIVE("16","直播类型"),
    TYPE_GOODS("20", "文艺辅材"),
    TYPE_SHOWGOODS("21", "文艺演出"),
    TYPE_EXHGOODS("22", "展品类型"),
    TYPE_EXH("23", "展览展示"),
    TYPE_SHOWORGAN("24", "组织机构类型"),
    TYPE_PER("25", "文艺专家"),
    TYPE_SUP("26", "供需类型"),
    TYPE_SUP_BRAND("27", "供需品牌类型"),
    TYPE_GAT("28", "众筹类型"),
    TYPE_GAT_BRAND("29", "众筹品牌类型"),
    TYPE_MASS("50", "群文类型"),
    TYPE_MASS_BRAND("51", "群文文化专题类型"),
    TYPE_MASS_BATCH("54", "群文专题届次类型"),
    TYPE_MASS_ARTIST("52","群文艺术人才类型"),
    TYPE_MASS_RESOURCE("53","群文资源"),
    TYPE_MASS_TEAM("55","群文团队管理"),
    TYPE_MASS_YXJT("56","群文艺术讲坛资源占位"),
    TYPE_TEA("31", "培训师资"),
    TYPE_DRSC("32", "培训资源"),
    TYPE_LIVEDRSC("33", "直播视频"),
    TYPE_MAJOR("34", "微专业类型"),
    TYPE_PINPAI("35", "文化品牌类型"),
    TYPE_PINPAI_RESOURCE("135", "文化品牌资源类型"), //以前品牌没有自己的资源，此处为自己标记资源
    TYPE_ZX("36", "资讯类型"),
    TYPE_FWLX("37", "服务类型"),
    TYPE_GXPXRY("38", "供需培训人员");

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
