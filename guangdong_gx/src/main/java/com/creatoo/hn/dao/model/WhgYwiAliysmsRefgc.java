package com.creatoo.hn.dao.model;

import javax.persistence.*;

@Table(name = "whg_ywi_aliysms_refgc")
public class WhgYwiAliysmsRefgc {
    @Id
    private String id;

    /**
     * 模板组ID
     */
    private String groupid;

    /**
     * 短信模板ID
     */
    private String codeid;

    /**
     * 调用切入点标记用于区分报名审核面试等调用点
     */
    private String actpoint;

    /**
     * 调用切入点说明
     */
    private String actdesc;

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
     * 获取模板组ID
     *
     * @return groupid - 模板组ID
     */
    public String getGroupid() {
        return groupid;
    }

    /**
     * 设置模板组ID
     *
     * @param groupid 模板组ID
     */
    public void setGroupid(String groupid) {
        this.groupid = groupid == null ? null : groupid.trim();
    }

    /**
     * 获取短信模板ID
     *
     * @return codeid - 短信模板ID
     */
    public String getCodeid() {
        return codeid;
    }

    /**
     * 设置短信模板ID
     *
     * @param codeid 短信模板ID
     */
    public void setCodeid(String codeid) {
        this.codeid = codeid == null ? null : codeid.trim();
    }

    /**
     * 获取调用切入点标记用于区分报名审核面试等调用点
     *
     * @return actpoint - 调用切入点标记用于区分报名审核面试等调用点
     */
    public String getActpoint() {
        return actpoint;
    }

    /**
     * 设置调用切入点标记用于区分报名审核面试等调用点
     *
     * @param actpoint 调用切入点标记用于区分报名审核面试等调用点
     */
    public void setActpoint(String actpoint) {
        this.actpoint = actpoint == null ? null : actpoint.trim();
    }

    /**
     * 获取调用切入点说明
     *
     * @return actdesc - 调用切入点说明
     */
    public String getActdesc() {
        return actdesc;
    }

    /**
     * 设置调用切入点说明
     *
     * @param actdesc 调用切入点说明
     */
    public void setActdesc(String actdesc) {
        this.actdesc = actdesc == null ? null : actdesc.trim();
    }
}