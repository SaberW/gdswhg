<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>师资管理</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>
</head>
<body>
<!-- 表格 -->
<table id="whgdg" title="师资管理" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb', url:'${basePath}/admin/major/tea/srchList4p?mid=${mid}'">
    <thead>
    <tr>
        <th data-options="field:'name', width:100">老师名称</th>
        <th data-options="field:'teacherpic', width:100, formatter:WhgComm.FMTImg">老师照片</th>
        <th data-options="field:'province', width:100">省</th>
        <th data-options="field:'city', width:100">市</th>
        <th data-options="field:'area', width:100">区</th>
        <th data-options="field:'state', width:100, formatter:traStateFMT" >状态</th>
        <th data-options="field:'_opt', width:400, formatter:WhgComm.FMTOpt,fixed:true, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <div class="whgd-gtb-btn">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-undo" onclick="WhgComm.editDialogClose();">返回</a>
       <%-- <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="doAdd()">添加师资</a>--%>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="selAdd()">选择添加师资</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="view">查看</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="del">删除</a>
</div>
<!-- 操作按钮-END -->

<!--选择添加-->
<div id="whgwin-sign" style="display: none">
    <table id="course" title="课程信息" class="easyui-datagrid" style="display: none"
           data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true">
        <thead>
        <tr>
            <th data-options="field:'name', width:100">老师名称</th>
            <th data-options="field:'teacherpic', width:100, formatter:WhgComm.FMTImg">老师照片</th>
            <th data-options="field:'province', width:100">省</th>
            <th data-options="field:'city', width:100">市</th>
            <th data-options="field:'area', width:100">区</th>
            <th data-options="field:'state', width:100, formatter:traStateFMT" >状态</th>
            <th data-options="field:'_opt', width:100, formatter:WhgComm.FMTOpt,fixed:true, optDivId:'whgdg-opt2'">操作</th>
        </tr>
        </thead>
    </table>
</div>

<!-- 操作按钮 -->
<div id="whgdg-opt2" style="display: none;">
    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="_add">添加</a>
</div>
<!-- 操作按钮-END -->


<script>

/**
 * 添加课程
 * @param idx
 */
function doAdd(idx){
    WhgComm.editDialog('${basePath}/admin/train/tea/view/add?mid=${mid}');
}

/**
 * 查看
 * @param idx
 */
function view(idx){
    var row = $("#whgdg").datagrid("getRows")[idx];
    WhgComm.editDialog('${basePath}/admin/train/tea/view/add?targetShow=1&id='+row.tid);
}

/**
 * 删除
 * @param idx
 */
function del(idx){
    var row = $("#whgdg").datagrid("getRows")[idx];
    var confireStr = '确定要删除选中的项吗？'
    $.messager.confirm("确认信息", confireStr, function(r){
        if (r){
            $.messager.progress();
            $.post('${basePath}/admin/major/tea/del', {id: row.cid}, function(data){
                $("#whgdg").datagrid('reload');
                if (!data.success || data.success != "1"){
                    $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                }
                $.messager.progress('close');
            }, 'json');
        }
    })
}

/**
 * 选择添加课程
 * @param idx
 */
function selAdd(){
    $('#whgwin-sign').dialog({
        title: '课程列表',
        cache: false,
        modal: true,
        width: '800',
        height: '400',
        maximizable: true,
        resizable: true,
        buttons: '#whgwin-sign-btn',
        onOpen: function () {
            $("#course").datagrid({
                url: '${basePath}/admin/train/tea/srchTeaList?mid=${mid}',
            });
        }
    });
}

/**
 * 选择
 * @param idx
 */
function _add(idx){
    var row = $("#course").datagrid("getRows")[idx];
    var mid = '${mid}';
    var confireStr = '确定要添加选中的项吗？'
    $.messager.confirm("确认信息", confireStr, function(r){
        if (r){
            $.messager.progress();
            $.post('${basePath}/admin/major/tea/add', {entid: row.id,mid:mid}, function(data){
                $("#course").datagrid('reload');
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
