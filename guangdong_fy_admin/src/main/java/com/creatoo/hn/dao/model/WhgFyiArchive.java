package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_fyi_archive")
public class WhgFyiArchive {
    @Id
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 图片
     */
    private String image;

    /**
     * 类型
     */
    private String etype;

    /**
     * 格式（1、图片 2、视频  3、音频  4、文档）
     */
    private Integer format;

    /**
     * 内容链接
     */
    private String enturl;

    /**
     * 创建时间
     */
    private Date crtdate;

    /**
     * 创建人
     */
    private String crtuser;

    /**
     * 文化馆id
     */
    private String cultid;

    /**
     * 操作时间
     */
    private Date statemdfdate;

    /**
     * 操作用户
     */
    private String statemdfuser;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取类型
     *
     * @return etype - 类型
     */
    public String getEtype() {
        return etype;
    }

    /**
     * 设置类型
     *
     * @param etype 类型
     */
    public void setEtype(String etype) {
        this.etype = etype == null ? null : etype.trim();
    }

    /**
     * 获取格式（1、图片 2、视频  3、音频  4、文档）
     *
     * @return format - 格式（1、图片 2、视频  3、音频  4、文档）
     */
    public Integer getFormat() {
        return format;
    }

    /**
     * 设置格式（1、图片 2、视频  3、音频  4、文档）
     *
     * @param format 格式（1、图片 2、视频  3、音频  4、文档）
     */
    public void setFormat(Integer format) {
        this.format = format;
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
     * 获取操作时间
     *
     * @return statemdfdate - 操作时间
     */
    public Date getStatemdfdate() {
        return statemdfdate;
    }

    /**
     * 设置操作时间
     *
     * @param statemdfdate 操作时间
     */
    public void setStatemdfdate(Date statemdfdate) {
        this.statemdfdate = statemdfdate;
    }

    /**
     * 获取操作用户
     *
     * @return statemdfuser - 操作用户
     */
    public String getStatemdfuser() {
        return statemdfuser;
    }

    /**
     * 设置操作用户
     *
     * @param statemdfuser 操作用户
     */
    public void setStatemdfuser(String statemdfuser) {
        this.statemdfuser = statemdfuser == null ? null : statemdfuser.trim();
    }

    public String getCultid() {
        return cultid;
    }

    public void setCultid(String cultid) {
        this.cultid = cultid;
    }

    public String getEnturl() {
        return enturl;
    }

    public void setEnturl(String enturl) {
        this.enturl = enturl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}