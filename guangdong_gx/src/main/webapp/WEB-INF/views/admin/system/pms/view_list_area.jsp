<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>权限组管理</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <style>
        /*.gray{color: lightgrey;}*/
    </style>
</head>
<body>
<!-- 表格 -->
<table id="whgdg" title="权限组管理" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:true, pagination:true, toolbar:'#whgdg-tb', url:'${basePath}/admin/system/pms/srchList4p?sysflag=sysmgr'">
    <thead>
    <tr>
        <th data-options="field:'name', width:250">权限组名称</th>
        <th data-options="field:'type', width:100, sortable:true">权限组分类</th>
        <th data-options="field:'crtdate', width:130, formatter:WhgComm.FMTDateTime, sortable:true">创建时间</th>
        <th data-options="field:'state', width:50, formatter:WhgComm.FMTState, sortable:true">状态</th>
        <th data-options="field:'_opt', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <shiro:hasPermission name="${resourceid}:add">
    <div class="whgd-gtb-btn">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="doAdd('sysmgr');">添加权限组</a>
    </div>
    </shiro:hasPermission>

    <div class="whgdg-tb-srch">
        <form id="whgdg-tb-srch-form">
            <input class="easyui-textbox" style="width: 200px;" name="name" data-options="prompt:'请输入权限组名称', validType:'length[1,32]'" />
            <input class="easyui-combobox" style="width: 110px;" name="state" data-options="prompt:'请选择状态', editable:false, valueField:'id', textField:'text', data:WhgComm.getState()"/>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
        </form>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <shiro:hasPermission name="${resourceid}:view"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="doSee">查看</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="0,1" method="doEdit">编辑</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:off"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="1" method="doOff">停用</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:on"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="0" method="doOn">启用</a></shiro:hasPermission>
    <%--<shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="vaildScopeSite" method="doScopeSite">适用站点</a></shiro:hasPermission>--%>
    <shiro:hasPermission name="${resourceid}:del"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="0" method="doDel">删除</a></shiro:hasPermission>
</div>
<!-- 操作按钮-END -->

<!-- script -->
<script type="text/javascript">
    /** 业务管理系统的权限组可以配置适应站点 */
    function vaildScopeSite(idx){
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        return curRow.state == 1 && curRow.sysflag == 'bizmgr';
    }

    /** 业务管理系统-总分馆管理系统 */
    function getConsoleSystems(){
        return WhgSysData.getStateData("EnumConsoleSystem");
    }

    /**
     * 添加文化馆
     */
    function doAdd(sysflag) {
        WhgComm.editDialog('${basePath}/admin/system/pms/view/add?sysflag='+sysflag);
    }

    /**
     * 编辑信息
     * @param idx 行下标
     */
    function doEdit(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/system/pms/view/edit?id='+curRow.id);
    }

    /**
     * 查看资料
     * @param idx 行下标
     */
    function doSee(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/system/pms/view/edit?id='+curRow.id+"&onlyshow=1");
    }

    /** 适应站点 */
    function doScopeSite(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.openDialog4size('适应站点配置', '${basePath}/admin/system/pms/view/site?id='+curRow.id, 800, 600, {cancelBtnText:'关  闭',closeFun: function(){
            $('#whgdg').datagrid('reload');
        }});
    }

    /**
     * AJAX调用修改状态服务
     * @param ids 修改对象ID，多个用逗号分隔
     * @param fromState 修改前的状态
     * @param toState 修改后的状态
     * @private
     */
    function _doUpdState(ids, fromState, toState){
        $.ajax({
            cache: false,
            url: getFullUrl('/admin/system/pms/updstate'),
            data: {ids:ids, fromState:fromState, toState:toState},
            success: function (data) {
                if(data && data.success == '1'){
                    $('#whgdg').datagrid('reload');
                } else {
                    $.messager.alert('提示', '操作失败:'+data.errormsg+'！', 'error');
                }
            }
        });
    }

    /**
     * 单个启用
     * @param idx 行下标
     */
    function doOn(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        _doUpdState(curRow.id, 0, 1);
    }

    /**
     * 单个停用
     * @param idx 行下标
     */
    function doOff(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        _doUpdState(curRow.id, 1, 0);
    }

    /**
     * 删除
     * @param idx
     */
    function doDel(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        $.messager.confirm('提示', '您确定要删除此记录吗？', function(r){
            if (r){
                $.ajax({
                    type: "POST",
                    url: getFullUrl('/admin/system/pms/del'),
                    data: {ids : curRow.id},
                    success: function(Json){
                        if(Json && Json.success == '1'){
                            //$.messager.alert('提示', '操作成功！', 'info');
                            $('#whgdg').datagrid('reload');
                        } else {
                            $.messager.alert('提示', '操作失败:'+Json.errormsg+'！', 'error');
                        }
                    }
                });
            }
        });
    }
</script>
<!-- script END -->
</body>
</html>