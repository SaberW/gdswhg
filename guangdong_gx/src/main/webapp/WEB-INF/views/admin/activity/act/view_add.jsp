<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<% String path = request.getContextPath();%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>活动管理-发布活动</title>
	<%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
	<link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
	<link rel="stylesheet" href="${basePath}/static/admin/Font-Awesome/css/font-awesome.min.css"/>
	<link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>

	<script type="text/javascript" charset="utf-8"	src="<%=path%>/static/ueditor/ueditor.config.js"></script>
	<script type="text/javascript" charset="utf-8"	src="<%=path%>/static/ueditor/ueditor.all.min.js"></script>
	<script type="text/javascript" charset="utf-8"	src="<%=path%>/static/ueditor/lang/zh-cn/zh-cn.js"></script>

	<script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
	<script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
	<script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
	<script type="text/javascript" src="${basePath}/static/plupload/upload-file.js"></script>
    <script type="text/javascript" src="${basePath}/static/common/js/whg.maps.js"></script>
    <script src="${basePath}/static/admin/js/whgmodule-venseatmaps.js"></script>

	<script type="text/javascript" src="${basePath}/static/common/js/area.js"></script>
    <script>
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
                    var sdVal = $('#' + param[0]).datetimebox('getValue');
                    var d1 = WhgComm.parseDateTime(sdVal);//$.fn.datebox.defaults.parser(sdVal);
                    var d2 = WhgComm.parseDate(value);//$.fn.datebox.defaults.parser(value);
                    //return d2.getTime() > d1.getTime();
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
    </script>
</head>
<body>

	<input id="supplement" type="hidden" value="0">
	<form id="whgff" method="post" class="whgff">
		<input id="tempstorage" name="tempstorage" type="hidden" value="0">
		<h2>活动管理</h2>
		
		<div class="whgff-row">
			<div class="whgff-row-label"><i>*</i>活动名称：</div>
			<div class="whgff-row-input">
				<input class="easyui-textbox" name="name"
					style="width: 500px; height: 32px" data-options="required:true,validType:['length[1,100]']" />
			</div>
		</div>
		
		<div class="whgff-row">
		<div class="whgff-row-label">活动简介：</div>
		<div class="whgff-row-input">
			<input class="easyui-textbox" name="actsummary"  style="width:500px; height:32px" data-options="required:false,validType:['length[1,200]']"></div>
		</div>
	
		<div class="whgff-row">
			<div class="whgff-row-label">
				<label style="color: red">*</label>上传封面：
			</div>
			<div class="whgff-row-input">
	            <input type="hidden" id="act_imgurl1" name="imgurl" data-options="required:true">
	            <div class="whgff-row-input-imgview" id="previewImg1"></div>
	            <div class="whgff-row-input-imgfile" id="imgUrl_pic">
	                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
	                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
	            </div>
	        </div>
		</div>
		
		 <div class="whgff-row">
			 <div class="whgff-row-label">
				<label style="color: red">*</label>区域：
			 </div>
			 <%--<div class="whgff-row-input">
	            <div class="radio radio-primary whg-js-data"  name="areaid"  js-data="WhgComm.getAreaType"></div>
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
				<input class="easyui-textbox" name="host"
					style="width: 500px; height: 32px" data-options="required:false,validType:['length[0,100]']" />
			</div>
		</div>
		<div class="whgff-row">
			<div class="whgff-row-label">承办单位：</div>
			<div class="whgff-row-input">
				<input class="easyui-textbox" name="organizer"
					style="width: 500px; height: 32px" data-options="required:false,validType:['length[0,100]']" />
			</div>
		</div>
		
		<div class="whgff-row">
			<div class="whgff-row-label">协办单位：</div>
			<div class="whgff-row-input">
				<input class="easyui-textbox" name="coorganizer"
					style="width: 500px; height: 32px" data-options="required:false,validType:['length[0,100]']" />
			</div>
		</div>
			
		<div class="whgff-row">	
			<div class="whgff-row-label">演出单位：</div>
			<div class="whgff-row-input">
				<input class="easyui-textbox" name="performed"
					style="width: 500px; height: 32px" data-options="required:false,validType:['length[0,100]']" />
			</div>
		</div>
        <div class="whgff-row">
            <div class="whgff-row-label"><i>*</i>联系人：</div>
            <div class="whgff-row-input"><input class="easyui-textbox" name="contacts" style="width:500px; height:32px" data-options="prompt:'请输入联系人',required:true, validType:['length[2,20]']"></div>
        </div>
        <div class="whgff-row">
            <div class="whgff-row-label"><i>*</i>固定电话：</div>
            <div class="whgff-row-input"><input class="easyui-textbox" name="telephone" style="width:500px; height:32px" data-options="prompt:'请输入固定电话', required:true, validType:'isTel'"></div>
        </div>
		
		<div class="whgff-row">
			<div class="whgff-row-label">主讲人：</div>
			<div class="whgff-row-input">
				<input class="easyui-textbox" name="speaker"
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
			<div class="whgff-row-label">
				<label style="color: red">*</label>活动分类：
			</div>
			<div class="whgff-row-input" data-check="true" target="etype" err-msg="请至少选择一个分类">
				<div class="checkbox checkbox-primary" id="etype" name="etype"></div>
			</div>
		</div>
	
		<div class="whgff-row">
			<div class="whgff-row-label"><i>*</i>艺术分类：</div>
			<div class="whgff-row-input" data-check="true" target="arttype" err-msg="请至少选择一个艺术分类">
				<div class="checkbox checkbox-primary" id="arttype" name="arttype"></div>
	       	</div>
		</div>
		</div>
        <div class="whgff-row">
			<div class="whgff-row-label">标签：</div>
            <div class="whgff-row-input" data-check="false" target="etag" err-msg="请至少选择一个标签">
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
			<div class="whgff-row-label">
				<label style="color: red">*</label>活动报名时间：
			</div>
			<div class="whgff-row-input">
				<input type="text" class="easyui-datetimebox enterstrtime"style="width: 240px; height: 32px;" id="enterstrtime" name="enterstrtime" required="required" data-options="editable:false,required:true,prompt:'请选择'"/> ~
				<input type="text" class="easyui-datetimebox enterendtime"style="width: 240px; height: 32px;" id="enterendtime" name="enterendtime" required="required" data-options="editable:false,required:true,prompt:'请选择',validType:'bmEndTime[\'enterstrtime\']'" />
			</div>
		</div>

		<div class="whgff-row">
			<div class="whgff-row-label">
				<label style="color: red">*</label>活动日期：
			</div>
			<div class="whgff-row-input">
				<input type="text" class="easyui-datebox" style="width: 240px; height: 32px;" id="starttime"
					   name="starttime" required="required"
					   data-options="editable:false,required:true,prompt:'请选择',validType:'bmStartDate[\'enterendtime\']'"/>
				~
				<input type="text" class="easyui-datebox"style="width: 240px; height: 32px;" id="endtime" name="endtime" required="required" data-options="editable:false,required:true,prompt:'请选择',validType:'hdEndDate[\'starttime\']'" />
			</div>
		</div>

		<div class="whgff-row">
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
			<div class="whgff-row-label">
				<label style="color: red">*</label>地址：
			</div>
			<div class="whgff-row-input">
				<input class="easyui-textbox" name="address" id="address" style="width: 500px; height: 32px" data-options="required:true,validType:['length[0,100]']">
			</div>
		</div>
		<div class="whgff-row">
			<div class="whgff-row-label"><label style="color: red">*</label>坐标：
			</div>
			<div class="whgff-row-input">
				<input class="easyui-numberbox" name="actlon" id="actlon" style="width: 150px; height: 32px" data-options="required:true, precision:6,readonly:true,prompt:'X轴'"> 
				<input class="easyui-numberbox" name="actlat" id="actlat" style="width: 150px; height: 32px" data-options="required:true, precision:6,readonly:true,prompt:'Y轴'"> 
				<a class="easyui-linkbutton whg-maps" map-addr="#address" vm-x="actlon" vm-y="actlat" id="getXYPointBtn" text="选择坐标"></a>
			</div>
		</div>
	
		<div class="whgff-row">
			<div class="whgff-row-label"><label style="color: red">*</label>联系电话：</div>
			<div class="whgff-row-input">
				<input class="easyui-textbox" name="telphone" style="width: 500px; height: 32px" data-options="prompt:'请输入联系人手机', required:true, validType:'isPhone'">
			</div>
		</div>

		<div class="whgff-row" style="display: none">
			<div class="whgff-row-label">
				<label style="color: red">*</label>是否消耗积分：
			</div>
			<div class="whgff-row-input">
		    	<div class="radio radio-primary whg-js-data" name="integral" id="integral"  onclick="isIntegral()" 
                 js-data='[{"id":"1","text":"默认"},{"id":"2","text":"积分"}]'>
                 <%--<input id="sellticketSelection" type="hidden" value="1">--%>
            </div>
             <span id="integralCount" style="display:none">该活动需要消耗积分<input class="easyui-numberspinner" name="integralnum" value="50" id="integralnum" style="width: 50px; height: 25px" data-options="required:false,min:0,max:999">分 </span> 
			</div>
		</div>

		<div class="whgff-row" style="display: none">
			<div class="whgff-row-label">
				<label style="color: red">*</label>是否收费：
			</div>
			<div class="whgff-row-input">
		    	<div class="radio radio-primary whg-js-data" name="hasfees" id="hasfees"  
                 js-data='[{"id":"0","text":"否"},{"id":"1","text":"是"}]'>
            </div>
			</div>
		</div>

        <div class="whgff-row">
            <div class="whgff-row-label"><label style="color: red">*</label>实名制：</div>
            <div class="whgff-row-input">
                <div class="radio radio-primary whg-js-data" name="isrealname" js-data='[{"id":"0","text":"否"},{"id":"1","text":"是"}]'></div>
            </div>
        </div>

		<div class="whgff-row">
			<div class="whgff-row-label"><i>*</i>提醒方式：
			</div>
			<div class="whgff-row-input">
				<div class="checkbox checkbox-primary whg-js-data" name="noticetype" id="noticetype"
					 js-data='[{"id":"SMS","text":"短信"},{"id":"ZNX","text":"站内信"}]'>
				</div>
			</div>
		</div>
		
		<div class="whgff-row">
			<div class="whgff-row-label">
				<label style="color: red">*</label>在线售票：
			</div>
			<div class="whgff-row-input">
		    	<div class="radio radio-primary whg-js-data" name="sellticket"  id="sellticket"  onclick="isCheck()"
                 js-data='[{"id":"1","text":"不可预定"},{"id":"2","text":"自由入座"},{"id":"3","text":"在线选座"}]'>
					<input id="sellticketSelection" type="hidden" value="1">
				</div>
				<span id="maxBuySeatCount" style="display:none">每人每场最多购票<input class="easyui-numberspinner" name="seats"
																			   value="3" id="seats"
																			   style="width: 80px; height: 25px"
																			   data-options="required:false,min:0,max:99">张 </span>
				<span id="maxSoldSeatCount" style="display:none">每场次最多售票<input class="easyui-numberspinner" name="ticketnum" value="1000" id="ticketnum"  style="width:100px; height: 25px" data-options="required:false,min:0,max:999999">张 </span>
			</div>
		</div>

        <div class="whgff-row acturl">
            <div class="whgff-row-label">跳转链接：</div>
            <div class="whgff-row-input">
                <input class="easyui-textbox" name="acturl" style="width:600px; height:32px" data-options="required:false,validType:'url',prompt:'请输入连接地址,如：http://yoursite.com/'">
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
				<div id="remark" name="remark"  type="text/plain" style="width: 800px; height: 600px;"></div>
			</div>
		</div>
		
		<div class="whgff-row">
			<div class="whgff-row-label">上传附件：</div>
			<div class="whgff-row-input">
				<input id="act_filepath1" name="filepath" data-options="required:true" style="width:600px;height:32px;" readonly="readonly"/>
				<!-- <input id="filepath"  class="easyui-filebox" name="filepath" style="width:800px;height:32px;" data-options="buttonText:'选择文件', accept:'xls,doc,txt,pdf,jpg,png'"> -->
				<div class="whgff-row-input" id="filepath">
	                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="fileUploadBtn1">选择文件</a></i>
	                <i>附件格式为doc,docx,xls,zip,xlsx，建议文件大小为10MB以内</i>
	            </div>
			</div>
		</div>
	</form>
	
	<div class="whgff-but" style="width: 400px; margin: 10px auto;">
		<a class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok" id="whgwin-add-btn-save">提 交</a>
		<a class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok" title="暂时保存数据，可下次编辑提交"
		   id="whgwin-add-btn-storage">保存草稿</a>
		<a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo"
		   onclick="WhgComm.editDialogClose()">返 回</a>
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
		changeCity(newVal, oldVal, function(){
            if(typeof(window.__init_area) == 'undefined'){
                $('#__AREA_ELE').combobox('setValue', '${act.area}');
                window.__init_area = true;
            }
        });
	}  //省市区------END



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
	  //根据地址取坐标
    WhgMap.init({basePath:'${basePath}', addrFieldId:'address', xpointFieldId:'actlon', ypointFieldId:'actlat', getPointBtnId:'getXYPointBtn'});
	  

	//图片初始化
    var whgImg = WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'act_imgurl1', previewImgId: 'previewImg1'});
	//文件上传初始化
    var whgFile = WhgUploadFile.init({basePath: '${basePath}', uploadBtnId: 'fileUploadBtn1', hiddenFieldId: 'act_filepath1'});
	
	
	$(function() {
        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'',
            deptEid:'deptid', deptValue:'',
            ywiWhppEid:'ebrand', ywiWhppValue:'',
            ywiArtTypeEid:'arttype', ywiArtTypeValue:'',
            ywiTypeEid:'etype', ywiTypeValue:'', ywiTypeClass:4,
            ywiKeyEid:'ekey', ywiKeyValue:'', ywiKeyClass:4,
            ywiTagEid:'etag', ywiTagValue:'', ywiTagClass:4,
            venEid:'venueid', venValue:false,
            roomEid:'roomid', roomValue:false
        });

        timesUI.init();
		$('#province').combobox('setValue', province);
		
		var seatmap = $("div.whgff-row-map");

		//清空
		 $("#whgff-but-clear").on('click', function(){
			$('#whgff').form('clear');
	        whgImg.clear();
	        //第一个单选又点上
	        $('#whgff').find("div.radio").find(':radio:eq(0)').click();
	        remark.setContent('');
	    });

		//提交事件
		function submitFun() {
            if(!supplement()){
                return;
            }
            if(!checkTime()){
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

        //暂存事件
        function storageFun() {
            $("#tempstorage").val("1");//暂存
            $('#whgff').submit();
        }

		//初始化表单
		$('#whgff').form({
			novalidate : true,
          /*  async: false,
            cache: false,*/
			url : getFullUrl('/admin/activity/add'),
			onSubmit : function(param) {
				//坐位相关值处理 START
                var seatsValue = seatmap.whgVenseatmaps('getValue');
                //坐位相关值处理End
                var _valid = $(this).form('enableValidation').form('validate');
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
                var list = $("#put-ticket-list").children("div.activityTimeLabel1");
                var activityTimeList = [];
				/*  var bool = false;
                var bookdate = $("#starttime").datebox("getValue");//开始日期
                var bookenddate = $("#enterendtime").datetimebox("getValue");//预订结束日期
                var d1 = WhgComm.parseDateTime(bookenddate);//$.fn.datebox.defaults.parser(sdVal);
                var _valid2 = false;
                $.each(list,function(index,value){
                    var playstrtime = $(value).find("input[name='playstrtime']").val();
                    var playendtime = $(value).find("input[name='playendtime']").val();
                    var item = {};
                    var d2 = WhgComm.parseDateTime(bookdate + " " + playstrtime);
                    if (d2.getTime() <= d1.getTime()) {//预订结束时间大于活动开始时间
                        _valid2 = true;
                    }
                    item.playstrtime = playstrtime;
					item.playendtime = playendtime;
                    activityTimeList.push(item);
				});
				 param.activityTimeList = JSON.stringify(activityTimeList);
				 */
                var times = timesUI.getValue();
                param.activityTimeList = JSON.stringify(times);
				/*if (_valid2) {
                    _valid = false;
				 }*/
                var isstorage = $("#tempstorage").val();
                if (isstorage == "1") {//暂存，放过所有验证
                    _valid = true;
                    var _dept = $("#deptid").combobox('getValue');
                    if (_dept == "") {
                        $.messager.alert("提示", '所属部门不能为空', 'error');
                        _valid = false;
                    }
                }
                if (_valid == false && _valid2 == true) {
                    $.messager.alert("提示", '活动开始时间必须大于预订结束时间', 'error');
                }
				if (_valid) {
					$.messager.progress();
				} else {
					//失败时再注册提交事件
					$('#whgwin-add-btn-save').off('click').one('click', submitFun);
				}
				return _valid;
			},
            success : function(data) {
                $.messager.progress('close');
                var Json = eval('(' + data + ')');
                if (Json && Json.success == '1') {
                    var isstorage = $("#tempstorage").val();
                    // var msg = '活动信息提交成功';
                    // if (isstorage == "1") {//暂存，放过所有验证
                    //     msg = '活动信息保存成功';
                    // }
					var msg = "操作成功"
                    $.messager.alert("提示", msg, 'info',
                        function () {
                            window.parent.$('#whgdg').datagrid('reload');
                            WhgComm.editDialogClose();
                        }
                    );
                } else {
                    $.messager.alert('提示', '操作失败:'+Json.errormsg+'！', 'error');
                    $('#whgwin-add-btn-save').off('click').one('click', submitFun);
                }
            }
		});

        function supplement() {
            //验证区域
            /*var areaid = $("input[name='areaid']:checked").val();
            if(null == areaid || "" == areaid){
                $.messager.alert("错误", '请选择区域', 'error');
                return false;
            }*/

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

			/* //验证标签
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

		//注册提交事件
		$('#whgwin-add-btn-save').on('click', submitFun);
        //注册暂存事件
        $('#whgwin-add-btn-storage').on('click', storageFun);

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
		
        $('#add').click(function(){
            var $div = $("<div class='ticket-item activityTimeLabel1' style='margin-top: 5px;'>");
            var $playstrtime = $("<input class='easyui-timespinner' style='width: 100px; height: 32px;' value='08:00' name='playstrtime'>");
            var $playendtime = $("<input class='easyui-timespinner' style='width: 100px; height: 32px;' value='10:00' name='playendtime'>");
            var $add = $("<a href='javascript:void(0)' id='cancel' style='margin-left:7px; '></a>");
            $add.html("取消");
            $add.on("click",function(){
                $(this).parent("div.activityTimeLabel1").remove();
            });
            $div.append($playstrtime);
            $div.append("- ");
            $div.append($playendtime);
            $div.append($add);
            $div.append("<label style='color: red;margin-left: 10px;display: none'>时间段填写不正确，不能与前时间段重合</label>");
            $div.appendTo("#put-ticket-list");
            $.parser.parse("#put-ticket-list");
        });
    });
	
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
	
	//是否在线售票
	function isCheck(){
		var val=$('input:radio[name="sellticket"]:checked').val();
		var valueBeforeSelect = $("#sellticketSelection").val();
		if(1 == val){
            $(".acturl").show();
			$("#maxBuySeatCount").hide();
			$("#maxSoldSeatCount").hide();
            $("#onLineSeat").hide();
            $("#sellticketSelection").val(val);
		}
		if(2 == val){
            $(".acturl").hide();
            $("#maxBuySeatCount").show();
            $("#maxSoldSeatCount").show();
            $("#onLineSeat").hide();
            $("#sellticketSelection").val(val);
		}
		if(3 == val){
            $(".acturl").hide();
            if(rowNum == 0 || rowNum == undefined){
            	$.messager.alert("错误", '请选择有座位的活动室!', 'error');
                $("input:radio[name='sellticket'][value='" + valueBeforeSelect + "']").prop("checked", "checked");
                return;
            }
            $("#maxBuySeatCount").show();
            $("#maxSoldSeatCount").hide();
            $('.test-seatmaps').whgVenseatmaps({type: 'use', itemDbClick: false})
                .whgVenseatmaps('setSeatSize', rowNum,colNum)
                .whgVenseatmaps('setValue', JSON.parse(seatjson));//itemDbClick false 屏蔽双击 改选座位名称
            $("#onLineSeat").show();
            $("#sellticketSelection").val(val);
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
	

    function checkTime() {
        var timeList = $("#put-ticket-list").children("div.activityTimeLabel1");
        var prePlaystrtime = false;
        for(var i = 0;i < timeList.length;i++){
            var item = timeList[i];
            var playstrtime = $(item).find("input[name='playstrtime']").val();
            var playendtime = $(item).find("input[name='playendtime']").val();
            if(2 == compareTime(playstrtime,playendtime,prePlaystrtime)){
                $(item).find("label").hide();
            }else{
                $(item).find("label").show();
                return false;
            }
            prePlaystrtime = playendtime;//记录前一个结束时间
        }
        return true;
    }

	function roomSelected(rec){
		//活动室onSelect事件时，隐藏在线选座
		$("#onLineSeat").css("display","none");
		rowNum = rec.seatrows;
		colNum = rec.seatcols;
		seatjson = rec.seatjson;
		console.info(seatjson);
	}

    function compareTime(time1,time2, preTime1) {
	    if(!preTime1){
            preTime1 = time1;
        }
        var time10 = "2010-01-01 " + preTime1 + ":00";
        var time11 = "2010-01-01 " + time1 + ":30";
        var time22 = "2010-01-01 " + time2 + ":30";
        var date0 = WhgComm.parseDateTime(time10);//new Date(time11);
        var date1 = WhgComm.parseDateTime(time11);//new Date(time11);
        var date2 = WhgComm.parseDateTime(time22);//new Date(time22);
        if(date2 > date1 && date1 > date0){
            return 2;
        }else{
            return 1;
        }
    }
    
    //场馆改变，将场馆所对应的坐标与地址带出来
    function changeVen(){
		/* var venueid = $("#venueid").val(); */
		var venueid = $('#venueid').combobox('getValue'); 
		if(venueid !=null && venueid != ''){
			var url = getFullUrl('/admin/venue/changeVen?venueId='+venueid);
			$.ajax({  
		           url: url,  
		           type: "POST",  
		           success : function(data){  
		        	   if (!data) return;
		        	   $("#address").textbox('setValue', data.address);
		        	   $('#actlon').numberspinner('setValue', data.longitude);  
		        	   $('#actlat').numberspinner('setValue', data.latitude);
		           }, error : function() {
		                 alert("error");  
		           }  
		        });
		}
    }

</script>

</body>
</html>
