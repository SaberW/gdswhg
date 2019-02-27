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
    <title>添加专项资金</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>

    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <!-- 图片上传相关-END -->
    <script type="text/javascript" src="${basePath}/static/plupload/upload-file.js"></script>
    <script type="text/javascript" src="${basePath}/static/common/js/area.js"></script>
</head>
<body>

<form id="whgff" class="whgff" method="post" >
    <h2>添加专项资金</h2>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>资金编号：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="fundnum" value="${fund.fundnum}"  style="width:500px; height:32px" data-options="required:true,prompt:'请输入内容', validType:'length[1,30]'"></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>拨付事项：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="matter" value="${fund.matter}"  style="width:500px; height:32px" data-options="required:true,prompt:'请输入内容', validType:'length[1,30]'"></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>拨付资金类别：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary" id="etype" name="etype"></div>
        </div>
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
        <div class="whgff-row-label"><label style="color: red">*</label>单位名称：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:245px; height:32px" name="cultid" id="cultid" data-options="required:true" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>补助金额：</div>
        <div class="whgff-row-input">
            <input class="easyui-numberspinner" name="money" value="${fund.money}" style="width:500px; height:32px" data-options="min:0,required:true,prompt:'请输入补助金额'">
        </div>
    </div>
    <div class="whgff-row train">
        <div class="whgff-row-label "><label style="color: red">*</label>拨付日期：</div>
        <div class="whgff-row-input">
            <input class="easyui-datebox " id="funddate" name="funddate" value="<fmt:formatDate value="${fund.funddate}" pattern="yyyy-MM-dd"/>" style="width:200px; height:32px" data-options="editable:false,required:true" />
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i></i>备注：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="remark" value="${fund.remark}" style="width: 500px; height: 100px" data-options="required:true,multiline:true,validType:['length[1,512]']"/>
        </div>
    </div>
    <div class="whgff-row" >
        <div class="whgff-row-label">上传附件：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="whg_file_upload" name="ratifydoc" value="${fund.ratifydoc}">
            <div class="whgff-row-input-fileview" id="whg_file_pload_view"></div>
            <div class="whgff-row-input-filefile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="fileUploadBtn2">选择附件</a></i>
                <i>附件格式为doc,docx,xls,xlsx,zip,pdf,建议文件大小为10MB以内</i>
            </div>
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
    var province = '${fund.province}';
    var city = '${fund.city}';
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
            window.setTimeout(function () {$('#__AREA_ELE').combobox('setValue', '${fund.area}')}, 500);
            __init_area = false;
        }
    }  //省市区------END




    $(function () {
        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'${fund.cultid}',
            ywiTypeEid:'etype', ywiTypeValue:'${fund.etype}', ywiTypeClass:5
        });

        //初始省值
        if (!province || province==''){
            province = WhgComm.getProvince()?WhgComm.getProvince():'广东省';
        }
        $('#province').combobox('setValue', province);
        var city = '${fund.city}';
        if (!city || city==''){
            city = WhgComm.getCity()?WhgComm.getCity():"";
        }
        $("#__CITY_ELE").combobox('setValue', city); //初始省值


        <!--文件上传控件 -->
        WhgUploadFile.init({basePath: '${basePath}', uploadBtnId: 'fileUploadBtn2', hiddenFieldId: 'whg_file_upload',previewFileId:'whg_file_pload_view'});

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
        var url = id ? "${basePath}/admin/fyi/fund/edit" : "${basePath}/admin/fyi/fund/add";

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
