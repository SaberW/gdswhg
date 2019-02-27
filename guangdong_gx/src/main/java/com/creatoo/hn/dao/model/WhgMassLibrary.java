package com.creatoo.hn.dao.model;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "whg_mass_library")
public class WhgMassLibrary {
    /**
     * 资源库唯一标识
     */
    @Id
    private String id;

    /**
     * 资源类型。img-图片,video-视频，audio-音频，file-文档
     */
    private String resourcetype;

    /**
     * 资源库名称，最多32个字符
     */
    private String name;

    /**
     * 资源库对应表名，表名以whg_mass_library_开头，最多40个英文字符
     */
    private String tablename;

    /**
     * 资源库备注，最多128个字符
     */
    private String memo;

    /**
     * 排序索引
     */
    private Integer idx;

    /**
     * 所属文化馆标识
     */
    private String cultid;

    /**
     * 所属部门标识
     */
    private String deptid;

    /**
     * 资源库创建管理员标识
     */
    private String crtuser;

    /**
     * 资源库创建时间
     */
    private Date crtdate;

    /**
     * 资源库最后操作管理员标识
     */
    private String lastoptuser;

    /**
     * 资源库最后操作时间
     */
    private Date lastoptdate;

    /**
     * 状态。0-停用；1-启用。
     */
    private Integer state;

    /**
     * 资源库管理员操作信息
     */
    private String optinfo;

    /**
     * 共享文化馆ID
     */
    private String sharecultid;

    private String nationallanguage;

    private String copyright;

    private String constructionunit;

    private String imgurl;

    private String arttype;

    private String tags;


    /**
     * 获取资源库唯一标识
     *
     * @return id - 资源库唯一标识
     */
    public String getId() {
        return id;
    }

    /**
     * 设置资源库唯一标识
     *
     * @param id 资源库唯一标识
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取资源类型。img-图片,video-视频，audio-音频，file-文档
     *
     * @return resourcetype - 资源类型。img-图片,video-视频，audio-音频，file-文档
     */
    public String getResourcetype() {
        return resourcetype;
    }

    /**
     * 设置资源类型。img-图片,video-视频，audio-音频，file-文档
     *
     * @param resourcetype 资源类型。img-图片,video-视频，audio-音频，file-文档
     */
    public void setResourcetype(String resourcetype) {
        this.resourcetype = resourcetype == null ? null : resourcetype.trim();
    }

    /**
     * 获取资源库名称，最多32个字符
     *
     * @return name - 资源库名称，最多32个字符
     */
    public String getName() {
        return name;
    }

    /**
     * 设置资源库名称，最多32个字符
     *
     * @param name 资源库名称，最多32个字符
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取资源库对应表名，表名以whg_mass_library_开头，最多40个英文字符
     *
     * @return tablename - 资源库对应表名，表名以whg_mass_library_开头，最多40个英文字符
     */
    public String getTablename() {
        return tablename;
    }

    /**
     * 设置资源库对应表名，表名以whg_mass_library_开头，最多40个英文字符
     *
     * @param tablename 资源库对应表名，表名以whg_mass_library_开头，最多40个英文字符
     */
    public void setTablename(String tablename) {
        this.tablename = tablename == null ? null : tablename.trim();
    }

    /**
     * 获取资源库备注，最多128个字符
     *
     * @return memo - 资源库备注，最多128个字符
     */
    public String getMemo() {
        return memo;
    }

    /**
     * 设置资源库备注，最多128个字符
     *
     * @param memo 资源库备注，最多128个字符
     */
    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    /**
     * 获取排序索引
     *
     * @return idx - 排序索引
     */
    public Integer getIdx() {
        return idx;
    }

    /**
     * 设置排序索引
     *
     * @param idx 排序索引
     */
    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    /**
     * 获取所属文化馆标识
     *
     * @return cultid - 所属文化馆标识
     */
    public String getCultid() {
        return cultid;
    }

    /**
     * 设置所属文化馆标识
     *
     * @param cultid 所属文化馆标识
     */
    public void setCultid(String cultid) {
        this.cultid = cultid == null ? null : cultid.trim();
    }

    /**
     * 获取所属部门标识
     *
     * @return deptid - 所属部门标识
     */
    public String getDeptid() {
        return deptid;
    }

    /**
     * 设置所属部门标识
     *
     * @param deptid 所属部门标识
     */
    public void setDeptid(String deptid) {
        this.deptid = deptid == null ? null : deptid.trim();
    }

    /**
     * 获取资源库创建管理员标识
     *
     * @return crtuser - 资源库创建管理员标识
     */
    public String getCrtuser() {
        return crtuser;
    }

    /**
     * 设置资源库创建管理员标识
     *
     * @param crtuser 资源库创建管理员标识
     */
    public void setCrtuser(String crtuser) {
        this.crtuser = crtuser == null ? null : crtuser.trim();
    }

    /**
     * 获取资源库创建时间
     *
     * @return crtdate - 资源库创建时间
     */
    public Date getCrtdate() {
        return crtdate;
    }

    /**
     * 设置资源库创建时间
     *
     * @param crtdate 资源库创建时间
     */
    public void setCrtdate(Date crtdate) {
        this.crtdate = crtdate;
    }

    /**
     * 获取资源库最后操作管理员标识
     *
     * @return lastoptuser - 资源库最后操作管理员标识
     */
    public String getLastoptuser() {
        return lastoptuser;
    }

    /**
     * 设置资源库最后操作管理员标识
     *
     * @param lastoptuser 资源库最后操作管理员标识
     */
    public void setLastoptuser(String lastoptuser) {
        this.lastoptuser = lastoptuser == null ? null : lastoptuser.trim();
    }

    /**
     * 获取资源库最后操作时间
     *
     * @return lastoptdate - 资源库最后操作时间
     */
    public Date getLastoptdate() {
        return lastoptdate;
    }

    /**
     * 设置资源库最后操作时间
     *
     * @param lastoptdate 资源库最后操作时间
     */
    public void setLastoptdate(Date lastoptdate) {
        this.lastoptdate = lastoptdate;
    }

    /**
     * 获取状态。0-停用；1-启用。
     *
     * @return state - 状态。0-停用；1-启用。
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态。0-停用；1-启用。
     *
     * @param state 状态。0-停用；1-启用。
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取资源库管理员操作信息
     *
     * @return optinfo - 资源库管理员操作信息
     */
    public String getOptinfo() {
        return optinfo;
    }


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
     * 设置资源库管理员操作信息
     *
     * @param optinfo 资源库管理员操作信息
     */
    public void setOptinfo(String optinfo) {
        this.optinfo = optinfo == null ? null : optinfo.trim();
    }

    public String getSharecultid() {
        return sharecultid;
    }

    public void setSharecultid(String sharecultid) {
        this.sharecultid = sharecultid;
    }

    public String getNationallanguage() {
        return nationallanguage;
    }

    public void setNationallanguage(String nationallanguage) {
        this.nationallanguage = nationallanguage;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getConstructionunit() {
        return constructionunit;
    }

    public void setConstructionunit(String constructionunit) {
        this.constructionunit = constructionunit;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getArttype() {
        return arttype;
    }

    public void setArttype(String arttype) {
        this.arttype = arttype;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}