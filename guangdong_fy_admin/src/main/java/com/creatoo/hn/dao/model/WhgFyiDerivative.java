package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_fyi_derivative")
public class WhgFyiDerivative {
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
     * 类型
     */
    private String type;

    /**
     * 非遗类型
     */
    private String etype;

    /**
     * 制作人/单位
     */
    private String producer;

    /**
     * 创建人
     */
    private String createuser;

    /**
     * 创建时间
     */
    private Date createtime;

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
     * 是否推荐(0：不推荐1：推荐)
     */
    private Integer isrecommend;

    /**
     * 所属文化管
     */
    private String cultid;

    /**
     * 描述
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
     * 获取类型
     *
     * @return type - 类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置类型
     *
     * @param type 类型
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * 获取非遗类型
     *
     * @return etype - 非遗类型
     */
    public String getEtype() {
        return etype;
    }

    /**
     * 设置非遗类型
     *
     * @param etype 非遗类型
     */
    public void setEtype(String etype) {
        this.etype = etype == null ? null : etype.trim();
    }

    /**
     * 获取制作人/单位
     *
     * @return producer - 制作人/单位
     */
    public String getProducer() {
        return producer;
    }

    /**
     * 设置制作人/单位
     *
     * @param producer 制作人/单位
     */
    public void setProducer(String producer) {
        this.producer = producer == null ? null : producer.trim();
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
     * @return createtime - 创建时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 设置创建时间
     *
     * @param createtime 创建时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
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
     * 获取是否推荐(0：不推荐1：推荐)
     *
     * @return isrecommend - 是否推荐(0：不推荐1：推荐)
     */
    public Integer getIsrecommend() {
        return isrecommend;
    }

    /**
     * 设置是否推荐(0：不推荐1：推荐)
     *
     * @param isrecommend 是否推荐(0：不推荐1：推荐)
     */
    public void setIsrecommend(Integer isrecommend) {
        this.isrecommend = isrecommend;
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
     * 获取描述
     *
     * @return introduction - 描述
     */
    public String getIntroduction() {
        return introduction;
    }

    /**
     * 设置描述
     *
     * @param introduction 描述
     */
    public void setIntroduction(String introduction) {
        this.introduction = introduction == null ? null : introduction.trim();
    }
}