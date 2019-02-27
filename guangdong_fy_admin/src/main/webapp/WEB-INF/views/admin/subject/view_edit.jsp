<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<% String path = request.getContextPath();%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>非遗专题管理</title>
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

	<script type="text/javascript" src="${basePath}/static/common/js/area.js"></script>
</head>
<body>
	<form id="whgff" method="post" class="whgff">
		<c:choose>
			<c:when test="${not empty targetShow}">
				<h2>查看非遗虚拟展厅</h2>
			</c:when>
			<c:otherwise>
				<h2>编辑非遗虚拟展厅</h2>
			</c:otherwise>
		</c:choose>
		<input type="hidden" name="id" value="${id}"/>
		<div class="whgff-row">
			<div class="whgff-row-label"><i>*</i>专题名称：</div>
			<div class="whgff-row-input">
				<input class="easyui-textbox" name="name" value="${subject.name}"
					   style="width: 500px; height: 32px" data-options="required:true,validType:['length[1,40]']" />
			</div>
		</div>

		<div class="whgff-row">
			<div class="whgff-row-label"><i>*</i>所属单位：</div>
			<div class="whgff-row-input">
				<input class="easyui-combobox" style="width:245px; height:32px" name="cultid" id="cultid" data-options="required:false,prompt:'请选择所属机构'" />
			</div>
		</div>

		<div class="whgff-row">
			<div class="whgff-row-label"><i>*</i>图片：</div>
			<div class="whgff-row-input">
				<input type="hidden" id="cult_picture1" name="picture" value="${subject.picture}">
				<div class="whgff-row-input-imgview" id="previewImg1"></div>
				<div class="whgff-row-input-imgfile">
					<i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
					<i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
				</div>
			</div>
		</div>

		<div class="whgff-row">
			<div class="whgff-row-label"><i></i>背景图片：</div>
			<div class="whgff-row-input">
				<input type="hidden" id="cult_picture2" name="bgpicture" value="${subject.bgpicture}">
				<div class="whgff-row-input-imgview" id="previewImg2"></div>
				<div class="whgff-row-input-imgfile">
					<i><a href="#" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn2">选择图片</a></i>
					<i>图片格式为jpg、png、gif，建议图片尺寸 1920*430，大小为2MB以内</i>
				</div>
			</div>
		</div>

		<div class="whgff-row">
			<div class="whgff-row-label">
				<label style="color: red">*</label>背景图片是否平铺：
			</div>
			<div class="whgff-row-input">
				<div class="radio radio-primary whg-js-data" name="norepeat" value="${subject.norepeat}"
					 js-data='[{"id":"1","text":"是"},{"id":"0","text":"否"}]'>
				</div>
			</div>
		</div>

		<div class="whgff-row">
			<div class="whgff-row-label"><label style="color: red"></label>品牌背景颜色：</div>
			<div class="whgff-row-input"><input class="easyui-textbox" name="bgcolour" value="${subject.bgcolour}" style="width:300px; height:32px" data-options="prompt:'背景颜色格式:#ffffff',required:false, validType:'length[1,30]'"></div>
		</div>

		<div class="whgff-row">
			<div class="whgff-row-label">
				<label style="color: red">*</label>非遗专题介绍：
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
        elementPathEnabled: false,
        readonly: '${targetShow}' ? true : false
    };
    var ue_catalog = UE.getEditor('catalog', ueConfig);
    ue_catalog.ready(function () {
        ue_catalog.setContent('${subject.introduction}')
    });

    $(function () {
        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'${subject.cultid}'
        });
		/*图片初始化*/
        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1'});
        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn2', hiddenFieldId: 'cult_picture2', previewImgId: 'previewImg2',cutWidth:1920,cutHeight:480});

        $('#whgff').form({
            novalidate: true,
            url: "${basePath}/admin/subject/edit",
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
    function validateUE(){
        if (!ue_catalog.hasContents()) {
            $.messager.alert("错误", '专题介绍不能为空', 'error');
            return false;
        }
        return true;
    }

	/*查看时的处理*/
    $(function () {
        var targetShow = '${targetShow}';

        if (targetShow) {
            //取消表单验证
            $("#whgff").form("disableValidation");

            $('.easyui-textbox').textbox('readonly');
            $('.easyui-combobox').combobox('readonly');
            $("#whgff").find("input[type='checkbox'], input[type='radio']").on('click', function () {
                return false
            });

			/*不显示提交 button*/
            $('#whgwin-add-btn-save').hide();
            return;
        }
    });
</script>

</body>
</html>
