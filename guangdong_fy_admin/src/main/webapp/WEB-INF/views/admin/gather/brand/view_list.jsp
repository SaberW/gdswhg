<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<% request.setAttribute("resourceopts", request.getParameter("opts")); %>
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
            <c:set var="pageTitle" value="众筹品牌列表"></c:set>
        </c:otherwise>
    </c:choose>

    <title>${pageTitle}</title>

    <script src="${basePath}/static/admin/js/whgmodule-gridopts.js"></script>
</head>
<body>
<jsp:include page="../../fkproject/fkproject_condition.jsp"/>
<table id="whgdg" class="easyui-datagrid" title="${pageTitle}" style="display: none;"
       toolbar="#tb" pagination=true pageSize=30 pageList="[10,20,30,50,100,200]" loadMsg="数据加载中，请稍候。。。"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:true, checkOnSelect:true, selectOnCheck:true,
           loader:whgListTool.paramLoader">
    <thead>
    <tr>
        <th data-options="sortable:false, field:'title' ">名称</th>
        <th data-options="sortable:false, field:'titleshort' ">简称</th>
        <th data-options="sortable: true, field:'state', formatter:WhgComm.FMTBizState ">状态</th>
        <c:if test="${pageType eq 'listpublish'}">
            <th data-options="sortable: true, field:'statemdfdate', formatter:WhgComm.FMTDateTime ">状态变更时间</th>
        </c:if>
        <th data-options="sortable:true, field:'recommend', formatter:function(v){
            return (v && v==1)? '是':'否'
        }">是否推荐</th>
        <th data-options="sortable:false, field:'id', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>

<div id="tb" style="display: none;">
    <c:if test="${pageType eq 'listedit'}">
        <shiro:hasPermission name="${resourceid}:add">
            <div class="whgd-gtb-btn">
                <a class="easyui-linkbutton" iconCls="icon-add" onclick="whgListTool.add()">添 加</a>
            </div>
        </shiro:hasPermission>
    </c:if>

    <div class="whgdg-tb-srch">
    <select class="easyui-combobox" name="cultid" id="cultid" prompt="请选择文化馆" panelHeight="auto" limitToList="true"
            data-options="editable:false, width:180, valueField:'id', textField:'text', data:WhgComm.getMgrCults(), value:WhgComm.getMgrCults()[0].id"></select>

    <input class="easyui-textbox" name="title" prompt="请输入名称" data-options="width:120">

    <input class="easyui-textbox" name="titleshort" prompt="请输入简称" data-options="width:120">

    <select class="easyui-combobox" name="state" prompt="请选择状态" panelHeight="auto" limitToList="true"
            data-options="width:120, value:'', valueField:'id', textField:'text', data:WhgComm.getBizState()">
    </select>

    <c:if test="${pageType eq 'listpublish'}">
        <select class="easyui-combobox" name="recommend" prompt="是否推荐" panelHeight="auto" limitToList="true"
                data-options="width:120, value:'', valueField:'id', textField:'text', data:[{id:'1',text:'推荐'},{id:'0',text:'非推荐'}]">
        </select>
    </c:if>

    <a class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#tb');">查 询</a>
    </div>
</div>


<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">

    <shiro:hasPermission name="${resourceid}:view"><a plain="true" method="whgListTool.view">查看</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:view"><a plain="true" method="whgListTool.viewGathers">项目列表</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:edit">
        <c:if test="${fn:contains(resourceopts, 'edit')}">
            <a plain="true" method="whgListTool.edit" validKey="state" validVal="1,9,2,4,5">编辑</a>
        </c:if>
    </shiro:hasPermission>
    <%--<shiro:hasPermission name="${resourceid}:setPublish"><a plain="true" validKey="state" validVal="1,9,2,4" method="doSetProject">设置发布信息</a></shiro:hasPermission>--%>

    <shiro:hasPermission name="${resourceid}:checkgo"><a plain="true" method="whgListTool.checkgo" validKey="state" validVal="1,5">送审</a></shiro:hasPermission>

    <shiro:hasPermission name="${resourceid}:checkon"><a plain="true" method="whgListTool.checkon" validKey="state" validVal="9">审核通过</a></shiro:hasPermission>

    <shiro:hasPermission name="${resourceid}:publish"><a plain="true" method="whgListTool.publish" validKey="state" validVal="2,4">发布</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:publishoff"><a plain="true" method="whgListTool.publishoff" validKey="state" validVal="6">撤销发布</a></shiro:hasPermission>

    <shiro:hasPermission name="${resourceid}:back"><a plain="true" method="whgListTool.back" validKey="state" validVal="9,2">打回</a></shiro:hasPermission>

    <shiro:hasPermission name="${resourceid}:edit">
        <c:if test="${fn:contains(resourceopts, 'edit')}">
            <a plain="true" method="whgListTool.resource">资源管理</a>
        </c:if>
    </shiro:hasPermission>

    <shiro:hasPermission name="${resourceid}:recommend"><a plain="true" method="whgListTool.recommend" validFun="whgListTool.recommendVfun">推荐</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:recommendoff"><a plain="true" method="whgListTool.recommendoff" validFun="whgListTool.recommendOffVfun">取消推荐</a></shiro:hasPermission>

    <shiro:hasPermission name="${resourceid}:undel"><a plain="true" method="whgListTool.undel" validKey="delstate" validVal="1">还原</a></shiro:hasPermission>
    <c:choose>
        <c:when test="${pageType eq 'listdel'}">
            <shiro:hasPermission name="${resourceid}:del"><a plain="true" method="whgListTool.del" validKey="delstate" validVal="1">删除</a></shiro:hasPermission>
        </c:when>
        <c:otherwise>
            <shiro:hasPermission name="${resourceid}:del"><a plain="true" method="whgListTool.del" validKey="state" validVal="1,9,2,4,5">回收</a></shiro:hasPermission>
        </c:otherwise>
    </c:choose>

</div>
<!-- 操作按钮-END -->


<script>

    $(function(){
        $("#tb a[iconCls='icon-search']").click();
    });

    var whgListTool = new Gridopts();

    Gridopts.prototype.paramLoader = function (param, success, error) {
        if (!param.cultid || param.cultid == ''){
            return false;
        }

        $.ajax({
            url: '${basePath}/admin/brand/srchList?pageType=${pageType}',
            type: 'post',
            data : param,
            dataType: 'json',
            success: success,
            error: error
        })
    };


    Gridopts.prototype.resource = function(idx) {
        var row = this.__getGridRow(idx);
        var cultid = $('#cultid').combobox('getValue');
        WhgComm.editDialog('${basePath}/admin/resource/view/list?reftype=29&refid=' + row.id+'&cultid='+cultid);
    };

    Gridopts.prototype.viewGathers = function(idx){
        var row = this.__getGridRow(idx);
        WhgComm.editDialog('${basePath}/admin/gather/view/listpublish?brandshow=1&brandid='+row.id);
    }

</script>
</body>
</html>
