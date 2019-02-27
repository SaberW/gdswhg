package com.creatoo.hn.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_rep_order")
public class WhgRepOrder {
    /**
     * ID
     */
    @Id
    private String id;

    /**
     * 设备类型（0、pc  1、微信）
     */
    private Integer devtype;

    /**
     * 实体ID
     */
    private String entid;

    /**
     * 记录时间
     */
    private Date crtdate;

    /**
     * 对应订单ID
     */
    private String orderid;

    /**
     * 实体类型
     */
    private String enttype;

    /**
     * 获取ID
     *
     * @return id - ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取设备类型（0、pc  1、微信）
     *
     * @return devtype - 设备类型（0、pc  1、微信）
     */
    public Integer getDevtype() {
        return devtype;
    }

    /**
     * 设置设备类型（0、pc  1、微信）
     *
     * @param devtype 设备类型（0、pc  1、微信）
     */
    public void setDevtype(Integer devtype) {
        this.devtype = devtype;
    }

    /**
     * 获取实体ID
     *
     * @return entid - 实体ID
     */
    public String getEntid() {
        return entid;
    }

    /**
     * 设置实体ID
     *
     * @param entid 实体ID
     */
    public void setEntid(String entid) {
        this.entid = entid;
    }

    /**
     * 获取记录时间
     *
     * @return crtdate - 记录时间
     */
    public Date getCrtdate() {
        return crtdate;
    }

    /**
     * 设置记录时间
     *
     * @param crtdate 记录时间
     */
    public void setCrtdate(Date crtdate) {
        this.crtdate = crtdate;
    }

    /**
     * 获取对应订单ID
     *
     * @return orderid - 对应订单ID
     */
    public String getOrderid() {
        return orderid;
    }

    /**
     * 设置对应订单ID
     *
     * @param orderid 对应订单ID
     */
    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getEnttype() {
        return enttype;
    }

    public void setEnttype(String enttype) {
        this.enttype = enttype;
    }
}