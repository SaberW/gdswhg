<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">

    <title>节目单列表</title>

    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
</head>
<body>
<!-- 表格 -->
<table id="whgdg" title="节目单列表" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb', url:'${basePath}/admin/show/jmd/srchList4p?entid=${entid}'">
    <thead>
    <tr>
        <th data-options="field:'title',width:80">名称</th>
        <th data-options="field:'etype',width:80">类型</th>
        <th data-options="field:'person',width:80">参演人员</th>
        <th data-options="field:'time',width:80">时长</th>
        <th data-options="field:'crtdate', width:80,formatter:WhgComm.FMTDateTime">创建时间</th>
        <th data-options="field:'_opt', formatter:WhgComm.FMTOpt,fixed:true, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>

</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">

    <div id="tb">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="add()">添加</a>
        <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
    </div>

    <div class="whgdg-tb-srch" style="padding-top: 8px">
        <input class="easyui-textbox" style="width: 200px;" name="title" data-options="prompt:'请输入名称', validType:'length[1,32]'" />

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="view">查看</a>
    <a href="javascript:void(0)" class="easyui-linkbutton"  plain="true" method="edit">编辑</a>
    <a href="javascript:void(0)" class="easyui-linkbutton"  plain="true" method="del">删除</a>

</div>
<!-- 操作按钮-END -->

<script>

    /**
     * 添加
     */
    function add(){
        WhgComm.editDialog('${basePath}/admin/show/jmd/view/edit?entid=${entid}');
    }

    /**
     * 编辑
     * @param idx
     */
    function edit(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/show/jmd/view/edit?entid=${entid}&id='+row.id);
    }

    /**
     * 查看
     * @param idx
     */
    function view(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/show/jmd/view/edit?targetShow=2&id='+row.id);
    }


    /**
     * 删除
     * @param idx
     */
    function del(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        var confireStr = '确定要删除选中的项吗？';
        if (row.isdel === 1){
            confireStr = '此操作将会永久性删除数据！'+ confireStr;
        }
        $.messager.confirm("确认信息", confireStr, function(r){
            if (r){
                $.messager.progress();
                $.post('${basePath}/admin/show/jmd/del', {id: row.id}, function(data){
                    $("#whgdg").datagrid('reload');
                    if (!data.success || data.success != "1"){
                        $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                    }
                    $.messager.progress('close');
                }, 'json');
            }
        })
    }

</script>
</body>
</html>
