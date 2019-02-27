package com.creatoo.hn.dao.model;

import javax.persistence.*;

@Table(name = "whg_sys_pms_detail")
public class WhgSysPmsDetail {
    /**
     * 权限组明细唯一标识
     */
    @Id
    private String id;

    /**
     * 权限组标识
     */
    private String pmsid;

    /**
     * 权限字符串
     */
    private String pmsstr;

    /**
     * 获取权限组明细唯一标识
     *
     * @return id - 权限组明细唯一标识
     */
    public String getId() {
        return id;
    }

    /**
     * 设置权限组明细唯一标识
     *
     * @param id 权限组明细唯一标识
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
     * 获取权限字符串
     *
     * @return pmsstr - 权限字符串
     */
    public String getPmsstr() {
        return pmsstr;
    }

    /**
     * 设置权限字符串
     *
     * @param pmsstr 权限字符串
     */
    public void setPmsstr(String pmsstr) {
        this.pmsstr = pmsstr == null ? null : pmsstr.trim();
    }
}