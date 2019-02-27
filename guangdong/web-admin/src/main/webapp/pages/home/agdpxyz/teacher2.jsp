<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<%@include file="/pages/comm/agdhead.jsp"%>
<title>广东省文化馆-培训驿站-培训师资</title>
<link href="${basePath }/static/assets/css/train/teach.css" rel="stylesheet">
<script src="${basePath }/static/assets/js/train/teach.js"></script>
<script type="text/javascript">

$(function(){
	pageTool.init();
});

var pageTool = {
    apiSite: '${apiSite}',
    apiRoot: '${apiRoot}',

    //查询条件层选择器
    queryDiv: "div.categoryChange",

	init: function(){
        var self = this;
        //数据模板
        var jsDome = $(".js-item-dome-li");
        self.jsItemDomeLi = jsDome.prop("outerHTML");
        self.targetUL = jsDome.parents("ul");

        self.getCultInfo();

        //处理点击类型的查询条件
        $(self.queryDiv).on("click.pname", "a[pname]", function(){
			$(this).parents("span.item").addClass("active").siblings().removeClass("active");
			self.loadData();
        });

        //查询点击
        $("#btn_sub").on('click', function(e){
            e.preventDefault();
            self.loadData();
        });

        //回车事件
        $("body").keydown(function() {
            var isFocus = $("#title").is(":focus");
            if (event.keyCode == "13" && isFocus) {
                self.loadData();
            }
        });
	},

    getCultInfo: function(){
        var self = this;
        $.get(self.apiRoot+'api/outer/comm/getCultBySite',
            {cultsite: self.apiSite},
            function(data){
                if (data.data){
                    self.cultInfo = data.data;
                    self.getTypes();
                    self.getAreas();
                    self.loadData();
                }
            }, 'json');
    },

    getTypes: function(){
        var self = this;
        $.get(self.apiRoot+'api/outer/comm/getTypes',
            { cultsite: self.apiSite, type: 1 },
            function(data){
                if (data && data.rows){
                    var targetDiv = $("#arttype");
                    for (var i in data.rows){
                        var row = data.rows[i];
                        var temp = $('<span class="item"><a href="javascript:void(0)" pname="arttype" pvalue=""></a></span>');
                        temp.find("a").attr({pvalue:row.id}).text(row.name);
                        targetDiv.append(temp);
                    }
                }
            }, 'json');
    },

    getAreas: function(){
        var self = this;
        var _area = self.cultInfo.level == 1? self.cultInfo.province : self.cultInfo.city;
        $.get(self.apiRoot+'api/outer/comm/getAreas',
            { area: _area},
            function(data){
                if (data && data.rows){
                    var targetDiv = $("#area");
                    for (var i in data.rows){
                        var row = data.rows[i];
                        var temp = $('<span class="item"><a href="javascript:void(0)" pname="area" pvalue=""></a></span>');
                        temp.find("a").attr({pvalue:row.name}).text(row.name);
                        targetDiv.append(temp);
                    }
                }
            }, 'json');
    },

    getQueryParams: function(){
        var self = this;
        var params = {};
        //选中项
        $(self.queryDiv).find(".active:visible").each(function(){
            var paramA = $(this).find('a');
            var name = paramA.attr('pname');
            var value = paramA.attr('pvalue');
            if (name && value){
                params[name] = value;
            }
        });
        //输入项
        var title = $(self.queryDiv).find("#title").val();
        if (title){
            params.title = title;
        }

        return params;
    },

	loadData: function(page, pageSize){
        var self = this;
        var params = self.getQueryParams();
        params.page = page||1;
        params.pageSize = pageSize||10;
        //转换参数
        var cult = self.cultInfo;
        if (!cult || !cult.id){
            return;
        }

        params.cultid = cult.id;
        params.province = cult.province;
        if (params.area && cult.level == 1){
            params.city = params.area;
            delete params.area;
        }

        $.ajax({
            url: self.apiRoot+'api/px/tea/list',
            type: "POST",
            data: params,
            success : function(data){
                self.showData(data);
                genPagging('whgPagging', params.page, params.pageSize, data.total||0, function(page, pageSize){
                    self.loadData(page, pageSize)
                });
                if(data.total == 0){
                    $(self.targetUL).find(".public-no-message").show();
                }else{
                    $(self.targetUL).find(".public-no-message").hide();
                }
            }
        });
	},

	showData: function(data){
        var self = this;
        //显示数据项
        self.targetUL.children("li").remove();
        for (var i in data.rows){
            var row = data.rows[i];
            var item = $(self.jsItemDomeLi);
            self.targetUL.append(item);
            item.removeClass("js-item-dome-li").show();

            item.find(".js-mv-text").each(function(){
                var mvkey = $(this).attr("mvkey");
                if (!mvkey || mvkey=='') return true;
                var isHtml = $(this).hasClass("mv-html");
                if (isHtml){
                    $(this).html(row[mvkey]||'');
                }else{
                    $(this).text(row[mvkey]||'');
                }
                if (row[mvkey] && row[mvkey]!=''){
                    item.find("[js-mv-none='"+mvkey+"']").show();
                    item.find("[js-mv-unnone='"+mvkey+"']").hide();
                }else{
                    item.find("[js-mv-none='"+mvkey+"']").hide();
                    item.find("[js-mv-unnone='"+mvkey+"']").show();
                }
            });

            item.find("img").attr("src", WhgComm.getImg750_500(data.imgPath+row.teacherpic));
            var _href = item.find("a").attr("href");
            item.find("a").attr("href", _href+row.id);
        }
	}
}
</script>
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

<!--主体开始-->
<div class="main-info-bg main-info-no-padding main-info-bgColorW">
  <div class="main-info-container">
    <div class="categoryChange">
        <div class="row clearfix">
            <div class="title">区域</div>
            <div class="adrList" id="area">
				<span class="item active"><a href="javascript:void(0)" pname="area" pvalue="">全部</a></span>
            </div>
        </div>
        <div class="row clearfix">
            <div class="title">类型</div>
            <div class="adrList adrList1" id="arttype">
				<span class="item active"><a href="javascript:void(0)" pname="arttype" pvalue="">全部</a></span>
            </div>
            <div class="searchCont">
                <input placeholder="搜点什么..." id="title">
                <button type="button" id="btn_sub"></button>
            </div>
        </div>
    </div>
    <div class="teacherList">
    	<ul>
			<div class="public-no-message "></div>
			<li class="js-item-dome-li">
				<div class="img">
					<a target="_blank" href="${apiZxpx}${apiSite}/pxsz/detail?&id="><img width="136" height="136"></a>
				</div>
				<div class="info">
					<a target="_blank" href="${apiZxpx}${apiSite}/pxsz/detail?&id=">
						<h2 class="js-mv-text" mvkey="name"></h2>
						<p class="type" js-mv-none="typname">专长 :<span class="js-mv-text" mvkey="typname"></span></p>
						<p js-mv-none="teacherdesc">简介 :<span class="js-mv-text" mvkey="teacherdesc"></span></p>
					</a>
					<div class="arrow"></div>
				</div>
			</li>

    		<%--<c:choose>
			   <c:when test="${rows != null && fn:length(rows) > 0 }">  
					<c:forEach items="${rows }" var="row" varStatus="s">
			        	<li>
			            	<div class="img">
			                	<a href="${basePath }/agdpxyz/teacherinfo?id=${row.teacherid}"><img src="${imgServerAddr}${row.teacherpic}" width="136" height="136"></a>
			                </div>
			                <div class="info">
			                	<a href="${basePath }/agdpxyz/teacherinfo?id=${row.teacherid}">
				                	<h2>${row.teachername }</h2>
									<c:if test="${not empty row.teachertype }">
										<p class="type">专长 :<span>${row.teachertype }</span></p>
									</c:if>
									<c:if test="${not empty row.teacherintroduce }">
				                    	<p>简介 :<span>${row.teacherintroduce }</span></p>
									</c:if>
			                    </a>
			                    <div class="arrow"></div>
			                </div>
			            </li>
	    			</c:forEach>      
			   </c:when>
			   <c:otherwise> 
			   		<div class="public-no-message "></div>
			   </c:otherwise>
			</c:choose>--%>
        </ul>
        
        <!-- 分页 -->
		<div class="green-black" id="whgPagging"></div>
		<!-- 分页-END -->
    </div>
  </div>
</div>
<!--主体结束-->

<!--公共主底部开始-->
<%@include file="/pages/comm/agdfooter.jsp"%>
<!--公共主底部结束-END-->
</body>
</html>