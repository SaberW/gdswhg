package com.creatoo.hn.dao.model;

import javax.persistence.*;

@Table(name = "whg_sys_ids")
public class WhgSysIds {
    /**
     * ID生成的日期
     */
    @Id
    private String iddate;

    /**
     * ID的值
     */
    @Id
    private String idval;

    /**
     * 获取ID生成的日期
     *
     * @return iddate - ID生成的日期
     */
    public String getIddate() {
        return iddate;
    }

    /**
     * 设置ID生成的日期
     *
     * @param iddate ID生成的日期
     */
    public void setIddate(String iddate) {
        this.iddate = iddate == null ? null : iddate.trim();
    }

    /**
     * 获取ID的值
     *
     * @return idval - ID的值
     */
    public String getIdval() {
        return idval;
    }

    /**
     * 设置ID的值
     *
     * @param idval ID的值
     */
    public void setIdval(String idval) {
        this.idval = idval == null ? null : idval.trim();
    }
}