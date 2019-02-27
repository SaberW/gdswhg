<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <title>线下培训新增订单</title>
</head>
<body>
<!-- 表格 -->
<table id="whgdg" title="用户列表" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:true, pagination:true, toolbar:'#whgdg-tb', url:'${basePath}/admin/train/enrol/userTraList?isrealname=1'">
    <thead>
    <tr>
        <th data-options="width: 30, checkbox: true, field:'checkbox' ">全选</th>
        <th data-options="field:'nickname', width:150">昵称</th>
        <th data-options="field:'phone', width:150">手机</th>
        <th data-options="field:'email', width:150">邮箱</th>
        <th data-options="field:'name', width:150">姓名</th>
    </tr>
    </thead>
</table>

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <div class="whgdg-tb-srch">
        <input class="easyui-combobox" id="param_traid" name="traid" prompt="请选择培训" data-options="editable:false,width:200,valueField:'id',textField:'title'
                 ,url:'${basePath}/admin/train/enrol/params/tralist?biz=${biz}'"/>

        <input class="easyui-textbox" name="name" style="width: 150px;"
               data-options="prompt:'请输入姓名',width:100,validType:'length[1,60]'"/>
        <input class="easyui-textbox" name="nickname" style="width: 150px;"
               data-options="prompt:'请输入昵称',width:100,validType:'length[1,60]'"/>
        <input class="easyui-textbox" name="phone" style="width: 150px;"
               data-options="prompt:'请输入手机',width:100,validType:'length[1,60]'"/>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search"
           onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
        <a href="javascript:void(0)" class="easyui-linkbutton randomcheckon" iconCls="icon-add" onclick="doQueren()">新增订单</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-undo" onclick="WhgComm.editDialogClose();">返回</a>
    </div>
</div>

<!-- 添加表单 END -->

<script>


    function doQueren(idx) {
        var rows = $('#whgdg').datagrid('getChecked');
        var paramTraid = $("#param_traid").combobox("getValue");
        if (paramTraid == "") {
            $.messager.alert("提示", '请选择需要操作的培训', 'warning');
            return;
        }
        if (rows.length < 1) {
            $.messager.alert('提示', '请选中要操作的记录！', 'warning');
            return;
        }
        var _ids = "";
        var spt = "";
        for (var i = 0; i < rows.length; i++) {
            _ids += spt + rows[i].id;
            spt = ',';
        }
        $.messager.confirm("确认信息", "确定要操作选中的项吗？", function (r) {
            if (r) {
                $.messager.progress();
                var params = {ids: _ids, traid: paramTraid};
                $.post('${basePath}/admin/train/enrol/addorder', params, function (data) {
                    $("#whgdg").datagrid('reload');
                    if (!data.success || data.success != "1") {
                        $.messager.alert("错误", data.errormsg || '操作失败', 'error');
                    } else {
                        $.messager.alert('提示', '已操作成功！', 'warning');
                    }
                    $.messager.progress('close');
                }, 'json');
            }
        })
    }

</script>
</body>
</html>
