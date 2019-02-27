<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>自定义属性-添加编辑属性</title>
    <!-- 编辑表单 -->
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/bootstrap/css/bootstrap-theme.min.css"/>
    <%--<script type="text/javascript" src="${basePath}/static/bootstrap/js/bootstrap.min.js"></script>--%>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <script type="text/javascript" src="${basePath}/static/admin/js/mass-library-custom-field-save.js"></script>
    <script type="text/javascript">
        /**
         * 当前支持组件类型
         * @returns {[*,*,*,*,*,*,*,*,*,*,*]}
         */
        function componentType(){
            return [
                {"id":"easyui-textbox", "text":"普通文本"},
                {"id":"easyui-combobox", "text":"下拉选择框"},
                {"id":"easyui-datebox", "text":"日期选择框"},
                {"id":"easyui-datetimebox", "text":"日期时间选择框"},
                {"id":"easyui-numberspinner", "text":"数字"},
                {"id":"radio", "text":"单选"},
                {"id":"checkbox", "text":"多选"},
                {"id":"textarea", "text":"多行文本"},
                //{"id":"imginput", "text":"图片"},
                //{"id":"fileinput", "text":"附件"},
                {"id":"richtext", "text":"富文本"}
            ];
        }

        //支持的验证类型
        function textboxValidType(){
            return [{"id":"length", "text":"限制长度"},{"id":"url", "text":"网址"},{"id":"email", "text":"邮箱地址"},{"id":"isPhone", "text":"手机号码"}];
        }

        /**
         * 宽度单位
         * @returns {[*,*]}
         */
        function widthUnit() {
            return [
                {"id":"%", "text":"％"},
                {"id":"px", "text":"PX"}
            ];
        }

        /**
         * 默认宽度单位
         */
        function getFirstWidthUnit() {
            return widthUnit()[0].id;
        }

        $.extend($.fn.validatebox.defaults.rules, {
            isTableName: {
                validator: function(value, param){
                    //$.fn.validatebox.defaults.rules.isTableName.message = '只能是6-16个长度的英文小写字母.';
                    return /^[a-z][a-z0-9]{3,15}$/.test(value);
                },
                message: '只能是4-16个长度的英文小写字母.'
            }
            ,GT: {
                validator: function(value, param){
                    var min = $('#'+param[0]).numberspinner('getValue');
                    return value > min;
                },
                message: '必须大于最小值.'
            }
        });
    </script>
</head>
<body>

<div class="container-fluid" style="padding-top: 20px;">
    <form class="form-horizontal" role="form" id="whgff" method="post">
        <input type="hidden" id="fid1" name="fid1" value="${fid1}"  />
        <input type="hidden" id="id" name="id" value="${fid2}"  />
        <input type="hidden" id="libid" name="libid" value="${fieldObj.libid}"  />
        <input type="hidden" id="formid" name="formid" value="${param.columnid}"  />
        <input type="hidden" id="fieldidx" name="fieldidx" value="${param.idx}"  />

        <div class="form-group">
            <label class="col-sm-2 control-label">表单Label：</label>
            <div class="col-sm-4">
                <input class="easyui-textbox" style="width: 100%; height: 34px;" id="labelname" name="labelname" data-options="prompt:'请输入表单说明文字',required:true,validType:['length[2,10]']" />
            </div>
            <label class="col-sm-2 control-label">字段编码：</label>
            <div class="col-sm-4">
                <input class="easyui-textbox" style="width: 100%; height: 34px;" id="fieldcode" name="fieldcode" data-options="prompt:'请输入全英文小写的字段编码',required:true,validType:'isTableName'" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">字段名称：</label>
            <div class="col-sm-4">
                <input class="easyui-textbox" style="width: 100%; height: 34px;" id="fieldname" name="fieldname" data-options="prompt:'请输入字段名称',required:true,validType:['length[2,10]']" />
            </div>
            <label class="col-sm-2 control-label">字段类型：</label>
            <div class="col-sm-4">
                <input class="easyui-combobox" style="width: 100%; height: 34px;" id="fieldtype" name="fieldtype" data-options="prompt:'请选择字段类型',required:true,editable:false,limitToList:true,valueField:'id',textField:'text',data:componentType(), onChange:WhgCustomFieldSave.changeComponentType" />
            </div>
        </div>

        <div class="form-group" style="display: none;" id="setOptionDiv">
            <label class="col-sm-2 control-label">可选值：</label>
            <div class="col-sm-10">
                <table>
                    <tbody>
                    <tr>
                        <td>
                            <input class="easyui-textbox" style="width: 150px; height: 34px;" name="optionval"
                                   data-options="prompt:'可选值',required:true,validType:'length[1,60]',novalidate:true"/>
                        </td>
                        <td>
                            <span class="glyphicon glyphicon-plus" style="cursor: pointer;" onclick="WhgCustomFieldSave.addTR()"></span>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">是否必须：</label>
            <div class="col-sm-4">
                <label style="padding-top: 7px;"><input type="radio" name="fieldrequired" value="1" checked="checked"/>是</label>
                <label style="padding-top: 7px;"><input type="radio" name="fieldrequired" value="0"/>否</label>
            </div>
            <label class="col-sm-2 control-label">默认值：</label>
            <div class="col-sm-4" id="defaultValDiv">
                <input class="easyui-textbox" style="width: 100%; height: 34px;" id="fielddefaultval"
                       name="fielddefaultval"
                       data-options="prompt:'请输入字段默认值',required:true, validType:['length[1,60]']"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">显示宽：</label>
            <div class="col-sm-4">
                <input class="easyui-numberspinner" style="width: 60%; height: 34px; display: inline-block" id="cptwidth1" name="cptwidth1" value="100" data-options="prompt:'在表单中输入元素的宽度',required:true,min:1,max:1000" />
                <input class="easyui-combobox" style="width: 30%; height: 34px; display: inline-block" id="cptwidth2" name="cptwidth2" value="%" data-options="prompt:'宽度单位',editable:false,valueField:'id',textField:'text',data:widthUnit(),panelHeight:80,onChange:changeWidthUnit" />
            </div>
            <label class="col-sm-2 control-label">显示高：</label>
            <div class="col-sm-4">
                <input class="easyui-numberspinner" style="width: 80%; height: 34px;" id="cptheight" name="cptheight" value="34px" data-options="prompt:'在表单中输入元素的高度',required:true,min:10,max:1000" />PX
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">输入提示：</label>
            <div class="col-sm-4">
                <input class="easyui-textbox" style="width: 100%; height: 34px;" id="fieldprompt" name="fieldprompt"
                       data-options="prompt:'请输入字段输入提示',required:false,validType:['length[1,60]']"/>
            </div>
            <label class="col-sm-2 control-label">限制类型：</label>
            <div class="col-sm-4">
                <input class="easyui-combobox" style="width: 100%; height: 34px;" id="fieldvalidtype" name="fieldvalidtype" data-options="prompt:'请选择字段值限制类型',
                required:true,editable:false,limitToList:true,valueField:'id',textField:'text',data:textboxValidType(), onChange:changeValidType" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">允许最小值：</label>
            <div class="col-sm-4">
                <input class="easyui-numberspinner" style="width: 100%; height: 34px;" id="fieldminval" name="fieldminval" data-options="prompt:'在表单中输入元素的宽度',required:true,min:1,max:1000" />
            </div>
            <label class="col-sm-2 control-label">允许最大值：</label>
            <div class="col-sm-4">
                <input class="easyui-numberspinner" style="width: 100%; height: 34px;" id="fieldmaxval" name="fieldmaxval" data-options="prompt:'在表单中输入元素的宽度',required:true,min:1,max:1000,validType:'GT[\'fieldminval\']'" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">不能编辑：</label>
            <div class="col-sm-4">
                <label style="padding-top: 7px;"><input type="radio" name="fieldeditable" value="1" checked="checked"/>是</label>
                <label style="padding-top: 7px;"><input type="radio" name="fieldeditable" value="0"/>否</label>
            </div>
            <label class="col-sm-2 control-label">限制输入：</label>
            <div class="col-sm-4">
                <label style="padding-top: 7px;"><input type="radio" name="fieldlimittolist" value="1" checked="checked"/>是</label>
                <label style="padding-top: 7px;"><input type="radio" name="fieldlimittolist" value="0"/>否</label>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">输入框前缀：</label>
            <div class="col-sm-4">
                <input class="easyui-textbox" style="width: 100%; height: 34px;" id="fieldprefix" name="fieldprefix" data-options="prompt:'表单输入框的前缀字符',required:false,validType:['length[1,20]']" />
            </div>
            <label class="col-sm-2 control-label">输入框后缀：</label>
            <div class="col-sm-4">
                <input class="easyui-textbox" style="width: 100%; height: 34px;" id="fieldsuffix" name="fieldsuffix" data-options="prompt:'表单输入框的后缀字符',required:false,validType:['length[1,20]']" />

            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">前端展示：</label>
            <div class="col-sm-4">
                <label style="padding-top: 7px;"><input type="radio" name="isshowfront" value="1" checked="checked"/>是</label>
                <label style="padding-top: 7px;"><input type="radio" name="isshowfront" value="0"/>否</label>
            </div>
            <%--<label class="col-sm-2 control-label">列表展示：</label>
            <div class="col-sm-4">
                <label style="padding-top: 7px;"><input type="radio" name="isshowlist" value="1" checked="checked"/>是</label>
                <label style="padding-top: 7px;"><input type="radio" name="isshowlist" value="0"/>否</label>
            </div>--%>
        </div>
    </form>
</div>


<!-- script -->
<script type="text/javascript">

    //切换宽度单位
    function changeWidthUnit(newVal, oldVal){
        if(newVal == '%'){
            $('#cptwidth1').numberspinner({max:100});
        }else{
            $('#cptwidth1').numberspinner({max:1000});
        }
    }

    //切换限制类型
    function changeValidType(newVal, oldVal){
        if(newVal){
            if(newVal == 'length'){
                WhgCustomFieldSave.showFieldMinVal(true);
                WhgCustomFieldSave.showFieldMaxVal(true);
            }else{
                WhgCustomFieldSave.showFieldMinVal(false);
                WhgCustomFieldSave.showFieldMaxVal(false);
            }
        }
    }

    //window.onload
    $(function () {
        //初始值
        window.init_fieldlistdata = '${fieldObj.fieldlistdata}';
        window.init_fielddefaultval = '${fieldObj.fielddefaultval}';
        window.init_fieldwidth = '${fieldObj.fieldwidth}';
        window.init_fieldheight = '${fieldObj.fieldheight}';
        window.init_fieldprompt = '${fieldObj.fieldprompt}';
        window.init_fieldvalidtype = '${fieldObj.fieldvalidtype}';
        window.init_fieldminval = '${fieldObj.fieldminval}';
        window.init_fieldmaxval = '${fieldObj.fieldmaxval}';
        window.init_fieldeditable = '${fieldObj.fieldeditable}';
        window.init_fieldlimittolist = '${fieldObj.fieldlimittolist}';
        window.init_fieldprefix = '${fieldObj.fieldprefix}';
        window.init_fieldsuffix = '${fieldObj.fieldsuffix}';
        WhgCustomFieldSave.showLabelName(true,'${formObj.labelname}');
        WhgCustomFieldSave.showFieldCode(true,'${fieldObj.fieldcode}', ('${param.fid1}' != ''));//如果已经保存过为资源库的一个字段，不能修改字段类型
        WhgCustomFieldSave.showFieldName(true,'${fieldObj.fieldname}' || '${formObj.labelname}', true);
        WhgCustomFieldSave.showFieldType(true,'${fieldObj.fieldtype}', ('${param.fid1}' != ''));//如果已经保存过为资源库的一个字段，不能修改字段类型
        WhgCustomFieldSave.showFieldRequired(true,'${fieldObj.fieldrequired}', true);
        WhgCustomFieldSave.showIsShowFront(true,'${fieldObj.isshowfront}', true);
        //WhgCustomFieldSave.showIsShowList(false,'$ {fieldObj.isshowlist}', true);

        //表单提交
        $('#whgff').form({
            novalidate: true,
            url: "${basePath}/admin/mass/library/savefield",
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
                    Json.data.labelname = $('#labelname').textbox('getValue');
                    window.parent.WhgCustomForm.afterEditField(Json.data);
                    window.parent.WhgCustomForm.closeDialog();
                } else {
                    $.messager.alert('提示', '操作失败:'+Json.errormsg+'!', 'error');
                    window.parent.WhgCustomForm.getDialogSubmitBtn().off('click').one('click', function (){$('#whgff').submit();});
                }
            }
        });
        //注册提交事件
        window.parent.WhgCustomForm.getDialogSubmitBtn().off('click').one('click', function (){$('#whgff').submit();});
    });//window.onload END
</script>
<!-- script END -->
</body>
</html>