package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_act_seat")
public class WhgActivitySeat {
    /**
     * 活动座位表主键ID
     */
    @Id
    private String id;

    /**
     * 活动座位价格
     */
    private Integer seatprice;

    /**
     * 座位出票状态(1-未出票 2-已出票 3-出票失败)
     */
    private Integer seatticketstatus;

    /**
     * 活动ID
     */
    private String activityid;

    /**
     * 座位状态(1-正常2-待修 3-不存在)
     */
    private Integer seatstatus;

    /**
     * 座位坐标(行)
     */
    private Integer seatrow;

    /**
     * 座位坐标(列)
     */
    private Integer seatcolumn;

    /**
     * 座位区域
     */
    private String seatarea;

    /**
     * 座位编号
     */
    private String seatcode;

    /**
     * 座位创建时间
     */
    private Date crtdate;

    /**
     * 座位最后修改时间
     */
    private Date statemdfdate;

    /**
     * 座位创建人
     */
    private String crtuser;

    /**
     * 座位最后修改人
     */
    private String statemdfuser;

    /**
     * 座位编号，座位别名
     */
    private String seatnum;

    /**
     * 获取活动座位表主键ID
     *
     * @return id - 活动座位表主键ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置活动座位表主键ID
     *
     * @param id 活动座位表主键ID
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取活动座位价格
     *
     * @return seatprice - 活动座位价格
     */
    public Integer getSeatprice() {
        return seatprice;
    }

    /**
     * 设置活动座位价格
     *
     * @param seatprice 活动座位价格
     */
    public void setSeatprice(Integer seatprice) {
        this.seatprice = seatprice;
    }

    /**
     * 获取座位出票状态(1-未出票 2-已出票 3-出票失败)
     *
     * @return seatticketstatus - 座位出票状态(1-未出票 2-已出票 3-出票失败)
     */
    public Integer getSeatticketstatus() {
        return seatticketstatus;
    }

    /**
     * 设置座位出票状态(1-未出票 2-已出票 3-出票失败)
     *
     * @param seatticketstatus 座位出票状态(1-未出票 2-已出票 3-出票失败)
     */
    public void setSeatticketstatus(Integer seatticketstatus) {
        this.seatticketstatus = seatticketstatus;
    }

    /**
     * 获取活动ID
     *
     * @return activityid - 活动ID
     */
    public String getActivityid() {
        return activityid;
    }

    /**
     * 设置活动ID
     *
     * @param activityid 活动ID
     */
    public void setActivityid(String activityid) {
        this.activityid = activityid == null ? null : activityid.trim();
    }

    /**
     * 获取座位状态(1-正常2-待修 3-不存在)
     *
     * @return seatstatus - 座位状态(1-正常2-待修 3-不存在)
     */
    public Integer getSeatstatus() {
        return seatstatus;
    }

    /**
     * 设置座位状态(1-正常2-待修 3-不存在)
     *
     * @param seatstatus 座位状态(1-正常2-待修 3-不存在)
     */
    public void setSeatstatus(Integer seatstatus) {
        this.seatstatus = seatstatus;
    }

    /**
     * 获取座位坐标(行)
     *
     * @return seatrow - 座位坐标(行)
     */
    public Integer getSeatrow() {
        return seatrow;
    }

    /**
     * 设置座位坐标(行)
     *
     * @param seatrow 座位坐标(行)
     */
    public void setSeatrow(Integer seatrow) {
        this.seatrow = seatrow;
    }

    /**
     * 获取座位坐标(列)
     *
     * @return seatcolumn - 座位坐标(列)
     */
    public Integer getSeatcolumn() {
        return seatcolumn;
    }

    /**
     * 设置座位坐标(列)
     *
     * @param seatcolumn 座位坐标(列)
     */
    public void setSeatcolumn(Integer seatcolumn) {
        this.seatcolumn = seatcolumn;
    }

    /**
     * 获取座位区域
     *
     * @return seatarea - 座位区域
     */
    public String getSeatarea() {
        return seatarea;
    }

    /**
     * 设置座位区域
     *
     * @param seatarea 座位区域
     */
    public void setSeatarea(String seatarea) {
        this.seatarea = seatarea == null ? null : seatarea.trim();
    }

    /**
     * 获取座位编号
     *
     * @return seatcode - 座位编号
     */
    public String getSeatcode() {
        return seatcode;
    }

    /**
     * 设置座位编号
     *
     * @param seatcode 座位编号
     */
    public void setSeatcode(String seatcode) {
        this.seatcode = seatcode == null ? null : seatcode.trim();
    }

    /**
     * 获取座位创建时间
     *
     * @return crtdate - 座位创建时间
     */
    public Date getCrtdate() {
        return crtdate;
    }

    /**
     * 设置座位创建时间
     *
     * @param crtdate 座位创建时间
     */
    public void setCrtdate(Date crtdate) {
        this.crtdate = crtdate;
    }

    /**
     * 获取座位最后修改时间
     *
     * @return statemdfdate - 座位最后修改时间
     */
    public Date getStatemdfdate() {
        return statemdfdate;
    }

    /**
     * 设置座位最后修改时间
     *
     * @param statemdfdate 座位最后修改时间
     */
    public void setStatemdfdate(Date statemdfdate) {
        this.statemdfdate = statemdfdate;
    }

    /**
     * 获取座位创建人
     *
     * @return crtuser - 座位创建人
     */
    public String getCrtuser() {
        return crtuser;
    }

    /**
     * 设置座位创建人
     *
     * @param crtuser 座位创建人
     */
    public void setCrtuser(String crtuser) {
        this.crtuser = crtuser == null ? null : crtuser.trim();
    }

    /**
     * 获取座位最后修改人
     *
     * @return statemdfuser - 座位最后修改人
     */
    public String getStatemdfuser() {
        return statemdfuser;
    }

    /**
     * 设置座位最后修改人
     *
     * @param statemdfuser 座位最后修改人
     */
    public void setStatemdfuser(String statemdfuser) {
        this.statemdfuser = statemdfuser == null ? null : statemdfuser.trim();
    }

    /**
     * 获取座位编号，座位别名
     *
     * @return seatnum - 座位编号，座位别名
     */
    public String getSeatnum() {
        return seatnum;
    }

    /**
     * 设置座位编号，座位别名
     *
     * @param seatnum 座位编号，座位别名
     */
    public void setSeatnum(String seatnum) {
        this.seatnum = seatnum == null ? null : seatnum.trim();
    }
}