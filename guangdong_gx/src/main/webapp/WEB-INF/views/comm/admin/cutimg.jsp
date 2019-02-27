<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>图片裁剪</title>
    <link rel="stylesheet" href="${basePath}/static/common/jcrop/css/jquery.Jcrop.min.css">
    <script type="text/javascript" src="${basePath }/static/easyui/jquery.min.js" ></script>
    <script type="text/javascript" src="${basePath}/static/common/jcrop/js/jquery.Jcrop.min.js"></script>
</head>
<body>

<img id="whg_img_cut" src="${param.imgurl}"/>

<script type="text/javascript">
var JcropApi;
var cutw  = parseInt('${param.w}', 10);//裁剪宽高
var cuth  = parseInt('${param.h}', 10);//裁剪高度
var wh = parseInt('${param.wh}', 10);//父窗口高度
var ww = parseInt('${param.ww}', 10);//父窗口宽度
var width = ww-50;
var height = wh-140;

$(function () {
    window.setTimeout(function () {
        $('#whg_img_cut').Jcrop({allowSelect: false, allowResize:false, bgOpacity: 0.5, bgColor: 'white', boxWidth: width, boxHeight:height}, function(){
            JcropApi = this;
            JcropApi.setSelect([0, 0, cutw, cuth]);
            JcropApi.setOptions({ bgFade: true});
            JcropApi.ui.selection.addClass('jcrop-selection');
        });
    },500);
});
</script>
</body>
</html>
