<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <c:choose>
    <c:when test="${type eq 'edit'}">
        <c:set var="pageTitle" value="编辑列表"></c:set>
        <c:set var="pageValue" value="1"></c:set>
    </c:when>
    <c:when test="${type eq 'check'}">
        <c:set var="pageTitle" value="审核列表"></c:set>
        <c:set var="pageValue" value="9"></c:set>
    </c:when>
    <c:when test="${type eq 'publish'}">
        <c:set var="pageTitle" value="发布列表"></c:set>
        <c:set var="pageValue" value="2"></c:set>
    </c:when>
    <c:when test="${type eq 'del'}">
        <c:set var="pageTitle" value="回收站"></c:set>
    </c:when>
    <c:otherwise>
        <c:set var="pageTitle" value="在线课程信息"></c:set>
    </c:otherwise>
    </c:choose>
    <title>${pageTitle}</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>
</head>
<body>
<!-- 表格 -->
<table id="whgdg" title="${pageTitle}" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb'">
    <thead>
    <tr>
        <th data-options="field:'_opt_kc',width:70,formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt-kc'">操作</th>
        <th data-options="field:'title'">直播标题</th>
        <th data-options="field:'isterm',width:50, formatter:yesNoFMT">学期制</th>
        <th data-options="field:'ismultisite',width:60, formatter:typeFMT">场次类别</th>
        <%--<th data-options="field:'crtdate',width:130, formatter:WhgComm.FMTDateTime">创建时间</th>--%>
        <th data-options=" field:'crtuser',width:120, formatter:WhgComm.FMTUserName ">编辑者</th>
        <th data-options=" field:'checkor',width:120, formatter:WhgComm.FMTUserName ">审核者</th>
        <th data-options=" field:'publisher',width:120, formatter:WhgComm.FMTUserName ">发布者</th>
        <th data-options="sortable: true,width:130, field:'statemdfdate', formatter:WhgComm.FMTDateTime ">操作时间</th>
        <th data-options="field:'state',width:50, formatter:WhgComm.FMTBizState" >状态</th>
        <th data-options="field:'_opt', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <shiro:hasPermission name="${resourceid}:add">
        <div class="whgd-gtb-btn"><a class="easyui-linkbutton" iconCls="icon-add" onclick="add()">添 加</a></div>
    </shiro:hasPermission>
    <div class="whgdg-tb-srch">
        <input class="easyui-combobox" name="cultid" id="cultid" data-options="editable:false,width:150" prompt="请选择文化馆"/>
        <input class="easyui-combobox" name="deptid" id="deptid" data-options="editable:false,width:150" prompt="请选择部门"/>
        <input class="easyui-textbox" style="width: 200px;" name="title" data-options="prompt:'请输入直播名称', validType:'length[1,32]'" />
        <input class="easyui-combobox" style="width:110px" name="state" id="state" data-options="prompt:'请选择状态',editable:false,valueField:'id',textField:'text'"/>
        <input class="easyui-combobox" style="width:110px" name="recommend" id="recommend" data-options="prompt:'是否推荐',editable:false,valueField:'id',textField:'text',data:[{id: '0',text: '未推荐'},{id: '1',text: '已推荐'}]"/>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<div id="whgdg-opt-kc" style="display: none;">
    <shiro:hasPermission name="${resourceid}:kecheng"><a href="javascript:void(0)" class="easyui-linkbutton" validFun="validKecheng" method="course">课程管理</a></shiro:hasPermission>
</div>
<div id="whgdg-opt" style="display: none;">
    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="view">查看</a>
    <shiro:hasPermission name="${resourceid}:edit"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validEdit" method="edit">编辑</a>
        <a plain="true" validFun="validEdit" method="setSmsGroup">设置短信组</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:ziyuan"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validZiyuan" method="resource">资源管理</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:pxzx"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validZiyuan" method="info">培训资讯</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkgo"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validCheckGo" method="checkgo">提交审核</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkon"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validCheckOn" method="checkon">审核通过</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkoff"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validCheckOff" method="checkoff">审核打回</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:publish"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validPublish" method="publish">发布</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:publishoff"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validPublishOff" method="publishoff">撤销发布</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:baoming"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validBaoMing" method="enroll">报名管理</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:recommend"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validRecommend" method="recommend">推荐</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:recommendoff"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validRecommendOff" method="recommendoff">撤销推荐</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:recovery"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validRecovery" method="doRecovery">回收</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:undel"><a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validUnRecovery" method="doUnRecovery">撤销回收</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:del"> <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validDel" method="doDel">删除</a></shiro:hasPermission>
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
    var pageType = '${type}';//编辑审核发布删除页面 edit|check|publish|del

    $(function(){
        //初始查询条件-业务状态 - 是否推荐
        init_condition_state();

        //初始查询条件-文化馆和部门
        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'',allcult: true,
            deptEid:'deptid', deptValue:'',alldept: true
        });

        //执行查询
        WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/traLive/srchList4p?type=${type}');
    });

    //初始查询条件-业务状态
    function init_condition_state() {
        var stateArr = WhgComm.getBizState().slice();
        if('edit' != pageType){
            stateArr.splice(0,1);
        }
        if('del' != pageType){
            $('#state').combobox('loadData', stateArr);
            if('edit' == pageType){
                $('#state').combobox('setValue', 1);
                $('#recommend').combobox('destroy');
            }else if('check' == pageType){
                $('#state').combobox('setValue', 9);
                $('#recommend').combobox('destroy');
            }else if('publish' == pageType){
                $('#state').combobox('setValue', 2);
            }
        }else{
            $('#state').combobox('destroy');
            $('#recommend').combobox('destroy');
        }
    }

    //操作按钮验证
    function validEdit(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return (pageType == 'edit' && row.state == 1) || (pageType == 'check' && row.state == 9) || (pageType == 'publish' && (row.state == 2 || row.state == 4));
    }
    function validKecheng(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return (pageType == 'edit' && row.state == 1) || (pageType == 'check' && row.state == 9) || (pageType == 'publish' && (row.state == 2 || row.state == 4 || row.state == 6));
    }
    function validZiyuan(idx){
        return validKecheng(idx);
    }
    function validCheckGo(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return pageType == 'edit' && row.state == 1;
    }
    function validCheckOn(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return pageType == 'check' && row.state == 9;
    }
    function validCheckOff(idx){
        return validCheckOn(idx);
    }
    function validPublish(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return pageType == 'publish' && (row.state == 2 || row.state == 4);
    }
    function validPublishOff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return pageType == 'publish' && (row.state == 6);
    }
    function validBaoMing(idx){
        return validPublishOff(idx);
    }
    function validRecommend(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 6 && row.recommend == 0;
    }
    function validRecommendOff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return row.state == 6 && row.recommend == 1;
    }
    function validRecovery(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return (pageType == 'check' && row.state == 9) || (pageType == 'publish' && (row.state == 2));
    }
    function validUnRecovery(idx){
        return pageType == 'del';
    }
    function validDel(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        return (pageType == 'edit' && row.state == 1) || (pageType == 'del' && row.delstate == 1);
    }

    //格式化列表中场次类型
    function typeFMT(val, rowData, index){
        switch(val){
            case 0 : return "单场";
            case 1 : return "多场";
            case 2 : return "固定场";
            default : return val;
        }
    }

    /**
     * 添加
     */
    function add(){
        WhgComm.editDialog('${basePath}/admin/traLive/view/add');
    }

    /**
     * 编辑
     * @param idx
     */
    function edit(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/traLive/view/add?id='+row.id);
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
        WhgComm.editDialog('${basePath}/admin/traLive/view/add?targetShow=1&id='+row.id);
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
        $.post('${basePath}/admin/traLive/updrecommend', params, function(data){
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
        var cultid = $('#cultid').combobox('getValue');
        WhgComm.editDialog('${basePath}/admin/train/course/view/list?id='+row.id+'&starttime='+row.starttime+'&endtime='+row.endtime+'&islive='+row.islive+'&cultid='+cultid);
    }

    /**
     * 资源
     * @param idx
     */
    function resource(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        //var cultid = $('#cultid').combobox('getValue');
        //WhgComm.editDialog('${basePath}/admin/resource/view/list?reftype=16&refid=' + row.id+'&cultid='+cultid);
        WhgComm.editDialog('${basePath}/admin/mass/resmanage/view/list?refid=' + row.id+'&reftype=16');
    }

    /**
     * 删除
     * @param idx
     */
    function doDel(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        var confireStr = '确定要删除选中的项吗？';
        if (row.delstate == 1 || row.state == 1){
            confireStr = '此操作将会永久性删除数据！'+ confireStr;
        }
        WhgComm.confirm("确认信息", confireStr, function(){
            $.messager.progress();
            $.post('${basePath}/admin/traLive/del', {id: row.id}, function(data){
                $("#whgdg").datagrid('reload');
                if (!data.success || data.success != "1"){
                    $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                }
                $.messager.progress('close');
            }, 'json');
        })
    }

    /**
     * 回收
     */
    function doRecovery(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        var confireStr = '您确认要回收选中的项吗？';
        WhgComm.confirm("确认信息", confireStr, function(){
            $.messager.progress();
            $.post('${basePath}/admin/traLive/del', {id: row.id}, function(data){
                $("#whgdg").datagrid('reload');
                if (!data.success || data.success != "1"){
                    $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                }
                $.messager.progress('close');
            }, 'json');
        })
    }

    /**
     * 撤销回收
     * @param idx
     */
    function doUnRecovery(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.confirm("确认信息", "您确认要撤销回收选中的项吗？", function(){
            $.messager.progress();
            $.post('${basePath}/admin/traLive/undel', {id: row.id, delstate: 0}, function(data){
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
                    url : '${basePath}/admin/traLive/updstate',
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
        $.post('${basePath}/admin/traLive/updstate', params, function(data){
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
