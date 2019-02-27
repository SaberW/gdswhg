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
            }
        });
    </script>
</head>
<body>


<form id="whgff" class="whgff" method="post">
    <h2>管理员管理-添加管理员</h2>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>文化馆：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width: 500px; height:32px" name="cultid" id="cultid" data-options="required:true, prompt:'请选择文化馆', limitToList:true, valueField:'id', textField:'text', data:WhgComm.getMgrCultsAndChild(), onChange:changeCult"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>账号：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="account" style="width:500px; height:32px" data-options="prompt:'管理员账号长度限制3-30字符，只允许英字母数字下划线组合,首字符必须是字母',required:true, validType:['length[3,30]','isAccount']"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>密码：</div>
        <div class="whgff-row-input"><input class="easyui-passwordbox" id="password1" name="password1" style="width:500px; height:32px" data-options="prompt:'请输入管理员密码',required:true,validType:['length[2,20]']"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>确认密码：</div>
        <div class="whgff-row-input"><input class="easyui-passwordbox" id="password2" name="password2" style="width:500px; height:32px" data-options="prompt:'请输入管理员确认密码',required:true,validType:['length[2,20]','myPassword']"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i></i>权限配置：</div>
        <div class="whgff-row-input" id="pmsCultDiv"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i></i>联系人：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="contact" style="width:500px; height:32px" data-options="prompt:'请输入管理员姓名',required:false, validType:['length[2,20]']"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i></i>联系方式：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="contactnum" style="width:500px; height:32px" data-options="prompt:'请输入管理员手机号码', required:false, validType:'isPhone'"></div>
    </div>

</form>

<div id="whgwin-add-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-save" id="whgwin-add-btn-save">保 存</a>
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
                    '<input id="cultid_'+cults[i].id+'" name="pms_cultid" style="width: 160px; height:32px"> '+
                    '<input id="role_'+cults[i].id+'" name="pms_role_'+cults[i].id+'" style="width: 160px; height:32px"> '+
                    '<input id="dept_'+cults[i].id+'" name="pms_dept_'+cults[i].id+'" style="width: 160px; height:32px"> '+
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

    /** 获角色数据 */
    function getRoleData(){
        var _data = [];
        $.ajax({
            url: '${basePath}/admin/system/role/srchList',
            cache: false,
            async: false,
            type: 'POST',
            data: {state: 1, delstate:0},
            success: function (data) {
                for(var i=0; i<data.length; i++){
                    _data.push( {"id":data[i].id, "text":data[i].name} );
                }
            }
        });
        return _data;
    }

    /** 获取权限分馆数据 */
    function getCultData() {
        var _data = [];
        $.ajax({
            url: '${basePath}/admin/system/cult/srchList',
            cache: false,
            async: false,
            type: 'POST',
            data: {state: 1, delstate:0},
            success: function (data) {
                _data.push( {"id":"TOP", "text":"总馆"} );
                for(var i=0; i<data.length; i++){
                    _data.push( {"id":data[i].id, "text":data[i].name} );
                }
            }
        });
        return _data;
    }

    $(function () {
        /** 初始表单 */
        $('#whgff').form({
            novalidate: true,
            url : getFullUrl('/admin/system/user/add'),
            onSubmit : function(param) {
                var _valid = $(this).form('enableValidation').form('validate');
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
    });
</script>
<!-- script END -->
</body>
</html>
