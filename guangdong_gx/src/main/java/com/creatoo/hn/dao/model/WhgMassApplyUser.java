package com.creatoo.hn.dao.model;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Table(name = "whg_mass_apply_user")
public class WhgMassApplyUser {

    @Id
    private String id;

    /**
     * 群文用户ID
     */
    private String userid;

    /**
     * 资源库ID
     */
    private String masslibraryid;

    /**
     * 申请权限：view 查看，download 下载
     */
    private String applytype;

    private String applystate;

    @Transient
    private String cultid;

    private Date applytime;

    private String statemdfuser;

    private Date statemdfdate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMasslibraryid() {
        return masslibraryid;
    }

    public void setMasslibraryid(String masslibraryid) {
        this.masslibraryid = masslibraryid;
    }

    public String getApplytype() {
        return applytype;
    }

    public void setApplytype(String applytype) {
        this.applytype = applytype;
    }

    public String getApplystate() {
        return applystate;
    }

    public void setApplystate(String applystate) {
        this.applystate = applystate;
    }

    public String getCultid() {
        return cultid;
    }

    public void setCultid(String cultid) {
        this.cultid = cultid;
    }

    public Date getApplytime() {
        return applytime;
    }

    public void setApplytime(Date applytime) {
        this.applytime = applytime;
    }

    public String getStatemdfuser() {
        return statemdfuser;
    }

    public void setStatemdfuser(String statemdfuser) {
        this.statemdfuser = statemdfuser;
    }

    public Date getStatemdfdate() {
        return statemdfdate;
    }

    public void setStatemdfdate(Date statemdfdate) {
        this.statemdfdate = statemdfdate;
    }
}