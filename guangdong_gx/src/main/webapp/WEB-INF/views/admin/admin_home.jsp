<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()); %>
<% request.setAttribute("viewBasePath", request.getScheme() + "://" + request.getServerName() + request.getContextPath()); %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>数字文化馆后台管理系统</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${basePath}/static/bootstrap2.3.2/css/mian.css" rel="stylesheet" media="screen">
    <link href="${basePath}/static/bootstrap2.3.2/css/bootstrap.css" rel="stylesheet" media="screen">
    <script type="text/javascript" src="${basePath }/static/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath }/static/bootstrap2.3.2/js/bootstrap.min.js"></script>
    <style>
        .box{
            border: 1px solid #d9d9d9;
            margin-top: 10px;
        }
        .box .box-header{
             height: 35px;
             line-height: 35px;
             background-color: #ddd;
         }
        .box .box-header span{
            margin-left: 10px;
            font-size: 14px;
            font-weight: 800;
        }
        .box .box-header i{
            color:red;
            font-size: 18px;
            font-weight: 800;
        }
        span.num{
            color:red;
            font-size: 18px;
            font-weight: 400;
        }
        .propName{
            width: 70px;
            display: inline-block;
        }
        .logoImg{
            border: 1px solid #bbb;
            border-radius: 5px;
        }
        .cultImg{
            border-bottom: 1px solid #bbb;
        }
    </style>
</head>
<body style="overflow-x: hidden">

<div class="container-fluid">
    <div class="crumbs" style="border: 1px solid #d9d9d9;">
        <ul id="breadcrumbs" class="breadcrumb">
            <li><i class="icon-home"></i><a href="javascript:void(0);">欢迎您进入文化馆业务管理系统！</a></li>
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
        <div class="span8">
            <div class="box">
                <div class="box-header"><span>待办事项-内部供需</span>(<i class="nbgx-total">0</i>)</div>
                <div class="box-body">
                    <table class="table table-striped table-hover" style="margin-bottom: 0px;">
                        <tbody class="table-hover nbgx-data">
                        </tbody>
                    </table>
                    <p>没有待办信息</p>
                </div>
            </div>

            <div class="box">
                <div class="box-header"><span>待办事项-培训管理</span>(<i class="pxgl-total">0</i>)</div>
                <div class="box-body">
                    <table class="table table-striped table-hover" style="margin-bottom: 0px;">
                        <tbody class="table-hover pxgl-data">
                        </tbody>
                    </table>
                    <p>没有待办信息</p>
                </div>
            </div>

            <div class="box">
                <div class="box-header"><span>待办事项-外部供需</span>(<i class="wbgx-total">0</i>)</div>
                <div class="box-body">
                    <table class="table table-striped table-hover" style="margin-bottom: 0px;">
                        <tbody class="table-hover wbgx-data">
                        </tbody>
                    </table>
                    <p>没有待办信息</p>
                </div>
            </div>

            <div class="box">
                <div class="box-header"><span>待办事项-群文管理</span>(<i class="qwgl-total">0</i>)</div>
                <div class="box-body">
                    <table class="table table-striped table-hover" style="margin-bottom: 0px;">
                        <tbody class="table-hover qwgl-data">
                        </tbody>
                    </table>
                    <p>没有待办信息</p>
                </div>
            </div>
        </div>

        <div class="span4">
            <div class="box">
                <div class="box-header"><span>本馆信息</span></div>
                <div class="box-body">
                    <div class="box-body-img"><img class="cultImg" src="${imgPath}${cultInfo.picture}" alt="本馆图片"/></div>
                    <div class="box-body-title"><h4>　${cultInfo.name}　</h4></div>
                    <div class="box-body-info">
                        <p><span class="propName">　站点地址</span>：<a href="${basePath}/web/${cultInfo.cultsite}/index"
                                                                  target="_blank">${viewBasePath}/${cultInfo.cultsite}/index</a>
                        </p>
                        <p><span class="propName">　H5地址</span>：<a href="${basePath}/wechat" target="_blank">${viewBasePath}/wechat</a></p>
                        <p><span class="propName">　LOGO</span>：<img class="logoImg"
                                                                    <c:if test="${not empty cultInfo.logopicture}">src="${imgPath}${cultInfo.logopicture}"</c:if>
                                                                    <c:if test="${empty cultInfo.logopicture}">src="${basePath}/static/img/frontLogo.png"</c:if>
                                                                    alt="LOGO图片"/></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<script>
    var pmslist = "";//权限菜单
    var managerCults = ${sessionAdminUserCults};//用户权限馆
    var g_cultid = managerCults && managerCults.length ? (managerCults[0].id || ''): '';

    $(function () {
        $.post('${basePath}/admin/remind/getPmsList', {id:'${sessionAdminUser.id}'}, function(data){
            pmslist = data.data;
            filterPMSList(pmslist);
            nbgxLoad();
            wbgxLoad();
            wlpxLoad();
            massLoad();
        }, 'json');
    });

    var pmsFilters = [];
    function filterPMSList(pmslist){
        if (pmsFilters.length){
            return pmsFilters;
        }
        var pmsFilterOpts = ['checkon', 'publish', 'baoming', 'reserve', 'leave'];
        for (var i = 0;i< pmslist.length;i++){
            var pmsitemarr = pmslist[i].split(":",2);
            if (pmsitemarr && pmsitemarr.length>1 && $.inArray(pmsitemarr[1], pmsFilterOpts)!=-1){
                pmsFilters.push(pmsitemarr);
            }
        }
    }

    /**是否存在数组中*/
    function isInArray(arr,value, pmsItem){
        for(var i = 0; i < arr.length; i++){
            if (!arr[i].length){
                continue;
            }
            if (pmsItem && arr[i].length>1){
                if (value == arr[i][0] && pmsItem == arr[i][1]){
                    return true;
                }
            }
            else if (value == arr[i][0]){
                return true;
            }
        }
        return false;
    }

    /**内部供需*/
    function nbgxLoad(){
        var params = {cultid: g_cultid};
        $.post('${basePath}/admin/remind/nbgxsrchList', params, function(data){
            var myData = pmsFilters;//new Array();
            var _data = new Array();
            var _dgData = data.data;
            /*for (var i = 0;i< pmslist.length;i++){
                var aaa = pmslist[i].split(":",1);
                myData.push(aaa);
            }*/
            var dgData = _getNbData(myData,_data,_dgData);
            if(dgData.cnt > 0){
                $('.nbgx-total').text(dgData.cnt);
                for(var i=0; i<dgData.arr.length; i++){
                    $('.nbgx-data').append('<tr><td>'+dgData.arr[i].title+'</td><td style="width:100px;">'+dgData.arr[i].tags+'</td></tr>');
                }
                $('.nbgx-data').parent('table').next('p').hide();
            }
        }, 'json');
    }

    function _getNbData(myData,_data,_dgData) {
        var _cnt = 0;

        var _value = 'WHG_SUPPLY_TRA_C';
        var _result = isInArray(myData,_value, 'checkon');
        if(_result == true && _dgData[0].trauncheck != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].trauncheck + '</span> 条内部供需培训待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("WHG_GXPS","WHG_SUPPLY_TRA","WHG_SUPPLY_TRA_C")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].trauncheck,10);
        }

        var _result = isInArray(myData,'WHG_SUPPLY_TRA_P', 'publish');
        if(_result == true && _dgData[0].traunpub != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].traunpub + '</span> 条内部供需培训待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("WHG_GXPS","WHG_SUPPLY_TRA","WHG_SUPPLY_TRA_P")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].traunpub,10);
        }


        _value = 'WHG_SHOW_EXH_C';
        _result = isInArray(myData,_value, 'checkon');
        if(_result == true && _dgData[0].exhuncheck != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].exhuncheck + '</span> 条内部供需展览展示待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("WHG_GXPS","M_WHG_SHOW_EXH","WHG_SHOW_EXH_C")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].exhuncheck,10);
        }

        _result = isInArray(myData,'WHG_SHOW_EXH_P', 'publish');
        if(_result == true && _dgData[0].exhunpub != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].exhunpub + '</span> 条内部供需展览展示待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("WHG_GXPS","M_WHG_SHOW_EXH","WHG_SHOW_EXH_P")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].exhunpub,10);
        }

        _value = 'M_GXPS_VEN_LIST_C';
        _result = isInArray(myData,_value, 'checkon');
        if(_result == true && _dgData[0].venuncheck != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].venuncheck + '</span> 条内部供需场馆待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("WHG_GXPS","M_GXPS_MENUS_VEN","M_GXPS_VEN_LIST_C")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].venuncheck,10);
        }

        _value = 'M_GXPS_VEN_LIST_P';
        _result = isInArray(myData,_value, 'publish');
        if(_result == true && _dgData[0].venunpub != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].venunpub + '</span> 条内部供需场馆待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("WHG_GXPS","M_GXPS_MENUS_VEN","M_GXPS_VEN_LIST_P")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].venunpub,10);
        }

        _value = 'M_GXPS_ROOM_LIST_C';
        _result = isInArray(myData,_value, 'checkon');
        if(_result == true && _dgData[0].roomuncheck != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].roomuncheck + '</span> 条内部供需活动室待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("WHG_GXPS","M_GXPS_MENUS_ROOM","M_GXPS_ROOM_LIST_C")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].roomuncheck,10);
        }

        _value = 'M_GXPS_ROOM_LIST_P';
        _result = isInArray(myData,_value, 'publish');
        if(_result == true && _dgData[0].roomunpub != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].roomunpub + '</span> 条内部供需活动室待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("WHG_GXPS","M_GXPS_MENUS_ROOM","M_GXPS_ROOM_LIST_P")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].roomunpub,10);
        }

        _value = 'M_EXH_GOODS_LIST_C';
        _result = isInArray(myData,_value, 'checkon');
        if(_result == true && _dgData[0].exhgoodsuncheck != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].exhgoodsuncheck + '</span> 条内部供需展品待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("WHG_GXPS","M_GXPS_MENUS_EXHGOODS","M_EXH_GOODS_LIST_C")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].exhgoodsuncheck,10);
        }

        _result = isInArray(myData,'M_EXH_GOODS_LIST_P', 'publish');
        if(_result == true && _dgData[0].exhgoodsunpub != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].exhgoodsunpub + '</span> 条内部供需展品待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("WHG_GXPS","M_GXPS_MENUS_EXHGOODS","M_EXH_GOODS_LIST_P")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].exhgoodsunpub,10);
        }

        _value = 'M_GXPS_ORGAN_LIST_C';
        _result = isInArray(myData,_value, 'checkon');
        if(_result == true && _dgData[0].organuncheck != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].organuncheck + '</span> 条内部供需组织机构待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("WHG_GXPS","M_GXPS_MENUS_ORGAN","M_GXPS_ORGAN_LIST_C")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].organuncheck,10);
        }

        _result = isInArray(myData,'M_GXPS_ORGAN_LIST_P', 'publish');
        if(_result == true && _dgData[0].organunpub != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].organunpub + '</span> 条内部供需组织机构待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("WHG_GXPS","M_GXPS_MENUS_ORGAN","M_GXPS_ORGAN_LIST_P")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].organunpub,10);
        }

        _value = 'WHG_SHOW_GOODS_C';
        _result = isInArray(myData,_value);
        if(_result == true && _dgData[0].sguncheck != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].sguncheck + '</span> 条内部供需文艺演出待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("WHG_GXPS","WHG_SHOW_GOODS","WHG_SHOW_GOODS_C")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].sguncheck,10);
        }
        _result = isInArray(myData,'WHG_SHOW_GOODS_P', 'publish');
        if(_result == true && _dgData[0].sgunpub != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].sgunpub + '</span> 条内部供需文艺演出待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("WHG_GXPS","WHG_SHOW_GOODS","WHG_SHOW_GOODS_P")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].sgunpub,10);
        }

        _value = 'M_GXPS_GOODS_LIST_C';
        _result = isInArray(myData,_value, 'checkon');
        if(_result == true && _dgData[0].goodsuncheck != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].goodsuncheck + '</span> 条内部供需文艺辅材待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("WHG_GXPS","M_GXPS_MENUS_GOODS","M_GXPS_GOODS_LIST_C")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].goodsuncheck,10);
        }
        _result = isInArray(myData,'M_GXPS_GOODS_LIST_P', 'publish');
        if(_result == true && _dgData[0].goodsunpub != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].goodsunpub + '</span> 条内部供需文艺辅材待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("WHG_GXPS","M_GXPS_MENUS_GOODS","M_GXPS_GOODS_LIST_P")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].goodsunpub,10);
        }

        _value = 'WHG_GXPS_NEED2';
        _result = isInArray(myData,_value, 'checkon');
        if(_result == true && _dgData[0].supuncheck != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].supuncheck + '</span> 条内部供需需求待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("WHG_GXPS","WHG_GXPS_NEED","WHG_GXPS_NEED2")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].supuncheck,10);
        }
        _result = isInArray(myData,'WHG_GXPS_NEED3', 'publish');
        if(_result == true && _dgData[0].supunpub != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].supunpub + '</span> 条内部供需需求待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("WHG_GXPS","WHG_GXPS_NEED","WHG_GXPS_NEED3")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].supunpub,10);
        }

        _value = 'WHG_GXPS_PERSONNEL002';
        _result = isInArray(myData,_value,'checkon');
        if(_result == true && _dgData[0].peruncheck != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].peruncheck + '</span> 条内部供需文艺专家待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("WHG_GXPS","WHG_GXPS_PERSONNEL","WHG_GXPS_PERSONNEL002")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].peruncheck,10);
        }
        isInArray(myData,'WHG_GXPS_PERSONNEL003','publish');
        if(_result == true && _dgData[0].perunpub != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].perunpub + '</span> 条内部供需文艺专家待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("WHG_GXPS","WHG_GXPS_PERSONNEL","WHG_GXPS_PERSONNEL003")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].perunpub,10);
        }
        return {"arr":_data, cnt:_cnt};
    }




    /**外部供需*/
    function wbgxLoad() {
        var params = {cultid: g_cultid};
        $.post('${basePath}/admin/remind/wbgxsrchList', params, function(data){
            var myWbData = pmsFilters;//new Array();
            var _Wbdata = new Array();
            var _wbdgData = data.data ||[];
            /*for (var i = 0;i< pmslist.length;i++){
                myWbData[i] = pmslist[i].split(":",2);
            }*/
            var WbdgData = _getWbData(myWbData,_Wbdata,_wbdgData);
            if(WbdgData.cnt > 0){
                $('.wbgx-total').text(WbdgData.cnt);
                for(var i=0; i<WbdgData.arr.length; i++){
                    $('.wbgx-data').append('<tr><td>'+WbdgData.arr[i].title+'</td><td style="width:100px;">'+WbdgData.arr[i].tags+'</td></tr>');
                }
                $('.wbgx-data').parent('table').next('p').hide();
            }
        }, 'json');
    }



    function _getWbData(myData,_data,_dgData) {
        var _value = 'M_VEN_LIST_CHECK';
        var _result = isInArray(myData,_value, 'checkon');
        var _cnt = 0;

        if(_result == true && _dgData[0].venuncheck != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].venuncheck + '</span> 条外部供需场馆待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_WBGXGL","M_VEN","M_VEN_LIST_CHECK")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].venuncheck,10);
        }

        _value = 'M_VEN_LIST_PUBLISH';
        _result = isInArray(myData,_value, 'publish');
        if(_result == true && _dgData[0].venunpub != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].venunpub + '</span> 条外部供需场馆待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_WBGXGL","M_VEN","M_VEN_LIST_PUBLISH")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].venunpub,10);
        }

        _value = 'M_VEN_ROOM_LIST_CHECK';
        _result = isInArray(myData,_value, 'checkon');
        if(_result == true && _dgData[0].roomuncheck != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].roomuncheck + '</span> 条外部供需活动室待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_WBGXGL","M_VEN_ROOM","M_VEN_ROOM_LIST_CHECK")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].roomuncheck,10);
        }

        _value = 'M_VEN_ROOM_LIST_PUBLISH';
        _result = isInArray(myData,_value, 'publish');
        if(_result == true && _dgData[0].roomunpub != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].roomunpub + '</span> 条外部供需活动室待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_WBGXGL","M_VEN_ROOM","M_VEN_ROOM_LIST_PUBLISH")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].roomunpub,10);
        }

        //_value = 'M_VEN_ROOM_LIST_PUBLISH';
        _value = 'M_VEN_ROOM_LIST_ORDER';
        _result = isInArray(myData,_value, 'reserve');
        if(_result == true && _dgData[0].vrouncheck != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].vrouncheck + '</span> 条外部供需活动室订单待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_WBGXGL","M_VEN_ROOM","M_VEN_ROOM_LIST_ORDER")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].vrouncheck,10);
        }

        _value = 'M_ACT_CHECK';
        _result = isInArray(myData,_value, 'checkon');
        if(_result == true && _dgData[0].actuncheck != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].actuncheck + '</span> 条外部供需活动待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_WBGXGL","M_ACT","M_ACT_CHECK")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].actuncheck,10);
        }

        _value = 'M_ACT_PUBLISH';
        _result = isInArray(myData,_value, 'publish');
        if(_result == true && _dgData[0].actunpub != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].actunpub + '</span> 条外部供需活动待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_WBGXGL","M_ACT","M_ACT_PUBLISH")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].actunpub,10);
        }
        return {"arr":_data, cnt:_cnt};
    }


    /**网络培训*/
    function wlpxLoad() {
        var params = {cultid: "${cultid}"};
        $.post('${basePath}/admin/remind/wlpxsrchList', params, function(data){
            var myPxData = new Array();
            var _Pxdata = new Array();
            var _pxdgData = data.data || [];
            for (var i = 0;i< pmslist.length;i++){
                myPxData[i] = pmslist[i].split(":",1);
            }
            var PxdgData = _getPxData(myPxData,_Pxdata,_pxdgData);
            if(PxdgData.cnt > 0){
                $('.pxgl-total').text(PxdgData.cnt);
                for(var i=0; i<PxdgData.arr.length; i++){
                    $('.pxgl-data').append('<tr><td>'+PxdgData.arr[i].title+'</td><td style="width:100px;">'+PxdgData.arr[i].tags+'</td></tr>');
                }
                $('.pxgl-data').parent('table').next('p').hide();
            }
        }, 'json');
    }
    function _getPxData(myData,_data,_dgData) {
        var _value = 'whg_outer_px_3';
        var _result = isInArray(myData,_value,'checkon');
        var _cnt = 0;

        if(_result == true && _dgData[0].trauncheck != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].trauncheck + '</span> 条线下培训待审核',
                'tags': '<a  href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_3")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].trauncheck,10);
        }

        _value = 'whg_outer_px_4';
        _result = isInArray(myData,_value,'publish');
        if(_result == true && _dgData[0].traunpub != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].traunpub + '</span> 条线下培训待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_4")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].traunpub,10);
        }

        _value = 'whg_outer_px_order';
        _result = isInArray(myData,_value, 'baoming');
        if(_result == true && _dgData[0].enroluncheck != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].enroluncheck + '</span> 条线下培训订单待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_order")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].enroluncheck,10);
        }

        _value = 'whg_outer_px_order';
        _result = isInArray(myData,_value, 'baoming');
        if(_result == true && _dgData[0].enrolunview != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].enrolunview + '</span> 条线下培训订单待面试',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_order")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].enrolunview,10);
        }

        _value = 'whg_outer_px_6';
        _result = isInArray(myData,_value,'leave');
        if(_result == true && _dgData[0].leaveuncheck != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].leaveuncheck + '</span> 条线下培训课程请假信息待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_outer_px","whg_outer_px_6")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].leaveuncheck,10);
        }

        _value = 'whg_live_px_3';
        _result = isInArray(myData,_value,'checkon');
        if(_result == true && _dgData[0].traliveuncheck != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].traliveuncheck + '</span> 条在线课程待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_live_px","whg_live_px_3")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].traliveuncheck,10);
        }

        _value = 'whg_live_px_4';
        _result = isInArray(myData,_value,'publish');
        if(_result == true && _dgData[0].traliveunpub != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].traliveunpub + '</span> 条在线课程待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","whg_live_px","whg_live_px_4")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].traliveunpub,10);
        }

        _value = 'M_TRA_MAJOR_C';
        _result = isInArray(myData,_value,'checkon');
        if(_result == true && _dgData[0].majoruncheck != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].majoruncheck + '</span> 条微专业待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","M_TRA_MAJOR","M_TRA_MAJOR_C")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].majoruncheck,10);
        }
        _result = isInArray(myData,'M_TRA_MAJOR_P','publish');
        if(_result == true && _dgData[0].majorunpub != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].majorunpub + '</span> 条微专业待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","M_TRA_MAJOR","M_TRA_MAJOR_P")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].majorunpub,10);
        }

        _value = 'M_TRA_TEA_C';
        _result = isInArray(myData,_value, 'checkon');
        if(_result == true && _dgData[0].teauncheck != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].teauncheck + '</span> 条培训师资待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","M_TRA_TEA","M_TRA_TEA_C")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].teauncheck,10);
        }
        _result = isInArray(myData,'M_TRA_TEA_P', 'publish');
        if(_result == true && _dgData[0].teaunpub != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].teaunpub + '</span> 条培训师资待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","M_TRA_TEA","M_TRA_TEA_P")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].teaunpub,10);
        }

        _value = 'M_TRA_DRSC_C';
        _result = isInArray(myData,_value,'checkon');
        if(_result == true && _dgData[0].drscuncheck != 0){
            _data.push({
                'title': '您有 <span class="num">' + _dgData[0].drscuncheck + '</span> 条培训资源待审核',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","M_TRA_DRSC","M_TRA_DRSC_C")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].drscuncheck,10);
        }
        _result = isInArray(myData,'M_TRA_DRSC_P', 'publish');
        if(_result == true && _dgData[0].drscunpub != 0){
            _data.push( {
                'title': '您有 <span class="num">' + _dgData[0].drscunpub + '</span> 条培训资源待发布',
                'tags': '<a href="javascript:void(0)" onclick=doTurn("M_PXGL","M_TRA_DRSC","M_TRA_DRSC_P")>处理</a>'
            });
            _cnt += parseInt(_dgData[0].drscunpub,10);
        }
        return {"arr":_data, cnt:_cnt};
    }


    /**群文*/
    function massLoad(){
        $.post('${basePath}/admin/remind/massSrchList', function(data){
            var myData = pmsFilters; //new Array();
            var _dbData = data.data ||{};
            /*for (var i = 0;i< pmslist.length;i++){
                myData[i] = pmslist[i].split(":",1);
            }*/
            var allData = _getMessData(myData, _dbData);

            if(allData.cnt > 0){
                $('.qwgl-total').text(allData.cnt);
                for(var i=0; i<allData.arr.length; i++){
                    $('.qwgl-data').append('<tr><td>'+allData.arr[i].title+'</td><td style="width:100px;">'+allData.arr[i].tags+'</td></tr>');
                }
                $('.qwgl-data').parent('table').next('p').hide();
            }
        }, 'json');
    }
    function _getMessData(myData, dbData){
        var rest = {};
        rest.cnt = 0;
        rest.arr = [];

        var massKeys = [
            {typekey:'M_MAS_ZT_LIST_C', optkey:'checkon', parentkey:'M_MAS_MENUS_ZT', rootkey:'M_MAS_MENUS', text:'文化专题待审核'},
            {typekey:'M_MAS_ZT_LIST_P', optkey:'publish', parentkey:'M_MAS_MENUS_ZT', rootkey:'M_MAS_MENUS', text:'文化专题待发布'},
            {typekey:'M_MAS_JC_LIST_C', optkey:'checkon', parentkey:'M_MAS_MENUS_JC', rootkey:'M_MAS_MENUS', text:'专题届次待审核'},
            {typekey:'M_MAS_JC_LIST_P', optkey:'publish', parentkey:'M_MAS_MENUS_JC', rootkey:'M_MAS_MENUS', text:'专题届次待发布'},
            {typekey:'M_MAS_RC_LIST_C', optkey:'checkon', parentkey:'M_MAS_MENUS_RC', rootkey:'M_MAS_MENUS', text:'艺术人才待审核'},
            {typekey:'M_MAS_RC_LIST_P', optkey:'publish', parentkey:'M_MAS_MENUS_RC', rootkey:'M_MAS_MENUS', text:'艺术人才待发布'},
            {typekey:'M_MAS_TD_LIST_C', optkey:'checkon', parentkey:'M_MAS_MENUS_TD', rootkey:'M_MAS_MENUS', text:'艺术团队待审核'},
            {typekey:'M_MAS_TD_LIST_P', optkey:'publish', parentkey:'M_MAS_MENUS_TD', rootkey:'M_MAS_MENUS', text:'艺术团队待发布'},
            {typekey:'M_MAS_RES_C', optkey:'checkon', parentkey:'M_MAS_RES', rootkey:'M_MAS_MENUS', text:'群文资源待审核'},
            {typekey:'M_MAS_RES_P', optkey:'publish', parentkey:'M_MAS_RES', rootkey:'M_MAS_MENUS', text:'群文资源待发布'}
        ];

        for(var i in massKeys){
            var key = massKeys[i];
            var isPms = isInArray(myData,key.typekey, key.optkey);
            if (!isPms) continue;
            var _cnt = dbData[key.typekey] || 0;
            if (_cnt == 0) continue;

            rest.cnt += Number(_cnt);
            rest.arr.push({
                'title': '您有 <span class="num">' + _cnt + '</span> 条'+key.text,
                'tags': '<a href="javascript:void(0)" onclick=doTurn("'+key.rootkey+'","'+key.parentkey+'","'+key.typekey+'")>处理</a>'
            })
        }

        return rest;
    }

    function doTurn(ppid, pid, id) {
        window.parent.doOpenMenu(ppid, pid, id);
    }
</script>
</body>
</html>