<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<% String path = request.getContextPath();%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>添加专家</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>

    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>

    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>

    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <!-- 图片上传相关-END -->
    <script type="text/javascript" src="${basePath}/static/common/js/area.js"></script>
</head>
<body>

<form id="whgff" class="whgff" method="post" >
    <h2>添加专家</h2>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>姓名：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="name" value="${expert.name}"  style="width:500px; height:32px" data-options="required:true,prompt:'请输入内容', validType:'length[1,30]'"></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>图片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture1" name="image" value="${expert.image}" />
            <div class="whgff-row-input-imgview" id="previewImg1"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>性别：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" value="${expert.sex}" name="sex" js-data='[{"id":"0","text":"女"},{"id":"1","text":"男"}]'></div>
        </div>
    </div>
    <div class="whgff-row train">
        <div class="whgff-row-label "><label style="color: red">*</label>出生年月：</div>
        <div class="whgff-row-input">
            <input class="easyui-datebox " id="birthday" name="birthday" value="<fmt:formatDate value="${expert.birthday}" pattern="yyyy-MM-dd"/>" style="width:200px; height:32px" data-options="editable:false,required:true" />
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>工作单位：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="workunit" value="${expert.workunit}"  style="width:500px; height:32px" data-options="required:true,prompt:'请输入内容', validType:'length[1,100]'"></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>职务：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="job" value="${expert.job}"  style="width:500px; height:32px" data-options="required:true,prompt:'请输入内容', validType:'length[1,30]'"></div>
    </div>
     <div class="whgff-row">
         <div class="whgff-row-label"><label style="color: red">*</label>区域：</div>
         <div class="whgff-row-input">
             <input class="easyui-combobox" style="width:150px; height:32px" id="province" name="province" data-options="required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name', data:__PROVINCE,onChange:myChangeProvince"/>
             <input class="easyui-combobox" style="width:150px; height:32px" id="__CITY_ELE" name="city" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name', data:[], onChange:myChangeCity"/>
             <input class="easyui-combobox" style="width:179px; height:32px" id="__AREA_ELE" name="area" data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
         </div>
     </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>所属机构：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:245px; height:32px" name="cultid" id="cultid" data-options="required:true" />
        </div>
    </div>
    <div class="whgff-row train">
        <div class="whgff-row-label "><label style="color: red">*</label>年代：</div>
        <div class="whgff-row-input">
            <input class="easyui-datebox " id="years" name="years" value="<fmt:formatDate value="${expert.years}" pattern="yyyy-MM-dd"/>" style="width:200px; height:32px" data-options="editable:false,required:true" />
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>艺术分类：</div>
        <div class="whgff-row-input">
            <div class="checkbox checkbox-primary" id="arttype" name="arttype"></div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>专长类型：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary" id="etype" name="etype"></div>
        </div>
    </div>


    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>简介：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="introduce" value="${expert.introduce}" style="width: 500px; height: 100px" data-options="required:true,multiline:true,validType:['length[1,512]']"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>工作经历：</div>
        <div class="whgff-row-input">
            <script id="workexper" name="workexper" type="text/plain" style="width:600px; height:300px;"></script>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>获奖情况：</div>
        <div class="whgff-row-input">
            <script id="awards" name="awards" type="text/plain" style="width:600px; height:300px;"></script>
        </div>
    </div>
</form>

<div id="whgwin-add-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <div style="display: inline-block; margin: 0 auto">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="whgwin-add-btn-save">保 存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
    </div>
</div>

<!-- script -->
<script type="text/javascript">
    //省市区
    var province = '${expert.province}';
    var city = '${expert.city}';
    var __init_city = true;
    var __init_area = true;
    function myChangeProvince(newVal, oldVal) {
        changeProvince(newVal, oldVal);
        if(__init_city){
            if(!city || city==''){
                city = WhgComm.getCity()?WhgComm.getCity():"";
            }
            window.setTimeout(function () {$('#__CITY_ELE').combobox('setValue', city);}, 500);
            __init_city = false;
        }
    }

    function myChangeCity(newVal, oldVal) {
        changeCity(newVal, oldVal);
        if(__init_area){
            window.setTimeout(function () {$('#__AREA_ELE').combobox('setValue', '${expert.area}')}, 500);
            __init_area = false;
        }
    }  //省市区------END

    //处理UE
    var ueConfig = {
        scaleEnabled:true,
        autoFloatEnabled: false,
        elementPathEnabled:false,
        readonly: '${show}'? true: false
    };
    var workexper = UE.getEditor('workexper', ueConfig);
    var awards = UE.getEditor('awards', ueConfig);

    //UE 设置值
    workexper.ready(function(){  workexper.setContent('${expert.workexper}') });
    awards.ready(function(){  awards.setContent('${expert.awards}') });



    $(function () {
        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'${expert.cultid}',
            ywiArtTypeEid:'arttype', ywiArtTypeValue:'${expert.arttype}',
            ywiTypeEid:'etype', ywiTypeValue:'${expert.etype}', ywiTypeClass:5
        });

        //初始省值
        if (!province || province==''){
            province = WhgComm.getProvince()?WhgComm.getProvince():'广东省';
        }
        $('#province').combobox('setValue', province);
        var city = '${expert.city}';
        if (!city || city==''){
            city = WhgComm.getCity()?WhgComm.getCity():"";
        }
        $("#__CITY_ELE").combobox('setValue', city); //初始省值


        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1'});

        var show = "${show}";
        var id = "${id}";
        var frm = $("#whgff");
        //查看时的处理
        if (show){
            //取消表单验证
            frm.form("disableValidation");
            //组件设为只读
            $('.easyui-textbox').textbox('readonly');
            $('.easyui-combobox').combobox('readonly');
            $('.easyui-datetimebox').combobox('readonly');
            $('.easyui-datebox').combobox('readonly');
            $('.easyui-numberspinner').numberspinner('readonly');
            $("#imgUploadBtn1").hide();
            //处理选项点击不生效
            frm.find("input[type='checkbox'], input[type='radio']").on('click', function(){return false});
            //不显示提交 button
            $("#whgwin-add-btn-save").hide();
            return;
        }

        //定义表单提交
        var url = id ? "${basePath}/admin/fyi/expert/edit" : "${basePath}/admin/fyi/expert/add";

        $('#whgff').form({
            novalidate: true,
            url: url,
            onSubmit : function(param) {
                if (id){
                    param.id = id;
                }
                var _valid = $(this).form('enableValidation').form('validate')
                if(_valid) {
                    //图片必填
                    if($('#cult_picture1').val() == ""){
                        _valid = false;
                        $.messager.alert('提示', '请选择图片');
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


</script>

</body>
</html>
