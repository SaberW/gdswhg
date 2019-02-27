package com.creatoo.hn.dao.model;


import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "whg_mass_view")
public class WhgMassView {
    @Id
    private String id;

    private String resid;

    private String ip;

    private Date createdate;

    public String getId() {
        return id;
    }

    public String getResid() {
        return resid;
    }

    public String getIp() {
        return ip;
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

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }
}
