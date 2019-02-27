<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>活动管理</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
    <script src="${basePath}/static/admin/js/whgmodule-gridopts.js"></script>
    <c:choose>
        <c:when test="${type eq 'editList'}">
            <c:set var="pageTitle" value="活动编辑列表"></c:set>
            <c:set var="typevalue" value="1"></c:set>
        </c:when>
        <c:when test="${type eq 'check'}">
            <c:set var="pageTitle" value="活动审核列表"></c:set>
            <c:set var="typevalue" value="9"></c:set>
        </c:when>
        <c:when test="${type eq 'publish'}">
            <c:set var="pageTitle" value="活动发布列表"></c:set>
            <c:set var="typevalue" value="2"></c:set>
        </c:when>
        <c:when test="${type eq 'del'}">
            <c:set var="pageTitle" value="回收站"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="活动信息"></c:set>
        </c:otherwise>
    </c:choose>

    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
</head>
<body class="easyui-layout">
<div data-options="region:'west',title:'区域',split:true"
     style="width:250px;padding:10px;">
    <ul class="easyui-tree" id="mass_type_tree"></ul>
</div>
<div data-options="region:'center',title:'活动列表'">
    <!-- 表格 -->
    <table id="whgdg" class="easyui-datagrid" style="display: none"
           data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb'">
        <thead>
        <tr>
            <th data-options="field:'name', sortable: true<%--,width:120--%>">名称</th>
            <th data-options="field:'cultname',width:120">所属文化馆</th>
            <th data-options="field:'starttime', <%--width:80,--%>sortable: true, formatter:WhgComm.FMTDate ">开始时间</th>
            <th data-options="field:'endtime',<%-- width:80,--%>sortable: true, formatter:WhgComm.FMTDate ">结束时间</th>
            <th data-options="field:'telphone',<%--, width:100--%>">联系手机</th>
            <th data-options="field:'address',<%--, width:120--%>">地址</th>
            <th data-options="field:'crtuser',width:120,formatter:WhgComm.FMTUserName">编辑者</th>
            <th data-options="field:'checkor',width:120,formatter:WhgComm.FMTUserName">审核者</th>
            <th data-options="field:'publisher',width:120,formatter:WhgComm.FMTUserName">发布者</th>
            <th data-options="field:'statemdfdate',width:130,sortable: true,<%-- width:150,--%> formatter:WhgComm.FMTDateTime ">
                操作时间
            </th>
            <th data-options="field:'state', width:50,<%--width:60,--%> formatter:WhgComm.FMTBizState">状态</th>
            <th data-options="field:'_opt', <%--width:540,--%> formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
        </tr>
        </thead>
    </table>
    <!-- 表格 END -->

    <!-- 表格操作工具栏 -->
    <div id="whgdg-tb" style="display: none;">
        <div class="whgd-gtb-btn">
            <shiro:hasPermission name="${resourceid}:add"><a href="javascript:void(0)" class="easyui-linkbutton"
                                                             iconCls="icon-add"
                                                             onclick="doAdd();">添加活动</a></shiro:hasPermission>
        </div>
        <div class="whgdg-tb-srch">
            <input class="easyui-textbox" style="width: 200px;" name="name"
                   data-options="prompt:'请输入活动名称', validType:'length[1,32]'"/>
                    <input class="easyui-combobox" style="width: 200px;" name="state" panelHeight="auto"
                           limitToList="true"
                           data-options="prompt:'请选择状态', value:'${typevalue}', valueField:'id', textField:'text', data:WhgComm.getBizState('1,9,2')"/>
            <input type="hidden" name="cultid" id="cultid"/>
            <input type="hidden" name="cultname" id="cultname"/>
            <input type="hidden" name="cultlevel" id="cultlevel"/>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search"
               onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
        </div>
    </div>
    <!-- 表格操作工具栏-END -->

    <!-- 操作按钮 -->
    <div id="whgdg-opt" style="display: none;">
        <shiro:hasPermission name="${resourceid}:view"><a href="javascript:void(0)" class="easyui-linkbutton"
                                                          plain="true" method="doSee">查看</a></shiro:hasPermission>
        <%--   <shiro:hasPermission name="${resourceid}:publish"> <a href="javascript:void(0)" class="easyui-linkbutton"
                                                                 plain="true" validKey="state" validVal="2,4"
                                                                 method="doPublish">发布</a></shiro:hasPermission>--%>
        <shiro:hasPermission name="${resourceid}:publishoff">
            <a href="javascript:void(0)" class="easyui-linkbutton"
                                                                plain="true" validKey="state" validVal="6"
                                                                method="publishoff">撤销发布</a></shiro:hasPermission>

        <shiro:hasPermission name="${resourceid}:rce2province">
            <a plain="true" method="whgListTool.toprovince" validKey="toprovince" validVal="0">省级推荐</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="${resourceid}:unrce2province">
            <a plain="true" method="whgListTool.untoprovince" validKey="toprovince" validVal="1">取消省级推荐</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="${resourceid}:rce2city">
            <a plain="true" method="whgListTool.tocity" validKey="tocity" validVal="0">市级推荐</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="${resourceid}:unrce2city">
            <a plain="true" method="whgListTool.untocity" validKey="tocity" validVal="1">取消市级推荐</a>
        </shiro:hasPermission>
    </div>
    <!-- 操作按钮-END -->

    <!--发布设置发布时间表单 -->
    <div id="whgwin-add" style="display: none">
        <form id="whgff" class="whgff" method="post">
            <div class="whgff-row">
                <div class="whgff-row-label _check" style="width: 30%"><i>*</i>发布时间：</div>
                <div class="whgff-row-input" style="width: 70%">
                    <input class="easyui-datetimebox statemdfdate" name="statemdfdate" style="width:200px; height:32px"
                           data-options="required:true">
                </div>
            </div>
        </form>
    </div>

    <!--下架时填写下架原因表单 -->
    <div id="whgwin-xj" style="display: none">
        <form id="whgffxj" class="whgff" method="post">
            <div class="whgff-row">
                <div class="whgff-row-label _check" style="width: 30%"><i>*</i>下架原因：</div>
                <div class="whgff-row-input" style="width: 70%">
                    <input class="easyui-textbox" name="reason" id="reason" value="" multiline="true"
                           style="width:250px;height: 100px;"
                           data-options="required:true,validType:['length[1,400]']">
                </div>
            </div>
            <div class="whgff-row">
                <div class="whgff-row-label _check" style="width: 30%"><i>*</i>是否发送短信：</div>
                <div class="whgff-row-input" style="width: 70%">
                    <div class="radio radio-primary whg-js-data" name="issms" id="issms"
                         js-data='[{"id":"1","text":"是"},{"id":"0","text":"否"}]'>
                    </div>
                </div>
            </div>

        </form>
    </div>


    <div id="whgwin-add-btn" style="text-align: center; display: none">
        <div style="display: inline-block; margin: 0 auto">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="btn">确 定</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
               onclick="$('#whgwin-add').dialog('close')">关 闭</a>
        </div>
    </div>
    <div id="whgwin-xj-btn" style="text-align: center; display: none">
        <div style="display: inline-block;">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="xjbtn">确 定</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
               onclick="$('#whgwin-xj').dialog('close')">关 闭</a>
        </div>
    </div>
</div>
<!-- 发布设置发布时间表单 END -->

<!-- script -->
<script type="text/javascript">
    var whgListTool = new Gridopts({pageType: "${viewType}", pageTypeS:'list2'});

    $(function () {
        WhgCommAreaTree.initAreaTree({
            eleId: 'mass_type_tree', loadCult: true, selectFirstNode: true, onSelect: function (node) {
                $('#cultid').val("");
                $('#cultname').val("");
                $('#cultlevel').val("");
                if (node.iscult && node.iscult == 1) {
                    $('#cultid').val(node.id);
                } else {
                    if (node.level && node.level != "") {
                        $('#cultlevel').val(node.level);
                    }
                    $('#cultname').val(node.text);
                }
                $('#addBtn').linkbutton('enable');
                $('#queryBtn').linkbutton('enable');
                WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/activity/srchList4p?__pageType=${type}');
            }
        });
        // WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/activity/srchList4p?__pageType=${type}');
    });

    function validRecommendOn(idx) {
        var is = false;
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        if (curRow.state == '6' && curRow.isrecommend != '1') {
            is = true;
        }
        return is;
    }

    function validSend(idx) {
        var is = false;
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        if (curRow.state == '1' && curRow.tempstorage == '0') {
            is = true;
        }
        return is;
    }

    function validRecommendOff(idx) {
        var is = false;
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        if (curRow.state == '6' && curRow.isrecommend == '1') {
            is = true;
        }
        return is;
    }

    function validBannerOn(idx) {
        var is = false;
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        if (curRow.state == '6' && curRow.isbigbanner != '1') {
            is = true;
        }
        return is;
    }

    function validBannerOff(idx) {
        var is = false;
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        if (curRow.state == '6' && curRow.isbigbanner == '1') {
            is = true;
        }
        return is;
    }

    /**
     * 添加活动
     */
    function doAdd() {
        WhgComm.editDialog('${basePath}/admin/activity/view/add');
        //window.parent.$("a[title='添加活动']").click();
    }

    /**
     * 编辑信息
     * @param idx 行下标
     */
    function doEdit(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/activity/view/edit?id=' + curRow.id + '&pageType=${type}');
    }

    function doUpdateScreenings(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/activity/view/screenings?id=' + curRow.id);
    }

    /**
     * 查看资料
     * @param idx 行下标
     */
    function doSee(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/activity/view/edit?id=' + curRow.id + "&onlyshow=1");
    }

    /**
     * 送审 [1,5]->9
     * @param idx
     */
    function doCheckgo(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "确定要送审选中的项吗？", function () {
            __updStateSend(row.id, "1", 9);
        })
    }

    /**
     * 审通过 [9]->2
     * @param idx
     */
    function doCheckOn(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "确定要审核选中的项吗？", function () {
            __updStateSend(row.id, 9, 2);
        })
    }

    /**
     * 审不通过 [9]->1
     * @param idx
     */
    function doCheckOff(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "确定要打回选中的项吗？", function () {
            __updStateSend(row.id, 9, 1);
        })
    }

    /**
     * 发布 [2,4]->6
     * @param idx
     */
    function doPublish(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        $('#whgwin-add').dialog({
            title: '设置发布时间',
            cache: false,
            modal: true,
            width: '400',
            height: '150',
            maximizable: true,
            resizable: true,
            buttons: '#whgwin-add-btn',
            onOpen: function () {
                $(".statemdfdate").datetimebox('setValue', new Date().Format('yyyy-MM-dd hh:mm:ss'));
                $('#whgff').form({
                    url: '${basePath}/admin/activity/updstate',
                    onSubmit: function (param) {
                        param.ids = row.id;
                        if (2 == row.state) {
                            param.fromState = 2;
                        } else if (4 == row.state) {
                            param.fromState = 4;
                        }
                        //param.fromState = "2,4";
                        param.toState = 6;
                        var isValid = $(this).form('enableValidation').form('validate');
                        if (isValid) {
                            $.messager.progress();
                        } else {
                            $("#btn").off("click").one("click", function () {
                                $('#whgff').submit();
                            });
                        }
                        return isValid;
                    },
                    success: function (data) {
                        $.messager.progress('close');
                        var Json = $.parseJSON(data);
                        if (Json.success == "1") {
                            $('#whgdg').datagrid('reload');
                            $('#whgwin-add').dialog('close');
                        } else {
                            $.messager.alert("提示", data.errorMsg);
                        }
                    }
                });
                $("#btn").off("click").one("click", function () {
                    $('#whgff').submit();
                });
            }
        })
        /*WhgComm.confirm("确认信息", "确定要发布选中的项吗？", function(r){
         if (r){
         if(2 == row.state){
         __updStateSend(row.id, 2, 6);
         }else if(4 == row.state){
         __updStateSend(row.id, 4, 6);
         }
         }
         })*/
    }

    /**
     * 取消发布 [6]->4
     * @param idx
     */
    function publishoff(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        $('#whgwin-xj').dialog({
            title: '设置下架原因',
            cache: false,
            modal: true,
            width: '400',
            height: '350',
            maximizable: true,
            resizable: true,
            buttons: '#whgwin-xj-btn',
            onOpen: function () {
                $('#whgffxj').form({
                    url: '${basePath}/admin/activity/updstate',
                    onSubmit: function (param) {
                        param.ids = row.id;
                        param.fromState = 6;
                        param.toState = 4;
                        var isValid = $(this).form('enableValidation').form('validate');
                        if (isValid) {
                            $.messager.progress();
                        } else {
                            $("#xjbtn").off("click").one("click", function () {
                                $('#whgffxj').submit();
                            });
                        }
                        return isValid;
                    },
                    success: function (data) {
                        $.messager.progress('close');
                        var Json = $.parseJSON(data);
                        if (Json.success == "1") {
                            $('#whgdg').datagrid('reload');
                            $('#whgwin-xj').dialog('close');
                        } else {
                            $.messager.alert("提示", data.errorMsg);
                        }
                    }
                });
                $("#xjbtn").off("click").one("click", function () {
                    $('#whgffxj').submit();
                });
            }
        })


        /* var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "确定要取消发布选中的项吗？", function () {
            __updStateSend(row.id, 6, 4);//原来是1 回到编辑列表 现在改成4 下架列表
         })*/
    }

    /**
     * 打回编辑 [6]->1
     * @param idx
     */
    function reBackEdit(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        var fromState = row.state;
        WhgComm.confirm("确认信息", "确定要打回编辑选中的项吗？", function () {
            __updStateSend(row.id, fromState, 1);
        })
    }


    /**
     * 状态变更（送审、审核、发布、推荐...操作状态变更）
     * @param idx
     */
    function __updStateSend(ids, fromState, toState) {
        $.messager.progress();
        var params = {ids: ids, fromState: fromState, toState: toState};
        $.post('${basePath}/admin/activity/updstate', params, function (data) {
            $("#whgdg").datagrid('reload');
            if (!data.success || data.success != "1") {
                $.messager.alert("错误", data.errormsg || '操作失败', 'error');
            }
            $.messager.progress('close');
        }, 'json');
    }

    /**
     * 回收
     * @param idx
     */
    function doRecovery(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.confirm('提示', '您确定回收此记录吗？', function () {
            $.ajax({
                type: "POST",
                cache: false,
                url: getFullUrl('/admin/activity/recovery'),
                data: {ids: curRow.id, delStatus: 1},
                success: function (Json) {
                    if (Json && Json.success == '1') {
                        $('#whgdg').datagrid('reload');
                    } else {
                        $.messager.alert('提示', '操作失败:' + Json.errormsg + '！', 'error');
                    }
                }
            });
        });
    }

    /**
     * 删除
     * @param idx
     */
    function doDel(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.confirm('提示', '此操作将会永久性删除数据，确定要删除此记录吗？', function () {
            $.ajax({
                type: "POST",
                cache: false,
                url: getFullUrl('/admin/activity/del'),
                data: {ids: curRow.id, delStatus: 1},
                success: function (Json) {
                    if (Json && Json.success == '1') {
                        $('#whgdg').datagrid('reload');
                    } else {
                        $.messager.alert('提示', '操作失败:' + Json.errormsg + '！', 'error');
                    }
                }
            });
        });
    }
    /**
     * 还原
     * @param idx
     */
    function reBack(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.confirm('提示', '您确定要还原此记录吗？', function () {
            $.ajax({
                type: "POST",
                cache: false,
                url: getFullUrl('/admin/activity/reBack'),
                data: {ids: curRow.id, delStatus: 0},
                success: function (Json) {
                    if (Json && Json.success == '1') {
                        $('#whgdg').datagrid('reload');
                    } else {
                        $.messager.alert('提示', '操作失败:' + Json.errormsg + '！', 'error');
                    }
                }
            });
        });
    }

    /**
     * 资源管理
     * */
    function doResource(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        var cultid = $('#cultid').combobox('getValue');
        WhgComm.editDialog('${basePath}/admin/resource/view/list?reftype=4&refid=' + curRow.id + '&cultid=' + cultid);
    }

    /**
     * 订单管理
     *
     * */
    function doOrder(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/activity/view/orderList?reftype=2&id=' + curRow.id);
    }

    /**
     * 推荐状态变更
     * 0-1
     * @param idx
     */
    function _updCommend(ids, fromState, toState) {
        $.messager.progress();
        var params = {ids: ids, fromState: fromState, toState: toState};
        $.post('${basePath}/admin/activity/updCommend', params, function (data) {
            $("#whgdg").datagrid('reload');
            if (!data.success || data.success != "1") {
                $.messager.alert("错误", data.errormsg || '操作失败', 'error');
            }
            $.messager.progress('close');
        }, 'json');
    }
    /**
     * 推荐状态变更
     * 0-1
     * @param idx
     */
    function _updBanner(ids, fromState, toState) {
        $.messager.progress();
        var params = {ids: ids, fromState: fromState, toState: toState};
        $.post('${basePath}/admin/activity/updBanner', params, function (data) {
            $("#whgdg").datagrid('reload');
            if (!data.success || data.success != "1") {
                $.messager.alert("错误", data.errormsg || '操作失败', 'error');
            }
            $.messager.progress('close');
        }, 'json');
    }

    /**
     * 推荐状态变更 [0]->1
     * @param idx
     */
    function doCommend(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "确定要推荐选中的项吗？", function () {
            _updCommend(row.id, 0, 1);
        })
    }
    /**
     * 推荐状态变更 [0]->1
     * @param idx
     */
    function doBigBanner(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "确定要推荐选中的项吗？", function () {
            _updBanner(row.id, 0, 1);
        })
    }
    /**
     * 推荐状态变更 [1]->0
     * @param idx
     */
    function commendOff(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "确定要取消推荐选中的项吗？", function () {
            _updCommend(row.id, 1, 0);
        })
    }
    /**
     * 推荐状态变更 [1]->0
     * @param idx
     */
    function bigBannerOff(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "确定要取消推荐选中的项吗？", function () {
            _updBanner(row.id, 1, 0);
        })
    }

    /**
     * 资讯公告
     * @param idx
     */
    function info(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/info/view/list?entityid=' + row.id + '&entity=2&cultid=' + row.cultid + '&deptid=' + row.deptid);
    }
</script>
<!-- script END -->
</body>
</html>