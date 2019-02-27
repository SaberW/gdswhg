package com.creatoo.hn.dao.model;

import javax.persistence.*;

@Table(name = "whg_sys_pms_scope_area")
public class WhgSysPmsScopeArea {
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
     * 1-省，2-市， 3-区
     */
    private Integer arealevel;

    /**
     * 省市区名称
     */
    private String areaval;

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
     * 获取1-省，2-市， 3-区
     *
     * @return arealevel - 1-省，2-市， 3-区
     */
    public Integer getArealevel() {
        return arealevel;
    }

    /**
     * 设置1-省，2-市， 3-区
     *
     * @param arealevel 1-省，2-市， 3-区
     */
    public void setArealevel(Integer arealevel) {
        this.arealevel = arealevel;
    }

    /**
     * 获取省市区名称
     *
     * @return areaval - 省市区名称
     */
    public String getAreaval() {
        return areaval;
    }

    /**
     * 设置省市区名称
     *
     * @param areaval 省市区名称
     */
    public void setAreaval(String areaval) {
        this.areaval = areaval == null ? null : areaval.trim();
    }
}