package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_show_goods")
public class WhgShowGoods {
    /**
     * ID
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
     * 产品图片
     */
    private String image;

    /**
     * 节目名称
     */
    private String title;

    /**
     * 节目类型
     */
    private String type;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 所属机构
     */
    private String organ;

    /**
     * 区域
     */
    private String area;

    /**
     * 演出时长
     */
    private String showtime;

    /**
     * 演出人数
     */
    private Integer shownum;

    /**
     * 演出人员
     */
    private String showperson;

    /**
     * 是否收费（0、否 1、是）
     */
    private Integer ismoney;

    /**
     * 演出简介
     */
    private String showdesc;

    /**
     * 所属文化馆
     */
    private String cultid;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 节目单
     */
    private String playbill;

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
     * 获取产品图片
     *
     * @return image - 产品图片
     */
    public String getImage() {
        return image;
    }

    /**
     * 设置产品图片
     *
     * @param image 产品图片
     */
    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    /**
     * 获取节目名称
     *
     * @return title - 节目名称
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置节目名称
     *
     * @param title 节目名称
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取节目类型
     *
     * @return type - 节目类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置节目类型
     *
     * @param type 节目类型
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * 获取所属机构
     *
     * @return organ - 所属机构
     */
    public String getOrgan() {
        return organ;
    }

    /**
     * 设置所属机构
     *
     * @param organ 所属机构
     */
    public void setOrgan(String organ) {
        this.organ = organ == null ? null : organ.trim();
    }

    /**
     * 获取区域
     *
     * @return area - 区域
     */
    public String getArea() {
        return area;
    }

    /**
     * 设置区域
     *
     * @param area 区域
     */
    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    /**
     * 获取演出时长
     *
     * @return showtime - 演出时长
     */
    public String getShowtime() {
        return showtime;
    }

    /**
     * 设置演出时长
     *
     * @param showtime 演出时长
     */
    public void setShowtime(String showtime) {
        this.showtime = showtime == null ? null : showtime.trim();
    }

    /**
     * 获取演出人数
     *
     * @return shownum - 演出人数
     */
    public Integer getShownum() {
        return shownum;
    }

    /**
     * 设置演出人数
     *
     * @param shownum 演出人数
     */
    public void setShownum(Integer shownum) {
        this.shownum = shownum;
    }

    /**
     * 获取演出人员
     *
     * @return showperson - 演出人员
     */
    public String getShowperson() {
        return showperson;
    }

    /**
     * 设置演出人员
     *
     * @param showperson 演出人员
     */
    public void setShowperson(String showperson) {
        this.showperson = showperson == null ? null : showperson.trim();
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
     * 获取演出简介
     *
     * @return showdesc - 演出简介
     */
    public String getShowdesc() {
        return showdesc;
    }

    /**
     * 设置演出简介
     *
     * @param showdesc 演出简介
     */
    public void setShowdesc(String showdesc) {
        this.showdesc = showdesc == null ? null : showdesc.trim();
    }

    /**
     * 获取节目单
     *
     * @return playbill - 节目单
     */
    public String getPlaybill() {
        return playbill;
    }

    /**
     * 设置节目单
     *
     * @param playbill 节目单
     */
    public void setPlaybill(String playbill) {
        this.playbill = playbill == null ? null : playbill.trim();
    }

    public String getCultid() {
        return cultid;
    }

    public void setCultid(String cultid) {
        this.cultid = cultid;
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

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}