package com.creatoo.hn.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_sign_data")
public class WhgSignData {
    /**
     * ID
     */
    private String id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 身份证号码
     */
    private String idcard;

    /**
     * 签到时间
     */
    private Date crtdate;

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
        this.id = id;
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
        this.name = name;
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
        this.idcard = idcard;
    }

    /**
     * 获取签到时间
     *
     * @return crtdate - 签到时间
     */
    public Date getCrtdate() {
        return crtdate;
    }

    /**
     * 设置签到时间
     *
     * @param crtdate 签到时间
     */
    public void setCrtdate(Date crtdate) {
        this.crtdate = crtdate;
    }
}