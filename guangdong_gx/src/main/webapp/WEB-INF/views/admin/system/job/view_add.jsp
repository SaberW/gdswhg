<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>岗位管理-添加岗位</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>
    <style>
        .red{
            color:red;
        }
        .ul-list>ul{
            margin: 10px 0 0 0;
            padding: 0;
            list-style: none;
            font-size: 14px;
        }
        .ul-list>ul>li{
            margin-top: 15px;
            margin-bottom: 0;
        }
        .ul-list>ul>li>label{
            font-weight: normal;
            margin-right: 15px;
        }
        .ul-list>ul>li>label:first-child{
            margin-top: 0;
        }
    </style>
</head>
<body>


<form id="whgff" class="whgff" method="post">
    <input type="hidden" name="id" value="${job.id}"/>
    <h2>岗位管理-添加岗位</h2>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>岗位名称：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="name" style="width:500px; height:32px" data-options="prompt:'请输入岗位名称，最多允许30个字符', required:true, validType:'length[3,30]'"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>权限组　：</div>
        <div class="whgff-row-input">
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="chioceAll();">全　　选</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="unChioceAll();">取消全选</a>
            <div class="ul-list"></div>
        </div>
    </div>

</form>

<div id="whgwin-add-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-save" id="whgwin-add-btn-save">保 存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返  回</a>
</div>

<!-- script -->
<script type="text/javascript">
    /** 岗位已有的权限组 */
    var job_pms = eval('(${job_pms})');

    /** 指定的权限组是否是岗位已有的权限组 */
    function _isChecked(pmsid){
        var isChecked = false;
        for(var i=0; i<job_pms.length; i++){
            if(job_pms[i].pmsid == pmsid){
                isChecked = true;
                break;
            }
        }
        return isChecked;
    }

    /**  标红 */
    function setRed(input){
        var curtChecked = input.is(':checked') ? 'checked' : '';
        if(curtChecked == input.attr('defaultCheck')){
            input.parent('label').removeClass("red")
        }else{
            input.parent('label').addClass("red");
        }
    }

    //全选
    function chioceAll() {
        $('.ul-list input[name="pms"]').each(function(){
            $(this).prop("checked", true);
            setRed($(this));
        });
        $('.ul-list input[name="pmsAll"]').prop('checked', true);
    }

    //取消全选
    function unChioceAll(){
        $('.ul-list input[name="pms"]').each(function(){
            $(this).prop("checked", false);
            setRed($(this));
        });
        $('.ul-list input[name="pmsAll"]').prop('checked', false);
    }

    /** 加载可用的权限组 */
    function initPms(){
        $.ajax({
            type: "POST",
            url: '${basePath}/admin/system/pms/srchList',
            data: {"sysflag":"${sysflag}", "state":"1", "delstate":"0"},
            cache:false,
            success: function(data){
                if($.isArray(data)){
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
                                html += '<li><label><input type="checkbox" name="pmsAll" value="1"/>全选</label>';
                            }
                            var pmsGroup = pmsArr[i];
                            var isChecked = _isChecked(pmsGroup.id) ? 'checked' : '';
                            html += '<label><input type="checkbox" name="pms" value="'+pmsGroup.id+'" defaultCheck="'+isChecked+'" '+isChecked+' />'+pmsGroup.name+'</label>';
                            if(i == pmsArr.length-1){
                                html += '</li>';
                            }
                        }
                    }
                    html += '</ul>';
                    $('.ul-list').html(html);
                    //注册事件
                    $('.ul-list input[name="pms"]').change(function(){
                        setRed($(this));
                    });
                    $('.ul-list input[name="pmsAll"]').change(function(){
                        var this_checked = $(this).is(":checked");
                        $(this).parents("li").find('input[name="pms"]').each(function () {
                            $(this).prop("checked", this_checked);
                            setRed($(this));
                        });
                    });
                }
            }
        });
    }

    /** 表单初始 */
    function initForm() {
        $('#whgff').form({
            novalidate: true,
            url : getFullUrl('/admin/system/job/add'),
            onSubmit : function(param) {
                var _valid = $(this).form('enableValidation').form('validate');
                if(_valid){
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
                    $.messager.alert('提示', '操作失败:'+Json.errormsg+'！', 'error');
                    $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
                }
            }
        });
        //注册提交事件
        $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
    }

    $(function () {
        //初始权限组
        initPms();

        //表单
        initForm();
    });
</script>
<!-- script END -->
</body>
</html>
