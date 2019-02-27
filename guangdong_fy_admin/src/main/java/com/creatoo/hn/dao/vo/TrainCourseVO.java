package com.creatoo.hn.dao.vo;

import com.creatoo.hn.dao.model.WhgTraCourse;
import com.creatoo.hn.dao.model.WhgTraEnrol;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 在线直播课程
 * Created by wangxl on 2017/10/16.
 */
@ApiModel(value = "在线课程目录")
public class TrainCourseVO extends WhgTraCourse implements Serializable {

    @ApiModelProperty(value = "课程状态: 0-未开始, 1-正在直播, 2-已结束")
    private Integer listState;

    @ApiModelProperty(value = "课程标题")
    private String title;

    @ApiModelProperty(value = "培训图片")
    private String trainimg;

    @ApiModelProperty(value = "课程时间")
    private String timedesc;

    @ApiModelProperty(value = "学员列表")
    private List<WhgTraEnrol> users;

    @ApiModelProperty(value = "课程列表")
    private List<TrainCourseVO> courses;

    @ApiModelProperty(value = "当前用户能否观看")
    private boolean canSee;

    @ApiModelProperty(value = "直播地址")
    private String liveUrl;

    @ApiModelProperty(value = "回看地址")
    private String backUrl;

    @ApiModelProperty(value = "是否请假")
    private boolean applyLeave;

    @ApiModelProperty(value = "请假开始时间")
    private Date applyLeaveStime;

    @ApiModelProperty(value = "请假结束时间")
    private Date applyLeaveEtime;

    @ApiModelProperty(value = "请假原因")
    private String applyLeaveCause;

    @ApiModelProperty(value = "请假状态")
    private Integer applyLeaveState;

    @ApiModelProperty(value = "请假审核消息")
    private String applyLeaveSuggest;

    public boolean isApplyLeave() {
        return applyLeave;
    }

    public void setApplyLeave(boolean applyLeave) {
        this.applyLeave = applyLeave;
    }

    public Date getApplyLeaveStime() {
        return applyLeaveStime;
    }

    public void setApplyLeaveStime(Date applyLeaveStime) {
        this.applyLeaveStime = applyLeaveStime;
    }

    public Date getApplyLeaveEtime() {
        return applyLeaveEtime;
    }

    public void setApplyLeaveEtime(Date applyLeaveEtime) {
        this.applyLeaveEtime = applyLeaveEtime;
    }

    public String getApplyLeaveCause() {
        return applyLeaveCause;
    }

    public void setApplyLeaveCause(String applyLeaveCause) {
        this.applyLeaveCause = applyLeaveCause;
    }

    public Integer getApplyLeaveState() {
        return applyLeaveState;
    }

    public void setApplyLeaveState(Integer applyLeaveState) {
        this.applyLeaveState = applyLeaveState;
    }

    public String getApplyLeaveSuggest() {
        return applyLeaveSuggest;
    }

    public void setApplyLeaveSuggest(String applyLeaveSuggest) {
        this.applyLeaveSuggest = applyLeaveSuggest;
    }

    public String getTrainimg() {
        return trainimg;
    }

    public void setTrainimg(String trainimg) {
        this.trainimg = trainimg;
    }

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public List<WhgTraEnrol> getUsers() {
        return users;
    }

    public void setUsers(List<WhgTraEnrol> users) {
        this.users = users;
    }

    public List<TrainCourseVO> getCourses() {
        return courses;
    }

    public void setCourses(List<TrainCourseVO> courses) {
        this.courses = courses;
    }

    public boolean isCanSee() {
        return canSee;
    }

    public void setCanSee(boolean canSee) {
        this.canSee = canSee;
    }

    public Integer getListState() {
        return listState;
    }

    public void setListState(Integer listState) {
        this.listState = listState;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimedesc() {
        return timedesc;
    }

    public void setTimedesc(String timedesc) {
        this.timedesc = timedesc;
    }
}
