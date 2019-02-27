package com.creatoo.hn.util.enums;

/**
 * 操作日志映射的对象类型
 * Created by wangxl on 2017/4/25.
 */
public enum EnumOptType {
    CONSOLE_LOGIN(51, "控制台登录"),
    ACT(1, "文化活动"),
    TRA(2, "文化培训"),
    VEN(3, "文化场馆"),
    ROOM(4, "场馆活动室"),
    CULT(5,"分馆管理"),
    DEPT(6,"部门管理"),
    POST(7,"岗位管理"),
    ADMIN(8,"管理员管理"),
    TYPE(9,"分类配置"),
    TAG(10,"标签配置"),
    KEY(11,"关键字配置"),
    LBT(12,"轮播图配置"),
    COMMENT(13,"评论管理"),
    WHPP(14,"文化品牌配置"),
    SMS(15,"短信模板管理"),
    VIDEO(16,"视频管理"),
    MEMBER(17,"会员管理"),
    COLUMN(18,"栏目设置"),
    NOTICE(19,"资讯公告管理"),
    TEAM(20,"官办团队管理"),
    OPINION(21,"意见反馈管理"),
    MINGLU(22,"名录管理"),
    SUCCOR(23,"传承人管理"),
    VOLTRA(24,"志愿培训"),
    VOLACT(25,"志愿活动"),
    ORG(26,"优秀组织"),
    PERSON(27,"先进个人"),
    PROJECT(28,"项目示范"),
    TRATEA(29,"培训师资管理"),
    ONLINE(30,"在线点播管理"),
    TEAMEM(31,"馆办团队成员管理"),
    GOODS(32,"商品管理"),
    SUPPLY(33,"供需信息"),
    MAJOR(39,"微专业"),
    EXH(35,"展览"),
    EXHGOODS(36,"展品"),
    SHOWGOODS(37,"表演"),
    ORGAN(38,"组织机构"),
    GATHER(34,"众筹管理"),
    PSSH(35,"配送审核"),
    DIGITAL(40, "数字展厅"),
    TRALIVE(36,"直播"),
    MASS(50,"群文管理"),
    MASS_BRAND(51, "群文文化专题类型"),
    MASS_ARTIST(52,"群文艺术人才管理"),
    MASS_RESOURCE(53,"群文资源"),
    MASS_BATCH(54,"群文届次管理"),
    MASS_TEAM(55,"群文团队管理"),
    MASS_TYPE(56,"群文资源库"),
    SUPTRA(66,"供需培训")





    ,ConsoleSystem_PermissionGroup(2111, "总分馆平台权限组管理")


    ;
    private int value;
    private String name;

    private EnumOptType(int value, String name){
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
