package com.creatoo.hn.dao.vo;

import com.creatoo.hn.dao.model.WhgResource;
import com.creatoo.hn.dao.model.WhgTra;
import com.creatoo.hn.dao.model.WhgTraEnrol;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 在线课程对象
 * Created by wangxl on 2017/10/16.
 */
@ApiModel("在线课程对象")
public class TrainLiveVO extends WhgTra implements Serializable {
    @ApiModelProperty(value = "列表状态：0-未开始(直播预告), 1-正在直播，2-已结束(精彩回顾)")
    private Integer listState;

    @ApiModelProperty(value = "当前的报名人数")
    private Integer curtPeoples;

    @ApiModelProperty(value = "剩下的报名人数")
    private Integer restPeoples;

    @ApiModelProperty(value = "剩下的直播时间")
    private Integer restTimes;

    @ApiModelProperty(value = "剩下的直播时间单位")
    private String restTimesUnit;

    @ApiModelProperty(value = "限制年龄说明")
    private String ageUnit;

    @ApiModelProperty(value = "标签描述列表")
    private List<String> tags;

    @ApiModelProperty(value = "课程目录列表")
    private List<TrainCourseVO> courses;

    @ApiModelProperty(value = "报名状态: 0-未开始，1-可报名, 2-已报满, 3-已结束, 4-不满足条件， 5-已报名")
    private Integer enrolState;

    @ApiModelProperty(value = "收藏次数")
    private Integer favoriteTimes;

    @ApiModelProperty(value = "点赞次数")
    private Integer likedTimes;

    @ApiModelProperty(value = "当前用户是否已经收藏")
    private boolean isFavorite;

    @ApiModelProperty(value = "当前用户是否已经点赞")
    private boolean isLiked;

    @ApiModelProperty(value = "当前用户能否观看直播")
    private boolean canSee;

    @ApiModelProperty(value = "学员列表")
    private List<WhgTraEnrol> users;

    @ApiModelProperty(value = "资料列表")
    private List<WhgResource> docs;

    public boolean isCanSee() {
        return canSee;
    }

    public void setCanSee(boolean canSee) {
        this.canSee = canSee;
    }

    public Integer getEnrolState() {
        return enrolState;
    }

    public void setEnrolState(Integer enrolState) {
        this.enrolState = enrolState;
    }

    public String getAgeUnit() {
        return ageUnit;
    }

    public void setAgeUnit(String ageUnit) {
        this.ageUnit = ageUnit;
    }

    public Integer getCurtPeoples() {
        return curtPeoples;
    }

    public void setCurtPeoples(Integer curtPeoples) {
        this.curtPeoples = curtPeoples;
    }

    public Integer getListState() {
        return listState;
    }

    public void setListState(Integer listState) {
        this.listState = listState;
    }

    public Integer getRestPeoples() {
        return restPeoples;
    }

    public void setRestPeoples(Integer restPeoples) {
        this.restPeoples = restPeoples;
    }

    public Integer getRestTimes() {
        return restTimes;
    }

    public void setRestTimes(Integer restTimes) {
        this.restTimes = restTimes;
    }

    public String getRestTimesUnit() {
        return restTimesUnit;
    }

    public void setRestTimesUnit(String restTimesUnit) {
        this.restTimesUnit = restTimesUnit;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<TrainCourseVO> getCourses() {
        return courses;
    }

    public void setCourses(List<TrainCourseVO> courses) {
        this.courses = courses;
    }

    public Integer getFavoriteTimes() {
        return favoriteTimes;
    }

    public void setFavoriteTimes(Integer favoriteTimes) {
        this.favoriteTimes = favoriteTimes;
    }

    public Integer getLikedTimes() {
        return likedTimes;
    }

    public void setLikedTimes(Integer likedTimes) {
        this.likedTimes = likedTimes;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public List<WhgTraEnrol> getUsers() {
        return users;
    }

    public void setUsers(List<WhgTraEnrol> users) {
        this.users = users;
    }

    public List<WhgResource> getDocs() {
        return docs;
    }

    public void setDocs(List<WhgResource> docs) {
        this.docs = docs;
    }
}
