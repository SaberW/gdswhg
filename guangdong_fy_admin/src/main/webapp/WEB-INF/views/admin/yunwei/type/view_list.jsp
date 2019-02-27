<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>
    <c:choose>
        <c:when test="${classify == 1}">
            <c:set var="pageTitle" value="艺术分类配置"></c:set>
        </c:when>
        <c:when test="${classify == 2}">
            <c:set var="pageTitle" value="场馆分类配置"></c:set>
        </c:when>
        <c:when test="${classify == 3}">
            <c:set var="pageTitle" value="活动室分类配置"></c:set>
        </c:when>
        <c:when test="${classify == 4}">
            <c:set var="pageTitle" value="活动分类配置"></c:set>
        </c:when>
        <c:when test="${classify == 5}">
            <c:set var="pageTitle" value="培训分类配置"></c:set>
        </c:when>
        <c:when test="${classify == 7}">
            <c:set var="pageTitle" value="活动室设备配置"></c:set>
        </c:when>
        <c:when test="${classify == 11}">
            <c:set var="pageTitle" value="老师专长配置"></c:set>
        </c:when>
        <c:when test="${classify == 16}">
            <c:set var="pageTitle" value="直播分类配置"></c:set>
        </c:when>
        <c:when test="${classify == 20}">
            <c:set var="pageTitle" value="商品分类配置"></c:set>
        </c:when>
        <c:when test="${classify == 21}">
            <c:set var="pageTitle" value="展演类商品分类配置"></c:set>
        </c:when>
        <c:when test="${classify == 22}">
            <c:set var="pageTitle" value="展品分类配置"></c:set>
        </c:when>
        <c:when test="${classify == 23}">
            <c:set var="pageTitle" value="展览类商品分类配置"></c:set>
        </c:when>
        <c:when test="${classify == 25}">
            <c:set var="pageTitle" value="人才分类配置"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="分类配置"></c:set>
        </c:otherwise>
    </c:choose>
    <title>${pageTitle}</title>
</head>
<body>

<!-- 表格 -->
<table id="whgdg" title="${pageTitle}" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:true, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb', queryParams:{cultid:WhgComm.getMgrCultsFirstId()}, url:'${basePath}/admin/yunwei/type/srchList4p?type=${classify}'">
    <thead>
    <tr>
        <th data-options="field:'name', width:160">名称</th>
        <th data-options="field:'idx', width:160">排序值</th>
        <th data-options="field:'_opt', width:160, formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <div class="whgd-gtb-btn">
        <shiro:hasPermission name="${resourceid}:add"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="add();">添加</a></shiro:hasPermission>
    </div>
    <div class="whgdg-tb-srch">
        <input class="easyui-combobox" name="cultid" id="cultid" style="width:200px;" panelHeight="auto" data-options="prompt:'请选择文化馆'">
        <input class="easyui-textbox" style="width: 200px;" name="name" data-options="prompt:'请输入分类名称', validType:'length[1,32]'" />
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="edit">编辑</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="canOpen" method="optOpen">启用</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="canClose" method="optClose">停用</a></shiro:hasPermission>
    <%--<shiro:hasPermission name="${resourceid}:off"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="sort">排序</a></shiro:hasPermission>--%>
    <shiro:hasPermission name="${resourceid}:del"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="del">删除</a></shiro:hasPermission>
</div>
<!-- 操作按钮-END -->

<!-- 添加表单 -->
<div id="whgwin-add" style="display: none">
    <form id="whgff" class="whgff" method="post">
        <input type="hidden" name="id"/>

        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 25%"><i>*</i>所属文化馆：</div>
            <div class="whgff-row-input" style="width: 75%">
                <input class="easyui-combobox" name="cultid" id="cultid2" style="width:90%; height:32px" panelHeight="auto" data-options="prompt:'请选择文化馆', required:true">
            </div>
        </div>

        <div class="whgff-row" id="showBigType">
            <div class="whgff-row-label" style="width: 25%"><i>*</i>父级分类：</div>
            <input class="easyui-combobox" style="width:60%; height:32px" name="pid" id="pid" data-options="prompt:'请选择父级分类'" />
        </div>

        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 25%"><i>*</i>名称：</div>
            <div class="whgff-row-input" style="width: 75%"><input class="easyui-textbox" name="name" style="width:90%; height:32px" data-options="required:true,validType:'length[1,20]'"></div>
        </div>

        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 25%"><i>*</i>排序值：</div>
            <div class="whgff-row-input" style="width: 75%"><input class="easyui-numberspinner" name="idx" style="width:90%; height:32px" data-options="required:true,validType:'length[0,10]'"></div>
        </div>
    </form>
</div>
<div id="whgwin-add-btn" style="text-align: center; display: none">
    <div style="display: inline-block; margin: 0 auto">
        <a href="#" class="easyui-linkbutton" iconCls="icon-save" id="btn" >保 存</a>
        <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#whgwin-add').dialog('close')">关 闭</a>
    </div>
</div>
<!-- 添加表单 END -->



<script>
    $(function () {
        //条件查询-文化馆
        WhgComm.initPMS({cultEid:'cultid', ywiBigTypeEid:"pid", ywiBigTypeValue:false,ywiBigType:"${classify}"
        });
    });

    /**
     * 是否可以停止
     * @param idx
     * @returns {boolean}
     */
    function canClose(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        if((curRow.pid==null||curRow.pid=="")&&(curRow.closeid==null||curRow.closeid.indexOf(_init_cult_value)==-1)){//父级
            return true;
        }
        return false;
    }

    /**
     * 是否可以启动
     * @param idx
     * @returns {boolean}
     */
    function canOpen(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        if((curRow.pid==null||curRow.pid=="")&&curRow.closeid!=null&&curRow.closeid.indexOf(_init_cult_value)!=-1){//父级
            return true;
        }
        return false;
    }

    /**
     * 启动操作
     * @param idx
     * @returns {boolean}
     */
    function optOpen(idx){
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        updateOpt(curRow.id,1);// 1  启动  0  停止
    }

    /**
     * 关闭操作
     * @param idx
     * @returns {boolean}
     */
    function optClose(idx){
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        updateOpt(curRow.id,0);// 1  启动  0  停止
    }


  function updateOpt(ids,type){
        $.ajax({
            url: getFullUrl('/admin/yunwei/type/updateOpt'),
            data: {id:ids, cultid:_init_cult_value,type:type},
            cache: false,
            success: function (data) {
                if(data && data.success == '1'){
                    $('#whgdg').datagrid('reload');
                } else {
                    $.messager.alert('提示', '操作失败:'+data.errormsg+'！', 'error');
                }
            }
        });
    }


    /** 添加分类 */
    function add(){
        $('#whgwin-add').dialog({
            title: '分类管理-添加分类',
            cache: false,
            modal: true,
            width: '400',
            height: '320',
            maximizable: true,
            resizable: true,
            // href: '${basePath}/admin/system/cult/view/add',
            buttons: '#whgwin-add-btn',
            onOpen: function () {
                if(_init_cult_value&&_init_cult_value=="0000000000000000"){
                    $("#showBigType").hide();
                }else{
                    $("#showBigType").show();
                }
                //init Form
                $('#whgff').form({
                    url : '${basePath}/admin/yunwei/type/add?type=${classify}',
                    onSubmit : function(param) {
                        var isValid = $(this).form('enableValidation').form('validate');
                        if(isValid){
                            $.messager.progress();
                        }else{
                            $("#btn").off("click").one("click",function () { $('#whgff').submit(); });
                        }
                        return isValid
                    },
                    success : function(data) {
                        $.messager.progress('close');
                        var Json = $.parseJSON(data);
                        if(Json.success == "1"){
                            $('#whgdg').datagrid('reload');
                            $('#whgwin-add').dialog('close');
                        }else{
                            $.messager.alert("提示", Json.errormsg);
                            $("#btn").off("click").on("click",function () {$('#whgff').submit();});
                        }

                    }
                });
                WhgComm.initPMS({cultEid:'cultid2'});
                $('#whgff').form("clear");
                $("#btn").off("click").one("click",function () { $('#whgff').submit(); });
                WhgComm.initPMS({cultEid:'cultid2'});
            }
        });
    }

    /** 更新分类方法 */
    function edit(idx){
        $('#whgwin-add').dialog({
            title: '分类管理-编辑分类',
            cache: false,
            modal: true,
            width: '400',
            height: '320',
            maximizable: true,
            resizable: true,
            // href: '${basePath}/admin/system/cult/view/add',
            buttons: '#whgwin-add-btn',
            onOpen : function () {
                var rows = $("#whgdg").datagrid('getRows');
                var row = rows[idx];
                var id = row.id;
                if (row){
                    $('#whgff').form('load', row);
                    $('#whgff').form({
                        url : '${basePath}/admin/yunwei/type/edit',
                        onSubmit : function(param) {
                            var isValid = $(this).form('enableValidation').form('validate');
                            if(isValid){
                                $.messager.progress();
                            }else{
                                $("#btn").off("click").one("click",function () { $('#whgff').submit(); });
                            }
                            return isValid
                        },
                        success : function(data) {
                            $.messager.progress('close');
                            var Json = $.parseJSON(data);
                            if(Json.success == "1"){
                                $('#whgdg').datagrid('reload');
                                $('#whgwin-add').dialog('close');
                            }else{
                                $.messager.alert("提示", Json.errormsg);
                                $("#btn").off("click").on("click",function () {$('#whgff').submit();});

                            }

                        }
                    });
                }
                $("#btn").off("click").on("click",function () {$('#whgff').submit();});

                WhgComm.initPMS({cultEid:'cultid2', cultValue:row.cultid});
            }
        });
    }

    /*
     * 删除分类 */
    function del(idx){
        var rows = $("#whgdg").datagrid('getRows');
        var row = rows[idx];
        var id = row.id;
        $.messager.confirm('确认对话框', '您确认要删除吗？', function(r){
            if (r){
                $.ajax({
                    type: "POST",
                    url: "${basePath}/admin/yunwei/type/del",
                    data: {id : id},
                    success: function(data){
                        //var Json = $.parseJSON(data);
                        if(data.success == "1"){
                            $('#whgdg').datagrid('reload');
                        }else{
                            $.messager.alert("提示", data.errorMsg);
                        }

                    }
                });
            }
        });
    }
</script>
</body>
</html>