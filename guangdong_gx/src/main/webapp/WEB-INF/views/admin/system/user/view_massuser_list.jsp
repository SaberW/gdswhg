<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>管理员管理</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
    <style>
        .loginUser{
            color: #f5f5f5;
            background-color: #00438a;
            display: inline-block;
            margin-left: 5px;
        }
        .superUser{
            color: #f5f5f5;
            background-color: #FF5722;
            display: inline-block;
            margin-right: 5px;
        }
        .normalUser{
            color: #f5f5f5;
            background-color: #00BCD4;
            display: inline-block;
            margin-right: 5px;
        }
    </style>
</head>
<body>
<!-- 表格 -->
<table id="whgdg" title="管理员管理" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:true, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb', queryParams:{cultid:WhgComm.getMgrCultsFirstId()}, url:'${basePath}/admin/system/user/srchList4p?admintype=masmgr'">
    <thead>
    <tr>
        <th data-options="field:'account', width:250, formatter:FMTAccount">账号</th>
        <th data-options="field:'contactnum', width:100">手机号</th>
        <th data-options="field:'lastdate', width:130, formatter:WhgComm.FMTDateTime">最后登录时间</th>
        <th data-options="field:'state', width:60, formatter:WhgComm.FMTState">状态</th>
        <th data-options="field:'_opt', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <div class="whgd-gtb-btn">
        <shiro:hasPermission name="${resourceid}:add"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="doAdd();">添加管理员</a></shiro:hasPermission>
        <%--     <shiro:hasPermission name="${resourceid}:off"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-no" onclick="doBatchUpd(1,0);">批量停用</a></shiro:hasPermission>
             <shiro:hasPermission name="${resourceid}:on"> <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doBatchUpd(0,1);">批量启用</a></shiro:hasPermission>
        --%>
    </div>
    <div class="whgdg-tb-srch">
        <input class="easyui-textbox" style="width: 200px;" name="account" data-options="prompt:'请输入管理员账号', validType:'length[1,32]'" />
        <input class="easyui-textbox" style="width: 200px;" name="contactnum" data-options="prompt:'请输入手机号', validType:'length[1,11]'" />
        <input class="easyui-combobox" style="width: 200px;" name="state" data-options="prompt:'请选择状态', editable:false, valueField:'id', textField:'text', data:WhgComm.getState()"/>
        <input class="easyui-combobox" style="width: 200px;display: none" id="cultid" name="cultid" data-options="prompt:'请选择文化馆',readonly:true, value:WhgComm.getMgrCultsFirstId(), valueField:'id', textField:'text', data:WhgComm.getMgrCults()"/>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <shiro:hasPermission name="${resourceid}:view"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="doSee">查看</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validEdit" method="doEdit">编辑</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:permissionsallocation">
        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="doPower">权限分配</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:off"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validOff" method="doOff">停用</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:on">  <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validOn" method="doOn">启用</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:del"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validDel" method="doDel">删除</a></shiro:hasPermission>
</div>
<!-- 操作按钮-END -->

<!-- script -->
<script type="text/javascript">
    function FMTAccount(val, row, idx){
        var html = '';
        var html2 = '';
        if(row.id == '${sessionAdminUser.id}'){
            html2 += '<i class="loginUser">登录账号</i>';
        }
        if(row.isbizmgr == 1){
            html += '<i class="superUser">超级账号</i>';
        }else{
            html += '<i class="normalUser"></i>';
        }
        return html+val+html2;
    }

    function validEdit(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        return curRow.state == 0 && curRow.id != '${sessionAdminUser.id}' && curRow.isbizmgr != 1;
    }
    
    function validOff(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        return curRow.state == 1 && curRow.id != '${sessionAdminUser.id}' && curRow.isbizmgr != 1;
    }
    
    function validOn(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        return curRow.state == 0 && curRow.id != '${sessionAdminUser.id}' && curRow.isbizmgr != 1;
    }

    function validDel(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        return curRow.state == 0 && curRow.id != '${sessionAdminUser.id}' && curRow.isbizmgr != 1;
    }

    /**
     * 添加文化馆
     */
    function doAdd() {
        var cultname = $('#cultid').combobox('getText');
        WhgComm.editDialog('${basePath}/admin/system/user/view/add?admintype=masmgr&isbizmgr=0&cultid='+WhgComm.getMgrCultsFirstId()+'&cultname='+encodeURIComponent(cultname));
    }

    /**
     * 编辑信息
     * @param idx 行下标
     */
    function doEdit(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/system/user/view/edit?id='+curRow.id);
    }

    /**
     * 查看资料
     * @param idx 行下标
     */
    function doSee(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/system/user/view/edit?id='+curRow.id+"&onlyshow=1");
    }

    /**
     * 权限分配
     */
    function doPower(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/system/user/view/powerallot_list?id='+curRow.id+"&account="+curRow.account);
    }

    /**
     * 批量启用或停用
     * @param fromState 修改前的状态
     * @param toState 修改后的状态
     */
    function doBatchUpd(fromState, toState) {
        //选中的记录数
        var rows = $('#whgdg').datagrid('getChecked');
        if(rows.length < 1){
            $.messager.alert('提示', '请选中要操作的记录！', 'warning');
        }
        var _ids = ""; var spt = "";
        for(var i=0; i<rows.length; i++){
            _ids += spt+rows[i].id;
            spt = ',';
        }
        _doUpdState(_ids, fromState, toState);
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
            url: getFullUrl('/admin/system/user/updstate'),
            data: {ids:ids, fromState:fromState, toState:toState},
            cache: false,
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
                    url: getFullUrl('/admin/system/user/del'),
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