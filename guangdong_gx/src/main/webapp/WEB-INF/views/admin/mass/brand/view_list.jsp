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

    <c:choose>
        <c:when test="${pageType eq 'listdel'}">
            <c:set var="pageTitle" value="回收站"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="文化专题列表"></c:set>
        </c:otherwise>
    </c:choose>

    <title>${pageTitle}</title>

    <script src="${basePath}/static/admin/js/whgmodule-gridopts.js"></script>
</head>
<body>
<table id="whgdg" class="easyui-datagrid" title="${pageTitle}" style="display: none;"
       toolbar="#tb" pagination=true
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:true, checkOnSelect:true, selectOnCheck:true">
    <thead>
    <tr>
        <th data-options="sortable:false,width:200, field:'title' ">名称</th>
        <th data-options="sortable:false,width:150, field:'titleshort' ">简称</th>

        <th data-options="field:'crtuser',width:120, formatter:WhgComm.FMTUserName">编辑者</th>
        <th data-options="field:'checkor',width:120, formatter:WhgComm.FMTUserName">审核者</th>
        <th data-options="field:'publisher',width:120, formatter:WhgComm.FMTUserName">发布者</th>
        <th data-options="field:'statemdfdate',width:130, formatter:WhgComm.FMTDateTime, sortable:true">操作时间</th>
        <th data-options="field:'state',width:50, formatter:WhgComm.FMTBizState" >状态</th>

        <c:if test="${pageType eq 'listpublish'}">
            <th data-options="sortable:true, field:'recommend', formatter:function(v){
                return (v && v==1)? '是':'否'
            }">是否推荐</th>
        </c:if>

        <th data-options="sortable:false, field:'id', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>

<div id="tb" style="display: none;">
    <shiro:hasPermission name="${resourceid}:add">
        <div class="whgd-gtb-btn">
            <a class="easyui-linkbutton" iconCls="icon-add" onclick="whgListTool.add()">添 加</a>
        </div>
    </shiro:hasPermission>

    <div class="whgdg-tb-srch">
        <input class="easyui-combobox" name="cultid" id="cultid" prompt="请选择文化馆" data-options="editable:false,width:150"/>
        <input class="easyui-combobox" name="deptid" id="deptid" prompt="请选择部门" data-options="editable:false,width:150"/>

        <c:if test="${pageType ne 'listdel'}">
            <input class="easyui-combobox" name="state" id="state" prompt="请选择状态" data-options="editable:false,width:110, panelHeight:'auto', limitToList:true"/>
        </c:if>

        <c:if test="${pageType eq 'listpublish'}">
            <select class="easyui-combobox" name="recommend" prompt="是否推荐" panelHeight="auto" limitToList="true"
                    data-options="width:120, value:'', valueField:'id', textField:'text', data:[{id:'1',text:'推荐'},{id:'0',text:'非推荐'}]">
            </select>
        </c:if>

        <input class="easyui-textbox" name="title" prompt="请输入名称" data-options="width:150">

        <input class="easyui-textbox" name="titleshort" prompt="请输入简称" data-options="width:150">

        <a class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#tb');">查 询</a>
    </div>
</div>


<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">

    <a plain="true" method="whgListTool.view">查看</a>

    <shiro:hasPermission name="${resourceid}:edit">
        <a plain="true" method="whgListTool.edit" validFun="whgListTool.optValid4EditState">编辑</a>
    </shiro:hasPermission>
    <%--<shiro:hasPermission name="${resourceid}:ziyuan">
        <a plain="true" method="whgListTool.resource" validFun="whgListTool.optValid4PageTypeState">资源管理</a>
    </shiro:hasPermission>--%>

    <shiro:hasPermission name="${resourceid}:checkgo">
        <a plain="true" method="whgListTool.checkgo" validFun="whgListTool.optValid4EditState">提交审核</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkon">
        <a plain="true" method="whgListTool.checkon" validFun="whgListTool.optValid4EditState">审核通过</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkoff">
        <a plain="true" method="whgListTool.checkoff" validFun="whgListTool.optValid4EditState">审核打回</a>
    </shiro:hasPermission>

    <shiro:hasPermission name="${resourceid}:publish">
        <a plain="true" method="whgListTool.publish" validFun="whgListTool.optValid4EditState">发布</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:publishoff">
        <a plain="true" method="whgListTool.publishoff" validKey="state" validVal="6">撤销发布</a>
    </shiro:hasPermission>

    <shiro:hasPermission name="${resourceid}:recommend">
        <a plain="true" method="whgListTool.recommend" validFun="whgListTool.recommendVfun">推荐</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:recommendoff">
        <a plain="true" method="whgListTool.recommendoff" validFun="whgListTool.recommendOffVfun">撤销推荐</a>
    </shiro:hasPermission>

    <shiro:hasPermission name="${resourceid}:recovery">
        <a plain="true" method="whgListTool.recovery" validFun="whgListTool.optValid4RecoveryState">回收</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:undel">
        <a plain="true" method="whgListTool.undel" validKey="delstate" validVal="1">撤销回收</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:del">
        <a plain="true" method="whgListTool.del" validFun="whgListTool.optValid4Del">删除</a>
    </shiro:hasPermission>

</div>
<!-- 操作按钮-END -->


<script>

    $(function(){
        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'', allcult:true,
            deptEid:'deptid', deptValue:'', alldept:true
        });

        $("#state").combobox({
            valueField:'id',
            textField:'text',
            value: whgListTool.getDefaultState4PageType(),
            data: whgListTool.getDataState4PageType()
        });
        WhgComm.search('#whgdg', '#tb', '${basePath}/admin/mass/brand/srchList4p?pageType=${pageType}');
    });

    var whgListTool = new Gridopts({pageType: "${pageType}"});

</script>
</body>
</html>
