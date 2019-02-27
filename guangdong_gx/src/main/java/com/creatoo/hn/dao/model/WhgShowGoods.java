package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_show_goods")
public class WhgShowGoods {
    /**
     * ID
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
     * 状态
     */
    private Integer state;

    /**
     * 状态变更时间
     */
    private Date statemdfdate;

    /**
     * 状态变更用户
     */
    private String statemdfuser;

    /**
     * 产品图片
     */
    private String image;

    /**
     * 部门id
     */
    private String deptid;

    /**
     * 节目名称
     */
    private String title;

    /**
     * 节目类型
     */
    private String type;

    /**
     * 所属机构
     */
    private String organ;

    /**
     * 区域
     */
    private String area;

    /**
     * 演出时长
     */
    private String showtime;

    /**
     * 演出人数
     */
    private Integer shownum;

    /**
     * 演出人员
     */
    private String showperson;

    /**
     * 是否收费（0、否 1、是）
     */
    private Integer ismoney;

    /**
     * 所属文化馆
     */
    private String cultid;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 艺术分类
     */
    private String arttype;

    /**
     * 关键字
     */
    private String ekey;

    /**
     * 服务类型
     */
    private String fwtype;

    /**
     * 装台时长
     */
    private String fixtime;

    /**
     * 演出方式
     */
    private String showway;

    /**
     * 适宜人群
     */
    private String fitcrowd;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 说明
     */
    private String showexplain;

    /**
     * 配送次数
     */
    private Integer psnumber;

    /**
     * 配送省
     */
    private String psprovince;

    /**
     * 配送范围
     */
    private String pscity;

    /**
     * 演出简介
     */
    private String showdesc;

    /**
     * 节目单
     */
    private String playbill;

    /**
     * 审核者
     */
    private String checkor;

    /**
     * 发布者
     */
    private String publisher;

    /**
     * 审核时间
     */
    private Date checkdate;

    /**
     * 发布时间
     */
    private Date publishdate;

    /**
     * 删除状态
     */
    private Integer delstate;

    /**
     * 所属单位
     */
    private String workplace;

    /**
     * 固定电话
     */
    private String telephone;

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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
     * 获取状态
     *
     * @return state - 状态
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态
     *
     * @param state 状态
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
     * 获取状态变更用户
     *
     * @return statemdfuser - 状态变更用户
     */
    public String getStatemdfuser() {
        return statemdfuser;
    }

    /**
     * 设置状态变更用户
     *
     * @param statemdfuser 状态变更用户
     */
    public void setStatemdfuser(String statemdfuser) {
        this.statemdfuser = statemdfuser == null ? null : statemdfuser.trim();
    }

    /**
     * 获取产品图片
     *
     * @return image - 产品图片
     */
    public String getImage() {
        return image;
    }

    /**
     * 设置产品图片
     *
     * @param image 产品图片
     */
    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    /**
     * 获取节目名称
     *
     * @return title - 节目名称
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置节目名称
     *
     * @param title 节目名称
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取节目类型
     *
     * @return type - 节目类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置节目类型
     *
     * @param type 节目类型
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * 获取所属机构
     *
     * @return organ - 所属机构
     */
    public String getOrgan() {
        return organ;
    }

    /**
     * 设置所属机构
     *
     * @param organ 所属机构
     */
    public void setOrgan(String organ) {
        this.organ = organ == null ? null : organ.trim();
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
     * 获取演出时长
     *
     * @return showtime - 演出时长
     */
    public String getShowtime() {
        return showtime;
    }

    /**
     * 设置演出时长
     *
     * @param showtime 演出时长
     */
    public void setShowtime(String showtime) {
        this.showtime = showtime == null ? null : showtime.trim();
    }

    /**
     * 获取演出人数
     *
     * @return shownum - 演出人数
     */
    public Integer getShownum() {
        return shownum;
    }

    /**
     * 设置演出人数
     *
     * @param shownum 演出人数
     */
    public void setShownum(Integer shownum) {
        this.shownum = shownum;
    }

    /**
     * 获取演出人员
     *
     * @return showperson - 演出人员
     */
    public String getShowperson() {
        return showperson;
    }

    /**
     * 设置演出人员
     *
     * @param showperson 演出人员
     */
    public void setShowperson(String showperson) {
        this.showperson = showperson == null ? null : showperson.trim();
    }

    /**
     * 获取是否收费（0、否 1、是）
     *
     * @return ismoney - 是否收费（0、否 1、是）
     */
    public Integer getIsmoney() {
        return ismoney;
    }

    /**
     * 设置是否收费（0、否 1、是）
     *
     * @param ismoney 是否收费（0、否 1、是）
     */
    public void setIsmoney(Integer ismoney) {
        this.ismoney = ismoney;
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
     * 获取联系人
     *
     * @return contacts - 联系人
     */
    public String getContacts() {
        return contacts;
    }

    /**
     * 设置联系人
     *
     * @param contacts 联系人
     */
    public void setContacts(String contacts) {
        this.contacts = contacts == null ? null : contacts.trim();
    }

    /**
     * 获取联系电话
     *
     * @return phone - 联系电话
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置联系电话
     *
     * @param phone 联系电话
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
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
     * 获取服务类型
     *
     * @return fwtype - 服务类型
     */
    public String getFwtype() {
        return fwtype;
    }

    /**
     * 设置服务类型
     *
     * @param fwtype 服务类型
     */
    public void setFwtype(String fwtype) {
        this.fwtype = fwtype == null ? null : fwtype.trim();
    }

    /**
     * 获取装台时长
     *
     * @return fixtime - 装台时长
     */
    public String getFixtime() {
        return fixtime;
    }

    /**
     * 设置装台时长
     *
     * @param fixtime 装台时长
     */
    public void setFixtime(String fixtime) {
        this.fixtime = fixtime == null ? null : fixtime.trim();
    }

    /**
     * 获取演出方式
     *
     * @return showway - 演出方式
     */
    public String getShowway() {
        return showway;
    }

    /**
     * 设置演出方式
     *
     * @param showway 演出方式
     */
    public void setShowway(String showway) {
        this.showway = showway == null ? null : showway.trim();
    }

    /**
     * 获取适宜人群
     *
     * @return fitcrowd - 适宜人群
     */
    public String getFitcrowd() {
        return fitcrowd;
    }

    /**
     * 设置适宜人群
     *
     * @param fitcrowd 适宜人群
     */
    public void setFitcrowd(String fitcrowd) {
        this.fitcrowd = fitcrowd == null ? null : fitcrowd.trim();
    }

    /**
     * 获取邮箱
     *
     * @return email - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 获取说明
     *
     * @return showexplain - 说明
     */
    public String getShowexplain() {
        return showexplain;
    }

    /**
     * 设置说明
     *
     * @param showexplain 说明
     */
    public void setShowexplain(String showexplain) {
        this.showexplain = showexplain == null ? null : showexplain.trim();
    }

    /**
     * 获取配送次数
     *
     * @return psnumber - 配送次数
     */
    public Integer getPsnumber() {
        return psnumber;
    }

    /**
     * 设置配送次数
     *
     * @param psnumber 配送次数
     */
    public void setPsnumber(Integer psnumber) {
        this.psnumber = psnumber;
    }

    /**
     * 获取配送省
     *
     * @return psprovince - 配送省
     */
    public String getPsprovince() {
        return psprovince;
    }

    /**
     * 设置配送省
     *
     * @param psprovince 配送省
     */
    public void setPsprovince(String psprovince) {
        this.psprovince = psprovince == null ? null : psprovince.trim();
    }

    /**
     * 获取配送范围
     *
     * @return pscity - 配送范围
     */
    public String getPscity() {
        return pscity;
    }

    /**
     * 设置配送范围
     *
     * @param pscity 配送范围
     */
    public void setPscity(String pscity) {
        this.pscity = pscity == null ? null : pscity.trim();
    }

    /**
     * 获取演出简介
     *
     * @return showdesc - 演出简介
     */
    public String getShowdesc() {
        return showdesc;
    }

    /**
     * 设置演出简介
     *
     * @param showdesc 演出简介
     */
    public void setShowdesc(String showdesc) {
        this.showdesc = showdesc == null ? null : showdesc.trim();
    }

    /**
     * 获取节目单
     *
     * @return playbill - 节目单
     */
    public String getPlaybill() {
        return playbill;
    }

    /**
     * 设置节目单
     *
     * @param playbill 节目单
     */
    public void setPlaybill(String playbill) {
        this.playbill = playbill == null ? null : playbill.trim();
    }

    public String getDeptid() {
        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    public String getCheckor() {
        return checkor;
    }

    public void setCheckor(String checkor) {
        this.checkor = checkor;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getCheckdate() {
        return checkdate;
    }

    public void setCheckdate(Date checkdate) {
        this.checkdate = checkdate;
    }

    public Date getPublishdate() {
        return publishdate;
    }

    public void setPublishdate(Date publishdate) {
        this.publishdate = publishdate;
    }

    public Integer getDelstate() {
        return delstate;
    }

    public void setDelstate(Integer delstate) {
        this.delstate = delstate;
    }
}