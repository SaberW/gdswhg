package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_ywi_group")
public class WhgYwiGroup {
    /**
     * 培训组唯一标识
     */
    @Id
    private String id;

    /**
     * 培训组名称
     */
    private String name;

    /**
     * 每人最可报名培训组中的多少个培训
     */
    private Integer max;

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
     * 关联文化馆标识
     */
    private String cultid;

    /**
     * 获取培训组唯一标识
     *
     * @return id - 培训组唯一标识
     */
    public String getId() {
        return id;
    }

    /**
     * 设置培训组唯一标识
     *
     * @param id 培训组唯一标识
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取培训组名称
     *
     * @return name - 培训组名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置培训组名称
     *
     * @param name 培训组名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取每人最可报名培训组中的多少个培训
     *
     * @return max - 每人最可报名培训组中的多少个培训
     */
    public Integer getMax() {
        return max;
    }

    /**
     * 设置每人最可报名培训组中的多少个培训
     *
     * @param max 每人最可报名培训组中的多少个培训
     */
    public void setMax(Integer max) {
        this.max = max;
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
     * 获取关联文化馆标识
     *
     * @return cultid - 关联文化馆标识
     */
    public String getCultid() {
        return cultid;
    }

    /**
     * 设置关联文化馆标识
     *
     * @param cultid 关联文化馆标识
     */
    public void setCultid(String cultid) {
        this.cultid = cultid == null ? null : cultid.trim();
    }
}