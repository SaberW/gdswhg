<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <c:choose>
        <c:when test="${reftype eq '25'}">
            <c:set var="pageTitle" value="作品"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="资源"></c:set>
        </c:otherwise>
    </c:choose>

    <title>添加${pageTitle}</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>
    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <!-- 图片上传相关-END -->

    <script type="text/javascript" src="${basePath}/static/plupload/upload-file.js"></script>
</head>
<body>

<form id="whgff" class="whgff" method="post">

    <h2>添加${pageTitle}</h2>
    <input type="hidden" name="reftype" id="reftype" value="${reftype}">

    <c:if test="${reftype eq '25'}">
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>艺术分类：</div>
        <div class="whgff-row-input" data-check="true" target="arttype" err-msg="请至少选择一个艺术分类">
            <div class="checkbox checkbox-primary" id="arttype" name="arttype"></div>
        </div>
    </div>
    </c:if>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>资源类型：</div>
        <select id="cc" class="easyui-combobox" name="enttype" panelHeight="auto" style="width:600px; height:32px" data-options="editable:true, limitToList:true,required:true,prompt:'请选择资源类型',value:'1'">
            <option value="1">图片</option>
            <option value="2">视频</option>
            <option value="3">音频</option>
            <option value="4">文档</option>
        </select>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>${pageTitle}名称：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="name" style="width:600px; height:32px" data-options="required:true, validType:'length[1,60]'"></div>
    </div>
    <div class="whgff-row video_wrap" style="display: none">
        <div class="whgff-row-label"><i>*</i>${pageTitle}地址：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" name="entdir" id="video_entdir" style="height:35px;width:190px" data-options="prompt:'请选择目录',editable:true,limitToList:true,valueField:'id',textField:'text',url:'${basePath}/admin/video/srchPagging?srchDir=1&cultid=${param.cultid}'"/>
            <input class="easyui-combobox" name="enturl" id="video_enturl" style="height:35px;width:400px" data-options="prompt:'请选择音视频',editable:true,limitToList:true,valueField:'addr',textField:'key'"/>
        </div>
    </div>

    <div class="whgff-row doc_wrap" style="display: none">
        <div class="whgff-row-label"><i>*</i>上传附件：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="whg_file_upload" name="doc_enturl">
            <div class="whgff-row-input-fileview" id="whg_file_pload_view"></div>
            <div class="whgff-row-input-filefile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="fileUploadBtn2">选择附件</a></i>
                <i>附件格式为doc,docx,xls,xlsx,zip,pdf,建议文件大小为10MB以内</i>
            </div>
        </div>
    </div>

    <div class="whgff-row video_wrap" style="display: none">
        <div class="whgff-row-label"><i></i>视频/音频时长：</div>
        <div class="whgff-row-input"><input class="easyui-timespinner" name="enttimes" style="width:300px; height:32px" data-options="showSeconds:true" prompt="请选择资源时长"></div>
    </div>

    <div class="whgff-row picture_warp">
        <div class="whgff-row-label"><i>*</i>${pageTitle}图片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture2" name="penturl" >
            <div class="whgff-row-input-imgview" id="previewImg2"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn2">选择图片</a></i>
                <i>图片格式为jpg、png、gif，大小为2MB以内</i>
            </div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">${pageTitle}简介：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="summary" style="width:500px; height:100px" multiline="true"
                   data-options="required:false,validType:['length[1,400]']"></div>
    </div>
    <div class="whgff-row video_wrap" id="spfm" style="display: none">
        <div class="whgff-row-label"><i>*</i>视频封面图片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture1" name="deourl" >
            <div class="whgff-row-input-imgview" id="previewImg1"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>

</form>

<div id="whgwin-add-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok" id="whgwin-add-btn-save">提 交</a>
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
</div>

<!-- script -->
<script type="text/javascript">
    //初始艺术分类
    function initArtType(eid, values){
        $.get(basePath +'/admin/yunwei/type/srchList', {type:1, cultid:managerCults[0].id}, function(data){
            if($.isArray(data)){
                var _values = [];
                if(values && values != ''){
                    _values = values.split(',');
                }
                var _type = 'radio', __cls = '';
                if ($('#'+eid).hasClass("checkbox")){
                    _type = "checkbox";
                    __cls = 'class="styled"';
                }
                var html = '';
                for(var i=0; i<data.length; i++){
                    var row = data[i];
                    var _name = $('#'+eid).attr('name');
                    var ischecked = $.inArray(row.id, _values) > -1 ? ' checked="checked"' : '';
                    html += '<input type="'+_type+'" id="'+_name+"_"+row.id+'" name="'+_name+'" value="'+row.id+'" '+__cls+' '+ischecked+'>';
                    html += '<label for="'+_name+"_"+row.id+'">'+row.name+'</label>';
                }
                $('#'+eid).html(html);
            }
        });
    }

    $(function () {
        //专家作品-初始艺术分类
        if($('#reftype').val() == '25')initArtType('arttype');

        //选择资源目录后级连资源地址
        $('#video_entdir').combobox({
            onChange: function (newv, oldv) {
                $('#video_enturl').combobox({
                    valueField:'addr',textField:'key',url:'${basePath}/admin/video/srchPagging?srchFile=1&dir='+encodeURIComponent(newv)
                });
            }
        });

        //根据下拉框的值进行显示隐藏操作
        $('#cc').combobox({
            onChange: function(newv,oldv) {
                if(newv == 1){//图片
                    $(".picture_warp").show();
                    $(".video_wrap").hide();
                    $(".doc_wrap").hide();
                }else if(newv == 2){//视频
                    $(".video_wrap").show();
                    $(".picture_warp").hide();
                    $(".doc_wrap").hide();
                }else if (newv == 3){//音频
                    $(".video_wrap").show();
                    $("#spfm").hide();
                    $(".picture_warp").hide();
                    $(".doc_wrap").hide();
                }else if (newv == 4){//文档
                    $(".picture_warp").hide();
                    $(".video_wrap").hide();
                    $(".doc_wrap").show();

                }
            }
        });

        <!--文件上传控件 -->
        WhgUploadFile.init({basePath: '${basePath}', uploadBtnId: 'fileUploadBtn2', hiddenFieldId: 'whg_file_upload',previewFileId:'whg_file_pload_view'});
        //初始图片上传
        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1',needCut:true});
        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn2', hiddenFieldId: 'cult_picture2', previewImgId: 'previewImg2',needCut:false});

        //初始表单
        $('#whgff').form({
            novalidate: true,
            url : getFullUrl('/admin/resource/add?refid=${refid}'),
            onSubmit : function(param) {
                var _valid = $(this).form('enableValidation').form('validate');
                if(_valid && $('#reftype').val() == '25' && (!$("input[name='arttype']:checked").val())){
                    _valid = false;
                    $.messager.alert("提示", '请选择艺术分类');
                }else if(_valid && $("[name=enttype]").val() == ""){
                    _valid = false;
                    $.messager.alert('提示', '请选择${pageTitle}类型');
                }else if(_valid && $("[name=enttype]").val() != 1 && $("[name=enttype]").val() != 4 && $("[name = enturl]").val() == ""){
                    _valid = false;
                    $.messager.alert('提示', '请选择${pageTitle}地址');
                }else if(_valid && $("[name=enttype]").val() == 1 && $("[name = penturl]").val() == ""){
                    _valid = false;
                    $.messager.alert('提示', '请选择${pageTitle}图片');
                }else if(_valid && $("[name=enttype]").val() == 2 && $("[name = deourl]").val() == ""){
                    _valid = false;
                    $.messager.alert('提示', '请选择视频封面');
                }else if(_valid && $("[name=enttype]").val() == 4 && $("#whg_file_upload").val() == ""){
                    _valid = false;
                    $.messager.alert('提示', '请选择文档');
                }
                if(_valid){
                    $.messager.progress();
                }else{
                    //失败时再注册提交事件
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
                    $.messager.alert('提示', '操作失败！', 'error');
                    $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
                }
            }
        });
        //注册提交事件
        $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
    });
</script>
<!-- script END -->
</body>
</html>