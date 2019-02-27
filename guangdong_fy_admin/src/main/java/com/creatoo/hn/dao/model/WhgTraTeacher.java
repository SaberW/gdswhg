package com.creatoo.hn.dao.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_tra_teacher")
public class WhgTraTeacher {
    /**
     * 培训老师标识
     */
    @Id
    private String id;

    /**
     * 性别 0、女 1、男
     */
    private Integer sex;

    /**
     * 培训老师所属注册用户标识
     */
    private String teacheruid;

    /**
     * 培训老师照片
     */
    private String teacherpic;

    /**
     * 培训老师名称
     */
    private String name;

    /**
     * 标签
     */
    private String etag;

    /**
     * 关键字
     */
    private String ekey;

    /**
     * 培训老师专长类型
     */
    private String teachertype;

    /**
     * 培训老师所属区域
     */
    private String area;

    /**
     * 培训老师艺术类型
     */
    private String arttype;

    /**
     * 培训老师简介
     */
    private String teacherdesc;

    /**
     * 专长介绍
     */
    private String teacherexpdesc;

    /**
     * 注册日期
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date crtdate;

    /**
     * 修改状态的时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date statemdfdate;

    /**
     * 状态变更用户
     */
    private String statemdfuser;

    /**
     * 状态:参考枚举
     */
    private Integer state;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 所属文化馆标识
     */
    private String cultid;

    /**
     * 获取培训老师标识
     *
     * @return id - 培训老师标识
     */
    public String getId() {
        return id;
    }

    /**
     * 设置培训老师标识
     *
     * @param id 培训老师标识
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取性别 0、女 1、男
     *
     * @return sex - 性别 0、女 1、男
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * 设置性别 0、女 1、男
     *
     * @param sex 性别 0、女 1、男
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * 获取培训老师所属注册用户标识
     *
     * @return teacheruid - 培训老师所属注册用户标识
     */
    public String getTeacheruid() {
        return teacheruid;
    }

    /**
     * 设置培训老师所属注册用户标识
     *
     * @param teacheruid 培训老师所属注册用户标识
     */
    public void setTeacheruid(String teacheruid) {
        this.teacheruid = teacheruid == null ? null : teacheruid.trim();
    }

    /**
     * 获取培训老师照片
     *
     * @return teacherpic - 培训老师照片
     */
    public String getTeacherpic() {
        return teacherpic;
    }

    /**
     * 设置培训老师照片
     *
     * @param teacherpic 培训老师照片
     */
    public void setTeacherpic(String teacherpic) {
        this.teacherpic = teacherpic == null ? null : teacherpic.trim();
    }

    /**
     * 获取培训老师名称
     *
     * @return name - 培训老师名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置培训老师名称
     *
     * @param name 培训老师名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取标签
     *
     * @return etag - 标签
     */
    public String getEtag() {
        return etag;
    }

    /**
     * 设置标签
     *
     * @param etag 标签
     */
    public void setEtag(String etag) {
        this.etag = etag == null ? null : etag.trim();
    }

    /**
     * 获取关键字
     *
     * @return ekey - 关键字
     */
    public String getEkey() {
        return ekey;
    }

    /**
     * 设置关键字
     *
     * @param ekey 关键字
     */
    public void setEkey(String ekey) {
        this.ekey = ekey == null ? null : ekey.trim();
    }

    /**
     * 获取培训老师专长类型
     *
     * @return teachertype - 培训老师专长类型
     */
    public String getTeachertype() {
        return teachertype;
    }

    /**
     * 设置培训老师专长类型
     *
     * @param teachertype 培训老师专长类型
     */
    public void setTeachertype(String teachertype) {
        this.teachertype = teachertype == null ? null : teachertype.trim();
    }

    /**
     * 获取培训老师所属区域
     *
     * @return area - 培训老师所属区域
     */
    public String getArea() {
        return area;
    }

    /**
     * 设置培训老师所属区域
     *
     * @param area 培训老师所属区域
     */
    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    /**
     * 获取培训老师艺术类型
     *
     * @return arttype - 培训老师艺术类型
     */
    public String getArttype() {
        return arttype;
    }

    /**
     * 设置培训老师艺术类型
     *
     * @param arttype 培训老师艺术类型
     */
    public void setArttype(String arttype) {
        this.arttype = arttype == null ? null : arttype.trim();
    }

    /**
     * 获取培训老师简介
     *
     * @return teacherdesc - 培训老师简介
     */
    public String getTeacherdesc() {
        return teacherdesc;
    }

    /**
     * 设置培训老师简介
     *
     * @param teacherdesc 培训老师简介
     */
    public void setTeacherdesc(String teacherdesc) {
        this.teacherdesc = teacherdesc == null ? null : teacherdesc.trim();
    }

    /**
     * 获取专长介绍
     *
     * @return teacherexpdesc - 专长介绍
     */
    public String getTeacherexpdesc() {
        return teacherexpdesc;
    }

    /**
     * 设置专长介绍
     *
     * @param teacherexpdesc 专长介绍
     */
    public void setTeacherexpdesc(String teacherexpdesc) {
        this.teacherexpdesc = teacherexpdesc == null ? null : teacherexpdesc.trim();
    }

    /**
     * 获取注册日期
     *
     * @return crtdate - 注册日期
     */
    public Date getCrtdate() {
        return crtdate;
    }

    /**
     * 设置注册日期
     *
     * @param crtdate 注册日期
     */
    public void setCrtdate(Date crtdate) {
        this.crtdate = crtdate;
    }

    /**
     * 获取修改状态的时间
     *
     * @return statemdfdate - 修改状态的时间
     */
    public Date getStatemdfdate() {
        return statemdfdate;
    }

    /**
     * 设置修改状态的时间
     *
     * @param statemdfdate 修改状态的时间
     */
    public void setStatemdfdate(Date statemdfdate) {
        this.statemdfdate = statemdfdate;
    }

    /**
     * 获取状态:参考枚举
     *
     * @return state - 状态:参考枚举
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态:参考枚举
     *
     * @param state 状态:参考枚举
     */
    public void setState(Integer state) {
        this.state = state;
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

    public String getStatemdfuser() {
        return statemdfuser;
    }

    public void setStatemdfuser(String statemdfuser) {
        this.statemdfuser = statemdfuser;
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
}