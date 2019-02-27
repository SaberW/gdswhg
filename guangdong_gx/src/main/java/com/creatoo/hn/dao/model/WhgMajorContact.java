package com.creatoo.hn.dao.model;

import javax.persistence.*;

@Table(name = "whg_major_contact")
public class WhgMajorContact {
    /**
     * 关联表ID
     */
    @Id
    private String id;

    /**
     * 关联实体类型（1、培训 2、老师 3、资源）
     */
    private Integer type;

    /**
     * 关联实体id
     */
    private String entid;

    /**
     * 关联微专业ID
     */
    private String majorid;

    /**
     * 获取关联表ID
     *
     * @return id - 关联表ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置关联表ID
     *
     * @param id 关联表ID
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取关联实体类型（1、培训 2、老师 3、资源）
     *
     * @return type - 关联实体类型（1、培训 2、老师 3、资源）
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置关联实体类型（1、培训 2、老师 3、资源）
     *
     * @param type 关联实体类型（1、培训 2、老师 3、资源）
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取关联实体id
     *
     * @return entid - 关联实体id
     */
    public String getEntid() {
        return entid;
    }

    /**
     * 设置关联实体id
     *
     * @param entid 关联实体id
     */
    public void setEntid(String entid) {
        this.entid = entid == null ? null : entid.trim();
    }

    public String getMajorid() {
        return majorid;
    }

    public void setMajorid(String majorid) {
        this.majorid = majorid;
    }
}