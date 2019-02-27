package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_inf_colinfo")
public class WhgInfColinfo {
    /**
     * 栏目内容id
     */
    @Id
    private String clnfid;

    /**
     * 标题
     */
    private String clnftltle;

    /**
     * 创立时间
     */
    private Date clnfcrttime;

    /**
     * 来源
     */
    private String clnfsource;

    /**
     * 作者
     */
    private String clnfauthor;

    /**
     * 列表图
     */
    private String clnfpic;

    /**
     * 详情图
     */
    private String clnfbigpic;

    /**
     * 简介
     */
    private String clnfintroduce;

    /**
     * 上首页0否1是
     */
    private Integer clnfghp;

    /**
     * 上首页排序
     */
    private Integer clnfidx;

    /**
     * 改变状态操作时间
     */
    private Date clnfopttime;

    /**
     * 状态0.1未审2审核3发布
     */
    private Integer clnfstata;

    /**
     * 栏目类型
     */
    private String clnftype;

    /**
     * 访问量
     */
    private Integer clnfbrowse;

    /**
     * 所属文化馆标识
     */
    private String clnvenueid;

    /**
     * 栏目关键字
     */
    private String clnfkey;

    /**
     * 置顶：0不置顶，1置顶
     */
    private Integer totop;

    /**
     * 是否上首页（0、否 1、是）上首页用当前字段
     */
    private Integer upindex;

    /**
     * 发布系统标识：内部供需，外部供需，非遗。。。
     */
    private String toproject;

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
     * 所属部门
     */
    private String deptid;

    /**
     * 审核人标识
     */
    private String checkor;

    /**
     * 发布人标识
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
     * 创建人标识
     */
    private String crtuser;

    /**
     * 2、提交为编辑1、提交为发布
     */
    private Integer ispub;

    /**
     * 删除状态 0：未删除 1： 删除
     */
    private Integer delstate;

    /**
     * 详细内容
     */
    private String clnfdetail;

    /**
     * 获取栏目内容id
     *
     * @return clnfid - 栏目内容id
     */
    public String getClnfid() {
        return clnfid;
    }

    /**
     * 设置栏目内容id
     *
     * @param clnfid 栏目内容id
     */
    public void setClnfid(String clnfid) {
        this.clnfid = clnfid == null ? null : clnfid.trim();
    }

    /**
     * 获取标题
     *
     * @return clnftltle - 标题
     */
    public String getClnftltle() {
        return clnftltle;
    }

    /**
     * 设置标题
     *
     * @param clnftltle 标题
     */
    public void setClnftltle(String clnftltle) {
        this.clnftltle = clnftltle == null ? null : clnftltle.trim();
    }

    /**
     * 获取创立时间
     *
     * @return clnfcrttime - 创立时间
     */
    public Date getClnfcrttime() {
        return clnfcrttime;
    }

    /**
     * 设置创立时间
     *
     * @param clnfcrttime 创立时间
     */
    public void setClnfcrttime(Date clnfcrttime) {
        this.clnfcrttime = clnfcrttime;
    }

    /**
     * 获取来源
     *
     * @return clnfsource - 来源
     */
    public String getClnfsource() {
        return clnfsource;
    }

    /**
     * 设置来源
     *
     * @param clnfsource 来源
     */
    public void setClnfsource(String clnfsource) {
        this.clnfsource = clnfsource == null ? null : clnfsource.trim();
    }

    /**
     * 获取作者
     *
     * @return clnfauthor - 作者
     */
    public String getClnfauthor() {
        return clnfauthor;
    }

    /**
     * 设置作者
     *
     * @param clnfauthor 作者
     */
    public void setClnfauthor(String clnfauthor) {
        this.clnfauthor = clnfauthor == null ? null : clnfauthor.trim();
    }

    /**
     * 获取列表图
     *
     * @return clnfpic - 列表图
     */
    public String getClnfpic() {
        return clnfpic;
    }

    /**
     * 设置列表图
     *
     * @param clnfpic 列表图
     */
    public void setClnfpic(String clnfpic) {
        this.clnfpic = clnfpic == null ? null : clnfpic.trim();
    }

    /**
     * 获取详情图
     *
     * @return clnfbigpic - 详情图
     */
    public String getClnfbigpic() {
        return clnfbigpic;
    }

    /**
     * 设置详情图
     *
     * @param clnfbigpic 详情图
     */
    public void setClnfbigpic(String clnfbigpic) {
        this.clnfbigpic = clnfbigpic == null ? null : clnfbigpic.trim();
    }

    /**
     * 获取简介
     *
     * @return clnfintroduce - 简介
     */
    public String getClnfintroduce() {
        return clnfintroduce;
    }

    /**
     * 设置简介
     *
     * @param clnfintroduce 简介
     */
    public void setClnfintroduce(String clnfintroduce) {
        this.clnfintroduce = clnfintroduce == null ? null : clnfintroduce.trim();
    }

    /**
     * 获取上首页0否1是
     *
     * @return clnfghp - 上首页0否1是
     */
    public Integer getClnfghp() {
        return clnfghp;
    }

    /**
     * 设置上首页0否1是
     *
     * @param clnfghp 上首页0否1是
     */
    public void setClnfghp(Integer clnfghp) {
        this.clnfghp = clnfghp;
    }

    /**
     * 获取上首页排序
     *
     * @return clnfidx - 上首页排序
     */
    public Integer getClnfidx() {
        return clnfidx;
    }

    /**
     * 设置上首页排序
     *
     * @param clnfidx 上首页排序
     */
    public void setClnfidx(Integer clnfidx) {
        this.clnfidx = clnfidx;
    }

    /**
     * 获取改变状态操作时间
     *
     * @return clnfopttime - 改变状态操作时间
     */
    public Date getClnfopttime() {
        return clnfopttime;
    }

    /**
     * 设置改变状态操作时间
     *
     * @param clnfopttime 改变状态操作时间
     */
    public void setClnfopttime(Date clnfopttime) {
        this.clnfopttime = clnfopttime;
    }

    /**
     * 获取状态0.1未审2审核3发布
     *
     * @return clnfstata - 状态0.1未审2审核3发布
     */
    public Integer getClnfstata() {
        return clnfstata;
    }

    /**
     * 设置状态0.1未审2审核3发布
     *
     * @param clnfstata 状态0.1未审2审核3发布
     */
    public void setClnfstata(Integer clnfstata) {
        this.clnfstata = clnfstata;
    }

    /**
     * 获取栏目类型
     *
     * @return clnftype - 栏目类型
     */
    public String getClnftype() {
        return clnftype;
    }

    /**
     * 设置栏目类型
     *
     * @param clnftype 栏目类型
     */
    public void setClnftype(String clnftype) {
        this.clnftype = clnftype == null ? null : clnftype.trim();
    }

    /**
     * 获取访问量
     *
     * @return clnfbrowse - 访问量
     */
    public Integer getClnfbrowse() {
        return clnfbrowse;
    }

    /**
     * 设置访问量
     *
     * @param clnfbrowse 访问量
     */
    public void setClnfbrowse(Integer clnfbrowse) {
        this.clnfbrowse = clnfbrowse;
    }

    /**
     * 获取所属文化馆标识
     *
     * @return clnvenueid - 所属文化馆标识
     */
    public String getClnvenueid() {
        return clnvenueid;
    }

    /**
     * 设置所属文化馆标识
     *
     * @param clnvenueid 所属文化馆标识
     */
    public void setClnvenueid(String clnvenueid) {
        this.clnvenueid = clnvenueid == null ? null : clnvenueid.trim();
    }

    /**
     * 获取栏目关键字
     *
     * @return clnfkey - 栏目关键字
     */
    public String getClnfkey() {
        return clnfkey;
    }

    /**
     * 设置栏目关键字
     *
     * @param clnfkey 栏目关键字
     */
    public void setClnfkey(String clnfkey) {
        this.clnfkey = clnfkey == null ? null : clnfkey.trim();
    }

    /**
     * 获取置顶：0不置顶，1置顶
     *
     * @return totop - 置顶：0不置顶，1置顶
     */
    public Integer getTotop() {
        return totop;
    }

    /**
     * 设置置顶：0不置顶，1置顶
     *
     * @param totop 置顶：0不置顶，1置顶
     */
    public void setTotop(Integer totop) {
        this.totop = totop;
    }

    /**
     * 获取是否上首页（0、否 1、是）上首页用当前字段
     *
     * @return upindex - 是否上首页（0、否 1、是）上首页用当前字段
     */
    public Integer getUpindex() {
        return upindex;
    }

    /**
     * 设置是否上首页（0、否 1、是）上首页用当前字段
     *
     * @param upindex 是否上首页（0、否 1、是）上首页用当前字段
     */
    public void setUpindex(Integer upindex) {
        this.upindex = upindex;
    }

    /**
     * 获取发布系统标识：内部供需，外部供需，非遗。。。
     *
     * @return toproject - 发布系统标识：内部供需，外部供需，非遗。。。
     */
    public String getToproject() {
        return toproject;
    }

    /**
     * 设置发布系统标识：内部供需，外部供需，非遗。。。
     *
     * @param toproject 发布系统标识：内部供需，外部供需，非遗。。。
     */
    public void setToproject(String toproject) {
        this.toproject = toproject == null ? null : toproject.trim();
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
     * 获取所属部门
     *
     * @return deptid - 所属部门
     */
    public String getDeptid() {
        return deptid;
    }

    /**
     * 设置所属部门
     *
     * @param deptid 所属部门
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
     * 获取2、提交为编辑1、提交为发布
     *
     * @return ispub - 2、提交为编辑1、提交为发布
     */
    public Integer getIspub() {
        return ispub;
    }

    /**
     * 设置2、提交为编辑1、提交为发布
     *
     * @param ispub 2、提交为编辑1、提交为发布
     */
    public void setIspub(Integer ispub) {
        this.ispub = ispub;
    }

    /**
     * 获取删除状态 0：未删除 1： 删除
     *
     * @return delstate - 删除状态 0：未删除 1： 删除
     */
    public Integer getDelstate() {
        return delstate;
    }

    /**
     * 设置删除状态 0：未删除 1： 删除
     *
     * @param delstate 删除状态 0：未删除 1： 删除
     */
    public void setDelstate(Integer delstate) {
        this.delstate = delstate;
    }

    /**
     * 获取详细内容
     *
     * @return clnfdetail - 详细内容
     */
    public String getClnfdetail() {
        return clnfdetail;
    }

    /**
     * 设置详细内容
     *
     * @param clnfdetail 详细内容
     */
    public void setClnfdetail(String clnfdetail) {
        this.clnfdetail = clnfdetail == null ? null : clnfdetail.trim();
    }
}