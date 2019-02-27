<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>管理员管理-添加管理员</title>
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
    <script type="text/javascript">
        $.extend($.fn.validatebox.defaults.rules, {
            isAccount: {
                validator: function (value, param) {
                    if(!/^[a-zA-Z]\w{2,30}$/.test(value)){
                        $.fn.validatebox.defaults.rules.isAccount.message = "长度限制3-30字符，只允许英字母,数字,下划线,首字符必须是字母";
                        return false;
                    }
                    return true;
                },
                message: ''
            }, myPassword: {
                validator: function (value, param) {
                    var p1 = $('#password1').passwordbox('getValue');
                    if (value != p1) {
                        $.fn.validatebox.defaults.rules.myPassword.message = "两次密码输入不一致.";
                        return false;
                    }
                    return true;
                },
                message: ''
            }, passwordCheck: {
                validator: function(value, param){
                    var ls = 0;
                    if(value.length<8){
                        $.fn.validatebox.defaults.rules.passwordCheck.message ="密码强度弱，请输入不少于8位、字母数字组合密码";
                        return false;
                    }
                    if(value.match(/([a-z])+/)){
                        ls++;
                    }
                    if(value.match(/([0-9])+/)){
                        ls++;
                    }
                    if(value.match(/([A-Z])+/)){
                        ls++;
                    }
                    if(value.match(/[^a-zA-Z0-9]+/)){
                        ls++;
                    }
                    if(ls<2){
                        $.fn.validatebox.defaults.rules.passwordCheck.message ="密码强度弱，请输入不少于8位、字母数字组合密码";
                        return false;
                    }
                    return true;
                },
                message: ''
            },
        });
    </script>
</head>
<body>


<form id="whgff" class="whgff" method="post">
    <!-- 区域管理员或者站点管理员 -->
    <input type="hidden" name="admintype" value="${param.admintype}" />
    <input type="hidden" name="adminlevel" value="${param.adminlevel}" />
    <input type="hidden" name="isbizmgr" value="${param.isbizmgr}" />
    <input type="hidden" name="cultid" id="cultid" value="${param.cultid}" />

    <h2>账号管理-添加账号</h2>

    <div class="whgff-row row-cultname" style="display: none;">
        <div class="whgff-row-label"><i>*</i>所属文化馆：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="cultname" value="${param.cultname}" style="width:500px; height:32px" data-options="readonly:true">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>账　号　　：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="account" style="width:500px; height:32px" data-options="prompt:'管理员账号长度限制3-30字符，只允许英字母数字下划线组合,首字符必须是字母',required:true, validType:['length[3,30]','isAccount']"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>密　码　　：</div>
        <div class="whgff-row-input"><input class="easyui-passwordbox" id="password1" name="password1" style="width:500px; height:32px" data-options="prompt:'请输入管理员密码',required:true,validType:['passwordCheck']"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>确认密码　：</div>
        <div class="whgff-row-input"><input class="easyui-passwordbox" id="password2" name="password2" style="width:500px; height:32px" data-options="prompt:'请输入管理员确认密码',required:true,validType:['length[2,20]','myPassword']"></div>
    </div>

    <div class="whgff-row row-adminprovince" style="display: none;">
        <div class="whgff-row-label"><i>*</i>管理省　　：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" style="width: 500px; height:32px" name="adminprovince" id="adminprovince" data-options="readonly:true"/>
        </div>
    </div>

    <div class="whgff-row row-admincity" style="display: none;">
        <div class="whgff-row-label"><i>*</i>管理市　　：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" style="width: 500px; height:32px" name="admincity" id="admincity" data-options="readonly:true"/>
        </div>
    </div>

    <div class="whgff-row row-adminarea" style="display: none;">
        <div class="whgff-row-label"><i>*</i>管理区域　：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" style="width: 500px; height:32px" name="adminarea" id="adminarea" data-options="readonly:true"/>
        </div>
    </div>

    <div class="whgff-row row-adminjob" style="display: none;">
        <div class="whgff-row-label"><i>*</i>管理员岗位：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width: 500px; height:32px" name="adminjob" id="adminjob" data-options=""/>
        </div>
    </div>

    <div class="whgff-row pmsList" style="display: none;">
        <div class="whgff-row-label"><i>*</i>权限组　　：</div>
        <div class="whgff-row-input">
            <div class="ul-list"></div>
        </div>
    </div>

    <div class="whgff-row row-admindept" style="display: none;">
        <div class="whgff-row-label"><i>*</i>管理部门　：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width: 500px; height:32px" name="admindept" id="admindept" data-options=""/>
            <div><i></i></div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>联系人　　：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="contact" style="width:500px; height:32px" data-options="prompt:'请输入管理员姓名',required:true, validType:['length[2,20]']"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>手机号码　：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" id="contactnum" name="contactnum" style="width:200px; height:32px" data-options="prompt:'请输入正确的手机号码',required:true,validType:'isPhone'">
            <input class="easyui-textbox" id="validcode" name="validcode" style="width:200px; height:32px" data-options="prompt:'请输入短信验证码',required:true,validType:'length[6,6]'">
            <a href="javascript:void(0)" class="easyui-linkbutton" id="validCodeBtn" data-options="disabled:true">获取验证码</a>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>固定电话　：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="telephone" style="width:500px; height:32px" data-options="prompt:'请输入固定电话', required:true, validType:'isTel'"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>身份证号码：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="idcard" style="width:500px; height:32px" data-options="prompt:'请输入身份证号码', required:true, validType:'isIdcard'"></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>身份证正面：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_idcardface" name="idcardface" >
            <div class="whgff-row-input-imgview" id="idcardface" style="height: 200px;"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgidcardface">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>身份证反面：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_idcardback" name="idcardback" >
            <div class="whgff-row-input-imgview" id="idcardback" style="height: 200px;"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgidcardback">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>

</form>

<div id="whgwin-add-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-save" id="whgwin-add-btn-save">保 存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返  回</a>
</div>

<!-- script -->
<script type="text/javascript">
    /** 发送手机验证码 */
    function clickSendCode(){
        $('#validCodeBtn').linkbutton('disable');
        window.__cnt_send_code = 60;
        window.__cntInterval = window.setInterval(function(){
            window.__cnt_send_code = window.__cnt_send_code -1;
            if(window.__cnt_send_code < 0){
                $('#validCodeBtn').linkbutton({text:'获取验证码'});
                var isvalid = $('#contactnum').textbox('isValid');
                if(isvalid){
                    $('#validCodeBtn').linkbutton('enable');
                }
                if(window.__cntInterval){
                    window.clearInterval(window.__cntInterval);
                }
            }else{
                $('#validCodeBtn').linkbutton({text:'获取验证码 '+(window.__cnt_send_code+1)+'‘'});
            }
        },1000);
    }

    /** 手机验证码 */
    function doPhoneValidCode(){
        $('#contactnum').textbox({novalidate:true,onChange:function(newVal, oldVal){
            $('#contactnum').textbox({novalidate:false});
            var isvalid = $('#contactnum').textbox('isValid');
            if(isvalid){
                $('#validCodeBtn').linkbutton('enable');
                $('#validCodeBtn').on('click', clickSendCode);
            }else{
                $('#validCodeBtn').linkbutton('disable');
            }
        }});
    }

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
            $('#adminjob').combobox({url:'${basePath}/admin/system/job/srchList?sysflag=sysmgr&state=1&delstate=0',valueField:'id',textField:'name',required:true,editable:false});
        }else if(admintype == 'bizmgr'){
            if(isbizmgr != '1'){//站点管理员需要配置部门
                $('.row-admindept').show();
                initTree4Dept();
                $('.row-adminjob').show();
                $('#adminjob').combobox({url:'${basePath}/admin/system/job/srchList?sysflag=bizmgr&state=1&delstate=0&cultid=${param.cultid}',valueField:'id',textField:'name',required:true,editable:false});
            }
            $('.row-cultname').show();
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
            required:true,
            onBeforeExpand: function(node){
                /*var t = $(this);
                var cultlevel = 1;
                var parentNode = t.tree('getParent', node.target);
                while(parentNode){
                    cultlevel ++;
                    parentNode = t.tree('getParent', parentNode.target);
                }
                t.tree('options').queryParams = {level:cultlevel, area:node.text};*/
            },
            onLoadSuccess: function (node, data) {
                var t = $(this);
                if(data){
                    $(data).each(function(index, d){
                        var n = t.tree('find', this.id);
                        t.tree('select', n.target);
                        if(typeof(d.iscult) == 'undefined'){
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
            },
            onBeforeSelect: function (node) {
                //return node && typeof(node.iscult) != 'undefined' && node.iscult == '1';
            }
        });
    }

    /** 初始表单 */
    function initForm(){
        $('#whgff').form({
            novalidate: true,
            url : getFullUrl('/admin/system/user/add'),
            onSubmit : function(param) {
                var _valid = $(this).form('enableValidation').form('validate');
                if(_valid){
                    var idcardface = $("#cult_idcardface").val();
                    var idcardback = $("#cult_idcardback").val();
                    if(idcardface == ''){
                        $.messager.alert('提示', '请上传账号身份证正面图！', 'error');
                        _valid = false;
                    }else if(idcardback == ''){
                        $.messager.alert('提示', '请上传账号身份证反面图！', 'error');
                        _valid = false;
                    }
                }
                if(_valid){
                    var pwd = $("#password1").passwordbox('getValue');
                    param.password = $.md5(pwd); //加密
                    $.messager.progress();
                }else{
                    //失败时再注册提交事件
                    $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
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
                    $.messager.alert('提示', '操作失败: '+Json.errormsg+'！', 'error');
                    $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
                }
            }
        });
        //注册提交事件
        $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
    }

    /** 站点超级管理员显示权限组 */
    function showPms(){
        var data = ${pmsList};
        if($.isArray(data) && data.length > 0){
            $('.pmsList').show();

            var jsonObj = {};//{"分组1":[], "分组2":[]}
            var html = '';
            for(var i=0; i<data.length; i++){
                var t_type = data[i].type;
                if(typeof(jsonObj[t_type]) == "undefined"){
                    jsonObj[t_type] = [];
                }
                jsonObj[t_type].push(data[i]);
            }
            html += '<ul>';
            for(var pmstype in jsonObj){
                var pmsArr = jsonObj[pmstype];
                for(var i=0; i<pmsArr.length; i++){
                    if(i == 0){
                        //html += '<li><label><input type="checkbox" name="pmsAll" value="1" checked readonly/>全选</label>';
                        html += '<li>';
                    }
                    var pmsGroup = pmsArr[i];
                    html += '<label><input type="checkbox" name="pms" value="'+pmsGroup.id+'" checked disabled />'+pmsGroup.name+'</label>';
                    if(i == pmsArr.length-1){
                        html += '</li>';
                    }
                }
            }
            html += '</ul>';
            $('.ul-list').html(html);

        }
    }

    $(function () {
        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgidcardface', hiddenFieldId: 'cult_idcardface', previewImgId: 'idcardface', needCut:false});
        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgidcardback', hiddenFieldId: 'cult_idcardback', previewImgId: 'idcardback', needCut:false});

        /** 根据管理员类型展示不同的界面 */
        initAdminType('${param.admintype}', '${param.adminlevel}', '${param.isbizmgr}', '${param.adminprovince}', '${param.admincity}', '${param.adminarea}');

        /** 初始表单 */
        initForm();

        /**显示超级站点管理员权限组*/
        showPms();

        /** 手机验证码 -暂时先不要验证码 */
        //doPhoneValidCode();
        $('#validcode').textbox({required:false});
        $('#validcode').textbox('destroy');
        $('#validCodeBtn').hide();
    });
</script>
<!-- script END -->
</body>
</html>
