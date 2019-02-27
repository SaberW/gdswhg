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
            <c:set var="pageTitle" value="数字展厅管理-查看数字展厅"></c:set>
        </c:when>
        <c:when test="${pageType eq '1'}">
            <c:set var="pageTitle" value="数字展厅管理-编辑数字展厅"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="数字展厅管理-添加数字展厅"></c:set>
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
    <script type="text/javascript"
            src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <!-- 图片上传相关-END -->

    <script type="text/javascript" src="${basePath}/static/common/js/area.js"></script>
    <script>
        function changePSProvince(newVal, oldVal) {
            var citys = [];
            if (newVal) {
                for (var i = 0; i < __CITY.length; i++) {
                    if (__CITY[i].proname == newVal) {
                        citys.push(__CITY[i]);
                    }
                }
            }
            $('#__PSCITY_ELE').combobox('loadData', citys);
            $('#__PSCITY_ELE').combobox('setValues', '');
        }
        ;
    </script>
</head>

<body class="body_add">
<form id="whgff" method="post" class="whgff">
    <h2>${pageTitle}</h2>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>名称：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="title" value="${info.title}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,100]'], prompt:'请输入数字展厅名称'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>图片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture1" name="imgurl" value="${info.imgurl}">
            <div class="whgff-row-input-imgview" id="previewImg1"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save"
                      id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>所属文化馆：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:245px; height:32px" name="cultid" id="cultid"
                   data-options="required:true"/>
            <input class="easyui-combobox" style="width:245px; height:32px" name="deptid" id="deptid"
                   data-options="editable:false,required:true"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>区域：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:150px; height:32px" id="province" name="province"
                   data-options="required:true,readonly:true,prompt:'请选择省', limitToList:true, valueField:'name', textField:'name', data:__PROVINCE, onChange:myChangeProvince"/>
            <input class="easyui-combobox" style="width:150px; height:32px" id="__CITY_ELE" name="city"
                   data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name', data:[], onChange:myChangeCity"/>
            <input class="easyui-combobox" style="width:179px; height:32px" id="__AREA_ELE" name="area"
                   data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>艺术分类：</div>
        <div class="whgff-row-input">
            <div class="checkbox checkbox-primary" id="arttype" name="arttype"></div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">主办方：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="host" value="${info.host}"
                   style="width: 500px; height: 32px" data-options="required:false,validType:['length[0,100]']"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>展厅简介：</div>
        <div class="whgff-row-input">
            <script id="remark" name="remark" type="text/plain" style="width:800px; height:200px;"></script>
            <textarea id="value_remark" style="display: none;">${info.remark}</textarea>
        </div>
    </div>
    <div class="whgff-row" style="display: none" id="showReason">
        <div class="whgff-row-label">
            下架原因：
        </div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" id="reason" readonly="true" value="" multiline="true"
                   style="width:550px;height: 150px;">
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
    function myChangeProvince(newVal, oldVal) {
        changeProvince(newVal, oldVal, function () {
            if (typeof(window.__init_city) == 'undefined') {
                if (!city || city == '') {
                    city = WhgComm.getCity() ? WhgComm.getCity() : "";
                }
                $('#__CITY_ELE').combobox('setValue', city);
                window.__init_city = true;
            }
        });
    }

    function myChangeCity(newVal, oldVal) {
        changeCity(newVal, oldVal, function () {
            if (typeof(window.__init_area) == 'undefined') {
                $('#__AREA_ELE').combobox('setValue', '${info.area}');
                window.__init_area = true;
            }
        });
    }  //省市区------END


    //图片初始化
    var __WhgUploadImg = WhgUploadImg.init({
        basePath: '${basePath}',
        uploadBtnId: 'imgUploadBtn1',
        hiddenFieldId: 'cult_picture1',
        previewImgId: 'previewImg1'
    });

    //处理UE
    var ueConfig = {
        scaleEnabled: true,
        autoFloatEnabled: false,
        elementPathEnabled: false,
        readonly: '${pageType}' == 2 ? true : false
    };
    var showdesc = UE.getEditor('remark', ueConfig);
    // var playbill = UE.getEditor('playbill', ueConfig);

    //UE 设置值
    showdesc.ready(function () {
        showdesc.setContent($("#value_remark").val())
    });
    // playbill.ready(function(){  playbill.setContent($("#value_playbill").val()) });


    $(function () {
        var state = '${info.state}';
        if (state && state == 4) {
            showXjReason('${info.id}');
        }
        WhgComm.initPMS({
            basePath: '${basePath}',
            cultEid: 'cultid', cultValue: '${info.cultid}',
            deptEid: 'deptid', deptValue: '${info.deptid}',
            ywiArtTypeEid: 'arttype', ywiArtTypeValue: '${info.arttype}'
            , psprovinceEid: '__PSPROVINCE_ELE', psprovinceValue: '${info.province}',
            pscityEid: '__PSCITY_ELE', pscityValue: '${info.city}'
        });
        if (!province || province == '') {
            province = WhgComm.getProvince() ? WhgComm.getProvince() : '广东省';
        }
        $('#province').combobox('setValue', province);

        var city = '${info.city}';
        if (!city || city == '') {
            city = WhgComm.getCity() ? WhgComm.getCity() : "";
        }
        $("#__CITY_ELE").combobox('setValue', city); //初始省值


        var id = '${id}';
        var pageType = '${pageType}';
        var frm = $("#whgff");
        var buts = $("div.whgff-but");

        //查看时的处理
        if (pageType == 2) {
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
            frm.find("input[type='checkbox'], input[type='radio']").on('click', function () {
                return false
            });

            //不显示提交 button
            buts.find("a.whgff-but-submit").hide();
            return;
        }


        //定义表单提交
        var url = id ? "${basePath}/admin/digitalexhibition/edit" : "${basePath}/admin/digitalexhibition/add";
        frm.form({
            url: url,
            novalidate: true,
            onSubmit: function (param) {
                if (id) {
                    param.id = id;
                }
                $(this).form("enableValidation");
                var isValid = $(this).form('validate');
                //if(isValid){
                //图片不能为空
                var picture1 = $("#cult_picture1").val();
                if (isValid && !picture1) {
                    $.messager.alert("错误", '图片不能为空！', 'error');
                    isValid = false;
                }
                var arttypes = $("[name='arttype']:checkbox:checked");
                if (isValid && (!arttypes || arttypes.size() == 0)) {
                    $.messager.alert("错误", '艺术分类不能为空！', 'error');
                    isValid = false;
                }
                if (isValid && !showdesc.hasContents()) {
                    $.messager.alert("错误", '展厅简介不能为空', 'error');
                    isValid = false;
                }
                //}
                if (!isValid) {
                    $.messager.progress('close');
                    buts.find("a.whgff-but-submit").off('click').one('click', function () {
                        frm.submit();
                    });
                }
                return isValid;

            },
            success: function (data) {
                data = $.parseJSON(data);
                $.messager.progress('close');
                buts.find("a.whgff-but-submit").off('click').one('click', function () {
                    frm.submit();
                });
                if (!data.success || data.success != "1") {
                    $.messager.alert("错误", data.errormsg || '操作失败', 'error');
                    return;
                }

                WhgComm.editDialogClose();
                window.parent.$('#whgdg').datagrid('reload');

            }
        });
        buts.find("a.whgff-but-submit").off('click').one('click', function () {
            frm.submit();
        });
    })
</script>

</body>
</html>
