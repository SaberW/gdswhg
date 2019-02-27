package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_user")
public class WhgUser {
    @Id
    private String id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 最后登录时间
     */
    private Date lastdate;

    /**
     * 性别 0：女  1：男
     */
    private String sex;

    /**
     * 职业
     */
    private String job;

    /**
     * 出生日期
     */
    private Date birthday;

    /**
     * qq账号
     */
    private String qq;

    /**
     * 微信账号
     */
    private String wx;

    /**
     * 注册密码
     */
    private String password;

    /**
     * 民族
     */
    private String nation;

    /**
     * 籍贯
     */
    private String origo;

    /**
     * 工作单位
     */
    private String company;

    /**
     * 通讯地址
     */
    private String address;

    /**
     * 个人简历
     */
    private String resume;

    /**
     * 从事文艺活动简介
     */
    @Column(name = "actBrief")
    private String actbrief;

    /**
     * 是否实名 0-待完善; 1-已认证;  2-认证失败; 3-提交审核中
     */
    private Integer isrealname;

    /**
     * 是否完善资料 0：否 1：是
     */
    private Integer isperfect;

    /**
     * 身份证号码
     */
    private String idcard;

    /**
     * 身份证正面图
     */
    private String idcardface;

    /**
     * 身份证背面
     */
    private String idcardback;

    /**
     * 实名审核被打回的消息
     */
    private String checkmsg;

    /**
     * QQ用户唯一标识
     */
    private String openid;

    /**
     * 所属文化馆标识
     */
    private String venueid;

    /**
     * 用户的微博标识
     */
    private String wid;

    /**
     * 微信登录标识
     */
    private String wxopenid;

    /**
     * 注册时间
     */
    private Date registtime;

    private String headurl;

    /**
     * 身份证类型 1：中国大陆 2：港澳台
     */
    private Integer idcardtype;

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
     * 获取昵称
     *
     * @return nickname - 昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置昵称
     *
     * @param nickname 昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    /**
     * 获取电话号码
     *
     * @return phone - 电话号码
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置电话号码
     *
     * @param phone 电话号码
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 获取电子邮件
     *
     * @return email - 电子邮件
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置电子邮件
     *
     * @param email 电子邮件
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
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
     * 获取职业
     *
     * @return job - 职业
     */
    public String getJob() {
        return job;
    }

    /**
     * 设置职业
     *
     * @param job 职业
     */
    public void setJob(String job) {
        this.job = job == null ? null : job.trim();
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
     * 获取qq账号
     *
     * @return qq - qq账号
     */
    public String getQq() {
        return qq;
    }

    /**
     * 设置qq账号
     *
     * @param qq qq账号
     */
    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    /**
     * 获取微信账号
     *
     * @return wx - 微信账号
     */
    public String getWx() {
        return wx;
    }

    /**
     * 设置微信账号
     *
     * @param wx 微信账号
     */
    public void setWx(String wx) {
        this.wx = wx == null ? null : wx.trim();
    }

    /**
     * 获取注册密码
     *
     * @return password - 注册密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置注册密码
     *
     * @param password 注册密码
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 获取民族
     *
     * @return nation - 民族
     */
    public String getNation() {
        return nation;
    }

    /**
     * 设置民族
     *
     * @param nation 民族
     */
    public void setNation(String nation) {
        this.nation = nation == null ? null : nation.trim();
    }

    /**
     * 获取籍贯
     *
     * @return origo - 籍贯
     */
    public String getOrigo() {
        return origo;
    }

    /**
     * 设置籍贯
     *
     * @param origo 籍贯
     */
    public void setOrigo(String origo) {
        this.origo = origo == null ? null : origo.trim();
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
     * 获取通讯地址
     *
     * @return address - 通讯地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置通讯地址
     *
     * @param address 通讯地址
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * 获取个人简历
     *
     * @return resume - 个人简历
     */
    public String getResume() {
        return resume;
    }

    /**
     * 设置个人简历
     *
     * @param resume 个人简历
     */
    public void setResume(String resume) {
        this.resume = resume == null ? null : resume.trim();
    }

    /**
     * 获取从事文艺活动简介
     *
     * @return actBrief - 从事文艺活动简介
     */
    public String getActbrief() {
        return actbrief;
    }

    /**
     * 设置从事文艺活动简介
     *
     * @param actbrief 从事文艺活动简介
     */
    public void setActbrief(String actbrief) {
        this.actbrief = actbrief == null ? null : actbrief.trim();
    }

    /**
     * 获取是否实名 0-待完善; 1-已认证;  2-认证失败; 3-提交审核中
     *
     * @return isrealname - 是否实名 0-待完善; 1-已认证;  2-认证失败; 3-提交审核中
     */
    public Integer getIsrealname() {
        return isrealname;
    }

    /**
     * 设置是否实名 0-待完善; 1-已认证;  2-认证失败; 3-提交审核中
     *
     * @param isrealname 是否实名 0-待完善; 1-已认证;  2-认证失败; 3-提交审核中
     */
    public void setIsrealname(Integer isrealname) {
        this.isrealname = isrealname;
    }

    /**
     * 获取是否完善资料 0：否 1：是
     *
     * @return isperfect - 是否完善资料 0：否 1：是
     */
    public Integer getIsperfect() {
        return isperfect;
    }

    /**
     * 设置是否完善资料 0：否 1：是
     *
     * @param isperfect 是否完善资料 0：否 1：是
     */
    public void setIsperfect(Integer isperfect) {
        this.isperfect = isperfect;
    }

    /**
     * 获取身份证号码
     *
     * @return idcard - 身份证号码
     */
    public String getIdcard() {
        return idcard;
    }

    /**
     * 设置身份证号码
     *
     * @param idcard 身份证号码
     */
    public void setIdcard(String idcard) {
        this.idcard = idcard == null ? null : idcard.trim();
    }

    /**
     * 获取身份证正面图
     *
     * @return idcardface - 身份证正面图
     */
    public String getIdcardface() {
        return idcardface;
    }

    /**
     * 设置身份证正面图
     *
     * @param idcardface 身份证正面图
     */
    public void setIdcardface(String idcardface) {
        this.idcardface = idcardface == null ? null : idcardface.trim();
    }

    /**
     * 获取身份证背面
     *
     * @return idcardback - 身份证背面
     */
    public String getIdcardback() {
        return idcardback;
    }

    /**
     * 设置身份证背面
     *
     * @param idcardback 身份证背面
     */
    public void setIdcardback(String idcardback) {
        this.idcardback = idcardback == null ? null : idcardback.trim();
    }

    /**
     * 获取实名审核被打回的消息
     *
     * @return checkmsg - 实名审核被打回的消息
     */
    public String getCheckmsg() {
        return checkmsg;
    }

    /**
     * 设置实名审核被打回的消息
     *
     * @param checkmsg 实名审核被打回的消息
     */
    public void setCheckmsg(String checkmsg) {
        this.checkmsg = checkmsg == null ? null : checkmsg.trim();
    }

    /**
     * 获取QQ用户唯一标识
     *
     * @return openid - QQ用户唯一标识
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * 设置QQ用户唯一标识
     *
     * @param openid QQ用户唯一标识
     */
    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    /**
     * 获取所属文化馆标识
     *
     * @return venueid - 所属文化馆标识
     */
    public String getVenueid() {
        return venueid;
    }

    /**
     * 设置所属文化馆标识
     *
     * @param venueid 所属文化馆标识
     */
    public void setVenueid(String venueid) {
        this.venueid = venueid == null ? null : venueid.trim();
    }

    /**
     * 获取用户的微博标识
     *
     * @return wid - 用户的微博标识
     */
    public String getWid() {
        return wid;
    }

    /**
     * 设置用户的微博标识
     *
     * @param wid 用户的微博标识
     */
    public void setWid(String wid) {
        this.wid = wid == null ? null : wid.trim();
    }

    /**
     * 获取微信登录标识
     *
     * @return wxopenid - 微信登录标识
     */
    public String getWxopenid() {
        return wxopenid;
    }

    /**
     * 设置微信登录标识
     *
     * @param wxopenid 微信登录标识
     */
    public void setWxopenid(String wxopenid) {
        this.wxopenid = wxopenid == null ? null : wxopenid.trim();
    }

    /**
     * 获取注册时间
     *
     * @return registtime - 注册时间
     */
    public Date getRegisttime() {
        return registtime;
    }

    /**
     * 设置注册时间
     *
     * @param registtime 注册时间
     */
    public void setRegisttime(Date registtime) {
        this.registtime = registtime;
    }

    /**
     * @return headurl
     */
    public String getHeadurl() {
        return headurl;
    }

    /**
     * @param headurl
     */
    public void setHeadurl(String headurl) {
        this.headurl = headurl == null ? null : headurl.trim();
    }

    /**
     * 获取身份证类型 1：中国大陆 2：港澳台
     *
     * @return idcardtype - 身份证类型 1：中国大陆 2：港澳台
     */
    public Integer getIdcardtype() {
        return idcardtype;
    }

    /**
     * 设置身份证类型 1：中国大陆 2：港澳台
     *
     * @param idcardtype 身份证类型 1：中国大陆 2：港澳台
     */
    public void setIdcardtype(Integer idcardtype) {
        this.idcardtype = idcardtype;
    }
}