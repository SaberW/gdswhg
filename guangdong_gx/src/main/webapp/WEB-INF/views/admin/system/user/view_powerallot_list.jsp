<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>权限分配</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <style>

    </style>
</head>
<body>
<!-- 表格 -->
<table id="whgdg" title="权限分配" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:true, checkOnSelect:true, selectOnCheck:false, pagination:true, toolbar:'#whgdg-tb', queryParams:{cultid:WhgComm.getMgrCultsFirstId(),userid:'${userId}'}, url:'${basePath}/admin/mass/library/srchlibsByPage'">
    <thead>
    <tr>
        <th data-options="field:'ck',checkbox:true"></th>
        <th data-options="field:'name', width:300">资源库名称</th>
        <th data-options="field:'opt', formatter:formatOper, width:150">权限操作</th>
    </tr>
    </thead>
</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <div class="whgdg-tb-srch">
        <input type="hidden" value="${userId}" id="userId">
        <div style="float: left;width: 200px;">账号：${account} </div>
        <input class="easyui-textbox" style="width: 200px;" name="name" data-options="prompt:'请输入群文库名称', validType:'length[1,32]'" />
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>

        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="doBatchApiSelect()" style="float: right;margin-right: 10px;">批量分配API接口</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="doBatchDownloadSelect()" style="float: right;margin-right: 20px;">批量分配下载</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="doBatchViewSelect()" style="float: right;margin-right: 10px;">批量分配查看</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<%--<div id="whgdg-opt" style="display: none;">
    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true"><label><input type="checkbox" name="powerCheckbox" value="view" onclick="doPowerSelect(this)">查看</label></a>
    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" style="margin-left: 30px;"><label><input type="checkbox" name="powerCheckbox" value="download" onclick="doPowerSelect(this)">下载</label></a>
</div>--%>
<!-- 操作按钮-END -->

<!-- script -->
<script type="text/javascript">

    function formatOper(value, row, index) {
        var view = '<a href="javascript:void(0)" style="color: black;text-decoration: none;"><label><input type="checkbox" class="viewCheckbox_'+row.id+'" name="powerCheckbox_'+row.id+'" value="view" onclick="doPowerSelect(this, \''+row.id+'\')">查看</label></a>';
        var viewChecked = '<a href="javascript:void(0)" style="color: black;text-decoration: none;"><label><input type="checkbox" class="viewCheckbox_'+row.id+'" name="powerCheckbox_'+row.id+'" checked value="view" onclick="doPowerSelect(this, \''+row.id+'\')">查看</label></a>';

        var download = '<a href="javascript:void(0)" style="margin-left: 30px;color: black;text-decoration: none;"><label><input type="checkbox" class="downloadCheckbox_'+row.id+'" name="powerCheckbox_'+row.id+'" value="download" onclick="doPowerSelect(this, \''+row.id+'\')">下载</label></a>';
        var downloadChecked = '<a href="javascript:void(0)" style="margin-left: 30px;color: black;text-decoration: none;"><label><input type="checkbox" class="downloadCheckbox_'+row.id+'" checked name="powerCheckbox_'+row.id+'" value="download" onclick="doPowerSelect(this, \''+row.id+'\')">下载</label></a>';

        var api = '<a href="javascript:void(0)" style="margin-left: 30px;color: black;text-decoration: none;"><label><input type="checkbox" class="apiCheckbox_'+row.id+'"  value="api" onclick="doPowerSelect(this, \''+row.id+'\')">API接口</label></a>';
        var apiChecked = '<a href="javascript:void(0)" style="margin-left: 30px;color: black;text-decoration: none;"><label><input type="checkbox" class="apiCheckbox_'+row.id+'" checked  value="api" onclick="doPowerSelect(this, \''+row.id+'\')">API接口</label></a>';
        var type = row.arttype;
        var returnVal = '';
        if(typeof(type) != 'undefined' && type.indexOf('download') >= 0){// 下载 查看
            returnVal =  viewChecked + downloadChecked;
        }else if(typeof(type) != 'undefined' && type.indexOf('view') >= 0 && type.indexOf('download') < 0){   //查看
            returnVal =  viewChecked + download;
        }else{
            returnVal =  view + download;
        }
        if(typeof(type) != 'undefined' && type.indexOf('api') >= 0){    //api接口
            returnVal +=  apiChecked;
        }else{
            returnVal +=  api;
        }
        return returnVal;
        /*if(typeof(type) != 'undefined' && type.indexOf('download') >= 0){// 下载 查看 都有
            return viewChecked + downloadChecked +api;
        }else if(typeof(type) != 'undefined' && type.indexOf('view') >= 0 && type.indexOf('download') < 0){// 只有查看
            return viewChecked + download;
        }else{// 都没有
            return view + download;
        }*/
    }
    
     /**
     * 权限按钮
     * @param obj
     */
    function doPowerSelect(obj, libid) {
//        var checkedItems = $('#whgdg').datagrid('getChecked');
         var userId = $('#userId').val();
         var flag = $(obj).prop("checked")?1:0;
         var toPower = $(obj).val();
         if($(obj).val() != 'api'){
             if($(obj).prop("checked") && $(obj).val() == 'download'){
                 $("input[name=powerCheckbox_"+libid+"]:checkbox").prop("checked", true);
                 $("input[class=viewCheckbox_"+libid+"]:checkbox").prop("disabled", true);
             }else{
                 $("input[class=viewCheckbox_"+libid+"]:checkbox").prop("disabled", false);
                 if(!$(obj).prop("checked") && $(obj).val() == 'view'){
                     $("input[class=downloadCheckbox_"+libid+"]:checkbox").prop("checked", false);
                     $("input[class=downloadCheckbox_"+libid+"]:checkbox").prop("disabled", true);
                 }else{
                     $("input[class=downloadCheckbox_"+libid+"]:checkbox").prop("disabled", false);
                 }
             }
         }

         _doUpdPower(libid, userId, flag, toPower);
    }

    /**
     * 批量分配查看权限按钮
     * @param obj
     */
    function doBatchViewSelect() {
        var checkedItems = $('#whgdg').datagrid('getChecked');
        var libids = "";
        if(checkedItems.length <= 0){
            $.messager.alert('提示', '请选择批量分配查看的数据！', 'error');
            return;
        }
        $.messager.confirm("确认信息", "确定要批量分配查看权限吗？", function(r){
            if (r){
                $.each(checkedItems , function(index, obj) {
                    libids += obj.id + ",";
                });
                var userId = $('#userId').val();
                _doUpdPower(libids, userId, "1", "view");
            }
        })
    }

    /**
     * 批量分配下载权限按钮
     * @param obj
     */
    function doBatchDownloadSelect() {
        var checkedItems = $('#whgdg').datagrid('getChecked');
        var libids = "";
        if(checkedItems.length <= 0){
            $.messager.alert('提示', '请选择批量分配下载的数据！', 'error');
            return;
        }
        $.messager.confirm("确认信息", "确定要批量分配下载权限吗？", function(r) {
            $.each(checkedItems, function (index, obj) {
                libids += obj.id + ",";
            });
            var userId = $('#userId').val();
            _doUpdPower(libids, userId, "1", "download");
        })
    }
    /**
     * 批量分配API接口权限按钮
     * @param obj
     */
    function doBatchApiSelect() {
        var checkedItems = $('#whgdg').datagrid('getChecked');
        var libids = "";
        if(checkedItems.length <= 0){
            $.messager.alert('提示', '请选择批量分配API接口的数据！', 'error');
            return;
        }
        $.messager.confirm("确认信息", "确定要批量分配API接口权限吗？", function(r) {
            $.each(checkedItems, function (index, obj) {
                libids += obj.id + ",";
            });
            var userId = $('#userId').val();
            _doUpdPower(libids, userId, "1", "api");
        })
    }
    /**
     * AJAX调用修改状态服务
     *
     * @param libids 资源库ID，多个用逗号分隔
     * @param userId 用户ID
     * @param flag 取消/开启（0：取消，1：开启）
     * @param toPower 权限（view：查看，download：下载）
     * @private
     */
    function _doUpdPower(libids, userId, flag, toPower){
        $.ajax({
            url: getFullUrl('/admin/mass/library/addUserAuth'),
            type: "POST",
            data: {libids:libids, userid:userId, flag: flag, power:toPower},
            cache: false,
            success: function (data) {
                if(data && data.success == '1'){
                    $('#whgdg').datagrid('reload');
                } else {
                    $.messager.alert('提示', '操作失败:'+data.errormsg+'！', 'error');
                    return;
                }
            }
        });
    }
</script>
<!-- script END -->
</body>
</html>