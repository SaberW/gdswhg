package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_supply_traperson")
public class WhgSupplyTraperson {
    /**
     * 人员ID
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
     * 状态(1-可编辑, 9-待审核, 2-待发布，6-已发布, 4-已下架, 5-已撤消)
     */
    private Integer state;

    /**
     * 状态变更时间
     */
    private Date statemdfdate;

    /**
     * 状态变更用户ID
     */
    private String statemdfuser;

    /**
     * 主讲人姓名
     */
    private String name;

    /**
     * 照片
     */
    private String image;

    /**
     * 性别：0 女 1男
     */
    private Integer sex;

    /**
     * 出生年月日
     */
    private String birthstr;

    /**
     * 学历
     */
    private String xueli;

    /**
     * 单位
     */
    private String cultid;

    /**
     * 省份
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
     * 专长
     */
    private String special;

    /**
     * 关键字
     */
    private String ekey;

    /**
     * 标签
     */
    private String etag;

    /**
     * 主讲人简历
     */
    private String teacherdesc;

    /**
     * 职称
     */
    private String technical;

    /**
     * 培训ID
     */
    private String entid;

    /**
     * 所属单位
     */
    private String company;

    /**
     * 工作人员
     */
    private String worker;

    /**
     * 获取人员ID
     *
     * @return id - 人员ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置人员ID
     *
     * @param id 人员ID
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
     * 获取状态(1-可编辑, 9-待审核, 2-待发布，6-已发布, 4-已下架, 5-已撤消)
     *
     * @return state - 状态(1-可编辑, 9-待审核, 2-待发布，6-已发布, 4-已下架, 5-已撤消)
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态(1-可编辑, 9-待审核, 2-待发布，6-已发布, 4-已下架, 5-已撤消)
     *
     * @param state 状态(1-可编辑, 9-待审核, 2-待发布，6-已发布, 4-已下架, 5-已撤消)
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取状态变更时间
     *
     * @return statemdfdate - 状态变更时间
     */
    public Date getStatemdfdate() {
        return statemdfdate;
    }

    /**
     * 设置状态变更时间
     *
     * @param statemdfdate 状态变更时间
     */
    public void setStatemdfdate(Date statemdfdate) {
        this.statemdfdate = statemdfdate;
    }

    /**
     * 获取状态变更用户ID
     *
     * @return statemdfuser - 状态变更用户ID
     */
    public String getStatemdfuser() {
        return statemdfuser;
    }

    /**
     * 设置状态变更用户ID
     *
     * @param statemdfuser 状态变更用户ID
     */
    public void setStatemdfuser(String statemdfuser) {
        this.statemdfuser = statemdfuser == null ? null : statemdfuser.trim();
    }

    /**
     * 获取主讲人姓名
     *
     * @return name - 主讲人姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置主讲人姓名
     *
     * @param name 主讲人姓名
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取照片
     *
     * @return image - 照片
     */
    public String getImage() {
        return image;
    }

    /**
     * 设置照片
     *
     * @param image 照片
     */
    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    /**
     * 获取性别：0 女 1男
     *
     * @return sex - 性别：0 女 1男
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * 设置性别：0 女 1男
     *
     * @param sex 性别：0 女 1男
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * 获取出生年月日
     *
     * @return birthstr - 出生年月日
     */
    public String getBirthstr() {
        return birthstr;
    }

    /**
     * 设置出生年月日
     *
     * @param birthstr 出生年月日
     */
    public void setBirthstr(String birthstr) {
        this.birthstr = birthstr == null ? null : birthstr.trim();
    }

    /**
     * 获取学历
     *
     * @return xueli - 学历
     */
    public String getXueli() {
        return xueli;
    }

    /**
     * 设置学历
     *
     * @param xueli 学历
     */
    public void setXueli(String xueli) {
        this.xueli = xueli == null ? null : xueli.trim();
    }

    /**
     * 获取单位
     *
     * @return cultid - 单位
     */
    public String getCultid() {
        return cultid;
    }

    /**
     * 设置单位
     *
     * @param cultid 单位
     */
    public void setCultid(String cultid) {
        this.cultid = cultid == null ? null : cultid.trim();
    }

    /**
     * 获取省份
     *
     * @return province - 省份
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置省份
     *
     * @param province 省份
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
     * 获取专长
     *
     * @return special - 专长
     */
    public String getSpecial() {
        return special;
    }

    /**
     * 设置专长
     *
     * @param special 专长
     */
    public void setSpecial(String special) {
        this.special = special == null ? null : special.trim();
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
     * 获取主讲人简历
     *
     * @return teacherdesc - 主讲人简历
     */
    public String getTeacherdesc() {
        return teacherdesc;
    }

    /**
     * 设置主讲人简历
     *
     * @param teacherdesc 主讲人简历
     */
    public void setTeacherdesc(String teacherdesc) {
        this.teacherdesc = teacherdesc == null ? null : teacherdesc.trim();
    }

    /**
     * 获取职称
     *
     * @return technical - 职称
     */
    public String getTechnical() {
        return technical;
    }

    /**
     * 设置职称
     *
     * @param technical 职称
     */
    public void setTechnical(String technical) {
        this.technical = technical == null ? null : technical.trim();
    }

    /**
     * 获取培训ID
     *
     * @return entid - 培训ID
     */
    public String getEntid() {
        return entid;
    }

    /**
     * 设置培训ID
     *
     * @param entid 培训ID
     */
    public void setEntid(String entid) {
        this.entid = entid == null ? null : entid.trim();
    }

    /**
     * 获取所属单位
     *
     * @return company - 所属单位
     */
    public String getCompany() {
        return company;
    }

    /**
     * 设置所属单位
     *
     * @param company 所属单位
     */
    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }

    /**
     * 获取工作人员
     *
     * @return worker - 工作人员
     */
    public String getWorker() {
        return worker;
    }

    /**
     * 设置工作人员
     *
     * @param worker 工作人员
     */
    public void setWorker(String worker) {
        this.worker = worker == null ? null : worker.trim();
    }
}