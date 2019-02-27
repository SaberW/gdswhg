<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
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
            <c:set var="pageTitle" value="场馆管理-查看场馆"></c:set>
        </c:when>
        <c:when test="${pageType eq 'edit'}">
            <c:set var="pageTitle" value="场馆管理-编辑场馆"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="场馆管理-添加场馆"></c:set>
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

    <!-- 根据地址取坐标 -->
    <script type="text/javascript" src="${basePath}/static/common/js/whg.maps.js"></script>
    <script type="text/javascript" src="${basePath}/static/common/js/area.js"></script>

</head>
<body class="body_add">
<form id="whgff" method="post" class="whgff">

    <h2>${pageTitle}</h2>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>名称：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="title" value="${info.title}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,60]'], prompt:'请输入场馆名称'">
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
        <div class="whgff-row-label">建馆时间：</div>
        <div class="whgff-row-input">
            <input type="text" class="easyui-datebox" style="width:300px;height: 32px;" name="datebuild_str"
                   value="<fmt:formatDate value='${info.datebuild}' pattern="yyyy-MM-dd"></fmt:formatDate>" data-options="editable:false,prompt:'请选择'"/>
        </div>
    </div>

    <%--<div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>所属文化馆：</div>
        <div class="whgff-row-input">
            <select class="easyui-combobox select-cultid" name="cultid" style="width:500px; height:32px" prompt="请选择文化馆"
                    data-options="editable:false, required:true, valueField:'id', textField:'text',
                    value:'${info.cultid}', data:WhgComm.getMgrCults(),
                    onSelect: selectCult"></select>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">所属部门：</div>
        <div class="whgff-row-input">
            <select class="easyui-combobox select-deptid" name="deptid" style="width:500px; height:32px" prompt="请选择部门"
                    data-options="editable:false, required:false, valueField:'id', textField:'text',
                    value:'${info.deptid}'"></select>
        </div>
    </div>--%>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>所属单位：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:245px; height:32px" name="cultid" id="cultid" data-options="editable:false,required:true" />
            <input class="easyui-combobox" style="width:245px; height:32px" name="deptid" id="deptid" data-options="required:true" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>场馆分类：</div>
        <div class="whgff-row-input">
            <%--<div class="radio radio-primary whg-js-data"
                 name="etype" value="${info.etype}" js-data="WhgComm.getVenueType">
            </div>--%>
            <div class="radio radio-primary" id="etype" name="etype"></div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">场馆标签：</div>
        <div class="whgff-row-input">
            <%--<div class="checkbox checkbox-primary whg-js-data"
                 name="etag" value="${info.etag}" js-data="WhgComm.getVenueTag">
            </div>--%>
            <div class="checkbox checkbox-primary" id="etag" name="etag"></div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">场馆关键字：</div>
        <div class="whgff-row-input">
            <%--<select id="ekey" class="easyui-combobox" style="width:600px; height:32px" validType="notQuotes"
                   data-options="multiple:true, valueField:'text', textField:'text',  data:WhgComm.getVenueKey()" ></select>
            (多于2个关键字之间用半角逗号分隔)--%>
            <input class="easyui-combobox" id="ekey" style="width:500px; height:32px" validType="notQuotes"
                   data-options="multiple:true, editable:true, prompt:'请填写关键字'
                   ,onChange: function (val, oldval) {
                        if (val.length>1 && val[0]==''){
                            val.shift();
                            $(this).combobox('setValues', val);
                        }
                    }"/>
            <span>（如需手动输入，请用英文逗号隔开！）</span>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>区域：</div>
        <div class="whgff-row-input">
            <%--<div class="radio radio-primary whg-js-data" name="area" value="${info.area}"
                 js-data="WhgComm.getAreaType">
            </div>--%>
            <input class="easyui-combobox" style="width:150px; height:32px" id="province" name="province" data-options="readonly:true, required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name', data:__PROVINCE, onChange:changeProvince"/>
            <input class="easyui-combobox" style="width:150px; height:32px" id="__CITY_ELE" name="city" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name', data:[], onChange:changeCity"/>
            <input class="easyui-combobox" style="width:179px; height:32px" id="__AREA_ELE" name="area" data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>地址：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" id="address" name="address" value="${info.address}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,60]'], prompt:'请输入详细地址'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>坐标：</div>
        <div class="whgff-row-input">
            <input class="easyui-numberbox" id="longitude" name="longitude" value="${info.longitude}"
                   style="width:100px; height:32px" data-options="required:true, precision:6,readonly:true,prompt:'X轴'">
            -
            <input class="easyui-numberbox" id="latitude" name="latitude" value="${info.latitude}" style="width:100px; height:32px"
                   data-options="required:true, precision:6,readonly:true,prompt:'Y轴'">
            <a class="easyui-linkbutton" id="getXYPointBtn" text="选择坐标"></a>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>联系人：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="contacts" value="${info.contacts}" style="width:300px; height:32px"
                   data-options="required:true,validType:['length[1,20]'], prompt:'请输入联系人姓名'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>联系电话：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="phone" value="${info.phone}" style="width:300px; height:32px"
                   data-options="required:true,validType:['length[1,20]', 'isPhone'], prompt:'请输入联系人电话号码'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>场馆简介：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="summary" value="${info.summary}" multiline="true" style="width:600px;height: 100px;"
                   data-options="required:true,validType:['length[1,400]']">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>场馆描述：</div>
        <div class="whgff-row-input">
            <div id="description" name="_description" type="text/plain" style="width:600px; height:200px;"></div>
            <textarea id="value_description" style="display: none;">${info.description}</textarea>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">场馆设施描述：</div>
        <div class="whgff-row-input">
            <div id="facilitydesc" name="_facilitydesc" type="text/plain" style="width:600px; height:200px;"></div>
            <textarea id="value_facilitydesc" style="display: none;">${info.facilitydesc}</textarea>
        </div>
    </div>
</form>

<div class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <%--<a class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok">提 交</a>
    <a class="easyui-linkbutton whgff-but-clear" iconCls="icon-no">清 空</a>--%>
</div>

<script>


    /*function selectCult(record) {
        var cultid = record.id;
        var data = WhgComm.getChildDept(cultid);
        $(".select-deptid").combobox({data: data});
    }*/

    $(function(){
        formTool.init();
    });


    var formTool = {
        /** 入口*/
        init: function () {
            var pageType = this.getPageType();

            this.__whgUEInit();
            this.__whgImgInit();
            this.__whgMapInit();

            this.initVal();

            var me = this;

            me.frm = $("#whgff");
            me.buttonDiv = $("div.whgff-but");

            if (me[pageType] && $.type(me[pageType])==='function'){
                me[pageType].call(me);
            }

            WhgComm.initPMS({
                basePath:'${basePath}',
                cultEid:'cultid', cultValue:'${info.cultid}',
                deptEid:'deptid', deptValue:'${info.deptid}',
                ywiTypeEid:'etype', ywiTypeValue:'${info.etype}', ywiTypeClass:2,
                ywiKeyEid:'ekey', ywiKeyValue:'${info.ekey}', ywiKeyClass:2,
                ywiTagEid:'etag', ywiTagValue:'${info.etag}', ywiTagClass:2
            });
        },

        /** 页面类型*/
        getPageType: function(){
            return '${pageType}';
        },

        initVal: function() {
            var ekey = "${info.ekey}";
            if (ekey && ekey!='') {
                $("#ekey").combobox('setValue', ekey);
            };

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
            //me.frm.find("input[type='checkbox']").on('click', function(){return false});
            //me.frm.find("input[type='radio']").attr('disabled', true);
            $("div.radio").on("DOMNodeInserted", function(e){
                $(e.target).attr('disabled', true);
            });
            me.frm.on('click', "input[type='checkbox']", function(){return false});

            $("#getXYPointBtn").hide();
            $("#imgUploadBtn1").hide();
        },

        /** 编辑*/
        edit : function () {
            var me = this;

            var okBut = me.__appendButton("提 交", 'icon-ok');
            var undoBut = me.__appendButton("返 回", 'icon-undo', function(){ me.__closeForm(); });

            var id = '${id}';
            me.__submitForm(okBut, '${basePath}/admin/venue/edit', id);
        },

        /** 添加*/
        add : function() {
            var me = this;

            var okBut = me.__appendButton("提 交", 'icon-ok');
            //var noBut = me.__appendButton("清 空", 'icon-no', function(){ me.__clearForm(); });
            var undoBut = me.__appendButton("返 回", 'icon-undo', function(){ me.__closeForm(); });

            me.__submitForm(okBut, '${basePath}/admin/venue/add');
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
                        param.ekey = $("#ekey").combobox('getText');
                    }
                    return isValid;
                },
                success: function(data){
                    $.messager.progress('close');
                    oneSubmit();
                    data = $.parseJSON(data);
                    if (data.success && data.success=="1"){
                        $.messager.alert("提示", '场馆信息提交成功', 'info',
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
            if (!picture1){
                $.messager.alert("错误", '图片不能为空', 'error');
                return false;
            }

            var etype = $("[name='etype']:radio:checked").val();
            if (!etype){
                $.messager.alert("错误", '场馆分类不能为空', 'error');
                return false;
            }

            /*var area = $("[name='area']:radio").val();
            if (!etype){
                $.messager.alert("错误", '区域不能为空', 'error');
                return false;
            }*/

            if (!me.ue_description.hasContents()){
                $.messager.alert("错误", '场馆描述不能为空', 'error');
                return false;
            }else {
                param.description = me.ue_description.getContent();
            }

            if (me.ue_facilitydesc.hasContents()){
                param.facilitydesc = me.ue_facilitydesc.getContent();
            }else {
                param.facilitydesc = '';
            }

            return true;
        },

        /** 表单清空*/
        __clearForm: function () {
            var me = this;

            me.frm.form("disableValidation");
            me.frm.form('clear');
            me.whgImg.clear();

            //第一个单选又点上
            me.frm.find("div.radio").find(':radio:eq(0)').click();
            me.ue_description.setContent('');
            me.ue_facilitydesc.setContent('');
        },

        /** 关闭返回*/
        __closeForm: function () {
            window.parent.whgListTool.reload();
            WhgComm.editDialogClose();
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

        /** 初始坐标组件*/
        __whgMapInit: function () {
            WhgMap.init({
                basePath:'${basePath}',
                addrFieldId:'address',
                xpointFieldId:'longitude',
                ypointFieldId:'latitude',
                getPointBtnId:'getXYPointBtn'
            });
        },

        __whgImgInit: function(){
            this.whgImg = WhgUploadImg.init({
                basePath: '${basePath}',
                uploadBtnId: 'imgUploadBtn1',
                hiddenFieldId: 'cult_picture1',
                previewImgId: 'previewImg1'
            });
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
            var ue_facilitydesc = UE.getEditor('facilitydesc', ueConfig);
            ue_description.ready(function(){  ue_description.setContent($("#value_description").val()) });
            ue_facilitydesc.ready(function(){  ue_facilitydesc.setContent($("#value_facilitydesc").val()) });

            this.ue_description = ue_description;
            this.ue_facilitydesc = ue_facilitydesc;
        }
    };

</script>

</body>
</html>
