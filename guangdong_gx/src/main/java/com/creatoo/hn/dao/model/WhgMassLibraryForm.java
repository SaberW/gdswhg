package com.creatoo.hn.dao.model;

import javax.persistence.*;

@Table(name = "whg_mass_library_form")
public class WhgMassLibraryForm {
    /**
     * 资源库表单标识
     */
    @Id
    private String id;

    /**
     * 资源库标识
     */
    private String libid;

    /**
     * 表单的第几行
     */
    private Integer rows;

    /**
     * 表单第rows行是几列布局
     */
    private Integer columns;

    /**
     * 总列数
     */
    private Integer totalcolumns;

    /**
     * 第rows行的第一列中label文字
     */
    private String labelname;

    /**
     * 列类型。0-普通，1-组合多字段，2-组合多字段多行
     */
    private Integer columntype;

    /**
     * 一列中的字段个数
     */
    private Integer size;

    /**
     * 是否临时数据,0-否， 1-是
     */
    private Integer istemp;

    /**
     * 创建管理员标识
     */
    private String crtuser;

    /**
     * 获取资源库表单标识
     *
     * @return id - 资源库表单标识
     */
    public String getId() {
        return id;
    }

    /**
     * 设置资源库表单标识
     *
     * @param id 资源库表单标识
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取资源库标识
     *
     * @return libid - 资源库标识
     */
    public String getLibid() {
        return libid;
    }

    /**
     * 设置资源库标识
     *
     * @param libid 资源库标识
     */
    public void setLibid(String libid) {
        this.libid = libid == null ? null : libid.trim();
    }

    /**
     * 获取表单的第几行
     *
     * @return rows - 表单的第几行
     */
    public Integer getRows() {
        return rows;
    }

    /**
     * 设置表单的第几行
     *
     * @param rows 表单的第几行
     */
    public void setRows(Integer rows) {
        this.rows = rows;
    }

    /**
     * 获取表单第rows行是几列布局
     *
     * @return columns - 表单第rows行是几列布局
     */
    public Integer getColumns() {
        return columns;
    }

    /**
     * 设置表单第rows行是几列布局
     *
     * @param columns 表单第rows行是几列布局
     */
    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    /**
     * 获取总列数
     *
     * @return totalcolumns - 总列数
     */
    public Integer getTotalcolumns() {
        return totalcolumns;
    }

    /**
     * 设置总列数
     *
     * @param totalcolumns 总列数
     */
    public void setTotalcolumns(Integer totalcolumns) {
        this.totalcolumns = totalcolumns;
    }

    /**
     * 获取第rows行的第一列中label文字
     *
     * @return labelname - 第rows行的第一列中label文字
     */
    public String getLabelname() {
        return labelname;
    }

    /**
     * 设置第rows行的第一列中label文字
     *
     * @param labelname 第rows行的第一列中label文字
     */
    public void setLabelname(String labelname) {
        this.labelname = labelname == null ? null : labelname.trim();
    }

    /**
     * 获取列类型。0-普通，1-组合多字段，2-组合多字段多行
     *
     * @return columntype - 列类型。0-普通，1-组合多字段，2-组合多字段多行
     */
    public Integer getColumntype() {
        return columntype;
    }

    /**
     * 设置列类型。0-普通，1-组合多字段，2-组合多字段多行
     *
     * @param columntype 列类型。0-普通，1-组合多字段，2-组合多字段多行
     */
    public void setColumntype(Integer columntype) {
        this.columntype = columntype;
    }

    /**
     * 获取一列中的字段个数
     *
     * @return size - 一列中的字段个数
     */
    public Integer getSize() {
        return size;
    }

    /**
     * 设置一列中的字段个数
     *
     * @param size 一列中的字段个数
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * 获取是否临时数据,0-否， 1-是
     *
     * @return istemp - 是否临时数据,0-否， 1-是
     */
    public Integer getIstemp() {
        return istemp;
    }

    /**
     * 设置是否临时数据,0-否， 1-是
     *
     * @param istemp 是否临时数据,0-否， 1-是
     */
    public void setIstemp(Integer istemp) {
        this.istemp = istemp;
    }

    /**
     * 获取创建管理员标识
     *
     * @return crtuser - 创建管理员标识
     */
    public String getCrtuser() {
        return crtuser;
    }

    /**
     * 设置创建管理员标识
     *
     * @param crtuser 创建管理员标识
     */
    public void setCrtuser(String crtuser) {
        this.crtuser = crtuser == null ? null : crtuser.trim();
    }
}