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
    <title>非遗展馆管理</title>
    <script src="${basePath}/static/admin/js/whgmodule-gridopts.js"></script>
</head>
<body>
<!-- 表格 -->
<table id="whgdg" title="非遗展馆管理" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb', url:'${basePath}/admin/showroom/showRoomList'">
    <thead>
    <tr>
        <th data-options="field:'name', width:60">名称</th>
        <th data-options="field:'type', width:60 ,formatter:roomType">类型</th>
        <th data-options="field:'createtime', width:60 ,formatter:datetimeFMT">创建时间</th>
        <th data-options="field:'state',width:60, sortable:true,formatter:WhgComm.FMTBizState">状态</th>
        <th data-options="field:'_opt', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <div>
        <shiro:hasPermission name="${resourceid}:add"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add"  onclick="add();" data-options="size:'small'">添加</a></shiro:hasPermission>
    </div>
    <div style="padding-top: 5px">
        <label>
            <input class="easyui-textbox" name="name" style="width:200px" data-options="prompt:'请输标题名称', validType:'length[1,32]'"/>
        </label>
        <input class="easyui-combobox" style="width: 200px;" name="state" panelHeight="auto" limitToList="true"
               data-options="prompt:'请选择状态', value:'', valueField:'id', textField:'text', data:[{id:'1',text:'可编辑'},
                    {id:'6',text:'已发布'}]"/>
        <a href="javascript:void(0)" class="easyui-linkbutton tb_search" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <shiro:hasPermission name="${resourceid }:view"><a href="javascript:void(0)"  method="lookAll">查看</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid }:edit"><a href="javascript:void(0)" validKey="state" validVal="1,4" method="edit">编辑</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:publish"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" validKey="state" validVal="1,2,4" method="publish">发布</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:publishoff"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" validKey="state" validVal="6" method="publishoff" >取消发布</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:recommend"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="_recommend" method="recommend">推荐</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:recommendoff"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="_recommendoff" method="recommendoff">取消推荐</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid }:del"><a href="javascript:void(0)" validKey="state" validVal="1,3,4"  method="del">删除</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:ziyuan"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="1,4" method="resource">展览作品管理</a></shiro:hasPermission>
</div>
<!-- 操作按钮-END -->

<!-- 添加表单 -->
<div id="frm" class="none" style="display: none" data-options="	maximized:true">
</div>
<div id="whgwin-add"></div>
<div id="whgwin-add-btn" style="text-align: center; display: none">xq
    <div style="display: inline-block; margin: 0 auto">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="whgwin-add-btn-save">保 存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#whgwin-add').dialog('close')">关 闭</a>
    </div>
</div>
<!-- 添加表单 END -->

<!-- script -->
<script type="text/javascript">
    function roomType(type){
        if(type ===1){
            return "虚拟展厅";
        }else {
            return "线上展厅";
        }
    }

    /**
     *推荐状态验证
     */
    function _recommendoff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 6 && row.isrecommend == 1;
    }
    function _recommend(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 6 && row.isrecommend == 0;
    }
    /**
     * 添加
     */
    function add() {
        WhgComm.editDialog('${basePath}/admin/showroom/view/add');
    }

    /**
     * 编辑
     * @param idx 行下标
     */
    function edit(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/showroom/view/edit?id='+curRow.id);
    }

    /**
     * 查看
     * @param idx 行下标
     */
    function lookAll(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/showroom/view/edit?targetShow=1&id='+curRow.id);
    }

    /**
     * 展览作品管理
     * @param idx
     */
    function resource(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/fygoods/view/list?showRoomId=' + row.id);
    }

    /**
     * 推荐
     * @param idx
     */
    function recommend(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要推荐选中的项吗？", function(r){
            if (r){
                __updrecommend(row.id, 1);
            }
        })
    }

    /**
     * 取消推荐
     * @param idx
     */
    function recommendoff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要取消推荐选中的项吗？", function(r){
            if (r){
                __updrecommend(row.id, 0);
            }
        })
    }

    /**
     * 推荐提交
     */
    function __updrecommend(ids, toState) {
        $.messager.progress();
        var params = {ids: ids, toState: toState};
        $.post('${basePath}/admin/showroom/updCommend', params, function(data){
            $("#whgdg").datagrid('reload');
            if (!data.success || data.success != "1"){
                $.messager.alert("错误", data.errormsg||'操作失败', 'error');
            }
            $.messager.progress('close');
        }, 'json');
    }

    /**
     * 发布 [1,2,4]->6
     * @param idx
     */
    function publish(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要发布选中的项吗？", function(r){
            if (r){
                __updStateSend(row.id, "1,2,4", 6);
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
                __updStateSend(row.id, 6, 1);
            }
        })
    }

    /**
     * 发布和取消发布时修改状态
     * @param ids
     * @param formstates
     * @param tostate
     * @private
     */
    function __updStateSend(ids, formstates, tostate){
        $.ajax({
            url: getFullUrl("/admin/showroom/updstate"),
            data: {ids: ids, formstates: formstates, tostate: tostate},
            success: function (data) {
                if(data && data.success == '1'){
                    $('#whgdg').datagrid('reload');
                } else {
                    $.messager.alert('提示', '操作失败:'+data.errormsg+'！', 'error');
                }
            }
        });
    }

    /**
     * 删除
     * @param idx
     */
    function del(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        $.messager.confirm('提示', '您确定要删除此记录吗？', function(r){
            if (r){
                $.ajax({
                    type: "POST",
                    url: getFullUrl('/admin/showroom/del'),
                    data: {id : curRow.id},
                    success: function(Json){
                        if(Json && Json.success == '1'){
                            $('#whgdg').datagrid('reload');
                        } else {
                            $.messager.alert('提示', '操作失败:'+Json.errormsg+'！', 'error');
                        }
                    }
                });
            }
        });
    }

</script>
<!-- script END -->
</body>
</html>