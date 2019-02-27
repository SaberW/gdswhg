package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_supply_time")
public class WhgSupplyTime {
    /**
     * PK
     */
    @Id
    private String id;

    /**
     * 供需信处ID
     */
    private String supplyid;

    /**
     * 时段开始
     */
    private Date timestart;

    /**
     * 时段结束
     */
    private Date timeend;

    /**
     * 获取PK
     *
     * @return id - PK
     */
    public String getId() {
        return id;
    }

    /**
     * 设置PK
     *
     * @param id PK
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取供需信处ID
     *
     * @return supplyid - 供需信处ID
     */
    public String getSupplyid() {
        return supplyid;
    }

    /**
     * 设置供需信处ID
     *
     * @param supplyid 供需信处ID
     */
    public void setSupplyid(String supplyid) {
        this.supplyid = supplyid == null ? null : supplyid.trim();
    }

    /**
     * 获取时段开始
     *
     * @return timestart - 时段开始
     */
    public Date getTimestart() {
        return timestart;
    }

    /**
     * 设置时段开始
     *
     * @param timestart 时段开始
     */
    public void setTimestart(Date timestart) {
        this.timestart = timestart;
    }

    /**
     * 获取时段结束
     *
     * @return timeend - 时段结束
     */
    public Date getTimeend() {
        return timeend;
    }

    /**
     * 设置时段结束
     *
     * @param timeend 时段结束
     */
    public void setTimeend(Date timeend) {
        this.timeend = timeend;
    }
}