package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_gather_order")
public class WhgGatherOrder {
    /**
     * PK
     */
    @Id
    private String id;

    /**
     * 众筹类型：4活动，5培训，0其它
     */
    private String gathertype;

    /**
     * 众筹id
     */
    private String gatherid;

    /**
     * 用户id
     */
    private String userid;

    /**
     * 姓名
     */
    private String name;

    /**
     * 证件类型：1身份证
     */
    private Integer caedtype;

    /**
     * 证件号
     */
    private String caednumber;

    /**
     * 出生年月
     */
    private String birthday;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 订单状态：1有效，0无效
     */
    private Integer state;

    /**
     * 订单号
     */
    private String orderid;

    /**
     * 订单生成时间
     */
    private Date crtdate;

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
     * 获取众筹类型：4活动，5培训，0其它
     *
     * @return gathertype - 众筹类型：4活动，5培训，0其它
     */
    public String getGathertype() {
        return gathertype;
    }

    /**
     * 设置众筹类型：4活动，5培训，0其它
     *
     * @param gathertype 众筹类型：4活动，5培训，0其它
     */
    public void setGathertype(String gathertype) {
        this.gathertype = gathertype == null ? null : gathertype.trim();
    }

    /**
     * 获取众筹id
     *
     * @return gatherid - 众筹id
     */
    public String getGatherid() {
        return gatherid;
    }

    /**
     * 设置众筹id
     *
     * @param gatherid 众筹id
     */
    public void setGatherid(String gatherid) {
        this.gatherid = gatherid == null ? null : gatherid.trim();
    }

    /**
     * 获取用户id
     *
     * @return userid - 用户id
     */
    public String getUserid() {
        return userid;
    }

    /**
     * 设置用户id
     *
     * @param userid 用户id
     */
    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
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
     * 获取证件类型：1身份证
     *
     * @return caedtype - 证件类型：1身份证
     */
    public Integer getCaedtype() {
        return caedtype;
    }

    /**
     * 设置证件类型：1身份证
     *
     * @param caedtype 证件类型：1身份证
     */
    public void setCaedtype(Integer caedtype) {
        this.caedtype = caedtype;
    }

    /**
     * 获取证件号
     *
     * @return caednumber - 证件号
     */
    public String getCaednumber() {
        return caednumber;
    }

    /**
     * 设置证件号
     *
     * @param caednumber 证件号
     */
    public void setCaednumber(String caednumber) {
        this.caednumber = caednumber == null ? null : caednumber.trim();
    }

    /**
     * 获取出生年月
     *
     * @return birthday - 出生年月
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * 设置出生年月
     *
     * @param birthday 出生年月
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday == null ? null : birthday.trim();
    }

    /**
     * 获取手机号码
     *
     * @return phone - 手机号码
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机号码
     *
     * @param phone 手机号码
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 获取订单状态：1有效，0无效
     *
     * @return state - 订单状态：1有效，0无效
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置订单状态：1有效，0无效
     *
     * @param state 订单状态：1有效，0无效
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取订单号
     *
     * @return orderid - 订单号
     */
    public String getOrderid() {
        return orderid;
    }

    /**
     * 设置订单号
     *
     * @param orderid 订单号
     */
    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

    /**
     * 获取订单生成时间
     *
     * @return crtdate - 订单生成时间
     */
    public Date getCrtdate() {
        return crtdate;
    }

    /**
     * 设置订单生成时间
     *
     * @param crtdate 订单生成时间
     */
    public void setCrtdate(Date crtdate) {
        this.crtdate = crtdate;
    }
}