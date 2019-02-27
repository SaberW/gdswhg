package com.creatoo.hn.dao.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_personnel_awards")
public class WhgPersonnelAwards {
    @Id
    private String id;

    /**
     * 获奖日期
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date issuedate;

    /**
     * 奖项名称
     */
    private String title;

    /**
     * 实体ID
     */
    private String entid;

    /**
     * 创建日期
     */
    private Date crtdate;

    /**
     * 创建人
     */
    private String crtuser;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取获奖日期
     *
     * @return issuedate - 获奖日期
     */
    public Date getIssuedate() {
        return issuedate;
    }

    /**
     * 设置获奖日期
     *
     * @param issuedate 获奖日期
     */
    public void setIssuedate(Date issuedate) {
        this.issuedate = issuedate;
    }

    /**
     * 获取奖项名称
     *
     * @return title - 奖项名称
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置奖项名称
     *
     * @param title 奖项名称
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取创建日期
     *
     * @return crtdate - 创建日期
     */
    public Date getCrtdate() {
        return crtdate;
    }

    /**
     * 设置创建日期
     *
     * @param crtdate 创建日期
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
}