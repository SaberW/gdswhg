<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>文化品牌配置-编辑文化品牌</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>

    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>


    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>

    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <!-- 图片上传相关-END -->
    <script type="text/javascript" src="${basePath}/static/common/js/area.js"></script>

    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
</head>
<body>



<form id="whgff" class="whgff" method="post" action="${basePath}/admin/yunwei/whpp/add" >
    <h2>文化品牌配置-编辑文化品牌</h2>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>所属文化馆：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" name="cultid" id="cultid" style="width:600px; height:32px" panelHeight="auto" data-options="prompt:'请选择文化馆', required:true">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>区域：</div>
        <%--<div class="whgff-row-input">
            <input class="easyui-combobox" style="width:150px; height:32px" id="province" name="province" data-options="required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name', data:__PROVINCE, onChange:myChangeProvince"/>
            <input class="easyui-combobox" style="width:150px; height:32px" id="__CITY_ELE" name="city" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name', data:[], onChange:myChangeCity"/>
            <input class="easyui-combobox" style="width:179px; height:32px" id="__AREA_ELE" name="area" data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
        </div>--%>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:150px; height:32px" id="province" name="province" data-options="required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name'"/>
            <input class="easyui-combobox" style="width:150px; height:32px" id="__CITY_ELE" name="city" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name'"/>
            <input class="easyui-combobox" style="width:179px; height:32px" id="__AREA_ELE" name="area" data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name'"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>品牌类型：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="pptype" value="${whpp.pptype}"
                 js-data='[{"id":"0","text":"省级"},{"id":"1","text":"市级"},{"id":"2","text":"县(区)级"}]'>
            </div>
        </div>
    </div>

    <input type="hidden" name="id" id="id" value="${id}"/>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>名称：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="name" value="${whpp.name}"
                                            style="width:600px; height:32px"
                                            data-options="required:true, validType:'length[1,60]'"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>简称：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="shortname" value="${whpp.shortname}"
                                            style="width:600px; height:32px"
                                            data-options="required:true, validType:'length[1,60]'"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>图片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture1" name="picture" value="${whpp.picture}">
            <div class="whgff-row-input-imgview" id="previewImg1"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>

    <%--<div class="whgff-row">
        <div class="whgff-row-label"><i></i>背景图片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture2" name="bgpicture" value="${whpp.bgpicture}">
            <div class="whgff-row-input-imgview" id="previewImg2"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="#" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn2">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 1920*500，大小为2MB以内</i>
            </div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>背景图片是否平铺:</div>
        <select id="cc" class="easyui-combobox" name="norepeat" style="width:300px; height:32px" data-options="editable:false,required:true" prompt='请选择是否平铺'>
            <option value="1">平铺</option>
            <option value="0">不平铺</option>
        </select>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i></i>背景颜色：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="bgcolour" value="${whpp.bgcolour}"
                                            style="width:300px; height:32px"
                                            data-options="prompt:'背景颜色格式:#ffffff',required:false, validType:'length[1,60]'">
        </div>
    </div>--%>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>文化品牌介绍：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="introduction" value="${whpp.introduction}" multiline="true" style="width:600px;height: 200px;"
                   data-options="required:true,validType:['length[1,2000]']">
        </div>
    </div>

    <%--<div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>文化品牌介绍：</div>
        <div class="whgff-row-input">
            <div id="catalog" type="text/plain" style="width:700px; height:250px;"></div>
        </div>
    </div>--%>
</form>
<div id="whgwin-add-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <div style="display: inline-block; margin: 0 auto">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="whgwin-add-btn-save">保 存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返  回</a>
    </div>
</div>

<!-- script -->
<script type="text/javascript">
    /*//省市区
    var __init_city = true;
    var __init_area = true;
    function myChangeProvince(newVal, oldVal) {
        changeProvince(newVal, oldVal);
        if(__init_city){
            window.setTimeout(function () {$('#__CITY_ELE').combobox('setValue', '${whpp.city}');}, 500);
            __init_city = false;
        }
    }

    function myChangeCity(newVal, oldVal) {
        changeCity(newVal, oldVal);
        if(__init_area){
            window.setTimeout(function () {$('#__AREA_ELE').combobox('setValue', '${whpp.area}')}, 500);
            __init_area = false;
        }
    }  //省市区------END
*/
    /*var ueConfig = {
        scaleEnabled: false,
        autoFloatEnabled: false,
        readonly: '${targetShow}'? true: false//富文本编辑器设为只读
    };
    var ue_catalog = UE.getEditor('catalog', ueConfig);

    ue_catalog.ready(function () {
        ue_catalog.setContent('${whpp.introduction}')
    });*/

    $(function () {
        $("#cc").combobox({
            onLoadSuccess: function () {
                $("#cc").combobox("select", "${whpp.norepeat}")
            }
        });
    });


    //查看时的处理
    $(function () {
        //初始省值
        //$('#province').combobox('setValue', '${whpp.province}');

        var targetShow = '${targetShow}';
        WhgComm.initPMS({
            cultEid:'cultid', cultValue:'${whpp.cultid}',
            provinceEid:'province', provinceValue:'${whpp.province}',
            cityEid:'__CITY_ELE', cityValue:'${whpp.city}',
            areaEid:'__AREA_ELE', areaValue:'${whpp.area}'
            ,cultOnChange:function(v, ov){
                var cultOptions = $('#cultid').combobox("options") || {};
                var cultdata = cultOptions.data || [];
                var cultInfo = {};
                for(var i in cultdata){
                    if (v == cultdata[i].id){
                        cultInfo = $.extend({}, cultdata[i]);
                        break;
                    }
                }
                //console.info(cultInfo);
                if (!cultInfo.level){
                    return;
                }

                var pptypeRadios = $("input[name='pptype']:radio");
                if (pptypeRadios.length<3){
                    return;
                }
                pptypeRadios.attr('disabled', false);
                switch (cultInfo.level){
                        //市级
                    case '2':
                        pptypeRadios.eq(0).attr('disabled', true);
                        break;
                        //区级
                    case '3':
                        pptypeRadios.eq(0).attr('disabled', true);
                        pptypeRadios.eq(1).attr('disabled', true);
                        break;
                }
            }
        });

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

    $(function () {
        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1'});
        //WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn2', hiddenFieldId: 'cult_picture2', previewImgId: 'previewImg2',cutWidth:1920,cutHeight:500});

        $('#whgff').form({
            novalidate: true,
            url :'${basePath}/admin/yunwei/whpp/edit',
            onSubmit : function(param) {
                var _valid = $(this).form('enableValidation').form('validate')
                if(_valid){
                    //图片必填
                    if($('#cult_picture1').val() == ""){
                        _valid = false;
                        $.messager.alert('提示', '请选择文化品牌图片');
                    }/*else if(!isUEvalid) {
                        var isUEvalid = validateUE();
                        if (isUEvalid) {
                            param.introduction = ue_catalog.getContent();
                            $.messager.progress();
                        } else {
                            _valid = false;
                        }
                    }*/
                }
                if(!_valid){
                    $.messager.progress('close');
                    //失败时再注册提交事件
                    $('#whgwin-add-btn-save').off('click').on('click', function () { $('#whgff').submit(); });
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
                }
            }
        });
        //注册提交事件
        $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
    });

    /*function validateUE(){
        if (!ue_catalog.hasContents()) {
            $.messager.alert("错误", '文化品牌介绍不能为空', 'error');
            return false;
        }
        return true;
    }*/



</script>
<!-- script END -->
</body>
</html>
