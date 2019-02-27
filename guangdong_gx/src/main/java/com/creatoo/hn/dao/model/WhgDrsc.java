package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_drsc")
public class WhgDrsc {
    /**
     * 数字资源标识
     */
    @Id
    private String drscid;

    /**
     * 数字资源名称
     */
    private String drsctitle;

    /**
     * 数字资源来源
     */
    private String drscfrom;

    /**
     * 数字资源封面图片
     */
    private String drscpic;

    /**
     * 数字资源创建时间
     */
    private Date drsccrttime;

    /**
     * 数字资源路径
     */
    private String enturl;

    /**
     * 艺术分类
     */
    private String drscarttyp;

    /**
     * 数字资源的分类(1图片 2 视频 3音频 4 文档)
     */
    private String drsctyp;

    /**
     * 状态.0-初始;1-送审;2-已审;3-发布.
     */
    private Integer drscstate;

    /**
     * 修改状态时间
     */
    private Date drscopttime;

    /**
     * 资源时长
     */
    private String drsctime;

    /**
     * 数字资源关键字
     */
    private String drsckey;

    /**
     * 数字资源简介
     */
    private String drscintro;

    /**
     * 所属文化馆标识
     */
    private String drscvenueid;

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
     * 创建人标识
     */
    private String crtuser;

    /**
     * 回收状态: 0-未回收， 1-已回收
     */
    private Integer delstate;

    /**
     * 是否推荐
     */
    private Integer isrecommend;

    /**
     * 省级推荐标记：0取消，1推荐
     */
    private Integer toprovince;

    /**
     * 市级推荐标记：0取消，1推荐
     */
    private Integer tocity;

    /**
     * 获取数字资源标识
     *
     * @return drscid - 数字资源标识
     */
    public String getDrscid() {
        return drscid;
    }

    /**
     * 设置数字资源标识
     *
     * @param drscid 数字资源标识
     */
    public void setDrscid(String drscid) {
        this.drscid = drscid == null ? null : drscid.trim();
    }

    /**
     * 获取数字资源名称
     *
     * @return drsctitle - 数字资源名称
     */
    public String getDrsctitle() {
        return drsctitle;
    }

    /**
     * 设置数字资源名称
     *
     * @param drsctitle 数字资源名称
     */
    public void setDrsctitle(String drsctitle) {
        this.drsctitle = drsctitle == null ? null : drsctitle.trim();
    }

    /**
     * 获取数字资源来源
     *
     * @return drscfrom - 数字资源来源
     */
    public String getDrscfrom() {
        return drscfrom;
    }

    /**
     * 设置数字资源来源
     *
     * @param drscfrom 数字资源来源
     */
    public void setDrscfrom(String drscfrom) {
        this.drscfrom = drscfrom == null ? null : drscfrom.trim();
    }

    /**
     * 获取数字资源封面图片
     *
     * @return drscpic - 数字资源封面图片
     */
    public String getDrscpic() {
        return drscpic;
    }

    /**
     * 设置数字资源封面图片
     *
     * @param drscpic 数字资源封面图片
     */
    public void setDrscpic(String drscpic) {
        this.drscpic = drscpic == null ? null : drscpic.trim();
    }

    /**
     * 获取数字资源创建时间
     *
     * @return drsccrttime - 数字资源创建时间
     */
    public Date getDrsccrttime() {
        return drsccrttime;
    }

    /**
     * 设置数字资源创建时间
     *
     * @param drsccrttime 数字资源创建时间
     */
    public void setDrsccrttime(Date drsccrttime) {
        this.drsccrttime = drsccrttime;
    }

    /**
     * 获取数字资源路径
     *
     * @return enturl - 数字资源路径
     */
    public String getEnturl() {
        return enturl;
    }

    /**
     * 设置数字资源路径
     *
     * @param enturl 数字资源路径
     */
    public void setEnturl(String enturl) {
        this.enturl = enturl == null ? null : enturl.trim();
    }

    /**
     * 获取艺术分类
     *
     * @return drscarttyp - 艺术分类
     */
    public String getDrscarttyp() {
        return drscarttyp;
    }

    /**
     * 设置艺术分类
     *
     * @param drscarttyp 艺术分类
     */
    public void setDrscarttyp(String drscarttyp) {
        this.drscarttyp = drscarttyp == null ? null : drscarttyp.trim();
    }

    /**
     * 获取数字资源的分类(1图片 2 视频 3音频 4 文档)
     *
     * @return drsctyp - 数字资源的分类(1图片 2 视频 3音频 4 文档)
     */
    public String getDrsctyp() {
        return drsctyp;
    }

    /**
     * 设置数字资源的分类(1图片 2 视频 3音频 4 文档)
     *
     * @param drsctyp 数字资源的分类(1图片 2 视频 3音频 4 文档)
     */
    public void setDrsctyp(String drsctyp) {
        this.drsctyp = drsctyp == null ? null : drsctyp.trim();
    }

    /**
     * 获取状态.0-初始;1-送审;2-已审;3-发布.
     *
     * @return drscstate - 状态.0-初始;1-送审;2-已审;3-发布.
     */
    public Integer getDrscstate() {
        return drscstate;
    }

    /**
     * 设置状态.0-初始;1-送审;2-已审;3-发布.
     *
     * @param drscstate 状态.0-初始;1-送审;2-已审;3-发布.
     */
    public void setDrscstate(Integer drscstate) {
        this.drscstate = drscstate;
    }

    /**
     * 获取修改状态时间
     *
     * @return drscopttime - 修改状态时间
     */
    public Date getDrscopttime() {
        return drscopttime;
    }

    /**
     * 设置修改状态时间
     *
     * @param drscopttime 修改状态时间
     */
    public void setDrscopttime(Date drscopttime) {
        this.drscopttime = drscopttime;
    }

    /**
     * 获取资源时长
     *
     * @return drsctime - 资源时长
     */
    public String getDrsctime() {
        return drsctime;
    }

    /**
     * 设置资源时长
     *
     * @param drsctime 资源时长
     */
    public void setDrsctime(String drsctime) {
        this.drsctime = drsctime == null ? null : drsctime.trim();
    }

    /**
     * 获取数字资源关键字
     *
     * @return drsckey - 数字资源关键字
     */
    public String getDrsckey() {
        return drsckey;
    }

    /**
     * 设置数字资源关键字
     *
     * @param drsckey 数字资源关键字
     */
    public void setDrsckey(String drsckey) {
        this.drsckey = drsckey == null ? null : drsckey.trim();
    }

    /**
     * 获取数字资源简介
     *
     * @return drscintro - 数字资源简介
     */
    public String getDrscintro() {
        return drscintro;
    }

    /**
     * 设置数字资源简介
     *
     * @param drscintro 数字资源简介
     */
    public void setDrscintro(String drscintro) {
        this.drscintro = drscintro == null ? null : drscintro.trim();
    }

    /**
     * 获取所属文化馆标识
     *
     * @return drscvenueid - 所属文化馆标识
     */
    public String getDrscvenueid() {
        return drscvenueid;
    }

    /**
     * 设置所属文化馆标识
     *
     * @param drscvenueid 所属文化馆标识
     */
    public void setDrscvenueid(String drscvenueid) {
        this.drscvenueid = drscvenueid == null ? null : drscvenueid.trim();
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

    public Integer getIsrecommend() {
        return isrecommend;
    }

    public void setIsrecommend(Integer isrecommend) {
        this.isrecommend = isrecommend;
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