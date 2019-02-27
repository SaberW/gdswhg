<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>管理员信息</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>
    <style>
        table tr td, table tr th {
            padding: 5px 10px;
        }
        .ul-list>ul{
            margin: 5px 0 0 0;
            padding: 0;
            list-style: none;
            font-size: 14px;
        }
        .ul-list>ul>li{
            margin-bottom: 10px;
        }
        .ul-list>ul>li>label{
            font-weight: normal;
            margin-right: 15px;
        }
    </style>

    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <!-- 图片上传相关-END -->

    <script type="text/javascript" src="${basePath }/static/common/js/jQuery.md5.js"></script>
    <script type="text/javascript" src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
</head>
<body>


<form id="whgff" class="whgff" method="post">
    <input type="hidden" name="id" value="${adminuser.id}" >
    <!-- 区域管理员或者站点管理员 -->
    <input type="hidden" name="admintype" value="${adminuser.admintype}" />
    <input type="hidden" name="adminlevel" value="${adminuser.adminlevel}" />
    <input type="hidden" name="isbizmgr" value="${adminuser.isbizmgr}" />
    <input type="hidden" name="cultid" id="cultid" value="${adminuser.cultid}" />

    <div class="whgff-row row-cultname" style="display: none;">
        <div class="whgff-row-label"><i></i>所属文化馆：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="cultname" value="${cultname}" style="width:500px; height:32px" data-options="readonly:true">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i></i>账　号　　：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="account" value="${adminuser.account}" style="width:500px; height:32px" data-options="prompt:'管理员账号长度限制3-30字符，只允许英字母数字下划线组合,首字符必须是字母',required:false, validType:['length[3,30]','isAccount']"></div>
    </div>

    <div class="whgff-row row-adminprovince" style="display: none;">
        <div class="whgff-row-label"><i></i>管理省　　：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" style="width: 500px; height:32px" name="adminprovince" id="adminprovince" data-options="readonly:true"/>
        </div>
    </div>

    <div class="whgff-row row-admincity" style="display: none;">
        <div class="whgff-row-label"><i></i>管理市　　：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" style="width: 500px; height:32px" name="admincity" id="admincity" data-options="readonly:true"/>
        </div>
    </div>

    <div class="whgff-row row-adminarea" style="display: none;">
        <div class="whgff-row-label"><i></i>管理区域　：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" style="width: 500px; height:32px" name="adminarea" id="adminarea" data-options="readonly:true"/>
        </div>
    </div>

    <div class="whgff-row row-adminjob" style="display: none;">
        <div class="whgff-row-label"><i></i>管理员岗位：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width: 500px; height:32px" name="adminjob" value="" id="adminjob" data-options=""/>
        </div>
    </div>

    <div class="whgff-row pmsList" style="display: none;">
        <div class="whgff-row-label"><i></i>权限组　　：</div>
        <div class="whgff-row-input">
            <div class="ul-list"></div>
        </div>
    </div>

    <div class="whgff-row row-admindept" style="display: none;">
        <div class="whgff-row-label"><i></i>管理部门　：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width: 500px; height:32px" name="admindept" id="admindept" data-options="panelHeight:200"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i></i>联系人　　：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="contact" value="${adminuser.contact}" style="width:500px; height:32px" data-options="prompt:'请输入管理员姓名',required:false, validType:['length[2,20]']"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i></i>手机号码　：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" id="contactnum" name="contactnum" value="${adminuser.contactnum}" style="width:500px; height:32px" data-options="prompt:'请输入正确的手机号码',required:false,validType:'isPhone'">
        </div>
    </div>
    <div class="whgff-row" id="div_telephone">
        <div class="whgff-row-label"><i></i>固定电话　：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="telephone" value="${adminuser.telephone}" style="width:500px; height:32px" data-options="prompt:'请输入固定电话', required:false, validType:'isTel'"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i></i>身份证号码：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="idcard" value="${adminuser.idcard}" style="width:500px; height:32px" data-options="prompt:'请输入身份证号码', required:false, validType:'isIdcard'"></div>
    </div>
</form>

<!-- script -->
<script type="text/javascript">

    /** 根据添加的区域管理员类型显示不同的元素 */
    function initAdminType(admintype, adminlevel, isbizmgr, adminprovince, admincity, adminarea){
        if(admintype == 'sysmgr'){
            $('.row-adminprovince').show();
            $('#adminprovince').textbox('setValue', adminprovince);
            if(adminlevel == '2' || adminlevel == '3'){
                $('.row-admincity').show();
                $('#admincity').textbox('setValue', admincity);
            }
            if(adminlevel == '3'){
                $('.row-adminarea').show();
                $('#adminarea').textbox('setValue', adminarea);
            }
            $('.row-adminjob').show();
            $('#adminjob').combobox({url:'${basePath}/admin/system/job/srchList?sysflag=sysmgr&state=1&delstate=0',valueField:'id',textField:'name',required:false,editable:false});
            $('#adminjob').combobox('setValue', '${adminjob}');
        }else if(admintype == 'bizmgr'){
            if(isbizmgr != '1'){//站点管理员需要配置部门
                $('.row-admindept').show();
                initTree4Dept();
                $('.row-adminjob').show();
                $('#adminjob').combobox({url:'${basePath}/admin/system/job/srchList?sysflag=bizmgr&state=1&delstate=0&cultid=${adminuser.cultid}',valueField:'id',textField:'name',required:false,editable:false});
                $('#adminjob').combobox('setValue', '${adminjob}');
            }
            $('.row-cultname').show();
        }else if(admintype == 'masmgr'){
            $("#div_telephone").remove();
        }
    }

    /** 初始化部门树,可多选 */
    function initTree4Dept(){
        $('#admindept').combotree({
            url:'${basePath}/admin/system/dept/srchList4tree?cultid='+$('#cultid').val(),
            animate: true,
            lines: true,
            multiple: true,
            cascadeCheck: false,
            required:false,
            onLoadSuccess: function (node, data) {
                var t = $(this);
                if(data){
                    $(data).each(function(index, d){
                        var n = t.tree('find', this.id);
                        t.tree('select', n.target);
                        if(typeof(d.iscult) == 'undefined'){
                            console.log(n.target);
                            $(n.target).addClass('gray');
                        }
                        if(typeof(window.__initPage__) == "undefined" && typeof(d.iscult) != 'undefined'){
                            __initPage__ = true;
                        }
                        if(this.state == 'closed'){
                            t.tree('expandAll');
                        }
                    });
                }
            }
        });
        $('#admindept').combotree('setValues', '${admindept}'.split(','));
    }

    /** 查看时的处理 */
    function doOnlyShow() {
        if(false){
            window.setTimeout(function () {
                $('.edit-btn').linkbutton("disable");
                $('#validcode').textbox('destroy');
                $('#validCodeBtn').hide();
                $('.easyui-textbox[textboxname="account"]').textbox('readonly', true);
                //$('.easyui-passwordbox[textboxname="password1"]').textbox('readonly', true);
                //$('.easyui-passwordbox[textboxname="password2"]').textbox('readonly', true);
                $('.easyui-combobox[textboxname="adminjob"]').textbox('readonly', true);
                $('.easyui-combobox[textboxname="admindept"]').textbox('readonly', true);
                $('.easyui-textbox[textboxname="contact"]').textbox('readonly', true);
                $('.easyui-textbox[textboxname="contactnum"]').textbox('readonly', true);
                $('.easyui-textbox[textboxname="telephone"]').textbox('readonly', true);
                $('.easyui-textbox[textboxname="idcard"]').textbox('readonly', true);
            }, 500);
        }
    }

    $(function () {
        /** 根据管理员类型展示不同的界面 */
        initAdminType(
            '${adminuser.admintype}',
            '${adminuser.adminlevel}',
            '${adminuser.isbizmgr}',
            '${adminuser.adminprovince}',
            '${adminuser.admincity}',
            '${adminuser.adminarea}'
        );

        /** 查看时处理 */
        doOnlyShow();
    });
</script>
<!-- script END -->
</body>
</html>