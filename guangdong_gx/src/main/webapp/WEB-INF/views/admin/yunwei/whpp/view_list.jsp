<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>文化品牌配置</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
</head>
<body>
<!-- 表格 -->
<table id="whgdg" title="文化品牌配置" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb', queryParams:{cultid:WhgComm.getMgrCultsFirstId()}, url:'${basePath}/admin/yunwei/whpp/srchList4p'">
    <thead>
    <tr>
        <th data-options="field:'ck', checkbox:true"></th>
        <th data-options="field:'name', width:160">名称</th>
        <th data-options="field:'picture', width:160 , formatter:WhgComm.FMTImg">图片</th>
        <%--<th data-options="field:'bgcolour', width:160">背景颜色</th>--%>
        <th data-options="field:'state', width:160, formatter:WhgComm.FMTState">状态</th>
        <th data-options="field:'_opt', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <div class="whgd-gtb-btn">
        <shiro:hasPermission name="${resourceid}:add"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="doAdd();">添加文化品牌</a></shiro:hasPermission>
        <shiro:hasPermission name="${resourceid}:off"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-no" onclick="doBatchUpd(1,0);">批量停用</a></shiro:hasPermission>
        <shiro:hasPermission name="${resourceid}:on"> <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doBatchUpd(0,1);">批量启用</a></shiro:hasPermission>
    </div>
    <div class="whgdg-tb-srch">
        <input class="easyui-combobox" name="cultid" id="cultid" prompt="请选择文化馆" panelHeight="auto" limitToList="true"
                data-options="width:180, valueField:'id', textField:'text', data:WhgComm.getMgrCults(), value:WhgComm.getMgrCults()[0].id">
        <input class="easyui-textbox" style="width: 200px;" name="name" data-options="prompt:'请输入品牌名称', validType:'length[1,32]'" />
        <input class="easyui-combobox" style="width: 200px;" name="state" data-options="prompt:'请选择状态', value:'', valueField:'id', textField:'text', data:WhgComm.getState()"/>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <shiro:hasPermission name="${resourceid}:view"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="doSee">查看</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="0" method="doEdit">编辑</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:off"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="1" method="doOff">停用</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:on">  <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="0" method="doOn">启用</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:del"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="0" method="doDel">删除</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:ziyuan"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="currResource">资源管理</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:view"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="resource">查看活动资源</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:info"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="infos">资讯管理</a></shiro:hasPermission>
</div>
<!-- 操作按钮-END -->

<!-- 添加表单 -->
<div id="whgwin-add"></div>
<div id="whgwin-add-btn" style="text-align: center; display: none">
    <div style="display: inline-block; margin: 0 auto">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="whgwin-add-btn-save">保 存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#whgwin-add').dialog('close')">关 闭</a>
    </div>
</div>
<!-- 添加表单 END -->

<!-- 编辑表单 -->
<div id="whgwin-edit"></div>
<!-- 编辑表单 END -->


<div id="whgwin-view"></div>

<!-- script -->
<script type="text/javascript">
    /**
     * 资源管理
     * @param idx
     */
    function resource(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        //var cultid = $('#cultid').combobox('getValue');
        //WhgComm.editDialog('${basePath}/admin/resource/view/list?isbrand=2&reftype=35&refid=' + row.id+'&cultid='+cultid);
        WhgComm.editDialog('${basePath}/admin/mass/resmanage/view/list?refid=' + row.id+'&reftype=35');
    }

    /**
     * 品牌的资源管理
     */
    function currResource(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/mass/resmanage/view/list?refid=' + row.id+'&reftype=135');
    }

    function infos(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/info/view/list?entityid='+row.id+'&entity=2&cultid='+row.cultid+'&deptid='+row.deptid);
    }

    /**
     * 添加文化品牌
     */
    function doAdd() {
        WhgComm.editDialog('${basePath}/admin/yunwei/whpp/view/add');
    }

    /**
     * 编辑信息
     * @param idx 行下标
     */
    function doEdit(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/yunwei/whpp/view/edit?id='+curRow.id);
    }

    /**
     * 查看资料
     * @param idx 行下标
     */
    function doSee(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/yunwei/whpp/view/edit?targetShow=1&id='+curRow.id+"&onlyshow=1");
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
            url: getFullUrl('/admin/yunwei/whpp/updstate'),
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
                    url: getFullUrl('/admin/yunwei/whpp/del'),
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

</script>
<!-- script END -->
</body>
</html>