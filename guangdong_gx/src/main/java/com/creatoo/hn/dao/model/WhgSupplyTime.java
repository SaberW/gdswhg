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
     * 是否收费：0不收，1收
     */
    private Integer hasfees;
    /**
     * 配送范围省
     */
    private String psprovince;
    /**
     * 配送范围市
     */
    private String pscity;
    /**
     * 供给类型，参考EnumSupplyType
     */
    private String supplytype;

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

    public Integer getHasfees() {
        return hasfees;
    }

    public void setHasfees(Integer hasfees) {
        this.hasfees = hasfees;
    }

    public String getPsprovince() {
        return psprovince;
    }

    public void setPsprovince(String psprovince) {
        this.psprovince = psprovince;
    }

    public String getPscity() {
        return pscity;
    }

    public void setPscity(String pscity) {
        this.pscity = pscity;
    }

    public String getSupplytype() {
        return supplytype;
    }

    public void setSupplytype(String supplytype) {
        this.supplytype = supplytype;
    }
}