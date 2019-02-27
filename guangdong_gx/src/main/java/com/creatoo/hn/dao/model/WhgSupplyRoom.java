package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_supply_room")
public class WhgSupplyRoom {
    /**
     * pk
     */
    @Id
    private String id;

    /**
     * 关联场馆id
     */
    private String venid;

    /**
     * 创建时间
     */
    private Date crtdate;

    /**
     * 创建人
     */
    private String crtuser;

    /**
     * 类型
     */
    private String etype;

    /**
     * 标签
     */
    private String etag;

    /**
     * 关键字
     */
    private String ekey;

    /**
     * 状态：1可编辑，9待审核，2待发布，6已发布，4已下架，5已撤消
     */
    private Integer state;

    /**
     * 删除状态：0未删除，1已删除
     */
    private Integer delstate;

    private Date statemdfdate;

    private String statemdfuser;

    /**
     * 是否推荐：0否1是
     */
    private Integer recommend;

    /**
     * 活动室名称
     */
    private String title;

    /**
     * 图片
     */
    private String imgurl;

    /**
     * 位置
     */
    private String location;

    /**
     * 面积大小
     */
    private String sizearea;

    /**
     * 可容人数
     */
    private Integer sizepeople;

    /**
     * 坐标经度
     */
    private String longitude;

    /**
     * 坐标纬度
     */
    private String latitude;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 适宜人群
     */
    private String crowd;

    /**
     * 是否收费：0不收，1收
     */
    private Integer hasfees;

    /**
     * 设施
     */
    private String facility;

    /**
     * 简介
     */
    private String summary;

    /**
     * 配送单位
     */
    private String cultid;

    /**
     * 配送范围省
     */
    private String psprovince;

    /**
     * 配送时间开始
     */
    private Date pstimestart;

    /**
     * 配送时间结束
     */
    private Date pstimeend;

    /**
     * 通知类型：SMS 短信 ZNX 站内信
     */
    private String noticetype;

    /**
     * 所属部门
     */
    private String deptid;

    /**
     * 配送说明
     */
    private String notice;

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
     * 配送范围市
     */
    private String pscity;

    /**
     * 配送次数
     */
    private String psinfo;

    /**
     * 所属单位
     */
    private String workplace;

    /**
     * 固定电话
     */
    private String telephone;

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
     * 获取pk
     *
     * @return id - pk
     */
    public String getId() {
        return id;
    }

    /**
     * 设置pk
     *
     * @param id pk
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取关联场馆id
     *
     * @return venid - 关联场馆id
     */
    public String getVenid() {
        return venid;
    }

    /**
     * 设置关联场馆id
     *
     * @param venid 关联场馆id
     */
    public void setVenid(String venid) {
        this.venid = venid == null ? null : venid.trim();
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
     * 获取类型
     *
     * @return etype - 类型
     */
    public String getEtype() {
        return etype;
    }

    /**
     * 设置类型
     *
     * @param etype 类型
     */
    public void setEtype(String etype) {
        this.etype = etype == null ? null : etype.trim();
    }

    /**
     * 获取标签
     *
     * @return etag - 标签
     */
    public String getEtag() {
        return etag;
    }

    /**
     * 设置标签
     *
     * @param etag 标签
     */
    public void setEtag(String etag) {
        this.etag = etag == null ? null : etag.trim();
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
     * 获取状态：1可编辑，9待审核，2待发布，6已发布，4已下架，5已撤消
     *
     * @return state - 状态：1可编辑，9待审核，2待发布，6已发布，4已下架，5已撤消
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态：1可编辑，9待审核，2待发布，6已发布，4已下架，5已撤消
     *
     * @param state 状态：1可编辑，9待审核，2待发布，6已发布，4已下架，5已撤消
     */
    public void setState(Integer state) {
        this.state = state;
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
     * @return statemdfdate
     */
    public Date getStatemdfdate() {
        return statemdfdate;
    }

    /**
     * @param statemdfdate
     */
    public void setStatemdfdate(Date statemdfdate) {
        this.statemdfdate = statemdfdate;
    }

    /**
     * @return statemdfuser
     */
    public String getStatemdfuser() {
        return statemdfuser;
    }

    /**
     * @param statemdfuser
     */
    public void setStatemdfuser(String statemdfuser) {
        this.statemdfuser = statemdfuser == null ? null : statemdfuser.trim();
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
     * 获取活动室名称
     *
     * @return title - 活动室名称
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置活动室名称
     *
     * @param title 活动室名称
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取图片
     *
     * @return imgurl - 图片
     */
    public String getImgurl() {
        return imgurl;
    }

    /**
     * 设置图片
     *
     * @param imgurl 图片
     */
    public void setImgurl(String imgurl) {
        this.imgurl = imgurl == null ? null : imgurl.trim();
    }

    /**
     * 获取位置
     *
     * @return location - 位置
     */
    public String getLocation() {
        return location;
    }

    /**
     * 设置位置
     *
     * @param location 位置
     */
    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    /**
     * 获取面积大小
     *
     * @return sizearea - 面积大小
     */
    public String getSizearea() {
        return sizearea;
    }

    /**
     * 设置面积大小
     *
     * @param sizearea 面积大小
     */
    public void setSizearea(String sizearea) {
        this.sizearea = sizearea == null ? null : sizearea.trim();
    }

    /**
     * 获取可容人数
     *
     * @return sizepeople - 可容人数
     */
    public Integer getSizepeople() {
        return sizepeople;
    }

    /**
     * 设置可容人数
     *
     * @param sizepeople 可容人数
     */
    public void setSizepeople(Integer sizepeople) {
        this.sizepeople = sizepeople;
    }

    /**
     * 获取坐标经度
     *
     * @return longitude - 坐标经度
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * 设置坐标经度
     *
     * @param longitude 坐标经度
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? null : longitude.trim();
    }

    /**
     * 获取坐标纬度
     *
     * @return latitude - 坐标纬度
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * 设置坐标纬度
     *
     * @param latitude 坐标纬度
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
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
     * 获取适宜人群
     *
     * @return crowd - 适宜人群
     */
    public String getCrowd() {
        return crowd;
    }

    /**
     * 设置适宜人群
     *
     * @param crowd 适宜人群
     */
    public void setCrowd(String crowd) {
        this.crowd = crowd == null ? null : crowd.trim();
    }

    /**
     * 获取是否收费：0不收，1收
     *
     * @return hasfees - 是否收费：0不收，1收
     */
    public Integer getHasfees() {
        return hasfees;
    }

    /**
     * 设置是否收费：0不收，1收
     *
     * @param hasfees 是否收费：0不收，1收
     */
    public void setHasfees(Integer hasfees) {
        this.hasfees = hasfees;
    }

    /**
     * 获取设施
     *
     * @return facility - 设施
     */
    public String getFacility() {
        return facility;
    }

    /**
     * 设置设施
     *
     * @param facility 设施
     */
    public void setFacility(String facility) {
        this.facility = facility == null ? null : facility.trim();
    }

    /**
     * 获取简介
     *
     * @return summary - 简介
     */
    public String getSummary() {
        return summary;
    }

    /**
     * 设置简介
     *
     * @param summary 简介
     */
    public void setSummary(String summary) {
        this.summary = summary == null ? null : summary.trim();
    }

    /**
     * 获取配送单位
     *
     * @return cultid - 配送单位
     */
    public String getCultid() {
        return cultid;
    }

    /**
     * 设置配送单位
     *
     * @param cultid 配送单位
     */
    public void setCultid(String cultid) {
        this.cultid = cultid == null ? null : cultid.trim();
    }

    /**
     * 获取配送范围省
     *
     * @return psprovince - 配送范围省
     */
    public String getPsprovince() {
        return psprovince;
    }

    /**
     * 设置配送范围省
     *
     * @param psprovince 配送范围省
     */
    public void setPsprovince(String psprovince) {
        this.psprovince = psprovince == null ? null : psprovince.trim();
    }

    /**
     * 获取配送时间开始
     *
     * @return pstimestart - 配送时间开始
     */
    public Date getPstimestart() {
        return pstimestart;
    }

    /**
     * 设置配送时间开始
     *
     * @param pstimestart 配送时间开始
     */
    public void setPstimestart(Date pstimestart) {
        this.pstimestart = pstimestart;
    }

    /**
     * 获取配送时间结束
     *
     * @return pstimeend - 配送时间结束
     */
    public Date getPstimeend() {
        return pstimeend;
    }

    /**
     * 设置配送时间结束
     *
     * @param pstimeend 配送时间结束
     */
    public void setPstimeend(Date pstimeend) {
        this.pstimeend = pstimeend;
    }

    /**
     * 获取通知类型：SMS 短信 ZNX 站内信
     *
     * @return noticetype - 通知类型：SMS 短信 ZNX 站内信
     */
    public String getNoticetype() {
        return noticetype;
    }

    /**
     * 设置通知类型：SMS 短信 ZNX 站内信
     *
     * @param noticetype 通知类型：SMS 短信 ZNX 站内信
     */
    public void setNoticetype(String noticetype) {
        this.noticetype = noticetype == null ? null : noticetype.trim();
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
     * 获取配送范围市
     *
     * @return pscity - 配送范围市
     */
    public String getPscity() {
        return pscity;
    }

    /**
     * 设置配送范围市
     *
     * @param pscity 配送范围市
     */
    public void setPscity(String pscity) {
        this.pscity = pscity == null ? null : pscity.trim();
    }

    /**
     * 获取配送次数
     *
     * @return psinfo - 配送次数
     */
    public String getPsinfo() {
        return psinfo;
    }

    /**
     * 设置配送次数
     *
     * @param psinfo 配送次数
     */
    public void setPsinfo(String psinfo) {
        this.psinfo = psinfo == null ? null : psinfo.trim();
    }
}