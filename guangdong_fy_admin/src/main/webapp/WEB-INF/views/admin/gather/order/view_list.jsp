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
    <title>众筹预定管理(其它众筹)</title>
    <script src="${basePath}/static/admin/js/whgmodule-gridopts.js"></script>
</head>
<body>
    <table id="whgdg" class="easyui-datagrid" title="预定管理-${title}" style="display: none;"
           toolbar="#tb" pagination=true pageSize=30 pageList="[10,20,30,50,100,200]" loadMsg="数据加载中，请稍候。。。"
           data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:true, checkOnSelect:true, selectOnCheck:true,
           url:'${basePath}/admin/gather/order/srchList?gatherid=${gatherid}'">
        <thead>
            <tr>
                <th data-options="sortable:false, field:'gatherid', formatter:function(){return '${title}';} ">名称</th>
                <th data-options="sortable:false, field:'orderid'">订单号</th>
                <th data-options="sortable: true, field:'crtdate', formatter:WhgComm.FMTDate ">订单日期</th>
                <th data-options="sortable: true, field:'name'">姓名</th>
                <th data-options="sortable: true, field:'phone' ">联系电话</th>
                <th data-options="sortable:false, field:'id', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
            </tr>
        </thead>
    </table>

    <div id="tb" style="display: none;">
        <div>
            <a class="easyui-linkbutton" iconCls="icon-back" onclick="WhgComm.editDialogClose()">返回</a>
        </div>

        <div style="padding-top: 5px">
            <input class= "easyui-textbox" name="name" data-options="width:120" prompt="姓名"/>
            <input class= "easyui-textbox" name="phone" data-options="width:120" prompt="联系电话"/>
            <a class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#tb');">查 询</a>
        </div>
    </div>


    <!-- 操作按钮 -->
    <div id="whgdg-opt" style="display: none;">
        <a plain="true" method="whgListTool.view">查看</a>
    </div>
    <!-- 操作按钮-END -->

    <!-- 编辑表单 -->
    <div id="whgwin-edit"></div>
    <!-- 编辑表单 END -->

<script>

    var whgListTool = new Gridopts();

    Gridopts.prototype.view = function(idx){
        var row = this.__getGridRow(idx);
        WhgComm.editDialog( this.modeUrl+'/view/show?id='+row.id);
    };

</script>
</body>
</html>
