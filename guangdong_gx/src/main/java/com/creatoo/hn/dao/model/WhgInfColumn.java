package com.creatoo.hn.dao.model;

import javax.persistence.*;

@Table(name = "whg_inf_column")
public class WhgInfColumn {
    @Id
    private String colid;

    /**
     * 栏目标题
     */
    private String coltitle;

    /**
     * 父类型
     */
    private String colpid;

    /**
     * 排序
     */
    private Integer colidx;

    /**
     * 状态0否 1是
     */
    private Integer colstate;

    /**
     * 图片
     */
    private String colpic;

    /**
     * 所属文化馆标识
     */
    private String colvenueid;

    /**
     * 发布系统标识：内部供需，外部供需，非遗等。。。
     */
    private String toproject;

    /**
     * 前端按钮
     */
    private String frontmenu;

    /**
     * @return colid
     */
    public String getColid() {
        return colid;
    }

    /**
     * @param colid
     */
    public void setColid(String colid) {
        this.colid = colid == null ? null : colid.trim();
    }

    /**
     * 获取栏目标题
     *
     * @return coltitle - 栏目标题
     */
    public String getColtitle() {
        return coltitle;
    }

    /**
     * 设置栏目标题
     *
     * @param coltitle 栏目标题
     */
    public void setColtitle(String coltitle) {
        this.coltitle = coltitle == null ? null : coltitle.trim();
    }

    /**
     * 获取父类型
     *
     * @return colpid - 父类型
     */
    public String getColpid() {
        return colpid;
    }

    /**
     * 设置父类型
     *
     * @param colpid 父类型
     */
    public void setColpid(String colpid) {
        this.colpid = colpid == null ? null : colpid.trim();
    }

    /**
     * 获取排序
     *
     * @return colidx - 排序
     */
    public Integer getColidx() {
        return colidx;
    }

    /**
     * 设置排序
     *
     * @param colidx 排序
     */
    public void setColidx(Integer colidx) {
        this.colidx = colidx;
    }

    /**
     * 获取状态0否 1是
     *
     * @return colstate - 状态0否 1是
     */
    public Integer getColstate() {
        return colstate;
    }

    /**
     * 设置状态0否 1是
     *
     * @param colstate 状态0否 1是
     */
    public void setColstate(Integer colstate) {
        this.colstate = colstate;
    }

    /**
     * 获取图片
     *
     * @return colpic - 图片
     */
    public String getColpic() {
        return colpic;
    }

    /**
     * 设置图片
     *
     * @param colpic 图片
     */
    public void setColpic(String colpic) {
        this.colpic = colpic == null ? null : colpic.trim();
    }

    /**
     * 获取所属文化馆标识
     *
     * @return colvenueid - 所属文化馆标识
     */
    public String getColvenueid() {
        return colvenueid;
    }

    /**
     * 设置所属文化馆标识
     *
     * @param colvenueid 所属文化馆标识
     */
    public void setColvenueid(String colvenueid) {
        this.colvenueid = colvenueid == null ? null : colvenueid.trim();
    }

    /**
     * 获取发布系统标识：内部供需，外部供需，非遗等。。。
     *
     * @return toproject - 发布系统标识：内部供需，外部供需，非遗等。。。
     */
    public String getToproject() {
        return toproject;
    }

    /**
     * 设置发布系统标识：内部供需，外部供需，非遗等。。。
     *
     * @param toproject 发布系统标识：内部供需，外部供需，非遗等。。。
     */
    public void setToproject(String toproject) {
        this.toproject = toproject == null ? null : toproject.trim();
    }

    /**
     * 获取前端按钮
     *
     * @return frontmenu - 前端按钮
     */
    public String getFrontmenu() {
        return frontmenu;
    }

    /**
     * 设置前端按钮
     *
     * @param frontmenu 前端按钮
     */
    public void setFrontmenu(String frontmenu) {
        this.frontmenu = frontmenu == null ? null : frontmenu.trim();
    }
}