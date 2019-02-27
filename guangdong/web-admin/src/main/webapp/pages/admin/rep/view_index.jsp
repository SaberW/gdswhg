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
    <script type="text/javascript" src="${basePath }/static/admin/easyui/jquery.min.js" ></script>
    <script type="text/javascript" src="${basePath }/static/bootstrap2.3.2/js/bootstrap.min.js"></script>

    <link rel="stylesheet" type="text/css" href="${basePath}/static/jqplot1.0.9/jquery.jqplot.min.css" />
    <!--[if lt IE 9]><script language="javascript" type="text/javascript" src="${basePath}/static/jqplot1.0.9/excanvas.js"></script><![endif]-->
    <%--<script language="javascript" type="text/javascript" src="${basePath}/static/jqplot1.0.9/jquery.min.js"></script>--%>
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
    </style>
</head>
<body style="overflow-x: hidden">

<div class="container-fluid">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li>
                <i class="icon-home"></i>
                <a href="index.html">文化馆控制台</a>
            </li>
        </ul>
        <ul class="crumb-buttons">
            <li class="range">
                <a href="#">
                    <i class="icon-calendar">
                    </i>
                    <span>2017年6月22日 星期三 下午03:23</span>
                    <i class="icon-angle-down">
                    </i>
                </a>
            </li>
        </ul>
    </div>


    <div class="row-fluid">
        <div class="span12">
            <div class="page-header">
                <div class="page-title">
                    <h3>欢迎您进入文化馆控制台</h3>
                    <span>您好，administrator！</span>
                </div>
                <ul class="page-stats">
                    <li>
                        <div class="summary"><span>会员数量</span><h3>${tongji_comm.USERS}</h3></div>
                    </li>
                    <li>
                        <div class="summary"><span>实名会员数</span><h3>${tongji_comm.REL_USERS}</h3></div>
                    </li>
                </ul>
            </div>
        </div>
    </div>


    <div class="row-fluid">
        <div class="span12">
            <div class="widget box">

                <div class="widget-content">
                    <ul class="stats">
                        <li>
                            <strong>${tongji_comm.PV}</strong>
                            <small>今日PV</small>
                        </li>
                        <li class="light">
                            <strong>${tongji_comm.UV}</strong>
                            <small>今日UV</small>
                        </li>
                        <li>
                            <strong>${tongji_comm.IP}</strong>
                            <small>今日IP</small>
                        </li>
                        <li class="light">
                            <strong>${tongji_comm.ACTS}</strong>
                            <small>发布活动数</small>
                        </li>
                        <li>
                            <strong>${tongji_comm.TRAS}</strong>
                            <small>发布培训数</small>
                        </li>
                        <li class="light">
                            <strong>${tongji_comm.VENS}</strong>
                            <small>可预定场馆</small>
                        </li>
                    </ul>
                </div>

                <div class="widget-content">
                    <div class="row-fluid">
                        <div class="span6">
                            <div id="chartdiv1"></div>
                        </div>

                        <div class="span6">
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
                    <h4><i class="icon-align-justify"></i>新增会员统计</h4>
                </div>
                <div class="widget-content">
                    <div class="row-fluid">
                        <div class="span6">
                            <div style="float: right; margin-left:5px; margin-right: 10px;">
                                <button class="btn btn-primary btn-small disabled chartdiv3" id="chartdiv3_next" stype="next">下月 &gt;</button>
                            </div>
                            <div style="float: right; margin:0 5px;">
                                <button class="btn btn-primary btn-small disabled chartdiv3" id="chartdiv3_curt" stype="curt">本月</button>
                            </div>
                            <div style="float:right;margin: 0 5px;">
                                <button class="btn btn-primary btn-small disabled chartdiv3" id="chartdiv3_pre" stype="pre">&lt; 上月</button>
                            </div>
                            <div id="chartdiv3"></div>
                        </div>

                        <div class="span6">
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
                    <h4><i class="icon-align-justify"></i>会员大数据统计</h4>
                </div>


                <div class="widget-content">
                    <div class="row-fluid">
                        <div class="span2">
                             <div id="chartdiv5"></div>
                        </div>
                        <div class="span2">
                            <div id="chartdiv5_2"></div>
                        </div>
                        <div class="span2">
                            <div id="chartdiv5_3"></div>
                        </div>

                        <div class="span6">
                            <div id="chartdiv6"></div>
                        </div>

                    </div>

                    <div class="row-fluid">
                        <div class="span6">
                            <div id="chartdiv7"></div>
                        </div>

                        <div class="span6">
                            <div id="chartdiv8"></div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>

</div>

<script type="text/javascript">
    var plot3;//




    $(function () {

        //今日PV分布
        var data_plot1 = [
            ['微信(${tongji_comm.PV_WX}PV)', ${tongji_comm.PV_WX}],['PC(${tongji_comm.PV_PC}PV)', ${tongji_comm.PV_PC}]
        ];

        try {
            var plot1 = jQuery.jqplot ('chartdiv1', [data_plot1],
                {
                    title: '今日PV分布',
                    animate: true,
                    seriesColors:['#4d7496','purple','#7EF25F'],
                    seriesDefaults: {
                        renderer: jQuery.jqplot.PieRenderer,
                        rendererOptions: {
                            fill: false,
                            showDataLabels: true,
                            sliceMargin: 2,
                            lineWidth: 10
                        }
                    },
                    legend: { show:true, location: 'e' }
                }
            );
        } catch (e) {
        }



        //会员注册来源
        var USERS = "${tongji_comm.USERS}".replace(/[,]/g, '');
        USERS = Number(USERS);
        var USERS_WX = "${tongji_comm.USERS_WX}".replace(/[,]/g, '');
        USERS_WX = Number(USERS_WX);
        var USERS_PC = USERS - USERS_WX;
        var data_plot2 = [ ['微信注册('+USERS_WX+'会员)', USERS_WX],['PC注册('+USERS_PC+'会员)', USERS_PC] ];
        try {
            var plot2 = jQuery.jqplot ('chartdiv2', [data_plot2],
                {
                    title: '会员注册来源',
                    animate: true,
                    seriesDefaults: {
                        renderer: jQuery.jqplot.PieRenderer,
                        rendererOptions: {
                            fill: false,
                            showDataLabels: true,
                            sliceMargin: 2,
                            lineWidth: 10
                        }
                    },
                    legend: { show:true, location: 'e' }
                }
            );
        } catch (e) {
        }

        //每天新增用户数
        var data_plot3 = ${tongji_add_user_month}; //[['A', 7], ['B', 3], ['C', 8]]
        try {
            plot3 = $.jqplot ('chartdiv3', [data_plot3], {
                title: '${tongji_add_user_month_title}用户新增趋势',
                animate: true,
                axesDefaults: {
                    labelRenderer: $.jqplot.CanvasAxisLabelRenderer
                },
                seriesDefaults: {
                    rendererOptions: {
                        smooth: true
                    },
                    pointLabels: { show: true }
                },
                axes: {
                    xaxis: {
                        renderer: $.jqplot.CategoryAxisRenderer,
                        pad: 0
                    }
                }
            });
        } catch (e) {
        }
        $('#chartdiv3').attr("tongji_month", "${tongji_add_user_month_val}");
        $('#chartdiv3').attr("tongji_month_now", "${tongji_add_user_month_val}");
        $('#chartdiv3_pre').removeClass("disabled");
        //上月下月每日统计新增用户
        $('button.chartdiv3').on('click', function(e){
            if(!$(this).hasClass("disabled")){
                var month = $('#chartdiv3').attr("tongji_month");
                var type = $(this).attr("stype");
                $.get('${basePath}/admin/tongjiMonthAddUser', {"month":month, "type": type}, function(ajaxData){
                    var tongji_add_user_month = ajaxData.tongji_add_user_month;
                    var tongji_add_user_month_title = ajaxData.tongji_add_user_month_title;
                    var tongji_add_user_month_val = ajaxData.tongji_add_user_month_val;
                    var title = tongji_add_user_month_title+'用户新增趋势';
                    redraw_chartdiv3(tongji_add_user_month, title);

                    $('#chartdiv3').attr("tongji_month", tongji_add_user_month_val);
                    var tongji_month_now = $('#chartdiv3').attr("tongji_month_now");
                    if(parseInt(tongji_add_user_month_val, 10) < parseInt(tongji_month_now, 10)){
                        $('#chartdiv3_next').removeClass("disabled");
                        $('#chartdiv3_curt').removeClass("disabled");
                    }else{
                        $('#chartdiv3_next').addClass("disabled");
                        $('#chartdiv3_curt').addClass("disabled");
                    }


                },'json');
            }
        });



        //每月新增用户数
        var data_plot4 = ${tongji_add_user_year};//[['1月', 43],['2月', 16],['3月', 24],['4月', 35],['5月', 36],['6月', 77],['7月', 69],['8月', 82],['9月', 85],['10月', 96],['11月', 0],['12月', 0]];
        try {
            var plot4 = $.jqplot('chartdiv4', [data_plot4], {
                title:'${tongji_add_user_year_title}用户新增趋势',
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

        //活跃用户占比 [饼图]
        var data_plot5_users = Number("${tongji_active_user.USERS}".replace(/[,]/g,''));//总用户数
        var data_plot5_users_wx = Number("${tongji_active_user.USERS_WX}".replace(/[,]/g,''));//总用户数
        var data_plot5_users_active_pc = Number("${tongji_active_user.PC_ACTIVE_USERS}".replace(/[,]/g,''));//PC活跃用户
        var data_plot5_users_active_pc_no = data_plot5_users-data_plot5_users_wx-data_plot5_users_active_pc;//PC非活跃用户
        var data_plot5_users_active_wx = Number("${tongji_active_user.WX_ACTIVE_USERS}".replace(/[,]/g,''));//WX活跃用户
        var data_plot5_users_active_wx_no = data_plot5_users_wx-data_plot5_users_active_wx;//WX非活跃用户
        var data_plot5_users_active_all = data_plot5_users_active_pc+data_plot5_users_active_wx;//总活跃用户
        var data_plot5_users_active_all_no = data_plot5_users - data_plot5_users_active_all;//总活跃用户

        var data_plot5 = [
            ['非活跃用户'+data_plot5_users_active_all_no+'人', data_plot5_users_active_all_no],['活跃用户'+data_plot5_users_active_all+'人', data_plot5_users_active_all]
        ];
        try {
            var plot5 = jQuery.jqplot ('chartdiv5', [data_plot5],
                {
                    title: '总活跃用户百分比',
                    animate: true,
                    //seriesColors:['yellow','purple','#7EF25F'],
                    seriesDefaults: {
                        renderer: jQuery.jqplot.PieRenderer,
                        rendererOptions: {
                            fill: false,
                            showDataLabels: true,
                            sliceMargin: 2,
                            lineWidth: 5
                        }
                    },
                    legend: { show:true, location: 's' }
                }
            );
        } catch (e) {
        }

        //活跃用户占比 [饼图]
        var data_plot5_2 = [
            ['PC非活跃用户'+data_plot5_users_active_pc_no+'人', data_plot5_users_active_pc_no],['PC活跃用户'+data_plot5_users_active_pc+'人', data_plot5_users_active_pc]
        ];
        try {
            var plot5_2 = jQuery.jqplot ('chartdiv5_2', [data_plot5_2],
                {
                    title: 'PC活跃用户百分比',
                    animate: true,
                    seriesColors:['#0088cc','#f89406','#7EF25F'],
                    seriesDefaults: {
                        renderer: jQuery.jqplot.PieRenderer,
                        rendererOptions: {
                            fill: false,
                            showDataLabels: true,
                            sliceMargin: 2,
                            lineWidth: 5
                        }
                    },
                    legend: { show:true, location: 's' }
                }
            );
        } catch (e) {
        }

        var data_plot5_3 = [
            ['微信非活跃用户'+data_plot5_users_active_wx_no+'人', data_plot5_users_active_wx_no],['活跃用户'+data_plot5_users_active_wx+'人', data_plot5_users_active_wx]
        ];
        try {
            var plot5_3 = jQuery.jqplot ('chartdiv5_3', [data_plot5_3],
                {
                    title: '微信活跃用户百分比',
                    animate: true,
                    //seriesColors:['yellow','purple','#7EF25F'],
                    seriesDefaults: {
                        renderer: jQuery.jqplot.PieRenderer,
                        rendererOptions: {
                            fill: false,
                            showDataLabels: true,
                            sliceMargin: 2,
                            lineWidth: 5
                        }
                    },
                    legend: { show:true, location: 's' }
                }
            );
        } catch (e) {
        }

        //用户粘度趋势 (折线图)
        var data_plot6 = ${tongji_active_user_year};
        var plot6 = $.jqplot ('chartdiv6', [data_plot6], {
            title: '${tongji_active_user_year_title}用户粘度趋势',
            animate: true,
            axesDefaults: {
                labelRenderer: $.jqplot.CanvasAxisLabelRenderer
            },
            seriesDefaults: {
                rendererOptions: {
                    smooth: true
                },
                pointLabels: { show: true }
            },
            axes: {
                xaxis: {
                    renderer: $.jqplot.CategoryAxisRenderer,
                    pad: 0
                }
            }
        });

        //参与活动的会员趋势 (柱图)
        var data_plot7 = ${tongji_user_act}; //[['1月', 43],['2月', 16],['3月', 24],['4月', 35],['5月', 36],['6月', 77],['7月', 69],['8月', 82],['9月', 85],['10月', 96],['11月', 0],['12月', 0]];
        try {
            var plot7 = $.jqplot('chartdiv7', [data_plot7], {
                title:'${tongji_user_act_title}参与活动的会员趋势',
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

        //参与培训的会员趋势 (柱图)
        var data_plot8 = ${tongji_user_tra}; //[['1月', 43],['2月', 16],['3月', 24],['4月', 35],['5月', 36],['6月', 77],['7月', 69],['8月', 82],['9月', 85],['10月', 96],['11月', 0],['12月', 0]];
        try {
            var plot8 = $.jqplot('chartdiv8', [data_plot8], {
                title:'${tongji_user_tra_title}参与培训的会员趋势 ',
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
            plot4.replot({resetAxes:true});
            plot5.replot({resetAxes:true});
            plot5_2.replot({resetAxes:true});
            plot5_3.replot({resetAxes:true});
            plot6.replot({resetAxes:true});
            plot7.replot({resetAxes:true});
            plot8.replot({resetAxes:true});
        });
    });


    /**
     * 重绘plot3
     * @param data
     * @param title
     */
    function redraw_chartdiv3(data, title){
        plot3.destroy();
        try {
            plot3 = $.jqplot ('chartdiv3', [data], {
                title: title,
                animate: true,
                axesDefaults: {
                    labelRenderer: $.jqplot.CanvasAxisLabelRenderer
                },
                seriesDefaults: {
                    rendererOptions: {
                        smooth: true
                    },
                    pointLabels: { show: true }
                },
                axes: {
                    xaxis: {
                        renderer: $.jqplot.CategoryAxisRenderer,
                        pad: 0
                    }
                }
            });
        } catch (e) {
        }
    }


</script>
</body>
</html>