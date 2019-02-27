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
    <c:choose>
        <c:when test="${type eq 'editlist'}">
            <c:set var="pageTitle" value="需求编辑列表"></c:set>
            <c:set var="typevalue" value="1"></c:set>
        </c:when>
        <c:when test="${type eq 'check'}">
            <c:set var="pageTitle" value="需求审核列表"></c:set>
            <c:set var="typevalue" value="9"></c:set>
        </c:when>
        <c:when test="${type eq 'publish'}">
            <c:set var="pageTitle" value="需求发布列表"></c:set>
            <c:set var="typevalue" value="2"></c:set>
        </c:when>
        <c:when test="${type eq 'recycle'}">
            <c:set var="pageTitle" value="回收站"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="无标题"></c:set>
        </c:otherwise>
    </c:choose>


    <title>${pageTitle}</title>

    <script src="${basePath}/static/admin/js/whgmodule-gridopts.js"></script>
</head>
<body>
<table id="whgdg" class="easyui-datagrid" title="${pageTitle}" style="display: none;"
       toolbar="#tb" pagination=true pageSize=30 pageList="[10,20,30,50,100,200]" loadMsg="数据加载中，请稍候。。。"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:true, checkOnSelect:true, selectOnCheck:true,
       <%--loader:whgListTool.paramLoader--%>">
    <thead>
    <tr>
        <th data-options=" sortable:false, field:'title' ">标题</th>
        <%--<th data-options=" sortable:false, field:'gxtype',formatter:function(v){return v==1?'需求':'供给'} ">分类</th>--%>
        <th data-options=" sortable:false, field:'etype',formatter:WhgComm.FMTSupplyType ">类型</th>
        <th data-options=" sortable:false, field:'contacts' ">联系人</th>
        <th data-options=" sortable:false, field:'phone' ">联系电话</th>
        <th data-options="field:'crtuser',width:120,formatter:WhgComm.FMTUserName">编辑者</th>
        <th data-options="field:'checkor',width:120,formatter:WhgComm.FMTUserName">审核者</th>
        <th data-options="field:'publisher',width:120,formatter:WhgComm.FMTUserName">发布者</th>

        <th data-options=" sortable: true, field:'state', formatter:WhgComm.FMTBizState ">状态</th>
        <th data-options=" sortable: true, field:'statemdfdate', formatter:WhgComm.FMTDateTime ">状态变更时间</th>
        <th data-options="field:'id', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
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
        <input class="easyui-combobox" name="cultid" id="cultid" prompt="请选择文化馆"
               data-options="editable:true,width:180" />
        <input class="easyui-combobox" name="deptid" id="deptid" prompt="请选择部门"
               data-options="editable:true,width:120"/>

        <input class="easyui-textbox" name="title" prompt="请输入供需标题" data-options="width:120">

        <%--<select class="easyui-combobox" name="gxtype" prompt="请选择供需分类" panelHeight="auto" limitToList="true"
                data-options="width:120, value:'', valueField:'id', textField:'text', data:gxTypeData()">
        </select>--%>

        <select class="easyui-combobox" name="etype" prompt="请选择信息分类" panelHeight="auto" limitToList="true"
                data-options="width:120, value:'', valueField:'id', textField:'text', data:WhgComm.getSupplyType()">
        </select>
        <c:choose>
            <c:when test="${type eq 'editlist'}">
                <input class="easyui-combobox" style="width: 200px;" name="state" panelHeight="auto" limitToList="true"
                       data-options="prompt:'请选择状态', value:'${typevalue}', valueField:'id', textField:'text', data:WhgComm.getBizState()"/>
            </c:when>
            <c:when test="${type eq 'recycle'}">
            </c:when>
            <c:otherwise>
                <input class="easyui-combobox" style="width: 200px;" name="state" panelHeight="auto" limitToList="true"
                       data-options="prompt:'请选择状态', value:'${typevalue}', valueField:'id', textField:'text', data:WhgComm.getBizState('1')"/>
            </c:otherwise>
        </c:choose>

        <a class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#tb');">查 询</a>
    </div>
</div>

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <a plain="true" method="doSee">查看</a>
    <shiro:hasPermission name="${resourceid}:edit">
        <a plain="true" method="doEdit" validFun="whgListTool.optValid4EditState">编辑</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:ziyuan">
        <a plain="true" method="doResource" validFun="whgListTool.optValid4PageTypeState">资源管理</a>
    </shiro:hasPermission>
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
        <a plain="true" method="publishoff" validKey="state" validVal="6">撤销发布</a>
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

    /**
     * 资源管理
     * */
    function doResource(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        var cultid = $('#cultid').combobox('getValue');
        WhgComm.editDialog('${basePath}/admin/resource/view/list?reftype=4&refid=' + curRow.id + '&cultid=' + cultid);
    }

    $(function(){
        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid: 'cultid', cultValue: '', allcult: true,
            deptEid: 'deptid', deptValue: '', alldept: true
        });

        WhgComm.search('#whgdg', '#tb', '${basePath}/admin/supply/srchList4p?type=${type}');
    });

    /**
     * 编辑信息
     * @param idx 行下标
     */
    function doEdit(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/supply/view/edit?id=' + curRow.id + '&type=${type}');
    }

    /**
     * 查看资料
     * @param idx 行下标
     */
    function doSee(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/supply/view/show?id=' + curRow.id + "&targetShow=1" + '&type=${type}');
    }

    /**
     * 取消发布 [6]->4
     * @param idx
     */
    function publishoff(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "确定要取消发布选中的项吗？", function () {
            updStateSend(row.id, 6, 4);//原来是1 回到编辑列表 现在改成4 下架列表
        })
    }
    /**
     * 状态变更（送审、审核、发布、推荐...操作状态变更）
     * @param idx
     */
    function updStateSend(ids, fromState, toState) {
        $.messager.progress();
        var params = {ids: ids, formstates: fromState, tostate: toState};
        $.post('${basePath}/admin/supply/updstate', params, function (data) {
            $("#whgdg").datagrid('reload');
            if (!data.success || data.success != "1") {
                $.messager.alert("错误", data.errormsg || '操作失败', 'error');
            }
            $.messager.progress('close');
        }, 'json');
    }
    var whgListTool = new Gridopts({
        pageType: "${type}",
        pageTypeE: "editlist",
        pageTypeC: "check",
        pageTypeP: "publish",
        pageTypeD: "recycle"
    });
    Gridopts.prototype.resource = function (idx) {
        var row = this.__getGridRow(idx);
        var cultid = $('#cultid').combobox('getValue');
        WhgComm.editDialog('${basePath}/admin/supply/srchList4p?type=${type}&refid=' + row.id + "&cultid=" + cultid + "&delstate=" + row.delstate);
    };

</script>

</body>
</html>
