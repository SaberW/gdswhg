<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
<script src="${basePath}/static/common/js/area.js"></script>
    <script>
        /**
         * 时间项处理
         * */
        var timesUI = {
            target: "#timesUI",
            check: "true",
            beginTimeKey : "timestart",
            endTimeKey: "timeend",

            init: function(jq){
                this.target = jq || this.target;
                this.main = $(this.target);

                this.validRules();
                this.appendRow();
                var me = this;
                if(this.check=="true"){
                    this.main.on("click",".timesui-row-del", function(e){me.delRow(e)});
                    this.main.on("click",".timesui-row-add", function(){me.addRow()});
                }
                this.check="false";
            },


            delRow: function (e) {
                var evtObj = e.target;
                $(evtObj).parents(".timesui-row").remove();
            },

            addRow: function(){
                this.appendRow();
                this.disableValidation();
            },

            clearRows: function () {
                this.main.find(".timesui-row").remove();
            },

            setIsShow: function () {
                this.disableValidation();
                this.main.find(".timesui-input").datetimebox('readonly');
                this.main.find(".timesui-row-del,.timesui-row-add").hide();
            },

            getValue: function(){
                var rest = [];
                var me = this;
                if(this.main) {
                    this.main.find(".timesui-row").each(function () {
                        var timestart = $(this).find(".timesui-input:eq(0)").datetimebox("getValue");
                        var timeend = $(this).find(".timesui-input:eq(1)").datetimebox("getValue");
                        var item = {};
                        item[me.beginTimeKey] = timestart;
                        item[me.endTimeKey] = timeend;

                        rest.push(item);
                    });
                }
                return rest;
            },

            setValue: function(times){
                this.clearRows();
                if (!times || !$.isArray(times) || times.length==0){
                    this.appendRow();
                }
                for(var key in times){
                    var item = times[key];
                    var timestart = item[this.beginTimeKey];
                    var timeend = item[this.endTimeKey];
                    var ts = new Date(timestart).Format("yyyy-MM-dd hh:mm:ss");
                    var te = new Date(timeend).Format("yyyy-MM-dd hh:mm:ss");
                    this.appendRow(ts, te);
                }
            },

            appendRow: function(timestart, timeend){
                var row = '<div class="timesui-row" style="margin-bottom: 10px">' +
                    '<input class="timesui-input"/> 至 <input class="timesui-input"/>' +
                    '</div>';
                this.main.append(row);
                var jrow = this.main.find(".timesui-row:last");
                jrow.find(".timesui-input").datetimebox({
                    required:true,
                    validType: ['TimesUI_gtpretime']
                });

                var rowidx = this.main.find(".timesui-row").index(jrow);
                if (rowidx>0){
                    jrow.append('<a class="timesui-row-del">删除</a>');
                }
                jrow.append('<a class="timesui-row-add" style="margin-left: 5px;">添加</a>');

                if (timestart){
                    jrow.find(".timesui-input:eq(0)").datetimebox("setValue", timestart);
                }
                if (timeend){
                    jrow.find(".timesui-input:eq(1)").datetimebox("setValue", timeend);
                }
            },

            enableValidation: function(){
                this.main.find(".timesui-input").datetimebox('enableValidation');
            },

            disableValidation: function () {
                this.main.find(".timesui-input").datetimebox('disableValidation');
            },

            validRules: function(){
                var me = this;

                function date2number(date){
                    var times = date.split(/\D+/);
                    if (times.length<6){
                        return false;
                    }
                    times[1] = Number(times[1])-1;
                    return new Date(times[0], times[1], times[2], times[3], times[4], times[5])
                }

                $.extend($.fn.validatebox.defaults.rules, {
                    TimesUI_gtpretime: {
                        validator: function(value, param){
                            var optInput = $(this).parents("span").prev(".timesui-input");
                            var optIdx = $('.timesui-input', me.main).index(optInput);
                            if (optIdx == 0){
                                return true;
                            }
                            var prevIdx = optIdx-1;
                            var prevInput = $('.timesui-input:eq('+prevIdx+')', me.main);
                            var prevValue = prevInput.datetimebox("getValue");

                            var prevDate = date2number(prevValue);
                            var optDate = date2number(value);
                            if (!prevDate || !optDate){
                                return true;
                            }
                            return prevDate < optDate;
                        },
                        message: '必须大需前一个时间'
                    }
                });
            }
        };

        IDUtil = (function(){
            var __id = 100;

            function _getId(){
                return __id++;
            }

            return {
                getId: function(){
                    return _getId();
                }
            }
        })();
    </script>


<!--发布设置发布时间表单 -->
<div id="whgwin-pro-add" style="display: none">
    <form id="whgff_pro" class="whgff" method="post">
        <div class="whgff-row" >
            <div class="whgff-row-label _check" style="width: 15%"><i>*</i>发布至：</div>
            <div class="whgff-row-input" data-check="true" target="protype" err-msg="请至少选择一个系统" style="width: 85%">
                <div class="checkbox checkbox-primary whg-js-data" name="proType" id="proType"  value="WBGX"   js-data="WhgComm.getFromProject">
                </div>
            </div>
        </div>
        <div class="whgff-row" id="pscity">
            <div class="whgff-row-label" style="width: 15%"><i>*</i>配送范围：</div>
            <div class="whgff-row-input" style="width: 85%;">
                <input class="easyui-combobox" style="width:30%" name="psprovince" id="__PROVINCE_ELE"
                       data-options="required:true, prompt:'请选择省', editable:false, limitToList:true, valueField:'name', textField:'name', data:__PROVINCE, onChange:changeProvince"/>
                <input class="easyui-combobox" style="width: 60%;" name="pscity" id="__CITY_ELE"
                       data-options="required:true, prompt:'请选择市(多选)', editable:false, multiple:true, valueField:'name', textField:'name'"/>
            </div>
        </div>
        <div class="whgff-row" id="pstime" style="display: none">
            <div class="whgff-row-label" style="width: 15%"><i>*</i>配送时间：</div>
            <div class="whgff-row-input" style="width: 85%" id="timesUI">
            </div>
        </div>
    </form>

</div>
<div id="whgwin-pro-add-btn" style="text-align: center;display: none">
    <div style="display: inline-block; margin: 0 auto">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="pro-btn" >确 定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#whgwin-pro-add').dialog('close')">关 闭</a>
    </div>
</div>
<!-- 发布设置发布时间表单 END -->

<!-- script -->
<script type="text/javascript">

    /**
     * 发布 [2,4]->6
     * @param idx
     */
    function doSetProject(idx){
        var row = $("#whgdg").datagrid("getRows")[idx];
        $('#whgwin-pro-add').dialog({
            title: '设置发布信息',
            cache: false,
            modal: true,
            width: '560',
            height: '350',
            maximizable: true,
            resizable: true,
            buttons: '#whgwin-pro-add-btn',
            onOpen: function () {
                $("#whgff_pro")[0].reset();
                $("#pstime").hide();
                $("#pscity").hide();
                $.ajax({
                    url:  '${basePath}/admin/project/list?fkId='+row.id,
                    type: "POST",
                    success : function(data){
                        if (!data) return;
                        var el = $("#proType");
                        el.html("");//清空以前数据
                        var typesFn = el.attr('js-data');
                        var name = el.attr('name');
                        var value = el.attr('value');
                        if(data.data.proType&&data.data.proType!=null) {
                            value = data.data.proType;
                        }else{
                            value='WBGX';//默认外部供需系统
                        }
                        var cls = el.attr("class");
                        var inputType = cls.indexOf('radio') != -1 ? 'radio' : 'checkbox';
                        var textVal = el.attr("textVal") ? true : false;
                        showTypes(el, name, value, typesFn, inputType, textVal);//再次获取checkbox
                        doSetClick();

                        var protypes = data.data.proType || '';
                        if (protypes.indexOf('NBGX') != -1){
                            $("#pstime").show();
                            if(data.data.times&&data.data.times!=""){
                                $("#timesUI").html("");
                                timesUI.init();
                                timesUI.setValue( JSON.parse(data.data.times) );
                            }
                            $("#pscity").show();
                            $("#__PROVINCE_ELE").combobox({
                                onLoadSuccess : function () {
                                    var psprovince = (data.data.psprovince && data.data.psprovince!='')?
                                        data.data.psprovince : '广东省'
                                    $(this).combobox('setValue', psprovince);
                                    if (data.data.pscity){
                                        $("#__CITY_ELE").combobox("setValues", data.data.pscity);
                                    }
                                }
                            });

                        }
                    }
                });
                $('#whgff_pro').form({
                    url : '${basePath}/admin/project/add',
                    onSubmit : function(param) {
                        param.ids = row.id;
                        var times = timesUI.getValue();
                        param.times = JSON.stringify(times);
                        var isValid = $(this).form('enableValidation').form('validate');
                        var bool=false;
                        $('input[name="proType"]:checked').each(function(){
                           if($(this).val()=='NBGX'){
                               bool=true;
                           }
                        });
                        if(!bool||isValid){
                            $.messager.progress();
                        }else{
                            $("#pro-btn").off("click").one("click",function () { $('#whgff_pro').submit(); });
                        }
                        return !bool||isValid;
                    },
                    success : function(data) {
                        $.messager.progress('close');
                        var Json = $.parseJSON(data);
                        if(Json.success == "1"){
                            $('#whgdg').datagrid('reload');
                            $('#whgwin-pro-add').dialog('close');
                        }else{
                            $.messager.alert("提示", data.errorMsg);
                        }
                    }
                });
                $("#pro-btn").off("click").one("click",function () { $('#whgff_pro').submit(); });
            }
        })
    }



    $(function () {
        doSetClick();

        $("#__CITY_ELE").combobox({
            onChange: function (val, oldval) {
                if (val.length>1 && val[0]==''){
                    val.shift();
                    $(this).combobox("setValues", val);
                }
            }
        });
    });

    function doSetClick(){
        $('input:checkbox[name=proType]').change(function () {
            if(this.value=="NBGX"){
                if($(this).is(':checked')){
                    $("#pstime").show();
                    $("#pscity").show();
                    $("#timesUI").html("");
                    timesUI.init();
                    $("#__PROVINCE_ELE").combobox("setValue","广东省");
                }else{
                    $("#pstime").hide();
                    $("#pscity").hide();
                    $("#timesUI").html("");
                }
            }
        });
    }
</script>