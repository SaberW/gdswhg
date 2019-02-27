package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_sys_cult")
public class WhgSysCult {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 上级ID，顶级为0
     */
    private String pid;

    /**
     * 创建时间
     */
    private Date crtdate;

    /**
     * 创建用户
     */
    private String crtuser;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 修改时间
     */
    private Date statemdfdate;

    /**
     * 修改用户
     */
    private String statemdfuser;

    /**
     * 删除状态
     */
    private Integer delstate;

    /**
     * 文化馆名称
     */
    private String name;

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
     * 文化馆地址
     */
    private String address;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 图片
     */
    private String picture;

    /**
     * LOGO
     */
    private String logopicture;

    /**
     * 水印图片
     */
    private String sypicture;

    /**
     * 图片
     */
    private String bgpicture;

    private String introduction;

    private String memo;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 联系方式
     */
    private String contactnum;

    /**
     * 文化馆站点地址
     */
    private String siteurl;

    /**
     * 排序值
     */
    private Integer idx;

    /**
     * 是否上首页（0、否 1、是）
     */
    private Integer upindex;

    /**
     * 前端菜单栏显示
     */
    private String frontmenu;

    /**
     * 申请状态：0 审核失败 1 待申请 2申请通过 null（非申请，后台直接添加）
     */
    private Integer sqstate;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public String getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取上级ID，顶级为0
     *
     * @return pid - 上级ID，顶级为0
     */
    public String getPid() {
        return pid;
    }

    /**
     * 设置上级ID，顶级为0
     *
     * @param pid 上级ID，顶级为0
     */
    public void setPid(String pid) {
        this.pid = pid == null ? null : pid.trim();
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
     * 获取修改时间
     *
     * @return statemdfdate - 修改时间
     */
    public Date getStatemdfdate() {
        return statemdfdate;
    }

    /**
     * 设置修改时间
     *
     * @param statemdfdate 修改时间
     */
    public void setStatemdfdate(Date statemdfdate) {
        this.statemdfdate = statemdfdate;
    }

    /**
     * 获取修改用户
     *
     * @return statemdfuser - 修改用户
     */
    public String getStatemdfuser() {
        return statemdfuser;
    }

    /**
     * 设置修改用户
     *
     * @param statemdfuser 修改用户
     */
    public void setStatemdfuser(String statemdfuser) {
        this.statemdfuser = statemdfuser == null ? null : statemdfuser.trim();
    }

    /**
     * 获取删除状态
     *
     * @return delstate - 删除状态
     */
    public Integer getDelstate() {
        return delstate;
    }

    /**
     * 设置删除状态
     *
     * @param delstate 删除状态
     */
    public void setDelstate(Integer delstate) {
        this.delstate = delstate;
    }

    /**
     * 获取文化馆名称
     *
     * @return name - 文化馆名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置文化馆名称
     *
     * @param name 文化馆名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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
     * 获取文化馆地址
     *
     * @return address - 文化馆地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置文化馆地址
     *
     * @param address 文化馆地址
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * 获取经度
     *
     * @return longitude - 经度
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * 设置经度
     *
     * @param longitude 经度
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? null : longitude.trim();
    }

    /**
     * 获取纬度
     *
     * @return latitude - 纬度
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * 设置纬度
     *
     * @param latitude 纬度
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
    }

    /**
     * 获取图片
     *
     * @return picture - 图片
     */
    public String getPicture() {
        return picture;
    }

    /**
     * 设置图片
     *
     * @param picture 图片
     */
    public void setPicture(String picture) {
        this.picture = picture == null ? null : picture.trim();
    }

    /**
     * 获取LOGO
     *
     * @return logopicture - LOGO
     */
    public String getLogopicture() {
        return logopicture;
    }

    /**
     * 设置LOGO
     *
     * @param logopicture LOGO
     */
    public void setLogopicture(String logopicture) {
        this.logopicture = logopicture == null ? null : logopicture.trim();
    }

    /**
     * 获取水印图片
     *
     * @return sypicture - 水印图片
     */
    public String getSypicture() {
        return sypicture;
    }

    /**
     * 设置水印图片
     *
     * @param sypicture 水印图片
     */
    public void setSypicture(String sypicture) {
        this.sypicture = sypicture == null ? null : sypicture.trim();
    }

    /**
     * 获取图片
     *
     * @return bgpicture - 图片
     */
    public String getBgpicture() {
        return bgpicture;
    }

    /**
     * 设置图片
     *
     * @param bgpicture 图片
     */
    public void setBgpicture(String bgpicture) {
        this.bgpicture = bgpicture == null ? null : bgpicture.trim();
    }

    /**
     * @return introduction
     */
    public String getIntroduction() {
        return introduction;
    }

    /**
     * @param introduction
     */
    public void setIntroduction(String introduction) {
        this.introduction = introduction == null ? null : introduction.trim();
    }

    /**
     * @return memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * @param memo
     */
    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    /**
     * 获取联系人
     *
     * @return contact - 联系人
     */
    public String getContact() {
        return contact;
    }

    /**
     * 设置联系人
     *
     * @param contact 联系人
     */
    public void setContact(String contact) {
        this.contact = contact == null ? null : contact.trim();
    }

    /**
     * 获取联系方式
     *
     * @return contactnum - 联系方式
     */
    public String getContactnum() {
        return contactnum;
    }

    /**
     * 设置联系方式
     *
     * @param contactnum 联系方式
     */
    public void setContactnum(String contactnum) {
        this.contactnum = contactnum == null ? null : contactnum.trim();
    }

    /**
     * 获取文化馆站点地址
     *
     * @return siteurl - 文化馆站点地址
     */
    public String getSiteurl() {
        return siteurl;
    }

    /**
     * 设置文化馆站点地址
     *
     * @param siteurl 文化馆站点地址
     */
    public void setSiteurl(String siteurl) {
        this.siteurl = siteurl == null ? null : siteurl.trim();
    }

    /**
     * 获取排序值
     *
     * @return idx - 排序值
     */
    public Integer getIdx() {
        return idx;
    }

    /**
     * 设置排序值
     *
     * @param idx 排序值
     */
    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    /**
     * 获取是否上首页（0、否 1、是）
     *
     * @return upindex - 是否上首页（0、否 1、是）
     */
    public Integer getUpindex() {
        return upindex;
    }

    /**
     * 设置是否上首页（0、否 1、是）
     *
     * @param upindex 是否上首页（0、否 1、是）
     */
    public void setUpindex(Integer upindex) {
        this.upindex = upindex;
    }

    /**
     * 获取前端菜单栏显示
     *
     * @return frontmenu - 前端菜单栏显示
     */
    public String getFrontmenu() {
        return frontmenu;
    }

    /**
     * 设置前端菜单栏显示
     *
     * @param frontmenu 前端菜单栏显示
     */
    public void setFrontmenu(String frontmenu) {
        this.frontmenu = frontmenu == null ? null : frontmenu.trim();
    }

    /**
     * 获取申请状态：0 审核失败 1 待申请 2申请通过 null（非申请，后台直接添加）
     *
     * @return sqstate - 申请状态：0 审核失败 1 待申请 2申请通过 null（非申请，后台直接添加）
     */
    public Integer getSqstate() {
        return sqstate;
    }

    /**
     * 设置申请状态：0 审核失败 1 待申请 2申请通过 null（非申请，后台直接添加）
     *
     * @param sqstate 申请状态：0 审核失败 1 待申请 2申请通过 null（非申请，后台直接添加）
     */
    public void setSqstate(Integer sqstate) {
        this.sqstate = sqstate;
    }
}