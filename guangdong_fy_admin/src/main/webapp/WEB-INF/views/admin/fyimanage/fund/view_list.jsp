<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">

    <title>专项资金列表</title>

    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
</head>
<body>
<!-- 表格 -->
<table id="whgdg" title="专项资金列表" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb',queryParams:{cultid: WhgComm.getMgrCults()[0].id}, url:'${basePath}/admin/fyi/fund/srchList4p'">
    <thead>
    <tr>
        <th data-options="field:'fundnum',width:80">资金编号</th>
        <th data-options="field:'matter',width:80">拨付事项名称</th>
        <th data-options="field:'etype',width:80">拨付资金类别</th>
        <th data-options="field:'area',width:80">区域</th>
        <th data-options="field:'cultid',formatter:WhgComm.FMTCult,width:40">单位名称</th>
        <th data-options="field:'money',width:80">补助金额</th>
        <th data-options="field:'funddate', width:60,formatter:WhgComm.FMTDateTime">拨付日期</th>
        <th data-options="field:'_opt', formatter:WhgComm.FMTOpt,fixed:true, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>

</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">

    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="add()">添加</a>


    <div class="whgdg-tb-srch" style="padding-top: 8px">

        <input class="easyui-textbox" style="width: 200px;" name="fundnum" data-options="prompt:'请输入编号', validType:'length[1,32]'" />
        <input class="easyui-textbox" style="width: 200px;" name="matter" data-options="prompt:'请输入项目名称', validType:'length[1,32]'" />
        <input class="easyui-datebox" style="width: 200px;" name="funddate" style="width:200px; height:32px" data-options="prompt:'请输入拨付日期'" />
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->
<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
   <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="view">查看</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="edit">编辑</a>
   <a href="javascript:void(0)" class="easyui-linkbutton"  plain="true" method="del">删除</a>

</div>
<!-- 操作按钮-END -->

<script>

    /**
     * 添加
     */
    function add(){
        WhgComm.editDialog('${basePath}/admin/fyi/fund/view/add');
    }

    /**
     * 编辑
     * @param idx
     */
    function edit(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/fyi/fund/view/add?id='+row.id);
    }

    /**
     * 查看
     * @param idx
     */
    function view(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/fyi/fund/view/add?show=1&id='+row.id);
    }


    /**
     * 删除
     * @param idx
     */
    function del(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        var confireStr = '确定要删除选中的项吗？';
        $.messager.confirm("确认信息", confireStr, function(r){
            if (r){
                $.messager.progress();
                $.post('${basePath}/admin/fyi/fund/del', {id: row.id}, function(data){
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
