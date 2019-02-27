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
                <h2>子馆管理-查看子馆资料</h2>
            </c:when>
            <c:otherwise>
                <h2>子馆管理-编辑子馆资料</h2>
            </c:otherwise>
        </c:choose>
    </title>
    
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>

    <!-- 编辑表单 -->
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>
    <!-- 编辑表单-END -->
    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <!-- 图片上传相关-END -->

    <!-- 根据地址取坐标 -->
    <script type="text/javascript" src="${basePath}/static/common/js/whg.maps.js"></script>
    <script type="text/javascript" src="${basePath}/static/common/js/area.js"></script>
</head>
<body>


<form id="whgff" class="whgff" method="post">
    <input type="hidden" name="id" value="${cult.id}" >
    <input type="hidden" name="onlyshow" value="${param.onlyshow}" >

    <c:choose>
        <c:when test="${param.onlyshow == '1'}">
            <h2>子馆管理-查看子馆资料</h2>
        </c:when>
        <c:otherwise>
            <h2>子馆管理-编辑子馆资料</h2>
        </c:otherwise>
    </c:choose>

    <c:if test="${param.noparnet != '1'}">
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>上级文化馆：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:500px; height:32px" name="pid" id="pid" value="${cult.pid}" data-options="required:true, prompt:'请选择上级文化馆', valueField:'id', textField:'text', data:WhgComm.getMgrCults()"/>
        </div>
    </div>
    </c:if>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>名称：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="name" value="${cult.name}" style="width:500px; height:32px" data-options="required:true, validType:'length[3,30]'"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>LOGO：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture3" name="logopicture" value="${cult.logopicture}" >
            <div class="whgff-row-input-imgview" id="previewImg3" style="height: 70px;"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn3">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 166*35，大小为2MB以内</i>
            </div>
        </div>
    </div>

  <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>水印图片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture4" name="sypicture" value="${cult.sypicture}" >
            <div class="whgff-row-input-imgview" id="previewImg4" style="height: 70px;"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn4">选择图片</a></i>
                <i>图片格式为png，建议图片尺寸 166*35，大小为2MB以内</i>
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>封面图片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture1" name="picture" value="${cult.picture}" >
            <div class="whgff-row-input-imgview" id="previewImg1" style="height: 200px;"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>

    <%--<div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>背景图片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture2" name="bgpicture"value="${cult.bgpicture}" >
            <div class="whgff-row-input-imgview" id="previewImg2"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn2">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 1920*1080，大小为2MB以内</i>
            </div>
        </div>
    </div>--%>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>省市区：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:150px; height:32px" id="province" name="province" data-options="required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name', data:__PROVINCE, onChange:myChangeProvince"/>
            <input class="easyui-combobox" style="width:150px; height:32px" id="__CITY_ELE" name="city" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name', data:[], onChange:myChangeCity"/>
            <input class="easyui-combobox" style="width:179px; height:32px" id="__AREA_ELE" name="area" data-options="prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
            <span style="color: #999;font-size: 12px;display: block;margin-top: 10px;font-style: normal;">温馨提示：如果新建市馆选择到市即可，如：广东省xxx市，区馆则需全填</span>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>文化馆地址：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" id="address" name="address" value="${cult.address}" style="width:500px; height:32px" data-options="prompt:'请输入文化馆地址', required:true, validType:['length[2,35]']">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>文化馆坐标：</div>
        <div class="whgff-row-input">
            <input class="easyui-numberbox" id="longitude" name="longitude" value="${cult.longitude}" style="width:150px; height:32px" data-options="required:true, precision:6, readonly:true, prompt:'经度'">
            <input class="easyui-numberbox" id="latitude" name="latitude" value="${cult.latitude}" style="width:150px; height:32px" data-options="required:true, precision:6, readonly:true, prompt:'纬度'">
            <a class="easyui-linkbutton" id="getXYPointBtn" text="选择坐标"></a>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>联系人：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="contact" value="${cult.contact}" style="width:500px; height:32px" data-options="prompt:'请输入联系人姓名',required:true,validType:['length[2,20]']"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>联系方式：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="contactnum" value="${cult.contactnum}" style="width:500px; height:32px" data-options="prompt:'请输入联系人手机', required:true, validType:'isPhone'"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">文化馆站点：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="siteurl" value="${cult.siteurl}" style="width:500px; height:32px" data-options="prompt:'文化馆站点地址，如:http://yoursite.com/', validType:['url', 'length[2,512]']">
        </div>
    </div>

    <div class="whgff-row" >
        <div class="whgff-row-label"><i>*</i>前端菜单：</div>
        <div class="whgff-row-input" data-check="true" target="frontmenu" err-msg="请至少选择一个前端菜单">
            <div class="checkbox checkbox-primary whg-js-data" name="frontmenu" id="frontmenu" value="${cult.frontmenu}"  js-data="WhgComm.getFrontMenu">
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>简介：</div>
        <div class="whgff-row-input">
            <textarea id="textarea_introduction" style="display: none;">${cult.introduction}</textarea>
            <input class="easyui-textbox" name="introduction" id="introduction" value="" style="width:600px; height:250px" data-options="required:true, multiline:true, validType:['length[1,500]', 'isText']">
        </div>
    </div>
</form>

<div id="whgwin-edit-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <c:if test="${param.onlyshow != '1'}">
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok" id="whgwin-edit-btn-save">提 交</a>
    </c:if>
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
</div>

<!-- script -->
<script type="text/javascript">
    var __init_city = true;
    var __init_area = true;
    function myChangeProvince(newVal, oldVal) {
        changeProvince(newVal, oldVal);
        if(__init_city){
            window.setTimeout(function () {$('#__CITY_ELE').combobox('setValue', '${cult.city}');}, 500);
            __init_city = false;
        }
    }

    function myChangeCity(newVal, oldVal) {
        changeCity(newVal, oldVal);
        if(__init_area){
            window.setTimeout(function () {$('#__AREA_ELE').combobox('setValue', '${cult.area}')}, 500);
            __init_area = false;
        }
    }

    $(function () {
        //图片初始化
        if($('#whgff input[name="onlyshow"]').val() == "1"){
            WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1', allowUpload:false});
        }else{
            WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1'});
        }
        //WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn2', hiddenFieldId: 'cult_picture2', previewImgId: 'previewImg2', needCut:false, cutWidth:1920 });
        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn3', hiddenFieldId: 'cult_picture3', previewImgId: 'previewImg3', needCut:false, cutWidth:166, cutHeight:35});
        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn4', hiddenFieldId: 'cult_picture4', previewImgId: 'previewImg4', needCut:false, cutWidth:166, cutHeight:35,isSingleSy:false});

        //简介赋初始值
        $('#introduction').textbox("setValue", $('#textarea_introduction').val());

        $('#whgff').form({
            novalidate: true,
            url : getFullUrl('/admin/system/cult/edit'),
            onSubmit : function(param) {
                //alert('onSubmit');
                var _valid = $(this).form('enableValidation').form('validate');
                //图片必填
                if(_valid){
                    if($('#cult_picture3').val() == ""){
                        _valid = false;
                        $.messager.alert('提示', '请选择文化馆的LOGO图片');
                    }else if($('#cult_picture1').val() == ""){
                        _valid = false;
                        $.messager.alert('提示', '请选择文化馆的封面图片');
                    }else if($('#cult_picture4').val() == ""){
                        _valid = false;
                        $.messager.alert('提示', '请选择文化馆的水印图片');
                    }/*else if($('#cult_picture2').val() == ""){
                        _valid = false;
                        $.messager.alert('提示', '请选择文化馆的主页背景图片');
                    }*/
                }
                if(_valid){
                    $.messager.progress();
                }else{
                    //失败时再注册提交事件
                    $('#whgwin-edit-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
                }
                return _valid;
            },
            success : function(data) {
                $.messager.progress('close');
                var Json = eval('('+data+')');
                if(Json && Json.success == '1'){
                    if($('#whgff input[name="onlyshow"]').val() != "1") {
                        window.parent.$('#whgdg').datagrid('reload');
                    }
                    WhgComm.editDialogClose();
                } else {
                    $.messager.alert('提示', '操作失败: '+Json.errormsg+'!', 'error');
                    $('#whgwin-edit-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
                }
            }
        });
        //注册提交事件
        $('#whgwin-edit-btn-save').off('click').one('click', function () { $('#whgff').submit(); });

        //初始省值
        $('#province').combobox('setValue', '${cult.province}');

        //查看状态下表单元素不能编辑
        if($('#whgff input[name="onlyshow"]').val() == "1"){
            $('#whgff input').attr('readonly', 'readonly');
            $('#whgff textarea').attr('readonly', 'readonly');
            $('#province').combobox('readonly');
            $('#__CITY_ELE').combobox('readonly');
            $('#__AREA_ELE').combobox('readonly');
            $('#pid').combobox('readonly');
        }

        //根据地址取坐标
        WhgMap.init({
            basePath:'${basePath}',
            addrFieldId:'address',
            xpointFieldId:'longitude',
            ypointFieldId:'latitude',
            getPointBtnId:'getXYPointBtn'
        });
    });
</script>
<!-- script END -->
</body>
</html>