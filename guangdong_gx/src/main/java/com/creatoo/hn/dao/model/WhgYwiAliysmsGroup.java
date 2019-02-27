package com.creatoo.hn.dao.model;

import javax.persistence.*;

@Table(name = "whg_ywi_aliysms_group")
public class WhgYwiAliysmsGroup {
    @Id
    private String id;

    /**
     * 组业务标记区分用于活动培训场馆等
     */
    private String gptype;

    /**
     * 短信组说明信息
     */
    private String gpdesc;

    /**
     * 是否默认组：1是0否
     */
    private Integer isdefault;

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
     * 获取组业务标记区分用于活动培训场馆等
     *
     * @return gptype - 组业务标记区分用于活动培训场馆等
     */
    public String getGptype() {
        return gptype;
    }

    /**
     * 设置组业务标记区分用于活动培训场馆等
     *
     * @param gptype 组业务标记区分用于活动培训场馆等
     */
    public void setGptype(String gptype) {
        this.gptype = gptype == null ? null : gptype.trim();
    }

    /**
     * 获取短信组说明信息
     *
     * @return gpdesc - 短信组说明信息
     */
    public String getGpdesc() {
        return gpdesc;
    }

    /**
     * 设置短信组说明信息
     *
     * @param gpdesc 短信组说明信息
     */
    public void setGpdesc(String gpdesc) {
        this.gpdesc = gpdesc == null ? null : gpdesc.trim();
    }

    /**
     * 获取是否默认组：1是0否
     *
     * @return isdefault - 是否默认组：1是0否
     */
    public Integer getIsdefault() {
        return isdefault;
    }

    /**
     * 设置是否默认组：1是0否
     *
     * @param isdefault 是否默认组：1是0否
     */
    public void setIsdefault(Integer isdefault) {
        this.isdefault = isdefault;
    }
}