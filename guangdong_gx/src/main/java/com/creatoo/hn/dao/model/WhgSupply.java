package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_supply")
public class WhgSupply {
    /**
     * PK
     */
    @Id
    private String id;

    /**
     * 创建时间
     */
    private Date crtdate;

    /**
     * 创建人
     */
    private String crtuser;

    /**
     * 状态：1可编辑，9待审核，2待发布，6已发布，4已下架
     */
    private Integer state;

    /**
     * 状态更时间
     */
    private Date statemdfdate;

    /**
     * 状态变更人
     */
    private String statemdfuser;

    /**
     * 所属文化馆
     */
    private String cultid;

    /**
     * 分类
     */
    private String etype;

    /**
     * 供需标题
     */
    private String title;

    /**
     * 供需类型1需求，用于适配供需流程
     */
    private String gxtype;

    private String psprovince;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 二级分类
     */
    private String reftype;

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
     * 所属部门
     */
    private String deptid;

    /**
     * 关键字, 各类型共用
     */
    private String ekey;

    /**
     * 艺术分类，专家类的，各类型可共用
     */
    private String arttype;

    /**
     * 人数，场馆和展演共用
     */
    private String renshu;

    /**
     * 场馆类型-面积
     */
    private String cgmianji;

    /**
     * 培训类型-老师
     */
    private String pxlaoshi;

    /**
     * 展演类型-演员
     */
    private String zyyanyuan;

    /**
     * 展演类型-演出时长
     */
    private String zyshichang;

    /**
     * 专家类型-姓名
     */
    private String zjxingming;

    /**
     * 配送说明
     */
    private String notice;

    /**
     * 审核人标识
     */
    private String checkor;

    /**
     * 发布人标识
     */
    private String publisher;

    /**
     * 审核时间
     */
    private Date checkdate;

    /**
     * 发布时间
     */
    private Date publishdate;

    /**
     * 删除状态 0：未删除 1： 删除
     */
    private Integer delstate;

    private String pscity;

    /**
     * 说明
     */
    private String content;

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
     * 获取创建人
     *
     * @return crtuser - 创建人
     */
    public String getCrtuser() {
        return crtuser;
    }

    /**
     * 设置创建人
     *
     * @param crtuser 创建人
     */
    public void setCrtuser(String crtuser) {
        this.crtuser = crtuser == null ? null : crtuser.trim();
    }

    /**
     * 获取状态：1可编辑，9待审核，2待发布，6已发布，4已下架
     *
     * @return state - 状态：1可编辑，9待审核，2待发布，6已发布，4已下架
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态：1可编辑，9待审核，2待发布，6已发布，4已下架
     *
     * @param state 状态：1可编辑，9待审核，2待发布，6已发布，4已下架
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取状态更时间
     *
     * @return statemdfdate - 状态更时间
     */
    public Date getStatemdfdate() {
        return statemdfdate;
    }

    /**
     * 设置状态更时间
     *
     * @param statemdfdate 状态更时间
     */
    public void setStatemdfdate(Date statemdfdate) {
        this.statemdfdate = statemdfdate;
    }

    /**
     * 获取状态变更人
     *
     * @return statemdfuser - 状态变更人
     */
    public String getStatemdfuser() {
        return statemdfuser;
    }

    /**
     * 设置状态变更人
     *
     * @param statemdfuser 状态变更人
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
     * 获取分类
     *
     * @return etype - 分类
     */
    public String getEtype() {
        return etype;
    }

    /**
     * 设置分类
     *
     * @param etype 分类
     */
    public void setEtype(String etype) {
        this.etype = etype == null ? null : etype.trim();
    }

    /**
     * 获取供需标题
     *
     * @return title - 供需标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置供需标题
     *
     * @param title 供需标题
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取供需类型1需求，用于适配供需流程
     *
     * @return gxtype - 供需类型1需求，用于适配供需流程
     */
    public String getGxtype() {
        return gxtype;
    }

    /**
     * 设置供需类型1需求，用于适配供需流程
     *
     * @param gxtype 供需类型1需求，用于适配供需流程
     */
    public void setGxtype(String gxtype) {
        this.gxtype = gxtype == null ? null : gxtype.trim();
    }

    /**
     * @return psprovince
     */
    public String getPsprovince() {
        return psprovince;
    }

    /**
     * @param psprovince
     */
    public void setPsprovince(String psprovince) {
        this.psprovince = psprovince == null ? null : psprovince.trim();
    }

    /**
     * 获取联系人
     *
     * @return contacts - 联系人
     */
    public String getContacts() {
        return contacts;
    }

    /**
     * 设置联系人
     *
     * @param contacts 联系人
     */
    public void setContacts(String contacts) {
        this.contacts = contacts == null ? null : contacts.trim();
    }

    /**
     * 获取联系电话
     *
     * @return phone - 联系电话
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置联系电话
     *
     * @param phone 联系电话
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 获取二级分类
     *
     * @return reftype - 二级分类
     */
    public String getReftype() {
        return reftype;
    }

    /**
     * 设置二级分类
     *
     * @param reftype 二级分类
     */
    public void setReftype(String reftype) {
        this.reftype = reftype == null ? null : reftype.trim();
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
     * 获取关键字, 各类型共用
     *
     * @return ekey - 关键字, 各类型共用
     */
    public String getEkey() {
        return ekey;
    }

    /**
     * 设置关键字, 各类型共用
     *
     * @param ekey 关键字, 各类型共用
     */
    public void setEkey(String ekey) {
        this.ekey = ekey == null ? null : ekey.trim();
    }

    /**
     * 获取艺术分类，专家类的，各类型可共用
     *
     * @return arttype - 艺术分类，专家类的，各类型可共用
     */
    public String getArttype() {
        return arttype;
    }

    /**
     * 设置艺术分类，专家类的，各类型可共用
     *
     * @param arttype 艺术分类，专家类的，各类型可共用
     */
    public void setArttype(String arttype) {
        this.arttype = arttype == null ? null : arttype.trim();
    }

    /**
     * 获取人数，场馆和展演共用
     *
     * @return renshu - 人数，场馆和展演共用
     */
    public String getRenshu() {
        return renshu;
    }

    /**
     * 设置人数，场馆和展演共用
     *
     * @param renshu 人数，场馆和展演共用
     */
    public void setRenshu(String renshu) {
        this.renshu = renshu == null ? null : renshu.trim();
    }

    /**
     * 获取场馆类型-面积
     *
     * @return cgmianji - 场馆类型-面积
     */
    public String getCgmianji() {
        return cgmianji;
    }

    /**
     * 设置场馆类型-面积
     *
     * @param cgmianji 场馆类型-面积
     */
    public void setCgmianji(String cgmianji) {
        this.cgmianji = cgmianji == null ? null : cgmianji.trim();
    }

    /**
     * 获取培训类型-老师
     *
     * @return pxlaoshi - 培训类型-老师
     */
    public String getPxlaoshi() {
        return pxlaoshi;
    }

    /**
     * 设置培训类型-老师
     *
     * @param pxlaoshi 培训类型-老师
     */
    public void setPxlaoshi(String pxlaoshi) {
        this.pxlaoshi = pxlaoshi == null ? null : pxlaoshi.trim();
    }

    /**
     * 获取展演类型-演员
     *
     * @return zyyanyuan - 展演类型-演员
     */
    public String getZyyanyuan() {
        return zyyanyuan;
    }

    /**
     * 设置展演类型-演员
     *
     * @param zyyanyuan 展演类型-演员
     */
    public void setZyyanyuan(String zyyanyuan) {
        this.zyyanyuan = zyyanyuan == null ? null : zyyanyuan.trim();
    }

    /**
     * 获取展演类型-演出时长
     *
     * @return zyshichang - 展演类型-演出时长
     */
    public String getZyshichang() {
        return zyshichang;
    }

    /**
     * 设置展演类型-演出时长
     *
     * @param zyshichang 展演类型-演出时长
     */
    public void setZyshichang(String zyshichang) {
        this.zyshichang = zyshichang == null ? null : zyshichang.trim();
    }

    /**
     * 获取专家类型-姓名
     *
     * @return zjxingming - 专家类型-姓名
     */
    public String getZjxingming() {
        return zjxingming;
    }

    /**
     * 设置专家类型-姓名
     *
     * @param zjxingming 专家类型-姓名
     */
    public void setZjxingming(String zjxingming) {
        this.zjxingming = zjxingming == null ? null : zjxingming.trim();
    }

    /**
     * 获取配送说明
     *
     * @return notice - 配送说明
     */
    public String getNotice() {
        return notice;
    }

    /**
     * 设置配送说明
     *
     * @param notice 配送说明
     */
    public void setNotice(String notice) {
        this.notice = notice == null ? null : notice.trim();
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
     * @return pscity
     */
    public String getPscity() {
        return pscity;
    }

    /**
     * @param pscity
     */
    public void setPscity(String pscity) {
        this.pscity = pscity == null ? null : pscity.trim();
    }

    /**
     * 获取说明
     *
     * @return content - 说明
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置说明
     *
     * @param content 说明
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}