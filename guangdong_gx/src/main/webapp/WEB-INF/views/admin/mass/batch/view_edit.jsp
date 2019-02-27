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
            <c:set var="pageTitle" value="专题届次-查看届次"></c:set>
        </c:when>
        <c:when test="${pageType eq 'edit'}">
            <c:set var="pageTitle" value="专题届次-编辑届次"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="专题届次-添加届次"></c:set>
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
        <div class="whgff-row-label"><i>*</i>所属单位：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:245px; height:32px" name="cultid" id="cultid" data-options="editable:false,required:true" />
            <input class="easyui-combobox" style="width:245px; height:32px" name="deptid" id="deptid" data-options="editable:false,required:true" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">所属场馆：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:400px; height:32px" name="venid" id="venid"
                   data-options="editable:false,required:false, prompt:'选择场馆'" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>文化专题：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" id="brandid" name="brandid" style="width:400px; height:32px"
                   value="${info.brandid}"
                   data-options="readonly:false,required:true, editable:true, limitToList: true, valueField:'id', textField:'title'"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>举办日期：</div>
        <div class="whgff-row-input">
            <input class="easyui-datetimebox" id="batdate" name="_batdate" style="width:200px; height:32px"
                   value="<fmt:formatDate value='${info.batdate}' pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>"
                   required="true" data-options="editable:false,prompt:'请选择'"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>主办地址：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:150px; height:32px" id="province" name="province" prompt="请选择省"
                data-options="readonly:true, required:true, limitToList:true, valueField:'name', textField:'name'<%--, data:__PROVINCE, onChange:changeProvince--%>"/>
            <input class="easyui-combobox" style="width:150px; height:32px" id="__CITY_ELE" name="city" prompt="请选择市"
                data-options="required:true, limitToList:true, valueField:'name', textField:'name'<%--, data:[], onChange:changeCity--%>"/>
            <input class="easyui-combobox" style="width:179px; height:32px" id="__AREA_ELE" name="area" prompt="请选择区"
                data-options="required:true, limitToList:true, valueField:'name', textField:'name', data:[]"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">举办单位：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="sponsor" value="${info.sponsor}" style="width:600px; height:32px"
                   data-options="required:false,validType:['length[1,100]'], prompt:'请输入内容'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">承办单位：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="organizer" value="${info.organizer}" style="width:600px; height:32px"
                   data-options="required:false,validType:['length[1,100]'], prompt:'请输入内容'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">协办单位：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="cosponsor" value="${info.cosponsor}" style="width:600px; height:32px"
                   data-options="required:false,validType:['length[1,200]'], prompt:'请输入内容'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">演出单位：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="deduce" value="${info.deduce}" style="width:600px; height:32px"
                   data-options="required:false,validType:['length[1,200]'], prompt:'请输入内容'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">简介：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="summary" value="${info.summary}" style="width:600px; height:150px" prompt="请输入内容，字数限制在500个字符内"
                   data-options="multiline:true, required:false, validType:['length[1,500]'] "/>
        </div>
    </div>
</form>



<div class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
</div>

<script>

    $(function(){
        formTool.init();

        showXjReason('${id}', '${info.state}', $("#whgff"));
    });


    var formTool = {
        pageType: '${pageType}',
        basePath: '${basePath}',
        modelPath: '/admin/mass/batch/',

        /** 入口*/
        init: function(){
            var pageType = this.pageType;

            this.initVal();

            var me = this;

            me.frm = $("#whgff");
            me.buttonDiv = $("div.whgff-but");

            if (me[pageType] && $.type(me[pageType])==='function'){
                me[pageType].call(me);
            }

            WhgComm.initPMS({
                basePath: me.basePath,
                cultEid:'cultid', cultValue:'${info.cultid}',
                deptEid:'deptid', deptValue:'${info.deptid}',
                cultOnChange: function(val, oval){
                    $("#brandid").combobox({
                        url : '${basePath}/admin/mass/brand/getBrands',
                        queryParams: {cultid: val},
                        validateOnCreate: false
                    });
                },
                venEid:'venid', venValue:'${info.venid}'

                ,provinceEid:'province', provinceValue:'${info.province}',
                cityEid:'__CITY_ELE', cityValue:'${info.city}',
                areaEid:'__AREA_ELE', areaValue:'${info.area}'
            });
        },

        initVal: function(){
            var me = this;

            me.imgurl = WhgUploadImg.init({
                basePath: me.basePath,
                uploadBtnId: 'imgUploadBtn1',
                hiddenFieldId: 'cult_picture1',
                previewImgId: 'previewImg1'
            });

            /*var province = '${info.province}';
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
            }*/
        },

        __validata: function (param) {
            var me = this;

            var picture1 = $("#cult_picture1").val();
            if (!picture1){
                $.messager.alert("错误", '图片不能为空', 'error');
                return false;
            }

            var cultid = $("#cultid").combobox("getValue");
            var deptid = $("#deptid").combobox("getValue");
            if (!cultid || $.trim(cultid) == '' || !deptid || $.trim(deptid)==''){
                $.messager.alert("错误", '请指定所属单位', 'error');
                return false;
            }

            return true;
        },

        /** 查看*/
        show : function(){
            var me = this;

            //取消表单验证
            me.frm.form("disableValidation");
            var undoBut = me.__appendButton("返 回", 'icon-undo', function(){ me.__closeForm(); });

            //组件设为只读
            me.frm.find(".whgff-row-input>:input[class*='easyui-']").each(function(){
                var _class = $(this).attr("class");
                _class = String(_class).match(/easyui-\w+/);
                if (!_class || _class.length ==0){
                    return true;
                }
                _class = _class[0].toLowerCase();
                var method = _class.replace("easyui-", "");
                if (method!=''){
                    var obj = $(this);
                    obj[method].call(obj, 'readonly');
                }
            });

            me.frm.find(".whgff-row-input-imgfile a").hide();

            //处理选项点击不生效
            me.frm.find("input[type='radio']").attr('disabled', true);
            $("div.radio").on("DOMNodeInserted", function(e){
                $(e.target).attr('disabled', true);
            });
            me.frm.on('click', "input[type='checkbox']", function(){return false});
        },

        /** 编辑*/
        edit : function () {
            var me = this;

            var okBut = me.__appendButton("提 交", 'icon-ok');
            var undoBut = me.__appendButton("返 回", 'icon-undo', function(){ me.__closeForm(); });

            var id = '${id}';
            me.__submitForm(okBut, me.basePath+me.modelPath+me.pageType, id);
        },

        /** 添加*/
        add : function() {
            var me = this;

            var okBut = me.__appendButton("提 交", 'icon-ok');
            var undoBut = me.__appendButton("返 回", 'icon-undo', function(){ me.__closeForm(); });

            me.__submitForm(okBut, me.basePath+me.modelPath+me.pageType);
        },

        /** 表单提交*/
        __submitForm: function (okBut, url, id) {
            var me = this;

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
                        if (id && id!=''){ param.id = id; }
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
                                me.__closeForm();
                            }
                        );
                    }else{
                        $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                    }
                }
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
        }

    }
</script>
</body>
</html>
