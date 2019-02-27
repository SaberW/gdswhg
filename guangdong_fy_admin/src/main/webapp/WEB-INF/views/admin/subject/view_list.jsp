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
    <title>非遗专题管理</title>
    <script src="${basePath}/static/admin/js/whgmodule-gridopts.js"></script>
</head>
<body>
<!-- 表格 -->
<table id="whgdg" title="非遗专题管理" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb', url:'${basePath}/admin/subject/subjectList'">
    <thead>
    <tr>
        <th data-options="field:'name', width:60">名称</th>
        <th data-options="field:'cultid', width:80, formatter:WhgComm.FMTCult">所属单位名称</th>
        <th data-options="field:'picture', width:60 ,formatter:WhgComm.FMTImg">作品图片</th>
        <th data-options="field:'createdate', width:60 ,formatter:datetimeFMT">创建时间</th>
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
            <input class="easyui-textbox" name="name" style="width:200px" data-options="prompt:'请输专题名称', validType:'length[1,32]'"/>
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
    <shiro:hasPermission name="${resourceid }:del"><a href="javascript:void(0)" validKey="state" validVal="1,3,4"  method="del">删除</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:ziyuan"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="1" method="resource">风采管理</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:ziyuan"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="1" >主题活动</a></shiro:hasPermission>
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

    /**
     * 添加
     */
    function add() {
        WhgComm.editDialog('${basePath}/admin/subject/view/add');
    }

    /**
     * 编辑
     * @param idx 行下标
     */
    function edit(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/subject/view/edit?id='+curRow.id);
    }

    /**
     * 查看
     * @param idx 行下标
     */
    function lookAll(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/subject/view/edit?targetShow=1&id='+curRow.id);
    }

    /**
     * 风采管理
     * @param idx  reftype传入参数同枚举类EnumResourceType
     */
    function resource(idx) {
        var cultid = WhgComm.getMgrCultsFirstId();
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/resource/view/list?reftype=14&refid=' + row.id+'&cultid='+cultid);
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
            url: getFullUrl("/admin/subject/updstate"),
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
                    url: getFullUrl('/admin/subject/del'),
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