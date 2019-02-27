<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>
        <c:choose>
            <c:when test="${param.onlyshow == '1'}">
                <h2>管理员管理-查看管理员</h2>
            </c:when>
            <c:otherwise>
                <h2>管理员管理-编辑管理员</h2>
            </c:otherwise>
        </c:choose>
    </title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>

    <script type="text/javascript" src="${basePath }/static/common/js/jQuery.md5.js"></script>
    <script type="text/javascript" src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
    <script type="text/javascript">
        $.extend($.fn.validatebox.defaults.rules, {
            isAccount: {
                validator: function (value, param) {
                    if(!/^\w{3,30}$/.test(value)){
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
            }
        });
    </script>
</head>
<body>


<form id="whgff" class="whgff" method="post">
    <input type="hidden" name="id" value="${adminuser.id}" >

    <c:choose>
        <c:when test="${param.onlyshow == '1'}">
            <h2>管理员管理-查看管理员</h2>
        </c:when>
        <c:otherwise>
            <h2>管理员管理-编辑管理员</h2>
        </c:otherwise>
    </c:choose>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>文化馆：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width: 500px; height:32px" name="cultid" id="cultid" data-options="required:true, prompt:'请选择文化馆', limitToList:true, valueField:'id', textField:'text', data:WhgComm.getMgrCultsAndChild(), onChange:changeCult"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>账号：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="account" id="account" value="${adminuser.account}" style="width:500px; height:32px" data-options="prompt:'管理员账号长度限制3-30字符，只允许英字母数字下划线组合,首字符必须是字母',required:true, validType:['length[3,30]','isAccount']"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>密码：</div>
        <div class="whgff-row-input"><input class="easyui-passwordbox" id="password1" name="password1" value="${adminuser.passwordMemo}" style="width:500px; height:32px" data-options="prompt:'请输入管理员密码',required:true,validType:['length[2,20]']"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>确认密码：</div>
        <div class="whgff-row-input"><input class="easyui-passwordbox" id="password2" name="password2" value="${adminuser.passwordMemo}" style="width:500px; height:32px" data-options="prompt:'请输入管理员确认密码',required:true,validType:['length[2,20]','myPassword']"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i></i>权限配置：</div>
        <div class="whgff-row-input" id="pmsCultDiv"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i></i>联系人：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="contact" id="contact" value="${adminuser.contact}" style="width:500px; height:32px" data-options="prompt:'请输入管理员姓名',required:false, validType:['length[2,20]']"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i></i>联系方式：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="contactnum" id="contactnum" value="${adminuser.contactnum}" style="width:500px; height:32px" data-options="prompt:'请输入管理员手机号码', required:false, validType:'isPhone'"></div>
    </div>

</form>

<div id="whgwin-add-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <c:if test="${param.onlyshow != '1'}" >
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-save" id="whgwin-add-btn-save">保 存</a>
    </c:if>
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返  回</a>
</div>

<!-- script -->
<script type="text/javascript">
    /**
     * 修改所属文化馆
     **/
    function changeCult(newVal, oldVal) {
        var isPmsCult = false;//所属文化馆是否权限分馆
        for(var i=0; i<managerCults.length; i++){
            if(managerCults[i]["id"] == newVal){
                isPmsCult = true;
            }
        }

        //能够赋权的分馆
        var cults = [];
        for(var i=0; i<managerCultsAndChild.length; i++){
            if(managerCultsAndChild[i]["id"] == newVal){
                cults.push(managerCultsAndChild[i]);
            }
            if(isPmsCult){
                if(managerCultsAndChild[i]["pid"] == newVal){
                    cults.push(managerCultsAndChild[i]);
                }
            }
        }

        //生成权限配置元素
        var html = '';
        for(var i=0; i<cults.length; i++){
            html += '<div style="margin-bottom: '+(i==cults.length-1?0:10)+'px;">' +
                '<input class="pmsinput" id="cultid_'+cults[i].id+'" name="pms_cultid" style="width: 160px; height:32px"> '+
                '<input class="pmsinput" id="role_'+cults[i].id+'" name="pms_role_'+cults[i].id+'" style="width: 160px; height:32px"> '+
                '<input class="pmsinput" id="dept_'+cults[i].id+'" name="pms_dept_'+cults[i].id+'" style="width: 160px; height:32px"> '+
                '</div>';
        }
        $('#pmsCultDiv').html(html);
        for(var i=0; i<cults.length; i++){
            $('#cultid_'+cults[i].id).combobox({prompt:'文化馆', limitToList:true, valueField:'id', textField:'text', data:[cults[i]], onChange:changeCultPms});
            $('#role_'+cults[i].id).combobox({prompt:'角色', limitToList:true, valueField:'id', textField:'name', data:[]});
            $('#dept_'+cults[i].id).combobox({prompt:'部门', editable:false, valueField:'id', textField:'name', data:[], multiple:true});
        }
    }

    /**
     * 选择权限分馆后确定分馆下的角色和部门
     **/
    function changeCultPms(newVal, oldVal) {
        var cultid = $(this).combobox('getData')[0].id;
        if(newVal && /\d+/.test(newVal)){
            $('#role_'+cultid).combobox({required:true});
            $('#dept_'+cultid).combobox({required:true});
            $('#role_'+cultid).combobox('reload', '${basePath}/admin/system/role/srchList?state=1&cultid='+newVal);
            $('#dept_'+cultid).combobox('reload', '${basePath}/admin/system/dept/srchList?state=1&cultid='+newVal);
        }else{
            $('#role_'+cultid).combobox({required:false});
            $('#dept_'+cultid).combobox({required:false});
            $('#role_'+cultid).combobox('loadData', []);
            $('#dept_'+cultid).combobox('loadData', []);
        }
    }

    /** 对部门数据过滤处理 */
    function myFilter(data){
        var rows = data.rows;
        var newRows = [];
        var lastNodeList = [];
        if(rows.length > 0){
            for(var i=0; i<rows.length; i++){
                var _id = rows[i].id;
                var _text = rows[i].name;
                var _node = {"id":_id, "text":_text};
                if(rows[i].pid){
                    while(lastNodeList.length > 0 && rows[i].pid != lastNodeList[lastNodeList.length-1]["id"]){
                        lastNodeList.splice(lastNodeList.length-1, 1);
                    }
                    if(lastNodeList.length > 0){
                        if(typeof lastNodeList[lastNodeList.length-1]["children"] == "undefined"){
                            lastNodeList[lastNodeList.length-1]["children"] = [];
                        }
                        lastNodeList[lastNodeList.length-1]["children"].push(_node);
                        lastNodeList.push(_node);
                    }else{
                        newRows.push(_node);
                        lastNodeList = [];
                        lastNodeList.push(_node);
                    }
                }else{
                    newRows.push(_node);
                    lastNodeList = [];
                    lastNodeList.push(_node);
                }
            }
        }
        return newRows;
    }

    $(function () {
        /** 初始表单 */
        $('#whgff').form({
            novalidate: true,
            url : getFullUrl('/admin/system/user/edit'),
            onSubmit : function(param) {
                var _valid = $(this).form('enableValidation').form('validate');
                if(_valid){
                    var pwd = $("#password1").passwordbox('getValue');
                    param.password = $.md5(pwd);
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
                    if('${param.onlyshow}' != '1'){
                        window.parent.$('#whgdg').datagrid('reload');
                    }
                    WhgComm.editDialogClose();
                } else {
                    $.messager.alert('提示', '操作失败:'+Json.errormsg+'！', 'error');
                    $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
                }
            }
        });

        //注册提交事件
        $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });

        //初始时设置值
        $('#cultid').combobox('setValue', '${adminuser.cultid}');
        var mgr_pms = ${adminuser_pms};
        var pms_cultids = mgr_pms.pms_cult;
        if($.isArray(pms_cultids) && pms_cultids.length > 0){
            for(var i=0; i<pms_cultids.length; i++){
                $('#cultid_'+pms_cultids[i]).combobox('setValue', pms_cultids[i]);
                $('#role_'+pms_cultids[i]).combobox('setValue', mgr_pms['pms_role_'+pms_cultids[i]]);
                $('#dept_'+pms_cultids[i]).combobox('setValue', mgr_pms['pms_dept_'+pms_cultids[i]]);
            }
        }


        //只查看时不允许编辑
        if('${param.onlyshow}' == '1'){
            $('#cultid').combobox('readonly', true);
            $('#account').textbox('readonly', true);
            //$('#password1').passwordbox('readonly', true);
            //$('#password2').passwordbox('readonly', true);
            $('#contact').textbox('readonly', true);
            $('#contactnum').textbox('readonly', true);
            window.setTimeout(function(){
                $('.pmsinput').combobox('readonly', true);
            }, 500);
        }
    });
</script>
<!-- script END -->
</body>
</html>