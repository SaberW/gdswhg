<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>直播管理-线下培训列表</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <c:choose>
        <c:when test="${reftype == '1'}"><c:set var="reftitle" value="活动"></c:set> </c:when>
        <c:when test="${reftype == '2'}"><c:set var="reftitle" value="培训"></c:set> </c:when>
        <c:otherwise><c:set var="reftitle" value="在线课程"></c:set></c:otherwise>
    </c:choose>
</head>
<body>

<!-- 表格 -->
<table id="whgdg" title="${pageTitle}" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:true, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb'">
    <thead>
    <tr>
        <th data-options="field:'ck',checkbox:true"></th>
        <th data-options="field:'title',width:300">培训标题</th>
        <th data-options="field:'starttime',width:130,formatter:WhgComm.FMTDateTime ">开始时间</th>
        <th data-options="field:'endtime',width:130,formatter:WhgComm.FMTDateTime ">结束时间</th>
        <th data-options="field:'etype',width:120, formatter:WhgComm.FMTTrainType">培训类型</th>
        <th data-options="field:'isterm', width:50, formatter:yesNoFMT">学期制</th>
        <th data-options="field:'ismultisite', width:120, formatter:typeFMT">场次类别</th>
        <th data-options="field:'crtdate', width:130, formatter:WhgComm.FMTDateTime">创建时间</th>
        <th data-options="field:'crtuser',width:120, formatter:WhgComm.FMTUserName ">编辑者</th>
        <th data-options="field:'checkor',width:120, formatter:WhgComm.FMTUserName ">审核者</th>
        <th data-options="field:'publisher',width:120, formatter:WhgComm.FMTUserName ">发布者</th>
        <th data-options="field:'statemdfdate', width:130, sortable:true, formatter:WhgComm.FMTDateTime ">操作时间</th>
        <th data-options="field:'state',width:50, formatter:WhgComm.FMTBizState" >状态</th>
    </tr>
    </thead>
</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <div class="whgd-gtb-btn"> </div>
    <div class="whgdg-tb-srch">
        <input class="easyui-textbox" style="width: 200px;" name="title" data-options="prompt:'请输入培训名称', validType:'length[1,32]'" />
        <input class="easyui-combobox" name="recommend" prompt="是否推荐" style="width:110px" validType="notQuotes" data-options="editable:false,valueField:'id',textField:'text', data:[{id: '0',text: '未推荐'},{id: '1',text: '已推荐'}]"/>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->


<!-- script -->
<script type="text/javascript">
    /** 选中一条记录 */
    function clickSubmit(){
        var row = $('#whgdg').datagrid('getSelected');
        if(row && row.id){
            window.parent.$('#cultid').val(row.cultid);
            window.parent.$('#deptid').val(row.deptid);
            window.parent.$('#refid').val(row.id);
            window.parent.$('#refname').textbox('setValue', row.title);
            window.parent.$('#limitStartTime').val(WhgComm.FMTDateTime(row.starttime));
            window.parent.$('#limitEndTime').val(WhgComm.FMTDateTime(row.endtime));
            window.parent.$('#starttime').datetimebox('setValue', WhgComm.FMTDateTime(row.starttime));
            window.parent.$('#endtime').datetimebox('setValue', WhgComm.FMTDateTime(row.endtime));
            WhgComm.editDialogClose();
        }else{
            $.messager.alert('警告', '请选择一条记录再进行此操作！', 'warning');
        }
    }

    $(function(){
        //查询
        var cultid = WhgComm.getMgrCultsFirstId();
        WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/train/srchList4p?state=6&cultid='+cultid+'&type=publish');

        //注册确认事件
        WhgComm.getOpenDialogSubmitBtn().off('click').one('click', clickSubmit);
    });
</script>
<!-- script END -->
</body>
</html>