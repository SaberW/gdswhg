<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>群文资源库-添加</title>
    <!-- 编辑表单 -->
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap-theme.min.css"/>
    <%--<script type="text/javascript" src="${basePath}/static/bootstrap/js/bootstrap.min.js"></script>--%>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-file.js"></script>
    <!-- 图片上传相关-END -->
    <script src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
    <script type="text/javascript" src="${basePath}/static/admin/js/mass-library-custom-form.js"></script>
    <style>
        .opreation{ margin: 5px; display: block; }
        div.form-row{ border: 1px dashed lightgrey; padding: 10px 0; position: relative; }
        div.row-opreation{ position: absolute; float: left; top:0; left: 0; }
        i{ color: red; font-weight: bold; }
    </style>
    <script type="text/javascript">
        $.extend($.fn.validatebox.defaults.rules, {
            isTableName: {
                validator: function(value, param){
                    //$.fn.validatebox.defaults.rules.isTableName.message = '只能是6-16个长度的英文小写字母.';
                    return /[a-z]{6,16}/.test(value);
                },
                message: '只能是6-16个长度的英文小写字母.'
            }
        });
    </script>
</head>
<body style="overflow-x: hidden">
<div class="container-fluid" style="padding-top: 50px;">
    <form class="form-horizontal" role="form" id="whgff" method="post">

        <input type="hidden" id="libid" name="libid" value="${param.libid}" />
        <input type="hidden" id="formid" name="formid" value="${param.formid}" />
        <input type="hidden" id="totalcolumns" name="totalcolumns" value="${param.column}" />

        <div class="form-group">
            <label class="col-sm-3 control-label"><i>*</i><c:if test="${param.column == 2}">第<span class="one">1</span>列</c:if>说明文字：</label>
            <div class="col-sm-9">
                <input class="easyui-textbox" style="width: 80%; height: 34px;" id="labelname1" name="labelname1" data-options="prompt:'请输入列的输入说明,长度4-6个字符最佳',required:true,validType:'length[2,10]'" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label"><i>*</i><c:if test="${param.column == 2}">第<span class="one">1</span>列</c:if>字段形式：</label>
            <div class="col-sm-9">
                <input class="easyui-combobox" style="width: 80%; height:34px" id="columntype1" name="columntype1" data-options="prompt:'请选择列中存放的字段形式',required:true,editable:false,valueField:'id',textField:'text',data:getColumnType(),panelHeight:110,onChange:changeType" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label"><i>*</i><c:if test="${param.column == 2}">第<span class="one">1</span>列</c:if>字段数量：</label>
            <div class="col-sm-9">
                <input class="easyui-numberspinner" style="width: 80%; height:34px" id="columns1" name="columns1" data-options="prompt:'请选择列中字段的数量',required:true,value:2,min:2,max:8" />
            </div>
        </div>

        <c:if test="${param.column == 2}">
        <div class="form-group">
            <label class="col-sm-3 control-label"><i>*</i>第<span class="one">2</span>列说明文字：</label>
            <div class="col-sm-9">
                <input class="easyui-textbox" style="width: 80%; height: 34px;" id="labelname2" name="labelname2" data-options="prompt:'请输入列的输入说明,长度4-6个字符最佳',required:true,validType:'length[2,10]'" />
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label"><i>*</i>第<span>2</span>列字段形式：</label>
            <div class="col-sm-9">
                <input class="easyui-combobox" style="width: 80%; height:34px" id="columntype2" name="columntype2" data-options="prompt:'请选择列中存放的字段形式',required:true,editable:false,valueField:'id',textField:'text',data:getColumnType(),panelHeight:110,onChange:changeType" />
            </div>
        </div>


        <div class="form-group">
            <label class="col-sm-3 control-label"><i>*</i>第<span>2</span>列字段数量：</label>
            <div class="col-sm-9">
                <input class="easyui-numberspinner" style="width: 80%; height:34px" id="columns2" name="columns2" data-options="prompt:'请选择列中字段的数量',required:true,value:2,min:2,max:8" />
            </div>
        </div>
        </c:if>
    </form>
</div>


<!-- script -->
<script type="text/javascript">
    /**
     * 列字段类型
     * @returns {[*,*,*]}
     */
    function getColumnType(){
        //return [{"id":"0", "text":"一个字段"},{"id":"1", "text":"组合多字段"},{"id":"2", "text":"组合列字段"}];
        return [{"id":"0", "text":"一个字段"},{"id":"1", "text":"组合多字段"}];//暂时只支持前两种
    }

    function changeType(newVal, oldVal){
        if(newVal == '0'){
            $(this).parents('div.form-group').next('div.form-group').find('.easyui-numberspinner').numberspinner('disable');
        }else{
            $(this).parents('div.form-group').next('div.form-group').find('.easyui-numberspinner').numberspinner('enable');
        }
    }

    //初始表单提交
    function initForm(){
        //表单初始
        $('#whgff').form({
            novalidate: true,
            url: "${basePath}/admin/mass/library/saverow",
            onSubmit : function(param) {
                var _valid = $(this).form('enableValidation').form('validate');
                if (!_valid){
                    $.messager.progress('close');
                    window.parent.WhgCustomForm.getDialogSubmitBtn().off('click').one('click', function (){$('#whgff').submit();});
                }
                return _valid;
            },
            success : function(data) {
                $.messager.progress('close');
                var Json = eval('('+data+')');
                if(Json && Json.success == '1'){
                    var ids = Json.data;
                    if('${param.column}' == '2'){
                        var idArr = ids.split(',');
                        var column1 = {id:idArr[0], totalcolumns:2, labelname:$('#labelname1').textbox('getValue'), columntype:$('#columntype1').combobox('getValue')};
                        if(column1.type != '0'){
                            column1.size = $('#columns1').numberspinner('getValue');
                        }
                        var column2 = {id:idArr[1], totalcolumns:2, labelname:$('#labelname2').textbox('getValue'), columntype:$('#columntype2').combobox('getValue')};
                        if(column2.type != '0'){
                            column2.size = $('#columns2').numberspinner('getValue');
                        }
                        var columnInfo = [column1,column2];
                        window.parent.WhgCustomForm.createRow('${param.formid}', columnInfo);
                    }else if('${param.column}' == '1'){
                        var column1 = {id:ids, totalcolumns:1, labelname:$('#labelname1').textbox('getValue'), columntype:$('#columntype1').combobox('getValue')};
                        if(column1.type != '0'){
                            column1.size = $('#columns1').numberspinner('getValue');
                        }
                        var columnInfo = [column1];
                        window.parent.WhgCustomForm.createRow('${param.formid}', columnInfo);
                    }
                    window.parent.WhgCustomForm.closeDialog();
                } else {
                    $.messager.alert('提示', '操作失败:'+Json.errormsg+'!', 'error');
                    window.parent.WhgCustomForm.getDialogSubmitBtn().off('click').one('click', function (){$('#whgff').submit();});
                }
            }
        });
        //注册提交事件
        window.parent.WhgCustomForm.getDialogSubmitBtn().off('click').one('click', function (){$('#whgff').submit();});
    }//初始表单提交 END


    //window.onload
    $(function () {
        //初始文化馆和权限
        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:false,
            deptEid:'deptid', deptValue:false
        });

        //初始表单
        initForm();

        //默认单字段
        $('#columntype1').combobox('setValue', "0");
        $('#columntype2').combobox('setValue', "0");
    });//window.onload END
</script>
<!-- script END -->
</body>
</html>