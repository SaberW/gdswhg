package com.creatoo.hn.dao.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_fyi_expert")
public class WhgFyiExpert {
    /**
     * 专家ID
     */
    @Id
    private String id;

    /**
     * 专家姓名
     */
    private String name;

    /**
     * 图片
     */
    private String image;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 出生日期
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date birthday;

    /**
     * 工作单位
     */
    private String workunit;

    /**
     * 职务
     */
    private String job;

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
     * 年代
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date years;

    /**
     * 艺术分类
     */
    private String arttype;

    /**
     * 所属机构
     */
    private String cultid;

    /**
     * 专长类型
     */
    private String etype;

    /**
     * 简介
     */
    private String introduce;

    /**
     * 创建时间
     */
    private Date crtdate;

    /**
     * 创建人
     */
    private String crtuser;

    /**
     * 状态变更时间
     */
    private Date statemdfdate;

    /**
     * 状态变更用户
     */
    private String statemdfuser;

    /**
     * 工作经历
     */
    private String workexper;

    /**
     * 获奖情况
     */
    private String awards;

    /**
     * 获取专家ID
     *
     * @return id - 专家ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置专家ID
     *
     * @param id 专家ID
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取专家姓名
     *
     * @return name - 专家姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置专家姓名
     *
     * @param name 专家姓名
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取图片
     *
     * @return image - 图片
     */
    public String getImage() {
        return image;
    }

    /**
     * 设置图片
     *
     * @param image 图片
     */
    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    /**
     * 获取性别
     *
     * @return sex - 性别
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * 设置性别
     *
     * @param sex 性别
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * 获取出生日期
     *
     * @return birthday - 出生日期
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * 设置出生日期
     *
     * @param birthday 出生日期
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * 获取工作单位
     *
     * @return workunit - 工作单位
     */
    public String getWorkunit() {
        return workunit;
    }

    /**
     * 设置工作单位
     *
     * @param workunit 工作单位
     */
    public void setWorkunit(String workunit) {
        this.workunit = workunit == null ? null : workunit.trim();
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
     * 获取年代
     *
     * @return years - 年代
     */
    public Date getYears() {
        return years;
    }

    /**
     * 设置年代
     *
     * @param years 年代
     */
    public void setYears(Date years) {
        this.years = years;
    }

    /**
     * 获取艺术分类
     *
     * @return arttype - 艺术分类
     */
    public String getArttype() {
        return arttype;
    }

    /**
     * 设置艺术分类
     *
     * @param arttype 艺术分类
     */
    public void setArttype(String arttype) {
        this.arttype = arttype == null ? null : arttype.trim();
    }

    /**
     * 获取所属机构
     *
     * @return cultid - 所属机构
     */
    public String getCultid() {
        return cultid;
    }

    /**
     * 设置所属机构
     *
     * @param cultid 所属机构
     */
    public void setCultid(String cultid) {
        this.cultid = cultid == null ? null : cultid.trim();
    }

    /**
     * 获取专长类型
     *
     * @return etype - 专长类型
     */
    public String getEtype() {
        return etype;
    }

    /**
     * 设置专长类型
     *
     * @param etype 专长类型
     */
    public void setEtype(String etype) {
        this.etype = etype == null ? null : etype.trim();
    }

    /**
     * 获取简介
     *
     * @return introduce - 简介
     */
    public String getIntroduce() {
        return introduce;
    }

    /**
     * 设置简介
     *
     * @param introduce 简介
     */
    public void setIntroduce(String introduce) {
        this.introduce = introduce == null ? null : introduce.trim();
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
     * 获取状态变更用户
     *
     * @return statemdfuser - 状态变更用户
     */
    public String getStatemdfuser() {
        return statemdfuser;
    }

    /**
     * 设置状态变更用户
     *
     * @param statemdfuser 状态变更用户
     */
    public void setStatemdfuser(String statemdfuser) {
        this.statemdfuser = statemdfuser == null ? null : statemdfuser.trim();
    }

    /**
     * 获取工作经历
     *
     * @return workexper - 工作经历
     */
    public String getWorkexper() {
        return workexper;
    }

    /**
     * 设置工作经历
     *
     * @param workexper 工作经历
     */
    public void setWorkexper(String workexper) {
        this.workexper = workexper == null ? null : workexper.trim();
    }

    /**
     * 获取获奖情况
     *
     * @return awards - 获奖情况
     */
    public String getAwards() {
        return awards;
    }

    /**
     * 设置获奖情况
     *
     * @param awards 获奖情况
     */
    public void setAwards(String awards) {
        this.awards = awards == null ? null : awards.trim();
    }
}