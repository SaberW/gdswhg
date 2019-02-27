<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()); %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>数字文化馆后台管理系统</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${basePath}/static/bootstrap2.3.2/css/bootstrap.css" rel="stylesheet" media="screen">
    <link href="${basePath}/static/bootstrap2.3.2/css/mian.css" rel="stylesheet" media="screen">

    <link rel="stylesheet" type="text/css" href="${basePath }/static/admin/easyui/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="${basePath }/static/admin/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${basePath }/static/admin/easyui/themes/color.css">

    <script type="text/javascript" src="${basePath }/static/bootstrap2.3.2/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${basePath }/static/admin/easyui/jquery.min.js" ></script>
    <script type="text/javascript" src="${basePath }/static/admin/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${basePath }/static/admin/easyui/locale/easyui-lang-zh_CN.js"></script>


    <%--<script src="http://cdn.hcharts.cn/highcharts/highcharts.js"></script>--%>
    <script type="text/javascript" src="${basePath }/static/admin/js/highcharts.src.js"></script>

    <style>
        div.widget.box{
            border: 0px;
        }
    </style>
</head>
<body style="overflow-x: hidden">

<div class="container-fluid">

    <div class="row-fluid">
        <div class="span12">
            <div class="page-header">
                <div class="page-title">
                    <h3>培训统计</h3>
                </div>
            </div>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span12">
            <div class="widget box">
                <div class="widget-header">
                    <h4><i class="icon-align-justify"></i>培训发布量统计</h4>
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
                    <h4><i class="icon-align-justify"></i>培训访问量TOP10统计</h4>
                </div>
                <div class="widget-content">
                    <div class="row-fluid">
                        <div class="span12">
                            <div id="chartdiv5">
                                <table class="easyui-datagrid" style=""
                                       data-options="url:'${basePath}/admin/rep/searchTraTop10',fitColumns:true,singleSelect:true,rownumbers:true">
                                    <thead>
                                    <tr>
                                        <th data-options="field:'name',width:100">培训标题</th>
                                        <th data-options="field:'viewcount',width:100">访问量</th>
                                        <th data-options="field:'pccount',width:100">PC访问量</th>
                                        <th data-options="field:'wxcount',width:100">微信访问量</th>
                                    </tr>
                                    </thead>
                                </table>
                            </div>
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
                    <h4><i class="icon-align-justify"></i>培训数据统计</h4>
                </div>

                <div class="widget-content">
                    <div class="row-fluid">
                        <div class="span12">
                            <table id="timeOrderGrid" class="easyui-datagrid" style="width: 100%;"
                                   toolbar="#totb" pagination=true pageSize=10 pageList="[10,20,30,50,100,200]" loadMsg="数据加载中，请稍候。。。"
                                   data-options="striped:true, rownumbers:true, fitColumns:true">
                                <thead>
                                <tr>
                                    <th data-options="field:'title', width:100,sortable:false">培训标题</th>
                                    <th data-options="field:'collectcount', width:100,sortable:false">收藏</th>
                                    <th data-options="field:'commentcount',width:100,sortable:false">评论</th>
                                    <th data-options="field:'remcount', width:100,sortable:false">点赞</th>
                                    <th data-options="field:'ordercount',width:100,sortable:false">总报名</th>
                                    <th data-options="field:'pcordercount', width:100,sortable:false" >报名（PC）</th>
                                    <th data-options="field:'wxordercount', width:100,sortable:false" >报名（微信）</th>
                                </tr>
                                </thead>
                            </table>

                            <div id="totb" style="display: none;">
                                <input class="easyui-textbox" name="title" style="height: 30px" prompt="请输入培训名称" data-options="width:150">
                                <a class="easyui-linkbutton" iconCls="icon-search">查 询</a>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>

</div>


</body>

<script>
    $(function () {
        reloadChart1();
        reloadChart2();
        reloadChart3();
        timeOrder.init();
    });

    var timeOrder = {
        grid: "#timeOrderGrid",
        toolbar : "#totb",
        init : function(){
            var that = this;
            $(this.toolbar).find("a:eq(0)").on("click", function(){that.load()});
            $(this.toolbar).find("a:eq(0)").click();
        },
        load : function(){
            var title = $(this.toolbar).find("[name='title']").val();
            $(this.grid).datagrid({
                url : '${basePath}/admin/rep/reptra',
                queryParams : {title: title}
            });
        },

    };


    /**各类型培训发布量*/
    function reloadChart1 () {
        $.ajax({
            url: '${basePath}/admin/rep/repTraEtype',
            success: function (data) {
               var _data = JSON.parse(data);
                //alert(_data.data[0].name);
                var xArr = [], yArr = [];
                for (var i in _data.data) {
                    xArr.push(_data.data[i].name);
                    yArr.push(parseInt(_data.data[i].count));
                }

                $("#chartdiv1").highcharts({
                    chart: {
                        type: 'column'
                    },
                    title: {
                        text: '各类型培训发布量'
                    },
                    credits: {
                        enabled: false  //去掉官网链接
                    },
                    xAxis: {
                        categories: xArr,
                        crosshair: true
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: '各类型培训发布量'
                        }
                    },
                    tooltip: {
                        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                        pointFormat: '<tr><td style="padding:0">{point.key}:{point.y:1f} </td>',
                        footerFormat: '</table>',
                        shared: true,
                        useHTML: true
                    },
                    plotOptions: {
                        column: {
                            pointPadding: 0.2,
                            borderWidth: 0
                        }
                    },
                    series: [{
                        animation: false,
                        name: '培训发布量' ,
                        data: yArr
                    }]
                });
            }
        });
    }   //各类型培训发布量--END

    /**各区域培训发布量*/
    function reloadChart2 () {
        $.ajax({
            url: '${basePath}/admin/rep/repTraArea',

            success: function (data) {
                var _data = JSON.parse(data);
                //alert(_data.data[0].name);
                var xArr = [], yArr = [];
                for (var i in _data.data) {
                    xArr.push(_data.data[i].name);
                    yArr.push(parseInt(_data.data[i].count));
                }
                $("#chartdiv2").highcharts({
                    chart: {
                        type: 'column'
                    },
                    title: {
                        text: '各区域培训发布量'
                    },
                    credits: {
                        enabled: false  //去掉官网链接
                    },
                    xAxis: {
                        categories: xArr,
                        crosshair: true
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: '各区域培训发布量'
                        }
                    },
                    tooltip: {
                        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                        pointFormat: '<tr><td style="padding:0">{point.key}:{point.y:1f} </td>',
                        footerFormat: '</table>',
                        shared: true,
                        useHTML: true
                    },
                    plotOptions: {
                        column: {
                            pointPadding: 0.2,
                            borderWidth: 0
                        }
                    },
                    series: [{
                        animation: false,
                        name: '培训发布量' ,
                        data: yArr
                    }]
                });
            }
        });
    }   //各区域培训发布量--END

    /**1-12月份培训发布统计*/
    function reloadChart3 () {
        $.ajax({
            url: '${basePath}/admin/rep/srchTraByMonth',
            success: function (data) {
                var _data = JSON.parse(data);

                $("#chartdiv3").highcharts({
                    chart: {
                        type: 'column'
                    },
                    title: {
                        text: '1-12月份培训发布统计'
                    },
                    credits: {
                        enabled: false  //去掉官网链接
                    },

                    xAxis: {
                        categories: _data.categories,
                        crosshair: true
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: '1-12月份培训发布统计'
                        }
                    },
                    tooltip: {
                        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                        pointFormat: '<tr><td style="color:{series.color};padding:0"></td>' +
                        '<td style="padding:0"><b>{point.y:1f} </b></td></tr>',
                        footerFormat: '</table>',
                        shared: true,
                        useHTML: true
                    },
                    plotOptions: {
                        column: {
                            pointPadding: 0.2,
                            borderWidth: 0
                        }
                    },
                    series: [{
                        animation: false,
                        name: '培训发布量',
                        data: _data.data
                    }]
                });
            }
        });
    }   //1-12月份培训发布统计--END


</script>
</html>
