<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <c:choose>
    <c:when test="${type eq 'edit'}">
        <c:set var="pageTitle" value="重点文物编辑列表"></c:set>
    </c:when>
    <c:when test="${type eq 'check'}">
        <c:set var="pageTitle" value="重点文物审核列表"></c:set>
    </c:when>
    <c:when test="${type eq 'publish'}">
        <c:set var="pageTitle" value="重点文物发布列表"></c:set>
    </c:when>
    <c:when test="${type eq 'recycle'}">
        <c:set var="pageTitle" value="回收站"></c:set>
    </c:when>
    <c:otherwise>
        <c:set var="pageTitle" value="无标题"></c:set>
    </c:otherwise>
    </c:choose>

    <title>${pageTitle}</title>

    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
</head>
<body>
<!-- 表格 -->
<table id="whgdg" title="${pageTitle}" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb', queryParams:{cultid: WhgComm.getMgrCults()[0].id},url:'${basePath}/admin/fyiHistorical/srchList4p?type=${type}'">
    <thead>
    <tr>
        <th data-options="field:'name',width:80">名称</th>
        <th data-options="field:'summary',width:80">简介</th>
        <th data-options="field:'cultid',formatter:WhgComm.FMTCult,width:40">文化馆</th>
        <th data-options="field:'crtdate', width:60,formatter:WhgComm.FMTDateTime">创建时间</th>
        <th data-options="field:'state', width:50,formatter:WhgComm.FMTBizState" >状态</th>
        <th data-options="field:'_opt', formatter:WhgComm.FMTOpt,fixed:true, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>

</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <c:choose>
        <c:when test="${type eq 'edit'}">
            <div id="tb">
                <shiro:hasPermission name="${resourceid}:add"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="add()">添加</a></shiro:hasPermission>
            </div>
        </c:when>
    </c:choose>
    <div class="whgdg-tb-srch" style="padding-top: 8px">
        <input class="easyui-textbox" style="width: 200px;" name="name" data-options="prompt:'请输入名称', validType:'length[1,32]'" />
        <select class="easyui-combobox" name="state" prompt="请选择状态" panelHeight="auto" limitToList="true"
                data-options="width:120, value:'', valueField:'id', textField:'text', data:WhgComm.getBizState()">
        </select>
        <input class="easyui-combobox" style="width: 200px;" name="cultid" data-options="required:true, prompt:'请选择文化馆', value:WhgComm.getMgrCults()[0].id, valueField:'id', textField:'text', data:WhgComm.getMgrCults()"/>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <shiro:hasPermission name="${resourceid}:view"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="view">查看</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" validFun="_edit" plain="true" method="edit">编辑</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkgo"><a href="javascript:void(0)" class="easyui-linkbutton" validFun="_checkgo" plain="true" method="checkgo">送审</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkon"><a href="javascript:void(0)" class="easyui-linkbutton" validFun="_checkon" plain="true" method="checkon">审核通过</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkon"><a href="javascript:void(0)" class="easyui-linkbutton" validFun="_checkoff" plain="true" method="checkoff">审核不通过</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:publish"><a href="javascript:void(0)" class="easyui-linkbutton" validFun="_publish" plain="true" method="publish">发布</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:publishoff"><a href="javascript:void(0)" class="easyui-linkbutton" validFun="_publishoff" plain="true" method="publishoff">撤销发布</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:recommend"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validRecommendOn" method="doCommend">推荐</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:recommendoff"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validRecommendOff" method="commendOff">取消推荐</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:undel"><a href="javascript:void(0)" class="easyui-linkbutton" validFun="_undel" plain="true" method="undel">还原</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:del"> <a href="javascript:void(0)" class="easyui-linkbutton" validFun="_del" plain="true" method="del">${type == 'recycle'?'删除':'回收'}</a></shiro:hasPermission>

</div>
<!-- 操作按钮-END -->

<script>
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
    function _edit(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 1 || row.state == 9 || row.state == 2 || row.state == 4 || row.state == 5;
    }

    function validKC(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 2 || row.state == 9 || row.state == 1 || row.state == 4;
    }

    function validRecommendOn(idx){
        var is = false;
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        if(curRow.state == 6 && curRow.isrecommend == 0){
            is = true;
        }
        return is;
    }

    function validRecommendOff(idx){
        var is = false;
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        if(curRow.state == 6 && curRow.isrecommend == 1){
            is = true;
        }
        return is;
    }
    /**
     * 添加
     */
    function add(){
        WhgComm.editDialog('${basePath}/admin/fyiHistorical/view/add');
    }

    /**
     * 编辑
     * @param idx
     */
    function edit(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/fyiHistorical/view/add?id='+row.id);
    }

    /**
     * 查看
     * @param idx
     */
    function view(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/fyiHistorical/view/add?targetShow=1&id='+row.id);
    }


    /**
     * 删除
     * @param idx
     */
    function del(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        var confireStr = '确定要删除选中的项吗？';
        if (row.isdel === 1){
            confireStr = '此操作将会永久性删除数据！'+ confireStr;
        }
        $.messager.confirm("确认信息", confireStr, function(r){
            if (r){
                $.messager.progress();
                $.post('${basePath}/admin/fyiHistorical/del', {id: row.id}, function(data){
                    $("#whgdg").datagrid('reload');
                    if (!data.success || data.success != "1"){
                        $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                    }
                    $.messager.progress('close');
                }, 'json');
            }
        })
    }

    /**
     * 还原删除项
     * @param idx
     */
    function undel(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要还原选中的项吗？", function(r){
            if (r){
                $.messager.progress();
                $.post('${basePath}/admin/fyiHistorical/undel', {id: row.id, delstate: 0}, function(data){
                    $("#whgdg").datagrid('reload');
                    if (!data.success || data.success != "1"){
                        $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                    }
                    $.messager.progress('close');
                }, 'json');
            }
        })
    }

    /**
     * 发布 [2,4]->6
     * @param idx
     */
    function publish(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要发布选中的项吗？", function(r){
            if (r){
                __updStateSend(row.id, "2,4", 6);
            }
        })
    }

    /**
     * 取消发布 [6]->1
     * @param idx
     */
    function publishoff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要取消发布选中的项吗？", function(r){
            if (r){
                __updStateSend(row.id, 6, 1);
            }
        })
    }

    /**
     * 送审 [1,5]->9
     * @param idx
     */
    function checkgo(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要送审选中的项吗？", function(r){
            if (r){
                __updStateSend(row.id, "1,5", 9);
            }
        })
    }

    /**
     * 审通过 [9]->2
     * @param idx
     */
    function checkon(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要审核选中的项吗？", function(r){
            if (r){
                __updStateSend(row.id, 9, 2);
            }
        })
    }

    /**
     * 审不通过 [9]->5
     * @param idx
     */
    function checkoff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        if(row.state == 4){
            $.messager.alert("提示", "撤销发布的不可以进行打回操作！");
            return;
        }
        $.messager.confirm("确认信息", "确定要打回选中的项吗？", function(r){
            if (r){
                __updStateSend(row.id, "9,2", 1);
            }
        })
    }

    /**
     * 修改状态提交
     * @param ids
     * @param formstates
     * @param tostate
     * @private
     */
    function __updStateSend(ids, formstates, tostate){
        $.messager.progress();
        var params = {ids: ids, formstates: formstates, tostate: tostate};
        $.post('${basePath}/admin/fyiHistorical/updstate', params, function(data){
            $("#whgdg").datagrid('reload');
            if (!data.success || data.success != "1"){
                $.messager.alert("错误", data.errormsg||'操作失败', 'error');
            }
            $.messager.progress('close');
        }, 'json');
    }


    /**
     * 推荐状态变更
     * 0-1
     * @param idx
     */
    function _updCommend(ids, fromState, toState){
        $.messager.progress();
        var params = {ids: ids, fromState: fromState, toState: toState};
        $.post('${basePath}/admin/fyiHistorical/updCommend', params, function(data){
            $("#whgdg").datagrid('reload');
            if (!data.success || data.success != "1"){
                $.messager.alert("错误", data.errormsg||'操作失败', 'error');
            }
            $.messager.progress('close');
        }, 'json');
    }

    /**
     * 推荐状态变更 [0]->1
     * @param idx
     */
    function doCommend(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要推荐选中的项吗？", function(r){
            if (r){
                _updCommend(row.id, 0, 1);
            }
        })
    }

    /**
     * 推荐状态变更 [1]->0
     * @param idx
     */
    function commendOff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要取消推荐选中的项吗？", function(r){
            if (r){
                _updCommend(row.id, 1, 0);
            }
        })
    }

</script>
</body>
</html>
