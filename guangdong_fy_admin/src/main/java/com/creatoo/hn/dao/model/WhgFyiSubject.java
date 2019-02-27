package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_fyi_subject")
public class WhgFyiSubject {
    /**
     * ID
     */
    @Id
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 图片
     */
    private String picture;

    /**
     * 背景图片
     */
    private String bgpicture;

    /**
     * 背景颜色
     */
    private String bgcolour;

    /**
     * 创建人
     */
    private String createuser;

    /**
     * 创建时间
     */
    private Date createdate;

    /**
     * 状态：1可编辑，9待审核，2待发布，6已发布，4已下架
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
     * 背景图片不平铺:1-是， 0-否
     */
    private Integer norepeat;

    /**
     * 所属文化管
     */
    private String cultid;

    /**
     * 内容简介
     */
    private String introduction;

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
     * 获取背景图片
     *
     * @return bgpicture - 背景图片
     */
    public String getBgpicture() {
        return bgpicture;
    }

    /**
     * 设置背景图片
     *
     * @param bgpicture 背景图片
     */
    public void setBgpicture(String bgpicture) {
        this.bgpicture = bgpicture == null ? null : bgpicture.trim();
    }

    /**
     * 获取背景颜色
     *
     * @return bgcolour - 背景颜色
     */
    public String getBgcolour() {
        return bgcolour;
    }

    /**
     * 设置背景颜色
     *
     * @param bgcolour 背景颜色
     */
    public void setBgcolour(String bgcolour) {
        this.bgcolour = bgcolour == null ? null : bgcolour.trim();
    }

    /**
     * 获取创建人
     *
     * @return createuser - 创建人
     */
    public String getCreateuser() {
        return createuser;
    }

    /**
     * 设置创建人
     *
     * @param createuser 创建人
     */
    public void setCreateuser(String createuser) {
        this.createuser = createuser == null ? null : createuser.trim();
    }

    /**
     * 获取创建时间
     *
     * @return createdate - 创建时间
     */
    public Date getCreatedate() {
        return createdate;
    }

    /**
     * 设置创建时间
     *
     * @param createdate 创建时间
     */
    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    /**
     * 获取状态：1可编辑，9待审核，2待发布，6已发布，4已下架
     *
     * @return state - 状态：1可编辑，9待审核，2待发布，6已发布，4已下架
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态：1可编辑，9待审核，2待发布，6已发布，4已下架
     *
     * @param state 状态：1可编辑，9待审核，2待发布，6已发布，4已下架
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
     * 获取背景图片不平铺:1-是， 0-否
     *
     * @return norepeat - 背景图片不平铺:1-是， 0-否
     */
    public Integer getNorepeat() {
        return norepeat;
    }

    /**
     * 设置背景图片不平铺:1-是， 0-否
     *
     * @param norepeat 背景图片不平铺:1-是， 0-否
     */
    public void setNorepeat(Integer norepeat) {
        this.norepeat = norepeat;
    }

    /**
     * 获取所属文化管
     *
     * @return cultid - 所属文化管
     */
    public String getCultid() {
        return cultid;
    }

    /**
     * 设置所属文化管
     *
     * @param cultid 所属文化管
     */
    public void setCultid(String cultid) {
        this.cultid = cultid == null ? null : cultid.trim();
    }

    /**
     * 获取内容简介
     *
     * @return introduction - 内容简介
     */
    public String getIntroduction() {
        return introduction;
    }

    /**
     * 设置内容简介
     *
     * @param introduction 内容简介
     */
    public void setIntroduction(String introduction) {
        this.introduction = introduction == null ? null : introduction.trim();
    }
}