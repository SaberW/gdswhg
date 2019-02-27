<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>课程管理</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>
</head>
<body>
<!-- 表格 -->
<table id="whgdg" title="课程管理" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb', url:'${basePath}/admin/major/tra/srchList4p?mid=${mid}'">
    <thead>
    <tr>
        <th data-options="field:'title', width:100">课程名称</th>
        <th data-options="field:'islive', width:100, formatter:isliveFMT">培训类型</th>
        <th data-options="field:'isterm', width:100, formatter:yesNoFMT">学期制</th>
        <th data-options="field:'ismultisite', width:100, formatter:typeFMT">场次类别</th>
        <%--<th data-options="field:'crtdate', width:100, formatter:WhgComm.FMTDateTime">创建时间</th>--%>
        <th data-options="field:'state', width:100, formatter:WhgComm.FMTBizState" >状态</th>
        <th data-options="width:100, sortable: true, field:'statemdfdate', formatter:WhgComm.FMTDateTime ">操作时间</th>
        <th data-options="field:'_opt', width:300, formatter:WhgComm.FMTOpt,fixed:true, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <div class="whgd-gtb-btn">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-undo" onclick="WhgComm.editDialogClose();">返回</a>
        <%--<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="doAdd()">添加课程</a>--%>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="selAdd()">选择添加</a>
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
    <table id="course" title="" class="easyui-datagrid" style="display: none"
           data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true">
        <thead>
        <tr>
            <th data-options="field:'title', width:100">课程名称</th>
            <th data-options="field:'islive', width:100, formatter:isliveFMT">培训类型</th>
            <th data-options="field:'isterm', width:100, formatter:yesNoFMT">学期制</th>
            <th data-options="field:'ismultisite', width:100, formatter:typeFMT">场次类别</th>
            <th data-options="field:'crtdate', width:100, formatter:WhgComm.FMTDateTime">创建时间</th>
            <th data-options="field:'state', width:100, formatter:WhgComm.FMTBizState" >状态</th>
            <th data-options="width:150, sortable: true, field:'statemdfdate', formatter:WhgComm.FMTDateTime ">操作时间</th>
            <th data-options="field:'_opt', width:100, formatter:WhgComm.FMTOpt,fixed:true, optDivId:'whgdg-opt2'">操作</th>
        </tr>
        </thead>
    </table>
</div>

<!-- 操作按钮 -->
<div id="whgdg-opt2" style="display: none;">
    <a href="javascript:void(0)" class="easyui-linkbutton" style="color: #0e0e0e; font-weight: bold"  plain="true" method="_add">添加</a>
</div>
<!-- 操作按钮-END -->


<script>

function typeFMT(val, rowData, index){
    switch(val){
        case 0 : return "单场";
        case 1 : return "多场";
        case 2 : return "固定场";
        default : return val;
    }
}

function isliveFMT(val, rowData, index){
    switch(val){
        case 0 : return "线下培训";
        case 1 : return "在线课程";
        default : return val;
    }
}


/**
 * 添加课程
 * @param idx
 */
function doAdd(idx){
    WhgComm.editDialog('${basePath}/admin/train/view/add?mid=${mid}');
}

/**
 * 查看
 * @param idx
 */
function view(idx){
    var row = $("#whgdg").datagrid("getRows")[idx];
    WhgComm.editDialog('${basePath}/admin/train/view/add?targetShow=1&id='+row.traid);
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
            $.post('${basePath}/admin/major/tra/del', {id: row.cid}, function(data){
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
                url: '${basePath}/admin/train/srchTraList?mid=${mid}',
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
            $.post('${basePath}/admin/major/tra/add', {entid: row.id,mid:mid}, function(data){
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
