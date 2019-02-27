<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<% request.setAttribute("now", new Date()); %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<%@include file="/pages/comm/agdhead.jsp"%>
<title class="page-title">活动详情</title>
<link href="${basePath }/static/assets/css/activity/activityDetaiil.css" rel="stylesheet">
<link href="${basePath }/static/assets/js/plugins/oiplayer-master/css/oiplayer.css" rel="stylesheet">

<script src="${basePath }/static/assets/js/plugins/sidebar/stickySidebar.js"></script>
<script src="${basePath }/static/assets/js/activity/activityDetail.js"></script>
<script src="${basePath }/static/assets/js/plugins/oiplayer-master/plugins/flowplayer-3.2.6.min.js"></script>
<script src="${basePath }/static/assets/js/plugins/oiplayer-master/js/jquery.oiplayer.js"></script>


<script type="text/javascript">

	function showActDetail(data) {
	    var model =data.data.actdetail;
	    var statemdfdate=moment(model.statemdfdate).format('YYYY-MM-DD');
	    var endtime=moment(model.endtime).format('YYYY-MM-DD');
	    var starttime=moment(model.starttime).format('YYYY-MM-DD');
        var liststate = data.data.liststate;
        var acturl = model.acturl;
        var remark = model.remark;
        var name = model.name;
        var imgPath = data.imgPath+WhgComm.getImg750_500(model.imgurl);
        $("#showRemark").html(remark);
        $("#showName").html(name);
        $("#showImg").attr("src",imgPath);
        $("title.page-title").text(name);
     var actHtml="";
        actHtml+="<h1>"+name+"</h1>";
        actHtml+=' <div class="detail">';
        actHtml+='<div class="time"><i class="public-s-ico s-ico-8"></i>发布时间：<span>'+statemdfdate+'</span></div>';
        actHtml+='<div class="time1 clearfix"><i class="public-s-ico s-ico-9"></i><span>活动时间：</span>'+starttime;
        actHtml+='~'+endtime+'</div>';
        actHtml+='<div class="tel"><i class="public-s-ico s-ico-17"></i>活动电话：<span>'+model.telphone+'</span></div>';
        actHtml+='<input type=hidden name="sellticket" id="sellticket" value="'+model.sellticket+'"> </div>';
        actHtml+='<div class="detail detail1"><div class="time"><i class="public-s-ico s-ico-19"></i>活动标签：';
        actHtml+='  <span id="showTag">';
        actHtml+='</span>';
        actHtml+='</div></div>';
        actHtml+='  <div class="goNext" id="goNext">';
        if(liststate == 1){
            actHtml+=' <a  href="${apiWbgx}${apiSite}/hd/detail?visitType=1&id='+model.id+'&phoneNum=${wbgx_sessUserKey.mobile}">立即报名</a>';
        }else if(liststate == 2){
            actHtml+='   <a href="javascript:void(0)" style="background-color: #333">已预订完</a>';
        }else if(liststate == 3){
            actHtml+='   <a href="'+acturl+'"  >查看链接</a>';
        }else if(liststate == 4){
            actHtml+='   <a href="javascript:void(0)" style="background-color: #333">报名已结束</a>';
         }else{
            actHtml+='   <a href="javascript:void(0)" style="background-color: #333">报名未开始</a>';
        }
        actHtml+=" </div>";
        $("#actDetailDiv").append(actHtml);
        var str=model.etag;
        var strs=str.split(",");
        var shtml='';
        for (var i in strs) {
            var tagName = strs[i];
            shtml+='<span class="label"><a href="javascript:void (0)"><em class="js-fmt-text" js-fn="FMTActivityTag" js-val="'+tagName+'">'+tagName+'</em></a></span>';
        }
        $("#showTag").append(shtml);
        var actList=data.data.acttj;
        var tjHtml="";
        if(actList.length&&actList.length>0){
            tjHtml+='<h2>推荐活动</h2>';
            for(var i in actList){
				var model=actList[i];
                if(i<4){
					tjHtml+='<div class="item clearfix">';
					tjHtml+='<div class="right-img">';
					tjHtml+='<a href="${basePath }/agdwhhd/activityinfo?actvid='+model.id+'"><img src="'+data.imgPath+model.imgurl+'" width="130" height="90" onerror=showDefaultIMG(this, "${basePath }/static/assets/img/img_demo/1.jpg")></a>';
					tjHtml+='</div><div class="right-detail">';
					tjHtml+='<a href="${basePath }/agdwhhd/activityinfo?actvid='+model.id+'"><h3>'+model.name+'</h3></a>';
					tjHtml+='<p class="time">'+moment(model.starttime).format('YYYY-MM-DD')+'</p>';
					tjHtml+='</div></div>';
                }
			}
			$("#showTjList").append(tjHtml);
        }else{
            $("#showTjList").hide();
        }
    }

    function showResource(data) {
        var resourceHtml1="";
        var resourceHtml2="";
        var resourceHtml3="";
        var resourceHtml4="";
        var mview = $("#showResouceDiv");
        var imgtemp = '<a href="javascript:void(0)" >' +
            '    <div class="img1">' +
            '        <img  width="252" height="170">' +
            '        <span></span>' +
            '    </div>' +
            '</a>';
        var vediotemp = '<a href="javascript:void(0)" >' +
            '    <div class="mask"></div>' +
            '    <div class="video1">' +
            '        <img width="252" height="150">' +
            '        <span></span>' +
            '    </div>' +
            '</a>';
        var rediotemp = '<a href="javascript:void(0)" >' +
            '    <div class="mask"></div>' +
            '    <div class="mp31">' +
            '        <span></span>' +
            '    </div>' +
            '</a>';
        var filetemp = '<li>' +
            '    <a class="js-urlencode"><i></i></a>' +
            '</li>';
        var img_i=0;
        var vedio_i=0;
        var audio_i=0;
		for (var i in data.data) {
			var module = data.data[i];
            var enttype=module.enttype;
            switch (enttype){
                case "1" : //图片
                    resourceHtml1="1";
                    var imgView = mview.find(".js-mv-res-img").children("div");
                    var item = $(imgtemp);
                    imgView.append(item);
                    if ((img_i+1)%3 == 0){
                        item.addClass("last");
                    }
                    var enturl = /^http/.test(module.enturl)?module.enturl: data.imgPath+module.enturl;
                    item.attr("onClick", 'show_img(this,{url:"'+enturl+'"})');
                    item.find("img").attr("src", WhgComm.getImg750_500(enturl));
                    item.find("span").text(module.name);
                    img_i++;
                    break;
                case "2" :  //视频
                    resourceHtml2="1";
                    var imgView = mview.find(".js-mv-res-video").children("div");
                    var item = $(vediotemp);
                    imgView.append(item);
                    if ((vedio_i+1)%3 == 0){
                        item.addClass("last");
                    }
                    var enturl = /^http/.test(module.enturl)?module.enturl: data.imgPath+module.enturl;
                    var deourl = /^http/.test(module.deourl)?module.deourl: data.imgPath+module.deourl;
                    item.attr("onClick", 'show_vedio(this,{url:"'+enturl+'"})');
                    item.find("img").attr("src", WhgComm.getImg750_500(deourl));
                    item.find("span").text(module.name);
                    vedio_i++;
                    break;
                case "3" :  //音频
                    resourceHtml3="1";
                    var imgView = mview.find(".js-mv-res-audio").children("div");
                    var item = $(rediotemp);
                    imgView.append(item);
                    if ((audio_i+1)%3 == 0){
                        item.addClass("last");
                    }
                    var enturl = /^http/.test(module.enturl)?module.enturl: data.imgPath+module.enturl;
                    item.attr("onClick", 'show_vedio(this,{url:"'+enturl+'"})');
                    item.find("span").text(module.name);
                    audio_i++;
                    break;
                case "4" : //文档
                    resourceHtml4="1";
                    var imgView = mview.find(".js-mv-res-file").children("div");
                    var item = $(filetemp);
                    imgView.append(item);
                    var enturl = /^http/.test(module.enturl)?module.enturl: data.imgPath+module.enturl;
                    item.find("a").attr("href", '${basePath}/whtools/downFile?filePath='+enturl);
                    item.find("a").html('<i></i>'+module.name);
                    break;
			}
		}
		if(resourceHtml1!=""){
            $("#showResouce").append("<li >活动图片</li>");
        }else{
            mview.find(".js-mv-res-img").remove();
        }
        if(resourceHtml2!=""){
            $("#showResouce").append("<li >活动视频</li>");
        }else{
            mview.find(".js-mv-res-video").remove();
        }
        if(resourceHtml3!=""){
            $("#showResouce").append("<li >活动音频</li>");
        }else {
            mview.find(".js-mv-res-audio").remove();
        }
        if(resourceHtml4!=""){
            $("#showResouce").append("<li>活动文档</li>");
        }else {
            mview.find(".js-mv-res-file").remove();
        }
        $(".tab li").click(function() {
            $(this).addClass("active").siblings().removeClass('active');
            $(".list1").eq($(".tab li").index(this)).addClass("on").siblings().removeClass('on');
        })
        $("ul.tab li:eq(0)").click();
        $("a.js-urlencode").each(function(){
            var _href = $(this).attr("href");
            _href = encodeURI(_href);
            $(this).attr("href", _href);
        })
    }

    function showRelationzx(data){
        var zxHtml='';
	    var zx=data.data;
		if(zx.length>0){
			zxHtml+='<h2>相关咨讯</h2>';
			zxHtml+=' <ul>';
			for(var i in zx){
				var model=zx[i];
				zxHtml+='<li><a href="${basePath }/agdwhhd/newsinfo?id='+model.clnfid+'">'+model.clnftltle+'</a><p class="time">'+moment(model.clnfcrttime).format('YYYY-MM-DD')+'</p></li>';
			}
			zxHtml+=' </ul>';
			$("#relationzx").append(zxHtml);
	   }else{
            $("#relationzx").hide();
        }
	}

$(function(){
    $.ajax({
        type: 'post',
        url: '${apiRoot}/api/outer/activity/detail',
        data: {
            cultsite:'${apiSite}',
            actId:'${actId}'
        },
        success: function(data){
            if (data&&data.code==0) {
                showActDetail(data);
            }
        }
    });
    $.ajax({
        type: 'post',
        url: '${apiRoot}/api/outer/comm/getResources',
        data: {
            cultsite:'${apiSite}',
            reftype:'4',
            refid:'${actId}'
        },
        success: function(data){
            if (data&&data.code==0) {
                showResource(data);
            }
        }
    });
    $.ajax({
        url: '${apiRoot}/api/outer/comm/getRelationZx',
        data: {
            id:'${actId}',
            type:'2016111900000018'
        },
        success: function(data){
            if (data&&data.code==0) {
                showRelationzx(data);
            }
        }
    })
})

/*

	$(".source li:eq(0)").addClass("active");
	var _html = $(".source li:eq(0)").html();
	$(".sourceinfo").children("div:eq(0)").addClass("on");
	
	if($(".nowlist:eq(0)").size() == 0){
		$(".actvitm:eq(0)").click();
	}else{
		$(".nowlist:eq(0)").click();
	}
	
	if($(".source li").size() == 0){
		$(".source").remove();
	}
	
	 $(".js-fmt-text").each(function () {
         var v = $(this).attr("js-val");
         var fn = $(this).attr('js-fn');
         var text = WhgComm[fn].call(WhgComm, v);
         $(this).text(text);
     })
     

  */



</script>

</head>
<body class="oiplayer-example">
<!-- 公共头部开始 -->
<%@include file="/pages/comm/agdtopsmall.jsp"%>
<!-- 公共头部结束-END -->

<!--公共主头部开始-->
<div id="header-fix">
	<div class="header-nav-bg">
		<div class="header-nav">
			<div class="logo-small">
				<a href="${basePath }"><img src="${basePath }/static/assets/img/public/logoSmall.png"></a>
			</div>
			<ul>
				<li class="active"><a href="${basePath }/agdwhhd/activitylist">活动预约</a></li>
				 <li><a href="${basePath }/agdwhhd/notice">活动公告</a></li>
				<li><a href="${basePath }/agdwhhd/news">活动资讯</a></li>
				<li class="last"><a href="${basePath }/agdwhhd/brandlist">品牌活动</a></li>
			</ul>
		</div>
	</div>
</div>
<!--公共主头部结束-END-->

<!--主体开始-->
<div class="special-bg">
    <div class="activity-main">
        <div class="public-crumbs">
            <span><a href="${basePath }">首页</a></span><span></span>
            <%--<span><a href="${basePath }/agdwhhd/index">文化活动</a></span><span>></span>--%>
            <span><a href="${basePath }/agdwhhd/activitylist">活动预约</a></span><span>></span>
            <span id="showName"></span>
        </div>
        <div class="special-head">
            <div class="special-head-left">
            	<img alt="" src="" id="showImg" style="width:380px;height: 240px">
            </div>
            <div class="special-head-right clearfix">
            
            <!-- 收藏 -->
            <div class="public-fav"><a reftyp="4" id="collection" refid="${actId }" class="shoucang"></a></div>
               
                <div class="head-father">
                    <div class="head-con on" id="actDetailDiv">

                    </div>
                </div>
            </div>
        </div>
        <div class="special-content clearfix">
            <div class="con-left clearfix">
            	<div class="public-info-step">
		        	<h3><span>活动详情</span></h3>
		            <div class="info" id="showRemark"></div>
					<!-- 下载 -->
					<%--<c:if test="${not empty loadlist}">
						<div class="file-download-cont">
							<ul>
								<c:forEach items="${loadlist}" var="cc" varStatus="s">
									<li>
										<a href="${basePath }/whtools/downFile?filePath=${cc}"><i></i>${whg:getFileName(cc)}</a>
									</li>
								</c:forEach>
							</ul>
						</div>
					</c:if>--%>
              	</div>
              	<!--分享 -->
              	<div class="public-share">
                     <span class="btn qq"><a href="javascript:void(0);" class="fxqq"></a></span>
                     <span class="btn weixin"><a a href="javascript:void(0);" class="fxweix"></a></span>
                     <span class="btn weibo"><a href="javascript:void(0)" class="fxweibo" target="_blank"></a></span>
                     <span class="btn dianzan">
                         <em>0</em>
                         <a href="javascript:void(0)" class="dianzan" reftyp="4" refid="${actId }" id="good"></a>
                     </span>
                 </div>
				    <div class="site clearfix sourceinfo" id="showResouceDiv">
                          <ul class="tab clearfix source" id="showResouce"></ul>
                        <div class="list1 on js-mv-res-img">
                            <div class="demo-list list-video clearfix">
                            </div>
                        </div>
                        <div class="list1 js-mv-res-video">
                            <div class="demo-list list-video clearfix">
                            </div>
                        </div>

                        <div class="list1 js-mv-res-audio">
                            <div class="demo-list list-mp3 clearfix">
                            </div>
                        </div>
                        <!-- 下载 -->
                        <div class="list1 js-mv-res-file">
                            <div class="file-download-cont">
                                <ul>
                                </ul>
                            </div>
                        </div>
                    </div>
<%--				<!-- 动态包含评论 -->
				<jsp:include page="/pages/comm/agdcomment.jsp" flush="true">
					<jsp:param name="reftype" value="1"/>
					<jsp:param name="refid" value="${actdetail.id }"/>
				</jsp:include>
				<!-- 动态包含评论-END -->--%>

            </div>
            <div class="public-right-main">
					<div class="public-other-notice" id="showTjList">
					</div>
                <div class="public-other-notice" style="margin-top: 20px;" id="relationzx">

                </div>
            </div>
        </div>
    </div>
</div>
<!--主体结束-->
<!--公共主底部开始-->
<%@include file="/pages/comm/agdfooter.jsp"%>
<!--公共主底部结束-END-->
</body>
<script>
$(function(){
	var sellticket = "${actdetail.sellticket }";
	if(sellticket == 1){
		$("#goNext").css("display","none");
	}
})

function checkLogin(){
	rongDialog({ type : false, title : "登录之后才能预定！", time : 3*1000 });
	$('#toLogin').attr('href','${basePath }/login');
}

function checkActState(){
	var actId = '${actdetail.id}';
	 $.ajax({
         url : '${basePath }/agdwhhd/checkActPublish',
         data : {actId: actId},
         dataType : "json",
         success : function(data){
			 //alert(data.errormsg);
             if (data.success == 1){
                 window.location.href = '${basePath }/agdwhhd/actBaoMing?actvid='+actId;
             }else {
				 if("101" == data.errormsg){
					 rongAlertDialog({ title: "提示信息", desc : "该活动需实名认证后才可报名，请先进行实名认证！", closeBtn : false, icoType : 1 }, function(){
						 window.location.href = basePath+"/center/safely-userReal";
					 });
				 }if("201" == data.errormsg){
					 rongAlertDialog({ title: "提示信息", desc : "您还未绑定手机，请先去绑定手机！", closeBtn : false, icoType : 1 }, function(){
						 window.location.href = basePath+"/center/safely-phone";
					 });
				 }else{
					 rongAlertDialog({ title: '提示信息', desc:data.errormsg, closeBtn: true, closeIco:false, icoType:3});
					 //rongDialog({ type : false, title : data.errormsg, time : 3*1000 });
				 }
             }
         }
     });
}
</script>
</html>

