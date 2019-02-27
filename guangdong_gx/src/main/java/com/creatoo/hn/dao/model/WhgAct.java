package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_act")
public class WhgAct {
    /**
     * 主键ID
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
     * 1：可编辑, 9：待审核, 2：待发布，6：已发布, 4：已下架
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
     * 删除状态 0：未删除 1： 删除
     */
    private Integer delstate;

    /**
     * 权限字段
     */
    private String epms;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 活动封面
     */
    private String imgurl;

    /**
     * 场馆Id
     */
    private String venueid;

    /**
     * 主办方
     */
    private String host;

    /**
     * 承办单位
     */
    private String organizer;

    /**
     * 协办单位
     */
    private String coorganizer;

    /**
     * 演出单位
     */
    private String performed;

    /**
     * 主讲人
     */
    private String speaker;

    /**
     * 活动开始时间
     */
    private Date starttime;

    /**
     * 活动结束时间
     */
    private Date endtime;

    /**
     * 活动地址
     */
    private String address;

    /**
     * 活动电话
     */
    private String telphone;

    /**
     * 是否消耗活动积分 1：是 2：否
     */
    private Integer integral;

    /**
     * 在线售票 1：不可预定 2：自由入座 3：在线选票
     */
    private Integer sellticket;

    /**
     * 附件
     */
    private String filepath;

    /**
     * 活动坐标经度
     */
    private Double actlon;

    /**
     * 活动坐标维度
     */
    private Double actlat;

    /**
     * 文化馆ID
     */
    private String cultid;

    /**
     * 部门ID
     */
    private String deptid;

    /**
     * 报名开始时间
     */
    private Date enterstrtime;

    /**
     * 报名结束时间
     */
    private Date enterendtime;

    /**
     * 活动室ID
     */
    private String roomid;

    /**
     * 是否推荐 0：否 1：是
     */
    private Integer isrecommend;

    /**
     * 座位总数
     */
    private Integer seatcount;

    /**
     * 每场次售票张数
     */
    private Integer ticketnum;

    /**
     * 是否收费：0不收，1收
     */
    private Integer hasfees;

    /**
     * 每场每人限制预定座位数
     */
    private Integer seats;

    /**
     * 区域ID
     */
    private String areaid;

    /**
     * 活动简介
     */
    private String actsummary;

    /**
     * 是否需要实名认证0:否 1:是
     */
    private Integer isrealname;

    /**
     * 积分数
     */
    private Integer integralnum;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区分活动与众筹活动标志：有值为众筹 没值为普通活动
     */
    private String biz;

    /**
     * 是否上大首页 0：否 1：是
     */
    private Integer isbigbanner;

    /**
     * 通知类型：SMS 短信 ZNX 站内信
     */
    private String noticetype;

    /**
     * 固定电话
     */
    private String telephone;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 跳转链接
     */
    private String acturl;

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
     * 活动描述
     */
    private String remark;

    /**
     * 获取主键ID
     *
     * @return id - 主键ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置主键ID
     *
     * @param id 主键ID
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
     * 获取1：可编辑, 9：待审核, 2：待发布，6：已发布, 4：已下架
     *
     * @return state - 1：可编辑, 9：待审核, 2：待发布，6：已发布, 4：已下架
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置1：可编辑, 9：待审核, 2：待发布，6：已发布, 4：已下架
     *
     * @param state 1：可编辑, 9：待审核, 2：待发布，6：已发布, 4：已下架
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
     * 获取权限字段
     *
     * @return epms - 权限字段
     */
    public String getEpms() {
        return epms;
    }

    /**
     * 设置权限字段
     *
     * @param epms 权限字段
     */
    public void setEpms(String epms) {
        this.epms = epms == null ? null : epms.trim();
    }

    /**
     * 获取活动名称
     *
     * @return name - 活动名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置活动名称
     *
     * @param name 活动名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取活动封面
     *
     * @return imgurl - 活动封面
     */
    public String getImgurl() {
        return imgurl;
    }

    /**
     * 设置活动封面
     *
     * @param imgurl 活动封面
     */
    public void setImgurl(String imgurl) {
        this.imgurl = imgurl == null ? null : imgurl.trim();
    }

    /**
     * 获取 场馆Id
     *
     * @return venueid -  场馆Id
     */
    public String getVenueid() {
        return venueid;
    }

    /**
     * 设置 场馆Id
     *
     * @param venueid 场馆Id
     */
    public void setVenueid(String venueid) {
        this.venueid = venueid == null ? null : venueid.trim();
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
     * 获取承办单位
     *
     * @return organizer - 承办单位
     */
    public String getOrganizer() {
        return organizer;
    }

    /**
     * 设置承办单位
     *
     * @param organizer 承办单位
     */
    public void setOrganizer(String organizer) {
        this.organizer = organizer == null ? null : organizer.trim();
    }

    /**
     * 获取协办单位
     *
     * @return coorganizer - 协办单位
     */
    public String getCoorganizer() {
        return coorganizer;
    }

    /**
     * 设置协办单位
     *
     * @param coorganizer 协办单位
     */
    public void setCoorganizer(String coorganizer) {
        this.coorganizer = coorganizer == null ? null : coorganizer.trim();
    }

    /**
     * 获取演出单位
     *
     * @return performed - 演出单位
     */
    public String getPerformed() {
        return performed;
    }

    /**
     * 设置演出单位
     *
     * @param performed 演出单位
     */
    public void setPerformed(String performed) {
        this.performed = performed == null ? null : performed.trim();
    }

    /**
     * 获取主讲人
     *
     * @return speaker - 主讲人
     */
    public String getSpeaker() {
        return speaker;
    }

    /**
     * 设置主讲人
     *
     * @param speaker 主讲人
     */
    public void setSpeaker(String speaker) {
        this.speaker = speaker == null ? null : speaker.trim();
    }

    /**
     * 获取活动开始时间
     *
     * @return starttime - 活动开始时间
     */
    public Date getStarttime() {
        return starttime;
    }

    /**
     * 设置活动开始时间
     *
     * @param starttime 活动开始时间
     */
    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    /**
     * 获取活动结束时间
     *
     * @return endtime - 活动结束时间
     */
    public Date getEndtime() {
        return endtime;
    }

    /**
     * 设置活动结束时间
     *
     * @param endtime 活动结束时间
     */
    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    /**
     * 获取活动地址
     *
     * @return address - 活动地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置活动地址
     *
     * @param address 活动地址
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * 获取活动电话
     *
     * @return telphone - 活动电话
     */
    public String getTelphone() {
        return telphone;
    }

    /**
     * 设置活动电话
     *
     * @param telphone 活动电话
     */
    public void setTelphone(String telphone) {
        this.telphone = telphone == null ? null : telphone.trim();
    }

    /**
     * 获取是否消耗活动积分 1：是 2：否
     *
     * @return integral - 是否消耗活动积分 1：是 2：否
     */
    public Integer getIntegral() {
        return integral;
    }

    /**
     * 设置是否消耗活动积分 1：是 2：否
     *
     * @param integral 是否消耗活动积分 1：是 2：否
     */
    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    /**
     * 获取在线售票 1：不可预定 2：自由入座 3：在线选票
     *
     * @return sellticket - 在线售票 1：不可预定 2：自由入座 3：在线选票
     */
    public Integer getSellticket() {
        return sellticket;
    }

    /**
     * 设置在线售票 1：不可预定 2：自由入座 3：在线选票
     *
     * @param sellticket 在线售票 1：不可预定 2：自由入座 3：在线选票
     */
    public void setSellticket(Integer sellticket) {
        this.sellticket = sellticket;
    }

    /**
     * 获取附件
     *
     * @return filepath - 附件
     */
    public String getFilepath() {
        return filepath;
    }

    /**
     * 设置附件
     *
     * @param filepath 附件
     */
    public void setFilepath(String filepath) {
        this.filepath = filepath == null ? null : filepath.trim();
    }

    /**
     * 获取活动坐标经度
     *
     * @return actlon - 活动坐标经度
     */
    public Double getActlon() {
        return actlon;
    }

    /**
     * 设置活动坐标经度
     *
     * @param actlon 活动坐标经度
     */
    public void setActlon(Double actlon) {
        this.actlon = actlon;
    }

    /**
     * 获取活动坐标维度
     *
     * @return actlat - 活动坐标维度
     */
    public Double getActlat() {
        return actlat;
    }

    /**
     * 设置活动坐标维度
     *
     * @param actlat 活动坐标维度
     */
    public void setActlat(Double actlat) {
        this.actlat = actlat;
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
     * 获取部门ID
     *
     * @return deptid - 部门ID
     */
    public String getDeptid() {
        return deptid;
    }

    /**
     * 设置部门ID
     *
     * @param deptid 部门ID
     */
    public void setDeptid(String deptid) {
        this.deptid = deptid == null ? null : deptid.trim();
    }

    /**
     * 获取报名开始时间
     *
     * @return enterstrtime - 报名开始时间
     */
    public Date getEnterstrtime() {
        return enterstrtime;
    }

    /**
     * 设置报名开始时间
     *
     * @param enterstrtime 报名开始时间
     */
    public void setEnterstrtime(Date enterstrtime) {
        this.enterstrtime = enterstrtime;
    }

    /**
     * 获取报名结束时间
     *
     * @return enterendtime - 报名结束时间
     */
    public Date getEnterendtime() {
        return enterendtime;
    }

    /**
     * 设置报名结束时间
     *
     * @param enterendtime 报名结束时间
     */
    public void setEnterendtime(Date enterendtime) {
        this.enterendtime = enterendtime;
    }

    /**
     * 获取活动室ID
     *
     * @return roomid - 活动室ID
     */
    public String getRoomid() {
        return roomid;
    }

    /**
     * 设置活动室ID
     *
     * @param roomid 活动室ID
     */
    public void setRoomid(String roomid) {
        this.roomid = roomid == null ? null : roomid.trim();
    }

    /**
     * 获取是否推荐 0：否 1：是
     *
     * @return isrecommend - 是否推荐 0：否 1：是
     */
    public Integer getIsrecommend() {
        return isrecommend;
    }

    /**
     * 设置是否推荐 0：否 1：是
     *
     * @param isrecommend 是否推荐 0：否 1：是
     */
    public void setIsrecommend(Integer isrecommend) {
        this.isrecommend = isrecommend;
    }

    /**
     * 获取座位总数
     *
     * @return seatcount - 座位总数
     */
    public Integer getSeatcount() {
        return seatcount;
    }

    /**
     * 设置座位总数
     *
     * @param seatcount 座位总数
     */
    public void setSeatcount(Integer seatcount) {
        this.seatcount = seatcount;
    }

    /**
     * 获取每场次售票张数
     *
     * @return ticketnum - 每场次售票张数
     */
    public Integer getTicketnum() {
        return ticketnum;
    }

    /**
     * 设置每场次售票张数
     *
     * @param ticketnum 每场次售票张数
     */
    public void setTicketnum(Integer ticketnum) {
        this.ticketnum = ticketnum;
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
     * 获取每场每人限制预定座位数
     *
     * @return seats - 每场每人限制预定座位数
     */
    public Integer getSeats() {
        return seats;
    }

    /**
     * 设置每场每人限制预定座位数
     *
     * @param seats 每场每人限制预定座位数
     */
    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    /**
     * 获取区域ID
     *
     * @return areaid - 区域ID
     */
    public String getAreaid() {
        return areaid;
    }

    /**
     * 设置区域ID
     *
     * @param areaid 区域ID
     */
    public void setAreaid(String areaid) {
        this.areaid = areaid == null ? null : areaid.trim();
    }

    /**
     * 获取活动简介
     *
     * @return actsummary - 活动简介
     */
    public String getActsummary() {
        return actsummary;
    }

    /**
     * 设置活动简介
     *
     * @param actsummary 活动简介
     */
    public void setActsummary(String actsummary) {
        this.actsummary = actsummary == null ? null : actsummary.trim();
    }

    /**
     * 获取是否需要实名认证0:否 1:是
     *
     * @return isrealname - 是否需要实名认证0:否 1:是
     */
    public Integer getIsrealname() {
        return isrealname;
    }

    /**
     * 设置是否需要实名认证0:否 1:是
     *
     * @param isrealname 是否需要实名认证0:否 1:是
     */
    public void setIsrealname(Integer isrealname) {
        this.isrealname = isrealname;
    }

    /**
     * 获取积分数
     *
     * @return integralnum - 积分数
     */
    public Integer getIntegralnum() {
        return integralnum;
    }

    /**
     * 设置积分数
     *
     * @param integralnum 积分数
     */
    public void setIntegralnum(Integer integralnum) {
        this.integralnum = integralnum;
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
     * 获取区分活动与众筹活动标志：有值为众筹 没值为普通活动
     *
     * @return biz - 区分活动与众筹活动标志：有值为众筹 没值为普通活动
     */
    public String getBiz() {
        return biz;
    }

    /**
     * 设置区分活动与众筹活动标志：有值为众筹 没值为普通活动
     *
     * @param biz 区分活动与众筹活动标志：有值为众筹 没值为普通活动
     */
    public void setBiz(String biz) {
        this.biz = biz == null ? null : biz.trim();
    }

    /**
     * 获取是否上大首页 0：否 1：是
     *
     * @return isbigbanner - 是否上大首页 0：否 1：是
     */
    public Integer getIsbigbanner() {
        return isbigbanner;
    }

    /**
     * 设置是否上大首页 0：否 1：是
     *
     * @param isbigbanner 是否上大首页 0：否 1：是
     */
    public void setIsbigbanner(Integer isbigbanner) {
        this.isbigbanner = isbigbanner;
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
     * 获取固定电话
     *
     * @return telephone - 固定电话
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * 设置固定电话
     *
     * @param telephone 固定电话
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
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
     * 获取跳转链接
     *
     * @return acturl - 跳转链接
     */
    public String getActurl() {
        return acturl;
    }

    /**
     * 设置跳转链接
     *
     * @param acturl 跳转链接
     */
    public void setActurl(String acturl) {
        this.acturl = acturl == null ? null : acturl.trim();
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
     * 获取活动描述
     *
     * @return remark - 活动描述
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置活动描述
     *
     * @param remark 活动描述
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}