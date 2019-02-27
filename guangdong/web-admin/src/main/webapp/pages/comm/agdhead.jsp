<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="whg" uri="/WEB-INF/WhgUtils.tld"%>
<%String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath(); request.setAttribute("basePath", basePath); %>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="${not empty page_desc? page_desc : '数字文化馆,广东省数字文化馆'}">
<meta name="keywords" content="${not empty page_key? page_key : '数字文化馆,广东省数字文化馆' }">
<meta name="author" content="${page_author }">
<link rel="shortcut icon" href="${basePath }/favicon.ico" />
<link href="${basePath }/static/assets/css/public/reset.css" rel="stylesheet">
<link href="${basePath }/static/assets/js/plugins/oiplayer-master/css/oiplayer.css" rel="stylesheet">

<script src="${basePath }/static/fullcalendar/lib/moment.min.js" type="text/javascript"></script>
<script src="${basePath }/static/assets/js/public/jquery-1.11.0.min.js" type="text/javascript"></script>
<script src="${basePath }/static/assets/js/public/jquery-migrate-1.0.0.js" type="text/javascript"></script>
<script src="${basePath }/static/assets/js/public/rong-dialog.js"></script>
<script src="${basePath }/static/assets/js/public/public.js"></script>
<script src="${basePath }/static/assets/js/plugins/oiplayer-master/plugins/flowplayer-3.2.6.min.js" type="text/javascript"></script>
<script src="${basePath }/static/assets/js/plugins/oiplayer-master/js/jquery.oiplayer.js"></script>
<script src="${basePath }/static/assets/js/plugins/roll/jquery.sly.min.js" type="text/javascript"></script>
<!--[if lt IE 9] >
<script src="${basePath }/static/assets/js/plugins/ie/IE9.js"></script>
<! [endif]]-->
<script type="text/javascript">
    var basePath = '${basePath}';
    var imgServerAddr = '${imgServerAddr}';
    var apiRoot = '${apiRoot}';
    var apiStaticPath = '${apiStaticPath}';
    var apiSite = '${apiSite}';
    var apiWbgx = '${apiWbgx}';
    var apiZxpx = '${apiZxpx}';
</script>
<script src="${basePath }/pages/comm/agdcomm.js"></script>
<script src="${basePath }/static/common/js/whg.sys.base.data.js" type="text/javascript"></script>
<script type="text/javascript">$.ajax({ url: apiWbgx+'jsonp/getCK', type: 'GET', async: false, dataType: 'jsonp',jsonp: "callback", success: function (data) { $.get('${basePath }/visit/saveCK?ck='+data, function(ck){  }); } });
</script>