<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>供需文艺专家列表</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <script src="${basePath}/static/admin/js/whgmodule-gridopts.js"></script>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'文化馆',split:true, width:'20%'">
    <div class="easyui-tree" id="mass_type_tree"></div>
</div>
<div data-options="region:'center',title:'供需文艺演出列表'">
    <!-- 表格 -->
    <table id="whgdg" title="${pageTitle}" class="easyui-datagrid" style="display: none"
           data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb'">
        <thead>
        <tr>
            <th data-options="field:'name',width:80">姓名</th>
            <%--<th data-options="field:'summary',width:80">简介</th>--%>
            <th data-options="field:'xueli',width:40">学历</th>
            <th data-options="field:'birthstr', width:60,formatter:WhgComm.FMTDate">出生日期</th>
            <th data-options="field:'job',width:40">职务</th>
            <th data-options="field:'crtuser',width:120,formatter:WhgComm.FMTUserName">编辑者</th>
            <th data-options="field:'checkor',width:120,formatter:WhgComm.FMTUserName">审核者</th>
            <th data-options="field:'publisher',width:120,formatter:WhgComm.FMTUserName">发布者</th>
            <th data-options="field:'crtdate', width:60,formatter:WhgComm.FMTDateTime">创建时间</th>
            <th data-options="field:'state', width:50,formatter:WhgComm.FMTBizState">状态</th>
            <th data-options="field:'_opt', formatter:WhgComm.FMTOpt,fixed:true, optDivId:'whgdg-opt'">操作</th>
        </tr>
        </thead>

    </table>
    <!-- 表格 END -->
</div>
<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">

    <div class="whgdg-tb-srch" style="padding-top: 8px">
        <input class="easyui-textbox" style="width: 200px;" name="name"
               data-options="prompt:'请输入名称', validType:'length[1,32]'"/>
        <input class="easyui-combobox" name="state" id="state" prompt="请选择状态"
               data-options="editable:false,width:110, panelHeight:'auto', limitToList:true"/>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search"
           onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <a plain="true" method="whgListTool.view">查看</a>
    <shiro:hasPermission name="${resourceid}:publishoff">
        <a plain="true" method="whgListTool.sysPublishoff" validKey="state" validVal="6">撤销发布</a>
    </shiro:hasPermission>

</div>
<!-- 操作按钮-END -->

<script>

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
                WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/personnel/srchList4p?type=${type}');
            }
        })
    });

    var whgListTool = new Gridopts({pageType: "${type}"});

    Gridopts.prototype.resource = function (idx) {
        var row = this.__getGridRow(idx);
        var cultid = $('#cultid').combobox('getValue');
        WhgComm.editDialog('${basePath}/admin/personnel/srchList4p?type=${type}&refid=' + row.id + "&cultid=" + cultid);
    };

    function _publish(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 2 || row.state == 4;
    }
    function _publishoff(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 6;
    }
    function _del(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 1 || row.state == 9 || row.state == 2 || row.state == 4 || row.state == 5;
    }
    function _checkon(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 9;
    }
    function _checkgo(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 1 || row.state == 5;
    }
    function _checkoff(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 9 || row.state == 2;
    }
    function _undel(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.isdel == 1;
    }


</script>
</body>
</html>
