package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_brand_act")
public class WhgBrandAct {
    /**
     * 关联表主键
     */
    @Id
    private String bracid;

    /**
     * 活动标识
     */
    private String bracactid;

    /**
     * 活动开始日期
     */
    private Date bracsdate;

    /**
     * 活动开始时间
     */
    private Date bracstime;

    /**
     * 联系电话
     */
    private String bractelephone;

    /**
     * 活动地址
     */
    private String bracaddr;

    /**
     * 状态:0-无效;1-有效
     */
    private Integer bracstate;

    /**
     * 专题活动ID 
     */
    private String braid;

    /**
     * 活动结束时间
     */
    private Date bracedate;

    /**
     * 所属文化馆标识
     */
    private String bravenueid;

    /**
     * 获取关联表主键
     *
     * @return bracid - 关联表主键
     */
    public String getBracid() {
        return bracid;
    }

    /**
     * 设置关联表主键
     *
     * @param bracid 关联表主键
     */
    public void setBracid(String bracid) {
        this.bracid = bracid == null ? null : bracid.trim();
    }

    /**
     * 获取活动标识
     *
     * @return bracactid - 活动标识
     */
    public String getBracactid() {
        return bracactid;
    }

    /**
     * 设置活动标识
     *
     * @param bracactid 活动标识
     */
    public void setBracactid(String bracactid) {
        this.bracactid = bracactid == null ? null : bracactid.trim();
    }

    /**
     * 获取活动开始日期
     *
     * @return bracsdate - 活动开始日期
     */
    public Date getBracsdate() {
        return bracsdate;
    }

    /**
     * 设置活动开始日期
     *
     * @param bracsdate 活动开始日期
     */
    public void setBracsdate(Date bracsdate) {
        this.bracsdate = bracsdate;
    }

    /**
     * 获取活动开始时间
     *
     * @return bracstime - 活动开始时间
     */
    public Date getBracstime() {
        return bracstime;
    }

    /**
     * 设置活动开始时间
     *
     * @param bracstime 活动开始时间
     */
    public void setBracstime(Date bracstime) {
        this.bracstime = bracstime;
    }

    /**
     * 获取联系电话
     *
     * @return bractelephone - 联系电话
     */
    public String getBractelephone() {
        return bractelephone;
    }

    /**
     * 设置联系电话
     *
     * @param bractelephone 联系电话
     */
    public void setBractelephone(String bractelephone) {
        this.bractelephone = bractelephone == null ? null : bractelephone.trim();
    }

    /**
     * 获取活动地址
     *
     * @return bracaddr - 活动地址
     */
    public String getBracaddr() {
        return bracaddr;
    }

    /**
     * 设置活动地址
     *
     * @param bracaddr 活动地址
     */
    public void setBracaddr(String bracaddr) {
        this.bracaddr = bracaddr == null ? null : bracaddr.trim();
    }

    /**
     * 获取状态:0-无效;1-有效
     *
     * @return bracstate - 状态:0-无效;1-有效
     */
    public Integer getBracstate() {
        return bracstate;
    }

    /**
     * 设置状态:0-无效;1-有效
     *
     * @param bracstate 状态:0-无效;1-有效
     */
    public void setBracstate(Integer bracstate) {
        this.bracstate = bracstate;
    }

    /**
     * 获取专题活动ID 
     *
     * @return braid - 专题活动ID 
     */
    public String getBraid() {
        return braid;
    }

    /**
     * 设置专题活动ID 
     *
     * @param braid 专题活动ID 
     */
    public void setBraid(String braid) {
        this.braid = braid == null ? null : braid.trim();
    }

    /**
     * 获取活动结束时间
     *
     * @return bracedate - 活动结束时间
     */
    public Date getBracedate() {
        return bracedate;
    }

    /**
     * 设置活动结束时间
     *
     * @param bracedate 活动结束时间
     */
    public void setBracedate(Date bracedate) {
        this.bracedate = bracedate;
    }

    /**
     * 获取所属文化馆标识
     *
     * @return bravenueid - 所属文化馆标识
     */
    public String getBravenueid() {
        return bravenueid;
    }

    /**
     * 设置所属文化馆标识
     *
     * @param bravenueid 所属文化馆标识
     */
    public void setBravenueid(String bravenueid) {
        this.bravenueid = bravenueid == null ? null : bravenueid.trim();
    }
}