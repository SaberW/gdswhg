package com.creatoo.hn.dao.model;

import javax.persistence.*;

@Table(name = "whg_tra_live")
public class WhgTraLive {
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
     * AppName应用名，用于组成推流和播放地址
     */
    private String appname;

    /**
     * StreamName流名，用于组成推流和播放地址
     */
    private String streamname;

    /**
     * 播放地址模板
     */
    private String playaddr;

    /**
     * 推流地址
     */
    private String flowaddr;

    /**
     * 回顾上传视频
     */
    private String enturl;

    /**
     * 直播回看地址
     */
    private String liveaddr;

    public String getLiveaddr() {
        return liveaddr;
    }

    public void setLiveaddr(String liveaddr) {
        this.liveaddr = liveaddr;
    }

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
     * 获取AppName应用名，用于组成推流和播放地址
     *
     * @return appname - AppName应用名，用于组成推流和播放地址
     */
    public String getAppname() {
        return appname;
    }

    /**
     * 设置AppName应用名，用于组成推流和播放地址
     *
     * @param appname AppName应用名，用于组成推流和播放地址
     */
    public void setAppname(String appname) {
        this.appname = appname == null ? null : appname.trim();
    }

    /**
     * 获取StreamName流名，用于组成推流和播放地址
     *
     * @return streamname - StreamName流名，用于组成推流和播放地址
     */
    public String getStreamname() {
        return streamname;
    }

    /**
     * 设置StreamName流名，用于组成推流和播放地址
     *
     * @param streamname StreamName流名，用于组成推流和播放地址
     */
    public void setStreamname(String streamname) {
        this.streamname = streamname == null ? null : streamname.trim();
    }

    /**
     * 获取播放地址模板
     *
     * @return playaddr - 播放地址模板
     */
    public String getPlayaddr() {
        return playaddr;
    }

    /**
     * 设置播放地址模板
     *
     * @param playaddr 播放地址模板
     */
    public void setPlayaddr(String playaddr) {
        this.playaddr = playaddr == null ? null : playaddr.trim();
    }

    /**
     * 获取推流地址
     *
     * @return flowaddr - 推流地址
     */
    public String getFlowaddr() {
        return flowaddr;
    }

    /**
     * 设置推流地址
     *
     * @param flowaddr 推流地址
     */
    public void setFlowaddr(String flowaddr) {
        this.flowaddr = flowaddr == null ? null : flowaddr.trim();
    }

    public String getEnturl() {
        return enturl;
    }

    public void setEnturl(String enturl) {
        this.enturl = enturl;
    }
}