package com.creatoo.hn.dao.model;

import javax.persistence.*;

@Table(name = "whg_ywi_config")
public class WhgYwiConfig {
    /**
     * 配置项key
     */
    @Id
    private String cfgkey;

    /**
     * 配置分类
     */
    private String cfgtype;

    /**
     * 配置项值
     */
    private String cfgvalue;

    /**
     * 配置项名称
     */
    private String cfgtext;

    /**
     * 获取配置项key
     *
     * @return cfgkey - 配置项key
     */
    public String getCfgkey() {
        return cfgkey;
    }

    /**
     * 设置配置项key
     *
     * @param cfgkey 配置项key
     */
    public void setCfgkey(String cfgkey) {
        this.cfgkey = cfgkey == null ? null : cfgkey.trim();
    }

    /**
     * 获取配置分类
     *
     * @return cfgtype - 配置分类
     */
    public String getCfgtype() {
        return cfgtype;
    }

    /**
     * 设置配置分类
     *
     * @param cfgtype 配置分类
     */
    public void setCfgtype(String cfgtype) {
        this.cfgtype = cfgtype == null ? null : cfgtype.trim();
    }

    /**
     * 获取配置项值
     *
     * @return cfgvalue - 配置项值
     */
    public String getCfgvalue() {
        return cfgvalue;
    }

    /**
     * 设置配置项值
     *
     * @param cfgvalue 配置项值
     */
    public void setCfgvalue(String cfgvalue) {
        this.cfgvalue = cfgvalue == null ? null : cfgvalue.trim();
    }

    /**
     * 获取配置项名称
     *
     * @return cfgtext - 配置项名称
     */
    public String getCfgtext() {
        return cfgtext;
    }

    /**
     * 设置配置项名称
     *
     * @param cfgtext 配置项名称
     */
    public void setCfgtext(String cfgtext) {
        this.cfgtext = cfgtext == null ? null : cfgtext.trim();
    }
}