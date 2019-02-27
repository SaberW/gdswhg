<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>
    <c:choose>
        <c:when test="${editType eq 'edit'}">
            <c:set var="pageTitle" value="云直播编辑"></c:set>
        </c:when>
        <c:when test="${editType eq 'add'}">
            <c:set var="pageTitle" value="云直播添加"></c:set>
        </c:when>
        <c:when test="${editType eq 'view'}">
            <c:set var="pageTitle" value="云直播查看"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="无标题"></c:set>
        </c:otherwise>
    </c:choose>
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>

    <script type="text/javascript" charset="utf-8"	src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8"	src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript" charset="utf-8"	src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>

    <script type="text/javascript" src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-file.js"></script>
    <script type="text/javascript" src="${basePath}/static/common/js/whg.maps.js"></script>
    <script type="text/javascript" src="${basePath}/static/admin/js/whgmodule-venseatmaps.js"></script>
</head>
<body>
    <form id="whgff" method="post" class="whgff">
        <h2>${pageTitle}</h2>
        <input id="id" name="id" type="hidden" value="${whgLive.id}">
        <div class="whgff-row">
            <div class="whgff-row-label"><i>*</i>直播标题：</div>
            <div class="whgff-row-input">
                <input class="easyui-textbox" name="livetitle"
                       style="width: 500px; height: 32px" data-options="required:true,validType:['length[1,60]']" value="${whgLive.livetitle}"/>
            </div>
        </div>
        <div class="whgff-row">
            <div class="whgff-row-label"><label style="color: red">*</label>选择领域 ：</div>
            <div class="whgff-row-input">
                <div class="radio radio-primary whg-js-data" name="domain" value="${whgLive.domain}"
                     js-data="WhgComm.getLiveKey" >
                </div>
            </div>
        </div>

        <div class="whgff-row">
            <div class="whgff-row-label"><label style="color: red">*</label>选择直播分类 ：</div>
            <div class="whgff-row-input">
                <div class="radio radio-primary whg-js-data" name="livetype" value="${whgLive.livetype}"
                     js-data="WhgComm.getLiveType" >
                </div>
            </div>
        </div>
        <div class="whgff-row">
            <div class="whgff-row-label"><label style="color: red">*</label>所属文化馆：</div>
            <div class="whgff-row-input">
                <select class="easyui-combobox" name="cultid" style="width:500px; height:32px"
                        data-options="editable:false, required:true, valueField:'id', textField:'text', value:'${whgLive.cultid}', data:WhgComm.getMgrCults(),prompt:'请选择所属单位',
                    onSelect: selectCult"></select>
            </div>
        </div>
       <%-- <div class="whgff-row">
            <div class="whgff-row-label"><label style="color: red">*</label>所属单位：</div>
            <div class="whgff-row-input">
                <input class="easyui-combobox" name="branch" id="branch" panelHeight="auto" limitToList="true" style="width:500px; height:32px"
                       data-options="required:false, editable:false,multiple:false, mode:'remote',
                   valueField:'id', textField:'name'
                   "/>
            </div>
        </div>--%>

        <div class="whgff-row">
            <div class="whgff-row-label">
                <label style="color: red">*</label>上传封面：
            </div>
            <div class="whgff-row-input">
                <input type="hidden" id="act_imgurl1" name="livecover" data-options="required:true" value="${whgLive.livecover}">
                <div class="whgff-row-input-imgview" id="previewImg1"></div>
                <div class="whgff-row-input-imgfile" id="imgUrl_pic1">
                    <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                    <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
                </div>
            </div>
        </div>

        <div class="whgff-row">
            <div class="whgff-row-label">置为轮播图：</div>
            <div class="whgff-row-input">
                <div class="radio radio-primary whg-js-data" onclick="showSelectIslbt()" id="islbt" name="islbt" value="${whgLive.islbt}" js-data="getIslbtData"></div>
            </div>
        </div>

        <div class="whgff-row" id="uploadlbt" style="display: none">
            <div class="whgff-row-label">
                <label style="color: red">*</label>上传轮播图：
            </div>
            <div class="whgff-row-input">
                <input type="hidden" id="act_imgurl2" name="livelbt" data-options="required:true" value="${whgLive.livelbt}">
                <div class="whgff-row-input-imgview" id="previewImg2"></div>
                <div class="whgff-row-input-imgfile" id="imgUrl_pic2">
                    <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn2">选择图片</a></i>
                    <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
                </div>
            </div>
        </div>

        <div class="whgff-row">
            <div class="whgff-row-label"><label style="color: red">*</label>选择推流地址模板：</div>
            <div class="whgff-row-input">
                <input class="easyui-combobox" name="flowaddr" id="flowaddr" panelHeight="auto" limitToList="true" style="width:500px; height:32px"
                       data-options="required:false, editable:false,multiple:false, mode:'remote', valueField:'id', textField:'name'"/>
            </div>
        </div>

        <div class="whgff-row">
            <div class="whgff-row-label"><label style="color: red">*</label>选择播放地址模板：</div>
            <div class="whgff-row-input">
                <input class="easyui-combobox" name="playaddr" id="playaddr" panelHeight="auto" limitToList="true" style="width:500px; height:32px"
                       data-options="required:false, editable:false,multiple:false, mode:'remote', valueField:'id', textField:'name'"/>
            </div>
        </div>

        <div class="whgff-row">
            <div class="whgff-row-label"><i>*</i>应用名：</div>
            <div class="whgff-row-input">
                <input class="easyui-textbox" name="appname"
                       style="width: 500px; height: 32px" data-options="required:true,validType:['length[1,60]']" value="${whgLive.appname}"/>
            </div>
        </div>

        <div class="whgff-row">
            <div class="whgff-row-label"><i>*</i>流名：</div>
            <div class="whgff-row-input">
                <input class="easyui-textbox" name="streamname"
                       style="width: 500px; height: 32px" data-options="required:true,validType:['length[1,60]']" value="${whgLive.streamname}"/>
            </div>
        </div>

        <div class="whgff-row">
            <div class="whgff-row-label"><i>*</i>直播时间：</div>
            <div class="whgff-row-input">
                <input type="text" class="easyui-datetimebox starttime"style="width: 240px; height: 32px;" id="starttime" name="starttime"  value="<fmt:formatDate value='${whgLive.starttime}' pattern='yyyy-MM-dd HH:mm:ss' />" data-options="required:true" /> ~
                <input type="text" class="easyui-datetimebox endtime"style="width: 240px; height: 32px;" id="endtime" name="endtime"  value="<fmt:formatDate value='${whgLive.endtime}' pattern='yyyy-MM-dd HH:mm:ss' />" data-options="required:true,validType:'bmEndTime[\'starttime\']'" />
            </div>
        </div>

        <div class="whgff-row">
            <div class="whgff-row-label">
                <label style="color: red">*</label>直播描述：
            </div>
            <div class="whgff-row-input">
                <script id="remark" name="remark" id="remark" type="text/plain" style="width: 800px; height: 600px;">${whgLive.livedesc}</script>
            </div>
        </div>
        <div id="whgwin-edit-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
            <c:if test="${editType != 'view'}">
                <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok" id="whgwin-edit-btn-save">提 交</a>
            </c:if>
            <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
        </div>
    </form>
</body>

<script type="text/javascript">
    function selectCult(record) {
        var cultid = record.id;
        var data = WhgComm.getChildDept(cultid);
        $(".select-deptid").combobox({data: data});
    }
    /*function getDomain() {
        $.ajaxSetup({
            async: false
        });
        var res = [];
        $.getJSON("${basePath}/admin/yunwei/key/srchList?type=11",function (data) {
            if("1" != data.success){
                $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                return;
            }
            var rows = data.rows;
            $.each(rows,function (index,value) {
                var item = {};
                item.id = value.id;
                item.text = value.name;
                res.push(item);
            });
        });
        $.ajaxSetup({
            async: true
        });
        return res;
    }
    
    function getLiveType() {
        $.ajaxSetup({
            async: false
        });
        var res = [];
        $.getJSON("${basePath}/admin/yunwei/type/srchList?type=21",function (data) {
            if("1" != data.success){
                $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                return;
            }
            var rows = data.rows;
            $.each(rows,function (index,value) {
                var item = {};
                item.id = value.id;
                item.text = value.name;
                res.push(item);
            });
        });
        $.ajaxSetup({
            async: true
        });
        return res;
    }*/
    
    function getIslbtData() {
        var data = [];
        data.push({"id":"2","text":"否"});
        data.push({"id":"1","text":"是"});
        return data;
    }

    function setFlowaddr() {
        $.getJSON("${basePath}/admin/live/getFlowaddr",function (data) {
            //debugger;
            if("1" != data.success){
                $.messager.alert("错误", data.errormsg, 'error');
                return;
            }
            var rows = data.rows;
            $("#flowaddr").combobox("loadData",rows);
//                debugger;
            var flowaddr = "${whgLive.flowaddr}";
            if(0 < rows.length){
                flowaddr = flowaddr != ""?flowaddr:rows[0].id;
                $("#flowaddr").combobox("setValue",flowaddr);
            }
        });
    }

    function setPlayaddr() {
        $.getJSON("${basePath}/admin/live/getPlayaddr",function (data) {
            //debugger;
            if("1" != data.success){
                $.messager.alert("错误", data.errormsg, 'error');
                return;
            }
            var rows = data.rows;
            $("#playaddr").combobox("loadData",rows);
//                debugger;
            var playaddr = "${whgLive.playaddr}";
            if(0 < rows.length){
                playaddr = playaddr != ""?playaddr:rows[0].id;
                $("#playaddr").combobox("setValue",playaddr);
            }
        });
    }

    function showSelectIslbt() {
        var val=$('input:radio[name="islbt"]:checked').val();
        if(1 == val){
            $("#uploadlbt").show();
        }else {
            $("#uploadlbt").hide();
        }
    }

    function getSelectIslbt() {
        return $('input:radio[name="islbt"]:checked').val();
    }

    function checkCover() {
        //debugger;
        var act_imgurl1 = $("#act_imgurl1").val();
        if(null == act_imgurl1 || "" == act_imgurl1){
            $.messager.alert("错误", '封面图片不能为空！', 'error');
            return false;
        }
        return true;
    }

    function checkDomain() {
        var selectValues = $('input:radio[name="domain"]:checked').val();
        if(null == selectValues || 0 == selectValues.length){
            $.messager.alert("错误", '至少要选择1个领域！', 'error');
            return false;
        }
        return true;
    }

    function checklbt() {
        var islbt = getSelectIslbt();
        if(2 == islbt){
            return true;
        }
        if(1 == islbt){
            var act_imgurl2 = $("#act_imgurl2").val();
            if(null == act_imgurl2 || "" == act_imgurl2){
                $.messager.alert("错误", '轮播图不能为空！', 'error');
                return false;
            }
        }
        return true;
    }

    function checkdesc() {
        if (!remark.hasContents()){
            $.messager.alert("错误", '云直播描述不能为空', 'error');
           return false;
        }
        return true;
    }

    $('#whgff').form({
        novalidate: true,
        url : getFullUrl('/admin/live/doEdit/${editType}?id=${whgLive.id}'),
        onSubmit : function(param) {
            var _valid = $(this).form('enableValidation').form('validate');
            if(_valid){
                if(!checkAll()){
                    _valid = false;
                    $('#whgwin-edit-btn-save').off('click').one('click',saveFun);
                    return _valid;
                }
                $.messager.progress();
            }else{
                //失败时再注册提交事件
                $('#whgwin-edit-btn-save').off('click').one('click',saveFun);
            }
            return _valid;
        },
        success : function(data) {
            $.messager.progress('close');
            var Json = eval('('+data+')');
            if(Json && Json.success == '1'){
                if($('#whgff input[name="onlyshow"]').val() != "1") {
                    window.parent.$('#whgdg').datagrid('reload');
                }
                WhgComm.editDialogClose();
            } else {
                $.messager.alert('提示', '操作失败:'+Json.errormsg+'!', 'error');
                $('#whgwin-add-btn-save').off('click').one('click', saveFun);
            }
        }
    });

    function checkAll() {
        if(checkDomain()&&checkCover()&&checklbt()&&checkdesc()){
            return true;
        }
        return false;
    }

    function saveFun() {
        $("#whgff").submit();
    }
    
    $("#whgwin-edit-btn-save").off("click").one("click",saveFun);
    
    var __WhgUploadImg1 = null;
    var __WhgUploadImg2 = null;
    var remark = null;
    $(function (){
        //初始化富文本
        var ueConfig = {
            scaleEnabled: false,
            autoFloatEnabled: false
        };
        remark = UE.getEditor('remark', ueConfig);
        __WhgUploadImg1 = WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'act_imgurl1', previewImgId: 'previewImg1'});
        __WhgUploadImg2 = WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn2', hiddenFieldId: 'act_imgurl2', previewImgId: 'previewImg2',needCut:false});
        setFlowaddr();
        setPlayaddr();
        showSelectIslbt();
        /*setBranch();*/
    });

    /*function setBranch() {
        $.getJSON("$ {basePath}/admin/branch/branchListUser",function (data) {

            if("1" != data.success){
                $.messager.alert("错误", data.errormsg, 'error');
                return;
            }
            var rows = data.rows;
            $("#branch").combobox("loadData",rows);
            var branchId = "$ {whBranchRel.branchid}";
            if(0 < rows.length){
                branchId = branchId != ""?branchId:rows[0].id;
                $("#branch").combobox("setValue",branchId);
            }
        });
    }*/

    $.extend($.fn.validatebox.defaults.rules, {
        bmEndTime: {
            validator: function(value, param){
                //alert(value);
                var sdVal = $('#'+param[0]).datetimebox('getValue');
                var d1 = WhgComm.parseDateTime(sdVal);
                var d2 = WhgComm.parseDateTime(value);
                return d2.getTime()>d1.getTime();
            },
            message: '结束时间必须大于开始时间.'
        },
        traEndTime: {
            validator: function(value, param){
                var _type = param[0];
                var _refid = param[1];
                var mutle = $('input[name="ismultisite"]:checked').val();

                if(parseInt(mutle) === 0){
                    return true;
                }else{
                    if(_type === 0){
                        var sdVal = $("#whgff").find("input[name='enrollendtime']").val();
                        var d1 = WhgComm.parseDateTime(sdVal);
                        var d2 = WhgComm.parseDateTime(value+" 23:59:59");
                        return d2.getTime() > d1.getTime();
                    }else if(_type === 1){
                        var sdVal = $('#'+param[1]).datebox('getValue');
                        var d1 = WhgComm.parseDateTime(sdVal);
                        var d2 = WhgComm.parseDateTime(value+" 23:59:59");
                        return d2.getTime() > d1.getTime();
                    }
                }
            },
            message: '培训时间必须晚于培训报名时间.'
        },
        _validage: {
            validator: function(value, param){
                var val = $('#'+param[0]).numberspinner('getValue');
                return parseInt(value) > parseInt(val);
            },
            message: '培训适合年龄后面的必须大于前面的.'
        },
        sinedit_Time:{
            validator: function(value, param){
                var sdVal = $('#'+param[0]).datetimebox('getValue');
                var d1 = WhgComm.parseDateTime(sdVal);
                var d2 = WhgComm.parseDateTime(value);
                var mutle = $('input[name="ismultisite"]:checked').val();
                if(parseInt(mutle) == 0){
                    return d1.getTime() < d2.getTime();

                }else{
                    return true;
                }
            },
            message: '培训时间必须晚于培训报名时间.'
        }

    });

</script>
</html>
