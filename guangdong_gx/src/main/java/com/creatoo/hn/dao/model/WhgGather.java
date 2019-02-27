package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_gather")
public class WhgGather {
    /**
     * PK
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
     * 状态：1可编辑，9待审核，2待发布，6已发布，4已下架
     */
    private Integer state;

    /**
     * 状态更时间
     */
    private Date statemdfdate;

    /**
     * 状态变更人
     */
    private String statemdfuser;

    /**
     * 删除状态：0未删除，1已删除
     */
    private Integer delstate;

    /**
     * 所属文化馆
     */
    private String cultid;

    /**
     * 所属部门
     */
    private String deptid;

    /**
     * 是否推荐：0否1是
     */
    private Integer recommend;

    /**
     * 分类:0其它，4活动，5培训
     */
    private String etype;

    /**
     * 标签
     */
    private String etag;

    /**
     * 关键字
     */
    private String ekey;

    /**
     * 众筹品牌
     */
    private String brandid;

    /**
     * 众筹名称
     */
    private String title;

    /**
     * 众筹开始时间
     */
    private Date timestart;

    /**
     * 众筹结束时间
     */
    private Date timeend;

    /**
     * 总份数
     */
    private Integer numsum;

    /**
     * 达成份数
     */
    private Integer nummin;

    /**
     * 众筹封面
     */
    private String imgurl;

    /**
     * 回报说明
     */
    private String explains;

    /**
     * 众筹须知
     */
    private String notice;

    /**
     * 简介
     */
    private String summary;

    /**
     * 附件
     */
    private String enclosure;

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
     * 类型为活动或培训时的关联ID
     */
    private String refid;

    /**
     * 是否成功：0初始,1成功,2失败
     */
    private Integer issuccess;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 成功失败通知标记：1已通知，其它未通知
     */
    private Integer ismessage;

    /**
     * 通知类型：SMS 短信 ZNX 站内信
     */
    private String noticetype;

    /**
     * 相关活动
     */
    private String relateactid;

    /**
     * 相关培训
     */
    private String relatetraid;

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
     * 众筹单位
     */
    private String company;

    /**
     * 众筹介绍
     */
    private String descriptions;

    /**
     * 获取PK
     *
     * @return id - PK
     */
    public String getId() {
        return id;
    }

    /**
     * 设置PK
     *
     * @param id PK
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
     * 获取状态：1可编辑，9待审核，2待发布，6已发布，4已下架
     *
     * @return state - 状态：1可编辑，9待审核，2待发布，6已发布，4已下架
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态：1可编辑，9待审核，2待发布，6已发布，4已下架
     *
     * @param state 状态：1可编辑，9待审核，2待发布，6已发布，4已下架
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取状态更时间
     *
     * @return statemdfdate - 状态更时间
     */
    public Date getStatemdfdate() {
        return statemdfdate;
    }

    /**
     * 设置状态更时间
     *
     * @param statemdfdate 状态更时间
     */
    public void setStatemdfdate(Date statemdfdate) {
        this.statemdfdate = statemdfdate;
    }

    /**
     * 获取状态变更人
     *
     * @return statemdfuser - 状态变更人
     */
    public String getStatemdfuser() {
        return statemdfuser;
    }

    /**
     * 设置状态变更人
     *
     * @param statemdfuser 状态变更人
     */
    public void setStatemdfuser(String statemdfuser) {
        this.statemdfuser = statemdfuser == null ? null : statemdfuser.trim();
    }

    /**
     * 获取删除状态：0未删除，1已删除
     *
     * @return delstate - 删除状态：0未删除，1已删除
     */
    public Integer getDelstate() {
        return delstate;
    }

    /**
     * 设置删除状态：0未删除，1已删除
     *
     * @param delstate 删除状态：0未删除，1已删除
     */
    public void setDelstate(Integer delstate) {
        this.delstate = delstate;
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
     * 获取是否推荐：0否1是
     *
     * @return recommend - 是否推荐：0否1是
     */
    public Integer getRecommend() {
        return recommend;
    }

    /**
     * 设置是否推荐：0否1是
     *
     * @param recommend 是否推荐：0否1是
     */
    public void setRecommend(Integer recommend) {
        this.recommend = recommend;
    }

    /**
     * 获取分类:0其它，4活动，5培训
     *
     * @return etype - 分类:0其它，4活动，5培训
     */
    public String getEtype() {
        return etype;
    }

    /**
     * 设置分类:0其它，4活动，5培训
     *
     * @param etype 分类:0其它，4活动，5培训
     */
    public void setEtype(String etype) {
        this.etype = etype == null ? null : etype.trim();
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
     * 获取众筹品牌
     *
     * @return brandid - 众筹品牌
     */
    public String getBrandid() {
        return brandid;
    }

    /**
     * 设置众筹品牌
     *
     * @param brandid 众筹品牌
     */
    public void setBrandid(String brandid) {
        this.brandid = brandid == null ? null : brandid.trim();
    }

    /**
     * 获取众筹名称
     *
     * @return title - 众筹名称
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置众筹名称
     *
     * @param title 众筹名称
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取众筹开始时间
     *
     * @return timestart - 众筹开始时间
     */
    public Date getTimestart() {
        return timestart;
    }

    /**
     * 设置众筹开始时间
     *
     * @param timestart 众筹开始时间
     */
    public void setTimestart(Date timestart) {
        this.timestart = timestart;
    }

    /**
     * 获取众筹结束时间
     *
     * @return timeend - 众筹结束时间
     */
    public Date getTimeend() {
        return timeend;
    }

    /**
     * 设置众筹结束时间
     *
     * @param timeend 众筹结束时间
     */
    public void setTimeend(Date timeend) {
        this.timeend = timeend;
    }

    /**
     * 获取总份数
     *
     * @return numsum - 总份数
     */
    public Integer getNumsum() {
        return numsum;
    }

    /**
     * 设置总份数
     *
     * @param numsum 总份数
     */
    public void setNumsum(Integer numsum) {
        this.numsum = numsum;
    }

    /**
     * 获取达成份数
     *
     * @return nummin - 达成份数
     */
    public Integer getNummin() {
        return nummin;
    }

    /**
     * 设置达成份数
     *
     * @param nummin 达成份数
     */
    public void setNummin(Integer nummin) {
        this.nummin = nummin;
    }

    /**
     * 获取众筹封面
     *
     * @return imgurl - 众筹封面
     */
    public String getImgurl() {
        return imgurl;
    }

    /**
     * 设置众筹封面
     *
     * @param imgurl 众筹封面
     */
    public void setImgurl(String imgurl) {
        this.imgurl = imgurl == null ? null : imgurl.trim();
    }

    /**
     * 获取回报说明
     *
     * @return explains - 回报说明
     */
    public String getExplains() {
        return explains;
    }

    /**
     * 设置回报说明
     *
     * @param explains 回报说明
     */
    public void setExplains(String explains) {
        this.explains = explains == null ? null : explains.trim();
    }

    /**
     * 获取众筹须知
     *
     * @return notice - 众筹须知
     */
    public String getNotice() {
        return notice;
    }

    /**
     * 设置众筹须知
     *
     * @param notice 众筹须知
     */
    public void setNotice(String notice) {
        this.notice = notice == null ? null : notice.trim();
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
     * 获取附件
     *
     * @return enclosure - 附件
     */
    public String getEnclosure() {
        return enclosure;
    }

    /**
     * 设置附件
     *
     * @param enclosure 附件
     */
    public void setEnclosure(String enclosure) {
        this.enclosure = enclosure == null ? null : enclosure.trim();
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
     * 获取类型为活动或培训时的关联ID
     *
     * @return refid - 类型为活动或培训时的关联ID
     */
    public String getRefid() {
        return refid;
    }

    /**
     * 设置类型为活动或培训时的关联ID
     *
     * @param refid 类型为活动或培训时的关联ID
     */
    public void setRefid(String refid) {
        this.refid = refid == null ? null : refid.trim();
    }

    /**
     * 获取是否成功：0初始,1成功,2失败
     *
     * @return issuccess - 是否成功：0初始,1成功,2失败
     */
    public Integer getIssuccess() {
        return issuccess;
    }

    /**
     * 设置是否成功：0初始,1成功,2失败
     *
     * @param issuccess 是否成功：0初始,1成功,2失败
     */
    public void setIssuccess(Integer issuccess) {
        this.issuccess = issuccess;
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
     * 获取成功失败通知标记：1已通知，其它未通知
     *
     * @return ismessage - 成功失败通知标记：1已通知，其它未通知
     */
    public Integer getIsmessage() {
        return ismessage;
    }

    /**
     * 设置成功失败通知标记：1已通知，其它未通知
     *
     * @param ismessage 成功失败通知标记：1已通知，其它未通知
     */
    public void setIsmessage(Integer ismessage) {
        this.ismessage = ismessage;
    }

    /**
     * 获取通知类型：SMS 短信 ZNX 站内信
     *
     * @return noticetype - 通知类型：SMS 短信 ZNX 站内信
     */
    public String getNoticetype() {
        return noticetype;
    }

    /**
     * 设置通知类型：SMS 短信 ZNX 站内信
     *
     * @param noticetype 通知类型：SMS 短信 ZNX 站内信
     */
    public void setNoticetype(String noticetype) {
        this.noticetype = noticetype == null ? null : noticetype.trim();
    }

    /**
     * 获取相关活动
     *
     * @return relateactid - 相关活动
     */
    public String getRelateactid() {
        return relateactid;
    }

    /**
     * 设置相关活动
     *
     * @param relateactid 相关活动
     */
    public void setRelateactid(String relateactid) {
        this.relateactid = relateactid == null ? null : relateactid.trim();
    }

    /**
     * 获取相关培训
     *
     * @return relatetraid - 相关培训
     */
    public String getRelatetraid() {
        return relatetraid;
    }

    /**
     * 设置相关培训
     *
     * @param relatetraid 相关培训
     */
    public void setRelatetraid(String relatetraid) {
        this.relatetraid = relatetraid == null ? null : relatetraid.trim();
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
     * 获取众筹单位
     *
     * @return company - 众筹单位
     */
    public String getCompany() {
        return company;
    }

    /**
     * 设置众筹单位
     *
     * @param company 众筹单位
     */
    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }

    /**
     * 获取众筹介绍
     *
     * @return descriptions - 众筹介绍
     */
    public String getDescriptions() {
        return descriptions;
    }

    /**
     * 设置众筹介绍
     *
     * @param descriptions 众筹介绍
     */
    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions == null ? null : descriptions.trim();
    }
}