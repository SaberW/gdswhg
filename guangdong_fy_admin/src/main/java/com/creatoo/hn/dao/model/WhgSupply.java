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
     * 供需类型：0供给，1需求
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

    private String pscity;

    /**
     * 供需内容
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
     * 获取供需类型：0供给，1需求
     *
     * @return gxtype - 供需类型：0供给，1需求
     */
    public String getGxtype() {
        return gxtype;
    }

    /**
     * 设置供需类型：0供给，1需求
     *
     * @param gxtype 供需类型：0供给，1需求
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
     * 获取供需内容
     *
     * @return content - 供需内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置供需内容
     *
     * @param content 供需内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}