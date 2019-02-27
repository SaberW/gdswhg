package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_supply_tra")
public class WhgSupplyTra {
    /**
     * 培训ID
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
     * 状态(1-可编辑, 9-待审核, 2-待发布，6-已发布, 4-已下架, 5-已撤消)
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
     * 培训名称
     */
    private String title;

    /**
     * 部门id
     */
    private String deptid;

    /**
     * 培训图片
     */
    private String image;

    /**
     * 艺术分类
     */
    private String arttype;

    /**
     * 分类
     */
    private String etype;

    /**
     * 关键字
     */
    private String ekey;

    /**
     * 培训方式（0 普通培训 1直播）
     */
    private String islive;

    /**
     * 培训时长
     */
    private String duration;

    /**
     * 培训周期
     */
    private String period;

    /**
     * 是否收费
     */
    private Integer ismoney;

    /**
     * 省份
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区域
     */
    private String area;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 培训联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 配送次数
     */
    private String psnumber;

    /**
     * 配送省份
     */
    private String psprovince;

    /**
     * 配送范围
     */
    private String pscity;

    /**
     * 合适人群
     */
    private String fitcrowd;

    /**
     * 说明
     */
    private String notice;

    /**
     * 文化馆ID
     */
    private String cultid;

    /**
     * 培训简介
     */
    private String coursedesc;

    /**
     * 培训大纲
     */
    private String outline;

    /**
     * 审核者
     */
    private String checkor;

    /**
     * 发布者
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
     * 所属单位
     */
    private String workplace;

    /**
     * 固定电话
     */
    private String telephone;

    /**
     * 文艺专家
     */
    private String artistid;

    public String getArtistid() {
        return artistid;
    }

    public void setArtistid(String artistid) {
        this.artistid = artistid;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * 获取培训ID
     *
     * @return id - 培训ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置培训ID
     *
     * @param id 培训ID
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
     * 获取状态(1-可编辑, 9-待审核, 2-待发布，6-已发布, 4-已下架, 5-已撤消)
     *
     * @return state - 状态(1-可编辑, 9-待审核, 2-待发布，6-已发布, 4-已下架, 5-已撤消)
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态(1-可编辑, 9-待审核, 2-待发布，6-已发布, 4-已下架, 5-已撤消)
     *
     * @param state 状态(1-可编辑, 9-待审核, 2-待发布，6-已发布, 4-已下架, 5-已撤消)
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
     * 获取培训名称
     *
     * @return title - 培训名称
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置培训名称
     *
     * @param title 培训名称
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取培训图片
     *
     * @return image - 培训图片
     */
    public String getImage() {
        return image;
    }

    /**
     * 设置培训图片
     *
     * @param image 培训图片
     */
    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    /**
     * 获取艺术分类
     *
     * @return arttype - 艺术分类
     */
    public String getArttype() {
        return arttype;
    }

    /**
     * 设置艺术分类
     *
     * @param arttype 艺术分类
     */
    public void setArttype(String arttype) {
        this.arttype = arttype == null ? null : arttype.trim();
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
     * 获取关键字
     *
     * @return ekey - 关键字
     */
    public String getEkey() {
        return ekey;
    }

    /**
     * 设置关键字
     *
     * @param ekey 关键字
     */
    public void setEkey(String ekey) {
        this.ekey = ekey == null ? null : ekey.trim();
    }

    /**
     * 获取培训方式（0 普通培训 1直播）
     *
     * @return islive - 培训方式（0 普通培训 1直播）
     */
    public String getIslive() {
        return islive;
    }

    /**
     * 设置培训方式（0 普通培训 1直播）
     *
     * @param islive 培训方式（0 普通培训 1直播）
     */
    public void setIslive(String islive) {
        this.islive = islive;
    }

    /**
     * 获取培训时长
     *
     * @return duration - 培训时长
     */
    public String getDuration() {
        return duration;
    }

    /**
     * 设置培训时长
     *
     * @param duration 培训时长
     */
    public void setDuration(String duration) {
        this.duration = duration == null ? null : duration.trim();
    }

    /**
     * 获取培训周期
     *
     * @return period - 培训周期
     */
    public String getPeriod() {
        return period;
    }

    /**
     * 设置培训周期
     *
     * @param period 培训周期
     */
    public void setPeriod(String period) {
        this.period = period == null ? null : period.trim();
    }

    /**
     * 获取是否收费
     *
     * @return ismoney - 是否收费
     */
    public Integer getIsmoney() {
        return ismoney;
    }

    /**
     * 设置是否收费
     *
     * @param ismoney 是否收费
     */
    public void setIsmoney(Integer ismoney) {
        this.ismoney = ismoney;
    }

    /**
     * 获取省份
     *
     * @return province - 省份
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置省份
     *
     * @param province 省份
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
     * 获取培训联系电话
     *
     * @return phone - 培训联系电话
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置培训联系电话
     *
     * @param phone 培训联系电话
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 获取邮箱
     *
     * @return email - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 获取配送次数
     *
     * @return psnumber - 配送次数
     */
    public String getPsnumber() {
        return psnumber;
    }

    /**
     * 设置配送次数
     *
     * @param psnumber 配送次数
     */
    public void setPsnumber(String psnumber) {
        this.psnumber = psnumber == null ? null : psnumber.trim();
    }

    /**
     * 获取配送省份
     *
     * @return psprovince - 配送省份
     */
    public String getPsprovince() {
        return psprovince;
    }

    /**
     * 设置配送省份
     *
     * @param psprovince 配送省份
     */
    public void setPsprovince(String psprovince) {
        this.psprovince = psprovince == null ? null : psprovince.trim();
    }

    /**
     * 获取配送范围
     *
     * @return pscity - 配送范围
     */
    public String getPscity() {
        return pscity;
    }

    /**
     * 设置配送范围
     *
     * @param pscity 配送范围
     */
    public void setPscity(String pscity) {
        this.pscity = pscity == null ? null : pscity.trim();
    }

    /**
     * 获取合适人群
     *
     * @return fitcrowd - 合适人群
     */
    public String getFitcrowd() {
        return fitcrowd;
    }

    /**
     * 设置合适人群
     *
     * @param fitcrowd 合适人群
     */
    public void setFitcrowd(String fitcrowd) {
        this.fitcrowd = fitcrowd == null ? null : fitcrowd.trim();
    }

    /**
     * 获取说明
     *
     * @return notice - 说明
     */
    public String getNotice() {
        return notice;
    }

    /**
     * 设置说明
     *
     * @param notice 说明
     */
    public void setNotice(String notice) {
        this.notice = notice == null ? null : notice.trim();
    }

    /**
     * 获取文化馆ID
     *
     * @return cultid - 文化馆ID
     */
    public String getCultid() {
        return cultid;
    }

    /**
     * 设置文化馆ID
     *
     * @param cultid 文化馆ID
     */
    public void setCultid(String cultid) {
        this.cultid = cultid == null ? null : cultid.trim();
    }

    /**
     * 获取培训简介
     *
     * @return coursedesc - 培训简介
     */
    public String getCoursedesc() {
        return coursedesc;
    }

    /**
     * 设置培训简介
     *
     * @param coursedesc 培训简介
     */
    public void setCoursedesc(String coursedesc) {
        this.coursedesc = coursedesc == null ? null : coursedesc.trim();
    }

    /**
     * 获取培训大纲
     *
     * @return outline - 培训大纲
     */
    public String getOutline() {
        return outline;
    }

    /**
     * 设置培训大纲
     *
     * @param outline 培训大纲
     */
    public void setOutline(String outline) {
        this.outline = outline == null ? null : outline.trim();
    }

    public String getDeptid() {
        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    public String getCheckor() {
        return checkor;
    }

    public void setCheckor(String checkor) {
        this.checkor = checkor;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getCheckdate() {
        return checkdate;
    }

    public void setCheckdate(Date checkdate) {
        this.checkdate = checkdate;
    }

    public Date getPublishdate() {
        return publishdate;
    }

    public void setPublishdate(Date publishdate) {
        this.publishdate = publishdate;
    }
}