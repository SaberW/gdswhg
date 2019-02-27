package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_ywi_note")
public class WhgYwiNote {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 管理员ID
     */
    private String adminid;

    /**
     * 管理员账号
     */
    private String adminaccount;

    /**
     * 操作说明
     */
    private String optdesc;

    /**
     * 操作时间
     */
    private Date opttime;

    /**
     * 操作对象
     */
    private Integer opttype;

    /**
     * 请求参数
     */
    private String optargs;

    /**
     * 关联文化馆id
     */
    private String cultid;

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
     * 获取管理员ID
     *
     * @return adminid - 管理员ID
     */
    public String getAdminid() {
        return adminid;
    }

    /**
     * 设置管理员ID
     *
     * @param adminid 管理员ID
     */
    public void setAdminid(String adminid) {
        this.adminid = adminid == null ? null : adminid.trim();
    }

    /**
     * 获取管理员账号
     *
     * @return adminaccount - 管理员账号
     */
    public String getAdminaccount() {
        return adminaccount;
    }

    /**
     * 设置管理员账号
     *
     * @param adminaccount 管理员账号
     */
    public void setAdminaccount(String adminaccount) {
        this.adminaccount = adminaccount == null ? null : adminaccount.trim();
    }

    /**
     * 获取操作说明
     *
     * @return optdesc - 操作说明
     */
    public String getOptdesc() {
        return optdesc;
    }

    /**
     * 设置操作说明
     *
     * @param optdesc 操作说明
     */
    public void setOptdesc(String optdesc) {
        this.optdesc = optdesc == null ? null : optdesc.trim();
    }

    /**
     * 获取操作时间
     *
     * @return opttime - 操作时间
     */
    public Date getOpttime() {
        return opttime;
    }

    /**
     * 设置操作时间
     *
     * @param opttime 操作时间
     */
    public void setOpttime(Date opttime) {
        this.opttime = opttime;
    }

    /**
     * 获取操作对象
     *
     * @return opttype - 操作对象
     */
    public Integer getOpttype() {
        return opttype;
    }

    /**
     * 设置操作对象
     *
     * @param opttype 操作对象
     */
    public void setOpttype(Integer opttype) {
        this.opttype = opttype;
    }

    /**
     * 获取请求参数
     *
     * @return optargs - 请求参数
     */
    public String getOptargs() {
        return optargs;
    }

    /**
     * 设置请求参数
     *
     * @param optargs 请求参数
     */
    public void setOptargs(String optargs) {
        this.optargs = optargs == null ? null : optargs.trim();
    }

    /**
     * 获取关联文化馆id
     *
     * @return cultid - 关联文化馆id
     */
    public String getCultid() {
        return cultid;
    }

    /**
     * 设置关联文化馆id
     *
     * @param cultid 关联文化馆id
     */
    public void setCultid(String cultid) {
        this.cultid = cultid == null ? null : cultid.trim();
    }
}