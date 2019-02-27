package com.creatoo.hn.dao.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_fyi_fund")
public class WhgFyiFund {
    /**
     * ID
     */
    @Id
    private String id;

    /**
     * 资金编号
     */
    private String fundnum;

    /**
     * 拨付事项
     */
    private String matter;

    /**
     * 拨付资金类别
     */
    private String etype;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String area;

    /**
     * 单位名称
     */
    private String cultid;

    /**
     * 补助金额
     */
    private Integer money;

    /**
     * 拨付日期
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date funddate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 审批文件
     */
    private String ratifydoc;

    /**
     * 相关类型
     */
    private Integer elsetype;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date crtdate;

    /**
     * 创建人
     */
    private String crtuser;

    /**
     * 状态变更时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date statemdfdate;

    /**
     * 状态变更用户ID
     */
    private String statemdfuser;

    /**
     * 相关类型名称
     */
    private String elsetypename;

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
     * 获取资金编号
     *
     * @return fundnum - 资金编号
     */
    public String getFundnum() {
        return fundnum;
    }

    /**
     * 设置资金编号
     *
     * @param fundnum 资金编号
     */
    public void setFundnum(String fundnum) {
        this.fundnum = fundnum == null ? null : fundnum.trim();
    }

    /**
     * 获取拨付事项
     *
     * @return matter - 拨付事项
     */
    public String getMatter() {
        return matter;
    }

    /**
     * 设置拨付事项
     *
     * @param matter 拨付事项
     */
    public void setMatter(String matter) {
        this.matter = matter == null ? null : matter.trim();
    }

    /**
     * 获取拨付资金类别
     *
     * @return etype - 拨付资金类别
     */
    public String getEtype() {
        return etype;
    }

    /**
     * 设置拨付资金类别
     *
     * @param etype 拨付资金类别
     */
    public void setEtype(String etype) {
        this.etype = etype == null ? null : etype.trim();
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
     * 获取区
     *
     * @return area - 区
     */
    public String getArea() {
        return area;
    }

    /**
     * 设置区
     *
     * @param area 区
     */
    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    /**
     * 获取单位名称
     *
     * @return cultid - 单位名称
     */
    public String getCultid() {
        return cultid;
    }

    /**
     * 设置单位名称
     *
     * @param cultid 单位名称
     */
    public void setCultid(String cultid) {
        this.cultid = cultid == null ? null : cultid.trim();
    }

    /**
     * 获取补助金额
     *
     * @return money - 补助金额
     */
    public Integer getMoney() {
        return money;
    }

    /**
     * 设置补助金额
     *
     * @param money 补助金额
     */
    public void setMoney(Integer money) {
        this.money = money;
    }

    /**
     * 获取拨付日期
     *
     * @return funddate - 拨付日期
     */
    public Date getFunddate() {
        return funddate;
    }

    /**
     * 设置拨付日期
     *
     * @param funddate 拨付日期
     */
    public void setFunddate(Date funddate) {
        this.funddate = funddate;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 获取审批文件
     *
     * @return ratifydoc - 审批文件
     */
    public String getRatifydoc() {
        return ratifydoc;
    }

    /**
     * 设置审批文件
     *
     * @param ratifydoc 审批文件
     */
    public void setRatifydoc(String ratifydoc) {
        this.ratifydoc = ratifydoc == null ? null : ratifydoc.trim();
    }

    /**
     * 获取相关类型
     *
     * @return elsetype - 相关类型
     */
    public Integer getElsetype() {
        return elsetype;
    }

    /**
     * 设置相关类型
     *
     * @param elsetype 相关类型
     */
    public void setElsetype(Integer elsetype) {
        this.elsetype = elsetype;
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
     * 获取相关类型名称
     *
     * @return elsetypename - 相关类型名称
     */
    public String getElsetypename() {
        return elsetypename;
    }

    /**
     * 设置相关类型名称
     *
     * @param elsetypename 相关类型名称
     */
    public void setElsetypename(String elsetypename) {
        this.elsetypename = elsetypename == null ? null : elsetypename.trim();
    }
}