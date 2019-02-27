<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>省市区管理</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
</head>
<body class="easyui-layout">
    <div data-options="region:'west',split:true,title:'区域',collapsible:false,tools:[{iconCls:'icon-reload', handler:reloadTree}]" style="width:300px;padding:10px;">
        <ul class="easyui-tree" id="mass_type_tree"></ul>
    </div>
    <div data-options="region:'center',title:'区域列表'">
        <!-- 表格 -->
        <table id="whgdg" title="" class="easyui-datagrid" style="display: none"
               data-options="border:0, fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:true, pagination:false, toolbar:'#whgdg-tb'">
            <thead>
            <tr>
                <th data-options="field:'name', width:100">名称</th>
                <th data-options="field:'code', width:100">编码</th>
                <th data-options="field:'idx', width:30">排序</th>
                <th data-options="field:'_opt', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
            </tr>
            </thead>
        </table>
        <!-- 表格 END -->

        <!-- 表格操作工具栏 -->
        <div id="whgdg-tb" style="display: none;">
            <div class="whgd-gtb-btn">
                <shiro:hasPermission name="${resourceid}:add"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="doAdd();">添加</a></shiro:hasPermission>
            </div>
            <div class="whgdg-tb-srch">
                <form id="whgdg-tb-srch-form">
                    <input type="hidden" id="pid" name="pid" value="ROOT"/>
                    <input class="easyui-combobox" style="width: 200px;" name="state" data-options="prompt:'请选择状态', editable:false, valueField:'id', textField:'text', data:WhgComm.getState()"/>
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
                </form>
            </div>
        </div>
        <!-- 表格操作工具栏-END -->

        <!-- 操作按钮 -->
        <div id="whgdg-opt" style="display: none;">
            <shiro:hasPermission name="${resourceid}:view"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="doSee">查看</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="0,1" method="doEdit">编辑</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:off"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="1"  method="doOff">停用</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:on">  <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="0"  method="doOn">启用</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:del"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="0" method="doDel">删除</a></shiro:hasPermission>
        </div>
        <!-- 操作按钮-END -->

    </div>
</body>



<!-- script -->
<script type="text/javascript">
    /**
     * 添加
     */
    function doAdd() {
        WhgComm.openDialog4size('添加区域', '${basePath}/admin/yunwei/area/view/add?pid='+$('#pid').val(), 800, 380);
    }

    /**
     * 编辑
     * @param idx 行下标
     */
    function doEdit(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.openDialog4size('编辑区域', '${basePath}/admin/yunwei/area/view/edit?id='+curRow.id, 800, 380);
    }

    /**
     * 查看
     * @param idx 行下标
     */
    function doSee(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.openDialog4size('查看区域', '${basePath}/admin/yunwei/area/view/edit?id='+curRow.id, 800, 380, {hideSubmitBtn:true,cancelBtnText:'关 闭'});
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
            url: getFullUrl('/admin/yunwei/area/updateState'),
            data: {id:ids, fromState:fromState, toState:toState},
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
     * 启用
     * @param idx 行下标
     */
    function doOn(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        _doUpdState(curRow.id, 0, 1);
    }

    /**
     * 停用
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
                    cache: false,
                    url: getFullUrl('/admin/yunwei/area/del'),
                    data: {id : curRow.id},
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

    //初始tree
    function initMassTypeTree(){
        WhgCommAreaTree.initAreaTree({
            eleId: 'mass_type_tree',
            selectFirstNode: true,
            onSelect: function(node, firstLevel, areaInfo){
                $('#pid').val(node.id);
                WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/yunwei/area/srchList');
            }
        });
    }

    //刷新Tree
    function reloadTree(){
        initMassTypeTree();
    }
    
    $(function () {
        initMassTypeTree();
    });
</script>
<!-- script END -->
</body>
</html>