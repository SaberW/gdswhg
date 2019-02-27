package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_ywi_area")
public class WhgYwiArea {
    /**
     * 行政区域唯一标识
     */
    @Id
    private String id;

    /**
     * 父标识
     */
    private String pid;

    /**
     * 名称
     */
    private String name;

    /**
     * 区域编码
     */
    private String code;

    /**
     * 描述
     */
    private String memo;

    /**
     * 排序索引
     */
    private Integer idx;

    /**
     * 创建管理员标识
     */
    private String crtuser;

    /**
     * 创建时间
     */
    private Date crtdate;

    /**
     * 状态.0-无效，1-有效
     */
    private Integer state;

    /**
     * 获取行政区域唯一标识
     *
     * @return id - 行政区域唯一标识
     */
    public String getId() {
        return id;
    }

    /**
     * 设置行政区域唯一标识
     *
     * @param id 行政区域唯一标识
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取父标识
     *
     * @return pid - 父标识
     */
    public String getPid() {
        return pid;
    }

    /**
     * 设置父标识
     *
     * @param pid 父标识
     */
    public void setPid(String pid) {
        this.pid = pid == null ? null : pid.trim();
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
     * 获取区域编码
     *
     * @return code - 区域编码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置区域编码
     *
     * @param code 区域编码
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
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
     * 获取排序索引
     *
     * @return idx - 排序索引
     */
    public Integer getIdx() {
        return idx;
    }

    /**
     * 设置排序索引
     *
     * @param idx 排序索引
     */
    public void setIdx(Integer idx) {
        this.idx = idx;
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
}