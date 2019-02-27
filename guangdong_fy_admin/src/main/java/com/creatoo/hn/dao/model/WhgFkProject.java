package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_fk_project")
public class WhgFkProject {
    @Id
    private String id;

    /**
     * 关联id
     */
    private String fkid;

    /**
     * 系统类型: 内部供需 NBGX 外部供需 WBGX
     */
    private String protype;

    /**
     * 状态：1 正常 2 删除 3 失效
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 供需范围-市
     */
    private String pscity;

    /**
     * 供需范围-省
     */
    private String psprovince;

    public String getPscity() {
        return pscity;
    }

    public void setPscity(String pscity) {
        this.pscity = pscity;
    }

    public String getPsprovince() {
        return psprovince;
    }

    public void setPsprovince(String psprovince) {
        this.psprovince = psprovince;
    }

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
     * 获取关联id
     *
     * @return fkid - 关联id
     */
    public String getFkid() {
        return fkid;
    }

    /**
     * 设置关联id
     *
     * @param fkid 关联id
     */
    public void setFkid(String fkid) {
        this.fkid = fkid == null ? null : fkid.trim();
    }

    /**
     * 获取系统类型: 内部供需 NBGX 外部供需 WBGX
     *
     * @return protype - 系统类型: 内部供需 NBGX 外部供需 WBGX
     */
    public String getProtype() {
        return protype;
    }

    /**
     * 设置系统类型: 内部供需 NBGX 外部供需 WBGX
     *
     * @param protype 系统类型: 内部供需 NBGX 外部供需 WBGX
     */
    public void setProtype(String protype) {
        this.protype = protype == null ? null : protype.trim();
    }

    /**
     * 获取状态：1 正常 2 删除 3 失效
     *
     * @return state - 状态：1 正常 2 删除 3 失效
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态：1 正常 2 删除 3 失效
     *
     * @param state 状态：1 正常 2 删除 3 失效
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取创建时间
     *
     * @return createtime - 创建时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 设置创建时间
     *
     * @param createtime 创建时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}