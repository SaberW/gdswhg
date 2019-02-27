package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_brand")
public class WhgBrand {
    /**
     * 专题活动标识
     */
    @Id
    private String braid;

    /**
     * 专题活动标题
     */
    private String bratitle;

    /**
     * 专题简介
     */
    private String braintroduce;

    /**
     * 专题图片
     */
    private String brapic;

    /**
     * 专题详情背景图
     */
    private String brabigpic;

    /**
     * 专题活动开始时间
     */
    private Date brasdate;

    /**
     * 专题活动结束时间
     */
    private Date braedate;

    /**
     * 状态：0-初始;1-送审;2-已审;3已发布
     */
    private Integer brastate;

    /**
     * 短标题
     */
    private String brashorttitle;

    /**
     * 所属文化馆标识
     */
    private String bravenueid;

    /**
     * 专题活动修改时间
     */
    private Date bravoptime;

    /**
     * 专题活动的现场介绍
     */
    private String bradetail;

    /**
     * 获取专题活动标识
     *
     * @return braid - 专题活动标识
     */
    public String getBraid() {
        return braid;
    }

    /**
     * 设置专题活动标识
     *
     * @param braid 专题活动标识
     */
    public void setBraid(String braid) {
        this.braid = braid == null ? null : braid.trim();
    }

    /**
     * 获取专题活动标题
     *
     * @return bratitle - 专题活动标题
     */
    public String getBratitle() {
        return bratitle;
    }

    /**
     * 设置专题活动标题
     *
     * @param bratitle 专题活动标题
     */
    public void setBratitle(String bratitle) {
        this.bratitle = bratitle == null ? null : bratitle.trim();
    }

    /**
     * 获取专题简介
     *
     * @return braintroduce - 专题简介
     */
    public String getBraintroduce() {
        return braintroduce;
    }

    /**
     * 设置专题简介
     *
     * @param braintroduce 专题简介
     */
    public void setBraintroduce(String braintroduce) {
        this.braintroduce = braintroduce == null ? null : braintroduce.trim();
    }

    /**
     * 获取专题图片
     *
     * @return brapic - 专题图片
     */
    public String getBrapic() {
        return brapic;
    }

    /**
     * 设置专题图片
     *
     * @param brapic 专题图片
     */
    public void setBrapic(String brapic) {
        this.brapic = brapic == null ? null : brapic.trim();
    }

    /**
     * 获取专题详情背景图
     *
     * @return brabigpic - 专题详情背景图
     */
    public String getBrabigpic() {
        return brabigpic;
    }

    /**
     * 设置专题详情背景图
     *
     * @param brabigpic 专题详情背景图
     */
    public void setBrabigpic(String brabigpic) {
        this.brabigpic = brabigpic == null ? null : brabigpic.trim();
    }

    /**
     * 获取专题活动开始时间
     *
     * @return brasdate - 专题活动开始时间
     */
    public Date getBrasdate() {
        return brasdate;
    }

    /**
     * 设置专题活动开始时间
     *
     * @param brasdate 专题活动开始时间
     */
    public void setBrasdate(Date brasdate) {
        this.brasdate = brasdate;
    }

    /**
     * 获取专题活动结束时间
     *
     * @return braedate - 专题活动结束时间
     */
    public Date getBraedate() {
        return braedate;
    }

    /**
     * 设置专题活动结束时间
     *
     * @param braedate 专题活动结束时间
     */
    public void setBraedate(Date braedate) {
        this.braedate = braedate;
    }

    /**
     * 获取状态：0-初始;1-送审;2-已审;3已发布
     *
     * @return brastate - 状态：0-初始;1-送审;2-已审;3已发布
     */
    public Integer getBrastate() {
        return brastate;
    }

    /**
     * 设置状态：0-初始;1-送审;2-已审;3已发布
     *
     * @param brastate 状态：0-初始;1-送审;2-已审;3已发布
     */
    public void setBrastate(Integer brastate) {
        this.brastate = brastate;
    }

    /**
     * 获取短标题
     *
     * @return brashorttitle - 短标题
     */
    public String getBrashorttitle() {
        return brashorttitle;
    }

    /**
     * 设置短标题
     *
     * @param brashorttitle 短标题
     */
    public void setBrashorttitle(String brashorttitle) {
        this.brashorttitle = brashorttitle == null ? null : brashorttitle.trim();
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

    /**
     * 获取专题活动修改时间
     *
     * @return bravoptime - 专题活动修改时间
     */
    public Date getBravoptime() {
        return bravoptime;
    }

    /**
     * 设置专题活动修改时间
     *
     * @param bravoptime 专题活动修改时间
     */
    public void setBravoptime(Date bravoptime) {
        this.bravoptime = bravoptime;
    }

    /**
     * 获取专题活动的现场介绍
     *
     * @return bradetail - 专题活动的现场介绍
     */
    public String getBradetail() {
        return bradetail;
    }

    /**
     * 设置专题活动的现场介绍
     *
     * @param bradetail 专题活动的现场介绍
     */
    public void setBradetail(String bradetail) {
        this.bradetail = bradetail == null ? null : bradetail.trim();
    }
}