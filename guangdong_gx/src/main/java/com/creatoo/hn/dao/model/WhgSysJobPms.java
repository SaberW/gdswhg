package com.creatoo.hn.dao.model;

import javax.persistence.*;

@Table(name = "whg_sys_job_pms")
public class WhgSysJobPms {
    /**
     * 唯一标识
     */
    @Id
    private String id;

    /**
     * 岗位标识
     */
    private String jobid;

    /**
     * 权限组标识
     */
    private String pmsid;

    /**
     * 获取唯一标识
     *
     * @return id - 唯一标识
     */
    public String getId() {
        return id;
    }

    /**
     * 设置唯一标识
     *
     * @param id 唯一标识
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取岗位标识
     *
     * @return jobid - 岗位标识
     */
    public String getJobid() {
        return jobid;
    }

    /**
     * 设置岗位标识
     *
     * @param jobid 岗位标识
     */
    public void setJobid(String jobid) {
        this.jobid = jobid == null ? null : jobid.trim();
    }

    /**
     * 获取权限组标识
     *
     * @return pmsid - 权限组标识
     */
    public String getPmsid() {
        return pmsid;
    }

    /**
     * 设置权限组标识
     *
     * @param pmsid 权限组标识
     */
    public void setPmsid(String pmsid) {
        this.pmsid = pmsid == null ? null : pmsid.trim();
    }
}