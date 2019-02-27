package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_live_comm")
public class WhgLiveComm {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 直播名称
     */
    private String name;

    /**
     * 开始时间
     */
    private Date starttime;

    /**
     * 结束时间
     */
    private Date endtime;

    /**
     * 直播关联类型。1-活动，2-培训，3-在线课程
     */
    private Integer reftype;

    /**
     * 关联对象的标识
     */
    private String refid;

    /**
     * 关联对象为培训时，直播对应的课程标识
     */
    private String courseid;

    /**
     * 阿里直播AppName
     */
    private String appname;

    /**
     * 阿里直播StreamName
     */
    private String streamname;

    /**
     * 直播地址
     */
    private String playaddr;

    /**
     * 阿里直播推流地址
     */
    private String flowaddr;

    /**
     * 阿里直播回看地址
     */
    private String liveaddr;

    /**
     * 自己上传的视频资源地址，用做直播回看
     */
    private String enturl;

    /**
     * 直播介绍
     */
    private String memo;

    /**
     * 创建时间
     */
    private Date crtdate;

    /**
     * 创建人标识
     */
    private String crtuser;

    /**
     * 文化馆标识
     */
    private String cultid;

    /**
     * 部门标识
     */
    private String deptid;

    /**
     * 状态。1-启用，0-停用
     */
    private Integer state;

    /**
     * 预览视频地址
     */
    private String advancevideoaddr;
    /**
     * 添加方式 1-推流地址和直播地址添加方式 2-直接添加直播地址方式 3-链接跳转方式
     */
    private String addtype;

    /**
     * 预览视频的名称
     */
    private String advancevideoname;

    public String getAdvancevideoname() {
        return advancevideoname;
    }

    public void setAdvancevideoname(String advancevideoname) {
        this.advancevideoname = advancevideoname;
    }

    public String getAdvancevideoaddr() {
        return advancevideoaddr;
    }

    public void setAdvancevideoaddr(String advancevideoaddr) {
        this.advancevideoaddr = advancevideoaddr;
    }

    public String getAddtype() {
        return addtype;
    }

    public void setAddtype(String addtype) {
        this.addtype = addtype;
    }

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
     * 获取直播名称
     *
     * @return name - 直播名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置直播名称
     *
     * @param name 直播名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取开始时间
     *
     * @return starttime - 开始时间
     */
    public Date getStarttime() {
        return starttime;
    }

    /**
     * 设置开始时间
     *
     * @param starttime 开始时间
     */
    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    /**
     * 获取结束时间
     *
     * @return endtime - 结束时间
     */
    public Date getEndtime() {
        return endtime;
    }

    /**
     * 设置结束时间
     *
     * @param endtime 结束时间
     */
    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    /**
     * 获取直播关联类型。1-活动，2-培训，3-在线课程
     *
     * @return reftype - 直播关联类型。1-活动，2-培训，3-在线课程
     */
    public Integer getReftype() {
        return reftype;
    }

    /**
     * 设置直播关联类型。1-活动，2-培训，3-在线课程
     *
     * @param reftype 直播关联类型。1-活动，2-培训，3-在线课程
     */
    public void setReftype(Integer reftype) {
        this.reftype = reftype;
    }

    /**
     * 获取关联对象的标识
     *
     * @return refid - 关联对象的标识
     */
    public String getRefid() {
        return refid;
    }

    /**
     * 设置关联对象的标识
     *
     * @param refid 关联对象的标识
     */
    public void setRefid(String refid) {
        this.refid = refid == null ? null : refid.trim();
    }

    /**
     * 获取关联对象为培训时，直播对应的课程标识
     *
     * @return courseid - 关联对象为培训时，直播对应的课程标识
     */
    public String getCourseid() {
        return courseid;
    }

    /**
     * 设置关联对象为培训时，直播对应的课程标识
     *
     * @param courseid 关联对象为培训时，直播对应的课程标识
     */
    public void setCourseid(String courseid) {
        this.courseid = courseid == null ? null : courseid.trim();
    }

    /**
     * 获取阿里直播AppName
     *
     * @return appname - 阿里直播AppName
     */
    public String getAppname() {
        return appname;
    }

    /**
     * 设置阿里直播AppName
     *
     * @param appname 阿里直播AppName
     */
    public void setAppname(String appname) {
        this.appname = appname == null ? null : appname.trim();
    }

    /**
     * 获取阿里直播StreamName
     *
     * @return streamname - 阿里直播StreamName
     */
    public String getStreamname() {
        return streamname;
    }

    /**
     * 设置阿里直播StreamName
     *
     * @param streamname 阿里直播StreamName
     */
    public void setStreamname(String streamname) {
        this.streamname = streamname == null ? null : streamname.trim();
    }

    /**
     * 获取直播地址
     *
     * @return playaddr - 直播地址
     */
    public String getPlayaddr() {
        return playaddr;
    }

    /**
     * 设置直播地址
     *
     * @param playaddr 直播地址
     */
    public void setPlayaddr(String playaddr) {
        this.playaddr = playaddr == null ? null : playaddr.trim();
    }

    /**
     * 获取阿里直播推流地址
     *
     * @return flowaddr - 阿里直播推流地址
     */
    public String getFlowaddr() {
        return flowaddr;
    }

    /**
     * 设置阿里直播推流地址
     *
     * @param flowaddr 阿里直播推流地址
     */
    public void setFlowaddr(String flowaddr) {
        this.flowaddr = flowaddr == null ? null : flowaddr.trim();
    }

    /**
     * 获取阿里直播回看地址
     *
     * @return liveaddr - 阿里直播回看地址
     */
    public String getLiveaddr() {
        return liveaddr;
    }

    /**
     * 设置阿里直播回看地址
     *
     * @param liveaddr 阿里直播回看地址
     */
    public void setLiveaddr(String liveaddr) {
        this.liveaddr = liveaddr == null ? null : liveaddr.trim();
    }

    /**
     * 获取自己上传的视频资源地址，用做直播回看
     *
     * @return enturl - 自己上传的视频资源地址，用做直播回看
     */
    public String getEnturl() {
        return enturl;
    }

    /**
     * 设置自己上传的视频资源地址，用做直播回看
     *
     * @param enturl 自己上传的视频资源地址，用做直播回看
     */
    public void setEnturl(String enturl) {
        this.enturl = enturl == null ? null : enturl.trim();
    }

    /**
     * 获取直播介绍
     *
     * @return memo - 直播介绍
     */
    public String getMemo() {
        return memo;
    }

    /**
     * 设置直播介绍
     *
     * @param memo 直播介绍
     */
    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
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
     * 获取创建人标识
     *
     * @return crtuser - 创建人标识
     */
    public String getCrtuser() {
        return crtuser;
    }

    /**
     * 设置创建人标识
     *
     * @param crtuser 创建人标识
     */
    public void setCrtuser(String crtuser) {
        this.crtuser = crtuser == null ? null : crtuser.trim();
    }

    /**
     * 获取文化馆标识
     *
     * @return cultid - 文化馆标识
     */
    public String getCultid() {
        return cultid;
    }

    /**
     * 设置文化馆标识
     *
     * @param cultid 文化馆标识
     */
    public void setCultid(String cultid) {
        this.cultid = cultid == null ? null : cultid.trim();
    }

    /**
     * 获取部门标识
     *
     * @return deptid - 部门标识
     */
    public String getDeptid() {
        return deptid;
    }

    /**
     * 设置部门标识
     *
     * @param deptid 部门标识
     */
    public void setDeptid(String deptid) {
        this.deptid = deptid == null ? null : deptid.trim();
    }

    /**
     * 获取状态。1-启用，0-停用
     *
     * @return state - 状态。1-启用，0-停用
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态。1-启用，0-停用
     *
     * @param state 状态。1-启用，0-停用
     */
    public void setState(Integer state) {
        this.state = state;
    }
}