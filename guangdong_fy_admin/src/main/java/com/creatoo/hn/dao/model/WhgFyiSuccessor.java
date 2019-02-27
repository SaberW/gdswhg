package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_fyi_successor")
public class WhgFyiSuccessor {
    /**
     * 传承人id
     */
    @Id
    private String suorid;

    /**
     * 传承项目关联id
     */
    private String proid;

    /**
     * 传承人姓名
     */
    private String suorname;

    /**
     * 传承人图片
     */
    private String suorpic;

    /**
     * 传承人区域
     */
    private String suorqy;

    /**
     * 传承人级别
     */
    private String suorlevel;

    /**
     * 传承人批次
     */
    private String suoritem;

    /**
     * 传承人类别
     */
    private String suortype;

    /**
     * 传承人 介绍
     */
    private String suorjs;

    /**
     * 传承人状态
     */
    private Integer suorstate;

    /**
     * 所属文化馆标识
     */
    private String suorvenueid;

    /**
     * 传承人修改时间
     */
    private Date suoroptime;

    /**
     * 是否推荐：0否1是
     */
    private Integer recommend;

    /**
     * 传承人叙史
     */
    private String suorxus;

    /**
     * 传承人成就
     */
    private String suorachv;

    /**
     * 获取传承人id
     *
     * @return suorid - 传承人id
     */
    public String getSuorid() {
        return suorid;
    }

    /**
     * 设置传承人id
     *
     * @param suorid 传承人id
     */
    public void setSuorid(String suorid) {
        this.suorid = suorid == null ? null : suorid.trim();
    }

    /**
     * 获取传承项目关联id
     *
     * @return proid - 传承项目关联id
     */
    public String getProid() {
        return proid;
    }

    /**
     * 设置传承项目关联id
     *
     * @param proid 传承项目关联id
     */
    public void setProid(String proid) {
        this.proid = proid == null ? null : proid.trim();
    }

    /**
     * 获取传承人姓名
     *
     * @return suorname - 传承人姓名
     */
    public String getSuorname() {
        return suorname;
    }

    /**
     * 设置传承人姓名
     *
     * @param suorname 传承人姓名
     */
    public void setSuorname(String suorname) {
        this.suorname = suorname == null ? null : suorname.trim();
    }

    /**
     * 获取传承人图片
     *
     * @return suorpic - 传承人图片
     */
    public String getSuorpic() {
        return suorpic;
    }

    /**
     * 设置传承人图片
     *
     * @param suorpic 传承人图片
     */
    public void setSuorpic(String suorpic) {
        this.suorpic = suorpic == null ? null : suorpic.trim();
    }

    /**
     * 获取传承人区域
     *
     * @return suorqy - 传承人区域
     */
    public String getSuorqy() {
        return suorqy;
    }

    /**
     * 设置传承人区域
     *
     * @param suorqy 传承人区域
     */
    public void setSuorqy(String suorqy) {
        this.suorqy = suorqy == null ? null : suorqy.trim();
    }

    /**
     * 获取传承人级别
     *
     * @return suorlevel - 传承人级别
     */
    public String getSuorlevel() {
        return suorlevel;
    }

    /**
     * 设置传承人级别
     *
     * @param suorlevel 传承人级别
     */
    public void setSuorlevel(String suorlevel) {
        this.suorlevel = suorlevel == null ? null : suorlevel.trim();
    }

    /**
     * 获取传承人批次
     *
     * @return suoritem - 传承人批次
     */
    public String getSuoritem() {
        return suoritem;
    }

    /**
     * 设置传承人批次
     *
     * @param suoritem 传承人批次
     */
    public void setSuoritem(String suoritem) {
        this.suoritem = suoritem == null ? null : suoritem.trim();
    }

    /**
     * 获取传承人类别
     *
     * @return suortype - 传承人类别
     */
    public String getSuortype() {
        return suortype;
    }

    /**
     * 设置传承人类别
     *
     * @param suortype 传承人类别
     */
    public void setSuortype(String suortype) {
        this.suortype = suortype == null ? null : suortype.trim();
    }

    /**
     * 获取传承人 介绍
     *
     * @return suorjs - 传承人 介绍
     */
    public String getSuorjs() {
        return suorjs;
    }

    /**
     * 设置传承人 介绍
     *
     * @param suorjs 传承人 介绍
     */
    public void setSuorjs(String suorjs) {
        this.suorjs = suorjs == null ? null : suorjs.trim();
    }

    /**
     * 获取传承人状态
     *
     * @return suorstate - 传承人状态
     */
    public Integer getSuorstate() {
        return suorstate;
    }

    /**
     * 设置传承人状态
     *
     * @param suorstate 传承人状态
     */
    public void setSuorstate(Integer suorstate) {
        this.suorstate = suorstate;
    }

    /**
     * 获取所属文化馆标识
     *
     * @return suorvenueid - 所属文化馆标识
     */
    public String getSuorvenueid() {
        return suorvenueid;
    }

    /**
     * 设置所属文化馆标识
     *
     * @param suorvenueid 所属文化馆标识
     */
    public void setSuorvenueid(String suorvenueid) {
        this.suorvenueid = suorvenueid == null ? null : suorvenueid.trim();
    }

    /**
     * 获取传承人修改时间
     *
     * @return suoroptime - 传承人修改时间
     */
    public Date getSuoroptime() {
        return suoroptime;
    }

    /**
     * 设置传承人修改时间
     *
     * @param suoroptime 传承人修改时间
     */
    public void setSuoroptime(Date suoroptime) {
        this.suoroptime = suoroptime;
    }

    /**
     * 获取是否推荐：0否1是
     *
     * @return recommend - 是否推荐：0否1是
     */
    public Integer getRecommend() {
        return recommend;
    }

    /**
     * 设置是否推荐：0否1是
     *
     * @param recommend 是否推荐：0否1是
     */
    public void setRecommend(Integer recommend) {
        this.recommend = recommend;
    }

    /**
     * 获取传承人叙史
     *
     * @return suorxus - 传承人叙史
     */
    public String getSuorxus() {
        return suorxus;
    }

    /**
     * 设置传承人叙史
     *
     * @param suorxus 传承人叙史
     */
    public void setSuorxus(String suorxus) {
        this.suorxus = suorxus == null ? null : suorxus.trim();
    }

    /**
     * 获取传承人成就
     *
     * @return suorachv - 传承人成就
     */
    public String getSuorachv() {
        return suorachv;
    }

    /**
     * 设置传承人成就
     *
     * @param suorachv 传承人成就
     */
    public void setSuorachv(String suorachv) {
        this.suorachv = suorachv == null ? null : suorachv.trim();
    }
}