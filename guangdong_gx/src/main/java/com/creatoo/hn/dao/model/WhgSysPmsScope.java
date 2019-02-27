package com.creatoo.hn.dao.model;

import javax.persistence.*;

@Table(name = "whg_sys_pms_scope")
public class WhgSysPmsScope {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 权限组标识
     */
    private String pmsid;

    /**
     * 文化馆标识
     */
    private String cultid;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public String getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取权限组标识
     *
     * @return pmsid - 权限组标识
     */
    public String getPmsid() {
        return pmsid;
    }

    /**
     * 设置权限组标识
     *
     * @param pmsid 权限组标识
     */
    public void setPmsid(String pmsid) {
        this.pmsid = pmsid == null ? null : pmsid.trim();
    }

    /**
     * 获取文化馆标识
     *
     * @return cultid - 文化馆标识
     */
    public String getCultid() {
        return cultid;
    }

    /**
     * 设置文化馆标识
     *
     * @param cultid 文化馆标识
     */
    public void setCultid(String cultid) {
        this.cultid = cultid == null ? null : cultid.trim();
    }
}