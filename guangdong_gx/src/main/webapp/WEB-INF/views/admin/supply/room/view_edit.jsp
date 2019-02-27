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
            <c:set var="pageTitle" value="供需活动室管理-查看"></c:set>
        </c:when>
        <c:when test="${pageType eq 'edit'}">
            <c:set var="pageTitle" value="供需活动室管理-编辑"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="供需活动室管理-添加"></c:set>
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

    <script type="text/javascript" src="${basePath}/static/common/js/whg.maps.js"></script>
    <script type="text/javascript" src="${basePath}/static/common/js/area.js"></script>
    <%--<script>
        ;function changePSProvince(newVal, oldVal){
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
        };
    </script>--%>
</head>
<body class="body_add">
<form id="whgff" method="post" class="whgff">
    <h2>${pageTitle}</h2>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>活动室名称：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="title" value="${info.title}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,40]'], prompt:'请输入活动室名称'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>活动室图片：</div>
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
        <div class="whgff-row-label"><i>*</i>供需文化馆：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:200px; height:32px" name="cultid" id="cultid" data-options="editable:false,required:true" />
            <input class="easyui-combobox" style="width:200px; height:32px" name="deptid" id="deptid" data-options="editable:false,required:true" />
            <input class="easyui-combobox" style="width:200px; height:32px" name="venid" id="venid" data-options="editable:false,required:true" />
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
        <div class="whgff-row-label"><i>*</i>场地面积：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="sizearea" value="${info.sizearea}" style="width:300px; height:32px"
                   data-options="required:true, prompt:'请输入面积，如100平方米'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>可容纳人数：</div>
        <div class="whgff-row-input">
            <input class="easyui-numberbox" name="sizepeople" value="${info.sizepeople}" style="width:300px; height:32px"
                   data-options="required:true,min:0,max:1000000, prompt:'请输入可容人数'">
            <span>人</span>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">活动室设施：</div>
        <div class="whgff-row-input">
            <div class="checkbox checkbox-primary" id="facility" name="facility"></div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>活动室地址：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" id="location" name="location" value="${info.location}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,200]'], prompt:'请输入活动室地址'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">适宜人群：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="crowd" value="${info.crowd}" style="width:600px; height:32px"
                   data-options="required:false,validType:['length[1,100]']">
        </div>
    </div>

    <%--<div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>收费：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="hasfees" value="${info.hasfees}"
                 js-data='[{"id":"0","text":"不收费"},{"id":"1","text":"收费"}]'>
            </div>
        </div>
    </div>--%>

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
    <%--<div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>配送范围：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:150px; height:32px" name="psprovince" id="__PSPROVINCE_ELE"
                   data-options="readonly:true, required:true, prompt:'请选择省', editable:false, limitToList:true, valueField:'name', textField:'name'&lt;%&ndash;, data:__PROVINCE, onChange:changePSProvince&ndash;%&gt;"/>
            <input class="easyui-combobox" style="width:400px; height:32px" name="pscity" id="__PSCITY_ELE"
                   data-options="required:true, prompt:'请选择市(多选)', editable:false, multiple:true, valueField:'name', textField:'name'"/>
        </div>
    </div>--%>

    <%--<div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>配送时间：</div>
        <div class="whgff-row-input" id="timesUI">

        </div>
    </div>--%>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>配送说明：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox textarea-value" name="notice" multiline="true" style="width:600px;height: 200px;"
                   data-options="required:true,validType:['length[1,1000]']">
            <textarea class="textarea-value" style="display:none;">${not empty info.notice? info.notice :'一旦达成意向，经费参照国家和地方相关标准执行，相关具体事宜由派出方与承接方相互协商而定。'}</textarea>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>活动室简介：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox textarea-value" name="summary" multiline="true" style="width:600px;height: 300px;"
                   data-options="required:true,validType:['length[1,1000]']">
            <textarea class="textarea-value" style="display:none;">${info.summary}</textarea>
        </div>
    </div>

</form>

<div class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
</div>

<script>

    $(function(){
        //timesUI.init();
        formTool.init();

        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'${info.cultid}',
            deptEid:'deptid', deptValue:'${info.deptid}',
            cultOnChange: function(val, oval){
                $('#venid').combobox({
                    limitToList:true, valueField:'id', textField:'title',
                    url:'${basePath}/admin/supply/ven/getVens?cultid='+val
                    , novalidate:true
                });
                $('#venid').combobox('setValue', '${info.venid}');
            },

            ywiTypeEid:['etype', 'facility'], ywiTypeValue:['${info.etype}','${info.facility}'], ywiTypeClass:[3,7],
            ywiKeyEid:'ekey', ywiKeyValue:'${info.ekey}', ywiKeyClass:3,
            ywiTagEid:'etag', ywiTagValue:'${info.etag}', ywiTagClass:3

            /*,psprovinceEid:'__PSPROVINCE_ELE', psprovinceValue:'${info.psprovince}',
            pscityEid:'__PSCITY_ELE', pscityValue:'${info.pscity}'*/
        });
    });


    var formTool = {
        pageType: '${pageType}',
        basePath: '${basePath}',
        modelPath: '/admin/supply/room/',

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

            var ekey = "${info.ekey}";
            if (ekey && ekey!='') {
                $("#ekey").combobox('setValue', ekey);
            };

            /*var timesJson = '${info.times}';
            if (timesJson && timesJson!=''){
                timesUI.setValue( JSON.parse(timesJson) );
            }*/

            $(".textarea-value").each(function(){
                var textarea = $(this).nextAll("textarea.textarea-value");
                if (textarea && textarea.size()){
                    $(this).textbox("setValue", textarea.val())
                }
            });

            me.imgurl = WhgUploadImg.init({
                basePath: me.basePath,
                uploadBtnId: 'imgUploadBtn1',
                hiddenFieldId: 'cult_picture1',
                previewImgId: 'previewImg1'
            });


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
                return false;
            }

            param.ekey = $("#ekey").combobox('getText');

            /*var times = timesUI.getValue();
            param.times = JSON.stringify(times);*/

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
            //timesUI.setIsShow();

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

    };


    /**
     * 时间项处理
     * */
    /*var timesUI = {
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
    };*/

</script>

</body>
</html>
