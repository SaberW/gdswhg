<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<% String path = request.getContextPath();%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>添加公文档案</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>

    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>

    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>

    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <!-- 图片上传相关-END -->
    <script type="text/javascript" src="${basePath}/static/plupload/upload-file.js"></script>

    <script type="text/javascript" src="${basePath}/static/common/js/area.js"></script>
</head>
<body>

<form id="whgff" class="whgff" method="post" >
    <h2>添加公文档案</h2>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>标题：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="title" value="${archive.title}"  style="width:500px; height:32px" data-options="required:true,prompt:'请输入内容', validType:'length[1,30]'"></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>类型：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary" id="etype" name="etype"></div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>格式：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" value="${archive.format}" id="format" name="format" js-data='[{"id":"1","text":"图片"},{"id":"2","text":"视频"},{"id":"3","text":"音频"},{"id":"4","text":"文档"}]'></div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>上传单位：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:245px; height:32px" name="cultid" id="cultid" data-options="required:true" />
        </div>
    </div>

    <div class="whgff-row picture_warp" style="display: none">
        <div class="whgff-row-label"><i>*</i>图片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture1" name="image" value="${archive.image}" />
            <div class="whgff-row-input-imgview" id="previewImg1"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>
    <div class="whgff-row video_wrap" style="display: none">
        <div class="whgff-row-label"><i>*</i>资源地址：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" name="entdir" id="video_entdir" style="height:35px;width:190px" data-options="prompt:'请选择目录',editable:true,limitToList:true,valueField:'id',textField:'text',url:'${basePath}/admin/video/srchPagging?srchDir=1&cultid=${param.cultid}'"/>
            <input class="easyui-combobox" name="enturl" id="video_enturl" style="height:35px;width:400px" data-options="prompt:'请选择音视频',editable:true,limitToList:true,valueField:'addr',textField:'key'"/>
        </div>
    </div>

    <div class="whgff-row doc_wrap" style="display: none">
        <div class="whgff-row-label">上传附件：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="whg_file_upload" name="doc_enturl" value="${archive.enturl}">
            <div class="whgff-row-input-fileview" id="whg_file_pload_view"></div>
            <div class="whgff-row-input-filefile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="fileUploadBtn2">选择附件</a></i>
                <i>附件格式为doc,docx,xls,xlsx,zip,pdf,建议文件大小为10MB以内</i>
            </div>
        </div>
    </div>

</form>

<div id="whgwin-add-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <div style="display: inline-block; margin: 0 auto">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="whgwin-add-btn-save">保 存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
    </div>
</div>

<!-- script -->
<script type="text/javascript">




    $(function () {
        //选择资源目录后级连资源地址
        $('#video_entdir').combobox({
            onChange: function (newv, oldv) {
                $('#video_enturl').combobox({
                    valueField:'addr',textField:'key',url:'${basePath}/admin/video/srchPagging?srchFile=1&dir='+encodeURIComponent(newv)
                });
            }
        });



        $("div[name='format']").on("click","input[name='format']", function(){
            var etype = $(this).val();
            if(etype == 1){//图片
                $(".picture_warp").show();
                $(".video_wrap").hide();
                $(".doc_wrap").hide();
            }else if(etype == 2){//视频
                $(".video_wrap").show();
                $(".picture_warp").hide();
                $(".doc_wrap").hide();
            }else if (etype == 3){//音频
                $(".video_wrap").show();
                $(".picture_warp").hide();
                $(".doc_wrap").hide();
            }else if (etype == 4){//文档
                $(".picture_warp").hide();
                $(".video_wrap").hide();
                $(".doc_wrap").show();
            }
        });
        $("div[name='format']").find("input[name='format']:checked").click();

        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'${expert.cultid}',
            ywiTypeEid:'etype', ywiTypeValue:'${expert.etype}', ywiTypeClass:5
        });

        <!--文件上传控件 -->
        WhgUploadFile.init({basePath: '${basePath}', uploadBtnId: 'fileUploadBtn2', hiddenFieldId: 'whg_file_upload',previewFileId:'whg_file_pload_view'});
        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1'});

        var show = "${show}";
        var id = "${id}";
        var frm = $("#whgff");
        //查看时的处理
        if (show){
            //取消表单验证
            frm.form("disableValidation");
            //组件设为只读
            $('.easyui-textbox').textbox('readonly');
            $('.easyui-combobox').combobox('readonly');
            $('.easyui-datetimebox').combobox('readonly');
            $('.easyui-datebox').combobox('readonly');
            $('.easyui-numberspinner').numberspinner('readonly');
            $("#imgUploadBtn1").hide();
            //处理选项点击不生效
            frm.find("input[type='checkbox'], input[type='radio']").on('click', function(){return false});
            //不显示提交 button
            $("#whgwin-add-btn-save").hide();
            return;
        }

        //定义表单提交
        var url = id ? "${basePath}/admin/fyi/archive/edit" : "${basePath}/admin/fyi/archive/add";

        $('#whgff').form({
            novalidate: true,
            url: url,
            onSubmit : function(param) {
                if (id){
                    param.id = id;
                }
                var _valid = $(this).form('enableValidation').form('validate')
                if(_valid) {
                    //图片必填
                    if($('#cult_picture1').val() == ""){
                        _valid = false;
                        $.messager.alert('提示', '请选择图片');
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


</script>

</body>
</html>
