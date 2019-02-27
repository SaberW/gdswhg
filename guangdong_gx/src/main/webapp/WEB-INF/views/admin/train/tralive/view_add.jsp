<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <c:choose>
        <c:when test="${not empty id and not empty targetShow}">
            <c:set var="pageTitle" value="在线课程管理-查看在线课程"></c:set>
        </c:when>
        <c:when test="${not empty id}">
            <c:set var="pageTitle" value="在线课程管理-编辑在线课程"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="在线课程管理-添加在线课程"></c:set>
        </c:otherwise>
    </c:choose>
    <title>${pageTitle}</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>

    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>
    <style>
        .none{ display: none; }
    </style>

    <script src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>

    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>

    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>

    <!-- 图片上传相关-END -->

    <!-- 根据地址取坐标 -->
    <script type="text/javascript" src="${basePath}/static/common/js/whg.maps.js"></script>
    <script type="text/javascript" src="${basePath}/static/common/js/area.js"></script>

    <style>
        .slider-h{margin-left: 10px}
    </style>
    <script>
        $.extend($.fn.validatebox.defaults.rules, {
            fixedEndTime: {
                validator: function (value, param) {
                    //固定周期的直播时间
                    var hhmm_s = $('#'+param[0]).timespinner('getValue');
                    var hhmm_e = value;
                    var isValid = true;
                    if(hhmm_s != ''){
                        var d1 = WhgComm.parseDateTime("2018-01-01 "+hhmm_s+":00");
                        var d2 = WhgComm.parseDateTime("2018-01-01 "+hhmm_e+":00");
                        var times = d2.getTime()-d1.getTime();
                        var isValid = times > 1000*60*10 && times < 21600000;//[直播时间：10分钟到6个小时之间]
                    }
                    return isValid;
                },
                message: '直播时间必须介于10分钟到6个小时之间.'
            },
            traTime: {//多场直播时间
                validator: function(value, param){
                    var _type = param[0];//0-starttime 1-endtime
                    var _refid = param[1];//0-starttime 1-endtime
                    var t_value = WhgComm.parseDateTime(value);

                    //必须大于的时间
                    var _ptime = false;
                    if(_type === 0){//0-starttime
                        var pDiv = $('#'+_refid).parents('div[name="multiContent"]');
                        var preDiv = pDiv.prev();
                        if(preDiv.is('[name="multiContent"]')){
                            var preEndTimeEl = preDiv.find('._endtime');
                            _ptime = preEndTimeEl.datetimebox('getValue');
                            var t_ptime = WhgComm.parseDateTime(_ptime);
                            if(t_ptime.getTime() >= t_value.getTime()){
                                $.fn.validatebox.defaults.rules.traTime.message ="直播开始时间必须大于"+_ptime+"。";
                                return false;
                            }
                        }else{//如果没有前续的，必须大于报名结束时间
                            var mustsignup = $("input[name='mustsignup']:checked").val();
                            if(mustsignup == '1'){
                                var enrollendtime = $('#enrollendtime').datetimebox('getValue');
                                if(enrollendtime != ''){
                                    _ptime = enrollendtime;
                                    var t_ptime = WhgComm.parseDateTime(_ptime);
                                    if(t_ptime.getTime() >= t_value.getTime()){
                                        $.fn.validatebox.defaults.rules.traTime.message ="直播开始时间必须大于报名结束时间。";
                                        return false;
                                    }
                                }
                            }
                            //直播开始时间必须大于当前时间
                            var now_ptime = new Date();
                            now_ptime.setTime(now_ptime.getTime());
                            if(now_ptime.getTime() >= t_value.getTime()){
                                $.fn.validatebox.defaults.rules.traTime.message ="直播开始时间必须大于当前时间。";
                                return false;
                            }
                        }
                    }else if(_type === 1){//1-endtime
                        _ptime = $('#'+_refid).datetimebox('getValue');
                        if(_ptime.substring(0,10) != value.substring(0,10)){
                            //直播时间段必须在同一天
                            $.fn.validatebox.defaults.rules.traTime.message ="直播时间必须在同一天";
                            return false;
                        }
                        var d2 = WhgComm.parseDateTime(value);
                        var d1 = WhgComm.parseDateTime(_ptime);
                        var times = d2.getTime()-d1.getTime();
                        var isValid = times > 1000*60*10 && times < 21600000;//[直播时间：10分钟到6个小时之间]
                        if(!isValid){
                            $.fn.validatebox.defaults.rules.traTime.message ="直播时间必须介于10分钟到6个小时之间.";
                            return false;
                        }
                    }
                    return true;
                },
                message: '请输入有效的直播时间。结束时间大于开始时间，各直播时间段不能重合！'
            },
            bmEndTime: {
                validator: function(value, param){
                    var sdVal = $('#'+param[0]).datetimebox('getValue');
                    var d1 = WhgComm.parseDateTime(sdVal);
                    var d2 = WhgComm.parseDateTime(value);
                    return d2.getTime()>d1.getTime();
                },
                message: '培训报名结束时间必须大于培训报名开始时间.'
            },
            traEndTime: {
                validator: function(value, param){
                    var _type = param[0];
                    var _refid = param[1];

                    if(_type === 0){
                        var d1;
                        var mustsignup = $("input[name='mustsignup']:checked").val();
                        if(mustsignup == '1'){
                            var sdVal = $("#enrollendtime").datetimebox('getValue');
                            d1 = WhgComm.parseDateTime(sdVal);
                            $.fn.validatebox.defaults.rules.traEndTime.message ="直播结束时间必须大于开始时间,并且必须大于报名结束时间";
                        }else{
                            //sdVal = new Date().Format("yyyy-MM-dd");
                            var now = new Date();
                            now.setDate(now.getDate()+1);
                            var nowStr = now.Format("yyyy-MM-dd");
                            d1 = WhgComm.parseDate(nowStr);
                            $.fn.validatebox.defaults.rules.traEndTime.message ="直播时间周期必须从"+nowStr+"开始";
                        }
                        var d2 = WhgComm.parseDateTime(value+" 23:59:59");
                        return d2.getTime() > d1.getTime();
                    }else if(_type === 1){
                        var sdVal = $('#'+param[1]).datebox('getValue');
                        var d1 = WhgComm.parseDateTime(sdVal+" 23:59:59");
                        var d2 = WhgComm.parseDateTime(value+" 23:59:59");
                        $.fn.validatebox.defaults.rules.traEndTime.message ="直播时间周期的结束时间必须大于开始时间";
                        return d2.getTime() > d1.getTime();
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
            sinTime:{
                validator: function(value, param){
                    var d2 = WhgComm.parseDateTime(value);//当前时间
                    var isSt = param[1]; //st-直播开始时间 et-直播结束时间
                    if(isSt == 'st'){
                        var isValid = true;
                        var mustsignup = $("input[name='mustsignup']:checked").val();
                        if(mustsignup == '1'){//必须报名
                            var sdVal = $('#'+param[0]).datetimebox('getValue');//报名结束时间
                            var d1 = WhgComm.parseDateTime(sdVal);//
                            isValid = d2.getTime()>=d1.getTime();
                            if(!isValid)$.fn.validatebox.defaults.rules.sinTime.message ="直播结束时间必须大于开始时间,并且必须大于报名结束时间";
                        }
                        if(isValid){
                            var d1 = new Date();
                            isValid = d2.getTime() >= (d1.getTime());
                            if(!isValid)$.fn.validatebox.defaults.rules.sinTime.message ="直播开始时间必须大于当前时间";
                        }
                        return isValid;
                    }else{
                        var sdVal = $('#'+param[0]).datetimebox('getValue');
                        var d1 = WhgComm.parseDateTime(sdVal);//直播开始时间
                        var times = d2.getTime()-d1.getTime();
                        var isValid = value.substring(0,10) == sdVal.substring(0,10) && times > 1000*60*10 && times < 21600000; //[直播时间：10分钟到6个小时之间]
                        if(!isValid){
                            $.fn.validatebox.defaults.rules.sinTime.message ="直播时间必须在同一天，直播时间必须介于10分钟到6个小时之间.";
                            return isValid;
                        }
                    }
                    return true;
                },
                message: '直播时间必须在同一天，直播开始时间必须大于当前时间，直播时间必须介于10分钟到6个小时之间.'
            },
            oneDay:{
                validator: function(value, param){
                    var sdVal = $('#'+param[0]).datetimebox('getValue');
                    if(value && sdVal){
                        return sdVal.substring(0,11) == value.substring(0,11);
                    }
                    return true;
                },
                message: '直播时间必须在同一天'
            }
        });
    </script>
</head>
<body>
<form id="whgff" method="post" class="whgff">
    <h2>${pageTitle}</h2>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>标　　题　　：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="title" value="${whgTra.title}" style="width:500px; height:32px" data-options="prompt:'请输入标题',required:true,validType:['length[1,60]']" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>图　　片　　：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture1" name="trainimg" value="${whgTra.trainimg}">
            <div class="whgff-row-input-imgview" id="previewImg1"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>所属单位　　：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:245px; height:32px" name="cultid" id="cultid" data-options="required:true" />
            <input class="easyui-combobox" style="width:245px; height:32px" name="deptid" id="deptid" data-options="required:true" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>区　　域　　：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:150px; height:32px" id="province" name="province" data-options="required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name', data:__PROVINCE, onChange:myChangeProvince"/>
            <input class="easyui-combobox" style="width:150px; height:32px" id="__CITY_ELE" name="city" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name', data:[], onChange:myChangeCity"/>
            <input class="easyui-combobox" style="width:179px; height:32px" id="__AREA_ELE" name="area" data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>主办方：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" style="width:500px; height:32px" name="host" id="host" value="${whgTra.host}" data-options="prompt:'请输入主办单位名称', validType:['length[1,60]']" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>艺术分类　　：</div>
        <div class="whgff-row-input">
            <div class="checkbox checkbox-primary" id="arttype" name="arttype"></div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>课程分类　　：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary" id="etype" name="etype"></div>
        </div>
    </div>

    <%--<div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>培训周期：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" value="${whgTra.isterm}"  name="isterm" js-data='[{"id":"0","text":"学年制"},{"id":"1","text":"学期制"},{"id":"2","text":"短期制"}]'></div>
        </div>
    </div>--%>

    <%--<div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>是否收费：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" value="${whgTra.ismoney}" name="ismoney" js-data='[{"id":"0","text":"否"},{"id":"1","text":"是"}]'></div>
        </div>
    </div>--%>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>需要报名　　：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" value="${whgTra.mustsignup}" name="mustsignup" js-data='[{"id":"0","text":"否"},{"id":"1","text":"是"}]'></div>
        </div>
    </div>

    <%--<div class="whgff-row none nobm">
        <div class="whgff-row-label"><label style="color: red">*</label>录取规则：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" value="${whgTra.isbasicclass}"  name="isbasicclass" js-data='[&lt;%&ndash;{"id":"0","text":"需要面试"},{"id":"1","text":"需人工录取"},&ndash;%&gt;{"id":"2","text":"即报即得"}]'></div>
        </div>
    </div>--%>
    <input type="hidden" name="isbasicclass" value="2"/>

    <div class="whgff-row none nobm">
        <div class="whgff-row-label"><label style="color: red">*</label>实名制　　　：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" value="${whgTra.isrealname}" name="isrealname" js-data='[{"id":"0","text":"否"},{"id":"1","text":"是"}]'></div>
        </div>
    </div>

    <div class="whgff-row none none nobm">
        <div class="whgff-row-label"><label style="color: red">*</label>报名人数上限：</div>
        <div class="whgff-row-input">
            <input class="easyui-numberspinner" name="maxnumber" value="${whgTra.maxnumber}" style="width:500px; height:32px" data-options="min:0,required:true,prompt:'请输入人数'">
        </div>
    </div>
    <div class="whgff-row none nobm">
        <div class="whgff-row-label"><label style="color: red">*</label>基础报名人数：</div>
        <div class="whgff-row-input">
            <input class="easyui-numberspinner" name="basicenrollnumber" value="${whgTra.basicenrollnumber}" style="width:500px; height:32px" data-options="min:0,required:true,prompt:'请输入人数'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>直播场次　　：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" value="${whgTra.ismultisite}"  name="ismultisite" js-data='[{"id":"0","text":"单场"},{"id":"1","text":"多场"},{"id":"2","text":"固定场"}]'></div>
        </div>
    </div>

    <%--<div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>培训时长：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="duration" value="${whgTra.duration}" style="width:500px; height:32px" data-options="validType:['length[1,100]'],prompt:'请填写培训时长'">分钟
        </div>
    </div>--%>

    <div class="whgff-row none nobm">
        <div class="whgff-row-label"><label style="color: red">*</label>报名时间　　：</div>
        <div class="whgff-row-input">
            <input class="easyui-datetimebox enrollstarttime" id="enrollstarttime" name="enrollstarttime" value="<fmt:formatDate value="${whgTra.enrollstarttime}" pattern="yyyy-MM-dd HH:mm:ss"/>" style="width:200px; height:32px" data-options="required:true"/>至
            <input class="easyui-datetimebox enrollendtime" id="enrollendtime" name="enrollendtime" value="<fmt:formatDate value="${whgTra.enrollendtime}" pattern="yyyy-MM-dd HH:mm:ss"/>" style="width:200px; height:32px" data-options="required:true, validType:'bmEndTime[\'enrollstarttime\']'"/>
        </div>
    </div>

    <div class="whgff-row train">
        <div class="whgff-row-label "><label style="color: red">*</label>直播时间周期：</div>
        <div class="whgff-row-input">
            <input class="easyui-datebox starttime" id="starttime" name="starttime" value="<fmt:formatDate value="${whgTra.starttime}" pattern="yyyy-MM-dd"/>" style="width:200px; height:32px" data-options="required:true, validType:'traEndTime[0,\'enrollendtime\']'" />至
            <input class="easyui-datebox endtime" id="endtime" name="endtime" value="<fmt:formatDate value="${whgTra.endtime}" pattern="yyyy-MM-dd"/>" style="width:200px; height:32px" data-options="required:true, validType:'traEndTime[1,\'starttime\']'"/>
        </div>
    </div>

    <!--添加时单场的培训上课时间-->
    <div class="whgff-row single" >
        <div class="whgff-row-label"><label style="color: red">*</label>培训直播时间：</div>
        <div class="whgff-row-input">
            <input class="easyui-datetimebox sin_starttime" id="sin_starttime" name="sin_starttime" style="width:200px; height:32px" data-options="validType:'sinTime[\'enrollendtime\', \'st\']'"/>至
            <input class="easyui-datetimebox sin_endtime" id="sin_endtime" name="sin_endtime" style="width:200px; height:32px" data-options="validType:['sinTime[\'sin_starttime\', \'et\']', 'oneDay[\'sin_starttime\']']"/>
        </div>
    </div>

    <!--添加时多场的培训上课时间-->
    <c:if test="${empty course}">
        <div class="whgff-row multi" name="multiContent">
            <div class="whgff-row-label"><label style="color: red">*</label>培训直播时间：</div>
            <div class="whgff-row-input">
                <input class="easyui-datetimebox _starttime" name="_starttime" id="_starttime_1"  style="width:200px; height:32px" data-options="required:true,validType:'traTime[0, \'_starttime_1\']'"/>至
                <input class="easyui-datetimebox _endtime" name="_endtime" id="_starttime_2" style="width:200px; height:32px" data-options="required:true,validType:'traTime[1,\'_starttime_1\']'"/>
                <a href="javascript:void(0)" class="timeico add">添加</a>
                    <%-- <a href="javascript:void(0)" class="timeico del">删除</a>--%>
            </div>
        </div>
    </c:if>

    <div class="whgff-row fixed">
        <div class="whgff-row-label"><label style="color: red">*</label>固定周几　　：</div>
        <div class="whgff-row-input">
            <div class="checkbox checkbox-primary whg-js-data"  name="fixedweek" value="${whgTra.fixedweek}" js-data='[{"id":"1","text":"周一"},{"id":"2","text":"周二"},{"id":"3","text":"周三"},{"id":"4","text":"周四"},{"id":"5","text":"周五"},{"id":"6","text":"周六"},{"id":"7","text":"周日"}]'> </div>
        </div>
    </div>

    <div class="whgff-row fixed">
        <div class="whgff-row-label"><label style="color: red">*</label>固定直播时间：</div>
        <div class="whgff-row-input">
            <input class="easyui-timespinner _coursestarttime" name="fixedstarttime" id="fixedstarttime" value="<fmt:formatDate value="${whgTra.fixedstarttime}" pattern="HH:mm"/>"  style="width:200px; height:32px"/>至
            <input class="easyui-timespinner _courseendtime" name="fixedendtime" id="fixedendtime" data-options="validType:'fixedEndTime[\'fixedstarttime\']'" value="<fmt:formatDate value="${whgTra.fixedendtime}" pattern="HH:mm"/>" style="width:200px; height:32px" />
        </div>
    </div>

    <div class="whgff-row none nobm">
        <div class="whgff-row-label"><label style="color: red"></label>适合年龄　　：</div>
        <div class="whgff-row-input">
            <input class="easyui-numberspinner" id="age1" name="age1" value="${age1}" style="width:80px;" data-options="min:1,max:100, editable:true">&nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;
            <input class="easyui-numberspinner" id="age2" name="age2" value="${age2}" style="width:80px;" data-options="min:1,max:100, editable:true, validType:'_validage[\'age1\']'">岁
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>文化品牌　　：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:500px; height:32px" name="ebrand" id="ebrand" data-options="prompt:'请选择文化品牌'" />
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>微专业　　　：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" id="majorid" name="majorid" style="width:500px; height:32px" data-options="multiple:true,editable:true"/>
        </div>
    </div>

    <div class="whgff-row none nobm">
        <div class="whgff-row-label"><label style="color: red"></label>培训组　　　：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" id="groupid" name="groupid" style="width:500px; height:32px" data-options="limitToList:true, valueField:'id', textField:'name'"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>联系电话　　：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="phone" value="${whgTra.phone}" style="width:500px; height:32px" data-options="validType:'isPhoneTel[\'traphone\']',prompt:'请填写联系电话'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>联系人　　　：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="contacts" value="${whgTra.contacts}" style="width:500px; height:32px" data-options="validType:['length[1,20]'],prompt:'请填写联系人'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>标　　签　　：</div>
        <div class="whgff-row-input">
            <div class="checkbox checkbox-primary" id="etag" name="etag"></div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>关键字　　　：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" id="ekey" style="width:500px; height:32px" validType="notQuotes" data-options="multiple:true, editable:true, prompt:'请填写关键字'
             ,onChange: function (val, oldval) {
                    if (val.length>1 && val[0]==''){
                        val.shift();
                        $(this).combobox('setValues', val);
                    }
                }"/>
            <span>（如需手动输入，请用英文逗号隔开！）</span>
        </div>
    </div>


    <%--<div class="whgff-row">
        <div class="whgff-row-label">地址：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" id="address" name="address" value="${whgTra.address}" style="width:500px; height:32px" data-options="required:false,validType:['length[1,200]'],prompt:'请填写地址'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">坐标：</div>
        <div class="whgff-row-input">
            <input class="easyui-numberbox" id="longitude" name="longitude" value="${whgTra.longitude}" style="width:100px; height:32px" data-options="required:false, precision:6,readonly:true,prompt:'X轴'">
            <input class="easyui-numberbox" id="latitude" name="latitude" value="${whgTra.latitude}" style="width:100px; height:32px" data-options="required:false, precision:6,readonly:true,prompt:'y轴'">
            <a class="easyui-linkbutton whg-maps" map-addr="#address" vm-x="#longitude" vm-y="#latitude" id="getXYPointBtn" text="选择坐标"></a>
        </div>
    </div>--%>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>培训老师　　：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" id="teachername" name="teachername" value="${whgTra.teachername}" style="width:500px; height:32px" data-options="multiple:true,editable:true,valueField:'name',textField:'name',url:'${basePath}/admin/train/tea/srchList'
            ,onChange: function (val, oldval) {
                    if (val.length>1 && val[0]==''){
                        val.shift();
                        $(this).combobox('setValues', val);
                    }
                }"/>
            <span>（如需手动输入，请用英文逗号隔开！）</span>
        </div>
    </div>

    <%--<div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>是否显示最大报名人数：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" value="${whgTra.isshowmaxnumber}" name="isshowmaxnumber" js-data='[{"id":"0","text":"否"},{"id":"1","text":"是"}]'></div>
        </div>
    </div>--%>


    <div class="whgff-row">
        <div class="whgff-row-label">温馨提示　　：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="notice" value="${not empty whgTra.notice? whgTra.notice :'请按时参加培训，不得无故缺席，如累计无故缺席数次后，一段时间内将被取消报名资格；'}" style="width:600px; height:100px" data-options="multiline:true, validType:['length[1,1000]'],prompt:'请填写温馨提示'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>培训详情　　：</div>
        <div class="whgff-row-input">
            <script id="detail" name="detail" type="text/plain" style="width:600px; height:300px;"></script>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>培训大纲　　：</div>
        <div class="whgff-row-input">
            <script id="catalog" name="catalog" type="text/plain" style="width:600px; height:300px;"></script>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>老师介绍　　：</div>
        <div class="whgff-row-input">
            <script id="teacherIntro" name="teacherIntro" type="text/plain" style="width:600px; height:300px;"></script>
        </div>
    </div>
</form>

<div class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <a class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok">提 交</a>
    <a class="easyui-linkbutton whgff-but-clear" iconCls="icon-no">返 回</a>
</div>


<script>
    //省市区
    var province = WhgComm.getProvince()?WhgComm.getProvince():'广东省';
    var city = WhgComm.getCity()?WhgComm.getCity():"";
    function myChangeProvince(newVal, oldVal) {
        changeProvince(newVal, oldVal, function(){
            if(typeof(window.__init_city) == 'undefined'){
                $('#__CITY_ELE').combobox('setValue', city);
                window.__init_city = true;
            }
        });
    }

    function myChangeCity(newVal, oldVal) {
        changeCity(newVal, oldVal, function () {
            if(typeof(window.__init_area) == 'undefined'){
                $('#__AREA_ELE').combobox('setValue', '${whgTra.area}');
                window.__init_area = true;
            }
        });
    }  //省市区------END


    //显示培训时间(0单场,1多场,2固定场)
    function showTrainTime(isMulti){
        var id = '${id}';
        var state = '${state}';
        //添加的时候
        if(!id){
            //单场
            if(0 == isMulti){
                $(".single").css("display","");
                $(".sin_starttime").datetimebox({required:true});
                $(".sin_endtime").datetimebox({required:true});
                $(".multi").css("display","none");
                $("._starttime").datetimebox({required:false});
                $("._endtime").datetimebox({required:false});
                $(".fixed").css("display","none");
                $("._coursestarttime").timespinner({required:false});
                $("._courseendtime").timespinner({required:false});
                $(".train").css("display","none");
                $(".starttime").datebox({required:false});
                $(".endtime").datebox({required:false});
            }
            //多场
            if(1 == isMulti){
                $(".single").css("display","none");
                $(".sin_starttime").datetimebox({required:false});
                $(".sin_endtime").datetimebox({required:false});
                $(".multi").css("display","");
                $("._starttime").datetimebox({required:true});
                $("._endtime").datetimebox({required:true});
                $(".fixed").css("display","none");
                $("._coursestarttime").timespinner({required:false});
                $("._courseendtime").timespinner({required:false});
                $(".train").css("display", "none");
                $(".starttime").datebox({required:false});
                $(".endtime").datebox({required:false});
            }
            //固定场
            if(2 == isMulti){
                $(".single").css("display","none");
                $(".sin_starttime").datetimebox({required:false});
                $(".sin_endtime").datetimebox({required:false});
                $(".multi").css("display","none");
                $("._starttime").datetimebox({required:false});
                $("._endtime").datetimebox({required:false});
                $(".fixed").css("display","");
                $("._coursestarttime").timespinner({required:true});
                $("._courseendtime").timespinner({required:true});
                $(".train").css("display","");
                $(".starttime").datebox({required:true});
                $(".endtime").datebox({required:true});
            }
        }
    }

    //是否需要报名的处理
    function doMustSignUp(){
        $('.whg-js-data').on('click','input[name="mustsignup"]',function(){
            var ck = this.checked;
            if(ck){
                must_sign_up($(this).val());
            }
        });
        function must_sign_up(value) {
            if( value == '1'){//需要报名
                $('.nobm').removeClass('none');
                $('.nobm .easyui-numberspinner').numberspinner({novalidate:true, required:true});
                $('.nobm .easyui-datetimebox').datetimebox({novalidate:true, required:true});
                $('#age1').numberspinner({novalidate:true, required:false});
                $('#age2').numberspinner({novalidate:true, required:false});
            }
            if( value == '0'){//不需报名
                $('.nobm').addClass('none');
                $('.nobm .easyui-numberspinner').numberspinner({novalidate:true, required:false});
                $('.nobm .easyui-datetimebox').datetimebox({novalidate:true, required:false});
            }
        }
        var mustsignup = $("input[name='mustsignup']:checked").val();
        must_sign_up(mustsignup);
    }

    //处理UE
    var ueConfig = {
        scaleEnabled:true,
        autoFloatEnabled: false,
        elementPathEnabled:false,
        readonly: '${targetShow}'? true: false
    };
    var teacherIntro = UE.getEditor('teacherIntro', ueConfig);
    var catalog = UE.getEditor('catalog', ueConfig);
    var detail = UE.getEditor('detail', ueConfig);
    //UE 设置值
    teacherIntro.ready(function(){  teacherIntro.setContent('${whgTra.teacherdesc}') });
    catalog.ready(function(){  catalog.setContent('${whgTra.outline}') });
    detail.ready(function(){  detail.setContent('${whgTra.coursedesc}') });

    $(function () {
        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'${whgTra.cultid}',cultOnChange: function (newVal, oldVal) {
                $("#teachername").combobox({
                    url:'${basePath}/admin/train/tea/srchList?cultid='+newVal,
                    valueField:'name',
                    textField:'name'
                });
                $("#majorid").combobox({
                    url:'${basePath}/admin/tra/major/srchList?cultid='+newVal,
                    valueField:'id',
                    textField:'name'
                });
                $('#groupid').combobox({
                    url:'${basePath}/admin/yunwei/group/srchList?cultid='+newVal,
                    multiple:true,
                    editable:false,
                    limitToList:false,
                    valueField:'id',
                    textField:'name'
                });
            },
            deptEid:'deptid', deptValue:'${whgTra.deptid}',
            ywiWhppEid:'ebrand', ywiWhppValue:'${whgTra.ebrand}',
            ywiArtTypeEid:'arttype', ywiArtTypeValue:'${whgTra.arttype}',
            ywiTypeEid:'etype', ywiTypeValue:'${whgTra.etype}', ywiTypeClass:16,
            ywiKeyEid:'ekey', ywiKeyValue:'${whgTra.ekey}', ywiKeyClass:16,
            ywiTagEid:'etag', ywiTagValue:'${whgTra.etag}', ywiTagClass:16
        });

        var mid = '${mid}';
        $('#province').combobox('setValue', province);
        //根据地址取坐标
        WhgMap.init({basePath:'${basePath}', addrFieldId:'address', xpointFieldId:'longitude', ypointFieldId:'latitude', getPointBtnId:'getXYPointBtn'});
        //图片初始化
        var __WhgUploadImg = WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1'});

        //添加一行课程添加的表单
        $(document).on('click','.add',function(){
            var trs = $("#whgff").find("div[name='multiContent']");
            var length = $(trs).length;
            var content = "";
            if(length >= 1){
                for(var i=0;i<length;i++){
                    if(i<1){
                        if(!id) {

                            var _sid = WhgIDUtil.getId();
                            var _eid = WhgIDUtil.getId();
                            var addHtmlContent = '<div class="whgff-row-label"><label style="color: red">*</label>培训直播时间：</div>'
                                    + '<div class="whgff-row-input">'
                                    + '<input class="easyui-datetimebox _starttime" name="_starttime" id="_starttime_' + _sid + '" style="width:200px; height:32px" data-options="required:true, validType:\'traTime[0, \\\'_starttime_' + _sid + '\\\']\'">至'
                                    + '&nbsp;<input class="easyui-datetimebox _endtime" name="_endtime" id="_endtime_' + _eid + '" style="width:200px; height:32px" data-options="required:true, validType:\'traTime[1, \\\'_starttime_' + _sid + '\\\']\'" >'
                                    + '&nbsp;<a href="javascript:void(0)" class="timeico add">添加</a>'
                                    + '&nbsp;&nbsp;<a href="javascript:void(0)" class="timeico del">删除</a>'
                                    + '</div>';
                        }else{
                            var _sid = WhgIDUtil.getId(); var _eid = WhgIDUtil.getId();
                            var addHtmlContent = '<div class="whgff-row-label"><label style="color: red">*</label>培训直播时间：</div>'
                                    +'<div class="whgff-row-input">'
                                    +'<input class="easyui-datetimebox starttime_edit" name="_starttime" id="_starttime_'+_sid+'" style="width:200px; height:32px" data-options="required:true, validType:\'edittraTime[0, \\\'_starttime_'+_sid+'\\\']\'">至'
                                    +'&nbsp;<input class="easyui-datetimebox endtime_edit" name="_endtime" id="_endtime_'+_eid+'" style="width:200px; height:32px" data-options="required:true, validType:\'edittraTime[1, \\\'_starttime_'+_sid+'\\\']\'" >'
                                    +'&nbsp;<a href="javascript:void(0)" class="timeico add">添加</a>'
                                    +'&nbsp;&nbsp;<a href="javascript:void(0)" class="timeico del">删除</a>'
                                    +'</div>';
                        }
                        content += '<div class="whgff-row multi js-temp-addnow" name="multiContent">' + addHtmlContent + '</div>';
                    }
                }
                $($(trs)[length-1]).after(content);
                $.parser.parse("div.js-temp-addnow[name='multiContent']")
            }
            //找到添加的行清除原有内容
            $("#whgff").find("div.js-temp-addnow[name='multiContent']").each(function (i){
                $(this).find('input').val("");
                $(this).attr("name","multiContent");
                $(this).removeClass("js-temp-addnow");
                //$(this).find(".easyui-datetimebox").datetimebox({});
            });
        }) //添加一行课程添加的表单  END

        //删除一行
        $(document).on('click','.del',function(){
            var length = $("#whgff").find("div[name='multiContent']").length;
            if(1 == length){
                $.messager.alert("提示","最少保留一次培训日期！");
                return
            }
            //删除当前行,上一行
            var curTr = $(this).parent().parent();
            $(curTr).remove();
        })  //删除一行 END

        //多场|单场|固定场
        var isMulti = $('input[name="ismultisite"]:checked').val();
        showTrainTime(isMulti);

        //单场培训、多场、固定场
        $('.whg-js-data').on('click','input[name="ismultisite"]',function(){
            var ck = this.checked;
            var value = $(this).val();
            if(ck){
                showTrainTime(value);
            }else{
                showTrainTime(0);
            }
        });

        //是否需要报名的处理
        doMustSignUp();

        var buts = $("div.whgff-but");
        var id = '${id}';
        var targetShow = '${targetShow}';
        var frm = $("#whgff");
        //添加时开启清除 ，其它为返回
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
        var url = id ? "${basePath}/admin/traLive/edit" : "${basePath}/admin/traLive/add";
        frm.form({
            url: url,
            novalidate: true,
            onSubmit: function (param) {
                if (id){
                    param.id = id;
                }
                if (mid){
                    param.mid = mid;
                }
                $(this).form("enableValidation");
                var isValid = $(this).form('validate');
                if (isValid){
                    //富文本验证
                    var isUEvalid = validateUE();
                    if (isUEvalid){
                        param.teacherdesc = teacherIntro.getContent();
                        param.outline = catalog.getContent();
                        param.coursedesc = detail.getContent();
                    }else{
                        isValid = false;
                    }//富文本验证 --END

                    //报名人数
                    var _basicenrollnumber = $("#whgff").find("input[name='basicenrollnumber']").val();
                    var _maxnumber = $("#whgff").find("input[name='maxnumber']").val();
                    if(parseInt(_basicenrollnumber) > parseInt(_maxnumber)){
                        $.messager.alert("错误", '基础报名人数必须小于报名人数限制！', 'error');
                        isValid = false;
                    }

                    //固定班周几验证
                    if(parseInt($('input[name="ismultisite"]:checked').val()) == 2){

                        var week = $('input[name="fixedweek"]:checked').val();
                        if(!week){
                            $.messager.alert("错误", '固定周几不能为空！', 'error');
                            isValid = false;
                        }

                        var starthour = $("._coursestarttime").timespinner('getHours');
                        var startmin = $("._coursestarttime").timespinner('getMinutes');
                        var endhour = $("._courseendtime").timespinner('getHours');
                        var endmin = $("._courseendtime").timespinner('getMinutes');
                        var _a = starthour*60+startmin;
                        var _b = endhour*60+endmin;

                        if(_a > _b){
                            $.messager.alert("错误", '固定上课开始时间不能晚于结束时间！', 'error');
                            isValid = false;
                        }

                    }//固定班周几验证 --END

                }
                if (!isValid){
                    $.messager.progress('close');
                    buts.find("a.whgff-but-submit").off('click').one('click', function () {
                        frm.submit();
                    });
                }
                param.ekey = $("#ekey").combobox("getText");
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
                    /*$(this).form('clear');
                    frm.find("div.radio").find(':radio:eq(0)').click();
                    teacherIntro.setContent('');
                    catalog.setContent('');
                    detail.setContent('');*/
                    $.messager.show({
                        title:'提示消息',
                        msg:'培训信息提交成功',
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

        //处理UE项的验证
        function validateUE(){
            //图片不能为空
            var picture1 = $("#cult_picture1").val();
            if (!picture1){
                $.messager.alert("错误", '图片不能为空！', 'error');
                return false;
            }
            //艺术分类不能为空
            var arttype = $("#whgff").find("input[name='arttype']:checked").val();
            if (!arttype){
                $.messager.alert("错误", '艺术分类不能为空！', 'error');
                return false;
            }
            //课程详情不能为空
            if (!detail.hasContents()){
                $.messager.alert("错误", '培训详情不能为空！', 'error');
                return false;
            }
            return true;
        }
    });
</script>
</body>
</html>
