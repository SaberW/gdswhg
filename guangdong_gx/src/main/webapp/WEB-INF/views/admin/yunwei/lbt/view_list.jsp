<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>轮播图管理</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <c:choose>
        <c:when test="${clazz == 1}">
            <c:set var="pageTitle" value="PC首页轮播图配置"></c:set>
        </c:when>
        <c:when test="${clazz == 2}">
            <c:set var="pageTitle" value="APP首页轮播图配置"></c:set>
        </c:when>
        <c:when test="${clazz == 3}">
            <c:set var="pageTitle" value="PC首页广告图配置"></c:set>
        </c:when>
        <c:when test="${clazz == 4}">
            <c:set var="pageTitle" value="APP首页广告图图配置"></c:set>
        </c:when>
        <c:when test="${clazz == 5}">
            <c:set var="pageTitle" value="PC培训首页轮播图配置"></c:set>
        </c:when>
        <c:when test="${clazz == 6}">
            <c:set var="pageTitle" value="PC志愿服务首页风采展示"></c:set>
        </c:when>
        <c:when test="${clazz == 7}">
            <c:set var="pageTitle" value="PC非遗首页广告图配置"></c:set>
        </c:when>
        <c:when test="${clazz == 8}">
            <c:set var="pageTitle" value="PC志愿服务首页广告图配置"></c:set>
        </c:when>
        <c:when test="${clazz == 9}">
            <c:set var="pageTitle" value="PC非遗首页轮播图配置"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="轮播图配置"></c:set>
        </c:otherwise>
    </c:choose>
    <title>轮播图配置</title>
</head>
<body>
<!-- 表格 -->
<table id="whgdg" title="轮播图配置" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:true, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb', queryParams:{cultid:WhgComm.getMgrCultsFirstId()},url:'${basePath}/admin/yunwei/lbt/srchList?type=${clazz}'">
    <thead>
    <tr>
        <th data-options="field:'name', width:350">标题</th>
        <th data-options="field:'picture',width:367, formatter:WhgComm.FMTImg">图片</th>
        <%--<th data-options="field:'url', width:200">URL</th>--%>
        <th data-options="field:'state', width:40, formatter:WhgComm.FMTState">状态</th>
        <th data-options="field:'_opt', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <shiro:hasPermission name="${resourceid}:add">
    <div class="whgd-gtb-btn">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="doAdd();">添加</a>
    </div>
    </shiro:hasPermission>

    <div class="whgdg-tb-srch">
        <input class="easyui-combobox" name="cultid" id="cultid" style="width:200px;" panelHeight="auto" data-options="prompt:'请选择文化馆'">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="doSee">查看</a>
    <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="0" method="doEdit">编辑</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:off"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="1" method="doOff">停用</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:on"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="0" method="doOn">启用</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:del"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="0" method="doDel">删除</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:toprovince"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="_toProvice"  method="toProvice">推送全省站</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:untoprovince"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="_unToProvice"  method="toProvice">取消推送全省站</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:tocity"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="_toCity"  method="toCity">推送全市站</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:untocity"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="_unToCity"  method="toCity">取消推送全市站</a></shiro:hasPermission>
</div>
<!-- 操作按钮-END -->

<script>
    /** 查看 */
    function doSee(idx){
        var curtRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/yunwei/lbt/view/edit/${clazz}?id=' + curtRow.id+"&onlyshow=1");
    }//End查看

    /**
     * 上移
     * @param idx
     */
    function doUp(idx){
        var row = $('#whgdg').datagrid('getRows')[idx];
        $.ajax({
            url: "${basePath}/admin/yunwei/lbt/move",
            data: {id:row.id, type: 'up', dataScope:'cultid', clazz:'${clazz}'},
            success: function (data) {
                if (data.success == "1") {
                    $('#whgdg').datagrid('reload');
                } else {
                    $.messager.alert("提示", data.errormsg);
                }
            }
        });
    }
    /**
     * 置顶
     * @param idx
     */
    function doTop(idx){
        var row = $('#whgdg').datagrid('getRows')[idx];
        var dataScope = $('#province').val() != '' ? "province" : "city";
        $.ajax({
            url: "${basePath}/admin/yunwei/lbt/move",
            data: {id:row.id, type: 'top', dataScope:'cultid', clazz:'${clazz}'},
            success: function (data) {
                if (data.success == "1") {
                    $('#whgdg').datagrid('reload');
                } else {
                    $.messager.alert("提示", data.errormsg);
                }
            }
        });
    }
    function validUp(idx){
        return idx != 0;
    }
    function  _toProvice(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 1 && !row.province;
    }
    function  _unToProvice(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 1 && row.province;
    }
    function  _toCity(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 1 && !row.city;
    }
    function  _unToCity(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 1 && row.city;
    }

    $(function () {
        //条件查询-文化馆
        WhgComm.initPMS({cultEid:'cultid'});
    });
    /** 添加图片方法 */
    function doAdd() {
        WhgComm.editDialog('${basePath}/admin/yunwei/lbt/view/add/${clazz}');
    }
    /** 添加图片方法-END */

    /** 编辑图片方法 */
    function doEdit(idx) {
        var curtRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/yunwei/lbt/view/edit/${clazz}?id=' + curtRow.id);
    }
    /** 编辑图片方法-END */

    /**删除 */
    function doDel(idx) {
        var rows = $("#whgdg").datagrid('getRows');
        var row = rows[idx];
        var id = row.id;
        $.messager.confirm('确认对话框', '您确认要删除吗？', function (r) {
            if (r) {
                $.ajax({
                    type: "POST",
                    url: "${basePath}/admin/yunwei/lbt/del",
                    data: {id: id},
                    success: function (data) {
                        //var Json = $.parseJSON(data);
                        if (data.success == "1") {
                            $('#whgdg').datagrid('reload');
                        } else {
                            $.messager.alert("提示", data.errormsg);
                        }

                    }
                });
            }
        });
    }

    /** 启用角色 */
    function doOn(idx) {
        var curtRow = $('#whgdg').datagrid('getRows')[idx];
        updataState(curtRow.id, '1');
    }



    /** 停用角色 */
    function doOff(idx) {
        var curtRow = $('#whgdg').datagrid('getRows')[idx];
        updataState(curtRow.id, '0');

    }


    /** 修改角色状态 */
    function updataState(id, state) {
        var myDate = new Date();
        $.ajax({
            url: "${basePath}/admin/yunwei/lbt/updstate?datatime="+myDate.getSeconds(),
            data: {id: id, state: state},
            success: function (data) {
                //var Json = $.parseJSON(data);
                if (data.success == "1") {
                    $('#whgdg').datagrid('reload');
//                    $.messager.alert("提示", "操作成功");
                } else {
                    $.messager.alert("提示", data.errormsg);
                }

            }

        });
    }


    /** 推荐或者取消推荐到省 */
    function toProvice(idx) {
        var row = $('#whgdg').datagrid('getRows')[idx];
        subToProvice(1,row.id, row.cultid);
    }

    /** 推荐或者取消推荐到省 */
    function toCity(idx) {
        var row = $('#whgdg').datagrid('getRows')[idx];
        subToProvice(2,row.id, row.cultid);
    }

    /** 推到省 */
    function subToProvice(type,id,cultid) {
        $.ajax({
            url: "${basePath}/admin/yunwei/lbt/toarea",
            data: {type: type,id: id, cultid: cultid},
            success: function (data) {
                if (data.success == "1") {
                    $('#whgdg').datagrid('reload');
                } else {
                    $.messager.alert("提示", data.errormsg);
                }
            }
        });
    }
</script>
</body>
</html>
