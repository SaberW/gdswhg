package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_ins_message")
public class WhgInsMessage {
    /**
     * PK
     */
    @Id
    private String id;

    /**
     * 类型：0系统，1私人
     */
    private String type;

    /**
     * 发送人ID
     */
    private String fromuserid;

    /**
     * 接收人ID
     */
    private String touserid;

    /**
     * 发送时间
     */
    private Date sendtime;

    /**
     * 是否已读：0未读，1已读
     */
    private String isread;

    /**
     * 查看时间
     */
    private Date readtime;

    /**
     * 消息相关实体类型
     */
    private String reftype;

    /**
     * 消息相关实体ID
     */
    private String refid;

    /**
     * 所属系统：WBGX，NBGX
     */
    private String toproject;

    /**
     * 消息信息
     */
    private String message;

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
     * 获取类型：0系统，1私人
     *
     * @return type - 类型：0系统，1私人
     */
    public String getType() {
        return type;
    }

    /**
     * 设置类型：0系统，1私人
     *
     * @param type 类型：0系统，1私人
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * 获取发送人ID
     *
     * @return fromuserid - 发送人ID
     */
    public String getFromuserid() {
        return fromuserid;
    }

    /**
     * 设置发送人ID
     *
     * @param fromuserid 发送人ID
     */
    public void setFromuserid(String fromuserid) {
        this.fromuserid = fromuserid == null ? null : fromuserid.trim();
    }

    /**
     * 获取接收人ID
     *
     * @return touserid - 接收人ID
     */
    public String getTouserid() {
        return touserid;
    }

    /**
     * 设置接收人ID
     *
     * @param touserid 接收人ID
     */
    public void setTouserid(String touserid) {
        this.touserid = touserid == null ? null : touserid.trim();
    }

    /**
     * 获取发送时间
     *
     * @return sendtime - 发送时间
     */
    public Date getSendtime() {
        return sendtime;
    }

    /**
     * 设置发送时间
     *
     * @param sendtime 发送时间
     */
    public void setSendtime(Date sendtime) {
        this.sendtime = sendtime;
    }

    /**
     * 获取是否已读：0未读，1已读
     *
     * @return isread - 是否已读：0未读，1已读
     */
    public String getIsread() {
        return isread;
    }

    /**
     * 设置是否已读：0未读，1已读
     *
     * @param isread 是否已读：0未读，1已读
     */
    public void setIsread(String isread) {
        this.isread = isread == null ? null : isread.trim();
    }

    /**
     * 获取查看时间
     *
     * @return readtime - 查看时间
     */
    public Date getReadtime() {
        return readtime;
    }

    /**
     * 设置查看时间
     *
     * @param readtime 查看时间
     */
    public void setReadtime(Date readtime) {
        this.readtime = readtime;
    }

    /**
     * 获取消息相关实体类型
     *
     * @return reftype - 消息相关实体类型
     */
    public String getReftype() {
        return reftype;
    }

    /**
     * 设置消息相关实体类型
     *
     * @param reftype 消息相关实体类型
     */
    public void setReftype(String reftype) {
        this.reftype = reftype == null ? null : reftype.trim();
    }

    /**
     * 获取消息相关实体ID
     *
     * @return refid - 消息相关实体ID
     */
    public String getRefid() {
        return refid;
    }

    /**
     * 设置消息相关实体ID
     *
     * @param refid 消息相关实体ID
     */
    public void setRefid(String refid) {
        this.refid = refid == null ? null : refid.trim();
    }

    /**
     * 获取所属系统：WBGX，NBGX
     *
     * @return toproject - 所属系统：WBGX，NBGX
     */
    public String getToproject() {
        return toproject;
    }

    /**
     * 设置所属系统：WBGX，NBGX
     *
     * @param toproject 所属系统：WBGX，NBGX
     */
    public void setToproject(String toproject) {
        this.toproject = toproject == null ? null : toproject.trim();
    }

    /**
     * 获取消息信息
     *
     * @return message - 消息信息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置消息信息
     *
     * @param message 消息信息
     */
    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }
}