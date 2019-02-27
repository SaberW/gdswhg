package com.creatoo.hn.util.enums;

/**
 * 对应系统的标识
 * Created by Administrator on 2017/3/16.
 */
public enum EnumProject {

    PROJECT_WBGX("WBGX", "外部供需"),
    PROJECT_NBGX("NBGX", "内部供需"),
    PROJECT_WLPX("WLPX", "网络培训"),
    PROJECT_ZC("ZC", "众筹"),
    PROJECT_WHT("WHT", "文汇通"),
    PROJECT_ZYK("ZYK", "资源库"),
    PROJECT_FY("FY", "非遗");


    private String value;
    private String name;

    EnumProject(String  value, String name){
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
