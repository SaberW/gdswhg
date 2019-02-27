package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_show_exh")
public class WhgShowExh {
    /**
     * ID
     */
    @Id
    private String id;

    /**
     * 展览名称
     */
    private String title;

    /**
     * 展览图片
     */
    private String image;

    /**
     * 展览类型
     */
    private String etype;

    /**
     * 展览时长
     */
    private String exhtime;

    private String exhorgan;

    /**
     * 联系人
     */
    private String exhcontacts;

    /**
     * 联系方式
     */
    private String exhphone;

    /**
     * 展品
     */
    private String exhgoods;

    /**
     * 区域
     */
    private String area;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 展览幅数
     */
    private Integer exhnum;

    /**
     * 是否收费（0、否 1、是）
     */
    private Integer ismoney;

    /**
     * 创建时间
     */
    private Date crtdate;

    /**
     * 创建人
     */
    private String crtuser;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 状态变更时间
     */
    private Date statemdfdate;

    /**
     * 状态变更用户
     */
    private String statemdfuser;

    /**
     * 展览简介
     */
    private String exhdesc;

    /**
     * 所属文化馆
     */
    private String cultid;

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
     * 获取展览名称
     *
     * @return title - 展览名称
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置展览名称
     *
     * @param title 展览名称
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取展览图片
     *
     * @return image - 展览图片
     */
    public String getImage() {
        return image;
    }

    /**
     * 设置展览图片
     *
     * @param image 展览图片
     */
    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    /**
     * 获取展览类型
     *
     * @return etype - 展览类型
     */
    public String getEtype() {
        return etype;
    }

    /**
     * 设置展览类型
     *
     * @param etype 展览类型
     */
    public void setEtype(String etype) {
        this.etype = etype == null ? null : etype.trim();
    }

    /**
     * 获取展览时长
     *
     * @return exhtime - 展览时长
     */
    public String getExhtime() {
        return exhtime;
    }

    /**
     * 设置展览时长
     *
     * @param exhtime 展览时长
     */
    public void setExhtime(String exhtime) {
        this.exhtime = exhtime == null ? null : exhtime.trim();
    }

    /**
     * @return exhorgan
     */
    public String getExhorgan() {
        return exhorgan;
    }

    /**
     * @param exhorgan
     */
    public void setExhorgan(String exhorgan) {
        this.exhorgan = exhorgan == null ? null : exhorgan.trim();
    }

    /**
     * 获取联系人
     *
     * @return exhcontacts - 联系人
     */
    public String getExhcontacts() {
        return exhcontacts;
    }

    /**
     * 设置联系人
     *
     * @param exhcontacts 联系人
     */
    public void setExhcontacts(String exhcontacts) {
        this.exhcontacts = exhcontacts == null ? null : exhcontacts.trim();
    }

    /**
     * 获取联系方式
     *
     * @return exhphone - 联系方式
     */
    public String getExhphone() {
        return exhphone;
    }

    /**
     * 设置联系方式
     *
     * @param exhphone 联系方式
     */
    public void setExhphone(String exhphone) {
        this.exhphone = exhphone == null ? null : exhphone.trim();
    }

    /**
     * 获取展品
     *
     * @return exhgoods - 展品
     */
    public String getExhgoods() {
        return exhgoods;
    }

    /**
     * 设置展品
     *
     * @param exhgoods 展品
     */
    public void setExhgoods(String exhgoods) {
        this.exhgoods = exhgoods == null ? null : exhgoods.trim();
    }

    /**
     * 获取展览幅数
     *
     * @return exhnum - 展览幅数
     */
    public Integer getExhnum() {
        return exhnum;
    }

    /**
     * 设置展览幅数
     *
     * @param exhnum 展览幅数
     */
    public void setExhnum(Integer exhnum) {
        this.exhnum = exhnum;
    }

    /**
     * 获取是否收费（0、否 1、是）
     *
     * @return ismoney - 是否收费（0、否 1、是）
     */
    public Integer getIsmoney() {
        return ismoney;
    }

    /**
     * 设置是否收费（0、否 1、是）
     *
     * @param ismoney 是否收费（0、否 1、是）
     */
    public void setIsmoney(Integer ismoney) {
        this.ismoney = ismoney;
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
     * 获取展览简介
     *
     * @return exhdesc - 展览简介
     */
    public String getExhdesc() {
        return exhdesc;
    }

    /**
     * 设置展览简介
     *
     * @param exhdesc 展览简介
     */
    public void setExhdesc(String exhdesc) {
        this.exhdesc = exhdesc == null ? null : exhdesc.trim();
    }

    public String getCultid() {
        return cultid;
    }

    public void setCultid(String cultid) {
        this.cultid = cultid;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}