package com.force.librarybase.entity;

/**
 * @author yijiangtao
 * @version v1.0
 * @Package com.daydao.caterers.fastfood.modules.main.business.checkout.event
 * @Description: 扫码消息event类，传输扫码枪扫入的条码
 * @date 2017/6/20.
 */

public class BarcodeEvent {
    //扫码
    private String barCode;

    public BarcodeEvent(String barCode) {
        this.barCode = barCode;
    }


    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
}
