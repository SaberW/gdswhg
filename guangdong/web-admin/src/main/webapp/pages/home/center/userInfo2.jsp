<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath(); request.setAttribute("basePath", basePath); %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge"> 
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="author" content="">
<title>广东省文化馆-用户中心</title>
<link href="${basePath }/static/assets/css/public/reset.css" rel="stylesheet">
<link href="${basePath }/static/assets/css/userCenter/userCenter.css" rel="stylesheet">
<script src="${basePath }/static/assets/js/public/jquery-1.11.0.min.js"></script>
<script src="${basePath }/static/assets/js/public/jquery-migrate-1.0.0.js"></script>
<!--[if lt IE 9] >
<script src="${basePath }/static/assets/js/plugins/ie/IE9.js"></script> 
<! [endif]]-->
</head>
<body>

<!-- 公共头部 -->
<%@include file="/pages/comm/agdtop.jsp"%>
<!-- 公共头部END -->

<!-- 公共绑定 -->
<%@include file="/pages/comm/comm_center.jsp"%>
<!-- 公共绑定END -->

<div class="main clearfix">

	<div class="leftPanel">
		<ul>
			<!--用户中心导航-->
			<%@include file="/pages/comm/ucnav.jsp"%>
			<!--用户中心导航END-->
		</ul>
	</div>
  
	<div class="rightPanel">
		<ul class="commBtn clearfix">
			<li class="active">基本信息</li>
		</ul>
		<form id="inputForm">
			<!-- 基本信息 -->
			<div class="formContainer">
				<dl class="clearfix">
					<dt class="float-left">昵称</dt>
					<dd class="float-left">
						<input class="in-txt"  name="nickname" id="nickname" value="${wbgx_sessUserKey.nickName}" readonly>
						<em></em>
					</dd>
				</dl>
				<dl class="clearfix" style="display: none">
					<dt class="float-left">性别</dt>
					<dd class="float-left">
						<div class="">
							<input type="radio" value="1" name="sex" id="r1" disabled> <span>男</span>
							<input type="radio" value="0" name="sex" id="r2" disabled> <span>女</span>
						</div>
					</dd>
				</dl>

                <dl class="clearfix" style="display: none">
                    <dt class="float-left">职业</dt>
                    <dd class="float-left">
                        <input class="in-txt"  name="job" id="job" value="" readonly>
                        <em></em>
                    </dd>
                </dl>

				<dl class="clearfix">
					<dt class="float-left">手机号码</dt>
					<dd class="float-left">
						<div class="box float-left">
							<input class="in-txt" type="text" name="phone" id="phone" value="${wbgx_sessUserKey.mobile}" readonly>
							<em></em>
						</div>
					</dd>
				</dl>
                
                <dl class="clearfix">
                    <dt class="float-left">&nbsp;</dt>
                    <dd class="float-left">
                        <div class="goNext float-left" id="modify" onclick="showEdit()">修 改</div>
                    </dd>
                </dl>
			</div>
			<!-- 基本信息END -->
		</form>
	</div>
</div>

<!--底部-->
<%@include file="/pages/comm/agdfooter.jsp"%>
<!--底部END-->

<!-- core public JavaScript -->
<script src="${basePath }/static/assets/js/public/formUI.js"></script>
<script src="${basePath }/static/assets/js/plugins/tipso/tipso.js"></script><script src="${basePath }/static/assets/js/public/rong-dialog.js"></script>
<script src="${basePath }/static/assets/js/userCenter/public.js"></script>
<script src="${basePath }/static/assets/js/plugins/laydate.dev.min.js"></script>
<%--<script src="${basePath }/pages/home/center/js/userInfo.js"></script>--%>
<script src="${basePath }/static/assets/js/public/comm.js"></script>

<script>
    $(function () {
        var userId = '${userId}';
        if (!userId || userId == ''){
            return;
        }
    })

	function showEdit() {
        window.location.href = '${apiWbgx}${apiSite}/center/info';
    }
</script>
</body>
</html>
