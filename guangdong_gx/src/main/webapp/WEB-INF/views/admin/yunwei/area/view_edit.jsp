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

    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-file.js"></script>
    <!-- 图片上传相关-END -->
    <script src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
</head>
<body style="overflow-x: hidden">

<div class="container-fluid">
    <form class="form-horizontal" role="form" id="whgff" style="margin-top: 20px;" method="post">
        <input type="hidden" id="id" name="id" value="${whgYwiArea.id}" />

        <div class="form-group">
            <label class="col-sm-2 control-label" for="name"><i style="color:red;">*</i>名称：</label>
            <div class="col-sm-10">
                <input class="easyui-textbox" style="width: 90%; height: 34px;" id="name" name="name" value="${whgYwiArea.name}" data-options="prompt:'请输入资源分类名称',required:true, validType:['length[0,32]']" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label" for="name"><i style="color:red;">*</i>编码：</label>
            <div class="col-sm-10">
                <input class="easyui-textbox" style="width: 90%; height: 34px;" id="code" name="code" value="${whgYwiArea.code}" data-options="prompt:'请输入地址编码，可站点访问前缀，小写字母及数字组合',required:true, validType:['length[3,10]','xyz']" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label" for="idx"><i style="color:red;">*</i>排序：</label>
            <div class="col-sm-10">
                <input class="easyui-numberspinner" style="width: 90%; height: 34px;" id="idx" name="idx" value="${whgYwiArea.idx}" data-options="prompt:'请输入资源分类的排序',required:true, min:1, max:999" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label" for="memo">备注：</label>
            <div class="col-sm-10">
                <textarea id="textarea_memo" style="display: none;">${whgYwiArea.memo}</textarea>
                <input class="easyui-textbox" style="width: 90%; height: 100px;" id="memo" name="memo" data-options="prompt:'请输入资源分类备注信息', multiline:true, validType:['length[0,250]']" />
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
            url: "${basePath}/admin/yunwei/area/edit",
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
        //设置“备注”的值
        $('#memo').textbox('setValue', $('#textarea_memo').val());

        //初始表单
        initForm();
    });//window.onload END
</script>
<!-- script END -->
</body>
</html>