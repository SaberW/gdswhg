package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_exh_goods")
public class WhgExhGoods {
    /**
     * ID
     */
    @Id
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 类别
     */
    private String etype;

    /**
     * 作者
     */
    private String author;

    /**
     * 年代
     */
    private String years;

    /**
     * 材质
     */
    private String material;

    /**
     * 尺寸
     */
    private String size;

    /**
     * 价值
     */
    private String cost;

    /**
     * 展品图片
     */
    private String image;

    /**
     * 展品数量
     */
    private Integer number;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Date crtdate;

    /**
     * 创建用户
     */
    private String crtuser;

    /**
     * 状态变更时间
     */
    private Date statemdfdate;

    /**
     * 状态变更用户
     */
    private String statemdfuser;

    /**
     * 所属文化馆
     */
    private String cultid;

    /**
     * 所属部门
     */
    private String deptid;

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
     * 删除状态：0未删除，1已删除
     */
    private Integer delstate;

    /**
     * 展品简介
     */
    private String cowrydesc;

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
        this.id = id == null ? null : id.trim();
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
     * 获取类别
     *
     * @return etype - 类别
     */
    public String getEtype() {
        return etype;
    }

    /**
     * 设置类别
     *
     * @param etype 类别
     */
    public void setEtype(String etype) {
        this.etype = etype == null ? null : etype.trim();
    }

    /**
     * 获取作者
     *
     * @return author - 作者
     */
    public String getAuthor() {
        return author;
    }

    /**
     * 设置作者
     *
     * @param author 作者
     */
    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    /**
     * 获取年代
     *
     * @return years - 年代
     */
    public String getYears() {
        return years;
    }

    /**
     * 设置年代
     *
     * @param years 年代
     */
    public void setYears(String years) {
        this.years = years == null ? null : years.trim();
    }

    /**
     * 获取材质
     *
     * @return material - 材质
     */
    public String getMaterial() {
        return material;
    }

    /**
     * 设置材质
     *
     * @param material 材质
     */
    public void setMaterial(String material) {
        this.material = material == null ? null : material.trim();
    }

    /**
     * 获取尺寸
     *
     * @return size - 尺寸
     */
    public String getSize() {
        return size;
    }

    /**
     * 设置尺寸
     *
     * @param size 尺寸
     */
    public void setSize(String size) {
        this.size = size == null ? null : size.trim();
    }

    /**
     * 获取价值
     *
     * @return cost - 价值
     */
    public String getCost() {
        return cost;
    }

    /**
     * 设置价值
     *
     * @param cost 价值
     */
    public void setCost(String cost) {
        this.cost = cost == null ? null : cost.trim();
    }

    /**
     * 获取展品图片
     *
     * @return image - 展品图片
     */
    public String getImage() {
        return image;
    }

    /**
     * 设置展品图片
     *
     * @param image 展品图片
     */
    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    /**
     * 获取展品数量
     *
     * @return number - 展品数量
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * 设置展品数量
     *
     * @param number 展品数量
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * 获取状态
     *
     * @return state - 状态
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态
     *
     * @param state 状态
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取创建时间
     *
     * @return crtdate - 创建时间
     */
    public Date getCrtdate() {
        return crtdate;
    }

    /**
     * 设置创建时间
     *
     * @param crtdate 创建时间
     */
    public void setCrtdate(Date crtdate) {
        this.crtdate = crtdate;
    }

    /**
     * 获取创建用户
     *
     * @return crtuser - 创建用户
     */
    public String getCrtuser() {
        return crtuser;
    }

    /**
     * 设置创建用户
     *
     * @param crtuser 创建用户
     */
    public void setCrtuser(String crtuser) {
        this.crtuser = crtuser == null ? null : crtuser.trim();
    }

    /**
     * 获取状态变更时间
     *
     * @return statemdfdate - 状态变更时间
     */
    public Date getStatemdfdate() {
        return statemdfdate;
    }

    /**
     * 设置状态变更时间
     *
     * @param statemdfdate 状态变更时间
     */
    public void setStatemdfdate(Date statemdfdate) {
        this.statemdfdate = statemdfdate;
    }

    /**
     * 获取状态变更用户
     *
     * @return statemdfuser - 状态变更用户
     */
    public String getStatemdfuser() {
        return statemdfuser;
    }

    /**
     * 设置状态变更用户
     *
     * @param statemdfuser 状态变更用户
     */
    public void setStatemdfuser(String statemdfuser) {
        this.statemdfuser = statemdfuser == null ? null : statemdfuser.trim();
    }

    /**
     * 获取所属文化馆
     *
     * @return cultid - 所属文化馆
     */
    public String getCultid() {
        return cultid;
    }

    /**
     * 设置所属文化馆
     *
     * @param cultid 所属文化馆
     */
    public void setCultid(String cultid) {
        this.cultid = cultid == null ? null : cultid.trim();
    }

    /**
     * 获取所属部门
     *
     * @return deptid - 所属部门
     */
    public String getDeptid() {
        return deptid;
    }

    /**
     * 设置所属部门
     *
     * @param deptid 所属部门
     */
    public void setDeptid(String deptid) {
        this.deptid = deptid == null ? null : deptid.trim();
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
     * 获取删除状态：0未删除，1已删除
     *
     * @return delstate - 删除状态：0未删除，1已删除
     */
    public Integer getDelstate() {
        return delstate;
    }

    /**
     * 设置删除状态：0未删除，1已删除
     *
     * @param delstate 删除状态：0未删除，1已删除
     */
    public void setDelstate(Integer delstate) {
        this.delstate = delstate;
    }

    /**
     * 获取展品简介
     *
     * @return cowrydesc - 展品简介
     */
    public String getCowrydesc() {
        return cowrydesc;
    }

    /**
     * 设置展品简介
     *
     * @param cowrydesc 展品简介
     */
    public void setCowrydesc(String cowrydesc) {
        this.cowrydesc = cowrydesc == null ? null : cowrydesc.trim();
    }
}