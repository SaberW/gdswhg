<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<% String path = request.getContextPath();%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <c:choose>
        <c:when test="${not empty id and not empty targetShow}">
            <c:set var="pageTitle" value="查看主讲老师"></c:set>
        </c:when>
        <c:when test="${not empty id}">
            <c:set var="pageTitle" value="编辑主讲老师"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="添加主讲老师"></c:set>
        </c:otherwise>
    </c:choose>
    <title>${pageTitle}</title>
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

<form id="whgff" class="whgff" method="post" >

    <h2>${pageTitle}</h2>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>主讲人姓名：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="name" value="${per.name}"
                                            style="width:500px; height:32px"
                                            data-options="required:true, validType:'length[1,60]'"></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>性别：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="sex" value="${per.sex}" js-data='[{"id":"0","text":"女"},{"id":"1","text":"男"}]'></div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">学历：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="xueli" value="${per.xueli}"
                                            style="width:500px; height:32px" data-options="validType:'length[1,60]'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">单位：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="company" value="${per.company}"
                                            style="width:500px; height:32px" data-options="validType:'length[0,200]'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">职称：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="technical" value="${per.technical}"
                                            style="width:500px; height:32px" data-options="validType:'length[1,60]'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>出生日期：</div>
        <div class="whgff-row-input">
            <input class="easyui-datetimebox" name="birthstr"  value="${per.birthstr}"  style="width:200px; height:32px" data-options="required:true">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>上传照片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture1" name="image" value="${per.image}">
            <div class="whgff-row-input-imgview" id="previewImg1" ></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>所属文化馆：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:245px; height:32px" name="cultid" id="cultid" data-options="required:true" />
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>区域：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:150px; height:32px" id="province" name="province"
                   data-options="required:true,readonly:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name'<%--, data:__PROVINCE, onChange:myChangeProvince--%>"/>
            <input class="easyui-combobox" style="width:150px; height:32px" id="__CITY_ELE" name="city"
                   data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name'<%--, data:[], onChange:myChangeCity--%>"/>
            <input class="easyui-combobox" style="width:179px; height:32px" id="__AREA_ELE" name="area"
                   data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">专长：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="special" value="${per.special}" style="width:500px; height:32px" data-options="validType:'length[1,50]'"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>标签：</div>
        <div class="whgff-row-input">
            <div class="checkbox checkbox-primary" id="etag" name="etag"></div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>关键字：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" id="ekey" <%--name="ekey"--%> style="width:500px; height:32px" validType="notQuotes" data-options="multiple:true, editable:true, prompt:'请填写关键字'
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
        <div class="whgff-row-label">主讲人简历：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="teacherdesc" value="${per.teacherdesc}"  multiline="true" style="width:600px;height: 100px;"
                   data-options="validType:['length[1,1000]']">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>工作人员：</div>
        <div class="whgff-row-input">
            <script id="worker" name="worker" type="text/plain" style="width:700px; height:250px;"></script>
            </div>
    </div>
</form>

<div class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <a class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok">提 交</a>
    <a class="easyui-linkbutton whgff-but-clear" iconCls="icon-no" onclick="WhgComm.editDialogClose();">返 回</a>
</div>

<!-- script -->
<script type="text/javascript">
    var entid = '${entid}';
/*

    //省市区
    var province = '${per.province}';
    var city = '${per.city}';
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
                $('#__AREA_ELE').combobox('setValue', '${per.area}');
                window.__init_area = true;
            }
        });
    }  //省市区------END
*/


    //处理UE
    var ueConfig = {
        scaleEnabled:true,
        autoFloatEnabled: false,
        elementPathEnabled:false
    };
    var worker = UE.getEditor('worker', ueConfig);
    //UE 设置值

    worker.ready(function(){  worker.setContent('${per.worker}') });



    $(function () {

        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'${per.cultid}',
            ywiKeyEid:'ekey', ywiKeyValue:'${per.ekey}', ywiKeyClass:38,
            ywiTagEid:'etag', ywiTagValue:'${per.etag}', ywiTagClass:38

            ,provinceEid:'province', provinceValue:'${per.province}',
            cityEid:'__CITY_ELE', cityValue:'${per.city}',
            areaEid:'__AREA_ELE', areaValue:'${per.area}'

        });
/*

        if (!province || province==''){
            province = WhgComm.getProvince()?WhgComm.getProvince():'广东省';
        }
        $('#province').combobox('setValue', province);

        var city = '${info.tra.city}';
        if (!city || city==''){
            city = WhgComm.getCity()?WhgComm.getCity():"";
        }
        $("#__CITY_ELE").combobox('setValue', city); //初始省值
*/

        //图片初始化
        var __WhgUploadImg = WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1'});

        var id = '${id}';
        var targetShow = '${targetShow}';
        var frm = $("#whgff");
        var buts = $("div.whgff-but");

        //查看时的处理
        if (targetShow){
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
        var url = id ? "${basePath}/admin/supply/traper/edit" : "${basePath}/admin/supply/traper/add";
        frm.form({
            url: url,
            novalidate: true,
            onSubmit: function (param) {
                if (id){
                    param.id = id;
                }
                if (entid){
                    param.entid = entid;
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
                param.ekey = $("#ekey").combobox('getText');
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
        buts.find("a.whgff-but-submit").on('click', function () {
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
            //课程详情不能为空
            if (!worker.hasContents()){
                $.messager.alert("错误", '工作人员不能为空！', 'error');
                return false;
            }
            return true;
        }
    });

    </script>
<!-- script END -->
</body>
</html>
