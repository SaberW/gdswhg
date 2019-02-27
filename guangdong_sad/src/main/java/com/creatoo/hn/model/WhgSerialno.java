package com.creatoo.hn.model;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "whg_serialno")
public class WhgSerialno {
    @Id
    private String tname;

    @Id
    private String cdate;

    private Integer curval;

    /**
     * @return tname
     */
    public String getTname() {
        return tname;
    }

    /**
     * @param tname
     */
    public void setTname(String tname) {
        this.tname = tname;
    }

    /**
     * @return cdate
     */
    public String getCdate() {
        return cdate;
    }

    /**
     * @param cdate
     */
    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

    /**
     * @return curval
     */
    public Integer getCurval() {
        return curval;
    }

    /**
     * @param curval
     */
    public void setCurval(Integer curval) {
        this.curval = curval;
    }
}