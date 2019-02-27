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
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:true, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb'">
    <thead>
    <tr>
        <th data-options="field:'name', width:250">名称</th>
        <th data-options="field:'etype',width:50, formatter:etypeFMT">类型</th>
        <th data-options="field:'image',width:130, formatter:WhgComm.FMTImg">背景图片</th>
        <th data-options="field:'crtuser',width:120, formatter:WhgComm.FMTUserName">编辑者</th>
        <th data-options="field:'checkor',width:120, formatter:WhgComm.FMTUserName">审核者</th>
        <th data-options="field:'publisher',width:120, formatter:WhgComm.FMTUserName">发布者</th>
        <th data-options="field:'statemdfdate',width:130, formatter:WhgComm.FMTDateTime, sortable:true">操作时间</th>
        <th data-options="field:'state',width:50, formatter:WhgComm.FMTBizState" >状态</th>
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
       <input class="easyui-combobox" style="width: 150px;" name="cultid" id="cultid" prompt="请选择文化馆"/>
       <input class="easyui-combobox" style="width: 150px;" name="deptid" id="deptid" prompt="请选择部门"/>
       <input class="easyui-textbox" style="width: 200px;" name="name" id="name" data-options="prompt:'请输入微专业标题',validType:'length[1,60]'"/>
       <input class="easyui-combobox" style="width:110px" name="state" id="state" data-options="prompt:'请选择状态',editable:false,valueField:'id',textField:'text'"/>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display:none" >
    <shiro:hasPermission name="${resourceid}:view">        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="doSee">查看</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:edit">        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validEdit" method="doUpd">编辑</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:kecheng">     <a href="javascript:void(0)" class="easyui-linkbutton"  plain="true" validFun="validCourse" method="course">课程管理</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:shizi">       <a href="javascript:void(0)" class="easyui-linkbutton"  plain="true" validFun="validTeacher" method="teacher">师资管理</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:ziyuan">      <a href="javascript:void(0)" class="easyui-linkbutton"  plain="true" validFun="validResource" method="_resource">资源管理</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkgo">     <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validCheckGo" method="doCheckGo">提交审核</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkon">     <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validCheckOn" method="doCheck">审核通过</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkoff">    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validCheckOff" method="doUnCheck">审核打回</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:publish">     <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validPublish" method="doRelease">发布</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:publishoff">  <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validPublishOff" method="doUnRelease">撤销发布</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:recommend">   <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validRecommend" method="recommend">推荐</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:recommendoff"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validRecommendOff" method="recommendoff">撤销推荐</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:recovery">    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validRecycle" method="doRecycle">回收</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:undel">    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validUndel" method="doUndel">撤销回收</a></shiro:hasPermission>
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
            cultEid:'cultid', cultValue:'', allcult: true,
            deptEid:'deptid', deptValue:'', alldept: true
        });

        //执行初始查询操作
        if('list_e' == pageType){
            WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/tra/major/srchList4p?delstate=0&crtuser=${sessionAdminUser.id}');
        }else if('list_c' == pageType){
            WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/tra/major/srchList4p?delstate=0&defaultState=9,2,4,6');
        }else if('list_p' == pageType){
            WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/tra/major/srchList4p?delstate=0&defaultState=9,2,4,6');
        }else{
            WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/tra/major/srchList4p?delstate=1');
        }
    });

    //初始查询条件-业务状态
    function init_condition_state() {
        var stateArr = WhgComm.getBizState().slice();
        if('list_e' != pageType){
            stateArr.splice(0,1);
        }
        if('list_r' != pageType){
            $('#state').combobox('loadData', stateArr);
            if('list_e' == pageType){
                $('#state').combobox('setValue', 1);
            }else if('list_c' == pageType){
                $('#state').combobox('setValue', 9);
            }else if('list_p' == pageType){
                $('#state').combobox('setValue', 2);
            }
        }else{
            $('#state').combobox('destroy');
        }
    }

    //微专业类型,
    function etypeFMT(val) {
        switch(val){
            case "0":return "音乐";
            case "1":return "舞蹈";
            case "2":return "美术";
            case "3":return "戏剧";
            case "4":return "曲艺";
            case "5":return "书法";
            case "6":return "摄影";
            default:return val;
        }
    }

    //操作按钮显示控制
    function validEdit(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        if(pageType == 'list_e'){
            return row.state == 1;
        }else if(pageType == 'list_c'){
            return row.state == 9;
        }else if(pageType == 'list_p'){
            return row.state == 2 || row.state == 4 || row.state == 6;
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
        return pageType == 'list_e' && row.state == 1;
    }
    function validCheckOn(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return pageType == 'list_c' && row.state == 9;
    }
    function validCheckOff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return pageType == 'list_c' && row.state == 9;
    }
    function validPublish(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return pageType == 'list_p' && (row.state == 2 || row.state == 4);
    }
    function validPublishOff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return pageType == 'list_p' && row.state == 6;
    }
    function validRecommend(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return pageType == 'list_p' && row.state == 6 && row.recommend != 1;
    }
    function validRecommendOff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return pageType == 'list_p' && row.state == 6 && row.recommend == 1;
    }
    function validDel(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return (pageType == 'list_e' && row.state == 1) || (pageType == 'list_r' && row.delstate == 1);
    }
    function validRecycle(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return (pageType == 'list_c' && row.state == 9) || (pageType == 'list_p' && (row.state == 2 || row.state == 4));
    }
    function validUndel(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return pageType == 'list_r' && row.delstate == 1;
    }

    /**
     * 添加
     * @param idx
     */
    function add(){
        WhgComm.editDialog('${basePath}/admin/tra/major/view/add');
    }

    /**
     * 查看
     * @param idx
     */
    function doSee(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/tra/major/view/add?targetShow=1&id='+row.id);
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
        //WhgComm.editDialog('${basePath}/admin/resource/view/list?isbrand=3&refid=' + row.id);
        WhgComm.editDialog('${basePath}/admin/mass/resmanage/view/list?refid=' + row.id+'&reftype=34');
    }

    /** 删除微专业 */
    function doDel(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm('确认信息', '此操作将会永久性删除数据，确定要删除选中的项吗？', function(){
            if (true){
                $.ajax({
                    type: "POST",
                    url: getFullUrl('/admin/tra/major/del'),
                    data: {id : row.id},
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
    }/** 删除微专业-END */

    //回收微专业
    function doRecycle(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm('确认信息', '确定要回收此微专业吗？', function(){
            if (true){
                $.ajax({
                    type: "POST",
                    url: getFullUrl('/admin/tra/major/recycle'),
                    data: {id : row.id},
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
        WhgComm.confirm('确认信息', '确定要撤销回收此微专业吗？', function(){
            if (true){
                $.ajax({
                    type: "POST",
                    url: getFullUrl('/admin/tra/major/unrecycle'),
                    data: {id : row.id},
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
                    //$.messager.alert('提示', '操作成功！', 'info');
                    $('#whgdg').datagrid('reload');
                } else {
                    $.messager.alert('提示', '操作失败:'+Json.errmsg+'！', 'error');
                }
            }
        });
    }/** 修改微专业状态-END */

    //提交审核
    function doCheckGo(index){
        var row = $("#whgdg").datagrid("getRows")[index];
        WhgComm.confirm('确认信息', '您确认要提交审核吗？', function () {
            _updState(1, 9, row.id);
        });
    }

    /** 审批 */
    function doCheck(index){
        var row = $("#whgdg").datagrid("getRows")[index];
        WhgComm.confirm('确认信息', '您确认要审核通过吗', function () {
            _updState(9, 2, row.id);
        });
    }/** 审批-END */

    /** 取消审批微专业 */
    function doUnCheck(index){
        var row = $("#whgdg").datagrid("getRows")[index];
        WhgComm.confirm('确认信息', '您确认要审核打回吗？', function () {
            _updState(9, 1, row.id);
        });
    }/** 取消审批微专业-END */

    /** 发布微专业 */
    function doRelease(index){
        var row = $("#whgdg").datagrid("getRows")[index];
        WhgComm.confirm('确认信息', '您确认要发布吗？', function () {
            _updState('2,4', 6, row.id);
        });
    }/** 发布微专业-END */

    /** 取消发布微专业 */
    function doUnRelease(index){
        var row = $("#whgdg").datagrid("getRows")[index];
        WhgComm.confirm('确认信息', '您确认要撤销发布吗？', function () {
            _updState(6, 4, row.id);
        });
    }/** 取消发布微专业-END */

    /**
     * 推荐
     * @param idx
     */
    function recommend(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "确定要推荐选中的项吗？", function(){
            if (true){
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
        WhgComm.confirm("确认信息", "确定要取消推荐选中的项吗？", function(){
            if (true){
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
