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
    <title>编辑专家人才</title>
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
            <h2>查看专家人才</h2>
        </c:when>
        <c:otherwise>
            <h2>编辑专家人才</h2>
        </c:otherwise>
    </c:choose>
    <input type="hidden" name="id" id="id" value="${id}"/>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>名称：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="name" value="${cult.name}" style="width:500px; height:32px" data-options="required:true, validType:'length[1,30]'"></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>性别：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="sex" value="${cult.sex}" js-data='[{"id":"0","text":"女"},{"id":"1","text":"男"}]'></div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>所属文化馆：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:245px; height:32px" name="cultid" id="cultid" data-options="required:true" />
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">民族：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="family" value="${cult.family}" style="width:500px; height:32px" data-options="validType:'length[1,30]'"></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">身份证号：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="cardno" value="${cult.cardno}"  style="width:500px; height:32px" data-options=" validType:'length[1,30]'"></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">住址：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="address" value="${cult.address}" style="width:500px; height:32px" data-options="validType:'length[1,30]'"></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">联系方式：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="phoneno" value="${cult.phoneno}" style="width:500px; height:32px" data-options="validType:'length[1,30]'"></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">文艺职务：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="job" value="${cult.job}" style="width:500px; height:32px" data-options=" validType:'length[1,30]'"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>专家人才类型：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="type" value="${cult.type}"
                 js-data="WhgComm.getPerType">
            </div>
         </div>
        <%--<div class="whgff-row-input"><input class="easyui-combobox" name="type" value="${cult.type}" style="height:32px;width: 500px"
                                            data-options="editable:false,required:true, valueField:'id',textField:'text',prompt:'请选择专家人才类型', data:WhgSysData.getTypeData('16')"/></div>--%>
    </div>



    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>区域：</div>
        <div class="whgff-row-input">
            <%--<div class="radio radio-primary whg-js-data" name="area" value="${info.area}"
                 js-data="WhgComm.getAreaType">
            </div>--%>
            <input class="easyui-combobox" style="width:150px; height:32px" id="__PROVINCE_ELE" name="province" data-options="required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name', data:__PROVINCE,  onChange:changeProvince"/>
            <input class="easyui-combobox" style="width:150px; height:32px" id="__CITY_ELE" name="city" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name', data:[], onChange:changeCity"/>
            <input class="easyui-combobox" style="width:179px; height:32px" id="__AREA_ELE" name="area" data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">获奖情况：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="hjqk"  multiline="true" value="${cult.hjqk}" style="width:600px;height: 100px;"
                   data-options="validType:['length[1,400]']">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>上传照片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture1" name="picture" value="${cult.picture}">
            <div class="whgff-row-input-imgview" id="previewImg1"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>



    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>人才简介：</div>
        <div class="whgff-row-input">
            <script id="catalog" type="text/plain" style="width:700px; height:250px;"></script>
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
        ue_catalog.setContent('${cult.summary}')
    });
    $(function () {
        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'${cult.cultid}',
            ywiTypeEid:'type', ywiTypeValue:'${cult.type}', ywiTypeClass:25
        });
        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1'});
        debugger;

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
        var targetShow = '${targetShow}';
        var province = '${cult.province}';
        if (!province || province==''){
            province = '广东省';
        }
        $("#__PROVINCE_ELE").combobox("setValue", province);

        var city = '${cult.city}';
        if (city && city!=''){
            $("#__CITY_ELE").combobox("setValue", city);
        }
        var area = '${cult.area}';
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
    </script>
<!-- script END -->
</body>
</html>
