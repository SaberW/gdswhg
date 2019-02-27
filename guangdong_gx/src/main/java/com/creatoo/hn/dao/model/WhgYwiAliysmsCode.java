package com.creatoo.hn.dao.model;

import javax.persistence.*;

@Table(name = "whg_ywi_aliysms_code")
public class WhgYwiAliysmsCode {
    @Id
    private String id;

    /**
     * 模板code
     */
    private String tpcode;

    /**
     * 模板名称
     */
    private String tpname;

    /**
     * 模板内容
     */
    private String tpcontent;

    /**
     * 模板说明
     */
    private String tpdesc;

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
     * 获取模板code
     *
     * @return tpcode - 模板code
     */
    public String getTpcode() {
        return tpcode;
    }

    /**
     * 设置模板code
     *
     * @param tpcode 模板code
     */
    public void setTpcode(String tpcode) {
        this.tpcode = tpcode == null ? null : tpcode.trim();
    }

    /**
     * 获取模板名称
     *
     * @return tpname - 模板名称
     */
    public String getTpname() {
        return tpname;
    }

    /**
     * 设置模板名称
     *
     * @param tpname 模板名称
     */
    public void setTpname(String tpname) {
        this.tpname = tpname == null ? null : tpname.trim();
    }

    /**
     * 获取模板内容
     *
     * @return tpcontent - 模板内容
     */
    public String getTpcontent() {
        return tpcontent;
    }

    /**
     * 设置模板内容
     *
     * @param tpcontent 模板内容
     */
    public void setTpcontent(String tpcontent) {
        this.tpcontent = tpcontent == null ? null : tpcontent.trim();
    }

    /**
     * 获取模板说明
     *
     * @return tpdesc - 模板说明
     */
    public String getTpdesc() {
        return tpdesc;
    }

    /**
     * 设置模板说明
     *
     * @param tpdesc 模板说明
     */
    public void setTpdesc(String tpdesc) {
        this.tpdesc = tpdesc == null ? null : tpdesc.trim();
    }
}