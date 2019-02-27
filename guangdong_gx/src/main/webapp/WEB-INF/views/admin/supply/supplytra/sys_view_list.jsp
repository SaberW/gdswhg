<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>

    <title>供需培训列表</title>

    <script src="${basePath}/static/admin/js/whgmodule-gridopts.js"></script>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'文化馆',split:true, width:'20%'" >
    <div class="easyui-tree" id="mass_type_tree"></div>
</div>


<div data-options="region:'center',title:'供需培训列表'" >
    <table id="whgdg" title="${pageTitle}" class="easyui-datagrid" style="display: none"
           data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#tb'">
        <thead>
        <tr>
            <th data-options="field:'title'">培训标题</th>
            <th data-options="sortable:false, field:'cultname' ">所属文化馆</th>
            <th data-options="field:'etype',width:120, formatter:WhgComm.FMTTrainType">培训类型</th>
            <th data-options="field:'crtdate', width:130, formatter:WhgComm.FMTDateTime">创建时间</th>
            <th data-options="field:'crtuser',width:120, formatter:WhgComm.FMTUserName ">编辑者</th>
            <th data-options="field:'checkor',width:120, formatter:WhgComm.FMTUserName ">审核者</th>
            <th data-options="field:'publisher',width:120, formatter:WhgComm.FMTUserName ">发布者</th>
            <th data-options="field:'state',width:50, formatter:WhgComm.FMTBizState" >状态</th>
            <th data-options="width:130, sortable: true, field:'statemdfdate', formatter:WhgComm.FMTDateTime ">操作时间</th>
            <th data-options="field:'_opt',formatter:WhgComm.FMTOpt,fixed:true, optDivId:'whgdg-opt'">操作</th>
        </tr>
        </thead>
    </table>

    <div id="tb" style="display: none;">
        <div class="whgdg-tb-srch">
            <input class="easyui-textbox" style="width: 200px;" name="title" data-options="prompt:'请输入培训名称', validType:'length[1,32]'" />
            <input class="easyui-combobox" name="state" id="state" prompt="请选择状态" data-options="editable:false,width:110, panelHeight:'auto', limitToList:true"/>

            <a class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#tb');">查 询</a>
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
</div>



<script>

    $(function(){

        $("#state").combobox({
            valueField:'id',
            textField:'text',
            value: whgListTool.getDefaultState4PageType(),
            data: whgListTool.getDataState4PageType()
        });

        WhgCommAreaTree.initAreaTree({
            eleId: 'mass_type_tree',
            selectFirstNode: true,
            loadCult: true,
            onSelect: function(node){
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
                WhgComm.search('#whgdg', '#tb', '${basePath}/admin/supply/tra/sysSrchList4p');
            }
        })

    });

    var whgListTool = new Gridopts({pageType: "${type}"});

    Gridopts.prototype.view = function(idx){
        var row = this.__getGridRow(idx);
        WhgComm.editDialog('${basePath}/admin/supply/tra/view/add?pageType=1&id='+row.id);
    }


</script>
</body>
</html>
