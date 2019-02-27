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
    <title>阿里云短信模板管理</title>

    <script src="${basePath}/static/admin/js/whgmodule-gridopts.js"></script>
</head>
<body>
<table id="whgdg" class="easyui-datagrid" title="阿里云短信模板管理" style="display: none;" toolbar="#tb"
       data-options="fit:true, striped:true, rownumbers:true, pagination:true, fitColumns:true, singleSelect:true, checkOnSelect:true">
    <thead>
    <tr>
        <th data-options="sortable:false, width:80, field:'tpcode' ">模板code</th>
        <th data-options="sortable:false, width:100, field:'tpname' ">模板名称</th>
        <th data-options="sortable:false, width:150, field:'tpdesc' ">模板说明</th>
        <th data-options="sortable:false, field:'id', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>

<div id="tb" style="display: none;">
    <shiro:hasPermission name="${resourceid}:add">
        <div class="whgd-gtb-btn">
            <a class="easyui-linkbutton" iconCls="icon-add" onclick="whgListTool.add()">添 加</a>
        </div>
    </shiro:hasPermission>

    <div class="whgdg-tb-srch">
        <input class="easyui-textbox" name="tpcode" prompt="请输入模板code" data-options="width:200"/>
        <input class="easyui-textbox" name="tpname" prompt="请输入模板名称" data-options="width:200"/>
        <a class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#tb');">查 询</a>
        <span style="color: #ff0000;">&nbsp;&nbsp;&nbsp;&nbsp;请参考www.aliyun.com控制台中审核通过的短信模板配置</span>
    </div>
</div>

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">

    <a plain="true" method="whgListTool.view">查看</a>

    <shiro:hasPermission name="${resourceid}:edit">
        <a plain="true" method="whgListTool.edit" >编辑</a>
    </shiro:hasPermission>

    <shiro:hasPermission name="${resourceid}:del">
        <a plain="true" method="whgListTool.del" >删除</a>
    </shiro:hasPermission>

</div>
<!-- 操作按钮-END -->

<script>

    $(function(){
        WhgComm.search('#whgdg', '#tb', '${basePath}/admin/yunwei/aliysmscode/srchList4p');
    });

    var whgListTool = new Gridopts();

</script>

</body>
</html>
