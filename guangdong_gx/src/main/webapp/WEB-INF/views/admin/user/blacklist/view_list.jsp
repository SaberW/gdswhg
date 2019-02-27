<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>黑名单管理</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
</head>
<body>
<!-- 表格 -->
<table id="whgdg" title="黑名单管理" class="easyui-datagrid" style="display: none"
       pagination=true pageSize=30 pageList="[10,20,30,50,100,200]" loadMsg="数据加载中，请稍候。。。"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:true, pagination:true,
       toolbar:'#whgdg-tb', url:'${basePath}/admin/user/black/srchList4p'">
    <thead>
    <tr>
        <th data-options="field:'userphone', width:100">会员手机号</th>
        <th data-options="field:'note', width:200 ">黑名单说明</th>
        <th data-options="field:'jointime', width:100, formatter:WhgComm.FMTDateTime">加入黑名单时间</th>
        <th data-options="field:'updatetime', width:100, formatter:WhgComm.FMTDateTime" >状态变改变时间</th>
        <th data-options="field:'state', width:60, formatter:WhgComm.FMTState" >状态</th>
        <th data-options="field:'_opt', width:50, formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>
<!-- 表格 END -->


<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <div class="whgdg-tb-srch">
        <input class="easyui-textbox" style="width: 200px;" name="userphone" data-options="prompt:'请输入会员手机号', validType:'length[1,32]'" />
        <select class="easyui-combobox" name="state" prompt="请选择状态" panelHeight="auto" limitToList="true"
                data-options="width:120, value:'', valueField:'id', textField:'text', data:WhgComm.getState()">
        </select>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <shiro:hasPermission name="${resourceid}:off">
        <a href="javascript:void(0)" validKey="state" validVal="1" plain="true" method="cancel">取消</a>
    </shiro:hasPermission>
</div>
<!-- 操作按钮-END -->



<script>

    function cancel(idx) {
        var row = $("#whgdg").datagrid('getRows')[idx];
        $.messager.confirm('确认对话框', '您确认要取消选择的项吗？', function(r){
            if (r){
                $.ajax({
                    type: "POST",
                    url: "${basePath}/admin/user/black/cancel",
                    data: {userid : row.userid},
                    success: function(data){
                        if(data.success == "1"){
                            $('#whgdg').datagrid('reload');
                        }else{
                            $.messager.alert("提示", data.errorMsg);
                        }

                    }
                });
            }
        });
    }

</script>
</body>
</html>
