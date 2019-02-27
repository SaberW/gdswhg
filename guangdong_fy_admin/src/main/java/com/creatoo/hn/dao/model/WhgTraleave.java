package com.creatoo.hn.dao.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_traleave")
public class WhgTraleave {
    /**
     * ID
     */
    @Id
    private String id;

    /**
     * 培训ID
     */
    private String traid;

    /**
     * 课程ID
     */
    private String courseid;

    /**
     * 培训名称
     */
    private String tratitle;

    /**
     * 课程开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date starttime;

    /**
     * 课程结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endtime;

    /**
     * 请假原因
     */
    private String cause;

    /**
     * 是否同意（0、否 1、是）
     */
    private Integer state;

    /**
     * 审核意见
     */
    private String suggest;

    /**
     * 申请人
     */
    private String proposer;

    /**
     * 用户ID
     */
    private String userid;

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
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取培训ID
     *
     * @return traid - 培训ID
     */
    public String getTraid() {
        return traid;
    }

    /**
     * 设置培训ID
     *
     * @param traid 培训ID
     */
    public void setTraid(String traid) {
        this.traid = traid == null ? null : traid.trim();
    }

    /**
     * 获取课程ID
     *
     * @return courseid - 课程ID
     */
    public String getCourseid() {
        return courseid;
    }

    /**
     * 设置课程ID
     *
     * @param courseid 课程ID
     */
    public void setCourseid(String courseid) {
        this.courseid = courseid == null ? null : courseid.trim();
    }

    /**
     * 获取培训名称
     *
     * @return tratitle - 培训名称
     */
    public String getTratitle() {
        return tratitle;
    }

    /**
     * 设置培训名称
     *
     * @param tratitle 培训名称
     */
    public void setTratitle(String tratitle) {
        this.tratitle = tratitle == null ? null : tratitle.trim();
    }

    /**
     * 获取课程开始时间
     *
     * @return starttime - 课程开始时间
     */
    public Date getStarttime() {
        return starttime;
    }

    /**
     * 设置课程开始时间
     *
     * @param starttime 课程开始时间
     */
    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    /**
     * 获取课程结束时间
     *
     * @return endtime - 课程结束时间
     */
    public Date getEndtime() {
        return endtime;
    }

    /**
     * 设置课程结束时间
     *
     * @param endtime 课程结束时间
     */
    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    /**
     * 获取请假原因
     *
     * @return cause - 请假原因
     */
    public String getCause() {
        return cause;
    }

    /**
     * 设置请假原因
     *
     * @param cause 请假原因
     */
    public void setCause(String cause) {
        this.cause = cause == null ? null : cause.trim();
    }

    /**
     * 获取是否同意（0、否 1、是）
     *
     * @return state - 是否同意（0、否 1、是）
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置是否同意（0、否 1、是）
     *
     * @param state 是否同意（0、否 1、是）
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取审核意见
     *
     * @return suggest - 审核意见
     */
    public String getSuggest() {
        return suggest;
    }

    /**
     * 设置审核意见
     *
     * @param suggest 审核意见
     */
    public void setSuggest(String suggest) {
        this.suggest = suggest == null ? null : suggest.trim();
    }

    public String getProposer() {
        return proposer;
    }

    public void setProposer(String proposer) {
        this.proposer = proposer;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}