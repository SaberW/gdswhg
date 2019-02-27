package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_sys_job")
public class WhgSysJob {
    /**
     * 岗位标识
     */
    @Id
    private String id;

    /**
     * 创建管理员标识
     */
    private String crtuser;

    /**
     * 创建时间
     */
    private Date crtdate;

    /**
     * 状态，参考开关状态枚举
     */
    private Integer state;

    /**
     * 修改状态的时间
     */
    private Date statemdfdate;

    /**
     * 修改状态的用户
     */
    private String statemdfuser;

    /**
     * 删除状态。0-未删除， 1-已删除
     */
    private Integer delstate;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String memo;

    /**
     * 所属系统.bizmgr-业务管理系统,sysmgr-总分馆管理系统
     */
    private String sysflag;

    /**
     * 所属文化馆
     */
    private String cultid;

    /**
     * 获取岗位标识
     *
     * @return id - 岗位标识
     */
    public String getId() {
        return id;
    }

    /**
     * 设置岗位标识
     *
     * @param id 岗位标识
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取创建管理员标识
     *
     * @return crtuser - 创建管理员标识
     */
    public String getCrtuser() {
        return crtuser;
    }

    /**
     * 设置创建管理员标识
     *
     * @param crtuser 创建管理员标识
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
     * 获取状态，参考开关状态枚举
     *
     * @return state - 状态，参考开关状态枚举
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态，参考开关状态枚举
     *
     * @param state 状态，参考开关状态枚举
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取修改状态的时间
     *
     * @return statemdfdate - 修改状态的时间
     */
    public Date getStatemdfdate() {
        return statemdfdate;
    }

    /**
     * 设置修改状态的时间
     *
     * @param statemdfdate 修改状态的时间
     */
    public void setStatemdfdate(Date statemdfdate) {
        this.statemdfdate = statemdfdate;
    }

    /**
     * 获取修改状态的用户
     *
     * @return statemdfuser - 修改状态的用户
     */
    public String getStatemdfuser() {
        return statemdfuser;
    }

    /**
     * 设置修改状态的用户
     *
     * @param statemdfuser 修改状态的用户
     */
    public void setStatemdfuser(String statemdfuser) {
        this.statemdfuser = statemdfuser == null ? null : statemdfuser.trim();
    }

    /**
     * 获取删除状态。0-未删除， 1-已删除
     *
     * @return delstate - 删除状态。0-未删除， 1-已删除
     */
    public Integer getDelstate() {
        return delstate;
    }

    /**
     * 设置删除状态。0-未删除， 1-已删除
     *
     * @param delstate 删除状态。0-未删除， 1-已删除
     */
    public void setDelstate(Integer delstate) {
        this.delstate = delstate;
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
     * 获取描述
     *
     * @return memo - 描述
     */
    public String getMemo() {
        return memo;
    }

    /**
     * 设置描述
     *
     * @param memo 描述
     */
    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    /**
     * 获取所属系统.bizmgr-业务管理系统,sysmgr-总分馆管理系统
     *
     * @return sysflag - 所属系统.bizmgr-业务管理系统,sysmgr-总分馆管理系统
     */
    public String getSysflag() {
        return sysflag;
    }

    /**
     * 设置所属系统.bizmgr-业务管理系统,sysmgr-总分馆管理系统
     *
     * @param sysflag 所属系统.bizmgr-业务管理系统,sysmgr-总分馆管理系统
     */
    public void setSysflag(String sysflag) {
        this.sysflag = sysflag == null ? null : sysflag.trim();
    }

    /**
     * 获取所属文化馆
     *
     * @return cultid - 所属文化馆
     */
    public String getCultid() {
        return cultid;
    }

    /**
     * 设置所属文化馆
     *
     * @param cultid 所属文化馆
     */
    public void setCultid(String cultid) {
        this.cultid = cultid == null ? null : cultid.trim();
    }
}