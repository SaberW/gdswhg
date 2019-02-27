<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>群文资源库-编辑</title>
    <!-- 编辑表单 -->
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap-theme.min.css"/>
    <%--<script type="text/javascript" src="${basePath}/static/bootstrap/js/bootstrap.min.js"></script>--%>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-file.js"></script>
    <!-- 图片上传相关-END -->
    <script src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
    <script type="text/javascript" src="${basePath}/static/admin/js/mass-library-custom-form.js"></script>
    <script type="text/javascript" src="${basePath}/static/admin/js/mass-library-custom-field.js"></script>
    <script type="text/javascript" src="${basePath}/static/admin/js/mass-library-custom-comm.js"></script>

    <script type="text/javascript" src="${basePath}/static/common/js/area.js"></script>
    <script type="text/javascript" src="${basePath}/static/common/js/whg.maps.js"></script>

    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>
    <link rel="stylesheet" href="${basePath}/static/Font-Awesome/css/font-awesome.min.css"/>

    <style>
        div.opreation{ margin: 5px; display: block; }
        div.form-row{ border: 1px dashed lightgrey; padding: 10px 0; position: relative; }
        div.row-opreation{ position: absolute; float: left; top:0; left: 0; }
        i{ color: red; font-weight: bold; }
        /*.checkbox label, .radio label{padding-left:5px!important;}
        .checkbox label::before {
            content: "";
            display: inline-block;
            position: absolute;
            width: 17px;
            height: 17px;
            left: 0;
            margin-left: -20px;
            border: 1px solid #cccccc;
            border-radius: 3px;
            background-color: #fff;
            -webkit-transition: border 0.15s ease-in-out, color 0.15s ease-in-out;
            -o-transition: border 0.15s ease-in-out, color 0.15s ease-in-out;
            transition: border 0.15s ease-in-out, color 0.15s ease-in-out;
        }
        .checkbox input[type="checkbox"]:checked + label::after, .checkbox input[type="radio"]:checked + label::after {
            font-family: "FontAwesome";
            content: "\f00c";
        }
        .checkbox label::after {
            display: inline-block;
            position: absolute;
            width: 16px;
            height: 16px;
            left: 0;
            top: 0;
            margin-left: -20px;
            padding-left: 3px;
            padding-top: 1px;
            font-size: 11px;
            color: #555555;
        }
        input[type="checkbox"].styled:checked + label:after, input[type="radio"].styled:checked + label:after {
            font-family: 'FontAwesome';
            content: "\f00c";
        }
        .checkbox-primary input[type="checkbox"]:checked + label::after, .checkbox-primary input[type="radio"]:checked + label::after {
            color: #000;
        }
        .checkbox-primary input[type="checkbox"]:checked + label::after, .checkbox-primary input[type="radio"]:checked + label::after {
            color: #fff;
        }
        .checkbox label {
            display: inline-block;
            vertical-align: middle;
            position: relative;
            padding-left: 5px;
        }
        .checkbox-primary input[type="checkbox"]:checked + label::before, .checkbox-primary input[type="radio"]:checked + label::before {
            background-color: #337ab7;
            border-color: #337ab7;
        }
        .checkbox input[type="checkbox"], .checkbox input[type="radio"] {
            opacity: 0;
            z-index: 1;
        }*/


        .btn {
            display: inline-block;
            padding: 6px 12px;
            margin-bottom: 0;
            font-size: 14px;
            font-weight: normal;
            line-height: 1.42857143;
            text-align: center;
            white-space: nowrap;
            vertical-align: middle;
            -ms-touch-action: manipulation;
            touch-action: manipulation;
            cursor: pointer;
            -webkit-user-select: none;
            -moz-user-select: none;
            -ms-user-select: none;
            user-select: none;
            background-image: none;
            border: 1px solid transparent;
            border-radius: 4px;
        }
        .btn:focus,
        .btn:active:focus,
        .btn.active:focus,
        .btn.focus,
        .btn:active.focus,
        .btn.active.focus {
            outline: 5px auto -webkit-focus-ring-color;
            outline-offset: -2px;
        }
        .btn:hover,
        .btn:focus,
        .btn.focus {
            color: #333;
            text-decoration: none;
        }
        .btn:active,
        .btn.active {
            background-image: none;
            outline: 0;
            -webkit-box-shadow: inset 0 3px 5px rgba(0, 0, 0, .125);
            box-shadow: inset 0 3px 5px rgba(0, 0, 0, .125);
        }
        .btn.disabled,
        .btn[disabled],
        fieldset[disabled] .btn {
            cursor: not-allowed;
            filter: alpha(opacity=65);
            -webkit-box-shadow: none;
            box-shadow: none;
            opacity: .65;
        }
        a.btn.disabled,
        fieldset[disabled] a.btn {
            pointer-events: none;
        }
        .btn-default {
            color: #333;
            background-color: #fff;
            border-color: #ccc;
        }
        .btn-default:focus,
        .btn-default.focus {
            color: #333;
            background-color: #e6e6e6;
            border-color: #8c8c8c;
        }
        .btn-default:hover {
            color: #333;
            background-color: #e6e6e6;
            border-color: #adadad;
        }
        .btn-default:active,
        .btn-default.active,
        .open > .dropdown-toggle.btn-default {
            color: #333;
            background-color: #e6e6e6;
            border-color: #adadad;
        }
        .btn-default:active:hover,
        .btn-default.active:hover,
        .open > .dropdown-toggle.btn-default:hover,
        .btn-default:active:focus,
        .btn-default.active:focus,
        .open > .dropdown-toggle.btn-default:focus,
        .btn-default:active.focus,
        .btn-default.active.focus,
        .open > .dropdown-toggle.btn-default.focus {
            color: #333;
            background-color: #d4d4d4;
            border-color: #8c8c8c;
        }
        .btn-default:active,
        .btn-default.active,
        .open > .dropdown-toggle.btn-default {
            background-image: none;
        }
        .btn-default.disabled:hover,
        .btn-default[disabled]:hover,
        fieldset[disabled] .btn-default:hover,
        .btn-default.disabled:focus,
        .btn-default[disabled]:focus,
        fieldset[disabled] .btn-default:focus,
        .btn-default.disabled.focus,
        .btn-default[disabled].focus,
        fieldset[disabled] .btn-default.focus {
            background-color: #fff;
            border-color: #ccc;
        }
        .btn-default .badge {
            color: #fff;
            background-color: #333;
        }
        .btn-primary {
            color: #fff;
            background-color: #337ab7;
            border-color: #2e6da4;
        }
        .btn-primary:focus,
        .btn-primary.focus {
            color: #fff;
            background-color: #286090;
            border-color: #122b40;
        }
        .btn-primary:hover {
            color: #fff;
            background-color: #286090;
            border-color: #204d74;
        }
        .btn-primary:active,
        .btn-primary.active,
        .open > .dropdown-toggle.btn-primary {
            color: #fff;
            background-color: #286090;
            border-color: #204d74;
        }
        .btn-primary:active:hover,
        .btn-primary.active:hover,
        .open > .dropdown-toggle.btn-primary:hover,
        .btn-primary:active:focus,
        .btn-primary.active:focus,
        .open > .dropdown-toggle.btn-primary:focus,
        .btn-primary:active.focus,
        .btn-primary.active.focus,
        .open > .dropdown-toggle.btn-primary.focus {
            color: #fff;
            background-color: #204d74;
            border-color: #122b40;
        }
        .btn-primary:active,
        .btn-primary.active,
        .open > .dropdown-toggle.btn-primary {
            background-image: none;
        }
        .btn-primary.disabled:hover,
        .btn-primary[disabled]:hover,
        fieldset[disabled] .btn-primary:hover,
        .btn-primary.disabled:focus,
        .btn-primary[disabled]:focus,
        fieldset[disabled] .btn-primary:focus,
        .btn-primary.disabled.focus,
        .btn-primary[disabled].focus,
        fieldset[disabled] .btn-primary.focus {
            background-color: #337ab7;
            border-color: #2e6da4;
        }
        .btn-primary .badge {
            color: #337ab7;
            background-color: #fff;
        }
        .btn-success {
            color: #fff;
            background-color: #5cb85c;
            border-color: #4cae4c;
        }
        .btn-success:focus,
        .btn-success.focus {
            color: #fff;
            background-color: #449d44;
            border-color: #255625;
        }
        .btn-success:hover {
            color: #fff;
            background-color: #449d44;
            border-color: #398439;
        }
        .btn-success:active,
        .btn-success.active,
        .open > .dropdown-toggle.btn-success {
            color: #fff;
            background-color: #449d44;
            border-color: #398439;
        }
        .btn-success:active:hover,
        .btn-success.active:hover,
        .open > .dropdown-toggle.btn-success:hover,
        .btn-success:active:focus,
        .btn-success.active:focus,
        .open > .dropdown-toggle.btn-success:focus,
        .btn-success:active.focus,
        .btn-success.active.focus,
        .open > .dropdown-toggle.btn-success.focus {
            color: #fff;
            background-color: #398439;
            border-color: #255625;
        }
        .btn-success:active,
        .btn-success.active,
       .open > .dropdown-toggle.btn-success {
            background-image: none;
        }
        .btn-success.disabled:hover,
        .btn-success[disabled]:hover,
        fieldset[disabled] .btn-success:hover,
        .btn-success.disabled:focus,
        .btn-success[disabled]:focus,
        fieldset[disabled] .btn-success:focus,
        .btn-success.disabled.focus,
        .btn-success[disabled].focus,
        fieldset[disabled] .btn-success.focus {
            background-color: #5cb85c;
            border-color: #4cae4c;
        }
        .btn-success .badge {
            color: #5cb85c;
            background-color: #fff;
        }
        .btn-info {
            color: #fff;
            background-color: #5bc0de;
            border-color: #46b8da;
        }
        .btn-info:focus,
        .btn-info.focus {
            color: #fff;
            background-color: #31b0d5;
            border-color: #1b6d85;
        }
        .btn-info:hover {
            color: #fff;
            background-color: #31b0d5;
            border-color: #269abc;
        }
        .btn-info:active,
        .btn-info.active,
        .open > .dropdown-toggle.btn-info {
            color: #fff;
            background-color: #31b0d5;
            border-color: #269abc;
        }
        .btn-info:active:hover,
        .btn-info.active:hover,
        .open > .dropdown-toggle.btn-info:hover,
        .btn-info:active:focus,
        .btn-info.active:focus,
        .open > .dropdown-toggle.btn-info:focus,
        .btn-info:active.focus,
        .btn-info.active.focus,
        .open > .dropdown-toggle.btn-info.focus {
            color: #fff;
            background-color: #269abc;
            border-color: #1b6d85;
        }
        .btn-info:active,
        .btn-info.active,
        .open > .dropdown-toggle.btn-info {
            background-image: none;
        }
        .btn-info.disabled:hover,
        .btn-info[disabled]:hover,
        fieldset[disabled] .btn-info:hover,
        .btn-info.disabled:focus,
        .btn-info[disabled]:focus,
        fieldset[disabled] .btn-info:focus,
        .btn-info.disabled.focus,
        .btn-info[disabled].focus,
        fieldset[disabled] .btn-info.focus {
            background-color: #5bc0de;
            border-color: #46b8da;
        }
        .btn-info .badge {
            color: #5bc0de;
            background-color: #fff;
        }
        .btn-warning {
            color: #fff;
            background-color: #f0ad4e;
            border-color: #eea236;
        }
        .btn-warning:focus,
        .btn-warning.focus {
            color: #fff;
            background-color: #ec971f;
            border-color: #985f0d;
        }
        .btn-warning:hover {
            color: #fff;
            background-color: #ec971f;
            border-color: #d58512;
        }
        .btn-warning:active,
        .btn-warning.active,
        .open > .dropdown-toggle.btn-warning {
            color: #fff;
            background-color: #ec971f;
            border-color: #d58512;
        }
        .btn-warning:active:hover,
        .btn-warning.active:hover,
        .open > .dropdown-toggle.btn-warning:hover,
        .btn-warning:active:focus,
        .btn-warning.active:focus,
        .open > .dropdown-toggle.btn-warning:focus,
        .btn-warning:active.focus,
        .btn-warning.active.focus,
        .open > .dropdown-toggle.btn-warning.focus {
            color: #fff;
            background-color: #d58512;
            border-color: #985f0d;
        }
        .btn-warning:active,
        .btn-warning.active,
        .open > .dropdown-toggle.btn-warning {
            background-image: none;
        }
        .btn-warning.disabled:hover,
        .btn-warning[disabled]:hover,
        fieldset[disabled] .btn-warning:hover,
        .btn-warning.disabled:focus,
        .btn-warning[disabled]:focus,
        fieldset[disabled] .btn-warning:focus,
        .btn-warning.disabled.focus,
        .btn-warning[disabled].focus,
        fieldset[disabled] .btn-warning.focus {
            background-color: #f0ad4e;
            border-color: #eea236;
        }
        .btn-warning .badge {
            color: #f0ad4e;
            background-color: #fff;
        }
        .btn-danger {
            color: #fff;
            background-color: #d9534f;
            border-color: #d43f3a;
        }
        .btn-danger:focus,
        .btn-danger.focus {
            color: #fff;
            background-color: #c9302c;
            border-color: #761c19;
        }
        .btn-danger:hover {
            color: #fff;
            background-color: #c9302c;
            border-color: #ac2925;
        }
        .btn-danger:active,
        .btn-danger.active,
        .open > .dropdown-toggle.btn-danger {
            color: #fff;
            background-color: #c9302c;
            border-color: #ac2925;
        }
        .btn-danger:active:hover,
        .btn-danger.active:hover,
        .open > .dropdown-toggle.btn-danger:hover,
        .btn-danger:active:focus,
        .btn-danger.active:focus,
        .open > .dropdown-toggle.btn-danger:focus,
        .btn-danger:active.focus,
        .btn-danger.active.focus,
        .open > .dropdown-toggle.btn-danger.focus {
            color: #fff;
            background-color: #ac2925;
            border-color: #761c19;
        }
        .btn-danger:active,
        .btn-danger.active,
        .open > .dropdown-toggle.btn-danger {
            background-image: none;
        }
        .btn-danger.disabled:hover,
        .btn-danger[disabled]:hover,
        fieldset[disabled] .btn-danger:hover,
        .btn-danger.disabled:focus,
        .btn-danger[disabled]:focus,
        fieldset[disabled] .btn-danger:focus,
        .btn-danger.disabled.focus,
        .btn-danger[disabled].focus,
        fieldset[disabled] .btn-danger.focus {
            background-color: #d9534f;
            border-color: #d43f3a;
        }
        .btn-danger .badge {
            color: #d9534f;
            background-color: #fff;
        }
        .btn-link {
            font-weight: normal;
            color: #337ab7;
            border-radius: 0;
        }
        .btn-link,
        .btn-link:active,
        .btn-link.active,
        .btn-link[disabled],
        fieldset[disabled] .btn-link {
            background-color: transparent;
            -webkit-box-shadow: none;
            box-shadow: none;
        }
        .btn-link,
        .btn-link:hover,
        .btn-link:focus,
        .btn-link:active {
            border-color: transparent;
        }
        .btn-link:hover,
        .btn-link:focus {
            color: #23527c;
            text-decoration: underline;
            background-color: transparent;
        }
        .btn-link[disabled]:hover,
        fieldset[disabled] .btn-link:hover,
        .btn-link[disabled]:focus,
        fieldset[disabled] .btn-link:focus {
            color: #777;
            text-decoration: none;
        }
        .btn-lg,
        .btn-group-lg > .btn {
            padding: 10px 16px;
            font-size: 18px;
            line-height: 1.3333333;
            border-radius: 6px;
        }
        .btn-sm,
        .btn-group-sm > .btn {
            padding: 5px 10px;
            font-size: 12px;
            line-height: 1.5;
            border-radius: 3px;
        }
        .btn-xs,
        .btn-group-xs > .btn {
            padding: 1px 5px;
            font-size: 12px;
            line-height: 1.5;
            border-radius: 3px;
        }
        .btn-block {
            display: block;
            width: 100%;
        }
        .btn-block + .btn-block {
            margin-top: 5px;
        }
        input[type="submit"].btn-block,
        input[type="reset"].btn-block,
        input[type="button"].btn-block {
            width: 100%;
        }


    </style>
    <script type="text/javascript">
        $.extend($.fn.validatebox.defaults.rules, {
            isTableName: {
                validator: function(value, param){
                    //$.fn.validatebox.defaults.rules.isTableName.message = '只能是6-16个长度的英文小写字母.';
                    return /^[a-z][a-z0-9]{3,15}$/.test(value);
                },
                message: '只能是4-16个长度的英文小写字母.'
            }
        });
    </script>
</head>
<body style="overflow-x: hidden">
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand">群文管理</a>
            <a class="navbar-brand"> &gt;&nbsp;群文资源库</a>
            <c:choose>
                <c:when test="${param.onlyshow == '1'}"><a class="navbar-brand"> &gt;&nbsp;查看</a></c:when>
                <c:otherwise><a class="navbar-brand"> &gt;&nbsp;编辑</a></c:otherwise>
            </c:choose>
        </div>
    </div>
</nav>


<div class="container-fluid">
    <form class="form-horizontal" role="form" id="whgff" method="post">
        <fieldset>
            <legend>资源库基本信息</legend>
            <input type="hidden" id="id" name="id" value="${library.id}"/>

            <div class="form-group">
                <label class="col-sm-2 control-label"><i>*</i>资源类型：</label>
                <div class="col-sm-4">
                    <input class="easyui-combobox" style="width: 100%; height: 34px;" id="resourcetype" name="resourcetype" value="${library.resourcetype}" data-options="prompt:'请选择资源类型',required:true,editable:false,readonly:true,valueField:'id',textField:'text',data:WhgComm.getResourceTypeData()" />
                </div>
                <label class="col-sm-2 control-label"><i>*</i>所属文化馆：</label>
                <div class="col-sm-4">
                    <input class="easyui-combobox" style="width:100%; height:32px" name="cultid" id="cultid" data-options="required:true,readonly:true" />
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label"><i>*</i>所属部门：</label>
                <div class="col-sm-4">
                    <input class="easyui-combobox" style="width:100%; height:32px" name="deptid" id="deptid" data-options="required:true" />
                </div>
                <label class="col-sm-2 control-label" for="name"><i>*</i>资源库名称：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" style="width: 100%; height: 34px;" id="name" name="name" value="${library.name}" data-options="prompt:'请输入资源库名称，长度不能超过120个字符',required:true,validType:'length[1,120]'" />
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label" for="tablename"><i>*</i>资源库代码：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" style="width: 100%; height: 34px;" id="tablename" name="tablename" value="${library.tablename}" data-options="prompt:'请输入资源库代码，只能是4-16长度的英文小写字母或者数字',readonly:true,required:true,validType:'isTableName'" />
                </div>
                <label class="col-sm-2 control-label" for="idx"><i>*</i>排序值：</label>
                <div class="col-sm-4">
                    <input class="easyui-numberspinner" style="width: 100%; height: 34px;" id="idx" name="idx" value="${library.idx}" data-options="prompt:'请输入资源库排序值，在展示时按升序排列',required:true,min:1,max:999" />
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label"><i>*</i>艺术类型：</label>
                <%--<div class="col-sm-4">
                    <div class="whgff-row-input">
                        <div class="checkbox checkbox-primary" id="arttype" name="arttype"></div>
                    </div>
                </div>--%>
                <div class="whgff-row" style="float: left;width: 75%;overflow: hidden;padding-left:20px;position: relative;">
                    <div class="whgff-row-input">
                        <div class="checkbox checkbox-primary" id="arttype" name="arttype" data-options="required:true" ></div>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label"><i>*</i>地区：</label>
                <div class="col-sm-4">
                    <div class="whgff-row-input">
                        <input class="easyui-combobox" style="width:125px; height:32px" id="province" name="province" data-options="readonly:true, required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name'<%--, data:__PROVINCE, onChange:changeProvince--%>"/>
                        <input class="easyui-combobox" style="width:150px; height:32px" id="__CITY_ELE" name="city" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name'<%--, data:[], onChange:changeCity--%>"/>
                        <input class="easyui-combobox" style="width:179px; height:32px" id="__AREA_ELE" name="area" data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
                    </div>
                </div>
                <label class="col-sm-2 control-label">民族语言：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" style="width: 100%; height: 34px;" id="nationallanguage" name="nationallanguage" value="${library.nationallanguage}" data-options="" />
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">标签：</label>
                <div class="col-sm-4">
                    <div class="whgff-row-input">
                        <input class="EEE-combobox" id="tags" name="etags" style="width:300px; height:32px" validType="notQuotes"
                               data-options="multiple:true, prompt:''
                                ,onChange: function (val, oldval) {
                                    if (val.length>1 && val[0]==''){
                                        val.shift();
                                        $(this).combobox('setValues', val);
                                    }
                                }"
                        />
                        <span class="btn btn-success btn-sm"  onclick="javascript:addTag()">添加标签</span>
                    </div>
                    <div class="clearfix" id="select_tags" style="text-align: left;margin-top: 10px;">
                        <ul style="text-align: left;padding: 0;list-style: none;">
                            <c:forEach items="${tagList}" var="tag">
                                <li id="${tag.name}" style="float: left;position: relative;margin-right: 10px;margin-bottom: 10px;"><input type="hidden" name="tags" value="${tag.id}">
                                    <label class="btn btn-default btn-block" style="padding-right: 22px;">${tag.name}</label>
                                    <a href="javascript:void(0)" class="textbox-icon icon-clear" icon-index="0" tabindex="-1" style="width: 18px; height: 30px;position:absolute;top: 2px;right: 2px;"></a>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>

            </div>


            <div class="form-group">
                <label class="col-sm-2 control-label"><i>*</i>封面图片：</label>
                <div class="col-sm-4">
                    <div class="whgff-row-input">
                        <input type="hidden" id="library_imgurl" name="imgurl" value="${library.imgurl}" >
                        <div class="whgff-row-input-imgview" id="previewImg1" style="height: 200px; width: 300px; border: 2px dashed #ccc; border-radius: 10px; text-align: center; overflow: hidden;"></div>
                        <div class="whgff-row-input-imgfile" id="divImgFile">
                            <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                            <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
                        </div>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">资源库版权所有者：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" style="width: 100%; height: 34px;" id="copyright" name="copyright" value="${library.copyright}" data-options="" />
                </div>

                <label class="col-sm-2 control-label">资源库建设单位：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" value="${library.constructionunit}" style="width:100%; height:32px" name="constructionunit" id="constructionunit" data-options="" />
                </div>
            </div>


            <div class="form-group">
                <label class="col-sm-2 control-label" for="memo">资源库简介：</label>
                <div class="col-sm-4">
                    <textarea style="display: none;" id="textarea_memo">${library.memo}</textarea>
                    <input class="easyui-textbox" style="width: 100%; height: 100px;" id="memo" name="memo" data-options="prompt:'请输入资源库简介',multiline:true,validType:'length[0,500]'" />
                </div>
            </div>


        </fieldset>

        <fieldset>
            <legend>资源库公共表单项<span class="glyphicon glyphicon-chevron-down" id="btn_down"></span><span class="glyphicon glyphicon-chevron-up" id="btn_up"></span></legend>

            <!-- 图片资源-显示图片 -->
            <c:if test="${library.resourcetype == 'img'}">
            <div class="form-group">
                <label class="col-sm-2 control-label"><i>*</i>封面图片：</label>
                <div class="col-sm-10">
                    <div class="custom-field custom-field-imginput">
                        <div class="whgff-row-input-imgview" style="height: 200px; width: 300px; border: 2px dashed #ccc; border-radius: 10px; text-align: center; overflow: hidden;"></div>
                    </div>
                </div>
            </div>
            </c:if>

            <!-- 视频资源-需要上传图片做为封面 -->
            <c:if test="${library.resourcetype == 'video'}">
            <div class="form-group">
                <label class="col-sm-2 control-label"><i>*</i>封面图片：</label>
                <div class="col-sm-10">
                    <div class="custom-field custom-field-imginput">
                        <input type="hidden" id="respicture" value="">
                        <div class="whgff-row-input-imgview" id="preview_respicture" style="height: 200px; width: 300px; border: 2px dashed #ccc; border-radius: 10px; text-align: center; overflow: hidden;"></div>
                        <div class="whgff-row-input-imgfile">
                            <i><button type="button" class="btn btn-default btn-sm" id="uploadBtn_respicture"><span class="glyphicon glyphicon-folder-open"></span>&nbsp;选择图片</button></i>
                        </div>
                    </div>
                </div>
            </div>
            </c:if>

            <div class="form-group">
                <label class="col-sm-2 control-label"><i>*</i>资源名称：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" style="width: 100%; height: 34px;" id="resname" data-options="readonly:true,prompt:'请输入资源名称，长度不能超过120个字符',required:false,novalidate:true,validType:'length[1,120]'" />
                </div>
                <label class="col-sm-2 control-label">资源作者：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" style="width: 100%; height: 34px;" id="resauthor" data-options="readonly:true,prompt:'请输入资源作者，长度不能超过32个字符',required:false,novalidate:true,validType:'length[1,32]'" />
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">艺术分类：</label>
                <div class="col-sm-10">
                    <div style="margin-top: 7px;" id="div_resarttype"></div>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">标签：</label>
                <div class="col-sm-10">
                    <div style="margin-top: 7px;" id="div_restag"></div>
                </div>
            </div>

            <c:if test="${library.resourcetype == 'img'}">
            <div class="form-group">
                <label class="col-sm-2 control-label">宽：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" style="width: 100%; height: 34px;" id="reswidth" data-options="readonly:true,prompt:'图片宽度',novalidate:true,validType:'length[1,32]'" />
                </div>
                <label class="col-sm-2 control-label">高：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" style="width: 100%; height: 34px;" id="resheight" data-options="readonly:true,prompt:'图片高度',novalidate:true,validType:'length[1,32]'" />
                </div>
            </div>
            </c:if>

            <div class="form-group">
                <label class="col-sm-2 control-label">资源来源：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" style="width: 100%; height: 34px;" id="resorigin" data-options="prompt:'请输入资来源。如：新浪网',readonly:true,required:false,novalidate:true,validType:'length[1,32]'" />
                </div>
                <label class="col-sm-2 control-label">资源大小：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" style="width: 100%; height: 34px;" id="ressize" data-options="readonly:true,required:false,novalidate:true,validType:'length[1,32]'" />
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">资源简介：</label>
                <div class="col-sm-10">
                    <script id="resintroduce" name="resintroduce" type="text/plain" style="width: 100%; height: 300px;"></script>
                    </div>
                    </div>
                    </fieldset>

                    <fieldset id="extFieldDiv">
                        <legend>资源库自定义表单</legend>
                        </fieldset>

                        <c:if test="${param.onlyshow != '1'}">
                        <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                        <button type="button" class="btn btn-success btn-sm" onclick="rowOneField();"><span class="glyphicon glyphicon-plus"></span>&nbsp;单行一列</button>
                    <button type="button" class="btn btn-info btn-sm" onclick="rowTwoField();"><span class="glyphicon glyphicon-plus"></span>&nbsp;单行两列</button>
                    </div>
                    </div>
                    </c:if>

                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                        <c:choose>
                        <c:when test="${param.onlyshow == '1'}">
                        <button type="button" class="btn btn-default" onclick="WhgComm.editDialogClose();">&nbsp;返  回</button>
                    </c:when>
                    <c:otherwise>
                    <button type="button" class="btn btn-primary" id="doSubmit"><span class="glyphicon glyphicon-ok"></span>&nbsp;保  存</button>
                    <button type="button" class="btn btn-default" onclick="WhgComm.editDialogClose();">&nbsp;返  回</button>
                    </c:otherwise>
                    </c:choose>
                    </div>
                    </div>
                    </form>
                    </div>


                    <!-- script -->
                    <script type="text/javascript">
                        //单行一列
                        function rowOneField() {
                            WhgCustomForm.createRowOneColumn('extFieldDiv');
                        }//单行一列 END

                    //单行两列
                    function rowTwoField(){
                        WhgCustomForm.createRowTwoColumn('extFieldDiv');
                    }//单行两列 END

                    //初始表单提交
                    function initForm(){
                        //表单初始
                        $('#whgff').form({
                            novalidate: true,
                            url: "${basePath}/admin/mass/library/edit",
                            onSubmit : function(param) {
                                var _valid = $(this).form('enableValidation').form('validate');

                                if(_valid){
                                    //艺术分类不能为空
                                    var arttype = $("#whgff").find("input[name='arttype']:checked").val();
                                    if (!arttype){
                                        _valid = false;
                                        $.messager.alert("提示", '艺术分类不能为空！', 'warning');
                                    }

                                    var picture1 = $("#library_imgurl").val();
                                    if (!picture1){
                                        $.messager.alert("提示", '封面图片不能为空', 'error');
                                        _valid = false;
                                    }

                                }

                                if (!_valid){
                                    $.messager.progress('close');
                                    $('#doSubmit').off('click').one('click', function (){$('#whgff').submit();});
                                }else{
                                    //自定义字段
                                    var formData = WhgCustomForm.getFormData( $('#extFieldDiv') );
                                    param.adds = formData.add;
                                    param.edits = formData.edit;
                                    param.dels = formData.del;
                                    param.keeps = formData.keep;
                                }
                                return _valid;
                            },
                            success : function(data) {
                                $.messager.progress('close');
                                var Json = eval('('+data+')');
                                if(Json && Json.success == '1'){
                                    window.parent.$('#whgdg').datagrid('reload');
                                    WhgComm.editDialogClose();
                                } else {
                                    $.messager.alert('提示', '操作失败:'+Json.errormsg+'!', 'error');
                                    $('#doSubmit').off('click').one('click', function (){$('#whgff').submit();});
                                }
                            }
                        });
                        //注册提交事件
                        $('#doSubmit').off('click').one('click', function (){$('#whgff').submit();});
                    }//初始表单提交 END

                    //构造现有表单
                    function initCustomForm(){
                        $.getJSON('${basePath}/admin/mass/library/findfields?id=${library.id}', function(data){
                            var forms = data.forms;
                            var fields = data.fields;

                            if($.isArray(fields) && $.isArray(forms)){
                                //创建表单
                                var curt_rows = -1;//当前行
                                var curt_columns = [];//当前行的列
                                for(var i=0; i<forms.length; i++){
                                    var t_column = forms[i];
                                    var t_rows = t_column.rows;
                                    var isNewRow = curt_rows != t_rows;
                                    var isLast = i == forms.length-1;
                                    var isFirst = i == 0;
                                    if(!isFirst && isNewRow){
                                        WhgCustomForm.createRow('extFieldDiv', curt_columns, '${param.onlyshow}' === '1');
                                    }
                                    if(isNewRow){
                                        curt_rows = t_rows;
                                        curt_columns = [];
                                    }
                                    curt_columns.push(t_column);
                                    if(isLast){
                                        WhgCustomForm.createRow('extFieldDiv', curt_columns, '${param.onlyshow}' === '1');
                                    }
                                }

                                //创建字段
                                for(var i=0; i<fields.length; i++){
                                    var t_field = fields[i];
                                    var t_formid = t_field.formid;
                                    var t_fieldidx = t_field.fieldidx;
                                    var inputEle_a = $('#inputEle_'+t_formid);
                                    var inputEle_b = $('#inputEle_'+t_fieldidx+'_'+t_formid);
                                    if(inputEle_a.size() == 1){
                                        WhgCustomFormField.createField(t_field,true,inputEle_a);
                                        inputEle_a.find('button.field-edit').attr('fid1', t_field.id);
                                    }else if(inputEle_b.size() == 1){
                                        WhgCustomFormField.createField(t_field,true,inputEle_b);
                                        inputEle_b.find('button.field-edit').attr('fid1', t_field.id);

                                        var labelEle = $('#labelEle_'+t_fieldidx+'_'+t_formid);
                                        if(labelEle.size() == 1){
                                            labelEle.html("&nbsp;&nbsp;"+t_field.fieldname+"&nbsp;&nbsp;");
                                        }
                                    }
                                }
                            }
                        });
                    }

                    function initCommForm() {
                       /* var tags = "${library.tags}";
                        if (tags && tags!=''){
                            $("#tags").combobox('setValue', "${library.tags}");
                        }*/

                        //图片初始化
                        var whgImg = WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'library_imgurl', previewImgId: 'previewImg1'});

                        //初始艺术分类
                        WhgCustomComm.createArtTypeHtml('div_resarttype', '${library.cultid}', '${res.resarttype}');

                        //初始标签
                        WhgCustomComm.createTagHtml('div_restag', '${library.cultid}', '${res.restag}');
                        $("#div_resarttype, #div_restag").on("click", "input:checkbox", function(){return false});

                        //初始富文本
                        window.ue_resintroduce = UE.getEditor('resintroduce',{readonly: true});

                        $('#btn_up').parents('fieldset').find('div.form-group').hide();
                        $('#btn_up').hide();
                        $('#btn_down').show();

                        $('#btn_down').on('click', function () {
                            $(this).parents('fieldset').find('div.form-group').show();
                            $(this).hide();
                            $('#btn_up').show();
                        });
                        $('#btn_up').on('click', function () {
                            $(this).parents('fieldset').find('div.form-group').hide();
                            $(this).hide();
                            $('#btn_down').show();
                        });
                    }

                    //window.onload
                    $(function () {
                        //初始文化馆和权限
                        WhgComm.initPMS({
                            basePath:'${basePath}',
                            cultEid:'cultid', cultValue:'${library.cultid}',
                            deptEid:'deptid', deptValue:'${library.deptid}',

                            provinceEid:'province', provinceValue:'${library.province}',
                            cityEid:'__CITY_ELE', cityValue:'${library.city}',
                            areaEid:'__AREA_ELE', areaValue:'${library.area}',

                            ywiArtTypeEid:'arttype', ywiArtTypeValue:'${library.arttype}',

                            ywiTagEid2:'tags', ywiTagValue:'', ywiKeyClass:56,

                        });

                        //初始备注
                        $('#memo').textbox('setValue', $('#textarea_memo').val());

                        //初始表单
                        initForm();

                        //初始自定义表表单字段
                        initCustomForm();

                        //公共表单
                        initCommForm();

                        $("#select_tags ul").on('click','li a',function(){
                            $(this).parent().remove();
                        })

                    });//window.onload END

                    /** 添加标签 */
                    function addTag(){
                        var tags = $("input[name='etags']").val();
                        var name =  $("#tags").combobox("getText");

                        $.ajax({
                            type: "POST",
                            url: "${basePath}/admin/yunwei/tag/add",
                            data: {type : 56,name :name},
                            success: function(data){

                                 // $.messager.alert("提示", "操作成功");
                                if(data.data){
                                    tags = data.data;
                                }
                                if(name){
                                    $('#tags').combobox('setValues','');
                                    //判断标签是否已选
                                    if ( $("#"+name).length > 0 ) {
                                        $.messager.alert("提示", "标签不可重复添加");
                                        return;
                                    }
                                    var html = '<li id="'+name+'" style="float: left;position: relative;margin-right: 10px;margin-bottom: 10px;"><input type="hidden" name="tags" value="'+tags+'">\n' +
                                        '<label class="btn btn-default btn-block" style="padding-right: 22px;">'+name+'</label>\n' +
                                        '<a href="javascript:void(0)" class="textbox-icon icon-clear" icon-index="0" tabindex="-1" style="width: 18px; height: 30px;position:absolute;top: 2px;right: 2px;"></a>\n' +
                                        '</li>';
                                    $('#select_tags ul').append(html)
                                }
                            }
                        });
                    }

                    window.onload = function (ev) {
                        /*setTimeout(function(){
                            if(!'${library.tags}') return;
                            var html = '';
                            var tagsName =  $('#tags').combobox("getText").split(',');
                            var tagsId = '${library.tags}'.split(',');
                            for (var i = 0; i < tagsName.length; i++) {
                                var oName = tagsName[i];
                                var oId = tagsId[i];
                                html += '<li id="'+oName+'" style="float: left;position: relative;margin-right: 10px;margin-bottom: 10px;"><input type="hidden" name="tags" value="'+oId+'">\n' +
                                    '<label class="btn btn-default btn-block" style="padding-right: 22px;">'+oName+'</label>\n' +
                                    '<a href="javascript:void(0)" class="textbox-icon icon-clear" icon-index="0" tabindex="-1" style="width: 18px; height: 30px;position:absolute;top: 2px;right: 2px;"></a>\n' +
                                    '</li>';
                            }
                            $('#select_tags ul').append(html)
                            $('#tags').combobox('setValues','');
                        },1000)*/
                    }
                    </script>
                    <!-- script END -->
</body>
</html>