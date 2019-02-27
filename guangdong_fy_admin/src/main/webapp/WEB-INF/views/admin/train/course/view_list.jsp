<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>
    <meta charset="UTF-8">
    <title>课程管理</title>
    <style>
        p em{
            color: #ac2925;
            font-size: medium;
            font-weight: bolder;
        }
    </style>
</head>
<body>
<!-- 表格 -->
<table id="whgdg" title="${pageTitle}" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:true, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb', url:'${basePath}/admin/train/course/srchList4p?id=${id}'">
    <thead>
    <tr>
        <th data-options="field:'starttime', width:160, formatter:WhgComm.FMTDateTime">课程开始时间</th>
        <th data-options="field:'endtime', width:160, formatter:WhgComm.FMTDateTime">课程结束时间</th>
        <th data-options="field:'state', width:160, formatter:WhgComm.FMTState">状态</th>
        <th data-options="field:'_opt', width:160, formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">

    <div class="whgdg-tb-srch">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-undo" onclick="WhgComm.editDialogClose();">返回</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="1" plain="true" method="edit">编辑</a>
    <c:if test="${islive == 1}">
        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="addFlowAddr">推流地址</a>
    </c:if>
    <a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="1" plain="true" method="publishoff">取消</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="0" plain="true" method="publish">启用</a>
</div>
<!-- 操作按钮-END -->

<!-- 添加表单 -->
<div id="whgwin-add" style="display: none">
    <form id="whgff" class="whgff" method="post">
        <input type="hidden" name="id"/>
        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 25%"><i>*</i>开始时间：</div>
            <div class="whgff-row-input" style="width: 75%"><input class="easyui-datetimebox" name="starttime" style="width:90%; height:32px" data-options="required:true"></div>
        </div>

        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 25%"><i>*</i>结束时间：</div>
            <div class="whgff-row-input" style="width: 75%"><input class="easyui-datetimebox" name="endtime" style="width:90%; height:32px" data-options="required:true"></div>
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

<!-- 添加推流地址表单 -->
<div id="whgwin-playaddr" style="display: none">
    <form id="whgff1" class="whgff" method="post">

        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 25%"><i>*</i>选择推流地址模板：</div>
            <div class="whgff-row-input" style="width: 75%">
                <input class="easyui-combobox" name="flowaddr" id="flowaddr" panelHeight="auto" limitToList="true" style="width:500px; height:32px"
                                                                   data-options="required:false, editable:false,multiple:false, mode:'remote', valueField:'id', textField:'name'"/>
            </div>
        </div>
        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 25%"><i>*</i>选择播放地址模板：</div>
            <div class="whgff-row-input" style="width: 75%">
                <input class="easyui-combobox" name="playaddr" id="playaddr" panelHeight="auto" limitToList="true" style="width:500px; height:32px"
                       data-options="required:false, editable:false,multiple:false, mode:'remote', valueField:'id', textField:'name'"/>
            </div>
        </div>
        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 25%"><i>*</i>应用名：</div>
            <div class="whgff-row-input" style="width: 75%">
                <input class="easyui-textbox" id="appname" name="appname" value="gdswhg" style="width: 500px; height: 32px" data-options="required:true, readonly:true,validType:['length[1,60]']" />
            </div>
        </div>
        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 25%"><i>*</i>流名：</div>
            <div class="whgff-row-input" style="width: 75%">
                <%--<input class="easyui-textbox" id="streamname" name="streamname" style="width: 500px; height: 32px" data-options="required:true,validType:['length[1,60]']" value="${whgLive.streamname}"/>--%>
                <input class="easyui-combobox" id="streamname" name="streamname" style="width: 500px; height: 32px" data-options="required:true, limitToList:true, valueField:'id', textField:'text', data:streamNameData()" value="${whgLive.streamname}"/>
            </div>
        </div>
        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 25%">&nbsp;</div>
            <div class="whgff-row-input" style="width: 75%">
                <p>注意，最后的推流地址为将“推流地址模板”字符串中的“<em>AppName</em>”和“<em>StreamName</em>”替换成表单中输入的“<em>应用名</em>”和“<em>流名</em>”。</p>
            </div>
        </div>
    </form>
</div>
<div id="whgwin-playaddr-btn" style="text-align: center; display: none">
    <div style="display: inline-block; margin: 0 auto">
        <a href="#" class="easyui-linkbutton" iconCls="icon-save" id="playaddr-btn" >保 存</a>
        <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#whgwin-playaddr').dialog('close')">关 闭</a>
    </div>
</div>
<!-- 添加推流地址表单 END -->



<script>
    /**
     * 可选直播流
     **/
    function streamNameData(){
        return [
            {"id":"live1", "text":"live1"},
            {"id":"live2", "text":"live2"},
            {"id":"live3", "text":"live3"},
            {"id":"live4", "text":"live4"},
            {"id":"live5", "text":"live5"},
            {"id":"live6", "text":"live6"},
            {"id":"live7", "text":"live7"},
            {"id":"live8", "text":"live8"},
            {"id":"live9", "text":"live9"},
            {"id":"live10", "text":"live10"},
        ];
    }

    $(function () {
        setFlowaddr();
        setPlayaddr();
    })

    /**
     * 编辑
     * @param idx
     */
    function edit(idx){
        $('#whgwin-add').dialog({
            title: '编辑课程',
            cache: false,
            modal: true,
            width: '400',
            height: '200',
            maximizable: true,
            resizable: true,
            buttons: '#whgwin-add-btn',
            onOpen : function () {
                var _trastarttime = WhgComm.FMTDateTime(${starttime});
                var _traendtime = WhgComm.FMTDateTime(${endtime}+" 23:59:59");


                var rows = $("#whgdg").datagrid('getRows');
                var row = rows[idx];
                var id = row.id;
                var traid = row.traid;
                //格式化日期
                var _data = $.extend({}, row,
                        {
                            starttime : WhgComm.FMTDateTime(row.starttime),
                            endtime : WhgComm.FMTDateTime(row.endtime),
                        });
                if (row){
                    $('#whgff').form('load', _data);
                    $('#whgff').form({

                        url : '${basePath}/admin/train/course/edit',
                        onSubmit : function(param) {
                            param.traid = traid;
                            var isValid = $(this).form('enableValidation').form('validate');
                            var starttime = $("#whgff").find("input[name='starttime']").val();
                            var endtime = $("#whgff").find("input[name='endtime']").val();

                            if((starttime > endtime)){
                                $.messager.alert("提示", "课程开始时间应早于结束时间！");
                                isValid = false;
                            }
                            if(starttime < _trastarttime || endtime > _traendtime){
                                $.messager.alert("提示", "课程时间应在培训周期内！");
                                isValid = false;
                            }
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
                                //  $.messager.alert("提示", "操作成功");
                                $('#whgwin-add').dialog('close');
                            }else{
                                $.messager.alert("提示", data.errormsg);
                            }
                        }
                    });
                }
                $("#btn").off("click").on("click",function () {$('#whgff').submit();});
            }
        });
    }

    /**取消*/
    function publishoff(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要取消选中的项吗？", function(r){
            if (r){
                $.messager.progress();
                var params = {ids: row.id, formstates: 1, tostate: 0};
                $.post('${basePath}/admin/train/course/updstate', params, function(data){
                    $("#whgdg").datagrid('reload');
                    if (!data.success || data.success != "1"){
                        $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                    }
                    $.messager.progress('close');
                }, 'json');
            }
        })
    }
    /**启用*/
    function publish(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要启用选中的项吗？", function(r){
            if (r){
                $.messager.progress();
                var params = {ids: row.id, formstates: 0, tostate: 1};
                $.post('${basePath}/admin/train/course/updstate', params, function(data){
                    $("#whgdg").datagrid('reload');
                    if (!data.success || data.success != "1"){
                        $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                    }
                    $.messager.progress('close');
                }, 'json');
            }
        })
    }

    /**
     * 删除
     * @param idx
     */
    function del(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        var confireStr = '确定要删除选中的项吗？'

        $.messager.confirm("确认信息", confireStr, function(r){
            if (r){
                $.messager.progress();
                $.post('${basePath}/admin/train/course/del', {id: row.id}, function(data){
                    $("#whgdg").datagrid('reload');
                    if (!data.success || data.success != "1"){
                        $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                    }
                    $.messager.progress('close');
                }, 'json');
            }
        })
    }

    /**
     * 添加推流地址
     * @param idx
     */
    function addFlowAddr(idx) {
        var islive = '${islive}';
        var row = $("#whgdg").datagrid("getRows")[idx];
        $('#whgwin-playaddr').dialog({
            title: '编辑推流地址',
            cache: false,
            modal: true,
            width: '800',
            height: '400',
            maximizable: true,
            resizable: true,
            buttons: '#whgwin-playaddr-btn',
            onOpen : function () {
                $.getJSON("${basePath}/admin/traLive/srchTraLive?traid="+row.traid+"&courseid="+row.id,function (data) {
                    if("1" == data.success){
                        $("#appname").textbox("setValue",data.data.appname);
                        $("#streamname").combobox("setValue",data.data.streamname);
                    }
                    $('#whgff1').form({
                        url : '${basePath}/admin/traLive/addLive',
                        onSubmit : function(param) {
                            if(data.id != null || data.id != ""){
                                param.id = row.id;
                            }
                            param.traid = row.traid;
                            param.courseid = row.id;
                            var isValid = $(this).form('enableValidation').form('validate');

                            if(isValid){
                                $.messager.progress();
                            }else{
                                $("#playaddr-btn").off("click").one("click",function () { $('#whgff1').submit(); });
                            }
                            return isValid
                        },
                        success : function(data) {
                            $.messager.progress('close');
                            var Json = $.parseJSON(data);
                            //alert(Json.success == "1");
                            if(Json.success == "1"){
                                $('#whgwin-playaddr').dialog('close');
                            }else{
                                $.messager.alert("提示", data.errormsg);
                            }
                        }
                    });
                    $("#playaddr-btn").off("click").on("click",function () {$('#whgff1').submit();});

                });
            }
        });

    }

    function setFlowaddr() {
        $.getJSON("${basePath}/admin/live/getFlowaddr",function (data) {
            //debugger;
            if("1" != data.success){
                $.messager.alert("错误", data.errormsg, 'error');
                return;
            }
            var rows = data.rows;
            $("#flowaddr").combobox("loadData",rows);
//                debugger;
            var flowaddr = "${whgLive.flowaddr}";
            if(0 < rows.length){
                flowaddr = flowaddr != ""?flowaddr:rows[0].id;
                $("#flowaddr").combobox("setValue",flowaddr);
            }
        });
    }

    function setPlayaddr() {
        $.getJSON("${basePath}/admin/live/getPlayaddr",function (data) {
            //debugger;
            if("1" != data.success){
                $.messager.alert("错误", data.errormsg, 'error');
                return;
            }
            var rows = data.rows;
            $("#playaddr").combobox("loadData",rows);
//                debugger;
            var playaddr = "${whgLive.playaddr}";
            if(0 < rows.length){
                playaddr = playaddr != ""?playaddr:rows[0].id;
                $("#playaddr").combobox("setValue",playaddr);
            }
        });
    }
</script>
</body>
</html>
