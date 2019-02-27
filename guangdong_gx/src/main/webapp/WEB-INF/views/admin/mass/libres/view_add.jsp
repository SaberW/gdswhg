<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>上传图片</title>
    <link rel="stylesheet" type="text/css" href="${basePath}/static/lib/style.css"/>
    <script type="text/javascript" src="${basePath }/static/easyui/jquery.min.js" ></script>
    <script type="text/javascript">
        var basePath = '${basePath}';
        var _resourcetype = '${library.resourcetype}';
        var _cultid = '${library.cultid}';

        var _userDir = '${cult.level}/${library.resourcetype}/发布级/';

        var _libid='${param.libid}';
        var _uid='${sessionAdminUser.id}';

    </script>
    <style type="text/css">
        #container {position: relative; border-bottom: 1px solid #ccc; background-color: #ddd; margin: -8px -8px 0 -8px; padding: 10px; }
        .title{ border-bottom: 1px solid #ccc; margin: 0 -8px; padding: 10px; background-color: #f5f5f5; }
        .mymimetype{ margin-top: 12px; float: left; margin-right: 10px; font-size: 12px; color: #777; }
        span.weight{ font-weight: bold; }
    </style>
</head>
<body>
    <form name=theform>
        <div id="container">
            <a id="selectfiles" href="javascript:void(0);" class='btn' style="background-color: #bbb;">选择资源文件</a>
            <a id="postfiles" href="javascript:void(0);" class='btn' style="background-color: #bbb;">开始上传</a>
            <c:if test="${library.resourcetype == 'img'}"><span class="mymimetype">支持的图片格式：<span class="weight">jpg,gif,png,bmp</span></span></c:if>
            <c:if test="${library.resourcetype == 'video'}"><span class="mymimetype">支持的视频格式：<span class="weight">mp4</span></span></c:if>
            <c:if test="${library.resourcetype == 'audio'}"><span class="mymimetype">支持的音频格式：<span class="weight">mp3</span></span></c:if>
            <c:if test="${library.resourcetype == 'file'}"><span class="mymimetype">支持的文档格式：<span class="weight">word,excel,pdf,zip</span></span></c:if>
        </div>

        <div style="display: none;">
            <label><input type="radio" name="myradio" value="local_name" checked="checked"/> 上传文件名字保持本地文件名字</label>
            <label><input type="radio" name="myradio" value="random_name"/> 上传文件名字是随机文件名, 后缀保留</label>
        </div>

        <div class="title" id="filesTitle" style="display: none;">资源列表</div>
        <div id="ossfile"></div>
        <pre id="console" style="display: none;"></pre>
    </form>
    <script type="text/javascript" src="${basePath}/static/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/lib/upload-mass.js"></script>
</body>
</html>