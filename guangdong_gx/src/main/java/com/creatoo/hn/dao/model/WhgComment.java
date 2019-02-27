package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_comment")
public class WhgComment {
    /**
     * 点评id
     */
    @Id
    private String rmid;

    /**
     * 用户id
     */
    private String rmuid;

    /**
     * 评论时间
     */
    private Date rmdate;

    /**
     * 评论信息
     */
    private String rmcontent;

    /**
     * 回复类型 0:内部回复 1:公开回复
     */
    private Integer rmpltype;

    /**
     * 点评对象的标题
     */
    private String rmtitle;

    /**
     * 点评对象的连接
     */
    private String rmurl;

    /**
     * 1:(活动)2:(培训)3:(培训师资)4:培训点播5:志愿培训6:场馆评论7:场馆-活动室8:文化遗产9:重点文物10:文化人才11.特色资源12市民风采13直播14文化团队15影院
     */
    private String rmreftyp;

    /**
     * 评论关联类型id
     */
    private String rmrefid;

    /**
     * 0评论,1回复
     */
    private Integer rmtyp;

    /**
     * 评论状态：0:待审核，1:审核通过，2：审核不通过
     */
    private Integer rmstate;

    /**
     * 所属文化馆标识
     */
    private String rmvenueid;

    /**
     * 获取点评id
     *
     * @return rmid - 点评id
     */
    public String getRmid() {
        return rmid;
    }

    /**
     * 设置点评id
     *
     * @param rmid 点评id
     */
    public void setRmid(String rmid) {
        this.rmid = rmid == null ? null : rmid.trim();
    }

    /**
     * 获取用户id
     *
     * @return rmuid - 用户id
     */
    public String getRmuid() {
        return rmuid;
    }

    /**
     * 设置用户id
     *
     * @param rmuid 用户id
     */
    public void setRmuid(String rmuid) {
        this.rmuid = rmuid == null ? null : rmuid.trim();
    }

    /**
     * 获取评论时间
     *
     * @return rmdate - 评论时间
     */
    public Date getRmdate() {
        return rmdate;
    }

    /**
     * 设置评论时间
     *
     * @param rmdate 评论时间
     */
    public void setRmdate(Date rmdate) {
        this.rmdate = rmdate;
    }

    /**
     * 获取评论信息
     *
     * @return rmcontent - 评论信息
     */
    public String getRmcontent() {
        return rmcontent;
    }

    /**
     * 设置评论信息
     *
     * @param rmcontent 评论信息
     */
    public void setRmcontent(String rmcontent) {
        this.rmcontent = rmcontent == null ? null : rmcontent.trim();
    }

    /**
     * 获取回复类型 0:内部回复 1:公开回复
     *
     * @return rmpltype - 回复类型 0:内部回复 1:公开回复
     */
    public Integer getRmpltype() {
        return rmpltype;
    }

    /**
     * 设置回复类型 0:内部回复 1:公开回复
     *
     * @param rmpltype 回复类型 0:内部回复 1:公开回复
     */
    public void setRmpltype(Integer rmpltype) {
        this.rmpltype = rmpltype;
    }

    /**
     * 获取点评对象的标题
     *
     * @return rmtitle - 点评对象的标题
     */
    public String getRmtitle() {
        return rmtitle;
    }

    /**
     * 设置点评对象的标题
     *
     * @param rmtitle 点评对象的标题
     */
    public void setRmtitle(String rmtitle) {
        this.rmtitle = rmtitle == null ? null : rmtitle.trim();
    }

    /**
     * 获取点评对象的连接
     *
     * @return rmurl - 点评对象的连接
     */
    public String getRmurl() {
        return rmurl;
    }

    /**
     * 设置点评对象的连接
     *
     * @param rmurl 点评对象的连接
     */
    public void setRmurl(String rmurl) {
        this.rmurl = rmurl == null ? null : rmurl.trim();
    }

    /**
     * 获取1:(活动)2:(培训)3:(培训师资)4:培训点播5:志愿培训6:场馆评论7:场馆-活动室8:文化遗产9:重点文物10:文化人才11.特色资源12市民风采13直播14文化团队15影院
     *
     * @return rmreftyp - 1:(活动)2:(培训)3:(培训师资)4:培训点播5:志愿培训6:场馆评论7:场馆-活动室8:文化遗产9:重点文物10:文化人才11.特色资源12市民风采13直播14文化团队15影院
     */
    public String getRmreftyp() {
        return rmreftyp;
    }

    /**
     * 设置1:(活动)2:(培训)3:(培训师资)4:培训点播5:志愿培训6:场馆评论7:场馆-活动室8:文化遗产9:重点文物10:文化人才11.特色资源12市民风采13直播14文化团队15影院
     *
     * @param rmreftyp 1:(活动)2:(培训)3:(培训师资)4:培训点播5:志愿培训6:场馆评论7:场馆-活动室8:文化遗产9:重点文物10:文化人才11.特色资源12市民风采13直播14文化团队15影院
     */
    public void setRmreftyp(String rmreftyp) {
        this.rmreftyp = rmreftyp == null ? null : rmreftyp.trim();
    }

    /**
     * 获取评论关联类型id
     *
     * @return rmrefid - 评论关联类型id
     */
    public String getRmrefid() {
        return rmrefid;
    }

    /**
     * 设置评论关联类型id
     *
     * @param rmrefid 评论关联类型id
     */
    public void setRmrefid(String rmrefid) {
        this.rmrefid = rmrefid == null ? null : rmrefid.trim();
    }

    /**
     * 获取0评论,1回复
     *
     * @return rmtyp - 0评论,1回复
     */
    public Integer getRmtyp() {
        return rmtyp;
    }

    /**
     * 设置0评论,1回复
     *
     * @param rmtyp 0评论,1回复
     */
    public void setRmtyp(Integer rmtyp) {
        this.rmtyp = rmtyp;
    }

    /**
     * 获取评论状态：0:待审核，1:审核通过，2：审核不通过
     *
     * @return rmstate - 评论状态：0:待审核，1:审核通过，2：审核不通过
     */
    public Integer getRmstate() {
        return rmstate;
    }

    /**
     * 设置评论状态：0:待审核，1:审核通过，2：审核不通过
     *
     * @param rmstate 评论状态：0:待审核，1:审核通过，2：审核不通过
     */
    public void setRmstate(Integer rmstate) {
        this.rmstate = rmstate;
    }

    /**
     * 获取所属文化馆标识
     *
     * @return rmvenueid - 所属文化馆标识
     */
    public String getRmvenueid() {
        return rmvenueid;
    }

    /**
     * 设置所属文化馆标识
     *
     * @param rmvenueid 所属文化馆标识
     */
    public void setRmvenueid(String rmvenueid) {
        this.rmvenueid = rmvenueid == null ? null : rmvenueid.trim();
    }
}