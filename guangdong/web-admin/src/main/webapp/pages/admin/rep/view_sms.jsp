<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<%
    request.setAttribute("nowSys", System.currentTimeMillis());
%>
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

    <style>
        h4{padding-top: 15px}
    </style>
</head>
<body>
<div class="container-fluid">

    <!-- 短信发送量统计 Begin -->
    <div class="row">
        <div class="col-md-12">
            <h4><i class="glyphicon glyphicon-align-justify"></i> 短信发送量统计</h4>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <div id="tt" class="easyui-tabs" style="width:100%;" data-options="plain:false">
                <div title=" 活动 " style="display:none;">
                    <table id="actSmsGrid" class="easyui-datagrid" style="width: 100%;"
                           toolbar="#actSmsGtb" pagination=true pageSize=30 pageList="[10,20,30,50,100,200]" loadMsg="数据加载中，请稍候。。。"
                           data-options="striped:true, rownumbers:true, fitColumns:true">
                        <thead>
                        <tr>
                            <th data-options="field:'title', width:40 ">活动</th>
                            <th data-options="field:'ydcount', width:60">短信发送量</th>
                        </tr>
                        </thead>
                    </table>
                    <div id="actSmsGtb" style="display: none;">
                        <input name="year">
                        <input name="month">
                        <a class="easyui-linkbutton" iconCls="icon-search">查 询</a>
                    </div>
                </div>
                <div title=" 培训 " style="display:none;">
                    <table id="traSmsGrid" class="easyui-datagrid" style="width: 100%;"
                           toolbar="#traSmsGtb" pagination=true pageSize=30 pageList="[10,20,30,50,100,200]" loadMsg="数据加载中，请稍候。。。"
                           data-options="striped:true, rownumbers:true, fitColumns:true">
                        <thead>
                        <tr>
                            <th data-options="field:'title', width:40 ">培训</th>
                            <th data-options="field:'ydcount', width:60">短信发送量</th>
                        </tr>
                        </thead>
                    </table>
                    <div id="traSmsGtb" style="display: none;">
                        <input name="year">
                        <input name="month">
                        <a class="easyui-linkbutton" iconCls="icon-search">查 询</a>
                    </div>
                </div>
                <div title=" 场馆 " style="display:none;">
                    <table id="venSmsGrid" class="easyui-datagrid" style="width: 100%;"
                           toolbar="#venSmsGtb" pagination=true pageSize=30 pageList="[10,20,30,50,100,200]" loadMsg="数据加载中，请稍候。。。"
                           data-options="striped:true, rownumbers:true, fitColumns:true">
                        <thead>
                        <tr>
                            <th data-options="field:'title', width:40 ">活动室</th>
                            <th data-options="field:'ydcount', width:60">短信发送量</th>
                        </tr>
                        </thead>
                    </table>
                    <div id="venSmsGtb" style="display: none;">
                        <input name="year">
                        <input name="month">
                        <a class="easyui-linkbutton" iconCls="icon-search">查 询</a>
                    </div>
                </div>
            </div>



        </div>
    </div>
    <!-- 短信发送量统计 End -->

    <!-- 资讯访问统计 Begin -->
    <div class="row">
        <div class="col-md-12">
            <h4><i class="glyphicon glyphicon-align-justify"></i> 资讯访问统计</h4>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <table id="zxCountGrid" class="easyui-datagrid" style="width: 100%;"
                   toolbar="#zxCountGtb" pagination=true pageSize=30 pageList="[10,20,30,50,100,200]" loadMsg="数据加载中，请稍候。。。"
                   data-options="striped:true, rownumbers:true, fitColumns:true">
                <thead>
                <tr>
                    <th data-options="field:'title', width:40">资讯标题</th>
                    <th data-options="field:'sumcount', width:20">总访问量</th>
                    <th data-options="field:'pccount', width:20">PC访问量</th>
                    <th data-options="field:'wxcount', width:20">微信访问量</th>
                </tr>
                </thead>
            </table>

            <div id="zxCountGtb" style="display: none;">
                <input name="year">
                <input name="month">
                <a class="easyui-linkbutton" iconCls="icon-search">查 询</a>
            </div>
        </div>
    </div>
    <!-- 资讯访问统计 End -->
</div>

<script>
    var venGrid = new MyGrid();
    var traGrid = new MyGrid();
    var actGrid = new MyGrid();
    var zxcGrid = new MyGrid();

    $(function(){
        venGrid.init("#venSmsGrid","#venSmsGtb","${basePath}/admin/rep/sms/smsVenCount");
        traGrid.init("#traSmsGrid","#traSmsGtb","${basePath}/admin/rep/sms/smsTraCount");
        actGrid.init("#actSmsGrid","#actSmsGtb","${basePath}/admin/rep/sms/smsActCount");
        zxcGrid.init("#zxCountGrid","#zxCountGtb","${basePath}/admin/rep/sms/zxCount");
    });

    function MyGrid(){
        this.grid = "";
        this.toolbar = "";
        this.url = "";
        this.nowSys = '${nowSys}';

        if (typeof MyGrid.__initialization__ != 'undefined'){ return; }
        MyGrid.__initialization__ = true;

        MyGrid.prototype.init= function(grid, toolbar, url){
            this.grid = grid;
            this.toolbar = toolbar;
            this.url = url;

            this.showYearMonth();

            var that = this;
            $(this.toolbar).find("a.easyui-linkbutton:eq(0)").on("click", function(){that.load()});
            $(this.toolbar).find("a.easyui-linkbutton:eq(0)").click();
        };

        MyGrid.prototype.load = function () {
            var title = $(this.toolbar).find("[name='title']").val();
            var serializearray = $(this.toolbar).find("[name]").serializeArray();
            var params = {};
            for(var i in serializearray){
                var p = serializearray[i];
                if (p.value && p.name){
                    params[p.name] = p.value;
                }
            }

            $(this.grid).datagrid({
                url : this.url,
                queryParams : params
            });
        };

        MyGrid.prototype.showYearMonth = function () {
            var year = $(this.toolbar).find("[name='year']");
            var month = $(this.toolbar).find("[name='month']");

            //当前时间取年
            var now = new Date();
            if (this.nowSys) {
                now = new Date(Number(this.nowSys));
            }
            var nowYear = now.getFullYear();
            var nowMonth = now.getMonth()+1;
            var yearData = [];
            for(var i=0; i<10; i++){
                var value = nowYear-i;
                yearData.push({value: value, text: value+" 年"});
            }
            var monthData = [];
            for (var i=1; i<=12; i++){
                monthData.push({value: i, text: i+" 月"});
            }

            year.combobox({
                limitToList: true,
                data: yearData,
                value : nowYear
            });

            month.combobox({
                limitToList: true,
                data: monthData,
                value: nowMonth
            });
        }
    }

</script>
</body>
</html>
