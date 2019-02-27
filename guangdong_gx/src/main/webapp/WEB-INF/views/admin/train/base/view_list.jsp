<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid"));
    System.out.println("rsid:" + request.getParameter("rsid"));
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <c:choose>
    <c:when test="${type eq 'edit'}">
        <c:set var="pageTitle" value="培训编辑列表"></c:set>
        <c:set var="pageValue" value="1"></c:set>
    </c:when>
    <c:when test="${type eq 'check'}">
        <c:set var="pageTitle" value="培训审核列表"></c:set>
        <c:set var="pageValue" value="9"></c:set>
    </c:when>
    <c:when test="${type eq 'publish'}">
        <c:set var="pageTitle" value="培训发布列表"></c:set>
        <c:set var="pageValue" value="2"></c:set>
    </c:when>
    <c:when test="${type eq 'del'}">
        <c:set var="pageTitle" value="回收站"></c:set>
    </c:when>
    <c:otherwise>
        <c:set var="pageTitle" value="培训信息"></c:set>
    </c:otherwise>
    </c:choose>

    <title>${pageTitle}</title>

    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>
</head>
<body>
<jsp:include page="../../fkproject/fkproject_condition.jsp"/>
<!-- 表格 -->
<table id="whgdg" title="${pageTitle}" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb'">
    <thead>
    <tr>
        <%--<th data-options="width: 30, checkbox: true, field:'checkbox' ">全选</th>--%>
        <th data-options="field:'_opt_kc',formatter:WhgComm.FMTOpt,fixed:true, optDivId:'whgdg-opt-kc'">操作</th>
        <th data-options="field:'title'<%--, width:100--%>">培训标题</th>
        <th data-options="field:'etype',width:120, formatter:WhgComm.FMTTrainType">培训类型</th>
        <th data-options="field:'isterm', width:50, formatter:yesNoFMT">学期制</th>
        <th data-options="field:'ismultisite', width:120, formatter:typeFMT">场次类别</th>
        <th data-options="field:'crtdate', width:130, formatter:WhgComm.FMTDateTime">创建时间</th>
        <th data-options="field:'crtuser',width:120, formatter:WhgComm.FMTUserName ">编辑者</th>
        <th data-options="field:'checkor',width:120, formatter:WhgComm.FMTUserName ">审核者</th>
        <th data-options="field:'publisher',width:120, formatter:WhgComm.FMTUserName ">发布者</th>
        <th data-options="width:130, sortable: true, field:'statemdfdate', formatter:WhgComm.FMTDateTime ">操作时间</th>
        <th data-options="field:'state',width:50, formatter:WhgComm.FMTBizState" >状态</th>
        <th data-options="field:'_opt',formatter:WhgComm.FMTOpt,fixed:true, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <c:if test="${type eq 'edit'}">
        <shiro:hasPermission name="${resourceid}:add">
            <div class="whgd-gtb-btn">
                <a class="easyui-linkbutton" iconCls="icon-add" onclick="add()">添 加</a>
            </div>
        </shiro:hasPermission>
    </c:if>
    <div class="whgdg-tb-srch">
        <input class="easyui-combobox" name="cultid" id="cultid" data-options="editable:false,width:150" prompt="请选择文化馆"/>
        <input class="easyui-combobox" name="deptid" id="deptid" data-options="editable:false,width:150" prompt="请选择部门"/>
        <input class="easyui-textbox" style="width: 200px;" name="title" data-options="prompt:'请输入培训名称', validType:'length[1,32]'" />

        <c:choose>
            <c:when test="${type eq 'edit'}">
                <select class="easyui-combobox" name="state" prompt="请选择状态" panelHeight="auto" limitToList="true" data-options="editable:false,width:110, value:'${pageValue}', valueField:'id', textField:'text', data:WhgComm.getBizState()"></select>
            </c:when>
            <c:when test="${type eq 'del'}"></c:when>
            <c:otherwise>
                <select class="easyui-combobox" name="state" prompt="请选择状态" panelHeight="auto" limitToList="true" data-options="editable:false,width:110, value:'${pageValue}', valueField:'id', textField:'text', data:WhgComm.getBizState('1')"></select>
            </c:otherwise>
        </c:choose>

        <c:if test="${type == 'publish'}">
            <input class="easyui-combobox" name="recommend" prompt="是否推荐" style="width:110px" validType="notQuotes" data-options="editable:false,valueField:'id',textField:'text', data:[{id: '0',text: '未推荐'},{id: '1',text: '已推荐'}]"/>
        </c:if>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<div id="whgdg-opt-kc" style="display: none;">
    <c:choose>
        <c:when test="${type eq 'edit'}">
            <shiro:hasPermission name="${resourceid}:kecheng"><a href="javascript:void(0)" class="easyui-linkbutton" style="color: #0e0e0e; font-weight: bold" validKey="state" validVal="1" plain="true" method="course">课程管理</a></shiro:hasPermission>
        </c:when>
        <c:when test="${type eq 'check'}">
            <shiro:hasPermission name="${resourceid}:kecheng"><a href="javascript:void(0)" class="easyui-linkbutton" style="color: #0e0e0e; font-weight: bold" validKey="state" validVal="9" plain="true" method="course">课程管理</a></shiro:hasPermission>
        </c:when>
        <c:when test="${type eq 'publish'}">
            <shiro:hasPermission name="${resourceid}:kecheng"><a href="javascript:void(0)" class="easyui-linkbutton" style="color: #0e0e0e; font-weight: bold" validKey="state" validVal="2,4,6" plain="true" method="course">课程管理</a></shiro:hasPermission>
        </c:when>
    </c:choose>
</div>


<div id="whgdg-opt" style="display: none;">
    <shiro:hasPermission name="${resourceid}:view"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="view">查看</a></shiro:hasPermission>
    <c:choose>
        <c:when test="${type eq 'edit'}">
            <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="1" plain="true" method="edit">编辑</a>
                <a plain="true" validKey="state" validVal="1" method="setSmsGroup">设置短信组</a>
            </shiro:hasPermission>
            <%--<shiro:hasPermission name="${resourceid}:kecheng"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="1" plain="true" method="course">课程管理</a></shiro:hasPermission>--%>
            <shiro:hasPermission name="${resourceid}:zlsc"><a href="javascript:void(0)"
                                                              class="easyui-linkbutton uploadFile" validVal="1"
                                                              method="addzy">上传资料</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:pxzx"><a href="javascript:void(0)" class="easyui-linkbutton"
                                                              validKey="state" validVal="1"
                                                              method="info">培训资讯</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:ziyuan"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="1" plain="true" method="resource">资源管理</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:checkgo"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="1" plain="true" method="checkgo">提交审核</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:del"> <a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="1" plain="true" method="del">删除</a></shiro:hasPermission>

        </c:when>
        <c:when test="${type eq 'check'}">
            <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="9" plain="true" method="edit">编辑</a>
                <a plain="true" validKey="state" validVal="9" method="setSmsGroup">设置短信组</a>
            </shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:zlsc"><a href="javascript:void(0)"
                                                              class="easyui-linkbutton uploadFile" validVal="9"
                                                              method="addzy">上传资料</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:pxzx"><a href="javascript:void(0)" class="easyui-linkbutton"
                                                              validKey="state" validVal="9"
                                                              method="info">培训资讯</a></shiro:hasPermission>
            <%--<shiro:hasPermission name="${resourceid}:kecheng"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="9" plain="true" method="course">课程管理</a></shiro:hasPermission>--%>
            <shiro:hasPermission name="${resourceid}:ziyuan"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="9" plain="true" method="resource">资源管理</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:checkon"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="9" plain="true" method="checkon">审核通过</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:checkoff"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="9" plain="true" method="checkoff">审核打回</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:recovery"> <a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="9" plain="true" method="del">回收</a></shiro:hasPermission>
        </c:when>
        <c:when test="${type eq 'publish'}">
            <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="2,4" plain="true" method="edit">编辑</a>
                <a plain="true" validKey="state" validVal="2,4" method="setSmsGroup">设置短信组</a>
            </shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:zlsc"><a href="javascript:void(0)" class="easyui-linkbutton uploadFile" validVal="2,4" method="addzy">上传资料</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:pxzx"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="2,4,6" method="info">培训资讯</a></shiro:hasPermission>
            <%--<shiro:hasPermission name="${resourceid}:kecheng"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="2,4" plain="true" method="course">课程管理</a></shiro:hasPermission>--%>
            <shiro:hasPermission name="${resourceid}:ziyuan"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="2,4,6" plain="true" method="resource">资源管理</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:baoming"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="6" plain="true" method="enroll">报名管理</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:publish"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="2,4" plain="true" method="publish">发布</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:publishoff"><a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="6" plain="true" method="publishoff">撤销发布</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:recommend"><a href="javascript:void(0)" class="easyui-linkbutton" validFun="_recommend" plain="true" method="recommend">推荐</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:recommendoff"><a href="javascript:void(0)" class="easyui-linkbutton" validFun="_recommendoff" plain="true" method="recommendoff">撤销推荐</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:recovery"> <a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="2" plain="true" method="del">回收</a></shiro:hasPermission>
        </c:when>
        <c:when test="${type eq 'del'}">
            <shiro:hasPermission name="${resourceid}:del"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="del">删除</a></shiro:hasPermission>
            <shiro:hasPermission name="${resourceid}:undel"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="undel">撤销回收</a></shiro:hasPermission>
        </c:when>
    </c:choose>
</div>
<!-- 操作按钮-END -->

<!--发布设置发布时间表单 -->
<div id="whgwin-add" style="display: none">
    <form id="whgff" class="whgff" method="post">
        <div class="whgff-row">
            <div class="whgff-row-label _check" style="width: 30%"><i>*</i>发布时间：</div>
            <div class="whgff-row-input" style="width: 70%">
                <input class="easyui-datetimebox statemdfdate" name="statemdfdate" style="width:200px; height:32px" data-options="required:true">
            </div>
        </div>
    </form>
</div>
<div id="whgwin-add-btn" style="text-align: center; display: none">
    <div style="display: inline-block; margin: 0 auto">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="btn" >确 定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#whgwin-add').dialog('close')">关 闭</a>
    </div>
</div>
<!-- 发布设置发布时间表单 END -->



<script>
    $(function(){

        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'',allcult: true,
            deptEid:'deptid', deptValue:'',alldept: true
        });

        WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/train/srchList4p?type=${type}');
    });

    function typeFMT(val, rowData, index){
        switch(val){
            case 0 : return "单场";
            case 1 : return "多场";
            case 2 : return "固定场";
            default : return val;
        }
    }

    function _recommendoff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 6 && row.recommend == 1;
    }
    function _upindexon(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 6 && row.upindex == 0;
    }
    function _upindexoff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 6 && row.upindex == 1;
    }
    function _recommend(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 6 && row.recommend == 0;
    }


    //资料上传
    function addzy(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/infoupload/uploads/view/list?rsid=${resourceid}&refid=' + row.id);
    }

    /**
     * 添加
     */
    function add(){
        WhgComm.editDialog('${basePath}/admin/train/view/add');
    }

    /**
     * 编辑
     * @param idx
     */
    function edit(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/train/view/add?id='+row.id);
    }

    function setSmsGroup(idx){
        var row = $('#whgdg').datagrid('getRows')[idx];
        var url = '${basePath}/admin/yunwei/aliysmsgroup/refuse/view/edit?enttype=5&entid='+row.id;
        WhgComm.openDialog4size('设置短信组', url, 550, 280);
    }

    /**
     * 查看
     * @param idx
     */
    function view(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/train/view/add?targetShow=1&id='+row.id);
    }
    /**
     * 推荐
     * @param idx
     */
    function recommend(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "确定要推荐选中的项吗？", function(){
            __updrecommend(row.id, 0, 1);
        })
    }
    /**
     * 取消推荐
     * @param idx
     */
    function recommendoff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "确定要取消推荐选中的项吗？", function(){
            __updrecommend(row.id, 1, 0);
        })
    }
    /**
     * 推荐提交
     */
    function __updrecommend(ids, formrecoms, torecom) {
        $.messager.progress();
        var params = {ids: ids, formrecoms: formrecoms, torecom: torecom};
        $.post('${basePath}/admin/train/updrecommend', params, function(data){
            $("#whgdg").datagrid('reload');
            if (!data.success || data.success != "1"){
                $.messager.alert("错误", data.errormsg||'操作失败', 'error');
            }
            $.messager.progress('close');
        }, 'json');
    }

    /**
     * 上首页
     * @param idx
     */
    function upindex(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "确定要将选中的项推上首页吗？", function(){
            __upindex(row.id, 0, 1);
        })
    }
    /**
     * 取消上首页
     * @param idx
     */
    function noupindex(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "确定要将选中的项取消推上首页吗？", function(){
            __upindex(row.id, 1, 0);
        })
    }
    /**
     * 上首页提交
     */
    function __upindex(ids, formupindex, toupindex) {
        $.messager.progress();
        var params = {ids: ids, formupindex: formupindex, toupindex: toupindex};
        $.post('${basePath}/admin/train/upindex', params, function(data){
            $("#whgdg").datagrid('reload');
            if (!data.success || data.success != "1"){
                $.messager.alert("错误", data.errormsg||'操作失败', 'error');
            }
            $.messager.progress('close');
        }, 'json');
    }
    /**
     * 课程
     * @param idx
     */
    function course(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/train/course/view/list?id='+row.id+'&starttime='+row.starttime+'&endtime='+row.endtime);
    }

    /**
     * 资源
     * @param idx
     */
    function resource(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        //var cultid = $('#cultid').combobox('getValue');
        //WhgComm.editDialog('${basePath}/admin/resource/view/list?reftype=5&refid=' + row.id+'&cultid='+cultid);
        WhgComm.editDialog('${basePath}/admin/mass/resmanage/view/list?refid=' + row.id+'&reftype=5');
    }

    /**
     * 删除
     * @param idx
     */
    function del(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        var confireStr = '确定要回收选中的项吗？'
        if (row.delstate == 1 || row.state == 1){
            confireStr = '此操作将会永久性删除数据！'+ confireStr;
        }
        WhgComm.confirm("确认信息", confireStr, function(){
            $.messager.progress();
            $.post('${basePath}/admin/train/del', {id: row.id}, function(data){
                $("#whgdg").datagrid('reload');
                if (!data.success || data.success != "1"){
                    $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                }
                $.messager.progress('close');
            }, 'json');
        })
    }

    /**
     * 还原删除项
     * @param idx
     */
    function undel(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "确定要还原选中的项吗？", function(){
            $.messager.progress();
            $.post('${basePath}/admin/train/undel', {id: row.id, delstate: 0}, function(data){
                $("#whgdg").datagrid('reload');
                if (!data.success || data.success != "1"){
                    $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                }
                $.messager.progress('close');
            }, 'json');
        })
    }

    /**
     * 发布 [2,4]->6
     * @param idx
     */
    function publish(idx){
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
                $(".statemdfdate").datetimebox('setValue',new Date().Format('yyyy-MM-dd hh:mm:ss'));
                $('#whgff').form({
                    url : '${basePath}/admin/train/updstate',
                    onSubmit : function(param) {
                        param.ids = row.id;
                        param.formstates = "2,4";
                        param.tostate = 6;
                        var isValid = $(this).form('enableValidation').form('validate');
                        if(isValid){
                            $.messager.progress();
                        }else{
                            $("#btn").off("click").one("click",function () { $('#whgff').submit(); });
                        }
                        return isValid;
                    },
                    success : function(data) {
                        $.messager.progress('close');
                        var Json = $.parseJSON(data);
                        if(Json.success == "1"){
                            $('#whgdg').datagrid('reload');
                            $('#whgwin-add').dialog('close');
                        }else{
                            $.messager.alert("提示", data.errorMsg);
                        }
                    }
                });
                $("#btn").off("click").one("click",function () { $('#whgff').submit(); });
            }
        })
        /*$.messager.confirm("确认信息", "确定要发布选中的项吗？", function(r){
            if (r){
                __updStateSend(row.id, "2,4", 6);
            }
        })*/
    }

    /**
     * 取消发布 [6]->4
     * @param idx
     */
    function publishoff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "确定要取消发布选中的项吗？", function(){
            __updStateSend(row.id, 6, 4);
        })
    }

    /**
     * 送审 [1,5]->9
     * @param idx
     */
    function checkgo(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "确定要送审选中的项吗？", function(){
            __updStateSend(row.id, "1,5", 9);
        })
    }

    /**
     * 审通过 [9]->2
     * @param idx
     */
    function checkon(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "确定要审核选中的项吗？", function(){
            __updStateSend(row.id, 9, 2);
        })
    }

    /**
     * 审不通过 [9]->5
     * @param idx
     */
    function checkoff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        if(row.state == 4){
            $.messager.alert("提示", "撤销发布的不可以进行打回操作！");
            return;
        }
        WhgComm.confirm("确认信息", "确定要打回选中的项吗？", function(){
            __updStateSend(row.id, "9,2", 1);
        })
    }

    /**
     * 修改状态提交
     * @param ids
     * @param formstates
     * @param tostate
     * @private
     */
    function __updStateSend(ids, formstates, tostate){
        $.messager.progress();
        var params = {ids: ids, formstates: formstates, tostate: tostate};
        $.post('${basePath}/admin/train/updstate', params, function(data){
            $("#whgdg").datagrid('reload');
            if (!data.success || data.success != "1"){
                $.messager.alert("错误", data.errormsg||'操作失败', 'error');
            }
            $.messager.progress('close');
        }, 'json');
    }

    /**
     * 报名管理
     */
    function enroll(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/train/enrol/view/list?id='+row.id+'&isbasicclass='+row.isbasicclass);
    }

    /**
     * 资讯公告
     * @param idx
     */
    function info(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        console.log('url:${basePath}/admin/info/view/list?entityid='+row.id+'&entity=1&cultid='+row.cultid+'&deptid='+row.deptid);
        WhgComm.editDialog('${basePath}/admin/info/view/list?entityid='+row.id+'&entity=1&cultid='+row.cultid+'&deptid='+row.deptid);
    }
</script>
</body>
</html>
