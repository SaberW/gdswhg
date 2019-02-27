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
        tr.datagrid-row-selected{
            background-color: #0081c2;
        }
    </style>
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
                <th data-options="field:'ck',checkbox:true"></th>
                <th data-options="field:'resname', width:300, formatter:FMTResurl">资源名称</th>
                <th data-options="field:'respicture', width:130, formatter:WhgComm.FMTResourceImg">封面图片</th>
                <th data-options="field:'ressize', width:100, formatter:FMTRessize">大小</th>
                <th data-options="field:'reswidth', width:100, formatter:FMTReswidth">尺寸</th>
                <%--<th data-options="field:'resauthor', width:150">作者</th>--%>
                <th data-options="field:'crtdate', width:150, formatter:WhgComm.FMTDateTime, sortable:true">创建日期</th>
                <th data-options="field:'_opt', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
            </tr>
            </thead>
        </table>
        <!-- 表格 END -->

        <!-- 表格操作工具栏 -->
        <div id="whgdg-tb" style="display: none;">
            <div class="whgd-gtb-btn">
                <shiro:hasPermission name="${resourceid}:add"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="doAdd();">添加资源</a></shiro:hasPermission>
            </div>
            <div class="whgdg-tb-srch">
                <form id="whgdg-tb-srch-form">
                    <input type="hidden" id="libid" name="libid"/>
                    <input type="hidden" id="resourcetype" name="resourcetype"/>
                    <input class="easyui-textbox" style="width: 200px;" name="resname" data-options="prompt:'请输入资源名称', valieType:'length[1,32]'"/>
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
                </form>
            </div>
        </div>
        <!-- 表格操作工具栏-END -->

        <!-- 操作按钮 -->
        <div id="whgdg-opt" style="display: none;">
            <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validVideo" method="doLook">查看</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validFile" method="doLook">下载</a>
        </div>
        <!-- 操作按钮-END -->
    </div>

<!-- script -->
<script type="text/javascript">
    /**  */
    function validFile(idx){
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        var resurl = curRow.resurl;
        var restype = $('#resourcetype').val();
        return 'video' != restype && 'audio' != restype && 'img' != restype;
    }

    function validVideo(idx){
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        var resurl = curRow.resurl;
        var restype = $('#resourcetype').val();
        return 'video' == restype || 'audio' == restype;
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
        var restype = $('#resourcetype').val();
        var resurl = rowData.resurl;
        if('img' == restype){
            return val;
        }else if('video' == restype){
            var picture = rowData.respicture || '';
            return '<span><span class="glyphicon glyphicon-film"></span>&nbsp;'+val+'</span>';
            //return '<span onclick="viewResource(\'video\', \''+resurl+'\', \''+picture+'\')"><span class="glyphicon glyphicon-film"></span>&nbsp;'+val+'</span>';
        }else if('audio' == restype){
            var picture = rowData.respicture || '';
            return '<span><span class="glyphicon glyphicon-headphones"></span>&nbsp;'+val+'</span>';
            //return '<span onclick="viewResource(\'audio\', \''+resurl+'\', \''+picture+'\')"><span class="glyphicon glyphicon-headphones"></span>&nbsp;'+val+'</span>';
        }else{
            return '<span><span class="glyphicon glyphicon-file"></span>&nbsp;'+val+'</span>';
            //return '<span onclick="viewResource(\'file\', \''+resurl+'\')"><span class="glyphicon glyphicon-file"></span>&nbsp;'+val+'</span>';
        }
    }

    /**查看视频音频和下载文件*/
    function doLook(idx){
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        var resurl = curRow.resurl;
        var restype = $('#resourcetype').val();
        if('img' == restype){
            return val;
        }else if('video' == restype){
            var picture = curRow.respicture || '';
            viewResource('video', resurl, picture);
        }else if('audio' == restype){
            var picture = curRow.respicture || '';
            viewResource('audio', resurl, picture);
        }else{
            viewResource('file', resurl);
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
        return val + "*" + rowData.resheight;
    }

    /** 添加 */
    function doAdd() {
        var libid = $('#libid').val();
        if(libid == ''){
            $.messager.alert('提示', '请选择群文资源库或者先创建群文资源库！', 'warning');
        }else {
            WhgComm.openDialog4size('添加资源', '${basePath}/admin/mass/libres/view/add?libid='+libid+'&t='+new Date().getTime(), 800, 500, {
                hideSubmitBtn: true,
                cancelBtnText: '关  闭',
                closeFun: function () {
                    $('#whgdg').datagrid('reload');
                }
            });
        }
    }

    /**
     * 编辑
     * @param idx 行下标
     */
    function doEdit(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        var libid = $('#libid').val();
        WhgComm.editDialog('${basePath}/admin/mass/libres/view/edit?libid='+libid+'&id='+curRow.id);
    }

    /**
     * 查看
     * @param idx 行下标
     */
    function doSee(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        var libid = $('#libid').val();
        WhgComm.editDialog('${basePath}/admin/mass/libres/view/edit?libid='+libid+'&id='+curRow.id+"&onlyshow=1");
    }

    /**
     * 删除
     * @param idx
     */
    function doDel(idx,force) {
        force = force || '0';//强制删除
        var libid = $('#libid').val();
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        $.messager.confirm('提示', '您确定要删除此记录吗？', function(r){
            if (r){
                $.ajax({
                    type: "POST",
                    cache: false,
                    url: getFullUrl('/admin/mass/libres/del'),
                    data: {libid:libid, id : curRow.id, force: force},
                    success: function(Json){
                        if(Json && Json.success == '1'){
                            $('#whgdg').datagrid('reload');
                        } else {
                            $.messager.confirm('提示', '删除失败，资源正在被其它对象引用，您要强制删除此记录吗？', function(r){
                                if(r){ doDel(idx,"1"); }
                            });
                        }
                    }
                });
            }
        });
    }

    //初始tree
    function initMassTypeTree(){
        $('#mass_type_tree').tree({
            //data: getTreeData(),
            url: '${basePath}/admin/mass/library/srchlibs?limittype=${param.restype}',
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
                    WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/mass/libres/srchList4p?state=${param.state}');
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
        $('#whgdg').datagrid('loadData', []);
        $('#whgdg').datagrid({url:'', queryParams:{}});
    }

    //tree Data一级数据
    function getTreeData() {
        //限制资源类型
        var limit_restype = '${param.restype}'; //img|video|audio|file

        //通过枚举定义资源类型
        var _treeData = [];
        var treeData = WhgComm.getResourceTypeData();
        for(var i=0; i<treeData.length; i++){
            var rowData = treeData[i];
            rowData['state'] = 'closed';
            if(limit_restype == '' || limit_restype == rowData.id) {
                _treeData.push(rowData);
            }
        }
        return _treeData;
    }

    $(function () {
        //初始文化馆和权限
        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'${param.cultid}',allcult:true,
            deptEid:'deptid', deptValue:'${param.deptid}',alldept:true
        });
        $('#cultid').combobox('clear');
        if('${param.cultid}' != ''){
            $('#cultid').combobox("readonly", true);
        }
        if('${param.deptid}' != ''){
            $('#deptid').combobox("readonly", true);
        }

        //初始树
        initMassTypeTree();

        //给弹出层的选择按钮注册事件
        if(window.parent && typeof(window.parent.WhgMassResource) != 'undefined'){
            window.parent.WhgMassResource.getSubmitBtn().on('click', function () {
                var curRow = $('#whgdg').datagrid('getSelected');
                if(curRow){
                    var libid = $('#libid').val();
                    var resourcetype = $('#resourcetype').val();
                    var option = {"libid":libid, "resid":curRow.id, "restype":resourcetype, "resname":curRow.resname, "respicture":curRow.respicture, "resurl":curRow.resurl};
                    window.parent.WhgMassResource.doSubmit(option);
                }else{
                    $.messager.alert('提示', '您还没有从“群文资源”列表中单击选择资源!', 'error');
                }
            });
        }
    });
</script>
<!-- script END -->
</body>
</html>