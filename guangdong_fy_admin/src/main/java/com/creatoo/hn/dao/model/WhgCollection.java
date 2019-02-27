package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_collection")
public class WhgCollection {
    /**
     * 收藏id
     */
    @Id
    private String cmid;

    /**
     * 收藏时间
     */
    private Date cmdate;

    /**
     * 用户id
     */
    private String cmuid;

    /**
     * 收藏对象的标题
     */
    private String cmtitle;

    /**
     * 收藏对象的连接地址
     */
    private String cmurl;

    /**
     * 收藏关联类型 2.场馆 3.场馆活动室 4活动 5培训 8.文化遗产9.重点文物,10文化人才11.特色资源12市民风采 23.直播24文化团队
     */
    private String cmreftyp;

    /**
     * 收藏关联类型id
     */
    private String cmrefid;

    /**
     * 操作类型:0收藏,1浏览,2推荐,3置顶
     */
    private String cmopttyp;

    /**
     * 系统类型
     */
    private String systype;

    /**
     * 所属文化馆标识
     */
    private String cmvenueid;

    /**
     * 获取收藏id
     *
     * @return cmid - 收藏id
     */
    public String getCmid() {
        return cmid;
    }

    /**
     * 设置收藏id
     *
     * @param cmid 收藏id
     */
    public void setCmid(String cmid) {
        this.cmid = cmid == null ? null : cmid.trim();
    }

    /**
     * 获取收藏时间
     *
     * @return cmdate - 收藏时间
     */
    public Date getCmdate() {
        return cmdate;
    }

    /**
     * 设置收藏时间
     *
     * @param cmdate 收藏时间
     */
    public void setCmdate(Date cmdate) {
        this.cmdate = cmdate;
    }

    /**
     * 获取用户id
     *
     * @return cmuid - 用户id
     */
    public String getCmuid() {
        return cmuid;
    }

    /**
     * 设置用户id
     *
     * @param cmuid 用户id
     */
    public void setCmuid(String cmuid) {
        this.cmuid = cmuid == null ? null : cmuid.trim();
    }

    /**
     * 获取收藏对象的标题
     *
     * @return cmtitle - 收藏对象的标题
     */
    public String getCmtitle() {
        return cmtitle;
    }

    /**
     * 设置收藏对象的标题
     *
     * @param cmtitle 收藏对象的标题
     */
    public void setCmtitle(String cmtitle) {
        this.cmtitle = cmtitle == null ? null : cmtitle.trim();
    }

    /**
     * 获取收藏对象的连接地址
     *
     * @return cmurl - 收藏对象的连接地址
     */
    public String getCmurl() {
        return cmurl;
    }

    /**
     * 设置收藏对象的连接地址
     *
     * @param cmurl 收藏对象的连接地址
     */
    public void setCmurl(String cmurl) {
        this.cmurl = cmurl == null ? null : cmurl.trim();
    }

    /**
     * 获取收藏关联类型 2.场馆 3.场馆活动室 4活动 5培训 8.文化遗产9.重点文物,10文化人才11.特色资源12市民风采 23.直播24文化团队
     *
     * @return cmreftyp - 收藏关联类型 2.场馆 3.场馆活动室 4活动 5培训 8.文化遗产9.重点文物,10文化人才11.特色资源12市民风采 23.直播24文化团队
     */
    public String getCmreftyp() {
        return cmreftyp;
    }

    /**
     * 设置收藏关联类型 2.场馆 3.场馆活动室 4活动 5培训 8.文化遗产9.重点文物,10文化人才11.特色资源12市民风采 23.直播24文化团队
     *
     * @param cmreftyp 收藏关联类型 2.场馆 3.场馆活动室 4活动 5培训 8.文化遗产9.重点文物,10文化人才11.特色资源12市民风采 23.直播24文化团队
     */
    public void setCmreftyp(String cmreftyp) {
        this.cmreftyp = cmreftyp == null ? null : cmreftyp.trim();
    }

    /**
     * 获取收藏关联类型id
     *
     * @return cmrefid - 收藏关联类型id
     */
    public String getCmrefid() {
        return cmrefid;
    }

    /**
     * 设置收藏关联类型id
     *
     * @param cmrefid 收藏关联类型id
     */
    public void setCmrefid(String cmrefid) {
        this.cmrefid = cmrefid == null ? null : cmrefid.trim();
    }

    /**
     * 获取操作类型:0收藏,1浏览,2推荐,3置顶
     *
     * @return cmopttyp - 操作类型:0收藏,1浏览,2推荐,3置顶
     */
    public String getCmopttyp() {
        return cmopttyp;
    }

    /**
     * 设置操作类型:0收藏,1浏览,2推荐,3置顶
     *
     * @param cmopttyp 操作类型:0收藏,1浏览,2推荐,3置顶
     */
    public void setCmopttyp(String cmopttyp) {
        this.cmopttyp = cmopttyp == null ? null : cmopttyp.trim();
    }

    /**
     * 获取所属文化馆标识
     *
     * @return cmvenueid - 所属文化馆标识
     */
    public String getCmvenueid() {
        return cmvenueid;
    }

    /**
     * 设置所属文化馆标识
     *
     * @param cmvenueid 所属文化馆标识
     */
    public void setCmvenueid(String cmvenueid) {
        this.cmvenueid = cmvenueid == null ? null : cmvenueid.trim();
    }

    public String getSystype() {
        return systype;
    }

    public void setSystype(String systype) {
        this.systype = systype;
    }
}