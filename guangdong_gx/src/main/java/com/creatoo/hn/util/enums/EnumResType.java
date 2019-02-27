package com.creatoo.hn.util.enums;

/**
 * Created by rbg on 2017/8/10.
 */
public enum EnumResType {
    TYPE_IMAGE("1", "图片"),
    TYPE_VIDEO("2", "视频"),
    TYPE_AUDIO("3", "音频"),
    TYPE_FILE("4", "文档");

    private String value;
    private String name;

    EnumResType(String value, String name) {
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
