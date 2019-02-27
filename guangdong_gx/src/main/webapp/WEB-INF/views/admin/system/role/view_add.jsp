<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>岗位管理-添加岗位</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>
</head>
<body>


<form id="whgff" class="whgff" method="post">

    <h2>岗位管理-添加岗位</h2>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>文化馆：</div>
        <div class="whgff-row-input">
               <input class="easyui-combobox" style="width: 500px; height:32px" name="cultid" id="cultid" data-options="required:true, prompt:'请选择文化馆', limitToList:true, valueField:'id', textField:'text', data:WhgComm.getMgrCultsAndChild(), onChange:changeCult"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>名称：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="name" style="width:300px; height:32px" data-options="prompt:'请输入角色名称', required:true, validType:'length[3,30]'"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>权限：</div>
        <div class="whgff-row-input">
            <a href="javascript:void(0)" class="easyui-linkbutton" style="width:100px;height:32px;" id="chioceAll" >全&nbsp;&nbsp;选</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" style="width:100px;height:32px;" id="unchioceAll">取&nbsp;&nbsp;消</a>
            <table id="tdg_menu" style="width:80%"></table>
        </div>
    </div>

</form>

<div id="whgwin-add-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-save" id="whgwin-add-btn-save">保 存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返  回</a>
</div>

<!-- script -->
<script type="text/javascript">
    var optMap = eval('(${optMap})');
    var adminobj ="${optadmin}";

    /**
     * 修改所属文化馆
     **/
    function changeCult(newVal, oldVal) {
       if(newVal!=""){
          // doLoadTree(newVal);
       }
    }

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
            $('#pmsAll-'+rcid).parents('div').find("input[type='checkbox'][cctype='true'][id^='"+rcid+"-']").prop("checked", true);
        }else{
            $('#pmsAll-'+rcid).parents('div').find("input[type='checkbox'][cctype='true'][id^='"+rcid+"-']").prop("checked", false);
        }
    }

    /** 所有资源全选与取消 */
    function chioceAll(checked){
        if(checked){
            $("input[type='checkbox'][name='pms'][cctype='true']").prop('checked', true);
            $("input[type='checkbox'][name='pmsAll'][cctype='true']").prop('checked', true);
        }else{
            $("input[type='checkbox'][name='pms'][cctype='true']").prop('checked', false);
            $("input[type='checkbox'][name='pmsAll'][cctype='true']").prop('checked', false);
        }
    }

    function pmsOnChange(){
        $("input[type='checkbox'][name='pms']").change(function(){
            if( $(this).prop('checked') ){
                $(this).parent('label').css('color', 'red');
            }else{
                $(this).parent('label').css('color', '');
            }
        });
    }


    function doLoadTree(cultid){
        var spath='${basePath}/admin/system/role/srchMenuTree';
        if(cultid&&cultid!=""){
            spath=spath+"?cultid="+cultid;
        }
        $('#tdg_menu').treegrid({height:600,
            url:spath ,
            idField: 'id',
            treeField: 'text',
            fitColumns: true,
            singleSelect: true,
            columns:[[
                {field:'text', title:'权限资源', width:100},
                {field:'opts', title:'支持操作', width:300, formatter: pmsFMT}
            ]],
            onLoadSuccess: function (row, data) {

                var model=data.data;
                //全选与取消
                $('#chioceAll').show().off('click.wxl').on('click.wxl', function(){
                    chioceAll(true);
                });
                $('#unchioceAll').show().off('click.wxl').on('click.wxl', function(){
                    chioceAll(false);
                });
                if(adminobj&&adminobj=="true"){
                    $("input[type='checkbox']").attr("cctype", "true");
                }else {
                    if (model) {
                        //编辑时设置已有权限  pms
                        $("input[type='checkbox'][name='pms']").parent().hide();
                        $("input[type='checkbox'][name='pms']").hide();
                        var datastr = eval(model);
                        for (var i = 0; i < datastr.length; i++) {
                            $('#' + datastr[i].replace(":", "-")).attr("cctype", "true").show();
                            $('#' + datastr[i].replace(":", "-")).parent().show();
                        }
                        $('input:checkbox[name="pmsAll"]').each(function () {
                            var obj = $(this).parent().parent();
                            if ($(obj).find("input[type='checkbox'][cctype='true']").length == 0) {//只有全選 需隱藏
                                $(this).parent().hide();
                                $(this).parents("TR:eq(0)").hide();
                                $(this).parents("TR:eq(1)").prev().hide();//隐藏 大的栏目
                            } else {
                                $(this).attr("cctype", "true");
                                $(this).parents("TR:eq(1)").prev().show();
                            }
                        });
                    }
                }
            }
        });
    }

    $(function () {
        //tdg_menu
        var _height = $(window).height() - 360;
        $('#tdg_menu').css({height:_height});
        //初始权限编辑表格
        doLoadTree();
        //表单
        $('#whgff').form({
            novalidate: true,
            url : getFullUrl('/admin/system/role/add'),
            onSubmit : function(param) {
                //alert('onSubmit');
                var _valid = $(this).form('enableValidation').form('validate')
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
