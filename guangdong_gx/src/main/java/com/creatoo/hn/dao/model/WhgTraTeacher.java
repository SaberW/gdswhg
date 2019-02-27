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
     * 状态:参考枚举
     */
    private Integer state;

    /**
     * 所属文化馆标识
     */
    private String cultid;

    /**
     * 状态变更用户id
     */
    private String statemdfuser;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 部门id
     */
    private String deptid;

    /**
     * 审核人标识
     */
    private String checkor;

    /**
     * 审核时间
     */
    private Date checkdate;

    /**
     * 发布人标识
     */
    private String publisher;

    /**
     * 发布时间
     */
    private Date publishdate;

    /**
     * 回收状态: 0-未回收， 1-已回收
     */
    private Integer delstate;

    /**
     * 创建人标识
     */
    private String crtuser;

    /**
     * 出生日期
     */
    private Date birthday;

    /**
     * 工作单位
     */
    private String workunit;

    /**
     * 手机号码
     */
    private String phonenumber;

    /**
     * 固定号码
     */
    private String fixednumber;

    /**
     * 政治面貌:中共党员,民主党派,群众,其他
     */
    private String politicalstatus;

    /**
     * 职业:国家公务员,专业技术人员,职员,企业管理人员,工人,农民,学生,教师,现役军人,自由职业者,个体经营者,无业人员,退（离）休人员,其他
     */
    private String occupation;

    /**
     * 兴趣爱好
     */
    private String hobby;

    /**
     * 学历:小学,初中,高中,专科,本科,硕士,博士,其他
     */
    private String education;

    /**
     * 所在地
     */
    private String location;

    /**
     * 职称
     */
    private String titles;

    /**
     * 资质
     */
    private String aptitude;

    /**
     * 荣誉
     */
    private String honor;

    /**
     * 省级推荐标记：0取消，1推荐
     */
    private Integer toprovince;

    /**
     * 市级推荐标记：0取消，1推荐
     */
    private Integer tocity;

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

    /**
     * 获取状态变更用户id
     *
     * @return statemdfuser - 状态变更用户id
     */
    public String getStatemdfuser() {
        return statemdfuser;
    }

    /**
     * 设置状态变更用户id
     *
     * @param statemdfuser 状态变更用户id
     */
    public void setStatemdfuser(String statemdfuser) {
        this.statemdfuser = statemdfuser == null ? null : statemdfuser.trim();
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
     * 获取部门id
     *
     * @return deptid - 部门id
     */
    public String getDeptid() {
        return deptid;
    }

    /**
     * 设置部门id
     *
     * @param deptid 部门id
     */
    public void setDeptid(String deptid) {
        this.deptid = deptid == null ? null : deptid.trim();
    }

    /**
     * 获取审核人标识
     *
     * @return checkor - 审核人标识
     */
    public String getCheckor() {
        return checkor;
    }

    /**
     * 设置审核人标识
     *
     * @param checkor 审核人标识
     */
    public void setCheckor(String checkor) {
        this.checkor = checkor == null ? null : checkor.trim();
    }

    /**
     * 获取审核时间
     *
     * @return checkdate - 审核时间
     */
    public Date getCheckdate() {
        return checkdate;
    }

    /**
     * 设置审核时间
     *
     * @param checkdate 审核时间
     */
    public void setCheckdate(Date checkdate) {
        this.checkdate = checkdate;
    }

    /**
     * 获取发布人标识
     *
     * @return publisher - 发布人标识
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * 设置发布人标识
     *
     * @param publisher 发布人标识
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher == null ? null : publisher.trim();
    }

    /**
     * 获取发布时间
     *
     * @return publishdate - 发布时间
     */
    public Date getPublishdate() {
        return publishdate;
    }

    /**
     * 设置发布时间
     *
     * @param publishdate 发布时间
     */
    public void setPublishdate(Date publishdate) {
        this.publishdate = publishdate;
    }

    /**
     * 获取回收状态: 0-未回收， 1-已回收
     *
     * @return delstate - 回收状态: 0-未回收， 1-已回收
     */
    public Integer getDelstate() {
        return delstate;
    }

    /**
     * 设置回收状态: 0-未回收， 1-已回收
     *
     * @param delstate 回收状态: 0-未回收， 1-已回收
     */
    public void setDelstate(Integer delstate) {
        this.delstate = delstate;
    }

    /**
     * 获取创建人标识
     *
     * @return crtuser - 创建人标识
     */
    public String getCrtuser() {
        return crtuser;
    }

    /**
     * 设置创建人标识
     *
     * @param crtuser 创建人标识
     */
    public void setCrtuser(String crtuser) {
        this.crtuser = crtuser == null ? null : crtuser.trim();
    }

    /**
     * 获取出生日期
     *
     * @return birthday - 出生日期
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * 设置出生日期
     *
     * @param birthday 出生日期
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * 获取工作单位
     *
     * @return workunit - 工作单位
     */
    public String getWorkunit() {
        return workunit;
    }

    /**
     * 设置工作单位
     *
     * @param workunit 工作单位
     */
    public void setWorkunit(String workunit) {
        this.workunit = workunit == null ? null : workunit.trim();
    }

    /**
     * 获取手机号码
     *
     * @return phonenumber - 手机号码
     */
    public String getPhonenumber() {
        return phonenumber;
    }

    /**
     * 设置手机号码
     *
     * @param phonenumber 手机号码
     */
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber == null ? null : phonenumber.trim();
    }

    /**
     * 获取固定号码
     *
     * @return fixednumber - 固定号码
     */
    public String getFixednumber() {
        return fixednumber;
    }

    /**
     * 设置固定号码
     *
     * @param fixednumber 固定号码
     */
    public void setFixednumber(String fixednumber) {
        this.fixednumber = fixednumber == null ? null : fixednumber.trim();
    }

    /**
     * 获取政治面貌:中共党员,民主党派,群众,其他
     *
     * @return politicalstatus - 政治面貌:中共党员,民主党派,群众,其他
     */
    public String getPoliticalstatus() {
        return politicalstatus;
    }

    /**
     * 设置政治面貌:中共党员,民主党派,群众,其他
     *
     * @param politicalstatus 政治面貌:中共党员,民主党派,群众,其他
     */
    public void setPoliticalstatus(String politicalstatus) {
        this.politicalstatus = politicalstatus == null ? null : politicalstatus.trim();
    }

    /**
     * 获取职业:国家公务员,专业技术人员,职员,企业管理人员,工人,农民,学生,教师,现役军人,自由职业者,个体经营者,无业人员,退（离）休人员,其他
     *
     * @return occupation - 职业:国家公务员,专业技术人员,职员,企业管理人员,工人,农民,学生,教师,现役军人,自由职业者,个体经营者,无业人员,退（离）休人员,其他
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     * 设置职业:国家公务员,专业技术人员,职员,企业管理人员,工人,农民,学生,教师,现役军人,自由职业者,个体经营者,无业人员,退（离）休人员,其他
     *
     * @param occupation 职业:国家公务员,专业技术人员,职员,企业管理人员,工人,农民,学生,教师,现役军人,自由职业者,个体经营者,无业人员,退（离）休人员,其他
     */
    public void setOccupation(String occupation) {
        this.occupation = occupation == null ? null : occupation.trim();
    }

    /**
     * 获取兴趣爱好
     *
     * @return hobby - 兴趣爱好
     */
    public String getHobby() {
        return hobby;
    }

    /**
     * 设置兴趣爱好
     *
     * @param hobby 兴趣爱好
     */
    public void setHobby(String hobby) {
        this.hobby = hobby == null ? null : hobby.trim();
    }

    /**
     * 获取学历:小学,初中,高中,专科,本科,硕士,博士,其他
     *
     * @return education - 学历:小学,初中,高中,专科,本科,硕士,博士,其他
     */
    public String getEducation() {
        return education;
    }

    /**
     * 设置学历:小学,初中,高中,专科,本科,硕士,博士,其他
     *
     * @param education 学历:小学,初中,高中,专科,本科,硕士,博士,其他
     */
    public void setEducation(String education) {
        this.education = education == null ? null : education.trim();
    }

    /**
     * 获取所在地
     *
     * @return location - 所在地
     */
    public String getLocation() {
        return location;
    }

    /**
     * 设置所在地
     *
     * @param location 所在地
     */
    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    /**
     * 获取职称
     *
     * @return titles - 职称
     */
    public String getTitles() {
        return titles;
    }

    /**
     * 设置职称
     *
     * @param titles 职称
     */
    public void setTitles(String titles) {
        this.titles = titles == null ? null : titles.trim();
    }

    /**
     * 获取资质
     *
     * @return aptitude - 资质
     */
    public String getAptitude() {
        return aptitude;
    }

    /**
     * 设置资质
     *
     * @param aptitude 资质
     */
    public void setAptitude(String aptitude) {
        this.aptitude = aptitude == null ? null : aptitude.trim();
    }

    /**
     * 获取荣誉
     *
     * @return honor - 荣誉
     */
    public String getHonor() {
        return honor;
    }

    /**
     * 设置荣誉
     *
     * @param honor 荣誉
     */
    public void setHonor(String honor) {
        this.honor = honor == null ? null : honor.trim();
    }

    public Integer getToprovince() {
        return toprovince;
    }

    public void setToprovince(Integer toprovince) {
        this.toprovince = toprovince;
    }

    public Integer getTocity() {
        return tocity;
    }

    public void setTocity(Integer tocity) {
        this.tocity = tocity;
    }
}