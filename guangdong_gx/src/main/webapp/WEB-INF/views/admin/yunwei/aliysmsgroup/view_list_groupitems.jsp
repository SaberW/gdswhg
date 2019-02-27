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
    <title>阿里云短信组模板管理</title>

    <script src="${basePath}/static/admin/js/whgmodule-gridopts.js"></script>
</head>
<body>

<table id="whgdg" class="easyui-datagrid" title="短信组模板管理" style="display: none;" toolbar="#tb"
       data-options="fit:true, striped:true, rownumbers:true, pagination:false, fitColumns:false, singleSelect:true, checkOnSelect:true
        ,url:'${basePath}/admin/yunwei/aliysmsgroup/smsgcref/srchlistgcref?id=${info.id}' ">
    <thead frozen="true">
    <tr>
        <th data-options="sortable:false, width:60, field:'id', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
        <th data-options="sortable:false, width:120, field:'tpcode' ">模板code</th>
        <th data-options="sortable:false, width:150, field:'tpname' ">模板名称</th>
        <th data-options="sortable:false, width:150, field:'actpoint' ">切入点</th>
    </tr>
    </thead>
    <thead>
    <tr>
        <th data-options="sortable:false, field:'actdesc' ">切入点说明</th>
        <th data-options="sortable:false, field:'tpdesc' ">模板说明</th>
        <th data-options="sortable:false, field:'tpcontent' ">模板内容</th>
    </tr>
    </thead>
</table>

<div id="tb" style="display: none;">
    <div class="whgd-gtb-btn">
        <a class="easyui-linkbutton" iconCls="icon-back" onclick="WhgComm.editDialogClose()">返 回</a>
        <a class="easyui-linkbutton" iconCls="icon-add" onclick="whgListTool.add()">设置模板</a>
        <a class="easyui-linkbutton" iconCls="icon-reload" onclick="whgListTool.reload()">刷新</a>
    </div>
</div>

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <a plain="true" method="whgListTool.del" >删除</a>
</div>
<!-- 操作按钮-END -->

<script>
    $(function(){

    });

    var whgListTool = new Gridopts();

    Gridopts.prototype.add = function(){
        var url = '${basePath}/admin/yunwei/aliysmsgroup/smsgcref/view/edit?id=${info.id}';
        WhgComm.openDialog4size('设置短信组模板', url, 800, 550);
    };


</script>

</body>
</html>
