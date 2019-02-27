<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>
    <title>阿里云短信组管理</title>

    <script src="${basePath}/static/admin/js/whgmodule-gridopts.js"></script>
</head>
<body>
<table id="whgdg" class="easyui-datagrid" title="短信组管理" style="display: none;" toolbar="#tb"
       data-options="fit:true, striped:true, rownumbers:true, pagination:true, fitColumns:true, singleSelect:true, checkOnSelect:true">
    <thead>
    <tr>
        <th data-options="sortable:false, width:50, field:'gptype', formatter:function(v){return formmat.fmtGptype(v)} ">业务类型</th>
        <th data-options="sortable:false, width:120, field:'gpdesc'">短信组描述</th>
        <th data-options="sortable:false, width:30, field:'isdefault', formatter:function(v){return formmat.fmtIsdefault(v)} ">是否默认组</th>
        <th data-options="sortable:false, field:'id', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>

<div id="tb" style="display: none;">
    <shiro:hasPermission name="${resourceid}:add">
    <div class="whgd-gtb-btn">
        <a class="easyui-linkbutton" iconCls="icon-add" onclick="whgListTool.add()">添 加</a>
    </div>
    </shiro:hasPermission>

    <div class="whgdg-tb-srch">
        <input class="easyui-combobox" name="gptype" id="gptype" prompt="请选择业务类型"
               data-options="width:150, panelHeight:'auto', valueField:'id'"/>
        <input class="easyui-textbox" name="gpdesc" prompt="请输入短信组描述" data-options="width:200"/>
        <a class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#tb');">查 询</a>
        <span style="color: #ff0000;">&nbsp;&nbsp;&nbsp;&nbsp;各业务类型需要设置一个默认组</span>
    </div>
</div>

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">

    <a plain="true" method="whgListTool.view">查看</a>

    <shiro:hasPermission name="${resourceid}:edit">
        <a plain="true" method="whgListTool.edit" >编辑</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:setaliysmsgroup">
        <a plain="true" method="whgListTool.setaliysmsgroup" >设置短信组模板</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:del">
        <a plain="true" method="whgListTool.del" >删除</a>
    </shiro:hasPermission>

</div>
<!-- 操作按钮-END -->

<script>

    var formmat = {
        init : function(){
            /*var gptypearr = [];
            gptypearr.push({"id":"5","text":"培训类型"});
            gptypearr.push({"id":"4","text":"活动类型"});
            gptypearr.push({"id":"2","text":"场馆类型"});
            this.gptypearr = gptypearr;
            this.selectLoadData();*/
            var that = this;
            $.ajax({
                url: "${basePath}/admin/yunwei/aliysmsgroup/getgptypes",
                async: false,
                success: function(data){
                    that.gptypearr = data || [];
                    that.selectLoadData();
                }
            });
        },

        selectLoadData: function(){
            var data = this.gptypearr;
            $("#gptype").combobox('loadData', data);
        },

        fmtGptype: function(v, i, r){
            var me = this;
            for(var i in me.gptypearr){
                if (me.gptypearr[i]["id"] == v){
                    return me.gptypearr[i]["text"]
                }
            }
            return v;
        },

        fmtIsdefault: function(v, i, r){
            if (v == 1) return '是';
            else return '否';
        }
    };


    $(function(){
        formmat.init();
        WhgComm.search('#whgdg', '#tb', '${basePath}/admin/yunwei/aliysmsgroup/srchList4p');
    });

    var whgListTool = new Gridopts();

    Gridopts.prototype.setaliysmsgroup = function(idx){
        var row = this.__getGridRow(idx);
        WhgComm.editDialog('${basePath}/admin/yunwei/aliysmsgroup/smsgcref/view/list?id=' + row.id);
    }

</script>

</body>
</html>
