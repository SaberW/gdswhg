package com.creatoo.hn.util.enums;

public enum EnumSupplyGxType {
    TYPE_G("0", "供给"),
    TYPE_X("1", "需求");

    private String name;
    private String value;

    EnumSupplyGxType(String value, String name){
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
