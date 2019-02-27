package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_ywi_lbt")
public class WhgYwiLbt {
    /**
     * ID
     */
    @Id
    private String id;

    /**
     * 标题
     */
    private String name;

    /**
     * 图片
     */
    private String picture;

    /**
     * 1:pc轮播图   2:APP轮播图
     */
    private String type;

    /**
     * 链接
     */
    private String url;

    /**
     * 创建人
     */
    private String crtuser;

    /**
     * 创建时间
     */
    private Date crtdate;

    /**
     * 状态（0-不正常；1-正常）
     */
    private Integer state;

    /**
     * 状态变更时间
     */
    private Date statemdfdate;

    /**
     * 状态变更用户ID
     */
    private String statemdfuser;

    /**
     * 删除状态(0-未删除；1-已删除)
     */
    private Integer delstate;

    /**
     * 关联文化馆id
     */
    private String cultid;

    /**
     * 发布系统标识：内部供需，外部供需，非遗。。。
     */
    private String toproject;

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
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取标题
     *
     * @return name - 标题
     */
    public String getName() {
        return name;
    }

    /**
     * 设置标题
     *
     * @param name 标题
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取图片
     *
     * @return picture - 图片
     */
    public String getPicture() {
        return picture;
    }

    /**
     * 设置图片
     *
     * @param picture 图片
     */
    public void setPicture(String picture) {
        this.picture = picture == null ? null : picture.trim();
    }

    /**
     * 获取1:pc轮播图   2:APP轮播图
     *
     * @return type - 1:pc轮播图   2:APP轮播图
     */
    public String getType() {
        return type;
    }

    /**
     * 设置1:pc轮播图   2:APP轮播图
     *
     * @param type 1:pc轮播图   2:APP轮播图
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * 获取链接
     *
     * @return url - 链接
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置链接
     *
     * @param url 链接
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * 获取创建人
     *
     * @return crtuser - 创建人
     */
    public String getCrtuser() {
        return crtuser;
    }

    /**
     * 设置创建人
     *
     * @param crtuser 创建人
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
     * 获取状态（0-不正常；1-正常）
     *
     * @return state - 状态（0-不正常；1-正常）
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态（0-不正常；1-正常）
     *
     * @param state 状态（0-不正常；1-正常）
     */
    public void setState(Integer state) {
        this.state = state;
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
     * 获取删除状态(0-未删除；1-已删除)
     *
     * @return delstate - 删除状态(0-未删除；1-已删除)
     */
    public Integer getDelstate() {
        return delstate;
    }

    /**
     * 设置删除状态(0-未删除；1-已删除)
     *
     * @param delstate 删除状态(0-未删除；1-已删除)
     */
    public void setDelstate(Integer delstate) {
        this.delstate = delstate;
    }

    /**
     * 获取关联文化馆id
     *
     * @return cultid - 关联文化馆id
     */
    public String getCultid() {
        return cultid;
    }

    /**
     * 设置关联文化馆id
     *
     * @param cultid 关联文化馆id
     */
    public void setCultid(String cultid) {
        this.cultid = cultid == null ? null : cultid.trim();
    }

    /**
     * 获取发布系统标识：内部供需，外部供需，非遗。。。
     *
     * @return toproject - 发布系统标识：内部供需，外部供需，非遗。。。
     */
    public String getToproject() {
        return toproject;
    }

    /**
     * 设置发布系统标识：内部供需，外部供需，非遗。。。
     *
     * @param toproject 发布系统标识：内部供需，外部供需，非遗。。。
     */
    public void setToproject(String toproject) {
        this.toproject = toproject == null ? null : toproject.trim();
    }
}