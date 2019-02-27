package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_mass_artist")
public class WhgMassArtist {
    /**
     * PK
     */
    @Id
    private String id;

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
     * 删除状态：0未删除，1已删除
     */
    private Integer delstate;

    /**
     * 所属文化馆
     */
    private String cultid;

    /**
     * 是否推荐：0否1是
     */
    private Integer recommend;

    /**
     * 姓名
     */
    private String name;

    /**
     * 图片
     */
    private String imgurl;

    /**
     * 性别 0：女  1：男
     */
    private String sex;

    /**
     * 出生年月
     */
    private Date birthday;

    /**
     * 工作单位
     */
    private String company;

    /**
     * 职务
     */
    private String job;

    /**
     * 年代
     */
    private String years;

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
     * 所属部门
     */
    private String deptid;

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
     * 艺术类型
     */
    private String arttype;

    /**
     * 专长类型
     */
    private String feattype;

    /**
     * 工作经历
     */
    private String live;

    /**
     * 获奖情况
     */
    private String prize;

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
     * 获取姓名
     *
     * @return name - 姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置姓名
     *
     * @param name 姓名
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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
     * 获取性别 0：女  1：男
     *
     * @return sex - 性别 0：女  1：男
     */
    public String getSex() {
        return sex;
    }

    /**
     * 设置性别 0：女  1：男
     *
     * @param sex 性别 0：女  1：男
     */
    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    /**
     * 获取出生年月
     *
     * @return birthday - 出生年月
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * 设置出生年月
     *
     * @param birthday 出生年月
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * 获取工作单位
     *
     * @return company - 工作单位
     */
    public String getCompany() {
        return company;
    }

    /**
     * 设置工作单位
     *
     * @param company 工作单位
     */
    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }

    /**
     * 获取职务
     *
     * @return job - 职务
     */
    public String getJob() {
        return job;
    }

    /**
     * 设置职务
     *
     * @param job 职务
     */
    public void setJob(String job) {
        this.job = job == null ? null : job.trim();
    }

    /**
     * 获取年代
     *
     * @return years - 年代
     */
    public String getYears() {
        return years;
    }

    /**
     * 设置年代
     *
     * @param years 年代
     */
    public void setYears(String years) {
        this.years = years == null ? null : years.trim();
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

    /**
     * 获取艺术类型
     *
     * @return arttype - 艺术类型
     */
    public String getArttype() {
        return arttype;
    }

    /**
     * 设置艺术类型
     *
     * @param arttype 艺术类型
     */
    public void setArttype(String arttype) {
        this.arttype = arttype == null ? null : arttype.trim();
    }

    /**
     * 获取专长类型
     *
     * @return feattype - 专长类型
     */
    public String getFeattype() {
        return feattype;
    }

    /**
     * 设置专长类型
     *
     * @param feattype 专长类型
     */
    public void setFeattype(String feattype) {
        this.feattype = feattype == null ? null : feattype.trim();
    }

    /**
     * 获取工作经历
     *
     * @return live - 工作经历
     */
    public String getLive() {
        return live;
    }

    /**
     * 设置工作经历
     *
     * @param live 工作经历
     */
    public void setLive(String live) {
        this.live = live == null ? null : live.trim();
    }

    /**
     * 获取获奖情况
     *
     * @return prize - 获奖情况
     */
    public String getPrize() {
        return prize;
    }

    /**
     * 设置获奖情况
     *
     * @param prize 获奖情况
     */
    public void setPrize(String prize) {
        this.prize = prize == null ? null : prize.trim();
    }
}