<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<html>
<head>
    <title>统计</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${basePath}/static/bootstrap/css/bootstrap.css" rel="stylesheet" media="screen">
    <link rel="stylesheet" type="text/css" href="${basePath }/static/admin/easyui/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="${basePath }/static/admin/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${basePath }/static/admin/easyui/themes/color.css">

    <script type="text/javascript" src="${basePath }/static/admin/easyui/jquery.min.js" ></script>
    <script type="text/javascript" src="${basePath }/static/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${basePath }/static/admin/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${basePath }/static/admin/easyui/locale/easyui-lang-zh_CN.js"></script>

    <%--<script src="http://cdn.hcharts.cn/highcharts/highcharts.js"></script>--%>
    <script type="text/javascript" src="${basePath }/static/admin/js/highcharts.src.js"></script>
    <style>
        h4{padding-top: 15px}
    </style>
</head>
<body>


<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <h4><i class="glyphicon glyphicon-align-justify"></i> 场馆发布量统计</h4>
        </div>
    </div>
    <!-- 场馆发布量统计Begin -->
    <div class="row">
        <div class="col-md-6">
            <div id="ven_type_column"></div>
        </div>
        <div class="col-md-6">
            <div id="ven_area_column"></div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <div id="ven_year_column"></div>
        </div>
    </div>
    <!-- 场馆发布量统计 -->

    <!-- 场馆访问量TOP10 Begin -->
    <div class="row">
        <div class="col-md-12">
            <h4><i class="glyphicon glyphicon-align-justify"></i> 场馆访问量TOP10</h4>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <table class="easyui-datagrid" style="width: 100%;" data-options="striped:true, rownumbers:true, fitColumns:true,
            url:'${basePath}/admin/rep/ven/listTop10'">
                <thead>
                <tr>
                    <th data-options="field:'title',width:40">活动室</th>
                    <th data-options="field:'sumcount',width:20">访问量</th>
                    <th data-options="field:'pccount',width:20">PC访问量</th>
                    <th data-options="field:'wxcount',width:20">微信访问量</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
    <!-- 场馆访问量TOP10 End -->

    <!-- 场馆开放率 Begin -->
    <div class="row">
        <div class="col-md-12">
            <h4><i class="glyphicon glyphicon-align-justify"></i> 场馆开放率</h4>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <table id="timeOpenGrid" class="easyui-datagrid" style="width: 100%;"
                   toolbar="#tb" pagination=true pageSize=30 pageList="[10,20,30,50,100,200]" loadMsg="数据加载中，请稍候。。。"
                   data-options="striped:true, rownumbers:true, fitColumns:true">
                <thead>
                <tr>
                    <th data-options="field:'title', width:40 ">活动室</th>
                    <th data-options="field:'allminute', width:20, formatter:timeOpen.munit2hour">总时间（小时）</th>
                    <th data-options="field:'openminute', width:20, formatter:timeOpen.munit2hour">开放时间（小时）</th>
                    <th data-options="field:'openrate', width:20, formatter:timeOpen.openrate">开放率</th>
                </tr>
                </thead>
            </table>
            <div id="tb" style="display: none;">
                <input class="easyui-textbox" name="title" prompt="请输入活动室名称" data-options="width:150">
                <a class="easyui-linkbutton" iconCls="icon-search">查 询</a>
            </div>
        </div>
    </div>
    <!-- 场馆开放率 End -->

    <!-- 场馆使用率/空置率 Begin -->
    <div class="row">
        <div class="col-md-12">
            <h4><i class="glyphicon glyphicon-align-justify"></i> 场馆使用率/空置率</h4>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <table id="timeOrderGrid" class="easyui-datagrid" style="width: 100%;"
                   toolbar="#totb" pagination=true pageSize=30 pageList="[10,20,30,50,100,200]" loadMsg="数据加载中，请稍候。。。"
                   data-options="striped:true, rownumbers:true, fitColumns:true">
                <thead>
                <tr>
                    <th data-options="field:'title', width:20">活动室</th>
                    <th data-options="field:'openminute', width:20, formatter:timeOpen.munit2hour">开放（小时）</th>
                    <th data-options="field:'ordernimute', width:20, formatter:timeOpen.munit2hour">预定（小时）</th>
                    <th data-options="field:'orderrate', width:20, formatter:timeOrder.orderrate">使用率</th>
                    <th data-options="field:'unorderrate', width:20, formatter:timeOrder.unorderrate">空置率</th>
                </tr>
                </thead>
            </table>

            <div id="totb" style="display: none;">
                <input class="easyui-textbox" name="title" prompt="请输入活动室名称" data-options="width:150">
                <a class="easyui-linkbutton" iconCls="icon-search">查 询</a>
            </div>
        </div>
    </div>
    <!-- 场馆使用率/空置率 End -->
</div>


<script>

    $(function(){
        myHighcharts.Chart4Type();
        myHighcharts.Chart4Area();
        myHighcharts.Chart4Year();

        timeOpen.init();
        timeOrder.init();
    });

    var timeOpen = {
        grid: "#timeOpenGrid",
        toolbar: "#tb",
        init : function(){
            var that = this;
            $(this.toolbar).find("a:eq(0)").on("click", function(){that.load()});
            $(this.toolbar).find("a:eq(0)").click();
        },
        load : function(){
            var title = $(this.toolbar).find("[name='title']").val();
            $(this.grid).datagrid({
                url : '${basePath}/admin/rep/ven/listTimeOpen',
                queryParams : {title: title}
            });
        },
        munit2hour: function(v, r, i){
            if (v){
                var munit = Number(v);
                var _hour = Math.floor(munit/60*10);
                return _hour/10;
            }else{
                return 0;
            }
        },
        openrate : function(v, r, i){
            if (r.allminute && r.openminute){
                var allminute = Number(r.allminute);
                var openminute = Number(r.openminute);

                allminute = Math.floor(allminute/60*10)/10;
                openminute = Math.floor(openminute/60*10)/10;

                var _rate = Math.floor((openminute/allminute)*10000);
                return (_rate/100) + "%"
            }else{
                return "-"
            }
        }
    };

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
                url : '${basePath}/admin/rep/ven/listTimeOrder',
                queryParams : {title: title}
            });
        },
        orderrate:function(v,r,i){
            if (r.ordernimute && r.openminute){
                var ordernimute = Number(r.ordernimute);
                var openminute = Number(r.openminute);
                var _rate = Math.floor((ordernimute/openminute)*10000);
                return (_rate/100) + "%"
            }else{
                return "-"
            }
        },
        unorderrate: function(v,r,i){
            if (r.ordernimute && r.openminute){
                /*var ordernimute = Number(r.ordernimute);
                var openminute = Number(r.openminute);
                var _rate = Math.floor(((openminute-ordernimute)/openminute)*10000);
                return (_rate/100) + "%"*/

                var ordernimute = Number(r.ordernimute);
                var openminute = Number(r.openminute);
                var _rate = Math.floor((ordernimute/openminute)*10000);
                return ( (10000-_rate)/100 ) + "%"
            }else{
                return "-"
            }
        }
    };


    var myHighcharts = {
        chart : {  type: 'column' },
        title : { text: '' },
        subtitle : { text: ''},
        tooltip : {
            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0"> </td>' +
            '<td style="padding:0"><b>{point.y:1f}</b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
        },
        xAxis : {
            categories: [],
            crosshair: true
        },
        yAxis : {
            min: 0,
            title: {
                text: '发布量'
            }
        },
        series : [{data:[], name:"num"}],
        plotOptions : {
            column: {
                pointPadding: 0.2,
                borderWidth: 0
            }
        },
        credits: { enabled: false },

        getJson: function(){
            var json = {};
            json.chart = this.chart;
            json.title = this.title;
            json.subtitle = this.subtitle;
            json.tooltip = this.tooltip;
            json.xAxis = this.xAxis;
            json.yAxis = this.yAxis;
            json.series = this.series;
            json.plotOptions = this.plotOptions;
            json.credits = this.credits;

            var _json = $.extend(true, {}, json);

            return _json;
        },

        Chart4Type: function(){

            var json = this.getJson();

            json.title.text = "各类型场馆发布量";
            json.yAxis.title.text = "各类型场馆发布量";

            $.ajax({
                url: "${basePath }/admin/rep/ven/chart4type",
                dataType: "JSON",
                success: function(data){
                    json.series[0].data = data.data;
                    json.series[0].name = "活动室发布量";
                    json.xAxis.categories = data.categories;

                    $('#ven_type_column').highcharts(json);
                }
            })
        },

        Chart4Area: function(){
            var json = this.getJson();

            json.title.text = "各区域场馆发布量";
            json.yAxis.title.text = "各区域场馆发布量";

            $.ajax({
                url: "${basePath }/admin/rep/ven/chart4area",
                dataType: "JSON",
                success: function(data){
                    json.series[0].data = data.data;
                    json.series[0].name = "活动室发布量";
                    json.xAxis.categories = data.categories;

                    $('#ven_area_column').highcharts(json);
                }
            })
        },

        Chart4Year: function(){
            var json = this.getJson();

            json.title.text = "1-12月份场馆发布统计";
            json.yAxis.title.text = "1-12月份场馆发布统计";

            $.ajax({
                url: "${basePath }/admin/rep/ven/chart4year",
                dataType: "JSON",
                success: function(data){
                    json.series[0].data = data.data;
                    json.series[0].name = "活动室发布量";
                    json.xAxis.categories = data.categories;

                    $('#ven_year_column').highcharts(json);
                }
            })

        }
    }

</script>
</body>
</html>
