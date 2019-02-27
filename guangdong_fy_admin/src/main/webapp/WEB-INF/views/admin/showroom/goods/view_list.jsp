<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>
    <title>非遗展馆作品管理</title>
    <script src="${basePath}/static/admin/js/whgmodule-gridopts.js"></script>
</head>
<body>
<!-- 表格 -->
<table id="whgdg" title="非遗展馆作品管理" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb', url:'${basePath}/admin/fygoods/goodsList?showroomid='+'${showRoomId}'">
    <thead>
    <tr>
        <th data-options="field:'title', width:60">名称</th>
        <th data-options="field:'author', width:60">作者</th>
        <th data-options="field:'image', width:60 ,formatter:WhgComm.FMTImg">作品图片</th>
        <th data-options="field:'crtdate', width:60 ,formatter:datetimeFMT">创建时间</th>
        <th data-options="field:'state',width:60, sortable:true,formatter:WhgComm.FMTState">状态</th>
        <th data-options="field:'_opt', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <div>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add"  onclick="add();" data-options="size:'small'">添加</a>
        <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
    </div>
    <div style="padding-top: 5px">
        <label>
            <input class="easyui-textbox" name="title" style="width:200px" data-options="prompt:'请输作品名称', validType:'length[1,32]'"/>
        </label>
        <label>
            <input class="easyui-textbox" name="author" style="width:200px" data-options="prompt:'请输作者名称', validType:'length[1,32]'"/>
        </label>
        <input class="easyui-combobox" style="width: 200px;" name="state" panelHeight="auto" limitToList="true"
               data-options="prompt:'请选择状态', value:'', valueField:'id', textField:'text', data:[{id:'0',text:'停用'},
                    {id:'1',text:'启用'}]"/>
        <a href="javascript:void(0)" class="easyui-linkbutton tb_search" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <a href="javascript:void(0)"  method="lookAll">查看</a>
    <a href="javascript:void(0)" validKey="state" validVal="0" method="edit">编辑</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" validKey="state" validVal="0" method="doOn">启用</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" validKey="state" validVal="1" method="doOff" >停用</a>
    <a href="javascript:void(0)" validKey="state" validVal="0"  method="del">删除</a>
</div>
<!-- 操作按钮-END -->

<!-- 添加表单 -->
<div id="frm" class="none" style="display: none" data-options="	maximized:true">
</div>
<div id="whgwin-add"></div>
<div id="whgwin-add-btn" style="text-align: center; display: none">xq
    <div style="display: inline-block; margin: 0 auto">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="whgwin-add-btn-save">保 存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#whgwin-add').dialog('close')">关 闭</a>
    </div>
</div>
<!-- 添加表单 END -->

<!-- script -->
<script type="text/javascript">

    /**
     * 添加
     */
    function add() {
        WhgComm.editDialog('${basePath}/admin/fygoods/view/add?showRoomId='+${showRoomId});
    }

    /**
     * 编辑
     * @param idx 行下标
     */
    function edit(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/fygoods/view/edit?id='+curRow.id);
    }

    /**
     * 查看
     * @param idx 行下标
     */
    function lookAll(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/fygoods/view/edit?targetShow=1&id='+curRow.id);
    }

    /**
     * 启用
     * @param idx
     */
    function doOn(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要启用选中的项吗？", function(r){
            if (r){
                __updrecommend(row.id, 1);
            }
        })
    }

    /**
     * 停用
     * @param idx
     */
    function doOff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要停用选中的项吗？", function(r){
            if (r){
                __updrecommend(row.id, 0);
            }
        })
    }

    /**
     * AJAX调用修改状态服务
     */
    function __updrecommend(ids, toState) {
        $.messager.progress();
        var params = {ids: ids, toState: toState};
        $.post('${basePath}/admin/fygoods/updCommend', params, function(data){
            $("#whgdg").datagrid('reload');
            if (!data.success || data.success != "1"){
                $.messager.alert("错误", data.errormsg||'操作失败', 'error');
            }
            $.messager.progress('close');
        }, 'json');
    }

    /**
     * 发布 [1,2,4]->6
     * @param idx
     */
    function publish(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要发布选中的项吗？", function(r){
            if (r){
                __updStateSend(row.id, "1,2,4", 6);
            }
        })
    }

    /**
     * 删除
     * @param idx
     */
    function del(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        $.messager.confirm('提示', '您确定要删除此记录吗？', function(r){
            if (r){
                $.ajax({
                    type: "POST",
                    url: getFullUrl('/admin/fygoods/del'),
                    data: {id : curRow.id},
                    success: function(Json){
                        if(Json && Json.success == '1'){
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