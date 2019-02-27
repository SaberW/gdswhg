<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <c:choose>
        <c:when test="${not empty id and pageType == 2}">
            <c:set var="pageTitle" value="查看供需培训"></c:set>
        </c:when>
        <c:when test="${not empty id}">
            <c:set var="pageTitle" value="编辑供需培训"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="添加供需培训"></c:set>
        </c:otherwise>
    </c:choose>

    <title>${pageTitle}</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>

    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/Font-Awesome/css/font-awesome.min.css"/>
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
    <%--<script>
        function changePSProvince(newVal, oldVal){
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
<body>
<form id="whgff" method="post" class="whgff">
    <h2>${pageTitle}</h2>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>标题：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="title" value="${info.tra.title}" style="width:500px; height:32px"
                   data-options="required:true,validType:['length[1,60]'],prompt:'请输入标题'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>图片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture1" name="image" value="${info.tra.image}">
            <div class="whgff-row-input-imgview" id="previewImg1"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>培训分类：</div>
        <div class="whgff-row-input">
            <%--<div class="radio radio-primary" id="etype" name="etype"></div>--%>
                <div class="checkbox checkbox-primary" id="etype" name="etype"></div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>艺术分类：</div>
        <div class="whgff-row-input">
            <div class="checkbox checkbox-primary" id="arttype" name="arttype"></div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>关键字：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" id="ekey" style="width:500px; height:32px" validType="notQuotes" data-options="required:true,multiple:true, editable:true, prompt:'请填写关键字'
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
        <div class="whgff-row-label"><i>*</i>培训方式：</div>
        <div class="whgff-row-input">
            <div class="checkbox checkbox-primary whg-js-data" name="islive" id="islive" value="${info.tra.islive}" js-data='[{"id":"0","text":"线上"},{"id":"1","text":"线下"}]'></div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>培训时长：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="duration" value="${info.tra.duration}" style="width:300px; height:32px"
                   data-options="required:true,<%--min:0,max:1000000,--%>validType:['length[0,100]'], prompt:'请输入培训时长'"> <%--小时--%>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>培训周期：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="period" value="${info.tra.period}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,100]'], prompt:'请输入培训周期'">
        </div>
    </div>
    <%--<div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>是否收费：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" value="${info.tra.ismoney}" name="ismoney" js-data='[{"id":"0","text":"否"},{"id":"1","text":"是"}]'></div>
        </div>
    </div>--%>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>区域：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:150px; height:32px" id="province" name="province"
                   data-options="required:true,readonly:true,prompt:'请选择省', limitToList:true, valueField:'name', textField:'name'<%--, data:__PROVINCE, onChange:myChangeProvince--%>"/>
            <input class="easyui-combobox" style="width:150px; height:32px" id="__CITY_ELE" name="city"
                   data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name'<%--, data:[], onChange:myChangeCity--%>"/>
            <input class="easyui-combobox" style="width:179px; height:32px" id="__AREA_ELE" name="area"
                   data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>联系人：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="contacts" value="${info.tra.contacts}" style="width:500px; height:32px"
                   data-options="required:true,validType:['length[1,20]'],prompt:'请填写联系人'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>联系电话：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="phone" value="${info.tra.phone}" style="width:500px; height:32px"
                   data-options="required:true,validType:'isPhone[\'traphone\']',prompt:'请填写联系电话'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>邮箱：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="email" value="${info.tra.email}" style="width:500px; height:32px"
                   data-options="required:true,validType:['length[1,20]', 'isEmail'], prompt:'请输入邮箱'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">所属单位：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="workplace" value="${info.tra.workplace}" style="width:300px; height:32px"
                   data-options="required:false, prompt:'请输入所属单位'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">固定电话：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="telephone" value="${info.tra.telephone}" style="width:300px; height:32px"
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
        <div class="whgff-row-input" style="width:500px" id="timesUI">
        </div>
    </div>--%>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>供需文化馆：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:245px; height:32px" name="cultid" id="cultid" data-options="required:true" />
            <input class="easyui-combobox" style="width:245px; height:32px" name="deptid" id="deptid" data-options="required:true" />
        </div>
    </div>
    <%--关联文艺专家--%>
    <div class="whgff-row">
        <div class="whgff-row-label">文艺专家：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:245px; height:32px" name="artistid" id="artistid" value="${info.tra.artistid}" data-options="required:false" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>合适人群：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="fitcrowd" value="${info.tra.fitcrowd}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,100]'], prompt:'请输入合适人群'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>说明：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="notice" value="${not empty info.tra.notice? info.tra.notice :'一旦达成意向，经费参照国家和地方相关标准执行，相关具体事宜由派出方与承接方相互协商而定。'}" style="width:500px; height:60px"
                   data-options="multiline:true,required:true,validType:['length[1,500]'], prompt:'请输入培训说明'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>培训简介：</div>
        <div class="whgff-row-input">
            <div id="coursedesc" name="coursedesc" type="text/plain" style="width:600px; height:300px;"></div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>课程大纲：</div>
        <div class="whgff-row-input">
            <div id="outline" name="outline" type="text/plain" style="width:600px; height:300px;"></div>
        </div>
    </div>

</form>

<div class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <a class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok">提 交</a>
    <a class="easyui-linkbutton whgff-but-clear" iconCls="icon-no" onclick="WhgComm.editDialogClose();">返 回</a>
</div>

<script>

    //处理UE
    var ueConfig = {
        scaleEnabled:true,
        autoFloatEnabled: false,
        elementPathEnabled:false,
        readonly: '${pageType}'? true: false
    };
    var coursedesc = UE.getEditor('coursedesc', ueConfig);
    var outline = UE.getEditor('outline', ueConfig);
    //UE 设置值

    coursedesc.ready(function(){
        coursedesc.setContent('${info.tra.coursedesc}');
        coursedesc.execCommand( 'unlink');
    });
    outline.ready(function(){
        outline.setContent('${info.tra.outline}');
        outline.execCommand( 'unlink');
    });



    $(function () {
        /*var aa = '${info.times}';
        $("#timesUI").html("");
        timesUI.init();
        if(aa){
            timesUI.setValue( JSON.parse(aa) );
        }*/

        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'${info.tra.cultid}',
            deptEid:'deptid', deptValue:'${info.tra.deptid}',
            ywiArtTypeEid:'arttype', ywiArtTypeValue:'${info.tra.arttype}',ywiArtTypeClass:1001,
            ywiTypeEid:'etype', ywiTypeValue:'${info.tra.etype}', ywiTypeClass:5001,
            ywiKeyEid:'ekey', ywiKeyValue:'${info.tra.ekey}', ywiKeyClass:38,
            artistId:'artistid', artistValue:'${info.tra.artistid}',
            provinceEid:'province', provinceValue:'${info.tra.province}',
            cityEid:'__CITY_ELE', cityValue:'${info.tra.city}',
            areaEid:'__AREA_ELE', areaValue:'${info.tra.area}'


            /*,psprovinceEid:'__PSPROVINCE_ELE', psprovinceValue:'${info.tra.psprovince}',
            pscityEid:'__PSCITY_ELE', pscityValue:'${info.tra.pscity}'*/
        });


        //图片初始化
        var __WhgUploadImg = WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1'});

        var id = '${id}';
        var pageType = '${pageType}';
        var frm = $("#whgff");
        var buts = $("div.whgff-but");

        showXjReason(id, '${info.state}', frm);

        //查看时的处理
        if (pageType){
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
        var url = id ? "${basePath}/admin/supply/tra/edit" : "${basePath}/admin/supply/tra/add";
        frm.form({
            url: url,
            novalidate: true,
            onSubmit: function (param) {
                if (id){
                    param.id = id;
                }

                $(this).form("enableValidation");
                var isValid = $(this).form('validate');
                if (isValid){
                    //富文本验证
                    var isUEvalid = validateUE();
                    if (!isUEvalid){
                        isValid = false;
                    }//富文本验证 --END
                }
                if (!isValid){
                    $.messager.progress('close');
                    buts.find("a.whgff-but-submit").off('click').one('click', function () {
                        frm.submit();
                    });
                }
                param.ekey = $("#ekey").combobox("getText");
                //param.artistid= $("#artistid").combobox("getValue");
                //alert(param.artistid);
                /*var times = timesUI.getValue();
                param.times = JSON.stringify(times);*/
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

        //处理UE项的验证
        function validateUE(){
            //图片不能为空
            var picture1 = $("#cult_picture1").val();
            if (!picture1){
                $.messager.alert("错误", '图片不能为空！', 'error');
                return false;
            }

            var etype = $("#whgff").find("input[name='etype']:checked");
            if (!etype.size()){
                $.messager.alert("错误", '培训分类不能为空！', 'error');
                return false;
            }
            //艺术分类不能为空
            var arttype = $("#whgff").find("input[name='arttype']:checked");
            if (!arttype.size()){
                $.messager.alert("错误", '艺术分类不能为空！', 'error');
                return false;
            }
            var islive = $("#whgff").find("input[name='islive']:checked");
            if (!islive.size()){
                $.messager.alert("错误", '培训方式不能为空！', 'error');
                return false;
            }


            //培训简介不能为空
            if (!coursedesc.hasContents()){
                $.messager.alert("错误", '培训简介不能为空！', 'error');
                return false;
            }
            if (!outline.hasContents()){
                $.messager.alert("错误", '课程大纲不能为空！', 'error');
                return false;
            }
            return true;
        }
    });


    /**
     * 时间项处理
     * */
    /*var timesUI = {
        target: "#timesUI",
        check: "true",
        beginTimeKey : "timestart",ei
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

</body>
</html>
