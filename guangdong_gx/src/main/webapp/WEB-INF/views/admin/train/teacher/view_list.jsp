<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">

    <title>培训师资列表</title>

    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>
</head>
<body>
<!-- 表格 -->
<table id="whgdg" title="培训师资列表" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb'">
    <thead>
    <tr>
        <th data-options="field:'name', width:250">老师名称</th>
        <th data-options="field:'teacherpic', width:130, formatter:WhgComm.FMTImg">图片</th>
        <th data-options="field:'area', width:100, formatter:WhgComm.FMTAreaType">区域</th>
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
        <input class="easyui-textbox" style="width: 200px;" name="name" data-options="prompt:'请输入老师名称', validType:'length[1,60]'"/>
        <input class="easyui-combobox" style="width:110px" name="state" id="state" data-options="prompt:'请选择状态',editable:false,valueField:'id',textField:'text'"/>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display:none" >
    <shiro:hasPermission name="${resourceid}:view"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" method="view">查看</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" validFun="validEdit" method="edit">编辑</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:ziyuan"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validResource" method="resource">资源管理</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkgo"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" validFun="validCheckGo" method="checkgo">提交审核</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkon"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" validFun="validCheckOn" method="checkon">审核通过</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkoff"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" validFun="validCheckOff" method="checkoff">审核打回</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:publish"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" validFun="validPublish" method="publish">发布</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:publishoff"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" validFun="validPublishOff" method="publishoff">撤销发布</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:recovery"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" validFun="validRecycle" method="doRecycle">回收</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:undel"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" validFun="validUndel" method="doUnRecycle">撤销回收</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:del"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" validFun="validDel" method="doDel">删除</a></shiro:hasPermission>
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
            cultEid:'cultid', cultValue:'',allcult: true,
            deptEid:'deptid', deptValue:'',alldept: true
        });

        //执行初始查询操作
        if('list_e' == pageType){
            WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/train/tea/srchList4p?delstate=0&crtuser=${sessionAdminUser.id}');
        }else if('list_c' == pageType){
            WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/train/tea/srchList4p?delstate=0&defaultState=9,2,4,6');
        }else if('list_p' == pageType){
            WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/train/tea/srchList4p?delstate=0&defaultState=9,2,4,6');
        }else{
            WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/train/tea/srchList4p?delstate=1');
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
     * 查看
     * @param idx
     */
    function view(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/train/tea/view/add?targetShow=1&id='+row.id);
    }

    /**
     * 添加
     */
    function add(){
        WhgComm.editDialog('${basePath}/admin/train/tea/view/add');
    }

    /**
     * 编辑
     * @param idx
     */
    function edit(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/train/tea/view/add?id='+row.id);
    }

    /**
     * 资源
     * @param idx
     */
    function resource(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        //WhgComm.editDialog('${basePath}/admin/resource/view/list?reftype=31&refid=' + row.id);
        WhgComm.editDialog('${basePath}/admin/mass/resmanage/view/list?refid=' + row.id+'&reftype=31');
    }


    /**
     * 删除
     * @param idx
     */
    function doDel(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", '此操作将会永久性删除数据，确定要删除选中的项吗？', function () {
            $.post('${basePath}/admin/train/tea/del', {id: row.id}, function(data){
                $("#whgdg").datagrid('reload');
                if (!data.success || data.success != "1"){
                    $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                }
            }, 'json');
        });
    }

    /**
     * 回收
     * @param idx
     */
    function doRecycle(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", '确定要回收选中的数据吗？',function () {
            $.post('${basePath}/admin/train/tea/recycle', {id: row.id}, function(data){
                $("#whgdg").datagrid('reload');
                if (!data.success || data.success != "1"){
                    $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                }
            }, 'json');
        });
    }

    /**
     * 撤销回收
     * @param idx
     */
    function doUnRecycle(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", '确定要撤销回收选中的数据吗？', function(){
            $.post('${basePath}/admin/train/tea/unrecycle', {id: row.id}, function(data){
                $("#whgdg").datagrid('reload');
                if (!data.success || data.success != "1"){
                    $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                }
            }, 'json');
        });
    }


    /**
     * 发布
     * @param idx
     */
    function publish(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "确定要发布选中的项吗？", function(){
            __updStateSend(row.id, '2,4', 6);
        });
    }

    /**
     * 取消发布
     * @param idx
     */
    function publishoff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "确定要取消发布选中的项吗？", function(r){
            __updStateSend(row.id, 6, 4);
        })
    }

    //提交审核
    function checkgo(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "确定要审核选中的项吗？", function(){
            __updStateSend(row.id, 1, 9);
        })
    }


    /**
     * 审通过
     * @param idx
     */
    function checkon(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "确定要审核选中的项吗？", function(){
            __updStateSend(row.id, 9, 2);
        });
    }

    /**
     * 打回
     * @param idx
     */
    function checkoff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "确定要打回选中的项吗？", function(){
            __updStateSend(row.id, 9, 1);
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
        $.post('${basePath}/admin/train/tea/updstate', params, function(data){
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
