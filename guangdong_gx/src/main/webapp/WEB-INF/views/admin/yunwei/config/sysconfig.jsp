<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>一体机配置</title>
    <!-- 编辑表单 -->
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap-theme.min.css"/>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <link rel="stylesheet" href="https://g.alicdn.com/de/prismplayer/2.1.0/skins/default/aliplayer-min.css" />
    <script type="text/javascript" src="https://g.alicdn.com/de/prismplayer/2.1.0/aliplayer-min.js"></script>
    <script type="text/javascript" src="${basePath}/static/admin/js/mass-resource.js"></script>
</head>
<body style="overflow-x: hidden">
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand">一体机配置</a>
        </div>
    </div>
</nav>


<div class="container-fluid">
    <form class="form-horizontal" role="form" id="whgff" method="post">
        <fieldset>
            <legend>一体机首页视频配置</legend>

            <div class="form-group">
                <label class="col-sm-2 control-label">视频标题：</label>
                <div class="col-sm-10">
                    <input class="easyui-textbox" style="width: 300px; height: 34px;" id="resname" name="resname" value="${resname}" data-options="prompt:'请配置视频标题，长度不能超过32个字符',required:true,validType:'length[1,32]'" />
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">配置视频：</label>
                <div class="col-sm-10">
                    <input type="hidden" id="resurl" name="resurl" value="${resurl}"/>
                    <input type="hidden" id="respicture" name="respicture" value="${respicture}"/>
                    <div style="width:862px; height:392px; margin-bottom: 10px; border:1px #777 solid; border-radius: 5px;">
                        <div id="J_prismPlayer" class="prism-player"></div>
                        <h1 id="noConfig">未配置</h1>
                    </div>
                </div>
            </div>


            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <shiro:hasPermission name="${resourceid}:edit">
                    <button type="button" class="btn btn-default" onclick="chioceVod()">&nbsp;选择视频</button>
                    <button type="button" class="btn btn-primary" id="doSubmit"><span class="glyphicon glyphicon-ok"></span>&nbsp;确认保存</button>
                    </shiro:hasPermission>
                </div>
            </div>

        </fieldset>
    </form>
</div>


<script type="text/javascript">
    /** 选择视频 */
    function chioceVod(){
        WhgMassResource.openResource({
            restype:'video',
            submitFn:function (row) {
                //{libid:'资源库标识', resid:'资源标识', restype:'资源类型', resname:'资源名称', respicture:'资源封面图片地址', resurl:'resurl'}
                row = row||{};
                if(row && typeof(row.resid) != 'undefined'){
                    $('#resurl').val(row.resurl);
                    $('#respicture').val(row.respicture);
                    initVod();
                }
            }
        });
    }

    /** 初始插放器 */
    function initVod() {
        var sourceUrl = $('#resurl').val();
        if(sourceUrl != ''){
            var pDiv = $('#J_prismPlayer').parent('div');
            if(typeof(window.player) != 'undefined'){
                window.player.destroy();
            }
            pDiv.append('<div id="J_prismPlayer" class="prism-player"></div>');
            window.player = new Aliplayer({
                id: "J_prismPlayer", // 容器id
                source: sourceUrl,// 视频地址
                autoplay: false,    //自动播放：否
                //width: "800px",       // 播放器宽度
                height: "390px",      // 播放器高度,
                cover: $('#respicture').val()
            });
            $('#noConfig').hide();
        }else{
            $('#noConfig').show();
        }
    }

    /** 保存视频配置 */
    function saveResUrl() {
        $.messager.progress();
        $.ajax({
            type: 'POST',
            url: '${basePath}/admin/yunwei/config/save',
            data: {url: $('#resurl').val()},
            cache: false,
            async: false,
            success: function (data) {
                $.messager.progress('close');
                if(data && data.success == '1'){
                    $.messager.alert('提示', '保存成功');
                } else {
                    $.messager.alert('提示', '操作失败: '+data.errormsg+'！', 'error');
                }
            }
        });
    }

    //初始表单提交
    function initForm(){
        //表单初始
        $('#whgff').form({
            novalidate: true,
            url: "${basePath}/admin/yunwei/config/save",
            onSubmit : function(param) {
                var _valid = $(this).form('enableValidation').form('validate');
                if(_valid){
                    if($('#resurl').val() == ''){
                        _valid = false;
                        $.messager.alert('提示', '请点击 “选择视频” 按钮配置好视频资源!', 'warning');
                    }
                }
                if (!_valid){
                    $.messager.progress('close');
                    $('#doSubmit').off('click').one('click', function (){$('#whgff').submit();});
                }
                return _valid;
            },
            success : function(data) {
                $.messager.progress('close');
                var Json = eval('('+data+')');
                if(Json && Json.success == '1'){
                    $.messager.alert('提示', '操作成功!');
                } else {
                    $.messager.alert('提示', '操作失败:'+Json.errormsg+'!', 'error');
                }
                $('#doSubmit').off('click').one('click', function (){$('#whgff').submit();});
            }
        });
        //注册提交事件
        $('#doSubmit').off('click').one('click', function (){$('#whgff').submit();});
    }//初始表单提交 END

    //<![CDATA[
    $(document).ready(function(){
        //视频配置
        initVod();

        //初始表单
        initForm();
    });
    //]]>
</script>
</body>
</html>