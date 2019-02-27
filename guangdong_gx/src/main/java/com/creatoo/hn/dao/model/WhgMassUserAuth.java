package com.creatoo.hn.dao.model;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "whg_mass_user_auth")
public class WhgMassUserAuth {

    /**
     * 群文用户ID
     */
    @Id
    private String userid;

    /**
     * 资源库ID
     */
    @Id
    private String masslibraryid;

    /**
     * 权限：view 查看，download 下载
     */
    private String authority;


    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setMasslibraryid(String masslibraryid) {
        this.masslibraryid = masslibraryid;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getUserid() {
        return userid;
    }

    public String getMasslibraryid() {
        return masslibraryid;
    }

    public String getAuthority() {
        return authority;
    }
}