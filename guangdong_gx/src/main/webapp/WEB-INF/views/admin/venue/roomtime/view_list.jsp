<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>

    <title>活动室时段管理</title>
    <script src="${basePath}/static/admin/js/whgmodule-gridopts.js"></script>
</head>
<body>
    <table id="whgdg" class="easyui-datagrid" title="预定时段管理-${roomtitle}" style="display: none;"
           toolbar="#tb" pagination=true pageSize=30 pageList="[10,20,30,50,100,200]" loadMsg="数据加载中，请稍候。。。"
           data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:false, checkOnSelect:true, selectOnCheck:true,
           url:'${basePath}/admin/venue/roomtime/srchList4p?roomid=${roomid}'">
        <thead>
            <tr>
                <th data-options="checkbox: true, field:'checkbox' ">全选</th>
                <th data-options="sortable:false, field:'roomid', formatter:function(){return '${roomtitle}';} ">活动室</th>
                <th data-options="sortable: true, field:'timeday', formatter:WhgComm.FMTDate ">日期</th>
                <th data-options="sortable: true, field:'timestart', formatter:WhgComm.FMTTime ">时段开始</th>
                <th data-options="sortable: true, field:'timeend', formatter:WhgComm.FMTTime ">时段结束</th>
                <th data-options="sortable: true, field:'hasfees', formatter:hasfeesFmt ">是否收费</th>
                <th data-options="sortable: true, field:'state', formatter:timeState ">状态</th>
                <th data-options="field:'id', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
            </tr>
        </thead>
    </table>

    <div id="tb" style="display: none;">
        <div>
            <a class="easyui-linkbutton" iconCls="icon-back" onclick="WhgComm.editDialogClose()">返回</a>
            <a class="easyui-linkbutton easyui-tooltip" title="按开放时间设置批量生成预订时段" iconCls="icon-add" onclick="whgListTool.add()">批量添加预定时段</a>
            <a class="easyui-linkbutton easyui-tooltip" title="补充添加开放时间设置未定义的预订时段" iconCls="icon-add" onclick="whgListTool.addOne()">添加预定时段</a>
            <a class="easyui-linkbutton" iconCls="icon-edit" onclick="whgListTool.rowsOpen()">批量开放预定</a>
            <a class="easyui-linkbutton" iconCls="icon-edit" onclick="whgListTool.rowsClose()">批量取消预定</a>
        </div>

        <div style="padding-top: 5px">
            <select class="easyui-combobox" name="state" prompt="请选择状态" panelHeight="auto" limitToList="true"
                    data-options="width:150, value:'', valueField:'id', textField:'text', data:[{id:'1',text:'开放预定'},{id:'0',text:'不开放预定'}]">
            </select>
            <input class= "easyui-datebox" name="startDay" data-options="width:120"/>
            至 <input class= "easyui-datebox" name="endDay" data-options="width:120"/>
            <a class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#tb');">查 询</a>
        </div>
    </div>


    <!-- 操作按钮 -->
    <div id="whgdg-opt" style="display: none;">
        <a plain="true" validKey="state" validVal="0" method="whgListTool.edit">编辑</a>
        <a plain="true" validKey="state" validVal="0" method="whgListTool.rowOpen">开放预定</a>
        <a plain="true" validKey="state" validVal="1" method="whgListTool.rowClose">取消预定</a>
    </div>
    <!-- 操作按钮-END -->

    <!-- 编辑表单 -->
    <div id="whgwin-edit"></div>
    <!-- 编辑表单 END -->

<script>
    function timeState(v){
        return v==1? '开放预定' : '不开放预定';
    }

    function hasfeesFmt(v){
        return v==1? '收费' : '不收费';
    }



    function myParseDay(daystr, timestr){
        var _day = new Date();

        var dstrs = /(\d{4})-(\d{1,2})-(\d{1,2})/.exec(daystr);
        if (dstrs){
            _day.setUTCFullYear( parseInt(dstrs[1]), parseInt(dstrs[2])-1, parseInt( dstrs[3]) );
        }
        if (timestr){
            var timestrs = /(\d{2}):(\d{2})/.exec(timestr);
            if (timestrs){
                _day.setUTCHours(parseInt(timestrs[1]), parseInt(timestrs[2]), 0, 0);
            }
        }else{
            _day.setUTCHours(0,0,0,0);
        }
        return _day;
    }

    $.extend($.fn.validatebox.defaults.rules, {
        afterDate: {
            validator: function(value, param){
                var preValue = $(param[0]).datebox('getValue');
                if (!preValue) return true;

                var currDate = myParseDay(value);//new Date( Date.parse(value) );
                var preDate = myParseDay(preValue);//new Date( Date.parse(preValue) );
                var lastDate = myParseDay(preValue);//new Date( Date.parse(preValue) );
                lastDate.setDate(lastDate.getDate()+30);
                return currDate >= preDate && currDate <= lastDate;
            },
            message: '请选择前一个日期之后30天内的日期'
        },
        startDate: {
            validator: function(value, param){
                //return Date.parse(value) >= Date.parse(param[0]);
                var vd = myParseDay(value);
                var pd = myParseDay(param[0]);
                return vd >= pd;
            },
            message: '日期不能小于{0}'
        },
        addOneEndDate: {
            validator: function(value, param){
                var vd = myParseDay(value);
                var pd = myParseDay(param[0]);
                return vd < pd;
            },
            message: '必须小于批量添加起始日期{0}'
        },

        afterTime: {
            validator: function(value, param){
                var preValue = $(param[0]).timespinner('getValue');
                if (!preValue) return true;

                function toMMs(v){
                    var tms = /^(\d{2}):(\d{2})/.exec(v);
                    if (!tms) return 0;
                    var hh = String(tms[1]);
                    var mm = String(tms[2]);
                    return Number(hh) * 60 + Number(mm);
                }
                var pretm = toMMs(preValue);
                var tm = toMMs(value);
                return tm > pretm;
            },
            message: '结束时间需大于开始时间'
        }
    });



    var whgListTool = new Gridopts();

    /**
     * 单行操作开启
     * @param idx
     */
    Gridopts.prototype.rowOpen = function(idx){
        var row = this.__getGridRow(idx);
        var mmx = this;
        WhgComm.confirm("确认信息", "确定开放预定选中的项吗？", function(r){
            //if (r){
                mmx.__updateStateSend(row.id, "0", 1);
            //}
        })
    };
    /**
     * 单行操作关闭
     * @param idx
     */
    Gridopts.prototype.rowClose = function(idx){
        var row = this.__getGridRow(idx);
        var mmx = this;
        WhgComm.confirm("确认信息", "确定取消预定选中的项吗？", function(r){
            //if (r){
                mmx.__updateStateSend(row.id, "1", 0);
            //}
        })
    };

    /**
     * 批量开启
     */
    Gridopts.prototype.rowsOpen = function(){
        var rowids = this.__getGridSelectIds();
        if (rowids.length<1){
            $.messager.alert("提示信息", "请选择要操作的选项", 'info');
            return;
        }
        var mmx = this;
        WhgComm.confirm("确认信息", "确定开放预定选中的项吗？", function(r){
            //if (r){
                mmx.__updateStateSend(rowids, "0", 1);
            //}
        })
    };

    /**
     * 批量关闭
     * @param idx
     */
    Gridopts.prototype.rowsClose = function(idx){
        var rowids = this.__getGridSelectIds();
        if (rowids.length<1){
            $.messager.alert("提示信息", "请选择要操作的选项", 'info');
            return;
        }
        var mmx = this;
        WhgComm.confirm("确认信息", "确定取消预定选中的项吗？", function(r){
            //if (r){
                mmx.__updateStateSend(rowids, "1", 0);
            //}
        })
    };

    /**
     * 批量添加预订时段记录
     */
    Gridopts.prototype.add = function(){
        var roomid = '${roomid}';
        var hasfees = '${hasfees}';
        var mmx = this;
        var addDialog =$('<div></div>').dialog({
            title: '批量添加预定时段',
            width: 400,
            height: 230,
            cache: false,
            modal: true,
            content: '<div class="dialog-content-add" style="padding: 30px 30px;"></div>',
            onOpen: function () {
                $(this).find('.dialog-content-add').empty();
                $(this).find('.dialog-content-add').append('\
                    <div>\
                    从：<input class= "easyui-datebox valid-statrDay" data-options="width:120,required:true"/>\
                    至 <input class= "easyui-datebox" validType="afterDate[\'.valid-statrDay\']" data-options="width:120,required:true"/>\
                    </div>\
                    <div style="margin-top: 10px;">\
                    是否收费：<select class="easyui-combobox" panelHeight="auto" data-options="width:150, required:true, editable:false">\
                        <option value="1">收费</option>\
                        <option value="0">不收费</option>\
                        </select>\
                    </div>\
                ');

                $.messager.progress();
                var that = this;
                $.getJSON('${basePath}/admin/venue/roomtime/ajaxAddStartDay?t='+(new Date()).getTime(), {roomid: roomid}, function(data){
                    $.messager.progress('close');
                    var sday = new Date(data);
                    var eday = new Date(data);
                    eday.setDate(sday.getDate()+6);
                    var __sday = WhgComm.FMTDate(sday);
                    $(that).find(".easyui-datebox:eq(0)").attr('validType', "startDate['"+__sday+"']");
                    $.parser.parse($(that));
                    $(that).find(".easyui-datebox:eq(0)").datebox('setValue', __sday);
                    $(that).find(".easyui-datebox:eq(1)").datebox('setValue', WhgComm.FMTDate(eday));

                    $(that).find(".easyui-combobox:eq(0)").combobox('setValue', (hasfees && hasfees=='1')? "1": "0");
                });
            },
            buttons: [{
                text:'确认',
                iconCls: 'icon-ok',
                handler:function(){
                    $.messager.progress();
                    var valid = true;
                    addDialog.find(".easyui-datebox").each(function () {
                        valid = $(this).datebox('isValid');
                        return valid;
                    });

                    if (!valid) {
                        $.messager.progress('close');
                        return;
                    }
                    var sday = addDialog.find(".easyui-datebox:eq(0)").datebox('getValue');
                    var eday = addDialog.find(".easyui-datebox:eq(1)").datebox('getValue');
                    var _hasfees = addDialog.find(".easyui-combobox:eq(0)").combobox('getValue');
                    $.ajax({
                        url: mmx.modeUrl+'/add',
                        data: {roomid: roomid, startDay: sday, endDay: eday, hasfees: _hasfees},
                        type: 'post',
                        dataType: 'json',
                        success: function(data){
                            mmx.__ajaxSuccess(data);
                            addDialog.dialog('close');
                            addDialog.remove()
                        },
                        error: function(xhr, ts, e){ mmx.__ajaxError(xhr, ts, e) }
                    });
                }
            },{
                text:'关闭',
                iconCls: 'icon-no',
                handler:function(){ addDialog.dialog('close'); addDialog.remove()}
            }]
        });

    };

    /**
     * 添加例外的预订时段
     */
    Gridopts.prototype.addOne = function(){
        var roomid = '${roomid}';
        var hasfees = '${hasfees}';
        var mmx = this;

        var addOneDialog =$('<div></div>').dialog({
            title: '添加预定时段',
            width: 400,
            height: 260,
            cache: false,
            modal: true,
            content: '<div class="dialog-content-add" style="padding: 30px 30px;"></div>',
            onOpen: function () {
                $(this).find('.dialog-content-add').empty();
                $(this).find('.dialog-content-add').append('\
                    <div>\
                    开放日期：<input class= "easyui-datebox valid-statrDay" data-options="width:180,required:true, novalidate:false"/>\
                    </div>\
                    <div style="margin-top: 10px;">\
                    时段开始：<input class="easyui-timespinner valid-befortime" data-options="width:60,required:true"/>\
                    时段结束：<input class="easyui-timespinner" validType="afterTime[\'.valid-befortime\']" data-options="width:60,required:true"/>\
                    </div>\
                    <div style="margin-top: 10px;">\
                    是否收费：<select class="easyui-combobox" panelHeight="auto" data-options="width:150, required:true, editable:false">\
                        <option value="1">收费</option>\
                        <option value="0">不收费</option>\
                        </select>\
                    </div>\
                ');

                $.messager.progress();
                var that = this;
                $.getJSON('${basePath}/admin/venue/roomtime/ajaxAddStartDay?t='+(new Date()).getTime(), {roomid: roomid}, function(data){
                    $.messager.progress('close');
                    var sday = new Date(data);
                    var initday = new Date(data);
                    initday.setDate(sday.getDate()-1);
                    var __sday = WhgComm.FMTDate(sday);
                    $(that).find(".easyui-datebox:eq(0)").attr('validType', "addOneEndDate['"+__sday+"']");
                    $.parser.parse($(that));

                    $(that).find(".easyui-datebox:eq(0)").datebox('setValue', WhgComm.FMTDate(initday));

                    $(that).find(".easyui-combobox").combobox('setValue', (hasfees && hasfees=='1')? "1": "0");
                });
            },
            buttons: [{
                text:'确认',
                iconCls: 'icon-ok',
                handler:function(){
                    $.messager.progress();
                    var valid = true;
                    addOneDialog.find(".easyui-datebox").each(function () {
                        valid = $(this).datebox('isValid');
                        return valid;
                    });
                    if (!valid) {
                        $.messager.progress('close');
                        return;
                    }

                    addOneDialog.find(".easyui-timespinner").each(function () {
                        valid = $(this).timespinner('isValid');
                        return valid;
                    });
                    if (!valid) {
                        $.messager.progress('close');
                        return;
                    }
                    var sday = addOneDialog.find(".easyui-datebox:eq(0)").datebox('getValue');
                    var timeStart = addOneDialog.find(".easyui-timespinner:eq(0)").timespinner('getValue');
                    var timeEnd = addOneDialog.find(".easyui-timespinner:eq(1)").timespinner('getValue');
                    var _hasfees = addOneDialog.find(".easyui-combobox:eq(0)").combobox('getValue');

                    $.ajax({
                        url: mmx.modeUrl+'/addOne',
                        data: {roomid: roomid, timeDay: sday, timeStart: timeStart, timeEnd: timeEnd, hasfees: _hasfees},
                        type: 'post',
                        dataType: 'json',
                        success: function(data){
                            mmx.__ajaxSuccess(data);
                            addOneDialog.dialog('close');
                            addOneDialog.remove()
                        },
                        error: function(xhr, ts, e){ mmx.__ajaxError(xhr, ts, e) }
                    });
                }
            },{
                text:'关闭',
                iconCls: 'icon-no',
                handler:function(){ addOneDialog.dialog('close'); addOneDialog.remove()}
            }]
        });
    };

    /**
     * 编辑预定时段记录
     * @param idx
     */
    Gridopts.prototype.edit = function(idx){
        var row = this.__getGridRow(idx);
        var roomid = '${roomid}';
        var id = row.id;
        var timeDay = WhgComm.FMTDate(row.timeday);

        var mmx = this;
        var optDialog =$('<div></div>').dialog({
            title: '编辑预定时段:'+timeDay,
            width: 400,
            height: 230,
            cache: false,
            modal: true,
            content: '<div class="dialog-content-add" style="padding: 30px 30px;"></div>',
            onOpen: function () {
                $(this).find(".dialog-content-add").append('\
                    <div>\
                    时段开始：<input class="easyui-timespinner valid-befortime" data-options="width:80,required:true"/>\
                    时段结束：<input class="easyui-timespinner" validType="afterTime[\'.valid-befortime\']" data-options="width:80,required:true"/>\
                    </div>\
                    <div style="margin-top: 10px;">\
                    是否收费：<select class="easyui-combobox" panelHeight="auto" data-options="width:150, required:true, editable:false">\
                        <option value="1">收费</option>\
                        <option value="0">不收费</option>\
                        </select>\
                    </div>\
                ');
                $.parser.parse($(this));
                $(this).find(".easyui-timespinner:eq(0)").timespinner('setValue', WhgComm.FMTTime(row.timestart));
                $(this).find(".easyui-timespinner:eq(1)").timespinner('setValue', WhgComm.FMTTime(row.timeend));
                $(this).find(".easyui-combobox:eq(0)").combobox('setValue', (row.hasfees && row.hasfees==1)? "1": "0");
            },
            buttons: [{
                text:'确认',
                iconCls: 'icon-ok',
                handler:function(){
                    $.messager.progress();
                    var valid = true;
                    optDialog.find(".easyui-timespinner").each(function () {
                        valid = $(this).timespinner('isValid');
                        return valid;
                    });

                    if (!valid) {
                        $.messager.progress('close');
                        return;
                    }
                    var timeStart = optDialog.find(".easyui-timespinner:eq(0)").timespinner('getValue');
                    var timeEnd = optDialog.find(".easyui-timespinner:eq(1)").timespinner('getValue');
                    var hasfees = optDialog.find(".easyui-combobox:eq(0)").combobox('getValue');
                    $.ajax({
                        url: mmx.modeUrl+'/edit',
                        data: {roomid: roomid, id: id, timeStart: timeStart, timeEnd: timeEnd, hasfees: hasfees},
                        type: 'post',
                        dataType: 'json',
                        success: function(data){
                            mmx.__ajaxSuccess(data);
                            optDialog.dialog('close');
                            optDialog.remove();
                        },
                        error: function(xhr, ts, e){ mmx.__ajaxError(xhr, ts, e) }
                    });
                }
            },{
                text:'关闭',
                iconCls: 'icon-no',
                handler:function(){ optDialog.dialog('close');optDialog.remove();}
            }]
        });

    };

</script>
</body>
</html>
