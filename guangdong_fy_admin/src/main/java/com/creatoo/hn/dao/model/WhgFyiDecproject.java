package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_fyi_decproject")
public class WhgFyiDecproject {
    /**
     * 名录项目 id
     */
    @Id
    private String mlproid;

    /**
     * 名录项目列表标题
     */
    private String mlproshortitel;

    /**
     * 名录项目详情标题
     */
    private String mlprotailtitle;

    /**
     * 名录项目级别
     */
    private String mlprolevel;

    /**
     * 名录项目批次
     */
    private String mlproitem;

    /**
     * 名录项目类型
     */
    private String mlprotype;

    /**
     * 名录项目区域
     */
    private String mlproqy;

    /**
     * 名录项目列表图
     */
    private String mlprosmpic;

    /**
     * 名录项目详情图
     */
    private String mlprobigpic;

    /**
     * 申报区域
     */
    private String mlprosbaddr;

    /**
     * 名录项目资源来源
     */
    private String mlprosource;

    /**
     * 名录项目关键字
     */
    private String mlprokey;

    /**
     * 名录项目状态
     */
    private Integer mlprostate;

    /**
     * 名录项目修改时间
     */
    private Date mlprotime;

    /**
     * 所属文化馆标识
     */
    private String mlpvenueid;

    /**
     * 是否推荐：0否1是
     */
    private Integer recommend;

    /**
     * 名录项目详情
     */
    private String mlprodetail;

    /**
     * 获取名录项目 id
     *
     * @return mlproid - 名录项目 id
     */
    public String getMlproid() {
        return mlproid;
    }

    /**
     * 设置名录项目 id
     *
     * @param mlproid 名录项目 id
     */
    public void setMlproid(String mlproid) {
        this.mlproid = mlproid == null ? null : mlproid.trim();
    }

    /**
     * 获取名录项目列表标题
     *
     * @return mlproshortitel - 名录项目列表标题
     */
    public String getMlproshortitel() {
        return mlproshortitel;
    }

    /**
     * 设置名录项目列表标题
     *
     * @param mlproshortitel 名录项目列表标题
     */
    public void setMlproshortitel(String mlproshortitel) {
        this.mlproshortitel = mlproshortitel == null ? null : mlproshortitel.trim();
    }

    /**
     * 获取名录项目详情标题
     *
     * @return mlprotailtitle - 名录项目详情标题
     */
    public String getMlprotailtitle() {
        return mlprotailtitle;
    }

    /**
     * 设置名录项目详情标题
     *
     * @param mlprotailtitle 名录项目详情标题
     */
    public void setMlprotailtitle(String mlprotailtitle) {
        this.mlprotailtitle = mlprotailtitle == null ? null : mlprotailtitle.trim();
    }

    /**
     * 获取名录项目级别
     *
     * @return mlprolevel - 名录项目级别
     */
    public String getMlprolevel() {
        return mlprolevel;
    }

    /**
     * 设置名录项目级别
     *
     * @param mlprolevel 名录项目级别
     */
    public void setMlprolevel(String mlprolevel) {
        this.mlprolevel = mlprolevel == null ? null : mlprolevel.trim();
    }

    /**
     * 获取名录项目批次
     *
     * @return mlproitem - 名录项目批次
     */
    public String getMlproitem() {
        return mlproitem;
    }

    /**
     * 设置名录项目批次
     *
     * @param mlproitem 名录项目批次
     */
    public void setMlproitem(String mlproitem) {
        this.mlproitem = mlproitem == null ? null : mlproitem.trim();
    }

    /**
     * 获取名录项目类型
     *
     * @return mlprotype - 名录项目类型
     */
    public String getMlprotype() {
        return mlprotype;
    }

    /**
     * 设置名录项目类型
     *
     * @param mlprotype 名录项目类型
     */
    public void setMlprotype(String mlprotype) {
        this.mlprotype = mlprotype == null ? null : mlprotype.trim();
    }

    /**
     * 获取名录项目区域
     *
     * @return mlproqy - 名录项目区域
     */
    public String getMlproqy() {
        return mlproqy;
    }

    /**
     * 设置名录项目区域
     *
     * @param mlproqy 名录项目区域
     */
    public void setMlproqy(String mlproqy) {
        this.mlproqy = mlproqy == null ? null : mlproqy.trim();
    }

    /**
     * 获取名录项目列表图
     *
     * @return mlprosmpic - 名录项目列表图
     */
    public String getMlprosmpic() {
        return mlprosmpic;
    }

    /**
     * 设置名录项目列表图
     *
     * @param mlprosmpic 名录项目列表图
     */
    public void setMlprosmpic(String mlprosmpic) {
        this.mlprosmpic = mlprosmpic == null ? null : mlprosmpic.trim();
    }

    /**
     * 获取名录项目详情图
     *
     * @return mlprobigpic - 名录项目详情图
     */
    public String getMlprobigpic() {
        return mlprobigpic;
    }

    /**
     * 设置名录项目详情图
     *
     * @param mlprobigpic 名录项目详情图
     */
    public void setMlprobigpic(String mlprobigpic) {
        this.mlprobigpic = mlprobigpic == null ? null : mlprobigpic.trim();
    }

    /**
     * 获取申报区域
     *
     * @return mlprosbaddr - 申报区域
     */
    public String getMlprosbaddr() {
        return mlprosbaddr;
    }

    /**
     * 设置申报区域
     *
     * @param mlprosbaddr 申报区域
     */
    public void setMlprosbaddr(String mlprosbaddr) {
        this.mlprosbaddr = mlprosbaddr == null ? null : mlprosbaddr.trim();
    }

    /**
     * 获取名录项目资源来源
     *
     * @return mlprosource - 名录项目资源来源
     */
    public String getMlprosource() {
        return mlprosource;
    }

    /**
     * 设置名录项目资源来源
     *
     * @param mlprosource 名录项目资源来源
     */
    public void setMlprosource(String mlprosource) {
        this.mlprosource = mlprosource == null ? null : mlprosource.trim();
    }

    /**
     * 获取名录项目关键字
     *
     * @return mlprokey - 名录项目关键字
     */
    public String getMlprokey() {
        return mlprokey;
    }

    /**
     * 设置名录项目关键字
     *
     * @param mlprokey 名录项目关键字
     */
    public void setMlprokey(String mlprokey) {
        this.mlprokey = mlprokey == null ? null : mlprokey.trim();
    }

    /**
     * 获取名录项目状态
     *
     * @return mlprostate - 名录项目状态
     */
    public Integer getMlprostate() {
        return mlprostate;
    }

    /**
     * 设置名录项目状态
     *
     * @param mlprostate 名录项目状态
     */
    public void setMlprostate(Integer mlprostate) {
        this.mlprostate = mlprostate;
    }

    /**
     * 获取名录项目修改时间
     *
     * @return mlprotime - 名录项目修改时间
     */
    public Date getMlprotime() {
        return mlprotime;
    }

    /**
     * 设置名录项目修改时间
     *
     * @param mlprotime 名录项目修改时间
     */
    public void setMlprotime(Date mlprotime) {
        this.mlprotime = mlprotime;
    }

    /**
     * 获取所属文化馆标识
     *
     * @return mlpvenueid - 所属文化馆标识
     */
    public String getMlpvenueid() {
        return mlpvenueid;
    }

    /**
     * 设置所属文化馆标识
     *
     * @param mlpvenueid 所属文化馆标识
     */
    public void setMlpvenueid(String mlpvenueid) {
        this.mlpvenueid = mlpvenueid == null ? null : mlpvenueid.trim();
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
     * 获取名录项目详情
     *
     * @return mlprodetail - 名录项目详情
     */
    public String getMlprodetail() {
        return mlprodetail;
    }

    /**
     * 设置名录项目详情
     *
     * @param mlprodetail 名录项目详情
     */
    public void setMlprodetail(String mlprodetail) {
        this.mlprodetail = mlprodetail == null ? null : mlprodetail.trim();
    }
}