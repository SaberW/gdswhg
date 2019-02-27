<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>直播管理-关联活动列表</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
</head>
<body>

<!-- 表格 -->
<table id="whgdg" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:true, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb'">
    <thead>
    <tr>
        <th data-options="field:'ck',checkbox:true"></th>
        <th data-options="field:'name',width:300">名称</th>
        <th data-options="field:'starttime',width:130,formatter:WhgComm.FMTDate ">开始时间</th>
        <th data-options="field:'endtime',width:130,formatter:WhgComm.FMTDate ">结束时间</th>
        <th data-options="field:'telphone',width:100">联系手机</th>
        <th data-options="field:'address',width:150">地址</th>
        <th data-options="field:'crtuser',width:120,formatter:WhgComm.FMTUserName">编辑者</th>
        <th data-options="field:'checkor',width:120,formatter:WhgComm.FMTUserName">审核者</th>
        <th data-options="field:'publisher',width:120,formatter:WhgComm.FMTUserName">发布者</th>
        <th data-options="field:'statemdfdate',width:130,sortable: true, formatter:WhgComm.FMTDateTime ">操作时间</th>
        <th data-options="field:'state', width:50, formatter:WhgComm.FMTBizState">状态</th>
    </tr>
    </thead>
</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <div class="whgd-gtb-btn"> </div>
    <div class="whgdg-tb-srch">
        <input class="easyui-textbox" style="width: 200px;" name="name" data-options="prompt:'请输入活动名称', validType:'length[1,32]'"/>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->


<!-- script -->
<script type="text/javascript">
    /** 选中一条记录 */
    function clickSubmit(){
        var row = $('#whgdg').datagrid('getSelected');
        if(row && row.id){
            window.parent.$('#cultid').val(row.cultid);
            window.parent.$('#deptid').val(row.deptid);
            window.parent.$('#refid').val(row.id);
            window.parent.$('#refname').textbox('setValue', row.name);
            window.parent.$('#limitStartTime').val(WhgComm.FMTDate(row.starttime)+' 00:00:00');
            window.parent.$('#limitEndTime').val(WhgComm.FMTDate(row.endtime)+' 23:59:59');
            window.parent.$('#starttime').datetimebox('setValue', WhgComm.FMTDate(row.starttime)+' 00:00:00');
            window.parent.$('#endtime').datetimebox('setValue', WhgComm.FMTDate(row.endtime)+' 23:59:59');
            WhgComm.editDialogClose();
        }else{
            $.messager.alert('警告', '请选择一条记录再进行此操作！', 'warning');
        }
    }

    $(function () {
        //条件查询-文化馆
        var cultid = WhgComm.getMgrCultsFirstId();
        WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/activity/srchList4p?state=6&cultid='+cultid+'&__pageType=publish');

        //注册确认事件
        WhgComm.getOpenDialogSubmitBtn().off('click').one('click', clickSubmit);
    });
</script>
<!-- script END -->
</body>
</html>