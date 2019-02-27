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
            <c:set var="pageTitle" value="文艺演出管理-查看文艺演出"></c:set>
        </c:when>
        <c:when test="${pageType eq '1'}">
            <c:set var="pageTitle" value="文艺演出管理-编辑文艺演出"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="文艺演出管理-添加文艺演出"></c:set>
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
            <input class="easyui-textbox" name="title" value="${info.goods.title}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,100]'], prompt:'请输入文艺演出名称'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>图片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture1" name="image" value="${info.goods.image}">
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
            <input class="easyui-combobox" style="width:245px; height:32px" name="cultid" id="cultid" data-options="required:true" />
            <input class="easyui-combobox" style="width:245px; height:32px" name="deptid" id="deptid" data-options="editable:false,required:true" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>区域：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:150px; height:32px" id="province" name="province" data-options="required:true,readonly:true,prompt:'请选择省', limitToList:true, valueField:'name', textField:'name', data:__PROVINCE, onChange:myChangeProvince"/>
            <input class="easyui-combobox" style="width:150px; height:32px" id="__CITY_ELE" name="city" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name', data:[], onChange:myChangeCity"/>
            <input class="easyui-combobox" style="width:179px; height:32px" id="__AREA_ELE" name="area" data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>演出类型：</div>
        <div class="whgff-row-input">
            <%--<div class="radio radio-primary" id="type" name="type"></div>--%>
                <div class="checkbox checkbox-primary" id="type" name="type"></div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>艺术分类：</div>
        <div class="whgff-row-input">
            <div class="checkbox checkbox-primary" id="arttype" name="arttype"></div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>关键字：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" id="ekey" name="ekey" style="width:500px; height:32px" validType="notQuotes" data-options="required:true,multiple:true, editable:true, prompt:'请填写关键字'
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
        <div class="whgff-row-label"><i>*</i>服务类型：</div>
        <div class="whgff-row-input">
            <%--<div class="radio radio-primary" id="fwtype" name="fwtype"></div>--%>
            <div class="checkbox checkbox-primary" id="fwtype" name="fwtype"></div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>演出方式：</div>
        <div class="whgff-row-input">
            <%--<div class="radio radio-primary" id="showway" name="showway"></div>--%>
            <div class="checkbox checkbox-primary" id="showway" name="showway"></div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>演出时长：</div>
        <div class="whgff-row-input">
            <input class="easyui-numberbox" name="showtime" value="${info.goods.showtime}" style="width:300px; height:32px"
                   data-options="required:true,min:0,max:1000000, prompt:'请输入演出时长'">  分钟
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>表演人数：</div>
        <div class="whgff-row-input">
            <input class="easyui-numberbox" name="shownum" value="${info.goods.shownum}" style="width:300px; height:32px"
                   data-options="required:true,min:0,max:1000000, prompt:'请输入表演人数'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>表演人员：</div>
        <div class="whgff-row-input">
            <select class="easyui-combobox" id="showperson" name="showperson" style="width:600px; height:32px"
                    data-options="required:true,multiple:true,value:'${info.goods.showperson}'" ></select>
            (如需手动输入，请用英文逗号隔开！)
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>演出机构：</div>
        <div class="whgff-row-input">
            <select class="easyui-combobox select-cultid" id="exhorgan" name="organ" value="${info.goods.organ}" style="width:500px; height:32px" prompt="请选择所属机构"
                    data-options="multiple:true, editable:false, required:true,value:'${info.goods.organ}'"></select>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>装台时长：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="fixtime" value="${info.goods.fixtime}" style="width:600px; height:32px"
                   data-options="required:true, validType:['length[1,100]'], prompt:'请输入装台时长'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>合适人群：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="fitcrowd" value="${info.goods.fitcrowd}" style="width:600px; height:32px"
                   data-options="required:true, validType:['length[1,100]'], prompt:'请输入合适人群'">
        </div>
    </div>

    <%--<div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>是否收费：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="ismoney" value="${info.goods.ismoney}"
                 js-data='[{"id":"0","text":"不收费"},{"id":"1","text":"收费"}]'>
            </div>
        </div>
    </div>--%>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>联系人：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="contacts" value="${info.goods.contacts}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,100]'], prompt:'请输入联系人'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>联系电话：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="phone" value="${info.goods.phone}" style="width:500px; height:32px" data-options="required:true, validType:'isPhone[\'phone\']',prompt:'请填写联系方式'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>邮箱：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="email" value="${info.goods.email}" style="width:500px; height:32px"
                   data-options="required:true,validType:['length[1,20]', 'isEmail'], prompt:'请输入邮箱'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">所属单位：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="workplace" value="${info.goods.workplace}" style="width:300px; height:32px"
                   data-options="required:false, prompt:'请输入所属单位'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">固定电话：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="telephone" value="${info.goods.telephone}" style="width:300px; height:32px"
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
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>说明：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="showexplain" value="${not empty info.goods.showexplain? info.goods.showexplain :'一旦达成意向，经费参照国家和地方相关标准执行，相关具体事宜由派出方与承接方相互协商而定。'}" style="width:500px; height:60px"
                   data-options="multiline:true,required:true,validType:['length[1,500]'], prompt:'请输入演出说明'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>表演简介：</div>
        <div class="whgff-row-input">
            <div id="showdesc" name="showdesc" type="text/plain" style="width:800px; height:200px;"></div>
            <textarea id="value_showdesc" style="display: none;">${info.goods.showdesc}</textarea>
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
   <%-- <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>节目单：</div>
        <div class="whgff-row-input">
            <script id="playbill" name="playbill" type="text/plain" style="width:800px; height:200px;"></script>
            <textarea id="value_playbill" style="display: none;">${info.goods.playbill}</textarea>
        </div>
    </div>--%>
</form>

<div class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <a class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok">提 交</a>
    <a class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose();">返 回</a>
</div>

<script>
    var province = '${info.goods.province}';
    var city = '${info.goods.city}';
    //省市区
    function myChangeProvince(newVal, oldVal) {
        changeProvince(newVal, oldVal, function(){
            if(typeof(window.__init_city) == 'undefined'){
                if(!city || city==''){
                    city = WhgComm.getCity()?WhgComm.getCity():"";
                }
                $('#__CITY_ELE').combobox('setValue', city);
                window.__init_city = true;
            }
        });
    }

    function myChangeCity(newVal, oldVal) {
        changeCity(newVal, oldVal, function(){
            if(typeof(window.__init_area) == 'undefined'){
                $('#__AREA_ELE').combobox('setValue', '${info.goods.area}');
                window.__init_area = true;
            }
        });
    }  //省市区------END


    //图片初始化
    var __WhgUploadImg = WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1'});

    //处理UE
    var ueConfig = {
        scaleEnabled:true,
        autoFloatEnabled: false,
        elementPathEnabled:false,
        readonly: '${pageType}' == 2? true: false
    };
    var showdesc = UE.getEditor('showdesc', ueConfig);
   // var playbill = UE.getEditor('playbill', ueConfig);

    //UE 设置值
    showdesc.ready(function(){  showdesc.setContent($("#value_showdesc").val()) });
   // playbill.ready(function(){  playbill.setContent($("#value_playbill").val()) });


    $(function () {
        /*var aa = '${info.times}';
        $("#timesUI").html("");
        timesUI.init();
        if(aa){
            timesUI.setValue( JSON.parse(aa) );
        }*/
        var state = '${info.goods.state}';
        if (state && state == 4) {
            showXjReason('${info.goods.id}');
        }
        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'${info.goods.cultid}',cultOnChange: function (newVal, oldVal) {
                $("#showperson").combobox({
                    url:'${basePath}/admin/personnel/srchList?cultid='+newVal,
                    valueField:'id',
                    textField:'name'
                })
                $("#exhorgan").combobox({
                    url:'${basePath}/admin/showOrgan/srchList?cultid='+newVal,
                    valueField:'id',
                    textField:'title'
                })
            },
            deptEid:'deptid', deptValue:'${info.goods.deptid}',
            ywiTypeEid:'type', ywiTypeValue:'${info.goods.type}', ywiTypeClass:21,
            ywiTypeEid:['type', 'fwtype', 'showway'], ywiTypeValue:['${info.goods.type}','${info.goods.fwtype}','${info.goods.showway}'], ywiTypeClass:[21,37,211],
            ywiKeyEid:'ekey', ywiKeyValue:'${info.goods.ekey}', ywiKeyClass:21,
            ywiArtTypeEid:'arttype', ywiArtTypeValue:'${info.goods.arttype}',
            ywiKeyEid:'ekey', ywiKeyValue:'${info.goods.ekey}', ywiKeyClass:21
            /*,psprovinceEid:'__PSPROVINCE_ELE', psprovinceValue:'${info.tra.psprovince}',
            pscityEid:'__PSCITY_ELE', pscityValue:'${info.tra.pscity}'*/

        });
        $("#ekey").combobox("setValue","${info.goods.ekey}");
        //初始省值
        // $('#province').combobox('setValue', '${info.goods.province}');
        if (!province || province==''){
            province = WhgComm.getProvince()?WhgComm.getProvince():'广东省';
        }
        $('#province').combobox('setValue', province);

        var city = '${info.goods.city}';
        if (!city || city==''){
            city = WhgComm.getCity()?WhgComm.getCity():"";
        }
        $("#__CITY_ELE").combobox('setValue', city); //初始省值




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
        var url = id ? "${basePath}/admin/showGoods/edit" : "${basePath}/admin/showGoods/add";
        frm.form({
            url: url,
            novalidate: true,
            onSubmit: function (param) {
                if (id){
                    param.id = id;
                }
                $(this).form("enableValidation");
                var isValid = $(this).form('validate');
                //if(isValid){
                    //图片不能为空
                    var picture1 = $("#cult_picture1").val();
                    if (isValid && !picture1){
                        $.messager.alert("错误", '图片不能为空！', 'error');
                        isValid = false;
                    }
//                    var type = $("[name='type']:radio:checked");
                var type = $("[name='type']:checkbox:checked");
                    if (isValid && (!type || type.size()==0)){
                        $.messager.alert("错误", '演出类型不能为空！', 'error');
                        isValid = false;
                    }
                    var arttypes = $("[name='arttype']:checkbox:checked");
                    if (isValid && (!arttypes || arttypes.size()==0)){
                        $.messager.alert("错误", '艺术分类不能为空！', 'error');
                        isValid = false;
                    }
                    var fwtypes = $("[name='fwtype']:checkbox:checked");
                    if (isValid && (!fwtypes || fwtypes.size()==0)){
                        $.messager.alert("错误", '服务类型不能为空！', 'error');
                        isValid = false;
                    }
                    var showways = $("[name='showway']:checkbox:checked");
                    if (isValid && (!showways || showways.size()==0)){
                        $.messager.alert("错误", '演出方式不能为空！', 'error');
                        isValid = false;
                    }
                    if (isValid && !showdesc.hasContents()){
                        $.messager.alert("错误", '表演简介不能为空', 'error');
                        isValid = false;
                    }
                //}
                if (!isValid){
                    $.messager.progress('close');
                    buts.find("a.whgff-but-submit").off('click').one('click', function () {
                        frm.submit();
                    });
                }
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
    })


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

</body>
</html>
