<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <c:choose>
        <c:when test="${listType eq 'edit'}">
            <c:set var="pageTitle" value="云直播编辑列表"></c:set>
            <c:set var="getUrl" value="/admin/live/search4p?type=edit"></c:set>
        </c:when>
        <c:when test="${listType eq 'check'}">
            <c:set var="pageTitle" value="云直播审核列表"></c:set>
            <c:set var="getUrl" value="/admin/live/search4p?type=check"></c:set>
        </c:when>
        <c:when test="${listType eq 'publish'}">
            <c:set var="pageTitle" value="云直播发布列表"></c:set>
            <c:set var="getUrl" value="/admin/live/search4p?type=publish"></c:set>
        </c:when>
        <c:when test="${listType eq 'cycle'}">
            <c:set var="pageTitle" value="回收站"></c:set>
            <c:set var="getUrl" value="/admin/live/search4p?type=cycle"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="无标题"></c:set>
        </c:otherwise>
    </c:choose>
    <title>${pageTitle}</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>
</head>
<body>
    <table id="whgdg" title="${pageTitle}" class="easyui-datagrid" style="display: none"
           data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb', url:'${basePath}${getUrl}'">
        <thead>
        <tr>
            <th data-options="field:'livetitle',width:80">直播标题</th>
            <th data-options="field:'livecreator',width:80,formatter:getCreator">创建人</th>
            <th data-options="field:'livecreattime', width:60,formatter:WhgComm.FMTDateTime">创建时间</th>
            <th data-options="field:'livestate', width:50,formatter:myState" >状态</th>
            <th data-options="field:'_opt', width:450, formatter:WhgComm.FMTOpt,fixed:true, optDivId:'whgdg-opt'">操作</th>
        </tr>
        </thead>
    </table>

    <!-- 表格操作工具栏 -->
    <div id="whgdg-tb" style="display: none;">
        <div id="tb">
            <shiro:hasPermission name="${resourceid}:add"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="add()">添加</a></shiro:hasPermission>
        </div>
        <div class="whgdg-tb-srch" style="padding-top: 8px">
            <select class="easyui-combobox" name="cultid" prompt="请选择文化馆" panelHeight="auto" limitToList="true"
                    data-options="editable:false, width:180, valueField:'id', textField:'text', data:WhgComm.getMgrCults(), value:WhgComm.getMgrCults()[0].id"></select>
            <input class="easyui-textbox" style="width: 200px;" name="livetitle" data-options="prompt:'请输入名称', validType:'length[1,32]'" />
            <select class="easyui-combobox" name="livestate" prompt="请选择状态" panelHeight="auto" limitToList="true"
                    data-options="width:120, value:'', valueField:'id', textField:'text', data:myStateAll()">
            </select>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
        </div>
    </div>

    <!-- 操作按钮 -->
    <div id="whgdg-opt" style="display: none;">
        <shiro:hasPermission name="${resourceid}:view"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="livestate" validVal="0,1,2,3" method="view">查看</a></shiro:hasPermission>
        <c:choose>
            <c:when test="${listType eq 'edit'}">
                <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="livestate" validVal="0" method="edit">编辑</a></shiro:hasPermission>
                <shiro:hasPermission name="${resourceid}:checkgo"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="livestate" validVal="0" method="checkgo">送审</a></shiro:hasPermission>
                <shiro:hasPermission name="${resourceid}:del"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="livestate" validVal="0,1,2" method="del">回收</a></shiro:hasPermission>
            </c:when>
            <c:when test="${listType eq 'check'}">
                <shiro:hasPermission name="${resourceid}:checkon"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="livestate" validVal="1" method="checkon">审核通过</a></shiro:hasPermission>
                <shiro:hasPermission name="${resourceid}:checkoff"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="livestate" validVal="1" method="checkoff">审核不通过</a></shiro:hasPermission>
                <shiro:hasPermission name="${resourceid}:del"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="livestate" validVal="0,1,2" method="del">回收</a></shiro:hasPermission>
            </c:when>
            <c:when test="${listType eq 'publish'}">
                <shiro:hasPermission name="${resourceid}:publish"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="livestate" validVal="2" method="publish">发布</a></shiro:hasPermission>
                <shiro:hasPermission name="${resourceid}:publishoff"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="livestate" validVal="3" method="publishoff">取消发布</a></shiro:hasPermission>
                <shiro:hasPermission name="${resourceid}:recommend"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="isrecommend" validVal="2" method="recommend">推荐</a></shiro:hasPermission>
                <shiro:hasPermission name="${resourceid}:recommendoff"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="isrecommend" validVal="1" method="recommendoff">取消推荐</a></shiro:hasPermission>
                <shiro:hasPermission name="${resourceid}:del"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="livestate" validVal="0,1,2" method="del">回收</a></shiro:hasPermission>
            </c:when>
            <c:when test="${listType eq 'cycle'}">
                <shiro:hasPermission name="${resourceid}:del"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="livestate" validVal="0,1,2,3" method="doDelForver">删除</a></shiro:hasPermission>
                <shiro:hasPermission name="${resourceid}:undel"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="livestate" validVal="0,1,2,3" method="undel">还原</a></shiro:hasPermission>
            </c:when>
        </c:choose>
    </div>
    <!-- 操作按钮-END -->
</body>
<script type="text/javascript">
    function myStateAll() {
        var res = [];
        res.push({"id":"0","text":"可编辑"});
        res.push({"id":"1","text":"待审核"});
        res.push({"id":"2","text":"待发布"});
        res.push({"id":"3","text":"已发布"});
        return res;
    }
    function myState(livestate) {
        if(0 == livestate){
            return "可编辑";
        }else if(1 == livestate){
            return "待审核";
        }else if(2 == livestate){
            return "待发布";
        }else if(3 == livestate){
            return "已发布";
        }else {
            return "未知状态";
        }
    }
    function getCreator(livecreator) {
        $.ajaxSetup({
            async: false
        });
        var res = "";
        $.getJSON("${basePath}/admin/system/user/srchOne?id="+livecreator,function (data) {
            if("1" != data.success){
                $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                return;
            }
            res = data.data.account;
        });
        $.ajaxSetup({
            async: true
        });
        return res;
    }
    
    function add() {
        WhgComm.editDialog("${basePath}/admin/live/edit/add");
    }

    function view(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/live/edit/view?id='+curRow.id);
    }

    function edit(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/live/edit/edit?id='+curRow.id);
    }

    function checkgo(idx) {
        $.messager.confirm("确认信息", "确定要送审吗？", function(r){
            if(r){
                var curRow = $('#whgdg').datagrid('getRows')[idx];
                $.getJSON("${basePath}/admin/live/updateState?type=checkpending&id="+curRow.id,function (data) {
                    if("1" != data.success){
                        $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                        return;
                    }
                    $('#whgdg').datagrid('reload');
                });
            }
        });

    }

    function checkon(idx) {
        $.messager.confirm("确认信息", "确定送审通过吗？", function(r){
            if(r){
                var curRow = $('#whgdg').datagrid('getRows')[idx];
                $.getJSON("${basePath}/admin/live/updateState?type=checked&id="+curRow.id,function (data) {
                    if("1" != data.success){
                        $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                        return;
                    }
                    $('#whgdg').datagrid('reload');
                });
            }
        });

    }
    
    function checkoff(idx) {
        $.messager.confirm("确认信息", "确定送审不通过吗？", function(r){
            if(r){
                var curRow = $('#whgdg').datagrid('getRows')[idx];
                $.getJSON("${basePath}/admin/live/updateState?type=initial&id="+curRow.id,function (data) {
                    if("1" != data.success){
                        $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                        return;
                    }
                    $('#whgdg').datagrid('reload');
                });
            }
        });

    }

    function publish(idx) {
        $.messager.confirm("确认信息", "确定发布吗？", function(r){
            if(r){
                var curRow = $('#whgdg').datagrid('getRows')[idx];
                $.getJSON("${basePath}/admin/live/updateState?type=published&id="+curRow.id,function (data) {
                    if("1" != data.success){
                        $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                        return;
                    }
                    $('#whgdg').datagrid('reload');
                });
            }
        });

    }

    function publishoff(idx) {
        $.messager.confirm("确认信息", "确定取消发布吗？", function(r){
            if(r){
                var curRow = $('#whgdg').datagrid('getRows')[idx];
                $.getJSON("${basePath}/admin/live/updateState?type=initial&id="+curRow.id,function (data) {
                    if("1" != data.success){
                        $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                        return;
                    }
                    $('#whgdg').datagrid('reload');
                });
            }
        });
    }

    function del(idx) {
        $.messager.confirm("确认信息", "确定删除吗？", function(r){
            if(r){
                var curRow = $('#whgdg').datagrid('getRows')[idx];
                $.getJSON("${basePath}/admin/live/del/del?id="+curRow.id,function (data) {
                    if("1" != data.success){
                        $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                        return;
                    }
                    $('#whgdg').datagrid('reload');
                });
            }
        });
    }
    
    function undel(idx) {
        $.messager.confirm("确认信息", "确定还原吗？", function(r){
            if(r){
                var curRow = $('#whgdg').datagrid('getRows')[idx];
                $.getJSON("${basePath}/admin/live/del/undel?id="+curRow.id,function (data) {
                    if("1" != data.success){
                        $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                        return;
                    }
                    $('#whgdg').datagrid('reload');
                });
            }
        });
    }

    function doDelForver(idx) {
        $.messager.confirm("确认信息", "确定永久删除吗？", function(r){
            if(r){
                var curRow = $('#whgdg').datagrid('getRows')[idx];
                $.getJSON("${basePath}/admin/live/del/delForever?id="+curRow.id,function (data) {
                    if("1" != data.success){
                        $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                        return;
                    }
                    $('#whgdg').datagrid('reload');
                });
            }
        });
    }
    
    function recommend(idx) {
        $.messager.confirm("确认信息", "确定推荐此选项吗？", function(r){
            if(r){
                var curRow = $('#whgdg').datagrid('getRows')[idx];
                $.getJSON("${basePath}/admin/live/recommend/on?id="+curRow.id,function (data) {
                    if("1" != data.success){
                        $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                        return;
                    }
                    $('#whgdg').datagrid('reload');
                });
            }
        });
    }

    function recommendoff(idx) {
        $.messager.confirm("确认信息", "确定取消推荐此选项吗？", function(r){
            if(r){
                var curRow = $('#whgdg').datagrid('getRows')[idx];
                $.getJSON("${basePath}/admin/live/recommend/off?id="+curRow.id,function (data) {
                    if("1" != data.success){
                        $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                        return;
                    }
                    $('#whgdg').datagrid('reload');
                });
            }
        });
    }
    
</script>
</html>
