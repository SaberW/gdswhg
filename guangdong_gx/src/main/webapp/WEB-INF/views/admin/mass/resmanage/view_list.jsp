<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>
    <title>群文资源管理列表</title>
    <script src="${basePath}/static/admin/js/whgmodule-gridopts.js"></script>
    <c:choose>
        <c:when test="${reftype eq 40}">
            <c:set var="pageTitle" value="展品"></c:set>
            <c:set var="pageTitle2" value="展品管理"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="资源"></c:set>
            <c:set var="pageTitle2" value="群文资源"></c:set>
        </c:otherwise>
    </c:choose>
</head>
<body>

<table id="whgdg" class="easyui-datagrid" title="${pageTitle2}" style="display: none;"
       toolbar="#tb" pagination=true pageSize=30 pageList="[10,20,30,50,100,200]" loadMsg="数据加载中，请稍候。。。"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:true, checkOnSelect:true, selectOnCheck:true,
           loader:whgListTool.paramLoader">
    <thead frozen="true">
        <tr>
            <th data-options="field:'id', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
        </tr>
    </thead>

    <thead>
    <tr>
        <th data-options="field:'enttype', width:80, formatter:function(val) {return ['图片','视频','音频','文档'][parseInt(val)-1]}">${pageTitle}类型</th>
        <th data-options="field:'name', width:200, ">${pageTitle}名</th>
        <th data-options="field:'crtdate', width:130, formatter:WhgComm.FMTDateTime ">添加时间</th>
        <th data-options="field:'imgview', formatter:function(v,r,i){
            if (r.enttype == 3 || r.enttype == 4) return r.enturl;
            var imgurl = '';
            if (r.enttype == 1){
                imgurl = r.enturl;
            }else if (r.enttype == 2){
                imgurl = r.deourl;
            }
            if (!imgurl || imgurl == '') return '';
            if (!/^http/.test(imgurl)){
                imgurl = WhgComm.getImgServerAddr()+imgurl;
            }
            return '<img src='+imgurl+'  height=45>'
        }, styler:function(v, r, i){
            return 'min-width:100px;'
        } ">${pageTitle}信息
        </th>
        <%--<th data-options="field:'enturl' ">资源地址(图片)</th>--%>
       <%-- <th data-options="field:'enttimes' ">视频/音频时长</th>--%>
        <%--<th data-options="field:'deourl',  formatter:WhgComm.FMTImg">视频封面</th>--%>
        <%--<th data-options="field:'id', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>--%>
    </tr>
    </thead>
</table>

<div id="tb" style="display: none;">
    <div class="whgd-gtb-btn">
        <a class="easyui-linkbutton" iconCls="icon-back" onclick="WhgComm.editDialogClose()">返回</a>
        <c:if test="${empty lookOpt}">
        <a class="easyui-linkbutton" iconCls="icon-add" onclick="whgListTool.add()">添 加</a>
        </c:if>
    </div>

    <div class="whgdg-tb-srch">
        <input class="easyui-textbox" name="name" prompt="${pageTitle}名称" data-options="width:120">
        <select class="easyui-combobox" name="enttype" prompt="${pageTitle}类型" panelHeight="auto"
                data-options="width:120, value:''">
            <option value="1">图片</option>
            <option value="2">视频</option>
            <option value="3">音频</option>
            <option value="4">文件</option>
        </select>

        <a class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#tb');">查 询</a>
    </div>
</div>

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <a plain="true" method="whgListTool.view">查看</a>
    <c:if test="${empty lookOpt}">
    <a plain="true" method="whgListTool.edit">编辑</a>
    <a plain="true" method="whgListTool.del">删除</a>
    </c:if>
    <c:if test="${reftype eq '54' or reftype eq '52'}">
    <a plain="true" method="whgListTool.isaward" validKey="extisaward" validVal="0">获奖</a>
    <a plain="true" method="whgListTool.unisaward" validKey="extisaward" validVal="1">非获奖</a>
    </c:if>
</div>
<!-- 操作按钮-END -->



<script>

    $(function(){

    });

    var whgListTool = new Gridopts();

    Gridopts.prototype.paramLoader = function (param, success, error) {
        param.reftype = '${reftype}';
        param.refid = '${refid}';

        if (!param.reftype ||!param.refid ||param.reftype=='' || param.refid=='') {
            return false;
        }
        $.ajax({
            url: '${basePath}/admin/mass/resmanage/srchList4p',
            type: 'post',
            data : param,
            dataType: 'json',
            success: success,
            error: error
        })
    };

    Gridopts.prototype.add = function(){
        WhgComm.editDialog( this.modeUrl+'/view/add?refid=${refid}&reftype=${reftype}&enttypes=${enttypes}');
    };

    Gridopts.prototype.view = function(idx){
        var row = this.__getGridRow(idx);
        WhgComm.editDialog( this.modeUrl+'/view/show?refid=${refid}&reftype=${reftype}&enttypes=${enttypes}&id='+row.id );
    };
    Gridopts.prototype.edit = function(idx){
        var row = this.__getGridRow(idx);
        WhgComm.editDialog( this.modeUrl+'/view/edit?refid=${refid}&reftype=${reftype}&enttypes=${enttypes}&id='+row.id );
    };
    Gridopts.prototype.del = function(idx){
        var row = this.__getGridRow(idx);
        var mmx = this;
        $.messager.confirm("确认信息", "确定要删除选中的项吗？", function(r){
            if (r){
                mmx.__ajaxTempSend("/del", {id: row.id});
            }
        })
    };
    Gridopts.prototype.isaward = function(idx){
        var row = this.__getGridRow(idx);
        var mmx = this;
        $.messager.confirm("确认信息", "确定设置为获奖作品？", function(r){
            if (r){
                mmx.__ajaxTempSend("/setisaward", {id: row.id, extisaward:1});
            }
        });
    };
    Gridopts.prototype.unisaward = function(idx){
        var row = this.__getGridRow(idx);
        var mmx = this;
        $.messager.confirm("确认信息", "确定设置为非获奖作品？", function(r){
            if (r){
                mmx.__ajaxTempSend("/setisaward", {id: row.id, extisaward:0});
            }
        });
    };

</script>

</body>
</html>
