package com.creatoo.hn.dao.model;

import javax.persistence.*;

@Table(name = "whg_sys_user_cult")
public class WhgSysUserCult {
    /**
     * 主键ID
     */
    @Id
    private String id;

    /**
     * 管理员账号ID
     */
    private String userid;

    /**
     * 子馆ID
     */
    private String cultid;

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
     * 获取管理员账号ID
     *
     * @return userid - 管理员账号ID
     */
    public String getUserid() {
        return userid;
    }

    /**
     * 设置管理员账号ID
     *
     * @param userid 管理员账号ID
     */
    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    /**
     * 获取子馆ID
     *
     * @return cultid - 子馆ID
     */
    public String getCultid() {
        return cultid;
    }

    /**
     * 设置子馆ID
     *
     * @param cultid 子馆ID
     */
    public void setCultid(String cultid) {
        this.cultid = cultid == null ? null : cultid.trim();
    }
}