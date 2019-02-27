<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">

    <c:choose>
        <c:when test="${pageType eq '2'}">
            <c:set var="pageTitle" value="展览类商品管理-查看商品"></c:set>
        </c:when>
        <c:when test="${pageType eq '1'}">
            <c:set var="pageTitle" value="展览类商品管理-编辑商品"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="展览类商品管理-添加商品"></c:set>
        </c:otherwise>
    </c:choose>
    <title>${pageTitle}</title>

    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>

    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>

    <script src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>

    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>

    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <!-- 图片上传相关-END -->

    <script type="text/javascript" src="${basePath}/static/common/js/area.js"></script>
</head>

<body class="body_add">
<form id="whgff" method="post" class="whgff">
    <h2>${pageTitle}</h2>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>商品名称：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="title" value="${info.title}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,100]'], prompt:'请输入商品名称'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>图片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture1" name="image" value="${info.image}">
            <div class="whgff-row-input-imgview" id="previewImg1"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>所属文化馆：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:245px; height:32px" name="cultid" id="cultid" data-options="required:true" />
            <%--<select class="easyui-combobox select-cultid" name="cultid" style="width:500px; height:32px" prompt="请选择文化馆"
                    data-options="editable:false, required:true, valueField:'id', textField:'text',
                    value:'${info.cultid}', data:WhgComm.getMgrCults()"></select>--%>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>区域：</div>
        <div class="whgff-row-input">
            <%--<div class="radio radio-primary whg-js-data" name="area" value="${info.area}"
                 js-data="WhgComm.getAreaType">
            </div>--%>
                <input class="easyui-combobox" style="width:150px; height:32px" id="province" name="province" data-options="required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name', data:__PROVINCE, onChange:myChangeProvince"/>
                <input class="easyui-combobox" style="width:150px; height:32px" id="__CITY_ELE" name="city" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name', data:[], onChange:myChangeCity"/>
                <input class="easyui-combobox" style="width:179px; height:32px" id="__AREA_ELE" name="area" data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>展览类型：</div>
        <div class="whgff-row-input">
            <div class="checkbox checkbox-primary" id="etype" name="etype"></div>
            <%--<div class="radio radio-primary whg-js-data" name="etype" value="${info.etype}"
                 js-data="WhgComm.getExhType">--%>
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>展览时长：</div>
        <div class="whgff-row-input">
            <input class="easyui-numberbox" name="exhtime" value="${info.exhtime}" style="width:300px; height:32px"
                   data-options="required:true,min:0,max:1000000, prompt:'请输入展览时长'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>展览幅数：</div>
        <div class="whgff-row-input">
            <input class="easyui-numberbox" name="exhnum" value="${info.exhnum}" style="width:300px; height:32px"
                   data-options="required:true,min:0,max:1000000, prompt:'请输入展览幅数'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>展品：</div>
        <div class="whgff-row-input">
            <select class="easyui-combobox select-cultid" id="exhgoods" name="exhgoods" style="width:500px; height:32px" prompt="请选择展品"
                    data-options=" multiple:true,editable:false, required:true,value:'${info.exhgoods}'
                    "></select>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>展览机构：</div>
        <div class="whgff-row-input">
            <select class="easyui-combobox select-cultid" id="exhorgan" name="exhorgan" value="${info.exhorgan}" style="width:500px; height:32px" prompt="请选择所属机构"
                    data-options="editable:false, required:true,value:'${info.exhorgan}'"></select>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>联系人：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="exhcontacts" value="${info.exhcontacts}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,100]'], prompt:'请输入联系人'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>联系方式：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="exhphone" value="${info.exhphone}" style="width:500px; height:32px" data-options="validType:'isPhone[\'exhphone\']',prompt:'请填写联系方式'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>是否收费：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="ismoney" value="${info.ismoney}"
                 js-data='[{"id":"0","text":"不收费"},{"id":"1","text":"收费"}]'>
            </div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>展览简介：</div>
        <div class="whgff-row-input">
            <script id="exhdesc" name="exhdesc" type="text/plain" style="width:800px; height:200px;"></script>
            <textarea id="value_exhdesc" style="display: none;">${info.exhdesc}</textarea>
        </div>
    </div>

</form>

<div class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <a class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok">提 交</a>
    <a class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose();">返 回</a>
</div>

<script>
    var province = '${info.province}';
    var city = '${info.city}';
    //省市区
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
            window.setTimeout(function () {$('#__AREA_ELE').combobox('setValue', '${info.area}')}, 500);
            __init_area = false;
        }
    }  //省市区------END

    //图片初始化
    var __WhgUploadImg = WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1'});

    //处理UE
    var ueConfig = {
        scaleEnabled:true,
        autoFloatEnabled: false,
        elementPathEnabled:false,
        readonly: '${pageType}' == 2? true: false
    };
    var exhdesc = UE.getEditor('exhdesc', ueConfig);

    //UE 设置值
    exhdesc.ready(function(){  exhdesc.setContent('${info.exhdesc}') });


    $(function () {
        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'${info.cultid}', cultOnChange: function (newVal, oldVal) {
                $("#exhgoods").combobox({
                    url:'${basePath}/admin/exhGoods/srchList?cultid='+newVal,
                    valueField:'id',
                    textField:'title'
                })
                $("#exhorgan").combobox({
                    url:'${basePath}/admin/showOrgan/srchList?cultid='+newVal,
                    valueField:'id',
                    textField:'title'
                })
            },
            ywiTypeEid:'etype', ywiTypeValue:'${info.etype}', ywiTypeClass:23,
        });

        //初始省值
        //$('#province').combobox('setValue', '${info.province}');

        if (!province || province==''){
            province = WhgComm.getProvince()?WhgComm.getProvince():'广东省';
        }
        $('#province').combobox('setValue', province);

        var city = '${info.city}';
        if (!city || city==''){
            city = WhgComm.getCity()?WhgComm.getCity():"";
        }
        $("#__CITY_ELE").combobox('setValue', city); //初始省值

        var id = '${id}';
        var pageType = '${pageType}';
        var frm = $("#whgff");
        var buts = $("div.whgff-but");

        //查看时的处理
        if (pageType == 2){
            //取消表单验证
            frm.form("disableValidation");

            //组件设为只读
            $('.easyui-textbox').textbox('readonly');
            $('.easyui-combobox').combobox('readonly');
            $('.easyui-datebox').combobox('readonly');
            $('.easyui-numberspinner').numberspinner('readonly');
            $('.easyui-datetimebox').combobox('readonly');
            $("#getXYPointBtn").hide();
            $("#imgUploadBtn1").hide();
            //处理选项点击不生效
            frm.find("input[type='checkbox'], input[type='radio']").on('click', function(){return false});

            //不显示提交 button
            buts.find("a.whgff-but-submit").hide();
            return;
        }


        //定义表单提交
        var url = id ? "${basePath}/admin/showExh/edit" : "${basePath}/admin/showExh/add";
        frm.form({
            url: url,
            novalidate: true,
            onSubmit: function (param) {
                if (id){
                    param.id = id;
                }
                $(this).form("enableValidation");
                var isValid = $(this).form('validate');
                if(isValid){
                    //图片不能为空
                    var picture1 = $("#cult_picture1").val();
                    if (!picture1){
                        $.messager.alert("错误", '图片不能为空！', 'error');
                        isValid = false;
                    }
                }
                if (!isValid){
                    $.messager.progress('close');
                    buts.find("a.whgff-but-submit").off('click').one('click', function () {
                        frm.submit();
                    });
                }
                return isValid;

            },
            success : function (data) {
                data = $.parseJSON(data);
                $.messager.progress('close');
                buts.find("a.whgff-but-submit").off('click').one('click', function () {
                    frm.submit();
                });
                if (!data.success || data.success != "1"){
                    $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                    return;
                }
                if (!id){
                    $(this).form("disableValidation");
                    buts.find("a.whgff-but-clear").click();
                    $.messager.show({
                        title:'提示消息',
                        msg:'信息提交成功',
                        showType:'slide',
                        timeout:1000,
                        width: 300,
                        height: 200
                    });
                    window.parent.$('#whgdg').datagrid('reload');
                }else{
                    WhgComm.editDialogClose();
                    window.parent.$('#whgdg').datagrid('reload');
                }
            }
        });
        buts.find("a.whgff-but-submit").off('click').one('click', function () {
            frm.submit();
        });
    })



</script>

</body>
</html>
