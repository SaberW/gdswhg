<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %><% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<% String path = request.getContextPath();%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
	<title>活动管理-
		<c:if test="${param.onlyshow != '1'}">编辑</c:if>
		<c:if test="${param.onlyshow == '1'}">查看</c:if>活动资料</title>
	<%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>

    <script type="text/javascript" charset="utf-8"	src="<%=path%>/static/ueditor/ueditor.config.js"></script>
	<script type="text/javascript" charset="utf-8"	src="<%=path%>/static/ueditor/ueditor.all.min.js"></script>
	<script type="text/javascript" charset="utf-8"	src="<%=path%>/static/ueditor/lang/zh-cn/zh-cn.js"></script>

    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-file.js"></script>

	<script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
	<script type="text/javascript" src="${basePath}/static/common/js/whg.maps.js"></script>
	<script src="${basePath}/static/admin/js/whgmodule-venseatmaps.js"></script>

	<script type="text/javascript" src="${basePath}/static/common/js/area.js"></script>
	<script>

         //省市区------END

        $.extend($.fn.validatebox.defaults.rules, {
            bmEndTime: {
                validator: function(value, param){
                    var sdVal = $('#'+param[0]).datetimebox('getValue');
                    var d1 = WhgComm.parseDateTime(sdVal);//$.fn.datebox.defaults.parser(sdVal);
                    var d2 = WhgComm.parseDateTime(value);//$.fn.datebox.defaults.parser(value);
                    return d2.getTime()>d1.getTime();
                },
                message: '活动报名结束时间必须大于活动报名开始时间.'
            }, bmStartDate: {//活动开始日期 大于 活动报名日期
                validator: function (value, param) {
                    var sdVal = $('input[name="' + param[0] + '"]').val();
                    //var sdVal = $('#' + param[0]).datetimebox('getValue');
                    var d1 = WhgComm.parseDateTime(sdVal);//$.fn.datebox.defaults.parser(sdVal);
                    var d2 = WhgComm.parseDate(value);//$.fn.datebox.defaults.parser(value);
                    // return d2.getTime() > d1.getTime();
                    return true;
                },
                message: '活动开始日期必须大于活动报名结束日期.'
            },
            hdEndDate: {
                validator: function(value, param){
                    var sdVal = $('#'+param[0]).datebox('getValue');
                    var d1 = WhgComm.parseDate(sdVal);//$.fn.datebox.defaults.parser(sdVal);
                    var d2 = WhgComm.parseDate(value);//$.fn.datebox.defaults.parser(value);
                    return d2.getTime()>=d1.getTime() || sdVal == value;
                },
                message: '活动结束日期必须大于活动开始日期.'
            }
        });

        function setBranch() {
            $.getJSON("${basePath}/admin/branch/branchListUser",function (data) {

                if("1" != data.success){
                    $.messager.alert("错误", data.errormsg, 'error');
                    return;
                }
                var rows = data.rows;
                $("#branch").combobox("loadData",rows);
//                debugger;
                var branchId = "${whBranchRel.branchid}";
                if(0 < rows.length){
                    branchId = branchId != ""?branchId:rows[0].id;
                    $("#branch").combobox("setValue",branchId);
                }
            });
        }
	</script>
</head>
<body>


<form id="whgff" class="whgff" method="post">
	<input id="tempstorage" name="tempstorage" type="hidden" value="0">
	<h2>活动管理-<c:if test="${param.onlyshow != '1'}">编辑</c:if><c:if test="${param.onlyshow == '1'}">查看</c:if>活动</h2>

    <input type="hidden" name="id" value="${act.id}" >
    <input type="hidden" name="onlyshow" value="${param.onlyshow}" >

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>活动名称：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="name" value="${act.name}" style="width:500px; height:32px" data-options="required:true, validType:'length[1,100]'"></div>
    </div>

	<div class="whgff-row">
		<div class="whgff-row-label">活动简介：</div>
		<div class="whgff-row-input">
			<input class="easyui-textbox" name="actsummary" value="${act.actsummary}"  style="width:500px; height:32px" data-options="required:false,validType:['length[1,200]']">
		</div>
	</div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>活动封面：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="act_imgurl1" name="imgurl" value="${act.imgurl}" >
            <div class="whgff-row-input-imgview" id="previewImg1"></div>
			<div class="whgff-row-input-imgfile" id="divImgFile">
				<i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
				<i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
			</div>
        </div>
    </div>

    <div class="whgff-row">
		 <div class="whgff-row-label"><i>*</i>区域：</div>
		 <%--<div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" id="areaid"  name="areaid" value="${act.areaid}" js-data="WhgComm.getAreaType"></div>
		</div>--%>
		<input class="easyui-combobox" style="width:150px; height:32px" id="province" name="province" data-options="required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name', data:__PROVINCE, onChange:myChangeProvince"/>
		<input class="easyui-combobox" style="width:150px; height:32px" id="__CITY_ELE" name="city" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name', data:[], onChange:myChangeCity"/>
		<input class="easyui-combobox" style="width:179px; height:32px" id="__AREA_ELE" name="areaid" data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
	</div>


	<div class="whgff-row">
		<div class="whgff-row-label"><i>*</i>所属单位：</div>
		<div class="whgff-row-input">
			<input class="easyui-combobox" style="width:245px; height:32px" name="cultid" id="cultid" data-options="required:true" />
			<input class="easyui-combobox" style="width:245px; height:32px" name="deptid" id="deptid" data-options="required:true" />

		</div>
	</div>

	<div class="whgff-row">
		<div class="whgff-row-label">文化品牌：</div>
		<div class="whgff-row-input">
			<input class="easyui-combobox" style="width:500px; height:32px" name="ebrand" id="ebrand" data-options="prompt:'请选择文化品牌'" />
		</div>
	</div>
	</div>

    <div class="whgff-row">
       <div class="whgff-row-label">场馆：</div>
		<div class="whgff-row-input">
			<input class="easyui-combobox" style="width:245px; height:32px" name="venueid" id="venueid"  data-options="editable:true,limitToList:true,required:false
                   ,onChange:changeVen" />
		</div>
    </div>

	<div class="whgff-row">
		<div class="whgff-row-label">活动室：</div>
		<div class="whgff-row-input">
			<input class="easyui-combobox" style="width:245px; height:32px" name="roomid" id="roomid"  data-options="editable:true,limitToList:true,required:false
                   ,onSelect:roomSelected
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
			<input class="easyui-textbox" name="host" value="${act.host }"
				style="width: 500px; height: 32px" data-options="required:false,validType:['length[0,100]']" />
		</div>
	</div>
	<div class="whgff-row">
		<div class="whgff-row-label">承办单位：</div>
		<div class="whgff-row-input">
			<input class="easyui-textbox" name="organizer" value="${act.organizer }"
				style="width: 500px; height: 32px" data-options="required:false,validType:['length[0,100]']" />
		</div>
	</div>

	<div class="whgff-row">
		<div class="whgff-row-label">协办单位：</div>
		<div class="whgff-row-input">
			<input class="easyui-textbox" name="coorganizer" value="${act.coorganizer }"
				style="width: 500px; height: 32px" data-options="required:false,validType:['length[0,100]']" />
		</div>
	</div>

	<div class="whgff-row">
		<div class="whgff-row-label">演出单位：</div>
		<div class="whgff-row-input">
			<input class="easyui-textbox" name="performed" value="${act.performed }"
				style="width: 500px; height: 32px" data-options="required:false,validType:['length[0,100]']" />
		</div>
	</div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>联系人：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="contacts" value="${act.contacts }" style="width:500px; height:32px" data-options="prompt:'请输入联系人',required:true, validType:['length[2,20]']"></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>固定电话：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="telephone" value="${act.telephone}"  style="width:500px; height:32px" data-options="prompt:'请输入固定电话', required:true, validType:'isTel'"></div>
    </div>

	<div class="whgff-row">
		<div class="whgff-row-label">主讲人：</div>
		<div class="whgff-row-input">
			<input class="easyui-textbox" name="speaker" value="${act.speaker }"
				style="width: 500px; height: 32px" data-options="required:false,validType:['length[0,100]']" />
		</div>
	</div>

	<%--<div class="whgff-row">
		<div class="whgff-row-label"><label style="color: red">*</label>所属单位：</div>
		<div class="whgff-row-input">
			<input class="easyui-combobox" name="branch" id="branch" panelHeight="auto" limitToList="true" style="width:500px; height:32px"
				   data-options="required:false, editable:false,multiple:false, mode:'remote',
                   valueField:'id', textField:'name'
                   "/>
		</div>
	</div>--%>



    <div class="whgff-row">
		 <div class="whgff-row-label"><i>*</i>活动分类：</div>
		 <div class="whgff-row-input">
			 <div class="checkbox checkbox-primary" id="etype" name="etype"></div>
		</div>
	</div>

	<div class="whgff-row">
		<div class="whgff-row-label"><i>*</i>艺术分类：</div>
		<div class="whgff-row-input">
			<div class="checkbox checkbox-primary" id="arttype" name="arttype"></div>
		</div>
	</div>


    <div class="whgff-row">
		<div class="whgff-row-label">标签：</div>
		<div class="whgff-row-input">
			<div class="checkbox checkbox-primary" id="etag" name="etag"></div>
		</div>
	</div>

	<div class="whgff-row">
		<div class="whgff-row-label">关键字：</div>
		<div class="whgff-row-input">
			<input class="easyui-combobox" id="ekey" name="ekey" style="width:500px; height:32px" validType="notQuotes"
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
		<div class="whgff-row-label"><i>*</i>活动报名时间：</div>
		<div class="whgff-row-input">
			<input class="easyui-datetimebox" style="width: 240px; height: 32px;" id="enterstrtime"
				   name="enterstrtime" <c:if test="${NotEdit eq 'true'}"> readonly="true" </c:if>
				   value="<fmt:formatDate value='${act.enterstrtime}' pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>"
				   data-options="editable:false,required:true,prompt:'请选择'"/> ~
			<input class="easyui-datetimebox" style="width: 240px; height: 32px;" id="enterendtime"
				   name="enterendtime" <c:if test="${NotEdit eq 'true'}"> readonly="true" </c:if>
				   value="<fmt:formatDate value='${act.enterendtime}' pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>"
				   data-options="editable:false,required:true,prompt:'请选择',validType:'bmEndTime[\'enterstrtime\']'"/>
		</div>
	</div>

	<div class="whgff-row">
		<div class="whgff-row-label"><i>*</i>活动日期：</div>
		<div class="whgff-row-input">
			<input class="easyui-datebox" style="width: 240px; height: 32px;" id="starttime"
				   name="starttime"
				   value="<fmt:formatDate value='${act.starttime}' pattern="yyyy-MM-dd"></fmt:formatDate>"
				   data-options="editable:false,required:true,prompt:'请选择',validType:'bmStartDate[\'enterendtime\']'"/>
			~
			<input class="easyui-datebox" style="width: 240px; height: 32px;" id="endtime"
				   name="endtime"
				   value="<fmt:formatDate value='${act.endtime}' pattern="yyyy-MM-dd"></fmt:formatDate>"
				   data-options="editable:false,prompt:'请选择',validType:'hdEndDate[\'starttime\']'"/>
        </div>
	</div>

	<div class="whgff-row" style="display: none">
		<div class="whgff-row-label">
			<label id="timeStarSpan" style="color: red">*</label>场次模板：
		</div>
		<div class="whgff-row-input">
			<div id="free-time-set">
				<div id="put-ticket-list" style="width: 800px;">

				</div>
			</div>
		</div>
	</div>

	<div class="whgff-row">
		<div class="whgff-row-label"><i>*</i>地址：</div>
		<div class="whgff-row-input">
			<input class="easyui-textbox" name="address" id="address" value="${act.address}" style="width: 500px; height: 32px" data-options="required:true,validType:['length[0,60]']">
		</div>
	</div>

	<div class="whgff-row">
		<div class="whgff-row-label"><label style="color: red">*</label>坐标：</div>
		<div class="whgff-row-input">
			<input class="easyui-numberbox" name="actlon" id="actlon" value="${act.actlon}"
				   style="width: 100px; height: 32px"
				   data-options="required:false, precision:6,readonly:true,prompt:'X轴'">
			<input class="easyui-numberbox" name="actlat" id="actlat" value="${act.actlat}"
				   style="width: 100px; height: 32px"
				   data-options="required:false, precision:6,readonly:true,prompt:'Y轴'">
			<a class="easyui-linkbutton whg-maps" map-addr="#address" vm-x="actlon" vm-y="actlat" id="getXYPointBtn" text="选择坐标"></a>
		</div>
	</div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>联系电话：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="telphone" value="${act.telphone}" style="width:300px; height:32px" data-options="prompt:'请输入联系人手机', required:true, validType:'isPhone'"></div>
    </div>

	<div class="whgff-row" style="display: none">
		<div class="whgff-row-label">
			<label style="color: red">*</label>是否消耗积分：
		</div>
		<div class="whgff-row-input">
			<div class="radio radio-primary whg-js-data" name="integral" id="integral" value="${act.integral }"
				 onclick="isIntegral()"
				 js-data='[{"id":"1","text":"默认"},{"id":"2","text":"积分"}]'>
                <input  type="hidden" value="1">
           </div>
           <span id="integralCount" style="display:none">该活动需要消耗积分<input class="easyui-numberspinner" name="integralnum" value="${act.integralnum }" id="integralnum" style="width: 50px; height: 25px" data-options="required:false,min:0,max:999">分 </span>
		</div>
	</div>

	<div class="whgff-row" style="display: none">
		<div class="whgff-row-label">
			<label style="color: red">*</label>是否收费：
		</div>
		<div class="whgff-row-input">
	    	<div class="radio radio-primary whg-js-data" name="hasfees" id="hasfees"   value="${act.hasfees }"
                js-data='[{"id":"0","text":"否"},{"id":"1","text":"是"}]'>
           </div>
		</div>
	</div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>实名制：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" value="${act.isrealname}" name="isrealname" js-data='[{"id":"0","text":"否"},{"id":"1","text":"是"}]'></div>
        </div>
    </div>

	<div class="whgff-row">
		<div class="whgff-row-label"><i>*</i>提醒方式：
		</div>
		<div class="whgff-row-input">
			<div class="checkbox checkbox-primary whg-js-data" name="noticetype" id="noticetype" value="${act.noticetype}"
				 js-data='[{"id":"SMS","text":"短信"},{"id":"ZNX","text":"站内信"}]'>
			</div>
		</div>
	</div>

	<div class="whgff-row">
		<div class="whgff-row-label"><i>*</i>在线售票：</div>
		<div class="whgff-row-input">
			<div class="radio radio-primary whg-js-data" name="sellticket" id="sellticket" readonly="true"
				 value="${act.sellticket }" onclick="setSeat()"
				 js-data='[{"id":"1","text":"不可预定"},{"id":"2","text":"自由入座"},{"id":"3","text":"在线选座"}]'>
                <input id="sellticketSelection" type="hidden" value="1">
           </div>
			<span id="maxBuySeatCount" style="display:none">每人每场最多购票<input class="easyui-numberspinner"
			<c:if test="${NotEdit eq 'true'}"> readonly="true" </c:if> name="seats" id="seats"
																		   style="width: 80px; height: 25px">张 </span>
			<span id="maxSoldSeatCount" style="display:none">每场次最多售票<input class="easyui-numberspinner"
			<c:if test="${NotEdit eq 'true'}"> readonly="true" </c:if> name="ticketnum" id="ticketnum"
																		   style="width: 100px; height: 25px">张 </span>
		</div>
	</div>
    <div class="whgff-row acturl">
        <div class="whgff-row-label">跳转链接：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="acturl" value="${act.acturl}" style="width:600px; height:32px" data-options="required:false,validType:'url',prompt:'请输入连接地址,如：http://yoursite.com/'">
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
			<script id="remark" name="remark"  type="text/plain" style="width: 800px; height: 600px;">${act.remark}</script>
		</div>
	</div>

	<div class="whgff-row">
		<div class="whgff-row-label">
			上传附件：
		</div>
		<div class="whgff-row-input">
			<input  id="act_filepath1" name="filepath" value="${act.filepath }" data-options="required:true" style="width:600px;height:32px;" readonly="readonly">
			<div class="whgff-row-input-file" id="filepath">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="fileUploadBtn1">选择文件</a></i>
                <i>附件格式为doc,docx,xls,zip,xlsx，建议图文件大小为10MB以内</i>
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

<div id="whgwin-edit-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <c:if test="${param.onlyshow != '1'}">
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok" id="whgwin-edit-btn-save">提 交</a>
		<c:if test="${pageType eq 'editList'}">
			<a class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok" title="暂时保存数据，可下次编辑提交"
			   id="whgwin-add-btn-storage">保存草稿</a>
		</c:if>
	</c:if>
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
</div>

<!-- script -->
<script type="text/javascript">
	//省市区
	function myChangeProvince(newVal, oldVal) {
		changeProvince(newVal, oldVal, function(){
            if(typeof(window.__init_city) == 'undefined'){
                $('#__CITY_ELE').combobox('setValue', '${act.city}');
                window.__init_city = true;
            }
        });
	}

	function myChangeCity(newVal, oldVal) {
		changeCity(newVal, oldVal, function(){
            if(typeof(window.__init_area) == 'undefined'){
                $('#__AREA_ELE').combobox('setValue', '${act.areaid}');
                window.__init_area = true;
            }
        });
	}  //省市区------END


	var seatmap;
	//定义座位变量
	var rowNum = 0;
	var colNum = 0;
	var seatjson = '';
	//初始化富文本
    var ueConfig = {
        scaleEnabled: false,
        autoFloatEnabled: false
    };
	var remark = UE.getEditor('remark', ueConfig);
    $(function () {
        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'${act.cultid}',
            deptEid:'deptid', deptValue:'${act.deptid}',
            ywiWhppEid:'ebrand', ywiWhppValue:'${act.ebrand}',
            ywiArtTypeEid:'arttype', ywiArtTypeValue:'${act.arttype}',
            ywiTypeEid:'etype', ywiTypeValue:'${act.etype}', ywiTypeClass:4,
            ywiKeyEid:'ekey', ywiKeyValue:'${act.ekey}', ywiKeyClass:4,
            ywiTagEid:'etag', ywiTagValue:'${act.etag}', ywiTagClass:4,
            venEid:'venueid', venValue:'${act.venueid}',
            roomEid:'roomid', roomValue:'${act.roomid}'
        });
		$('#province').combobox('setValue', '${act.province}');
    	var state = '${act.state}';
        if (state && state == 4) {
            showXjReason('${act.id}');
        }
        // setBranch();
        //var seatmap = $("div.whgff-row-map");
        seatmap = $('.test-seatmaps').whgVenseatmaps(({type: 'use', itemDbClick: false}));
      	//图片初始化
        var whgImg = WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'act_imgurl1', previewImgId: 'previewImg1'});
      	//文件上传初始化
        WhgUploadFile.init({basePath: '${basePath}', uploadBtnId: 'fileUploadBtn1', hiddenFieldId: 'act_filepath1'});
       //根据地址取坐标
        WhgMap.init({basePath:'${basePath}', addrFieldId:'address', xpointFieldId:'actlon', ypointFieldId:'actlat', getPointBtnId:'getXYPointBtn'});
        var show = "${param.onlyshow}";
        if (show && show == '1') {
            var me = this;
            //组件设为只读
            $('.easyui-textbox').textbox('readonly');
            $('.easyui-combobox').combobox('readonly');
            $('.easyui-datebox').combobox('readonly');
            $('.easyui-numberspinner').numberspinner('readonly');
            $('.easyui-datetimebox').datetimebox('readonly');
            $("div.radio").on("DOMNodeInserted", function (e) {
                $(e.target).attr('disabled', true);
            });
            $("#whgff").find("input[type='checkbox']").unbind();
            $("#whgff").find("input[type='radio']").unbind();
            $("#whgff").on('click', "input[type='checkbox']", function () {
                return false
            });
            $("#whgff").on('click', "input[type='radio']", function () {
                return false
            });
            $("#imgUploadBtn1").hide();
        }
        //暂存事件
        function storageFun() {
            $("#tempstorage").val("1");//暂存
            $('#whgff').submit();
        }

		//提交事件
		function submitFun() {
            if(!supplement()){
                return;
            }
            if(!validateUE()){
                return;
            }
            if(!validateTicket()){
                return;
            }
            if(!validateIntegral()){
                return;
            }
            $('#whgff').submit();
		}


        function supplement() {
		    //验证区域
//			var areaid = $("input[name='areaid']:checked").val();
//			if(null == areaid || "" == areaid){
//                $.messager.alert("错误", '请选择区域', 'error');
//                return false;
//			}

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

			/*//验证标签
			var etag = $("input[name='etag']:checked").val();
            if(null == etag || "" == etag){
                $.messager.alert("错误", '请选择标签', 'error');
                return false;
			 }*/

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
			return true;
        }

		 //处理UE项的验证
        function validateUE(){
           var picture1 = $("#act_imgurl1").val();
           if (!picture1){
               $.messager.alert("错误", '图片不能为空', 'error');
               return false;
           }
            //提醒方式不能为空
            var notice = $("#whgff").find("input[name='noticetype']:checked").val();
            if (!notice) {
                $.messager.alert("错误", '提醒方式不能为空！', 'error');
                return false;
            }
           if (!remark.hasContents()){
               $.messager.alert("错误", '活动描述不能为空', 'error');
               return false;
           }
           return true;
        }

      //验证积分数
		function validateIntegral(){
			var val=$('input:radio[name="integral"]:checked').val();
			if(val==1){
				return true;
			}
			if(val == 2){
				var integralNum = $('#integralnum').numberspinner('getValue');
				if(integralNum == null || integralNum == ""){
					$.messager.alert("提示", '积分数必填！', 'error');
					return false;
				}
			}
			return true;
        }

		//是否消耗积分
		function isIntegral(){
			var val=$('input:radio[name="integral"]:checked').val();
			if(val == 2){
				$("#integralCount").show();
			}
			if(val == 1){
				$("#integralCount").hide();
			}
		}

        //验证在线活动售票数
	 	function validateTicket(){
	           var val=$('input:radio[name="sellticket"]:checked').val();
	           if(val == '1'){//不可预定不验证
	               return true;
			}

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
		}

        //初始化富文本
	   	var ue_remark =  UE.getEditor('remark');
        $('#whgff').form({
            novalidate: true,
            url : getFullUrl('/admin/activity/edit'),
            onSubmit : function(param) {
                var seatsValue = seatmap.whgVenseatmaps('getValue');
                param.seatjson = JSON.stringify(seatsValue);
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
                        $('#ticketnum').numberspinner('setValue',len);
                    }
                }
				/* var list = $("#put-ticket-list").children("div.activityTimeLabel1");
                var times = timesUI.getValue();
				 param.activityTimeList = JSON.stringify(times);*/
                //在线选座，自由选座按钮恢复不可用
                $("input[name='sellticket']").attr('disabled', false);
                var _valid = $(this).form('enableValidation').form('validate');
                var isstorage = $("#tempstorage").val();
                if (isstorage == "1") {//暂存，放过所有验证
                    _valid = true;
                    var _dept = $("#deptid").combobox('getValue');
                    if (_dept == "") {
                        $.messager.alert("提示", '所属部门不能为空', 'error');
                        _valid = false;
                    }
                }
                if(_valid){
                    $.messager.progress();
                }else{
                    //失败时再注册提交事件
                    $('#whgwin-edit-btn-save').off('click').one('click',submitFun);
                }
                return _valid;
            },
            success : function(data) {
                $.messager.progress('close');
                var Json = eval('('+data+')');
                if(Json && Json.success == '1'){
                    if($('#whgff input[name="onlyshow"]').val() != "1") {
                        $.messager.alert("提示", '操作成功', 'info',
                            function () {
                                window.parent.$('#whgdg').datagrid('reload');
                                WhgComm.editDialogClose();
                            }
                        );
                    }
                } else {
                    $.messager.alert('提示', '操作失败:'+Json.errormsg+'!', 'error');
                    $('#whgwin-add-btn-save').off('click').one('click', submitFun);
                }
            }
        });
        //注册提交事件
        $('#whgwin-edit-btn-save').on('click', submitFun);
        //注册暂存事件
        $('#whgwin-add-btn-storage').on('click', storageFun);
        var whgSeat = JSON.parse('${whgSeat}');

        setSeat(whgSeat);
        isIntegral();
    });


    function roomSelected(rec){
		//活动室onSelect事件时，隐藏在线选座
		$("#onLineSeat").css("display","none");
		rowNum = rec.seatrows;
		colNum = rec.seatcols;
		seatjson = rec.seatjson? JSON.parse(rec.seatjson) : [];
		console.info(seatjson);
        var whgSeat = '';
		if(rec.id == '${act.roomid}' ){
            whgSeat = JSON.parse('${whgSeat}');
		}else{
			whgSeat = '';
		}

        setSeat(whgSeat);
	}


    function  setSeat(whgSeat) {
        var sellticket=$('input:radio[name="sellticket"]:checked').val();
		var valueBeforeSelect = $("#sellticketSelection").val();
        if("1" == sellticket){
            $(".acturl").show();
            $("#maxBuySeatCount").hide();
            $("#maxSoldSeatCount").hide();
            $("#onLineSeat").hide();
            return;
        }
        if("2" == sellticket){
            $(".acturl").hide();
            var ticketnum = "${act.ticketnum}";
            var seats = "${act.seats}";
            ticketnum = ("" != ticketnum?ticketnum:0);
            seats = ("" != seats?seats:0);
            $("#seats").numberspinner('setValue', seats);
            $("#ticketnum").numberspinner('setValue', ticketnum);
            $("#maxBuySeatCount").show();
            $("#maxSoldSeatCount").show();
            $("#onLineSeat").hide();
            return;
        }
        if("3" == sellticket){
            $(".acturl").hide();
            $("#maxBuySeatCount").show();
			var seats = "${act.seats}";
            $("#seats").numberspinner('setValue', seats);
            $("#maxSoldSeatCount").hide();
            var myWhgSeat = whgSeat;
            if(myWhgSeat != null && myWhgSeat != ''){
            	rowNum = myWhgSeat.rowNum;
            	colNum = myWhgSeat.colNum;
            	seatjson = myWhgSeat.mySeatMap;
            }
            if(rowNum == 0 || rowNum == undefined){

                $.messager.alert("错误", '请选择有座位的活动室!', 'error');
                $("input:radio[name='sellticket'][value='" + valueBeforeSelect + "']").prop("checked", "checked");
                $("#maxBuySeatCount").hide();
                return;
            }
            //$('.test-seatmaps').whgVenseatmaps({type: 'use'})
            seatmap.whgVenseatmaps('setSeatSize', rowNum,colNum)
                .whgVenseatmaps('setValue', seatjson);
            $("#onLineSeat").show();
            return;
        }
    }

    //场馆改变，将场馆所对应的坐标与地址带出来
    function changeVen() {
		/* var venueid = $("#venueid").val(); */
        var venueid = $('#venueid').combobox('getValue');
		var address = $("#address").textbox('getValue');
		if(venueid !=null && venueid != ''){
			var url = getFullUrl('/admin/venue/changeVen?venueId='+venueid);
            $.ajax({
                url: url,
                type: "POST",
                success: function (data) {
		        	   if (!data) return;
		        	   if(address == null || address == ''){
		        		   $("#address").textbox('setValue', data.address);
		        	   }
                    $('#actlon').numberspinner('setValue', data.longitude);
		        	   $('#actlat').numberspinner('setValue', data.latitude);
                },
                error: function () {
                    alert("error");
                }
		        });
		}
    }
    /**
     * 时间项处理
     * */
    var timesUI = {
        target: "#put-ticket-list",
        beginTimeKey: "playstime",
        endTimeKey: "playetime",

        init: function (jq) {
            this.target = jq || this.target;
            this.main = $(this.target);

            this.validRules();

            this.appendRow();

            var me = this;
            this.main.on("click", ".timesui-row-del", function (e) {
                me.delRow(e)
            });
            this.main.on("click", ".timesui-row-add", function () {
                me.addRow()
            });
        },


        delRow: function (e) {
            var evtObj = e.target;
            $(evtObj).parents(".timesui-row").remove();
        },

        addRow: function () {
            this.appendRow();
            this.disableValidation();
        },

        clearRows: function () {
            this.main.find(".timesui-row").remove();
        },

        setIsShow: function () {
            this.disableValidation();
            this.main.find(".easyui-timespinner").timespinner('readonly');
            this.main.find(".timesui-row-del,.timesui-row-add").hide();
        },

        getValue: function () {
            var rest = [];
            var me = this;
            this.main.find(".timesui-row").each(function () {
                var timestart = $(this).find(".easyui-timespinner:eq(0)").timespinner("getValue");
                var timeend = $(this).find(".easyui-timespinner:eq(1)").timespinner("getValue");
                var item = {};
                item[me.beginTimeKey] = timestart;
                item[me.endTimeKey] = timeend;
                rest.push(item);
            });
            return rest;
        },

        setValue: function (times) {
            this.clearRows();
            if (!times || !$.isArray(times) || times.length == 0) {
                this.appendRow();
            }
            for (var key in times) {
                var item = times[key];
                var timestart = item[this.beginTimeKey];
                var timeend = item[this.endTimeKey];
                this.appendRow(timestart, timeend);
            }
        },

        appendRow: function (timestart, timeend) {
            var row = '<div class="timesui-row" style="margin-bottom: 10px">' +
                '<input class="easyui-timespinner" /> 至 <input class="easyui-timespinner"/>' +
                '</div>';
            this.main.append(row);
            var jrow = this.main.find(".timesui-row:last");
            jrow.find(".easyui-timespinner").timespinner({
                required: true,
                validType: ['TimesUI_gtpretime']
            });

            var rowidx = this.main.find(".timesui-row").index(jrow);
            if (rowidx > 0) {
                jrow.append('<a class="timesui-row-del">删除</a>');
            }
            jrow.append('<a class="timesui-row-add" style="margin-left: 5px;">添加</a>');
            if (timestart) {
                jrow.find(".easyui-timespinner:eq(0)").timespinner("setValue", timestart);
            }
            if (timeend) {
                jrow.find(".easyui-timespinner:eq(1)").timespinner("setValue", timeend);
            }
        },

        enableValidation: function () {
            this.main.find(".easyui-timespinner").timespinner('enableValidation');
        },

        disableValidation: function () {
            this.main.find(".easyui-timespinner").timespinner('disableValidation');
        },

        validRules: function () {
            var me = this;

            function date2number(date) {
                var times = date.split(/\D+/);
                if (times.length < 2) {
                    return false;
                }
                var nowDate = new Date();
                return new Date(nowDate.getYear(), nowDate.getMonth(), nowDate.getDay(), times[0], times[1], 0)
            }

            $.extend($.fn.validatebox.defaults.rules, {
                TimesUI_gtpretime: {
                    validator: function (value, param) {
                        var optInput = $(this).parents("span").prev(".easyui-timespinner");
                        var optIdx = $('.easyui-timespinner', me.main).index(optInput);
                        if (optIdx == 0) {
                            return true;
                        }
                        var prevIdx = optIdx - 1;
                        var prevInput = $('.easyui-timespinner:eq(' + prevIdx + ')', me.main);
                        var prevValue = prevInput.timespinner("getValue");

                        var prevDate = date2number(prevValue);
                        var optDate = date2number(value);
                        if (!prevDate || !optDate) {
                            return true;
                        }
                        return prevDate < optDate;
                    },
                    message: '必须大需前一个时间'
                }
            });
        }
    };
</script>
<!-- script END -->
</body>
</html>