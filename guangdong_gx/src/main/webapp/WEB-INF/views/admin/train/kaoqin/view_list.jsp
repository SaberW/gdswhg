<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>

<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>培训考勤管理</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>

</head>
<body class="easyui-layout">

<div data-options="region:'north'" style="height:40px">
    <div id="tb" style="margin: 5px;">
        <input class="easyui-combobox" id="param_traid" name="traid" prompt="请选择培训"
               data-options="editable:true,width:250,valueField:'id',textField:'title'"/>
        <input class="easyui-numberspinner" id="yyyy" name="yyyy" prompt="请选年份"/>
        <input class="easyui-numberspinner" id="mm" name="mm" prompt="请选月份"/>
        <a class="easyui-linkbutton" iconCls="icon-search" onclick="whgListTool.loadData()">查 询</a>
    </div>
</div>

<div data-options="region:'center'" >
    <div id="whgdg"></div>
</div>

<script>

    $(function(){
        var now = new Date();
        var nowyyy = now.getFullYear();
        var minyyy = nowyyy - 10;
        var nowmmm = now.getMonth()+1;
        $('#mm').numberspinner({
            width:80,
            min: 1,
            max: 12,
            value: nowmmm
        });
        $('#yyyy').numberspinner({
            width:80,
            min: minyyy,
            max: nowyyy,
            value: nowyyy
        });

        $("#param_traid").combobox({
            url: '${basePath}/admin/train/enrol/params/tralist?biz=PT&state=6&islive=0',
            onLoadSuccess: function(){
                var data = $(this).combobox('getData');
                if (data!=null && data.length > 1){
                    $(this).combobox('setValue', data[0].id);
                    whgListTool.loadData()
                }
            }
        })
    });

    var whgListTool = {
        loadData: function(){
            var self = this;
            var cords = $("#tb [name]").serializeArray();
            var params = {};
            for(var i in cords){
                var ent = cords[i];
                if (ent.value && ent.name){
                    params[ent.name] = ent.value;
                }
            }
            if (!params.traid || params.params==''){
                return false;
            }

            if (params.mm && params.mm<10){
                params.mm = '0'+params.mm;
            }

            $.ajax({
                url: '${basePath}/admin/train/kaoqin/srchlist',
                data: params,
                dataType: 'json',
                success: function(data){
                    self.showDataGrid(data);
                }
            });
        },

        showDataGrid: function(kqData){
            var columns = this.getDataGridColumns(kqData);
            var rows = kqData.rows;

            var self = this;
            $("#whgdg").datagrid({
                fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:true, checkOnSelect:true,
                frozenColumns: [[{field:'userName',title:'人员', width:100}]],
                data: rows,
                columns: columns,
                onClickCell: function(index, field, value){
                    self.cellClick(index, field, value);
                }
            })
        },

        getDataGridColumns: function(kqData){
            var kcColumns = kqData.data;
            var columns = [];
            var fields = [];

            for(var i in kcColumns){
                var field = kcColumns[i];
                fields.push({
                    field:field.coruseId,
                    title:field.title,
                    formatter:function(v,r,i){
                        if (v=='缺勤'){
                            return '<span style="color:red">'+v+'</span>'
                        }else {
                            return v;
                        }
                    }
                });
            }
            columns.push(fields);

            return columns;
        },

        cellClick: function(index, field, value){
            var self = this;
            if (value != '缺勤'){
                return;
            }

            $.messager.confirm('操作提醒','是否对此用户进行签到？',function(r){
                if (r){
                    var rows = $("#whgdg").datagrid('getRows');
                    var row = rows[index];
                    var params = {
                        enrolid: row["enrolId"],
                        traid: row["traId"],
                        courseid: field,
                        userid: row["userId"]
                    };

                    $.messager.progress();
                    $.ajax({
                        url: '${basePath}/admin/train/kaoqin/updateKq2qd',
                        data: params,
                        dataType: 'json',
                        success: function(data){
                            $.messager.progress('close');
                            if (data.success == '1'){
                                self.loadData();
                            }else{
                                $.messager.alert("提示信息",data.errormsg||'操作出错'  , "error");
                            }
                        },
                        error: function(){
                            $.messager.progress('close');
                            $.messager.alert("提示信息", '操作出错', "error");
                        }
                    });
                }
            });
        }

    };
</script>
</body>
</html>
