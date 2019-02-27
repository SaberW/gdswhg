package com.creatoo.hn.dao.model;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "whg_live")
public class WhgLive {
    /**
     * 主键PK
     */
    @Id
    private String id;

    /**
     * 直播标题
     */
    private String livetitle;

    /**
     * 领域
     */
    private String domain;

    /**
     * 封面
     */
    private String livecover;

    /**
     * 首页大图
     */
    private String livelbt;

    /**
     * 置为轮播图：1、是，2、否
     */
    private Integer islbt;

    /**
     * 推荐：1、是，2、否
     */
    private Integer isrecommend;

    /**
     * 流程状态：0初始 initial，1送审 checkpending，2已审 checked，3已发 published
     */
    private Integer livestate;

    /**
     * 推流地址
     */
    private String flowaddr;

    /**
     * 创建人
     */
    private String livecreator;

    /**
     * 创建时间
     */
    private Date livecreattime;

    /**
     * 删除状态：1、已删除；2、未删除
     */
    private Integer isdel;

    /**
     * 开始时间
     */
    private Date starttime;

    /**
     * 结束时间
     */
    private Date endtime;

    /**
     * 直播分类
     */
    private String livetype;

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
     * 直播描述
     */
    private String livedesc;

    /**
     * 所属文化馆
     */
    private String cultid;

    /**
     * 获取主键PK
     *
     * @return id - 主键PK
     */
    public String getId() {
        return id;
    }

    /**
     * 设置主键PK
     *
     * @param id 主键PK
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取直播标题
     *
     * @return livetitle - 直播标题
     */
    public String getLivetitle() {
        return livetitle;
    }

    /**
     * 设置直播标题
     *
     * @param livetitle 直播标题
     */
    public void setLivetitle(String livetitle) {
        this.livetitle = livetitle;
    }

    /**
     * 获取领域
     *
     * @return domain - 领域
     */
    public String getDomain() {
        return domain;
    }

    /**
     * 设置领域
     *
     * @param domain 领域
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * 获取封面
     *
     * @return livecover - 封面
     */
    public String getLivecover() {
        return livecover;
    }

    /**
     * 设置封面
     *
     * @param livecover 封面
     */
    public void setLivecover(String livecover) {
        this.livecover = livecover;
    }

    /**
     * 获取首页大图
     *
     * @return livelbt - 首页大图
     */
    public String getLivelbt() {
        return livelbt;
    }

    /**
     * 设置首页大图
     *
     * @param livelbt 首页大图
     */
    public void setLivelbt(String livelbt) {
        this.livelbt = livelbt;
    }

    /**
     * 获取置为轮播图：1、是，2、否
     *
     * @return islbt - 置为轮播图：1、是，2、否
     */
    public Integer getIslbt() {
        return islbt;
    }

    /**
     * 设置置为轮播图：1、是，2、否
     *
     * @param islbt 置为轮播图：1、是，2、否
     */
    public void setIslbt(Integer islbt) {
        this.islbt = islbt;
    }

    /**
     * 获取推荐：1、是，2、否
     *
     * @return isrecommend - 推荐：1、是，2、否
     */
    public Integer getIsrecommend() {
        return isrecommend;
    }

    /**
     * 设置推荐：1、是，2、否
     *
     * @param isrecommend 推荐：1、是，2、否
     */
    public void setIsrecommend(Integer isrecommend) {
        this.isrecommend = isrecommend;
    }

    /**
     * 获取流程状态：0初始 initial，1送审 checkpending，2已审 checked，3已发 published
     *
     * @return livestate - 流程状态：0初始 initial，1送审 checkpending，2已审 checked，3已发 published
     */
    public Integer getLivestate() {
        return livestate;
    }

    /**
     * 设置流程状态：0初始 initial，1送审 checkpending，2已审 checked，3已发 published
     *
     * @param livestate 流程状态：0初始 initial，1送审 checkpending，2已审 checked，3已发 published
     */
    public void setLivestate(Integer livestate) {
        this.livestate = livestate;
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
        this.flowaddr = flowaddr;
    }

    /**
     * 获取创建人
     *
     * @return livecreator - 创建人
     */
    public String getLivecreator() {
        return livecreator;
    }

    /**
     * 设置创建人
     *
     * @param livecreator 创建人
     */
    public void setLivecreator(String livecreator) {
        this.livecreator = livecreator;
    }

    /**
     * 获取创建时间
     *
     * @return livecreattime - 创建时间
     */
    public Date getLivecreattime() {
        return livecreattime;
    }

    /**
     * 设置创建时间
     *
     * @param livecreattime 创建时间
     */
    public void setLivecreattime(Date livecreattime) {
        this.livecreattime = livecreattime;
    }

    /**
     * 获取删除状态：1、已删除；2、未删除
     *
     * @return isdel - 删除状态：1、已删除；2、未删除
     */
    public Integer getIsdel() {
        return isdel;
    }

    /**
     * 设置删除状态：1、已删除；2、未删除
     *
     * @param isdel 删除状态：1、已删除；2、未删除
     */
    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    /**
     * 获取开始时间
     *
     * @return starttime - 开始时间
     */
    public Date getStarttime() {
        return starttime;
    }

    /**
     * 设置开始时间
     *
     * @param starttime 开始时间
     */
    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    /**
     * 获取结束时间
     *
     * @return endtime - 结束时间
     */
    public Date getEndtime() {
        return endtime;
    }

    /**
     * 设置结束时间
     *
     * @param endtime 结束时间
     */
    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    /**
     * 获取直播分类
     *
     * @return livetype - 直播分类
     */
    public String getLivetype() {
        return livetype;
    }

    /**
     * 设置直播分类
     *
     * @param livetype 直播分类
     */
    public void setLivetype(String livetype) {
        this.livetype = livetype;
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
        this.appname = appname;
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
        this.streamname = streamname;
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
        this.playaddr = playaddr;
    }

    /**
     * 获取直播描述
     *
     * @return livedesc - 直播描述
     */
    public String getLivedesc() {
        return livedesc;
    }

    /**
     * 设置直播描述
     *
     * @param livedesc 直播描述
     */
    public void setLivedesc(String livedesc) {
        this.livedesc = livedesc;
    }

    public String getCultid() {
        return cultid;
    }

    public void setCultid(String cultid) {
        this.cultid = cultid;
    }
}