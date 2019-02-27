package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_show_exh")
public class WhgShowExh {
    /**
     * ID
     */
    @Id
    private String id;

    /**
     * 展览名称
     */
    private String title;

    /**
     * 展览图片
     */
    private String image;

    /**
     * 展览类型
     */
    private String etype;

    /**
     * 展览时长
     */
    private String exhtime;

    private String exhorgan;

    /**
     * 联系人
     */
    private String exhcontacts;

    /**
     * 联系方式
     */
    private String exhphone;

    /**
     * 展品
     */
    private String exhgoods;

    /**
     * 展览幅数
     */
    private Integer exhnum;

    /**
     * 是否收费（0、否 1、是）
     */
    private Integer ismoney;

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
     * 删除状态
     */
    private Integer delstate;

    /**
     * 状态变更时间
     */
    private Date statemdfdate;

    /**
     * 状态变更用户
     */
    private String statemdfuser;

    /**
     * 所属文化馆
     */
    private String cultid;

    /**
     * 区域
     */
    private String area;

    /**
     * 省
     */
    private String province;

    /**
     * 部门id
     */
    private String deptid;

    /**
     * 市
     */
    private String city;

    /**
     * 艺术分类
     */
    private String arttype;

    /**
     * 关键字
     */
    private String ekey;

    /**
     * 配套活动
     */
    private String elseact;

    /**
     * 特装
     */
    private String special;

    /**
     * 布展时间
     */
    private Integer arrayexhtime;

    /**
     * 合适人群
     */
    private String fitcrowd;

    /**
     * 配送次数
     */
    private Integer psnumber;

    /**
     * 配送省份
     */
    private String psprovince;

    /**
     * 配送区域
     */
    private String pscity;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 说明
     */
    private String exhexplain;

    /**
     * 展览简介
     */
    private String exhdesc;

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
     * 所属单位
     */
    private String workplace;

    /**
     * 固定电话
     */
    private String telephone;

    /**
     * YI@
     * 备注
     */
    private String exhcomment;

    public String getExhcomment() {
        return exhcomment;
    }

    public void setExhcomment(String exhcomment) {
        this.exhcomment = exhcomment;
    }
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
     * 获取展览名称
     *
     * @return title - 展览名称
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置展览名称
     *
     * @param title 展览名称
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取展览图片
     *
     * @return image - 展览图片
     */
    public String getImage() {
        return image;
    }

    /**
     * 设置展览图片
     *
     * @param image 展览图片
     */
    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    /**
     * 获取展览类型
     *
     * @return etype - 展览类型
     */
    public String getEtype() {
        return etype;
    }

    /**
     * 设置展览类型
     *
     * @param etype 展览类型
     */
    public void setEtype(String etype) {
        this.etype = etype == null ? null : etype.trim();
    }

    /**
     * 获取展览时长
     *
     * @return exhtime - 展览时长
     */
    public String getExhtime() {
        return exhtime;
    }

    /**
     * 设置展览时长
     *
     * @param exhtime 展览时长
     */
    public void setExhtime(String exhtime) {
        this.exhtime = exhtime == null ? null : exhtime.trim();
    }

    /**
     * @return exhorgan
     */
    public String getExhorgan() {
        return exhorgan;
    }

    /**
     * @param exhorgan
     */
    public void setExhorgan(String exhorgan) {
        this.exhorgan = exhorgan == null ? null : exhorgan.trim();
    }

    /**
     * 获取联系人
     *
     * @return exhcontacts - 联系人
     */
    public String getExhcontacts() {
        return exhcontacts;
    }

    /**
     * 设置联系人
     *
     * @param exhcontacts 联系人
     */
    public void setExhcontacts(String exhcontacts) {
        this.exhcontacts = exhcontacts == null ? null : exhcontacts.trim();
    }

    /**
     * 获取联系方式
     *
     * @return exhphone - 联系方式
     */
    public String getExhphone() {
        return exhphone;
    }

    /**
     * 设置联系方式
     *
     * @param exhphone 联系方式
     */
    public void setExhphone(String exhphone) {
        this.exhphone = exhphone == null ? null : exhphone.trim();
    }

    /**
     * 获取展品
     *
     * @return exhgoods - 展品
     */
    public String getExhgoods() {
        return exhgoods;
    }

    /**
     * 设置展品
     *
     * @param exhgoods 展品
     */
    public void setExhgoods(String exhgoods) {
        this.exhgoods = exhgoods == null ? null : exhgoods.trim();
    }

    /**
     * 获取展览幅数
     *
     * @return exhnum - 展览幅数
     */
    public Integer getExhnum() {
        return exhnum;
    }

    /**
     * 设置展览幅数
     *
     * @param exhnum 展览幅数
     */
    public void setExhnum(Integer exhnum) {
        this.exhnum = exhnum;
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
     * 获取配套活动
     *
     * @return elseact - 配套活动
     */
    public String getElseact() {
        return elseact;
    }

    /**
     * 设置配套活动
     *
     * @param elseact 配套活动
     */
    public void setElseact(String elseact) {
        this.elseact = elseact == null ? null : elseact.trim();
    }

    /**
     * 获取特装
     *
     * @return special - 特装
     */
    public String getSpecial() {
        return special;
    }

    /**
     * 设置特装
     *
     * @param special 特装
     */
    public void setSpecial(String special) {
        this.special = special == null ? null : special.trim();
    }

    /**
     * 获取布展时间
     *
     * @return arrayexhtime - 布展时间
     */
    public Integer getArrayexhtime() {
        return arrayexhtime;
    }

    /**
     * 设置布展时间
     *
     * @param arrayexhtime 布展时间
     */
    public void setArrayexhtime(Integer arrayexhtime) {
        this.arrayexhtime = arrayexhtime;
    }

    /**
     * 获取合适人群
     *
     * @return fitcrowd - 合适人群
     */
    public String getFitcrowd() {
        return fitcrowd;
    }

    /**
     * 设置合适人群
     *
     * @param fitcrowd 合适人群
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
     * @return exhexplain - 说明
     */
    public String getExhexplain() {
        return exhexplain;
    }

    /**
     * 设置说明
     *
     * @param exhexplain 说明
     */
    public void setExhexplain(String exhexplain) {
        this.exhexplain = exhexplain == null ? null : exhexplain.trim();
    }

    /**
     * 获取展览简介
     *
     * @return exhdesc - 展览简介
     */
    public String getExhdesc() {
        return exhdesc;
    }

    /**
     * 设置展览简介
     *
     * @param exhdesc 展览简介
     */
    public void setExhdesc(String exhdesc) {
        this.exhdesc = exhdesc == null ? null : exhdesc.trim();
    }

    public Integer getPsnumber() {
        return psnumber;
    }

    public void setPsnumber(Integer psnumber) {
        this.psnumber = psnumber;
    }

    public String getPsprovince() {
        return psprovince;
    }

    public void setPsprovince(String psprovince) {
        this.psprovince = psprovince;
    }

    public String getPscity() {
        return pscity;
    }

    public void setPscity(String pscity) {
        this.pscity = pscity;
    }

    public String getDeptid() {
        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    public Integer getDelstate() {
        return delstate;
    }

    public void setDelstate(Integer delstate) {
        this.delstate = delstate;
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
}