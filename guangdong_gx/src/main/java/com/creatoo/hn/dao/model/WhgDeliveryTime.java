package com.creatoo.hn.dao.model;

import javax.persistence.*;

@Table(name = "whg_delivery_time")
public class WhgDeliveryTime {
    /**
     * 主键ID
     */
    @Id
    private String id;

    /**
     * 配送信息ID
     */
    private String deliveryid;

    /**
     * 配送时间
     */
    private String deliverytime;

    /**
     * 0：不通过，1：通过
     */
    private Integer state;

    /**
     * 配送关联id
     */
    private String deliveryfkid;

    /**
     * 获取主键ID
     *
     * @return id - 主键ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置主键ID
     *
     * @param id 主键ID
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取配送信息ID
     *
     * @return deliveryid - 配送信息ID
     */
    public String getDeliveryid() {
        return deliveryid;
    }

    /**
     * 设置配送信息ID
     *
     * @param deliveryid 配送信息ID
     */
    public void setDeliveryid(String deliveryid) {
        this.deliveryid = deliveryid == null ? null : deliveryid.trim();
    }

    /**
     * 获取配送时间
     *
     * @return deliverytime - 配送时间
     */
    public String getDeliverytime() {
        return deliverytime;
    }

    /**
     * 设置配送时间
     *
     * @param deliverytime 配送时间
     */
    public void setDeliverytime(String deliverytime) {
        this.deliverytime = deliverytime == null ? null : deliverytime.trim();
    }

    /**
     * 获取0：不通过，1：通过
     *
     * @return state - 0：不通过，1：通过
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置0：不通过，1：通过
     *
     * @param state 0：不通过，1：通过
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取配送关联id
     *
     * @return deliveryfkid - 配送关联id
     */
    public String getDeliveryfkid() {
        return deliveryfkid;
    }

    /**
     * 设置配送关联id
     *
     * @param deliveryfkid 配送关联id
     */
    public void setDeliveryfkid(String deliveryfkid) {
        this.deliveryfkid = deliveryfkid == null ? null : deliveryfkid.trim();
    }
}