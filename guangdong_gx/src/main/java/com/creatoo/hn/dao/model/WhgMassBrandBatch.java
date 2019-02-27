package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_mass_brand_batch")
public class WhgMassBrandBatch {
    /**
     * PK
     */
    @Id
    private String id;

    /**
     * 文化专题ID
     */
    private String brandid;

    /**
     * 创建时间
     */
    private Date crtdate;

    /**
     * 创建人
     */
    private String crtuser;

    /**
     * 状态：1可编辑，9待审核，2待发布，6已发布，4已下架
     */
    private Integer state;

    /**
     * 状态更时间
     */
    private Date statemdfdate;

    /**
     * 状态变更人
     */
    private String statemdfuser;

    /**
     * 是否推荐：0否1是
     */
    private Integer recommend;

    /**
     * 删除状态：0未删除，1已删除
     */
    private Integer delstate;

    /**
     * 所属文化馆
     */
    private String cultid;

    /**
     * 所属部门
     */
    private String deptid;

    /**
     * 所属场馆
     */
    private String venid;

    /**
     * 名称
     */
    private String title;

    /**
     * 图片
     */
    private String imgurl;

    /**
     * 主办日期
     */
    private Date batdate;

    /**
     * 主办单位
     */
    private String sponsor;

    /**
     * 承办单位
     */
    private String organizer;

    /**
     * 协办单位
     */
    private String cosponsor;

    /**
     * 演出单位
     */
    private String deduce;

    /**
     * 简介
     */
    private String summary;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区域
     */
    private String area;

    /**
     * 审核人标识
     */
    private String checkor;

    /**
     * 审核时间
     */
    private Date checkdate;

    /**
     * 发布人标识
     */
    private String publisher;

    /**
     * 发布时间
     */
    private Date publishdate;

    /**
     * 获取PK
     *
     * @return id - PK
     */
    public String getId() {
        return id;
    }

    /**
     * 设置PK
     *
     * @param id PK
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取文化专题ID
     *
     * @return brandid - 文化专题ID
     */
    public String getBrandid() {
        return brandid;
    }

    /**
     * 设置文化专题ID
     *
     * @param brandid 文化专题ID
     */
    public void setBrandid(String brandid) {
        this.brandid = brandid == null ? null : brandid.trim();
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
     * 获取状态更时间
     *
     * @return statemdfdate - 状态更时间
     */
    public Date getStatemdfdate() {
        return statemdfdate;
    }

    /**
     * 设置状态更时间
     *
     * @param statemdfdate 状态更时间
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
     * 获取是否推荐：0否1是
     *
     * @return recommend - 是否推荐：0否1是
     */
    public Integer getRecommend() {
        return recommend;
    }

    /**
     * 设置是否推荐：0否1是
     *
     * @param recommend 是否推荐：0否1是
     */
    public void setRecommend(Integer recommend) {
        this.recommend = recommend;
    }

    /**
     * 获取删除状态：0未删除，1已删除
     *
     * @return delstate - 删除状态：0未删除，1已删除
     */
    public Integer getDelstate() {
        return delstate;
    }

    /**
     * 设置删除状态：0未删除，1已删除
     *
     * @param delstate 删除状态：0未删除，1已删除
     */
    public void setDelstate(Integer delstate) {
        this.delstate = delstate;
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
     * 获取所属部门
     *
     * @return deptid - 所属部门
     */
    public String getDeptid() {
        return deptid;
    }

    /**
     * 设置所属部门
     *
     * @param deptid 所属部门
     */
    public void setDeptid(String deptid) {
        this.deptid = deptid == null ? null : deptid.trim();
    }

    /**
     * 获取所属场馆
     *
     * @return venid - 所属场馆
     */
    public String getVenid() {
        return venid;
    }

    /**
     * 设置所属场馆
     *
     * @param venid 所属场馆
     */
    public void setVenid(String venid) {
        this.venid = venid == null ? null : venid.trim();
    }

    /**
     * 获取名称
     *
     * @return title - 名称
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置名称
     *
     * @param title 名称
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取图片
     *
     * @return imgurl - 图片
     */
    public String getImgurl() {
        return imgurl;
    }

    /**
     * 设置图片
     *
     * @param imgurl 图片
     */
    public void setImgurl(String imgurl) {
        this.imgurl = imgurl == null ? null : imgurl.trim();
    }

    /**
     * 获取主办日期
     *
     * @return batdate - 主办日期
     */
    public Date getBatdate() {
        return batdate;
    }

    /**
     * 设置主办日期
     *
     * @param batdate 主办日期
     */
    public void setBatdate(Date batdate) {
        this.batdate = batdate;
    }

    /**
     * 获取主办单位
     *
     * @return sponsor - 主办单位
     */
    public String getSponsor() {
        return sponsor;
    }

    /**
     * 设置主办单位
     *
     * @param sponsor 主办单位
     */
    public void setSponsor(String sponsor) {
        this.sponsor = sponsor == null ? null : sponsor.trim();
    }

    /**
     * 获取承办单位
     *
     * @return organizer - 承办单位
     */
    public String getOrganizer() {
        return organizer;
    }

    /**
     * 设置承办单位
     *
     * @param organizer 承办单位
     */
    public void setOrganizer(String organizer) {
        this.organizer = organizer == null ? null : organizer.trim();
    }

    /**
     * 获取协办单位
     *
     * @return cosponsor - 协办单位
     */
    public String getCosponsor() {
        return cosponsor;
    }

    /**
     * 设置协办单位
     *
     * @param cosponsor 协办单位
     */
    public void setCosponsor(String cosponsor) {
        this.cosponsor = cosponsor == null ? null : cosponsor.trim();
    }

    /**
     * 获取演出单位
     *
     * @return deduce - 演出单位
     */
    public String getDeduce() {
        return deduce;
    }

    /**
     * 设置演出单位
     *
     * @param deduce 演出单位
     */
    public void setDeduce(String deduce) {
        this.deduce = deduce == null ? null : deduce.trim();
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
     * 获取区域
     *
     * @return area - 区域
     */
    public String getArea() {
        return area;
    }

    /**
     * 设置区域
     *
     * @param area 区域
     */
    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    /**
     * 获取审核人标识
     *
     * @return checkor - 审核人标识
     */
    public String getCheckor() {
        return checkor;
    }

    /**
     * 设置审核人标识
     *
     * @param checkor 审核人标识
     */
    public void setCheckor(String checkor) {
        this.checkor = checkor == null ? null : checkor.trim();
    }

    /**
     * 获取审核时间
     *
     * @return checkdate - 审核时间
     */
    public Date getCheckdate() {
        return checkdate;
    }

    /**
     * 设置审核时间
     *
     * @param checkdate 审核时间
     */
    public void setCheckdate(Date checkdate) {
        this.checkdate = checkdate;
    }

    /**
     * 获取发布人标识
     *
     * @return publisher - 发布人标识
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * 设置发布人标识
     *
     * @param publisher 发布人标识
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher == null ? null : publisher.trim();
    }

    /**
     * 获取发布时间
     *
     * @return publishdate - 发布时间
     */
    public Date getPublishdate() {
        return publishdate;
    }

    /**
     * 设置发布时间
     *
     * @param publishdate 发布时间
     */
    public void setPublishdate(Date publishdate) {
        this.publishdate = publishdate;
    }
}