package com.creatoo.hn.dao.model;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "whg_mass_down")
public class WhgMassDown {
    @Id
    private String id;

    private String resid;

    private String ip;

    private Date datetime;

    public String getId() {
        return id;
    }

    public String getResid() {
        return resid;
    }

    public String getIp() {
        return ip;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}
