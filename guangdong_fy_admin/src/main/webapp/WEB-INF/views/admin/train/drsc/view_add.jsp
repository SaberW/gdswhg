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
<body>

<form id="whgff" class="whgff" method="post">

    <h2>${pageTitle}</h2>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>资源类型：</div>
        <select id="cc" class="easyui-combobox" name="drsctyp" panelHeight="auto" style="width:600px; height:32px" data-options="editable:true, limitToList:true,required:true">
            <option value="1">图片</option>
            <option value="2">视频</option>
            <option value="3">音频</option>
            <option value="4">文档</option>
        </select>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>资源标题：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="drsctitle" value="${drsc.drsctitle}" style="width:600px; height:32px" data-options="required:true,validType:['length[1,60]'],prompt:'请输入资源名称'">
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
        <div class="whgff-row-label"><label style="color: red">*</label>所属文化馆：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:600px; height:32px" name="drscvenueid" id="drscvenueid" data-options="required:true" />
            <%--<select class="easyui-combobox" name="drscvenueid" style="width:500px; height:32px"
                    data-options="editable:false, required:true, valueField:'id', textField:'text', value:'${drsc.drscvenueid}', data:WhgComm.getMgrCults(),prompt:'请选择所属文化馆'"></select>--%>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>艺术类型：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary" id="drscarttyp" name="drscarttyp"></div>
            <%-- <div class="radio radio-primary whg-js-data" name="drscarttyp" value="${drsc.drscarttyp}" js-data="WhgComm.getArtType"></div>--%>
        </div>
    </div>


    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>区域：</div>
        <input class="easyui-combobox" style="width:193px; height:32px" id="province" name="province" data-options="required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name', data:__PROVINCE, onChange:myChangeProvince"/>
        <input class="easyui-combobox" style="width:193px; height:32px" id="__CITY_ELE" name="city" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name', data:[], onChange:myChangeCity"/>
        <input class="easyui-combobox" style="width:194px; height:32px" id="__AREA_ELE" name="area" data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
    </div>

    <div class="whgff-row video_wrap">
        <div class="whgff-row-label"><i>*</i>资源地址：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" name="entdir" id="video_entdir" style="height:35px;width:190px" data-options="prompt:'请选择目录',editable:true,limitToList:true,valueField:'id',textField:'text', onChange:changeEntDir"/>
            <input class="easyui-combobox" name="enturl" id="video_enturl" style="height:35px;width:400px" data-options="editable:true,limitToList:true,valueField:'addr',textField:'key'"/>
        </div>
    </div>

    <div class="whgff-row video_wrap">
        <div class="whgff-row-label"><i></i>视频/音频时长：</div>
        <div class="whgff-row-input"><input class="easyui-timespinner" name="drsctime" value="${drsc.drsctime}" style="width:300px; height:32px" data-options="showSeconds:true"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>资源简介：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="drscintro" value="${drsc.drscintro}" style="width:600px; height:60px" data-options="multiline:true, validType:['length[1,200]'],prompt:'请填写资源简介'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>资源来源：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="drscfrom" value="${drsc.drscfrom}" style="width:600px; height:60px" data-options="multiline:true, validType:['length[1,400]'],prompt:'请填写资源来源'">
        </div>
    </div>

    <div class="whgff-row doc_wrap">
        <div class="whgff-row-label">上传附件：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="whg_file_upload" name="doc_enturl">
            <div class="whgff-row-input-fileview" id="whg_file_pload_view"></div>
            <div class="whgff-row-input-filefile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="fileUploadBtn2">选择附件</a></i>
                <i>附件格式为doc,docx,xls,xlsx,zip,pdf,建议文件大小为10MB以内</i>
            </div>
        </div>
    </div>



    <%--<div class="whgff-row  video_wrap" id="spfm">
        <div class="whgff-row-label"><i></i>视频封面图片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture1" name="deourl">
            <div class="whgff-row-input-imgview" id="previewImg1"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>--%>

</form>

<div id="whgwin-add-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok" id="whgwin-add-btn-save">提 交</a>
    <a class="easyui-linkbutton whgff-but-clear" iconCls="icon-no">清 空</a>
</div>

<!-- script -->
<script type="text/javascript">
    //省市区
    var province = '${drsc.province}';
    var city = '${drsc.city}';
    var __init_city = true;
    var __init_area = true;
    function myChangeProvince(newVal, oldVal) {
        changeProvince(newVal, oldVal);
        if(__init_city){
            if(!city || city==''){
                city = WhgComm.getCity()?WhgComm.getCity():"";
            }
            window.setTimeout(function () {$('#__CITY_ELE').combobox('setValue', city);}, 500);
            __init_city = false;
        }
    }

    function myChangeCity(newVal, oldVal) {
        changeCity(newVal, oldVal);
        if(__init_area){
            window.setTimeout(function () {$('#__AREA_ELE').combobox('setValue', '${drsc.area}')}, 500);
            __init_area = false;
        }
    }  //省市区------END


    /**
     * 切换文化馆时，需要动态加载视频文化目录
     * @param newVal
     * @param oldVal
     */
    function drscChangeCult(newVal, oldVal){
        $('#video_entdir').combobox("reload", '${basePath}/admin/video/srchPagging?srchDir=1&cultid='+newVal);
        $('#video_entdir').combobox('clear');
        $('#video_enturl').combobox("loadData", []);
        $('#video_enturl').combobox("clear");

        //如果是编辑状态，需要初始加载目录，设置默认值
        if(typeof(window.__init_entdir) == 'undefined'){
            window.__init_entdir = true;

            //音视频资源地址
            var video_enturl_val_host = '${drsc.enturl}';
            if(video_enturl_val_host != '') {
                for (var i = 0; i < 3; i++) {
                    var idx = video_enturl_val_host.indexOf("/");
                    video_enturl_val_host = video_enturl_val_host.substring(idx + 1);
                }
                var _idx = video_enturl_val_host.indexOf("\?");
                if (_idx > -1) {
                    video_enturl_val_host = video_enturl_val_host.substring(0, _idx);
                }
                var dirVal = video_enturl_val_host.substring(0, video_enturl_val_host.lastIndexOf("/") + 1);
                $('#video_entdir').combobox('setValue', decodeURIComponent(dirVal));
            }
        }
    }

    /**
     * 资源目录变化时，重新加载目录下的资源
     * @param newVal
     * @param oldVal
     */
    function changeEntDir(newVal, oldVal){
        $('#video_enturl').combobox('clear');
        $('#video_enturl').combobox({
            url:'${basePath}/admin/video/srchPagging?srchFile=1&dir='+encodeURIComponent(newVal),
            onLoadSuccess: function () {
                if(typeof(window.__init_enturl) == 'undefined'){
                    window.__init_enturl = true;
                    var initAddr = '${drsc.enturl}';//视频音频地址
                    if(initAddr != ''){
                        var valArr = $('#video_enturl').combobox('getData');
                        if(valArr.length > 0){
                            for(var i=0; i<valArr.length; i++){
                                var ___val = valArr[i].addr;
                                var ___idx = ___val.indexOf("\?");
                                if(___idx > -1){
                                    ___val = ___val.substring(0,___idx);
                                    initAddr = initAddr.substring(0, ___idx);
                                }
                                if(initAddr == ___val){
                                    $('#video_enturl').combobox('setValue', valArr[i].addr);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        });
    }


    $(function () {
        WhgComm.initPMS({
            basePath:'${basePath}', cultOnChange: drscChangeCult,
            cultEid:'drscvenueid', cultValue:'${drsc.drscvenueid}',
            ywiArtTypeEid:'drscarttyp', ywiArtTypeValue:'${drsc.drscarttyp}'
        });


        //初始省值
        if (!province || province==''){
            province = WhgComm.getProvince()?WhgComm.getProvince():'广东省';
        }
        $('#province').combobox('setValue', province);

        var city = '${drsc.city}';
        if (!city || city==''){
            city = WhgComm.getCity()?WhgComm.getCity():"";
        }
        $("#__CITY_ELE").combobox('setValue', city); //初始省值

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
                if (!id){
                    $(this).form("disableValidation");
                    buts.find("a.whgff-but-clear").click();
                    $.messager.show({
                        title:'提示消息',
                        msg:'培训资源信息提交成功',
                        showType:'slide',
                        timeout:1000,
                        width: 300,
                        height: 200
                    });
                }else{
                    WhgComm.editDialogClose();
                    window.parent.$('#whgdg').datagrid('reload');
                }
            }
        });
        buts.find("a.whgff-but-submit").off('click').one('click', function () {
            frm.submit();
        });

        //资源类型绑定change事件
        $('#cc').combobox({
            onChange: function (newv, oldv) {
                selectLoad(newv);
            }
        });
        $("#cc").combobox("setValue",  "${drsc.drsctyp}" || '1');

        //根据不同的资源类型，加载不同的表单元素
        function selectLoad(newv) {
            //console.log(newv);
            if (newv == 1) {
                $(".picture_warp").show();
                $(".video_wrap").hide();
                $(".doc_wrap").hide();
            } else if (newv == 2) {
                $(".video_wrap").show();
                $(".picture_warp").show();
                $(".doc_wrap").hide();
                <%--$("[name=deourl]").val("${wcr.deourl}");--%>
            } else if (newv == 3) {
                $(".video_wrap").show();
                $("#spfm").hide();
                $(".picture_warp").show();
                $(".doc_wrap").hide();
//                $("[name=deourl]").val("");
            }else if (newv == 4){//文档
                $(".picture_warp").show();
                $(".video_wrap").hide();
                $(".doc_wrap").show();

            }
        }
        selectLoad("${drsc.drsctyp}" || '1');
    });
</script>
<!-- script END -->
</body>
</html>