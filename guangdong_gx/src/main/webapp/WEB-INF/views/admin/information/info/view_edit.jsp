<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<!DOCTYPE html>
<html>
<html>
<head>
    <meta charset="UTF-8">
    <title>栏目内容管理</title>

    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>

    <script src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>

    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>

    <script type="text/javascript" src="${basePath}/static/common/js/area.js"></script>

</head>
<body class="body_add">
<form id="whgff" method="post" class="whgff">
    <h2>资讯979公告管理</h2>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>类型：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" value="${type}" name="type"
                 js-data='[{"id":"1","text":"资讯"}<%--,{"id":"2","text":"公告"}--%>]'></div>
        </div>
    </div>

    <div class="whgff-row" refField="clnftltle">
        <div class="whgff-row-label"><i>*</i>标题：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="clnftltle" value="${info.clnftltle}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,200]'], prompt:'请输入标题'">
        </div>
    </div>

    <div class="whgff-row" refField="clnfsource">
        <div class="whgff-row-label"><i>*</i>来源：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="clnfsource" value="${info.clnfsource}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,200]'], prompt:'请输入来源'">
        </div>
    </div>

    <div class="whgff-row" refField="clnfauthor">
        <div class="whgff-row-label"><i>*</i>作者：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="clnfauthor" value="${info.clnfauthor}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,200]'], prompt:'请输入作者'">
        </div>
    </div>

    <div class="whgff-row" refField="clnvenueid">
        <div class="whgff-row-label"><i>*</i>文化馆：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:245px; height:32px" name="clnvenueid" id="clnvenueid" data-options="required:true" />
            <input class="easyui-combobox" style="width:245px; height:32px" name="deptid" id="deptid" data-options="required:true" />
        </div>
    </div>
    <div class="whgff-row"  refField="area">
        <div class="whgff-row-label">
            <label style="color: red">*</label>区域：
        </div>
        <input class="easyui-combobox" style="width:150px; height:32px" id="province" name="province" data-options="required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name'<%--, data:__PROVINCE, onChange:myChangeProvince--%>"/>
        <input class="easyui-combobox" style="width:150px; height:32px" id="__CITY_ELE" name="city" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name'<%--, data:[], onChange:myChangeCity--%>"/>
        <input class="easyui-combobox" style="width:179px; height:32px" id="__AREA_ELE" name="area" data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
    </div>

    <div class="whgff-row" refField="clnfkey">
        <div class="whgff-row-label">关键字：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" id="clnfkey" name="clnfkey" style="width:500px; height:32px" validType="notQuotes"
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
        <div class="whgff-row-label"><i>*</i>提交类型：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="ispub" value="${info.ispub}" js-data='[{"id":"1","text":"直接发布"},{"id":"2","text":"提交为编辑"}]'></div>
            <span>（如选择直接发布，该信息将会直接发布到前台进行展示，可进行浏览！）</span>
        </div>
    </div>

    <div class="whgff-row" refField="clnfcrttime">
        <div class="whgff-row-label">创立时间：</div>
        <div class="whgff-row-input">
            <input type="text" class="easyui-datetimebox" style="width:300px;height: 32px;" name="clnfcrttime" value="${info.clnfcrttime}" data-options="editable:false,prompt:'请选择'"/>
        </div>
    </div>

    <div class="whgff-row" refField="clnfintroduce">
        <div class="whgff-row-label">简介：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="clnfintroduce" value="${info.clnfintroduce}" multiline="true" style="width:600px;height: 100px;"
                   data-options="required:false,validType:['length[1,400]']">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">详细介绍：</div>
        <div class="whgff-row-input">
            <script id="clnfdetail" name="clnfdetail" type="text/plain" style="width:600px; height:200px;"></script>

        </div>
    </div>

    <div class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
        <a class="easyui-linkbutton whgff-but-submit" id="submitButton" iconCls="icon-ok">提 交</a>
        <a class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo">返 回</a>
    </div>

</form>



<script>
    //省市区
    /*var province = WhgComm.getProvince()?WhgComm.getProvince():'广东省';
    var city = WhgComm.getCity()?WhgComm.getCity():"";
    function myChangeProvince(newVal, oldVal) {
        changeProvince(newVal, oldVal, function(){
            if(typeof(window.__init_city) == 'undefined'){
                $('#__CITY_ELE').combobox('setValue', '${info.city}');
                window.__init_city = true;
            }
        });
    }

    function myChangeCity(newVal, oldVal) {
        changeCity(newVal, oldVal, function(){
            if(typeof(window.__init_area) == 'undefined'){
                $('#__AREA_ELE').combobox('setValue', '${info.area}');
                window.__init_area = true;
            }
        });
    }*/  //省市区------END

    //处理UE
    var ueConfig = {
        scaleEnabled: false,
        autoFloatEnabled: false,
        readonly: '${targetShow}'? true: false
    };

    $(function(){
        var frm = $("#whgff");
        var buts = $("div.whgff-but");
        var targetShow = '${targetShow}';
        //$('#province').combobox('setValue', province);
        WhgComm.initPMS({
            basePath:'${basePath}',
            deptEid:'deptid', deptValue:'${info.deptid}',
            cultEid:'clnvenueid', cultValue:'${info.clnvenueid}',
            ywiKeyEid:'clnfkey', ywiKeyValue:'${info.clnfkey}', ywiKeyClass:25

            ,provinceEid:'province', provinceValue:'${info.province}',
            cityEid:'__CITY_ELE', cityValue:'${info.city}',
            areaEid:'__AREA_ELE', areaValue:'${info.area}'
        });

        var ue_description = UE.getEditor('clnfdetail', ueConfig);
        ue_description.ready(function () {
            ue_description.setContent('${info.clnfdetail}')
        });


        //返回
        buts.find("a.whgff-but-clear").linkbutton({
            onClick: function () {
                window.parent.$('#whgdg').datagrid('reload');
                WhgComm.editDialogClose();
            }
        });
        //查看处理
        if (targetShow) {
            //取消表单验证
            frm.form("disableValidation");
            buts.find("a.whgff-but-submit").hide();
            $('.easyui-textbox').textbox('readonly');
            //$('.easyui-combobox').combobox('readonly');
            $('.easyui-datetimebox').combobox('readonly');
            $('.easyui-numberspinner').combobox('readonly');
            $('#submitButton').hide();
            frm.find("input[type='checkbox']").on('click', function () {
                return false
            });
            frm.find("input[type='radio']").attr('readonly', true);
            return;
        }
        //表单提交处理
        var url = "${basePath}/admin/info/edit";
        frm.form({
            url: url,
            novalidate: true,
            onSubmit: function (param) {
                param.entityid = "${entityid}";
                param.entity = "${entity}";
                param.clnfid = "${clnfid}";
               // param.clnfdetail = ue_description.getContent();
                $(this).form("enableValidation");
                var isValid = $(this).form('validate');
                if (!isValid){
                    $.messager.progress('close');
                    oneSubmit();
                }
                return isValid;
            },
            success: function (data) {
                data = $.parseJSON(data);
                $.messager.progress('close');
                oneSubmit();

                if (!data.success || data.success != "1"){
                    $.messager.alert("错误", data.msg||'操作失败', 'error');
                    return;
                }
                window.parent.$('#whgdg').datagrid('reload');
                WhgComm.editDialogClose();
            }
        });

        //处理表单提交
        function submitForm(){
            $.messager.progress();
            frm.submit();
        }

        //处理重复点击提交
        function oneSubmit(){
            buts.find("a.whgff-but-submit").off('click').one('click', submitForm);
        }
        oneSubmit();

    });
</script>

</body>
</html>