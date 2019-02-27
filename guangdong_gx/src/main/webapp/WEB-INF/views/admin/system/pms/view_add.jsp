<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>权限组管理-添加权限组</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
</head>
<body>

<form id="whgff" class="whgff" method="post">
    <input type="hidden" id="sysflag" name="sysflag" value="${sysflag}"/>

    <h2>权限组管理-添加权限组</h2>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>权限组名称：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="name" style="width:500px; height:32px" data-options="prompt:'请输入权限组名称，最多可输入60个字符。', required:true, validType:'length[3,60]'" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>权限组分类：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" name="type" style="width:500px; height:32px" data-options="prompt:'请输入权限组分类，最多可输入30个字符。', required:true, validType:'length[3,30]', valueField:'id', textField:'text', url:'${basePath}/admin/system/pms/srchList4PmsType?sysflag=${sysflag}'" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>功能配置　：</div>
        <div class="whgff-row-input">
            <div style="margin-bottom: 5px;">
                <a href="javascript:void(0)" class="easyui-linkbutton" style="width:100px;height:32px;" id="chioceAll" >全&nbsp;&nbsp;选</a>
                <a href="javascript:void(0)" class="easyui-linkbutton" style="width:100px;height:32px;" id="unchioceAll">取&nbsp;&nbsp;消</a>
            </div>
            <div style="width: 95%; height: 400px; ">
                <table id="tdg_menu" style="overflow: auto;"></table>
            </div>
        </div>
    </div>

</form>

<div id="whgwin-add-btn" class="whgff-but" style="width: 400px; margin:20px 0px 10px 350px">
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-save" id="whgwin-add-btn-save">保 存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返  回</a>
</div>

<!-- script -->
<script type="text/javascript">
    /** 操作说明:{"checkoff":"审核打回"} */
    var optMap = eval('(${optMap})');

    /** 格式化权限 操作 */
    function pmsFMT(val, data, idx){
        var html = "";
        if(val){
            var pmsArr = val.split(',');
            if(pmsArr.length > 0){
                html += '<label><input type="checkbox" name="pmsAll" cctype="false" value="0" id="pmsAll-'+data.id+'" onchange="chiocePMS(\''+data.id+'\');" />全选</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
                for(var i=0; i<pmsArr.length; i++){
                    var pms = pmsArr[i];
                    var pmsid = $.trim(pms);
                    var pmstext = optMap[pmsid];
                    html += '<label><input type="checkbox" rcid="'+data.id+'" cctype="false" name="pms" id="'+data.id+'-'+pmsid+'" value="'+data.id+':'+pmsid+'" />'+pmstext+'</label>&nbsp;&nbsp;';
                }
            }
        }
        return html;
    }

    /** 单个资源全选 */
    function chiocePMS(rcid){
        if( $('#pmsAll-'+rcid).prop("checked") ){
            $('#pmsAll-'+rcid).parents('div').find("input[type='checkbox'][id^='"+rcid+"-']").prop("checked", true);
        }else{
            $('#pmsAll-'+rcid).parents('div').find("input[type='checkbox'][id^='"+rcid+"-']").prop("checked", false);
        }
    }

    /** 所有资源全选与取消 */
    function chioceAll(checked){
        if(checked){
            $("input[type='checkbox'][name='pms']").prop('checked', true);
            $("input[type='checkbox'][name='pmsAll']").prop('checked', true);
        }else{
            $("input[type='checkbox'][name='pms']").prop('checked', false);
            $("input[type='checkbox'][name='pmsAll']").prop('checked', false);
        }
    }

    function doLoadTree(height){
        var spath = '${basePath}/admin/system/pms/srchMenuTree?sysflag='+$('#sysflag').val();
        $('#tdg_menu').treegrid({
            height:height,
            url:spath ,
            idField: 'id',
            treeField: 'text',
            fitColumns: false,
            singleSelect: true,
            columns:[[
                {field:'text', title:'功能菜单'},
                {field:'opts', title:'权限操作', formatter: pmsFMT}
            ]],
            onLoadSuccess: function (row, data) {
                //全选与取消
                $('#chioceAll').show().off('click.wxl').on('click.wxl', function(){
                    chioceAll(true);
                });
                $('#unchioceAll').show().off('click.wxl').on('click.wxl', function(){
                    chioceAll(false);
                });
            }
        });
    }

    $(function () {
        //tdg_menu
        var _height = $(window).height() - 365;
        $('#tdg_menu').parent('div').css({height:_height});

        //初始权限编辑表格
        doLoadTree(_height);

        //表单
        $('#whgff').form({
            novalidate: true,
            url : getFullUrl('/admin/system/pms/add'),
            onSubmit : function(param) {
                var _valid = $(this).form('enableValidation').form('validate');
                if(_valid){
                    $.messager.progress();
                }else{
                    //失败时再注册提交事件
                    $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
                }
                return _valid;
            },
            success : function(data) {
                $.messager.progress('close');
                var Json = eval('('+data+')');
                if(Json && Json.success == '1'){
                    window.parent.$('#whgdg').datagrid('reload');
                    WhgComm.editDialogClose();
                } else {
                    $.messager.alert('提示', '操作失败:'+Json.errormsg+'！', 'error');
                    $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
                }
            }
        });
        //注册提交事件
        $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
    });
</script>
<!-- script END -->
</body>
</html>
