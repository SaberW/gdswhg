package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_mass_resource")
public class WhgMassResource {
    /**
     * PK
     */
    @Id
    private String id;

    /**
     * 创建时间
     */
    private Date crtdate;

    /**
     * 创建人
     */
    private String crtuser;

    /**
     * 状态：1可编辑，9待审核，2待发布，6已发布，4已下架
     */
    private Integer state;

    /**
     * 状态更时间
     */
    private Date statemdfdate;

    /**
     * 状态变更人
     */
    private String statemdfuser;

    /**
     * 删除状态：0未删除，1已删除
     */
    private Integer delstate;

    /**
     * 所属文化馆
     */
    private String cultid;

    /**
     * 是否推荐：0否1是
     */
    private Integer recommend;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区域
     */
    private String area;

    /**
     * 名称
     */
    private String title;

    /**
     * 作者
     */
    private String author;

    /**
     * 类型：1图片，2视频，3音频，4馆刊，5其它
     */
    private String etype;

    /**
     * 图片/封面
     */
    private String imgurl;

    /**
     * 资源地址/附件
     */
    private String resurl;

    /**
     * 时长
     */
    private String restime;

    /**
     * 年代
     */
    private String years;

    /**
     * 资源简介
     */
    private String summary;

    /**
     * 艺术类型
     */
    private String arttype;

    /**
     * 资源来源
     */
    private String origin;

    /**
     * 获取PK
     *
     * @return id - PK
     */
    public String getId() {
        return id;
    }

    /**
     * 设置PK
     *
     * @param id PK
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取创建时间
     *
     * @return crtdate - 创建时间
     */
    public Date getCrtdate() {
        return crtdate;
    }

    /**
     * 设置创建时间
     *
     * @param crtdate 创建时间
     */
    public void setCrtdate(Date crtdate) {
        this.crtdate = crtdate;
    }

    /**
     * 获取创建人
     *
     * @return crtuser - 创建人
     */
    public String getCrtuser() {
        return crtuser;
    }

    /**
     * 设置创建人
     *
     * @param crtuser 创建人
     */
    public void setCrtuser(String crtuser) {
        this.crtuser = crtuser == null ? null : crtuser.trim();
    }

    /**
     * 获取状态：1可编辑，9待审核，2待发布，6已发布，4已下架
     *
     * @return state - 状态：1可编辑，9待审核，2待发布，6已发布，4已下架
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态：1可编辑，9待审核，2待发布，6已发布，4已下架
     *
     * @param state 状态：1可编辑，9待审核，2待发布，6已发布，4已下架
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取状态更时间
     *
     * @return statemdfdate - 状态更时间
     */
    public Date getStatemdfdate() {
        return statemdfdate;
    }

    /**
     * 设置状态更时间
     *
     * @param statemdfdate 状态更时间
     */
    public void setStatemdfdate(Date statemdfdate) {
        this.statemdfdate = statemdfdate;
    }

    /**
     * 获取状态变更人
     *
     * @return statemdfuser - 状态变更人
     */
    public String getStatemdfuser() {
        return statemdfuser;
    }

    /**
     * 设置状态变更人
     *
     * @param statemdfuser 状态变更人
     */
    public void setStatemdfuser(String statemdfuser) {
        this.statemdfuser = statemdfuser == null ? null : statemdfuser.trim();
    }

    /**
     * 获取删除状态：0未删除，1已删除
     *
     * @return delstate - 删除状态：0未删除，1已删除
     */
    public Integer getDelstate() {
        return delstate;
    }

    /**
     * 设置删除状态：0未删除，1已删除
     *
     * @param delstate 删除状态：0未删除，1已删除
     */
    public void setDelstate(Integer delstate) {
        this.delstate = delstate;
    }

    /**
     * 获取所属文化馆
     *
     * @return cultid - 所属文化馆
     */
    public String getCultid() {
        return cultid;
    }

    /**
     * 设置所属文化馆
     *
     * @param cultid 所属文化馆
     */
    public void setCultid(String cultid) {
        this.cultid = cultid == null ? null : cultid.trim();
    }

    /**
     * 获取是否推荐：0否1是
     *
     * @return recommend - 是否推荐：0否1是
     */
    public Integer getRecommend() {
        return recommend;
    }

    /**
     * 设置是否推荐：0否1是
     *
     * @param recommend 是否推荐：0否1是
     */
    public void setRecommend(Integer recommend) {
        this.recommend = recommend;
    }

    /**
     * 获取省
     *
     * @return province - 省
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置省
     *
     * @param province 省
     */
    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    /**
     * 获取市
     *
     * @return city - 市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置市
     *
     * @param city 市
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    /**
     * 获取区域
     *
     * @return area - 区域
     */
    public String getArea() {
        return area;
    }

    /**
     * 设置区域
     *
     * @param area 区域
     */
    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    /**
     * 获取名称
     *
     * @return title - 名称
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置名称
     *
     * @param title 名称
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取作者
     *
     * @return author - 作者
     */
    public String getAuthor() {
        return author;
    }

    /**
     * 设置作者
     *
     * @param author 作者
     */
    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    /**
     * 获取类型：1图片，2视频，3音频，4馆刊，5其它
     *
     * @return etype - 类型：1图片，2视频，3音频，4馆刊，5其它
     */
    public String getEtype() {
        return etype;
    }

    /**
     * 设置类型：1图片，2视频，3音频，4馆刊，5其它
     *
     * @param etype 类型：1图片，2视频，3音频，4馆刊，5其它
     */
    public void setEtype(String etype) {
        this.etype = etype == null ? null : etype.trim();
    }

    /**
     * 获取图片/封面
     *
     * @return imgurl - 图片/封面
     */
    public String getImgurl() {
        return imgurl;
    }

    /**
     * 设置图片/封面
     *
     * @param imgurl 图片/封面
     */
    public void setImgurl(String imgurl) {
        this.imgurl = imgurl == null ? null : imgurl.trim();
    }

    /**
     * 获取资源地址/附件
     *
     * @return resurl - 资源地址/附件
     */
    public String getResurl() {
        return resurl;
    }

    /**
     * 设置资源地址/附件
     *
     * @param resurl 资源地址/附件
     */
    public void setResurl(String resurl) {
        this.resurl = resurl == null ? null : resurl.trim();
    }

    /**
     * 获取时长
     *
     * @return restime - 时长
     */
    public String getRestime() {
        return restime;
    }

    /**
     * 设置时长
     *
     * @param restime 时长
     */
    public void setRestime(String restime) {
        this.restime = restime == null ? null : restime.trim();
    }

    /**
     * 获取年代
     *
     * @return years - 年代
     */
    public String getYears() {
        return years;
    }

    /**
     * 设置年代
     *
     * @param years 年代
     */
    public void setYears(String years) {
        this.years = years == null ? null : years.trim();
    }

    /**
     * 获取资源简介
     *
     * @return summary - 资源简介
     */
    public String getSummary() {
        return summary;
    }

    /**
     * 设置资源简介
     *
     * @param summary 资源简介
     */
    public void setSummary(String summary) {
        this.summary = summary == null ? null : summary.trim();
    }

    /**
     * 获取艺术类型
     *
     * @return arttype - 艺术类型
     */
    public String getArttype() {
        return arttype;
    }

    /**
     * 设置艺术类型
     *
     * @param arttype 艺术类型
     */
    public void setArttype(String arttype) {
        this.arttype = arttype == null ? null : arttype.trim();
    }

    /**
     * 获取资源来源
     *
     * @return origin - 资源来源
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * 设置资源来源
     *
     * @param origin 资源来源
     */
    public void setOrigin(String origin) {
        this.origin = origin == null ? null : origin.trim();
    }
}