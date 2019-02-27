<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <title>设置短信组短信模板</title>

    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>

    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>

    <style>
        .panel-body {padding: 0px;}
    </style>
</head>
<body class="body_add">
<form id="whgff" method="post" class="whgff">

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>选择切入点：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" name="actpoint" id="actpoint" prompt="请选择切入点"
                   data-options="width:300, panelHeight:'auto', valueField:'id', required:true,
                    url:'${basePath}/admin/yunwei/aliysmsgroup/getgptypepoints?gptype=${info.gptype}' "/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>选择短信模板：</div>
        <div class="whgff-row-input">
            <table id="smscodegrid"></table>
        </div>
    </div>

</form>

<div id="tb" style="display: none;">
    <input class="easyui-textbox" name="tpcode" prompt="请输入模板code" data-options="width:100"/>
    <input class="easyui-textbox" name="tpname" prompt="请输入模板名称" data-options="width:150"/>
    <a class="easyui-linkbutton" iconCls="icon-search" onclick="ToolForm.serchCodeGrid()">查 询</a>
</div>

<script>

    $(function () {
        ToolForm.init();
    });

    var ToolForm = {
        init: function(){
            var that = this;
            //短信组ID
            that.id = '${info.id}';

            //设置短信模板列表
            that.loadCodeGrid();

            //保存事件
            that.frm = $("#whgff");
            that.frm.form("disableValidation");
            that.submetEdit();
        },

        loadCodeGrid: function(){
            $("#smscodegrid").datagrid({
                url: '${basePath}/admin/yunwei/aliysmscode/srchList4p',
                width: 550,
                height: 370,
                striped:true,
                rownumbers:true,
                pagination:true,
                fitColumns:false,
                singleSelect:true,
                frozenColumns: [[
                    {field:'id',checkbox:true},
                    {field:'tpcode',title:'模板code',width:100}
                ]],
                columns:[[
                    {field:'tpname',title:'模板名称'},
                    {field:'tpdesc',title:'模板说明'},
                    {field:'tpcontent',title:'模板内容'}
                ]],
                toolbar:'#tb'
            })
        },

        serchCodeGrid: function(){
            var params = {};
            var qnames = $("#tb [name]").serializeArray();
            for(var i in qnames){
                var pp = qnames[i];
                if (pp.name && pp.value){
                    params[pp.name] = pp.value;
                }
            }
            $("#smscodegrid").datagrid('load', params);
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
                url: '${basePath}/admin/yunwei/aliysmsgroup/smsgcref/save',
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
                        window.parent.whgListTool.reload();
                        WhgComm.editDialogClose();
                    }else{
                        $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                    }
                }
            })
        },

        __validata: function(param){
            var actdesc = $("#actpoint").combobox("getText");
            var coderow = $("#smscodegrid").datagrid("getSelected");
            var groupid = this.id;
            if (!groupid){
                $.messager.alert("错误", '短信组标识获取失败', 'error');
                return false;
            }
            if (!coderow || !coderow.id){
                $.messager.alert("错误", '请选择短信模板', 'error');
                return false;
            }

            param.groupid = groupid;
            param.codeid = coderow.id;
            param.actdesc = actdesc;

            return true;
        }
    }
</script>
</body>
</html>
