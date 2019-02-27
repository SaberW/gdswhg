package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_xj_reason")
public class WhgXjReason {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 关联id
     */
    private String fkid;

    /**
     * 关联类型，活动、场馆
     */
    private String fktype;

    /**
     * 关联业务标题
     */
    private String fktitile;

    /**
     * 创建者
     */
    private String crtuser;

    /**
     * 当前时间
     */
    private Date crtdate;

    /**
     * 下架原因
     */
    private String reason;

    /**
     * 关联业务发布人
     */
    private String touser;

    /**
     * 是否 发送短信   0 否  1  是
     */
    private Integer issms;

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
     * 获取关联id
     *
     * @return fkid - 关联id
     */
    public String getFkid() {
        return fkid;
    }

    /**
     * 设置关联id
     *
     * @param fkid 关联id
     */
    public void setFkid(String fkid) {
        this.fkid = fkid == null ? null : fkid.trim();
    }

    /**
     * 获取关联类型，活动、场馆
     *
     * @return fktype - 关联类型，活动、场馆
     */
    public String getFktype() {
        return fktype;
    }

    /**
     * 设置关联类型，活动、场馆
     *
     * @param fktype 关联类型，活动、场馆
     */
    public void setFktype(String fktype) {
        this.fktype = fktype == null ? null : fktype.trim();
    }

    /**
     * 获取关联业务标题
     *
     * @return fktitile - 关联业务标题
     */
    public String getFktitile() {
        return fktitile;
    }

    /**
     * 设置关联业务标题
     *
     * @param fktitile 关联业务标题
     */
    public void setFktitile(String fktitile) {
        this.fktitile = fktitile == null ? null : fktitile.trim();
    }

    /**
     * 获取创建者
     *
     * @return crtuser - 创建者
     */
    public String getCrtuser() {
        return crtuser;
    }

    /**
     * 设置创建者
     *
     * @param crtuser 创建者
     */
    public void setCrtuser(String crtuser) {
        this.crtuser = crtuser == null ? null : crtuser.trim();
    }

    /**
     * 获取当前时间
     *
     * @return crtdate - 当前时间
     */
    public Date getCrtdate() {
        return crtdate;
    }

    /**
     * 设置当前时间
     *
     * @param crtdate 当前时间
     */
    public void setCrtdate(Date crtdate) {
        this.crtdate = crtdate;
    }

    /**
     * 获取下架原因
     *
     * @return reason - 下架原因
     */
    public String getReason() {
        return reason;
    }

    /**
     * 设置下架原因
     *
     * @param reason 下架原因
     */
    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    /**
     * 获取关联业务发布人
     *
     * @return touser - 关联业务发布人
     */
    public String getTouser() {
        return touser;
    }

    /**
     * 设置关联业务发布人
     *
     * @param touser 关联业务发布人
     */
    public void setTouser(String touser) {
        this.touser = touser == null ? null : touser.trim();
    }

    /**
     * 获取是否 发送短信   0 否  1  是
     *
     * @return issms - 是否 发送短信   0 否  1  是
     */
    public Integer getIssms() {
        return issms;
    }

    /**
     * 设置是否 发送短信   0 否  1  是
     *
     * @param issms 是否 发送短信   0 否  1  是
     */
    public void setIssms(Integer issms) {
        this.issms = issms;
    }
}