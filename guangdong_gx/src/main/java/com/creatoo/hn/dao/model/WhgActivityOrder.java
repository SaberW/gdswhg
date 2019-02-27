package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_act_order")
public class WhgActivityOrder {
    /**
     * 活动订单主键ID
     */
    @Id
    private String id;

    /**
     * 预定活动ID
     */
    private String activityid;

    /**
     * 订单用户ID
     */
    private String userid;

    /**
     * 订单编号
     */
    private String ordernumber;

    /**
     * 短信发送时间
     */
    private Date ordersmstime;

    /**
     * 短信发送状态 1-未发送 2-发送成功 3-发送失败 
     */
    private Integer ordersmsstate;

    /**
     * 预定人姓名
     */
    private String ordername;

    /**
     * 预定人手机号
     */
    private String orderphoneno;

    /**
     * 订单是否有效(1-有效 2-无效 3撤销)
     */
    private Integer orderisvalid;

    /**
     * 订单生成时间
     */
    private Date ordercreatetime;

    /**
     * 活动场次id
     */
    private String eventid;

    /**
     * 取票验证码
     */
    private String ticketcode;

    /**
     * 取票状态。1：未取票，2：已取票，3：已取消
     */
    private Integer ticketstatus;

    /**
     * 取票时间
     */
    private Date printtime;

    /**
     * 取票次数
     */
    private Integer printtickettimes;

    /**
     * 发送短信内容
     */
    private String ordersms;

    /**
     * 订单摘要
     */
    private String ordersummary;

    /**
     * 获取活动订单主键ID
     *
     * @return id - 活动订单主键ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置活动订单主键ID
     *
     * @param id 活动订单主键ID
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取预定活动ID
     *
     * @return activityid - 预定活动ID
     */
    public String getActivityid() {
        return activityid;
    }

    /**
     * 设置预定活动ID
     *
     * @param activityid 预定活动ID
     */
    public void setActivityid(String activityid) {
        this.activityid = activityid == null ? null : activityid.trim();
    }

    /**
     * 获取订单用户ID
     *
     * @return userid - 订单用户ID
     */
    public String getUserid() {
        return userid;
    }

    /**
     * 设置订单用户ID
     *
     * @param userid 订单用户ID
     */
    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    /**
     * 获取订单编号
     *
     * @return ordernumber - 订单编号
     */
    public String getOrdernumber() {
        return ordernumber;
    }

    /**
     * 设置订单编号
     *
     * @param ordernumber 订单编号
     */
    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber == null ? null : ordernumber.trim();
    }

    /**
     * 获取短信发送时间
     *
     * @return ordersmstime - 短信发送时间
     */
    public Date getOrdersmstime() {
        return ordersmstime;
    }

    /**
     * 设置短信发送时间
     *
     * @param ordersmstime 短信发送时间
     */
    public void setOrdersmstime(Date ordersmstime) {
        this.ordersmstime = ordersmstime;
    }

    /**
     * 获取短信发送状态 1-未发送 2-发送成功 3-发送失败 
     *
     * @return ordersmsstate - 短信发送状态 1-未发送 2-发送成功 3-发送失败 
     */
    public Integer getOrdersmsstate() {
        return ordersmsstate;
    }

    /**
     * 设置短信发送状态 1-未发送 2-发送成功 3-发送失败 
     *
     * @param ordersmsstate 短信发送状态 1-未发送 2-发送成功 3-发送失败 
     */
    public void setOrdersmsstate(Integer ordersmsstate) {
        this.ordersmsstate = ordersmsstate;
    }

    /**
     * 获取预定人姓名
     *
     * @return ordername - 预定人姓名
     */
    public String getOrdername() {
        return ordername;
    }

    /**
     * 设置预定人姓名
     *
     * @param ordername 预定人姓名
     */
    public void setOrdername(String ordername) {
        this.ordername = ordername == null ? null : ordername.trim();
    }

    /**
     * 获取预定人手机号
     *
     * @return orderphoneno - 预定人手机号
     */
    public String getOrderphoneno() {
        return orderphoneno;
    }

    /**
     * 设置预定人手机号
     *
     * @param orderphoneno 预定人手机号
     */
    public void setOrderphoneno(String orderphoneno) {
        this.orderphoneno = orderphoneno == null ? null : orderphoneno.trim();
    }

    /**
     * 获取订单是否有效(1-有效 2-无效 3撤销)
     *
     * @return orderisvalid - 订单是否有效(1-有效 2-无效 3撤销)
     */
    public Integer getOrderisvalid() {
        return orderisvalid;
    }

    /**
     * 设置订单是否有效(1-有效 2-无效 3撤销)
     *
     * @param orderisvalid 订单是否有效(1-有效 2-无效 3撤销)
     */
    public void setOrderisvalid(Integer orderisvalid) {
        this.orderisvalid = orderisvalid;
    }

    /**
     * 获取订单生成时间
     *
     * @return ordercreatetime - 订单生成时间
     */
    public Date getOrdercreatetime() {
        return ordercreatetime;
    }

    /**
     * 设置订单生成时间
     *
     * @param ordercreatetime 订单生成时间
     */
    public void setOrdercreatetime(Date ordercreatetime) {
        this.ordercreatetime = ordercreatetime;
    }

    /**
     * 获取活动场次id
     *
     * @return eventid - 活动场次id
     */
    public String getEventid() {
        return eventid;
    }

    /**
     * 设置活动场次id
     *
     * @param eventid 活动场次id
     */
    public void setEventid(String eventid) {
        this.eventid = eventid == null ? null : eventid.trim();
    }

    /**
     * 获取取票验证码
     *
     * @return ticketcode - 取票验证码
     */
    public String getTicketcode() {
        return ticketcode;
    }

    /**
     * 设置取票验证码
     *
     * @param ticketcode 取票验证码
     */
    public void setTicketcode(String ticketcode) {
        this.ticketcode = ticketcode == null ? null : ticketcode.trim();
    }

    /**
     * 获取取票状态。1：未取票，2：已取票，3：已取消
     *
     * @return ticketstatus - 取票状态。1：未取票，2：已取票，3：已取消
     */
    public Integer getTicketstatus() {
        return ticketstatus;
    }

    /**
     * 设置取票状态。1：未取票，2：已取票，3：已取消
     *
     * @param ticketstatus 取票状态。1：未取票，2：已取票，3：已取消
     */
    public void setTicketstatus(Integer ticketstatus) {
        this.ticketstatus = ticketstatus;
    }

    /**
     * 获取取票时间
     *
     * @return printtime - 取票时间
     */
    public Date getPrinttime() {
        return printtime;
    }

    /**
     * 设置取票时间
     *
     * @param printtime 取票时间
     */
    public void setPrinttime(Date printtime) {
        this.printtime = printtime;
    }

    /**
     * 获取取票次数
     *
     * @return printtickettimes - 取票次数
     */
    public Integer getPrinttickettimes() {
        return printtickettimes;
    }

    /**
     * 设置取票次数
     *
     * @param printtickettimes 取票次数
     */
    public void setPrinttickettimes(Integer printtickettimes) {
        this.printtickettimes = printtickettimes;
    }

    /**
     * 获取发送短信内容
     *
     * @return ordersms - 发送短信内容
     */
    public String getOrdersms() {
        return ordersms;
    }

    /**
     * 设置发送短信内容
     *
     * @param ordersms 发送短信内容
     */
    public void setOrdersms(String ordersms) {
        this.ordersms = ordersms == null ? null : ordersms.trim();
    }

    /**
     * 获取订单摘要
     *
     * @return ordersummary - 订单摘要
     */
    public String getOrdersummary() {
        return ordersummary;
    }

    /**
     * 设置订单摘要
     *
     * @param ordersummary 订单摘要
     */
    public void setOrdersummary(String ordersummary) {
        this.ordersummary = ordersummary == null ? null : ordersummary.trim();
    }
}