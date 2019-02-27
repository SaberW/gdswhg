<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>直播管理-课时列表</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <c:choose>
        <c:when test="${reftype == '1'}"><c:set var="reftitle" value="活动"></c:set> </c:when>
        <c:when test="${reftype == '2'}"><c:set var="reftitle" value="培训"></c:set> </c:when>
        <c:otherwise><c:set var="reftitle" value="在线课程"></c:set></c:otherwise>
    </c:choose>
</head>
<body>

<!-- 表格 -->
<table id="whgdg" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:true, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb', url:'${basePath}/admin/train/course/srchList4p?id=${param.traid}'">
    <thead>
    <tr>
        <th data-options="field:'ck',checkbox:true"></th>
        <th data-options="field:'title', width:200">课程名称</th>
        <th data-options="field:'starttime', width:160, formatter:WhgComm.FMTDateTime">课程开始时间</th>
        <th data-options="field:'endtime', width:160, formatter:WhgComm.FMTDateTime">课程结束时间</th>
        <th data-options="field:'state', width:160, formatter:WhgComm.FMTState">状态</th>
    </tr>
    </thead>
</table>
<!-- 表格 END -->


<!-- script -->
<script type="text/javascript">
    /** 选中一条记录 */
    function clickSubmit(){
        var row = $('#whgdg').datagrid('getSelected');
        if(row && row.id){
            window.parent.$('#courseid').val(row.id);
            if(row.title && row.title.length > 1){
                window.parent.$('#coursename').textbox('setValue', row.title);
                window.parent.$('#name').textbox('setValue', row.title);
            }else{//课程没有标题时使用培训的标题
                var refname = window.parent.$('#refname').textbox('getValue');
                window.parent.$('#coursename').textbox('setValue', refname);
                window.parent.$('#name').textbox('setValue', refname);
            }

            window.parent.$('#starttime').datetimebox('setValue', WhgComm.FMTDateTime(row.starttime));
            window.parent.$('#endtime').datetimebox('setValue', WhgComm.FMTDateTime(row.endtime));
            window.parent.$('#starttime').datetimebox('readonly', true);
            window.parent.$('#endtime').datetimebox('readonly', true);
            WhgComm.editDialogClose();
        }else{
            $.messager.alert('警告', '请选择一条记录再进行此操作！', 'warning');
        }
    }

    $(function(){
        //注册确认事件
        WhgComm.getOpenDialogSubmitBtn().off('click').one('click', clickSubmit);
    });
</script>
<!-- script END -->
</body>
</html>