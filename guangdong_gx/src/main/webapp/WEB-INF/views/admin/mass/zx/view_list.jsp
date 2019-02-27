<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>

    <title>资讯管理</title>

    <script src="${basePath}/static/admin/js/whgmodule-gridopts.js"></script>

    <c:choose>
        <c:when test="${mtype eq '54'}">
            <c:set var="modelType" value="专题届次"></c:set>
        </c:when>
        <c:when test="${mtype eq '52'}">
            <c:set var="modelType" value="艺术人才"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="modelType" value=""></c:set>
        </c:otherwise>
    </c:choose>
</head>
<body>
<div id="pagelayout" class="easyui-layout" data-options="fit:true">

    <div data-options="region:'center',title:'${modelType}资讯列表'">
        <table id="whgdg" class="easyui-datagrid" style="display: none;"
               toolbar="#tb" pagination=true pageSize=30 pageList="[10,20,30,50,100,200]" loadMsg="数据加载中，请稍候。。。"
               data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:true,
           loader:paramLoader">
            <thead>
            <tr>
                <th data-options="sortable:false, field:'all', checkbox:true ">全选</th>
                <th data-options="sortable:false, width:100, field:'clnftltle' ">标题</th>
                <th data-options="sortable:false, width:40, field:'clnfauthor' ">作者</th>
                <th data-options="sortable:false, width:30, field:'clnfid', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
            </tr>
            </thead>
        </table>

        <div id="tb" style="display: none;">
            <div class="whgd-gtb-btn">
                <a class="easyui-linkbutton" iconCls="icon-back" onclick="WhgComm.editDialogClose()">返回</a>
                <a class="easyui-linkbutton" iconCls="icon-remove" onclick="whgListTool.removeZx()">批量移除</a>
            </div>

            <div class="whgdg-tb-srch">
                <%--<select class="easyui-combobox" name="cultid" id="cultid" prompt="请选择文化馆" panelHeight="auto" limitToList="true"
                        data-options="editable:true, width:180, valueField:'id', textField:'text', data:WhgComm.getMgrCults()&lt;%&ndash;, value:WhgComm.getMgrCults()[0].id&ndash;%&gt;"></select>
--%>
                    <input class="easyui-combobox" name="cultid" id="cultid" prompt="请选择文化馆"
                           data-options="editable:true,width:180"/>
                    <input class="easyui-combobox" name="deptid" id="deptid" prompt="请选择部门"
                           data-options="editable:true,width:120"/>

                <input class="easyui-textbox" name="clnftltle" prompt="请输入标题" data-options="width:120">

                <a class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#tb');">查 询</a>
            </div>
        </div>

        <div id="whgdg-opt" style="display: none;">
            <a plain="true" method="whgListTool.show">查看</a>
        </div>
    </div>


    <div data-options="region:'east', title:'群文资讯列表',split:true, width:'40%'">
        <table id="whgdg2" class="easyui-datagrid" style="display: none;"
               toolbar="#tb2" pagination=true pageSize=30 pageList="[10,20,30,50,100,200]" loadMsg="数据加载中，请稍候。。。"
               data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:true,
           loader:paramLoader2">
            <thead>
            <tr>
                <th data-options="sortable:false, field:'all', checkbox:true ">全选</th>
                <th data-options="sortable:false, width:100, field:'clnftltle' ">标题</th>
                <th data-options="sortable:false, width:40, field:'clnfauthor' ">作者</th>
                <th data-options="sortable:false, width:30, field:'clnfid', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt2'">操作</th>
            </tr>
            </thead>
        </table>

        <div id="tb2" style="display: none;">
            <div class="whgd-gtb-btn">
                <a class="easyui-linkbutton" iconCls="icon-add" onclick="whgListTool2.addZx()">批量添加到${modelType}</a>
            </div>

            <div class="whgdg-tb-srch">
                <%--<select class="easyui-combobox" name="cultid" prompt="请选择文化馆" panelHeight="auto" limitToList="true"
                        data-options="editable:false, width:180, valueField:'id', textField:'text', data:WhgComm.getMgrCults(), value:WhgComm.getMgrCults()[0].id"></select>
--%>
                    <input class="easyui-combobox" name="cultid" id="cultid2" prompt="请选择文化馆"
                           data-options="editable:true,width:180"/>
                    <input class="easyui-combobox" name="deptid" id="deptid2" prompt="请选择部门"
                           data-options="editable:true,width:120"/>

                <input class="easyui-textbox" name="clnftltle" prompt="请输入标题" data-options="width:120">

                <a class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg2', '#tb2');">查 询</a>
            </div>
        </div>

        <div id="whgdg-opt2" style="display: none;">
            <a plain="true" method="whgListTool2.show">查看</a>
        </div>
    </div>


    <div data-options="region:'south',title:'资讯详情',split:true,collapsed:true, height:'35%',border:false" style="overflow: hidden">
        <iframe id="iframe_show" width="100%" height="100%" frameborder="0"></iframe>
    </div>
</div>


<script>
    $(function(){
        WhgComm.initPMS({
            basePath: '${basePath}',
            cultEid:'cultid', cultValue:'', allcult:true,
            deptEid:'deptid', deptValue:'', alldept:true
        });
        WhgComm.initPMS({
            basePath: '${basePath}',
            cultEid:'cultid2', cultValue:'', allcult:true,
            deptEid:'deptid2', deptValue:'', alldept:true
        });

        //$("#tb a[iconCls='icon-search']").click();
        //$("#tb2 a[iconCls='icon-search']").click();

        $("#iframe_show").load(function(){
            //var _hh = $(this).contents().find(".easyui-textbox:first").val();
            //$(this).contents().find("form>h2").text(_hh);
            $(this).contents().find("form>h2").hide();
            $(this).contents().find(".whgff-but").hide();

            //$("#iframe_show").show();
        });
    });

    var whgListTool = new Gridopts();

    var whgListTool2 = new Gridopts({grid:"#whgdg2"});

    var paramLoader = function (param, success, error) {
        /*if (!param.cultid || param.cultid == ''){
            return false;
        }*/
        param.mid = '${mid}';
        param.mtype = '${mtype}';
        $.ajax({
            url: '${basePath}/admin/mass/ref/getrefzxlist',
            type: 'post',
            data : param,
            dataType: 'json',
            success: success,
            error: error
        })
    };
    var paramLoader2 = function (param, success, error) {
        /*if (!param.cultid || param.cultid == ''){
            return false;
        }*/
        param.mid = '${mid}';
        param.mtype = '${mtype}';
        $.ajax({
            url: '${basePath}/admin/mass/ref/getzxlist',
            type: 'post',
            data : param,
            dataType: 'json',
            success: success,
            error: error
        })
    };


    Gridopts.prototype.__ajaxSuccess = function(data){
        $(whgListTool.grid).datagrid('reload');
        $(whgListTool2.grid).datagrid('reload');
        if (!data.success || data.success != "1"){
            $.messager.alert("错误", data.errormsg||'操作失败', 'error');
        }
        $.messager.progress('close');
    };

    Gridopts.prototype.addZx = function(){
        var selecteds = this.__getGridSelectRows();
        var clnfidArr = [];
        for(var i in selecteds) {
            clnfidArr.push(selecteds[i].clnfid);
        }

        if (clnfidArr.length == 0) {
            $.messager.alert("提示信息", "请选择要操作的项目", 'error');
            return;
        }

        this.__ajaxTempSend("/addZx", {clnfids:clnfidArr.join(","), mid:'${mid}', mtype:'${mtype}' });
    };
    Gridopts.prototype.removeZx = function(){
        var selecteds = this.__getGridSelectRows();
        var clnfidArr = [];
        for(var i in selecteds) {
            clnfidArr.push(selecteds[i].clnfid);
        }

        if (clnfidArr.length == 0) {
            $.messager.alert("提示信息", "请选择要操作的项目", 'error');
            return;
        }

        this.__ajaxTempSend("/removeZx", {clnfids:clnfidArr.join(","), mid:'${mid}', mtype:'${mtype}' });
    };

    Gridopts.prototype.show = function(idx){
        var row = this.__getGridRow(idx);

        var south = $("#pagelayout").layout("panel", "south");
        var collapsed = south.panel("options").collapsed;
        if (collapsed) {
            $("#pagelayout").layout("expand", "south");
        }

        //$("#iframe_show").hide();
        var oldsrc = $("#iframe_show").attr("src");
        var nowsrc = '${basePath}/admin/information/view/add?offH2=1&targetShow=1&id='+row.clnfid;
        if (oldsrc && oldsrc == nowsrc) {
            //$("#iframe_show").show();
            return;
        }
        $("#iframe_show").attr("src", nowsrc);
    }

</script>

</body>
</html>