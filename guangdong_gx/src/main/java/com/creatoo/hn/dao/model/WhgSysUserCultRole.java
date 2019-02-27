package com.creatoo.hn.dao.model;

import javax.persistence.*;

@Table(name = "whg_sys_user_cult_role")
public class WhgSysUserCultRole {
    /**
     * 主键ID
     */
    @Id
    private String id;

    /**
     * 外键ID关联whg_sys_user_cult主键ID
     */
    private String refid;

    /**
     * 子馆权限角色信息ID
     */
    private String roleid;

    /**
     * 获取主键ID
     *
     * @return id - 主键ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置主键ID
     *
     * @param id 主键ID
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取外键ID关联whg_sys_user_cult主键ID
     *
     * @return refid - 外键ID关联whg_sys_user_cult主键ID
     */
    public String getRefid() {
        return refid;
    }

    /**
     * 设置外键ID关联whg_sys_user_cult主键ID
     *
     * @param refid 外键ID关联whg_sys_user_cult主键ID
     */
    public void setRefid(String refid) {
        this.refid = refid == null ? null : refid.trim();
    }

    /**
     * 获取子馆权限角色信息ID
     *
     * @return roleid - 子馆权限角色信息ID
     */
    public String getRoleid() {
        return roleid;
    }

    /**
     * 设置子馆权限角色信息ID
     *
     * @param roleid 子馆权限角色信息ID
     */
    public void setRoleid(String roleid) {
        this.roleid = roleid == null ? null : roleid.trim();
    }
}