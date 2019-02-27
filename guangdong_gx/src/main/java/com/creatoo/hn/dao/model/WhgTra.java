package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_tra")
public class WhgTra {
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
     * 艺术分类
     */
    private String arttype;

    /**
     * 分类
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
     * 文化品牌
     */
    private String ebrand;

    /**
     * 培训名称
     */
    private String title;

    /**
     * 区域
     */
    private String area;

    /**
     * 培训所在场馆
     */
    private String venue;

    /**
     * 培训地址
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
     * 是否实名(0:否 1:是)
     */
    private Integer isrealname;

    /**
     * 培训开始时间
     */
    private Date starttime;

    /**
     * 培训结束时间
     */
    private Date endtime;

    /**
     * 培训图片
     */
    private String trainimg;

    /**
     * 报名人数上限
     */
    private Integer maxnumber;

    /**
     * 报名开始时间
     */
    private Date enrollstarttime;

    /**
     * 报名结束时间
     */
    private Date enrollendtime;

    /**
     * 是否学期制
     */
    private Integer isterm;

    /**
     * 是否基础培训(0:需要面试，1:需人工录取 2、即报即得)
     */
    private Integer isbasicclass;

    /**
     * 基础报名人数
     */
    private Integer basicenrollnumber;

    /**
     * 是否多场次(0:单场 1:多场 )
     */
    private Integer ismultisite;

    /**
     * 是否显示最大报名人数
     */
    private Integer isshowmaxnumber;

    /**
     * 审核不通过/上架不通过原因
     */
    private String trainbackreason;

    /**
     * 文化馆ID
     */
    private String cultid;

    /**
     * 所属部门ID
     */
    private String deptid;

    /**
     * 固定班周几
     */
    private String fixedweek;

    /**
     * 课程时间
     */
    private String coursetime;

    /**
     * 所在活动室
     */
    private String venroom;

    /**
     * 是否推荐（0、不推荐 1、推荐）
     */
    private Integer recommend;

    /**
     * 固定场时段开始时间
     */
    private Date fixedstarttime;

    /**
     * 固定场时段结束时间
     */
    private Date fixedendtime;

    /**
     * 培训联系电话
     */
    private String phone;

    /**
     * 培训老师ID
     */
    private String teacherid;

    /**
     * 培训老师名称
     */
    private String teachername;

    /**
     * 培训课程的适合年龄段
     */
    private String age;

    /**
     * 是否收费
     */
    private Integer ismoney;

    /**
     * 是否上首页（0、否 1、是）
     */
    private Integer upindex;

    /**
     * 是否限制报名（0、不限制  1、限制为只报两个）
     */
    private Integer islimit;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 省份
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 培训时长
     */
    private String duration;

    /**
     * 区分培训与众筹培训标志：有值为众筹 没值为普通培训
     */
    private String biz;

    /**
     * 是否直播（0 普通培训 1直播）
     */
    private Integer islive;

    /**
     * 通知类型 （SMS短信  ZNX站内信）
     */
    private String noticetype;

    /**
     * 温馨提示
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
     * 在线课程是否需要报名：0-否;1-是
     */
    private Integer mustsignup;

    /**
     * 培训所属的培训组标识
     */
    private String groupid;

    /**
     * 主办方
     */
    private String host;

    /**
     * 省级推荐标记：0取消，1推荐
     */
    private Integer toprovince;

    /**
     * 市级推荐标记：0取消，1推荐
     */
    private Integer tocity;

    /**
     * 是否摇号：1 是；0否；
     */
    private Integer isyaohao;

    /**
     * 培训课程描述
     */
    private String coursedesc;

    /**
     * 培训大纲
     */
    private String outline;

    /**
     * 培训老师介绍
     */
    private String teacherdesc;

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
     * 获取文化品牌
     *
     * @return ebrand - 文化品牌
     */
    public String getEbrand() {
        return ebrand;
    }

    /**
     * 设置文化品牌
     *
     * @param ebrand 文化品牌
     */
    public void setEbrand(String ebrand) {
        this.ebrand = ebrand == null ? null : ebrand.trim();
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
     * 获取培训所在场馆
     *
     * @return venue - 培训所在场馆
     */
    public String getVenue() {
        return venue;
    }

    /**
     * 设置培训所在场馆
     *
     * @param venue 培训所在场馆
     */
    public void setVenue(String venue) {
        this.venue = venue == null ? null : venue.trim();
    }

    /**
     * 获取培训地址
     *
     * @return address - 培训地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置培训地址
     *
     * @param address 培训地址
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
     * 获取是否实名(0:否 1:是)
     *
     * @return isrealname - 是否实名(0:否 1:是)
     */
    public Integer getIsrealname() {
        return isrealname;
    }

    /**
     * 设置是否实名(0:否 1:是)
     *
     * @param isrealname 是否实名(0:否 1:是)
     */
    public void setIsrealname(Integer isrealname) {
        this.isrealname = isrealname;
    }

    /**
     * 获取培训开始时间
     *
     * @return starttime - 培训开始时间
     */
    public Date getStarttime() {
        return starttime;
    }

    /**
     * 设置培训开始时间
     *
     * @param starttime 培训开始时间
     */
    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    /**
     * 获取培训结束时间
     *
     * @return endtime - 培训结束时间
     */
    public Date getEndtime() {
        return endtime;
    }

    /**
     * 设置培训结束时间
     *
     * @param endtime 培训结束时间
     */
    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    /**
     * 获取培训图片
     *
     * @return trainimg - 培训图片
     */
    public String getTrainimg() {
        return trainimg;
    }

    /**
     * 设置培训图片
     *
     * @param trainimg 培训图片
     */
    public void setTrainimg(String trainimg) {
        this.trainimg = trainimg == null ? null : trainimg.trim();
    }

    /**
     * 获取报名人数上限
     *
     * @return maxnumber - 报名人数上限
     */
    public Integer getMaxnumber() {
        return maxnumber;
    }

    /**
     * 设置报名人数上限
     *
     * @param maxnumber 报名人数上限
     */
    public void setMaxnumber(Integer maxnumber) {
        this.maxnumber = maxnumber;
    }

    /**
     * 获取报名开始时间
     *
     * @return enrollstarttime - 报名开始时间
     */
    public Date getEnrollstarttime() {
        return enrollstarttime;
    }

    /**
     * 设置报名开始时间
     *
     * @param enrollstarttime 报名开始时间
     */
    public void setEnrollstarttime(Date enrollstarttime) {
        this.enrollstarttime = enrollstarttime;
    }

    /**
     * 获取报名结束时间
     *
     * @return enrollendtime - 报名结束时间
     */
    public Date getEnrollendtime() {
        return enrollendtime;
    }

    /**
     * 设置报名结束时间
     *
     * @param enrollendtime 报名结束时间
     */
    public void setEnrollendtime(Date enrollendtime) {
        this.enrollendtime = enrollendtime;
    }

    /**
     * 获取是否学期制
     *
     * @return isterm - 是否学期制
     */
    public Integer getIsterm() {
        return isterm;
    }

    /**
     * 设置是否学期制
     *
     * @param isterm 是否学期制
     */
    public void setIsterm(Integer isterm) {
        this.isterm = isterm;
    }

    /**
     * 获取是否基础培训(0:需要面试，1:需人工录取 2、即报即得)
     *
     * @return isbasicclass - 是否基础培训(0:需要面试，1:需人工录取 2、即报即得)
     */
    public Integer getIsbasicclass() {
        return isbasicclass;
    }

    /**
     * 设置是否基础培训(0:需要面试，1:需人工录取 2、即报即得)
     *
     * @param isbasicclass 是否基础培训(0:需要面试，1:需人工录取 2、即报即得)
     */
    public void setIsbasicclass(Integer isbasicclass) {
        this.isbasicclass = isbasicclass;
    }

    /**
     * 获取基础报名人数
     *
     * @return basicenrollnumber - 基础报名人数
     */
    public Integer getBasicenrollnumber() {
        return basicenrollnumber;
    }

    /**
     * 设置基础报名人数
     *
     * @param basicenrollnumber 基础报名人数
     */
    public void setBasicenrollnumber(Integer basicenrollnumber) {
        this.basicenrollnumber = basicenrollnumber;
    }

    /**
     * 获取是否多场次(0:单场 1:多场 )
     *
     * @return ismultisite - 是否多场次(0:单场 1:多场 )
     */
    public Integer getIsmultisite() {
        return ismultisite;
    }

    /**
     * 设置是否多场次(0:单场 1:多场 )
     *
     * @param ismultisite 是否多场次(0:单场 1:多场 )
     */
    public void setIsmultisite(Integer ismultisite) {
        this.ismultisite = ismultisite;
    }

    /**
     * 获取是否显示最大报名人数
     *
     * @return isshowmaxnumber - 是否显示最大报名人数
     */
    public Integer getIsshowmaxnumber() {
        return isshowmaxnumber;
    }

    /**
     * 设置是否显示最大报名人数
     *
     * @param isshowmaxnumber 是否显示最大报名人数
     */
    public void setIsshowmaxnumber(Integer isshowmaxnumber) {
        this.isshowmaxnumber = isshowmaxnumber;
    }

    /**
     * 获取审核不通过/上架不通过原因
     *
     * @return trainbackreason - 审核不通过/上架不通过原因
     */
    public String getTrainbackreason() {
        return trainbackreason;
    }

    /**
     * 设置审核不通过/上架不通过原因
     *
     * @param trainbackreason 审核不通过/上架不通过原因
     */
    public void setTrainbackreason(String trainbackreason) {
        this.trainbackreason = trainbackreason == null ? null : trainbackreason.trim();
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
     * 获取所属部门ID
     *
     * @return deptid - 所属部门ID
     */
    public String getDeptid() {
        return deptid;
    }

    /**
     * 设置所属部门ID
     *
     * @param deptid 所属部门ID
     */
    public void setDeptid(String deptid) {
        this.deptid = deptid == null ? null : deptid.trim();
    }

    /**
     * 获取固定班周几
     *
     * @return fixedweek - 固定班周几
     */
    public String getFixedweek() {
        return fixedweek;
    }

    /**
     * 设置固定班周几
     *
     * @param fixedweek 固定班周几
     */
    public void setFixedweek(String fixedweek) {
        this.fixedweek = fixedweek == null ? null : fixedweek.trim();
    }

    /**
     * 获取课程时间
     *
     * @return coursetime - 课程时间
     */
    public String getCoursetime() {
        return coursetime;
    }

    /**
     * 设置课程时间
     *
     * @param coursetime 课程时间
     */
    public void setCoursetime(String coursetime) {
        this.coursetime = coursetime == null ? null : coursetime.trim();
    }

    /**
     * 获取所在活动室
     *
     * @return venroom - 所在活动室
     */
    public String getVenroom() {
        return venroom;
    }

    /**
     * 设置所在活动室
     *
     * @param venroom 所在活动室
     */
    public void setVenroom(String venroom) {
        this.venroom = venroom == null ? null : venroom.trim();
    }

    /**
     * 获取是否推荐（0、不推荐 1、推荐）
     *
     * @return recommend - 是否推荐（0、不推荐 1、推荐）
     */
    public Integer getRecommend() {
        return recommend;
    }

    /**
     * 设置是否推荐（0、不推荐 1、推荐）
     *
     * @param recommend 是否推荐（0、不推荐 1、推荐）
     */
    public void setRecommend(Integer recommend) {
        this.recommend = recommend;
    }

    /**
     * 获取固定场时段开始时间
     *
     * @return fixedstarttime - 固定场时段开始时间
     */
    public Date getFixedstarttime() {
        return fixedstarttime;
    }

    /**
     * 设置固定场时段开始时间
     *
     * @param fixedstarttime 固定场时段开始时间
     */
    public void setFixedstarttime(Date fixedstarttime) {
        this.fixedstarttime = fixedstarttime;
    }

    /**
     * 获取固定场时段结束时间
     *
     * @return fixedendtime - 固定场时段结束时间
     */
    public Date getFixedendtime() {
        return fixedendtime;
    }

    /**
     * 设置固定场时段结束时间
     *
     * @param fixedendtime 固定场时段结束时间
     */
    public void setFixedendtime(Date fixedendtime) {
        this.fixedendtime = fixedendtime;
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
     * 获取培训老师ID
     *
     * @return teacherid - 培训老师ID
     */
    public String getTeacherid() {
        return teacherid;
    }

    /**
     * 设置培训老师ID
     *
     * @param teacherid 培训老师ID
     */
    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid == null ? null : teacherid.trim();
    }

    /**
     * 获取培训老师名称
     *
     * @return teachername - 培训老师名称
     */
    public String getTeachername() {
        return teachername;
    }

    /**
     * 设置培训老师名称
     *
     * @param teachername 培训老师名称
     */
    public void setTeachername(String teachername) {
        this.teachername = teachername == null ? null : teachername.trim();
    }

    /**
     * 获取培训课程的适合年龄段
     *
     * @return age - 培训课程的适合年龄段
     */
    public String getAge() {
        return age;
    }

    /**
     * 设置培训课程的适合年龄段
     *
     * @param age 培训课程的适合年龄段
     */
    public void setAge(String age) {
        this.age = age == null ? null : age.trim();
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
     * 获取是否限制报名（0、不限制  1、限制为只报两个）
     *
     * @return islimit - 是否限制报名（0、不限制  1、限制为只报两个）
     */
    public Integer getIslimit() {
        return islimit;
    }

    /**
     * 设置是否限制报名（0、不限制  1、限制为只报两个）
     *
     * @param islimit 是否限制报名（0、不限制  1、限制为只报两个）
     */
    public void setIslimit(Integer islimit) {
        this.islimit = islimit;
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
     * 获取区分培训与众筹培训标志：有值为众筹 没值为普通培训
     *
     * @return biz - 区分培训与众筹培训标志：有值为众筹 没值为普通培训
     */
    public String getBiz() {
        return biz;
    }

    /**
     * 设置区分培训与众筹培训标志：有值为众筹 没值为普通培训
     *
     * @param biz 区分培训与众筹培训标志：有值为众筹 没值为普通培训
     */
    public void setBiz(String biz) {
        this.biz = biz == null ? null : biz.trim();
    }

    /**
     * 获取是否直播（0 普通培训 1直播）
     *
     * @return islive - 是否直播（0 普通培训 1直播）
     */
    public Integer getIslive() {
        return islive;
    }

    /**
     * 设置是否直播（0 普通培训 1直播）
     *
     * @param islive 是否直播（0 普通培训 1直播）
     */
    public void setIslive(Integer islive) {
        this.islive = islive;
    }

    /**
     * 获取通知类型 （SMS短信  ZNX站内信）
     *
     * @return noticetype - 通知类型 （SMS短信  ZNX站内信）
     */
    public String getNoticetype() {
        return noticetype;
    }

    /**
     * 设置通知类型 （SMS短信  ZNX站内信）
     *
     * @param noticetype 通知类型 （SMS短信  ZNX站内信）
     */
    public void setNoticetype(String noticetype) {
        this.noticetype = noticetype == null ? null : noticetype.trim();
    }

    /**
     * 获取温馨提示
     *
     * @return notice - 温馨提示
     */
    public String getNotice() {
        return notice;
    }

    /**
     * 设置温馨提示
     *
     * @param notice 温馨提示
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
     * 获取在线课程是否需要报名：0-否;1-是
     *
     * @return mustsignup - 在线课程是否需要报名：0-否;1-是
     */
    public Integer getMustsignup() {
        return mustsignup;
    }

    /**
     * 设置在线课程是否需要报名：0-否;1-是
     *
     * @param mustsignup 在线课程是否需要报名：0-否;1-是
     */
    public void setMustsignup(Integer mustsignup) {
        this.mustsignup = mustsignup;
    }

    /**
     * 获取培训所属的培训组标识
     *
     * @return groupid - 培训所属的培训组标识
     */
    public String getGroupid() {
        return groupid;
    }

    /**
     * 设置培训所属的培训组标识
     *
     * @param groupid 培训所属的培训组标识
     */
    public void setGroupid(String groupid) {
        this.groupid = groupid == null ? null : groupid.trim();
    }

    /**
     * 获取主办方
     *
     * @return host - 主办方
     */
    public String getHost() {
        return host;
    }

    /**
     * 设置主办方
     *
     * @param host 主办方
     */
    public void setHost(String host) {
        this.host = host == null ? null : host.trim();
    }

    /**
     * 获取省级推荐标记：0取消，1推荐
     *
     * @return toprovince - 省级推荐标记：0取消，1推荐
     */
    public Integer getToprovince() {
        return toprovince;
    }

    /**
     * 设置省级推荐标记：0取消，1推荐
     *
     * @param toprovince 省级推荐标记：0取消，1推荐
     */
    public void setToprovince(Integer toprovince) {
        this.toprovince = toprovince;
    }

    /**
     * 获取市级推荐标记：0取消，1推荐
     *
     * @return tocity - 市级推荐标记：0取消，1推荐
     */
    public Integer getTocity() {
        return tocity;
    }

    /**
     * 设置市级推荐标记：0取消，1推荐
     *
     * @param tocity 市级推荐标记：0取消，1推荐
     */
    public void setTocity(Integer tocity) {
        this.tocity = tocity;
    }

    /**
     * 获取是否摇号：1 是；0否；
     *
     * @return isyaohao - 是否摇号：1 是；0否；
     */
    public Integer getIsyaohao() {
        return isyaohao;
    }

    /**
     * 设置是否摇号：1 是；0否；
     *
     * @param isyaohao 是否摇号：1 是；0否；
     */
    public void setIsyaohao(Integer isyaohao) {
        this.isyaohao = isyaohao;
    }

    /**
     * 获取培训课程描述
     *
     * @return coursedesc - 培训课程描述
     */
    public String getCoursedesc() {
        return coursedesc;
    }

    /**
     * 设置培训课程描述
     *
     * @param coursedesc 培训课程描述
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

    /**
     * 获取培训老师介绍
     *
     * @return teacherdesc - 培训老师介绍
     */
    public String getTeacherdesc() {
        return teacherdesc;
    }

    /**
     * 设置培训老师介绍
     *
     * @param teacherdesc 培训老师介绍
     */
    public void setTeacherdesc(String teacherdesc) {
        this.teacherdesc = teacherdesc == null ? null : teacherdesc.trim();
    }
}