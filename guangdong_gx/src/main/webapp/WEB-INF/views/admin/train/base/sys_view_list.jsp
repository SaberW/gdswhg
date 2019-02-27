<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>

<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>线下培训</title>

    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>

    <script src="${basePath}/static/admin/js/whgmodule-gridopts.js"></script>
</head>
<body class="easyui-layout">

    <div data-options="region:'west',title:'文化馆',split:true, width:'20%'">
        <ul class="easyui-tree" id="mass_type_tree"></ul>
    </div>
    <div data-options="region:'center',title:'线下培训'">
        <!-- 表格 -->
        <table id="whgdg"  class="easyui-datagrid" style="display: none"
               data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:true, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb'">
            <thead>
            <tr>
                <th data-options="field:'title'">培训标题</th>
                <th data-options="field:'etype',width:120, formatter:WhgComm.FMTTrainType">培训类型</th>
                <th data-options="field:'isterm', width:50, formatter:yesNoFMT">学期制</th>
                <th data-options="field:'cultname', width:120">所属文化馆</th>
                <th data-options="field:'crtdate', width:130, formatter:WhgComm.FMTDateTime">创建时间</th>
                <th data-options="field:'crtuser',width:120, formatter:WhgComm.FMTUserName ">编辑者</th>
                <th data-options="field:'checkor',width:120, formatter:WhgComm.FMTUserName ">审核者</th>
                <th data-options="field:'publisher',width:120, formatter:WhgComm.FMTUserName ">发布者</th>
                <th data-options="width:130, sortable: true, field:'statemdfdate', formatter:WhgComm.FMTDateTime ">操作时间</th>
                <th data-options="field:'state',width:50, formatter:WhgComm.FMTBizState" >状态</th>
                <th data-options="field:'_opt',formatter:WhgComm.FMTOpt,fixed:true, optDivId:'whgdg-opt'">操作</th>
            </tr>
            </thead>
        </table>
        <!-- 表格 END -->

        <!-- 表格操作工具栏 -->
        <div id="whgdg-tb" style="display: none;">
            <div class="whgdg-tb-srch">
                <input type="hidden" id="cultid" name="syscultid" value="" />
                <input type="hidden" id="cultname" name="cultname" value="" />
                <input type="hidden" id="level" name="level" value="" />
                <input type="hidden" id="iscult" name="iscult" value="" />
                <input class="easyui-textbox" style="width: 200px;" name="title" data-options="prompt:'请输入培训名称', validType:'length[1,32]'" />

                <%--<select class="easyui-combobox" name="state" prompt="请选择状态" panelHeight="auto" limitToList="true" data-options="editable:false,width:110, value:'${pageValue}', valueField:'id', textField:'text', data:WhgComm.getBizState('1')"></select>--%>
                <input class="easyui-combobox" name="state" id="state" prompt="请选择状态" data-options="editable:false,width:110, panelHeight:'auto', limitToList:true"/>

                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
            </div>
        </div>
        <!-- 表格操作工具栏-END -->
        <!-- 操作按钮 -->
        <div id="whgdg-opt" style="display: none;">
            <shiro:hasPermission name="${resourceid}:view"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="view">查看</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:publishoff"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="6" plain="true" method="whgListTool.sysPublishoff">撤销发布</a></shiro:hasPermission>

            <shiro:hasPermission name="${resourceid}:rce2province">
                <a plain="true" method="whgListTool.toprovince" validKey="toprovince" validVal="0">省级推荐</a>
            </shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:unrce2province">
                <a plain="true" method="whgListTool.untoprovince" validKey="toprovince" validVal="1">取消省级推荐</a>
            </shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:rce2city">
                <a plain="true" method="whgListTool.tocity" validKey="tocity" validVal="0">市级推荐</a>
            </shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:unrce2city">
                <a plain="true" method="whgListTool.untocity" validKey="tocity" validVal="1">取消市级推荐</a>
            </shiro:hasPermission>
        </div>
        <!-- 操作按钮-END -->
    </div>

<script>

    $(function () {
        $("#state").combobox({
            valueField:'id',
            textField:'text',
            value: whgListTool.getDefaultState4PageType(),
            data: whgListTool.getDataState4PageType()
        });

        WhgCommAreaTree.initAreaTree({
            eleId : 'mass_type_tree',
            loadCult : true,
            selectFirstNode : true,
            onSelect: function(node){
                $('#cultid').val(node.id);
                $('#cultname').val(node.text);
                $('#iscult').val(node.iscult);
                $('#level').val(node.level);
                WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/train/srchSysList4p?islive=0');
            }
        });
    });

    var whgListTool = new Gridopts({pageType: "${type}", pageTypeS:'syspublish'});

    /**
     * 取消发布 [6]->4
     * @param idx
     */
    /*function publishoff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "确定要取消发布选中的项吗？", function(){
            __updStateSend(row.id, 6, 4);
        })
    }*/

    /**
     * 修改状态提交
     * @param ids
     * @param formstates
     * @param tostate
     * @private
     */
    /*function __updStateSend(ids, formstates, tostate){
        $.messager.progress();
        var params = {ids: ids, formstates: formstates, tostate: tostate};
        $.post('${basePath}/admin/train/updstate', params, function(data){
            $("#whgdg").datagrid('reload');
            if (!data.success || data.success != "1"){
                $.messager.alert("错误", data.errormsg||'操作失败', 'error');
            }
            $.messager.progress('close');
        }, 'json');
    }*/

    /**
     * 查看
     * @param idx
     */
    function view(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/train/view/add?targetShow=1&id='+row.id);
    }
</script>
</body>
</html>
