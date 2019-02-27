<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<% String path = request.getContextPath();%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>非遗创意空间</title>
	<%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
	<link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
	<link rel="stylesheet" href="${basePath}/static/admin/Font-Awesome/css/font-awesome.min.css"/>
	<link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>

	<script type="text/javascript" charset="utf-8"	src="<%=path%>/static/ueditor/ueditor.config.js"></script>
	<script type="text/javascript" charset="utf-8"	src="<%=path%>/static/ueditor/ueditor.all.min.js"></script>
	<script type="text/javascript" charset="utf-8"	src="<%=path%>/static/ueditor/lang/zh-cn/zh-cn.js"></script>

	<script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
	<script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
	<script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
	<script type="text/javascript" src="${basePath }/static/common/js/whg.sys.base.data.js"></script>
	<script type="text/javascript" src="${basePath}/static/common/js/area.js"></script>
</head>
<body>
	<form id="whgff" method="post" class="whgff">
		<h2>新增非遗衍生品</h2>

		<div class="whgff-row">
			<div class="whgff-row-label"><i>*</i>名称：</div>
			<div class="whgff-row-input">
				<input class="easyui-textbox" name="name"
					style="width: 500px; height: 32px" data-options="required:true,validType:['length[1,40]']" />
			</div>
		</div>

		<div class="whgff-row">
			<div class="whgff-row-label">
				<label style="color: red">*</label>上传封面：
			</div>
			<div class="whgff-row-input">
	            <input type="hidden" id="cult_picture1" name="picture" data-options="required:true">
	            <div class="whgff-row-input-imgview" id="previewImg1"></div>
	            <div class="whgff-row-input-imgfile" id="imgUrl_pic">
	                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
	                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
	            </div>
	        </div>
		</div>

		<div class="whgff-row">
			<div class="whgff-row-label">
				<label style="color: red">*</label>类型：
			</div>
			<div class="whgff-row-input">
				<div class="radio radio-primary whg-js-data" name="type" id="type"
					 js-data='[{"id":"1","text":"非遗产品"},{"id":"2","text":"非遗展演"},{"id":"2","text":"衍生品"}]'>
				</div>
			</div>
		</div>

		<%--<div class="whgff-row">
			<div class="whgff-row-label"><label style="color: red">*</label>非遗类型：</div>
			<div class="whgff-row-input">
				<div class="radio radio-primary whg-js-data" id="etype" name="etype" js-data="WhgSysData.getTypeData('2')"></div>
			</div>
		</div>--%>

		<div class="whgff-row">
			<div class="whgff-row-label"><label style="color: red">*</label>非遗类型：</div>
			<div class="whgff-row-input"><input class="easyui-combobox" name="etype" style="height:32px;width: 500px"
												data-options="editable:false,required:true, valueField:'id',textField:'text',prompt:'请选择非遗类型', data:WhgSysData.getTypeData('2')"/></div>
		</div>

		<div class="whgff-row">
			<div class="whgff-row-label"><i>*</i>制作人/单位：</div>
			<div class="whgff-row-input">
				<input class="easyui-textbox" name="producer"
					   style="width: 500px; height: 32px" data-options="required:true,validType:['length[1,50]']" />
			</div>
		</div>

		<div class="whgff-row">
			<div class="whgff-row-label">
				<label style="color: red">*</label>简介：
			</div>
			<div class="whgff-row-input">
				<script id="catalog"  type="text/plain" style="width: 600px; height: 300px;"></script>
			</div>
		</div>
	</form>

	<div class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
		<a class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok" id="whgwin-add-btn-save">提 交</a>
		<a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
	</div>
<script>
    var ueConfig = {
        scaleEnabled: false,
        autoFloatEnabled: false,
        elementPathEnabled: false
    };
    var ue_catalog = UE.getEditor('catalog', ueConfig);

    $(function () {
        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'',
            ywiTypeEid:'etype', ywiTypeValue:'', ywiTypeClass:2
        });

        /*图片初始化*/
        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1'});

        $('#whgff').form({
            novalidate: true,
            url: "${basePath}/admin/derivative/add",
            onSubmit : function(param) {
                var _valid = $(this).form('enableValidation').form('validate')
                if(_valid) {
                    //图片必填
                    if($('#cult_picture1').val() == ""){
                        _valid = false;
                        $.messager.alert('提示', '请选择封面图片');
                    }else if(!isUEvalid) {
                        var isUEvalid = validateUE();
                        if (isUEvalid) {
                            param.introduction = ue_catalog.getContent();
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
                    $('#whgwin-add-btn-save').off('click').one('click', $('#whgff').submit());
                }
            }
        });
		/*防止表单重复提交*/
        $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
    });

	/*验证富文本框*/
    function validateUE() {
        if (!ue_catalog.hasContents()) {
            $.messager.alert("错误", '简介不能为空', 'error');
            return false;
        }
        return true;
    }
</script>

</body>
</html>
