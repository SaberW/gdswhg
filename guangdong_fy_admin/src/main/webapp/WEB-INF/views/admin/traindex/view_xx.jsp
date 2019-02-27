<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()); %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>数字文化馆后台管理系统</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>

    <link href="${basePath}/static/bootstrap2.3.2/css/mian.css" rel="stylesheet" media="screen">
    <link href="${basePath}/static/bootstrap2.3.2/css/bootstrap.css" rel="stylesheet" media="screen">
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
            margin-bottom: 20px;
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

        .res-cont-row-1 ul li div.active{
            background-color:#0088cc;
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
            <li><i class="icon-home"></i><a>培训指数 &gt; 线下培训</a></li>
        </ul>
    </div>


    <div class="title">
        <div class="left">
            <select class="select-box" id="cultid" name="cultid"></select>
            <%--<input class="easyui-combobox" style="width:500px; height:32px" name="cultid" data-options="required:true, prompt:'请选择文化馆', valueField:'id', textField:'text', data:WhgComm.getMgrCults()"/>--%>
            <%--培训统计--%>
        </div>
        <div class="right">
            统计截止日期：<span>${curtDate}</span>
        </div>
    </div>

    <div class="res-cont-row-1">
        <ul>
            <li><div class="cont active"><h2>线下培训量</h2><p>${allTrainIndex.XX}</p></div></li>
            <li><div class="cont"><h2>在线课程量</h2><p>${allTrainIndex.ZB}</p></div></li>
            <li><div class="cont"><h2>微专业数量</h2><p>${allTrainIndex.WZ}</p></div></li>
            <li><div class="cont"><h2>培训师资量</h2><p>${allTrainIndex.SZ}</p></div></li>
            <li><div class="cont"><h2>培训资源量</h2><p>${allTrainIndex.ZY}</p></div></li>
        </ul>
    </div>

    <div class="row-fluid">
        <div class="span12">
            <div class="widget box">
                <div class="widget-header">
                    <h4><i class="icon-align-justify"></i>区域发布量</h4>
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
                    <h4><i class="icon-align-justify"></i>类型发布量</h4>
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
                    <h4><i class="icon-align-justify"></i>月度统计</h4>
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
                    <h4><i class="icon-align-justify"></i>访问量统计</h4>
                </div>
                <div class="widget-content">
                    <div class="row-fluid">
                        <div class="span12">
                            <!-- 访问量统计============================================= -->
                            <!-- 表格 -->
                            <table id="whgdg" class="easyui-datagrid" style="display: none; width:100%; height: 300px;"
                                   data-options="striped:true, rownumbers:true, fitColumns:true, singleSelect:true, pagination:true, toolbar:'#whgdg-tb', sort:'totalvisit', order:'desc', queryParams:{cultid:'${cultid}', islive:0}, url:'${basePath}/admin/traindex/srchList4p4visit'">
                                <thead>
                                <tr>
                                    <th data-options="field:'title', width:100">培训标题</th>
                                    <th data-options="field:'totalvisit', width:40, sortable:true">访问量</th>
                                    <th data-options="field:'pcvisit', width:30, sortable:true">PC访问量</th>
                                    <th data-options="field:'wxvisit', width:30, sortable:true">微信访问量</th>
                                </tr>
                                </thead>
                            </table>
                            <!-- 表格 END -->

                            <!-- 表格操作工具栏 -->
                            <div id="whgdg-tb" style="display: none;">
                                <div class="whgdg-tb-srch">
                                    <input class="easyui-textbox" style="width: 200px;height: 32px;" name="title" data-options="prompt:'请输入培训标题', validType:'length[1,32]'" />
                                    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
                                </div>
                            </div>
                            <!-- 表格操作工具栏-END -->
                            <!-- 访问量统计==========================================END -->
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
                    <h4><i class="icon-align-justify"></i>数据统计</h4>
                </div>
                <div class="widget-content">
                    <div class="row-fluid">
                        <div class="span12">
                            <!-- 数据统计============================================= -->
                            <!-- 表格 -->
                            <table id="whgdg2" class="easyui-datagrid" style="display: none; width:100%; height: 300px;"
                                   data-options="striped:true, rownumbers:true, fitColumns:true, singleSelect:true, pagination:true, toolbar:'#whgdg-tb2', sort:'totalbm', order:'desc', queryParams:{cultid:'${cultid}', islive:0}, url:'${basePath}/admin/traindex/srchList4p4bm'">
                                <thead>
                                <tr>
                                    <th data-options="field:'title', width:100">培训标题</th>
                                    <th data-options="field:'sc', width:40, sortable:true">收藏</th>
                                    <th data-options="field:'pl', width:30, sortable:true">评论</th>
                                    <th data-options="field:'dz', width:30, sortable:true">点赞</th>
                                    <th data-options="field:'totalbm', width:30, sortable:true">总报名</th>
                                    <th data-options="field:'pcbm', width:30, sortable:true">PC报名</th>
                                    <th data-options="field:'wxbm', width:30, sortable:true">微信报名</th>
                                </tr>
                                </thead>
                            </table>
                            <!-- 表格 END -->

                            <!-- 表格操作工具栏 -->
                            <div id="whgdg-tb2" style="display: none;">
                                <div class="whgdg-tb-srch">
                                    <input class="easyui-textbox" style="width: 200px;height: 32px;" name="title" data-options="prompt:'请输入培训标题', validType:'length[1,32]'" />
                                    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="WhgComm.search('#whgdg2', '#whgdg-tb2');">查 询</a>
                                </div>
                            </div>
                            <!-- 表格操作工具栏-END -->
                            <!-- 数据统计==========================================END -->
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>

<script type="text/javascript">
    //选择文化馆
    var cults = WhgComm.getMgrCults();
    for(var i=0; i<cults.length; i++){
        $('#cultid').append('<option value="'+cults[i].id+'">'+cults[i].text+'</option>');  //添加一项option
    }
    $('#cultid').change(function () {
        var cultid = $(this).children('option:selected').val();
        window.location.href = '${basePath}/admin/traindex/view/xx?cultid='+cultid;
    });
    $("#cultid ").val("${cultid}");

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

    //窗口改变大小时处理
    $(window).resize(function() {
        plot1.replot({resetAxes:true});
        plot2.replot({resetAxes:true});
        plot3.replot({resetAxes:true});
    });

</script>
</body>
</html>