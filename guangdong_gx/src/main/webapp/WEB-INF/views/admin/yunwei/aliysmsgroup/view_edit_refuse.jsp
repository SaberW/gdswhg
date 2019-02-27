<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <title>设置业务的短信组</title>

    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>

    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
</head>
<body class="body_add">
<form id="whgff" method="post" class="whgff">
    <input type="hidden" name="entid" value="${entid}"/>
    <input type="hidden" name="enttype" value="${enttype}"/>
    <div class="whgff-row">
        <div class="whgff-row-label">选择短信组：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" name="groupid" id="groupid" value="${info.groupid}" prompt="请选择短信组"
                   data-options="width:350, panelHeight:120, valueField:'id', textField:'gpdesc', required:false,
                    url:'${basePath}/admin/yunwei/aliysmsgroup/refuse/srchlistgroup?enttype=${enttype}' "/>
        </div>
    </div>
</form>

<script>
    $(function () {
        ToolForm.init();
    });

    var ToolForm = {
        init: function(){
            var that = this;

            //保存事件
            that.frm = $("#whgff");
            that.frm.form("disableValidation");
            that.submetEdit();
        },

        submetEdit: function(){
            var me = this;

            function oneSubmit(){
                WhgComm.getOpenDialogSubmitBtn().off("click").one("click", function(){
                    $.messager.progress();
                    me.frm.submit();
                })
            }
            oneSubmit();

            me.frm.form({
                url: '${basePath}/admin/yunwei/aliysmsgroup/refuse/save',
                novalidate: true,
                onSubmit: function (param) {
                    $(this).form("enableValidation");
                    var isValid = $(this).form('validate');

                    if (isValid){
                        isValid = me.__validata(param);
                    }

                    if (!isValid){
                        $.messager.progress('close');
                        oneSubmit();
                    }
                    return isValid;
                },
                success: function(data){
                    $.messager.progress('close');
                    oneSubmit();
                    data = $.parseJSON(data);
                    if (data.success && data.success=="1"){
                        if (window.parent.whgListTool && window.parent.whgListTool.reload){
                            window.parent.whgListTool.reload();
                        }
                        WhgComm.editDialogClose();
                    }else{
                        $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                    }
                }
            })
        },

        __validata: function(param){
            return true;
        }
    }
</script>
</body>
</html>
