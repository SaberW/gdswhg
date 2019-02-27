<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">

    <c:choose>
        <c:when test="${pageType eq 'show'}">
            <c:set var="pageTitle" value="众筹项目管理-查看众筹项目"></c:set>
        </c:when>
        <c:when test="${pageType eq 'edit'}">
            <c:set var="pageTitle" value="众筹项目管理-编辑众筹项目"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="众筹项目管理-添加众筹项目"></c:set>
        </c:otherwise>
    </c:choose>
    <title>${pageTitle}</title>

    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>

    <script src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>

    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>

    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <!-- 图片上传相关-END -->
    <script type="text/javascript" src="${basePath}/static/plupload/upload-file.js"></script>

    <script type="text/javascript" src="${basePath}/static/common/js/area.js"></script>

    <script type="text/javascript" src="${basePath}/static/common/js/whg.maps.js"></script>

    <script src="${basePath}/static/admin/js/whgmodule-venseatmaps.js"></script>
</head>
<body class="body_add">
<form id="whgff" method="post" class="whgff">
    <h2>${pageTitle}</h2>


    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>众筹类型：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="gat_etype" value="${info.gat_etype}"
                 js-data='[{"id":"4","text":"活动众筹"},{"id":"5","text":"培训众筹"},{"id":"0","text":"其它众筹"}]'>
            </div>
        </div>
    </div>


    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>众筹名称：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="gat_title" value="${info.gat_title}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,100]'], prompt:'请输入名称'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>封面：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="gat_imgurl_2" name="gat_imgurl" value="${info.gat_imgurl}">
            <div class="whgff-row-input-imgview" id="gat_imgurl_3"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="gat_imgurl_1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 600*450，大小为2MB以内</i>
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>众筹时间：</div>
        <div class="whgff-row-input">
            <input class="easyui-datetimebox" id="gat_timestart" name="gat_timestart" value="<fmt:formatDate value='${info.gat_timestart}' pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>"
                   required="true" data-options="editable:false,prompt:'请选择'"/>
            至
            <input class="easyui-datetimebox" id="gat_timeend" name="gat_timeend" value="<fmt:formatDate value='${info.gat_timeend}' pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>"
                   required="true" data-options="editable:false,prompt:'请选择',validType:'bmEndTime[\'gat_timestart\']'"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>所属单位：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:500px; height:32px" name="gat_cultid" id="cultid" data-options="editable:false,required:true" />
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>区域：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:150px; height:32px" id="province" name="gat_province" data-options="readonly:true, required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name', data:__PROVINCE, onChange:changeProvince"/>
            <input class="easyui-combobox" style="width:150px; height:32px" id="__CITY_ELE" name="gat_city" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name', data:[], onChange:changeCity"/>
            <input class="easyui-combobox" style="width:179px; height:32px" id="__AREA_ELE" name="gat_area" data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">众筹品牌：</div>
        <div class="whgff-row-input">
            <%--<input class="easyui-combobox" id="gat_brandid" name="gat_brandid" value="${info.gat_brandid}" style="width:400px; height:32px"
                   data-options="required:false, editable:true, limitToList: true, valueField:'id', textField:'title',
                   url: '${basePath}/admin/brand/getList'"/>--%>
            <input class="easyui-combobox" id="gat_brandid" name="gat_brandid" value="${info.gat_brandid}" style="width:400px; height:32px"
                   data-options="required:false, editable:true, limitToList: true, valueField:'id', textField:'title'"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>总份数：</div>
        <div class="whgff-row-input">
            <input class="easyui-numberbox" id="gat_numsum" name="gat_numsum" data-options="min:1" value="${info.gat_numsum}"
                   required="true"/>
            份，达成份数：
            <input class="easyui-numberbox" name="gat_nummin" id="gat_nummin" data-options="min:1" value="${info.gat_nummin}"
                   required="true" validType="gatNummin['gat_numsum']"/>
            份
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>回报说明：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="gat_explains" id="gat_explains" value="${info.gat_explains}" prompt="请补充说明，字数限制在50个字符内"
                   multiline="true" required="true" style="width:600px; height:150px"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">众筹介绍：</div>
        <div class="whgff-row-input">
            <div id="gat_descriptions" name="_descriptions" type="text/plain" style="width:800px; height:200px;"></div>
            <textarea id="value_descriptions" style="display: none;">${info.gat_descriptions}</textarea>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>众筹须知：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="gat_notice" id="gat_notice" value="${info.gat_notice}" prompt="固定内容，供需修改"
                   multiline="true" required="true" style="width:600px; height:200px"/>
        </div>
    </div>

    <div class="whgff-row  whgff-row-split">
        <div class="whgff-row-label"><i>*</i>提醒方式：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="gat_noticetype" value="${info.gat_noticetype}"
                 js-data='[{"id":"SMS","text":"短信"},{"id":"ZNX","text":"站内信"}]'>
            </div>
        </div>
    </div>

</form>

<div class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
</div>

<%-- 其它众筹内容块 begin --%>
<div id="module-other" style="display: none;">
    <%--<div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>所属单位：</div>
        <div class="whgff-row-input">
            <select class="EEE-combobox" name="gat_cultid" style="width:500px; height:32px"
                    data-options="editable:false, required:true, valueField:'id', textField:'text', value:'${info.gat_cultid}', data:WhgComm.getMgrCults(),prompt:'请选择所属单位'"></select>
        </div>
    </div>--%>
    <%--<div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>区域：</div>
        <div class="whgff-row-input">
            <input class="EEE-combobox" style="width:150px; height:32px" id="XXXXprovince" name="gat_province" data-options="required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name', data:__PROVINCE, onChange:changeProvince"/>
            <input class="EEE-combobox" style="width:150px; height:32px" id="XXXX__CITY_ELE" name="gat_city" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name', data:[], onChange:changeCity"/>
            <input class="EEE-combobox" style="width:179px; height:32px" id="XXXX__AREA_ELE" name="gat_area" data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
        </div>
    </div>--%>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>联系电话：</div>
        <div class="whgff-row-input">
            <input class="EEE-textbox" name="gat_phone" value="${info.gat_phone}" style="width:500px; height:32px" data-options="validType:'isPhone',prompt:'请填写联系电话'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">
            上传附件：
        </div>
        <div class="whgff-row-input">
            <input  id="gat_enclosure_2" name="gat_enclosure" value="${info.gat_enclosure }" data-options="required:true" style="width:600px;height:32px;" readonly="readonly">
            <div class="whgff-row-input-file" id="gat_enclosure_3">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="gat_enclosure_1">选择文件</a></i>
                <i>附件格式为doc,docx,xls,zip,xlsx，建议图文件大小为10MB以内</i>
            </div>
        </div>
    </div>
</div>
<%-- 其它众筹内容块 end --%>

<%-- 活动众筹内容块 begin --%>
<div id="module-act" style="display: none;">
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>活动简介：</div>
        <div class="whgff-row-input">
            <input class="EEE-textbox" name="actsummary" value="${info.actsummary}"  style="width:500px; height:32px" data-options="required:true,validType:['length[1,60]']">
        </div>
    </div>

    <%--<div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>区域：</div>
        <input class="EEE-combobox" style="width:150px; height:32px" id="XXXprovince" name="province" data-options="required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name', data:__PROVINCE, onChange:changeProvince"/>
        <input class="EEE-combobox" style="width:150px; height:32px" id="XXX__CITY_ELE" name="city" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name', data:[], onChange:changeCity"/>
        <input class="EEE-combobox" style="width:179px; height:32px" id="XXX__AREA_ELE" name="areaid" data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
    </div>--%>

    <div class="whgff-row">
        <div class="whgff-row-label">文化品牌：</div>
        <div class="whgff-row-input">
            <div class="whgff-row-input">
                <input class="EEE-combobox" style="width:500px; height:32px" name="ebrand" id="XXXebrand"  data-options="prompt:'请选择文化品牌'" />
            </div>
        </div>
    </div>
    <%--<div class="whgff-row">
        <div class="whgff-row-label">场馆：</div>
        <div class="whgff-row-input">
            <input class="EEE-combobox" name="venueid" id="venueid" value="${info.venueid}" panelHeight="auto" limitToList="true" style="width:500px; height:32px"
                   data-options="required:false, editable:false,multiple:false, mode:'remote',
                  valueField:'id', textField:'title', url:'${basePath}/admin/venue/srchList?state=6&delstate=0',
                  &lt;%&ndash;onLoadSuccess: function(){$(this).combobox('setValue','${info.venueid}')},&ndash;%&gt;
                  prompt:'请选择所属场馆',onChange : ActTool.changeVen,
                  onSelect:function(rec){
                      &lt;%&ndash;$('[comboname=roomid]').combobox('clear');&ndash;%&gt;
                      $('[comboname=roomid]').combobox('reload', '${basePath}/admin/venue/room/getRoomList4venid?venid=' + rec.id);} "/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">活动室：</div>
        <div class="whgff-row-input">
            <input class="EEE-combobox" name="roomid" id="roomid" value="${info.roomid}" style="width:500px; height:32px"
                   data-options="editable:false,multiple:false,valueField:'id', textField:'title', mode:'remote',
                   &lt;%&ndash;onLoadSuccess: function(){$(this).combobox('setValue','${info.roomid}')},&ndash;%&gt;
                   prompt:'请选择所属活动室',onSelect:ActTool.roomSelected
                   ,loadFilter: function(data){
                        var value = $(this).combobox('getValue');
                        var setEmpty = true;
                        if (data){
                            for(var k in data){
                                var row = data[k];
                                if (row.id && row.id == value){
                                    setEmpty = false;
                                    break;
                                }
                            }
                        }
                        if (setEmpty){
                            $(this).combobox('clear');
                        }
                        return data;
                   }"/>
        </div>
    </div>--%>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>场馆：</div>
        <div class="whgff-row-input">
            <input class="EEE-combobox" style="width:245px; height:32px" name="venueid" id="venueid"
                   data-options="editable:true,limitToList:true,required:false
                   ,onSelect : ActTool.changeVen" />
            <input class="EEE-combobox" style="width:245px; height:32px" name="roomid" id="roomid"
                   data-options="editable:true,limitToList:true,required:false
                   ,onSelect:ActTool.roomSelected
                   ,loadFilter: function(data){
                        var value = $(this).combobox('getValue');
                        var setEmpty = true;
                        if (data){
                            for(var k in data){
                                var row = data[k];
                                if (row.id && row.id == value){
                                    setEmpty = false;
                                    break;
                                }
                            }
                        }
                        if (setEmpty){
                            $(this).combobox('clear');
                        }
                        return data;
                   }" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">主办方：</div>
        <div class="whgff-row-input">
            <input class="EEE-textbox" name="host" value="${info.host }"
                   style="width: 500px; height: 32px" data-options="required:false,validType:['length[0,20]']" />
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">承办单位：</div>
        <div class="whgff-row-input">
            <input class="EEE-textbox" name="organizer" value="${info.organizer }"
                   style="width: 500px; height: 32px" data-options="required:false,validType:['length[0,20]']" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">协办单位：</div>
        <div class="whgff-row-input">
            <input class="EEE-textbox" name="coorganizer" value="${info.coorganizer }"
                   style="width: 500px; height: 32px" data-options="required:false,validType:['length[0,20]']" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">演出单位：</div>
        <div class="whgff-row-input">
            <input class="EEE-textbox" name="performed" value="${info.performed }"
                   style="width: 500px; height: 32px" data-options="required:false,validType:['length[0,20]']" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">主讲人：</div>
        <div class="whgff-row-input">
            <input class="EEE-textbox" name="speaker" value="${info.speaker }"
                   style="width: 500px; height: 32px" data-options="required:false,validType:['length[0,20]']" />
        </div>
    </div>

    <%--<div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>所属单位：</div>
        <div class="whgff-row-input">
            <input class="EEE-combobox" style="width: 300px; height:32px" name="cultid" value="${info.cultid}"
                   data-options="required:true, prompt:'请选择文化馆', value:WhgComm.getMgrCults()[0].id, valueField:'id', textField:'text', data:WhgComm.getMgrCults()"/>
        </div>
    </div>--%>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>活动分类：</div>
        <div class="whgff-row-input">
        <%--<div class="whgff-row-input" data-check="true" target="etype" err-msg="请至少选择一个分类">--%>
            <%--<div class="checkbox checkbox-primary whg-js-data"
                 name="etype"
                 value="${info.etype}"
                 js-data="WhgComm.getActivityType"
                 data-options="required:true,onLoadSuccess:function(){$(this).att('readonly','readonly')}"></div>--%>
            <div class="checkbox checkbox-primary" id="XXXetype" name="etype"></div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>艺术分类：</div>
        <div class="whgff-row-input">
        <%--<div class="whgff-row-input" data-check="true" target="arttype" err-msg="请至少选择一个艺术分类">--%>
            <%--<div class="checkbox checkbox-primary whg-js-data" name="arttype" value="${info.arttype}" data-options="required:true" js-data="WhgComm.getArtType"></div>--%>
            <div class="checkbox checkbox-primary" id="XXXarttype" name="arttype"></div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>标签：</div>
        <div class="whgff-row-input">
        <%--<div class="whgff-row-input"  data-check="true" target="etag" err-msg="请至少选择一个标签">--%>
            <%--<div class="checkbox checkbox-primary whg-js-data"name="etag" value="${info.etag}" js-data="WhgComm.getActivityTag"></div>--%>
            <div class="checkbox checkbox-primary" id="etag" name="etag"></div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">关键字：</div>
        <div class="whgff-row-input">
        <%--<div class="whgff-row-input" data-check="true" target="ekey" err-msg="请至少选择一个关键字">--%>
            <%--<div class="checkbox checkbox-primary whg-js-data" name="ekey" value="${info.ekey}" js-data="WhgComm.getActivityKey"></div>--%>
            <input class="EEE-combobox" id="ekey" style="width:500px; height:32px" validType="notQuotes"
                   data-options="multiple:true, editable:true, prompt:'请填写关键字'
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
        <div class="whgff-row-label"><i>*</i>活动日期：</div>
        <div class="whgff-row-input">
            <input type="text" class="EEE-datebox" style="width: 240px; height: 32px;" id="XXXstarttime" name="starttime" required="required" data-options=" prompt:'请选择',validType:'hdEndDate[\'gat_timeend\']'" value="<fmt:formatDate value='${info.starttime}' pattern="yyyy-MM-dd"></fmt:formatDate>"   />
           <%-- ~<input type="text" class="EEE-datebox" style="width: 240px; height: 32px;" id="XXXendtime" name="endtime"  required="required" value="<fmt:formatDate value='${info.endtime}' pattern="yyyy-MM-dd"></fmt:formatDate>"  data-options="editable:false,prompt:'请选择',validType:'hdEndDate[\'starttime\']'" />--%>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">
            <label id="timeStarSpan" style="color: red">*</label>场次模板：
        </div>
        <div class="whgff-row-input">
            <div id="free-time-set">
                <div id="put-ticket-list" style="width: 800px;">
                    <div class="ticket-item activityTimeLabel1">
                        <input class="EEE-timespinner" style="width: 100px; height: 32px;" value="08:00" id="playstrtime" name="playstrtime" data-options="required:true"/>-
                        <input class="EEE-timespinner" style="width: 100px; height: 32px;" value="10:00" id="playendtime" name="playendtime" data-options="required:true"/>
                        <label style="color: red;margin-left: 10px;display: none;">时间填写不正确</label>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>地址：</div>
        <div class="whgff-row-input">
            <input class="EEE-textbox" name="address" id="XXXaddress" value="${info.address}" style="width: 500px; height: 32px" data-options="required:true,validType:['length[0,60]']">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>坐标：</div>
        <div class="whgff-row-input">
            <input class="EEE-numberbox" name="actlon" id="actlon" value="${info.actlon}" style="width: 100px; height: 32px" data-options="required:false, precision:6,readonly:true,prompt:'X轴'">
            <input class="EEE-numberbox" name="actlat" id="actlat" value="${info.actlat}"  style="width: 100px; height: 32px" data-options="required:false, precision:6,readonly:true,prompt:'Y轴'">
            <a class="EEE-linkbutton whg-maps" map-addr="#address" vm-x="actlon" vm-y="actlat" id="XXXgetXYPointBtn" text="选择坐标"></a>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>联系电话：</div>
        <div class="whgff-row-input"><input class="EEE-textbox" name="telphone" value="${info.telphone}" style="width:300px; height:32px" data-options="prompt:'请输入联系人手机', required:true, validType:'isPhone'"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">
            <label style="color: red">*</label>是否消耗积分：
        </div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="integral" id="integral" value="${info.integral }"  onclick="ActTool.isIntegral()"
                 js-data='[{"id":"1","text":"默认"},{"id":"2","text":"积分"}]'>
                <input  type="hidden" value="1">
            </div>
            <span id="integralCount" style="display:none">该活动需要消耗积分<input class="EEE-numberspinner" name="integralnum" value="${info.integralnum }" id="integralnum" style="width: 50px; height: 25px" data-options="required:false,min:0,max:999">分 </span>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">
            <label style="color: red">*</label>是否收费：
        </div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="hasfees" id="hasfees"   value="${info.hasfees }"
                 js-data='[{"id":"0","text":"否"},{"id":"1","text":"是"}]'>
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>在线售票：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="sellticket" id="sellticket" value="${info.sellticket }"
                 js-data='[{"id":"2","text":"自由入座"},{"id":"3","text":"在线选座"}]'>
                <input id="sellticketSelection" type="hidden" value="2">
            </div>
            <span id="maxBuySeatCount" style="display:none">每人每场最多购票<input class="EEE-textbox" name="seats"  id="seats" data-options="validType:'checkSeats'" style="width: 50px; height: 25px" >张 </span>
            <span id="maxSoldSeatCount" style="display:none">每场次最多售票<input class="EEE-textbox" name="ticketnum"   id="ticketnum"  style="width: 50px; height: 25px"  data-options="validType:'gatNumZPS[\'gat_numsum\']'" >张 </span>
        </div>
    </div>
    <div class="whgff-row" style="display: none" id="onLineSeat">
        <div class="whgff-row-map test-seatmaps">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">
            <label style="color: red">*</label>活动描述：
        </div>
        <div class="whgff-row-input">
            <div id="remark" name="_remark"  type="text/plain" style="width: 800px; height: 600px;"></div>
            <textarea id="value_remark" style="display: none;">${info.remark}</textarea>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">
            上传附件：
        </div>
        <div class="whgff-row-input">
            <input  id="act_filepath1" name="filepath" value="${info.filepath }" data-options="required:true" style="width:600px;height:32px;" readonly="readonly">
            <div class="whgff-row-input-file" id="filepath">
                <i><a href="javascript:void(0)" class="EEE-linkbutton" iconCls="icon-save" id="fileUploadBtn1">选择文件</a></i>
                <i>附件格式为doc,docx,xls,zip,xlsx，建议图文件大小为10MB以内</i>
            </div>
        </div>
    </div>
</div>
<%-- 活动众筹内容块 end --%>


<%-- 培训众筹内容块 begin --%>
<div id="module-tar" style="display: none;">
    <%--<div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>所属单位：</div>
        <div class="whgff-row-input">
            <select class="EEE-combobox" name="cultid" style="width:500px; height:32px"
                    data-options="editable:false, required:true, valueField:'id', textField:'text', value:'${info.cultid}', data:WhgComm.getMgrCults(),prompt:'请选择所属单位', onSelect: TarTool.selectCult"></select>
        </div>
    </div>--%>

    <div class="whgff-row">
        <div class="whgff-row-label">所属部门：</div>
        <div class="whgff-row-input">
            <%--<select class="EEE-combobox select-deptid" name="deptid" style="width:500px; height:32px" prompt="请选择部门"
                    data-options="editable:false, required:false, valueField:'id', textField:'text',value:'${info.deptid}'"></select>--%>
            <input class="EEE-combobox" style="width:500px; height:32px" name="deptid" id="deptid" data-options="required:true" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>所在场馆：</div>
        <div class="whgff-row-input">
            <%--<input class="EEE-combobox" id="venue" name="venue" style="width:500px; height:32px" value="${info.venue}"
                   data-options="editable:true, multiple:false, limitToList:true, mode:'remote', valueField:'id', textField:'title', url:'${basePath}/admin/venue/srchList?state=6&delstate=0',
                    prompt:'请选择所属场馆',
                     onChange:function(venue){
                        $('#venroom').combobox({
                            valueField:'id', //值字段
                            textField:'title', //显示的字段
                            url:'${basePath}/admin/venue/room/srchList?state=6&delstate=0&venid='+venue,
                            panelHeight:'auto',
                            editable:false,
                            value:''
                        });
                    }
                    ">--%>
            <input class="EEE-combobox" style="width:245px; height:32px" name="venue" id="venue" data-options="editable:true,limitToList:true,required:false" />
            <input class="EEE-combobox" style="width:245px; height:32px" name="venroom" id="venroom" data-options="editable:true,limitToList:true,required:false" />
        </div>
    </div>

   <%-- <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>所在活动室：</div>
        <div class="whgff-row-input">
            <input class="EEE-combobox" id="venroom" name="venroom" value="${info.venroom}" style="width:500px; height:32px"
                   data-options="editable:true, multiple:false,limitToList:true,valueField:'id', textField:'title', url:'${basePath}/admin/venue/room/srchList?state=6&delstate=0&venid=${info.venue}',
                        prompt:'请选择活动室',
                        panelHeight:'auto',
                        editable:false
                    ">
        </div>
    </div>--%>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>文化品牌：</div>
        <div class="whgff-row-input">
            <%--<select class="EEE-combobox" name="ebrand" style="width:500px; height:32px"
                    data-options="editable:false,multiple:true, valueField:'id', textField:'text', value:'${info.ebrand}', data:WhgComm.getBrand(),prompt:'请选择文化品牌'">
            </select>--%>
            <input class="EEE-combobox" style="width:245px; height:32px" name="ebrand" id="ebrand" data-options="required:false" />
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>联系电话：</div>
        <div class="whgff-row-input">
            <input class="EEE-textbox" name="phone" value="${info.phone}" style="width:500px; height:32px" data-options="validType:'isPhone[\'traphone\']',prompt:'请填写联系电话'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>联系人：</div>
        <div class="whgff-row-input">
            <input class="EEE-textbox" name="contacts" value="${info.contacts}" style="width:500px; height:32px" data-options="validType:['length[1,20]'],prompt:'请填写联系人'">
        </div>
    </div>

    <%--<div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>区域：</div>
        <div class="whgff-row-input">
            <input class="EEE-combobox" style="width:150px; height:32px" id="province" name="province" data-options="required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name', data:__PROVINCE, onChange:changeProvince"/>
            <input class="EEE-combobox" style="width:150px; height:32px" id="__CITY_ELE" name="city" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name', data:[], onChange:changeCity"/>
            <input class="EEE-combobox" style="width:179px; height:32px" id="__AREA_ELE" name="area" data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
        </div>
    </div>--%>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>艺术分类：</div>
        <div class="whgff-row-input">
            <%--<div class="checkbox checkbox-primary whg-js-data" value="${info.arttype}" name="arttype" js-data="WhgComm.getArtType"></div>--%>
            <div class="checkbox checkbox-primary" id="arttype" name="arttype"></div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>标签：</div>
        <div class="whgff-row-input">
            <%--<div class="checkbox checkbox-primary whg-js-data" value="${info.etag}" name="etag" js-data="WhgComm.getTrainTag"></div>--%>
            <div class="checkbox checkbox-primary" id="etag" name="etag"></div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>关键字：</div>
        <div class="whgff-row-input">
            <%--<input class="EEE-combobox" id="XXXekey" style="width:500px; height:32px" validType="notQuotes" data-options="multiple:true,editable:true,valueField:'text',textField:'text', data:WhgComm.getTrainKey()"/>--%>
            <input class="EEE-combobox" id="XXXekey" style="width:500px; height:32px" validType="notQuotes"
                   data-options="multiple:true, editable:true, prompt:'请填写关键字'
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
        <div class="whgff-row-label"><label style="color: red">*</label>地址：</div>
        <div class="whgff-row-input">
            <input class="EEE-textbox" id="address" name="address" value="${info.address}" style="width:500px; height:32px" data-options="required:true,validType:['length[1,200]'],prompt:'请填写地址'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>坐标：</div>
        <div class="whgff-row-input">
            <input class="EEE-numberbox" id="longitude" name="longitude" value="${info.longitude}" style="width:100px; height:32px" data-options="required:false,precision:6,readonly:true,prompt:'X轴'">
            <input class="EEE-numberbox" id="latitude" name="latitude" value="${info.latitude}" style="width:100px; height:32px" data-options="required:false,precision:6,readonly:true,prompt:'y轴'">
            <a class="EEE-linkbutton whg-maps" map-addr="#address" vm-x="#longitude" vm-y="#latitude" id="getXYPointBtn" text="选择坐标"></a>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>培训老师：</div>
        <div class="whgff-row-input">
            <input class="EEE-combobox" id="teachername" name="teachername" value="${info.teachername}" style="width:500px; height:32px"
                   data-options="multiple:true,editable:true,valueField:'name',textField:'name'
                    ,onChange: function (val, oldval) {
                        if (val.length>1 && val[0]==''){
                            val.shift();
                            $(this).combobox('setValues', val);
                        }
                    }"/>
            <span>（如需手动输入，请用英文逗号隔开！）</span>
        </div>
    </div>
    <%--
        <div class="whgff-row">
            <div class="whgff-row-label"><label style="color: red">*</label>报名人数上限：</div>
            <div class="whgff-row-input">
                <input class="EEE-numberspinner" name="maxnumber" value="${info.maxnumber}" style="width:500px; height:32px" data-options="min:0,required:true,prompt:'请输入人数'">
            </div>
        </div>
        <div class="whgff-row">
            <div class="whgff-row-label"><label style="color: red">*</label>基础报名人数：</div>
            <div class="whgff-row-input">
                <input class="EEE-numberspinner" name="basicenrollnumber" value="${info.basicenrollnumber}" style="width:500px; height:32px" data-options="min:0,required:true,prompt:'请输入人数'">
            </div>
        </div>
        <div class="whgff-row">
            <div class="whgff-row-label"><label style="color: red">*</label>最大报名人数：</div>
            <div class="whgff-row-input">
                <div class="radio radio-primary whg-js-data" value="${info.isshowmaxnumber}" name="isshowmaxnumber" js-data='[{"id":"0","text":"不显示"},{"id":"1","text":"显示"}]'></div>
            </div>
        </div>--%>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>培训分类：</div>
        <div class="whgff-row-input">
            <%--<div class="radio radio-primary whg-js-data" value="${info.etype}" name="etype" js-data="WhgComm.getTrainType"></div>--%>
            <div class="radio radio-primary" id="etype" name="etype"></div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>是否收费：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" value="${info.ismoney}" name="ismoney" js-data='[{"id":"0","text":"否"},{"id":"1","text":"是"}]'></div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>适合年龄：</div>
        <div class="whgff-row-input">
            <input class="EEE-numberspinner" id="age1" name="age1" value="${age1}" style="width:80px;" data-options="min:1,max:100, editable:true">&nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;
            <input class="EEE-numberspinner" id="age2" name="age2" value="${age2}" style="width:80px;" data-options="min:1,max:100, editable:true, validType:'_validage[\'age1\']'">岁
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>实名制：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" value="${info.isrealname}" name="isrealname" js-data='[{"id":"0","text":"否"},{"id":"1","text":"是"}]'></div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>培训周期：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" value="${info.isterm}"  name="isterm" js-data='[{"id":"0","text":"学年制"},{"id":"1","text":"学期制"},{"id":"2","text":"短期制"}]'></div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>录取规则：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" value="${info.isbasicclass}"  name="isbasicclass" js-data='[{"id":"0","text":"需要面试"},{"id":"1","text":"需人工录取"},{"id":"2","text":"即报即得"}]'></div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>培训场次：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" value="${info.ismultisite}"  name="ismultisite" js-data='[{"id":"0","text":"单场"},{"id":"1","text":"多场"},{"id":"2","text":"固定场"}]'></div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>培训时长：</div>
        <div class="whgff-row-input">
            <input class="EEE-textbox" name="duration" value="${info.duration}" style="width:500px; height:32px" data-options="validType:['length[1,100]'],prompt:'请填写培训时长'">
        </div>
    </div>

    <div class="whgff-row train">
        <div class="whgff-row-label "><label style="color: red">*</label>培训时间周期：</div>
        <div class="whgff-row-input">
            <input class="EEE-datebox starttime" id="starttime" name="starttime" value="<fmt:formatDate value="${info.starttime}" pattern="yyyy-MM-dd"/>" style="width:200px; height:32px" data-options="validType:'traEndTime[0,\'gat_timeend\']'"/>至
            <input class="EEE-datebox endtime" id="endtime" name="endtime" value="<fmt:formatDate value="${info.endtime}" pattern="yyyy-MM-dd"/>" style="width:200px; height:32px" data-options="validType:'traEndTime[1,\'starttime\']'"/>
        </div>
    </div>

<c:choose>
    <c:when test="${pageType eq 'add'}">
        <!--添加时单场的培训上课时间-->
        <div class="whgff-row single" >
            <div class="whgff-row-label"><label style="color: red">*</label>培训上课时间：</div>
            <div class="whgff-row-input">
                <input class="EEE-datetimebox sin_starttime" id="sin_starttime" name="sin_starttime" style="width:200px; height:32px" data-options="validType:'sinTime[\'gat_timeend\']'"/>至
                <input class="EEE-datetimebox sin_endtime" id="sin_endtime" name="sin_endtime" style="width:200px; height:32px" data-options="validType:'sinTime[\'sin_starttime\']'"/>
            </div>
        </div>
        <!--添加时多场的培训上课时间-->
        <c:if test="${empty course}">
            <div class="whgff-row multi" name="multiContent">
                <div class="whgff-row-label"><label style="color: red">*</label>培训上课时间：</div>
                <div class="whgff-row-input">
                    <input class="EEE-datetimebox _starttime" name="_starttime" id="_starttime_1"  style="width:200px; height:32px" data-options="required:true,validType:'traTime[0, \'gat_timeend\']'"/>至
                    <input class="EEE-datetimebox _endtime" name="_endtime" id="_starttime_2" style="width:200px; height:32px" data-options="required:true,validType:'traTime[1,\'_starttime_1\']'"/>
                    <a href="javascript:void(0)" class="timeico add">添加</a>
                </div>
            </div>
        </c:if>
    </c:when>

    <c:otherwise>
        <!--编辑时单场的培训上课时间-->
        <c:if test="${not empty course }">
            <c:forEach items="${course}" var="item" varStatus="s" end="0">
                <div class="whgff-row single_edit" >
                    <div class="whgff-row-label"><label style="color: red">*</label>培训上课时间：</div>
                    <div class="whgff-row-input">
                        <input class="EEE-datetimebox sin_starttime_edit" id="sin_starttime_edit" name="sin_starttime" value='<fmt:formatDate value="${item.starttime}" pattern="yyyy-MM-dd HH:mm:ss"/>' style="width:200px; height:32px" data-options="validType:'sinedit_Time[\'gat_timeend\']'"/>至
                        <input class="EEE-datetimebox sin_endtime_edit" id="sin_endtime_edit" name="sin_endtime" value='<fmt:formatDate value="${item.endtime}" pattern="yyyy-MM-dd HH:mm:ss"/>' style="width:200px; height:32px" data-options="validType:'sinedit_Time[\'sin_starttime_edit\']'"/>
                    </div>
                </div>
            </c:forEach>
        </c:if>
        <!--编辑时多场的培训上课时间-->
        <c:if test="${not empty course }">
            <c:forEach items="${course}" var="item" varStatus="s" >
                <div class="whgff-row multi_edit" name="multiContent">
                    <div class="whgff-row-label"><label style="color: red">*</label>培训上课时间：</div>
                    <div class="whgff-row-input">
                        <input class="EEE-datetimebox starttime_edit" id="_starttime_${s.count}" style="width:200px; height:32px" name="_starttime" value='<fmt:formatDate value="${info.ismultisite == 1?item.starttime:''}" pattern="yyyy-MM-dd HH:mm:ss"/>' data-options="validType:'edittraTime[0,\'_starttime_${s.count}\']'"/>至
                        <input class="EEE-datetimebox endtime_edit" id="_endtime_${s.count}" style="width:200px; height:32px" name="_endtime" value='<fmt:formatDate value="${info.ismultisite == 1?item.endtime:''}" pattern="yyyy-MM-dd HH:mm:ss"/>' data-options="validType:'edittraTime[1,\'_starttime_${s.count}\']'"/>
                        <c:if test="${info.state != 4}">
                            <a href="javascript:void(0)" class="timeico add">添加</a>
                        </c:if>
                        <c:if test="${s.count >1}">
                            <a href="javascript:void(0)" class="timeico del">删除</a>
                        </c:if>
                    </div>
                </div>
            </c:forEach>
        </c:if>
    </c:otherwise>
</c:choose>

    <div class="whgff-row fixed">
        <div class="whgff-row-label"><label style="color: red">*</label>固定周几：</div>
        <div class="whgff-row-input">
            <div class="checkbox checkbox-primary whg-js-data"  name="fixedweek" value="${info.fixedweek}" js-data='[{"id":"1","text":"周一"},{"id":"2","text":"周二"},{"id":"3","text":"周三"},{"id":"4","text":"周四"},{"id":"5","text":"周五"},{"id":"6","text":"周六"},{"id":"7","text":"周日"}]'> </div>
        </div>
    </div>

    <div class="whgff-row fixed">
        <div class="whgff-row-label"><label style="color: red">*</label>固定上课时间：</div>
        <div class="whgff-row-input">
            <input class="EEE-timespinner _coursestarttime" name="fixedstarttime" value="<fmt:formatDate value="${info.fixedstarttime}" pattern="HH:mm"/>"  style="width:200px; height:32px"/>至
            <input class="EEE-timespinner _courseendtime" name="fixedendtime" value="<fmt:formatDate value="${info.fixedendtime}" pattern="HH:mm"/>" style="width:200px; height:32px" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>培训详情：</div>
        <div class="whgff-row-input">
            <div id="detail" name="_detail" type="text/plain" style="width:600px; height:300px;"></div>
            <textarea id="value_detail" style="display: none;">${info.coursedesc}</textarea>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>培训大纲：</div>
        <div class="whgff-row-input">
            <script id="catalog" name="_catalog" type="text/plain" style="width:600px; height:300px;"></script>
            <textarea id="value_catalog" style="display: none;">${info.outline}</textarea>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>老师介绍：</div>
        <div class="whgff-row-input">
            <div id="teacherIntro" name="_teacherIntro" type="text/plain" style="width:600px; height:300px;"></div>
            <textarea id="value_teacherIntro" style="display: none;">${info.teacherdesc}</textarea>
        </div>
    </div>
</div>
<%-- 培训众筹内容块 end --%>

<script>

    $.extend($.fn.validatebox.defaults.rules, {
        gatNummin: {
            validator: function(value, param){
                if (!value){ return true}
                var sum = $("#"+param[0]).val();

                try {
                    sum = Number(sum);
                    value = Number(value);
                    return sum >= value;
                } catch (e) {
                    return true;
                }
            },
            message: "达成份数不能大于总份数"
        },
       gatNumZPS: {
            validator: function(value, param){
                if (!value){ return true}
                var sellticket=$('input:radio[name="sellticket"]:checked').val();
                if(sellticket=="2"){
                    var sum=0;
                    var val=0;
                    var paramValue=$("#"+param[0]).val();
                    try {

                        if(paramValue&&paramValue!=""){
                            sum=Number(paramValue);
                        }
                        if(value&&value!=""){
                            val=Number(value);
                        }
                        if(sum<1){
                            return false;
                        }
                    } catch (e) {
                        return false;
                    }
                    return sum <= val;
                }else{
                    return true;
                }
            },
            message: "每场总票数不能小于总份数,不能为空且要大于0"
        }, checkSeats: {
            validator: function(value){
                if (!value){ return false}
                    var sum=0;
                    try {
                        if(value&&value!=""){
                            sum=Number(value);
                        }
                    } catch (e) {
                        return false;
                    }
                    return sum >0;
            },
            message: "每人预定数不能为空且要大于0"
        },
        bmEndTime: {
            validator: function(value, param){
                var sdVal = $('#'+param[0]).datetimebox('getValue');
                if (!value || !sdVal){
                    return true;
                }
                var d1 = WhgComm.parseDateTime(sdVal);//$.fn.datebox.defaults.parser(sdVal);
                var d2 = WhgComm.parseDateTime(value);//$.fn.datebox.defaults.parser(value);
                return d2.getTime()>d1.getTime();
            },
            message: '结束时间必须大于开始时间.'
        },
       hdEndDate: {
            validator: function(value, param){
                var sdVal = $('#'+param[0]).datebox('getValue');
                if (!value || !sdVal){
                    return true;
                }
                var d1 = WhgComm.parseDateTime(sdVal);//$.fn.datebox.defaults.parser(sdVal);
                var d2 = WhgComm.parseDate(value);//$.fn.datebox.defaults.parser(value);
                return d2.getTime()>=d1.getTime();
            },
            message: '活动日期必须大于众筹结束日期.'
        },
        traEndTime: {
            validator: function(value, param){
                var _type = param[0];
                var _refid = param[1];
                var mutle = $('input[name="ismultisite"]:checked').val();

                var pageType = "${pageType}";
                if (pageType == "add"){
                    if(_type == 0){
                        var sdVal = $('#'+param[1]).datetimebox('getValue');
                        var d1 = WhgComm.parseDateTime(sdVal);
                        var d2 = WhgComm.parseDateTime(value+" 23:59:59");
                        return d2.getTime() > d1.getTime();
                    }else if(_type == 1){
                        var sdVal = $('#'+param[1]).datebox('getValue');
                        var d1 = WhgComm.parseDateTime(sdVal);
                        var d2 = WhgComm.parseDateTime(value+" 23:59:59");
                        return d2.getTime() > d1.getTime();
                    }
                }else{
                    if(parseInt(mutle) == 0){
                        return true;
                    }else{
                        if(_type == 0){
                            var sdVal = $('#'+param[1]).datetimebox('getValue');
                            var d1 = WhgComm.parseDateTime(sdVal);
                            var d2 = WhgComm.parseDateTime(value+" 23:59:59");
                            return d2.getTime() > d1.getTime();
                        }else if(_type == 1){
                            var sdVal = $('#'+param[1]).datebox('getValue');
                            var d1 = WhgComm.parseDateTime(sdVal);
                            var d2 = WhgComm.parseDateTime(value+" 23:59:59");
                            return d2.getTime() > d1.getTime();
                        }
                    }
                }
            },
            message: '培训时间必须晚于众筹时间.'
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
                var sdVal = $('#'+param[0]).datetimebox('getValue');
                var d1 = WhgComm.parseDateTime(sdVal);
                var d2 = WhgComm.parseDateTime(value);
                return d2.getTime()>=d1.getTime();
            },
            message: '培训时间必须晚于众筹时间.'
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
            message: '培训时间必须晚于众筹时间.'
        },

        edittraTime: {
            validator: function(value, param){
                var mutle = $('input[name="ismultisite"]:checked').val();
                var _valid = false;
                var _type = param[0];//0-starttime 1-endtime
                var _refid = param[1];//0-starttime 1-endtime
                var _stime = $('#starttime').datebox('getValue');
                var _etime = $('#endtime').datebox('getValue');
                var _ptime = false;
                if(parseInt(mutle) != 1){
                    _valid = true;
                    return _valid;
                }
                if(_type == 0){//0-starttime
                    var pDiv = $('#'+_refid).parents('div[name="multiContent"]');
                    var preDiv = pDiv.prev();
                    if(preDiv.is('[name="multiContent"]')){
                        var preEndTimeEl = preDiv.find('.endtime_edit');
                        _ptime = preEndTimeEl.datetimebox('getValue');
                    }
                }else if(_type == 1){//1-endtime
                    _ptime = $('#'+_refid).datetimebox('getValue');
                }

                var t_value = WhgComm.parseDateTime(value);
                var t__stime = WhgComm.parseDate(_stime);
                var t__etime = WhgComm.parseDateTime(_etime+" 23:59:59");

                if( t_value.getTime() > t__stime.getTime() && t_value.getTime() < t__etime.getTime() ){
                    if(_ptime){
                        var t_ptime = WhgComm.parseDateTime(_ptime);
                        if(t_value.getTime() > t_ptime.getTime()){
                            _valid = true;
                        }
                    }else{
                        _valid = true;
                    }
                }
                return _valid;
            },
            message: '培训课时必须在培训周期之内，结束时间大于开始时间，各培训课时段不能重合。'
        },

        traTime: {
            validator: function(value, param){
                var _valid = false;
                var _type = param[0];//0-starttime 1-endtime
                var _refid = param[1];//0-starttime 1-endtime
                var _stime = $('#starttime').datebox('getValue');
                var _etime = $('#endtime').datebox('getValue');
                var _ptime = false;

                if(_type == 0){//0-starttime
                    var pDiv = $('#'+_refid).parents('div[name="multiContent"]');
                    var preDiv = pDiv.prev();
                    if(preDiv.is('[name="multiContent"]')){
                        var preEndTimeEl = preDiv.find('._endtime');
                        _ptime = preEndTimeEl.datetimebox('getValue');
                    }
                }else if(_type == 1){//1-endtime
                    _ptime = $('#'+_refid).datetimebox('getValue');
                }

                //value in _stime,_etime
                //value > _ptime
                var t_value = WhgComm.parseDateTime(value);
                var t__stime = WhgComm.parseDate(_stime);
                var t__etime = WhgComm.parseDateTime(_etime+" 23:59:59");

                if( t_value.getTime() > t__stime.getTime() && t_value.getTime() < t__etime.getTime() ){
                    if(_ptime){
                        var t_ptime = WhgComm.parseDateTime(_ptime);
                        if(t_value.getTime() > t_ptime.getTime()){
                            _valid = true;
                        }
                    }else{
                        _valid = true;
                    }
                }
                return _valid;
            },
            message: '培训课时必须在培训周期之内，结束时间大于开始时间，各培训课时段不能重合。'
        }
    });

    $(function(){
        MainTool.init();
    });









    
    var MainTool = {
        //通用定义
        pageType: '${pageType}',    //页面类型
        basePath: '${basePath}',    //根URL
        baseAction: '${basePath}/admin/gather/', //表单提交根URL
        //入口
        init: function(){
            var me = this;

            me.frm = $("#whgff");   //表单
            me.buttonDiv = $("div.whgff-but");  //表单button块
            me.module_target = $(".whgff-row-split");  //表单众筹主体固定信息结束位，用于清除和装入关联信息定点

            //移除页面子模型，减少子块内容冲突
            me.html_module_act = $("#module-act").html();
            me.html_module_tar = $("#module-tar").html();
            me.html_module_other = $("#module-other").html();
            $("#module-act, #module-tar, #module-other").remove();

            //众筹类型点选事件
            $("div[name='gat_etype']").on("click", "input[name='gat_etype']", function(){
                var etype = $(this).val();
                me.setGatherType(etype);
            });
            $("div[name='gat_etype']").find("input[name='gat_etype']:checked").click();

            //接入增改查
            if (me[me.pageType] && $.type(me[me.pageType])==='function'){
                me[me.pageType].call(me);
            }

            me.initBuder();
        },

        initVal: function(){
            var ekey = "${info.ekey}";
            if (ekey && ekey!=''){
                $("#ekey").combobox('setValue', "${info.ekey}");
            }

            //var province = '${info.province}';
            //province = (!province || province=='') ? '${info.gat_province}' : province;
            var province = '${info.gat_province}';
            if (!province || province==''){
                province = WhgComm.getProvince()?WhgComm.getProvince():'广东省';
            }
            $('#province').combobox('setValue', province);

            //var city = '${info.city}';
            //city = (!city || city=='') ? '${info.gat_city}' : city;
            var city = '${info.gat_city}';
            if (!city || city==''){
                city = WhgComm.getCity()?WhgComm.getCity():"";
            }
            $("#__CITY_ELE").combobox('setValue', city);
            //var area = '${info.area}';
            //area = (!area || area=='') ? '${info.gat_area}' : area;
            var area = '${info.gat_area}';
            if (area && area!=''){
                $("#__AREA_ELE").combobox("setValue", area);
            }
            //var areaid = '${info.areaid}';
            //if (areaid && areaid!=''){
            //    $("#__AREA_ELE").combobox("setValue", areaid);
            //}

            if (this.pageType == 'add') {
                var gat_explains = $("#gat_explains").textbox('getValue');
                if (!gat_explains || gat_explains == '') {
                    $("#gat_explains").textbox('setValue', '请支持者密切关注众筹进度，如众筹活动一旦成功，支持者即可获得参与活动的资格（部分培训需要面试通过后才能参与），并请按照计划参与众筹活动。');
                }

                var gat_notice = $("#gat_notice").textbox('getValue');
                if (!gat_notice || gat_notice == '') {
                    $("#gat_notice").textbox('setValue', '1、 众筹不是商品交易。支持者根据自己的需求选择、支持众筹项目，并获得发起人承诺的回报。\r\
2、因众筹人数不足、市场风险、法律风险等各种因素，众筹可能失败。众筹失败时，发起人将不会提供相应的文化服务。\r\
3、由于紧急事件、突发气候变化等不可预测因素造成众筹活动无法开展，发起人有权终止众筹活动，且不承担任何法律和经济责任。');
                }
            }
        },

        //主众筹内容块初始组件
        initBuder: function(){
            var me = this;
            me.gat_imgurl = me.initImg("gat_imgurl_1","gat_imgurl_2", "gat_imgurl_3", 600, 450);
            me.gat_descriptions = me.initUE("gat_descriptions", "#value_descriptions");
        },

        cultOnChange: function(val, oval){
            $("#gat_brandid").combobox({
                url : '${basePath}/admin/brand/getList',
                queryParams: {cultid: val},
                validateOnCreate: true
            });
            var me = this;
            if (me.refTool.cultOnChange && $.type(me.refTool.cultOnChange)==='function'){
                me.refTool.cultOnChange(val, oval);
            }
        },

        //按众筹类型处理设置，会调用对应组件的 initBuder 方法
        setGatherType: function (etype) {
            var me = this;
            //不同类型的关联tool 与 模型
            var modehtml = null;
            switch (etype){
                case "4": me.refTool = ActTool; modehtml= me.html_module_act;
                    break;
                case "5": me.refTool = TarTool; modehtml= me.html_module_tar;
                    break;
                default: me.refTool = OtherTool; modehtml= me.html_module_other;
            }
            //类型值没变时不处理
            if (me.curr_gatetype && me.curr_gatetype == etype) {
                return;
            }
            me.curr_gatetype = etype;

            //清理定点标记后的内容项
            me.module_target.nextAll(".whgff-row").remove();
            //装入对应类型 HTML
            me.module_target.after(modehtml);
            //清除ID冲突处理标记
            me.frm.find("[id^='XXX']").each(function(i){
                var _id = $(this).attr("id");
                _id = _id.replace(/^X{3,}/, '');
                $(this).attr("id", _id);
            });
            //装入easyui class 之后调用解析器
            me.frm.find("[class^='EEE']").each(function(i){
                var _class = $(this).attr("class");
                _class = _class.replace(/^E{3,}/, 'easyui');
                $(this).attr("class", _class);

                var parent = $(this).parents(".whgff-row");
                if (parent.find("[class^='EEE']").size()==0){
                    $.parser.parse(parent);
                }
            });
            //调用组件初始
            if (me.refTool.initBuder && $.type(me.refTool.initBuder)=='function'){
                me.refTool.initBuder();
            }

            //控制调用一次组件的 pageType 同名方法
            var pageType = me.pageType;
            var bibibi = etype+"_"+pageType;
            me.bibibiArray = me.bibibiArray || [];
            if (me.refTool[pageType] && $.type(me.refTool[pageType])=='function'){
                //if (me.bibibiArray.indexOf(bibibi) == -1){
                if ( $.inArray(bibibi, me.bibibiArray)  == -1){
                    me.bibibiArray.push(bibibi);
                    me.refTool[pageType].call(me.refTool);
                }
            }

            me.frm.form("disableValidation");
        },

        //处理查看
        show: function(){
            var me = this;
            me.frm.form("disableValidation");
            var undoBut = me.appendButton("返 回", 'icon-undo', function(){ me.closeForm(); });

            //组件设为只读
            $('.easyui-textbox').textbox('readonly');
            //$('.easyui-combobox').combobox('readonly');
            $('.easyui-combobox').each(function(){
                var opts = $(this).combobox("options");
                if (opts.url || opts.mode == 'remote'){
                    $(this).combobox({
                        onLoadSuccess: function(){
                            $(this).combobox('readonly');
                        },
                        onBeforeLoad: function(){
                            $(this).combobox('readonly', false);
                        }
                    })
                }else{
                    $(this).textbox('readonly');
                }
            });


            $('.easyui-datebox').datebox('readonly');
            //处理选项点击不生效
            //me.frm.find("input[type='checkbox']").on('click', function(){return false});
            me.frm.find("input[type='radio']").attr('disabled', true);
            $("div.radio").on("DOMNodeInserted", function(e){
                $(e.target).attr('disabled', true);
            });
            me.frm.on('click', "input[type='checkbox']", function(){return false});

            $("#gat_imgurl_1").hide();
        },

        //处理编辑
        edit: function(){
            var me = this;
            var okBut = me.appendButton("提 交", 'icon-ok');
            var undoBut = me.appendButton("返 回", 'icon-undo', function(){ me.closeForm(); });

            //编辑锁定众筹类型
            me.frm.find("input[type='radio'][name='gat_etype']").attr('disabled', true);
            var state = '${info.state}';
            if(state == 6 || state == 4) {
                $("#gat_nummin").numberbox({disabled: true});
                $('#gat_timestart').datetimebox({disabled: true});
                $('#gat_timeend').datetimebox({disabled: true});
            }

            var id = '${id}';
            me.submitForm(okBut, me.baseAction+me.pageType, id);
        },

        //处理添加
        add: function(){
            var me = this;
            var okBut = me.appendButton("提 交", 'icon-ok');
            //var noBut = me.appendButton("清 空", 'icon-no', function(){ me.clearForm(); });
            var undoBut = me.appendButton("返 回", 'icon-undo', function(){ me.closeForm(); });

            me.submitForm(okBut, me.baseAction+me.pageType);
        },

        //关闭返回
        closeForm: function () {
            window.parent.whgListTool.reload();
            WhgComm.editDialogClose();
        },

        //清除表单，会调用对应组件的 clearForm 方法
        clearForm: function () {
            var me = this;

            me.frm.form("disableValidation");
            me.frm.form('clear');
            me.gat_imgurl.clear();

            //第一个单选又点上
            me.frm.find("div.radio").find(':radio:eq(0)').click();
            me.gat_descriptions.setContent('');

            if (me.refTool.clearForm && $.type(me.refTool.clearForm)==='function'){
                me.refTool.clearForm();
            }
        },

        //定义表单提交
        submitForm: function (okBut, url, id) {
            var me = this;


            function oneSubmit(){
                okBut.off("click").one("click", function(){
                    $.messager.progress();
                    me.frm.submit();
                })
            }
            oneSubmit();

            me.frm.form({
                url: url,
                novalidate: true,
                onSubmit: function (param) {
                    $(this).form("enableValidation");
                    var isValid = $(this).form('validate');
                    var gatherType=$("div[name='gat_etype']").find("input[name='gat_etype']:checked").val();
                    if (isValid){ isValid = me.validata(param); }

                    if(gatherType==4) {//活动
                            var seatmap = $("div.whgff-row-map");
                            var seatsValue = seatmap.whgVenseatmaps('getValue');
                            var dataJson = JSON.stringify(seatsValue);
                            var len = 0;
                            for (var o in seatsValue) {
                                for (var obj in seatsValue[o]) {
                                    if (seatsValue[o][obj].open && seatsValue[o][obj].open == "1") {
                                        len++;
                                    }
                                }
                            }
                        var val=$('input:radio[name="sellticket"]:checked').val();
                        if(val == '3') {//选座
                            if (len > 0) {
                                $("#ticketnum").val(len);
                            }
                        }
                            if (len != 0) {
                                var sum = 0;
                                var colsum = 0;
                                if ($("#gat_numsum").val() && $("#gat_numsum").val() != "") {
                                    sum = Number($("#gat_numsum").val());
                                }
                                if ($("#gat_nummin").val() && $("#gat_nummin").val() != "") {
                                    colsum = Number($("#gat_nummin").val());
                                }
                                if (Number(len) < sum) {
                                    isValid = false;
                                    $.messager.alert("错误", "每场可用总座位数不能小于总份数");
                                }
                                /* if(Number(len)<colsum){
                                isValid=false;
                                $.messager.alert("错误","每场可用总座位数不能小于达成份数");
                            }*/
                        }
                        param.seatjson =dataJson;
                        var list = $("#put-ticket-list").children("div.activityTimeLabel1");
                        var activityTimeList = [];
                        $.each(list, function (index, value) {
                            var playstrtime = $(value).find("input[name='playstrtime']").val();
                            var playendtime = $(value).find("input[name='playendtime']").val();
                            var item = {};
                            item.playstrtime = playstrtime;
                            item.playendtime = playendtime;
                            activityTimeList.push(item);
                        });
                        param.activityTimeList = JSON.stringify(activityTimeList);

                    }
                    if (!isValid){
                        $.messager.progress('close');
                        oneSubmit();
                    }else{
                        if (id){ param.gat_id = id; }
                    }
                    return isValid;
                },
                success: function(data){
                    $.messager.progress('close');
                    oneSubmit();
                    data = $.parseJSON(data);
                    if (data.success && data.success=="1"){
                        $.messager.alert("提示", '信息提交成功', 'info',
                                function(){
                                    //(me.pageType=='edit')?  me.closeForm() : me.clearForm();
                                    me.closeForm()
                                }
                        );
                    }else{
                        $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                    }
                }
            })
        },

        //验证，会调用对应组件的 validata 方法
        validata: function (param) {
            var me = this;

            var picture1 = $("#gat_imgurl_2").val();
            if (!picture1){
                $.messager.alert("错误", '图片不能为空', 'error');
                return false;
            }

//            if (!me.gat_descriptions.hasContents()){
//                $.messager.alert("错误", '众筹介绍不能为空', 'error');
//                return false;
//            }else{
                param.gat_descriptions = me.gat_descriptions.getContent();
//            }

            if (me.refTool.validata && $.type(me.refTool.validata)==='function'){
                return me.refTool.validata(param);
            }

            return true;
        },

        //处理图片
        initImg: function(uploadButId, hiddenFieldId, previewImgId, width, height, isCut){
            var me = this;
            var cfg = {
                basePath: me.basePath,
                uploadBtnId: uploadButId || 'imgUploadBtn1',
                hiddenFieldId: hiddenFieldId || 'cult_picture1',
                previewImgId: previewImgId || 'previewImg1',
                cutWidth: width||750,
                cutHeight: height||500,
                needCut: (typeof isCut != 'undefined')? isCut : true
            };
            return WhgUploadImg.init(cfg);
        },
        //处理UE
        initUE: function (ueID, valueJQ) {
            var pageType = this.pageType;
            var ueConfig = {
                scaleEnabled: false,
                autoFloatEnabled: false,
                elementPathEnabled:false,
                readonly: pageType=='show'
            };
            UE.delEditor(ueID);
            this.frm.find("textarea[name='_"+ueID+"']").remove();
            var ueobj = UE.getEditor(ueID, ueConfig);
            ueobj.ready(function(){
                try {
                    ueobj.setContent($(valueJQ).val())
                } catch (e) {
                    ueobj.setContent("")
                }
            });

            return ueobj;
        },
        //添加表单按钮
        appendButton: function (text, iconCls, onClick) {
            var button = $('<a style="margin-right: 5px"></a>');
            this.buttonDiv.append(button);

            var cfg = {};
            cfg.text = text;
            if (iconCls) cfg.iconCls = iconCls;
            if (onClick) cfg.onClick = onClick;

            button.linkbutton( cfg );
            return button;
        }
    };
    
    var ActTool = {
        rowNum : 0,
        colNum : 0,
        seatjson : [],

        initBuder: function () {
            var me = this;
            //文件上传初始化
            WhgUploadFile.init({basePath: MainTool.basePath, uploadBtnId: 'fileUploadBtn1', hiddenFieldId: 'act_filepath1'});
            //根据地址取坐标
            WhgMap.init({basePath: MainTool.basePath, addrFieldId:'address', xpointFieldId:'actlon', ypointFieldId:'actlat', getPointBtnId:'getXYPointBtn'});
            //活动描述富文本
            me.remark = MainTool.initUE("remark", "#value_remark");
            //座位图
            me.seatmap = $('.test-seatmaps').whgVenseatmaps({type: 'use'});

            me.rowNum = 0;
            me.colNum = 0;
            me.seatjson = [];

            MainTool.frm.find("input[type='radio'][name='sellticket']").off().on("click", function(){
                me.setSeat();
            });
            if(MainTool.pageType == "add") {
                me.setSeat();
            }
            MainTool.initVal();

            WhgComm.initPMS({
                basePath:'${basePath}',
                cultEid:'cultid', cultValue:'${info.gat_cultid}', cultOnChange: function(val, oval){ MainTool.cultOnChange(val, oval) },
                ywiTypeEid:'etype', ywiTypeValue:'${info.etype}', ywiTypeClass:4,
                ywiKeyEid:'ekey', ywiKeyValue:'${info.ekey}', ywiKeyClass:4,
                ywiWhppEid:'ebrand', ywiWhppValue:'${info.ebrand}',
                ywiTagEid:'etag', ywiTagValue:'${info.etag}', ywiTagClass:4,
                ywiArtTypeEid:'arttype', ywiArtTypeValue:'${info.arttype}',
                venEid:'venueid', venValue:'${info.venueid}',
                roomEid:'roomid', roomValue:'${info.roomid}'
            });
        },

        cultOnChange: function(val, oval){},

        show: function(){
            var me = this;

            var whgSeat = '';
            try {
                whgSeat = JSON.parse('${whgSeat}');
            } catch (e) {
            }

            me.setSeat(whgSeat);
            me.isIntegral();

            $('#seats').numberspinner({
                onComplete: function(context){
                    var _seats = "${info.seats}";
                    if (_seats != "" && _seats.match(/\d+/)){
                        $(this).numberspinner('setValue', Number(_seats));
                    }
                }
            });
            $('#ticketnum').numberspinner({
                onComplete: function(context){
                    var _ticketnum = "${info.ticketnum}";
                    if (_ticketnum != "" && _ticketnum.match(/\d+/)){
                        $(this).numberspinner('setValue', Number(_ticketnum));
                    }
                }
            })

            me.seatmap.whgVenseatmaps("setModuleType", "show");
            $("#fileUploadBtn1").hide();

            $("#venueid").combobox("setValue","${info.venueid}");
            $("#roomid").combobox("setValue","${info.roomid}");
        },

        edit: function(){
            var me = this;

            var whgSeat = '';
            try {
                whgSeat = JSON.parse('${whgSeat}');
            } catch (e) {
            }

            me.setSeat(whgSeat);
            me.isIntegral();

            var state = '${info.state}';
            if(state == 6 || state == 4){
                $("#venueid").combobox({disabled:true});
                $("#roomid").combobox({disabled:true});
                $('#enterstrtime').datetimebox({ disabled: true});
                $('#enterendtime').datetimebox({ disabled: true});
                $('#whgff').find("input[type='radio'][name='sellticket']").attr('disabled', true);
                $('#whgff').find("input[type='radio'][name='integral']").attr('disabled', true);
            }
        },

        clearForm: function(){
            var me = this;
            me.rowNum = 0;
            me.colNum = 0;
            me.seatjson = [];
        },

        //验证，同时封装特别的参数
        validata: function (param) {
            var me = this;

            if (!me.remark.hasContents()){
                $.messager.alert("错误", '活动描述不能为空', 'error');
                return false;
            }else{
                param.remark = me.remark.getContent();
            }
            //验证活动分类
            var etype = $("input[name='etype']:checked").val();
            if(null == etype || "" == etype){
                $.messager.alert("错误", '请选择活动分类', 'error');
                return false;
            }

            //验证艺术分类
            var arttype = $("input[name='arttype']:checked").val();
            if(null == arttype || "" == arttype){
                $.messager.alert("错误", '请选择艺术分类', 'error');
                return false;
            }

            //验证标签
            var etag = $("input[name='etag']:checked").val();
            if(null == etag || "" == etag){
                $.messager.alert("错误", '请选择标签', 'error');
                return false;
            }

            //验证是否消耗积分
            var integral = $("input[name='integral']:checked").val();
            if(null == integral || "" == integral){
                $.messager.alert("错误", '请选择是否消耗积分', 'error');
                return false;
            }

            //验证是否收费
            var hasfees = $("input[name='hasfees']:checked").val();
            if(null == hasfees || "" == hasfees){
                $.messager.alert("错误", '请选择是否收费', 'error');
                return false;
            }

            //验证在线售票
            var sellticket = $("input[name='sellticket']:checked").val();
            if(null == sellticket || "" == sellticket){
                $.messager.alert("错误", '请选择在线售票', 'error');
                return false;
            }

            var val=$('input:radio[name="integral"]:checked').val();
            if(val == 2){
                var integralNum = $('#integralnum').numberspinner('getValue');
                if(integralNum == null || integralNum == ""){
                    $.messager.alert("提示", '积分数必填！', 'error');
                    return false;
                }
            }
            var val=$('input:radio[name="sellticket"]:checked').val();
            var seats = $('#seats').numberspinner('getValue');
            var ticketNum = $('#ticketnum').numberspinner('getValue');
            if(val == '2'){
                if(seats == '' || ticketNum == ''){
                    $.messager.alert("提示", '每人每场次购票数和每场售票数必填', 'error');
                    return false;
                }else if(Number(seats) > Number(ticketNum)){
                    $.messager.alert("错误", '每人每场次购票不能大于每场次售票', 'error');
                    return false;
                }
            }
            if(val == '3'){
                if(seats == ''){
                    $.messager.alert("提示", '每人每场次购票数必填', 'error');
                    return false;
                }
            }
            return true;
        },

        //场馆改变，将场馆所对应的坐标与地址带出来
        changeVen: function(record){
            var venueid = record.id;//$('#venueid').combobox('getValue');
            var address = $("#address").textbox('getValue');
            if(venueid !=null && venueid != ''){
                var url = getFullUrl('/admin/venue/changeVen?venueId='+venueid);
                $.ajax({
                    url: url,
                    type: "POST",
                    success : function(data){
                        if (!data) return;
                        if(address == null || address == ''){
                            $("#address").textbox('setValue', data.address);
                        }
                        $('#actlon').numberspinner('setValue', data.longitude);
                        $('#actlat').numberspinner('setValue', data.latitude);
                    },
                    error : function() {
                        alert("error");
                    }
                });
            }
        },

        roomSelected: function(rec){
            //活动室onSelect事件时，隐藏在线选座
            $("#onLineSeat").css("display","none");
            ActTool.rowNum = rec.seatrows;
            ActTool.colNum = rec.seatcols;
            try {
                ActTool.seatjson = rec.seatjson ? JSON.parse(rec.seatjson) : [];
            } catch (e) {
                ActTool.seatjson = [];
            }
            var whgSeat = '';
            if(rec.id == '${info.roomid}' && ''!='${whgSeat}'){
                try {
                    whgSeat = JSON.parse('${whgSeat}');
                } catch (e) {
                }
            }else{
                whgSeat = '';
            }

            ActTool.setSeat(whgSeat);
        },

        setSeat: function(whgSeat) {
            var sellticket=$('input:radio[name="sellticket"]:checked').val();
            var valueBeforeSelect = $("#sellticketSelection").val();
            if("1" == sellticket){
                $("#maxBuySeatCount").hide();
                $("#maxSoldSeatCount").hide();
                $("#onLineSeat").hide();
                return;
            }
            if("2" == sellticket){
                var ticketnum = "${info.ticketnum}";
                var seats = "${info.seats}";
                ticketnum = ("" != ticketnum?ticketnum:1);
                seats = ("" != seats?seats:1);

                $("#maxBuySeatCount").find("input").val(seats);
                $("#maxSoldSeatCount").find("input").val(ticketnum);
                $("#maxBuySeatCount").show();
                $("#maxSoldSeatCount").show();
                $("#onLineSeat").hide();
                return;
            }
            if("3" == sellticket){
                $("#maxBuySeatCount").show();
                var seats = "${info.seats}";
                $("#maxBuySeatCount").find("input").val(seats);
                $("#maxSoldSeatCount").hide();
                var myWhgSeat = whgSeat;
                if(myWhgSeat != null && myWhgSeat != ''){
                    ActTool.rowNum = myWhgSeat.rowNum;
                    ActTool.colNum = myWhgSeat.colNum;
                    ActTool.seatjson = myWhgSeat.mySeatMap;
                }
                if(ActTool.rowNum == 0 || ActTool.rowNum == undefined){
                    $.messager.alert("错误", '请选择有座位的活动室!', 'error');
                    $("input:radio[name='sellticket'][value='" + valueBeforeSelect + "']").prop("checked", "checked");
                    $("#maxBuySeatCount").hide();
                    return;
                }
                ActTool.seatmap.whgVenseatmaps('setSeatSize', ActTool.rowNum,ActTool.colNum)
                        .whgVenseatmaps('setValue', ActTool.seatjson);
                $("#onLineSeat").show();
                return;
            }
        },

        //是否消耗积分
        isIntegral: function (){
            var val=$('input:radio[name="integral"]:checked').val();
            if(val == 2){
                $("#integralCount").show();
            }else{
                $("#integralCount").hide();
            }
        }
    };

    var TarTool = {
        initBuder: function () {
            var me = this;
            me.coursedesc = MainTool.initUE("detail", "#value_detail");
            me.outline = MainTool.initUE("catalog", "#value_catalog");
            me.teacherdesc = MainTool.initUE("teacherIntro", "#value_teacherIntro");

            //根据地址取坐标
            WhgMap.init({basePath:'${basePath}', addrFieldId:'address', xpointFieldId:'longitude', ypointFieldId:'latitude', getPointBtnId:'getXYPointBtn'});

            var isMulti = $('input[name="ismultisite"]:checked').val();
            me.showTrainTime(isMulti);

            MainTool.initVal();

            WhgComm.initPMS({
                basePath:'${basePath}',
                cultEid:'cultid', cultValue:'${info.gat_cultid}',cultOnChange: function(val, oval){ MainTool.cultOnChange(val, oval) },
                ywiTypeEid:'etype', ywiTypeValue:'${info.etype}', ywiTypeClass:5,
                ywiKeyEid:'ekey', ywiKeyValue:'${info.ekey}', ywiKeyClass:5,
                ywiTagEid:'etag', ywiTagValue:'${info.etag}', ywiTagClass:5,
                ywiWhppEid:'ebrand', ywiWhppValue:'${info.ebrand}',
                ywiArtTypeEid:'arttype', ywiArtTypeValue:'${info.arttype}',
                deptEid:'deptid', deptValue:'${info.deptid}',
                venEid:'venue', venValue:'${info.venue}',
                roomEid:'venroom', roomValue:'${info.venroom}'
            });

            me.selectCult({id:'${userCultid}'});
        },

        cultOnChange: function(newVal, oval){
            $("#teachername").combobox({
                url:'${basePath}/admin/train/tea/srchList?cultid='+newVal,
                valueField:'name',
                textField:'name'
            })
        },

        show: function(){
            $("#getXYPointBtn").hide();
            $("#imgUploadBtn1").hide();
            $('.easyui-numberspinner').numberspinner('readonly');
            $('.easyui-datetimebox').datetimebox('readonly');

            $("#venue").combobox("setValue","${info.venue}");
            $("#venroom").combobox("setValue","${info.venroom}");
        },

        edit: function(){
            //添加一行课程添加的表单
            MainTool.frm.on('click','.add',function(){
                var trs = $("#whgff").find("div[name='multiContent']");
                var length = $(trs).length;
                var content = "";
                if(length >= 1){
                    for(var i=0;i<length;i++){
                        if(i<1){

                            var _sid = IDUtil.getId(); var _eid = IDUtil.getId();
                            var addHtmlContent = '<div class="whgff-row-label"><label style="color: red">*</label>培训上课时间：</div>'
                                    +'<div class="whgff-row-input">'
                                    +'<input class="easyui-datetimebox starttime_edit" name="_starttime" id="_starttime_'+_sid+'" style="width:200px; height:32px" data-options="required:true, validType:\'edittraTime[0, \\\'_starttime_'+_sid+'\\\']\'">至'
                                    +'&nbsp;<input class="easyui-datetimebox endtime_edit" name="_endtime" id="_endtime_'+_eid+'" style="width:200px; height:32px" data-options="required:true, validType:\'edittraTime[1, \\\'_starttime_'+_sid+'\\\']\'" >'
                                    +'&nbsp;<a href="javascript:void(0)" class="timeico add">添加</a>'
                                    +'&nbsp;&nbsp;<a href="javascript:void(0)" class="timeico del">删除</a>'
                                    +'</div>';

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
            }); //添加一行课程添加的表单  END

            //删除一行
            MainTool.frm.on('click','.del',function(){
                TarTool.on_del.call(this);
            });  //删除一行 END

            //单场培训、多场、固定场
            MainTool.frm.on('click','.whg-js-data input[name="ismultisite"]',function(){
                //TarTool.on_del.call(this);
                TarTool.on_select.call(this);
            });
        },

        add: function(){
            //添加一行课程添加的表单
            MainTool.frm.on('click','.add',function(){
                var trs = $("#whgff").find("div[name='multiContent']");
                var length = $(trs).length;
                var content = "";
                if(length >= 1){
                    for(var i=0;i<length;i++){
                        if(i<1){
                            if(MainTool.pageType == 'add') {

                                var _sid = IDUtil.getId();
                                var _eid = IDUtil.getId();
                                var addHtmlContent = '<div class="whgff-row-label"><label style="color: red">*</label>培训上课时间：</div>'
                                        + '<div class="whgff-row-input">'
                                        + '<input class="easyui-datetimebox _starttime" name="_starttime" id="_starttime_' + _sid + '" style="width:200px; height:32px" data-options="required:true, validType:\'traTime[0, \\\'_starttime_' + _sid + '\\\']\'">至'
                                        + '&nbsp;<input class="easyui-datetimebox _endtime" name="_endtime" id="_endtime_' + _eid + '" style="width:200px; height:32px" data-options="required:true, validType:\'traTime[1, \\\'_starttime_' + _sid + '\\\']\'" >'
                                        + '&nbsp;<a href="javascript:void(0)" class="timeico add">添加</a>'
                                        + '&nbsp;&nbsp;<a href="javascript:void(0)" class="timeico del">删除</a>'
                                        + '</div>';
                            }else{
                                var _sid = IDUtil.getId(); var _eid = IDUtil.getId();
                                var addHtmlContent = '<div class="whgff-row-label"><label style="color: red">*</label>培训上课时间：</div>'
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
            }); //添加一行课程添加的表单  END

            //删除一行
            MainTool.frm.on('click','.del',function(){
                TarTool.on_del.call(this);
            });  //删除一行 END


            //单场培训、多场、固定场
            MainTool.frm.on('click','.whg-js-data input[name="ismultisite"]',function(){
                TarTool.on_select.call(this);
            });
        },

        clearForm: function(){
            var me = this;
            me.coursedesc.setContent('');
            me.outline.setContent('');
            me.teacherdesc.setContent('');
        },

        //验证，同时封装特别的参数
        validata: function (param) {
            var me = this;

            //艺术分类不能为空
            var arttype = $("#whgff").find("input[name='arttype']:checked").val();
            if (!arttype){
                $.messager.alert("错误", '艺术分类不能为空！', 'error');
                return false;
            }

            var etype = $("#whgff").find("input[name='etype']:checked").val();
            if (!etype) {
                $.messager.alert("错误", '培训分类不能为空！', 'error');
                return false;
            }

            //课程详情不能为空
            if (!me.coursedesc.hasContents()){
                $.messager.alert("错误", '培训详情不能为空！', 'error');
                return false;
            }

            //固定班周几验证
            if(parseInt($('input[name="ismultisite"]:checked').val()) == 2){

                var week = $('input[name="fixedweek"]:checked').val();
                if(!week){
                    $.messager.alert("错误", '固定周几不能为空！', 'error');
                    return false;
                }

                var starthour = $("._coursestarttime").timespinner('getHours');
                var startmin = $("._coursestarttime").timespinner('getMinutes');
                var endhour = $("._courseendtime").timespinner('getHours');
                var endmin = $("._courseendtime").timespinner('getMinutes');
                var _a = starthour*60+startmin;
                var _b = endhour*60+endmin;

                if(_a > _b){
                    $.messager.alert("错误", '固定上课开始时间不能晚于结束时间！', 'error');
                    return false;
                }

            }//固定班周几验证 --END

            param.teacherdesc = me.teacherdesc.getContent();
            param.outline = me.outline.getContent();
            param.coursedesc = me.coursedesc.getContent();

            param.ekey = $("#ekey").combobox("getText");
            return true;
        },

        on_del: function(){
            var length = $("#whgff").find("div[name='multiContent']").length;
            if(1 == length){
                $.messager.alert("提示","最少保留一次培训日期！");
                return
            }
            //删除当前行,上一行
            var curTr = $(this).parent().parent();
            $(curTr).remove();
        },

        on_select: function(){
            var ck = this.checked;
            var value = $(this).val();
            if(ck){
                TarTool.showTrainTime(value);
            }else{
                TarTool.showTrainTime(0);
            }
        },


        selectCult: function(record) {
            var cultid = record.id;
            var data = WhgComm.getChildDept(cultid);
            $(".select-deptid").combobox({data: data});
        },

        //显示培训时间(0单场,1多场,2固定场)
        showTrainTime: function(isMulti){
            //var id = '${info.gat_refid}';
            var state = '${state}';
            if (MainTool.pageType == 'add'){
                //添加的时候
                //if(!id){
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
                        $(".train").css("display","");
                        $(".starttime").datebox({required:true});
                        $(".endtime").datebox({required:true});
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
                //}
            }else{
                //编辑的时候
                //if(id){
                    //单场
                    if(0 == isMulti){
                        $(".train").css("display","none");
                        $(".starttime").datebox({required:false});
                        $(".endtime").datebox({required:false});
                        $(".single_edit").css("display","");
                        $(".multi_edit").css("display","none");
                        $(".fixed").css("display","none");
                        //单场编辑用的上课时间表单
                        $(".sin_starttime_edit").datetimebox("enable");
                        $(".sin_endtime_edit").datetimebox("enable");
                        //多场添加用的上课时间表单
                        $("._starttime").datetimebox("disable");
                        $("._endtime").datetimebox("disable");
                        if(state == 4){
                            $('input[name="isbasicclass"]').attr('disabled', true);
                            $('input[name="ismultisite"]').attr('disabled', true);
                            $(".enrollstarttime").datetimebox('readonly');
                            //  $(".enrollendtime").datetimebox('readonly');
                            $(".sin_starttime_edit").datetimebox("readonly");
                            $(".sin_endtime_edit").datetimebox("readonly");
                        }
                    }
                    //多场
                    if(1 == isMulti){
                        $(".single_edit").css("display","none");
                        $(".multi_edit").css("display","");
                        $(".fixed").css("display","none");
                        $(".train").css("display","");
                        $(".starttime").datebox({required:true});
                        $(".endtime").datebox({required:true});
                        if(state == 4){
                            $('input[name="isbasicclass"]').attr('disabled', true);
                            $('input[name="ismultisite"]').attr('disabled', true);
                            $(".enrollstarttime").datetimebox('readonly');
                            // $(".enrollendtime").datetimebox('readonly');
                            $(".starttime").datetimebox("readonly");
                            $(".endtime").datetimebox("readonly");
                        }
                        $("._starttime").datetimebox("disable");
                        $("._endtime").datetimebox("disable");
                        $(".sin_starttime").datetimebox("disable");
                        $(".sin_endtime").datetimebox("disable");
                    }
                    //固定场
                    if(2 == isMulti){
                        $(".train").css("display","");
                        $(".starttime").datebox({required:true});
                        $(".endtime").datebox({required:true});
                        //   $('input[name="ismultisite"]').on('click', function(){return false});
                        $(".single_edit").css("display","none");
                        $(".multi_edit").css("display","none");

                        $(".fixed").css("display","");
                        $("._coursestarttime").timespinner("enable");
                        $("._courseendtime").timespinner("enable");
                        if(state == 4){
                            $('input[name="isbasicclass"]').attr('disabled', true);
                            $('input[name="ismultisite"]').attr('disabled', true);
                            $(".enrollstarttime").datetimebox('readonly');
                            //  $(".enrollendtime").datetimebox('readonly');
                            $(".starttime").datetimebox("readonly");
                            $(".endtime").datetimebox("readonly");
                            $('input[name="fixedweek"]').on('click', function(){return false});
                            $("._coursestarttime").timespinner("readonly");
                            $("._courseendtime").timespinner("readonly");
                        }
                        //禁用单场添加时的日期框
                        $(".sin_starttime").datetimebox("disable");
                        $(".sin_endtime").datetimebox("disable");
                        $("._starttime").datetimebox("disable");
                        $("._endtime").datetimebox("disable");

                    }
                //}
            }
        }
    };

    var OtherTool = {
        initBuder: function (pageType) {
            WhgUploadFile.init({basePath: MainTool.basePath, uploadBtnId: 'gat_enclosure_1', hiddenFieldId: 'gat_enclosure_2'});
            MainTool.initVal();

            WhgComm.initPMS({
                basePath:'${basePath}',
                cultEid:'cultid', cultValue:'${info.gat_cultid}',cultOnChange: function(val, oval){ MainTool.cultOnChange(val, oval) }
            });
        },

        cultOnChange: function(val, oval){},

        show: function(){
            $("#gat_enclosure_1").hide();
        }
    };


    var IDUtil = (function(){
        var __id = 100;

        function _getId(){
            return __id++;
        }

        return {
            getId: function(){
                return _getId();
            }
        }
    })();

</script>

</body>
</html>
