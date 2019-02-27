<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>站点申请</title>
    <!-- 编辑表单 -->
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>


    <link rel="stylesheet" type="text/css" href="${basePath }/static/easyui/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="${basePath }/static/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${basePath }/static/easyui/themes/color.css">

    <script type="text/javascript">
        var basePath = '${basePath}';
        var imgServerAddr = '${baseImgPath}';
    </script>
    <script type="text/javascript" src="${basePath }/static/easyui/jquery.min.js" ></script>
    <script type="text/javascript" src="${basePath }/static/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${basePath }/static/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="${basePath }/static/admin/js/common.js"></script>
    <script type="text/javascript" src="${basePath }/static/common/js/whg.sys.base.data.js"></script>

    <!-- 编辑表单-END -->
    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <!-- 图片上传相关-END -->
    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
    <!-- 根据地址取坐标 -->
    <script type="text/javascript" src="${basePath}/static/common/js/whg.maps.js"></script>
    <script type="text/javascript" src="${basePath}/static/common/js/area.js"></script>

    <style type="text/css">
        .panel-title{ color:#333; font-size:14px;}
        .grid-control-group {padding: 10px; background: #fafafa;}
        .window, .window-shadow{
            -webkit-border-radius:0px;
            -moz-border-radius:0px;
            border-radius:0px;
        }
        .datagrid-header td, .datagrid-body td, .datagrid-footer td{
            padding-top:6px;
            padding-bottom:6px;
        }
        .datagrid-row-selected{
            background-color:#e5e5e5;
            color:#000;
        }

        .datagrid-toolbar, .datagrid-pager{
            padding:10px 0px 10px 10px
        }

        .panel-title{
            padding:10px 15px;
            height:auto;
        }

        input[type=radio], input[type=checkbox] {
            margin: 2px 5px 0 0;
            margin-top: 1px \9;
            line-height: normal;
            vertical-align: top;
        }

        .textbox{
            margin-right:6px;
        }



        /** 表单for wangxl */
        form.whgff{
            margin: 0 auto;
            width: 980px;
        }

        form.whgff h2{
            font-size:24px;
            padding-left:150px;
            margin-bottom:25px;
            margin-top:30px;
        }

        form.whgff > .whgff-row{
            padding: 10px 0;
            overflow: hidden;
        }
        form.whgff > .whgff-row > .whgff-row-label{
            float: left;
            width: 15%;
            text-align: right;
            font-weight:normal;
            font-size: 15px;
            line-height:25px;
        }
        form.whgff > .whgff-row > .whgff-row-label i{
            font-size:18px;
            display: inline-block;
            margin-right:5px;
            color:#ff0000;
            font-style:normal;
            vertical-align: middle;
            margin-top:5px;
        }
        form.whgff > .whgff-row > .whgff-row-input{
            float: left;
            width: 85%;
            overflow: hidden;
        }
        form.whgff > .whgff-row > .whgff-row-input > .whgff-row-input-imgview{
            border: 1px #ddd solid;
            width: 300px;
            height: 225px;
            border-radius: 5px;
            overflow: hidden;
            background:url("${basePath}/static/admin/img/no-img.png") no-repeat 50% 50% #f0f0f0;
        }
        form.whgff > .whgff-row > .whgff-row-input > .whgff-row-input-imgfile{
            margin-left: 320px;
            margin-top: -48px;
        }

        .whgff-row-input .radio label,.whgff-row-input .checkbox label{ display: inline-block; margin-right:30px; font-size:12px;}
        .whgff-row-input .radio, .whgff-row-input .checkbox{ margin:4px 0px 10px 0px}
        .whgff-row-input-imgfile i{color:#999; font-size:12px;  display: block; margin-top:10px; font-style: normal}
        .whgd-gtb-btn{margin-bottom:10px;}
        .body_add{background-color:#f5f5f5; border:1px #ddd solid;}
        .whgff-row-map{
            float:left;
            padding-left:113px;
            margin-top:20px;
            position:relative;
        }
        .whgff-row-map .seat-ul-div{
            max-width:650px;
            overflow-x:scroll;
        }
        .whgff-row-map .help-cont{
            position:absolute;
            width:200px;
            height:150px;
            right:-260px;
            top:49px;
        }
        .whgff-row-map .seat-row-control{
            position:absolute;
            width:30px;
            top:50px;
            right:-30px;
            font-size:12px;
        }
        .whgff-row-map .seat-row-control p{
            cursor: pointer;
        }
        .whgff-row-map .seat-column-control{
            padding-left:40px;
            height:30px;
            font-size:12px;
        }
        .whgff-row-map .seat-column-control p{
            height:30px;
            line-height:30px;
            width:75px;
            float:left;
            margin-right:5px;
            cursor: pointer;
        }
        .whgff-row-map .seat-row-control p,.whgff-row-map .seat-column-control p{
            color:#fff;
            background-color:#6da82e;
            text-align: center;
        }
        .whgff-row-map .led-cont{
            padding-left:40px;
        }
        .whgff-row-map .led-bg{
            height: 30px;
            width: 60%;
            margin:0px auto;
            background-color:#aaa;
            margin-bottom:20px;
            text-align: center;
            color:#fff;
            line-height:30px;
        }
        .whgff-row-map .help-row-number{
            position:absolute;
            width:55px;
            left:60px;
            top:49px;
            font-size:12px;
        }
        .whgff-row-map .help-cont p,.whgff-row-map .help-row-number p,.whgff-row-map .seat-row-control p{
            margin-bottom:5px;
            height:30px;
            line-height:30px;
        }
        .whgff-row-map .help-cont span{
            margin-right:10px;
            vertical-align: middle;
            display:inline-block;
            width:30px;
            height:30px;
        }
        .whgff-row-map ul {
            height:30px;
            margin-bottom:5px;
        }
        .whgff-row-map ul li{
            height:30px;
            width:75px;
            float:left;
            list-style: none;
            margin-right:5px;
            color:#fff;
            line-height:30px;
            text-align: center;
        }
        .whgff-row-map ul li input{
            height: 30px;
            width:75px;
            background-color:#fff;
            border:1px #999 solid;
            text-align: center;
            line-height:28px;
            color:#000;
        }
        .whgff-row-map ul .type-0,.whgff-row-map .help-cont .type-0{
            background-color:#6da82e;
        }
        .whgff-row-map ul .type-1,.whgff-row-map .help-cont .type-1{
            background-color:#aaa;
        }
        .whgff-row-map ul .type-2,.whgff-row-map .help-cont .type-2{
            background-color:#d82f2f;
        }
        .whgff-row-map ul .active,.whgff-row-map .help-cont .active{
            background-color: #f48331 ;
        }
        .seat-operation{
            margin-top:20px;
            margin-left:38px;
        }
        .whg-week-cont{
            border-left:1px #eee solid;
            margin-top:10px;
        }
        .whg-week-column{
            width:192px;
            display: inline-block;
            vertical-align: top;
            border:1px #eee solid;
            border-left:0px;
            background-color:#fff;
            padding:10px 15px;
            position:relative;
        }
        .whg-week-cont .disabled{
            background-color:#eee;
        }
        .whg-week-column dl{
            margin-bottom:5px;
        }
        .whg-week-column dl dt{
            font-weight:normal;
            text-indent: 50px;
            margin-bottom:5px;

        }
        .whg-week-cont .disabled dt{
            color:#999;
        }

        .whg-week-column dl dd{
            margin-bottom:5px;
        }
        .whg-week-column dl dd .del{
            display: none;
            width:20px;
            height:20px;
            background:url("${basePath}/static/admin/img/del.png") no-repeat 50% 50%;
            vertical-align: middle;
        }
        .whg-week-column dl dd:hover .del{
            display: inline-block;
            cursor: pointer;
        }

        .whg-week-column .add{
            width:16px;
            height:16px;
            background:url("${basePath}/static/admin/img/add.png") no-repeat 50% 50%;
            margin-left:60px;
            cursor:pointer;
        }
        .whg-week-cont .disabled .add{
            cursor:default;
            background:none;
        }
        .whg-week-timeinput{
            width:60px;
        }
        .validaMessage{
            line-height:22px;
            padding:2px 5px;
            background-color:#ffffcc;
            border:#cc9933 1px solid;
            text-align: center;
            width:600px;
            margin-top:10px;
            -webkit-border-radius:5px;
            -moz-border-radius:5px;
            border-radius:5px;
        }

        .crumbs {
            width: 1020px;
            margin: 100px auto 30px auto;
            height: 50px;
            line-height: 50px;
            color: #fff;
            font-size: 20px;
            padding: 0;
        }

        .crumbs li {
            width: 300px;
            height: 50px;
            float: left;
            text-align: center;
            margin-right: 40px;
            position: relative;
            list-style: none;
        }

        .crumbs li .arrow {
            background: url(../../static/img/crumbs.png) no-repeat;
            position: absolute;
            right: -40px;
            height: 50px;
            width: 40px;
            bottom: 0
        }

        .crumbs .last {
            margin-right: 0
        }

        .crumbs-1st .step-1 {
            background-color: #379bd8
        }

        .crumbs-1st .step-2, .crumbs-1st .step-3 {
            background-color: #bebebe
        }

        .crumbs-1st .step-1 .arrow {
            background-position: 0 0
        }

        .crumbs-1st .step-2 .arrow {
            background-position: -41px 0
        }

        .crumbs-2nd .step-1, .crumbs-2nd .step-2 {
            background-color: #379bd8
        }

        .crumbs-2nd .step-3 {
            background-color: #bebebe
        }

        .crumbs-2nd .step-1 .arrow {
            background-position: -82px 0
        }

        .crumbs-2nd .step-2 .arrow {
            background-position: 0 0
        }

        .crumbs-3rd .step-1, .crumbs-3rd .step-2 {
            background-color: #379bd8
        }

        .crumbs-3rd .step-3 {
            background-color: #5ebb2b
        }

        .crumbs-3rd .step-1 .arrow {
            background-position: -82px 0
        }

        .crumbs-3rd .step-2 .arrow {
            background-position: -123px 0
        }

        .easyui-linkbutton{
            padding: 5px 15px;
        }
        .l-btn{
            color: #fff!important;
            background: #379bd8;
            background-repeat: repeat-x;
            border: 1px solid #379bd8;
            background: -webkit-linear-gradient(top,#379bd8 0,#379bd8 100%);
            background: -moz-linear-gradient(top,#379bd8 0,#379bd8 100%);
            background: -o-linear-gradient(top,#379bd8 0,#379bd8 100%);
            background: linear-gradient(to bottom,#379bd8 0,#379bd8 100%);
            background-repeat: repeat-x;
            filter: progid:DXImageTransform.Microsoft.gradient(startColorstr=#379bd8,endColorstr=#379bd8,GradientType=0);
            -moz-border-radius: 5px 5px 5px 5px;
            -webkit-border-radius: 5px 5px 5px 5px;
            border-radius: 5px 5px 5px 5px;
        }
        .l-btn:hover {
            background: #379bd8;
            color: #fff;
            border: 1px solid #379bd8;
            filter: none;
        }
        .regist-logo {
            height: 155px;
            width: 100%;
            background: url(../../../static/img/logo.png) no-repeat 50% 50%;
            padding: 60px 0;
            text-align: center;
            margin-top: 100px;
        }
        div.success{
            background: #fff;
            height: 500px;
        }
        div.success img{
            margin: 0 auto;
            display: block;
        }
        div.success>span{
            display: block;
            text-align: center;
            font-size: 16px;
            color: #999;
            margin-top: 10px;
        }
    </style>

</head>
<body>
<div class="regist-logo"></div>
<ul class="crumbs crumbs-1st clearfix" id="showUl">
    <li class="step-1">1. 填写站点申请资料<em class="arrow"></em></li>
    <li class="step-2">2. 等待上级管理员审批<em class="arrow"></em></li>
    <li class="step-3 last">3. 上线运营</li>
</ul>
<form id="whgff" class="whgff" method="post">

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>省市区：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:150px; height:32px" id="province" name="province" readonly="true" data-options="required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name', data:__PROVINCE, onChange:myChangeProvince"/>
            <input class="easyui-combobox" style="width:150px; height:32px" name="city" id="__CITY_ELE" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name', data:[], onChange:changeCity"/>
            <input class="easyui-combobox" style="width:179px; height:32px" name="area" id="__AREA_ELE" data-options="prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
            <span style="color: #999;font-size: 12px;display: block;margin-top: 10px;font-style: normal;">温馨提示：如果申请市馆选择到市即可，如：广东省xxx市，区馆则需全填</span>
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
                <i><a href="javascript:void(0)" class="easyui-linkbutton" id="imgUploadBtn3">选择图片</a></i>
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
                <i><a href="javascript:void(0)" class="easyui-linkbutton" id="imgUploadBtn4">选择图片</a></i>
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
                <i><a href="javascript:void(0)" class="easyui-linkbutton" id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
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

    <div class="whgff-row"  style="display: none">
        <div class="whgff-row-label">文化馆站点：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="siteurl" style="width:500px; height:32px" data-options="prompt:'文化馆站点地址，如:http://yoursite.com/', validType:['url', 'length[2,512]']">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>简介：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="introduction" style="width:600px; height:250px" data-options="required:true, multiline:true, validType:['length[1,500]', 'isText']">
        </div>
    </div>

    <div id="whgwin-add-btn" class="whgff-but" style="width: 400px; margin:20px auto 50px auto">
        <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" id="whgwin-add-btn-save" style="margin-left: 50px;padding: 5px 25px">提 交</a>
    </div>
</form>
<div class="success" style="display: none">
    <img src="../../../static/img/success.png">
    <span>站点申请资料提交成功！</span>
    <span>上级管理员将通过电话或短信的方式与您联系，请注意查收！</span>
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
         WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn3', hiddenFieldId: 'cult_picture3', previewImgId: 'previewImg3', needCut:false, cutWidth:166, cutHeight:35,isSingleSy:false});
        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn4', hiddenFieldId: 'cult_picture4', previewImgId: 'previewImg4', needCut:false, cutWidth:166, cutHeight:35,isSingleSy:false});
        $('#whgff').form({
            novalidate: true,
            url : getFullUrl('cultregist/add'),
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
                    $("#showUl").removeClass();
                    $("#showUl").addClass("crumbs crumbs-2nd clearfix");
                    $("#whgff").hide();
                    $(".success").show();
                }else{
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