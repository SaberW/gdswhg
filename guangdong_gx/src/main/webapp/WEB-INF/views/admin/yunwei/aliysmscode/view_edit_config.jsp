<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <title>阿里云短信接口配置</title>

    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>

    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
</head>
<body class="body_add">
<form id="whgff" method="post" class="whgff">
    <h2>阿里云短信接口配置</h2>

    <div style="margin: 0px auto; width: 70%;">
        <p style="color: #ff0000;">请参考www.aliyun.com控制台中有效设置项进行配置</p>
    </div>

    <input type="hidden" name="cfgtype" value="${cfgtype}"/>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>AccessKeyID：</div>
        <div class="whgff-row-input">
            <input type="hidden" name="cfgkey" value="aliyunsmsAccessKeyId">
            <input type="hidden" name="cfgtext" value="AccessKeyID">
            <input class="easyui-textbox" name="cfgvalue" value="${info['aliyunsmsAccessKeyId']}" style="width:600px;" prompt="请输入内容，字数限制在100个字符内"
                   data-options="multiline:false, required:true, validType:['length[1,100]'] "/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>AccessKeySecret：</div>
        <div class="whgff-row-input">
            <input type="hidden" name="cfgkey" value="aliyunsmsAccessKeySecret">
            <input type="hidden" name="cfgtext" value="AccessKeySecret">
            <input class="easyui-textbox" name="cfgvalue" value="${info['aliyunsmsAccessKeySecret']}" style="width:600px;" prompt="请输入内容，字数限制在100个字符内"
                   data-options="multiline:false, required:true, validType:['length[1,100]'] "/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>签名名称：</div>
        <div class="whgff-row-input">
            <input type="hidden" name="cfgkey" value="aliyunsmsSignName">
            <input type="hidden" name="cfgtext" value="签名名称">
            <input class="easyui-textbox" name="cfgvalue" value="${info['aliyunsmsSignName']}" style="width:600px;" prompt="请输入内容，字数限制在100个字符内"
                   data-options="multiline:false, required:true, validType:['length[1,100]'] "/>
        </div>
    </div>
</form>

<div class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
<shiro:hasPermission name="${resourceid}:edit">
    <a id="saveButton" class="easyui-linkbutton" data-options="iconCls:'icon-ok', text:'保 存'"></a>
</shiro:hasPermission>
</div>

<script>

    $(function(){
        formTool.init();
    });


    var formTool = {
        basePath: '${basePath}',
        modelPath: '/admin/yunwei/aliysmscode/',
        pageType: 'saveconfig',

        /** 入口*/
        init: function(){
            this.frm = $("#whgff");
            this.edit();
        },

        /** 编辑*/
        edit : function () {
            var me = this;

            var okBut = $("#saveButton");

            me.__submitForm(okBut, me.basePath+me.modelPath+me.pageType);
        },

        /** 表单提交*/
        __submitForm: function (okBut, url, id) {
            var me = this;

            function oneSubmit(){
                okBut.off("click").one("click", function(){
                    $.messager.progress();
                    me.frm.submit();
                })
            }
            oneSubmit();

            me.frm.form({
                url: url,
                novalidate: true,
                onSubmit: function (param) {
                    $(this).form("enableValidation");
                    var isValid = $(this).form('validate');

                    if (!isValid){
                        $.messager.progress('close');
                        oneSubmit();
                    }

                    var cfgs = [];
                    $(this).find(".whgff-row").each(function (i) {
                        var iarr = $(this).find("input[name]").serializeArray();
                        var row = {};
                        for(var i in iarr){
                            var ent = iarr[i];
                            row[ent.name] = ent.value;
                        }
                        cfgs.push(row);
                    });
                    param.cfgJson = JSON.stringify(cfgs);

                    return isValid;
                },
                success: function(data){
                    $.messager.progress('close');
                    oneSubmit();
                    data = $.parseJSON(data);
                    if (data.success && data.success=="1"){
                        $.messager.alert("提示", '信息提交成功', 'info');
                    }else{
                        $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                    }
                }
            })
        }
    }
</script>
</body>
</html>
