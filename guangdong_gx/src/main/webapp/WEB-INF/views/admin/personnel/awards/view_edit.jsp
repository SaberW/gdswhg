<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<% String path = request.getContextPath();%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <c:choose>
        <c:when test="${not empty id and not empty targetShow}">
            <c:set var="pageTitle" value="查看获奖情况"></c:set>
        </c:when>
        <c:when test="${not empty id}">
            <c:set var="pageTitle" value="编辑获奖情况"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="添加获奖情况"></c:set>
        </c:otherwise>
    </c:choose>
    <title>${pageTitle}</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>
    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>

    <script type="text/javascript" src="${basePath }/static/common/js/whg.sys.base.data.js"></script>

</head>
<body>

<form id="whgff" class="whgff" method="post" >

    <h2>${pageTitle}</h2>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>奖项：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="title" value="${awards.title}"
                                            style="width:500px; height:32px"
                                            data-options="required:true, validType:'length[1,60]'"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>获奖日期：</div>
        <div class="whgff-row-input">
            <input class="easyui-datebox" name="issuedate" value="${awards.issuedate}" style="width:200px; height:32px" data-options="required:true">
        </div>
    </div>

</form>

<div class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <a class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok">提 交</a>
    <a class="easyui-linkbutton whgff-but-clear" iconCls="icon-no" onclick="WhgComm.editDialogClose();">返 回</a>
</div>

<!-- script -->
<script type="text/javascript">
    var entid = '${entid}';

    $(function () {

        var id = '${id}';
        var targetShow = '${targetShow}';
        var frm = $("#whgff");
        var buts = $("div.whgff-but");

        //查看时的处理
        if (targetShow){
            //取消表单验证
            frm.form("disableValidation");
            //组件设为只读
            $('.easyui-textbox').textbox('readonly');
            $('.easyui-combobox').combobox('readonly');
            $('.easyui-datebox').combobox('readonly');
            $('.easyui-numberspinner').numberspinner('readonly');
            $('.easyui-datetimebox').combobox('readonly');
            $("#getXYPointBtn").hide();
            $("#imgUploadBtn1").hide();
            //处理选项点击不生效
            frm.find("input[type='checkbox'], input[type='radio']").on('click', function(){return false});
            //不显示提交 button
            buts.find("a.whgff-but-submit").hide();
            return;
        }

        //定义表单提交
        var url = id ? "${basePath}/admin/per/awards/edit" : "${basePath}/admin/per/awards/add";
        frm.form({
            url: url,
            novalidate: true,
            onSubmit: function (param) {
                if (id){
                    param.id = id;
                }
                if (entid){
                    param.entid = entid;
                }
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
                window.parent.$('#whgdg').datagrid('reload');

            }
        });
        buts.find("a.whgff-but-submit").on('click', function () {
            frm.submit();
        });
    });

    </script>
<!-- script END -->
</body>
</html>
