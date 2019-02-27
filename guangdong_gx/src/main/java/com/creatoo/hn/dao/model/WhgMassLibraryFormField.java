package com.creatoo.hn.dao.model;

import javax.persistence.*;

@Table(name = "whg_mass_library_form_field")
public class WhgMassLibraryFormField {
    /**
     * 字段标识
     */
    @Id
    private String id;

    /**
     * 资源库标识
     */
    private String libid;

    /**
     * 表单标识
     */
    private String formid;

    /**
     * 一个列多个字段时的排序，从0开始
     */
    private Integer fieldidx;

    /**
     * 字段编码
     */
    private String fieldcode;

    /**
     * 字段说明
     */
    private String fieldname;

    /**
     * 字段类型
     */
    private String fieldtype;

    /**
     * 字段是否必填
     */
    private Integer fieldrequired;

    /**
     * 字段默认值
     */
    private String fielddefaultval;

    /**
     * 字段输入提示
     */
    private String fieldprompt;

    /**
     * 字段组件显示宽
     */
    private String fieldwidth;

    /**
     * 字段组件显示高
     */
    private String fieldheight;

    /**
     * 字段组件验证函数：length[0,100]
     */
    private String fieldvalidtype;

    /**
     * 字段允许最小数字
     */
    private Integer fieldminval;

    /**
     * 字段允许最大数字
     */
    private Integer fieldmaxval;

    /**
     * 字段combobox的limitToList属性
     */
    private Integer fieldlimittolist;

    /**
     * 字段combobox的editable属性
     */
    private Integer fieldeditable;

    /**
     * 字段允许出现的值
     */
    private String fieldlistdata;

    /**
     * 字段表单组件前缀字符
     */
    private String fieldprefix;

    /**
     * 字段表单组件后缀字符
     */
    private String fieldsuffix;

    /**
     * 在管理列表中是否显示
     */
    private Integer isshowlist;

    /**
     * 在网站前端是否显示
     */
    private Integer isshowfront;

    /**
     * 创建管理标识
     */
    private String crtuser;

    /**
     * 是否
     */
    private Integer istemp;

    /**
     * 获取字段标识
     *
     * @return id - 字段标识
     */
    public String getId() {
        return id;
    }

    /**
     * 设置字段标识
     *
     * @param id 字段标识
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
     * 获取表单标识
     *
     * @return formid - 表单标识
     */
    public String getFormid() {
        return formid;
    }

    /**
     * 设置表单标识
     *
     * @param formid 表单标识
     */
    public void setFormid(String formid) {
        this.formid = formid == null ? null : formid.trim();
    }

    /**
     * 获取一个列多个字段时的排序，从0开始
     *
     * @return fieldidx - 一个列多个字段时的排序，从0开始
     */
    public Integer getFieldidx() {
        return fieldidx;
    }

    /**
     * 设置一个列多个字段时的排序，从0开始
     *
     * @param fieldidx 一个列多个字段时的排序，从0开始
     */
    public void setFieldidx(Integer fieldidx) {
        this.fieldidx = fieldidx;
    }

    /**
     * 获取字段编码
     *
     * @return fieldcode - 字段编码
     */
    public String getFieldcode() {
        return fieldcode;
    }

    /**
     * 设置字段编码
     *
     * @param fieldcode 字段编码
     */
    public void setFieldcode(String fieldcode) {
        this.fieldcode = fieldcode == null ? null : fieldcode.trim();
    }

    /**
     * 获取字段说明
     *
     * @return fieldname - 字段说明
     */
    public String getFieldname() {
        return fieldname;
    }

    /**
     * 设置字段说明
     *
     * @param fieldname 字段说明
     */
    public void setFieldname(String fieldname) {
        this.fieldname = fieldname == null ? null : fieldname.trim();
    }

    /**
     * 获取字段类型
     *
     * @return fieldtype - 字段类型
     */
    public String getFieldtype() {
        return fieldtype;
    }

    /**
     * 设置字段类型
     *
     * @param fieldtype 字段类型
     */
    public void setFieldtype(String fieldtype) {
        this.fieldtype = fieldtype == null ? null : fieldtype.trim();
    }

    /**
     * 获取字段是否必填
     *
     * @return fieldrequired - 字段是否必填
     */
    public Integer getFieldrequired() {
        return fieldrequired;
    }

    /**
     * 设置字段是否必填
     *
     * @param fieldrequired 字段是否必填
     */
    public void setFieldrequired(Integer fieldrequired) {
        this.fieldrequired = fieldrequired;
    }

    /**
     * 获取字段默认值
     *
     * @return fielddefaultval - 字段默认值
     */
    public String getFielddefaultval() {
        return fielddefaultval;
    }

    /**
     * 设置字段默认值
     *
     * @param fielddefaultval 字段默认值
     */
    public void setFielddefaultval(String fielddefaultval) {
        this.fielddefaultval = fielddefaultval == null ? null : fielddefaultval.trim();
    }

    /**
     * 获取字段输入提示
     *
     * @return fieldprompt - 字段输入提示
     */
    public String getFieldprompt() {
        return fieldprompt;
    }

    /**
     * 设置字段输入提示
     *
     * @param fieldprompt 字段输入提示
     */
    public void setFieldprompt(String fieldprompt) {
        this.fieldprompt = fieldprompt == null ? null : fieldprompt.trim();
    }

    /**
     * 获取字段组件显示宽
     *
     * @return fieldwidth - 字段组件显示宽
     */
    public String getFieldwidth() {
        return fieldwidth;
    }

    /**
     * 设置字段组件显示宽
     *
     * @param fieldwidth 字段组件显示宽
     */
    public void setFieldwidth(String fieldwidth) {
        this.fieldwidth = fieldwidth == null ? null : fieldwidth.trim();
    }

    /**
     * 获取字段组件显示高
     *
     * @return fieldheight - 字段组件显示高
     */
    public String getFieldheight() {
        return fieldheight;
    }

    /**
     * 设置字段组件显示高
     *
     * @param fieldheight 字段组件显示高
     */
    public void setFieldheight(String fieldheight) {
        this.fieldheight = fieldheight == null ? null : fieldheight.trim();
    }

    /**
     * 获取字段组件验证函数：length[0,100]
     *
     * @return fieldvalidtype - 字段组件验证函数：length[0,100]
     */
    public String getFieldvalidtype() {
        return fieldvalidtype;
    }

    /**
     * 设置字段组件验证函数：length[0,100]
     *
     * @param fieldvalidtype 字段组件验证函数：length[0,100]
     */
    public void setFieldvalidtype(String fieldvalidtype) {
        this.fieldvalidtype = fieldvalidtype == null ? null : fieldvalidtype.trim();
    }

    /**
     * 获取字段允许最小数字
     *
     * @return fieldminval - 字段允许最小数字
     */
    public Integer getFieldminval() {
        return fieldminval;
    }

    /**
     * 设置字段允许最小数字
     *
     * @param fieldminval 字段允许最小数字
     */
    public void setFieldminval(Integer fieldminval) {
        this.fieldminval = fieldminval;
    }

    /**
     * 获取字段允许最大数字
     *
     * @return fieldmaxval - 字段允许最大数字
     */
    public Integer getFieldmaxval() {
        return fieldmaxval;
    }

    /**
     * 设置字段允许最大数字
     *
     * @param fieldmaxval 字段允许最大数字
     */
    public void setFieldmaxval(Integer fieldmaxval) {
        this.fieldmaxval = fieldmaxval;
    }

    /**
     * 获取字段combobox的limitToList属性
     *
     * @return fieldlimittolist - 字段combobox的limitToList属性
     */
    public Integer getFieldlimittolist() {
        return fieldlimittolist;
    }

    /**
     * 设置字段combobox的limitToList属性
     *
     * @param fieldlimittolist 字段combobox的limitToList属性
     */
    public void setFieldlimittolist(Integer fieldlimittolist) {
        this.fieldlimittolist = fieldlimittolist;
    }

    /**
     * 获取字段combobox的editable属性
     *
     * @return fieldeditable - 字段combobox的editable属性
     */
    public Integer getFieldeditable() {
        return fieldeditable;
    }

    /**
     * 设置字段combobox的editable属性
     *
     * @param fieldeditable 字段combobox的editable属性
     */
    public void setFieldeditable(Integer fieldeditable) {
        this.fieldeditable = fieldeditable;
    }

    /**
     * 获取字段允许出现的值
     *
     * @return fieldlistdata - 字段允许出现的值
     */
    public String getFieldlistdata() {
        return fieldlistdata;
    }

    /**
     * 设置字段允许出现的值
     *
     * @param fieldlistdata 字段允许出现的值
     */
    public void setFieldlistdata(String fieldlistdata) {
        this.fieldlistdata = fieldlistdata == null ? null : fieldlistdata.trim();
    }

    /**
     * 获取字段表单组件前缀字符
     *
     * @return fieldprefix - 字段表单组件前缀字符
     */
    public String getFieldprefix() {
        return fieldprefix;
    }

    /**
     * 设置字段表单组件前缀字符
     *
     * @param fieldprefix 字段表单组件前缀字符
     */
    public void setFieldprefix(String fieldprefix) {
        this.fieldprefix = fieldprefix == null ? null : fieldprefix.trim();
    }

    /**
     * 获取字段表单组件后缀字符
     *
     * @return fieldsuffix - 字段表单组件后缀字符
     */
    public String getFieldsuffix() {
        return fieldsuffix;
    }

    /**
     * 设置字段表单组件后缀字符
     *
     * @param fieldsuffix 字段表单组件后缀字符
     */
    public void setFieldsuffix(String fieldsuffix) {
        this.fieldsuffix = fieldsuffix == null ? null : fieldsuffix.trim();
    }

    /**
     * 获取在管理列表中是否显示
     *
     * @return isshowlist - 在管理列表中是否显示
     */
    public Integer getIsshowlist() {
        return isshowlist;
    }

    /**
     * 设置在管理列表中是否显示
     *
     * @param isshowlist 在管理列表中是否显示
     */
    public void setIsshowlist(Integer isshowlist) {
        this.isshowlist = isshowlist;
    }

    /**
     * 获取在网站前端是否显示
     *
     * @return isshowfront - 在网站前端是否显示
     */
    public Integer getIsshowfront() {
        return isshowfront;
    }

    /**
     * 设置在网站前端是否显示
     *
     * @param isshowfront 在网站前端是否显示
     */
    public void setIsshowfront(Integer isshowfront) {
        this.isshowfront = isshowfront;
    }

    /**
     * 获取创建管理标识
     *
     * @return crtuser - 创建管理标识
     */
    public String getCrtuser() {
        return crtuser;
    }

    /**
     * 设置创建管理标识
     *
     * @param crtuser 创建管理标识
     */
    public void setCrtuser(String crtuser) {
        this.crtuser = crtuser == null ? null : crtuser.trim();
    }

    /**
     * 获取是否
     *
     * @return istemp - 是否
     */
    public Integer getIstemp() {
        return istemp;
    }

    /**
     * 设置是否
     *
     * @param istemp 是否
     */
    public void setIstemp(Integer istemp) {
        this.istemp = istemp;
    }
}