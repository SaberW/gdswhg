<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>
    <meta charset="UTF-8">
    <title>请假管理</title>
</head>
<body>

<!--签到-->
<table id="whgdg" title="请假管理" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb'">
    <thead>
    <tr>
        <th data-options="field:'tratitle'">培训名称</th>
        <th data-options="field:'starttime', width:130, formatter:WhgComm.FMTDateTime">课程开始时间</th>
        <th data-options="field:'endtime', width:130, formatter:WhgComm.FMTDateTime">课程结束时间</th>
        <th data-options="field:'realname', width:120">报名人</th>
        <th data-options="field:'contactphone', width:130">联系电话</th>
        <th data-options="field:'crttime', width:130, formatter:WhgComm.FMTDateTime">报名时间</th>
        <th data-options="field:'state', width:60, formatter:FMTvstate">请假状态</th>
        <th data-options="field:'_opt', width:160, formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <div class="whgdg-tb-srch">
        <input class="easyui-combobox" name="cultid" id="cultid" data-options="editable:false,width:150" prompt="请选择文化馆"/>
        <input class="easyui-combobox" name="deptid" id="deptid" data-options="editable:false,width:150" prompt="请选择部门"/>
        <input class="easyui-combobox" name="traid" prompt="请选择培训" data-options="editable:false,width:200,valueField:'traid',textField:'tratitle',url:'${basePath}/admin/train/course/srchTra4Leave'"/>
        <input class="easyui-textbox" style="width: 200px;" name="realname" data-options="prompt:'请输入报名人', validType:'length[1,32]'" />
        <input class="easyui-combobox" name="state" prompt="状态" style="width:110px" data-options="value:'0',editable:false,valueField:'id',textField:'text', data:[{id: '0',text: '申请中'},{id: '1',text: '已请假'},{id: '2',text: '未通过'}]"/>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->
<!-- 操作按钮 -->
<div id="whgdg-opt" >
    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="view">查看</a>
    <shiro:hasPermission name="${resourceid}:leave"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validleave" method="leave">请假审核</a></shiro:hasPermission>
</div>
<!-- 操作按钮-END -->


<script>
    $(function(){
        WhgComm.initPMS({
            cultEid: 'cultid', cultValue: '', allcult: true,
            deptEid: 'deptid', deptValue: '', alldept: true
        });
        WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/train/course/srchLeaveList?biz=PT&islive=0');
    });

    function validleave(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 0;
    }

    function FMTsign(v){

        if(v == 1){
            return "已签到";
        }else{
            return "未签到";
        }
    }
    function FMTvstate(v){

        if(v == 1){
            return "已请假";
        } if(v == 0){
            return "请假申请中";
        }
        if(v == 2){
            return "请假未通过";
        }else{
            return "未请假";
        }
    }

    /**
     * 请假
     */
    function leave(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/train/enrol/leaveview?id='+row.courseid+'&userid='+row.userid);
    }


    /**
     * 请假
     */
    function view(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/train/enrol/leaveview?id='+row.courseid+'&userid='+row.userid+'&canEdit=false');
    }

</script>
</body>
</html>
