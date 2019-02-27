package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_resource")
public class WhgResource {
    /**
     * 资源ID
     */
    @Id
    private String id;

    /**
     * 资源类型（1图片/2视频/3音频4/文档）
     */
    private String enttype;

    /**
     * 实体类型（1、培训 2、活动 3、场馆 4、场馆活动室 5、名录、6传承人，7志愿活动，8优秀组织，9项目示范, 10先进个人，11资讯公告，12馆办团队13，文化品牌，14数字展览作品）
     */
    private String reftype;

    /**
     * 实体id(培训/活动的ID)
     */
    private String refid;

    /**
     * 资源的地址
     */
    private String enturl;

    /**
     * 资源的名字
     */
    private String name;

    /**
     * 视频类型相关封面图
     */
    private String deourl;

    /**
     * 视频/音频时长
     */
    private String enttimes;

    /**
     * 资源创建时间
     */
    private Date crtdate;

    /**
     * 资源更新时间
     */
    private Date redate;

    /**
     * 资源库标识
     */
    private String libid;

    /**
     * 资源标识
     */
    private String resid;

    /**
     * 扩展:是否获奖作品
     */
    private Integer extisaward;

    /**
     * 扩展:主讲老师
     */
    private String extemcee;

    /**
     * 简介
     */
    private String summary;

    /**
     * 艺术分类
     */
    private String arttype;

    /**
     * 获取资源ID
     *
     * @return id - 资源ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置资源ID
     *
     * @param id 资源ID
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取资源类型（1图片/2视频/3音频4/文档）
     *
     * @return enttype - 资源类型（1图片/2视频/3音频4/文档）
     */
    public String getEnttype() {
        return enttype;
    }

    /**
     * 设置资源类型（1图片/2视频/3音频4/文档）
     *
     * @param enttype 资源类型（1图片/2视频/3音频4/文档）
     */
    public void setEnttype(String enttype) {
        this.enttype = enttype == null ? null : enttype.trim();
    }

    /**
     * 获取实体类型（1、培训 2、活动 3、场馆 4、场馆活动室 5、名录、6传承人，7志愿活动，8优秀组织，9项目示范, 10先进个人，11资讯公告，12馆办团队13，文化品牌，14数字展览作品）
     *
     * @return reftype - 实体类型（1、培训 2、活动 3、场馆 4、场馆活动室 5、名录、6传承人，7志愿活动，8优秀组织，9项目示范, 10先进个人，11资讯公告，12馆办团队13，文化品牌，14数字展览作品）
     */
    public String getReftype() {
        return reftype;
    }

    /**
     * 设置实体类型（1、培训 2、活动 3、场馆 4、场馆活动室 5、名录、6传承人，7志愿活动，8优秀组织，9项目示范, 10先进个人，11资讯公告，12馆办团队13，文化品牌，14数字展览作品）
     *
     * @param reftype 实体类型（1、培训 2、活动 3、场馆 4、场馆活动室 5、名录、6传承人，7志愿活动，8优秀组织，9项目示范, 10先进个人，11资讯公告，12馆办团队13，文化品牌，14数字展览作品）
     */
    public void setReftype(String reftype) {
        this.reftype = reftype == null ? null : reftype.trim();
    }

    /**
     * 获取实体id(培训/活动的ID)
     *
     * @return refid - 实体id(培训/活动的ID)
     */
    public String getRefid() {
        return refid;
    }

    /**
     * 设置实体id(培训/活动的ID)
     *
     * @param refid 实体id(培训/活动的ID)
     */
    public void setRefid(String refid) {
        this.refid = refid == null ? null : refid.trim();
    }

    /**
     * 获取资源的地址
     *
     * @return enturl - 资源的地址
     */
    public String getEnturl() {
        return enturl;
    }

    /**
     * 设置资源的地址
     *
     * @param enturl 资源的地址
     */
    public void setEnturl(String enturl) {
        this.enturl = enturl == null ? null : enturl.trim();
    }

    /**
     * 获取资源的名字
     *
     * @return name - 资源的名字
     */
    public String getName() {
        return name;
    }

    /**
     * 设置资源的名字
     *
     * @param name 资源的名字
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取视频类型相关封面图
     *
     * @return deourl - 视频类型相关封面图
     */
    public String getDeourl() {
        return deourl;
    }

    /**
     * 设置视频类型相关封面图
     *
     * @param deourl 视频类型相关封面图
     */
    public void setDeourl(String deourl) {
        this.deourl = deourl == null ? null : deourl.trim();
    }

    /**
     * 获取视频/音频时长
     *
     * @return enttimes - 视频/音频时长
     */
    public String getEnttimes() {
        return enttimes;
    }

    /**
     * 设置视频/音频时长
     *
     * @param enttimes 视频/音频时长
     */
    public void setEnttimes(String enttimes) {
        this.enttimes = enttimes == null ? null : enttimes.trim();
    }

    /**
     * 获取资源创建时间
     *
     * @return crtdate - 资源创建时间
     */
    public Date getCrtdate() {
        return crtdate;
    }

    /**
     * 设置资源创建时间
     *
     * @param crtdate 资源创建时间
     */
    public void setCrtdate(Date crtdate) {
        this.crtdate = crtdate;
    }

    /**
     * 获取资源更新时间
     *
     * @return redate - 资源更新时间
     */
    public Date getRedate() {
        return redate;
    }

    /**
     * 设置资源更新时间
     *
     * @param redate 资源更新时间
     */
    public void setRedate(Date redate) {
        this.redate = redate;
    }

    /**
     * 获取资源库标识
     *
     * @return libid - 资源库标识
     */
    public String getLibid() {
        return libid;
    }

    /**
     * 设置资源库标识
     *
     * @param libid 资源库标识
     */
    public void setLibid(String libid) {
        this.libid = libid == null ? null : libid.trim();
    }

    /**
     * 获取资源标识
     *
     * @return resid - 资源标识
     */
    public String getResid() {
        return resid;
    }

    /**
     * 设置资源标识
     *
     * @param resid 资源标识
     */
    public void setResid(String resid) {
        this.resid = resid == null ? null : resid.trim();
    }

    /**
     * 获取扩展:是否获奖作品
     *
     * @return extisaward - 扩展:是否获奖作品
     */
    public Integer getExtisaward() {
        return extisaward;
    }

    /**
     * 设置扩展:是否获奖作品
     *
     * @param extisaward 扩展:是否获奖作品
     */
    public void setExtisaward(Integer extisaward) {
        this.extisaward = extisaward;
    }

    /**
     * 获取扩展:主讲老师
     *
     * @return extemcee - 扩展:主讲老师
     */
    public String getExtemcee() {
        return extemcee;
    }

    /**
     * 设置扩展:主讲老师
     *
     * @param extemcee 扩展:主讲老师
     */
    public void setExtemcee(String extemcee) {
        this.extemcee = extemcee == null ? null : extemcee.trim();
    }

    /**
     * 获取简介
     *
     * @return summary - 简介
     */
    public String getSummary() {
        return summary;
    }

    /**
     * 设置简介
     *
     * @param summary 简介
     */
    public void setSummary(String summary) {
        this.summary = summary == null ? null : summary.trim();
    }

    /**
     * 获取艺术分类
     *
     * @return arttype - 艺术分类
     */
    public String getArttype() {
        return arttype;
    }

    /**
     * 设置艺术分类
     *
     * @param arttype 艺术分类
     */
    public void setArttype(String arttype) {
        this.arttype = arttype == null ? null : arttype.trim();
    }
}