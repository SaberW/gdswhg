package com.creatoo.hn.dao.model;

import javax.persistence.*;

@Table(name = "whg_sys_user_cult_dept")
public class WhgSysUserCultDept {
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
     * 部门ID
     */
    private String deptid;

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
     * 获取部门ID
     *
     * @return deptid - 部门ID
     */
    public String getDeptid() {
        return deptid;
    }

    /**
     * 设置部门ID
     *
     * @param deptid 部门ID
     */
    public void setDeptid(String deptid) {
        this.deptid = deptid == null ? null : deptid.trim();
    }
}