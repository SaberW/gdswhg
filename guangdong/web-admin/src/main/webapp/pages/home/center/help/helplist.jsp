<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>广东省文化馆-用户中心</title>
    <%@include file="/pages/comm/agdhead.jsp"%>
    <link href="${basePath }/static/assets/css/userCenter/userCenter.css" rel="stylesheet">
    <script src="${basePath }/static/assets/js/plugins/tipso/tipso.js"></script>
    <script src="${basePath }/static/assets/js/userCenter/public.js"></script>
    <script src="${basePath }/static/assets/js/userCenter/activity.js"></script>

    <script>
        $(function(){
            //设置导航高度
            $('.leftPanel').css('minHeight', $('.rightPanel').outerHeight());
        })
    </script>
</head>
<body>
<!-- 公共头部开始 -->
<%@include file="/pages/comm/agdtop.jsp"%>
<!-- 公共头部结束 -->

<!-- 公共绑定开始 -->
<%@include file="/pages/comm/comm_center.jsp"%>
<!-- 公共绑定结束 -->


<div class="main main-boder clearfix">
    <div class="leftPanel">
        <ul id="uull">
            <!--用户中心导航开始-->
            <%@include file="/pages/comm/ucnav.jsp"%>
            <!--用户中心导航结束-->
        </ul>
    </div>
    
    <div class="rightPanel">
        <ul class="commBtn clearfix">
            <li class="active"><a href="${basePath }/center/help">帮助中心</a></li>
        </ul>
        <c:if test="${empty helpList}">
            <div class="sysmsg">
                <div class="ad"></div>
                <p>没有帮助信息</p>
            </div>
        </c:if>
        <c:if test="${not empty helpList}">
        <ul class="group helpList clearfix">
            <c:forEach var="item" items="${helpList}">
                <li>
                    <div class="title"><a href="${basePath }/center/help?id=${item.clnfid}">${item.clnftltle}</a></div>
                </li>
            </c:forEach>
        </ul>
        </c:if>
    </div>
</div>

<!--底部开始-->
<%@include file="/pages/comm/agdfooter.jsp"%>
<!--底部结束-->

</body>
</html>