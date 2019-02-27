package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_ywi_aliysms_senderr")
public class WhgYwiAliysmsSenderr {
    @Id
    private String id;

    /**
     * 目标手机号
     */
    private String phone;

    /**
     * 模板id
     */
    private String tempcode;

    /**
     * 短信发送状态码
     */
    private String code;

    /**
     * 状态码描述
     */
    private String message;

    /**
     * 回执ID
     */
    @Column(name = "bizId")
    private String bizid;

    /**
     * 短信参数
     */
    private String params;

    /**
     * 记录时间
     */
    private Date crtdate;

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
     * 获取目标手机号
     *
     * @return phone - 目标手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置目标手机号
     *
     * @param phone 目标手机号
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 获取模板id
     *
     * @return tempcode - 模板id
     */
    public String getTempcode() {
        return tempcode;
    }

    /**
     * 设置模板id
     *
     * @param tempcode 模板id
     */
    public void setTempcode(String tempcode) {
        this.tempcode = tempcode == null ? null : tempcode.trim();
    }

    /**
     * 获取短信发送状态码
     *
     * @return code - 短信发送状态码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置短信发送状态码
     *
     * @param code 短信发送状态码
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 获取状态码描述
     *
     * @return message - 状态码描述
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置状态码描述
     *
     * @param message 状态码描述
     */
    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }

    /**
     * 获取回执ID
     *
     * @return bizId - 回执ID
     */
    public String getBizid() {
        return bizid;
    }

    /**
     * 设置回执ID
     *
     * @param bizid 回执ID
     */
    public void setBizid(String bizid) {
        this.bizid = bizid == null ? null : bizid.trim();
    }

    /**
     * 获取短信参数
     *
     * @return params - 短信参数
     */
    public String getParams() {
        return params;
    }

    /**
     * 设置短信参数
     *
     * @param params 短信参数
     */
    public void setParams(String params) {
        this.params = params == null ? null : params.trim();
    }

    /**
     * 获取记录时间
     *
     * @return crtdate - 记录时间
     */
    public Date getCrtdate() {
        return crtdate;
    }

    /**
     * 设置记录时间
     *
     * @param crtdate 记录时间
     */
    public void setCrtdate(Date crtdate) {
        this.crtdate = crtdate;
    }
}