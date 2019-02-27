package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_ywi_hardware")
public class WhgYwiHardware {
    /**
     * 主键ID
     */
    @Id
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 介绍
     */
    private String detail;

    /**
     * 文化馆id
     */
    private String cultid;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 状态变更时间
     */
    private Date statemdfdate;

    /**
     * 状态变更用户ID
     */
    private String statemdfuser;

    /**
     * 状态（0 停用  1 启用）
     */
    private Integer state;

    /**
     * 实体ID
     */
    private String entid;

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
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取介绍
     *
     * @return detail - 介绍
     */
    public String getDetail() {
        return detail;
    }

    /**
     * 设置介绍
     *
     * @param detail 介绍
     */
    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    /**
     * 获取文化馆id
     *
     * @return cultid - 文化馆id
     */
    public String getCultid() {
        return cultid;
    }

    /**
     * 设置文化馆id
     *
     * @param cultid 文化馆id
     */
    public void setCultid(String cultid) {
        this.cultid = cultid == null ? null : cultid.trim();
    }

    /**
     * 获取类型
     *
     * @return type - 类型
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置类型
     *
     * @param type 类型
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取状态变更时间
     *
     * @return statemdfdate - 状态变更时间
     */
    public Date getStatemdfdate() {
        return statemdfdate;
    }

    /**
     * 设置状态变更时间
     *
     * @param statemdfdate 状态变更时间
     */
    public void setStatemdfdate(Date statemdfdate) {
        this.statemdfdate = statemdfdate;
    }

    /**
     * 获取状态变更用户ID
     *
     * @return statemdfuser - 状态变更用户ID
     */
    public String getStatemdfuser() {
        return statemdfuser;
    }

    /**
     * 设置状态变更用户ID
     *
     * @param statemdfuser 状态变更用户ID
     */
    public void setStatemdfuser(String statemdfuser) {
        this.statemdfuser = statemdfuser == null ? null : statemdfuser.trim();
    }

    /**
     * 获取状态（0 停用  1 启用）
     *
     * @return state - 状态（0 停用  1 启用）
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态（0 停用  1 启用）
     *
     * @param state 状态（0 停用  1 启用）
     */
    public void setState(Integer state) {
        this.state = state;
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
        this.entid = entid == null ? null : entid.trim();
    }
}