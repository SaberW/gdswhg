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

    <title>众筹项目列表</title>

    <script src="${basePath}/static/admin/js/whgmodule-gridopts.js"></script>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'文化馆',split:true, width:'20%'" >
    <div class="easyui-tree" id="mass_type_tree"></div>
</div>


<div data-options="region:'center',title:'众筹项目列表'" >

    <table id="whgdg" class="easyui-datagrid" style="display: none;"
           toolbar="#tb" pagination=true
           data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:true, checkOnSelect:true, selectOnCheck:true">
        <thead>
        <tr>
            <th data-options="sortable:false, field:'title' ">众筹名称</th>
            <th data-options="sortable:false, field:'cultname' ">所属文化馆</th>
            <th data-options="sortable: true, field:'etype', formatter:function(v){
                if (v=='4') return '活动众筹'
                else if (v=='5') return '培训众筹'
                else return '其它众筹'
            } ">众筹类型</th>
            <th data-options="sortable: true, field:'timestart', formatter:WhgComm.FMTDateTime ">众筹开始时间</th>
            <th data-options="sortable: true, field:'timeend', formatter:WhgComm.FMTDateTime ">众筹结束时间</th>

            <th data-options="sortable: true, field:'issuccess', formatter:fmtGatIssuccess ">进度</th>
            <th data-options="sortable:true, field:'recommend', formatter:function(v){
                return (v && v==1)? '是':'否'
            }">是否推荐</th>

            <th data-options="field:'crtuser',width:120, formatter:WhgComm.FMTUserName">编辑者</th>
            <th data-options="field:'checkor',width:120, formatter:WhgComm.FMTUserName">审核者</th>
            <th data-options="field:'publisher',width:120, formatter:WhgComm.FMTUserName">发布者</th>
            <th data-options="field:'statemdfdate',width:130, formatter:WhgComm.FMTDateTime, sortable:true">操作时间</th>
            <th data-options="field:'state',width:50, formatter:WhgComm.FMTBizState" >状态</th>

            <th data-options="field:'id', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
        </tr>
        </thead>
    </table>

    <div id="tb" style="display: none;">

        <div class="whgdg-tb-srch">
            <input class="easyui-textbox" name="title" prompt="请输入名称" data-options="width:200">

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
    function getGatEtypes(){
        return [{"id":"4","text":"活动众筹"},{"id":"5","text":"培训众筹"},{"id":"0","text":"其它众筹"}];
    }

    function fmtGatIssuccess(v,r,i){
        var now = whgListTool.__now;
        var timestart = r.timestart;
        var timeend = r.timeend;

        if (!now || !timestart || !timeend){
            if (v == 0){
                return "";
            }else if (v == 1) {
                return "众筹成功";
            }else {
                return "众筹失败";
            }
        }

        if (now < timestart) {
            return "未开始";
        }else if (now >= timestart && now <= timeend){
            return "众筹中";
        }else{
            if (v == 0){
                return "已结束";
            }else if (v == 1) {
                return "众筹成功";
            }else {
                return "众筹失败";
            }
        }
    }

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
                    refTreeId: node.id,
                    pcalevel: node.level,
                    pcatext: node.text
                };

                var grid = whgListTool.getGridJq();
                var gridOptions = grid.datagrid("options");
                gridOptions.queryParams = gridOptions.queryParams || {};
                $.extend(gridOptions.queryParams, gridParams);

                WhgComm.search('#whgdg', '#tb', '${basePath}/admin/gather/sysSrchList4p');
            }
        })

    });

    var whgListTool = new Gridopts({pageType: "${pageType}"});


</script>

</body>
</html>
