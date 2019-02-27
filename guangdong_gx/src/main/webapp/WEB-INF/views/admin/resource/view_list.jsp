<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <c:choose>
        <c:when test="${reftype eq '25'}">
            <c:set var="pageTitle" value="作品"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="资源"></c:set>
        </c:otherwise>
    </c:choose>
    <title>${pageTitle}管理</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
</head>
<body>
<!-- 表格 -->
<table id="whgdg" title="${pageTitle}管理" class="easyui-datagrid" style="display: none"
       data-options="
            fit:true,
            striped:true,
            rownumbers:true,
            fitColumns:true,
            singleSelect:true,
            checkOnSelect:true,
            selectOnCheck:true,
            pagination:true,
            toolbar:'#whgdg-tb', url:'${basePath}/admin/resource/srchList4p?isbrand=${isbrand}&refid=${refid}&reftype=${reftype}'">
    <thead>
    <tr>
        <%--<th data-options="field:'entid', width:60">资源ID</th>--%>
        <th data-options="field:'name', width:60">${pageTitle}名</th>
        <%--<th data-options="field:'enttype', width:60">资源类型</th>--%>
        <th data-options="field:'enttype',width:60,formatter:WhgComm.FMTResType">${pageTitle}类型</th>
        <%--<th data-options="field:'reftype', width:60">实体类型</th>--%>
        <th data-options="field:'reftype',width:60,formatter:WhgComm.FMTTypeClazz">实体类型</th>
        <%--<th data-options="field:'refid', width:60">实体ID</th>--%>
        <th data-options="field:'enturl', width:60,formatter:FMTErul">${pageTitle}地址(图片)</th>
        <th data-options="field:'enttimes', width:60">视频/音频时长</th>
        <th data-options="field:'deourl', width:60, formatter:WhgComm.FMTImg">视频封面</th>
        <th data-options="field:'_opt', width:60, formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <div class="whgd-gtb-btn">
        <c:if test="${isbrand == 0}">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add"
               onclick="doAdd();">添加${pageTitle}</a>
        </c:if>
        <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
    </div>
    <div class="whgdg-tb-srch">
        <input class="easyui-textbox" style="width: 200px; height:26px" name="name"
               data-options="prompt:'${pageTitle}名称'"/>
        <select id="cc" class="easyui-combobox" name="enttype" panelHeight="auto" style="width:200px; height:26px"
                data-options="prompt:'请选择${pageTitle}类型', value:''">
            <option value="1">图片</option>
            <option value="2">视频</option>
            <option value="3">音频</option>
            <option value="4">文档</option>
        </select>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="doView">查看</a>
    <c:if test="${isbrand == 0}">
        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="doEdit">编辑</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="doDel">删除</a>
    </c:if>


</div>
<!-- 操作按钮-END -->

<!-- 添加表单 -->
<div id="whgwin-add"></div>
<%--<div id="whgwin-add-btn" style="text-align: center">--%>
<%--<div style="display: inline-block; margin: 0 auto">--%>
<%--<a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="submitForm()">保 存</a>--%>
<%--<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#whgwin-add').dialog('close')">关 闭</a>--%>
<%--</div>--%>
<%--</div>--%>
<!-- 添加表单 END -->

<script>
    /** 格式化资源地址*/
    function FMTErul(val, data, idx) {
        var enttype = data.enttype;
        if("1" == enttype){
            return WhgComm.FMTImg(val);
        }else if("2" == enttype){
            return val;//return WhgComm.FMTImg(data.deourl);
        }
        else if("4" == enttype){
            return val;//return WhgComm.FMTImg(data.deourl);
        }
        return "";
    }

    /** 添加资源方法 */
    function doAdd() {
        var cultid = '${param.cultid}';
        WhgComm.editDialog('${basePath}/admin/resource/view/add/?refid=${refid}&reftype=${reftype}&cultid='+cultid );
    }
    <%--console.log(${reftype});--%>
    /** 添加资源方法-END */

    /** 编辑资源方法 */
    function doEdit(idx) {
        var curtRow = $('#whgdg').datagrid('getRows')[idx];
        var cultid = '${param.cultid}';
        WhgComm.editDialog('${basePath}/admin/resource/view/edit?reftype=${reftype}&id=' + curtRow.id + '&cultid=' + cultid);
    }
    /** 编辑资源方法-END */

    /** 查看资源方法 */
    function doView(idx) {
        var curtRow = $('#whgdg').datagrid('getRows')[idx];
        var cultid = '${param.cultid}';
        WhgComm.editDialog('${basePath}/admin/resource/view/edit?isview=1&id=' + curtRow.id+'&cultid='+cultid);
    }
    /** 查看资源方法-END */

    /**删除 */
    function doDel(idx) {
        var rows = $("#whgdg").datagrid('getRows');
        var row = rows[idx];
        var entid = row.id;
        $.messager.confirm('确认对话框', '您确认要删除吗？', function (r) {
            if (r) {
                $.ajax({
                    type: "POST",
                    url: "${basePath}/admin/resource/del",
                    data: {id: entid},
                    success: function (data) {
                        //var Json = $.parseJSON(data);
                        if (data.success == "1") {
                            $('#whgdg').datagrid('reload');
                        } else {
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
