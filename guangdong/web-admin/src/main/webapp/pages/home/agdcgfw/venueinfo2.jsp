<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <%@include file="/pages/comm/agdhead.jsp"%>
    <title class="js-mv-text" mvkey="title">场馆详情</title>
    <link href="${basePath }/static/assets/css/field/fieldOrder.css" rel="stylesheet">
    <%--<link href="${basePath }/static/assets/js/plugins/oiplayer-master/css/oiplayer.css" rel="stylesheet">--%>
    <script src="${basePath }/static/assets/js/plugins/laydate.dev.js"></script>
    <script src="${basePath }/static/assets/js/field/fieldOrder.js"></script>

    <%--<script src="${basePath }/static/assets/js/plugins/oiplayer-master/plugins/flowplayer-3.2.6.min.js"></script>
    <script src="${basePath }/static/assets/js/plugins/oiplayer-master/js/jquery.oiplayer.js"></script>--%>

    <%--<script type="text/javascript" src="${basePath }/pages/home/agdcgfw/wktool.js"></script>--%>
    <%--<script type="text/javascript" src="${basePath }/pages/home/agdcgfw/venueinfo.js"></script>--%>

    <script src="${basePath }/static/common/js/whg.maps.js"></script>

</head>
<body class="oiplayer-example">

<!-- 公共头部开始 -->
<%@include file="/pages/comm/agdtopsmall.jsp"%>
<!-- 公共头部结束-END -->


<div id="header-fix">
    <div class="header-nav-bg">
        <div class="header-nav">
            <div class="logo-small">
                <a href="${basePath }"><img src="${basePath }/static/assets/img/public/logoSmall.png"></a>
            </div>
            <ul>
                <li class="active last"><a href="${basePath }/agdcgfw/index">场馆服务</a></li>
            </ul>
        </div>
    </div>
</div>


<div class="public-crumbs"> 
	<span><a href="${basePath }">首页</a></span><span>></span>
	<span><a href="${basePath }/agdcgfw/index">场馆服务</a></span><span>></span>
	<span class="js-mv-text" mvkey="title"></span>
</div>

<!--主体开始-->
<div class="venue-main clearfix">
  <div class="venue-img"> <img class="js-mv-src" mvkey="image" width="420px" height="285px"> </div>
  <div class="venue-info">
  	<div class="public-fav"><a href="javascript:void(0)" class="shoucang" reftyp="2" refid="${venid }" id="collection"></a></div>
    <h1 class="js-mv-text" mvkey="title"></h1>
    <p class="clearfix"><i class="public-s-ico s-ico-11"></i><span class="tt">区域 :</span><span class="desc js-mv-text" mvkey="area"></span></p>
    <p class="clearfix"><i class="public-s-ico s-ico-12"></i><span class="tt">类型 :</span><span class="desc js-mv-text" mvkey="etype"></span></p>
    <p class="clearfix"><i class="public-s-ico s-ico-17"></i><span class="tt">电话 :</span><span class="desc js-mv-text" mvkey="telephone"></span></p>
    <p class="clearfix"><i class="public-s-ico s-ico-10"></i><span class="tt">地址 :</span><span class="desc js-mv-text" mvkey="address"></span></p>
    <p class="clearfix js-mv-none"><i class="public-s-ico s-ico-13"></i><span class="tt">用途 :</span>
        <span class="desc js-mv-em-etag">
            <%--<em></em>--%>
        </span>
    </p>

  </div>
</div>

<div class="main clearfix">
    <div class="main-info-bg main-info-no-padding main-info-bgColorW">
        <div class="main-info-container clearfix">
            <div class="public-left-main">

                <div class="public-info-step js-mv-none">
                    <h3> <span>选择场地</span><em>(<i class="red js-mv-roomnumber"></i>)</em>
                        <div class="pre-right"> <i class="kx-arrow kx-arrow-left"> <em></em> <span></span> </i> </div>
                        <div class="pre-left"> <i class="kx-arrow kx-arrow-right"> <em></em> <span></span> </i> </div>
                    </h3>
                    <div class="info">
                        <div class="eventliebiao_done">
                            <ul class="clearfix js-mv-rooms">
                                <%--<li class="active">
                                    <a href="/agdcgfw/venroominfo?roomid=">
                                    <img width="185" height="123">
                                    <em></em>
                                    </a>
                                </li>--%>
                            </ul>
                        </div>
                    </div>
                </div>

                <div class="public-info-step">
                    <h3><span>场馆简介</span></h3>
                    <div class="info js-mv-text" mvkey="intro">
                    </div>
                    <h3><span>场馆描述</span></h3>
                    <div class="info js-mv-text mv-html" mvkey="description">
                    </div>

                    <h3 mvkeynone="facilitydesc"><span>设施描述</span></h3>
                    <div class="info js-mv-text mv-html" mvkey="facilitydesc" mvkeynone="facilitydesc">
                    </div>

                    <%--<h3><span>申请条件</span></h3>
                    <div class="info">
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1. 筑造和栖居是人类建立和定义空间的基本。栖居的重要性不容置疑，人和家居空间的关系也并不止于理论范畴：家居空间的功能性；人（用户）在家庭场景内的感官互动、私密性和展示性的平衡；私人领域与公共领域的定义和分界等等，都从人对家居空间的讨论和思考中衍生而来家是营造情感、审美、兴趣、修养的空间，也是建立个体和群体间感情关系的空间 <br><br>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2. 家以一个最基本的社会组成单位的形态与所处时代互视和对话；在飞速的城市化进程中，家居缓慢了生活的物化节奏；本展共有十二位来自世界不同区域的艺术家创作关于“家”的作品（部分艺术家作品首次在中国展出），艺术家或将家居环境中的庸常之物重构为独特的视觉景观。<br><br>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3. 都从人对家居空间的讨论和思考中衍生而来家是营造情感、审美、兴趣、修养的空间，也是建立个体和群体间感情关系的空间。家以一个最基本的社会组成单位的形态与所处时代互视和对话；在飞速的城市化进程中，家居缓慢了生活的物化节奏；本展共有十二位来自世界不同区域的艺术家创作关于“家”的作品（部分艺术家作品首次在中国展出）。
                    </div>--%>

                    <div class="site clearfix js-mv-resources">
                        <ul class="tab clearfix">
                        </ul>

                        <div class="list1 on js-mv-res-img">
                            <div class="demo-list list-video clearfix">
                                <%--<c:forEach var="item" items="${venImgList}" varStatus="vs">
                                    <a ${vs.count==3? 'class="last"':''} href="javascript:void(0)" onClick="show_img(this,{url:'${imgServerAddr}${item.enturl}'})">
                                        <div class="img1">
                                            <img src="${imgServerAddr}${whg:getImg300_200(item.enturl)}" height="170">
                                            <span>${item.entname}</span>
                                        </div>
                                    </a>
                                </c:forEach>--%>
                            </div>
                        </div>

                        <div class="list1 js-mv-res-video">
                            <div class="demo-list list-video clearfix">
                                <%--<c:forEach var="item" items="${venVideoList}" varStatus="vs">
                                    <a ${vs.count==3? 'class="last"':''} href="javascript:void(0)" onClick="show_vedio(this,{url:'${item.enturl}'})">
                                        <div class="mask"></div>
                                        <div class="video1">
                                            <img src="${imgServerAddr}${whg:getImg300_200(item.deourl)}" height="170">
                                            <span>${item.entname}</span>
                                        </div>
                                    </a>
                                </c:forEach>--%>
                            </div>
                        </div>

                        <div class="list1 js-mv-res-audio">
                            <div class="demo-list list-mp3 clearfix">
                                <%--<c:forEach var="item" items="${venAudioList}" varStatus="vs">
                                    <a ${vs.count==3? 'class="last"':''} href="javascript:void(0)" onClick="show_vedio(this,{url:'${item.enturl}'})">
                                        <div class="mask"></div>
                                        <div class="mp31">
                                            <span>${item.entname}</span>
                                        </div>
                                    </a>
                                </c:forEach>--%>
                            </div>
                        </div>
                        <!-- 下载 -->
                        <div class="list1 js-mv-res-file">
                            <div class="file-download-cont">
                                <ul>
                                    <%--<c:forEach items="${doc}" var="loadlists" varStatus="s">
                                        <li>
                                            <a href="${basePath }/whtools/downFile?filePath=${loadlists.enturl}" class="js-urlencode"><i></i>${loadlists.entname}</a>
                                        </li>
                                    </c:forEach>--%>
                                </ul>
                                <%--<script>
                                    $(function(){
                                        $("a.js-urlencode").each(function(){
                                            var _href = $(this).attr("href");
                                            _href = encodeURI(_href);
                                            $(this).attr("href", _href);
                                        })
                                    })
                                </script>--%>
                            </div>
                        </div>
                        <!-- 下载 -->

                    </div>

                    <div class="public-share">
                        <span class="btn qq"><a href="javascript:void(0);" class="fxqq"></a></span>
                        <span class="btn weixin"><a a href="javascript:void(0);" class="fxweix"></a></span>
                        <span class="btn weibo"><a href="javascript:void(0)" class="fxweibo" target="_blank"></a></span>
                        <span class="btn dianzan">
                            <em>0</em>
                            <a href="javascript:void(0)" class="dianzan" reftyp="2" refid="${venid}" id="good"></a>
                        </span>
                    </div>
                </div>

                <!-- 动态包含评论 -->
                <%--<jsp:include page="/pages/comm/agdcomment.jsp" flush="true">
                    <jsp:param name="reftype" value="6"/>
                    <jsp:param name="refid" value="${venue.id }"/>
                </jsp:include>--%>
                <!-- 动态包含评论-END -->

            </div>

            <div class="public-right-main">
                <div class="public-other-notice margin-bottom padding-bottom-0">
                    <div class="map" id="maps_ven_target" style="width: 300px; height: 222px;">
                    </div>
                </div>
                <div class="public-other-notice js-mv-ecommendvens">
                    <h2>推荐场馆</h2>

                </div>
            </div>
        </div>
    </div>
</div>


<!--主体结束-->

<!--公共主底部开始-->
<%@include file="/pages/comm/agdfooter.jsp"%>
<!--公共主底部结束-END-->

<script>
    $(function () {
        pageTool.init();
    });

    var pageTool = {
        basePath: '${basePath}',
        apiSite: '${apiSite}',
        apiRoot: '${apiRoot}',
        apiWbgx: '${apiWbgx}',
        venid: '${venid}',

        init: function(){
            if (/\/$/.test(this.apiWbgx)){
                this.apiWbgx = this.apiWbgx.replace(/\/$/, '');
            }
            this.getVenInfo();
            this.getCultInfo();
        },

        getCultInfo: function(){
            var self = this;
            $.get(self.apiRoot+'api/outer/comm/getCultBySite',
                {cultsite: self.apiSite},
                function(data){
                    if (data.data){
                        self.getRecommendVenList(data.data);
                    }
                }, 'json');
        },

        getRecommendVenList: function(cult){
            var self = this;
            var view = $(".js-mv-ecommendvens");
            var temp = '<div class="item clearfix">' +
                '    <div class="right-img">' +
                '        <a ><img  width="130" height="90"></a>' +
                '    </div>' +
                '    <div class="right-detail">' +
                '        <a ><h3></h3></a>' +
                '        <p class="time"></p>' +
                '    </div>' +
                '</div>';
            $.get(self.apiRoot+'api/venue/recommendVenList', {
                exVenid: self.venid,
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

                    item.find("a").attr("href", self.basePath+'/agdcgfw/venueinfo?venid='+row.id);
                    item.find("h3").text(row.title);
                    item.find("img").attr("src", WhgComm.getImg300_200(data.imgPath+row.imgurl));
                }
            }, 'json');
        },

        getVenInfo: function(){
            var self = this;
            $.get(self.apiRoot+'api/venue/detail', {itemId: self.venid}, function(data){
                if (!data || !data.data){
                    return;
                }
                self.mvText(data.data);
                self.mvOther(data);
                self.mvRooms(data);
                self.mvResources(data);

                WhgMap.showMap('maps_ven_target', data.data.address, data.data.longitude, data.data.latitude)
            }, 'json');
        },

        mvResources: function(data){
            var self = this;
            var mview = $(".js-mv-resources");

            var imageList = data.data.imageList;
            var videoList = data.data.videoList;
            var audioList = data.data.audioList;
            var fileList = data.data.fileList;

            var navs = [];
            if (imageList && imageList.length){
                navs.push('<li>场馆图片</li>');
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
                navs.push('<li>场馆视频</li>');
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
                navs.push('<li>场馆音频</li>');
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
                navs.push('<li>场馆文件</li>');
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

        mvRooms: function(data){
            var self = this;
            var view = $(".js-mv-rooms");
            var roomlist = data.data.roomList;
            if (!roomlist || !roomlist.length){
                view.parents(".js-mv-none").hide();
                return;
            }
            $(".js-mv-roomnumber").text(roomlist.length);

            var temp = '<li>' +
                '    <a target="_blank">' +
                '    <img width="185" height="123">' +
                '    <em></em>' +
                '    </a>' +
                '</li>';
            for (var i in roomlist){
                var room = roomlist[i];
                var item = $(temp);
                view.append(item);
                //item.find("a").attr("href", self.basePath+'/agdcgfw/venroominfo?roomid='+room.itemId);
                //处理跳转到2期外部供需的场馆活动室详情
                item.find("a").attr("href", self.apiWbgx+'/'+self.apiSite+'/cg/venueroom?id='+room.itemId+'');
                item.find("img").attr("src", WhgComm.getImg300_200(data.imgPath+room.image));
                item.find("em").text(room.title);
                if (i==0){
                    item.addClass("active");
                }
            }

            $('.eventliebiao_done').addClass("eventliebiao");
            $('.eventliebiao_done').sly({
                itemNav: "smart",
                easing: "easeOutExpo",
                prevPage: ".pre-left",
                nextPage: ".pre-right",
                horizontal: 1,
                touchDragging: 1,
                dragContent: 1
            });
        },

        mvOther: function(data){
            $(".js-mv-src").attr("src", WhgComm.getImg750_500(data.imgPath+data.data.image));
            var targetView = $(".js-mv-em-etag");
            if (data.data.tag && data.data.tag != ''){
                var etags = data.data.tag;
                etags = etags.split(/\s*,\s*/);
                for(var i in etags){
                    targetView.append('<em>'+etags[i]+'</em>');
                }
            }else{
                targetView.parents(".js-mv-none").hide();
            }
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
        }
    }
</script>
</body>
</html>