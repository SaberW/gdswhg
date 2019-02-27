package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_fk_project")
public class WhgFkProject {
    @Id
    private String id;

    /**
     * 关联id
     */
    private String fkid;

    /**
     * 系统类型: 内部供需 NBGX 外部供需 WBGX
     */
    private String protype;

    /**
     * 状态：1 正常 2 删除 3 失效
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 配送区域：省
     */
    private String psprovince;

    /**
     * 标题
     */
    private String title;

    /**
     * 类型
     */
    private String type;

    /**
     * 文化馆id
     */
    private String cultid;

    /**
     * 文化馆名称
     */
    private String cultname;

    /**
     * 图片地址
     */
    private String imgpath;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String area;

    /**
     * 审核人标识
     */
    private String checkor;

    /**
     * 审核时间
     */
    private Date checkdate;

    /**
     * 发布人标识
     */
    private String publisher;

    /**
     * 发布时间
     */
    private Date publishdate;

    /**
     * 删除状态 0：未删除 1： 删除
     */
    private Integer delstate;

    /**
     * 配送区域：市县区
     */
    private String pscity;

    /**
     * 简介
     */
    private String memo;

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
     * 获取关联id
     *
     * @return fkid - 关联id
     */
    public String getFkid() {
        return fkid;
    }

    /**
     * 设置关联id
     *
     * @param fkid 关联id
     */
    public void setFkid(String fkid) {
        this.fkid = fkid == null ? null : fkid.trim();
    }

    /**
     * 获取系统类型: 内部供需 NBGX 外部供需 WBGX
     *
     * @return protype - 系统类型: 内部供需 NBGX 外部供需 WBGX
     */
    public String getProtype() {
        return protype;
    }

    /**
     * 设置系统类型: 内部供需 NBGX 外部供需 WBGX
     *
     * @param protype 系统类型: 内部供需 NBGX 外部供需 WBGX
     */
    public void setProtype(String protype) {
        this.protype = protype == null ? null : protype.trim();
    }

    /**
     * 获取状态：1 正常 2 删除 3 失效
     *
     * @return state - 状态：1 正常 2 删除 3 失效
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态：1 正常 2 删除 3 失效
     *
     * @param state 状态：1 正常 2 删除 3 失效
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取创建时间
     *
     * @return createtime - 创建时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 设置创建时间
     *
     * @param createtime 创建时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 获取配送区域：省
     *
     * @return psprovince - 配送区域：省
     */
    public String getPsprovince() {
        return psprovince;
    }

    /**
     * 设置配送区域：省
     *
     * @param psprovince 配送区域：省
     */
    public void setPsprovince(String psprovince) {
        this.psprovince = psprovince == null ? null : psprovince.trim();
    }

    /**
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取类型
     *
     * @return type - 类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置类型
     *
     * @param type 类型
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * 获取文化馆id
     *
     * @return cultid - 文化馆id
     */
    public String getCultid() {
        return cultid;
    }

    /**
     * 设置文化馆id
     *
     * @param cultid 文化馆id
     */
    public void setCultid(String cultid) {
        this.cultid = cultid == null ? null : cultid.trim();
    }

    /**
     * 获取文化馆名称
     *
     * @return cultname - 文化馆名称
     */
    public String getCultname() {
        return cultname;
    }

    /**
     * 设置文化馆名称
     *
     * @param cultname 文化馆名称
     */
    public void setCultname(String cultname) {
        this.cultname = cultname == null ? null : cultname.trim();
    }

    /**
     * 获取图片地址
     *
     * @return imgpath - 图片地址
     */
    public String getImgpath() {
        return imgpath;
    }

    /**
     * 设置图片地址
     *
     * @param imgpath 图片地址
     */
    public void setImgpath(String imgpath) {
        this.imgpath = imgpath == null ? null : imgpath.trim();
    }

    /**
     * 获取省
     *
     * @return province - 省
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置省
     *
     * @param province 省
     */
    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    /**
     * 获取市
     *
     * @return city - 市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置市
     *
     * @param city 市
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    /**
     * 获取区
     *
     * @return area - 区
     */
    public String getArea() {
        return area;
    }

    /**
     * 设置区
     *
     * @param area 区
     */
    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    /**
     * 获取审核人标识
     *
     * @return checkor - 审核人标识
     */
    public String getCheckor() {
        return checkor;
    }

    /**
     * 设置审核人标识
     *
     * @param checkor 审核人标识
     */
    public void setCheckor(String checkor) {
        this.checkor = checkor == null ? null : checkor.trim();
    }

    /**
     * 获取审核时间
     *
     * @return checkdate - 审核时间
     */
    public Date getCheckdate() {
        return checkdate;
    }

    /**
     * 设置审核时间
     *
     * @param checkdate 审核时间
     */
    public void setCheckdate(Date checkdate) {
        this.checkdate = checkdate;
    }

    /**
     * 获取发布人标识
     *
     * @return publisher - 发布人标识
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * 设置发布人标识
     *
     * @param publisher 发布人标识
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher == null ? null : publisher.trim();
    }

    /**
     * 获取发布时间
     *
     * @return publishdate - 发布时间
     */
    public Date getPublishdate() {
        return publishdate;
    }

    /**
     * 设置发布时间
     *
     * @param publishdate 发布时间
     */
    public void setPublishdate(Date publishdate) {
        this.publishdate = publishdate;
    }

    /**
     * 获取删除状态 0：未删除 1： 删除
     *
     * @return delstate - 删除状态 0：未删除 1： 删除
     */
    public Integer getDelstate() {
        return delstate;
    }

    /**
     * 设置删除状态 0：未删除 1： 删除
     *
     * @param delstate 删除状态 0：未删除 1： 删除
     */
    public void setDelstate(Integer delstate) {
        this.delstate = delstate;
    }

    /**
     * 获取配送区域：市县区
     *
     * @return pscity - 配送区域：市县区
     */
    public String getPscity() {
        return pscity;
    }

    /**
     * 设置配送区域：市县区
     *
     * @param pscity 配送区域：市县区
     */
    public void setPscity(String pscity) {
        this.pscity = pscity == null ? null : pscity.trim();
    }

    /**
     * 获取简介
     *
     * @return memo - 简介
     */
    public String getMemo() {
        return memo;
    }

    /**
     * 设置简介
     *
     * @param memo 简介
     */
    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}