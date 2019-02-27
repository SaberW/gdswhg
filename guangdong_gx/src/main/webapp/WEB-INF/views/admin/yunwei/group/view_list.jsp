<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>培训组管理</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>
    <title>${pageTitle}</title>
</head>
<body>
<!-- 表格 -->
<table id="whgdg" title="培训组管理" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:true, pagination:true, toolbar:'#whgdg-tb', url:'${basePath}/admin/yunwei/group/srchList4p'">
    <thead>
    <tr>
        <th data-options="field:'name', width:350">培训组名称</th>
        <th data-options="field:'max', width:160">限制会员报名数</th>
        <th data-options="field:'crtuser',width:120, formatter:WhgComm.FMTUserName">创建者</th>
        <th data-options="field:'crtdate',width:130, formatter:WhgComm.FMTDateTime, sortable:true">创建时间</th>
        <th data-options="field:'state', width:50, formatter:WhgComm.FMTState">状态</th>
        <th data-options="field:'_opt', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <div class="whgd-gtb-btn">
        <shiro:hasPermission name="${resourceid}:add"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="add();">添加</a></shiro:hasPermission>
    </div>
    <div class="whgdg-tb-srch">
        <input class="easyui-combobox" name="cultid" id="cultid" style="width:200px;" panelHeight="auto" data-options="prompt:'请选择文化馆'">
        <input class="easyui-textbox" style="width: 200px;" name="name" data-options="prompt:'请输入培训组名称', validType:'length[1,32]'" />
        <input class="easyui-combobox" style="width: 130px;" name="state" data-options="prompt:'请选择状态',editable:false, valueField:'id', textField:'text', data:WhgComm.getState()" />
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="0" method="doEdit">编辑</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:on"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="0" method="doOn">启用</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:off"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="1" method="doOff">停用</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:del"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="0" method="doDel">删除</a></shiro:hasPermission>
</div>
<!-- 操作按钮-END -->

<!-- 添加表单 -->
<div id="whgwin-add" style="display: none">
    <form id="whgff" class="whgff" method="post">
        <input type="hidden" name="id"/>

        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 25%"><i>*</i>所属单位　　　：</div>
            <div class="whgff-row-input" style="width: 75%">
                <input class="easyui-combobox" name="cultid" id="cultid2" style="width:90%; height:32px" panelHeight="auto" data-options="prompt:'请选择文化馆', required:true">
            </div>
        </div>

        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 25%"><i>*</i>培训组名称　　：</div>
            <div class="whgff-row-input" style="width: 75%"><input class="easyui-textbox" name="name" style="width:90%; height:32px" data-options="required:true,validType:'length[0,30]'"></div>
        </div>

        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 25%"><i>*</i>限制会员报名数：</div>
            <div class="whgff-row-input" style="width: 75%">
                <input class="easyui-numberspinner" name="max" style="width:90%; height:32px" data-options="required:true,min:1,max:999">
                <i style="display: block;">注意：表示允许一个会员最多报名参加属于此培训组的培训的数量。</i>
            </div>
        </div>
    </form>
</div>
<div id="whgwin-add-btn" style="text-align: center; display: none">
    <div style="display: inline-block; margin: 0 auto">
        <a href="#" class="easyui-linkbutton" iconCls="icon-save" id="btn" >保 存</a>
        <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#whgwin-add').dialog('close')">关 闭</a>
    </div>
</div>
<!-- 添加表单 END -->



<script>
    $(function () {
        //条件查询-文化馆
        WhgComm.initPMS({cultEid:'cultid'});
    });

    /** 添加 */
    function add(){
        $('#whgwin-add').dialog({
            title: '培训组管理-添加培训组',
            cache: false,
            modal: true,
            width: '600',
            height: '280',
            buttons: '#whgwin-add-btn',
            onOpen: function () {
                //init Form
                $('#whgff').form({
                    url : '${basePath}/admin/yunwei/group/add',
                    onSubmit : function(param) {
                        var isValid = $(this).form('enableValidation').form('validate');
                        if(isValid){
                            $.messager.progress();
                        }else{
                            $("#btn").off("click").one("click",function () { $('#whgff').submit(); });
                        }
                        return isValid
                    },
                    success : function(data) {
                        $.messager.progress('close');
                        var Json = $.parseJSON(data);
                        if(Json.success == "1"){
                            $('#whgdg').datagrid('reload');
                          //  $.messager.alert("提示", "操作成功");
                            $('#whgwin-add').dialog('close');
                        }else{
                            $.messager.alert("提示", Json.errormsg);
                            $("#btn").off("click").on("click",function () {$('#whgff').submit();});
                        }

                    }
                });
                WhgComm.initPMS({cultEid:'cultid2'});
                $('#whgff').form("clear");
                WhgComm.initPMS({cultEid:'cultid2'});
                $("#btn").off("click").one("click",function () { $('#whgff').submit(); });
                $(this).form('disableValidation');
            }
        });
    }

    /** 更新关键字方法 */
    function doEdit(idx){
        $('#whgwin-add').dialog({
            title: '培训组管理-编辑培训组',
            cache: false,
            modal: true,
            width: '600',
            height: '280',
            buttons: '#whgwin-add-btn',
            onOpen : function () {
                var rows = $("#whgdg").datagrid('getRows');
                var row = rows[idx];
                var id = row.id;
                if (row){
                    $('#whgff').form('load', row);
                    $('#whgff').form({
                        url : '${basePath}/admin/yunwei/group/edit',
                        onSubmit : function(param) {
                            var isValid = $(this).form('enableValidation').form('validate');
                            if(isValid){
                                $.messager.progress();
                            }else{
                                $("#btn").off("click").one("click",function () { $('#whgff').submit(); });
                            }
                            return isValid
                        },
                        success : function(data) {
                            $.messager.progress('close');
                            var Json = $.parseJSON(data);
                            if(Json.success == "1"){
                                $('#whgdg').datagrid('reload');
                              //  $.messager.alert("提示", "操作成功");
                                $('#whgwin-add').dialog('close');
                            }else{
                                $.messager.alert("提示", Json.errormsg);
                            }

                        }
                    });

                }
                $("#btn").off("click").on("click",function () {$('#whgff').submit();});
                WhgComm.initPMS({cultEid:'cultid2', cultValue:row.cultid});
                $(this).form('disableValidation');
            }
        });
    }

    /*
     * 启用
     **/
    function doOn(idx){
        _doAjax('on', idx);
    }

    /*
     * 停用
     **/
    function doOff(idx){
        _doAjax('off', idx, '您确认要停用选中的项吗？');
    }

    /*
     * 删除
     **/
    function doDel(idx){
        _doAjax('del', idx, '您确认要删除选中的项吗？');
    }

    /*
     * 删除
     **/
    function _doAjax(opt, idx, message){
        var row = $("#whgdg").datagrid('getRows')[idx];
        if(message){
            WhgComm.confirm('确认消息', message, function(){
                doAjax(opt, row.id);
            });
        }else{
            doAjax(opt, row.id);
        }
        function doAjax(opt, id) {
            $.ajax({
                type: "POST",
                url: "${basePath}/admin/yunwei/group/"+opt,
                data: {id : id},
                success: function(data){
                    if(data.success == "1"){
                        $('#whgdg').datagrid('reload');
                    }else{
                        $.messager.alert("提示", data.errormsg);
                    }
                }
            });
        }
    }
</script>
</body>
</html>
