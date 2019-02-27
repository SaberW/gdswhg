package com.creatoo.hn.util.enums;

/**
 * 对应外部供需前端菜单的标识
 * Created by Administrator on 2017/3/16.
 */
public enum EnumWbMenu {

    MENU_WHZX("WHZX", "外供-文化资讯"),
    MENU_WHHD("WHHD", "外供-文化活动"),
    MENU_PPZX("PPZX", "外供-品牌资讯"),
    MENU_CGYD("CGYD", "外供-场馆预订"),
    MENU_WHZC("WHZC", "外供-文化众筹"),
    MENU_WLPX("WLPX", "外供-网络培训"),
    MENU_QWZY("QWZY", "外供-群文资源"),
    MENU_WHGLM("WHGLM", "外供-文化馆联盟"),
    MENU_SZZG("SZZG", "外供-数字展馆"),
    MENU_ZCXM("ZCXM", "众筹-众筹项目"),
    MENU_ZCPP("ZCPP", "众筹-众筹品牌"),
    MENU_ZCZX("ZCZX", "众筹-众筹资讯"),
    MENU_FLFG("FLFG", "众筹-法律法规"),
    MENU_XXPX("XXPX", "培训-线下培训"),
    MENU_ZXKC("ZXKC", "培训-在线课程"),
    MENU_WXY("WXY", "培训-微专业"),
    MENU_PXSZ("PXSZ", "培训-培训师资"),
    MENU_PXZY("PXZY", "培训-培训资源");


    private String value;
    private String name;

    EnumWbMenu(String  value, String name){
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
