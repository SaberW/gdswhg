package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_show_organ")
public class WhgShowOrgan {
    /**
     * ID
     */
    @Id
    private String id;

    /**
     * 创建时间
     */
    private Date crtdate;

    /**
     * 创建用户
     */
    private String crtuser;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 状态变更时间
     */
    private Date statemdfdate;

    /**
     * 状态变更用户
     */
    private String statemdfuser;

    /**
     * 机构名称
     */
    private String title;

    /**
     * 法人
     */
    private String legalperson;

    /**
     * 封面图片
     */
    private String image;

    /**
     * 机构组织机构或者法人证书
     */
    private String certificate;

    /**
     * 所属文化馆
     */
    private String cultid;

    /**
     * 地址
     */
    private String address;

    /**
     * 负责人
     */
    private String contacts;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 机构简介
     */
    private String organdesc;

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
     * 获取状态
     *
     * @return state - 状态
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态
     *
     * @param state 状态
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
     * 获取机构名称
     *
     * @return title - 机构名称
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置机构名称
     *
     * @param title 机构名称
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取法人
     *
     * @return legalperson - 法人
     */
    public String getLegalperson() {
        return legalperson;
    }

    /**
     * 设置法人
     *
     * @param legalperson 法人
     */
    public void setLegalperson(String legalperson) {
        this.legalperson = legalperson == null ? null : legalperson.trim();
    }

    /**
     * @return certificate
     */
    public String getCertificate() {
        return certificate;
    }

    /**
     * @param certificate
     */
    public void setCertificate(String certificate) {
        this.certificate = certificate == null ? null : certificate.trim();
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
     * 获取负责人
     *
     * @return contacts - 负责人
     */
    public String getContacts() {
        return contacts;
    }

    /**
     * 设置负责人
     *
     * @param contacts 负责人
     */
    public void setContacts(String contacts) {
        this.contacts = contacts == null ? null : contacts.trim();
    }

    /**
     * 获取联系方式
     *
     * @return phone - 联系方式
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置联系方式
     *
     * @param phone 联系方式
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 获取机构简介
     *
     * @return organdesc - 机构简介
     */
    public String getOrgandesc() {
        return organdesc;
    }

    /**
     * 设置机构简介
     *
     * @param organdesc 机构简介
     */
    public void setOrgandesc(String organdesc) {
        this.organdesc = organdesc == null ? null : organdesc.trim();
    }

    public String getCultid() {
        return cultid;
    }

    public void setCultid(String cultid) {
        this.cultid = cultid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}