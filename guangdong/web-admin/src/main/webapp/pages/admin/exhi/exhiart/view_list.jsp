<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>数字展览作品管理</title>
    <%@include file="/pages/comm/admin/header.jsp"%>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
    <script type="text/javascript" src="${basePath}/static/common/js/easyui.wh.tools.js"></script>
</head>
<body>
<!-- 表格 -->
<table id="whgdg" title="数字展览作品管理" class="easyui-datagrid" style="display: none"
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:false, checkOnSelect:true, selectOnCheck:true, pagination:true, toolbar:'#whgdg-tb', url:'${basePath}/admin/exhi/exhiart/srchList4p?artexhid='+'${exhid}'">
    <thead>
    <tr>
        <th data-options="field:'ck', checkbox:true"></th>
        <th data-options="field:'arttitle'<%--, width:100--%>">标题</th>
        <th data-options="field:'artauthor'<%--, width:50--%>">作者</th>
        <th data-options="field:'artpic'<%--, width:80--%>, formatter:WhgComm.FMTImg">作品图片</th>
        <th data-options="field:'artcrttime'<%--, width:80--%>,formatter:WhgComm.FMTDateTime">创建时间</th>
        <th data-options="field:'artidx'<%--, width:30--%>">排序</th>
        <th data-options="field:'artstate'<%--, width:50--%>, formatter:WhgComm.FMTState">状态</th>
        <th data-options="field:'_opt', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>
<!-- 表格 END -->

<!-- 表格操作工具栏 -->
<div id="whgdg-tb" style="display: none;">
    <div class="whgd-gtb-btn">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="doAdd();">添加</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-no" onclick="doBatchUpd(1,0);">批量停用</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doBatchUpd(0,1);">批量启用</a>
        <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
    </div>
    <div class="whgdg-tb-srch">
        <input class="easyui-textbox" style="width: 200px;" name="arttitle" id="srch_arttitle" data-options="prompt:'请输入标题', validType:'length[1,32]'" />
        <input class="easyui-textbox" style="width: 200px;" name="artauthor" id="srch_artauthor" data-options="prompt:'请输入作者', validType:'length[1,32]'" />
        <input class="easyui-combobox" style="width: 200px;" name="artstate" id="srch_artstate" data-options="prompt:'请选择状态', value:'', valueField:'id', textField:'text', data:WhgComm.getState()"/>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#whgdg-tb');">查 询</a>
    </div>
</div>
<!-- 表格操作工具栏-END -->

<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">
    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="doSee">查看</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="artstate" validVal="0" method="doEdit">编辑</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="artstate" validVal="1" method="doOff">停用</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="artstate" validVal="0" method="doOn">启用</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validKey="artstate" validVal="0" method="doDel">删除</a>
    <%--<a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="canSort" method="doSort">上移</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" validFun="canSort" method="doTop">置顶</a>--%>
    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="setSort">设置排序值</a>
    <%--<a href="javascript:void(0)" class="easyui-linkbutton" plain="true" method="resource">资源管理</a>--%>
</div>
<!-- 操作按钮-END -->

<!-- 添加表单 -->
<div id="whgwin-add"></div>
<div id="whgwin-add-btn" style="text-align: center; display: none">
    <div style="display: inline-block; margin: 0 auto">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="whgwin-add-btn-save">保 存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#whgwin-add').dialog('close')">关 闭</a>
    </div>
</div>
<!-- 添加表单 END -->

<!-- 编辑表单 -->
<div id="whgwin-edit"></div>
<!-- 编辑表单 END -->


<div id="whgwin-view"></div>

<div id="idx_win" style="display: none">
    <form method="post">
        <div class="main">
            <div class="row">
                <div><label>排序：</label></div>
                <div>
                    <input name="artidx" class="easyui-numberbox" style="width:90%;height:35px" required="true"/>
                </div>
            </div>
        </div>
    </form>
</div>


<!-- script -->
<script type="text/javascript">
    var winform = new WhuiWinForm("#idx_win",{width:350,height:180});
    $(function(){
        winform.init();
        winform.setWinTitle("设置排序");
        winform.setFormStyleTemp({
            mainTop:'8px',
            formWidth : '90%',
            lableWidth : '80px'
        });
    });


    function setSort(idx){
        var curRow = $('#whgdg').datagrid('getRows')[idx];

        winform.openWin();
        var _form = winform.getWinBody().find("form").form({
            url : getFullUrl('/admin/exhi/exhiart/setsort'),
            onSubmit : function(param){
                param.artid = curRow.artid;
                return $(this).form('enableValidation').form('validate');
            },
            success : function(data){
                data = eval("("+data+")");
                if (data.success == "1"){
                    $.messager.alert("提示","设置排序成功");
                    $('#whgdg').datagrid("reload");
                    winform.closeWin();
                }else{
                    $.messager.alert("提示","设置排序失败");
                }
            }
        });

        var data = {artidx: curRow.artidx};
        _form.form("load", data);

        winform.setFoolterBut({onClick:function(){
                _form.submit();
            }})
    }

    /**
     * 资源管理
     * @param idx
     */
    function resource(idx) {
        var row = $("#whgdg").datagrid("getRows")[idx];
        WhgComm.editDialog('${basePath}/admin/train/resource/view/list?reftype=14&id=' + row.artid);
    }

    /**
     * 添加展览作品
     */
    function doAdd() {
        WhgComm.editDialog('${basePath}/admin/exhi/exhiart/view/add?exhid='+'${exhid}');
    }

    /**
     * 编辑信息
     * @param idx 行下标
     */
    function doEdit(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/exhi/exhiart/view/edit?artid='+curRow.artid);
    }

    /**
     * 查看
     * @param idx 行下标
     */
    function doSee(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        WhgComm.editDialog('${basePath}/admin/exhi/exhiart/view/edit?targetShow=1&artid='+curRow.artid);
    }

    /**
     * 批量启用或停用
     * @param fromState 修改前的状态
     * @param toState 修改后的状态
     */
    function doBatchUpd(fromState, toState) {
        //选中的记录数
        var rows = $('#whgdg').datagrid('getChecked');
        if(rows.length < 1){
            $.messager.alert('提示', '请选中要操作的记录！', 'warning');
        }
        var _ids = ""; var spt = "";
        for(var i=0; i<rows.length; i++){
            _ids += spt+rows[i].artid;
            spt = ',';
        }
        _doUpdState(_ids, fromState, toState);
    }

    /**
     * AJAX调用修改状态服务
     * @param ids 修改对象ID，多个用逗号分隔
     * @param fromState 修改前的状态
     * @param toState 修改后的状态
     * @private
     */
    function _doUpdState(ids, fromState, toState){
        $.ajax({
            url: getFullUrl('/admin/exhi/exhiart/updstate'),
            data: {ids:ids, fromState:fromState, toState:toState},
            success: function (data) {
                if(data && data.success == '1'){
                    $('#whgdg').datagrid('reload');
                } else {
                    $.messager.alert('提示', '操作失败:'+data.errormsg+'！', 'error');
                }
            }
        });
    }

    /**
     * 单个启用
     * @param idx 行下标
     */
    function doOn(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        _doUpdState(curRow.artid, 0, 1);
    }

    /**
     * 单个停用
     * @param idx 行下标
     */
    function doOff(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        _doUpdState(curRow.artid, 1, 0);
    }

    /**
     * 删除
     * @param idx
     */
    function doDel(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        $.messager.confirm('提示', '您确定要删除此记录吗？', function(r){
            if (r){
                $.ajax({
                    type: "POST",
                    url: getFullUrl('/admin/exhi/exhiart/del'),
                    data: {artid : curRow.artid},
                    success: function(Json){
                        if(Json && Json.success == '1'){
                            //$.messager.alert('提示', '操作成功！', 'info');
                            $('#whgdg').datagrid('reload');
                        } else {
                            $.messager.alert('提示', '操作失败:'+Json.errormsg+'！', 'error');
                        }
                    }
                });
            }
        });
    }

    /**
     * 上移
     * @param idx
     */
    function doSort(idx){
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        var srch_arttitle = $('#srch_arttitle').textbox('getValue');
        var srch_artauthor = $('#srch_artauthor').textbox('getValue');
        var srch_artstate = $('#srch_artstate').combobox('getValue');
        $.ajax({
            type: "POST",
            cache: false,
            url: getFullUrl('/admin/exhi/exhiart/sort'),
            data: {artid : curRow.artid, artexhid:curRow.artexhid, type:'up', artauthor:srch_arttitle, arttitle:srch_arttitle, artstate:srch_artstate},
            success: function(Json){
                if(Json && Json.success == '1'){
                    $('#whgdg').datagrid('reload');
                } else {
                    $.messager.alert('提示', '操作失败:'+Json.errormsg+'！', 'error');
                }
            }
        });
    }

    /**
     * 置顶
     * @param idx
     */
    function doTop(idx){
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        $.ajax({
            type: "POST",
            cache: false,
            url: getFullUrl('/admin/exhi/exhiart/sort'),
            data: {artid : curRow.artid, artexhid:curRow.artexhid, type:'top'},
            success: function(Json){
                if(Json && Json.success == '1'){
                    $('#whgdg').datagrid('reload');
                } else {
                    $.messager.alert('提示', '操作失败:'+Json.errormsg+'！', 'error');
                }
            }
        });
    }

    /**
     * 是否可以上移和置顶
     * @param idx
     * @returns {boolean}
     */
    function canSort(idx) {
        var curRow = $('#whgdg').datagrid('getRows')[idx];
        if(curRow.artidx != '1'){
            return true;
        }
        return false;
    }

</script>
<!-- script END -->
</body>
</html>