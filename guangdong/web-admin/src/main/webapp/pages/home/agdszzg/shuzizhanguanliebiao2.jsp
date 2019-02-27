<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <%@include file="/pages/comm/agdhead.jsp"%>
    <title>广东省数字文化馆-数字展馆</title>
    <link href="${basePath}/static/assets/css/resource/shuzizhanguan.css" rel="stylesheet">
    <script src="${basePath}/static/assets/js/plugins/roll/jquery.sly.min.js" type="text/javascript"></script>
    <script src="${basePath}/static/assets/js/science/science.js"></script>

</head>
<body>
<!--公共主头部开始-->
<%@include file="/pages/comm/agdtopsmall.jsp"%>

<div id="header-fix">
    <div class="header-nav-bg">
        <div class="header-nav">
            <div class="logo-small"> <a href="${basePath}/home"><img src="${basePath}/static/assets/img/public/logoSmall.png"></a> </div>
            <ul>
                <li class="last"><a href="${basePath }/agdszzg/index">数字展馆</a></li>
            </ul>
        </div>
    </div>
</div>
<!--公共主头部结束-->
<!--主体开始-->
<div class="sshd-main" style="margin-top:140px;">
    <div class="info clearfix" style="height:auto; margin-bottom:50px;">
        <ul class="l">
            <%--<c:if test="${not empty exh}">
                <c:forEach items="${exh}" var="row" varStatus="s">
                    <c:if test="${s.count%2==1}">
                        <li>
                            <div class="ss-main">
                                <a href="${basePath}/agdszzg/info?exhid=${row.exhid}"></a>
                                <div class="img">
                                    <img src="${imgServerAddr}/${row.exhpic}">
                                </div>
                                <h2>${row.exhtitle}</h2>
                                <p class="t">时间：<fmt:formatDate value="${row.exhstime}" pattern="yyyy-MM-dd"/> </p>
                                <c:choose>
                                    <c:when test="${fn:length(row.exhdesc) > 100}">
                                        <p class="i">${fn:substring(row.exhdesc, 1, 100)}......</p>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="i">${row.exhdesc}</p>
                                    </c:otherwise>
                                </c:choose>
                                <em></em>
                            </div>
                        </li>
                    </c:if>
                </c:forEach>
            </c:if>--%>
        </ul>
        <ul class="r">
            <%--<c:forEach items="${exh}" var="row" varStatus="s" >
                <c:if test="${s.count%2==0}">
                    <li>
                        <div class="ss-main">
                            <a href="${basePath}/agdszzg/info?exhid=${row.exhid}"></a>
                            <div class="img">
                                <img src="${imgServerAddr}/${row.exhpic}">
                            </div>
                            <h2>${row.exhtitle}</h2>
                            <p class="t">时间：<fmt:formatDate value="${row.exhstime}" pattern="yyyy-MM-dd"/> </p>
                            <c:choose>
                                <c:when test="${fn:length(row.exhdesc) > 100}">
                                    <p class="i">${fn:substring(row.exhdesc, 1, 100)}......</p>
                                </c:when>
                                <c:otherwise>
                                    <p class="i">${row.exhdesc}</p>
                                </c:otherwise>
                            </c:choose>
                            <em></em>
                        </div>
                    </li>
                </c:if>
            </c:forEach>--%>
        </ul>
    </div>
    <!-- 分页 -->
    <div class="green-black" id="whgPagging"></div>
    <!-- 分页-END -->


</div>

<!--主体结束-->

<!--底部开始-->
<%@include file="/pages/comm/agdfooter.jsp"%>
<!--底部结束-->
<a class="to-top"></a>


<script>

    $(function(){
        pageTool.init();
    });

    var pageTool = {
        apiSite: '${apiSite}',
        apiRoot: '${apiRoot}',
        apiWbgx: '${apiWbgx}',

        init: function(){
            var self = this;
            if (/\/$/.test(this.apiWbgx)){
                this.apiWbgx = this.apiWbgx.replace(/\/$/, '');
            }

            self.getCultInfo();
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

                    self.loadData();
                }, 'json');
        },

        //取数字展馆信息
        loadData: function(page, pageSize){
            var index = page || 1;
            var size = pageSize || 10;
            var self = this;
            var cultid = '';
            if (self.cultInfo && self.cultInfo.id){
                cultid = self.cultInfo.id;
            }
            $.get(self.apiRoot+'api/digitalExhibition/list',
                {index: index, size: size, cultid: cultid},
                function(data){
                    self.showNewsItems(data);

                    genPagging('whgPagging', index, size, data.total||0, function(page, pageSize){
                        self.loadData(page, pageSize)
                    });
                },
                'json'
            );
        },

        //显示项
        showNewsItems: function(data){
            if (!data.rows || data.rows.length == 0){
                return;
            }
            var self = this;
            var ULoll = $(".info ul.l");
            var ULorr = $(".info ul.r");
            ULoll.empty();
            ULorr.empty();

            var itemTemp = '<li>' +
                '    <div class="ss-main">' +
                '        <a ></a>' +
                '        <div class="img">' +
                '            <img >' +
                '        </div>' +
                '        <h2></h2>' +
                '        <p class="t">时间： </p>' +
                '        <p class="i"></p>' +
                '        <em></em>' +
                '    </div>' +
                '</li>';

            for(var i in data.rows){
                var UL = i%2 == 0? ULoll : ULorr;

                var row = data.rows[i];
                var _href = self.apiWbgx+'/'+self.apiSite+'/zg/detail?id='+row.id;

                var temp = $(itemTemp);
                temp.find("img").attr({ src: data.imgPath+row.imgurl });
                temp.find("a").attr({ "href": _href, "target":"_blank" });
                temp.find("h2").text(row.title);

                var datetime = row.publishdate? row.publishdate : row.crtdate;
                var _t = new Date(datetime).Format("yyyy-MM-dd");
                var remark = removeHTMLTag(row.remark);
                if (remark && remark.length>100) {
                    remark = remark.replace(/^(.{0,100}).*$/g, '$1......');
                }
                temp.find("p.t").text('时间： '+_t);
                temp.find("p.i").text(removeHTMLTag(remark));

                UL.append(temp);
            }
        }
    };

    //给消息过滤HTML标签
    function removeHTMLTag(str) {
        str = str.replace(/<\/?[^>]*>/g,''); //去除HTML tag
        str = str.replace(/[ | ]*\n/g,'\n'); //去除行尾空白
        //str = str.replace(/\n[\s| | ]*\r/g,'\n'); //去除多余空行
        str=str.replace(/&nbsp;/ig,'');//去掉&nbsp;
        return str;
    }
</script>

</body>
</html>