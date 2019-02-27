<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>轮播图配置</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <style>
        .provinceLBT{
            color: #f5f5f5;
            background-color: #9c27b0;
            display: inline-block;
            margin-right: 5px;
        }
        .cityLBT{
            color: #f5f5f5;
            background-color: #FF5722;
            display: inline-block;
            margin-right: 5px;
        }
        .siteLBT{
            color: #f5f5f5;
            background-color: #00bcd4;
            display: inline-block;
            margin-right: 5px;
        }
    </style>
</head>
<body class="easyui-layout">
    <div data-options="region:'west',title:'区域',collapsible:false,split:true,tools:[{iconCls:'icon-reload', handler:reloadTree}]" style="width:200px;padding:10px;">
        <ul class="easyui-tree" id="mass_type_tree"></ul>
    </div>
    <div data-options="region:'center',title:'轮播图配置列表'">
        <!-- 表格 -->
        <table id="whgdg" class="easyui-datagrid" style="display: none"
               data-options="border:false, fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:true, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb'">
            <thead>
            <tr>
                <th data-options="field:'name', width:350">标题</th>
                <th data-options="field:'picture', width:367, formatter:WhgComm.FMTImg">图片</th>
                <th data-options="field:'_val', width:130, formatter:FMTLbt">轮播图归属</th>
                <th data-options="field:'state', width:40, formatter:WhgComm.FMTState">状态</th>
                <th data-options="field:'areaidx', width:60">排序索引</th>
                <th data-options="field:'_opt', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
            </tr>
            </thead>
        </table>
        <!-- 表格 END -->

        <!-- 表格操作工具栏 -->
        <div id="whgdg-tb" style="display: none;">
            <shiro:hasPermission name="${resourceid}:add">
            <div class="whgd-gtb-btn">
                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="doAdd();">添加</a>
                <c:if test="${clazz!='11' and clazz!='12'}">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add"
                       onclick="doAdd2();">从文化馆添加</a>
                </c:if>
            </div>
            </shiro:hasPermission>
            <div class="whgdg-tb-srch">
                <input type="hidden" id="province" name="province" value=""/>
                <input type="hidden" id="city" name="city" value=""/>
                <input class="easyui-combobox" style="width: 200px;" name="state" data-options="prompt:'请选择状态', editable:false, valueField:'id', textField:'text', data:WhgComm.getState()"/>
                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
            </div>
        </div>
        <!-- 表格操作工具栏-END -->

        <!-- 操作按钮 -->
        <div id="whgdg-opt" style="display: none;">
            <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="doSee">查看</a>
            <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validEdit" method="doEdit">编辑</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validUp" method="doUp">上移</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validUp" method="doTop">置顶</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:off"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validOff" method="doOff">停用</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:on"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validOn" method="doOn">启用</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:del"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validDel" method="doDel">删除</a></shiro:hasPermission>
            <%--<shiro:hasPermission name="${resourceid}:toprovince"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="_toProvice"  method="toProvice">推送全省站</a></shiro:hasPermission>--%>
            <shiro:hasPermission name="${resourceid}:untoprovince"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="_unToProvice"  method="toProvice">取消推送全省站</a></shiro:hasPermission>
            <%--<shiro:hasPermission name="${resourceid}:tocity"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="_toCity"  method="toCity">推送全市站</a></shiro:hasPermission>--%>
            <shiro:hasPermission name="${resourceid}:untocity"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="_unToCity"  method="toCity">取消推送全市站</a></shiro:hasPermission>
        </div>
        <!-- 操作按钮-END -->
    </div>

    <script>
    /** 格式化显示 */
    function FMTLbt(val, row, idx){
        var html = '';
        if(typeof(row.province) != 'undefined' && row.province != ''){
            html += '<i class="provinceLBT">全省站</i>';
        }
        if(typeof(row.city) != 'undefined' && row.city != ''){
            html += '<i class="cityLBT">全市站</i>';
        }
        if(typeof(row.cultid) != 'undefined' && row.cultid != ''){
            html += '<i class="siteLBT">文化馆</i>';
        }
        return html;
    }

    //初始tree
    function initMassTypeTree(){
        $('#mass_type_tree').tree({
            url: '${basePath}/admin/yunwei/area/srchList4tree',
            animate: true,
            lines: true,
            onBeforeExpand: function(node){
                var t = $(this);
                t.tree('options').queryParams = {level:node.level};
                if(node.level >= 2) return false;
            },
            onLoadSuccess: function (node, data) {
                var t = $(this);
                if(data){
                    $(data).each(function(index, d){
                        if(typeof(window.__initPage__) == "undefined"){
                            var n = $('#mass_type_tree').tree('find', this.id);
                            $('#mass_type_tree').tree('select', n.target);
                            __initLevel__ = this.level;
                            __initPage__ = true;
                        }
                        if(this.state == 'closed' && this.level <= __initLevel__){
                            t.tree('expandAll');
                        }
                    });
                }
            },
            onSelect: function(node){
                if(node.level == 1){
                    $('#province').val(node.text);
                    $('#city').val('');
                }else{
                    $('#province').val('');
                    $('#city').val(node.text);
                }
                WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/yunwei/lbt/srchList4p4area?type=${clazz}');
            }
        });
    }

    //刷新树
    function reloadTree(){
        $('#mass_type_tree').tree('reload');
    }


    /** 按钮验证方法 */
    function isAreaLbt(row){
        var isArea = false;
        if ((typeof(row.cultid) == 'undefined' || row.cultid == '') && typeof(row.province) != 'undefined' && row.province != '') {
            isArea = true;
        } else if ((typeof(row.cultid) == 'undefined' || row.cultid == '') && typeof(row.city) != 'undefined' && row.city != '') {
            isArea = true;
        }
        return isArea;
    }
    function validUp(idx){
        return idx != 0;
    }
    function validEdit(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 0 && isAreaLbt(row);
    }
    function validOff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 1 && isAreaLbt(row);
    }
    function validOn(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 0 && isAreaLbt(row);
    }
    function validDel(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 0 && isAreaLbt(row);
    }
    function  _toProvice(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        var province = $('#province').val();
        var city = $('#city').val();
        return !isAreaLbt(row) && row.state == 1 && city == '' && province != '' && !row.province;
    }
    function  _unToProvice(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        var province = $('#province').val();
        var city = $('#city').val();
        return !isAreaLbt(row) && row.state == 1 && city == '' && province != '' && row.province ;
    }
    function  _toCity(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        var province = $('#province').val();
        var city = $('#city').val();
        return !isAreaLbt(row) && row.state == 1 && province == '' && city != '' && !row.city;
    }
    function  _unToCity(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        var province = $('#province').val();
        var city = $('#city').val();
        return !isAreaLbt(row) && row.state == 1 && province == '' && city != '' && row.city;
    }

    /** 查看 */
    function doSee(idx){
        var curtRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/yunwei/lbt/view/edit/${clazz}?id=' + curtRow.id+"&onlyshow=1");
    }//End查看

    /** 添加 */
    function doAdd() {
        var treeNode = $('#mass_type_tree').tree('getSelected');
        if(!treeNode){
            $.messager.alert('警告消息', '请选择区域，确定添加区域的轮播图！', 'warning');
        }
        var province = $('#province').val();
        var city = $('#city').val();
        WhgComm.editDialog('${basePath}/admin/yunwei/lbt/view/add/${clazz}?flag=1&province='+encodeURIComponent(province)+'&city='+encodeURIComponent(city));
    }
    /** 添加-END */

    /** 添加 */
    function doAdd2() {
        var treeNode = $('#mass_type_tree').tree('getSelected');
        if(!treeNode){
            $.messager.alert('警告消息', '请选择区域，确定添加区域的轮播图！', 'warning');
        }
        var province = $('#province').val();
        var city = $('#city').val();
        var title = '从文化馆添加全市站轮播图';
        if(province != ''){
            title = '从文化馆添加全省站轮播图';
        }
        WhgComm.openDialog4size(title, '${basePath}/admin/yunwei/lbt/view/list_add/${clazz}?province='+encodeURIComponent(province)+'&city='+encodeURIComponent(city));
    }
    /** 添加-END */

    /** 编辑 */
    function doEdit(idx) {
        var curtRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/yunwei/lbt/view/edit/${clazz}?id=' + curtRow.id);
    }
    /** 编辑-END */

    /**删除 */
    function doDel(idx) {
        var rows = $("#whgdg").datagrid('getRows');
        var row = rows[idx];
        var id = row.id;
        $.messager.confirm('确认对话框', '您确认要删除吗？', function (r) {
            if (r) {
                $.ajax({
                    type: "POST",
                    url: "${basePath}/admin/yunwei/lbt/del",
                    data: {id: id},
                    success: function (data) {
                        //var Json = $.parseJSON(data);
                        if (data.success == "1") {
                            $('#whgdg').datagrid('reload');
                        } else {
                            $.messager.alert("提示", data.errormsg);
                        }

                    }
                });
            }
        });
    }

    /** 启用 */
    function doOn(idx) {
        var curtRow = $('#whgdg').datagrid('getRows')[idx];
        updataState(curtRow.id, '1');
    }

    /** 停用 */
    function doOff(idx) {
        var curtRow = $('#whgdg').datagrid('getRows')[idx];
        updataState(curtRow.id, '0');

    }

    /** 修改状态 */
    function updataState(id, state) {
        $.ajax({
            url: "${basePath}/admin/yunwei/lbt/updstate",
            cache: false,
            data: {id: id, state: state},
            success: function (data) {
                if (data.success == "1") {
                    $('#whgdg').datagrid('reload');
                } else {
                    $.messager.alert("提示", data.data.errormsg);
                }

            }

        });
    }

    /** 推荐或者取消推荐到省 */
    function toProvice(idx) {
        var row = $('#whgdg').datagrid('getRows')[idx];
        subToProvice(1,row.id, row.cultid);
    }

    /** 推荐或者取消推荐到市 */
    function toCity(idx) {
        var row = $('#whgdg').datagrid('getRows')[idx];
        subToProvice(2,row.id, row.cultid);
    }

    /** 推到省 */
    function subToProvice(type,id,cultid) {
        $.ajax({
            url: "${basePath}/admin/yunwei/lbt/toarea",
            data: {type: type,id: id, cultid: cultid},
            success: function (data) {
                if (data.success == "1") {
                    $('#whgdg').datagrid('reload');
                } else {
                    $.messager.alert("提示", data.errormsg);
                }
            }
        });
    }

    /**
     * 上移
     * @param idx
     */
    function doUp(idx){
        var row = $('#whgdg').datagrid('getRows')[idx];
        var dataScope = $('#province').val() != '' ? "province" : "city";
        $.ajax({
            url: "${basePath}/admin/yunwei/lbt/move",
            data: {id:row.id, type: 'up', dataScope:dataScope, clazz:'${clazz}'},
            success: function (data) {
                if (data.success == "1") {
                    $('#whgdg').datagrid('reload');
                } else {
                    $.messager.alert("提示", data.errormsg);
                }
            }
        });
    }
    /**
     * 置顶
     * @param idx
     */
    function doTop(idx){
        var row = $('#whgdg').datagrid('getRows')[idx];
        var dataScope = $('#province').val() != '' ? "province" : "city";
        $.ajax({
            url: "${basePath}/admin/yunwei/lbt/move",
            data: {id:row.id, type: 'top', dataScope:dataScope, clazz:'${clazz}'},
            success: function (data) {
                if (data.success == "1") {
                    $('#whgdg').datagrid('reload');
                } else {
                    $.messager.alert("提示", data.errormsg);
                }
            }
        });
    }

    $(function () {
        initMassTypeTree();
    });
    </script>
</body>
</html>
