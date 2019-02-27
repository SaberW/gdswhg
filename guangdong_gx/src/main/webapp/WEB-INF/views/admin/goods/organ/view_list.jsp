<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>

    <c:choose>
        <c:when test="${pageType eq 'listdel'}">
            <c:set var="pageTitle" value="回收站"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="特色团队列表"></c:set>
        </c:otherwise>
    </c:choose>

    <title>${pageTitle}</title>

    <script src="${basePath}/static/admin/js/whgmodule-gridopts.js"></script>
</head>
<body>
<table id="whgdg" class="easyui-datagrid" title="${pageTitle}" style="display: none;"
       pagination=true
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:false, checkOnSelect:true, selectOnCheck:true, toolbar:'#whgdg-tb'">
    <thead>
    <tr>
        <th data-options="width:150, sortable:false, field:'title' ">机构名称</th>
        <th data-options="width:100, sortable:false, field:'legalperson' ">法人</th>
        <th data-options="width:100, sortable:false, field:'contacts' ">负责人</th>
        <th data-options="width:150, sortable: true, field:'address' ">地址</th>

        <th data-options="field:'crtuser',width:120, formatter:WhgComm.FMTUserName">编辑者</th>
        <th data-options="field:'checkor',width:120, formatter:WhgComm.FMTUserName">审核者</th>
        <th data-options="field:'publisher',width:120, formatter:WhgComm.FMTUserName">发布者</th>
        <th data-options="field:'statemdfdate',width:130, formatter:WhgComm.FMTDateTime, sortable:true">操作时间</th>
        <th data-options="field:'state',width:50, formatter:WhgComm.FMTBizState" >状态</th>

        <th data-options="field:'id', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>


<div id="whgdg-tb" style="display: none;">
    <shiro:hasPermission name="${resourceid}:add">
    <div class="whgd-gtb-btn">
        <a class="easyui-linkbutton" iconCls="icon-add" onclick="whgListTool.add()">添 加</a>
    </div>
    </shiro:hasPermission>
    <div class="whgdg-tb-srch">
        <input class="easyui-combobox" name="cultid" id="cultid" data-options="editable:false,width:150" prompt="请选择文化馆"/>
        <input class="easyui-combobox" name="deptid" id="deptid" data-options="editable:false,width:150" prompt="请选择部门"/>

        <input class="easyui-textbox" name="title" prompt="请输入组织机构名称" data-options="width:200">

        <c:if test="${pageType ne 'listdel'}">
            <input class="easyui-combobox" name="state" id="state" prompt="请选择状态" data-options="editable:false,width:110, panelHeight:'auto', limitToList:true"/>
        </c:if>

        <a class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>


<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <a plain="true" method="whgListTool.view">查看</a>

    <shiro:hasPermission name="${resourceid}:edit">
        <a plain="true" method="whgListTool.edit" validFun="whgListTool.optValid4EditState">编辑</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:ziyuan">
        <a plain="true" method="whgListTool.resource" validFun="whgListTool.optValid4PageTypeState">演出作品管理</a>
    </shiro:hasPermission>

    <shiro:hasPermission name="${resourceid}:checkgo">
        <a plain="true" method="whgListTool.checkgo" validFun="whgListTool.optValid4EditState">提交审核</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkon">
        <a plain="true" method="whgListTool.checkon" validFun="whgListTool.optValid4EditState">审核通过</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkoff">
        <a plain="true" method="whgListTool.checkoff" validFun="whgListTool.optValid4EditState">审核打回</a>
    </shiro:hasPermission>

    <shiro:hasPermission name="${resourceid}:publish">
        <a plain="true" method="whgListTool.publish" validFun="whgListTool.optValid4EditState">发布</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:publishoff">
        <a plain="true" method="whgListTool.publishoff" validKey="state" validVal="6">撤销发布</a>
    </shiro:hasPermission>

    <shiro:hasPermission name="${resourceid}:recovery">
        <a plain="true" method="whgListTool.recovery" validFun="whgListTool.optValid4RecoveryState">回收</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:undel">
        <a plain="true" method="whgListTool.undel" validKey="delstate" validVal="1">撤销回收</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:del">
        <a plain="true" method="whgListTool.del" validFun="whgListTool.optValid4Del">删除</a>
    </shiro:hasPermission>

    <%--<shiro:hasPermission name="${resourceid}:view"><a plain="true" method="view">查看</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:edit"><a plain="true" method="edit" validKey="state" validVal="1,9,2,4">编辑</a></shiro:hasPermission>
    &lt;%&ndash;<shiro:hasPermission name="${resourceid}:ziyuan"><a plain="true" method="resource">演出作品</a></shiro:hasPermission>&ndash;%&gt;
    <shiro:hasPermission name="${resourceid}:checkgo"><a plain="true" method="checkgo" validKey="state" validVal="1">送审</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkon"><a plain="true" method="checkon" validKey="state" validVal="9">审核通过</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkoff"><a plain="true" method="checkoff" validKey="state" validVal="9,2">打回</a></shiro:hasPermission>

    <shiro:hasPermission name="${resourceid}:publish"><a plain="true" method="publish" validKey="state" validVal="2,4">发布</a></shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:publishoff"><a plain="true" method="publishoff" validKey="state" validVal="6">撤销发布</a></shiro:hasPermission>

    <shiro:hasPermission name="${resourceid}:del"><a plain="true" method="del" validKey="state" validVal="1,9,2,4">删除</shiro:hasPermission>--%>

</div>
<!-- 操作按钮-END -->

<!--发布设置发布时间表单 -->
<%--<div id="whgwin-add" style="display: none">
    <form id="whgff" class="whgff" method="post">
        <div class="whgff-row">
            <div class="whgff-row-label _check" style="width: 30%"><i>*</i>发布时间：</div>
            <div class="whgff-row-input" style="width: 70%">
                <input class="easyui-datetimebox statemdfdate" name="optTime" style="width:200px; height:32px" data-options="required:true">
            </div>
        </div>
    </form>
</div>
<div id="whgwin-add-btn" style="text-align: center; display: none">
    <div style="display: inline-block; margin: 0 auto">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="btn" >确 定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#whgwin-add').dialog('close')">关 闭</a>
    </div>
</div>--%>
<!-- 发布设置发布时间表单 END -->
<script>

    function FMThasfees(v){
        return v? "收费":"免费";
    }

    $(function () {
        //条件查询-文化馆
        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'', allcult:true,
            deptEid:'deptid', deptValue:'', alldept:true
        });

        $("#state").combobox({
            valueField:'id',
            textField:'text',
            value: whgListTool.getDefaultState4PageType(),
            data: whgListTool.getDataState4PageType()
        });

        WhgComm.search('#whgdg', '#whgdg-tb', '${basePath}/admin/showOrgan/srchList4p?pageType=${pageType}');
    });

    var whgListTool = new Gridopts({pageType: "${pageType}"});

    Gridopts.prototype.resource = function(idx){
        var row = this.__getGridRow(idx);
        //var cultid = $('#cultid').combobox('getValue');
        //WhgComm.editDialog('${basePath}/admin/resource/view/list?reftype=24&refid=' + row.id+"&cultid="+cultid);
        WhgComm.editDialog('${basePath}/admin/mass/resmanage/view/list?refid=' + row.id+'&reftype=24');
    };


    /**
     * 编辑
     * @param idx
     */
    /*function add(){
        WhgComm.editDialog('${basePath}/admin/showOrgan/view/edit');
    }*/


    /**
     * 编辑
     * @param idx
     */
    /*function edit(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/showOrgan/view/edit?pageType=1&id='+row.id);
    }*/

    /**
     * 查看
     * @param idx
     */
    /*function view(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/showOrgan/view/edit?pageType=2&id='+row.id);
    }*/

    /**
     * 发布 [2,4]->6
     * @param idx
     */
    /*function publish(idx){
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
                    url : '${basePath}/admin/showOrgan/updstate',
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
    }*/

    /**
     * 取消发布 [6]->4
     * @param idx
     */
    /*function publishoff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要取消发布选中的项吗？", function(r){
            if (r){
                __updStateSend(row.id, 6, 4);
            }
        })
    }*/

    /**
     * 送审 [1,5]->9
     * @param idx
     */
    /*function checkgo(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要送审选中的项吗？", function(r){
            if (r){
                __updStateSend(row.id, "1,5", 9);
            }
        })
    }*/

    /**
     * 审通过 [9]->2
     * @param idx
     */
    /*function checkon(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        $.messager.confirm("确认信息", "确定要审核选中的项吗？", function(r){
            if (r){
                __updStateSend(row.id, 9, 2);
            }
        })
    }*/

    /**
     * 审不通过 [9]->5
     * @param idx
     */
    /*function checkoff(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        if(row.state == 4){
            $.messager.alert("提示", "撤销发布的不可以进行打回操作！");
            return;
        }
        $.messager.confirm("确认信息", "确定要打回选中的项吗？", function(r){
            if (r){
                __updStateSend(row.id, "9,2", 1);
            }
        })
    }*/

    /**
     * 修改状态提交
     * @param ids
     * @param formstates
     * @param tostate
     * @private
     */
    /*function __updStateSend(ids, formstates, tostate){
        $.messager.progress();
        var params = {ids: ids, formstates: formstates, tostate: tostate};
        $.post('${basePath}/admin/showOrgan/updstate', params, function(data){
            $("#whgdg").datagrid('reload');
            if (!data.success || data.success != "1"){
                $.messager.alert("错误", data.errormsg||'操作失败', 'error');
            }
            $.messager.progress('close');
        }, 'json');
    }*/

    /**
     * 删除
     * @param idx
     */
    /*function del(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        var confireStr = '确定要删除选中的项吗？'
        if (row.delstate == 1){
            confireStr = '此操作将会永久性删除数据！'+ confireStr;
        }
        $.messager.confirm("确认信息", confireStr, function(r){
            if (r){
                $.messager.progress();
                $.post('${basePath}/admin/showOrgan/del', {id: row.id}, function(data){
                    $("#whgdg").datagrid('reload');
                    if (!data.success || data.success != "1"){
                        $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                    }
                    $.messager.progress('close');
                }, 'json');
            }
        })
    }*/

    /**
     * 资源
     * @param idx
     */
    /*function resource(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        var cultid = $('#cultid').combobox('getValue');
        WhgComm.editDialog('${basePath}/admin/resource/view/list?reftype=24&refid=' + row.id+"&cultid="+cultid);
    }*/

</script>
</body>
</html>
