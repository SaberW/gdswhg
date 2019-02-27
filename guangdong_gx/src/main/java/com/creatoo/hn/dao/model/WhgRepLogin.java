package com.creatoo.hn.dao.model;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "whg_rep_login")
public class WhgRepLogin {
    /**
     * ID
     */
    @Id
    private String id;

    /**
     * 用户ID
     */
    private String userid;

    /**
     * 设备类型（0、pc 1、微信）
     */
    private Integer devtype;

    /**
     * 登录时间
     */
    private Date logintime;

    /**
     * 获取ID
     *
     * @return id - ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(String id) {
        this.id = id;
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
        this.userid = userid;
    }

    /**
     * 获取设备类型（0、pc 1、微信）
     *
     * @return devtype - 设备类型（0、pc 1、微信）
     */
    public Integer getDevtype() {
        return devtype;
    }

    /**
     * 设置设备类型（0、pc 1、微信）
     *
     * @param devtype 设备类型（0、pc 1、微信）
     */
    public void setDevtype(Integer devtype) {
        this.devtype = devtype;
    }

    /**
     * 获取登录时间
     *
     * @return logintime - 登录时间
     */
    public Date getLogintime() {
        return logintime;
    }

    /**
     * 设置登录时间
     *
     * @param logintime 登录时间
     */
    public void setLogintime(Date logintime) {
        this.logintime = logintime;
    }
}