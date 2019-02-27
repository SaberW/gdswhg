<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>数字文化馆后台管理系统</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" media="screen" href="${basePath}/static/bootstrap2.3.2/css/mian.css">
    <link rel="stylesheet" media="screen" href="${basePath}/static/bootstrap2.3.2/css/bootstrap.css">
    <link rel="stylesheet" media="screen" href="${basePath}/static/jqplot1.0.9/jquery.jqplot.min.css" />
    <style>
        div.widget.box{ border: 0px; }
        .title{
            font-size: 16px;
            line-height: 30px;
            margin: 0px 0 10px 0;
            max-height: 60px;
            overflow: hidden;
            font-weight: normal;
        }
        .act{
            font-size: 14px;
            color: #da635d;
            line-height: 30px;
        }
        .acttext{
            color: #959595;
        }
    </style>

    <script type="text/javascript" src="${basePath }/static/easyui/jquery.min.js" ></script>
    <script type="text/javascript" src="${basePath }/static/bootstrap2.3.2/js/bootstrap.js"></script>

    <!-- echarts -->
    <script type="text/javascript" src="${basePath }/static/echarts/echarts.min.js"></script>
    <script type="text/javascript" src="${basePath }/static/echarts/infographic.js"></script>
    <script type="text/javascript" src="${basePath }/static/echarts/shine.js"></script>
</head>
<body style="overflow-x: hidden">

<div class="container-fluid">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li>
                <i class="icon-home"></i>
                <a href="index.html">欢迎您进入总分馆管理系统！</a>
            </li>
        </ul>
        <ul class="crumb-buttons">
            <li class="range">
                <a href="#">
                    <i class="icon-calendar">
                    </i>
                    <span>${curtDate}</span>
                    <i class="icon-angle-down">
                    </i>
                </a>
            </li>
        </ul>
    </div>

    <div class="row-fluid" id="accounInfo">
        <div class="span12">
            <div class="widget box">

                <div class="widget-content">
                    <ul class="stats">
                        <li class="light">
                            <strong id="reg_user_total">2345</strong>
                            <small>累计注册用户</small>
                            <strong id="reg_user_month">4</strong>
                            <small>本月新增</small>
                            <strong id="reg_user_day">0</strong>
                            <small>今日新增</small>
                        </li>
                        <li class="light">
                            <strong id="pv_total">232</strong>
                            <small>累计PV</small>
                            <strong id="pv_month">22</strong>
                            <small>本月PV</small>
                            <strong id="pv_day">232</strong>
                            <small>今日PV</small>
                        </li>
                        <li class="light">
                            <strong id="uv_total">2424</strong>
                            <small>累计UV</small>
                            <strong id="uv_month">242</strong>
                            <small>本月UV</small>
                            <strong id="uv_day">2</strong>
                            <small>今日UV</small>
                        </li>
                        <li class="light">
                            <strong id="hot_user_total">2424</strong>
                            <small>累计活跃用户</small>
                            <strong id="hot_user_month">242</strong>
                            <small>本月活跃用户</small>
                            <strong id="hot_user_day">2</strong>
                            <small>今日活跃用户</small>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>


    <div class="row-fluid">
        <div class="span12">
            <div class="widget box">
                <div class="widget-header">
                    <h4><i class="icon-align-justify"></i>年度活跃文化馆TOP6</h4>
                </div>
                <div class="widget-content">
                    <div class="row-fluid">
                        <div class="span2">
                            <div id="chartDiv_1" style="width: 100%; height: 300px;"></div>
                        </div>

                        <div class="span2">
                            <div id="chartDiv_2" style="width: 100%; height: 300px;"></div>
                        </div>

                        <div class="span2">
                            <div id="chartDiv_3" style="width: 100%; height: 300px;"></div>
                        </div>

                        <div class="span2">
                            <div id="chartDiv_4" style="width: 100%; height: 300px;"></div>
                        </div>

                        <div class="span2">
                            <div id="chartDiv_5" style="width: 100%; height: 300px;"></div>
                        </div>

                        <div class="span2">
                            <div id="chartDiv_6" style="width: 100%; height: 300px;"></div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>


    <div class="row-fluid">
        <div class="span6">
            <div class="widget box">
                <div class="widget-header">
                    <h4><i class="icon-align-justify"></i>本年度最受欢迎活动</h4>
                </div>
                <div class="widget-content">
                    <div class="row-fluid">
                        <div class="span6">
                            <img src="${baseImgPath}${act_year.imgurl}"/>
                        </div>
                        <div class="span6">
                            <h1 class="title">${act_year.name}</h1>
                            <div class="act time"><i class="icon-time"></i>活动时间：<span class="acttext">${act_year.starttime}至${act_year.endtime}</span></div>
                            <div class="act cult"><i class="icon-home"></i>　文化馆：<span class="acttext">${act_year.cultName}</span></div>
                            <div class="act cult"><i class="icon-user"></i>　订票数：<span class="acttext">${act_year.bookTickets}</span></div>
                            <div class="act cult"><i class="icon-user"></i>活动热度：<span class="acttext">${act_year.hots}</span></div>
                            <div class="act desc"><i class="icon-th"></i>活动简介：<span class="acttext">${act_year.memo}</span></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="span6">
            <div class="widget box">
                <div class="widget-header">
                    <h4><i class="icon-align-justify"></i>本月最受欢迎活动</h4>
                </div>
                <div class="widget-content">
                    <div class="row-fluid">
                        <div class="span6">
                            <img src="${baseImgPath}${act_month.imgurl}"/>
                        </div>
                        <div class="span6">
                            <h1 class="title">${act_month.name}</h1>
                            <div class="act time"><i class="icon-time"></i>活动时间：<span class="acttext">${act_month.starttime}至${act_month.endtime}</span></div>
                            <div class="act cult"><i class="icon-home"></i>　文化馆：<span class="acttext">${act_month.cultName}</span></div>
                            <div class="act cult"><i class="icon-user"></i>　订票数：<span class="acttext">${act_month.bookTickets}</span></div>
                            <div class="act cult"><i class="icon-user"></i>活动热度：<span class="acttext">${act_month.hots}</span></div>
                            <div class="act desc"><i class="icon-th"></i>活动简介：<span class="acttext">${act_month.memo}</span></div>
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
                    <h4><i class="icon-align-justify"></i>文化服务趋势</h4>
                </div>
                <div class="widget-content">
                    <div class="row-fluid">
                        <div class="span12">
                            <div id="chartDiv_7" style="height: 300px; width: 100%"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>

<script type="text/javascript">
    /** 构造饼图1 */
    function init_chartDiv(eleId, cultName, acts, totals, theme){
        var myChart = false;
        try {
            //
            totals = totals || 0;
            cultName = cultName || 'xx文化馆';
            acts = acts || 0;

            var rate = totals == 0 ? '0.00' : ((acts / totals) * 100).toFixed(2);

            // 基于准备好的dom，初始化echarts实例
            myChart = echarts.init(document.getElementById(eleId), theme);

            // 指定图表的配置项和数据
            var option = {
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b}: {c} ({d}%)"
                },
                legend: {
                    orient: 'vertical',
                    x: 'left',
                    data:[cultName]
                },
                series: [
                    {
                        name:'场次',
                        type:'pie',
                        radius: ['50%', '70%'],
                        avoidLabelOverlap: false,
                        label: {
                            normal: {
                                show: true,
                                position: 'center',
                                textStyle: {color:'rgb(193, 46, 52)',fontSize:"20", fontWeight: 'bold'},
                                formatter: ' '+rate+'% \r\n'+acts+'场 '
                            },
                            emphasis: {
                                show: true,
                                textStyle: {
                                    fontSize: '20',
                                    fontWeight: 'bold'
                                }
                            }
                        },
                        labelLine: {
                            normal: {
                                show: true
                            }
                        },
                        data:[
                            {value:acts, name:cultName},
                            {value:(totals-acts), name:'其它'}
                        ]
                    }
                ]
            };

            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        } catch (e) {}
        return myChart;
    }

    /** 构造柱状图 */
    function initBar(eleId, dateArr, actData, traData, theme) {
        var myChart = false;
        try {
            //
            dateArr = $.isArray(dateArr) && dateArr.length == 6 ? dateArr : ['2018-01', '2018-02', '2018-03', '2018-04', '2018-05', '2018-06'];
            actData = $.isArray(actData) && actData.length == 6 ? actData : [90,203,340,324,234,55];
            traData = $.isArray(traData) && traData.length == 6 ? traData : [32,2,343,5,34,53];

            // 基于准备好的dom，初始化echarts实例
            myChart = echarts.init(document.getElementById(eleId), theme);

            var labelOption = {
                normal: {
                    show: true,
                    position: 'insideBottom',
                    distance: 15,
                    align: 'left',
                    verticalAlign: 'middle',
                    rotate: 90,
                    formatter: '{c}  {name|{a}}',
                    fontSize: 16,
                    rich: {
                        name: {
                            textBorderColor: '#fff'
                        }
                    }
                }
            };
            var option = {
                color: ['#c12e34', '#0098d9', '#4cabce', '#e5323e'],
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'shadow'
                    }
                },
                legend: {
                    data: ['活动', '培训'] /** 培训和活动 */
                },
                toolbox: {
                    show: true,
                    orient: 'vertical',
                    left: 'right',
                    top: 'center',
                    feature: {
                        mark: {show: true},
                        dataView: {show: true, readOnly: false},
                        magicType: {show: true, type: ['line', 'bar', 'stack', 'tiled']},
                        restore: {show: true},
                        saveAsImage: {show: true}
                    }
                },
                calculable: true,
                xAxis: [
                    {
                        type: 'category',
                        axisTick: {show: false},
                        data: dateArr/** 培训和活动 */
                    }
                ],
                yAxis: [
                    {
                        type: 'value'
                    }
                ],
                series: [
                    {
                        name: '活动',
                        type: 'bar',
                        barGap: 0,
                        label: labelOption,
                        data: actData
                    },
                    {
                        name: '培训',
                        type: 'bar',
                        label: labelOption,
                        data: traData
                    }
                ]
            };
            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        } catch (e) {}
        return myChart;
    }

    /** 初始 注册用户 活跃用户 PV UV */
    function init_user_data(reg_user_total, reg_user_month, reg_user_day,
                            hot_user_total, hot_user_month, hot_user_day,
                            pv_total, pv_month, pv_day,
                            uv_total, uv_month, uv_day) {
        $('#reg_user_total').text(reg_user_total || 0);
        $('#reg_user_month').text(reg_user_month || 0);
        $('#reg_user_day').text(reg_user_day || 0);
        $('#hot_user_total').text(hot_user_total || 0);
        $('#hot_user_month').text(hot_user_month || 0);
        $('#hot_user_day').text(hot_user_day || 0);
        $('#pv_total').text(pv_total || 0);
        $('#pv_month').text(pv_month || 0);
        $('#pv_day').text(pv_day || 0);
        $('#uv_total').text(uv_total || 0);
        $('#uv_month').text(uv_month || 0);
        $('#uv_day').text(uv_day || 0);
    }

    $(function () {
        if(1 != '${sessionAdminUser.adminlevel}'){
            $("#accounInfo").hide();
        }
        //数据统计
        init_user_data(
            '${reg_user_total}', '${reg_user_month}', '${reg_user_day}',
            '${hot_user_total}', '${hot_user_month}', '${hot_user_day}',
            '${pv_total}', '${pv_month}', '${pv_day}',
            '${uv_total}', '${uv_month}', '${uv_day}'
        );

        //文化馆TOP6
        var chart1 = init_chartDiv('chartDiv_1', '${act_times_1_name}', '${act_times_1}', '${act_times_total}');
        var chart2 = init_chartDiv('chartDiv_2', '${act_times_2_name}', '${act_times_2}', '${act_times_total}', 'infographic');
        var chart3 = init_chartDiv('chartDiv_3', '${act_times_3_name}', '${act_times_3}', '${act_times_total}');
        var chart4 = init_chartDiv('chartDiv_4', '${act_times_4_name}', '${act_times_4}', '${act_times_total}', 'infographic');
        var chart5 = init_chartDiv('chartDiv_5', '${act_times_5_name}', '${act_times_5}', '${act_times_total}');
        var chart6 = init_chartDiv('chartDiv_6', '${act_times_6_name}', '${act_times_6}', '${act_times_total}', 'infographic');

        //文化服务趋势
        var monthArr = ['${month1}','${month2}','${month3}','${month4}','${month5}','${month6}'];
        var actData = ['${act_month1}','${act_month2}','${act_month3}','${act_month4}','${act_month5}','${act_month6}'];
        var traData = ['${tra_month1}','${tra_month2}','${tra_month3}','${tra_month4}','${tra_month5}','${tra_month6}'];
        var chart7 = initBar('chartDiv_7', monthArr, actData, traData);

        //window.onresize
        //窗口改变大小时处理
        $(window).resize(function() {
            if(chart1) chart1.resize();
            if(chart2) chart2.resize();
            if(chart3) chart3.resize();
            if(chart4) chart4.resize();
            if(chart5) chart5.resize();
            if(chart6) chart6.resize();
            if(chart7) chart7.resize();
        });
    });
</script>
</body>
</html>