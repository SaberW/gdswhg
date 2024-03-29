package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_code")
public class WhgCode {
    @Id
    private String id;

    /**
     * 手机号码
     */
    @Column(name = "msgPhone")
    private String msgphone;

    /**
     * 发送时间
     */
    @Column(name = "msgTime")
    private Date msgtime;

    /**
     * 手机验证码
     */
    @Column(name = "msgContent")
    private String msgcontent;

    /**
     * 邮箱验证码
     */
    @Column(name = "emailCode")
    private String emailcode;

    /**
     * 邮箱状态  0：未激活 1：已激活
     */
    @Column(name = "emailState")
    private Integer emailstate;

    /**
     * 邮箱地址
     */
    @Column(name = "emailAddr")
    private String emailaddr;

    /**
     * 会话id
     */
    private String sessid;

    /**
     * 所属文化馆标识
     */
    private String venueid;

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
     * 获取手机号码
     *
     * @return msgPhone - 手机号码
     */
    public String getMsgphone() {
        return msgphone;
    }

    /**
     * 设置手机号码
     *
     * @param msgphone 手机号码
     */
    public void setMsgphone(String msgphone) {
        this.msgphone = msgphone == null ? null : msgphone.trim();
    }

    /**
     * 获取发送时间
     *
     * @return msgTime - 发送时间
     */
    public Date getMsgtime() {
        return msgtime;
    }

    /**
     * 设置发送时间
     *
     * @param msgtime 发送时间
     */
    public void setMsgtime(Date msgtime) {
        this.msgtime = msgtime;
    }

    /**
     * 获取手机验证码
     *
     * @return msgContent - 手机验证码
     */
    public String getMsgcontent() {
        return msgcontent;
    }

    /**
     * 设置手机验证码
     *
     * @param msgcontent 手机验证码
     */
    public void setMsgcontent(String msgcontent) {
        this.msgcontent = msgcontent == null ? null : msgcontent.trim();
    }

    /**
     * 获取邮箱验证码
     *
     * @return emailCode - 邮箱验证码
     */
    public String getEmailcode() {
        return emailcode;
    }

    /**
     * 设置邮箱验证码
     *
     * @param emailcode 邮箱验证码
     */
    public void setEmailcode(String emailcode) {
        this.emailcode = emailcode == null ? null : emailcode.trim();
    }

    /**
     * 获取邮箱状态  0：未激活 1：已激活
     *
     * @return emailState - 邮箱状态  0：未激活 1：已激活
     */
    public Integer getEmailstate() {
        return emailstate;
    }

    /**
     * 设置邮箱状态  0：未激活 1：已激活
     *
     * @param emailstate 邮箱状态  0：未激活 1：已激活
     */
    public void setEmailstate(Integer emailstate) {
        this.emailstate = emailstate;
    }

    /**
     * 获取邮箱地址
     *
     * @return emailAddr - 邮箱地址
     */
    public String getEmailaddr() {
        return emailaddr;
    }

    /**
     * 设置邮箱地址
     *
     * @param emailaddr 邮箱地址
     */
    public void setEmailaddr(String emailaddr) {
        this.emailaddr = emailaddr == null ? null : emailaddr.trim();
    }

    /**
     * 获取会话id
     *
     * @return sessid - 会话id
     */
    public String getSessid() {
        return sessid;
    }

    /**
     * 设置会话id
     *
     * @param sessid 会话id
     */
    public void setSessid(String sessid) {
        this.sessid = sessid == null ? null : sessid.trim();
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
}