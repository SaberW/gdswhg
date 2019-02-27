<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>资源浏览</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <link rel="stylesheet" href="https://g.alicdn.com/de/prismplayer/2.1.0/skins/default/aliplayer-min.css" />
    <script type="text/javascript" src="https://g.alicdn.com/de/prismplayer/2.1.0/aliplayer-min.js"></script>
    <script type="text/javascript">
        //<![CDATA[
        $(document).ready(function(){
            var resurl = '${param.resurl}';
            var player = new Aliplayer({
                id: "J_prismPlayer", // 容器id
                source: resurl,// 视频地址
                autoplay: false,    //自动播放：否
                width: "660px",       // 播放器宽度
                height: "390px"      // 播放器高度
            });
        });
        //]]>
    </script>
</head>
<body>
    <div class="live-cont" id="liveContainer"><div id="J_prismPlayer" class="prism-player"></div></div>
</body>
</html>