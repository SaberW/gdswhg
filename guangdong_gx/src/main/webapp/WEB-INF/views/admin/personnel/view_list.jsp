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
        <c:set var="pageTitle" value="文艺专家编辑列表"></c:set>
        <c:set var="typevalue" value="1"></c:set>
    </c:when>
    <c:when test="${type eq 'check'}">
        <c:set var="pageTitle" value="文艺专家审核列表"></c:set>
        <c:set var="typevalue" value="9"></c:set>
    </c:when>
    <c:when test="${type eq 'publish'}">
        <c:set var="pageTitle" value="文艺专家发布列表"></c:set>
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
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <script src="${basePath}/static/admin/js/whgmodule-gridopts.js"></script>
</head>
<body>
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
        <c:choose>
            <c:when test="${type eq 'edit'}">
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
        <input class="easyui-combobox" name="cultid" id="cultid" data-options="editable:true,width:180" prompt="请选择文化馆"/>
        <input class="easyui-combobox" name="deptid" id="deptid" data-options="editable:true,width:120" prompt="请选择部门"/>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <%--<shiro:hasPermission name="${resourceid}:view"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="view">查看</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" validFun="_edit" plain="true" method="edit">编辑</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:ziyuan"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="doResource">作品管理</a></shiro:hasPermission>
    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="awards">获奖情况</a>
    <shiro:hasPermission name="${resourceid}:checkgo"><a href="javascript:void(0)" class="easyui-linkbutton" validFun="_checkgo" plain="true" method="checkgo">送审</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkon"><a href="javascript:void(0)" class="easyui-linkbutton" validFun="_checkon" plain="true" method="checkon">审核通过</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkon"><a href="javascript:void(0)" class="easyui-linkbutton" validFun="_checkoff" plain="true" method="checkoff">审核不通过</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:publish"><a href="javascript:void(0)" class="easyui-linkbutton" validFun="_publish" plain="true" method="publish">发布</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:publishoff"><a href="javascript:void(0)" class="easyui-linkbutton" validFun="_publishoff" plain="true" method="publishoff">撤销发布</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:undel"><a href="javascript:void(0)" class="easyui-linkbutton" validFun="_undel" plain="true" method="undel">还原</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:del"> <a href="javascript:void(0)" class="easyui-linkbutton" validFun="_del" plain="true" method="del">${type == 'recycle'?'删除':'回收'}</a></shiro:hasPermission>
--%>

    <a plain="true" method="view">查看</a>
    <shiro:hasPermission name="${resourceid}:edit">
        <a plain="true" method="edit" validFun="whgListTool.optValid4EditState">编辑</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:supplymge">
        <a plain="true" method="whgListTool.supplymge" validFun="whgListTool.optValid4PageTypeState">供需管理</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:zuoping">
        <a plain="true" method="doResource" validFun="whgListTool.optValid4PageTypeState">作品管理</a>
        <%--<a plain="true" method="doResource2" validFun="whgListTool.optValid4PageTypeState">作品管理2</a>--%>
        <a validFun="whgListTool.optValid4PageTypeState" plain="true" method="awards">获奖情况</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkgo">
        <a plain="true" method="checkgo" validFun="whgListTool.optValid4EditState">提交审核</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkon">
        <a plain="true" method="checkon" validFun="whgListTool.optValid4EditState">审核通过</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkoff">
        <a plain="true" method="checkoff" validFun="whgListTool.optValid4EditState">审核打回</a>
    </shiro:hasPermission>

    <shiro:hasPermission name="${resourceid}:publish">
        <a plain="true" method="publish" validFun="whgListTool.optValid4EditState">发布</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:publishoff">
        <a plain="true" method="publishoff" validKey="state" validVal="6">撤销发布</a>
    </shiro:hasPermission>

    <shiro:hasPermission name="${resourceid}:recovery">
        <a plain="true" method="recovery" validFun="whgListTool.optValid4RecoveryState">回收</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:undel">
        <a plain="true" method="undel" validKey="delstate" validVal="1">撤销回收</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:del">
        <a plain="true" method="del" validFun="whgListTool.optValid4Del">删除</a>
    </shiro:hasPermission>



</div>
<!-- 操作按钮-END -->

<script>

    $(function () {
        //条件查询-文化馆
        WhgComm.initPMS({
            cultEid: 'cultid', cultValue: '', allcult: true,
            deptEid: 'deptid', deptValue: '', alldept: true
        });
        $("#state").combobox({
            valueField: 'id',
            textField: 'text',
            value: whgListTool.getDefaultState4PageType(),
            data: whgListTool.getDataState4PageType()
        });
        WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/personnel/srchList4p?type=${type}');
    });

    var whgListTool = new Gridopts({
        pageType: "${type}",
        pageTypeE: "edit",
        pageTypeC: "check",
        pageTypeP: "publish",
        pageTypeD: "recycle"
    });
    Gridopts.prototype.resource = function (idx) {
        var row = this.__getGridRow(idx);
        var cultid = $('#cultid').combobox('getValue');
        WhgComm.editDialog('${basePath}/admin/personnel/srchList4p?type=${type}&refid=' + row.id + "&cultid=" + cultid);
    };
    Gridopts.prototype.supplymge = function(idx){
        var row = this.__getGridRow(idx);
        WhgComm.editDialog('${basePath}/admin/supply/times/view/list?supplytype=${supplytype}&supplyid='+row.id);
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
    /**
     * 资源管理
     * */
    function doResource(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        //var cultid = $('#cultid').combobox('getValue');
        //WhgComm.editDialog('${basePath}/admin/resource/view/list?reftype=25&refid=' + curRow.id+'&cultid='+cultid);
        WhgComm.editDialog('${basePath}/admin/mass/resmanage/view/list?refid=' + curRow.id+'&reftype=25');
    }
    /*function doResource2(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        var cultid = $('#cultid').combobox('getValue');
        WhgComm.editDialog('${basePath}/admin/resource/view/list?reftype=25&refid=' + curRow.id+'&cultid='+cultid);
    }*/
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
     * 获奖情况
     * @param idx
     */
    function awards(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/per/awards/view/list?entid='+row.id );
    }

    /**
     * 添加
     */
    function add(){
        WhgComm.editDialog('${basePath}/admin/personnel/view/add');
    }

    /**
     * 编辑
     * @param idx
     */
    function edit(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/personnel/view/add?id='+row.id);
    }

    /**
     * 查看
     * @param idx
     */
    function view(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/personnel/view/add?targetShow=1&id='+row.id);
    }


    /**
     * 删除
     * @param idx
     */
    function del(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        var confireStr = '';
        if (row.delstate == 1 || row.state == 1){
            confireStr = '此操作将会永久性删除数据！确定要删除选中的项吗？'+ confireStr;
        }else{
            confireStr = '确定要回收选中的项吗？';
        }
        $.messager.confirm("确认信息", confireStr, function(r){
            if (r){
                $.messager.progress();
                $.post('${basePath}/admin/personnel/del', {id: row.id}, function(data){
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
     * 回收
     * @param idx
     */
    function recovery(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        var confireStr = '确定要回收选中的项吗？';
        $.messager.confirm("确认信息", confireStr, function (r) {
            if (r) {
                $.messager.progress();
                $.post('${basePath}/admin/personnel/recovery', {ids: row.id}, function (data) {
                    $("#whgdg").datagrid('reload');
                    if (!data.success || data.success != "1") {
                        $.messager.alert("错误", data.errormsg || '操作失败', 'error');
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
                $.post('${basePath}/admin/personnel/undel', {id: row.id, delstate: 0}, function(data){
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
                __updStateSend(row.id, 6, 4);
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
        $.post('${basePath}/admin/personnel/updstate', params, function(data){
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
        $.post('${basePath}/admin/personnel/updCommend', params, function(data){
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
