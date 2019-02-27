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
    <link rel="stylesheet" type="text/css" href="${basePath }/static/easyui/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="${basePath }/static/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${basePath }/static/easyui/themes/color.css">

    <script type="text/javascript" src="${basePath }/static/easyui/jquery.min.js" ></script>
    <script type="text/javascript" src="${basePath }/static/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${basePath }/static/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="${basePath }/static/bootstrap2.3.2/js/bootstrap.min.js"></script>




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
                <%--<div class="widget-header">
                    <h4><i class="icon-align-justify"></i>消息提醒</h4>
                </div>--%>
                <div class="widget-content">
                    <div class="row-fluid">
                        <div class="span12">

                                <div id="tt" class="easyui-tabs" style="width:500px;height:500px;" fit="true" justified="true" pill="true">
                                    <div title="内部供需" style="padding:20px;">
                                        <!-- 内部供需表格 -->
                                        <table id="nbdg">

                                        </table>
                                        <!-- 内部供需表格 END -->
                                    </div>
                                    <div title="外部供需" data-options="" style="overflow:auto;padding:20px;">
                                        <!-- 外部供需表格 -->
                                        <table id="wbdg">

                                        </table>
                                        <!-- 外部供需表格 END -->
                                    </div>
                                    <div title="网络培训" data-options="" style="padding:20px;">
                                        <!-- 网络培训表格 -->
                                        <table id="wlpx">

                                        </table>
                                        <!-- 网络培训表格 END -->
                                    </div>
                                </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<script type="text/javascript">
    var pmslist = "";
    $(function () {
        $.post('${basePath}/admin/remind/getPmsList', {id:'${sessionAdminUser.id}'}, function(data){
            pmslist = data.data;
            nbgxLoad();
            wbgxLoad();
            wlpxLoad();
        }, 'json');


    })

    function isInArray(arr,value){
        for(var i = 0; i < arr.length; i++){
            if(value == arr[i]){
                return true;
            }
        }
        return false;
    }

    /**内部供需*/
    function nbgxLoad() {

        $('#nbdg').datagrid({
            title : '内部供需待办事项表',
            iconCls : 'icon-a_detail',
            fit : true,
            fitColumns : true,
            singleSelect : true,
            border : false,
            striped : true,
            columns: [
                [{field: 'title', width: 100, align: 'left'},
                    {field: 'tags', title: '操作', width: 50, align: 'center'}
                ]]
        });

        var params = {cultid: ${cultid}};
        $.post('${basePath}/admin/remind/nbgxsrchList', params, function(data){
            var myData = new Array();
            var _data = new Array();
            var dgData = new Array();
            var _dgData = data.data;
            for (var i = 0;i< pmslist.length;i++){
                var aaa = pmslist[i].split(":",1);
                myData.push(aaa);
            }
            dgData = _getNbData(myData,_data,_dgData);
            $("#nbdg").datagrid("loadData", dgData);

        }, 'json');
    }

    function _getNbData(myData,_data,_dgData) {
        var _value = 'WHG_SUPPLY_TRA';
        var _result = isInArray(myData,_value);

        if(_result == true && _dgData[0].trauncheck != 0){
            _data.push({
                'title': '您有 ' + _dgData[0].trauncheck + ' 条内部供需培训待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_4")>处理</a>'
            }, {
                'title': '您有 ' + _dgData[0].traunpub + ' 条内部供需培训待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_4")>处理</a>'
            });
        }
        _value = 'WHG_SHOW_EXH';
        _result = isInArray(myData,_value);
        if(_result == true && _dgData[0].exhuncheck != 0){
            _data.push({
                'title': '您有 ' + _dgData[0].exhuncheck + ' 条内部供需展览展示待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_4")>处理</a>'
            }, {
                'title': '您有 ' + _dgData[0].exhunpub + ' 条内部供需展览展示待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_4")>处理</a>'
            });
        }

        _value = 'M_GXPS_VEN_LIST_C';
        _result = isInArray(myData,_value);
        if(_result == true && _dgData[0].venuncheck != 0){
            _data.push({
                'title': '您有 ' + _dgData[0].venuncheck + ' 条内部供需场馆待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_4")>处理</a>'
            });
        }

        _value = 'M_GXPS_VEN_LIST_P';
        _result = isInArray(myData,_value);
        if(_result == true && _dgData[0].venunpub != 0){
            _data.push({
                'title': '您有 ' + _dgData[0].venunpub + ' 条内部供需场馆待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_4")>处理</a>'
            });
        }

        _value = 'M_GXPS_ROOM_LIST_C';
        _result = isInArray(myData,_value);
        if(_result == true && _dgData[0].roomuncheck != 0){
            _data.push({
                'title': '您有 ' + _dgData[0].roomuncheck + ' 条内部供需活动室待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_4")>处理</a>'
            });
        }

        _value = 'M_GXPS_ROOM_LIST_P';
        _result = isInArray(myData,_value);
        if(_result == true && _dgData[0].roomunpub != 0){
            _data.push({
                'title': '您有 ' + _dgData[0].roomunpub + ' 条内部供需活动室待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_4")>处理</a>'
            });
        }

        _value = 'WHG_EXH_GOODS';
        _result = isInArray(myData,_value);
        if(_result == true && _dgData[0].exhgoodsuncheck != 0){
            _data.push({
                'title': '您有 ' + _dgData[0].exhgoodsuncheck + ' 条内部供需展品待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_4")>处理</a>'
            }, {
                'title': '您有 ' + _dgData[0].exhgoodsunpub + ' 条内部供需展品待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_4")>处理</a>'
            });
        }

        _value = 'WHG_SHOW_ORGAN';
        _result = isInArray(myData,_value);
        if(_result == true && _dgData[0].organuncheck != 0){
            _data.push({
                'title': '您有 ' + _dgData[0].organuncheck + ' 条内部供需组织机构待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_4")>处理</a>'
            }, {
                'title': '您有 ' + _dgData[0].organunpub + ' 条内部供需组织机构待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_4")>处理</a>'
            });
        }

        _value = 'WHG_SHOW_GOODS';
        _result = isInArray(myData,_value);
        if(_result == true && _dgData[0].sguncheck != 0){
            _data.push({
                'title': '您有 ' + _dgData[0].sguncheck + ' 条内部供需文艺演出待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_4")>处理</a>'
            }, {
                'title': '您有 ' + _dgData[0].sgunpub + ' 条内部供需文艺演出待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_4")>处理</a>'
            });
        }

        _value = 'WHG_GXPS_GOODS';
        _result = isInArray(myData,_value);
        if(_result == true && _dgData[0].goodsuncheck != 0){
            _data.push({
                'title': '您有 ' + _dgData[0].goodsuncheck + ' 条内部供需文艺辅材待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_4")>处理</a>'
            }, {
                'title': '您有 ' + _dgData[0].goodsunpub + ' 条内部供需文艺辅材待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_4")>处理</a>'
            });
        }

        _value = 'WHG_GXPS_NEED_CHECK';
        _result = isInArray(myData,_value);
        if(_result == true && _dgData[0].supuncheck != 0){
            _data.push({
                'title': '您有 ' + _dgData[0].supuncheck + ' 条内部供需需求待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_4")>处理</a>'
            }, {
                'title': '您有 ' + _dgData[0].supunpub + ' 条内部供需需求待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_4")>处理</a>'
            });
        }

        _value = 'WHG_GXPS_NEED_CHECK';
        _result = isInArray(myData,_value);
        if(_result == true && _dgData[0].peruncheck != 0){
            _data.push({'title': '您有 ' + _dgData[0].peruncheck + ' 条内部供需文艺专家待审核'}, {
                'title': '您有 ' + _dgData[0].perunpub + ' 条内部供需文艺专家待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_4")>处理</a>'
            });
        }
        return _data;
    }




    /**外部供需*/
    function wbgxLoad() {
        $('#wbdg').datagrid({
            title : '外部供需待办事项表',
            iconCls : 'icon-a_detail',
            fit : true,
            fitColumns : true,
            singleSelect : true,
            border : false,
            striped : true,
            columns: [[{field: 'title', width: 100, align: 'left'},
                {field: 'tags', title: '操作', width: 50, align: 'center'}]]
        });


        var params = {cultid: ${cultid}};
        $.post('${basePath}/admin/remind/wbgxsrchList', params, function(data){
            var myWbData = new Array();
            var _Wbdata = new Array();
            var WbdgData = new Array();
            var _wbdgData = data.data;
            for (var i = 0;i< pmslist.length;i++){
                myWbData[i] = pmslist[i].split(":",1);
            }
            WbdgData = _getWbData(myWbData,_Wbdata,_wbdgData);
            $("#wbdg").datagrid("loadData", WbdgData);
        }, 'json');
    }



        function _getWbData(myData,_data,_dgData) {
            var _value = 'M_VEN_LIST_CHECK';
            var _result = isInArray(myData,_value);
            if(_result == true && _dgData[0].venuncheck != 0){
                _data.push({
                    'title': '您有 ' + _dgData[0].venuncheck + ' 条外部供需场馆待审核',
                    'tags': '<a href="javascript:void(0)" onclick=doTurn("M_WBGXGL","M_VEN","M_VEN_LIST_CHECK")>处理</a>'
                });
            }

            _value = 'M_VEN_LIST_PUBLISH';
            _result = isInArray(myData,_value);
            if(_result == true && _dgData[0].venunpub != 0){
                _data.push({
                    'title': '您有 ' + _dgData[0].venunpub + ' 条外部供需场馆待发布',
                    'tags': '<a href="javascript:void(0)" onclick=doTurn("M_WBGXGL","M_VEN","M_VEN_LIST_PUBLISH")>处理</a>'
                });
            }

            _value = 'M_VEN_ROOM_LIST_CHECK';
            _result = isInArray(myData,_value);
            if(_result == true && _dgData[0].roomuncheck != 0){
                _data.push({
                    'title': '您有 ' + _dgData[0].roomuncheck + ' 条外部供需活动室待审核',
                    'tags': '<a href="javascript:void(0)" onclick=doTurn("M_WBGXGL","M_VEN_ROOM","M_VEN_ROOM_LIST_CHECK")>处理</a>'
                });
            }

            _value = 'M_VEN_ROOM_LIST_PUBLISH';
            _result = isInArray(myData,_value);
            if(_result == true && _dgData[0].roomunpub != 0){
                _data.push({
                    'title': '您有 ' + _dgData[0].roomunpub + ' 条外部供需活动室待发布',
                    'tags': '<a href="javascript:void(0)" onclick=doTurn("M_WBGXGL","M_VEN_ROOM","M_VEN_ROOM_LIST_PUBLISH")>处理</a>'
                });
            }

            _value = 'M_VEN_ROOM_LIST_PUBLISH';
            _result = isInArray(myData,_value);
            if(_result == true && _dgData[0].vrouncheck != 0){
                _data.push({
                    'title': '您有 ' + _dgData[0].vrouncheck + ' 条外部供需活动室订单待审核',
                    'tags': '<a href="javascript:void(0)" onclick=doTurn("M_WBGXGL","M_VEN_ROOM","M_VEN_ROOM_LIST_PUBLISH")>处理</a>'
                });
            }

            _value = 'M_ACT_CHECK';
            _result = isInArray(myData,_value);
            if(_result == true && _dgData[0].actuncheck != 0){
                _data.push({
                    'title': '您有 ' + _dgData[0].actuncheck + ' 条外部供需活动待审核',
                    'tags': '<a href="javascript:void(0)" onclick=doTurn("M_WBGXGL","M_ACT","M_ACT_CHECK")>处理</a>'
                });
            }

            _value = 'M_ACT_PUBLISH';
            _result = isInArray(myData,_value);
            if(_result == true && _dgData[0].actunpub != 0){
                _data.push({
                    'title': '您有 ' + _dgData[0].actunpub + ' 条外部供需活动待发布',
                    'tags': '<a href="javascript:void(0)" onclick=doTurn("M_WBGXGL","M_ACT","M_ACT_PUBLISH")>处理</a>'
                });
            }
            return _data;
        }


    /**网络培训*/
    function wlpxLoad() {


        $('#wlpx').datagrid({
            title : '网络培训待办事项表',
            iconCls : 'icon-a_detail',
            fit : true,
            fitColumns : true,
            singleSelect : true,
            border : false,
            striped : true,
            columns: [
                [{field: 'title', width: 100, align: 'left'},
                    {field: 'tags', title: '操作', width: 50, align: 'center'}
                ]]
        });


        var params = {cultid: ${cultid}};
        $.post('${basePath}/admin/remind/wlpxsrchList', params, function(data){
            var myPxData = new Array();
            var _Pxdata = new Array();
            var PxdgData = new Array();
            var _pxdgData = data.data;
            for (var i = 0;i< pmslist.length;i++){
                myPxData[i] = pmslist[i].split(":",1);
            }
            PxdgData = _getPxData(myPxData,_Pxdata,_pxdgData);
            $("#wlpx").datagrid("loadData", PxdgData);

        }, 'json');
    }
    function _getPxData(myData,_data,_dgData) {
        var _value = 'whg_outer_px_3';
        var _result = isInArray(myData,_value);
        if(_result == true && _dgData[0].trauncheck != 0){
            _data.push({
                'title': '您有 ' + _dgData[0].trauncheck + ' 条线下培训待审核',
                'tags': '<a  href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_3")>处理</a>'
            });
        }

        _value = 'whg_outer_px_4';
        _result = isInArray(myData,_value);
        if(_result == true && _dgData[0].traunpub != 0){
            _data.push({
                'title': '您有 ' + _dgData[0].traunpub + ' 条线下培训待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_4")>处理</a>'
            });
        }

        _value = 'whg_outer_px_4';
        _result = isInArray(myData,_value);
        if(_result == true && _dgData[0].enroluncheck != 0){
            _data.push({
                'title': '您有 ' + _dgData[0].enroluncheck + ' 条线下培训订单待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_live_px","whg_outer_px_4")>处理</a>'
            });
        }

        _value = 'whg_outer_px_4';
        _result = isInArray(myData,_value);
        if(_result == true && _dgData[0].enrolunview != 0){
            _data.push({
                'title': '您有 ' + _dgData[0].enrolunview + ' 条线下培训订单待面试',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_4")>处理</a>'
            });
        }

        _value = 'whg_outer_px_6';
        _result = isInArray(myData,_value);
        alert(_result +"---"+_dgData[0].leaveuncheck);
        if(_result == true && _dgData[0].leaveuncheck != 0){
            _data.push({
                'title': '您有 ' + _dgData[0].leaveuncheck + ' 条线下培训课程请假信息待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_6")>处理</a>'
            });
        }

        _value = 'whg_live_px_3';
        _result = isInArray(myData,_value);
        if(_result == true && _dgData[0].traliveuncheck != 0){
            _data.push({
                'title': '您有 ' + _dgData[0].traliveuncheck + ' 条在线课程待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_live_px","whg_live_px_3")>处理</a>'
            });
        }

        _value = 'whg_live_px_4';
        _result = isInArray(myData,_value);
        if(_result == true && _dgData[0].traliveunpub != 0){
            _data.push({
                'title': '您有 ' + _dgData[0].traliveunpub + ' 条在线课程待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_live_px","whg_live_px_4")>处理</a>'
            });
        }

        _value = 'M_TRA_MAJOR_PUB';
        _result = isInArray(myData,_value);
        if(_result == true && _dgData[0].majoruncheck != 0){
            _data.push({
                'title': '您有 ' + _dgData[0].majoruncheck + ' 条微专业待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","M_TRA_MAJOR","M_TRA_MAJOR_C")>处理</a>'
            }, {
                'title': '您有 ' + _dgData[0].majorunpub + ' 条微专业待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","M_TRA_MAJOR","M_TRA_MAJOR_P")>处理</a>'
            });
        }

        _value = 'M_TRA_TEA_PUB';
        _result = isInArray(myData,_value);
        if(_result == true && _dgData[0].teauncheck != 0){
            _data.push({
                'title': '您有 ' + _dgData[0].teauncheck + ' 条培训师资待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","M_TRA_TEA","M_TRA_TEA_C")>处理</a>'
            }, {
                'title': '您有 ' + _dgData[0].teaunpub + ' 条培训师资待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","M_TRA_TEA","M_TRA_TEA_P")>处理</a>'
            });
        }

        _value = 'M_TRA_DRSC_PUB';
        _result = isInArray(myData,_value);
        if(_result == true && _dgData[0].drscuncheck != 0){
            _data.push({
                'title': '您有 ' + _dgData[0].drscuncheck + ' 条培训资源待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","M_TRA_DRSC","M_TRA_DRSC_C")>处理</a>'
            }, {
                'title': '您有 ' + _dgData[0].drscunpub + ' 条培训资源待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","M_TRA_DRSC","M_TRA_DRSC_P")>处理</a>'
            });
        }
        return _data;
    }

    function doTurn(ppid, pid, id) {
        window.parent.doOpenMenu(ppid, pid, id);
    }

</script>
</body>
</html>