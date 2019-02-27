<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()); %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>群文统计</title>

    <link href="${basePath}/static/bootstrap2.3.2/css/mian.css" rel="stylesheet" media="screen">
    <link href="${basePath}/static/bootstrap2.3.2/css/bootstrap.css" rel="stylesheet" media="screen">
    <link rel="stylesheet" type="text/css" href="${basePath }/static/easyui/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="${basePath }/static/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${basePath }/static/easyui/themes/color.css">

    <script type="text/javascript" src="${basePath }/static/easyui/jquery.min.js" ></script>
    <script type="text/javascript" src="${basePath }/static/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${basePath }/static/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="${basePath }/static/bootstrap2.3.2/js/bootstrap.min.js"></script>

    <link rel="stylesheet" type="text/css" href="${basePath}/static/jqplot1.0.9/jquery.jqplot.min.css" />
    <!--[if lt IE 9]><script language="javascript" type="text/javascript" src="${basePath}/static/jqplot1.0.9/excanvas.js"></script><![endif]-->
    <script language="javascript" type="text/javascript" src="${basePath}/static/jqplot1.0.9/jquery.jqplot.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/jqplot1.0.9/plugins/jqplot.pieRenderer.js"></script>
    <script type="text/javascript" src="${basePath}/static/jqplot1.0.9/plugins/jqplot.donutRenderer.js"></script>
    <script type="text/javascript" src="${basePath}/static/jqplot1.0.9/plugins/jqplot.barRenderer.js"></script>
    <script type="text/javascript" src="${basePath}/static/jqplot1.0.9/plugins/jqplot.categoryAxisRenderer.js"></script>
    <script type="text/javascript" src="${basePath}/static/jqplot1.0.9/plugins/jqplot.pointLabels.js"></script>

    <script type="text/javascript">
        var basePath = '${basePath}';
        var imgServerAddr = '${baseImgPath}';
        var managerCults = ${sessionAdminUserCults};
        var managerDepts = ${sessionAdminUserDepts};
        $.ajax({
            async: false,
            type: "POST",
            cache: false,
            url: "/admin/doRefresh",
            success: function(data){
                var datastr = $.parseJSON(data);
                if (datastr && datastr != "") {
                    managerCults = datastr.sessionAdminUserCults;
                    managerDepts = datastr.sessionAdminUserDepts;
                }
            }
        });
    </script>
    <script type="text/javascript" src="${basePath }/static/admin/js/common.js"></script>
    <script type="text/javascript" src="${basePath }/static/common/js/whg.sys.base.data.js"></script>

    <style>
        div.widget.box{
            border: 0px;
        }
        .title{
            height:70px;
            line-height:70px;
        }
        .title:after{
            clear:both;
        }
        .title .left{
            float:left;
        }
        .title .left .select-box{
            border:1px #ddd solid;
            height:35px;
            line-height:35px;
            padding:0px 15px;
            margin-right:15px;
        }
        .title .right{
            float:right;
            padding-right:10px;
        }
        .res-cont-row-1{
            height:200px;
            padding:35px 25px;
            background-color:#f5f5f5;
        }
        .res-cont-row-1:after{
            clear:both;
        }
        .res-cont-row-1 ul,.res-cont-row-1 ul li{
            list-style:none;
            padding:0px;
            margin:0px;
        }
        .res-cont-row-1 ul li{
            float:left;
            width:20%;
        }
        .res-cont-row-1 ul li .cont{
            height:200px;
            width:200px;
            background-color:#fff;
            border:1px #ddd solid;
            -webkit-border-radius:50%;
            -moz-border-radius:50%;
            border-radius:50%;
            margin:0px auto;
            text-align:center;
            box-shadow: 3px 10px 10px #ddd;
        }
        .res-cont-row-1 ul li h2{
            padding-top:50px;
            font-size:16px;
        }
        .res-cont-row-1 ul li p{
            font-size:22px;
            font-weight:700;
            color:#173951;
        }
    </style>
</head>
<body style="overflow-x: hidden">

<div class="container-fluid">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li><i class="icon-home"></i><a href="javascript:void(0)">群文统计</a></li>
        </ul>
        <ul class="crumb-buttons">
            <li class="range">
                <a href="#">
                    <i class="icon-calendar"></i>
                    <span>${curtDate}</span>
                </a>
            </li>
            <li class="range" style="padding: 3px 5px">
                <input class="easyui-combobox" name="cultid" id="cultid" prompt="请选择文化馆"
                       data-options="editable:true,width:180,height:35"/>
            </li>
        </ul>
    </div>

    <%--<div class="row-fluid">
        <div class="span12">
            <div class="widget box">
                <div class="widget-header">
                    <h4><i class="icon-align-justify"></i>区域资源发布量</h4>
                </div>
                <div class="widget-content">
                    <div class="row-fluid">
                        <div class="span12">
                            <div id="chartdiv1"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>--%>

    <div class="row-fluid">
        <div class="span12">
            <div class="widget box">
                <div class="widget-header">
                    <h4><i class="icon-align-justify"></i>各类资源发布量</h4>
                </div>
                <div class="widget-content">
                    <div class="row-fluid">
                        <div class="span12">
                            <div id="chartdivtype"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span12">
            <div class="widget box">
                <div class="widget-header">
                    <h4><i class="icon-align-justify"></i>1-12月资源月度发布统计</h4>
                </div>
                <div class="widget-content">
                    <div class="row-fluid">
                        <div class="span12">
                            <div id="chartdivmonth"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <div class="row-fluid">
        <div class="span12">
            <div class="widget box">
                <div class="widget-header">
                    <h4><i class="icon-align-justify"></i>资源访问量TOP10统计</h4>
                </div>
                <div class="widget-content">
                    <div class="row-fluid">
                        <div class="span12">
                            <div id="chartdivtop"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>

<script>

    $(function(){
        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'',
            cultOnChange: function(nval, oval){
                pageUtil.init(nval);
            }
        });
    });

    var pageUtil = {
        init: function(cultid){
            var params = {cultid: cultid};

            //this.barRenderer("chartdiv1", "${basePath}/admin/mass/count/count01", params);
            this.barRenderer("chartdivtype", "${basePath}/admin/mass/count/count4ResType", params);
            this.barRenderer("chartdivmonth", "${basePath}/admin/mass/count/count4yearMonth", params);

            this.gridTop10(params);
        },

        barRenderer: function(el, url, params){
            $.post(url, params, function(data){
                $("#"+el).empty();
                if (!data || !$.isArray(data) || !data.length){
                    return;
                }

                var dataValue = new Array(); //数据
                var ticks = new Array(); //横坐标值
                $(data).each(function (index, item) {
                    dataValue.push(item.value);
                    ticks.push(item.tick);
                });

                var title = $("#"+el).parents(".widget").find(".widget-header h4").text();

                $.jqplot(el, [dataValue], {
                    animate: !$.jqplot.use_excanvas, //是否动画显示
                    seriesDefaults: {
                        renderer: $.jqplot.BarRenderer, // 利用渲染器（BarRenderer）渲染现有图表
                        pointLabels: { show: true }
                    },
                    title: title, //标题
                    axes: {
                        xaxis: {
                            renderer: $.jqplot.CategoryAxisRenderer, // 设置横（纵）轴上数据加载的渲染器
                            ticks: ticks//设置横（纵）坐标的刻度上的值，可为该ticks数组中的值
                        },
                        yaxis: {
                            //pad: 1.05, // 一个相乘因子
                            tickOptions: { formatString: '%d'}// 设置坐标轴上刻度值显示格式
                        }
                    }
                });
            }, "json");
        },

        gridTop10: function(params){
            var divtop = $("#chartdivtop");
            divtop.empty();

            var grid = $("<div></div>");
            divtop.append(grid);
            grid.datagrid({
                url:'${basePath}/admin/mass/count/count4top10',
                queryParams: params,
                fitColumns: true,
                striped: true,
                width: '98%',
                rownumbers: true,
                columns:[[
                    {field:'name',title:'资源名称',width:100},
                    {field:'count',title:'访问量',width:100},
                    {field:'pxcount',title:'PC访问量',width:100},
                    {field:'wxcount',title:'WX访问量',width:100},
                ]]
            })
        }
    }

</script>

</body>
</html>
