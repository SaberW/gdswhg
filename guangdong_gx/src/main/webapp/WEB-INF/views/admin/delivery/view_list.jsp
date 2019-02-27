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
        <c:when test="${clazz == 0}">
            <c:set var="pageTitle" value="供给申请审核"></c:set>
        </c:when>
        <c:when test="${clazz == 1}">
            <c:set var="pageTitle" value="提供配送审核"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="供给申请审核"></c:set>
        </c:otherwise>
    </c:choose>

    <title>${pageTitle}</title>
    <script src="${basePath}/static/admin/js/whgmodule-gridopts.js"></script>
</head>
<body>
<!-- 表格 -->

<table id="whgdg" class="easyui-datagrid" title="${pageTitle}" style="display: none;"
       pagination=true pageSize=30 pageList="[10,20,30,50,100,200]" loadMsg="数据加载中，请稍候。。。"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb', queryParams:{cultid: WhgComm.getMgrCults()[0].id},url:'${basePath}/admin/delivery/srchList4p?type=${clazz}'">
    <thead>
    <tr>
        <th data-options="checkbox: true, field:'checkbox' ">全选</th>
        <th data-options="width:100, sortable:false, field:'id' ">配送单号</th>
        <th data-options="width:120, sortable:false, field:'name' ">名称</th>
        <c:choose>
            <c:when test="${clazz == 0}">
                <th data-options="width:100, sortable:false, field:'fktype',formatter:FMTDeliveryType">类型</th>
                <th data-options="width:100, sortable:false, field:'cultname'">申请配送单位</th>
                <th data-options="width:150, sortable:false, field:'region'">申请配送区域</th>
            </c:when>
            <c:when test="${clazz == 1}">
                <th data-options="width:100, sortable:false, field:'cultname'">提供单位</th>
                <th data-options="width:150, sortable:false, field:'region'">申请配送区域</th>
            </c:when>
            <c:otherwise>
                <th data-options="width:100, sortable:false, field:'cultname'">申请配送单位</th>
                <th data-options="width:150, sortable:false, field:'region'">申请配送区域</th>
            </c:otherwise>
        </c:choose>
        <th data-options="width:100, sortable: true, field:'crtdate', formatter:WhgComm.FMTDateTime ">创建时间</th>
        <th data-options="width:120, sortable: true, field:'state', formatter:FMTDeliveryState ">状态</th>
        <th data-options="field:'_opt', formatter:WhgComm.FMTOpt,fixed:true, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <div class="whgdg-tb-srch" style="padding-top: 8px">
        <input class="easyui-textbox" style="width: 200px;" name="name" data-options="prompt:'请输入名称', validType:'length[1,32]'" />
        <select class="easyui-combobox" name="state" prompt="请选择状态" panelHeight="auto" limitToList="true"
                data-options="width:120, value:'', valueField:'id', textField:'text', data:[{'id':'', 'text':'全部'},{'id':'1', 'text':'待审核'},{'id':'2', 'text':'审核通过'},{'id':'3', 'text':'配送成功'},{'id':'0', 'text':'已驳回'}]">
        </select>
        <c:choose>
            <c:when test="${clazz == 0}">
                <select class="easyui-combobox" name="fktype" prompt="请选择配送类型" panelHeight="auto" limitToList="true"
                        data-options="width:120, value:'', valueField:'id', textField:'text',  data:[{'id':'', 'text':'全部'},{'id':'3', 'text':'供需活动室'},{'id':'23', 'text':'展览展示'},{'id':'21', 'text':'文艺演出'},{'id':'20', 'text':'文艺辅材'},{'id':'5', 'text':'供需培训'},{'id':'25', 'text':'文艺专家'}]">
                </select>
                <input class="easyui-textbox" style="width: 200px;" name="crtuser" data-options="prompt:'请选择申请配送单位', validType:'length[1,32]'"/>
            </c:when>
            <c:when test="${clazz == 1}">
                <input class="easyui-textbox" style="width: 200px;" name="crtuser" data-options="prompt:'请选择提供单位', validType:'length[1,32]'"/>
            </c:when>
            <c:otherwise>
                <select class="easyui-combobox" name="fktype" prompt="请选择配送类型" panelHeight="auto" limitToList="true"
                        data-options="width:120, value:'', valueField:'id', textField:'text',  data:[{'id':'', 'text':'全部'},{'id':'3', 'text':'供需活动室'},{'id':'23', 'text':'展览展示'},{'id':'21', 'text':'文艺演出'},{'id':'20', 'text':'文艺辅材'},{'id':'5', 'text':'供需培训'},{'id':'25', 'text':'文艺专家'}]">
                </select>
                <input class="easyui-textbox" style="width: 200px;" name="crtuser" data-options=" prompt:'请选择申请配送单位',validType:'length[1,32]'"/>
            </c:otherwise>
        </c:choose>


        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->


<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">

    <shiro:hasPermission name="${resourceid}:view"><a plain="true" method="view">查看</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkgo"><a plain="true" method="checkgo" validKey="state" validVal="1">配送审核</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkon"><a plain="true" method="checkon" validKey="state" validVal="2">完成配送</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:del"><a plain="true" method="del" validKey="state" validVal="0,1,2,3">删除</shiro:hasPermission>

</div>
<!-- 操作按钮-END -->

<!--发布设置发布时间表单 -->
<div id="whgwin-add" style="display: none">
    <form id="whgff" class="whgff" method="post">
        <div class="whgff-row">
            <div class="whgff-row-label _check" style="width: 30%"><i>*</i>发布时间：</div>
            <div class="whgff-row-input" style="width: 70%">
                <input class="easyui-datetimebox statemdfdate" name="optTime" style="width:200px; height:32px" data-options="required:true">
            </div>
        </div>
    </form>
</div>
<div id="whgwin-add-btn" style="text-align: center; display: none">
    <div style="display: inline-block; margin: 0 auto">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="btn" >确 定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#whgwin-add').dialog('close')">关 闭</a>
    </div>
</div>
<!-- 发布设置发布时间表单 END -->
<script>

    function FMThasfees(v){
        return v? "收费":"免费";
    }

    function FMTDeliveryType(v) {
        if ("3" == v) {
            return "供需活动室";
        } else if ("23" == v) {
            return "展览展示";
        } else if ("21" == v) {
            return "文艺演出";
        } else if ("5" == v) {
            return "供需培训";
        } else if ("20" == v) {
            return "文艺辅材";
        } else if ("25" == v) {
            return "文艺专家";
        }
        return "";
    }

    function FMTDeliveryState(v) {
        if ("1" == v) {
            return "待审核";
        } else if ("2" == v) {
            return "审核通过";
        } else if ("3" == v) {
            return "配送成功";
        } else if ("0" == v) {
            return "审核不通过";
        }
        return "";
    }


    /**
     * 查看
     * @param idx
     */
    function view(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/delivery/view/add/${clazz}?targetShow=1&id=' + row.id);
    }




    /**
     * 送审 [1,5]->9
     * @param idx
     */
    function checkgo(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/delivery/view/add/${clazz}?id=' + row.id);
    }

    /**
     * 审通过 [9]->2
     * @param idx
     */
    function checkon(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要完成配送选中的项吗？", function(r){
            if (r){
                __updStateSend(row.id, 2, 3);
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
        $.post('${basePath}/admin/delivery/updstate', params, function(data){
            $("#whgdg").datagrid('reload');
            if (!data.success || data.success != "1"){
                $.messager.alert("错误", data.errormsg||'操作失败', 'error');
            }
            $.messager.progress('close');
        }, 'json');
    }

    /**
     * 删除
     * @param idx
     */
    function del(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        var confireStr = '确定要删除选中的项吗？'
        if (row.delstate == 1){
            confireStr = '此操作将会永久性删除数据！'+ confireStr;
        }
        $.messager.confirm("确认信息", confireStr, function(r){
            if (r){
                $.messager.progress();
                $.post('${basePath}/admin/delivery/del', {id: row.id}, function(data){
                    $("#whgdg").datagrid('reload');
                    if (!data.success || data.success != "1"){
                        $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                    }
                    $.messager.progress('close');
                }, 'json');
            }
        })
    }


</script>
</body>
</html>
