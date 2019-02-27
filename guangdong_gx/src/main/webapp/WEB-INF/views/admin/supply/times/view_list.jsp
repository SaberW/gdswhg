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
    <title>供需配送时段列表</title>

    <script src="${basePath}/static/admin/js/whgmodule-gridopts.js"></script>
    <script type="text/javascript" src="${basePath}/static/common/js/area.js"></script>
    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
</head>
<body>

<table id="whgdg" class="easyui-datagrid" title="供需配送时段列表" style="display: none;"
       toolbar="#tb" pagination=true loadMsg="数据加载中，请稍候。。。"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:true, checkOnSelect:true, selectOnCheck:true,
           url:'${basePath}/admin/supply/times/srchList4p?supplyid=${supplyid}&supplytype=${supplytype}'">
    <thead frozen="true">
    <tr>
        <th data-options="field:'id', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
    <thead>
    <tr>
        <th data-options="sortable: false, field:'timestart', formatter:WhgComm.FMTDateTime ">配送开始时间</th>
        <th data-options="sortable: false, field:'timeend', formatter:WhgComm.FMTDateTime ">配送结束时间</th>
        <th data-options="sortable: false, field:'hasfees', formatter:FMThasfees ">是否收费</th>
        <th data-options="sortable: false, field:'pscity' ">配送范</th>

    </tr>
    </thead>
</table>

<div id="tb" style="display: none;">
    <div>
        <a class="easyui-linkbutton" iconCls="icon-back" onclick="WhgComm.editDialogClose()">返回</a>
        <a class="easyui-linkbutton" iconCls="icon-add" onclick="whgListTool.add()">添加</a>
    </div>

    <%--<div style="padding-top: 5px">
        <a class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#tb');">查 询</a>
    </div>--%>
</div>


<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <a plain="true" method="whgListTool.edit">编辑</a>
    <a plain="true" method="whgListTool.del">删除</a>
</div>
<!-- 操作按钮-END -->

<div id="edit-panel" style="display: none">
    <form class="whgff">
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>配送范围：</div>
        <div class="whgff-row-input">
            <input class="EEE-combobox" style="width:150px; height:32px" name="psprovince" refid="__PSPROVINCE_ELE"
                   data-options="readonly:true, required:true, prompt:'请选择省', editable:false, limitToList:true, valueField:'name', textField:'name'"/>
            <input class="EEE-combobox" style="width:400px; height:32px" name="pscity" refid="__PSCITY_ELE"
                   data-options="required:true, prompt:'请选择市(多选)', editable:false, multiple:true, valueField:'name', textField:'name'"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>配送时间：</div>
        <div class="whgff-row-input timesUI">
            <input class="EEE-datetimebox timesui-input" style="height:32px" name="_timestart"
                   data-options="required:true"/> 至
            <input class="EEE-datetimebox timesui-input" style="height:32px" name="_timeend"
                   data-options="required:true, validType:'TimesUI_gtpretime'"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>是否收费：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="hasfees" value=""
                 js-data='[{"id":"0","text":"不收费"},{"id":"1","text":"收费"}]'>
            </div>
        </div>
    </div>
    </form>
</div>

<script>
    function date2number(date){
        var times = date.split(/\D+/);
        if (times.length<6){
            return false;
        }
        times[1] = Number(times[1])-1;
        return new Date(times[0], times[1], times[2], times[3], times[4], times[5])
    }
    $.extend($.fn.validatebox.defaults.rules, {
        TimesUI_gtpretime: {
            validator: function(value, param){
                var prevInput = $(this).parents(".timesUI").find(".timesui-input:eq(0)");
                if (!prevInput){
                    return true;
                }
                var prevValue = prevInput.datetimebox("getValue");
                var prevDate = date2number(prevValue);
                var optDate = date2number(value);
                if (!prevDate || !optDate){
                    return true;
                }
                return prevDate <= optDate;
            },
            message: '不能小于开始时间'
        }
    });

    function FMThasfees(v){
        return v? "收费":"免费";
    }

    $(function(){

    });

    var whgListTool = new Gridopts();

    Gridopts.prototype.add = function(){
        var supplyid = '${supplyid}';
        var supplytype = '${supplytype}';
        if (!supplyid || !supplytype){
            $.messager.alert("错误", "配送标识或类型参数丢失！");
            return;
        }

        var mmx = this;
        var optDialog =$('<div></div>').dialog({
            title: '添加供需时段',
            width: 800,
            height: 310,
            cache: false,
            modal: true,
            content: '<div class="dialog-content-add body_add" style="padding: 30px 30px;"></div>',
            onClose: function(){
                optDialog.remove();
            },
            onOpen: function(){
                var content = $(this).find('.dialog-content-add');

                content.append($("#edit-panel").html());

                content.find("[class^='EEE']").each(function(i){
                    var _class = $(this).attr("class");
                    _class = _class.replace(/^E{3,}/, 'easyui');
                    $(this).attr("class", _class);

                    var _refid = $(this).attr("refid");
                    if (_refid){
                        $(this).attr("id", _refid);
                    }

                    var parent = $(this).parents(".whgff-row");
                    if (parent.find("[class^='EEE']").size()==0){
                        $.parser.parse(parent);
                    }
                });

                PsPcaTool.psprovinceEid = '__PSPROVINCE_ELE';
                PsPcaTool.pscityEid = '__PSCITY_ELE';
                PsPcaTool.psRefPCA();
                content.find("form").form('disableValidation');
            },
            buttons: [{
                text:'确认',
                iconCls: 'icon-ok',
                handler:function(){
                    $.messager.progress();

                    var content = optDialog.find('.dialog-content-add');
                    content.find("form").form('enableValidation');
                    var valid = content.find("form").form('validate');
                    if (!valid){
                        $.messager.progress('close');
                        return;
                    }

                    var frmInfo = content.find("[name]").serializeArray();
                    var params = {};
                    for(var i in frmInfo){
                        var ent = frmInfo[i];
                        if (params[ent.name]){
                            params[ent.name] += ","+ent.value;
                        }else{
                            params[ent.name] = ent.value;
                        }
                    }
                    params.supplyid = supplyid;
                    params.supplytype = supplytype;

                    mmx.__ajaxTempSend("/add", params);

                    optDialog.dialog('close');
                    optDialog.remove();
                }
            },{
                text:'取消',
                iconCls: 'icon-no',
                handler:function(){ optDialog.dialog('close');optDialog.remove();}
            }]
        });
    };

    Gridopts.prototype.edit = function(idx){
        var row = this.__getGridRow(idx);

        var supplyid = '${supplyid}';
        var supplytype = '${supplytype}';
        if (!supplyid || !supplytype){
            $.messager.alert("错误", "配送标识或类型参数丢失！");
            return;
        }

        var mmx = this;
        var optDialog =$('<div></div>').dialog({
            title: '编辑供需时段',
            width: 800,
            height: 310,
            cache: false,
            modal: true,
            content: '<div class="dialog-content-add body_add" style="padding: 30px 30px;"></div>',
            onClose: function(){
                optDialog.remove();
            },
            onOpen: function(){
                var content = $(this).find('.dialog-content-add');

                content.append($("#edit-panel").html());

                content.find("[class^='EEE']").each(function(i){
                    var _class = $(this).attr("class");
                    _class = _class.replace(/^E{3,}/, 'easyui');
                    $(this).attr("class", _class);

                    var _refid = $(this).attr("refid");
                    if (_refid){
                        $(this).attr("id", _refid);
                    }

                    var parent = $(this).parents(".whgff-row");
                    if (parent.find("[class^='EEE']").size()==0){
                        $.parser.parse(parent);
                    }
                });

                PsPcaTool.psprovinceEid = '__PSPROVINCE_ELE';
                PsPcaTool.pscityEid = '__PSCITY_ELE';
                PsPcaTool.psRefPCA();
                content.find("form").form('disableValidation');

                content.find("form").form("load", row);
                if (row.timestart){
                    content.find(".timesui-input:eq(0)").datetimebox("setValue", new Date(row.timestart).Format("yyyy-MM-dd hh:mm:ss"));
                }
                if (row.timeend){
                    content.find(".timesui-input:eq(1)").datetimebox("setValue", new Date(row.timeend).Format("yyyy-MM-dd hh:mm:ss"));
                }
            },
            buttons: [{
                text:'确认',
                iconCls: 'icon-ok',
                handler:function(){
                    $.messager.progress();

                    var content = optDialog.find('.dialog-content-add');
                    content.find("form").form('enableValidation');
                    var valid = content.find("form").form('validate');
                    if (!valid){
                        $.messager.progress('close');
                        return;
                    }

                    var frmInfo = content.find("[name]").serializeArray();
                    var params = {};
                    for(var i in frmInfo){
                        var ent = frmInfo[i];
                        if (params[ent.name]){
                            params[ent.name] += ","+ent.value;
                        }else{
                            params[ent.name] = ent.value;
                        }
                    }
                    params.id = row.id;

                    mmx.__ajaxTempSend("/edit", params);

                    optDialog.dialog('close');
                    optDialog.remove();
                }
            },{
                text:'取消',
                iconCls: 'icon-no',
                handler:function(){ optDialog.dialog('close');optDialog.remove();}
            }]
        });
    };

    Gridopts.prototype.del = function(idx){
        var row = this.__getGridRow(idx);
        var mmx = this;
        WhgComm.confirm("确认信息", "此操作将会永久性删除数据！确定要删除选中的项吗？", function(r){
            mmx.__ajaxTempSend("/del", {id: row[mmx.rowIdKey]});
        })
    };


    var PsPcaTool = {
        //配送省市
        psprovinceEid:false,
        psprovinceValue:false,
        pscityEid:false,
        pscityValue:false,

        psRefPCA: function(cultid, oldcultid){
            var opts = this;
            //无相关指定配送省市,就不需要处理了
            if (!opts.psprovinceEid || !opts.pscityEid){
                return;
            }

            //所选的文化馆信息,后台加入了省市区的数据项
           /* var cultOptions = $('#' + opts.cultEid).combobox("options") || {};
            var cultdata = cultOptions.data || [];*/
            var cultInfo = {};
            /*for(var i in cultdata){
                if (cultid == cultdata[i].id){
                    cultInfo = $.extend({}, cultdata[i]);
                    break;
                }
            }*/
            cultInfo.psprovince = '广东省'; //cultInfo.province || '广东省';

            setPsProvince();

            function setPsProvince(){
                var provinces = getProvinceData() || [];
                if (!provinces || !provinces.length){
                    setTimeout(function(){
                        setPsProvince();
                    }, 300);
                    return;
                }

                var el = $('#'+opts.psprovinceEid);
                el.combobox("loadData", provinces);

                if (opts.pscityEid){
                    var cityel = $('#'+opts.pscityEid);
                    cityel.combobox({
                        novalidate: true,
                        onChange: function (val, oldval) {
                            if (val.length>1 && val[0]==''){
                                val.shift();
                                $(this).combobox("setValues", val);
                            }
                        },
                        loadFilter: function(data){
                            if (data && data.length && data[0].name != '全省'){
                                data.unshift({name:"全省"});
                            }
                            return data;
                        },
                        onSelect: function(record){
                            if (record.name == "全省"){

                                var data = $(this).combobox("getData");
                                if (data && data.length){
                                    var values = [];
                                    for(var i in data){
                                        if (data[i].name=="全省") continue;
                                        values.push(data[i].name);
                                    }
                                    var that = this;
                                    setTimeout(function(){
                                        $(that).combobox("setValues", values);
                                        $(that).combobox("hidePanel");
                                    }, 100);

                                }
                            }
                        }
                    });
                    cityel.combobox('setValues', '');
                    el.combobox({
                        "onChange":function(newVal, oldVal){
                            if (newVal){
                                getCityData(newVal, function(citys){
                                    cityel.combobox('loadData', citys);
                                    cityel.combobox('setValues', '');

                                    var ps_city = cultInfo.pscity || '';
                                    if (opts.pscityValue && opts.pscityValue!=''){
                                        ps_city = opts.pscityValue;
                                        opts.pscityValue = ''; //初始值用一次清理
                                    }
                                    cityel.combobox('setValues', ps_city);
                                    cultInfo.pscity = ""; //值用过清理
                                });
                            }
                        }
                    });
                }

                var ps_province = cultInfo.psprovince || '';
                if (opts.psprovinceValue && opts.psprovinceValue!=''){
                    ps_province = opts.psprovinceValue;
                    opts.psprovinceValue = ''; //初始值用一次清理
                }
                el.combobox('setValue', ps_province);
            }
        }
    }

</script>
</body>
</html>
