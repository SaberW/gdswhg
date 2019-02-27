<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>账号管理-区域管理员</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
</head>
<body class="easyui-layout">
    <div data-options="region:'west',title:'区域',collapsible:false,split:true,tools:[{iconCls:'icon-reload', handler:reloadTree}]" style="width:200px;padding:10px;">
        <ul class="easyui-tree" id="mass_type_tree"></ul>
    </div>
    <div data-options="region:'center',title:'账号列表'">
        <!-- 表格 -->
        <table id="whgdg" class="easyui-datagrid" style="display: none"
               data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:true, pagination:true, toolbar:'#whgdg-tb'">
            <thead>
            <tr>
                <th data-options="field:'account', width:250">账号</th>
                <th data-options="field:'contactnum', width:100">手机号</th>
                <th data-options="field:'lastdate', width:130, formatter:WhgComm.FMTDateTime">最后登录时间</th>
                <th data-options="field:'state', width:60, formatter:WhgComm.FMTState">状态</th>
                <th data-options="field:'_opt', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
            </tr>
            </thead>
        </table>
        <!-- 表格 END -->

        <!-- 表格操作工具栏 -->
        <div id="whgdg-tb" style="display: none;">
            <div class="whgd-gtb-btn">
                <shiro:hasPermission name="${resourceid}:add"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="doAdd();">添加管理员</a></shiro:hasPermission>
            </div>
            <div class="whgdg-tb-srch">
                <input type="hidden" id="adminprovince" name="adminprovince" value=""/>
                <input type="hidden" id="admincity" name="admincity" value=""/>
                <input type="hidden" id="adminarea" name="adminarea" value=""/>
                <input class="easyui-textbox" style="width: 200px;" name="account" data-options="prompt:'请输入账号查询', validType:'length[1,32]'" />
                <input class="easyui-combobox" style="width: 200px;" name="state" data-options="prompt:'请选择状态',editable:false,valueField:'id',textField:'text',data:WhgComm.getState()"/>
                <input class="easyui-combobox" style="width: 200px;" id="adminlevel" name="adminlevel" data-options="prompt:'请选择状态',editable:false,valueField:'id',textField:'text', data:getAdminLevel()"/>
                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
            </div>
        </div>
        <!-- 表格操作工具栏-END -->

        <!-- 操作按钮 -->
        <div id="whgdg-opt" style="display: none;">
            <shiro:hasPermission name="${resourceid}:view"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="doSee">查看</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validEdit" method="doEdit">编辑</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:off"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validOff" method="doOff">停用</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:on">  <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validOn" method="doOn">启用</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:del"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validDel" method="doDel">删除</a></shiro:hasPermission>
        </div>
        <!-- 操作按钮-END -->
    </div>

    <!-- script -->
    <script type="text/javascript">
        /**区域管理员级别*/
        function getAdminLevel(){
            return WhgSysData.getStateData("EnumCultLevel");
        }

        //初始tree
        function initMassTypeTree(){
            WhgCommAreaTree.initAreaTree({
                eleId: 'mass_type_tree',
                selectFirstNode: true,
                onSelect: function (node, firstLevel, areaInfo) {
                    getArea();
                    WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/system/user/srchList4p?admintype=sysmgr');
                }
            });
        }

        //刷新Tree
        function reloadTree(){
            initMassTypeTree();
        }

        //获取选中的省市区
        function getArea(){
            var selectedNode = $('#mass_type_tree').tree('getSelected');
            var areaInfo = getProvinceCityArea(selectedNode.text);
            $('#adminprovince').val(areaInfo.province);
            $('#admincity').val(areaInfo.city);
            $('#adminarea').val(areaInfo.area);
            $('#adminlevel').combobox('setValue',selectedNode.level);
            return areaInfo;
        }

        function validEdit(idx) {
            var curRow = $('#whgdg').datagrid('getRows')[idx];
            return curRow.state == 0 && curRow.id != '${sessionAdminUser.id}';
        }

        function validOff(idx) {
            var curRow = $('#whgdg').datagrid('getRows')[idx];
            return curRow.state == 1 && curRow.id != '${sessionAdminUser.id}';
        }

        function validOn(idx) {
            var curRow = $('#whgdg').datagrid('getRows')[idx];
            return curRow.state == 0 && curRow.id != '${sessionAdminUser.id}';
        }

        function validDel(idx) {
            var curRow = $('#whgdg').datagrid('getRows')[idx];
            return curRow.state == 0 && curRow.id != '${sessionAdminUser.id}';
        }

        /**
         * 添加文化馆
         */
        function doAdd() {
            var areaObj = getArea();
            var param = '&adminprovince='+encodeURIComponent(areaObj.province)+'&admincity='+encodeURIComponent(areaObj.city)+'&adminarea='+encodeURIComponent(areaObj.area);
            WhgComm.editDialog('${basePath}/admin/system/user/view/add?admintype=sysmgr&isbizmgr=0&adminlevel='+areaObj.level+param);
        }

        /**
         * 编辑信息
         * @param idx 行下标
         */
        function doEdit(idx) {
            var curRow = $('#whgdg').datagrid('getRows')[idx];
            WhgComm.editDialog('${basePath}/admin/system/user/view/edit?id='+curRow.id);
        }

        /**
         * 查看资料
         * @param idx 行下标
         */
        function doSee(idx) {
            var curRow = $('#whgdg').datagrid('getRows')[idx];
            WhgComm.editDialog('${basePath}/admin/system/user/view/edit?id='+curRow.id+"&onlyshow=1");
        }

        /**
         * 批量启用或停用
         * @param fromState 修改前的状态
         * @param toState 修改后的状态
         */
        function doBatchUpd(fromState, toState) {
            //选中的记录数
            var rows = $('#whgdg').datagrid('getChecked');
            if(rows.length < 1){
                $.messager.alert('提示', '请选中要操作的记录！', 'warning');
            }
            var _ids = ""; var spt = "";
            for(var i=0; i<rows.length; i++){
                _ids += spt+rows[i].id;
                spt = ',';
            }
            _doUpdState(_ids, fromState, toState);
        }

        /**
         * AJAX调用修改状态服务
         * @param ids 修改对象ID，多个用逗号分隔
         * @param fromState 修改前的状态
         * @param toState 修改后的状态
         * @private
         */
        function _doUpdState(ids, fromState, toState){
            $.ajax({
                url: getFullUrl('/admin/system/user/updstate'),
                data: {ids:ids, fromState:fromState, toState:toState},
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

        /**
         * 单个启用
         * @param idx 行下标
         */
        function doOn(idx) {
            var curRow = $('#whgdg').datagrid('getRows')[idx];
            _doUpdState(curRow.id, 0, 1);
        }

        /**
         * 单个停用
         * @param idx 行下标
         */
        function doOff(idx) {
            var curRow = $('#whgdg').datagrid('getRows')[idx];
            _doUpdState(curRow.id, 1, 0);
        }

        /**
         * 删除
         * @param idx
         */
        function doDel(idx) {
            var curRow = $('#whgdg').datagrid('getRows')[idx];
            $.messager.confirm('提示', '您确定要删除此记录吗？', function(r){
                if (r){
                    $.ajax({
                        type: "POST",
                        url: getFullUrl('/admin/system/user/del'),
                        data: {ids : curRow.id},
                        success: function(Json){
                            if(Json && Json.success == '1'){
                                //$.messager.alert('提示', '操作成功！', 'info');
                                $('#whgdg').datagrid('reload');
                            } else {
                                $.messager.alert('提示', '操作失败:'+Json.errormsg+'！', 'error');
                            }
                        }
                    });
                }
            });
        }

        $(function () {
            initMassTypeTree();
        });
    </script>
    <!-- script END -->
</body>
</html>