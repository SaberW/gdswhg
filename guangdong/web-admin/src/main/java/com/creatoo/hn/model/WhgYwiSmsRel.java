package com.creatoo.hn.model;

import javax.persistence.*;

@Table(name = "whg_ywi_sms_rel")
public class WhgYwiSmsRel {
    /**
     * 发送短信的标识。同whg_ywi_sms主键
     */
    @Id
    @Column(name = "sl_id")
    private String slId;

    /**
     * 关联类型。1-活动, 2-培训, 3-活动室, 4-用户, 9-其它
     */
    @Column(name = "sl_reltype")
    private Integer slReltype;

    /**
     * 关联对象的ID。
     */
    @Column(name = "sl_relid")
    private String slRelid;

    /**
     * 发送短信时的短信模板编码
     */
    @Column(name = "sl_smscode")
    private String slSmscode;

    /**
     * 短信通道返回的标识
     */
    @Column(name = "sl_thirdid")
    private String slThirdid;

    /**
     * 获取发送短信的标识。同whg_ywi_sms主键
     *
     * @return sl_id - 发送短信的标识。同whg_ywi_sms主键
     */
    public String getSlId() {
        return slId;
    }

    /**
     * 设置发送短信的标识。同whg_ywi_sms主键
     *
     * @param slId 发送短信的标识。同whg_ywi_sms主键
     */
    public void setSlId(String slId) {
        this.slId = slId;
    }

    /**
     * 获取关联类型。1-活动, 2-培训, 3-活动室, 4-用户, 9-其它
     *
     * @return sl_reltype - 关联类型。1-活动, 2-培训, 3-活动室, 4-用户, 9-其它
     */
    public Integer getSlReltype() {
        return slReltype;
    }

    /**
     * 设置关联类型。1-活动, 2-培训, 3-活动室, 4-用户, 9-其它
     *
     * @param slReltype 关联类型。1-活动, 2-培训, 3-活动室, 4-用户, 9-其它
     */
    public void setSlReltype(Integer slReltype) {
        this.slReltype = slReltype;
    }

    /**
     * 获取关联对象的ID。
     *
     * @return sl_relid - 关联对象的ID。
     */
    public String getSlRelid() {
        return slRelid;
    }

    /**
     * 设置关联对象的ID。
     *
     * @param slRelid 关联对象的ID。
     */
    public void setSlRelid(String slRelid) {
        this.slRelid = slRelid;
    }

    /**
     * 获取发送短信时的短信模板编码
     *
     * @return sl_smscode - 发送短信时的短信模板编码
     */
    public String getSlSmscode() {
        return slSmscode;
    }

    /**
     * 设置发送短信时的短信模板编码
     *
     * @param slSmscode 发送短信时的短信模板编码
     */
    public void setSlSmscode(String slSmscode) {
        this.slSmscode = slSmscode;
    }

    /**
     * 获取短信通道返回的标识
     *
     * @return sl_thirdid - 短信通道返回的标识
     */
    public String getSlThirdid() {
        return slThirdid;
    }

    /**
     * 设置短信通道返回的标识
     *
     * @param slThirdid 短信通道返回的标识
     */
    public void setSlThirdid(String slThirdid) {
        this.slThirdid = slThirdid;
    }
}