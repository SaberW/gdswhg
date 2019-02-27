<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <%@include file="/pages/comm/agdhead.jsp"%>
    <title>广东省文化馆-场馆</title>
    <link href="${basePath }/static/assets/css/field/fieldList.css" rel="stylesheet">
    <script src="${basePath }/static/assets/js/field/fieldList.js"></script>

    <script src="${basePath }/static/common/js/whg.maps.js"></script>
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
                <li class="active last"><a href="${basePath }/agdcgfw/index">场馆服务</a></li>
            </ul>
        </div>
    </div>
</div>
<!--公共主头部结束-END-->

<!--主体开始-->
<div class="venue-main">
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
            <div class="title">类型</div>
            <div class="adrList" id="etype">
                <span class="item active"><a href="javascript:void(0)" pname="type" pvalue="">全部</a></span>
            </div>
        </div>
        
        <div class="row clearfix">
            <div class="title">标签</div>
            <div class="adrList" id="etag">
                <span class="item active"><a href="javascript:void(0)" pname="tag" pvalue="">全部</a></span>
            </div>
        </div>
        <div class="row clearfix">
           	<div class="title">状态</div>
            <div class="adrList adrList1">
                <span class="item active"><a href="javascript:void(0)" pname="isUse" pvalue="">全部</a></span>
                <span class="item"><a href="javascript:void(0)" pname="isUse" pvalue="1">可预定</a></span>
            </div> 
            
            <div class="searchCont">
                <input placeholder="搜点什么..." id="title">
                <button type="submit" id="btn_sub"></button>
            </div>
        </div>
        <div style="margin-top: 20px;color: #959595">广东省数字文化馆之“‘文化在线’——广东公共数字文化联盟”平台已开通，场馆预订请前往</div>
    </div>
</div>
<div class="main clearfix">
    <div class="venue-main-left">
    	<ul class="group" id="connet">
            <div class="public-no-message none"></div>
            <li class="item clearfix js-item-dome-li" style="display: none;">
                <div class="img">
                    <a><img style="width: 280px; height: 190px;"></a>
                </div>
                <div class="info">
                    <h2><a>罗店镇社区文化活动中心</a></h2>
                    <div class="info-main">
                        <div class="row-1">
                            <p class="type clearfix"></p>
                            <p class="adr clearfix"><span class="tt">地址 :</span><span class="desc-text">广东市南城区宏北路南城社保分局、交通分局旁</span><a href="#">[查看地图]</a></p>
                            <p class="cate clearfix"><span class="tt">类型 :</span><span class="desc-text">其它</span></p>
                            <p class="desc clearfix"><span class="tt">描述 :</span><span class="desc-text">由藏书区、借阅区、咨询服务区、公共活动与辅助服务区、业务区、行政办公区、技术设备区和后勤保障区等几个功能部分组成</span></p>
                        </div>
                    </div>
                </div>
            </li>
        </ul>
        
        <div class="green-black" id="paging"> 
        </div>
        
    </div>
</div>
<!--主体结束-->

<!--公共主底部开始-->
<%@include file="/pages/comm/agdfooter.jsp"%>
<!--公共主底部结束-END-->


<script type="text/javascript">

    $(function () {
        pageTool.init();
    });

    var pageTool = {
        apiSite: '${apiSite}',
        apiRoot: '${apiRoot}',
        apiWbgx: '${apiWbgx}',
        //查询条件层选择器
        queryDiv: "div.categoryChange",

        //入口
        init: function(){
            var self = this;
            if (/\/$/.test(this.apiWbgx)){
                this.apiWbgx = this.apiWbgx.replace(/\/$/, '');
            }

            //数据模板
            var jsDome = $(".js-item-dome-li");
            self.jsItemDomeLi = jsDome.prop("outerHTML");
            self.targetUL = jsDome.parents("ul");

            self.getCultInfo();

            //处理点击类型的查询条件
            $(self.queryDiv).on("click.pname", "a[pname]", function(){
                /*var _li = $(this).parents("li");
                if (_li.length){
                    //显隐动画之后再查数据
                    setTimeout(function(){self.loadData()}, 500);
                }else{*/
                    $(this).parents("span.item").addClass("active").siblings().removeClass("active");
                    self.loadData();
                /*}*/
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
                params.content = title;
            }

            return params;
        },

        //查询数据
        loadData: function(page, pageSize){
            var self = this;
            var params = self.getQueryParams();
            params.page = page||1;
            params.pageSize = pageSize||10;
            //转换参数
            var cult = self.cultInfo;
            if (!cult || !cult.id){
                //setTimeout(function(){self.loadData(page, pageSize)}, 300);
                return;
            }

            params.cultid = cult.id;
            params.province = cult.province;
            if (params.area && cult.level == 1){
                params.city = params.area;
                delete params.area;
            }

            $.ajax({
                url: self.apiRoot+'api/venue/list',
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

                //item.find(".img a, .info h2 a").attr("href", "${basePath}/agdcgfw/venueinfo?venid="+row.itemId);
                var _href = self.apiWbgx+'/'+self.apiSite+'/cg/detail?id='+row.itemId+'';
                item.find(".img a, .info h2 a").attr({"href": _href, "target":"_blank"});

                item.find(".img img").attr("src", WhgComm.getImg750_500(data.imgPath+row.image) );
                item.find(".info h2 a").text(row.title);
                item.find(".info p.adr span.desc-text").text(row.address);
                item.find(".info p.cate span.desc-text").text( row.type );
                item.find(".info p.desc span.desc-text").text(row.intro);

                //item.find(".info p.type").html('<span class="d">比赛</span>');
                var etagP = item.find(".info p.type");
                var etags = row.tag || '';
                etags = etags.split(/\s*,\s*/);
                for(var i in etags){
                    if (!etags[i]) continue;
                    etagP.append('<span class="d">'+etags[i]+'</span>')
                }
                var mapParams = {address:row.address,longitude:row.longitude, latitude:row.latitude };
                item.find(".info p.adr a").off("click.maps").on("click.maps",mapParams,function(evt){
                    WhgMap.openMap(evt.data.address, evt.data.longitude, evt.data.latitude);
                })
            }
        },

        //取文化馆信息
        getCultInfo: function(){
            var self = this;
            $.get(self.apiRoot+'api/outer/comm/getCultBySite',
                {cultsite: self.apiSite},
              function(data){
                    if (data.data){
                        self.cultInfo = data.data;
                    }

                    self.getTypes();
                    self.getTags();
                    //self.getAreas(data.data);

                    self.loadData();
                }, 'json');
        },
        //取区域
        getAreas: function(cult){
            var self = this;
            var _area = cult.level == 1? cult.province : cult.city;
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
        //取类型
        getTypes: function(){
            var self = this;
            $.get(self.apiRoot+'api/outer/comm/getTypes',
                { cultsite: self.apiSite, type: 2 },
                function(data){
                    if (data && data.rows){
                        var targetDiv = $("#etype");
                        for (var i in data.rows){
                            var row = data.rows[i];
                            var temp = $('<span class="item"><a href="javascript:void(0)" pname="type" pvalue=""></a></span>');
                            temp.find("a").attr({pvalue:row.id}).text(row.name);
                            targetDiv.append(temp);
                        }
                    }
                }, 'json');
        },
        //取标签
        getTags: function(){
            var self = this;
            $.get(self.apiRoot+'api/outer/comm/getTags',
                { cultsite: self.apiSite, type: 2 },
                function(data){
                    if (data && data.rows){
                        var targetDiv = $("#etag");
                        for (var i in data.rows){
                            var row = data.rows[i];
                            var temp = $('<span class="item"><a href="javascript:void(0)" pname="tag" pvalue=""></a></span>');
                            temp.find("a").attr({pvalue:row.id}).text(row.name);
                            targetDiv.append(temp);
                        }
                    }
                }, 'json');
        }
    }
</script>
</body>
</html>