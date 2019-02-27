package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_sys_dept")
public class WhgSysDept {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 关联文化馆ID
     */
    private String cultid;

    /**
     * 创建时间
     */
    private Date crtdate;

    /**
     * 创建用户
     */
    private String crtuser;

    /**
     * 状态（参考审核状态枚举）
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
     * 删除状态. （参考删除状态枚举）
     */
    private Integer delstate;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 层级关系编码，父部门ID_子部门ID
     */
    private String code;

    /**
     * 上级ID，顶级为空
     */
    private String pid;

    /**
     * 简介
     */
    private String summary;

    private Integer isfront;

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
     * 获取关联文化馆ID
     *
     * @return cultid - 关联文化馆ID
     */
    public String getCultid() {
        return cultid;
    }

    /**
     * 设置关联文化馆ID
     *
     * @param cultid 关联文化馆ID
     */
    public void setCultid(String cultid) {
        this.cultid = cultid == null ? null : cultid.trim();
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
     * 获取创建用户
     *
     * @return crtuser - 创建用户
     */
    public String getCrtuser() {
        return crtuser;
    }

    /**
     * 设置创建用户
     *
     * @param crtuser 创建用户
     */
    public void setCrtuser(String crtuser) {
        this.crtuser = crtuser == null ? null : crtuser.trim();
    }

    /**
     * 获取状态（参考审核状态枚举）
     *
     * @return state - 状态（参考审核状态枚举）
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态（参考审核状态枚举）
     *
     * @param state 状态（参考审核状态枚举）
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
     * 获取删除状态. （参考删除状态枚举）
     *
     * @return delstate - 删除状态. （参考删除状态枚举）
     */
    public Integer getDelstate() {
        return delstate;
    }

    /**
     * 设置删除状态. （参考删除状态枚举）
     *
     * @param delstate 删除状态. （参考删除状态枚举）
     */
    public void setDelstate(Integer delstate) {
        this.delstate = delstate;
    }

    /**
     * 获取部门名称
     *
     * @return name - 部门名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置部门名称
     *
     * @param name 部门名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取层级关系编码，父部门ID_子部门ID
     *
     * @return code - 层级关系编码，父部门ID_子部门ID
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置层级关系编码，父部门ID_子部门ID
     *
     * @param code 层级关系编码，父部门ID_子部门ID
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 获取上级ID，顶级为空
     *
     * @return pid - 上级ID，顶级为空
     */
    public String getPid() {
        return pid;
    }

    /**
     * 设置上级ID，顶级为空
     *
     * @param pid 上级ID，顶级为空
     */
    public void setPid(String pid) {
        this.pid = pid == null ? null : pid.trim();
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
     * @return isfront
     */
    public Integer getIsfront() {
        return isfront;
    }

    /**
     * @param isfront
     */
    public void setIsfront(Integer isfront) {
        this.isfront = isfront;
    }
}