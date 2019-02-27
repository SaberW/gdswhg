<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>轮播图配置-编辑轮播图</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>
    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <!-- 图片上传相关-END -->
</head>
<body>


<form id="whgff" method="post" class="whgff">
    <input type="hidden" name="type" value="${clazz}">
    <input type="hidden" name="id" id="id" value="${id}"/>

    <h2>${param.onlyshow == '1' ? '轮播图配置-查看轮播图' : '轮播图配置-编辑轮播图'}</h2>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>标　　题：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" id="name" name="name" value="${lbt.name}" style="width:600px; height:32px" data-options="required:true , validType:'length[0,30]'">
        </div>
    </div>

    <div class="whgff-row toCult" style="display: none;">
        <div class="whgff-row-label" ><i>*</i>文化馆　：</div>
        <div class="whgff-row-input" style="width: 75%">
            <input class="easyui-combobox" name="cultid" id="cultid" style="width:600px; height:32px" panelHeight="auto" data-options="prompt:'请选择文化馆', required:true">
        </div>
    </div>

    <div class="whgff-row toProvince" style="display: none;">
        <div class="whgff-row-label"><i>*</i>全省站　：</div>
        <div class="whgff-row-input" style="width: 75%">
            <input class="easyui-textbox" name="province" id="province" style="width:600px; height:32px" data-options="readonly:true">
        </div>
    </div>

    <div class="whgff-row toCity" style="display: none;">
        <div class="whgff-row-label"><i>*</i>全市站　：</div>
        <div class="whgff-row-input" style="width: 75%">
            <input class="easyui-textbox" name="city" id="city" style="width:600px; height:32px" data-options="readonly:true">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>图　　片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture1" name="picture" value="${lbt.picture}">
            <div class="whgff-row-input-imgview" id="previewImg1"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 1200*435，大小为2MB以内</i>
            </div>
        </div>
    </div>

    <div class="whgff-row" id="showRemark" style="display: none;">
        <div class="whgff-row-label">简介：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="remark" multiline="true" style="width:600px;height: 100px;"
                   value="${lbt.remark}"
                   data-options="required:false,validType:['length[1,800]']">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">连接地址：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" id="url" name="url" value="${lbt.url}" style="width:600px; height:32px" data-options="required:false,validType:'url',prompt:'请输入连接地址,如：http://yoursite.com/'">
        </div>
    </div>
</form>
<div id="whgwin-add-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <c:if test="${param.onlyshow != '1'}">
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" id="btn" type="submit">保 存</a>
    </c:if>
    <a href="#" class="easyui-linkbutton" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
</div>

<script>
    function initForm() {
        $('#whgff').form({
            novalidate: true,//去掉验证
            url: '${basePath}/admin/yunwei/lbt/edit',
            onSubmit: function () {
                var _valid = $(this).form('enableValidation').form('validate');
                if($("#cult_picture1").val() == ""){
                    _valid = false;
                    $.messager.alert('提示', '请选图片');
                }
                if (_valid) {
                    $.messager.progress();
                } else {
                    $('#btn').off('click').one('click', function () { $('#whgff').submit(); });
                }
                return _valid;
            },
            success: function (data) {
                $.messager.progress('close');
                var Json = $.parseJSON(data);
                if (Json.success == "1") {
                    window.parent.$('#whgdg').datagrid('reload');
                    WhgComm.editDialogClose();
                } else {
                    $.messager.alert("提示", Json.errormsg);
                }
            }
        });
        $('#btn').off('click').one('click', function () { $('#whgff').submit(); });
    }

    /** 初始省市或者所属文化馆 */
    function initCultProvinceCity(){
        var lbt_cultid = '${lbt.cultid}';
        var lbt_province = '${lbt.province}';
        var lbt_city = '${lbt.city}';

        if(lbt_cultid != ''){//文化馆配置的轮播图
            $('.toCult').show();
            WhgComm.initPMS({cultEid:'cultid', cultValue:"${lbt.cultid}"});
        }else if(lbt_province != ''){//全省站轮播图
            $('.toProvince').show();
            $('#province').textbox('setValue', lbt_province);
            $('#cultid').combobox({required:false, value:''});
        }else if(lbt_city != ''){//全市站轮播图
            $('.toCity').show();
            $('#city').textbox('setValue', lbt_city);
            $('#cultid').combobox({required:false, value:''});
        }
    }

    $(function () {
        /** 初始省市或者所属文化馆 */
        initCultProvinceCity();

        var clazz = '${clazz}';
        var _cutWidth = 1200, _cutHeight = 435;
        if ($.inArray(clazz, ['7', '8', '9', '10']) != -1) {
            _cutWidth = 750;
            _cutHeight = 360;
            var _cutText = $(".whgff-row-input-imgfile i:last").text();
            _cutText = _cutText.replace("1200*435", _cutWidth + "*" + _cutHeight);
            $(".whgff-row-input-imgfile i:last").text(_cutText);
        } else if ($.inArray(clazz, ['11']) != -1) {//活动广告位
            $("#showRemark").show();
            _cutWidth = 410;
            _cutHeight = 500;
            var _cutText = $(".whgff-row-input-imgfile i:last").text();
            _cutText = _cutText.replace("1200*435", _cutWidth + "*" + _cutHeight);
            $(".whgff-row-input-imgfile i:last").text(_cutText);
        } else if ($.inArray(clazz, ['12']) != -1) {//培训广告位
            _cutWidth = 560;
            _cutHeight = 525;
            var _cutText = $(".whgff-row-input-imgfile i:last").text();
            _cutText = _cutText.replace("1200*435", _cutWidth + "*" + _cutHeight);
            $(".whgff-row-input-imgfile i:last").text(_cutText);
        }

        //初始图片
        WhgUploadImg.init({ basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1', needCut: true, cutWidth: _cutWidth, cutHeight: _cutHeight });

        //初始化表单
        initForm();
    });
</script>
</body>
</html>