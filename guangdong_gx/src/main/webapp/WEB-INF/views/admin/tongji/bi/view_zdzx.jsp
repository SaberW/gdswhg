<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>站点资讯统计</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <style>
        .superUser{
            color: #f5f5f5;
            background-color: #FF5722;
            display: inline-block;
            margin-right: 5px;
        }
        .normalUser{
            color: #f5f5f5;
            background-color: #00BCD4;
            display: inline-block;
            margin-right: 5px;
        }
    </style>
    <script type="text/javascript">
        /** 级别数据 */
        function getLevelData(){
            return WhgSysData.getStateData("EnumCultLevel");
        }

        /** 格式化级别 */
        function FMTLevel(val){
            return WhgSysData.FMT(val, getLevelData());
        }

        /** 可查询时间范围 */
        function getTimeScopeData(){
            return [{id:'1', text:'本月'}, {id:'2', text:'本年'}, {id:'3', text:'自定义'}];
        }

        /** 切换时间范围 */
        function changeTimeScope(newVal, oldVal){
            if(newVal == '1' || newVal == '2' || newVal == ''){
                $('#startTime').datebox('clear');
                $('#endTime').datebox('clear');
                $('#startTimeSpan').hide();
                $('#endTimeSpan').hide();
                $('#timeSpan').hide();
            }else {
                $('#startTime').datebox('clear');
                $('#endTime').datebox('clear');
                $('#startTimeSpan').show();
                $('#endTimeSpan').show();
                $('#timeSpan').show();
            }
        }
    </script>
</head>
<body class="easyui-layout">
<div data-options="region:'west',title:'站点范围',collapsible:false,split:true,tools:[{iconCls:'icon-reload', handler:reloadTree}]" style="width:250px;padding:10px;">
    <ul class="easyui-tree" id="mass_type_tree"></ul>
</div>
<div data-options="region:'center',title:'站点资讯统计列表'">
    <!-- 表格 -->
    <table id="whgdg" class="easyui-datagrid" style="display: none"
           data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:true, pagination:true, toolbar:'#whgdg-tb', onLoadSuccess:loadSuccess, pageSize:50, pageList:[50,100,200]">
        <thead>
        <tr>
            <th data-options="field:'name', width:350">文化馆名称</th>
            <th data-options="field:'total', width:200, sortable:true">资讯总数</th>
            <%--<th data-options="field:'views', width:100, sortable:true">资讯总浏览数</th>--%>
        </tr>
        </thead>
    </table>
    <!-- 表格 END -->

    <!-- 表格操作工具栏 -->
    <div id="whgdg-tb" style="display: none;">
        <div class="whgdg-tb-srch">
            <input type="hidden" id="cultId" name="cultId" value="" />
            <input type="hidden" id="province" name="province" value="" />
            <input type="hidden" id="city" name="city" value="" />
            <input type="hidden" id="area" name="area" value="" />
            <input class="easyui-textbox" style="width: 200px;" id="cultName" name="cultName" data-options="prompt:'请输入文化馆名称', validType:'length[1,32]'" />
            <input class="easyui-combobox" style="width: 200px;" id="cultLevel" name="cultLevel" data-options="prompt:'请选择文化馆级别',editable:false, valueField:'id',textField:'text', data:getLevelData()"/>
            <input class="easyui-combobox" style="width: 200px;" id="timeScope" name="timeScope" data-options="prompt:'请选择时间范围',editable:false, value:'1', valueField:'id', textField:'text', data:getTimeScopeData(), onChange:changeTimeScope"/>
            <span style="display: none" id="startTimeSpan"><input class="easyui-datebox" style="width: 200px;" id="startTime" name="startTime" data-options="prompt:'开始时间'"/></span>
            <span style="display: none" id="timeSpan">-</span>
            <span style="display: none" id="endTimeSpan"><input class="easyui-datebox" style="width: 200px;" id="endTime" name="endTime" data-options="prompt:'结束时间'"/></span>

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" id="queryBtn" data-options="disabled:true" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
        </div>
    </div>
    <!-- 表格操作工具栏-END -->
</div>

<!-- script -->
<script type="text/javascript">
    //初始tree
    function initMassTypeTree(){
        WhgCommAreaTree.initAreaTree({
            eleId:'mass_type_tree',
            loadCult: true,
            selectFirstNode: true,
            onSelect: function(node, firstLevel, areaInfo){
                if(typeof(node.iscult) != 'undefined' && node.iscult == '1'){//选择文化馆
                    $('#cultId').val(node.id);
                    $('#cultName').textbox('clear');
                    $('#cultLevel').combobox('clear');
                    $('#province').val('');
                    $('#city').val('');
                    $('#area').val('');
                    $('#queryBtn').linkbutton('enable');
                    WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/tongji/bi/zdzx');
                }else{//选择省市区
                    $('#cultId').val('');
                    //$('#cultName').textbox('clear');
                    $('#cultLevel').combobox('setValue', areaInfo.level);
                    $('#province').val(areaInfo.province);
                    $('#city').val(areaInfo.city);
                    $('#area').val(areaInfo.area);
                    $('#queryBtn').linkbutton('enable');
                    WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/tongji/bi/zdzx');
                }
            }
        });
    }

    //刷新Tree
    function reloadTree(){
        $('#queryBtn').linkbutton('disable');
        initMassTypeTree();
    }

    //表格加载成功之后
    function loadSuccess() {
        //添加“合计”列
        var total_total = compute("total");
        var total_views = compute("views");

        $('#whgdg').datagrid('appendRow', {
            'name': '<span class="subtotal" style="float: right;">合计</span>',
            'total': '<span class="subtotal">' + total_total + '</span>'/*,
            'views': '<span class="subtotal">' + total_views + '</span>'*/
        });
    }

    //指定列求和
    function compute(colName) {
        var rows = $('#whgdg').datagrid('getRows');
        var total = 0;
        for (var i = 0; i < rows.length; i++) {
            total += parseFloat(rows[i][colName]);
        }
        return total;
    }

    //页面装载完成后处理
    $(function () {
        initMassTypeTree();
    });
</script>
<!-- script END -->
</body>
</html>