<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<% String path = request.getContextPath();%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>添加专家人才</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>
    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
    <script src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>

    <script type="text/javascript" src="${basePath }/static/common/js/whg.sys.base.data.js"></script>

    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-moreimg.js"></script>
    <!-- 图片上传相关-END -->
</head>
<body>

<form id="whgff" class="whgff" method="post">
    <h2>添加专家人才</h2>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>姓名：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="name" style="width:500px; height:32px" data-options="required:true, validType:'length[1,30]'"></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>性别：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="sex" js-data='[{"id":"0","text":"女"},{"id":"1","text":"男"}]'></div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">民族：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="family" style="width:500px; height:32px" data-options="validType:'length[1,30]'"></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">身份证号：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="cardno" style="width:500px; height:32px" data-options=" validType:'length[1,30]'"></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">住址：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="address" style="width:500px; height:32px" data-options="validType:'length[1,30]'"></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">联系方式：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="phoneno" style="width:500px; height:32px" data-options="validType:'length[1,30]'"></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">文艺职务：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="job" style="width:500px; height:32px" data-options=" validType:'length[1,30]'"></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>人才类型：</div>
        <div class="whgff-row-input"><input class="easyui-combobox" name="type" style="height:32px;width: 500px"
                                            data-options="editable:false,required:true, valueField:'id',textField:'text',prompt:'请选择人才类型', data:WhgSysData.getTypeData('16')"/></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>文化馆：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width: 300px; height:32px" name="cultid" data-options="required:true, prompt:'请选择文化馆', value:WhgComm.getMgrCults()[0].id, valueField:'id', textField:'text', data:WhgComm.getMgrCults()"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>上传照片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture1" name="picture" >
            <div class="whgff-row-input-imgview" id="previewImg1"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">获奖情况：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="hjqk"  multiline="true" style="width:600px;height: 100px;"
                   data-options="validType:['length[1,400]']">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>人才简介：</div>
        <div class="whgff-row-input">
            <script id="catalog" type="text/plain" style="width:700px; height:250px;"></script>
        </div>
    </div>
</form>

<div id="whgwin-add-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <div style="display: inline-block; margin: 0 auto">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="whgwin-add-btn-save">保 存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
</div>
</div>

<!-- script -->
<script type="text/javascript">
    var ueConfig = {
        scaleEnabled: false,
        autoFloatEnabled: false,
        elementPathEnabled:false,
        readonly: '${targetShow}'? true: false
    };
var ue_catalog = UE.getEditor('catalog', ueConfig);

$(function () {
    WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1'});

    $('#whgff').form({
        novalidate: true,
        url: "${basePath}/admin/personnel/add",
        onSubmit : function(param) {
            var _valid = $(this).form('enableValidation').form('validate')
            if(_valid) {
                //图片必填
                if($('#cult_picture1').val() == ""){
                    _valid = false;
                    $.messager.alert('提示', '请选择人才照片');
                }else if(!isUEvalid) {
                    var isUEvalid = validateUE();
                    if (isUEvalid) {
                        param.summary = ue_catalog.getContent();
                        $.messager.progress();
                    } else {
                        _valid = false;
                    }
                }
            }
            if (!_valid){
                $.messager.progress('close');
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
                $.messager.alert('提示', '操作失败:'+Json.errormsg+'!', 'error');
                $('#whgwin-add-btn-save').off('click').one('click', submitFun);
            }
        }
    });
    //注册提交事件
    $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
});

    function validateUE(){
        if (!ue_catalog.hasContents()) {
            $.messager.alert("错误", '人才简介不能为空', 'error');
            return false;
        }
        return true;
    }

</script>
<!-- script END -->
</body>
</html>
