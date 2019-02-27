package com.creatoo.hn.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/24.
 */
public class WhgActOrderExcel {

    /**
     * 活动ID
     */
    @Id
    private String id;

    /**
     * 活动名称
     */
    private String actname;

    /**
     * 订单ID
     */
    private String orderid;

    /**
     * 活动开始时间
     */
    private String starttime;

    /**
     * 活动结束时间
     */
    private String endtime;

    /**
     * 场次开始时间
     */
    private String playstime;

    /**
     * 场次结束时间
     */
    private String playetime;

    /**
     * 短信发送状态
     */
    private Integer ordersmsstate;

    /**
     * 状态.1：未取票，2：已取票，3：已取消.
     */
    private Integer state;

    /**
     * 报名的会员ID
     */
    private String userid;

    /**
     * 报名的会员姓名
     */
    private String username;

    /**
     * 报名会员手机号
     */
    private String orderphoneno;

    /**
     * 预定时间
     */
    private String ordercreatetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActname() {
        return actname;
    }

    public void setActname(String actname) {
        this.actname = actname;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getOrderphoneno() {
        return orderphoneno;
    }

    public void setOrderphoneno(String orderphoneno) {
        this.orderphoneno = orderphoneno;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlaystime() {
        return playstime;
    }

    public void setPlaystime(String playstime) {
        this.playstime = playstime;
    }

    public String getPlayetime() {
        return playetime;
    }

    public void setPlayetime(String playetime) {
        this.playetime = playetime;
    }

    public Integer getOrdersmsstate() {
        return ordersmsstate;
    }

    public void setOrdersmsstate(Integer ordersmsstate) {
        this.ordersmsstate = ordersmsstate;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getOrdercreatetime() {
        return ordercreatetime;
    }

    public void setOrdercreatetime(String ordercreatetime) {
        this.ordercreatetime = ordercreatetime;
    }
}
