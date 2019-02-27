<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()); %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>数字文化馆后台管理系统</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${basePath}/static/bootstrap2.3.2/css/mian.css" rel="stylesheet" media="screen">
    <link href="${basePath}/static/bootstrap2.3.2/css/bootstrap.css" rel="stylesheet" media="screen">
    <script type="text/javascript" src="${basePath }/static/easyui/jquery.min.js" ></script>
    <script type="text/javascript" src="${basePath }/static/bootstrap2.3.2/js/bootstrap.min.js"></script>

    <link rel="stylesheet" type="text/css" href="${basePath}/static/jqplot1.0.9/jquery.jqplot.min.css" />
    <!--[if lt IE 9]><script language="javascript" type="text/javascript" src="${basePath}/static/jqplot1.0.9/excanvas.js"></script><![endif]-->
    <script language="javascript" type="text/javascript" src="${basePath}/static/jqplot1.0.9/jquery.jqplot.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/jqplot1.0.9/plugins/jqplot.pieRenderer.js"></script>
    <script type="text/javascript" src="${basePath}/static/jqplot1.0.9/plugins/jqplot.donutRenderer.js"></script>
    <script type="text/javascript" src="${basePath}/static/jqplot1.0.9/plugins/jqplot.barRenderer.js"></script>
    <script type="text/javascript" src="${basePath}/static/jqplot1.0.9/plugins/jqplot.categoryAxisRenderer.js"></script>
    <script type="text/javascript" src="${basePath}/static/jqplot1.0.9/plugins/jqplot.pointLabels.js"></script>

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
            <li><i class="icon-home"></i><a href="index.html">文化馆控制台</a></li>
        </ul>
        <ul class="crumb-buttons">
            <li class="range">
                <a href="#">
                    <i class="icon-calendar"></i>
                    <span>${curtDate}</span>
                </a>
            </li>
        </ul>
    </div>


    <div class="row-fluid">
        <div class="span12">
            <div class="page-header">
                <div class="page-title">
                    <h3>欢迎您进入文化馆控制台</h3>
                    <span>您好，${sessionAdminUser.account}！</span>
                </div>
            </div>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span12">
            <div class="widget box">
                <div class="widget-header">
                    <h4><i class="icon-align-justify"></i>线下培训区域发布量</h4>
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
    </div>

    <div class="row-fluid">
        <div class="span12">
            <div class="widget box">
                <div class="widget-header">
                    <h4><i class="icon-align-justify"></i>线下培训类型发布量</h4>
                </div>
                <div class="widget-content">
                    <div class="row-fluid">
                        <div class="span12">
                            <div id="chartdiv2"></div>
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
                    <h4><i class="icon-align-justify"></i>线下培训月度统计</h4>
                </div>
                <div class="widget-content">
                    <div class="row-fluid">
                        <div class="span12">
                            <div id="chartdiv3"></div>
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
                    <h4><i class="icon-align-justify"></i>在线课程区域发布量</h4>
                </div>
                <div class="widget-content">
                    <div class="row-fluid">
                        <div class="span12">
                            <div id="chartdiv4"></div>
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
                    <h4><i class="icon-align-justify"></i>在线课程类型发布量</h4>
                </div>
                <div class="widget-content">
                    <div class="row-fluid">
                        <div class="span12">
                            <div id="chartdiv5"></div>
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
                    <h4><i class="icon-align-justify"></i>在线课程月度统计</h4>
                </div>
                <div class="widget-content">
                    <div class="row-fluid">
                        <div class="span12">
                            <div id="chartdiv6"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>




</div>

<script type="text/javascript">

    //区域发布量
    var data_plot1 = ${areaData};
    var plot1;
    try {
        plot1 = $.jqplot('chartdiv1', [data_plot1], {
            title: '在线课程区域发布量',
            animate: true,
            axesDefaults: {
                labelRenderer: $.jqplot.CanvasAxisLabelRenderer
            },
            seriesDefaults: {
                rendererOptions: {
                    smooth: true
                },
                pointLabels: {show: true}
            },
            axes: {
                xaxis: {
                    renderer: $.jqplot.CategoryAxisRenderer,
                    pad: 0
                }
            }
        });
    }catch(e){
    }

    //类型发布量
    var data_plot2 = ${typeData};
    var plot2;
    try {
        plot2 = $.jqplot('chartdiv2', [data_plot2], {
            title: '线下培训类型发布量',
            animate: true,
            axesDefaults: {
                labelRenderer: $.jqplot.CanvasAxisLabelRenderer
            },
            seriesDefaults: {
                rendererOptions: {
                    smooth: true
                },
                pointLabels: {show: true}
            },
            axes: {
                xaxis: {
                    renderer: $.jqplot.CategoryAxisRenderer,
                    pad: 0
                }
            }
        });
    }catch(e){
    }

    //月度统计
    var data_plot3 = ${monthData};
    var plot3;
    try {
        plot3 = $.jqplot('chartdiv3', [data_plot3], {
            title:'线下培训月度统计',
            animate: true,
            seriesColors:['#85802b', '#00749F', '#73C774', '#C7754C', '#17BDB8'],
            seriesDefaults:{
                renderer: $.jqplot.BarRenderer,
                rendererOptions: {
                    // Set varyBarColor to tru to use the custom colors on the bars.
                    varyBarColor: true
                },
                pointLabels: { show: true }
            },
            axes:{
                xaxis:{
                    renderer: $.jqplot.CategoryAxisRenderer
                }
            }
        });
    } catch (e) {
    }



    //区域发布量
    var data_plot4 = ${areaData2};
    var plot4;
    try {
        plot4 = $.jqplot('chartdiv4', [data_plot4], {
            title: '在线课程区域发布量',
            animate: true,
            axesDefaults: {
                labelRenderer: $.jqplot.CanvasAxisLabelRenderer
            },
            seriesDefaults: {
                rendererOptions: {
                    smooth: true
                },
                pointLabels: {show: true}
            },
            axes: {
                xaxis: {
                    renderer: $.jqplot.CategoryAxisRenderer,
                    pad: 0
                }
            }
        });
    }catch(e){
    }

    //类型发布量
    var data_plot5 = ${typeData2};
    var plot5;
    try {
        plot5 = $.jqplot('chartdiv5', [data_plot5], {
            title: '在线课程类型发布量',
            animate: true,
            axesDefaults: {
                labelRenderer: $.jqplot.CanvasAxisLabelRenderer
            },
            seriesDefaults: {
                rendererOptions: {
                    smooth: true
                },
                pointLabels: {show: true}
            },
            axes: {
                xaxis: {
                    renderer: $.jqplot.CategoryAxisRenderer,
                    pad: 0
                }
            }
        });
    }catch(e){
    }

    //月度统计
    var data_plot6 = ${monthData2};
    var plot6;
    try {
        plot6 = $.jqplot('chartdiv6', [data_plot6], {
            title:'在线课程月度统计',
            animate: true,
            seriesColors:['#85802b', '#00749F', '#73C774', '#C7754C', '#17BDB8'],
            seriesDefaults:{
                renderer: $.jqplot.BarRenderer,
                rendererOptions: {
                    // Set varyBarColor to tru to use the custom colors on the bars.
                    varyBarColor: true
                },
                pointLabels: { show: true }
            },
            axes:{
                xaxis:{
                    renderer: $.jqplot.CategoryAxisRenderer
                }
            }
        });
    } catch (e) {
    }

</script>
</body>
</html>