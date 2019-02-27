<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>文化馆列表</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'区域',collapsible:false,split:true,tools:[{iconCls:'icon-reload', handler:reloadTree}]" style="width:200px;padding:10px;">
    <ul class="easyui-tree" id="mass_type_tree"></ul>
</div>
<div data-options="region:'center',title:'文化馆列表'">
    <!-- 表格 -->
    <table id="whgdg" class="easyui-datagrid" style="display: none" data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:true, pagination:true, toolbar:'#whgdg-tb'">
        <thead>
        <tr>
            <th data-options="field:'name', width:250">文化馆名称</th>
            <th data-options="field:'level', width:80, formatter:FMTLevel">文化馆级别</th>
            <th data-options="field:'picture', width:130, formatter:WhgComm.FMTImg">图片</th>
            <th data-options="field:'contact', width:120">联系人</th>
            <th data-options="field:'contactnum', width:160">联系手机</th>
            <th data-options="field:'crtuser',width:120, formatter:WhgComm.FMTUserName">编辑者</th>
            <th data-options="field:'checkor',width:120, formatter:WhgComm.FMTUserName">审核者</th>
            <th data-options="field:'publisher',width:120, formatter:WhgComm.FMTUserName">发布者</th>
            <th data-options="field:'statemdfdate',width:130, formatter:WhgComm.FMTDateTime, sortable:true">操作时间</th>
            <th data-options="field:'state', width:50, formatter:WhgComm.FMTBizState">状态</th>
            <th data-options="field:'_opt', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
        </tr>
        </thead>
    </table>
    <!-- 表格 END -->

    <!-- 表格操作工具栏 -->
    <div id="whgdg-tb" style="display: none;">
        <shiro:hasPermission name="${resourceid}:add">
        <div class="whgd-gtb-btn">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="doAdd();">添加文化馆</a>
        </div>
        </shiro:hasPermission>
        <div class="whgdg-tb-srch">
            <input type="hidden" id="province" name="province" value=""/>
            <input type="hidden" id="city" name="city" value=""/>
            <input type="hidden" id="area" name="area" value=""/>
            <input class="easyui-textbox" style="width: 200px;" name="name" data-options="prompt:'请输入分馆名称', validType:'length[1,32]'" />
            <input class="easyui-combobox" style="width: 200px;" id="level" name="level" data-options="prompt:'请选择文化馆级别',editable:false,valueField:'id',textField:'text'"/>
            <input class="easyui-combobox" style="width: 200px;" id="state" name="state" data-options="prompt:'请选择状态',editable:false,valueField:'id',textField:'text',data:WhgComm.getBizState()"/>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
        </div>
    </div>
    <!-- 表格操作工具栏-END -->

    <!-- 操作按钮 -->
    <div id="whgdg-opt" style="display: none;">
        <shiro:hasPermission name="${resourceid}:view"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="doSee">查看</a></shiro:hasPermission>
        <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validEdit" method="doEdit">编辑</a></shiro:hasPermission>
        <shiro:hasPermission name="${resourceid}:checkgo"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validCheckGo" method="checkgo">提交审核</a></shiro:hasPermission>
        <shiro:hasPermission name="${resourceid}:checkon"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validCheckOn" method="doCheckTrue">审核通过</a></shiro:hasPermission>
        <shiro:hasPermission name="${resourceid}:checkoff"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validCheckOff" method="doCheckFalse">审核打回</a></shiro:hasPermission>
        <shiro:hasPermission name="${resourceid}:publish"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validPublish"  method="doPublish">发布</a></shiro:hasPermission>
        <shiro:hasPermission name="${resourceid}:publishoff"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validPublishOff"  method="doPublishOff">撤销发布</a></shiro:hasPermission>
        <shiro:hasPermission name="${resourceid}:recovery"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validRecovery" method="doRecovery">回收</a></shiro:hasPermission>
        <shiro:hasPermission name="${resourceid}:undel"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validUndel" method="doUndel">撤销回收</a></shiro:hasPermission>
        <shiro:hasPermission name="${resourceid}:del"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validDel" method="doDel">删除</a></shiro:hasPermission>
    </div>
    <!-- 操作按钮-END -->
</div>

<!-- script -->
<script type="text/javascript">
    //编辑审核发布删除页面
    var pageType = '${pageType}';

    $(function(){
        //初始查询条件-业务状态
        init_condition_state();

        //初始省市区树
        initMassTypeTree();
    });

    /** 级别数据 */
    function getLevelData(){
        return WhgSysData.getStateData("EnumCultLevel");
    }

    /** 格式化级别 */
    function FMTLevel(val){
        return WhgSysData.FMT(val, getLevelData());
    }

    /** 根据编辑审核发布回收页面不则，返回不同的URL */
    function getDatagridUrl(inLevel){
        var url = '';
        if('e' == pageType){
            url = '${basePath}/admin/system/cult/srchList4p?delstate=0&crtuser=${sessionAdminUser.id}&inLevel='+inLevel;
        }else if('c' == pageType){
            url = '${basePath}/admin/system/cult/srchList4p?delstate=0&inState=9,2,4,6&inLevel='+inLevel;
        }else if('p' == pageType){
            url = '${basePath}/admin/system/cult/srchList4p?delstate=0&inState=9,2,4,6&inLevel='+inLevel;
        }else{
            url = '${basePath}/admin/system/cult/srchList4p?delstate=1&inLevel='+inLevel;
        }
        return url;
    }

    //初始查询条件-业务状态
    function init_condition_state() {
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

    //初始tree
    function initMassTypeTree(){
        WhgCommAreaTree.initAreaTree({
            eleId: 'mass_type_tree',
            selectFirstNode: true,
            onSelect: function(node, firstLevel, areaInfo){
                var province = "";
                var city = "";
                var area = "";
                var inLevel = '';
                var levelData = WhgSysData.getStateData("EnumCultLevel").slice();
                if(node.level == '1'){
                    province = node.text;
                    inLevel = '1,2,3';
                }else if(node.level == '2'){
                    city = node.text;
                    inLevel = '2,3';
                    levelData.splice(0,1);
                }else{
                    area = node.text;
                    inLevel = '3';
                    levelData.splice(0,2);
                }
                $('#province').val(province);
                $('#city').val(city);
                $('#area').val(area);
                $('#level').combobox('loadData', levelData);
                WhgComm.search('#whgdg', '#whgdg-tb', getDatagridUrl(inLevel));
            }
        });
    }

    //刷新Tree
    function reloadTree(){
        initMassTypeTree();
    }

    /** 按钮是否可用 */
    function validEdit(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        if(pageType == 'e'){
            return row.state == 1;
        }else if(pageType == 'c'){
            return row.state == 9;
        }else if(pageType == 'p'){
            return row.state == 2 || row.state == 4 /*|| row.state == 6*/;
        }
    }
    function validUp(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return pageType == 'p' &&  row.state == 6;
    }
    function validTop(idx){
        return validUp(idx);
    }
    function validCheckGo(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 1;
    }
    function validCheckOn(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 9;
    }
    function validCheckOff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 9;
    }
    function validPublish(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 2 || row.state == 4;
    }
    function validPublishOff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 6;
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

    /**
     * 添加文化馆
     */
    function doAdd() {
        var node = $('#mass_type_tree').tree('getSelected');
        var areaInfo = getProvinceCityArea(node.text);
        var province = areaInfo.province;
        var city = areaInfo.city;
        var area = areaInfo.area;
        WhgComm.editDialog('${basePath}/admin/system/cult/view/add?province='+encodeURIComponent(province)+'&city='+encodeURIComponent(city)+'&area='+encodeURIComponent(area));
    }

    /**
     * 编辑信息
     * @param idx 行下标
     */
    function doEdit(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/system/cult/view/edit?id='+curRow.id);
    }

    /**
     * 查看资料
     * @param idx 行下标
     */
    function doSee(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/system/cult/view/edit?id='+curRow.id+"&onlyshow=1");
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
            url: getFullUrl('/admin/system/cult/updstate'),
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
     * 送审 [1,5]->9
     * @param idx
     */
    function checkgo(idx){
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        $.messager.confirm("确认信息", "确定提交审核选中的项吗？", function(r){
            if (r){
                _doUpdState(curRow.id, "1,5", 9);
            }
        })
    }


    /**
     * 单个审核通过
     * @param idx 行下标
     */
    function doCheckTrue(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        $.messager.confirm("确认信息", "确定审核通过选中的项吗？", function(r){
            if (r){
                _doUpdState(curRow.id, 9, 2);
            }
        })
    }
    /**
     * 单个审核失败
     * @param idx 行下标
     */
    function doCheckFalse(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        $.messager.confirm("确认信息", "确定审核不通过选中的项吗？", function(r){
            if (r){
                _doUpdState(curRow.id,  "9,2", 1);
            }
        })
    }

    /**
     * 撤销发布
     * @param idx 行下标
     */
    function doPublishOff(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.confirm("确认信息", "确定要撤销发布选中的项吗？", function(){
            if (true){
                _doUpdState(curRow.id, 6, 4);
            }
        })
    }

    /**
     * 发布
     * @param idx 行下标
     */
    function doPublish(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.confirm("确认信息", "确定要发布选中的项吗？", function(){
            if (true){
                _doUpdState(curRow.id, "2,4", 6);
            }
        })
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
                    cache: false,
                    url: getFullUrl('/admin/system/cult/del'),
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

    /**
     * 还原
     * @param idx
     */
    function doUndel(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        $.messager.confirm('提示', '您确定要还原此记录吗？', function(r){
            if (r){
                $.ajax({
                    type: "POST",
                    cache: false,
                    url: getFullUrl('/admin/system/cult/undel'),
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

    /**
     * 回收
     * @param idx
     */
    function doRecovery(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", '确定要回收选中的数据吗？',function () {
            $.post('${basePath}/admin/system/cult/del', {ids: row.id}, function(data){
                $("#whgdg").datagrid('reload');
                if (!data.success || data.success != "1"){
                    $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                }
            }, 'json');
        });
    }

    /**
     * 上移
     * @param idx
     */
    function doUp(idx){
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        $.ajax({
            type: "POST",
            cache: false,
            url: getFullUrl('/admin/system/cult/sort'),
            data: {id : curRow.id, type:'up'},
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

    /**
     * 置顶
     * @param idx
     */
    function doTop(idx){
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        $.ajax({
            type: "POST",
            cache: false,
            url: getFullUrl('/admin/system/cult/sort'),
            data: {id : curRow.id, type:'top'},
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

    /**
     * 是否可以上移和置顶
     * @param idx
     * @returns {boolean}
     */
    function canSort(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        if(curRow.idx != '1'){
            return true;
        }
        return false;
    }

    /**
     * 是否可以停止
     * @param idx
     * @returns {boolean}
     */
    function canStop(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        if(curRow.state == 1){
            return true;
        }
        return false;
    }

    /**
     * 是否可以启用
     * 后台直接添加 和 前台申请 后台审核通过 都可以 启用
     * @param idx
     * @returns {boolean}
     */
    function canStart(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        if(curRow.sqstate!=null) {
            if(curRow.state!=1&&curRow.sqstate == 2){
                return true;
            }
        }else{//后台直接添加
            if(curRow.state == 0){
                return true;
            }
        }
        return false;
    }
</script>
<!-- script END -->
</body>
</html>