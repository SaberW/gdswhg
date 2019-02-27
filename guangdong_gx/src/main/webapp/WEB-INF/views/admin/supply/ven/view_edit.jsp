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
            <c:set var="pageTitle" value="供需场馆管理-查看"></c:set>
        </c:when>
        <c:when test="${pageType eq 'edit'}">
            <c:set var="pageTitle" value="供需场馆管理-编辑"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="供需场馆管理-添加"></c:set>
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
        <div class="whgff-row-label"><i>*</i>场馆名称：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="title" value="${info.title}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,60]'], prompt:'请输入场馆名称'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>场馆图片：</div>
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
        <div class="whgff-row-label"><label style="color: red">*</label>供需文化馆：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:245px; height:32px" name="cultid" id="cultid" data-options="editable:false,required:true" />
            <input class="easyui-combobox" style="width:245px; height:32px" name="deptid" id="deptid" data-options="editable:false,required:true" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>类型：</div>
        <div class="whgff-row-input">
            <div class="checkbox checkbox-primary" id="etype" name="etype"></div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">标签：</div>
        <div class="whgff-row-input">
            <div class="checkbox checkbox-primary" id="etag" name="etag"></div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">关键字：</div>
        <div class="whgff-row-input">
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
        <div class="whgff-row-label"><i>*</i>所属区域：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:150px; height:32px" id="province" name="province" data-options="readonly:true, required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name'<%--, data:__PROVINCE, onChange:changeProvince--%>"/>
            <input class="easyui-combobox" style="width:150px; height:32px" id="__CITY_ELE" name="city" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name'<%--, data:[], onChange:changeCity--%>"/>
            <input class="easyui-combobox" style="width:179px; height:32px" id="__AREA_ELE" name="area" data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name'<%--, data:[]--%>"/>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>场馆地址：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" id="address" name="address" value="${info.address}" style="width:500px; height:32px"
                   data-options="required:true,validType:['length[1,200]'], prompt:'请输入详细地址'">
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
        <div class="whgff-row-label"><i>*</i>手机：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="phone" value="${info.phone}" style="width:300px; height:32px"
                   data-options="required:true,validType:['length[1,20]', 'isPhone'], prompt:'请输入联系人电话号码'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">邮箱：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="email" value="${info.email}" style="width:300px; height:32px"
                   data-options="required:false,validType:'email', prompt:'请输入联系人邮箱'">
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
        <div class="whgff-row-label"><i>*</i>场馆简介：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox textarea-value" name="summary" multiline="true" style="width:600px;height: 300px;"
                   data-options="required:true,validType:['length[1,1000]']">
            <textarea class="textarea-value" style="display: none">${info.summary}</textarea>
        </div>
    </div>

</form>

<div class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <%--<a class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok">提 交</a>
    <a class="easyui-linkbutton whgff-but-clear" iconCls="icon-no">清 空</a>--%>
</div>

<script>


    $(function(){
        formTool.init();

        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'${info.cultid}',
            deptEid:'deptid', deptValue:'${info.deptid}',
            ywiTypeEid:'etype', ywiTypeValue:'${info.etype}', ywiTypeClass:2001,
            ywiKeyEid:'ekey', ywiKeyValue:'${info.ekey}', ywiKeyClass:2,
            ywiTagEid:'etag', ywiTagValue:'${info.etag}', ywiTagClass:2002

            ,provinceEid:'province', provinceValue:'${info.province}',
            cityEid:'__CITY_ELE', cityValue:'${info.city}',
            areaEid:'__AREA_ELE', areaValue:'${info.area}'
        });
    });


    var formTool = {
        pageType: '${pageType}',
        basePath: '${basePath}',
        modelPath: '/admin/supply/ven/',

        /** 入口*/
        init: function(){
            var pageType = this.pageType;

            this.initVal();

            var me = this;

            me.frm = $("#whgff");
            me.buttonDiv = $("div.whgff-but");

            if (me[pageType] && $.type(me[pageType])==='function'){
                if (pageType=='show' || pageType=='edit'){
                    showXjReason('${id}', '${info.state}', me.frm);
                }
                me[pageType].call(me);
            }
        },

        initVal: function(){
            var me = this;

            $(".textarea-value").each(function(){
                var textarea = $(this).nextAll("textarea.textarea-value");
                if (textarea && textarea.size()){
                    $(this).textbox("setValue", textarea.val())
                }
            });

            var ekey = "${info.ekey}";
            if (ekey && ekey!='') {
                $("#ekey").combobox('setValue', ekey);
            };

            me.imgurl = WhgUploadImg.init({
                basePath: me.basePath,
                uploadBtnId: 'imgUploadBtn1',
                hiddenFieldId: 'cult_picture1',
                previewImgId: 'previewImg1'
            });

            WhgMap.init({
                basePath:'${basePath}',
                addrFieldId:'address',
                xpointFieldId:'longitude',
                ypointFieldId:'latitude',
                getPointBtnId:'getXYPointBtn'
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
            var etype = $("[name='etype']:checkbox:checked");
            if (!etype || etype.size()==0){
                $.messager.alert("错误", '类型不能为空', 'error');
                //return false;
                return true;
            }

            param.ekey = $("#ekey").combobox('getText');
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

            $("#getXYPointBtn").hide();
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

    };

</script>

</body>
</html>
