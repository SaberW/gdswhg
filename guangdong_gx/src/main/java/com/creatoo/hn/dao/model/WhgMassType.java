package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_mass_type")
public class WhgMassType {
    /**
     * 主键,资源库唯一标识
     */
    @Id
    private String id;

    /**
     * 父标识
     */
    private String pid;

    /**
     * 资源库名称
     */
    private String name;

    /**
     * 资源库描述
     */
    private String memo;

    /**
     * 资源库排序
     */
    private Integer idx;

    /**
     * 创建管理员标识
     */
    private String crtuser;

    /**
     * 创建时间
     */
    private Date crtdate;

    /**
     * 所有文化馆
     */
    private String cultid;

    /**
     * 所属文化馆的部门
     */
    private String deptid;

    /**
     * 状态.0-无效，1-有效
     */
    private Integer state;

    /**
     * 获取主键,资源库唯一标识
     *
     * @return id - 主键,资源库唯一标识
     */
    public String getId() {
        return id;
    }

    /**
     * 设置主键,资源库唯一标识
     *
     * @param id 主键,资源库唯一标识
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取父标识
     *
     * @return pid - 父标识
     */
    public String getPid() {
        return pid;
    }

    /**
     * 设置父标识
     *
     * @param pid 父标识
     */
    public void setPid(String pid) {
        this.pid = pid == null ? null : pid.trim();
    }

    /**
     * 获取资源库名称
     *
     * @return name - 资源库名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置资源库名称
     *
     * @param name 资源库名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取资源库描述
     *
     * @return memo - 资源库描述
     */
    public String getMemo() {
        return memo;
    }

    /**
     * 设置资源库描述
     *
     * @param memo 资源库描述
     */
    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    /**
     * 获取资源库排序
     *
     * @return idx - 资源库排序
     */
    public Integer getIdx() {
        return idx;
    }

    /**
     * 设置资源库排序
     *
     * @param idx 资源库排序
     */
    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    /**
     * 获取创建管理员标识
     *
     * @return crtuser - 创建管理员标识
     */
    public String getCrtuser() {
        return crtuser;
    }

    /**
     * 设置创建管理员标识
     *
     * @param crtuser 创建管理员标识
     */
    public void setCrtuser(String crtuser) {
        this.crtuser = crtuser == null ? null : crtuser.trim();
    }

    /**
     * 获取创建时间
     *
     * @return crtdate - 创建时间
     */
    public Date getCrtdate() {
        return crtdate;
    }

    /**
     * 设置创建时间
     *
     * @param crtdate 创建时间
     */
    public void setCrtdate(Date crtdate) {
        this.crtdate = crtdate;
    }

    /**
     * 获取所有文化馆
     *
     * @return cultid - 所有文化馆
     */
    public String getCultid() {
        return cultid;
    }

    /**
     * 设置所有文化馆
     *
     * @param cultid 所有文化馆
     */
    public void setCultid(String cultid) {
        this.cultid = cultid == null ? null : cultid.trim();
    }

    /**
     * 获取所属文化馆的部门
     *
     * @return deptid - 所属文化馆的部门
     */
    public String getDeptid() {
        return deptid;
    }

    /**
     * 设置所属文化馆的部门
     *
     * @param deptid 所属文化馆的部门
     */
    public void setDeptid(String deptid) {
        this.deptid = deptid == null ? null : deptid.trim();
    }

    /**
     * 获取状态.0-无效，1-有效
     *
     * @return state - 状态.0-无效，1-有效
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态.0-无效，1-有效
     *
     * @param state 状态.0-无效，1-有效
     */
    public void setState(Integer state) {
        this.state = state;
    }
}