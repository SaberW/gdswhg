package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_sys_user")
public class WhgSysUser {
    /**
     * 主键
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
     * 状态. （参考开关状态枚举）
     */
    private Integer state;

    /**
     * 修改状态的时间
     */
    private Date statemdfdate;

    /**
     * 修改状态的用户
     */
    private String statemdfuser;

    /**
     * 删除状态
     */
    private Integer delstate;

    /**
     * 登录账号
     */
    private String account;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 密码说明
     */
    @Column(name = "password_memo")
    private String passwordMemo;

    /**
     * 手机号
     */
    private String contactnum;

    /**
     * 最后登录时间
     */
    private Date lastdate;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 文化馆ID
     */
    private String cultid;

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
     * 获取状态. （参考开关状态枚举）
     *
     * @return state - 状态. （参考开关状态枚举）
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态. （参考开关状态枚举）
     *
     * @param state 状态. （参考开关状态枚举）
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取修改状态的时间
     *
     * @return statemdfdate - 修改状态的时间
     */
    public Date getStatemdfdate() {
        return statemdfdate;
    }

    /**
     * 设置修改状态的时间
     *
     * @param statemdfdate 修改状态的时间
     */
    public void setStatemdfdate(Date statemdfdate) {
        this.statemdfdate = statemdfdate;
    }

    /**
     * 获取修改状态的用户
     *
     * @return statemdfuser - 修改状态的用户
     */
    public String getStatemdfuser() {
        return statemdfuser;
    }

    /**
     * 设置修改状态的用户
     *
     * @param statemdfuser 修改状态的用户
     */
    public void setStatemdfuser(String statemdfuser) {
        this.statemdfuser = statemdfuser == null ? null : statemdfuser.trim();
    }

    /**
     * 获取删除状态
     *
     * @return delstate - 删除状态
     */
    public Integer getDelstate() {
        return delstate;
    }

    /**
     * 设置删除状态
     *
     * @param delstate 删除状态
     */
    public void setDelstate(Integer delstate) {
        this.delstate = delstate;
    }

    /**
     * 获取登录账号
     *
     * @return account - 登录账号
     */
    public String getAccount() {
        return account;
    }

    /**
     * 设置登录账号
     *
     * @param account 登录账号
     */
    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    /**
     * 获取登录密码
     *
     * @return password - 登录密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置登录密码
     *
     * @param password 登录密码
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 获取密码说明
     *
     * @return password_memo - 密码说明
     */
    public String getPasswordMemo() {
        return passwordMemo;
    }

    /**
     * 设置密码说明
     *
     * @param passwordMemo 密码说明
     */
    public void setPasswordMemo(String passwordMemo) {
        this.passwordMemo = passwordMemo == null ? null : passwordMemo.trim();
    }

    /**
     * 获取手机号
     *
     * @return contactnum - 手机号
     */
    public String getContactnum() {
        return contactnum;
    }

    /**
     * 设置手机号
     *
     * @param contactnum 手机号
     */
    public void setContactnum(String contactnum) {
        this.contactnum = contactnum == null ? null : contactnum.trim();
    }

    /**
     * 获取最后登录时间
     *
     * @return lastdate - 最后登录时间
     */
    public Date getLastdate() {
        return lastdate;
    }

    /**
     * 设置最后登录时间
     *
     * @param lastdate 最后登录时间
     */
    public void setLastdate(Date lastdate) {
        this.lastdate = lastdate;
    }

    /**
     * 获取邮箱
     *
     * @return email - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 获取联系人
     *
     * @return contact - 联系人
     */
    public String getContact() {
        return contact;
    }

    /**
     * 设置联系人
     *
     * @param contact 联系人
     */
    public void setContact(String contact) {
        this.contact = contact == null ? null : contact.trim();
    }

    /**
     * 获取文化馆ID
     *
     * @return cultid - 文化馆ID
     */
    public String getCultid() {
        return cultid;
    }

    /**
     * 设置文化馆ID
     *
     * @param cultid 文化馆ID
     */
    public void setCultid(String cultid) {
        this.cultid = cultid == null ? null : cultid.trim();
    }
}