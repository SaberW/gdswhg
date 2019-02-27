<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>群文配额管理</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
</head>
<body class="easyui-layout">

<div data-options="region:'center',title:'群文配额管理列表'">
    <!-- 表格 -->
        <table id="whgdg" class="easyui-datagrid" style="display: none"
               data-options="fit:true, striped:true, rownumbers:false, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:false, pagination:true, toolbar:'#whgdg-tb',  url:'${basePath}/admin/yunwei/quota/srchList4p'">
        <thead>
        <tr>
            <th data-options="width: 30, checkbox: true, field:'checkbox' ">全选</th>
            <th data-options="field:'name', width:250">文化馆名称</th>
            <th data-options="field:'size', width:250,formatter:FMTSize">系统空间设定</th>
            <th data-options="field:'usedsize', width:250,formatter:FMTUsedsize">已使用空间</th>
            <th data-options="align:'center',field:'usedstate', width:250,formatter:FMTUsedState">空间使用</th>
            <th data-options="field:'_opt', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
        </tr>
        </thead>
    </table>
    <!-- 表格 END -->

    <!-- 表格操作工具栏 -->
    <div id="whgdg-tb" style="display: none;">
        <shiro:hasPermission name="${resourceid}:edit">
        <div class="whgd-gtb-btn">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doBatchEdit();">批量修改</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="default_set();">默认设定</a>
        </div>
        </shiro:hasPermission>
        <div class="whgdg-tb-srch">
            <input type="hidden" id="province" name="province" value=""/>
            <input type="hidden" id="city" name="city" value=""/>
            <input type="hidden" id="area" name="area" value=""/>
            <input class="easyui-textbox" style="width: 200px;" name="name" data-options="prompt:'请输入分馆名称', validType:'length[1,32]'" />
            <input class="easyui-combobox" style="width: 200px;" id="usedstate" name="usedstate" data-options="prompt:'请选择状态',editable:false,valueField:'id',textField:'text',data:[{id:'1',text:'空间良好'},{id:'2',text:'空间不足'}]"/>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
        </div>
    </div>
    <!-- 表格操作工具栏-END -->

    <!-- 操作按钮 -->
    <div id="whgdg-opt" style="display: none;">
        <shiro:hasPermission name="${resourceid}:edit">
            <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="doEdit">编辑</a>
        </shiro:hasPermission>
    </div>
    <!-- 操作按钮-END -->
</div>


<!-- 添加表单 -->
<div id="whgwin-add" style="display: none">
    <form id="whgff" class="whgff" method="post">
        <input type="hidden" name="id"/>
        <input type="hidden" name="cultid"/>

        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 55%">
                <span style="float: left;margin-left: 25px;" id="now_usedsize"></span></div>
        </div>

        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 35%"><i>*</i>输入存储空间大小：</div>
            <div class="whgff-row-input" style="width: 65%"><input class="easyui-numberspinner" name="size" style="width:80%; height:32px" data-options="required:true, min:1, max:1000000">GB</div>
        </div>

        <div class="whgff-row" id="size_tip" style="display: none">
            <div class="whgff-row-label" style="width: 75%">
                <i><span style="float: left;font-size: 12px;"> *修改空间小于实际使用，请核对后重新输入。</span></i>
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




<!-- 添加表单 -->
<div id="batch-edit" style="display: none">
    <form id="batch-edit-form" class="whgff" method="post">
        <input type="hidden" id="cultids" name="cultids"/>

        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 35%"><i>*</i>输入存储空间大小：</div>
            <div class="whgff-row-input" style="width: 65%"><input class="easyui-numberspinner" name="size" style="width:80%; height:32px" data-options="required:true, min:1, max:1000000">GB</div>
        </div>

        <div class="whgff-row" id="batch-edit-tip" style="display: none">
            <div class="whgff-row-label" style="width: 75%">
                <i><span style="float: left;font-size: 12px;"> *修改空间小于实际使用，请核对后重新输入。</span></i>
            </div>
        </div>
    </form>
</div>
<div id="batch-edit-btn" style="text-align: center; display: none">
    <div style="display: inline-block; margin: 0 auto">
        <a href="#" class="easyui-linkbutton" iconCls="icon-save" id="edit" >保 存</a>
        <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#batch-edit').dialog('close')">关 闭</a>
    </div>
</div>
<!-- 添加表单 END -->


<!-- script -->
<script type="text/javascript">
    //编辑审核发布删除页面
    var pageType = '${pageType}';

    $(function(){
    });
    
    function FMTSize(val) {
        var number = new Number(val);
        return  (number / 1024 / 1024 / 1024)+"GB";
    }

    /** 格式化大小 */
    function FMTUsedsize(val) {
        if(!val) return '0KB';
        var number = new Number(val);
        if(number / 1024 < 1024){
            return (number / 1024).toFixed(2)+"KB";
        }else if(number / 1024 / 1024 < 1024){
            return (number / 1024 / 1024).toFixed(2)+"MB";
        }else{
            return (number / 1024 / 1024 / 1024).toFixed(2)+"GB";
        }
        return '';
    }

    function FMTUsedState(val){
        if(val==2){
            return "<img src='${basePath}/static/admin/img/red_spot.png' style='width:35px;'>";
        }else{
            return "<img src='${basePath}/static/admin/img/green_spot.png' style='width:35px;'>";
        }
    }

    /**
     * 添加
     */
    function default_set() {
        WhgComm.openDialog4size('修改默认设定', '${basePath}/admin/yunwei/quota/view/set', 800, 300);
    }




    /**
     * 编辑信息
     * @param idx 行下标
     */
    function doEdit(idx) {
        $("#size_tip").hide();
        $('#whgwin-add').dialog({
            title: '修改系统空间大小',
            cache: false,
            modal: true,
            width: '400',
            height: '250',
            maximizable: true,
            resizable: true,
            // href: '${basePath}/admin/system/cult/view/add',
            buttons: '#whgwin-add-btn',
            onOpen : function () {
                var rows = $("#whgdg").datagrid('getRows');
                var row = rows[idx];
                var id = row.id;
                if (row){
                    if(row.usedsize){
                        $("#now_usedsize").html("当前使用空间大小为："+FMTUsedsize(row.usedsize));
                    }else {
                        $("#now_usedsize").html("当前使用空间大小为：0KB");
                    }

                    row.size = row.size / 1024 / 1024 / 1024;
                    $('#whgff').form('load', row);
                    $('#whgff').form({
                        url : '${basePath}/admin/yunwei/quota/edit',
                        onSubmit : function(param) {
                            var isValid = $(this).form('enableValidation').form('validate');
                            if(isValid){
                                var size = $("#whgff").find("input[name='size']").val();
                                var usedsize = row.usedsize;
                                console.log(usedsize);
                                if(usedsize){
                                    if(size<= new Number(usedsize)/1024/1024/1024){
                                        $("#size_tip").show();
                                        isValid = false;
                                    }else{
                                        $("#size_tip").hide();
                                        $.messager.progress();
                                    }
                                }else{
                                    $.messager.progress();
                                }
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
            }
        });
    }




    /**
     * 批量启用或停用
     * @param fromState 修改前的状态
     * @param toState 修改后的状态
     */

    function doBatchEdit() {

        //选中的记录数
        var rows = $('#whgdg').datagrid('getChecked');
        if (rows.length < 1) {
            $.messager.alert('提示', '请选中要操作的记录！', 'warning');
            return;
        }
        var _ids = "";
        var spt = "";
        console.log(rows.length)
        for (var i = 0; i < rows.length; i++) {
            _ids += spt + rows[i].cultid;
            spt = ',';
        }
        $("#cultids").val(_ids);
        $('#batch-edit').dialog({
            title: '批量修改系统空间大小',
            cache: false,
            modal: true,
            width: '400',
            height: '250',
            maximizable: true,
            resizable: true,
            buttons: '#batch-edit-btn',
            onOpen : function () {
                    $('#batch-edit-form').form({
                        url : '${basePath}/admin/yunwei/quota/batchEdit',
                        success : function(data) {
                            $.messager.progress('close');
                            var Json = $.parseJSON(data);
                            if(Json.success == "1"){
                                $('#whgdg').datagrid('reload');
                                console.log(Json);
                                if(Json.data){
                                    $.messager.alert("提示", "批量修改执行结束，其中“"+Json.data+"”修改空间小于实际使用，请核对后重新输入");
                                }else{
                                    $.messager.alert("提示", "批量修改成功。");
                                }
                                $('#batch-edit').dialog('close');
                            }else{
                                $.messager.alert("提示", Json.errormsg);
                            }
                        }
                    });
                $("#edit").off("click").on("click",function () {$('#batch-edit-form').submit();});
            }
        });
    }



</script>
<!-- script END -->
</body>
</html>