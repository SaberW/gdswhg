package com.creatoo.hn.dao.model;

import javax.persistence.*;

@Table(name = "whg_user_product")
public class WhgUserProduct {
    /**
     * PK
     */
    @Id
    private String id;

    /**
     * 用户ID
     */
    private String userid;

    /**
     * 平台产品标记
     */
    private String product;

    /**
     * 获取PK
     *
     * @return id - PK
     */
    public String getId() {
        return id;
    }

    /**
     * 设置PK
     *
     * @param id PK
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取用户ID
     *
     * @return userid - 用户ID
     */
    public String getUserid() {
        return userid;
    }

    /**
     * 设置用户ID
     *
     * @param userid 用户ID
     */
    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    /**
     * 获取平台产品标记
     *
     * @return product - 平台产品标记
     */
    public String getProduct() {
        return product;
    }

    /**
     * 设置平台产品标记
     *
     * @param product 平台产品标记
     */
    public void setProduct(String product) {
        this.product = product == null ? null : product.trim();
    }
}