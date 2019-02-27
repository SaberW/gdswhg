<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>文化馆通知方式管理</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <style>
        .superUser {
            color: #f5f5f5;
            background-color: #FF5722;
            display: inline-block;
            margin-right: 5px;
        }

        .normalUser {
            color: #f5f5f5;
            background-color: #00BCD4;
            display: inline-block;
            margin-right: 5px;
        }
    </style>
</head>
<body class="easyui-layout">
<div data-options="region:'west',title:'文化馆',collapsible:false,split:true,tools:[{iconCls:'icon-reload', handler:reloadTree}]"
     style="width:250px;padding:10px;">
    <ul class="easyui-tree" id="mass_type_tree"></ul>
</div>
<div data-options="region:'center',title:'文化馆通知方式'">
    <div class="container-fluid" style="display: none">
        <form class="whgff" role="form" id="whgff" style="margin-top: 20px;" method="post">
            <input type="hidden" id="cultid" name="cultid" value=""/>

            <div class="whgff-row">
                <div class="whgff-row-label">短信通知号码：</div>
                <div class="whgff-row-input">
                    <input class="easyui-textbox" id="noticephone" name="noticephone" value="" style="width:400px; height:32px" data-options="prompt:'请输入短信通知号码', required:false, validType:'isPhone',disabled:true">
                    <shiro:hasPermission name="${resourceid}:edit">
                    <a href="#" class="easyui-linkbutton edit_phone" iconCls="icon-reload">修 改</a>
                    <a href="#" class="easyui-linkbutton save_phone" iconCls="icon-save" style="display: none">保 存</a>
                    </shiro:hasPermission>
                </div>
            </div>

            <div class="whgff-row">
                <div class="whgff-row-label">邮件通知地址：</div>
                <div class="whgff-row-input">
                    <input class="easyui-textbox" id="noticeemail" name="noticeemail" value="" style="width:400px; height:32px"
                           data-options="required:false,validType:['length[1,30]', 'isEmail'], prompt:'请输入邮箱',disabled:true">
                    <shiro:hasPermission name="${resourceid}:edit">
                    <a href="#" class="easyui-linkbutton edit_email" iconCls="icon-reload">修 改</a>
                    <a href="#" class="easyui-linkbutton save_email" iconCls="icon-save" style="display: none">保 存</a>
                    </shiro:hasPermission>
                </div>
            </div>


            <div id="whgwin-add-btn" style="text-align: center; display: none">
                <div style="display: inline-block; margin: 0 auto">
                    <a href="#" class="easyui-linkbutton" iconCls="icon-cancel"
                       onclick="$('#whgwin-add').dialog('close')">关 闭</a>
                </div>
            </div>

        </form>
    </div>
</div>
<!-- script -->
<script type="text/javascript">
    //初始tree
    function initMassTypeTree() {
        WhgCommAreaTree.initAreaTree({
            eleId: 'mass_type_tree',
            loadCult: true,
            selectFirstCult: false,
            selectFirstNode: true,
            onSelect: function (node, areaInfo) {
                if (typeof (node.iscult) != 'undefined' && node.iscult == '1') {
                    $('#cultid').val(node.id);
                    $('#cultname').val(node.text);
                    $(".container-fluid").show();
                    //WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/system/user/srchList4p?admintype=bizmgr');
                    querynoticetype(node.id);
                }else{
                    $(".container-fluid").hide();
                }
            }
        });
    }

    function querynoticetype(cultid){
        $("#noticephone").textbox("setValue", '');
        $("#noticeemail").textbox("setValue", '');
        $.ajax({
            url: getFullUrl('/admin/yunwei/quota/querynoticetype'),
            data: {cultid: cultid},
            cache: false,
            success: function (data) {
                if (data && data.success == '1') {
                    if(data.data!=null){
                        if(data.data.noticephone)
                            $("#noticephone").textbox("setValue", data.data.noticephone);
                        if(data.data.noticeemail)
                            $("#noticeemail").textbox("setValue", data.data.noticeemail);
                    }
                }
            }
        });
    }

    //刷新Tree
    function reloadTree() {
        $('#addBtn').linkbutton('disable');
        $('#queryBtn').linkbutton('disable');
        initMassTypeTree();
    }

    $(function () {
        initMassTypeTree();

        $(".edit_phone").on("click",function () {
             $("#noticephone").textbox({disabled: false})
             $(".edit_phone").hide();
             $(".save_phone").show();
        })
        $(".save_phone").on("click",function () {
            edit_phone();
        })


        $(".edit_email").on("click",function () {
            $("#noticeemail").textbox({disabled: false})
            $(this).hide();
            $(".save_email").show();
        })
        $(".save_email").on("click",function () {
            edit_email();
        })
    });

    function edit_phone(){
        var noticephone = $("#noticephone").val();
        var cultid = $("#cultid").val();

        var textbox = $("#noticephone");
        var valid = textbox.textbox('isValid');
        if (!valid) {
            $.messager.progress('close');
            //$.messager.alert("错误", "请输入正确的联系方式", 'error');
            //textbox.message ="请输入正确的联系方式, 如：13688888888";
            return false;
        }
        $.ajax({
            url: getFullUrl('/admin/yunwei/quota/edit_noticetype'),
            data: {cultid: cultid, noticephone: noticephone},
            cache: false,
            beforeSend:function(){
                $.messager.progress();
            },
            success: function (data) {
                $.messager.progress('close');
                if (data && data.success == '1') {
                    $(".edit_phone").show();
                    $(".save_phone").hide();
                    $("#noticephone").textbox({disabled: true})
                } else {
                    $.messager.alert('提示', '操作失败:' + data.errormsg + '！', 'error');
                }
            }
        });
    }

    function edit_email(){
        var cultid = $("#cultid").val();
        var textbox = $("#noticeemail");
        var noticeemail = textbox.val();
        var valid = textbox.textbox('isValid');
        if (!valid) {
            $.messager.progress('close');
            //$.messager.alert("错误", "请输入正确的联系方式", 'error');
            //textbox.message ="请输入正确的联系方式, 如：13688888888";
            return false;
        }
        $.ajax({
            url: getFullUrl('/admin/yunwei/quota/edit_noticetype'),
            data: {cultid: cultid, noticeemail: noticeemail},
            cache: false,
            beforeSend:function(){
                $.messager.progress();
            },
            success: function (data) {
                $.messager.progress('close');
                if (data && data.success == '1') {
                    $("#noticeemail").textbox({disabled: true})
                    $(".edit_email").show();
                    $(".save_email").hide();
                } else {
                    $.messager.alert('提示', '操作失败:' + data.errormsg + '！', 'error');
                }
            }
        });
    }
</script>
<!-- script END -->
</body>
</html>