package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_ywi_whpp")
public class WhgYwiWhpp {
    /**
     * ID
     */
    @Id
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 简称
     */
    private String shortname;

    /**
     * 图片
     */
    private String picture;

    /**
     * 背景图片
     */
    private String bgpicture;

    /**
     * 背景颜色
     */
    private String bgcolour;

    /**
     * 创建人
     */
    private String crtuser;

    /**
     * 创建时间
     */
    private Date crtdate;

    /**
     * 状态（0-不正常；1-正常）
     */
    private Integer state;

    /**
     * 状态变更时间
     */
    private Date statemdfdate;

    /**
     * 状态变更用户ID
     */
    private String statemdfuser;

    /**
     * 删除状态(0-未删除；1-已删除)
     */
    private Integer delstate;

    /**
     * 背景图片不平铺:1-是， 0-否
     */
    private Integer norepeat;

    /**
     * 关联文化馆ID
     */
    private String cultid;

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
     * 品牌类型:0省级,1市级,2县区级
     */
    private Integer pptype;

    /**
     * 内容简介
     */
    private String introduction;

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
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取简称
     *
     * @return shortname - 简称
     */
    public String getShortname() {
        return shortname;
    }

    /**
     * 设置简称
     *
     * @param shortname 简称
     */
    public void setShortname(String shortname) {
        this.shortname = shortname == null ? null : shortname.trim();
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
     * 获取背景图片
     *
     * @return bgpicture - 背景图片
     */
    public String getBgpicture() {
        return bgpicture;
    }

    /**
     * 设置背景图片
     *
     * @param bgpicture 背景图片
     */
    public void setBgpicture(String bgpicture) {
        this.bgpicture = bgpicture == null ? null : bgpicture.trim();
    }

    /**
     * 获取背景颜色
     *
     * @return bgcolour - 背景颜色
     */
    public String getBgcolour() {
        return bgcolour;
    }

    /**
     * 设置背景颜色
     *
     * @param bgcolour 背景颜色
     */
    public void setBgcolour(String bgcolour) {
        this.bgcolour = bgcolour == null ? null : bgcolour.trim();
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
     * 获取状态（0-不正常；1-正常）
     *
     * @return state - 状态（0-不正常；1-正常）
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态（0-不正常；1-正常）
     *
     * @param state 状态（0-不正常；1-正常）
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
     * 获取状态变更用户ID
     *
     * @return statemdfuser - 状态变更用户ID
     */
    public String getStatemdfuser() {
        return statemdfuser;
    }

    /**
     * 设置状态变更用户ID
     *
     * @param statemdfuser 状态变更用户ID
     */
    public void setStatemdfuser(String statemdfuser) {
        this.statemdfuser = statemdfuser == null ? null : statemdfuser.trim();
    }

    /**
     * 获取删除状态(0-未删除；1-已删除)
     *
     * @return delstate - 删除状态(0-未删除；1-已删除)
     */
    public Integer getDelstate() {
        return delstate;
    }

    /**
     * 设置删除状态(0-未删除；1-已删除)
     *
     * @param delstate 删除状态(0-未删除；1-已删除)
     */
    public void setDelstate(Integer delstate) {
        this.delstate = delstate;
    }

    /**
     * 获取背景图片不平铺:1-是， 0-否
     *
     * @return norepeat - 背景图片不平铺:1-是， 0-否
     */
    public Integer getNorepeat() {
        return norepeat;
    }

    /**
     * 设置背景图片不平铺:1-是， 0-否
     *
     * @param norepeat 背景图片不平铺:1-是， 0-否
     */
    public void setNorepeat(Integer norepeat) {
        this.norepeat = norepeat;
    }

    /**
     * 获取关联文化馆ID
     *
     * @return cultid - 关联文化馆ID
     */
    public String getCultid() {
        return cultid;
    }

    /**
     * 设置关联文化馆ID
     *
     * @param cultid 关联文化馆ID
     */
    public void setCultid(String cultid) {
        this.cultid = cultid == null ? null : cultid.trim();
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
     * 获取品牌类型:0省级,1市级,2县区级
     *
     * @return pptype - 品牌类型:0省级,1市级,2县区级
     */
    public Integer getPptype() {
        return pptype;
    }

    /**
     * 设置品牌类型:0省级,1市级,2县区级
     *
     * @param pptype 品牌类型:0省级,1市级,2县区级
     */
    public void setPptype(Integer pptype) {
        this.pptype = pptype;
    }

    /**
     * 获取内容简介
     *
     * @return introduction - 内容简介
     */
    public String getIntroduction() {
        return introduction;
    }

    /**
     * 设置内容简介
     *
     * @param introduction 内容简介
     */
    public void setIntroduction(String introduction) {
        this.introduction = introduction == null ? null : introduction.trim();
    }
}