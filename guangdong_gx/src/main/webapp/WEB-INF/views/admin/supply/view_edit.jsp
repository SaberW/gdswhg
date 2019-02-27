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
        <c:when test="${type eq 'show'}">
            <c:set var="pageTitle" value="需求管理-查看需求信息"></c:set>
        </c:when>
        <c:when test="${type eq 'edit'}">
            <c:set var="pageTitle" value="需求管理-编辑需求信息"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="需求管理-添加需求信息"></c:set>
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

    <script src="${basePath}/static/common/js/area.js"></script>
</head>
<body class="body_add">

<form id="whgff" method="post" class="whgff">
    <h2>${pageTitle}</h2>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>需求标题：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="title" value="${info.title}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,100]'], prompt:'请输入标题'">
        </div>
    </div>

    <%--<div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>需求分类：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="gxtype" value="${info.gxtype}"
                 js-data='[{"id":"0","text":"供给"},{"id":"1","text":"需求"}]'>
            </div>
        </div>
    </div>--%>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>所属单位：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:245px; height:32px" name="cultid" id="cultid" data-options="editable:false,required:true" />
            <input class="easyui-combobox" style="width:245px; height:32px" name="deptid" id="deptid" data-options="editable:false,required:true" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>区域：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:150px; height:32px" id="__PROVINCE_ELE" name="province" data-options="readonly:true, required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name'<%--, data:__PROVINCE,  onChange:changeProvince--%>"/>
            <input class="easyui-combobox" style="width:150px; height:32px" id="__CITY_ELE" name="city" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name'<%--, data:[], onChange:changeCity--%>"/>
            <input class="easyui-combobox" style="width:179px; height:32px" id="__AREA_ELE" name="area" data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>信息类型：</div>
        <div class="whgff-row-input">
            <select class="easyui-combobox" id="etype" name="etype" style="width:150px; height:32px" prompt="请选择类型" panelHeight="auto" limitToList="true"
                    data-options="editable:false, required:true, valueField:'id', textField:'text', data:WhgComm.getSupplyType()" ></select>

            <select class="easyui-combobox" id="reftype" name="reftype" style="width:338px; height:32px" prompt="请选择" panelHeight="auto" limitToList="true"
                    data-options="editable:false, required:true, valueField:'id', textField:'name' " ></select>
        </div>
    </div>

    <div class="whgff-row" refShowEtypes="6">
        <div class="whgff-row-label">艺术类型：</div>
        <div class="whgff-row-input">
            <div class="checkbox checkbox-primary" id="arttype" name="arttype"></div>
        </div>
    </div>
    <div class="whgff-row" refShowEtypes="2,3,4,5,6">
        <div class="whgff-row-label">关键字：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="ekey" value="${info.ekey}" style="width:600px; height:32px"
                   data-options="required:false,validType:['length[1,1000]']">
        </div>
    </div>
    <div class="whgff-row" refShowEtypes="1,4">
        <div class="whgff-row-label">人数：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="renshu" value="${info.renshu}" style="width:600px; height:32px"
                   data-options="required:false,validType:['length[1,100]']">
        </div>
    </div>
    <div class="whgff-row" refShowEtypes="1">
        <div class="whgff-row-label">场馆面积：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="cgmianji" value="${info.cgmianji}" style="width:600px; height:32px"
                   data-options="required:false,validType:['length[1,100]']">
        </div>
    </div>
    <div class="whgff-row" refShowEtypes="2">
        <div class="whgff-row-label">老师：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="pxlaoshi" value="${info.pxlaoshi}" style="width:600px; height:32px"
                   data-options="required:false,validType:['length[1,300]']">
        </div>
    </div>
    <div class="whgff-row" refShowEtypes="4">
        <div class="whgff-row-label">演员：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="zyyanyuan" value="${info.zyyanyuan}" style="width:600px; height:32px"
                   data-options="required:false,validType:['length[1,300]']">
        </div>
    </div>
    <div class="whgff-row" refShowEtypes="4">
        <div class="whgff-row-label">演出时长：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="zyshichang" value="${info.zyshichang}" style="width:600px; height:32px"
                   data-options="required:false,validType:['length[1,300]']">
        </div>
    </div>
    <div class="whgff-row" refShowEtypes="4">
        <div class="whgff-row-label">姓名：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="zjxingming" value="${info.zjxingming}" style="width:600px; height:32px"
                   data-options="required:false,validType:['length[1,300]']">
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
        <div class="whgff-row-label"><i>*</i>需求时间：</div>
        <div class="whgff-row-input" id="timesUI">

        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>配送范围：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:150px; height:32px" name="psprovince" id="__PSPROVINCE_ELE"
                   data-options="readonly:true, required:true, prompt:'请选择省', editable:false, limitToList:true, valueField:'name', textField:'name'<%--, data:__PROVINCE, onChange:changePSProvince--%>"/>
            <input class="easyui-combobox" style="width:400px; height:32px" name="pscity" id="__PSCITY_ELE"
                   data-options="required:true, prompt:'请选择市(多选)', editable:false, multiple:true, valueField:'name', textField:'name'"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>需求说明：</div>
        <div class="whgff-row-input">
            <div id="description" name="_content" type="text/plain" style="width:800px; height:200px;"></div>
            <textarea id="value_description" style="display: none;">${info.content}</textarea>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>配送说明：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox textarea-value" name="notice" multiline="true" style="width:600px;height: 200px;"
                   data-options="required:true,validType:['length[1,1000]']">
            <textarea class="textarea-value" style="display:none;">${not empty info.notice? info.notice :'一旦达成意向，经费参照国家和地方相关标准执行，相关具体事宜由派出方与承接方相互协商而定。'}</textarea>
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
</div>

<script>

    /*function changePSProvince(newVal, oldVal){
        var citys = [];
        if(newVal){
            for(var i=0; i<__CITY.length; i++){
                if(__CITY[i].proname == newVal){
                    citys.push(__CITY[i]);
                }
            }
        }
        $('#__PSCITY_ELE').combobox('loadData', citys);
        $('#__PSCITY_ELE').combobox('setValues', '');
    }*/

    $(function(){
        timesUI.init();
        formTool.init();

        var state = '${info.state}';
        if (state && state == 4) {
            showXjReason('${info.id}');
        }

        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'${info.cultid}',
            deptEid:'deptid', deptValue:'${info.deptid}',
            ywiArtTypeEid:'arttype', ywiArtTypeValue:'${info.arttype}',
            cultOnChange: function(val,oval){formTool.__reLoadTypes()}

            ,provinceEid:'__PROVINCE_ELE', provinceValue:'${info.province}',
            cityEid:'__CITY_ELE', cityValue:'${info.city}',
            areaEid:'__AREA_ELE', areaValue:'${info.area}'

            ,psprovinceEid:'__PSPROVINCE_ELE', psprovinceValue:'${info.psprovince}',
            pscityEid:'__PSCITY_ELE', pscityValue:'${info.pscity}'
        });
    });


    /**
     * 时间项处理
     * */
    var timesUI = {
        target: "#timesUI",
        beginTimeKey : "timestart",
        endTimeKey: "timeend",

        init: function(jq){
            this.target = jq || this.target;
            this.main = $(this.target);

            this.validRules();

            this.appendRow();

            var me = this;
            this.main.on("click",".timesui-row-del", function(e){me.delRow(e)});
            this.main.on("click",".timesui-row-add", function(){me.addRow()});
        },


        delRow: function (e) {
            var evtObj = e.target;
            $(evtObj).parents(".timesui-row").remove();
        },

        addRow: function(){
            this.appendRow();
            this.disableValidation();
        },

        clearRows: function () {
            this.main.find(".timesui-row").remove();
        },

        setIsShow: function () {
            this.disableValidation();
            this.main.find(".timesui-input").datetimebox('readonly');
            this.main.find(".timesui-row-del,.timesui-row-add").hide();
        },

        getValue: function(){
            var rest = [];
            var me = this;
            this.main.find(".timesui-row").each(function(){
                var timestart = $(this).find(".timesui-input:eq(0)").datetimebox("getValue");
                var timeend = $(this).find(".timesui-input:eq(1)").datetimebox("getValue");
                var item = {};
                item[me.beginTimeKey] = timestart;
                item[me.endTimeKey] = timeend;

                rest.push(item);
            });
            return rest;
        },

        setValue: function(times){
            this.clearRows();
            if (!times || !$.isArray(times) || times.length==0){
                this.appendRow();
            }
            for(var key in times){
                var item = times[key];
                var timestart = item[this.beginTimeKey];
                var timeend = item[this.endTimeKey];
                var ts = new Date(timestart).Format("yyyy-MM-dd hh:mm:ss");
                var te = new Date(timeend).Format("yyyy-MM-dd hh:mm:ss");
                this.appendRow(ts, te);
            }
        },

        appendRow: function(timestart, timeend){
            var row = '<div class="timesui-row" style="margin-bottom: 10px">' +
                    '<input class="timesui-input"/> 至 <input class="timesui-input"/>' +
                    '</div>';
            this.main.append(row);
            var jrow = this.main.find(".timesui-row:last");
            jrow.find(".timesui-input").datetimebox({
                required:true,
                validType: ['TimesUI_gtpretime']
            });

            var rowidx = this.main.find(".timesui-row").index(jrow);
            if (rowidx>0){
                jrow.append('<a class="timesui-row-del">删除</a>');
            }
            jrow.append('<a class="timesui-row-add" style="margin-left: 5px;">添加</a>');

            if (timestart){
                jrow.find(".timesui-input:eq(0)").datetimebox("setValue", timestart);
            }
            if (timeend){
                jrow.find(".timesui-input:eq(1)").datetimebox("setValue", timeend);
            }
        },

        enableValidation: function(){
            this.main.find(".timesui-input").datetimebox('enableValidation');
        },

        disableValidation: function () {
            this.main.find(".timesui-input").datetimebox('disableValidation');
        },

        validRules: function(){
            var me = this;

            function date2number(date){
                var times = date.split(/\D+/);
                if (times.length<6){
                    return false;
                }
                times[1] = Number(times[1])-1;
                return new Date(times[0], times[1], times[2], times[3], times[4], times[5])
            }

            $.extend($.fn.validatebox.defaults.rules, {
                TimesUI_gtpretime: {
                    validator: function(value, param){
                        var optInput = $(this).parents("span").prev(".timesui-input");
                        var optIdx = $('.timesui-input', me.main).index(optInput);
                        if (optIdx == 0){
                            return true;
                        }
                        var prevIdx = optIdx-1;
                        var prevInput = $('.timesui-input:eq('+prevIdx+')', me.main);
                        var prevValue = prevInput.datetimebox("getValue");

                        var prevDate = date2number(prevValue);
                        var optDate = date2number(value);
                        if (!prevDate || !optDate){
                            return true;
                        }
                        return prevDate < optDate;
                    },
                    message: '必须大需前一个时间'
                }
            });
        }
    };


    /**
     * 页面操作处理
     * */
    var formTool = {
        modelPath : '${basePath}/admin/supply/',
        jqFrm: "#whgff",
        jqButtonDiv: "div.whgff-but",

        /** 入口*/
        init: function(){
            var type = this.gettype();

            var me = this;

            me.frm = $(me.jqFrm);
            me.buttonDiv = $(me.jqButtonDiv);

            this.__typeRefAction();
            this.initValue();

            if (me[type] && $.type(me[type]) === 'function') {
                me[type].call(me);
            }
        },

        /** 页面类型*/
        gettype: function () {
            return '${type}';
        },

        setRefShowEtypes: function(etype){
            var me = this;

            me.frm.find(".whgff-row[refShowEtypes]").each(function(){
                var showEtypes = $(this).attr("refShowEtypes");
                if (!showEtypes || showEtypes==''){ return true; }

                var showArray = showEtypes.split(/\D+/);
                if ($.inArray(etype, showArray) ==-1){
                    $(this).hide().val('');
                }else {
                    $(this).show();
                }
            });
        },

        /** 分类及联动处理 */
        __typeRefAction: function () {
            var etype = $("#etype");
            var reftype = $("#reftype");
            var me = this;
            etype.combobox({
                //onChange: function(val, oval){
                onSelect: function(record){
                    me.setRefShowEtypes(record.id);
                    me.__reLoadTypes(record.id);
                }
            })
        },

        __reLoadTypes: function(etypeVal){
            var cultid = $("#cultid");
            var etype = $("#etype");
            var reftype = $("#reftype");

            var cultidVal = cultid.combobox("getValue");
            etypeVal  = etypeVal || etype.combobox("getValue");

            if (!cultidVal || !etypeVal){
                return;
            }

            var type;
            switch (etypeVal){
                case "1" : type = 2; break;    //场馆分类
                case "2" : type = 5; break;   //培训分类
                case "3" : type = 20; break;   //商品分类
                case "4" : type = 4; break;   //演出节目分类
                case "5" : type = 23; break;   //展览分类
                case "6" : type = 11; break;   //专家分类
            }

            reftype.combobox({
                url : '${basePath}/admin/yunwei/type/srchList',
                queryParams: {type: type, cultid: cultidVal},
                validateOnCreate: false
            });
        },

        initValue: function () {
            var type = this.gettype();

            $(".textarea-value").each(function(){
                var textarea = $(this).nextAll("textarea.textarea-value");
                if (textarea && textarea.size()){
                    $(this).textbox("setValue", textarea.val())
                }
            });

            var ueConfig = {
                scaleEnabled: false,
                autoFloatEnabled: false,
                elementPathEnabled:false,
                readonly: type == 'show'
            };
            var ue_description = UE.getEditor('description', ueConfig);
            ue_description.ready(function(){  ue_description.setContent($("#value_description").val()) });

            this.ue_description = ue_description;


            var timesJson = '${info.times}';
            if (timesJson && timesJson!=''){
                timesUI.setValue( JSON.parse(timesJson) );
            }

            var etype = '${info.etype}';
            if (!etype || etype==''){
                etype = 1;
            }
            $("#etype").combobox("setValue", etype);

            var reftype = '${info.reftype}';
            if (reftype && reftype!=''){
                $("#reftype").combobox({
                    onLoadSuccess: function(){
                        $(this).combobox("setValue", reftype);
                        $(this).combobox("options").onLoadSuccess = undefined;
                    }
                });
            }

            /*var psprovince = '${info.psprovince}';
            if (!psprovince || psprovince==''){
                psprovince = '广东省';
            }
            $("#__PSPROVINCE_ELE").combobox("setValue", psprovince);

            $("#__PSCITY_ELE").combobox({
                onChange: function (val, oldval) {
                    if (val.length>1 && val[0]==''){
                        val.shift();
                        $(this).combobox("setValues", val);
                    }
                }
            });
            var pscity = '${info.pscity}';
            if (pscity && pscity!=''){
                $("#__PSCITY_ELE").combobox("setValues", pscity);
            }

            var province = '${info.province}';
            if (!province || province==''){
                province = WhgComm.getProvince()?WhgComm.getProvince():'广东省';
            }
            $('#__PROVINCE_ELE').combobox('setValue', province);

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

            if (!me.ue_description.hasContents()){
                $.messager.alert("错误", '需求说明不能为空', 'error');
                return false;
            }else{
                param.content = me.ue_description.getContent();
            }

            var etype = $("#etype").combobox("getValue");
            if (etype!= 6){
                param.arttype = '';
            }

            var times = timesUI.getValue();
            param.times = JSON.stringify(times);

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
            timesUI.setIsShow();

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
            me.__submitForm(okBut, me.modelPath+'edit', id);
        },

        /** 添加*/
        add : function() {
            var me = this;

            var okBut = me.__appendButton("提 交", 'icon-ok');
            //var noBut = me.__appendButton("清 空", 'icon-no', function(){ me.__clearForm(); });
            var undoBut = me.__appendButton("返 回", 'icon-undo', function(){ me.__closeForm(); });

            me.__submitForm(okBut, me.modelPath+'add');
        },

        /** 表单提交*/
        __submitForm: function (okBut, url, id) {
            var me = this;
            var type = me.gettype();

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
        }
    }
</script>
</body>
</html>
