<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<% String path = request.getContextPath();%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>非遗展馆管理</title>
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
			<div class="whgff-row-label"><i>*</i>展厅名称：</div>
			<div class="whgff-row-input">
				<input class="easyui-textbox" name="name" value="${showRoom.name}"
					style="width: 500px; height: 32px" data-options="required:true,validType:['length[1,40]']" />
			</div>
		</div>

		<div class="whgff-row">
			<div class="whgff-row-label">
				<label style="color: red">*</label>类型：
			</div>
			<div class="whgff-row-input">
				<div class="radio radio-primary whg-js-data" name="type" id="type" value="${showRoom.type}"
					 js-data='[{"id":"1","text":"虚拟展厅"},{"id":"2","text":"线上展厅"}]'>
				</div>
			</div>
		</div>

		<div class="whgff-row">
			<div class="whgff-row-label">
				<label style="color: red">*</label>上传封面：
			</div>
			<div class="whgff-row-input">
	            <input type="hidden" id="cult_picture1" name="picture" value="${showRoom.picture}">
	            <div class="whgff-row-input-imgview" id="previewImg1"></div>
	            <div class="whgff-row-input-imgfile">
	                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
	                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
	            </div>
	        </div>
		</div>

		 <div class="whgff-row">
			 <div class="whgff-row-label">
				<label style="color: red">*</label>区域：
			 </div>
			 <input class="easyui-combobox" style="width:150px; height:32px" id="province" name="province" data-options="required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name', data:__PROVINCE, onChange:myChangeProvince"/>
			 <input class="easyui-combobox" style="width:150px; height:32px" id="__CITY_ELE" name="city" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name', data:[], onChange:myChangeCity"/>
			 <input class="easyui-combobox" style="width:179px; height:32px" id="__AREA_ELE" name="area" data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
		</div>

		<div class="whgff-row">
			<div class="whgff-row-label"><i>*</i>所属机构：</div>
			<div class="whgff-row-input">
				<input class="easyui-combobox" style="width:245px; height:32px" name="organization" id="organization" data-options="required:false,prompt:'请选择所属机构'" />
			</div>
		</div>

		<div class="whgff-row">
			<div class="whgff-row-label">链接地址：</div>
			<div class="whgff-row-input">
				<input class="easyui-textbox" name="url" style="width:600px; height:32px" data-options="required:false,validType:'url',prompt:'请输入连接地址,如：http://yoursite.com/'">
			</div>
		</div>

		<div class="whgff-row">
			<div class="whgff-row-label">
				<label style="color: red">*</label>展馆简介：
			</div>
			<div class="whgff-row-input">
				<script id="catalog" type="text/plain" style="width: 600px; height: 300px;"></script>
			</div>
		</div>
	</form>

	<div class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
		<a class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok" id="whgwin-add-btn-save">提 交</a>
		<a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
	</div>
<script>
    //省市区
    var __init_city = true;
    var __init_area = true;
    function myChangeProvince(newVal, oldVal) {
        changeProvince(newVal, oldVal);
        if (__init_city) {
            window.setTimeout(function () {
                $('#__CITY_ELE').combobox('setValue', '${showRoom.city}');
            }, 500);
            __init_city = false;
        }
    }

    function myChangeCity(newVal, oldVal) {
        changeCity(newVal, oldVal);
        if (__init_area) {
            window.setTimeout(function () {
                $('#__AREA_ELE').combobox('setValue', '${showRoom.area}')
            }, 500);
            __init_area = false;
        }
    }  //省市区------END

    var ueConfig = {
        scaleEnabled: false,
        autoFloatEnabled: false,
        elementPathEnabled: false,
        readonly: '${targetShow}' ? true : false
    };
    var ue_catalog = UE.getEditor('catalog', ueConfig);
    ue_catalog.ready(function () {
        ue_catalog.setContent('${showRoom.intro}')
    });

    $(function () {
        //初始化省
        $('#province').combobox('setValue', '${showRoom.province}');
        //图片初始化
        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1'});

        $('#whgff').form({
            novalidate: true,
            url: "${basePath}/admin/showroom/edit",
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
                            param.intro = ue_catalog.getContent();
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
            $.messager.alert("错误", '展馆简介不能为空', 'error');
            return false;
        }
        return true;
    }
	/*查看时的处理*/
    $(function () {
        var targetShow = '${targetShow}';
        var province = '${showRoom.province}';
        if (!province || province == '') {
            province = '广东省';
        }
        $("#__PROVINCE_ELE").combobox("setValue", province);

        var city = '${cult.city}';
        if (city && city != '') {
            $("#__CITY_ELE").combobox("setValue", city);
        }
        var area = '${cult.area}';
        if (area && area != '') {
            $("#__AREA_ELE").combobox("setValue", area);
        }
        if (targetShow) {
            //取消表单验证
            $("#whgff").form("disableValidation");

            $('.easyui-textbox').textbox('readonly');
            $('.easyui-combobox').combobox('readonly');
            $("#whgff").find("input[type='checkbox'], input[type='radio']").on('click', function () {
                return false
            });

            //不显示提交 button
            $('#whgwin-add-btn-save').hide();
            return;
        }

    });
</script>

</body>
</html>
