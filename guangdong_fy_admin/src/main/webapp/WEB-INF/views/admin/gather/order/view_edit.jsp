<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>查看预订</title>

    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>

</head>
<body class="body_add">
<form id="whgff" method="post" class="whgff">
    <h2>查看预订</h2>

    <div class="whgff-row">
        <div class="whgff-row-label">众筹名称：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" value="${info.gathertitle}" style="width:600px; height:32px"
                   data-options="readonly:true">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">众筹类型：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" value="${empty info.etype or info.etype eq '0'? '其它众筹' : (info.etype eq '4'? '活动众筹' : '培训众筹')}" style="width:600px; height:32px"
                   data-options="readonly:true">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">订单号：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" value="${info.orderid}" style="width:600px; height:32px"
                   data-options="readonly:true">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">预定人：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" value="${empty info.username? info.usernickname : info.username}" style="width:600px; height:32px"
                   data-options="readonly:true">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">联系人：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" value="${info.name}" style="width:600px; height:32px"
                   data-options="readonly:true">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">联系手机：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" value="${info.phone}" style="width:600px; height:32px"
                   data-options="readonly:true">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">生日：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" value="${info.birthday}" style="width:600px; height:32px"
                   data-options="readonly:true">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">下单时间：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" value="<fmt:formatDate value='${info.crtdate}' pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>" style="width:600px; height:32px"
                   data-options="readonly:true">
        </div>
    </div>

</form>

<div class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <a class="easyui-linkbutton whgff-but-clear">清 空</a>
</div>

<script>

    $(function(){
        var buts = $("div.whgff-but");
        //处理返回
        buts.find("a.whgff-but-clear").linkbutton({
            text: '返 回',
            iconCls: 'icon-undo',
            onClick: function(){
                window.parent.$('#whgdg').datagrid('reload');
                WhgComm.editDialogClose();
            }
        });
    });

</script>

</body>
</html>
