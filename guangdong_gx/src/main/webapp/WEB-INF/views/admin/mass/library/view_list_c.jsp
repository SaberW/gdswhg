<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>表单属性扩展管理</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
</head>
<body class="easyui-layout">
    <div data-options="region:'west',split:true,title:'群文资源类型',collapsible:false,tools:[{iconCls:'icon-reload', handler:reloadTree}]" style="width:300px;padding:10px;">
        <ul class="easyui-tree" id="mass_type_tree"></ul>
    </div>
    <div data-options="region:'center',title:'群文资源库'">
        <!-- 表格 -->
        <table id="whgdg" class="easyui-datagrid" style="display: none"
               data-options="border:0, fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:true, pagination:true, toolbar:'#whgdg-tb'">
            <thead>
            <tr>
                <th data-options="field:'name', width:300">名称</th>
                <th data-options="field:'resourcetype', width:60, formatter:WhgComm.FMTResourceType">资源类型</th>
                <th data-options="field:'tablename', width:150">代码</th>
                <th data-options="field:'cultname', width:150">所属文化馆</th>
                <th data-options="field:'rescount', width:50">资源数</th>
                <th data-options="field:'ressize', width:100, formatter:FMTRessize">容量</th>
                <%--<th data-options="field:'memo', width:300">备注</th>--%>
                <th data-options="field:'idx', width:50, sortable:true">排序</th>
                <th data-options="field:'crtdate', width:140, formatter:WhgComm.FMTDateTime, sortable:true">创建日期</th>
                <th data-options="field:'state', width:50, formatter:WhgComm.FMTState, sortable:true">状态</th>
                <th data-options="field:'_opt', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
            </tr>
            </thead>
        </table>
        <!-- 表格 END -->

        <!-- 表格操作工具栏 -->
        <div id="whgdg-tb" style="display: none;">
            <div class="whgd-gtb-btn">
                <shiro:hasPermission name="${resourceid}:add"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="doAdd();">添加资源库</a></shiro:hasPermission>
            </div>
            <div class="whgdg-tb-srch">
                <form id="whgdg-tb-srch-form">
                    <input type="hidden" id="resourcetype" name="resourcetype"/>
                    <input class="easyui-textbox" style="width: 200px;" name="name" data-options="prompt:'请输入资源库名称', valieType:'length[1,32]'"/>
                    <input class="easyui-combobox" style="width: 200px;" name="state" data-options="prompt:'请选择状态', valueField:'id', textField:'text', data:WhgComm.getState()"/>
                    <input class="easyui-combobox" style="width: 200px;" name="cultid" id="cultid" data-options="prompt:'请选择文化馆', valueField:'id', textField:'text'"/>
                    <input class="easyui-combobox" style="width: 200px;" name="deptid" id="deptid" data-options="prompt:'请选择部门', valueField:'id', textField:'text'"/>
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

<!-- script -->
<script type="text/javascript">
    /**
     * 添加
     */
    function doAdd() {
        var resourcetype = $('#resourcetype').val();
        if(resourcetype == ''){
            $.messager.alert('提示', '请选择群文资源类型！', 'warning');
        }else {
            WhgComm.editDialog('${basePath}/admin/mass/library/view/add?resourcetype='+resourcetype);
        }
    }

    /**
     * 编辑
     * @param idx 行下标
     */
    function doEdit(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/mass/library/view/edit?id='+curRow.id);
    }

    /**
     * 查看
     * @param idx 行下标
     */
    function doSee(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/mass/library/view/edit?id='+curRow.id+"&onlyshow=1");
    }

    /**
     * AJAX调用修改状态服务
     * @param id 修改对象ID
     * @param fromState 修改前的状态
     * @param toState 修改后的状态
     * @private
     */
    function _doUpdState(id, fromState, toState){
        $.ajax({
            type: "POST",
            url: getFullUrl('/admin/mass/library/updstate'),
            data: {id:id, fromState:fromState, toState:toState},
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
    function doDel(idx, force) {
        force = force || '0';//强制删除
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        if(force == '1'){
            doAjax(force);
        }else{
            $.messager.confirm('提示', '您确定要删除此记录吗？', function(r){ if (r){doAjax(force);} });
        }
        function doAjax(force){
            $.ajax({
                type: "POST",
                cache: false,
                url: getFullUrl('/admin/mass/library/del'),
                data: {id : curRow.id, force:force},
                success: function(Json){
                    if(Json && Json.success == '1'){
                        $('#whgdg').datagrid('reload');
                    } else {
                        if(force == '0'){
                            $.messager.confirm('提示', '删除失败:'+Json.errormsg+'！，您要强制删除此记录吗？', function(r){ if(r){ doDel(idx,"1"); } });
                        }else{
                            $.messager.alert('提示', '删除失败:'+Json.errormsg+'！', 'error');
                        }
                    }
                }
            });
        }

    }

    //初始tree
    function initMassTypeTree(){
        //通过枚举定义资源类型
        var treeData = WhgComm.getResourceTypeData();
        for(var i=0; i<treeData.length; i++){
            var rowData = treeData[i];
            rowData['state'] = 'open';
        }

        $('#mass_type_tree').tree({
            data: treeData,
            animate: true,
            lines: true,
            onLoadSuccess: function (node, data) {
                //第一次加载完成后，默认选择第一个节点
                if(typeof(window.__initPage__) == "undefined" && data.length > 0){
                    var n = $('#mass_type_tree').tree('find', data[0].id);
                    $('#mass_type_tree').tree('select', n.target);
                    __initPage__ = true;
                }
            },
            onSelect: function(node){
                $('#resourcetype').val(node.id);
                WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/mass/library/srchList4p');
            }
        });
    }

    //刷新Tree
    function reloadTree(){
        window.__initPage__ = undefined;
        $('#mass_type_tree').tree('reload');
    }

    $(function () {
        //初始文化馆和权限
        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'${library.cultid}', cultEditable:true, allcult:true,
            deptEid:'deptid', deptValue:'${library.deptid}', alldept:true
        });

        //初始树
        initMassTypeTree();
    });

    /** 格式化大小 */
    function FMTRessize(val, rowData, rowIdx) {
        if(!val) return '';
        var number = new Number(val);
        if(number / 1024 < 1024){
            return (number / 1024).toFixed(2)+"KB";
        }else if(number / 1024 / 1024 < 1024){
            return (number / 1024 / 1024).toFixed(2)+"MB";
        }else{
            return (number / 1024 / 1024 / 1024).toFixed(2)+"GB";
        }
        return '';
    }

</script>
<!-- script END -->
</body>
</html>