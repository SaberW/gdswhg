<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>

    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>

    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>

</head>
<body>


<!-- 请假表单 -->

<form id="whgff" class="whgff" method="post">
    <h2>请假审核</h2>
    <div class="whgff-row">
        <div class="whgff-row-label" ><label style="color: red">*</label>培训名称：</div>
        <div class="whgff-row-input" >
            <input class="easyui-textbox" id="tratitle" value="${leave.tratitle}" style="width:425px; height:35px"/>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label" ><label style="color: red">*</label>上课时间：</div>
        <div class="whgff-row-input" >
            <input class="easyui-datetimebox" id="starttime" value="<fmt:formatDate value="${ leave.starttime}" pattern="yyyy-MM-dd HH:mm:ss"/>" style="width:200px; height:32px" />至
            <input class="easyui-datetimebox" id="endtime" value="<fmt:formatDate value='${leave.endtime}' pattern='yyyy-MM-dd HH:mm:ss'/>" style="width:200px; height:32px" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label" ><label style="color: red">*</label>申请人：</div>
        <div class="whgff-row-input" >
            <input class="easyui-textbox" id="proposer" value="${leave.proposer}" style="width:425px; height:35px"/>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label" ><label style="color: red">*</label>请假原因：</div>
        <div class="whgff-row-input" >
            <input class="easyui-textbox" id="cause" value="${leave.cause}" style="width:425px; height:80px" data-options="multiline:true"/>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label" ><label style="color: red">*</label>审核结论：</div>
        <div class="whgff-row-input" >
            <div class="radio radio-primary whg-js-data"  value="${leave.state}" name="state" js-data='[{"id":"1","text":"是"},{"id":"2","text":"否"}]'></div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label" ><label style="color: red">*</label>审核意见：</div>
        <div class="whgff-row-input" >
            <input class="easyui-textbox" id="suggest" value="${leave.suggest}" name="suggest" style="width:425px; height:80px" data-options="multiline:true"/>
        </div>
    </div>
</form>

<div class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <a class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok">提 交</a>
    <a class="easyui-linkbutton whgff-but-clear" iconCls="icon-no" onclick=" WhgComm.editDialogClose()">关 闭</a>
</div>

<!-- 添加表单 END -->

<script>

    $(function () {

        var buts = $("div.whgff-but");
        var frm = $("#whgff");
        var id = '${leave.id}';

        //定义表单提交
        var url = "${basePath}/admin/train/enrol/editLeave";
        frm.form({
            url: url,
            novalidate: true,
            onSubmit: function (param) {
                param.id = id;

                $(this).form("enableValidation");
                var isValid = $(this).form('validate');

                if (!isValid){
                    $.messager.progress('close');
                    buts.find("a.whgff-but-submit").off('click').one('click', function () {
                        frm.submit();
                    });
                }
                return isValid;
            },
            success : function (data) {
                data = $.parseJSON(data);
                $.messager.progress('close');
                buts.find("a.whgff-but-submit").off('click').one('click', function () {
                    frm.submit();
                });
                if (!data.success || data.success != "1"){
                    $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                    return;
                }

                WhgComm.editDialogClose();
                window.parent.$('#course').datagrid('reload');
            }
        });
        buts.find("a.whgff-but-submit").off('click').one('click', function () {
            frm.submit();
        });


    });
</script>
</body>
</html>
