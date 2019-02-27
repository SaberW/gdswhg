package com.creatoo.hn.dao.model;

import javax.persistence.*;

@Table(name = "whg_ywi_aliysms_refusegrop")
public class WhgYwiAliysmsRefusegrop {
    @Id
    private String id;

    /**
     * 业务id关联活动培训等
     */
    private String entid;

    /**
     * 业务类型
     */
    private String enttype;

    /**
     * 短信组id
     */
    private String groupid;

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
     * 获取业务id关联活动培训等
     *
     * @return entid - 业务id关联活动培训等
     */
    public String getEntid() {
        return entid;
    }

    /**
     * 设置业务id关联活动培训等
     *
     * @param entid 业务id关联活动培训等
     */
    public void setEntid(String entid) {
        this.entid = entid == null ? null : entid.trim();
    }

    /**
     * 获取业务类型
     *
     * @return enttype - 业务类型
     */
    public String getEnttype() {
        return enttype;
    }

    /**
     * 设置业务类型
     *
     * @param enttype 业务类型
     */
    public void setEnttype(String enttype) {
        this.enttype = enttype == null ? null : enttype.trim();
    }

    /**
     * 获取短信组id
     *
     * @return groupid - 短信组id
     */
    public String getGroupid() {
        return groupid;
    }

    /**
     * 设置短信组id
     *
     * @param groupid 短信组id
     */
    public void setGroupid(String groupid) {
        this.groupid = groupid == null ? null : groupid.trim();
    }
}