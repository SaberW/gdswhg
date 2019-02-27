<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<% String path = request.getContextPath();%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>编辑文艺专家</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>
    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>

    <script type="text/javascript" src="${basePath }/static/common/js/whg.sys.base.data.js"></script>
    <script src="${basePath}/static/common/js/area.js"></script>
    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-moreimg.js"></script>

    <!-- 图片上传相关-END -->

</head>
<body>

<form id="whgff" class="whgff" method="post" action="${basePath}/admin/personnel/edit">
    <c:choose>
        <c:when test="${not empty targetShow}">
            <h2>查看文艺专家</h2>
        </c:when>
        <c:otherwise>
            <h2>编辑文艺专家</h2>
        </c:otherwise>
    </c:choose>
    <input type="hidden" name="id" id="id" value="${id}"/>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>名称：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="name" value="${cult.per.name}"
                                            style="width:500px; height:32px"
                                            data-options="required:true, validType:'length[1,60]'"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>上传照片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture1" name="picture" value="${cult.per.picture}">
            <div class="whgff-row-input-imgview" id="previewImg1"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save"
                      id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>供需文化馆：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:245px; height:32px" name="cultid" id="cultid"
                   data-options="required:true"/>
            <input class="easyui-combobox" style="width:245px; height:32px" name="deptid" id="deptid"
                   data-options="required:true"/>
        </div>
    </div>


    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>区域：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:150px; height:32px" id="__PROVINCE_ELE" name="province"
                   data-options="required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name', data:__PROVINCE,  onChange:changeProvince"/>
            <input class="easyui-combobox" style="width:150px; height:32px" id="__CITY_ELE" name="city"
                   data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name', data:[], onChange:changeCity"/>
            <input class="easyui-combobox" style="width:179px; height:32px" id="__AREA_ELE" name="area"
                   data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>专长：</div>
        <div class="whgff-row-input">
            <div class="checkbox checkbox-primary whg-js-data" name="type" value="${cult.per.type}"
                 js-data="WhgComm.getPerType">
            </div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>关键字：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" id="mainkey" name="mainkey" style="width:500px; height:32px"
                   validType="notQuotes" data-options="multiple:true, editable:true, prompt:'请填写关键字'
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
        <div class="whgff-row-label"><label style="color: red">*</label>性别：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="sex" value="${cult.per.sex}"
                 js-data='[{"id":"0","text":"女"},{"id":"1","text":"男"}]'></div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">民族：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="family" value="${cult.per.family}"
                                            style="width:500px; height:32px" data-options="validType:'length[1,60]'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">身份证号：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="cardno" value="${cult.per.cardno}"
                                            style="width:500px; height:32px" data-options=" validType:'length[1,60]'">
        </div>
    </div>


    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>出生日期：</div>
        <div class="whgff-row-input">
            <input class="easyui-datebox" name="birthstr" value="${cult.per.birthstr}" style="width:200px; height:32px"
                   data-options="required:true">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">学历：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="xueli" value="${cult.per.xueli}"
                                            style="width:500px; height:32px" data-options="validType:'length[1,60]'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">文艺职务：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="job" value="${cult.per.job}"
                                            style="width:500px; height:32px" data-options=" validType:'length[1,60]'">
        </div>
    </div>


    <div class="whgff-row">
        <div class="whgff-row-label">工作单位：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="workplace" value="${cult.per.workplace}" style="width:500px; height:32px" data-options="validType:'length[1,100]'"></div>
    </div>


    <div class="whgff-row">
        <div class="whgff-row-label">住址：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="address" value="${cult.per.address}"
                                            style="width:500px; height:32px" data-options="validType:'length[1,60]'">
        </div>
    </div>

    <%--<div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>是否收费：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="ismoney" value="${cult.per.ismoney}"
                 js-data='[{"id":"0","text":"不收费"},{"id":"1","text":"收费"}]'>
            </div>
        </div>
    </div>--%>


    <div class="whgff-row">
        <div class="whgff-row-label">联系人：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="contacts" value="${cult.per.contacts}"
                                            style="width:500px; height:32px" data-options="validType:'length[1,60]'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">联系电话：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="phoneno" value="${cult.per.phoneno}"
                                            style="width:500px; height:32px" data-options="validType:'length[1,60]'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">邮箱：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="email" style="width:500px; height:32px" value="${cult.per.email}"
                   data-options="required:false,validType:['length[1,20]', 'isEmail'], prompt:'请输入邮箱'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">固定电话：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="telephone" style="width:300px; height:32px" value="${cult.per.telephone}"
                   data-options="required:false,validType:'isTel', prompt:'请输入固定电话'">
        </div>
    </div>
    <%--<div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>配送范围：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:150px; height:32px" name="psprovince" id="__PSPROVINCE_ELE"
                   data-options="readonly:true, required:true, prompt:'请选择省', editable:false, limitToList:true, valueField:'name', textField:'name' "/>
            <input class="easyui-combobox" style="width:400px; height:32px" name="pscity" id="__PSCITY_ELE"
                   data-options="required:true, prompt:'请选择市(多选)', editable:false, multiple:true, valueField:'name', textField:'name'"/>
        </div>
    </div>--%>
    <%--<div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>配送时间：</div>
        <div class="whgff-row-input" style="width:500px" id="timesUI">
        </div>
    </div>--%>

   <%-- <div class="whgff-row">
        <div class="whgff-row-label">获奖情况：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="hjqk"  multiline="true" value="${cult.per.hjqk}" style="width:600px;height: 100px;"
                   data-options="validType:['length[1,400]']">
        </div>
    </div>--%>


    <div class="whgff-row">
        <div class="whgff-row-label">说明：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="perexplain" value="${not empty cult.per.perexplain? cult.per.perexplain :'一旦达成意向，经费参照国家和地方相关标准执行，相关具体事宜由派出方与承接方相互协商而定。'}"  multiline="true" style="width:600px;height: 100px;"
                   data-options="validType:['length[1,1000]']">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>人才简介：</div>
        <div class="whgff-row-input">
            <div id="catalog" type="text/plain" style="width:700px; height:250px;"></div>
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

<div id="whgwin-add-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <div style="display: inline-block; margin: 0 auto">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="whgwin-add-btn-save">保 存</a>
<a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
</div>
</div>

<!-- script -->
<script type="text/javascript">

    var ueConfig = {
        scaleEnabled: false,
        autoFloatEnabled: false,
        readonly: '${targetShow}'? true: false//富文本编辑器设为只读
    };
    var ue_catalog = UE.getEditor('catalog', ueConfig);

    ue_catalog.ready(function () {
        ue_catalog.setContent('${cult.per.summary}')
    });
    $(function () {
        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'${cult.per.cultid}',
            deptEid:'deptid', deptValue:'${cult.per.deptid}',
            ywiKeyEid:'mainkey', ywiKeyValue:'${cult.per.mainkey}', ywiKeyClass:25,
            ywiTypeEid:'type', ywiTypeValue:'${cult.per.type}', ywiTypeClass:25
            ,psprovinceEid:'__PSPROVINCE_ELE', psprovinceValue:'${cult.per.psprovince}',
            pscityEid:'__PSCITY_ELE', pscityValue:'${cult.per.pscity}'
        });
        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1'});

        var state = '${cult.per.state}';
        if (state && state == 4) {
            showXjReason('${cult.per.id}');
        }

        $('#whgff').form({
            novalidate: true,
            url: "${basePath}/admin/personnel/edit",
            onSubmit : function(param) {
                var _valid = $(this).form('enableValidation').form('validate')
                if(_valid) {
                    //图片必填
                    if($('#cult_picture1').val() == ""){
                        _valid = false;
                        $.messager.alert('提示', '请选择封面图片');
                    }else if(!isUEvalid) {
                        var isUEvalid = validateUE();
                        if (isUEvalid) {
                            param.summery = ue_catalog.getContent();
                            $.messager.progress();
                        } else {
                            _valid = false;
                        }
                    }
                }
                if (!_valid){
                    $.messager.progress('close');
                    $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
                }
                /*var times = timesUI.getValue();
                param.times = JSON.stringify(times);*/
                return _valid;
            },
            success : function(data) {
                $.messager.progress('close');
                var Json = eval('('+data+')');
                if(Json && Json.success == '1'){
                    window.parent.$('#whgdg').datagrid('reload');
                    WhgComm.editDialogClose();
                } else {
                    $.messager.alert('提示', '操作失败:'+Json.errormsg+'!', 'error');
                    $('#whgwin-add-btn-save').off('click').one('click', submitFun);
                }
            }
        });
        //注册提交事件
        $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
    });

    function validateUE(){
        if (!ue_catalog.hasContents()) {
            $.messager.alert("错误", '专家人才描述不能为空', 'error');
            return false;
        }
        return true;
    }

    //查看时的处理
    $(function () {
        /*var aa = '${cult.times}';
        $("#timesUI").html("");
        timesUI.init();
        timesUI.setValue( JSON.parse(aa) );*/


        var targetShow = '${targetShow}';
        var province = '${cult.per.province}';
        if (!province || province==''){
            province = '广东省';
        }
        $("#__PROVINCE_ELE").combobox("setValue", province);

        var city = '${cult.per.city}';
        if (city && city!=''){
            $("#__CITY_ELE").combobox("setValue", city);
        }
        var area = '${cult.per.area}';
        if (area && area!=''){
            $("#__AREA_ELE").combobox("setValue", area);
        }



        if (targetShow){
            //取消表单验证
            $("#whgff").form("disableValidation");

            $('.easyui-textbox').textbox('readonly');
            $('.easyui-combobox').combobox('readonly');

            //不显示提交 button
            $('#whgwin-add-btn-save').hide();
            return;
        }

    });


    /**
     * 时间项处理
     * */
    /*var timesUI = {
        target: "#timesUI",
        check: "true",
        beginTimeKey : "timestart",
        endTimeKey: "timeend",

        init: function(jq){
            this.target = jq || this.target;
            this.main = $(this.target);

            this.validRules();
            this.appendRow();
            var me = this;
            if(this.check=="true"){
                this.main.on("click",".timesui-row-del", function(e){me.delRow(e)});
                this.main.on("click",".timesui-row-add", function(){me.addRow()});
            }
            this.check="false";
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
            if(this.main) {
                this.main.find(".timesui-row").each(function () {
                    var timestart = $(this).find(".timesui-input:eq(0)").datetimebox("getValue");
                    var timeend = $(this).find(".timesui-input:eq(1)").datetimebox("getValue");
                    var item = {};
                    item[me.beginTimeKey] = timestart;
                    item[me.endTimeKey] = timeend;

                    rest.push(item);
                });
            }
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
<!-- script END -->
</body>
</html>
