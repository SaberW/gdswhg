<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>部门管理</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>
    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
</head>
<body>
<!-- 表格 -->
<table id="whgdg" title="部门管理" class="easyui-treegrid" style="display: none"
       data-options="fit:true, rownumbers:true, fitColumns:true, singleSelect:true, loadFilter:myLoadFilter, idField:'id', treeField:'name', toolbar:'#whgdg-tb', queryParams:{cultid: WhgComm.getMgrCultsFirstId()}, url:'${basePath}/admin/system/dept/srchList4p'">
    <thead>
    <tr>
        <th data-options="field:'name', width:100">名称</th>
        <%--<th data-options="field:'code', width:50">编码</th>--%>
        <%--<th data-options="field:'summary', width:150">简介</th>--%>
        <th data-options="field:'crtdate', width:80, formatter:WhgComm.FMTDateTime ">创建时间</th>
        <th data-options="field:'_opt', formatter:WhgComm.FMTTreeGridOpt, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <div class="whgd-gtb-btn">
        <shiro:hasPermission name="${resourceid}:add"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="doAdd()">添加</a></shiro:hasPermission>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" onclick="queryDept()">刷新</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <shiro:hasPermission name="${resourceid}:add"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="_state" validVal="1" method="doAddChild">添加</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:view"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true"  method="doView">查看</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="_state" validVal="0" method="doEdit">编辑</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:on"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="_state" validVal="0" method="doOn">启用</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:off"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="_state" validVal="1" method="doOff">停用</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:del"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validDel" method="doDel">删除</a></shiro:hasPermission>
</div>
<!-- 操作按钮-END -->

<!-- 添加编辑表单 -->
<div id="whgwin" style="display: none">
    <form id="whgff" class="whgff" method="post">
        <input type="hidden" name="id" value="" >
        <input type="hidden" name="pid" value="" >
        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 25%"><i>*</i>文化馆　：</div>
            <div class="whgff-row-input" style="width: 75%">
                <input type="hidden" id="cultid" name="cultid" value=""/>
                <input class="easyui-combobox" style="width:550px; height:32px" name="whgff_cultid" id="whgff_cultid" data-options="required:true, editable:false, prompt:'请选择文化馆', valueField:'id', textField:'text', data:WhgComm.getMgrAllCults()"/>
            </div>
        </div>
        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 25%"><i>*</i>名　　称：</div>
            <div class="whgff-row-input" style="width: 75%"><input class="easyui-textbox" id="whgff-row-input-name" name="name" style="width:550px; height:32px" data-options="prompt:'请输入部门名称,3到30字字符', required:true, validType:'length[3,30]'"></div>
        </div>

        <div class="whgff-row deptType" style="display: none;">
            <div class="whgff-row-label" style="width: 25%"><i></i>部门类型：</div>
            <div class="whgff-row-input" style="width: 75%">
                <div class="radio radio-primary whg-js-data" name="isfront" id="isfront"
                     js-data='[{"id":"0","text":"部门"},{"id":"1","text":"分馆"}]'>
                </div>
            </div>
        </div>

        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 25%">简　　介：</div>
            <div class="whgff-row-input" style="width: 75%">
                <input class="easyui-textbox" id="whgff-row-input-summary" name="summary" style="width:550px; height:80px" data-options="multiline:true, prompt:'请输入简介', required:false, validType:'length[0,2000]'">
            </div>
        </div>
    </form>
</div>
<div id="whgwin-btn" style="text-align: center; display: none">
    <div style="display: inline-block; margin: 0 auto">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="whgwin-btn-save">保 存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#whgwin').dialog('close')">关 闭</a>
    </div>
</div>
<!-- 添加编辑表单 END -->

<!-- script -->
<script type="text/javascript">
    /**
     * 查询部门
     **/
    function queryDept(){
        $('#whgdg').treegrid('reload');
    }

    /**
     * treeGrid数据中增加_parentId和_state属性
     */
    function myLoadFilter(data, parentId) {
        var rows = data.rows;
        for(var i=0; i<rows.length; i++){
            if(rows[i].pid && rows[i].pid != ''){
                rows[i]._parentId = rows[i].pid;
            }
            rows[i]._state = rows[i].state;
        }
        return data;
    }

    /**
     * 初始表单
     */
    function __initForm(action){
        $('#whgff').form({
            novalidate: true,
            url : action,
            onSubmit : function(param) {
                var _valid = $(this).form('enableValidation').form('validate');
                if(_valid){
                    $.messager.progress();
                }else{
                    //失败时再注册提交事件
                    $('#whgwin-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
                }
                return _valid;
            },
            success : function(data) {
                $.messager.progress('close');
                var Json = eval('('+data+')');
                if(Json && Json.success == '1'){
                    $('#whgdg').treegrid('reload');
                    $('#whgwin').dialog('close');
                } else {
                    $.messager.alert('提示', '操作失败:'+Json.errormsg+'！', 'error');
                    $('#whgwin-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
                }
            }
        });
    }

    /**
     * 添加部门
     */
    function doAdd() {
        __initForm('${basePath}/admin/system/dept/add');
        $('#whgff').form('clear');
        $('#whgff_cultid').combobox('readonly', false);
        $('#whgff_cultid').combobox('setValue', WhgComm.getMgrCultsFirstId());
        $('#whgff_cultid').combobox('disable');
        $('#cultid').val(WhgComm.getMgrCultsFirstId());
        $("input:radio[name='isfront'][value='0']").prop("checked", "checked");
        $('#whgwin-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
        if(managerCults[0].level == "3"){
            $('.deptType').show();
        }
        $('#whgwin').dialog({
            title: '部门管理-添加部门',
            modal: true,
            width: 800,
            height: 400,
            buttons: '#whgwin-btn'
        });
    }

    /**
     * 添加子部门
     */
    function doAddChild(pid) {
        var node = $('#whgdg').treegrid('find', pid);
        __initForm('${basePath}/admin/system/dept/add');
        $('#whgff').form('clear');
        $('#whgff input[name="pid"]').val(pid);
        if(node.cultid){
            $('#whgff_cultid').combobox('setValue', node.cultid);
            $('#whgff_cultid').combobox('readonly');
            $('#whgff_cultid').combobox('disable');
            $('#cultid').val(node.cultid);
        }
        $('#whgwin-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
        $("input:radio[name='isfront'][value='0']").prop("checked", "checked");
        if(managerCults[0].level == "3"){
            $('.deptType').show();
        }
        $('#whgwin').dialog({
            title: '部门管理-添加【'+node.name+'】的子部门',
            modal: true,
            width: 800,
            height: 400,
            buttons: '#whgwin-btn'
        });
    }

    /**
     * 是否禁用删除按钮
     */
    function validDel(id) {
        var node = $('#whgdg').treegrid('find', id);
        if(typeof node.children == 'undefined'){
            return true;
        }
        return false;
    }

    /**
     * 编辑信息
     * @param idx 行下标
     */
    function doEdit(id) {
        var node = $('#whgdg').treegrid('find', id);
        __initForm('${basePath}/admin/system/dept/edit');
        $('#whgff').form('clear');
        $('#whgff input[name="id"]').val(id);
        $('#whgff input[name="pid"]').val(node.pid || '');
        if(node.cultid){
            $('#whgff_cultid').combobox('setValue', node.cultid);
            $('#whgff_cultid').combobox('readonly');
            $('#whgff_cultid').combobox('disable');
            $('#cultid').val(node.cultid);
        }
        var _isfront = node.isfront || "0";
        $("input:radio[name='isfront'][value='"+_isfront+"']").prop("checked", "checked");
        $('#whgff-row-input-name').textbox('setValue', node.name);
        $('#whgff-row-input-summary').textbox('setValue', node.summary);
        $('#whgwin-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
        if(managerCults[0].level == "3"){
            $('.deptType').show();
        }
        $('#whgwin').dialog({
            title: '部门管理-编辑部门资料',
            modal: true,
            width: 800,
            height: 400,
            buttons: '#whgwin-btn'
        });
    }

    /**
     * 查看信息
     * @param idx 行下标
     */
    function doView(id) {
        var node = $('#whgdg').treegrid('find', id);
        __initForm('${basePath}/admin/system/dept/edit');
        $('#whgff').form('clear');
        $('#whgff input[name="id"]').val(id);
        if(node.cultid){
            $('#whgff_cultid').combobox('setValue', node.cultid);
            $('#whgff_cultid').combobox('readonly');
        }
        var _isfront = node.isfront || "0";
        $("input:radio[name='isfront'][value='"+_isfront+"']").prop("checked", "checked");
        $('#whgff input[name="pid"]').val(node.pid || '');
        $('#whgff-row-input-name').textbox('setValue', node.name);
        $('#whgff-row-input-summary').textbox('setValue', node.summary);
        $('#whgff-row-input-name').textbox('readonly',true);
        $('#whgff-row-input-summary').textbox('readonly', true);
        $("#whgwin-btn-save").hide();
       // $('#whgwin-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
        if(managerCults[0].level == "3"){
            $('.deptType').show();
        }
        $('#whgwin').dialog({
            title: '部门管理-编辑部门资料',
            modal: true,
            width: 800,
            height: 400,
            buttons: '#whgwin-btn',
            onClose: function () {
                $("#whgwin-btn-save").show();
                $('#whgff-row-input-name').textbox('readonly',false);
                $('#whgff-row-input-summary').textbox('readonly', false);
            }
        });

    }

    /**
     * 删除
     * @param idx
     */
    function doDel(id) {
        $.messager.confirm('提示', '您确定要删除此记录吗？', function(r){
            if (r){
                $.ajax({
                    type: "POST",
                    url: '${basePath}/admin/system/dept/del',
                    data: {ids : id},
                    cache: false,
                    success: function(data){
                        //var Json = eval('('+data+')');
                        if(data.success == '1'){
                            $('#whgdg').treegrid('reload');
                        } else {
                            $.messager.alert('提示', '操作失败:'+Json.errormsg+'！', 'error');
                        }
                    }
                });
            }
        });
    }

    function doOn(idx) {
        var node = $('#whgdg').treegrid('find', idx);
        _upState(node.id,0,1);
    }
    function doOff(idx) {
        var node = $('#whgdg').treegrid('find', idx);
        _upState(node.id,1,0);
    }
    function _upState(id, fromState, toState) {
        $.ajax({
            url: getFullUrl('/admin/system/dept/updstate'),
            data: {ids:id, fromState:fromState, toState:toState},
            cache: false,
            success: function (data) {
                if(data && data.success == '1'){
                    $('#whgdg').treegrid('reload');
                } else {
                    $.messager.alert('提示', '操作失败:'+data.errormsg+'！', 'error');
                }
            }
        });
    }

</script>
<!-- script END -->
</body>
</html>