package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_ywi_type")
public class WhgYwiType {
    /**
     * 类型ID
     */
    @Id
    private String id;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 类型
     */
    private Integer type;

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
     * 排序
     */
    private Integer idx;

    /**
     * 关联文化馆ID
     */
    private String cultid;

    /**
     * 去除id：存放被禁止显示的cultid
     */
    private String closeid;

    /**
     * 父id
     */
    private String pid;

    /**
     * 类型图标地址
     */
    private String typeicon;

    /**
     * 获取类型ID
     *
     * @return id - 类型ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置类型ID
     *
     * @param id 类型ID
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取类型名称
     *
     * @return name - 类型名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置类型名称
     *
     * @param name 类型名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取类型
     *
     * @return type - 类型
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置类型
     *
     * @param type 类型
     */
    public void setType(Integer type) {
        this.type = type;
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
     * 获取排序
     *
     * @return idx - 排序
     */
    public Integer getIdx() {
        return idx;
    }

    /**
     * 设置排序
     *
     * @param idx 排序
     */
    public void setIdx(Integer idx) {
        this.idx = idx;
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
     * 获取去除id：存放被禁止显示的cultid
     *
     * @return closeid - 去除id：存放被禁止显示的cultid
     */
    public String getCloseid() {
        return closeid;
    }

    /**
     * 设置去除id：存放被禁止显示的cultid
     *
     * @param closeid 去除id：存放被禁止显示的cultid
     */
    public void setCloseid(String closeid) {
        this.closeid = closeid == null ? null : closeid.trim();
    }

    /**
     * 获取父id
     *
     * @return pid - 父id
     */
    public String getPid() {
        return pid;
    }

    /**
     * 设置父id
     *
     * @param pid 父id
     */
    public void setPid(String pid) {
        this.pid = pid == null ? null : pid.trim();
    }

    /**
     * 获取类型图标地址
     *
     * @return typeicon - 类型图标地址
     */
    public String getTypeicon() {
        return typeicon;
    }

    /**
     * 设置类型图标地址
     *
     * @param typeicon 类型图标地址
     */
    public void setTypeicon(String typeicon) {
        this.typeicon = typeicon == null ? null : typeicon.trim();
    }
}