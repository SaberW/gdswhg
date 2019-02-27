<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>群文资源库-添加</title>
    <!-- 编辑表单 -->
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap-theme.min.css"/>
    <%--<script type="text/javascript" src="${basePath}/static/bootstrap/js/bootstrap.min.js"></script>--%>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-file.js"></script>
    <!-- 图片上传相关-END -->
    <script src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
    <script type="text/javascript" src="${basePath}/static/admin/js/mass-library-custom-form.js"></script>
    <script type="text/javascript" src="${basePath}/static/admin/js/mass-library-custom-field.js"></script>
    <script type="text/javascript" src="${basePath}/static/admin/js/mass-library-custom-comm.js"></script>

    <script type="text/javascript" src="${basePath}/static/common/js/area.js"></script>
    <script type="text/javascript" src="${basePath}/static/common/js/whg.maps.js"></script>

    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>
    <link rel="stylesheet" href="${basePath}/static/Font-Awesome/css/font-awesome.min.css"/>


    <style>
        div.opreation{ margin: 5px; display: block; }
        div.form-row{ border: 1px dashed lightgrey; padding: 10px 0; position: relative; }
        div.row-opreation{ position: absolute; float: left; top:0; left: 0; }
        i{ color: red; font-weight: bold; }
    </style>
    <script type="text/javascript">
        $.extend($.fn.validatebox.defaults.rules, {
            isTableName: {
                validator: function(value, param){
                    //$.fn.validatebox.defaults.rules.isTableName.message = '只能是6-16个长度的英文小写字母.';
                    return /^[a-z][a-z0-9]{3,15}$/.test(value);
                },
                message: '只能是字母开头的4-16个长度的英文小写字母或数字.'
            }
        });
    </script>
</head>
<body style="overflow-x: hidden">
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand">群文管理</a>
            <a class="navbar-brand"> &gt;&nbsp;群文资源库</a>
            <a class="navbar-brand"> &gt;&nbsp;添加</a>
        </div>
    </div>
</nav>


<div class="container-fluid">
    <form class="form-horizontal" role="form" id="whgff" method="post">
        <fieldset>
            <legend>基本配置</legend>
            <input type="hidden" id="id" name="id" value="${library.id}"/>

            <div class="form-group">
                <label class="col-sm-2 control-label" for="resourcetype"><i>*</i>资源类型：</label>
                <div class="col-sm-4">
                    <input class="easyui-combobox" style="width: 100%; height: 34px;" id="resourcetype" name="resourcetype" value="${param.resourcetype}" data-options="prompt:'请选择资源类型',required:true,editable:false,readonly:true,valueField:'id',textField:'text',data:WhgComm.getResourceTypeData()" />
                </div>
                <label class="col-sm-2 control-label" for="cultid"><i>*</i>所属文化馆：</label>
                <div class="col-sm-4">
                    <input class="easyui-combobox" style="width: 100%; height:32px" name="cultid" id="cultid" data-options="required:true" />
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label" for="cultid"><i>*</i>所属部门：</label>
                <div class="col-sm-4">
                    <input class="easyui-combobox" style="width: 100%; height:32px" name="deptid" id="deptid" data-options="required:true" />
                </div>
                <label class="col-sm-2 control-label" for="name"><i>*</i>资源库名称：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" style="width: 100%; height: 34px;" id="name" name="name" data-options="prompt:'请输入资源库名称，长度不能超过120个字符',required:true,validType:'length[1,120]'" />
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label" for="tablename"><i>*</i>资源库代码：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" style="width: 100%; height: 34px;" id="tablename" name="tablename" value="" data-options="prompt:'资源库代码，只能是字母开头的4-16长度的英文小写字母或数字',required:true,validType:'isTableName'" />
                </div>
                <label class="col-sm-2 control-label" for="idx"><i>*</i>排序值：</label>
                <div class="col-sm-4">
                    <input class="easyui-numberspinner" style="width: 100%; height: 34px;" id="idx" name="idx" data-options="prompt:'请输入资源库排序值，在展示时按升序排列',required:true,min:1,max:999" />
                </div>
            </div>



            <div class="form-group">
                <label class="col-sm-2 control-label"><i>*</i>艺术类型：</label>
                <%--<div class="col-sm-4">
                    <div class="whgff-row-input">
                        <div class="checkbox checkbox-primary" id="arttype" name="arttype"></div>
                    </div>
                </div>--%>
                <div class="whgff-row" style="float: left;width: 75%;overflow: hidden;padding-left:20px;position: relative;">
                    <div class="whgff-row-input">
                        <div class="checkbox checkbox-primary" id="arttype" name="arttype" data-options="required:true" ></div>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label"><i>*</i>地区：</label>
                <div class="col-sm-4">
                    <div class="whgff-row-input">
                        <input class="easyui-combobox" style="width:125px; height:32px" id="province" name="province" data-options="readonly:true, required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name'<%--, data:__PROVINCE, onChange:changeProvince--%>"/>
                        <input class="easyui-combobox" style="width:150px; height:32px" id="__CITY_ELE" name="city" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name'<%--, data:[], onChange:changeCity--%>"/>
                        <input class="easyui-combobox" style="width:179px; height:32px" id="__AREA_ELE" name="area" data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
                    </div>
                </div>
                <label class="col-sm-2 control-label">民族语言：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" style="width: 100%; height: 34px;" id="nationallanguage" name="nationallanguage" value="${library.nationallanguage}" data-options="" />
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">标签：</label>
                <div class="col-sm-4">
                    <div class="whgff-row-input">
                        <input class="EEE-combobox" id="tags" name="etags" style="width:300px; height:32px" validType="notQuotes"
                               data-options="multiple:true, prompt:''
                                ,onChange: function (val, oldval) {
                                    if (val.length>1 && val[0]==''){
                                        val.shift();
                                        $(this).combobox('setValues', val);
                                    }
                                }"
                        />
                        <span class="btn btn-success btn-sm"  onclick="javascript:addTag()">添加标签</span>
                    </div>
                    <div class="clearfix" id="select_tags" style="text-align: left;margin-top: 10px;">
                        <ul style="text-align: left;padding: 0;list-style: none;">
                            <c:forEach items="${tagList}" var="tag">
                                <li id="${tag.name}" style="float: left;position: relative;margin-right: 10px;margin-bottom: 10px;"><input type="hidden" name="tags" value="${tag.id}">
                                    <label class="btn btn-default btn-block" style="padding-right: 22px;">${tag.name}</label>
                                    <a href="javascript:void(0)" class="textbox-icon icon-clear" icon-index="0" tabindex="-1" style="width: 18px; height: 30px;position:absolute;top: 2px;right: 2px;"></a>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>

            </div>


            <div class="form-group">
                <label class="col-sm-2 control-label"><i>*</i>封面图片：</label>
                <div class="col-sm-4">
                    <div class="whgff-row-input">
                        <input type="hidden" id="library_imgurl" name="imgurl" value="${library.imgurl}" >
                        <div class="whgff-row-input-imgview" id="previewImg1" style="height: 200px; width: 300px; border: 2px dashed #ccc; border-radius: 10px; text-align: center; overflow: hidden;"></div>
                        <div class="whgff-row-input-imgfile" id="divImgFile">
                            <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                            <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
                        </div>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">资源库版权所有者：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" style="width: 100%; height: 34px;" id="copyright" name="copyright" value="${library.copyright}" data-options="" />
                </div>

                <label class="col-sm-2 control-label">资源库建设单位：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" value="${library.constructionunit}" style="width:100%; height:32px" name="constructionunit" id="constructionunit" data-options="" />
                </div>
            </div>


            <div class="form-group">
                <label class="col-sm-2 control-label" for="memo">资源库简介：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" style="width: 100%; height: 100px;" id="memo" name="memo" data-options="prompt:'请输入资源库简介',multiline:true,validType:'length[0,500]'" />
                </div>
            </div>
        </fieldset>

        <fieldset>
            <legend>资源库公共表单项<span class="glyphicon glyphicon-chevron-down" id="btn_down"></span><span class="glyphicon glyphicon-chevron-up" id="btn_up"></span></legend>

            <!-- 图片资源-显示图片 -->
            <c:if test="${param.resourcetype == 'img'}">
            <div class="form-group">
                <label class="col-sm-2 control-label"><i>*</i>封面图片：</label>
                <div class="col-sm-10">
                    <div class="custom-field custom-field-imginput">
                        <div class="whgff-row-input-imgview" style="height: 200px; width: 300px; border: 2px dashed #ccc; border-radius: 10px; text-align: center; overflow: hidden;"></div>
                    </div>
                </div>
            </div>
            </c:if>

            <!-- 视频资源-需要上传图片做为封面 -->
            <c:if test="${param.resourcetype == 'video'}">
            <div class="form-group">
                <label class="col-sm-2 control-label"><i>*</i>封面图片：</label>
                <div class="col-sm-10">
                    <div class="custom-field custom-field-imginput">
                        <input type="hidden" id="respicture" value="">
                        <div class="whgff-row-input-imgview" id="preview_respicture" style="height: 200px; width: 300px; border: 2px dashed #ccc; border-radius: 10px; text-align: center; overflow: hidden;"></div>
                        <div class="whgff-row-input-imgfile">
                            <i><button type="button" class="btn btn-default btn-sm" id="uploadBtn_respicture"><span class="glyphicon glyphicon-folder-open"></span>&nbsp;选择图片</button></i>
                        </div>
                    </div>
                </div>
            </div>
            </c:if>

            <div class="form-group">
                <label class="col-sm-2 control-label"><i>*</i>资源名称：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" style="width: 100%; height: 34px;" id="resname" data-options="readonly:true,prompt:'请输入资源名称，长度不能超过120个字符',required:false,novalidate:true,validType:'length[1,120]'" />
                </div>
                <label class="col-sm-2 control-label">资源作者：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" style="width: 100%; height: 34px;" id="resauthor" data-options="readonly:true,prompt:'请输入资源作者，长度不能超过32个字符',required:false,novalidate:true,validType:'length[1,32]'" />
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">艺术分类：</label>
                <div class="col-sm-10">
                    <div style="margin-top: 7px;" id="div_resarttype"></div>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">标签：</label>
                <div class="col-sm-10">
                    <div style="margin-top: 7px;" id="div_restag"></div>
                </div>
            </div>

            <c:if test="${param.resourcetype == 'img'}">
            <div class="form-group">
                <label class="col-sm-2 control-label">宽：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" style="width: 100%; height: 34px;" id="reswidth" data-options="readonly:true,prompt:'图片宽度',novalidate:true,validType:'length[1,32]'" />
                </div>
                <label class="col-sm-2 control-label">高：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" style="width: 100%; height: 34px;" id="resheight" data-options="readonly:true,prompt:'图片高度',novalidate:true,validType:'length[1,32]'" />
                </div>
            </div>
            </c:if>

            <div class="form-group">
                <label class="col-sm-2 control-label">资源来源：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" style="width: 100%; height: 34px;" id="resorigin" data-options="prompt:'请输入资来源。如：新浪网',readonly:true,required:false,novalidate:true,validType:'length[1,32]'" />
                </div>
                <label class="col-sm-2 control-label">资源大小：</label>
                <div class="col-sm-4">
                    <input class="easyui-textbox" style="width: 100%; height: 34px;" id="ressize" data-options="readonly:true,required:false,novalidate:true,validType:'length[1,32]'" />
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">资源简介：</label>
                <div class="col-sm-10">
                    <script id="resintroduce" name="resintroduce" type="text/plain" style="width: 100%; height: 300px;"></script>
                </div>
            </div>

        </fieldset>

        <fieldset id="extFieldDiv">
            <legend>资源库自定义表单项</legend>
        </fieldset>

        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="button" class="btn btn-success btn-sm" onclick="rowOneField();"><span class="glyphicon glyphicon-plus"></span>&nbsp;单行一列</button>
                <button type="button" class="btn btn-info btn-sm" onclick="rowTwoField();"><span class="glyphicon glyphicon-plus"></span>&nbsp;单行两列</button>
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="button" class="btn btn-primary" id="doSubmit"><span class="glyphicon glyphicon-ok"></span>&nbsp;保  存</button>
                <button type="button" class="btn btn-default" onclick="WhgComm.editDialogClose();">&nbsp;返  回</button>
            </div>
        </div>
    </form>
</div>


<!-- script -->
<script type="text/javascript">
    //单行一列
    function rowOneField() {
        WhgCustomForm.createRowOneColumn('extFieldDiv');
    }//单行一列 END

    //单行两列
    function rowTwoField(){
        WhgCustomForm.createRowTwoColumn('extFieldDiv');
    }//单行两列 END

    //初始表单提交
    function initForm(){
        //表单初始
        $('#whgff').form({
            novalidate: true,
            url: "${basePath}/admin/mass/library/add",
            onSubmit : function(param) {
                var _valid = $(this).form('enableValidation').form('validate');

                if(_valid){
                    //艺术分类不能为空
                    var arttype = $("#whgff").find("input[name='arttype']:checked").val();
                    if (!arttype){
                        _valid = false;
                        $.messager.alert("提示", '艺术分类不能为空！', 'warning');
                    }

                    var picture1 = $("#library_imgurl").val();
                    if (!picture1){
                        $.messager.alert("提示", '封面图片不能为空', 'error');
                        _valid = false;
                    }
                }

                if (!_valid){
                    $.messager.progress('close');
                    $('#doSubmit').off('click').one('click', function (){$('#whgff').submit();});
                }else{
                    //自定义字段
                    var formData = WhgCustomForm.getFormData( $('#extFieldDiv') );
                    param.adds = formData.add;
                    param.edits = formData.edit;
                    param.dels = formData.del;
                    param.keeps = formData.keep;
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
                    $('#doSubmit').off('click').one('click', function (){$('#whgff').submit();});
                }
            }
        });
        //注册提交事件
        $('#doSubmit').off('click').one('click', function (){$('#whgff').submit();});
    }//初始表单提交 END


    function initCommForm() {

        //图片初始化
        var whgImg = WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'library_imgurl', previewImgId: 'previewImg1'});

        //初始艺术分类
        WhgCustomComm.createArtTypeHtml('div_resarttype', '${library.cultid}', '${res.resarttype}');

        //初始标签
        WhgCustomComm.createTagHtml('div_restag', '${library.cultid}', '${res.restag}');
        $("#div_resarttype, #div_restag").on("click", "input:checkbox", function(){return false});

        //初始富文本
        window.ue_resintroduce = UE.getEditor('resintroduce',{readonly: true});

        $('#btn_up').parents('fieldset').find('div.form-group').hide();
        $('#btn_up').hide();
        $('#btn_down').show();

        $('#btn_down').on('click', function () {
            $(this).parents('fieldset').find('div.form-group').show();
            $(this).hide();
            $('#btn_up').show();
        });
        $('#btn_up').on('click', function () {
            $(this).parents('fieldset').find('div.form-group').hide();
            $(this).hide();
            $('#btn_down').show();
        });
    }


    //window.onload
    $(function () {
        //初始文化馆和权限
        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:false,
            deptEid:'deptid', deptValue:false,

            provinceEid:'province', provinceValue:'${library.province}',
            cityEid:'__CITY_ELE', cityValue:'${library.city}',
            areaEid:'__AREA_ELE', areaValue:'${library.area}',
            ywiArtTypeEid:'arttype', ywiArtTypeValue:'${library.arttype}',
            ywiTagEid2:'tags', ywiTagValue:'', ywiKeyClass:56,
        });

        //初始表单
        initForm();

        //公共表单
        initCommForm();


        $("#select_tags ul").on('click','li a',function(){
            $(this).parent().remove();
        })

    });//window.onload END

    /** 添加标签 */
    function addTag() {
        var tags = $("input[name='etags']").val();
        var name = $("#tags").combobox("getText");

        $.ajax({
            type: "POST",
            url: "${basePath}/admin/yunwei/tag/add",
            data: {type: 56, name: name},
            success: function (data) {

                // $.messager.alert("提示", "操作成功");
                if (data.data) {
                    tags = data.data;
                }
                if (name) {
                    $('#tags').combobox('setValues', '');
                    //判断标签是否已选
                    if ($("#" + name).length > 0) {
                        $.messager.alert("提示", "标签不可重复添加");
                        return;
                    }
                    var html = '<li id="' + name + '" style="float: left;position: relative;margin-right: 10px;margin-bottom: 10px;"><input type="hidden" name="tags" value="' + tags + '">\n' +
                        '<label class="btn btn-default btn-block" style="padding-right: 22px;">' + name + '</label>\n' +
                        '<a href="javascript:void(0)" class="textbox-icon icon-clear" icon-index="0" tabindex="-1" style="width: 18px; height: 30px;position:absolute;top: 2px;right: 2px;"></a>\n' +
                        '</li>';
                    $('#select_tags ul').append(html)
                }
            }
        });
    }
</script>
<!-- script END -->
</body>
</html>