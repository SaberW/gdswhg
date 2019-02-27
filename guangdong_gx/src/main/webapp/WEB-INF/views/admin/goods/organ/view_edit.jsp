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
        <c:when test="${pageType eq 'show'}">
            <c:set var="pageTitle" value="特色团队管理-查看特色团队"></c:set>
        </c:when>
        <c:when test="${pageType eq 'edit'}">
            <c:set var="pageTitle" value="特色团队管理-编辑特色团队"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="特色团队管理-添加特色团队"></c:set>
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
</head>

<body class="body_add">
<form id="whgff" method="post" class="whgff">
    <h2>${pageTitle}</h2>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>组织机构名称：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="title" value="${info.title}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,100]'], prompt:'请输入组织机构名称'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>封面图片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture" name="image" value="${info.image}">
            <div class="whgff-row-input-imgview" id="previewImg"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>供需文化馆：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:245px; height:32px" name="cultid" id="cultid" data-options="required:true" />
            <input class="easyui-combobox" style="width:245px; height:32px" name="deptid" id="deptid" data-options="required:true" />
           <%-- <select class="easyui-combobox select-cultid" name="cultid" style="width:500px; height:32px" prompt="请选择文化馆"
                    data-options="editable:false, required:true, valueField:'id', textField:'text',
                    value:'${info.cultid}', data:WhgComm.getMgrCults()"></select>--%>
        </div>
    </div>

    <div class="whgff-row">
        <%--<div class="whgff-row-label"><i>*</i>机构组织机构或者法人证书：</div>--%>
        <div class="whgff-row-label">机构组织机构或者法人证书：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture1" name="certificate" value="${info.certificate}">
            <div class="whgff-row-input-imgview" id="previewImg1"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>领队：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="legalperson" value="${info.legalperson}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,100]'], prompt:'请输入机构法人'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>机构地址：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="address" value="${info.address}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,100]'], prompt:'请输入机构地址'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>负责人：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="contacts" value="${info.contacts}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,100]'], prompt:'请输入负责人'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>联系人：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="linkcontacts" value="${info.linkcontacts}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,100]'], prompt:'请输入联系人'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>联系方式：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="phone" value="${info.phone}" style="width:500px; height:32px" data-options="required:true,validType:'isPhone[\'phone\']',prompt:'请填写联系方式'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">所属单位：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="workplace" value="${info.workplace}" style="width:300px; height:32px"
                   data-options="required:false, prompt:'请输入所属单位'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">固定电话：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="telephone" value="${info.telephone}" style="width:300px; height:32px"
                   data-options="required:false,validType:'isTel', prompt:'请输入固定电话'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>组织机构简介：</div>
        <div class="whgff-row-input">
            <script id="organdesc" name="organdesc" type="text/plain" style="width:800px; height:200px;"></script>
            <textarea id="value_organdesc" style="display: none;">${info.organdesc}</textarea>
        </div>
    </div>

</form>

<div class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <a class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok">提 交</a>
    <a class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose();">返 回</a>
</div>

<script>
    //图片初始化
    var __WhgUploadImg = WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1'});

    var WhgUploadImg = WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn', hiddenFieldId: 'cult_picture', previewImgId: 'previewImg'});

    //处理UE
    var ueConfig = {
        scaleEnabled:true,
        autoFloatEnabled: false,
        elementPathEnabled:false,
        readonly: '${pageType}' == 'show'? true: false
    };
    var organdesc = UE.getEditor('organdesc', ueConfig);

    //UE 设置值
    organdesc.ready(function(){  organdesc.setContent('${info.organdesc}') });


    $(function () {
        WhgComm.initPMS({
            basePath:'${basePath}',
            deptEid:'deptid', deptValue:'${info.deptid}',
            cultEid:'cultid', cultValue:'${info.cultid}',
        });


        var id = '${id}';
        var pageType = '${pageType}';
        var frm = $("#whgff");
        var buts = $("div.whgff-but");

        //查看时的处理
        if (pageType == 'show'){
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
        var url = id ? "${basePath}/admin/showOrgan/edit" : "${basePath}/admin/showOrgan/add";
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
//                    if (!picture1){
//                        $.messager.alert("错误", '图片不能为空！', 'error');
//                        isValid = false;
//                    }
                    //组织机构简介不能为空
                    if (!organdesc.hasContents()){
                        $.messager.alert("错误", '组织机构简介不能为空！', 'error');
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
