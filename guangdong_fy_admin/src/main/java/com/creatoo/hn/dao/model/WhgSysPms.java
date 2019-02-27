package com.creatoo.hn.dao.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_sys_pms")
public class WhgSysPms {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 状态. （参考开关状态枚举）
     */
    private Integer state;

    /**
     * 修改状态的时间
     */
    private Date statemdfdate;

    /**
     * 修改状态的用户
     */
    private String statemdfuser;

    /**
     * 描述
     */
    private String descri;

    /**
     * 权限编码
     */
    private String pmscode;

    /**
     * 名称
     */
    private String name;

    /**
     * 关联角色ID
     */
    private String roleid;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public String getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取状态. （参考开关状态枚举）
     *
     * @return state - 状态. （参考开关状态枚举）
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态. （参考开关状态枚举）
     *
     * @param state 状态. （参考开关状态枚举）
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取修改状态的时间
     *
     * @return statemdfdate - 修改状态的时间
     */
    public Date getStatemdfdate() {
        return statemdfdate;
    }

    /**
     * 设置修改状态的时间
     *
     * @param statemdfdate 修改状态的时间
     */
    public void setStatemdfdate(Date statemdfdate) {
        this.statemdfdate = statemdfdate;
    }

    /**
     * 获取修改状态的用户
     *
     * @return statemdfuser - 修改状态的用户
     */
    public String getStatemdfuser() {
        return statemdfuser;
    }

    /**
     * 设置修改状态的用户
     *
     * @param statemdfuser 修改状态的用户
     */
    public void setStatemdfuser(String statemdfuser) {
        this.statemdfuser = statemdfuser == null ? null : statemdfuser.trim();
    }

    /**
     * 获取描述
     *
     * @return descri - 描述
     */
    public String getDescri() {
        return descri;
    }

    /**
     * 设置描述
     *
     * @param descri 描述
     */
    public void setDescri(String descri) {
        this.descri = descri == null ? null : descri.trim();
    }

    /**
     * 获取权限编码
     *
     * @return pmscode - 权限编码
     */
    public String getPmscode() {
        return pmscode;
    }

    /**
     * 设置权限编码
     *
     * @param pmscode 权限编码
     */
    public void setPmscode(String pmscode) {
        this.pmscode = pmscode == null ? null : pmscode.trim();
    }

    /**
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取关联角色ID
     *
     * @return roleid - 关联角色ID
     */
    public String getRoleid() {
        return roleid;
    }

    /**
     * 设置关联角色ID
     *
     * @param roleid 关联角色ID
     */
    public void setRoleid(String roleid) {
        this.roleid = roleid == null ? null : roleid.trim();
    }
}