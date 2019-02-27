<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>
    <title>阿里云短信发送失败记录</title>

    <script src="${basePath}/static/admin/js/whgmodule-gridopts.js"></script>
</head>
<body>
<table id="whgdg" class="easyui-datagrid" title="阿里云短信发送失败记录" style="display: none;" toolbar="#tb"
       data-options="fit:true, striped:true, rownumbers:true, pagination:true, fitColumns:false, singleSelect:true, checkOnSelect:true">
    <thead>
    <tr>
        <th data-options="sortable:false, width:150, field:'code' ">短信发送状态码</th>
        <th data-options="sortable:false, width:150, field:'message' ">状态码描述</th>
        <th data-options="sortable:false, width:80, field:'phone' ">目标手机</th>
        <th data-options="sortable:false, width:150, field:'bizId' ">回执ID</th>
        <th data-options="sortable:false, width:150, field:'crtdate', formatter:WhgComm.FMTDateTime ">记录时间</th>
        <th data-options="sortable:false, width:100, field:'tempcode' ">阿里云模板</th>
        <th data-options="sortable:false,  field:'params' ">短信参数</th>
    </tr>
    </thead>
</table>

<div id="tb" style="display: none;">
    <div class="whgdg-tb-srch">
        <input class="easyui-textbox" name="phone" prompt="请输入完整手机号码" data-options="width:200"/>
        <a class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#tb');">查 询</a>
    </div>
</div>


<script>

    $(function(){
        WhgComm.search('#whgdg', '#tb', '${basePath}/admin/yunwei/aliysmserr/srchList4p');
    });

</script>

</body>
</html>
