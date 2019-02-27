package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_show_playbill")
public class WhgShowPlaybill {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 名称
     */
    private String title;

    /**
     * 类型
     */
    private String etype;

    /**
     * 实体ID
     */
    private String entid;

    /**
     * 参演人员
     */
    private String person;

    /**
     * 时长（分钟）
     */
    private Integer time;

    /**
     * 创建时间
     */
    private Date crtdate;

    /**
     * 文化馆id
     */
    private String cultid;

    /**
     * 创建人
     */
    private String crtuser;

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
     * 获取名称
     *
     * @return title - 名称
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置名称
     *
     * @param title 名称
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取类型
     *
     * @return etype - 类型
     */
    public String getEtype() {
        return etype;
    }

    /**
     * 设置类型
     *
     * @param etype 类型
     */
    public void setEtype(String etype) {
        this.etype = etype == null ? null : etype.trim();
    }

    /**
     * 获取参演人员
     *
     * @return person - 参演人员
     */
    public String getPerson() {
        return person;
    }

    /**
     * 设置参演人员
     *
     * @param person 参演人员
     */
    public void setPerson(String person) {
        this.person = person == null ? null : person.trim();
    }

    /**
     * 获取时长（分钟）
     *
     * @return time - 时长（分钟）
     */
    public Integer getTime() {
        return time;
    }

    /**
     * 设置时长（分钟）
     *
     * @param time 时长（分钟）
     */
    public void setTime(Integer time) {
        this.time = time;
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

    public String getEntid() {
        return entid;
    }

    public void setEntid(String entid) {
        this.entid = entid;
    }

    public String getCultid() {
        return cultid;
    }

    public void setCultid(String cultid) {
        this.cultid = cultid;
    }
}