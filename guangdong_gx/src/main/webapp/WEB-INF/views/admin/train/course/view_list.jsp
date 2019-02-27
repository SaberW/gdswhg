<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>

    <script type="text/javascript" src="${basePath}/static/plupload/upload-file.js"></script>
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
        <th data-options="field:'title', width:160">课程名称</th>
        <th data-options="field:'starttime', width:160, formatter:WhgComm.FMTDateTime">课程开始时间</th>
        <th data-options="field:'endtime', width:160, formatter:WhgComm.FMTDateTime">课程结束时间</th>
        <th data-options="field:'state', width:160, formatter:WhgComm.FMTState">状态</th>
        <th data-options="field:'_opt', width:320, formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">

    <div class="whgdg-tb-srch">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-undo" onclick="WhgComm.editDialogClose();">返回</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add"  onclick="add()">添加课程</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="1" plain="true" method="edit">编辑</a>
    <c:if test="${islive == 1}">
        <!--  直播单独放在一个模块
        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="addFlowAddr">推流地址</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validAddVideo" method="addVideo">回顾视频管理</a>
        -->
    </c:if>
    <a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="1" plain="true" method="publishoff">停用</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="0" plain="true" method="publish">启用</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="1" plain="true" method="del">删除</a>
    <c:if test="${islive != 1}">
        <a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="1" plain="true"  method="sign">签到</a>
        <%--<a href="javascript:void(0)" class="easyui-linkbutton" validKey="state" validVal="1" plain="true"  method="toleave">请假</a>--%>
    </c:if>
</div>
<!-- 操作按钮-END -->

<!-- 添加表单 -->
<div id="whgwin-add" style="display: none">
    <form id="whgff" class="whgff" method="post">
        <input type="hidden" name="id"/>

        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 25%"><i>*</i>课程名称：</div>
            <div class="whgff-row-input" style="width: 75%">
                <input class="easyui-textbox" name="title" style="width:90%; height:32px" data-options="required:true,validType:['length[1,60]']">
            </div>
        </div>

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
            <div class="whgff-row-label" style="width: 25%"><i>*</i>应用名　：</div>
            <div class="whgff-row-input" style="width: 75%">
                <input class="easyui-textbox" id="appname" name="appname" value="gdswhg" style="width: 500px; height: 32px" data-options="required:true, readonly:true,validType:['length[1,60]']" />
            </div>
        </div>
        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 25%"><i>*</i>流　　名：</div>
            <div class="whgff-row-input" style="width: 75%">
                <%--<input class="easyui-textbox" id="streamname" name="streamname" style="width: 500px; height: 32px" data-options="required:true,validType:['length[1,60]']" value="${whgLive.streamname}"/>--%>
                <input class="easyui-combobox" id="streamname" name="streamname" style="width: 500px; height: 32px" data-options="required:true, editable:false, valueField:'id', textField:'text', data:streamNameData(), onChange:changeLive" value="${whgLive.streamname}"/>
            </div>
        </div>
        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 25%">推流地址：</div>
            <div class="whgff-row-input" style="width: 75%">
                <input class="easyui-textbox" style="width: 500px; height: 32px" id="flowaddr" name="flowaddr" value="rtmp://video-center.alivecdn.com/gdswhg/{流名}?vhost=live.gdsqyg.com" data-options="readonly:true" />
                <%--<input class="easyui-combobox" name="flowaddr" id="flowaddr" panelHeight="auto" limitToList="true" style="width:500px; height:32px"
                                                                   data-options="required:false, editable:false,multiple:false, mode:'remote', valueField:'id', textField:'name'"/>--%>
            </div>
        </div>
        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 25%">播放地址：</div>
            <div class="whgff-row-input" style="width: 75%">
                <input class="easyui-textbox" style="width: 500px; height: 32px" id="playaddr" name="playaddr" value="http://live.gdsqyg.com/gdswhg/{流名}.m3u8" data-options="readonly:true" />
                <%--<input class="easyui-combobox" name="playaddr" id="playaddr" panelHeight="auto" limitToList="true" style="width:500px; height:32px"
                       data-options="required:false, editable:false,multiple:false, mode:'remote', valueField:'id', textField:'name'"/>--%>
            </div>
        </div>
        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 25%">&nbsp;</div>
            <div class="whgff-row-input" style="width: 75%">
                <p style="color:#777;">注意：确定“流　　名”后，推流地址为“推流地址”字段的值，插放地址为“播放地址”字段的值。</p>
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

<!-- 添加视频表单 -->
<div id="whgwin-videoaddr" style="display: none">
    <form id="whgff2" class="whgff" method="post">
        <div class="whgff-row video_wrap">
            <div class="whgff-row-label"><i>*</i>资源地址：</div>
            <div class="whgff-row-input">
                <input class="easyui-combobox" name="entdir" id="video_entdir" style="height:35px;width:190px" data-options="prompt:'请选择目录',editable:true,limitToList:true,valueField:'id',textField:'text',url:'${basePath}/admin/video/srchPagging?srchDir=1&cultid=${param.cultid}'"/>
                <input class="easyui-combobox" name="enturl" id="video_enturl" style="height:35px;width:400px" data-options="editable:true,limitToList:true"/>
            </div>
        </div>
    </form>
</div>
<div id="whgwin-videoaddr-btn" style="text-align: center; display: none">
    <div style="display: inline-block; margin: 0 auto">
        <a href="#" class="easyui-linkbutton" iconCls="icon-save" id="videoaddr-btn" >保 存</a>
        <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#whgwin-videoaddr').dialog('close')">关 闭</a>
    </div>
</div>
<!-- 添加视频表单 END -->

<!--签到-->
<div id="whgwin-sign" style="display: none">
    <table id="course" title="" class="easyui-datagrid" style="display: none"
           data-options="fit:true, striped:true, rownumbers:true, fitColumns:true">
        <thead>
        <tr>
            <th data-options="field:'starttime', width:160, formatter:WhgComm.FMTDateTime">开始时间</th>
            <th data-options="field:'endtime', width:160, formatter:WhgComm.FMTDateTime">结束时间</th>
            <th data-options="field:'realname', width:160">报名人</th>
            <th data-options="field:'contactphone', width:160">联系电话</th>
            <th data-options="field:'crttime', width:160, formatter:WhgComm.FMTDateTime">报名时间</th>
            <th data-options="field:'sign', width:160, formatter:FMTsign">状态</th>
            <th data-options="field:'vstate', width:160, formatter:FMTvstate">请假状态</th>
            <th data-options="field:'_optsign', width:160, formatter:WhgComm.FMTOpt, optDivId:'whgdg-optsign'">操作</th>
        </tr>
        </thead>
    </table>
</div>

<!-- 操作按钮 -->
<div id="whgdg-optsign" style="display: none;">
    <a href="javascript:void(0)" id="_sign" class="easyui-linkbutton" validFun="validSign" plain="true" method="_sign">签到</a>
    <%--<a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="validleave" method="leave">请假审核</a>--%>
</div>
<!-- 操作按钮-END -->




<script>
    /**
     * 回顾视频管理
     **/
    function validAddVideo(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        var show = false;
        $.ajax({
            async:false,
            cache:false,
            url: "${basePath}/admin/traLive/srchTraLive?traid="+row.traid+"&courseid="+row.id,
            success: function(data){
                if(1 == data.success){
                    show = true;
                }
            }
        });
        return show;
    }

    /**修改流名称时处理*/
    function changeLive(newVal, oldVal){
        newVal = newVal || '{流名}';
        $('#flowaddr').textbox('setValue', 'rtmp://video-center.alivecdn.com/gdswhg/'+newVal+'?vhost=live.gdsqyg.com');
        $('#playaddr').textbox('setValue', 'http://live.gdsqyg.com/gdswhg/'+newVal+'.m3u8');
    }


    /**添加视频*/
    function addVideo(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];

        $('#whgwin-videoaddr').dialog({
            title: '编辑回顾视频',
            cache: false,
            modal: true,
            width: '800',
            height: '200',
            maximizable: true,
            resizable: true,
            buttons: '#whgwin-videoaddr-btn',
            onOpen : function () {
                $.getJSON("${basePath}/admin/traLive/srchTraLive?traid="+row.traid+"&courseid="+row.id+"&t="+new Date().getTime(),function (data) {
                    if(1 == data.success){
                        //$("#video_enturl").combobox("setValue",data.data.enturl);
                        //----------
                        $('#video_enturl').combobox('clear');
                        var initAddr = typeof(data.data.enturl) != 'undefined' ? data.data.enturl : '';//视频音频地址

                        //选择资源目录后级连资源地址
                        $('#video_entdir').combobox({
                            onChange: function (newv, oldv) {
                                $('#video_enturl').combobox({
                                    valueField:'addr',textField:'key',url:'${basePath}/admin/video/srchPagging?srchFile=1&dir='+encodeURIComponent(newv),
                                    onLoadSuccess: function () {
                                        if(initAddr){
                                            var valArr = $('#video_enturl').combobox('getData');
                                            if(valArr.length > 0){
                                                for(var i=0; i<valArr.length; i++){
                                                    var ___val = valArr[i].addr;
                                                    var ___idx = ___val.indexOf("\?");
                                                    if(___idx > -1){
                                                        ___val = ___val.substring(0,___idx);
                                                        initAddr = initAddr.substring(0, ___idx);
                                                    }
                                                    if(initAddr == ___val){
                                                        $('#video_enturl').combobox('setValue', valArr[i].addr);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        initAddr = false;
                                    }
                                });
                            }
                        });
                        //编辑视频资源
                        if(initAddr){
                            //音视频所在目录
                            var video_enturl_val = initAddr;
                            var video_enturl_val_host = video_enturl_val;
                            for(var i=0; i<3; i++){
                                var idx = video_enturl_val_host.indexOf("/");
                                video_enturl_val_host = video_enturl_val_host.substring(idx+1);
                            }
                            var _idx = video_enturl_val_host.indexOf("\?");
                            if(_idx > -1){
                                video_enturl_val_host = video_enturl_val_host.substring(0,_idx);
                            }
                            var dirVal = video_enturl_val_host.substring(0,video_enturl_val_host.lastIndexOf("/")+1);
                            dirVal = decodeURIComponent(dirVal);
                            $('#video_entdir').combobox('setValue', dirVal);
                        }
                        //-------------

                    }
                    $('#whgff2').form({
                        url : '${basePath}/admin/traLive/addLive',
                        onSubmit : function(param) {
                            param.id = data.data.id;
                            param.traid = row.traid;
                            param.courseid = row.id;
                            var isValid = $(this).form('enableValidation').form('validate');

                            if(isValid){
                                $.messager.progress();
                            }else{
                                $("#videoaddr-btn").off("click").one("click",function () { $('#whgff2').submit(); });
                            }
                            return isValid
                        },
                        success : function(data) {
                            $.messager.progress('close');
                            var Json = $.parseJSON(data);
                            //alert(Json.success == "1");
                            if(Json.success == "1"){
                                $('#whgwin-videoaddr').dialog('close');
                            }else{
                                $.messager.alert("提示", data.errormsg);
                            }
                        }
                    });
                    $("#videoaddr-btn").off("click").on("click",function () {$('#whgff2').submit();});

                });
            }
        });

    }


    function validSign(idx){
        var row = $("#course").datagrid("getRows")[idx];
        //alert(row);
        return row.state == 1 && row.sign != 1 &&  row.vstate != 1;
    }
    function validleave(idx){
        var row = $("#course").datagrid("getRows")[idx];
        //alert(row.vstate);
        return  row.vstate == 0;
    }

    function FMTsign(v){

        if(v == 1){
            return "已签到";
        }else{
            return "未签到";
        }
    }
    function FMTvstate(v){

        if(v == 1){
            return "已请假";
        } if(v == 0){
            return "请假申请中";
        }
        if(v == 2){
            return "请假未通过";
        }else{
            return "未请假";
        }
    }

    /**
     * 请假
     */
    function leave(idx){
        var row = $("#course").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/train/enrol/leaveview?id='+row.id+'&userid='+row.userid);
    }


    /**
     * 请假
     */
    function toleave(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        $('#whgwin-sign').dialog({
            title: '课程表',
            cache: false,
            modal: true,
            width: '900',
            height: '500',
            maximizable: true,
            resizable: true,
            buttons: '#whgwin-sign-btn',
            onOpen: function () {
                $("#course").datagrid({
                    url: '${basePath}/admin/train/course/srchOne?traid=${id}&courseid='+row.id,

                    onLoadSuccess:function (data) {
                        if(data.success == 0){
                            $.messager.alert("提示", '没有报名数据！', 'error');

                           $('#whgdg').datagrid('reload');
                        }
                    }
                });
            }
        });
    }



    /**
     * 签到
     * @param idx
     */
    function sign(idx){

        var row = $("#whgdg").datagrid("getRows")[idx];
        $('#whgwin-sign').dialog({
            title: '课程表',
            cache: false,
            modal: true,
            width: '900',
            height: '500',
            maximizable: true,
            resizable: true,
            buttons: '#whgwin-sign-btn',
            onOpen: function () {
                $("#course").datagrid({
                    url: '${basePath}/admin/train/course/srchOne?traid=${id}&courseid='+row.id,
                    onLoadSuccess:function (data) {
                        if(data.success == 0){
                            $.messager.alert("提示", '没有报名数据！', 'error');
                            $('#whgdg').datagrid('reload');
                        }
                    }
                });
            }
        });

    }


    function _sign(idx) {
        var row = $("#course").datagrid("getRows")[idx];
        $.ajax({
            type: "POST",
            url: "${basePath}/admin/train/course/signCourse",
            data: {"traid": row.traid,"enrolid": row.enrolid,"userid": row.userid,"courseid": row.id,"starttime": row.starttime,"endtime": row.endtime},
            success: function(data){
                if(data.success == "1"){
                    $.messager.alert("提示", "操作成功");
                    $('#course').datagrid('reload');
                }else{
                    $.messager.alert("提示", data.errormsg);
                }
            }
        });
    }

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

    /*$(function () {
        setFlowaddr();
        setPlayaddr();
    })*/

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
            height: '250',
            maximizable: true,
            resizable: true,
            buttons: '#whgwin-add-btn',
            onOpen : function () {
                var _trastarttime = WhgComm.FMTDateTime(${starttime});
                var _traendtime = WhgComm.FMTDate(${endtime})+" 23:59:59";

                var rows = $("#whgdg").datagrid('getRows');
                var row = rows[idx];
                var id = row.id;
                var traid = row.traid;
                //格式化日期
                var _data = $.extend({}, row,
                        {
                            starttime : WhgComm.FMTDateTime(row.starttime),
                            endtime : WhgComm.FMTDateTime(row.endtime),
                            title : row.title
                        });
                if (row){
                    $('#whgff').form('load', _data);
                    $('#whgff').form({

                        url : '${basePath}/admin/train/course/edit',
                        onSubmit : function(param) {
                            param.traid = traid;
                            $(this).form("enableValidation");
                            var isValid = $(this).form('validate');
                            if (isValid) {

                                var starttime = $("#whgff").find("input[name='starttime']").val();
                                var endtime = $("#whgff").find("input[name='endtime']").val();

                                if ((starttime > endtime)) {
                                    $.messager.alert("提示", "课程开始时间应早于结束时间！");
                                    isValid = false;
                                }

                                // if (starttime < _trastarttime || endtime > _traendtime) {
                                //     $.messager.alert("提示", "课程时间应在培训周期内！");
                                //     isValid = false;
                                // }
                                if (isValid) {
                                    $.messager.progress();
                                } else {
                                    $("#btn").off("click").one("click", function () {
                                        $('#whgff').submit();
                                    });
                                }
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
                                $('#whgdg').datagrid('reload');
                                $.messager.alert("提示", Json.errormsg);
                            }
                        }
                    });
                }
                $("#btn").off("click").on("click",function () {$('#whgff').submit();});
            }
        });
    }

    /**
     *添加
     **/
    function add(){
        $('#whgwin-add').dialog({
            title: '添加课程',
            cache: false,
            modal: true,
            width: '400',
            height: '250',
            maximizable: true,
            resizable: true,
            buttons: '#whgwin-add-btn',
            onOpen : function () {
                var _trastarttime = WhgComm.FMTDateTime(${starttime});
                var _traendtime = WhgComm.FMTDate(${endtime})+" 23:59:59";
                var traid = ${id};

                $('#whgff').form('clear');
                $('#whgff').form({
                    url : '${basePath}/admin/train/course/addCourse',
                    onSubmit : function(param) {
                        param.traid = traid;
                        $(this).form("enableValidation");
                        var isValid = $(this).form('validate');
                        if (isValid) {

                            var starttime = $("#whgff").find("input[name='starttime']").val();
                            var endtime = $("#whgff").find("input[name='endtime']").val();

                            if ((starttime > endtime)) {
                                $.messager.alert("提示", "课程开始时间应早于结束时间！");
                                isValid = false;
                            }

                            // if (starttime < _trastarttime || endtime > _traendtime) {
                            //     $.messager.alert("提示", "课程时间应在培训周期内！");
                            //     isValid = false;
                            // }
                            if (isValid) {
                                $.messager.progress();
                            } else {
                                $("#btn").off("click").one("click", function () {
                                    $('#whgff').submit();
                                });
                            }
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
                            $('#whgdg').datagrid('reload');
                            $.messager.alert("提示", Json.errormsg);
                        }
                    }
                });
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
        var traid = ${id};
        var confireStr = '确定要删除选中的项吗？'

        $.messager.confirm("确认信息", confireStr, function(r){
            if (r){
                $.messager.progress();
                $.post('${basePath}/admin/train/course/del', {id: row.id,traid:traid}, function(data){
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
            buttons: '#whgwin-playaddr-btn',
            onOpen : function () {
                $.getJSON("${basePath}/admin/traLive/srchTraLive?traid="+row.traid+"&courseid="+row.id+"&t="+new Date().getTime(), function (data) {
                    if("1" == data.success){
                        $("#appname").textbox("setValue",data.data.appname);
                        $("#streamname").combobox("setValue",data.data.streamname);
                    }else{
                        $("#appname").textbox("setValue", 'gdswhg');
                        $("#streamname").combobox("setValue", '');
                    }

                    $('#whgff1').form({
                        novalidate: true,
                        url : '${basePath}/admin/traLive/addLive',
                        onSubmit : function(param) {
                            if(data.data){
                                param.id = data.data.id;
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
                                $("#whgdg").datagrid('reload');
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
