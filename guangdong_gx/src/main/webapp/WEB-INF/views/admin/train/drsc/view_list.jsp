<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>培训资源列表</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>
</head>
<body>
<!-- 表格 -->
<table id="whgdg" title="培训资源列表" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb'">
    <thead>
    <tr>
        <th data-options="field:'drsctitle', width:300">资源标题</th>
        <th data-options="field:'drscfrom', width:200">资源来源</th>
        <th data-options="field:'drscarttyp', width:100, formatter:WhgComm.FMTArtType">艺术类型</th>
        <th data-options="field:'drscpic', width:130, formatter:WhgComm.FMTImg">资源图片</th>

        <th data-options="field:'crtuser',width:120, formatter:WhgComm.FMTUserName">编辑者</th>
        <th data-options="field:'checkor',width:120, formatter:WhgComm.FMTUserName">审核者</th>
        <th data-options="field:'publisher',width:120, formatter:WhgComm.FMTUserName">发布者</th>
        <th data-options="field:'drscopttime',width:130, formatter:WhgComm.FMTDateTime, sortable:true">操作时间</th>
        <th data-options="field:'drscstate',width:50, formatter:WhgComm.FMTBizState" >状态</th>

        <th data-options="field:'_opt', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <shiro:hasPermission name="${resourceid}:add">
        <div class="whgd-gtb-btn"><a class="easyui-linkbutton" iconCls="icon-add" onclick="add()">添 加</a></div>
    </shiro:hasPermission>

    <div class="whgdg-tb-srch">
        <input class="easyui-combobox" style="width: 150px;" name="drscvenueid" id="drscvenueid" prompt="请选择文化馆" />
        <input class="easyui-combobox" style="width: 150px;" name="deptid" id="deptid" prompt="请选择部门" />
        <input class="easyui-textbox" style="width: 200px;" name="drsctitle" id="drsctitle" prompt='请输入资源标题' data-options="validType:'length[1,60]'"/>
        <input class="easyui-combobox" style="width:110px" name="drscstate" id="drscstate" data-options="prompt:'请选择状态',editable:false,valueField:'id',textField:'text'"/>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display:none" >
    <shiro:hasPermission name="${resourceid}:view">         <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="doSee">查看</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:edit">         <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validEdit" method="doUpd">编辑</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkgo">      <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validCheckGo" method="doCheckGo">提交审核</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkon">      <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validCheckOn" method="doCheck">审核通过</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkoff">     <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validCheckOff" method="doUnCheck">审核打回</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:publish">      <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validPublish" method="doRelease">发布</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:publishoff">   <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validPublishOff" method="doUnRelease">撤销发布</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:recommend">    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validRecommend" method="recommend">推荐</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:recommendoff"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validRecommendoff" method="recommendoff">撤销推荐</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:recovery">     <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validRecycle" method="doRecycle">回收</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:undel">        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validUndel" method="doUndel">撤销回收</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:del">          <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validDel" method="doDel">删除</a></shiro:hasPermission>
</div>
<!-- 操作按钮-END -->


<script>
    var pageType = '${type}';//编辑审核发布删除页面

    $(function(){
        //初始查询条件-业务状态
        init_condition_state();

        //初始查询条件-文化馆和部门
        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'drscvenueid', cultValue:'',allcult: true,
            deptEid:'deptid', deptValue:'',alldept: true
        });

        //执行初始查询操作
        if('list_e' == pageType){
            WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/drsc/srchPagging?delstate=0&crtuser=${sessionAdminUser.id}');
        }else if('list_c' == pageType){
            WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/drsc/srchPagging?delstate=0&defaultState=9,2,4,6');
        }else if('list_p' == pageType){
            WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/drsc/srchPagging?delstate=0&defaultState=9,2,4,6');
        }else{
            WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/drsc/srchPagging?delstate=1');
        }
    });

    //初始查询条件-业务状态
    function init_condition_state() {
        var stateArr = WhgComm.getBizState().slice();
        if('list_e' != pageType){
            stateArr.splice(0,1);
        }
        if('list_r' != pageType){
            $('#drscstate').combobox('loadData', stateArr);
            if('list_e' == pageType){
                $('#drscstate').combobox('setValue', 1);
            }else if('list_c' == pageType){
                $('#drscstate').combobox('setValue', 9);
            }else if('list_p' == pageType){
                $('#drscstate').combobox('setValue', 2);
            }
        }else{
            $('#drscstate').combobox('destroy');
        }
    }

    //操作按钮显示控制
    function validEdit(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        if(pageType == 'list_e'){
            return row.drscstate == 1;
        }else if(pageType == 'list_c'){
            return row.drscstate == 9;
        }else if(pageType == 'list_p'){
            return row.drscstate == 2 || row.drscstate == 4;
        }
    }
    function validCourse(idx){
        return validEdit(idx);
    }
    function validTeacher(idx){
        return validEdit(idx);
    }
    function validResource(idx){
        return validEdit(idx);
    }
    function validCheckGo(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return pageType == 'list_e' && row.drscstate == 1;
    }
    function validCheckOn(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return pageType == 'list_c' && row.drscstate == 9;
    }
    function validCheckOff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return pageType == 'list_c' && row.drscstate == 9;
    }
    function validPublish(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return pageType == 'list_p' && (row.drscstate == 2 || row.drscstate == 4);
    }
    function validPublishOff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return pageType == 'list_p' && row.drscstate == 6;
    }
    function validRecommend(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return pageType == 'list_p' && row.drscstate == 6 && row.isrecommend != 1;
    }
    function validRecommendoff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return pageType == 'list_p' && row.drscstate == 6 && row.isrecommend == 1;
    }
    function validDel(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return (pageType == 'list_e' && row.drscstate == 1) || (pageType == 'list_r' && row.delstate == 1);
    }
    function validRecycle(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return (pageType == 'list_c' && row.drscstate == 9) || (pageType == 'list_p' && (row.drscstate == 2 || row.drscstate == 4));
    }
    function validUndel(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return pageType == 'list_r' && row.delstate == 1;
    }

    /**
     * 查看
     * @param idx
     */
    function doSee(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/drsc/index/add?targetShow=1&id='+row.drscid);
    }

    /**
     * 添加
     * @param idx
     */
    function add(){
        WhgComm.editDialog('${basePath}/admin/drsc/index/add');
    }

    /**
     * 编辑
     * @param idx
     */
    function doUpd(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/drsc/index/add?id='+row.drscid);
    }

    /** 删除数字资源 */
    function doDel(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm('确认信息', '此操作将会永久性删除数据，确定要删除选中的项吗？', function(){
            if (true){
                $.ajax({
                    type: "POST",
                    url: getFullUrl('/admin/drsc/del'),
                    data: {id : row.drscid},
                    success: function(Json){
                        if(Json && Json.success == '0'){
                            //$.messager.alert('提示', '操作成功！', 'info');
                            $('#whgdg').datagrid('reload');
                        } else {
                            $.messager.alert('提示', '操作失败:'+Json.errmsg+'！', 'error');
                        }
                    }
                });
            }
        });
    }/** 删除数字资源-END */

    //回收微专业
    function doRecycle(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm('确认信息', '确定要回收此培训资源吗？', function(){
            if (true){
                $.ajax({
                    type: "POST",
                    url: getFullUrl('/admin/drsc/recycle'),
                    data: {id : row.drscid},
                    success: function(data){
                        if(data && data.success == '1'){
                            //$.messager.alert('提示', '操作成功！', 'info');
                            $('#whgdg').datagrid('reload');
                        } else {
                            $.messager.alert('提示', '操作失败:'+data.errmsg+'！', 'error');
                        }
                    }
                });
            }
        });
    }

    //撤销回收-改为编辑状态
    function doUndel(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm('确认信息', '确定要撤销回收此培训资源吗？', function(){
            if (true){
                $.ajax({
                    type: "POST",
                    url: getFullUrl('/admin/drsc/unrecycle'),
                    data: {id : row.drscid},
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
    }


    /** 修改数字资源状态 */
    function _updState(fromState, toState, drscid){
        var drscids = '';
        if(drscid){//单个操作
            drscids = drscid;
        }else{//批量操作
            rows = $("#zxDG").datagrid("getChecked");
            if(rows.length < 1){
                $.messager.alert('警告', '请选择要操作的数据！', 'warning');return;
            }
            var _spt = '';
            for(var i=0; i<rows.length; i++){
                drscids += _spt+rows[i].drscid;
                _spt = ',';
            }
        }
        $.ajax({
            type: "POST",
            url: getFullUrl('/admin/drsc/updState'),
            data: {ids : drscids, fromState: fromState, toState: toState},
            success: function(Json){
                if(Json && Json.success == '0'){
                    //$.messager.alert('提示', '操作成功！', 'info');
                    $('#whgdg').datagrid('reload');
                } else {
                    $.messager.alert('提示', '操作失败:'+Json.errmsg+'！', 'error');
                }
            }
        });
    }/** 修改数字资源状态-END */

    //提交审核
    function doCheckGo(index){
        var row = $("#whgdg").datagrid("getRows")[index];
        WhgComm.confirm('确认信息', '您确认要提交审核吗？', function () {
            _updState(1, 9, row.drscid);
        });
    }

    /** 审批数字资源 */
    function doCheck(index){
        var row = $("#whgdg").datagrid("getRows")[index];
        WhgComm.confirm('确认信息', '您确认要审核通过吗', function () {
            _updState(9, 2, row.drscid);
        });
    }/** 审批数字资源-END */

    /** 取消审批数字资源 */
    function doUnCheck(index){
        var row = $("#whgdg").datagrid("getRows")[index];
        WhgComm.confirm('确认信息', '您确认要审核打回吗？', function () {
            _updState(9, 1, row.drscid);
        });
    }/** 取消审批数字资源-END */

    /** 发布数字资源 */
    function doRelease(index){
        var row = $("#whgdg").datagrid("getRows")[index];
        WhgComm.confirm('确认信息', '您确认要发布吗？', function () {
            _updState('2,4', 6, row.drscid);
        });
    }/** 发布数字资源-END */

    /** 取消发布数字资源 */
    function doUnRelease(index){
        var row = $("#whgdg").datagrid("getRows")[index];
        WhgComm.confirm('确认信息', '您确认要撤销发布吗？', function () {
            _updState(6, 4, row.drscid);
        });
    }/** 取消发布数字资源-END */

    /**
     * 推荐
     * @param idx
     */
    function recommend(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "确定要推荐选中的项吗？", function(){
            if (true){
                __updrecommend(row.drscid, 0, 1);
            }
        })
    }
    /**
     * 取消推荐
     * @param idx
     */
    function recommendoff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "确定要取消推荐选中的项吗？", function(){
            if (true){
                __updrecommend(row.drscid, 1, 0);
            }
        })
    }
    /**
     * 推荐提交
     */
    function __updrecommend(ids, formrecoms, torecom) {
        $.messager.progress();
        var params = {ids: ids, formrecoms: formrecoms, torecom: torecom};
        $.post('${basePath}/admin/drsc/updrecommend', params, function(data){
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
