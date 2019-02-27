<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <c:choose>
        <c:when test="${not empty id and not empty targetShow}">
            <c:set var="pageTitle"  value="查看培训资源"></c:set>
        </c:when>
        <c:when test="${not empty id}">
            <c:set var="pageTitle" value="编辑培训资源"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="添加培训资源"></c:set>
        </c:otherwise>
    </c:choose>
    <title>${pageTitle}</title>
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

    <script type="text/javascript" src="${basePath}/static/common/js/area.js"></script>
    <script type="text/javascript" src="${basePath}/static/admin/js/mass-resource.js"></script>
<body class="body_add">

<form id="whgff" class="whgff" method="post">

    <h2>${pageTitle}</h2>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>资源类型：</div>
        <div class="whgff-row-input">
        <select id="cc" class="easyui-combobox" <%--name="drsctyp"--%> panelHeight="auto" style="width:300px; height:32px" data-options="editable:true, limitToList:true,required:true">
            <option value="1">图片</option>
            <option value="2">视频</option>
            <option value="3">音频</option>
            <option value="4">文档</option>
        </select>
        <a class="easyui-linkbutton" id="selectlinkbutton" style="background-color: green; display: none"
           data-options="onClick:getResourceItem">选择资源</a>
        </div>
    </div>

    <input type="hidden" name="drsctyp" value="${drsc.drsctyp}">
    <%--资源的地址--%>
    <input type="hidden"  class="vm-refvalue" vm="resurl" name="enturl" value="${drsc.enturl}">

    <div class="whgff-row video_wrap vm-src" style="display: none">
        <div class="whgff-row-label">资源地址 ： </div>
        <div class="whgff-row-input">
            <input class="easyui-textbox vm-text-ui" style="width:600px; height:32px"
                   data-options="readonly:true">
        </div>
    </div>

    <div class="whgff-row video_wrap">
        <div class="whgff-row-label"><i></i>视频/音频时长：</div>
        <div class="whgff-row-input"><input class="easyui-timespinner" name="drsctime" value="${drsc.drsctime}" style="width:300px; height:32px" data-options="showSeconds:true"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>资源标题：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" id="name" name="drsctitle" value="${drsc.drsctitle}" style="width:600px; height:32px" data-options="required:true,validType:['length[1,60]'],prompt:'请输入资源名称'">
        </div>
    </div>

    <div class="whgff-row picture_warp" >
        <div class="whgff-row-label"><i>*</i>资源图片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture2" name="drscpic" value="${drsc.drscpic}">
            <div class="whgff-row-input-imgview" id="previewImg2"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn2">选择图片</a></i>
                <i>图片格式为jpg、png、gif，大小为2MB以内</i>
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>所属单位：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:245px; height:32px" name="drscvenueid" id="drscvenueid" data-options="required:true" />
            <input class="easyui-combobox" style="width:245px; height:32px" name="deptid" id="deptid" data-options="required:true" />
            <%--<select class="easyui-combobox" name="drscvenueid" style="width:500px; height:32px"
                    data-options="editable:false, required:true, valueField:'id', textField:'text', value:'${drsc.drscvenueid}', data:WhgComm.getMgrCults(),prompt:'请选择所属文化馆'"></select>--%>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>艺术类型：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary" id="drscarttyp" name="drscarttyp" data-options="required:true"></div>
            <%-- <div class="radio radio-primary whg-js-data" name="drscarttyp" value="${drsc.drscarttyp}" js-data="WhgComm.getArtType"></div>--%>
        </div>
    </div>


    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>区　　域：</div>
        <input class="easyui-combobox" style="width:193px; height:32px" id="province" name="province" data-options="required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name'"/>
        <input class="easyui-combobox" style="width:193px; height:32px" id="__CITY_ELE" name="city" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name'"/>
        <input class="easyui-combobox" style="width:194px; height:32px" id="__AREA_ELE" name="area" data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
    </div>

    <%--<div class="whgff-row video_wrap">
        <div class="whgff-row-label"><i>*</i>资源地址：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" name="entdir" id="video_entdir" style="height:35px;width:190px" data-options="prompt:'请选择目录',editable:true,limitToList:true,valueField:'id',textField:'text', onChange:changeEntDir"/>
            <input class="easyui-combobox" name="enturl" id="video_enturl" style="height:35px;width:400px" data-options="editable:true,limitToList:true,valueField:'addr',textField:'key'"/>
        </div>
    </div>--%>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>资源简介：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="drscintro" value="${drsc.drscintro}" style="width:600px; height:60px" data-options="multiline:true, validType:['length[1,200]'],prompt:'请填写资源简介'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>资源来源：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="drscfrom" value="${drsc.drscfrom}" style="width:600px; height:32px" data-options="multiline:false, validType:['length[1,60]'],prompt:'请填写资源来源'">
        </div>
    </div>

    <div class="whgff-row doc_wrap">
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
    <div class="whgff-row" style="display: none" id="showReason">
        <div class="whgff-row-label">
            下架原因：
        </div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" id="reason" readonly="true" value="" multiline="true"
                   style="width:350px;height: 150px;">
        </div>
    </div>


</form>

<div id="whgwin-add-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok" id="whgwin-add-btn-save">提 交</a>
    <a class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose();">返 回</a>
</div>

<!-- script -->
<script type="text/javascript">

    var entResTypes = { "1":"img", "2":"video", "3":"audio", "4":"file" };
    function getResourceItem(){
        var enttype = $("#cc").combobox("getValue");
        if (!enttype || enttype == '' || enttype=='1' || enttype=='4') {
            return;
        }
        var restype = entResTypes[enttype];
        WhgMassResource.openResource({
            restype:restype,
            submitFn:function (row) {
                //{libid:'资源库标识', resid:'资源标识', restype:'资源类型', resname:'资源名称', respicture:'资源封面图片地址', resurl:'resurl'}
                row = row||{};

                $("#whgff .vm-refvalue").each(function(){
                    var vmkey = $(this).attr("vm");
                    $(this).val(row[vmkey]||'');
                });
                $("#name").textbox("setValue", row.resname||'');
                $("#whgff [name='drsctyp']").val(enttype);

                showResourc();
            }
        });
    }

    function showResourc(){
        var enttype = $("#whgff [name='drsctyp']").val();
        var enturl = $("#whgff [name='enturl']").val();

        var vmsrc = $("#whgff .vm-src");
        vmsrc.hide();

        switch (enttype){
            case "2":
            case "3":
                vmsrc.find(".vm-text-ui").textbox("setText", enturl||'');
                vmsrc.show();
                break;
        }
    }
    function clsResourc(){
        //$("#name").textbox("setValue", '');
        $("#whgff .vm-src .vm-text-ui").textbox("setText", '');
        $("#whgff .vm-refvalue").val('');
    }

    $(function () {
        //资源类型绑定change事件
        $('#cc').combobox({
            onChange: function (newv, oldv) {
                if (oldv=='2' || oldv=='3'){
                    clsResourc();
                }
                //selectLoad(newv);
            },
            onSelect: function(record){
                selectLoad(record.value);
            }
        });
        $("#cc").combobox("setValue",  "${drsc.drsctyp}" || '1');

        //根据不同的资源类型，加载不同的表单元素
        function selectLoad(newv) {
            $("#selectlinkbutton").hide();
            $("#whgff [name='drsctyp']").val(newv);
            if (newv == 1) {
                $(".picture_warp").show();
                $(".video_wrap").hide();
                $(".doc_wrap").hide();
                //$("#video_enturl").combobox({required: false});
            } else if (newv == 2) {
                $(".video_wrap").show();
                $(".picture_warp").show();
                $(".doc_wrap").hide();
                //$("#video_enturl").combobox({required: true});
                <%--$("[name=deourl]").val("${wcr.deourl}");--%>
                $("#selectlinkbutton").show();
            } else if (newv == 3) {
                $(".video_wrap").show();
                $("#spfm").hide();
                $(".picture_warp").show();
                $(".doc_wrap").hide();
                //$("#video_enturl").combobox({required: true});
//                $("[name=deourl]").val("");
                $("#selectlinkbutton").show();
            }else if (newv == 4){//文档
                $(".picture_warp").show();
                $(".video_wrap").hide();
                $(".doc_wrap").show();
                //$("#video_enturl").combobox({required: false});
            }
        }
        selectLoad("${drsc.drsctyp}" || '1');
        var infostate = '${drsc.drscstate}';
        if (infostate && infostate == "4") {
            showXjReason('${drsc.drscid}');
        }

        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'drscvenueid', cultValue:'${drsc.drscvenueid}',
            deptEid:'deptid', deptValue:'${drsc.deptid}',
            ywiArtTypeEid:'drscarttyp', ywiArtTypeValue:'${drsc.drscarttyp}',

            provinceEid:'province', provinceValue:'${drsc.province}',
            cityEid:'__CITY_ELE', cityValue:'${drsc.city}',
            areaEid:'__AREA_ELE', areaValue:'${drsc.area}'
        });

        //1-图片， 2-视频， 3-音频
        var wcr_enttype = '${drsc.drsctyp}';
        var isInitImg = wcr_enttype == '1';//图片
        var isInitVod = wcr_enttype == '2';//视频
        var isInitAud = wcr_enttype == '3';//音频
        var isInitdoc = wcr_enttype == '4';//文档

        //编辑图片资源
        if(isInitImg){
            $('#cult_picture2').val('${drsc.drscpic}');
        }

        if(isInitdoc){
            $('#whg_file_upload').val('${drsc.enturl}');
        }

        //编辑视频资源
        if(isInitVod || isInitAud){
            //初始视频封面图片
            if(isInitVod)$('#cult_picture2').val('${drsc.drscpic}');
            showResourc();
        }

        <!--文件上传控件 -->
        WhgUploadFile.init({basePath: '${basePath}', uploadBtnId: 'fileUploadBtn2', hiddenFieldId: 'whg_file_upload',previewFileId:'whg_file_pload_view'});
        //初始图片上传
        //  WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1',needCut:true});
        var __WhgUploadImg = WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn2', hiddenFieldId: 'cult_picture2', previewImgId: 'previewImg2',needCut:true});

        var mid = '${mid}';
        var buts = $("div.whgff-but");
        var id = '${id}';
        var targetShow = '${targetShow}';
        var frm = $("#whgff");
        //添加时开启清除 ，其它为返回
        if (!id){
            buts.find("a.whgff-but-clear").on('click', function(){
                frm.form('disableValidation');
                frm.form('clear');
                frm.find("div.radio").find(':radio:eq(0)').click();
                __WhgUploadImg.clear();
            });
        }else{
            //处理返回
            buts.find("a.whgff-but-clear").linkbutton({
                text: '返 回',
                iconCls: 'icon-undo',
                onClick: function(){
                    if (!targetShow){
                        window.parent.$('#whgdg').datagrid('reload');
                    }
                    WhgComm.editDialogClose();
                }
            });
        }

        //查看时的处理
        if (targetShow){
            //取消表单验证
            frm.form("disableValidation");

            //组件设为只读-因为连动触发事件，必须等待一定时间
            window.setTimeout(function () {
                $('.easyui-combobox').combobox('readonly');
            }, 800);

            $('.easyui-textbox').textbox('readonly');
            $('.easyui-datetimebox').combobox('readonly');
            $('.easyui-numberspinner').numberspinner('readonly');
            $("#imgUploadBtn1").hide();
            //处理选项点击不生效
            frm.find("input[type='checkbox'], input[type='radio']").on('click', function(){return false});

            //不显示提交 button
            buts.find("a.whgff-but-submit").hide();
            return;
        }

        //定义表单提交
        var url = id ? "${basePath}/admin/drsc/edit" : "${basePath}/admin/drsc/add";
        frm.form({
            url: url,
            novalidate: true,
            onSubmit: function (param) {
                if (id){
                    param.drscid = id;
                }
                if (mid){
                    param.mid = mid;
                }
                $(this).form("enableValidation");
                var isValid = $(this).form('validate');
                if(isValid){
                    //图片不能为空
                    var picture1 = $("#cult_picture2").val();
                    if (!picture1){
                        $.messager.alert("错误", '图片不能为空！', 'error');
                        isValid = false;
                    }
                    var arttype = $('input[name="drscarttyp"]:checked ').val();
                    if (isValid && !arttype){
                        $.messager.alert("错误", '艺术分类不能为空！', 'error');
                        isValid = false;
                    }

                    var _type = $('#cc').combobox('getValue');
                    if(isValid && _type == 4){
                        var file_upload = $("#whg_file_upload").val();
                        if (!file_upload){
                            $.messager.alert("错误", '文件不能为空！', 'error');
                            isValid = false;
                        }
                    }
                    if (isValid && (_type == 2 || _type == 3)){
                        var enturl = $("[name='enturl']").val();
                        if (!enturl || enturl == ''){
                            $.messager.alert("错误", '请选择资源！', 'error');
                            isValid = false;
                        }
                    }

                }
                if (!isValid){
                    $.messager.progress('close');
                    buts.find("a.whgff-but-submit").off('click').one('click', function () {
                        frm.submit();
                    });
                }
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
                if(mid){
                    WhgComm.editDialogClose();
                    window.parent.$('#whgdg').datagrid('reload');
                }

                WhgComm.editDialogClose();
                window.parent.$('#whgdg').datagrid('reload');

            }
        });
        buts.find("a.whgff-but-submit").off('click').one('click', function () {
            frm.submit();
        });


    });
</script>
<!-- script END -->
</body>
</html>