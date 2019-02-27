<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>群文资源库-添加</title>
    <!-- 编辑表单 -->
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap-theme.min.css"/>
    <style>
        .control-label i{color:red; margin: 0 5px;}
    </style>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <%--<script type="text/javascript" src="${basePath}/static/bootstrap/js/bootstrap.min.js"></script>--%>
    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-file.js"></script>
    <!-- 图片上传相关-END -->
    <script src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
    <script type="text/javascript" src="${basePath}/static/admin/js/mass-library-custom-form.js"></script>
    <script type="text/javascript" src="${basePath}/static/admin/js/mass-library-custom-field.js"></script>
    <script type="text/javascript" src="${basePath}/static/admin/js/mass-library-custom-comm.js"></script>
    <script type="text/javascript" src="${basePath}/static/admin/js/mass-resource.js"></script>
    <script type="text/javascript">
        $.extend($.fn.validatebox.defaults.rules, {
            isTableName: {
                validator: function(value, param){
                    //$.fn.validatebox.defaults.rules.isTableName.message = '只能是6-16个长度的英文小写字母.';
                    return /[a-z]{4,16}/.test(value);
                },
                message: '只能是4-16个长度的英文小写字母.'
            }
        });
    </script>
</head>
<body style="overflow-x: hidden">
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand">群文管理</a>
            <a class="navbar-brand"> &gt;&nbsp;群文资源管理</a>
            <a class="navbar-brand"> &gt;&nbsp;资源详情</a>
        </div>
    </div>
</nav>


<div class="container-fluid">
    <form class="form-horizontal" role="form" id="whgff" method="post">
        <input type="hidden" id="resid" name="resid" value="${res.id}"/>
        <input type="hidden" id="libid" name="libid" value="${library.id}"/>


        <fieldset>
            <legend>基础信息</legend>

            <!-- 图片资源-显示图片 -->
            <c:if test="${library.resourcetype == 'img'}">
                <div class="form-group">
                    <label class="col-sm-2 control-label"><i>*</i>封面图片：</label>
                    <div class="col-sm-10">
                        <div class="custom-field custom-field-imginput">
                            <div class="whgff-row-input-imgview" style="height: 200px; width: 300px; border: 2px dashed #ccc; border-radius: 10px; text-align: center; overflow: hidden;">
                                <c:choose>
                                    <c:when test="${res.reswidth > res.resheight}"><img src="${res.respicture}" style="width:100%;" /></c:when>
                                    <c:otherwise><img src="${res.respicture}" style="height:100%;" /></c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>

            <!-- 视频资源-需要上传图片做为封面 -->
            <c:if test="${library.resourcetype == 'video'}">
            <div class="form-group">
                <label class="col-sm-2 control-label"><i>*</i>封面图片：</label>
                <div class="col-sm-10">
                    <div class="custom-field custom-field-imginput">
                        <input type="hidden" id="respicture" name="respicture" value="">
                        <div class="whgff-row-input-imgview" id="preview_respicture" style="height: 200px; width: 300px; border: 2px dashed #ccc; border-radius: 10px; text-align: center; overflow: hidden;">
                            <img src="${res.respicture}" style="height:100%;" />
                        </div>
                        <div class="whgff-row-input-imgfile">
                            <i><button type="button" class="btn btn-default btn-sm" id="uploadBtn_respicture"><span class="glyphicon glyphicon-folder-open"></span>&nbsp;选择图片</button></i>
                        </div>
                    </div>
                </div>
            </div>
            </c:if>

            <div class="form-group">
                <label class="col-sm-2 control-label"><i>*</i>资源名称：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" style="width: 100%; height: 34px;" id="resname" name="resname" value="${res.resname}" data-options="prompt:'请输入资源名称，长度不能超过32个字符',required:true,validType:'length[1,32]'" />
                </div>
                <label class="col-sm-2 control-label"><i>*</i>资源作者：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" style="width: 100%; height: 34px;" id="resauthor" name="resauthor" value="${res.resauthor}" data-options="prompt:'请输入资源作者，长度不能超过32个字符',required:true,validType:'length[1,32]'" />
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">艺术分类：</label>
                <div class="col-sm-10">
                    <div style="margin-top: 7px;" id="div_resarttype"></div>
                </div>
            </div>

            <%--<div class="form-group">
                <label class="col-sm-2 control-label"><i>*</i>资源分类：</label>
                <div class="col-sm-10">
                    <input class="easyui-combotree" style="width: 100%; height: 34px;" id="restype" name="restype" data-options="prompt:'${restypeName}',url:'${basePath}/admin/mass/type/srchList4tree?pid=ROOT',required:false" />
                </div>
            </div>--%>

            <div class="form-group">
                <label class="col-sm-2 control-label">标签：</label>
                <div class="col-sm-10">
                    <div style="margin-top: 7px;" id="div_restag"></div>
                </div>
            </div>

            <c:if test="${library.resourcetype == 'img'}">
            <div class="form-group">
                <label class="col-sm-2 control-label">宽：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" style="width: 100%; height: 34px;" id="reswidth" value="${res.reswidth}" data-options="readonly:true,validType:'length[1,32]'" />
                </div>
                <label class="col-sm-2 control-label">高：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" style="width: 100%; height: 34px;" id="resheight" value="${res.resheight}" data-options="readonly:true,validType:'length[1,32]'" />
                </div>
            </div>
            </c:if>

            <div class="form-group">
                <label class="col-sm-2 control-label"><i>*</i>资源来源：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" style="width: 100%; height: 34px;" id="resorigin" name="resorigin" value="${res.resorigin}" data-options="prompt:'请输入资源来源,如：新浪网',required:true,validType:'length[1,32]'" />
                </div>
                <label class="col-sm-2 control-label">资源大小：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" style="width: 100%; height: 34px;" id="ressize" data-options="readonly:true,required:false,validType:'length[1,32]'" />
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">资源简介：</label>
                <div class="col-sm-10">
                    <script id="resintroduce" name="resintroduce" type="text/plain" style="width: 100%; height: 300px;">${res.resintroduce}</script>
                </div>
            </div>

            <div class="form-group" style="display: none" id="showReason">
                <label class="col-sm-2 control-label">下架原因：</label>
                <div class="col-sm-10">
                    <input class="easyui-textbox" id="reason" readonly="true" value="" multiline="true" style="width:350px;height: 150px;">
                </div>
            </div>
        </fieldset>

        <fieldset id="extFieldDiv">
            <legend>扩展信息</legend>
        </fieldset>

        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${param.onlyshow == 1}"></c:when>
                    <c:otherwise><button type="button" class="btn btn-primary" id="doSubmit"><span class="glyphicon glyphicon-ok"></span>&nbsp;保  存</button></c:otherwise>
                </c:choose>
                <button type="button" class="btn btn-default" onclick="WhgComm.editDialogClose();">&nbsp;返  回</button>
            </div>
        </div>
    </form>
</div>


<!-- script -->
<script type="text/javascript">
    /** 验证是否选择艺术分类和标签 */
    function validArtAndTag(){
        var valid_arttype = false;
        $("[name='resarttype'][checked]").each(function () {
            valid_arttype = true;
        });
        if(!valid_arttype){
            $.messager.alert('提示', '请选择艺术分类或者在运维管理中维护好艺术分类!', 'warning');
        }

        var valid_tag = false;
        $("[name='restag'][checked]").each(function () {
            valid_tag = true;
        });
        if(!valid_tag){
            $.messager.alert('提示', '请选择标签或者在运维管理中维护好标签!', 'warning');
        }

        return valid_arttype && valid_tag;
    }

    //初始表单提交
    function initForm(){
        //表单初始
        $('#whgff').form({
            novalidate: true,
            url: "${basePath}/admin/mass/libres/edit",
            onSubmit : function(param) {
                var _valid = $(this).form('enableValidation').form('validate');
                if (!_valid){
                    $.messager.progress('close');
                    $('#doSubmit').off('click').one('click', function (){$('#whgff').submit();});
                }else{
                    //自定义字段
                    var formData = WhgCustomForm.getFormData( $('#extFieldDiv') );
                    param.adds = formData.add;
                    param.edits = formData.edit;
                    param.dels = formData.del;
                    param.keeps = formData.keep;
                }
                if(_valid){//验证
                    //_valid = validArtAndTag();
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
                    $.messager.alert('提示', '操作失败:'+Json.errormsg+'!', 'error');
                    $('#doSubmit').off('click').one('click', function (){$('#whgff').submit();});
                }
            }
        });
        //注册提交事件
        $('#doSubmit').off('click').one('click', function (){$('#whgff').submit();});
    }//初始表单提交 END

    /** 资源大小转换 */
    function initResSize(size) {
        var _txt = '';
        var number = new Number(size);
        if(number / 1024 < 1024){
            _txt = (number / 1024).toFixed(2)+" KB";
        }else if(number / 1024 / 1024 < 1024){
            _txt =  (number / 1024 / 1024).toFixed(2)+" MB";
        }else{
            _txt = (number / 1024 / 1024 / 1024).toFixed(2)+" GB";
        }
        $('#ressize').textbox('setValue', _txt);
    }

    /** 扩展字段 */
    function initCustomForm(){
        $.getJSON('${basePath}/admin/mass/library/findfields?id=${library.id}&resid=${res.id}', function(data){
            var forms = data.forms;
            var fields = data.fields;

            if($.isArray(fields) && $.isArray(forms)){
                //创建表单行
                var curt_rows = -1;//当前行
                var curt_columns = [];//当前行的列
                for(var i=0; i<forms.length; i++){
                    var t_column = forms[i];
                    var t_rows = t_column.rows;
                    var isNewRow = curt_rows != t_rows;
                    var isLast = i == forms.length-1;
                    var isFirst = i == 0;
                    if(!isFirst && isNewRow){
                        WhgCustomForm.createRow('extFieldDiv', curt_columns, true);
                    }
                    if(isNewRow){
                        curt_rows = t_rows;
                        curt_columns = [];
                    }
                    curt_columns.push(t_column);
                    if(isLast){
                        WhgCustomForm.createRow('extFieldDiv', curt_columns, true);
                    }
                }

                //创建字段
                for(var i=0; i<fields.length; i++){
                    var t_field = fields[i];
                    var t_formid = t_field.formid;
                    var t_fieldidx = t_field.fieldidx;
                    var inputEle_a = $('#inputEle_'+t_formid);
                    var inputEle_b = $('#inputEle_'+t_fieldidx+'_'+t_formid);
                    if(inputEle_a.size() == 1){
                        WhgCustomFormField.createField(t_field,false,inputEle_a);
                        inputEle_a.find('button.field-edit').attr('fid1', t_field.id);
                    }else if(inputEle_b.size() == 1){
                        WhgCustomFormField.createField(t_field,false,inputEle_b);
                        inputEle_b.find('button.field-edit').attr('fid1', t_field.id);
                    }
                }
            }
        });
    }

    //window.onload
    $(function () {
        var onlyshow = '${param.onlyshow}' == '1' ? true : false;

        //视频文件需要上传图片
        if(!onlyshow && 'video' == '${library.resourcetype}'){
            WhgUploadImg.init({
                basePath: '${basePath}',
                uploadBtnId: 'uploadBtn_respicture',
                hiddenFieldId: 'respicture',
                previewImgId: 'preview_respicture',
                needCut:false,
                isSingleSy:false
            });
        }

        //初始艺术分类
        WhgCustomComm.createArtTypeHtml('div_resarttype', '${library.cultid}', '${res.resarttype}');

        //初始标签
        WhgCustomComm.createTagHtml('div_restag', '${library.cultid}', '${res.restag}');

        //初始富文本
        window.ue_resintroduce = UE.getEditor('resintroduce', {readonly:onlyshow});

        //资源大小
        initResSize('${res.ressize}');

        //初始表单
        initForm();

        //初始自定义表表单字段
        initCustomForm();

        //查看不允许编辑
        if(onlyshow){
            $('.easyui-textbox').textbox("readonly", true);
            $('.easyui-combobox').combobox("readonly", true);
            window.setTimeout(function () {
                $('input').attr("disabled","disabled");
            }, 500);
        }

        //查看下架原因
        showXjReason('${res.id}');
    });//window.onload END
</script>
<!-- script END -->
</body>
</html>