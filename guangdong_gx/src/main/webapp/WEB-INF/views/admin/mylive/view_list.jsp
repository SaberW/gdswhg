<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>直播管理-活动直播列表</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <c:choose>
        <c:when test="${reftype == '1'}"><c:set var="reftitle" value="活动"></c:set> </c:when>
        <c:when test="${reftype == '2'}"><c:set var="reftitle" value="培训"></c:set> </c:when>
        <c:otherwise><c:set var="reftitle" value="在线课程"></c:set></c:otherwise>
    </c:choose>
</head>
<body>
<div id="select_add_type">
    <a id="add_01" href="#" onclick="add_01()" style="margin-left: 65px;margin-top: 20px;height: 38px;width: 180px"></a>
    <a id="add_02" href="#" onclick="add_02()" style="margin-left: 65px;margin-top: 10px;height: 38px;width: 180px"></a>
    <a id="add_03" href="#" onclick="add_03()" style="margin-left: 65px;margin-top: 10px;height: 38px;width: 180px" ></a>
</div>
<!-- 表格 -->
<table id="whgdg" title="${reftitle}直播" class="easyui-datagrid" style="display: none" data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:true, pagination:true, toolbar:'#whgdg-tb'">
    <thead>
    <tr>
        <th data-options="field:'reftitle', width:300">${reftitle}</th>
        <th data-options="field:'name', width:250">直播标题</th>
        <th data-options="field:'starttime',width:130, formatter:WhgComm.FMTDateTime, sortable:true">开始时间</th>
        <th data-options="field:'endtime',width:130, formatter:WhgComm.FMTDateTime, sortable:true">结束时间</th>
        <th data-options="field:'flowaddr', width:430">推流地址</th>
        <th data-options="field:'playaddr', width:270">直播地址</th>
        <th data-options="field:'state', width:50, formatter:WhgComm.FMTState">状态</th>
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
    </div>
    </shiro:hasPermission>
    <div class="whgdg-tb-srch">
        <input class="easyui-textbox" style="width: 200px;" name="name" data-options="prompt:'请输入直播名称', validType:'length[1,32]'" />
        <input class="easyui-combobox" style="width: 200px;" id="state" name="state" data-options="prompt:'请选择状态',editable:false,valueField:'id',textField:'text',data:WhgComm.getState()"/>
        <%--<input class="easyui-combobox" style="width: 200px;" id="addtype" name="addtype" data-options="prompt:'其它方式',editable:false,valueField:'id',textField:'text',data:[{id:'1',text:'推流添加方式'},{id:'2',text:'直接添加方式'},{id:'3',text:'链接跳转方式'}]"/>--%>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <shiro:hasPermission name="${resourceid}:view"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="doSee">查看</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="0" method="doEdit">编辑</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="1" method="doEditVideo">回顾视频配置</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:off"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="1" method="doOff">停用</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:on"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="0" method="doOn">启用</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:del"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="state" validVal="0" method="doDel">删除</a></shiro:hasPermission>
</div>
<!-- 操作按钮-END -->


<!-- script -->
<script type="text/javascript">
    /** 查询数据 */
    function initDataGrid() {
        //所属文化馆
        var cultid = WhgComm.getMgrCultsFirstId();
        var deptidArr = WhgComm.getChildDept(cultid);
        var deptids = ''; var spt = '';
        for(var i=0; i<deptidArr.length; i++){
            deptids += spt+deptidArr[i].id;
            spt = ',';
        }
        //设置URL，并查询
        var url = '${basePath}/admin/mylive/srchList4p?reftype=${reftype}&cultid='+cultid+"&deptid="+deptids;
        WhgComm.search('#whgdg', '#whgdg-tb', url);
    }

    /** 添加 */
    function doAdd() {
      // WhgComm.editDialog('${basePath}/admin/mylive/view/add?reftype=${reftype}');
       $('#select_add_type').window({
            width:300,
            height:230,
            modal:true,
            title:"请选择添加直播方式"
        });
        $('#add_01').linkbutton({
            iconCls: '',
            text:"添加推流和直播地址方式"
        });
        $('#add_02').linkbutton({
            iconCls: '',
            text:"直接添加播放地址方式"
        });
        $('#add_03').linkbutton({
            iconCls: '',
            text:"添加跳转地址方式",
        });

    }

    /*推流地址方式添加*/
    function add_01(){
        $('#select_add_type').window("close");
        WhgComm.editDialog('${basePath}/admin/mylive/view/add/1?reftype=${reftype}');
    }
    /*直接播放地址方式添加*/
    function add_02(){
        $('#select_add_type').window("close");
        WhgComm.editDialog('${basePath}/admin/mylive/view/add/2?reftype=${reftype}');
    }
    /*跳转链接地址方式添加*/
    function add_03(){
        $('#select_add_type').window("close");
        WhgComm.editDialog('${basePath}/admin/mylive/view/add/3?reftype=${reftype}');
    }

    /** 编辑 */
    function doEdit(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/mylive/view/edit?id='+curRow.id);
    }

    /** 回顾视频配置-选择群文化资源 */
    function doEditVideo(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/mylive/view/edit?onlyshow=1&editVod=1&id='+curRow.id);
    }

    /** 查看 */
    function doSee(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/mylive/view/edit?onlyshow=1&id='+curRow.id);
    }

    /** 停用 */
    function doOff(idx){
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.confirm("确认信息", "确定要停用选中的记录吗？", function(){
            _doAjax('${basePath}/admin/mylive/updateState', {id:curRow.id, state:'0'});
        });
    }

    /** 启用 */
    function doOn(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.confirm("确认信息", "确定要启用选中的记录吗？", function(){
            _doAjax('${basePath}/admin/mylive/updateState', {id:curRow.id, state:'1'});
        });
    }

    /** 删除 */
    function doDel(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.confirm("确认信息", "确定要删除选中的记录吗？", function(){
            _doAjax('${basePath}/admin/mylive/del', {id:curRow.id});
        });
    }

    /** Ajax */
    function _doAjax(url, params){
        $.ajax({
            url: url,
            data: params,
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

    $(function () {
        //初始DataGrid查询
        initDataGrid();
    });
</script>
<!-- script END -->
</body>
</html>