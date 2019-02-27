<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>轮播图配置</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <style>
        .provinceLBT{
            color: #f5f5f5;
            background-color: #9c27b0;
            display: inline-block;
            margin-right: 5px;
        }
        .cityLBT{
            color: #f5f5f5;
            background-color: #FF5722;
            display: inline-block;
            margin-right: 5px;
        }
        .siteLBT{
            color: #f5f5f5;
            background-color: #00bcd4;
            display: inline-block;
            margin-right: 5px;
        }
    </style>
</head>
<body>

<!-- 表格 -->
<table id="whgdg" class="easyui-datagrid" style="display: none"
       data-options="border:false, fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb'">
    <thead>
    <tr>
        <th data-options="field:'ck', checkbox:true"></th>
        <th data-options="field:'name', width:350">标题</th>
        <th data-options="field:'picture', width:367, formatter:WhgComm.FMTImg">图片</th>
        <th data-options="field:'url'">连接地址</th>
    </tr>
    </thead>
</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <div class="whgdg-tb-srch">
        <input type="hidden" id="province" name="province" value="${param.province}"/>
        <input type="hidden" id="city" name="city" value="${param.city}"/>
        <input class="easyui-textbox" style="width: 200px;" name="name" data-options="prompt:'请输入轮播图标题', validType:'length[1,32]'" />
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<script>
/**
 * 保存为全省站轮播图或者全市站轮播图
 */
function saveFun(){
    var rows = $('#whgdg').datagrid('getChecked');
    if(rows.length < 1){
        $.messager.alert('警告', '选择至少一条记录！', 'warning');
        WhgComm.getOpenDialogSubmitBtn().one('click', saveFun);
    }else{
        var type = $('#province').val() != '' ? 1 : 2;
        var ids = [];
        for(var i=0; i<rows.length; i++){
            ids.push(rows[i].id);
        }
        $.ajax({
            type: "POST",
            url: "${basePath}/admin/yunwei/lbt/toarea",
            data: {type:type, id: ids.join(",")},
            success: function (data) {
                if (data.success == "1") {
                    window.parent.$('#whgdg').datagrid('reload');
                    WhgComm.editDialogClose();
                } else {
                    $.messager.alert("提示", data.errormsg);
                    WhgComm.getOpenDialogSubmitBtn().one('click', saveFun);
                }
            }
        });
    }
}

$(function () {
    //查询
    WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/yunwei/lbt/srchList4p4cult?type=${clazz}');

    //注册保存事件
    WhgComm.getOpenDialogSubmitBtn().one('click', saveFun);
});
</script>
</body>
</html>
