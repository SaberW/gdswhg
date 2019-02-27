<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <title>供需展览展示列表</title>

    <script src="${basePath}/static/admin/js/whgmodule-gridopts.js"></script>
</head>
<body class="easyui-layout">
<div data-options="region:'west',title:'文化馆',split:true, width:'20%'">
    <div class="easyui-tree" id="mass_type_tree"></div>
</div>

<div data-options="region:'center',title:'供需展览展示列表'">
    <table id="whgdg" class="easyui-datagrid" title="${pageTitle}" style="display: none;"
           pagination=true pageSize=30 pageList="[10,20,30,50,100,200]" loadMsg="数据加载中，请稍候。。。"
           data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:true, checkOnSelect:true, selectOnCheck:true, toolbar:'#whgdg-tb'">
        <thead>
        <tr>
            <th data-options="sortable:false, field:'title' ">展览名称</th>
            <th data-options="sortable:false, width:120,field:'exhcontacts' ">联系人</th>
            <th data-options="sortable: true, width:130,field:'exhphone' ">联系方式</th>
            <th data-options="field:'crtdate', width:130, formatter:WhgComm.FMTDateTime">创建时间</th>
            <th data-options="field:'crtuser',width:120, formatter:WhgComm.FMTUserName ">编辑者</th>
            <th data-options="field:'checkor',width:120, formatter:WhgComm.FMTUserName ">审核者</th>
            <th data-options="field:'publisher',width:120, formatter:WhgComm.FMTUserName ">发布者</th>
            <th data-options="field:'state',width:50, formatter:WhgComm.FMTBizState">状态</th>
            <th data-options="width:130, sortable: true, field:'statemdfdate', formatter:WhgComm.FMTDateTime ">操作时间</th>
            <th data-options="field:'id', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
        </tr>
        </thead>
    </table>
</div>

<div id="whgdg-tb" style="display: none;">

    <div class="whgdg-tb-srch">
        <input class="easyui-textbox" style="width: 200px;" name="title" prompt="请输入展览名称" data-options="width:120">
        <input class="easyui-combobox" name="state" id="state" prompt="请选择状态"
               data-options="editable:false,width:110, panelHeight:'auto', limitToList:true"/>

        <a class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <a plain="true" method="whgListTool.view">查看</a>
    <shiro:hasPermission name="${resourceid}:publishoff">
        <a plain="true" method="whgListTool.sysPublishoff" validKey="state" validVal="6">撤销发布</a>
    </shiro:hasPermission>
</div>
<!-- 操作按钮-END -->

<div id="whgwin-add-btn" style="text-align: center; display: none">
    <div style="display: inline-block; margin: 0 auto">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="btn">确 定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
           onclick="$('#whgwin-add').dialog('close')">关 闭</a>
    </div>
</div>
<!-- 发布设置发布时间表单 END -->
<script>

    function FMThasfees(v) {
        return v ? "收费" : "免费";
    }

    $(function () {
        $("#state").combobox({
            valueField: 'id',
            textField: 'text',
            value: whgListTool.getDefaultState4PageType(),
            data: whgListTool.getDataState4PageType()
        });

        WhgCommAreaTree.initAreaTree({
            eleId: 'mass_type_tree',
            selectFirstNode: true,
            loadCult: true,
            onSelect: function (node) {
                var gridParams = {
                    iscult: node.iscult || '',
                    refid: node.id,
                    pcalevel: node.level,
                    pcatext: node.text
                };

                var grid = whgListTool.getGridJq();
                var gridOptions = grid.datagrid("options");
                gridOptions.queryParams = gridOptions.queryParams || {};
                $.extend(gridOptions.queryParams, gridParams);

                //console.info(gridOptions.queryParams)
                WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/showExh/srchList4p?type=${type}');
            }
        })
    });

    var whgListTool = new Gridopts({pageType: "${type}"});

</script>
</body>
</html>
