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
            <c:set var="pageTitle" value="展品管理-查看展品"></c:set>
        </c:when>
        <c:when test="${pageType eq '1'}">
            <c:set var="pageTitle" value="展品管理-编辑展品"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="展品管理-添加展品"></c:set>
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
        <div class="whgff-row-label"><i>*</i>展品名称：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="title" value="${info.title}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,100]'], prompt:'请输入展品名称'">
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
           <%-- <select class="easyui-combobox select-cultid" name="cultid" style="width:500px; height:32px" prompt="请选择文化馆"
                    data-options="editable:false, required:true, valueField:'id', textField:'text',
                    value:'${info.cultid}', data:WhgComm.getMgrCults()"></select>--%>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>展品类型：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary" id="etype" name="etype"></div>
            <%--<div class="radio radio-primary whg-js-data" name="etype" value="${info.etype}"
                 js-data="WhgComm.getExhGoodsType">
            </div>--%>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">作者：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="author" value="${info.author}" style="width:600px; height:32px"
                   data-options="validType:['length[1,100]'], prompt:'请输入作者'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">年代：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="years" value="${info.years}" style="width:600px; height:32px"
                   data-options="validType:['length[1,100]'], prompt:'请输入年代'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">材质：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="material" value="${info.material}" style="width:600px; height:32px"
                   data-options="validType:['length[1,100]'], prompt:'请输入材质'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">尺寸：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="size" value="${info.size}" style="width:600px; height:32px"
                   data-options="validType:['length[1,100]'], prompt:'请输入尺寸'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">价值：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="cost" value="${info.cost}" style="width:600px; height:32px"
                   data-options="validType:['length[1,100]'], prompt:'请输入价值'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">数量：</div>
        <div class="whgff-row-input">
            <input class="easyui-numberbox" name="number" value="${info.number}" style="width:300px; height:32px"
                   data-options="min:0,max:1000000, prompt:'请输入展品数量'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>展品简介：</div>
        <div class="whgff-row-input">
            <script id="cowrydesc" name="cowrydesc" type="text/plain" style="width:800px; height:200px;"></script>
            <textarea id="value_cowrydesc" style="display: none;">${info.cowrydesc}</textarea>
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

    //处理UE
    var ueConfig = {
        scaleEnabled:true,
        autoFloatEnabled: false,
        elementPathEnabled:false,
        readonly: '${pageType}' == 2? true: false
    };
    var cowrydesc = UE.getEditor('cowrydesc', ueConfig);

    //UE 设置值
    cowrydesc.ready(function(){  cowrydesc.setContent('${info.cowrydesc}') });


    $(function () {
        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'${info.cultid}',
            ywiTypeEid:'etype', ywiTypeValue:'${info.etype}', ywiTypeClass:22,
        });


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
        var url = id ? "${basePath}/admin/exhGoods/edit" : "${basePath}/admin/exhGoods/add";
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
