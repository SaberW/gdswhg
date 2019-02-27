package com.creatoo.hn.dao.model;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "whg_ywi_quota")
public class WhgYwiQuota {
    /**
     * 关键字ID
     */
    @Id
    private String id;

    /**
     * 配额大小
     */
    private String size;

    /**
     * 已使用大小
     */
    private String usedsize;

    private Float warningpercent;

    /**
     * 变更时间
     */
    private Date mdfdate;

    /**
     * 变更用户ID
     */
    private String mdfuser;

    /**
     * 关联文化馆id
     */
    private String cultid;

    private String noticephone;

    private String noticeemail;

    //是否已发送通知 1：已发送 2：未发送
    private Boolean issendnotice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUsedsize() {
        return usedsize;
    }

    public void setUsedsize(String usedsize) {
        this.usedsize = usedsize;
    }

    public Date getMdfdate() {
        return mdfdate;
    }

    public void setMdfdate(Date mdfdate) {
        this.mdfdate = mdfdate;
    }

    public String getMdfuser() {
        return mdfuser;
    }

    public void setMdfuser(String mdfuser) {
        this.mdfuser = mdfuser;
    }

    public String getCultid() {
        return cultid;
    }

    public void setCultid(String cultid) {
        this.cultid = cultid;
    }

    public Float getWarningpercent() {
        return warningpercent;
    }

    public void setWarningpercent(Float warningpercent) {
        this.warningpercent = warningpercent;
    }

    public String getNoticephone() {
        return noticephone;
    }

    public void setNoticephone(String noticephone) {
        this.noticephone = noticephone;
    }

    public String getNoticeemail() {
        return noticeemail;
    }

    public void setNoticeemail(String noticeemail) {
        this.noticeemail = noticeemail;
    }

    public Boolean getIssendnotice() {
        return issendnotice;
    }

    public void setIssendnotice(Boolean issendnotice) {
        this.issendnotice = issendnotice;
    }
}