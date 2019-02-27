<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <%@include file="/pages/comm/agdhead.jsp"%>
    <title></title>
</head>
<body class="oiplayer-example">
    <video width="700" height="354" poster="${imgUrl}" controls oncontextmenu="return false" autoplay="autoplay">
        <source type="${type}" src="${url}" />
    </video>

<script>
    $(function () {
        $('body.oiplayer-example').oiplayer({
            server: '${basePath}',
            jar: '/static/assets/js/plugins/oiplayer-master/plugins/cortado-ovt-stripped-0.6.0.jar',
            flash: '/static/assets/js/plugins/oiplayer-master/plugins/flowplayer-3.2.7.swf',
            controls: 'top',
            log: 'info'
        });

        $(".oiplayer-example").on("click", ".screen a", function(){
            var tag = $(window.parent.document).find("#vedio_cont");
            var ifm = tag.find("iframe");
            var js_prop = tag.data("js_prop");
            if (js_prop){
                tag.data("js_prop", null);

                tag.css({width:js_prop.tag_width, height:js_prop.tag_height, left:js_prop.tag_left, top:js_prop.tag_top});
                ifm.attr("width", js_prop.ifm_width);
                ifm.attr("height", js_prop.ifm_height);
                $("video").css("width", ifm.css("width"));
                $("video").css("height", ifm.css("height"));

                return;
            }

            var prop = {
                tag_left: tag.css("left"),
                tag_top: tag.css("top"),
                tag_width: tag.css("width"),
                tag_height: tag.css("height"),
                ifm_width: ifm.attr("width"),
                ifm_height: ifm.attr("height")
            };
            tag.data("js_prop", prop);

            tag.css({width:'100%', height:'100%', left:0, top:0});
            ifm.attr("width", window.screen.availWidth);
            ifm.attr("height", window.screen.availHeight-200);
            $("video").css("width", ifm.css("width"));
            $("video").css("height", ifm.css("height"));

        })
    });
</script>
</body>
</html>
