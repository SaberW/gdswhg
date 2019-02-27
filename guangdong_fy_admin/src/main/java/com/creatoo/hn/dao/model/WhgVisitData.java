package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_visit_data")
public class WhgVisitData {
    /**
     * 主键
     */
    @Id
    @Column(name = "visit_id")
    private String visitId;

    /**
     * 访问IP
     */
    @Column(name = "visit_ip")
    private String visitIp;

    /**
     * 访问页面
     */
    @Column(name = "visit_page")
    private String visitPage;

    /**
     * 访客
     */
    @Column(name = "visit_user")
    private String visitUser;

    /**
     * 访问次数
     */
    @Column(name = "visit_count")
    private Integer visitCount;

    /**
     * 访问类型(1:PC,2:weixin,3:Android,4:IOS)
     */
    @Column(name = "visit_type")
    private Integer visitType;

    /**
     * 访问日期
     */
    @Column(name = "visit_date")
    private String visitDate;

    /**
     * 访问时间
     */
    @Column(name = "visit_time")
    private Date visitTime;

    /**
     * 访问类型。1-活动, 2-培训, 3-场馆, 4-活动室, 5-资讯, 9-其它
     */
    @Column(name = "visit_etype")
    private Integer visitEtype;

    /**
     * 访问类型的对象ID
     */
    @Column(name = "visit_eid")
    private String visitEid;

    /**
     * 获取主键
     *
     * @return visit_id - 主键
     */
    public String getVisitId() {
        return visitId;
    }

    /**
     * 设置主键
     *
     * @param visitId 主键
     */
    public void setVisitId(String visitId) {
        this.visitId = visitId == null ? null : visitId.trim();
    }

    /**
     * 获取访问IP
     *
     * @return visit_ip - 访问IP
     */
    public String getVisitIp() {
        return visitIp;
    }

    /**
     * 设置访问IP
     *
     * @param visitIp 访问IP
     */
    public void setVisitIp(String visitIp) {
        this.visitIp = visitIp == null ? null : visitIp.trim();
    }

    /**
     * 获取访问页面
     *
     * @return visit_page - 访问页面
     */
    public String getVisitPage() {
        return visitPage;
    }

    /**
     * 设置访问页面
     *
     * @param visitPage 访问页面
     */
    public void setVisitPage(String visitPage) {
        this.visitPage = visitPage == null ? null : visitPage.trim();
    }

    /**
     * 获取访客
     *
     * @return visit_user - 访客
     */
    public String getVisitUser() {
        return visitUser;
    }

    /**
     * 设置访客
     *
     * @param visitUser 访客
     */
    public void setVisitUser(String visitUser) {
        this.visitUser = visitUser == null ? null : visitUser.trim();
    }

    /**
     * 获取访问次数
     *
     * @return visit_count - 访问次数
     */
    public Integer getVisitCount() {
        return visitCount;
    }

    /**
     * 设置访问次数
     *
     * @param visitCount 访问次数
     */
    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }

    /**
     * 获取访问类型(1:PC,2:weixin,3:Android,4:IOS)
     *
     * @return visit_type - 访问类型(1:PC,2:weixin,3:Android,4:IOS)
     */
    public Integer getVisitType() {
        return visitType;
    }

    /**
     * 设置访问类型(1:PC,2:weixin,3:Android,4:IOS)
     *
     * @param visitType 访问类型(1:PC,2:weixin,3:Android,4:IOS)
     */
    public void setVisitType(Integer visitType) {
        this.visitType = visitType;
    }

    /**
     * 获取访问日期
     *
     * @return visit_date - 访问日期
     */
    public String getVisitDate() {
        return visitDate;
    }

    /**
     * 设置访问日期
     *
     * @param visitDate 访问日期
     */
    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate == null ? null : visitDate.trim();
    }

    /**
     * 获取访问时间
     *
     * @return visit_time - 访问时间
     */
    public Date getVisitTime() {
        return visitTime;
    }

    /**
     * 设置访问时间
     *
     * @param visitTime 访问时间
     */
    public void setVisitTime(Date visitTime) {
        this.visitTime = visitTime;
    }

    /**
     * 获取访问类型。1-活动, 2-培训, 3-场馆, 4-活动室, 5-资讯, 9-其它
     *
     * @return visit_etype - 访问类型。1-活动, 2-培训, 3-场馆, 4-活动室, 5-资讯, 9-其它
     */
    public Integer getVisitEtype() {
        return visitEtype;
    }

    /**
     * 设置访问类型。1-活动, 2-培训, 3-场馆, 4-活动室, 5-资讯, 9-其它
     *
     * @param visitEtype 访问类型。1-活动, 2-培训, 3-场馆, 4-活动室, 5-资讯, 9-其它
     */
    public void setVisitEtype(Integer visitEtype) {
        this.visitEtype = visitEtype;
    }

    /**
     * 获取访问类型的对象ID
     *
     * @return visit_eid - 访问类型的对象ID
     */
    public String getVisitEid() {
        return visitEid;
    }

    /**
     * 设置访问类型的对象ID
     *
     * @param visitEid 访问类型的对象ID
     */
    public void setVisitEid(String visitEid) {
        this.visitEid = visitEid == null ? null : visitEid.trim();
    }
}