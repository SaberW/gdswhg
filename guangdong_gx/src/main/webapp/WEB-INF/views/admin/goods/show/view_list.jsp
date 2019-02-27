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
        <c:when test="${type eq 'edit'}">
            <c:set var="pageTitle" value="文艺演出编辑列表"></c:set>
            <c:set var="pageValue" value="1"></c:set>
        </c:when>
        <c:when test="${type eq 'check'}">
            <c:set var="pageTitle" value="文艺演出审核列表"></c:set>
            <c:set var="pageValue" value="9"></c:set>
        </c:when>
        <c:when test="${type eq 'publish'}">
            <c:set var="pageTitle" value="文艺演出发布列表"></c:set>
            <c:set var="pageValue" value="2"></c:set>
        </c:when>
        <c:when test="${type eq 'del'}">
            <c:set var="pageTitle" value="回收站"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="文艺演出列表"></c:set>
        </c:otherwise>
    </c:choose>
    <title>${pageTitle}</title>

    <script src="${basePath}/static/admin/js/whgmodule-gridopts.js"></script>
</head>
<body>
<jsp:include page="../../fkproject/fkproject_condition.jsp"/>
<table id="whgdg" class="easyui-datagrid" title="${pageTitle}" style="display: none;"
       pagination=true pageSize=30 pageList="[10,20,30,50,100,200]" loadMsg="数据加载中，请稍候。。。"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:true, toolbar:'#whgdg-tb'">
    <thead>
    <tr>
        <th data-options="width:100, sortable:false, field:'title' ">名称</th>
        <th data-options="width:100, sortable:false, field:'type',formatter:WhgComm.FMTShowGoodsType ">类型</th>
        <th data-options="width:100, sortable: true, field:'ismoney', formatter:FMThasfees ">收费</th>
        <th data-options="field:'crtdate', width:130, formatter:WhgComm.FMTDateTime">创建时间</th>
        <th data-options="field:'crtuser',width:120, formatter:WhgComm.FMTUserName ">编辑者</th>
        <th data-options="field:'checkor',width:120, formatter:WhgComm.FMTUserName ">审核者</th>
        <th data-options="field:'publisher',width:120, formatter:WhgComm.FMTUserName ">发布者</th>
        <th data-options="field:'state',width:50, formatter:WhgComm.FMTBizState" >状态</th>
        <th data-options="width:130, sortable: true, field:'statemdfdate', formatter:WhgComm.FMTDateTime ">操作时间</th>
        <th data-options="field:'id', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>


<div id="whgdg-tb" style="display: none;">
    <c:if test="${type eq 'edit'}">
        <shiro:hasPermission name="${resourceid}:add">
            <div class="whgd-gtb-btn">
                <a class="easyui-linkbutton" iconCls="icon-add" onclick="add()">添 加</a>
            </div>
        </shiro:hasPermission>
    </c:if>
    <div class="whgdg-tb-srch">
        <input class="easyui-combobox" name="cultid" id="cultid" data-options="editable:true,width:180" prompt="请选择文化馆"/>
        <input class="easyui-combobox" name="deptid" id="deptid" data-options="editable:true,width:120" prompt="请选择部门"/>
        <input class="easyui-textbox" name="title" prompt="请输入文艺演出名称" data-options="width:120">
        <c:choose>
            <c:when test="${type eq 'edit'}">
                <select class="easyui-combobox" style="width: 110px;" name="state" prompt="请选择状态" panelHeight="auto" limitToList="true" data-options="editable:false,width:120, value:'${pageValue}', valueField:'id', textField:'text', data:WhgComm.getBizState()"></select>
            </c:when>
            <c:when test="${type eq 'del'}"></c:when>
            <c:otherwise>
                <select class="easyui-combobox" style="width: 110px;" name="state" prompt="请选择状态" panelHeight="auto" limitToList="true" data-options="editable:false,width:120, value:'${pageValue}', valueField:'id', textField:'text', data:WhgComm.getBizState('1')"></select>
            </c:otherwise>
        </c:choose>

        <a class="easyui-linkbutton" id="button" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>


<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">

    <shiro:hasPermission name="${resourceid}:view">      <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="view">       查看</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:edit">      <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validEdit" method="edit">       编辑</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:ziyuan">    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validResource" method="resource">   资源管理</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:supplymge"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validResource" method="supplymge">   供需管理</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:yjpz">      <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validyjpz" method="yjpz">       硬件设施要求</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:jmd">       <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validjmd" method="jmd">        节目单</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkgo">   <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validCheckGo" method="checkgo" >   提交审核</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkon">   <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validCheckOn" method="checkon" >   审核通过</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkoff">  <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validCheckOff" method="checkoff" >      审核打回</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:publish">   <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validPublish" method="publish" >   发布</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:publishoff"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validPublishOff" method="publishoff" >撤销发布</a></shiro:hasPermission>

    <shiro:hasPermission name="${resourceid}:recovery">  <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validRecycle" method="del">回收</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:undel">     <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validUndel" method="undel">撤销回收</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:del">       <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validDel" method="del">删除</a></shiro:hasPermission>

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
    var pageType = '${type}';

    $(function () {
        //条件查询-文化馆
        WhgComm.initPMS({
            cultEid:'cultid', cultValue:'',allcult: true,
            deptEid:'deptid', deptValue:'',alldept: true
        });

        WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/showGoods/srchList4p?pagetype=${type}');
    });

    function FMThasfees(v){
        return v? "收费":"免费";
    }

    //操作按钮显示控制
    function validEdit(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        if(pageType == 'edit'){
            return row.state == 1;
        }else if(pageType == 'check'){
            return row.state == 9;
        }else if(pageType == 'publish'){
            return row.state == 2 || row.state == 4 ;
        }
    }
    function validyjpz(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        if(pageType == 'edit'){
            return row.state == 1;
        }else if(pageType == 'check'){
            return row.state == 9;
        }else if(pageType == 'publish'){
            return row.state == 2 || row.state == 4 || row.state == 6;
        }
    }
    function validjmd(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        if(pageType == 'edit'){
            return row.state == 1;
        }else if(pageType == 'check'){
            return row.state == 9;
        }else if(pageType == 'publish'){
            return row.state == 2 || row.state == 4 || row.state == 6;
        }
    }
    function validResource(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        if(pageType == 'edit'){
            return row.state == 1;
        }else if(pageType == 'check'){
            return row.state == 9;
        }else if(pageType == 'publish'){
            return row.state == 2 || row.state == 4 || row.state == 6;
        }
    }
    function validCheckGo(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return pageType == 'edit' && row.state == 1;
    }
    function validCheckOn(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return pageType == 'check' && row.state == 9;
    }
    function validCheckOff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return pageType == 'check' && row.state == 9;
    }
    function validPublish(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return pageType == 'publish' && (row.state == 2 || row.state == 4);
    }
    function validPublishOff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return pageType == 'publish' && row.state == 6;
    }

    function validDel(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return (pageType == 'edit' && row.state == 1) || (pageType == 'del' && row.delstate == 1);
    }
    function validRecycle(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return (pageType == 'check' && row.state == 9) || (pageType == 'publish' && (row.state == 2 || row.state == 4));
    }
    function validUndel(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return pageType == 'del' && row.delstate == 1;
    }

    /**
     * 添加
     * @param idx
     */
    function add(){
        WhgComm.editDialog('${basePath}/admin/showGoods/view/add');
    }


    /**
     * 编辑
     * @param idx
     */
    function edit(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/showGoods/view/add?pageType=1&id='+row.id);
    }

    /**
     * 硬件配置
     * @param idx
     */
    function yjpz(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/yunwei/yjpz/view/list?type=21&entid='+row.id );
    }

    /**
     * 节目单
     * @param idx
     */
    function jmd(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/show/jmd/view/list?entid='+row.id );
    }

    /**
     * 查看
     * @param idx
     */
    function view(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/showGoods/view/add?pageType=2&id='+row.id);
    }

    /**
     * 发布 [2,4]->6
     * @param idx
     */
    function publish(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];

        $('#whgwin-add').dialog({
            title: '设置发布时间',
            cache: false,
            modal: true,
            width: '400',
            height: '150',
            maximizable: true,
            resizable: true,
            buttons: '#whgwin-add-btn',
            onOpen: function () {
                $(".statemdfdate").datetimebox('setValue',new Date().Format('yyyy-MM-dd hh:mm:ss'));
                $('#whgff').form({
                    url : '${basePath}/admin/showGoods/updstate',
                    onSubmit : function(param) {
                        param.ids = row.id;
                        param.formstates = "2,4";
                        param.tostate = 6;
                        var isValid = $(this).form('enableValidation').form('validate');
                        if(isValid){
                            $.messager.progress();
                        }else{
                            $("#btn").off("click").one("click",function () { $('#whgff').submit(); });
                        }
                        return isValid;
                    },
                    success : function(data) {
                        $.messager.progress('close');
                        var Json = $.parseJSON(data);
                        if(Json.success == "1"){
                            $('#whgdg').datagrid('reload');
                            $('#whgwin-add').dialog('close');
                        }else{
                            $.messager.alert("提示", data.errorMsg);
                        }
                    }
                });
                $("#btn").off("click").one("click",function () { $('#whgff').submit(); });
            }
        })
    }

    /**
     * 取消发布 [6]->4
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
        $.post('${basePath}/admin/showGoods/updstate', params, function(data){
            $("#whgdg").datagrid('reload');
            if (!data.success || data.success != "1"){
                $.messager.alert("错误", data.errormsg||'操作失败', 'error');
            }
            $.messager.progress('close');
        }, 'json');
    }

    /*/!**
     * 删除
     * @param idx
     *!/
    function del(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        var confireStr = '确定要删除选中的项吗？'
        if (row.delstate == 1){
            confireStr = '此操作将会永久性删除数据！'+ confireStr;
        }
        $.messager.confirm("确认信息", confireStr, function(r){
            if (r){
                $.messager.progress();
                $.post('${basePath}/admin/showGoods/del', {id: row.id}, function(data){
                    $("#whgdg").datagrid('reload');
                    if (!data.success || data.success != "1"){
                        $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                    }
                    $.messager.progress('close');
                }, 'json');
            }
        })
    }*/

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
        WhgComm.confirm("确认信息", confireStr, function(){
            $.messager.progress();
            $.post('${basePath}/admin/showGoods/del', {id: row.id}, function(data){
                $("#whgdg").datagrid('reload');
                if (!data.success || data.success != "1"){
                    $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                }
                $.messager.progress('close');
            }, 'json');
        })
    }


    /**
     * 还原删除项
     * @param idx
     */
    function undel(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "确定要还原选中的项吗？", function(){
            $.messager.progress();
            $.post('${basePath}/admin/showGoods/undel', {id: row.id, delstate: 0}, function(data){
                $("#whgdg").datagrid('reload');
                if (!data.success || data.success != "1"){
                    $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                }
                $.messager.progress('close');
            }, 'json');
        })
    }


    /**
     * 资源
     * @param idx
     */
    function resource(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        //var cultid = $('#cultid').combobox('getValue');
        //WhgComm.editDialog('${basePath}/admin/resource/view/list?reftype=21&refid=' + row.id+"&cultid="+cultid);
        WhgComm.editDialog('${basePath}/admin/mass/resmanage/view/list?refid=' + row.id+'&reftype=21');
    }

    function supplymge(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/supply/times/view/list?supplytype=${supplytype}&supplyid='+row.id);
    }

</script>
</body>
</html>
