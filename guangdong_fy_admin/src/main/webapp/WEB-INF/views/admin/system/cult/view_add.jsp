<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>子馆管理-添加子馆资料</title>
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

    <h2>子馆管理 &gt; 添加子馆资料</h2>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>上级文化馆：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:500px; height:32px" name="pid" data-options="required:true, prompt:'请选择上级文化馆', value:WhgComm.getMgrCults()[0].id, valueField:'id', textField:'text', data:WhgComm.getMgrCults()"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>名称：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="name" style="width:500px; height:32px" data-options="required:true, prompt:'子馆名称，最多30个字符', validType:'length[3,30]'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>LOGO：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture3" name="logopicture" >
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
            <input type="hidden" id="cult_picture4" name="sypicture" >
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
            <input type="hidden" id="cult_picture1" name="picture" >
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
            <input type="hidden" id="cult_picture2" name="bgpicture" >
            <div class="whgff-row-input-imgview" id="previewImg2"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="#" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn2">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 1920*1080，大小为2MB以内</i>
            </div>
        </div>
    </div>--%>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>省市区：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:150px; height:32px" id="province" name="province" readonly="true" data-options="required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name', data:__PROVINCE, onChange:myChangeProvince"/>
            <input class="easyui-combobox" style="width:150px; height:32px" name="city" id="__CITY_ELE" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name', data:[], onChange:changeCity"/>
            <input class="easyui-combobox" style="width:179px; height:32px" name="area" id="__AREA_ELE" data-options=" prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
            <span style="color: #999;font-size: 12px;display: block;margin-top: 10px;font-style: normal;">温馨提示：如果新建市馆选择到市即可，如：广东省xxx市，区馆则需全填</span>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>文化馆地址：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" id="address" name="address" style="width:500px; height:32px" data-options="prompt:'请输入文化馆地址', required:true, validType:['length[2,35]']">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>文化馆坐标：</div>
        <div class="whgff-row-input">
            <input class="easyui-numberbox" id="longitude" name="longitude" style="width:150px; height:32px" data-options="required:true, precision:6, readonly:true, prompt:'经度'">
            <input class="easyui-numberbox" id="latitude" name="latitude" style="width:150px; height:32px" data-options="required:true, precision:6, readonly:true, prompt:'纬度'">
            <a class="easyui-linkbutton" id="getXYPointBtn" text="选择坐标"></a>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>联系人：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="contact" style="width:500px; height:32px" data-options="prompt:'请输入联系人姓名',required:true,validType:['length[2,20]']"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>联系方式：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="contactnum" style="width:500px; height:32px" data-options="prompt:'请输入联系人手机', required:true, validType:'isPhone'"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">文化馆站点：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="siteurl" style="width:500px; height:32px" data-options="prompt:'文化馆站点地址，如:http://yoursite.com/', validType:['url', 'length[2,512]']">
        </div>
    </div>

    <div class="whgff-row" >
        <div class="whgff-row-label"><i>*</i>前端菜单：</div>
        <div class="whgff-row-input" data-check="true" target="frontmenu" err-msg="请至少选择一个前端菜单">
            <div class="checkbox checkbox-primary whg-js-data" name="frontmenu" id="frontmenu"   js-data="WhgComm.getFrontMenu">
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>简介：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="introduction" style="width:600px; height:250px" data-options="required:true, multiline:true, validType:['length[1,500]', 'isText']">
        </div>
    </div>
</form>

<div id="whgwin-add-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok" id="whgwin-add-btn-save">提 交</a>
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
</div>

<!-- script -->
<script type="text/javascript">
    var mydescUE;

    //省市区
    var province = WhgComm.getProvince()?WhgComm.getProvince():'广东省';
    var city = WhgComm.getCity()?WhgComm.getCity():"";
    var __init_city = true;
    var __init_area = true;
    function myChangeProvince(newVal, oldVal) {
        changeProvince(newVal, oldVal);
        if(__init_city){
            window.setTimeout(function () {$('#__CITY_ELE').combobox('setValue', city);}, 500);
            __init_city = false;
        }
    }

    $(function () {
        $('#province').combobox('setValue', province);
        //图片初始化
        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1'});
        //WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn2', hiddenFieldId: 'cult_picture2', previewImgId: 'previewImg2', needCut:false, cutWidth:1920});
        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn3', hiddenFieldId: 'cult_picture3', previewImgId: 'previewImg3', needCut:false, cutWidth:166, cutHeight:35,isSingleSy:false});
        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn4', hiddenFieldId: 'cult_picture4', previewImgId: 'previewImg4', needCut:false, cutWidth:166, cutHeight:35,isSingleSy:false});

        $('#whgff').form({
            novalidate: true,
            url : getFullUrl('/admin/system/cult/add'),
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