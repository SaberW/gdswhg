package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_personnel")
public class WhgPersonnel {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 编号
     */
    private String no;

    /**
     * 人才姓名
     */
    private String name;

    /**
     * 性别：0 女 1男
     */
    private Integer sex;

    /**
     * 地址
     */
    private String address;

    /**
     * 身份证号码
     */
    private String cardno;

    /**
     * 出生年月日
     */
    private String birthstr;

    /**
     * 民族
     */
    private String family;

    /**
     * 人才类型：专家，普通。。
     */
    private String type;

    /**
     * 获奖情况
     */
    private String hjqk;

    /**
     * 艺术门类
     */
    private String styletype;

    /**
     * 关键字
     */
    private String mainkey;

    /**
     * 标签
     */
    private String label;

    /**
     * 电话号码
     */
    private String phoneno;

    /**
     * 工作职务
     */
    private String job;

    /**
     * 人才照片
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
     * 创建user
     */
    private String crtuser;

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
     * 人才简介
     */
    private String summary;

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
     * 获取编号
     *
     * @return no - 编号
     */
    public String getNo() {
        return no;
    }

    /**
     * 设置编号
     *
     * @param no 编号
     */
    public void setNo(String no) {
        this.no = no == null ? null : no.trim();
    }

    /**
     * 获取人才姓名
     *
     * @return name - 人才姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置人才姓名
     *
     * @param name 人才姓名
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取性别：0 女 1男
     *
     * @return sex - 性别：0 女 1男
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * 设置性别：0 女 1男
     *
     * @param sex 性别：0 女 1男
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * 获取地址
     *
     * @return address - 地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置地址
     *
     * @param address 地址
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * 获取身份证号码
     *
     * @return cardno - 身份证号码
     */
    public String getCardno() {
        return cardno;
    }

    /**
     * 设置身份证号码
     *
     * @param cardno 身份证号码
     */
    public void setCardno(String cardno) {
        this.cardno = cardno == null ? null : cardno.trim();
    }

    /**
     * 获取出生年月日
     *
     * @return birthstr - 出生年月日
     */
    public String getBirthstr() {
        return birthstr;
    }

    /**
     * 设置出生年月日
     *
     * @param birthstr 出生年月日
     */
    public void setBirthstr(String birthstr) {
        this.birthstr = birthstr == null ? null : birthstr.trim();
    }

    /**
     * 获取民族
     *
     * @return family - 民族
     */
    public String getFamily() {
        return family;
    }

    /**
     * 设置民族
     *
     * @param family 民族
     */
    public void setFamily(String family) {
        this.family = family == null ? null : family.trim();
    }

    /**
     * 获取人才类型：专家，普通。。
     *
     * @return type - 人才类型：专家，普通。。
     */
    public String getType() {
        return type;
    }

    /**
     * 设置人才类型：专家，普通。。
     *
     * @param type 人才类型：专家，普通。。
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * 获取获奖情况
     *
     * @return hjqk - 获奖情况
     */
    public String getHjqk() {
        return hjqk;
    }

    /**
     * 设置获奖情况
     *
     * @param hjqk 获奖情况
     */
    public void setHjqk(String hjqk) {
        this.hjqk = hjqk == null ? null : hjqk.trim();
    }

    /**
     * 获取艺术门类
     *
     * @return styletype - 艺术门类
     */
    public String getStyletype() {
        return styletype;
    }

    /**
     * 设置艺术门类
     *
     * @param styletype 艺术门类
     */
    public void setStyletype(String styletype) {
        this.styletype = styletype == null ? null : styletype.trim();
    }

    /**
     * 获取关键字
     *
     * @return mainkey - 关键字
     */
    public String getMainkey() {
        return mainkey;
    }

    /**
     * 设置关键字
     *
     * @param mainkey 关键字
     */
    public void setMainkey(String mainkey) {
        this.mainkey = mainkey == null ? null : mainkey.trim();
    }

    /**
     * 获取标签
     *
     * @return label - 标签
     */
    public String getLabel() {
        return label;
    }

    /**
     * 设置标签
     *
     * @param label 标签
     */
    public void setLabel(String label) {
        this.label = label == null ? null : label.trim();
    }

    /**
     * 获取电话号码
     *
     * @return phoneno - 电话号码
     */
    public String getPhoneno() {
        return phoneno;
    }

    /**
     * 设置电话号码
     *
     * @param phoneno 电话号码
     */
    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno == null ? null : phoneno.trim();
    }

    /**
     * 获取工作职务
     *
     * @return job - 工作职务
     */
    public String getJob() {
        return job;
    }

    /**
     * 设置工作职务
     *
     * @param job 工作职务
     */
    public void setJob(String job) {
        this.job = job == null ? null : job.trim();
    }

    /**
     * 获取人才照片
     *
     * @return picture - 人才照片
     */
    public String getPicture() {
        return picture;
    }

    /**
     * 设置人才照片
     *
     * @param picture 人才照片
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
     * 获取创建user
     *
     * @return crtuser - 创建user
     */
    public String getCrtuser() {
        return crtuser;
    }

    /**
     * 设置创建user
     *
     * @param crtuser 创建user
     */
    public void setCrtuser(String crtuser) {
        this.crtuser = crtuser == null ? null : crtuser.trim();
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
     * 获取人才简介
     *
     * @return summary - 人才简介
     */
    public String getSummary() {
        return summary;
    }

    /**
     * 设置人才简介
     *
     * @param summary 人才简介
     */
    public void setSummary(String summary) {
        this.summary = summary == null ? null : summary.trim();
    }
}