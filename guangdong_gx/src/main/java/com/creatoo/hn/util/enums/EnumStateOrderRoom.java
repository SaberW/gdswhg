package com.creatoo.hn.util.enums;

/**
 * 活动室订单状态
 */
public enum EnumStateOrderRoom {
    STATE_APPLY(0, "申请预定"),
    STATE_CANCEL(1, "取消预定"),
    STATE_FAIL(2, "审核拒绝"),
    STATE_SUCCESS(3, "预定成功");

    private int value;
    private String name;

    EnumStateOrderRoom(int value, String name){
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
