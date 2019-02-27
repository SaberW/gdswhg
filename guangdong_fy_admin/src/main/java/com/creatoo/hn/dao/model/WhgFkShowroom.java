package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_fk_showroom")
public class WhgFkShowroom {
    @Id
    private String id;

    /**
     * 展厅名称
     */
    private String name;

    /**
     * 展厅类型
     */
    private String type;

    /**
     * 封面图片
     */
    private String picture;

    /**
     * 状态：1可编辑，9待审核，2待发布，6已发布，4已下架
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 创建人
     */
    private String createuser;

    /**
     * 状态变更时间
     */
    private Date statemdfdate;

    /**
     * 状态变更人
     */
    private String statemdfuser;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String area;

    /**
     * 所属机构
     */
    private String organization;

    /**
     * 链接地址
     */
    private String url;

    /**
     * 所属文化馆
     */
    private String cultid;

    /**
     * 是否推荐(0：不推荐1：推荐)
     */
    private Integer isrecommend;

    /**
     * 展馆简介
     */
    private String intro;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取展厅名称
     *
     * @return name - 展厅名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置展厅名称
     *
     * @param name 展厅名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取展厅类型
     *
     * @return type - 展厅类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置展厅类型
     *
     * @param type 展厅类型
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
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
     * 获取状态变更人
     *
     * @return statemdfuser - 状态变更人
     */
    public String getStatemdfuser() {
        return statemdfuser;
    }

    /**
     * 设置状态变更人
     *
     * @param statemdfuser 状态变更人
     */
    public void setStatemdfuser(String statemdfuser) {
        this.statemdfuser = statemdfuser == null ? null : statemdfuser.trim();
    }

    /**
     * 获取省
     *
     * @return province - 省
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置省
     *
     * @param province 省
     */
    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    /**
     * 获取市
     *
     * @return city - 市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置市
     *
     * @param city 市
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    /**
     * 获取区
     *
     * @return area - 区
     */
    public String getArea() {
        return area;
    }

    /**
     * 设置区
     *
     * @param area 区
     */
    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    /**
     * 获取所属机构
     *
     * @return organization - 所属机构
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * 设置所属机构
     *
     * @param organization 所属机构
     */
    public void setOrganization(String organization) {
        this.organization = organization == null ? null : organization.trim();
    }

    /**
     * 获取链接地址
     *
     * @return url - 链接地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置链接地址
     *
     * @param url 链接地址
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * 获取所属文化馆
     *
     * @return cultid - 所属文化馆
     */
    public String getCultid() {
        return cultid;
    }

    /**
     * 设置所属文化馆
     *
     * @param cultid 所属文化馆
     */
    public void setCultid(String cultid) {
        this.cultid = cultid == null ? null : cultid.trim();
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
     * 获取展馆简介
     *
     * @return intro - 展馆简介
     */
    public String getIntro() {
        return intro;
    }

    /**
     * 设置展馆简介
     *
     * @param intro 展馆简介
     */
    public void setIntro(String intro) {
        this.intro = intro == null ? null : intro.trim();
    }
}