<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<%@include file="/pages/comm/agdhead.jsp"%>
<title class="js-mv-text" mvkey="name"></title>
	<link href="${basePath}/static/assets/css/train/registrationInfo.css" rel="stylesheet">
	<script src="${basePath}/static/assets/js/train/registrationInfo.js"></script>
<link href="${basePath }/static/assets/css/train/teacher.css" rel="stylesheet">
</head>
<body>
<!-- 公共头部开始 -->
<%@include file="/pages/comm/agdtopsmall.jsp"%>
<!-- 公共头部结束-END -->

<!--公共主头部开始-->
<div id="header-fix">
    <div class="header-nav-bg">
    	<div class="header-nav">
        	<div class="logo-small">
            	<a href="${basePath }/home"><img src="${basePath }/static/assets/img/public/logoSmall.png"></a>
            </div>
            <ul>
            	<%--<li><a href="${basePath }/agdpxyz/index">培训驿站</a></li>--%>
            	<li><a href="${basePath }/agdpxyz/notice">培训公告</a></li>
                <li><a href="${basePath }/agdpxyz/news">培训资讯</a></li>
                <li class="active"><a href="${basePath }/agdpxyz/teacher">培训师资</a></li>
                <li><a href="${basePath }/agdpxyz/trainlist">在线报名</a></li>
                <li class="last"><a href="${basePath }/agdpxyz/vod">在线点播</a></li>
                <%-- <li class="last"><a href="${basePath }/agdpxyz/resources">培训资源库</a></li> --%>
            </ul>
        </div>
    </div>
</div>
<!--公共主头部开始-END-->
 
<!--面包屑开始-->
<div class="public-crumbs">
    <span><a href="${basePath }/home">首页</a></span><span>></span>
    <span><a href="${basePath }/agdpxyz/index">培训驿站</a></span><span>></span>
    <span><a href="${basePath }/agdpxyz/teacher">培训师资</a></span><span>></span>
    <span class="js-mv-text" mvkey="name"></span>
</div>
<!--面包屑结束-->

<!--主体开始-->
<div class="main-info-bg main-info-no-padding main-info-bgColorW">
	<div class="main-info-container clearfix">
  		<div class="teacherCont clearfix">
	    	<div class="img">
				<img class="js-mv-src" width="160" height="160">
	        </div>
	        <div class="info">
	        	<h1 class="js-mv-text" mvkey="name"></h1>

				<p mvkeynone="teachertype"><i class="public-s-ico s-ico-5"></i>专长 :
					<span class="js-mv-text" mvkey="teachertype"></span>
				</p>

				<%--<p mvkeynone="teacherdesc"><i class="public-s-ico s-ico-6"></i>课程说明 :
					<span class="js-mv-text" mvkey="teacherdesc"></span>
				</p>--%>
				<p mvkeynone="city"><i class="public-s-ico s-ico-10"></i>地区 :
					<span class="js-mv-text" mvkey="city"></span>
				</p>

	        </div>
    	</div>
    	
    	<div class="public-left-main">
	    	<div class="public-info-step">

				<h3 mvkeynone="teacherdesc"><span>讲师简介</span></h3>
				<div class="info js-mv-text" mvkey="teacherdesc" mvkeynone="teacherdesc"></div>

				<h3 mvkeynone="teacherexpdesc"><span>专长介绍</span></h3>
				<div class="info js-mv-text" mvkey="teacherexpdesc" mvkeynone="teacherexpdesc">${teacher.teacherexpdesc} &nbsp;</div>

	        </div>

			<div class="site clearfix js-mv-resources">
				<ul class="tab clearfix">
				</ul>
				<!-- 图片 -->
				<div class="list1 on js-mv-res-img">
					<div class="demo-list list-video clearfix">
					</div>
				</div>
				<!-- 图片  END -->

				<!-- 视频 -->
				<div class="list1 js-mv-res-video">
					<div class="demo-list list-video clearfix">
					</div>
				</div>
				<!-- 视频 END -->

				<!-- 音频 -->
				<div class="list1 js-mv-res-audio">
					<div class="demo-list list-mp3 clearfix">
					</div>
				</div>
				<!-- 音频 END -->
				<!-- 下载 -->
				<div class="list1 js-mv-res-file">
					<div class="file-download-cont">
						<ul>
						</ul>
					</div>
				</div>
				<!-- 下载 -->
			</div>

	        <!-- 分享到微信和空间 -->
	        <div class="public-share">
	            <span class="btn qq"><a href="javascript:void(0);" class="fxqq"></a></span>
	            <span class="btn weixin"><a href="javascript:void(0)" class="fxweix"></a></span>
				<span class="btn weibo"><a href="javascript:void(0)" class="fxweibo" target="_blank"></a></span>
	            <span class="btn dianzan">
	              	<em>0</em>
	              	<a href="javascript:void(0)" class="dianzan" reftyp="2016101400000051" refid="${id}" id="good"></a>
	            </span>
	       	</div> 
	        <!-- 动态包含评论 -->
	        <%--<jsp:include page="/pages/comm/agdcomment.jsp" flush="true">
			     <jsp:param name="reftype" value="3"/>
			     <jsp:param name="refid" value="${teacher.teacherid }"/> 
			</jsp:include>--%>
			<!-- 动态包含评论-END -->
			
		</div>
		<div class="public-right-main">
			<%--<c:if test="${not empty trainList}">
				<div class="public-other-notice">
					<h2>推荐课程</h2>
					<c:choose>
					   <c:when test="${trainList != null && fn:length(trainList) > 0 }">
						   <c:forEach items="${trainList }" var="row" varStatus="s">
								<div class="item clearfix">
									<div class="right-img">
										<a href="${basePath }/agdpxyz/traininfo?traid=${row.id }"><img src="${imgServerAddr}${row.trainimg}" width="130" height="90"></a>
									</div>
									<div class="right-detail">
										<a href="${basePath }/agdpxyz/traininfo?traid=${row.id }"><h3>${row.title}</h3></a>
										<p class="time"><fmt:formatDate value="${row.starttime}" pattern="yyyy-MM-dd" /></p>
									</div>
								</div>
						   </c:forEach>
					   </c:when>
					   <c:otherwise>
							<div class="public-no-message "></div>
					   </c:otherwise>
					</c:choose>
				</div>
			</c:if>--%>

			<!--相关课程-->
			<div class="public-other-notice js-mv-ecommendlist" style="margin-top: 20px">
				<h2>相关课程</h2>
				<%--<c:forEach items="${train }" var="row" varStatus="s">
					<div class="item clearfix">
						<div class="right-img">
							<a href="${basePath }/agdpxyz/traininfo?traid=${row.id }"><img src="${imgServerAddr}${row.trainimg}" width="130" height="90"></a>
						</div>
						<div class="right-detail">
							<a href="${basePath }/agdpxyz/traininfo?traid=${row.id }"><h3>${row.title}</h3></a>
							<p class="time"><fmt:formatDate value="${row.starttime}" pattern="yyyy-MM-dd" /></p>
						</div>
					</div>
				</c:forEach>--%>
			</div>
			<!--相关课程  END-->
		</div>
	</div>
</div>
<!--主体结束-->

<!--公共主底部开始-->
<%@include file="/pages/comm/agdfooter.jsp"%>
<!--公共主底部结束-END-->

<script>
	$(function(){
        pageTool.init();
	});

	var pageTool = {
        basePath: '${basePath}',
        apiSite: '${apiSite}',
        apiRoot: '${apiRoot}',
        apiZxpx: '${apiZxpx}',
        id: '${id}',

        init: function(){
            if (/\/$/.test(this.apiZxpx)){
                this.apiZxpx = this.apiZxpx.replace(/\/$/, '');
            }
            this.getInfo();

            this.getResources();
        },

        getInfo: function(){
            var self = this;
            $.get(self.apiRoot+'api/px/tea/detail', {id: self.id}, function(data){
                if (!data || !data.data){
                    return;
                }

                var info = self.convertInfo(data.data);
                self.mvText(info);

                self.mvOther(data);

                self.getRecommendList(data.data.name);

            }, 'json');
        },

        mvOther: function(data){
            var self = this;
            $(".js-mv-src").attr("src", WhgComm.getImg750_500(data.imgPath+data.data.teacherpic));
        },

        convertInfo: function(info){
            var data = $.extend({}, info ||{});

            return data;
        },

        mvText: function(data){
            var self = this;
            data = data || {};
            $(".js-mv-text").each(function(){
                var key = $(this).attr("mvkey");
                if (!key) {
                    return true;
                }
                if (!data[key] || $.trim(data[key])==''){
                    $("[mvkeynone='"+key+"']").hide();
                    return true;
                }
                if ($(this).hasClass("mv-html")){
                    $(this).html(data[key]);
                }else{
                    $(this).text(data[key]);
                }
            });
        },

        getRecommendList: function(name){
            var self = this;
            var view = $(".js-mv-ecommendlist");
            view.hide();
            var temp = '<div class="item clearfix">' +
                ' <div class="right-img">' +
                '  <a ><img width="130" height="90"></a>' +
                ' </div>' +
                ' <div class="right-detail">' +
                '  <a ><h3></h3></a>' +
                '  <p class="time"></p>' +
                ' </div>' +
                '</div>';

            $.get(self.apiRoot+'api/px/tea/getRecommendTea', {
                name: name,
                size: 3
            }, function(data){
                if (!data || !data.rows || !data.rows.length){
                    return;
                }
                view.show();
                for(var i in data.rows){
                    var row = data.rows[i];
                    var item = $(temp);
                    view.append(item);

                    item.find("a").attr("href", self.basePath+'/agdpxyz/traininfo?traid='+row.id);
                    item.find("h3").text(row.title);
                    item.find("img").attr("src", WhgComm.getImg300_200(data.imgPath+row.trainimg));
                }
            }, 'json');
        },

        getResources: function(){
            var self = this;
            $.get(self.apiRoot+'api/outer/comm/getResources',
                {reftype: 31, refid: self.id},
                function(data){
                    var res = {
                        imgPath: data.imgPath,
                        imageList:[],
                        videoList:[],
                        audioList:[],
                        fileList:[]
                    };
                    if (data.data){
                        for(var i in data.data){
                            var row = data.data[i];
                            switch (row.enttype){
                                case '1': res.imageList.push(row); break;
                                case '2': res.videoList.push(row); break;
                                case '3': res.audioList.push(row); break;
                                case '4': res.fileList.push(row); break;
                            }
                        }
                    }
                    self.mvResources(res);
                }, 'json');
        },

        mvResources: function(data){
            var self = this;
            var mview = $(".js-mv-resources");

            var imageList = data.imageList;
            var videoList = data.videoList;
            var audioList = data.audioList;
            var fileList = data.fileList;

            var navs = [];
            if (imageList && imageList.length){
                navs.push('<li>图片</li>');
                var imgtemp = '<a href="javascript:void(0)" >' +
                    '    <div class="img1">' +
                    '        <img  height="170">' +
                    '        <span></span>' +
                    '    </div>' +
                    '</a>';
                var imgView = mview.find(".js-mv-res-img").children("div");
                for(var i=0; i<imageList.length; i++){
                    var row = imageList[i];
                    var item = $(imgtemp);
                    imgView.append(item);
                    if ((i+1)%3 == 0){
                        item.addClass("last");
                    }
                    var enturl = /^http/.test(row.enturl)?row.enturl: data.imgPath+row.enturl;
                    item.attr("onClick", 'show_img(this,{url:"'+enturl+'"})');
                    item.find("img").attr("src", WhgComm.getImg750_500(enturl));
                    item.find("span").text(row.name);
                }
            }else{
                mview.find(".js-mv-res-img").remove();
            }

            if (videoList && videoList.length){
                navs.push('<li>视频</li>');
                var temp = '<a href="javascript:void(0)" >' +
                    '    <div class="mask"></div>' +
                    '    <div class="video1">' +
                    '        <img height="170">' +
                    '        <span></span>' +
                    '    </div>' +
                    '</a>';
                var imgView = mview.find(".js-mv-res-video").children("div");
                for(var i=0; i<videoList.length; i++){
                    var row = videoList[i];
                    var item = $(temp);
                    imgView.append(item);
                    if ((i+1)%3 == 0){
                        item.addClass("last");
                    }
                    var enturl = /^http/.test(row.enturl)?row.enturl: data.imgPath+row.enturl;
                    var deourl = /^http/.test(row.deourl)?row.deourl: data.imgPath+row.deourl;
                    item.attr("onClick", 'show_vedio(this,{url:"'+enturl+'"})');
                    item.find("img").attr("src", WhgComm.getImg750_500(deourl));
                    item.find("span").text(row.name);
                }
            }else{
                mview.find(".js-mv-res-video").remove();
            }

            if (audioList && audioList.length){
                navs.push('<li>音频</li>');
                var temp = '<a href="javascript:void(0)" >' +
                    '    <div class="mask"></div>' +
                    '    <div class="mp31">' +
                    '        <span></span>' +
                    '    </div>' +
                    '</a>';
                var imgView = mview.find(".js-mv-res-audio").children("div");
                for(var i=0; i<audioList.length; i++){
                    var row = audioList[i];
                    var item = $(temp);
                    imgView.append(item);
                    if ((i+1)%3 == 0){
                        item.addClass("last");
                    }
                    var enturl = /^http/.test(row.enturl)?row.enturl: data.imgPath+row.enturl;
                    item.attr("onClick", 'show_vedio(this,{url:"'+enturl+'"})');
                    item.find("span").text(row.name);
                }
            }else{
                mview.find(".js-mv-res-audio").remove();
            }

            if (fileList && fileList.length){
                navs.push('<li>文件</li>');
                var temp = '<li>' +
                    '    <a class="js-urlencode"><i></i></a>' +
                    '</li>';
                var imgView = mview.find(".js-mv-res-file").children("div");
                for(var i=0; i<fileList.length; i++){
                    var row = fileList[i];
                    var item = $(temp);
                    imgView.append(item);
                    var enturl = /^http/.test(row.enturl)?row.enturl: data.imgPath+row.enturl;
                    item.find("a").attr("href", self.basePath+'/whtools/downFile?filePath='+enturl);
                    item.find("a").html('<i></i>'+row.name);
                }
            }else{
                mview.find(".js-mv-res-file").remove();
            }

            if (!navs.length){
                mview.hide();
                return;
            }

            var nview = mview.children("ul");
            for(var i in navs){
                nview.append(navs[i]);
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
	}
</script>
</body>
</html>