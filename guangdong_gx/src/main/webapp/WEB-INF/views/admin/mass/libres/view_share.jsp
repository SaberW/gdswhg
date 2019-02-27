<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>

    <title>分享资源库</title>
    <style type="text/css">

    </style>
</head>
<body>
    <input type="hidden" value="${libid}" id="libid">
    <div style="float: left;border: 1px #D4D4D4 solid;width: 300px;height:490px;margin: 0px 10px 10px 10px;overflow: auto;">
        <h5 style="margin-left: 10px;">文化馆</h5>
        <div data-options="region:'west',title:'文化馆',split:true, width:'20%'" >
            <div class="easyui-tree" id="mass_type_tree"></div>
        </div>
    </div>
    <div style="float: left;border: 0px #D4D4D4 solid;width: 100px;height:490px;margin: 0px 10px 10px 10px;text-align: center;">
        <p style="margin-top: 200px;"><div><button style="width: 60px;height: 30px;" onclick="add()">>></button></div></p>
        <p style="margin-top: 30px;"><div><button style="width: 60px;height: 30px;" onclick="remove()"><<</button></div></p>
    </div>
    <div style="float: left;border: 1px #D4D4D4 solid;width: 280px;height:490px;margin: 0px 10px 10px 10px;overflow: auto;">
        <h5 style="margin-left: 10px;">已授权文化馆</h5>
        <div id="authDiv">

        </div>
    </div>
</body>
<script type="text/javascript">
    $(function(){
        // 初始化文化馆树
        WhgCommAreaTree.initAreaTree({
            eleId: 'mass_type_tree',
            selectFirstNode: false,
            loadCult: true,
            onSelect: function(node){
                var gridParams = {
                    iscult: node.iscult || '',
                    refTreeId: node.id,
                    pcalevel: node.level,
                    pcatext: node.text
                };
            }
        })

        // 初始化 已授权文化馆
        initShare();

        // 确认按钮
        WhgComm.getOpenDialogSubmitBtn().one('click', submit);
    });

    // 初始化 已授权文化馆
    function initShare() {
        var libid = $("#libid").val();
        $.ajax({
            url: '${basePath}/admin/mass/libres/getAuthorized',
            data: {libid: libid},
            type: 'post',
            dataType: 'json',
            success: function(data){
                if(data.success == '1'){
                    for(var i = 0; i < data.data.length; i++){
                        var obj = data.data[i];
                        $("#authDiv").append("<div class='"+ obj.id +"' style='margin: 10px;font-size: 12px;'><label><input type='checkbox' name='authCheck' value='"+ obj.id +"'/>"+ obj.text +"</label></div>");
                    }
                }
            },
            error: function(){

            }
        });
    }
    
    // 添加授权文化馆
    function add() {
        var id = $("#mass_type_tree").tree('getSelected').id;
        var iscult = $("#mass_type_tree").tree('getSelected').iscult;
        var text = $("#mass_type_tree").tree('getSelected').text;
        if(iscult == '1'){
            if($("#authDiv").find("div." + id).length <= 0){// 不存在
                $("#authDiv").append("<div class='"+ id +"' style='margin: 10px;font-size: 12px;'><label><input type='checkbox' name='authCheck' value='"+ id +"'/>"+ text +"</label></div>");
            }
        }else{
            $.messager.alert("温馨提示", '请选择文化馆', 'warning');
        }
    }

    // 移除已授权文化馆
    function remove() {
        if($('#authDiv input[type=checkbox]:checked').length > 0){
            $.each($('#authDiv input:checkbox:checked'),function(){
                $(this).parent().parent().remove();
            });
        }else{
            $.messager.alert("温馨提示", '请勾选要移除的已授权文化馆', 'warning');
        }
    }

    // 提交
    function submit() {
        var ids = "";
        $.each($('#authDiv div'),function(){
            ids += $(this).attr("class") + ",";
        });
        var libid = $("#libid").val();
        $.ajax({
            url: '${basePath}/admin/mass/libres/shareLib',
            data: {libid: libid, cultid: ids},
            type: 'post',
            async: false,
            dataType: 'json',
            success: function(data){
                if(data.success == '1'){
                    $.messager.alert("温馨提示", '授权文化馆成功', 'info',
                        function(){
                            // 关闭当前窗口
                            WhgComm.editDialogClose();
                        }
                    );
                }else {
                    // 重新绑定提交事件
                    WhgComm.getOpenDialogSubmitBtn().one('click', submit);
                }
            },
            error: function(){
                $.messager.alert("温馨提示", '系统错误', 'error');
                // 重新绑定提交事件
                WhgComm.getOpenDialogSubmitBtn().one('click', submit);
            }
        });
    }
</script>
</html>