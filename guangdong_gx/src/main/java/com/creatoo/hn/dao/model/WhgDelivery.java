package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_delivery")
public class WhgDelivery {
    /**
     * 主键ID
     */
    @Id
    private String id;

    /**
     * 标题
     */
    private String name;

    private Date crtdate;

    /**
     * 配送申请人
     */
    private String crtuser;

    /**
     * 配送状态 ：0：审核不通过被打回 1：申请待审核, 2：审核通过配送中, 3：配送成功，4：失效
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
     * 关联id：关于哪个件的配送信息
     */
    private String fkid;

    /**
     * 开始配送时间
     */
    private Date starttime;

    /**
     * 配送结束时间
     */
    private Date endtime;

    /**
     * 区域
     */
    private String region;

    /**
     * 附件
     */
    private String attachment;

    /**
     * 审核人（申请递交人）
     */
    private String touser;

    /**
     * 文化馆ID
     */
    private String cultid;

    /**
     * 部门ID
     */
    private String deptid;

    /**
     * 备注
     */
    private String memo;

    /**
     * 配送供需类型：0供给，1需求
     */
    private Integer type;

    /**
     * 删除状态 0：未删除 1： 删除
     */
    private Integer delstate;

    /**
     * 关联类型：25, 人才类型-26，供需。。。
     */
    private String fktype;

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
     * 获取标题
     *
     * @return name - 标题
     */
    public String getName() {
        return name;
    }

    /**
     * 设置标题
     *
     * @param name 标题
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * @return crtdate
     */
    public Date getCrtdate() {
        return crtdate;
    }

    /**
     * @param crtdate
     */
    public void setCrtdate(Date crtdate) {
        this.crtdate = crtdate;
    }

    /**
     * 获取配送申请人
     *
     * @return crtuser - 配送申请人
     */
    public String getCrtuser() {
        return crtuser;
    }

    /**
     * 设置配送申请人
     *
     * @param crtuser 配送申请人
     */
    public void setCrtuser(String crtuser) {
        this.crtuser = crtuser == null ? null : crtuser.trim();
    }

    /**
     * 获取配送状态 ：0：审核不通过被打回 1：申请待审核, 2：审核通过配送中, 3：配送成功，4：失效
     *
     * @return state - 配送状态 ：0：审核不通过被打回 1：申请待审核, 2：审核通过配送中, 3：配送成功，4：失效
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置配送状态 ：0：审核不通过被打回 1：申请待审核, 2：审核通过配送中, 3：配送成功，4：失效
     *
     * @param state 配送状态 ：0：审核不通过被打回 1：申请待审核, 2：审核通过配送中, 3：配送成功，4：失效
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
     * 获取关联id：关于哪个件的配送信息
     *
     * @return fkid - 关联id：关于哪个件的配送信息
     */
    public String getFkid() {
        return fkid;
    }

    /**
     * 设置关联id：关于哪个件的配送信息
     *
     * @param fkid 关联id：关于哪个件的配送信息
     */
    public void setFkid(String fkid) {
        this.fkid = fkid == null ? null : fkid.trim();
    }

    /**
     * 获取开始配送时间
     *
     * @return starttime - 开始配送时间
     */
    public Date getStarttime() {
        return starttime;
    }

    /**
     * 设置开始配送时间
     *
     * @param starttime 开始配送时间
     */
    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    /**
     * 获取配送结束时间
     *
     * @return endtime - 配送结束时间
     */
    public Date getEndtime() {
        return endtime;
    }

    /**
     * 设置配送结束时间
     *
     * @param endtime 配送结束时间
     */
    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    /**
     * 获取区域
     *
     * @return region - 区域
     */
    public String getRegion() {
        return region;
    }

    /**
     * 设置区域
     *
     * @param region 区域
     */
    public void setRegion(String region) {
        this.region = region == null ? null : region.trim();
    }

    /**
     * 获取附件
     *
     * @return attachment - 附件
     */
    public String getAttachment() {
        return attachment;
    }

    /**
     * 设置附件
     *
     * @param attachment 附件
     */
    public void setAttachment(String attachment) {
        this.attachment = attachment == null ? null : attachment.trim();
    }

    /**
     * 获取审核人（申请递交人）
     *
     * @return touser - 审核人（申请递交人）
     */
    public String getTouser() {
        return touser;
    }

    /**
     * 设置审核人（申请递交人）
     *
     * @param touser 审核人（申请递交人）
     */
    public void setTouser(String touser) {
        this.touser = touser == null ? null : touser.trim();
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
     * 获取备注
     *
     * @return memo - 备注
     */
    public String getMemo() {
        return memo;
    }

    /**
     * 设置备注
     *
     * @param memo 备注
     */
    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    /**
     * 获取配送供需类型：0供给，1需求
     *
     * @return type - 配送供需类型：0供给，1需求
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置配送供需类型：0供给，1需求
     *
     * @param type 配送供需类型：0供给，1需求
     */
    public void setType(Integer type) {
        this.type = type;
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
     * 获取关联类型：25, 人才类型-26，供需。。。
     *
     * @return fktype - 关联类型：25, 人才类型-26，供需。。。
     */
    public String getFktype() {
        return fktype;
    }

    /**
     * 设置关联类型：25, 人才类型-26，供需。。。
     *
     * @param fktype 关联类型：25, 人才类型-26，供需。。。
     */
    public void setFktype(String fktype) {
        this.fktype = fktype == null ? null : fktype.trim();
    }
}