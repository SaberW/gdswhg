<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>群文资源库-添加</title>
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap-theme.min.css"/>
    <script type="text/javascript" src="${basePath}/static/bootstrap/js/bootstrap.min.js"></script>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>

</head>
<body style="overflow-x: hidden">

<div class="container-fluid">
    <form class="form-horizontal" role="form" id="whgff" style="margin-top: 20px;" method="post">
        <input type="hidden" id="id" name="id" value="${quota.id}" />
        <div class="form-group">
            <label class="col-sm-3 control-label" for="size"><i style="color:red;">*</i>默认存储空间大小：</label>
            <div class="col-sm-8">
                <input class="easyui-numberspinner" maxlength="5" style="width: 90%; height: 34px;" id="size" name="size"
                       value="${quota.size}" data-options="prompt:'默认存储空间大小',required:true, min:1024, max:1000000" />GB
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label" for="warningpercent"><i style="color:red;">*</i>预警百分比：</label>
            <div class="col-sm-8">
                <input class="easyui-numberspinner" style="width: 90%; height: 34px;" id="warningpercent" name="warningpercent" value="${quota.warningpercent}" data-options="prompt:'请输入预警百分比',required:true, min:50, max:100" />%
            </div>
        </div>

        <div id="whgwin-add-btn" style="text-align: center; display: none">
            <div style="display: inline-block; margin: 0 auto">
                <a href="#" class="easyui-linkbutton" iconCls="icon-save" id="btn" >保 存</a>
                <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#whgwin-add').dialog('close')">关 闭</a>
            </div>
        </div>

    </form>
</div>


<!-- script -->
<script type="text/javascript">
    //初始表单提交
    function initForm(){
        //表单初始
        $('#whgff').form({
            novalidate: true,
            url: "${basePath}/admin/yunwei/quota/set_default",
            onSubmit : function(param) {
                var _valid = $(this).form('enableValidation').form('validate');
                if (!_valid){
                    $.messager.progress('close');
                    WhgComm.getOpenDialogSubmitBtn().off('click').one('click', function (){$('#whgff').submit();});
                }
                return _valid;
            },
            success : function(data) {
                $.messager.progress('close');
                var Json = eval('('+data+')');
                if(Json && Json.success == '1'){
                    window.parent.$('#whgdg').datagrid('reload');
                    WhgComm.editDialogClose();
                } else {
                    $.messager.alert('提示', '操作失败:'+Json.errormsg+'!', 'error');
                    WhgComm.getOpenDialogSubmitBtn().off('click').one('click', function (){$('#whgff').submit();});
                }
            }
        });
        //注册提交事件
        WhgComm.getOpenDialogSubmitBtn().off('click').one('click', function (){ $('#whgff').submit(); });
    }//初始表单提交 END


    //window.onload
    $(function () {
        //初始表单
        initForm();
    });//window.onload END
</script>
<!-- script END -->
</body>
</html>