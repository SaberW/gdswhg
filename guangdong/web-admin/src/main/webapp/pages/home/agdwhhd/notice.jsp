<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<%@include file="/pages/comm/agdhead.jsp"%>
	<title>广东省文化馆-培训驿站-培训资讯</title>
	<link href="${basePath }/static/assets/css/train/notice.css" rel="stylesheet">
	<script src="${basePath }/static/assets/js/train/notice.js"></script>
	<script type="text/javascript">
        /** page onload event */
        $(function(){
            //加载分页工具栏
            loadData();
        });

        function loadData(page, rows){
            var page = page || 1;
            var rows = rows || 10;
            $.ajax({
                type: 'post',
                url: '${apiRoot}api/outer/comm/getInfos',
                data: {
                    cultsite:'${apiSite}',
                    type:"${type}",
                    page: page,
                    rows: rows
                },
                success: function(data){
                    if (!data) return;
                    $("#showInfo").empty();
                    showInfoList(data);
                    genPagging('whgPagging', page, rows, data.total, loadData);
                }
            });
        }

        function  showInfoList(data) {
            var infoHtml='';
            if (data.code == 0) {
                if (data.rows.length > 0) {
                    for (var i in data.rows) {
                        var model = data.rows[i];
                        var year = moment(model.clnfcrttime).format('YYYY');
                        var day = moment(model.clnfcrttime).format('MM-DD');
                        infoHtml+='<li>';
                        infoHtml+='<div class="timeCont">';
                        infoHtml+='<p class="month">'+day+'</p><p class="year">'+year+'</p><div class="circular"></div>';
                        infoHtml+='</div><div class="notice-list-info">';
                        infoHtml+='<div class="arrow"></div>';
                        infoHtml+='<div class="info"><h2>';
                        infoHtml+='<a target="_blank" href="${apiWbgx}${apiSite}/zx/detail?id='+model.clnfid+'">'+model.clnftltle+'</a>';
                        infoHtml+='</h2>';
                        infoHtml+='<p>'+model.clnfintroduce+'</p>';
                        infoHtml+='<span class="more"><a href="${apiWbgx}${apiSite}/zx/detail?id='+model.clnfid+'">查看详情</a></span>';
                        infoHtml+='</div></div></li>';
                    }
                }else {
                    infoHtml+='<div class="public-no-message"></div>';
                }
                $("#showInfo").append(infoHtml);
            }
        }
	</script>
</head>
<body>
<!-- 公共头部 -->
<%@include file="/pages/comm/agdtopsmall.jsp"%>
<!-- 公共头部-END -->

<!--公共主头部-->
<div id="header-fix">
	<div class="header-nav-bg">
		<div class="header-nav">
			<div class="logo-small">
				<a href="${basePath }/home"><img src="${basePath }/static/assets/img/public/logoSmall.png"></a>
			</div>
			<ul>
				<li><a href="${basePath }/agdwhhd/activitylist">活动预约</a></li>
				<li class="active"><a href="${basePath }/agdwhhd/notice">活动公告</a></li>
				<li ><a href="${basePath }/agdwhhd/news">活动资讯</a></li>
				<li  class="last"><a href="${basePath }/agdwhhd/brandlist">品牌活动</a></li>
			</ul>
		</div>
	</div>
</div>
<!--公共主头部-END-->

<!-- 主体 -->
<div class="main-info-bg main-info-no-padding main-info-bgColorW">
	<div class="main-info-container">
		<div class="main-noticeList-container">
			<!-- 活动资讯 -->
			<ul id="showInfo">
			</ul>
			<!-- 活动资讯-END -->

			<!-- 分页 -->
			<div class="green-black" id="whgPagging"></div>
			<!-- 分页-END -->
		</div>
	</div>
</div>
<!-- 主体-END -->

<!--公共主底部开始-->
<%@include file="/pages/comm/agdfooter.jsp"%>
<!--公共主底部结束-END-->
</body>
</html>