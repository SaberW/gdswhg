<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<%@include file="/pages/comm/agdhead.jsp"%>
	<title class="js-mv-text" mvkey="title"></title>
	<link href="${basePath}/static/assets/css/train/registrationInfo.css" rel="stylesheet">
	<script src="${basePath}/static/assets/js/train/registrationInfo.js"></script>

	<script src="${basePath }/static/common/js/whg.maps.js"></script>

	<!--[if lt IE 9] >
    <script src="../../assets/js/plugins/ie/IE9.js"></script>
    <! [endif]]-->
	<script type="text/javascript">

		$(function () {
			pageTool.init();
		});

		var pageTool = {
            basePath: '${basePath}',
            apiSite: '${apiSite}',
            apiRoot: '${apiRoot}',
			apiZxpx: '${apiZxpx}',
            id: '${traid}',

			init: function(){
                if (/\/$/.test(this.apiZxpx)){
                    this.apiZxpx = this.apiZxpx.replace(/\/$/, '');
                }
                this.getInfo();
                this.getCourseList();
                this.getResources();

                this.getCultInfo();
                this.getRefzxlist();
			},

			getInfo: function(){
                var self = this;
                $.get(self.apiRoot+'api/px/detail', {id: self.id}, function(data){
                    if (!data || !data.data){
                        return;
                    }

                    var info = self.convertInfo(data.data.tra);
                    self.mvText(info);

                    self.mvOther(data);

                }, 'json');
			},

			mvOther: function(data){
			    var self = this;
                $(".js-mv-src").attr("src", WhgComm.getImg750_500(data.imgPath+data.data.tra.image));
                var tra = data.data.tra || {};
                var button = '';
                if (tra.applyBegin > tra.date){
                    button = '<a href="javascript:void(0)" class="open">报名未开始</a>'
				}else if (tra.applyEnd < tra.date){
                    button = '<a href="javascript:void(0)" class="open">报名已结束</a>'
				}else if (tra.enrolcount >= tra.maxApplyNum){
					button = '<a href="javascript:void(0)" class="open">报名名额已满</a>'
				}else {
				    var bmhref = self.apiZxpx+'/'+self.apiSite+'/xxpx/step?id='+self.id+'';
				    button = '<a id="showbm" href="'+bmhref+'" target="_blank">立即报名</a>'
				}
				$(".js-mv-baoming").html(button);

                WhgMap.showMap('maps_tra_target', tra.address, tra.longitude, tra.latitude);
			},

			convertInfo: function(info){
			    var data = $.extend({}, info ||{});
                if(data.teachername && data.teachername!=''){
                    data.teachers = '';
                    var teachers = data.teachername.split(/\s*,\s*/);
                    for(var k in teachers){
                        if (teachers[k] && teachers[k]!=''){
                            data.teachers += '<span>'+teachers[k]+' </span>&nbsp;';
                        }
                    }
                }
                data.enrolcount = data.enrolcount+'';
                data._maxApplyNum = "/"+data.maxApplyNum;
                data.applyBegin = new Date(data.applyBegin).Format("yyyy-MM-dd hh:mm:ss");
                data.applyEnd = new Date(data.applyEnd).Format("yyyy-MM-dd hh:mm:ss");

                data.pxzqtime = new Date(data.trainingBegin).Format("yyyy-MM-dd")
						+'&nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;'
					+new Date(data.trainingEnd).Format("yyyy-MM-dd");

                var age = data.age;
                if (age && age!=''){
                    age = age.split(/\s*\D+\s*/);
                    if (age.length){
                        data.age1 = age[0];
                        data.age2 = age.length>1? age[1] : age[0];
					}
				}

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

            getCourseList: function(page, pageSize){
                var self = this;
                var params = {
                    id: self.id,
					page: page || 1,
					pageSize: pageSize || 10
				};

                var $table = $(".table table");
                $table.find("tr").remove(":not(.table_th)");
                $.get(self.apiRoot+'api/px/course', params, function(data){
                    if (!data || !data.rows || !data.rows.length){
                        return;
                    }
                    var idx_course = (data.page -1)*params.pageSize;
					for (var i = 0; i<data.rows.length; i++){
                        var row = data.rows[i];
                        var list = '';
                        list += '<tr>';
                        list += '	<td class="not-conform">'+(i+1+idx_course)+'</td>';
                        list += '	<td class="not-conform">'+datetimeFMT(row.starttime)+'</td>';
                        list += '	<td class="not-conform">'+datetimeFMT(row.endtime)+'</td>';
                        list += '</tr>';
                        $table.append(list);
					}

                    //加载分页工具栏
                    genPagging('whgPagging', params.page, params.pageSize, data.total, function(page, pageSize){
                        self.getCourseList(page, pageSize);
					});
                }, 'json');
			},

			getResources: function(){
                var self = this;
                $.get(self.apiRoot+'api/outer/comm/getResources',
                    {reftype: 5, refid: self.id},
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
                    navs.push('<li>培训图片</li>');
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
                    navs.push('<li>培训视频</li>');
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
                    navs.push('<li>培训音频</li>');
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
                    navs.push('<li>培训文件</li>');
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
            },

            getCultInfo: function(){
                var self = this;
                $.get(self.apiRoot+'api/outer/comm/getCultBySite',
                    {cultsite: self.apiSite},
                    function(data){
                        if (data.data){
                            self.getRecommendList(data.data);
                        }
                    }, 'json');
            },

            getRecommendList: function(cult){
                var self = this;
                var view = $(".js-mv-ecommendlist");
                var temp = '<div class="item clearfix">' +
                    '    <div class="right-img">' +
                    '        <a ><img  width="130" height="90"></a>' +
                    '    </div>' +
                    '    <div class="right-detail">' +
                    '        <a ><h3></h3></a>' +
                    '        <p class="time"></p>' +
                    '    </div>' +
                    '</div>';

                $.get(self.apiRoot+'api/px/getRecommendTra', {
                    id: self.id,
                    cultid: cult.id,
                    size: 4
                }, function(data){
                    if (!data || !data.rows || !data.rows.length){
                        view.append('<div class="public-no-message"></div>');
                        return;
                    }
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

			getRefzxlist: function(){
                var self = this;
                var view = $(".js-mv-refzxlist");
                view.hide();

                var temp = '<li><a href="${basePath }/agdpxyz/newsinfo?id="></a>' +
                    '<p class="time"></p>' +
                    '</li>';
                $.get(self.apiRoot+'api/comm/glzx', {
                    entityid: self.id,
                    clnftype: '2016111900000021'
				}, function(data){
					if (!data || !data.data || !data.data.length){
					    return;
					}
					view.show();
					for(var i in data.data){
					    var row = data.data[i];
					    var item = $(temp);
					    view.find("ul").append(item);

					    var _href = item.find("a").attr("href");
                        item.find("a").attr("href", _href+row.clnfid)
							.text(row.clnftltle);
                        item.find("p").text( (new Date(row.publishdate).Format("yyyy-MM-dd hh:mm:ss")) );
					}
				}, 'json');
			}
		}

	</script>
</head>
<body class="oiplayer-example">
<!-- 公共头部开始 -->
<%@include file="/pages/comm/agdtopsmall.jsp"%>
<!-- 公共头部结束-END -->

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
				<li><a href="${basePath }/agdpxyz/teacher">培训师资</a></li>
				<li class="active"><a href="${basePath }/agdpxyz/trainlist">在线报名</a></li>
				<li class="last"><a href="${basePath }/agdpxyz/vod">在线点播</a></li>
				<%-- <li class="last"><a href="${basePath }/agdpxyz/resources">培训资源库</a></li> --%>
			</ul>
		</div>
	</div>
</div>

<!--面包屑开始-->
<div class="public-crumbs">
	<span><a href="${basePath }/home">首页</a></span><span>></span><span><a href="${basePath }/agdpxyz/index">培训驿站</a></span><span>></span><span><a href="${basePath }/agdpxyz/trainlist">在线报名</a></span><span>></span><span class="js-mv-text" mvkey="title"></span>
</div>
<!--面包屑结束-->

<!--主体开始-->
<div class="main-info-bg main-info-no-padding main-info-bgColorW">
	<div class="main-info-container clearfix">
		<div class="registrationInfo">
			<div class="img">
				<img class="js-mv-src"  width="480" height="315">
			</div>
			<div class="info">
				<div class="public-fav"><a id="collection" class="shoucang" reftype="5" refid="${traid}" href="javascript:void(0)"></a></div>
				<h1 class="js-mv-text" mvkey="title"></h1>

				<p class="name clearfix" mvkeynone="teachers"><i class="public-s-ico s-ico-15"></i><span class="desc">讲师 :</span>
					<span class="tt js-mv-text mv-html" mvkey="teachers">
					<%--<c:forEach items="${teacher}" var="item"><span>${item} </span>&nbsp;</c:forEach>--%>
				</span></p>

				<p class="seat clearfix"><i class="public-s-ico s-ico-16"></i><span class="desc">人数 :</span>
					<span class="tt">
						<span class="num js-mv-text" mvkey="enrolcount">
							<%--${(not empty count) ? count : 0 }--%>
						</span>
						<span class="js-mv-text" mvkey="_maxApplyNum"></span>
						<%--${train.isshowmaxnumber == 0 ?"":"/"}${train.isshowmaxnumber == 0 ? "" :train.maxnumber}--%>
					</span></p>

				<p class="tel clearfix" mvkeynone="phone"><i class="public-s-ico s-ico-17"></i><span class="desc">电话 :</span>
					<span class="tt js-mv-text" mvkey="phone"></span></p>

				<p class="adr clearfix"><i class="public-s-ico s-ico-10"></i><span class="desc">地址 :</span>
					<span class="tt js-mv-text" mvkey="address"></span></p>
				<p class="adr clearfix"><i class="public-s-ico s-ico-30"></i><span class="desc">报名周期 :</span>
					<span class="tt">
						<span class="js-mv-text" mvkey="applyBegin">
						<%--<fmt:formatDate value="${train.enrollstarttime}" pattern="yyyy-MM-dd HH:mm:ss"/>--%>
						</span>&nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;
						<span class="js-mv-text" mvkey="applyEnd">
							<%--<fmt:formatDate value="${train.enrollendtime}" pattern="yyyy-MM-dd HH:mm:ss"/>--%>
						</span>
					</span></p>
				<p class="adr clearfix"><i class="public-s-ico s-ico-31"></i><span class="desc">培训周期 :</span>
					<span class="tt js-mv-text mv-html" mvkey="pxzqtime">
						<%--<fmt:formatDate value="${train.starttime}" pattern="yyyy-MM-dd"/>&nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;<fmt:formatDate value="${train.endtime}" pattern="yyyy-MM-dd"/>--%>
					</span></p>

				<p class="adr clearfix" mvkeynone="age1"><i class="public-s-ico s-ico-1"></i><span class="desc">年龄段 :</span>
					<span class="tt">
						<span class="js-mv-text" mvkey="age1"></span>&nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;
						<span class="js-mv-text" mvkey="age2"></span>&nbsp;&nbsp;岁
					</span></p>

				<div class="goNext">
					<!--<a href="javascript:void(0)" class="open">直接前往</a>-->

					<div class="goNext js-mv-baoming">
						<%--<c:choose>
							<c:when test="${(train.enrollstarttime gt now)}">
								<a href="javascript:void(0)" class="open">报名未开始</a>
							</c:when>
							<c:when test="${(train.enrollendtime lt now)}">
								<a href="javascript:void(0)" class="open">报名已结束</a>
							</c:when>
							<c:when test="${(count ge train.maxnumber) }">
								<a href="javascript:void(0)" class="open">报名名额已满</a>
							</c:when>
							<c:otherwise>
								<a id="showbm" href="javascript:void(0)" trainId="${train.id}">立即报名</a>
								<a id="hiddenbm" style="display: none" class="open" href="javascript:void(0)">立即报名</a>
							</c:otherwise>
						</c:choose>--%>
					</div>

				</div>
			</div>
		</div>
		<div class="public-left-main">
			<div class="public-info-step">
				<h3><span>查看课程表</span></h3>
				<div class="table">
					<table>
						<tbody>
							<tr class="table_th">
								<th>序列</th>
								<th>开课时间</th>
								<th>结束时间</th>
							</tr>
						</tbody>
					</table>
					<!-- 分页栏 -->
					<div class="green-black" id="whgPagging"></div>
					<!-- 分页栏-END -->
				</div>

				<!--这里的表格有分页，10条一页-->
			</div>
			<div class="public-info-step">

				<h3 mvkeynone="intro"><span>课程简介</span></h3>
				<div class="info js-mv-text mv-html" mvkey="intro" mvkeynone="intro">
				</div>

				<h3 mvkeynone="outline"><span>课程内容</span></h3>
				<div class="info js-mv-text mv-html" mvkey="outline" mvkeynone="outline">
				</div>

				<h3 mvkeynone="teacherdesc"><span>老师介绍</span></h3>
				<div class="info js-mv-text mv-html" mvkey="teacherdesc" mvkeynone="teacherdesc">
				</div>
			</div>

			<div class="site clearfix js-mv-resources">
				<ul class="tab clearfix">
					<%--<c:if test="${not empty pic }">
						<li class="${not empty pic ? 'active' : '' }">培训图片</li>
					</c:if>
					<c:if test="${not empty video }">
						<li class="${not empty pic ? '' : 'active' }">培训视频</li>
					</c:if>
					<c:if test="${not empty audio }">
						<li class="${not empty pic || not empty video ? '' : 'active' }">培训音频</li>
					</c:if>
					<c:if test="${not empty doc }">
						<li class="${not empty pic || not empty video || not empty audio ? '' : 'active' }">培训文档</li>
					</c:if>--%>
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
			<%--<div class="public-share">
                <span class="btn qq"><a href="http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?"
										target="_blank"></a></span>
				<span class="btn weixin"><a href="javascript:void(0)" target="_blank"></a></span>
				<span class="btn weibo"><a href="javascript:void(0)" target="_blank"></a></span>
                <span class="btn dianzan">
                	<em>0</em>
                	<a href="javascript:void(0)" class="dianzan" reftyp="2016101400000051" refid="${train.id }" id="good"></a>
                    </a>
                </span>
			</div>--%>

			<div class="public-share">
				<span class="btn qq"><a href="javascript:void(0);" class="fxqq"></a></span>
				<span class="btn weixin"><a a href="javascript:void(0);" class="fxweix"></a></span>
				<span class="btn weibo"><a href="javascript:void(0)" class="fxweibo" target="_blank"></a></span>
				<span class="btn dianzan">
					<em>0</em>
					<a href="javascript:void(0)" class="dianzan" reftyp="5" refid="${traid }" id="good"></a>
				</span>
			</div>
			<!-- 动态包含评论 -->
			<%--<jsp:include page="/pages/comm/agdcomment.jsp" flush="true">
				<jsp:param name="reftype" value="2"/>
				<jsp:param name="refid" value="${train.id}"/>
			</jsp:include>--%>
			<!-- 动态包含评论-END -->

		</div>

		<div class="public-right-main">
			<div class="public-other-notice margin-bottom padding-bottom-0">
				<div class="map" id="maps_tra_target" style="width: 300px; height: 222px;">
				</div>
			</div>

			<div class="public-other-notice js-mv-ecommendlist">
				<h2>推荐课程</h2>
				<%--<c:choose>
					<c:when test="${not empty kecheng }">
						<c:forEach items="${kecheng }" var="item">
							<div class="item clearfix">
								<div class="right-img">
									<a href="${basePath }/agdpxyz/traininfo?traid=${item.id}"><img src="${imgServerAddr}${whg:getImg300_200(item.trainimg)}" width="130" height="90"></a>
								</div>
								<div class="right-detail">
									<a href="${basePath }/agdpxyz/traininfo?traid=${item.id}"><h3>${item.title }</h3></a>
									<p class="time"><fmt:formatDate value="${item.starttime }" pattern="yyyy-MM-dd "/></p>
								</div>
							</div>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<div class="public-no-message "></div>
					</c:otherwise>
				</c:choose>--%>
			</div>
			<!-- 相关资讯-->
			<div class="public-other-notice js-mv-refzxlist" style="margin-top: 20px">
				<h2>相关资讯</h2>
				<ul>
					<%--<c:forEach items="${info }" var="row" varStatus="s">
						<li><a href="${basePath }/agdpxyz/newsinfo?id=${row.clnfid}">${row.clnftltle }</a>
							<p class="time"><fmt:formatDate value="${row.clnfcrttime}" pattern="yyyy-MM-dd"/></p>
						</li>
					</c:forEach>--%>
				</ul>
			</div>
		</div>
	</div>
</div>
<!--主体结束-->

<!--公共主底部开始-->
<%@include file="/pages/comm/agdfooter.jsp"%>
<!--公共主底部结束-END-->

<a class="to-top"></a>
</body>
</html>