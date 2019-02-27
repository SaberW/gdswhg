<%@ page import="java.text.SimpleDateFormat" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    java.util.Date date = new java.util.Date();
    request.setAttribute("now", sdf.format(date) );
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<%@include file="/pages/comm/agdhead.jsp"%>
<title>广东省文化馆-培训驿站-在线报名</title>
    <link href="${basePath }/static/assets/css/field/fieldList.css" rel="stylesheet">
    <script src="${basePath }/static/assets/js/field/fieldList.js"></script>
<link href="${basePath }/static/assets/css/train/registration.css" rel="stylesheet">
<script src="${basePath }/static/assets/js/train/registration.js"></script>
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
                var _li = $(this).parents("li");
                if (_li.length){
                    //显隐动画之后再查数据
                    setTimeout(function(){self.loadData()}, 500);
                }else{
                    $(this).parents("span.item").addClass("active").siblings().removeClass("active");
                    self.loadData();
                }
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
                        self.getAreTypes();
                        //self.getAreas();
                        self.loadData();
                    }
                }, 'json');
        },

		getTypes: function(){
            var self = this;
            $.get(self.apiRoot+'api/outer/comm/getTypes',
                { cultsite: self.apiSite, type: 5 },
                function(data){
                    if (data && data.rows){
                        var targetDiv = $("#etype");
                        for (var i in data.rows){
                            var row = data.rows[i];
                            var temp = $('<span class="item"><a href="javascript:void(0)" pname="etype" pvalue=""></a></span>');
                            temp.find("a").attr({pvalue:row.id}).text(row.name);
                            targetDiv.append(temp);
                        }
                    }
                }, 'json');
		},
		getAreTypes: function(){
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
            params.pageSize = pageSize||12;
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
                url: self.apiRoot+'api/px/list',
                type: "POST",
                data: params,
                success : function(data){
                    self.showData(data);
                    genPagging('paging', params.page, params.pageSize, data.total||0, function(page, pageSize){
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

                row.time = new Date(row.starttime).Format("yyyy-MM-dd") +' 至 '+new Date(row.endtime).Format("yyyy-MM-dd");
                row.bmnum = row.enrolcount +' / '+row.maxnumber;
                if(row.age){
                    row.age = row.age.split(',');
                    row.age = row.age[0]+'-'+row.age[1];
                }
                var maxApplyNum = parseInt(row.maxnumber);
                var lastApplyNum = maxApplyNum - row.enrolcount;
                row.bmstate = '';
                if(data.data > row.enrollendtime ){
                    row.bmstate = '报名结束';
                }else if(lastApplyNum == 0){
                    row.bmstate = '名额已满';
                }else{ //未报名
                    if(row.enrollstarttime > data.data){
                        row.bmstate = '即将开始';
                    }else if(row.enrollstarttime < data.data && data.data < row.enrollendtime){
                        row.bmstate = row.enrolstate ? '已报名' : '立即报名';
                    }
                }
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

                item.find("img").attr("src", WhgComm.getImg750_500(data.imgPath+row.trainimg));
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
                <li><a href="${basePath }/agdpxyz/teacher">培训师资</a></li>
                <li class="active"><a href="${basePath }/agdpxyz/trainlist">在线报名</a></li>
                <li class="last"><a href="${basePath }/agdpxyz/vod">在线点播</a></li>
               <%--  <li class="last"><a href="${basePath }/agdpxyz/resources">培训资源库</a></li> --%>
            </ul>
        </div>
    </div>
</div>
<!--公共主头部开始-END-->

<!--主体开始-->
<div class="main-info-bg main-info-no-padding main-info-bgColorW">
  <div class="main-info-container">
    <div class="categoryChange">

		<div class="masterRow">
			<ul class="clearfix">
                <li class="active closeAdr"><a href="javascript:void(0)" pname="cultid" pvalue="TOP">省馆</a></li>
                <%--<li class="showAdr"><a href="javascript:void(0)" pname="cultid" pvalue="">全省</a></li>--%>
			</ul>
		</div>

      <%--<div class="row clearfix adrCont none">
        <div class="title">区域</div>
        <div class="adrList" id="area">
            <span class="item active"><a href="javascript:void(0)" pname="area" pvalue="">全部</a></span>
        </div>
      </div>--%>
      
      <div class="row clearfix">
        <div class="title">艺术类型</div>
        <div class="adrList" id="arttype">
            <span class="item active"><a href="javascript:void(0)" pname="arttype" pvalue="">全部</a></span>
        </div>
      </div>

		<div class="row clearfix">
			<div class="title">培训类型</div>
			<div class="adrList" id="etype">
                <span class="item active"><a href="javascript:void(0)" pname="etype" pvalue="">全部</a></span>
			</div>
		</div>
      
      <div class="row clearfix">
        <div class="title">排序</div>
        <div class="adrList adrList1"> 
	        <span class="item active"><a href="javascript:void(0) " pname="sort" pvalue="0">智能排序</a></span>
	        <span class="item "><a href="javascript:void(0) " pname="sort" pvalue="1">即将开始</a></span>
	        <span class="item "><a href="javascript:void(0) " pname="sort" pvalue="2" >最新发布</a></span>
        </div>
        
        <div class="searchCont">
          <input placeholder="搜点什么..." id="title">
          <button type="submit" id="btn_sub"></button>
        </div>
        
      </div>
        <div style="margin-top: 20px;color: #959595">广东省数字文化馆之“‘文化在线’——广东公共数字文化联盟”平台已开通，培训报名请前往</div>
    </div>
    
    <div class="registration container">
        <div class="con">
            <ul class="clearfix">
                <div class="public-no-message none"></div>
                <li class="js-item-dome-li">
                    <a target="_blank" href="${apiZxpx}${apiSite}/xxpx/detail?&id=">
                        <div class="img">
                            <img width="380" height="240"/>
                            <div class="mask"></div>
                        </div>
                    </a>
                    <div class="detail">
                        <h2 class="js-mv-text" mvkey="title"></h2>
                        <!-- <p class="page">余数 :<span class="num"><span>168</span>/200</span></p> -->
                        <p class="adr">地址 :<span class="js-mv-text" mvkey="address"></span></p>
                        <p class="time">时间 :<span class="js-mv-text" mvkey="time"></span></p>
                        <p class="time">报名状态 :<span class="js-mv-text" mvkey="bmstate"></span></p>
                        <p class="time">人数 :<span class="js-mv-text" mvkey="bmnum"></span> </p>

                        <p class="time" js-mv-none="age">年龄段 :<span class="js-mv-text" mvkey="age"></span>岁</p>

                        <p class="time" js-mv-unnone="age">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p>

                    </div>
                </li>
            </ul>
            <!-- 分页栏 -->
			    <div class="green-black" id="paging">
			    </div>
			<!-- 分页栏-END -->
        </div>
    </div>
  </div>
</div>
<!--主体结束-->



<!--公共主底部开始-->
<%@include file="/pages/comm/agdfooter.jsp"%>
<!--公共主底部结束-END-->
</body>
</html>