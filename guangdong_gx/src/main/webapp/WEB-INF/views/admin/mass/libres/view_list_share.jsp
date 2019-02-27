<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>表单属性扩展管理</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <%--<link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap-theme.min.css"/>--%>
    <script type="text/javascript" src="${basePath}/static/admin/js/mass-library-custom-form.js"></script>
    <style>
        @font-face{font-family:'Glyphicons Halflings';src:url(${basePath}/static/bootstrap/fonts/glyphicons-halflings-regular.eot); src:url(${basePath}/static/bootstrap/fonts/glyphicons-halflings-regular.eot?#iefix) format('embedded-opentype'), url(${basePath}/static/bootstrap//fonts/glyphicons-halflings-regular.woff2) format('woff2'), url(${basePath}/static/bootstrap/fonts/glyphicons-halflings-regular.woff) format('woff'), url(${basePath}/static/bootstrap/fonts/glyphicons-halflings-regular.ttf) format('truetype'), url(${basePath}/static/bootstrap/fonts/glyphicons-halflings-regular.svg#glyphicons_halflingsregular) format('svg')}
        .tree-srch{ padding: 5px; margin: -10px -10px 0 -10px; border-bottom: 1px solid #ccc; background-color: #F2F2F2; }
        .glyphicon { position: relative; top: 1px; display: inline-block; font-family: 'Glyphicons Halflings'; font-style: normal; font-weight: 400; line-height: 1; -webkit-font-smoothing: antialiased; -moz-osx-font-smoothing: grayscale; box-sizing: border-box; }
        .glyphicon-film:before { content: "\e009"; box-sizing: border-box; }
        .glyphicon-headphones:before { content: "\e035"; box-sizing: border-box; }
        .glyphicon-file:before { content: "\e022"; box-sizing: border-box; }
    </style>
    <script type="text/javascript">

    </script>
</head>
<body class="easyui-layout">
<div data-options="region:'west',split:true,title:'群文资源库',collapsible:false" style="width:300px;padding:10px;">
        <div class="tree-srch">
            <input class="easyui-combobox" style="width: 200px;" name="cultid" id="cultid" data-options="prompt:'请选择文化馆', valueField:'id', textField:'text'"/>
            <input class="easyui-combobox" style="width: 200px;" name="deptid" id="deptid" data-options="prompt:'请选择部门', valueField:'id', textField:'text'"/>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" onclick="reloadTree()">刷  新</a>
        </div>
        <ul class="easyui-tree" id="mass_type_tree"></ul>
    </div>
<div data-options="region:'center',title:'群文资源'">
        <!-- 表格 -->
        <table id="whgdg" class="easyui-datagrid" style="display: none"
               data-options="border:0, fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:true, pagination:true, toolbar:'#whgdg-tb'">
            <thead>
            <tr>
                <th data-options="field:'resname', width:300, formatter:FMTResurl">资源名称</th>
                <th data-options="field:'respicture', width:130, formatter:WhgComm.FMTResourceImg">封面图片</th>
                <th data-options="field:'ressize', width:100, formatter:FMTRessize">大小</th>
                <th data-options="field:'reswidth', width:100, formatter:FMTReswidth">尺寸</th>
                <%--<th data-options="field:'resauthor', width:150">作者</th>--%>
                <%--<th data-options="field:'crtdate', width:150, formatter:WhgComm.FMTDateTime, sortable:true">创建日期</th>--%>
                <th data-options="field:'crtuser',width:120, formatter:WhgComm.FMTUserName">编辑者</th>
                <th data-options="field:'checkor',width:120, formatter:WhgComm.FMTUserName">审核者</th>
                <th data-options="field:'publisher',width:120, formatter:WhgComm.FMTUserName">发布者</th>
                <th data-options="field:'statemdfdate',width:130, formatter:WhgComm.FMTDateTime, sortable:true">操作时间</th>
                <th data-options="field:'state',width:50, formatter:WhgComm.FMTBizState" >状态</th>
                <th data-options="field:'_opt', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
            </tr>
            </thead>
        </table>
        <!-- 表格 END -->

        <!-- 表格操作工具栏 -->
        <div id="whgdg-tb" style="display: none;">
            <div class="whgd-gtb-btn">
                <shiro:hasPermission name="${resourceid}:add"><a href="javascript:void(0)" style="display: none;" class="easyui-linkbutton addEditResources" iconCls="icon-add" onclick="doAdd();">添加资源</a></shiro:hasPermission>
            </div>
            <div class="whgdg-tb-srch">
                <form id="whgdg-tb-srch-form">
                    <input type="hidden" id="libid" name="libid"/>
                    <input type="hidden" id="resourcetype" name="resourcetype"/>
                    <input class="easyui-textbox" style="width: 200px;" name="resname" data-options="prompt:'请输入资源名称', valieType:'length[1,32]'"/>
                    <input class="easyui-combobox" style="width:110px" name="state" id="state" data-options="prompt:'请选择状态',editable:false,valueField:'id',textField:'text'"/>
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
                </form>
            </div>
        </div>
        <!-- 表格操作工具栏-END -->

        <!-- 操作按钮 -->
        <div id="whgdg-opt" style="display: none;">
            <shiro:hasPermission name="${resourceid}:view"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="doSee">查看</a></shiro:hasPermission>
        </div>
        <!-- 操作按钮-END -->
    </div>

<!-- script -->
<script type="text/javascript">
    var pageType = '${param.ptype}';

    /** 按钮是否显示 */
    function validEdit(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        if(pageType == 'e'){
            return row.state == 1;
        }else if(pageType == 'c'){
            return row.state == 9;
        }else if(pageType == 'p'){
            return row.state == 2 || row.state == 4 || row.state == 6;
        }
    }
    function validCheckgo(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 1;
    }
    function validCheckon(idx) {
        var row = $('#whgdg').datagrid('getRows')[idx];
        return row.state == 9;
    }
    function validCheckoff(idx) {
        var row = $('#whgdg').datagrid('getRows')[idx];
        return row.state == 9;
    }
    function validPublish(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        return curRow.state == 2 || curRow.state == 4;
    }
    function validPublishoff(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        return curRow.state == 6;
    }
    function validRecommend(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        return curRow.state == 6 && !(typeof(curRow.recommend) != "undefined" &&  curRow.recommend == 1);
    }
    function validRecommendoff(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        return curRow.state == 6 && typeof(curRow.recommend) != "undefined" &&  curRow.recommend == 1;
    }
    function validRecovery(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return (pageType == 'c' && row.state == 9) || (pageType == 'p' && (row.state == 2 || row.state == 4));
    }
    function validUndel(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return pageType == 'r' && row.delstate == 1;
    }
    function validDel(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return (pageType == 'e' && row.state == 1) || (pageType == 'r' && row.delstate == 1);
    }

    /** 修改状态 */
    function doCheckgo(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        var libid = curRow.libid;

        //获取必填字段
        var requiredFields = [];
        $.ajax({
            url: basePath+'/admin/mass/libres/queryLibFormRequiredField',
            type: 'POST',
            async: false,
            cache: false,
            data: {libid:libid, resid:curRow.id},
            success: function (data) {
                if(!$.isPlainObject(data)){
                    data = $.parseJSON(data);
                }
                requiredFields = data.rows;
            }
        });
        if(requiredFields && $.isArray(requiredFields) && requiredFields.length > 0){
            for(var i=0; i<requiredFields.length; i++){
                var field = requiredFields[i];
                var fieldcode = field.fieldcode;
                var fieldname = field.fieldname;
                if(fieldname){
                    $.messager.alert('警告信息', '请编辑好资源的“'+fieldname+'”再进行此操作！', 'warning');
                    return;
                }
            }
        }


        //视频必须上传封面图片
        var resourcetype = curRow.resourcetype;
        if(resourcetype == 'video'){
            if(typeof(curRow.respicture) == 'undefined' || curRow.respicture.length < 10){
                //视频封面图片
                $.messager.alert('警告信息', '视频资源必须上传封面才允许提交审核，请编辑好视频封面图片！', 'warning');
                return;
            }
        }

        WhgComm.confirm("确认信息", "确定要审核选中的项吗？", function(){
            doUpdateState(libid, curRow.id, "9");
        });
    }
    function doCheckon(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        var libid = curRow.libid;
        WhgComm.confirm("确认信息", "确定要审核选中的项吗？", function(){
            doUpdateState(libid, curRow.id, "2");
        });
    }
    function doCheckoff(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        var libid = curRow.libid;
        WhgComm.confirm("确认信息", "确定要打回选中的项吗？", function(){
            doUpdateState(libid, curRow.id, "1");
        });
    }
    function doPublish(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        var libid = curRow.libid;
        WhgComm.confirm("确认信息", "确定要发布选中的项吗？", function(){
            doUpdateState(libid, curRow.id, "6");
        });
    }
    function doPublishoff(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        var libid = curRow.libid;
        WhgComm.confirm("确认信息", "确定要取消发布选中的项吗？", function(r){
            doUpdateState(libid, curRow.id, "4");
        })
    }
    function doRecommend(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        var libid = curRow.libid;
        doUpdateRecommend(libid, curRow.id, "1");
    }
    function doRecommendoff(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        var libid = curRow.libid;
        doUpdateRecommend(libid, curRow.id, "0");
    }
    function doRecovery(idx){
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        var libid = curRow.libid;
        var resid = curRow.id;
        WhgComm.confirm("确认信息", '确定要回收选中的数据吗？',function () {
            $.ajax({
                type: "POST",
                cache: false,
                url: getFullUrl('/admin/mass/libres/updateDelstate'),
                data: {"libid": libid, "id": resid, "delstate": "1"},
                success: function (Json) {
                    if (Json && Json.success == '1') {
                        $('#whgdg').datagrid('reload');
                    } else {
                        $.messager.alert('提示', '操作失败:' + Json.errormsg + '!', 'error');
                    }
                }
            });
        });
    }
    function doUndel(idx){
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        var libid = curRow.libid;
        var resid = curRow.id;
        WhgComm.confirm("确认信息", '确定要撤销回收选中的数据吗？', function() {
            $.ajax({
                type: "POST",
                cache: false,
                url: getFullUrl('/admin/mass/libres/updateDelstate'),
                data: {"libid": libid, "id": resid, "delstate": "0"},
                success: function (Json) {
                    if (Json && Json.success == '1') {
                        $('#whgdg').datagrid('reload');
                    } else {
                        $.messager.alert('提示', '操作失败:' + Json.errormsg + '!', 'error');
                    }
                }
            });
        });
    }

    /** 浏览资源 */
    function viewResource(restype, resurl, picture) {
        if(restype == 'video'){
            var url = '${basePath}/admin/mass/libres/view/see?resurl='+resurl+'&respicture='+encodeURIComponent(picture)+'&t='+new Date().getTime();
            WhgComm.openDialog4size('资源浏览', url, 660, 500, { hideSubmitBtn: true, cancelBtnText: '关  闭', closeFun: function () {
                document.getElementById('iframe_WhgDialog4Edit').contentWindow.$("#jquery_jplayer_1").jPlayer('destroy');
            } });
        }else if(restype == 'audio'){
            var url = '${basePath}/admin/mass/libres/view/see_audio?resurl='+encodeURIComponent(resurl)+'&respicture='+encodeURIComponent(picture)+'&t='+new Date().getTime();
            WhgComm.openDialog4size('资源浏览', url, 520, 190, { hideSubmitBtn: true, cancelBtnText: '关  闭', closeFun: function () {
                document.getElementById('iframe_WhgDialog4Edit').contentWindow.$("#jquery_jplayer_1").jPlayer('destroy');
            } });
        }else{//文件 //直接下载
            var form = $('<form style="display: none;" target="" method="get" action="'+resurl+'">');
            $("body").append(form);
            form.submit();
        }
    }

    /** 资源名称 */
    function FMTResurl(val, rowData, rowIdx) {
        var restype = rowData.resourcetype;
        var resurl = rowData.resurl;
        if('img' == restype){
            return val;
        }else if('video' == restype){
            var picture = rowData.respicture || '';
            return '<span onclick="viewResource(\'video\', \''+resurl+'\', \''+picture+'\')"><span class="glyphicon glyphicon-film"></span>&nbsp;'+val+'</span>';
        }else if('audio' == restype){
            var picture = rowData.respicture || '';
            return '<span onclick="viewResource(\'audio\', \''+resurl+'\', \''+picture+'\')"><span class="glyphicon glyphicon-headphones"></span>&nbsp;'+val+'</span>';
        }else{
            return '<span onclick="viewResource(\'file\', \''+resurl+'\')"><span class="glyphicon glyphicon-file"></span>&nbsp;'+val+'</span>';
        }
    }

    /** 格式化大小 */
    function FMTRessize(val, rowData, rowIdx) {
        var number = new Number(val);
        if(number / 1024 < 1024){
            return (number / 1024).toFixed(2)+"KB";
        }else if(number / 1024 / 1024 < 1024){
            return (number / 1024 / 1024).toFixed(2)+"MB";
        }else{
            return (number / 1024 / 1024 / 1024).toFixed(2)+"GB";
        }
        return '';
    }

    /** 格式化尺寸 */
    function FMTReswidth(val, rowData, rowIdx) {
        if(val && rowData.resheight) return val + "*" + rowData.resheight;
        return '';
    }

    /** 添加 */
    function doAdd() {
        var libid = $('#libid').val();
        if(libid == ''){
            $.messager.alert('提示', '请选择群文资源库或者先创建群文资源库！', 'warning');
        }else {
            var disabledLib = libDisabled(libid);
            if(disabledLib) {
                $.messager.alert('警告', '群文资源库不可用，请刷新并选择其它群文资源库！', 'warning');
                return false;
            }
            WhgComm.openDialog4size('添加资源', '${basePath}/admin/mass/libres/view/add?libid='+libid+'&t='+new Date().getTime(), 800, 500, {
                hideSubmitBtn: true,
                cancelBtnText: '关  闭',
                closeFun: function () {
                    $('#whgdg').datagrid('reload');
                }
            });
        }
    }

    /** 分享资源库 */
    function doShareLibrary() {
        var libid = $('#libid').val();
        if(libid == ''){
            $.messager.alert('提示', '请选择群文资源库或者先创建群文资源库！', 'warning');
        }else {
            var disabledLib = libDisabled(libid);
            if(disabledLib) {
                $.messager.alert('警告', '群文资源库不可用，请刷新并选择其它群文资源库！', 'warning');
                return false;
            }
            WhgComm.openDialog4size('选择授权文化馆', '${basePath}/admin/mass/libres/view/share?libid='+libid+'&t='+new Date().getTime(), 800, 600, {
                cancelBtnText: '关  闭',
                submitBtnText: '确  认',
                closeFun: function () {
                    window.location.reload();
                }
            });
        }
    }

    /** 验证资源是否存在 */
    function libDisabled(libid){
        var exist = true;
        $.ajax({
            url: basePath+'/admin/mass/libres/libDisabled',
            type: 'POST',
            async: false,
            data: {resName:'', libid:libid},
            success: function (data) {
                if(!$.isPlainObject(data)){
                    data = $.parseJSON(data);
                }
                var existStr = data.data;
                exist = existStr == 'yes' ? true : false;
            }
        });
        return exist;
    }

    /**
     * 编辑
     * @param idx 行下标
     */
    function doEdit(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        var libid = curRow.libid;
        WhgComm.editDialog('${basePath}/admin/mass/libres/view/edit?libid='+libid+'&id='+curRow.id);
    }

    /**
     * 查看
     * @param idx 行下标
     */
    function doSee(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        var libid = curRow.libid;
        WhgComm.editDialog('${basePath}/admin/mass/libres/view/edit?libid='+libid+'&id='+curRow.id+"&onlyshow=1");
    }

    /**
     * 删除
     * @param idx
     */
    function doDel(idx,force) {
        force = force || '0';//强制删除
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        var libid = curRow.libid;

        if(force == '1'){
            doAjax(force);
        }else{
            $.messager.confirm('提示', '您确定要删除此记录吗？', function(r){ if (r){doAjax(force);} });
        }
        function doAjax(force){
            $.ajax({
                type: "POST",
                cache: false,
                url: getFullUrl('/admin/mass/libres/del'),
                data: {libid:libid, id : curRow.id, force: force},
                success: function(Json){
                    if(Json && Json.success == '1'){
                        $('#whgdg').datagrid('reload');
                    } else {
                        if(force == '0'){
                            $.messager.confirm('提示', '删除失败:'+Json.errormsg+'！，您要强制删除此记录吗？', function(r){ if(r){ doDel(idx,"1"); } });
                        }else{
                            $.messager.alert('提示', '删除失败:'+Json.errormsg+'！', 'error');
                        }
                    }
                }
            });
        }
    }

    /** 修改资源业务状态 */
    function doUpdateState(libid, id, state) {
        $.ajax({
            type: "POST",
            cache: false,
            url: getFullUrl('/admin/mass/libres/updateState'),
            data: {"libid":libid, "id":id, "state": state},
            success: function(Json){
                if(Json && Json.success == '1'){
                    $('#whgdg').datagrid('reload');
                } else {
                    $.messager.alert('提示', '操作失败:'+Json.errormsg+'!', 'error');
                }
            }
        });
    }

    /** 修改资源推荐状态 */
    function doUpdateRecommend(libid, id, recommend) {
        $.ajax({
            type: "POST",
            cache: false,
            url: getFullUrl('/admin/mass/libres/updateRecommend'),
            data: {"libid":libid, "id":id, "recommend": recommend},
            success: function(Json){
                if(Json && Json.success == '1'){
                    $('#whgdg').datagrid('reload');
                } else {
                    $.messager.alert('提示', '操作失败:'+Json.errormsg+'!', 'error');
                }
            }
        });
    }

    var libid='';
    //初始tree
    function initMassTypeTree(){
        $('#mass_type_tree').tree({
            url: '${basePath}/admin/mass/library/findShareLibrary',
            animate: true,
            lines: true,
            onLoadSuccess: function (node, data) {
                var t = $(this);
                if(data){
                    $(data).each(function(index, d){
                        if(this.state == 'closed'){
                            t.tree('expandAll');
                        }
                    });
                }
            },
            onBeforeExpand: function (node) {
                var cultid = $('#cultid').combobox("getValue");
                var deptid = $('#deptid').combobox("getValue");
                //$('#mass_type_tree').tree('options').url = basePath + "/admin/mass/library/srchlibs?type="+node.id+"&cultid="+cultid+"&deptid="+deptid;
                $('#mass_type_tree').tree('options').queryParams = {cultid:cultid, deptid:deptid};
            },
            onBeforeSelect: function (node) {
                return /\d+/.test(node.id);
            },
            onSelect: function(node){
                $('#libid').val(node.id);
                var cultid = $('#cultid').combobox("getValue");
                var deptid = $('#deptid').combobox("getValue");
                $.ajax({
                    url: '${basePath}/admin/mass/library/libIsShare',
                    data: {id: node.id, cultid:cultid, deptid:deptid},
                    type: 'post',
                    dataType: 'json',
                    success: function(data){
                        if(data.success == '1'){
                            if(data.data > 0){
                                $(".addEditResources").show();
                            }else{
                                $(".addEditResources").hide();
                            }
                        }
                    },
                    error: function(){

                    }
                });
                $.getJSON("/admin/mass/library/findLibrary?id="+node.id, function (data) {
                    var restype = data.resourcetype;
                    $('#resourcetype').val(restype);
                    if('img' == restype){
                        $('#whgdg').datagrid('showColumn', 'respicture');
                        $('#whgdg').datagrid('showColumn', 'reswidth');
                    }else if('video' == restype){
                        $('#whgdg').datagrid('showColumn', 'respicture');
                        $('#whgdg').datagrid('hideColumn', 'reswidth');
                    }else{
                        $('#whgdg').datagrid('hideColumn', 'respicture');
                        $('#whgdg').datagrid('hideColumn', 'reswidth');
                    }
                    WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/mass/libres/srchList5p?ptype=${param.ptype}&delstate=${param.delstate}');
                });
            }
        });
    }

    //刷新Tree
    function reloadTree(){
        window.__initPage__ = undefined;
        $('#mass_type_tree').tree('reload');
        //清除表格查询条件
        $('#libid').val('');
        $('#resourcetype').val('');
        var libid='';
        WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/mass/libres/srchList5p?ptype=${param.ptype}&delstate=${param.delstate}&libid='+libid);
    }

    //初始查询条件-业务状态
    function init_condition_state() {
        var pageType = '${param.ptype}';
        var stateArr = WhgComm.getBizState().slice();
        if('e' != pageType){
            stateArr.splice(0,1);
        }
        if('r' != pageType){
            $('#state').combobox('loadData', stateArr);
            if('e' == pageType){
                $('#state').combobox('setValue', 1);
            }else if('c' == pageType){
                $('#state').combobox('setValue', 9);
            }else if('p' == pageType){
                $('#state').combobox('setValue', 2);
            }
        }else{
            $('#state').combobox('destroy');
        }
    }

    $(function () {
        //初始查询条件-业务状态
        init_condition_state();

        //初始文化馆和权限
        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'${library.cultid}',allcult:true,
            deptEid:'deptid', deptValue:'${library.deptid}',alldept:true
        });

        //初始树
        initMassTypeTree();

        //查询
        WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/mass/libres/srchList5p?ptype=${param.ptype}');

    });
</script>
<!-- script END -->
</body>
</html>