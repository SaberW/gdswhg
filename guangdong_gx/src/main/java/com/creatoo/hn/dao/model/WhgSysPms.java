package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_sys_pms")
public class WhgSysPms {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 状态.0-无效，1-有效
     */
    private Integer state;

    /**
     * 删除状态。0-未删除， 1-已删除
     */
    private Integer delstate;

    /**
     * 修改状态的用户
     */
    private String statemdfuser;

    /**
     * 修改状态的时间
     */
    private Date statemdfdate;

    /**
     * 创建管理员标识
     */
    private String crtuser;

    /**
     * 创建时间
     */
    private Date crtdate;

    /**
     * 名称
     */
    private String name;

    /**
     * 权限组分类
     */
    private String type;

    /**
     * 所属系统.bizmgr-业务管理系统,sysmgr-总分馆管理系统
     */
    private String sysflag;

    /**
     * 权限组说明
     */
    private String memo;

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
     * 获取状态.0-无效，1-有效
     *
     * @return state - 状态.0-无效，1-有效
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态.0-无效，1-有效
     *
     * @param state 状态.0-无效，1-有效
     */
    public void setState(Integer state) {
        this.state = state;
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
     * 获取权限组分类
     *
     * @return type - 权限组分类
     */
    public String getType() {
        return type;
    }

    /**
     * 设置权限组分类
     *
     * @param type 权限组分类
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
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
     * 获取权限组说明
     *
     * @return memo - 权限组说明
     */
    public String getMemo() {
        return memo;
    }

    /**
     * 设置权限组说明
     *
     * @param memo 权限组说明
     */
    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}