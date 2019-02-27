package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_fyi_talents")
public class WhgFyiTalents {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 文化人才类型
     */
    private String type;

    /**
     * 简介
     */
    private String summary;

    /**
     * 封面图片
     */
    private String picture;

    /**
     * 创建时间
     */
    private Date crtdate;

    /**
     * 状态：1可编辑，9待审核，2待发布，6已发布，4已下架，5已撤消
     */
    private Integer state;

    /**
     * 是否推荐：0：否，1：是，默认0
     */
    private Integer isrecommend;

    /**
     * 最后操作时间
     */
    private Date statemdfdate;

    /**
     * 最后操作人
     */
    private String statemdfuser;

    /**
     * 是否已删除。0：否，1：是
     */
    private Integer isdel;

    /**
     * 分馆ID
     */
    private String cultid;

    /**
     * 部门ID
     */
    private String deptid;

    /**
     * 文化人物介绍
     */
    private String introduction;

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
     * 获取文化人才类型
     *
     * @return type - 文化人才类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置文化人才类型
     *
     * @param type 文化人才类型
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * 获取简介
     *
     * @return summary - 简介
     */
    public String getSummary() {
        return summary;
    }

    /**
     * 设置简介
     *
     * @param summary 简介
     */
    public void setSummary(String summary) {
        this.summary = summary == null ? null : summary.trim();
    }

    /**
     * 获取封面图片
     *
     * @return picture - 封面图片
     */
    public String getPicture() {
        return picture;
    }

    /**
     * 设置封面图片
     *
     * @param picture 封面图片
     */
    public void setPicture(String picture) {
        this.picture = picture == null ? null : picture.trim();
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
     * 获取状态：1可编辑，9待审核，2待发布，6已发布，4已下架，5已撤消
     *
     * @return state - 状态：1可编辑，9待审核，2待发布，6已发布，4已下架，5已撤消
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态：1可编辑，9待审核，2待发布，6已发布，4已下架，5已撤消
     *
     * @param state 状态：1可编辑，9待审核，2待发布，6已发布，4已下架，5已撤消
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取是否推荐：0：否，1：是，默认0
     *
     * @return isrecommend - 是否推荐：0：否，1：是，默认0
     */
    public Integer getIsrecommend() {
        return isrecommend;
    }

    /**
     * 设置是否推荐：0：否，1：是，默认0
     *
     * @param isrecommend 是否推荐：0：否，1：是，默认0
     */
    public void setIsrecommend(Integer isrecommend) {
        this.isrecommend = isrecommend;
    }

    /**
     * 获取最后操作时间
     *
     * @return statemdfdate - 最后操作时间
     */
    public Date getStatemdfdate() {
        return statemdfdate;
    }

    /**
     * 设置最后操作时间
     *
     * @param statemdfdate 最后操作时间
     */
    public void setStatemdfdate(Date statemdfdate) {
        this.statemdfdate = statemdfdate;
    }

    /**
     * 获取最后操作人
     *
     * @return statemdfuser - 最后操作人
     */
    public String getStatemdfuser() {
        return statemdfuser;
    }

    /**
     * 设置最后操作人
     *
     * @param statemdfuser 最后操作人
     */
    public void setStatemdfuser(String statemdfuser) {
        this.statemdfuser = statemdfuser == null ? null : statemdfuser.trim();
    }

    /**
     * 获取是否已删除。0：否，1：是
     *
     * @return isdel - 是否已删除。0：否，1：是
     */
    public Integer getIsdel() {
        return isdel;
    }

    /**
     * 设置是否已删除。0：否，1：是
     *
     * @param isdel 是否已删除。0：否，1：是
     */
    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    /**
     * 获取分馆ID
     *
     * @return cultid - 分馆ID
     */
    public String getCultid() {
        return cultid;
    }

    /**
     * 设置分馆ID
     *
     * @param cultid 分馆ID
     */
    public void setCultid(String cultid) {
        this.cultid = cultid == null ? null : cultid.trim();
    }

    /**
     * 获取部门ID
     *
     * @return deptid - 部门ID
     */
    public String getDeptid() {
        return deptid;
    }

    /**
     * 设置部门ID
     *
     * @param deptid 部门ID
     */
    public void setDeptid(String deptid) {
        this.deptid = deptid == null ? null : deptid.trim();
    }

    /**
     * 获取文化人物介绍
     *
     * @return introduction - 文化人物介绍
     */
    public String getIntroduction() {
        return introduction;
    }

    /**
     * 设置文化人物介绍
     *
     * @param introduction 文化人物介绍
     */
    public void setIntroduction(String introduction) {
        this.introduction = introduction == null ? null : introduction.trim();
    }
}