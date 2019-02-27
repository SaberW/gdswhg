<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>权限组管理-适应站点</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <style>
        .ul-list{ list-style: none; font-size: 14px; }
        .ul-list>li{ width: 33.3%; float: left; }
        .red{ color:red; }
    </style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'区域',collapsible:false,split:true,tools:[{iconCls:'icon-reload', handler:reloadTree}]" style="width:200px;padding:10px;">
    <ul class="easyui-tree" id="mass_type_tree"></ul>
</div>
<div data-options="region:'center'" style="border:0;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'north',title:'配置到区域下所有文化馆',split:false,collapsible:false" style="height:85px;">
            <div style="line-height: 32px; border-top: 1px solid #ccc; font-size: 14px; padding-left: 5px;">
                <label style="margin-right: 10px;"><input style="margin: 10px 2px 0 0;" type="checkbox" name="scopeArea" value="3" />权限组自动适应到选中区域下所有新发布的文化馆</label>
            </div>
        </div>
        <div data-options="region:'center',title:'配置到文化馆'">
            <div class="easyui-panel" data-options="fit:true">
                <div style="height: 32px; line-height: 32px; border-bottom: 1px solid #ccc; font-size: 14px; padding-left: 5px;">
                    <span>筛选文化馆级别：</span>
                    <label style="margin-right: 10px;"><input style="margin: 10px 2px 0 0;" type="checkbox" name="cultlevel" value="1" />省级馆</label>
                    <label style="margin-right: 10px;"><input style="margin: 10px 2px 0 0;" type="checkbox" name="cultlevel" value="2" />市级馆</label>
                    <label style="margin-right: 10px;"><input style="margin: 10px 2px 0 0;" type="checkbox" name="cultlevel" value="3" />区级馆</label>
                </div>
                <div style="height: 32px; line-height: 32px;">
                    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="chioceAll();">全　　选</a>
                    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="unChioceAll();">取消全选</a>
                </div>
                <div>
                    <ul class="ul-list" id="my-cults">
                        <li><label><input type="checkbox" name="cult" value="3r2342"/>广东省文化馆2</label></li>
                        <li><label><input type="checkbox" name="cult" value="3r2343"/>广东省文化馆3</label></li>
                        <li><label><input type="checkbox" name="cult" value="3r2344"/>广东省文化馆4</label></li>
                        <li><label><input type="checkbox" name="cult" value="3r2345"/>广东省文化馆5</label></li>
                        <li><label><input type="checkbox" name="cult" value="3r2346"/>广东省文化馆6</label></li>
                        <li><label><input type="checkbox" name="cult" value="3r2347"/>广东省文化馆7</label></li>
                        <li><label><input type="checkbox" name="cult" value="3r2348"/>广东省文化馆8</label></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- script -->
<script type="text/javascript">
    //本权限组适应的文化馆
    var choiceCults = eval('(${choiceCults})');

    $(function(){
        //初始省市区树
        initMassTypeTree();

        //注册筛选事件
        $('input[name="cultlevel"]').change(function(){
            loadCult(getArea(), getCultLevel());
        });

        //注册保存事件
        WhgComm.getOpenDialogSubmitBtn().one('click', submit);
    });

    //提交保存
    function submit(){
        //当前选中的文化馆
        var delArr = [];
        var addArr = [];
        $('#my-cults input[name="cult"]').each(function () {
            if($(this).parent('label').hasClass("red")){
                if($(this).is(':checked')){
                    addArr.push($(this).val());
                }else{
                    delArr.push($(this).val());
                }
            }
        });
        //区域配置
        var scopeArea = false;
        if($('input[name="scopeArea"]').parent('label').hasClass("red")){
            var selectedNode = $('#mass_type_tree').tree('getSelected');
            if($('input[name="scopeArea"]').is(':checked')){
                scopeArea = 'add,'+selectedNode.level+','+selectedNode.text;
            }else{
                scopeArea = 'del,'+selectedNode.level+','+selectedNode.text;
            }
        }

        if(delArr.length < 1 && addArr.length < 1 && !scopeArea){
            $.messager.alert('警告','未做任何配置！','warning');
            WhgComm.getOpenDialogSubmitBtn().one('click', submit);
        }else{
            $.ajax({
                type: "POST",
                url: '${basePath}/admin/system/pms/saveScope',
                data: {"pmsid":'${pmsgroup.id}', "dels":delArr.join(","), "adds":addArr.join(","), scopeArea:scopeArea?scopeArea:''},
                cache:false,
                success: function(data){
                    if(data.success != 1){
                        $.messager.alert('警告','保存失败：'+data.errormsg+'！','warning');
                    }else {
                        choiceCults = data.data;
                        loadCult(getArea(), getCultLevel());

                        var selectedNode = $('#mass_type_tree').tree('getSelected');
                        loadScopeArea('${pmsgroup.id}', selectedNode.level, selectedNode.text);
                        $.messager.alert('提示','保存成功！','info', function(){
                            WhgComm.editDialogClose();
                        });
                    }
                    WhgComm.getOpenDialogSubmitBtn().one('click', submit);
                }
            });
        }
    }

    //初始tree
    function initMassTypeTree(){
        WhgCommAreaTree.initAreaTree({
            eleId: 'mass_type_tree',
            selectFirstNode: true,
            onSelect: function(node, firstLevel, areaInfo){
                if(firstLevel == 2){
                    $('input[name="cultlevel"][value="1"]').parent('label').hide();
                }else if(firstLevel == 3){
                    $('input[name="cultlevel"][value="1"]').parent('label').hide();
                    $('input[name="cultlevel"][value="2"]').parent('label').hide();
                }
                loadCult(getArea(), getCultLevel());
                loadScopeArea('${pmsgroup.id}', node.level, node.text);
            }
        });
    }

    /**
     * 加载权限组是否适应到指定区域
     */
    function loadScopeArea(pmsid, areaLevel, areaVal){
        $.ajax({
            type: "POST",
            url: '${basePath}/admin/system/pms/srchScopeArea',
            data: {pmsid:pmsid, areaLevel:areaLevel, areaVal:areaVal},
            cache:false,
            success: function(data){
                if(data.data){
                    $('input[name="scopeArea"]').attr('defaultCheck', "checked");
                    $('input[name="scopeArea"]').prop("checked", true);
                }else{
                    $('input[name="scopeArea"]').attr('defaultCheck', '');
                    $('input[name="scopeArea"]').prop("checked", false);
                }
                $('input[name="scopeArea"]').parent('label').removeClass('red');
                $('input[name="scopeArea"]').change(function(){
                    setRed($(this));
                });
            }
        });
    }

    //刷新Tree
    function reloadTree(){
        initMassTypeTree();
    }

    /** 获取当前选中的文化馆级别 */
    function getCultLevel(){
        var valArr = [];
        $('input[name="cultlevel"]:checked').each(function(){
            valArr.push($(this).val());
        });
        return valArr.join(",");
    }

    //获取选中的省市区
    function getArea(){
        var selectedNode = $('#mass_type_tree').tree('getSelected');
        var areaObj = getProvinceCityArea(selectedNode.text);
        delete areaObj.level;
        if(areaObj.area == '') delete areaObj.area;
        if(areaObj.city == '') delete areaObj.city;
        return areaObj;
    }

    //指定文化馆是否已经被适应
    function isChecked(cultid){
        var isChecked = false;
        for(var i=0; i<choiceCults.length; i++){
            if(choiceCults[i].cultid == cultid){
                isChecked = true;
                break;
            }
        }
        return isChecked;
    }

    //加载文化馆列表
    function loadCult(area, cultLevel){
        var params = $.extend(area, {"cultLevel": cultLevel, "state":6, "delstate":0});
        $.ajax({
            type: "POST",
            url: '${basePath}/admin/system/cult/srchList',
            data: params,
            cache:false,
            success: function(cults){
                if($.isArray(cults)){
                    var html = '';
                    for(var i=0; i<cults.length; i++){
                        var t_cult = cults[i];
                        if(t_cult.id != '0000000000000000'){
                            var defaultCheck = isChecked(t_cult.id) ? 'checked' : '';
                            html += '<li><label><input type="checkbox" name="cult" value="'+t_cult.id+'" defaultCheck="'+defaultCheck+'" '+defaultCheck+' />'+t_cult.name+'</label></li>';
                        }
                    }
                    $('#my-cults').html(html);

                    //注册事件
                    $('#my-cults input[name="cult"]').change(function(){
                        setRed($(this));
                    });
                }
            }
        });
    }

    function setRed(input){
        var curtChecked = input.is(':checked') ? 'checked' : '';
        if(curtChecked == input.attr('defaultCheck')){
            input.parent('label').removeClass("red");
        }else{
            input.parent('label').addClass("red");
        }
    }

    //全选
    function chioceAll() {
        $('#my-cults input[name="cult"]').each(function(){
            $(this).prop("checked", true);
            setRed($(this));
        });
    }

    //取消全选
    function unChioceAll(){
        $('#my-cults input[name="cult"]').each(function(){
            $(this).prop("checked", false);
            setRed($(this));
        });
    }
</script>
<!-- script END -->
</body>
</html>
