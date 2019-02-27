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
            <c:set var="pageTitle" value="众筹品牌管理-查看众筹品牌"></c:set>
        </c:when>
        <c:when test="${pageType eq 'edit'}">
            <c:set var="pageTitle" value="众筹品牌管理-编辑众筹品牌"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="众筹品牌管理-添加众筹品牌"></c:set>
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
        <div class="whgff-row-label"><i>*</i>名称：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="title" value="${info.title}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,100]'], prompt:'请输入名称'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>简称：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="titleshort" value="${info.titleshort}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,100]'], prompt:'请输入内容'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>图片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture1" name="imgurl" value="${info.imgurl}">
            <div class="whgff-row-input-imgview" id="previewImg1"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>背景图片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture2" name="bgimgurl" value="${info.bgimgurl}">
            <div class="whgff-row-input-imgview" id="previewImg2"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn2">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 1920*600，大小为2MB以内</i>
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>背景图片平铺：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="isplanbg" value="${info.isplanbg}"
                 js-data='[{"id":"1","text":"是"},{"id":"0","text":"否"}]'>
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>品牌背景颜色：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="bgcolor" value="${info.bgcolor}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,100]'], prompt:'请输入内容'">
            (格式: #ffffff)
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>所属单位：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:500px; height:32px" name="cultid" id="cultid" data-options="editable:false,required:true" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>区域：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:150px; height:32px" id="province" name="province" data-options="readonly:true, required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name', data:__PROVINCE, onChange:changeProvince"/>
            <input class="easyui-combobox" style="width:150px; height:32px" id="__CITY_ELE" name="city" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name', data:[], onChange:changeCity"/>
            <input class="easyui-combobox" style="width:179px; height:32px" id="__AREA_ELE" name="area" data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>众筹品牌介绍：</div>
        <div class="whgff-row-input">
            <div id="description" name="_description" type="text/plain" style="width:800px; height:200px;"></div>
            <textarea id="value_description" style="display: none;">${info.description}</textarea>
        </div>
    </div>
</form>



<div class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
</div>

<script>

    $(function(){
        formTool.init();
    });


    var formTool = {
        /** 入口*/
        init: function(){
            var pageType = this.getPageType();

            this.__whgUEInit();
            this.__whgImgInit();

            this.initVal();

            var me = this;

            me.frm = $("#whgff");
            me.buttonDiv = $("div.whgff-but");

            if (me[pageType] && $.type(me[pageType])==='function'){
                me[pageType].call(me);
            }

            WhgComm.initPMS({
                basePath:'${basePath}',
                cultEid:'cultid', cultValue:'${info.cultid}'
            });
        },

        /** 页面类型*/
        getPageType: function(){
            return '${pageType}';
        },

        initVal: function(){

            var province = '${info.province}';
            if (!province || province==''){
                province = WhgComm.getProvince()?WhgComm.getProvince():'广东省';
            }
            $('#province').combobox('setValue', province);

            var city = '${info.city}';
            if (!city || city==''){
                city = WhgComm.getCity()?WhgComm.getCity():"";
            }
            $("#__CITY_ELE").combobox('setValue', city);
            var area = '${info.area}';
            if (area && area!=''){
                $("#__AREA_ELE").combobox("setValue", area);
            }
        },

        /** 查看*/
        show : function(){
            var me = this;

            //取消表单验证
            me.frm.form("disableValidation");
            var undoBut = me.__appendButton("返 回", 'icon-undo', function(){ me.__closeForm(); });

            //组件设为只读
            $('.easyui-textbox').textbox('readonly');
            $('.easyui-combobox').combobox('readonly');
            $('.easyui-datebox').combobox('readonly');
            //处理选项点击不生效
            me.frm.find("input[type='checkbox']").on('click', function(){return false});
            me.frm.find("input[type='radio']").attr('disabled', true);
            $("#imgUploadBtn1").hide();
            $("#imgUploadBtn2").hide();
        },

        /** 编辑*/
        edit : function () {
            var me = this;

            var okBut = me.__appendButton("提 交", 'icon-ok');
            var undoBut = me.__appendButton("返 回", 'icon-undo', function(){ me.__closeForm(); });

            var id = '${id}';
            me.__submitForm(okBut, '${basePath}/admin/brand/edit', id);
        },

        /** 添加*/
        add : function() {
            var me = this;

            var okBut = me.__appendButton("提 交", 'icon-ok');
            //var noBut = me.__appendButton("清 空", 'icon-no', function(){ me.__clearForm(); });
            var undoBut = me.__appendButton("返 回", 'icon-undo', function(){ me.__closeForm(); });

            me.__submitForm(okBut, '${basePath}/admin/brand/add');
        },

        /** 表单提交*/
        __submitForm: function (okBut, url, id) {
            var me = this;
            var pageType = me.getPageType();

            function oneSubmit(){
                okBut.off("click").one("click", function(){
                    $.messager.progress();
                    me.frm.submit();
                })
            }
            oneSubmit();

            me.frm.form({
                url: url,
                novalidate: true,
                onSubmit: function (param) {
                    $(this).form("enableValidation");
                    var isValid = $(this).form('validate');

                    if (isValid){
                        isValid = me.__validata(param);
                    }

                    if (!isValid){
                        $.messager.progress('close');
                        oneSubmit();
                    }else{
                        if (id){ param.id = id; }
                    }
                    return isValid;
                },
                success: function(data){
                    $.messager.progress('close');
                    oneSubmit();
                    data = $.parseJSON(data);
                    if (data.success && data.success=="1"){
                        $.messager.alert("提示", '信息提交成功', 'info',
                                function(){
//                                    if (pageType=='edit'){
                                        me.__closeForm();
//                                    }else{
//                                        me.__clearForm();
//                                    }
                                }
                        );
                    }else{
                        $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                    }
                }
            })
        },

        __validata: function (param) {
            var me = this;

            var picture1 = $("#cult_picture1").val();
            var picture2 = $("#cult_picture2").val();
            if (!picture1 || !picture2){
                $.messager.alert("错误", '图片不能为空', 'error');
                return false;
            }

            if (!me.description.hasContents()){
                $.messager.alert("错误", '众筹品牌介绍不能为空', 'error');
                return false;
            }else{
                param.description = me.description.getContent();
            }

            return true;
        },

        /** 初始UE 组件*/
        __whgUEInit: function () {
            var pageType = this.getPageType();
            var ueConfig = {
                scaleEnabled: false,
                autoFloatEnabled: false,
                elementPathEnabled:false,
                readonly: pageType=='show'
            };
            var ue_description = UE.getEditor('description', ueConfig);
            ue_description.ready(function(){  ue_description.setContent($("#value_description").val()) });

            this.description = ue_description;
        },

        __whgImgInit: function(){
            this.imgurl = WhgUploadImg.init({
                basePath: '${basePath}',
                uploadBtnId: 'imgUploadBtn1',
                hiddenFieldId: 'cult_picture1',
                previewImgId: 'previewImg1'
            });
            this.bgimgurl = WhgUploadImg.init({
                basePath: '${basePath}',
                uploadBtnId: 'imgUploadBtn2',
                hiddenFieldId: 'cult_picture2',
                previewImgId: 'previewImg2',
                needCut:false,
                cutWidth: 1920,
                cutHeight: 600
            })
        },

        /** 插入按钮*/
        __appendButton: function (text, iconCls, onClick) {
            var button = $('<a style="margin-right: 5px"></a>');
            this.buttonDiv.append(button);

            var cfg = {};
            cfg.text = text;
            if (iconCls) cfg.iconCls = iconCls;
            if (onClick) cfg.onClick = onClick;

            button.linkbutton( cfg );
            return button;
        },

        /** 关闭返回*/
        __closeForm: function () {
            window.parent.whgListTool.reload();
            WhgComm.editDialogClose();
        },

        /** 表单清空*/
        __clearForm: function () {
            var me = this;

            me.frm.form("disableValidation");
            me.frm.form('clear');
            me.imgurl.clear();
            me.bgimgurl.clear();

            //第一个单选又点上
            me.frm.find("div.radio").find(':radio:eq(0)').click();
            me.description.setContent('');
        }
    }
</script>

</body>
</html>
