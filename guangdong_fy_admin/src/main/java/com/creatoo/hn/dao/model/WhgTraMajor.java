package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_tra_major")
public class WhgTraMajor {
    /**
     * 微专业ID
     */
    @Id
    private String id;

    /**
     * 状态（参考状态枚举）
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Date crtdate;

    /**
     * 创建用户
     */
    private String crtuser;

    /**
     * 修改状态时间
     */
    private Date statemdfdate;

    /**
     * 修改用户
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
     * 合适人群
     */
    private String fitage;

    /**
     * 文化馆id
     */
    private String cultid;

    /**
     * 名称
     */
    private String name;

    /**
     * logo图片
     */
    private String logo;

    /**
     * 背景图片
     */
    private String image;

    /**
     * 是否推荐
     */
    private Integer recommend;

    /**
     * 微专业类型
     */
    private String etype;

    /**
     * 微专业介绍
     */
    private String detail;

    /**
     * 获取微专业ID
     *
     * @return id - 微专业ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置微专业ID
     *
     * @param id 微专业ID
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取状态（参考状态枚举）
     *
     * @return state - 状态（参考状态枚举）
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态（参考状态枚举）
     *
     * @param state 状态（参考状态枚举）
     */
    public void setState(Integer state) {
        this.state = state;
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
     * 获取创建用户
     *
     * @return crtuser - 创建用户
     */
    public String getCrtuser() {
        return crtuser;
    }

    /**
     * 设置创建用户
     *
     * @param crtuser 创建用户
     */
    public void setCrtuser(String crtuser) {
        this.crtuser = crtuser == null ? null : crtuser.trim();
    }

    /**
     * 获取修改状态时间
     *
     * @return statemdfdate - 修改状态时间
     */
    public Date getStatemdfdate() {
        return statemdfdate;
    }

    /**
     * 设置修改状态时间
     *
     * @param statemdfdate 修改状态时间
     */
    public void setStatemdfdate(Date statemdfdate) {
        this.statemdfdate = statemdfdate;
    }

    /**
     * 获取修改用户
     *
     * @return statemdfuser - 修改用户
     */
    public String getStatemdfuser() {
        return statemdfuser;
    }

    /**
     * 设置修改用户
     *
     * @param statemdfuser 修改用户
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
     * 获取logo图片
     *
     * @return logo - logo图片
     */
    public String getLogo() {
        return logo;
    }

    /**
     * 设置logo图片
     *
     * @param logo logo图片
     */
    public void setLogo(String logo) {
        this.logo = logo == null ? null : logo.trim();
    }

    /**
     * 获取背景图片
     *
     * @return image - 背景图片
     */
    public String getImage() {
        return image;
    }

    /**
     * 设置背景图片
     *
     * @param image 背景图片
     */
    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    /**
     * 获取微专业类型
     *
     * @return etype - 微专业类型
     */
    public String getEtype() {
        return etype;
    }

    /**
     * 设置微专业类型
     *
     * @param etype 微专业类型
     */
    public void setEtype(String etype) {
        this.etype = etype == null ? null : etype.trim();
    }

    /**
     * 获取微专业介绍
     *
     * @return detail - 微专业介绍
     */
    public String getDetail() {
        return detail;
    }

    /**
     * 设置微专业介绍
     *
     * @param detail 微专业介绍
     */
    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public Integer getRecommend() {
        return recommend;
    }

    public void setRecommend(Integer recommend) {
        this.recommend = recommend;
    }

    public String getFitage() {
        return fitage;
    }

    public void setFitage(String fitage) {
        this.fitage = fitage;
    }
}