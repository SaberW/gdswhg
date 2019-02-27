package com.creatoo.hn.dao.model;

import javax.persistence.*;

@Table(name = "whg_connect_yjpz")
public class WhgConnectYjpz {
    /**
     * 主键
     */
    private String id;

    /**
     * 实体ID
     */
    private String entid;

    /**
     * 硬件配置id
     */
    private String yjid;

    /**
     * 实体类型
     */
    private Integer type;

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
     * 获取实体ID
     *
     * @return entid - 实体ID
     */
    public String getEntid() {
        return entid;
    }

    /**
     * 设置实体ID
     *
     * @param entid 实体ID
     */
    public void setEntid(String entid) {
        this.entid = entid == null ? null : entid.trim();
    }

    /**
     * 获取硬件配置id
     *
     * @return yjid - 硬件配置id
     */
    public String getYjid() {
        return yjid;
    }

    /**
     * 设置硬件配置id
     *
     * @param yjid 硬件配置id
     */
    public void setYjid(String yjid) {
        this.yjid = yjid == null ? null : yjid.trim();
    }

    /**
     * 获取实体类型
     *
     * @return type - 实体类型
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置实体类型
     *
     * @param type 实体类型
     */
    public void setType(Integer type) {
        this.type = type;
    }
}