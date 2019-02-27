<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>硬件设施要求</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>

    <title>硬件设施配置</title>
</head>
<body>
<!-- 表格 -->
<table id="whgdg" title="硬件设施配置" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb', queryParams:{cultid:WhgComm.getMgrCultsFirstId()}, url:'${basePath}/admin/yunwei/yjpz/srchList4p?type=${type}&entid=${entid}'">
    <thead>
    <tr>
        <th data-options="field:'name', width:160">硬件设施配置名称</th>
        <th data-options="field:'detail', width:160">规格要求</th>
        <th data-options="field:'_opt', width:160, formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <div class="whgd-gtb-btn">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="add();">添加</a>
        <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
    </div>
    <div class="whgdg-tb-srch">
       <%-- <input class="easyui-combobox" name="cultid" id="cultid" style="width:200px;" panelHeight="auto" data-options="prompt:'请选择文化馆'">--%>
        <input class="easyui-textbox" style="width: 200px;" name="name" data-options="prompt:'请输入硬件设施名称', validType:'length[1,32]'" />
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="edit">编辑</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="del">删除</a>
</div>
<!-- 操作按钮-END -->

<!-- 添加表单 -->
<div id="whgwin-add" style="display: none">
    <form id="whgff" class="whgff" method="post">
        <input type="hidden" name="id"/>

        <%--<div class="whgff-row">
            <div class="whgff-row-label" style="width: 25%"><i>*</i>所属文化馆：</div>
            <div class="whgff-row-input" style="width: 75%">
                <input class="easyui-combobox" name="cultid" id="cultid2" style="width:90%; height:32px" panelHeight="auto" data-options="prompt:'请选择文化馆', required:true">
            </div>
        </div>--%>

        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 25%"><i>*</i>名称：</div>
            <div class="whgff-row-input" style="width: 75%"><input class="easyui-textbox" name="name" style="width:90%; height:32px" data-options="required:true,validType:'length[0,20]'"></div>
        </div>

        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 25%"><i>*</i>规格要求：</div>
            <div class="whgff-row-input" style="width: 75%"><input class="easyui-textbox" name="detail" style="width:90%; height:60px" data-options="required:true,validType:'length[0,200]'"></div>
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
    var type = '${type}';
    var entid = '${entid}';
   /* $(function () {
        //条件查询-文化馆
        WhgComm.initPMS({cultEid:'cultid'});
    });*/

    /** 添加关键字 */
    function add(){

        $('#whgwin-add').dialog({
            title: '硬件管理-添加硬件',
            cache: false,
            modal: true,
            width: '400',
            height: '250',
            maximizable: true,
            resizable: true,
            buttons: '#whgwin-add-btn',
            onOpen: function () {
                $('#whgff').form({
                    url : '${basePath}/admin/yunwei/yjpz/add?type='+type+'&entid='+entid,
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
            }
        });
    }

    /** 更新关键字方法 */
    function edit(idx){
        $('#whgwin-add').dialog({
            title: '硬件管理-编辑硬件',
            cache: false,
            modal: true,
            width: '400',
            height: '250',
            maximizable: true,
            resizable: true,
            buttons: '#whgwin-add-btn',
            onOpen : function () {
                var rows = $("#whgdg").datagrid('getRows');
                var row = rows[idx];
                var id = row.id;
                if (row){
                    $('#whgff').form('load', row);
                    $('#whgff').form({
                        url : '${basePath}/admin/yunwei/yjpz/edit',
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
                                $('#whgwin-add').dialog('close');
                            }else{
                                $.messager.alert("提示", Json.errormsg);
                            }
                        }
                    });
                }
                $("#btn").off("click").on("click",function () {$('#whgff').submit();});
                WhgComm.initPMS({cultEid:'cultid2', cultValue:row.cultid});
            }
        });
    }

    /*
     * 删除硬件配置 */
    function del(idx){
        var rows = $("#whgdg").datagrid('getRows');
        var row = rows[idx];
        var id = row.id;
        $.messager.confirm('确认对话框', '您确认要删除吗？', function(r){
            if (r){
                $.ajax({
                    type: "POST",
                    url: '${basePath}/admin/yunwei/yjpz/del',
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
        });
    }
</script>
</body>
</html>
