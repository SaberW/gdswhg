<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">

    <title>微专业列表</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>
</head>
<body>
<!-- 表格 -->
<table id="whgdg" title="微专业列表" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb',queryParams:{cultid: WhgComm.getMgrCultsAndChild()[0].id}, url:'${basePath}/admin/tra/major/srchList4p'">
    <thead>
    <tr>
        <th data-options="field:'name'">名称</th>
        <th data-options="field:'etype', formatter:WhgComm.FMTArtType">类型</th>
        <th data-options="field:'image', formatter:WhgComm.FMTImg">背景图片</th>
        <th data-options="field:'crtdate', formatter:WhgComm.FMTDateTime">创建时间</th>
        <th data-options="field:'state', formatter:traStateFMT" >状态</th>
        <th data-options="field:'_opt', formatter:WhgComm.FMTOpt,fixed:true, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <shiro:hasPermission name="${resourceid}:add">
        <div class="whgd-gtb-btn">
            <a class="easyui-linkbutton" iconCls="icon-add" onclick="add()">添 加</a>
        </div>
    </shiro:hasPermission>

    <div class="whgdg-tb-srch">
        <select class="easyui-combobox" name="cultid" prompt="请选择文化馆" panelHeight="auto" limitToList="true" data-options="editable:false, width:180, valueField:'id', textField:'text', data:WhgComm.getMgrCults(), value:WhgComm.getMgrCults()[0].id"></select>
        <input class="easyui-textbox" style="width: 200px;" name="name" prompt='请输入标题' data-options="validType:'length[1,30]'"/>
        <select class="easyui-combobox" name="state" prompt='请选择状态',>
            <option value="">全部</option>
            <option value="0">未审核</option>
            <option value="2">已审核</option>
            <option value="3">已发布</option>
        </select>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display:none" >
    <shiro:hasPermission name="${resourceid}:view">      <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="doSee">查看</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:edit">      <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="0,1" method="doUpd">编辑</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:kecheng">   <a href="javascript:void(0)" class="easyui-linkbutton"  plain="true" method="course">课程管理</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:shizi">   <a href="javascript:void(0)" class="easyui-linkbutton"  plain="true" method="teacher">师资管理</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:ziyuan">  <a href="javascript:void(0)" class="easyui-linkbutton"  plain="true" method="_resource">资源管理</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkon">   <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="0,1" method="doCheck">审核</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:back">      <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="2"   method="doUnCheck">打回</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:publish">   <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="2"   method="doRelease">发布</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:publishoff"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="3"   method="doUnRelease">撤销发布</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:recommend"><a href="javascript:void(0)" class="easyui-linkbutton" validFun="_recommend" plain="true" method="recommend">推荐</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:recommendoff"><a href="javascript:void(0)" class="easyui-linkbutton" validFun="_recommendoff" plain="true" method="recommendoff">取消推荐</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:del">       <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="0,1" method="doDel">删除</a></shiro:hasPermission>
</div>
<!-- 操作按钮-END -->


<script>
    function _recommendoff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 3 && row.recommend == 1;
    }
    function _recommend(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 3 && row.recommend == 0;
    }

    /**
     * 添加
     * @param idx
     */
    function add(){
        WhgComm.editDialog('${basePath}/admin/tra/major/view/add');
    }

    /**
     * 编辑
     * @param idx
     */
    function doUpd(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/tra/major/view/add?id='+row.id);
    }

    /**
     * 查看
     * @param idx
     */
    function doSee(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/tra/major/view/add?targetShow=1&id='+row.id);
    }


    /** 删除微专业 */
    function doDel(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm('确认对话框', '确定要删除此微专业吗？', function(r){
            if (r){
                $.ajax({
                    type: "POST",
                    url: getFullUrl('/admin/tra/major/del'),
                    data: {id : row.id},
                    success: function(data){
                        if(data && data.success == '1'){
                            $.messager.alert('提示', '操作成功！', 'info');
                            $('#whgdg').datagrid('reload');
                        } else {
                            $.messager.alert('提示', '操作失败:'+data.errmsg+'！', 'error');
                        }
                    }
                });
            }
        });
    }/** 删除微专业-END */


    /** 修改微专业状态 */
    function _updState(fromState, toState, id){
        var ids = '';
        if(id){//单个操作
            ids = id;
        }else{//批量操作
            rows = $("#zxDG").datagrid("getChecked");
            if(rows.length < 1){
                $.messager.alert('警告', '请选择要操作的数据！', 'warning');return;
            }
            var _spt = '';
            for(var i=0; i<rows.length; i++){
                ids += _spt+rows[i].id;
                _spt = ',';
            }
        }
        $.ajax({
            type: "POST",
            url: getFullUrl('/admin/tra/major/updstate'),
            data: {ids : ids, formstates: fromState, tostate: toState},
            success: function(Json){
                if(Json && Json.success == '1'){
                    $.messager.alert('提示', '操作成功！', 'info');
                    $('#whgdg').datagrid('reload');
                } else {
                    $.messager.alert('提示', '操作失败:'+Json.errmsg+'！', 'error');
                }
            }
        });
    }/** 修改微专业状态-END */

    /** 审批微专业 */
    function doCheck(index){
        var row = $("#whgdg").datagrid("getRows")[index];
        _updState(1, 2, row.id);
    }/** 审批微专业-END */

    /** 取消审批微专业 */
    function doUnCheck(index){
        var row = $("#whgdg").datagrid("getRows")[index];
        _updState(2, 1, row.id);
    }/** 取消审批微专业-END */

    /** 发布微专业 */
    function doRelease(index){
        var row = $("#whgdg").datagrid("getRows")[index];
        _updState(2, 3, row.id);
    }/** 发布微专业-END */

    /** 取消发布微专业 */
    function doUnRelease(index){
        var row = $("#whgdg").datagrid("getRows")[index];
        _updState(3, 2, row.id);
    }/** 取消发布微专业-END */


    /**
     * 课程
     * @param idx
     */
    function course(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/major/tra/view/list?mid='+row.id);
    }

    /**
     * 师资
     * @param idx
     */
    function teacher(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/major/tea/view/list?mid='+row.id);
    }

    /**
     * 资源
     * @param idx
     */
    function _resource(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/major/drsc/view/list?mid='+row.id);
    }

    /**
     * 推荐
     * @param idx
     */
    function recommend(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要推荐选中的项吗？", function(r){
            if (r){
                __updrecommend(row.id, 0, 1);
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
                __updrecommend(row.id, 1, 0);
            }
        })
    }
    /**
     * 推荐提交
     */
    function __updrecommend(ids, formrecoms, torecom) {
        $.messager.progress();
        var params = {ids: ids, formrecoms: formrecoms, torecom: torecom};
        $.post('${basePath}/admin/tra/major/updrecommend', params, function(data){
            $("#whgdg").datagrid('reload');
            if (!data.success || data.success != "1"){
                $.messager.alert("错误", data.errormsg||'操作失败', 'error');
            }
            $.messager.progress('close');
        }, 'json');
    }
</script>
</body>
</html>
