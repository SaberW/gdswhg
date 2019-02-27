<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<% String path = request.getContextPath();%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>配送审核</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
   <%-- <script src="${basePath}/static/easyui/jquery-ui.js"></script>--%>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>
    <%--<link rel="stylesheet" href="${basePath}/static/admin/css/style.css"/>--%>
    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-file.js"></script>
 <%--   <script src="${basePath}/static/admin/js/rong-datepicker-min.js"></script>--%>
  <%-- <script src="${basePath}/static/easyui/jquery-ui.js"></script>--%>
    <script src="${basePath}/static/common/js/area.js"></script>
    <script>
        var timesUIValues=[];
        /**
         * 时间项处理
         * */
        var timesUI = {
            target: "#timesUI",
            check: "true",
            beginTimeKey : "deliverytime",
            init: function(jq){
                this.target = jq || this.target;
                this.main = $(this.target);
               // this.validRules();
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
                this.main.find(".timesui-input").datebox('readonly');
                this.main.find(".timesui-row-del,.timesui-row-add").hide();
            },

            getValue: function(){
                var rest = [];
                var me = this;
                if(this.main) {
                    this.main.find(".timesui-row").each(function () {
                        var timestart = $(this).find(".timesui-input:eq(0)").datebox("getValue");
                        var timestr=timestart.split("-");
                        var times=timestr[0];
                        if(timestr[1]&&parseInt(timestr[1])<10){
                            times=times+'-0'+timestr[1];
                        }else{
                            times=times+'-'+timestr[1];
                        }
                        if(timestr[2]&&parseInt(timestr[2])<10){
                            times=times+'-0'+timestr[2];
                        }else{
                            times=times+'-'+timestr[2];
                        }
                        rest.push(times);
                    });
                }
                return rest.join(",");
            },

            setValue: function(times){
                this.clearRows();
                if (!times || !$.isArray(times) || times.length==0){
                    this.appendRow();
                }
                for(var key in times){
                    var item = times[key];
                    var timestart = item[this.beginTimeKey];
                    var ts = new Date(timestart).Format("yyyy-MM-dd hh:mm:ss");
                    this.appendRow(ts);
                }
            },
            appendRow: function(timestart){
                var row = '<div class="timesui-row" style="margin-bottom: 10px">' +
                    '<input class="timesui-input"/> ' +
                    '</div>';
                this.main.append(row);
                var jrow = this.main.find(".timesui-row:last");
                var timestr="";
                if(timesUIValues.length>0){
                    timestr=timesUIValues.toString()+",";
                }
                jrow.find(".timesui-input").datebox({
                    required:true,
                    formatter: function (date) { return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate(); },
                    onSelect: function(date){
                       // alert(date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate());
                        var bool=true;
                        var year=date.getFullYear();
                        var month=date.getMonth() + 1;
                        var day=date.getDate();
                        var datestr=date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
                        if(timestr!=""&&timestr.indexOf(datestr+",")!=-1){
                            $.messager.alert('提示', '操作失败:不能选择重复的日期!', 'error');
                            //jrow.find(".timesui-input").datebox("setValue","");
                            bool=false;
                        }
                        if(bool==true){
                        $.ajax({
                            url:  '${basePath}/admin/delivery/openDays?id=${cult.fkid}&year='+year+'&month='+month+'&day='+day,
                            type: "POST",
                            success : function(data){
                                if (!data) return;
                                if(data.success!="true"){
                                    $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                                    // jrow.find(".timesui-input").datebox("setValue","");
                                }
                            }});
                        }
                    }
                });

                var rowidx = this.main.find(".timesui-row").index(jrow);
                if (rowidx>0){
                    jrow.append('<a class="timesui-row-del">删除</a>');
                }
                jrow.append('<a class="timesui-row-add" style="margin-left: 5px;">添加</a>');
                if(timestart){

                    jrow.find(".timesui-input:eq(0)").datebox("setValue", timestart);
                }
            },

            enableValidation: function(){
                this.main.find(".timesui-input").datebox('enableValidation');
            },

            disableValidation: function () {
                this.main.find(".timesui-input").datebox('disableValidation');
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
              /* $.extend($.fn.validatebox.defaults.rules, {
                    TimesUI_gtpretime: {
                        validator: function(value, param){
                    /!*        var optInput = $(this).parents("span").prev(".timesui-input");
                            var optIdx = $('.timesui-input', me.main).index(optInput);
                            if (optIdx == 0){
                                return true;
                            }
                            var prevIdx = optIdx-1;
                            var prevInput = $('.timesui-input:eq('+prevIdx+')', me.main);
                            var prevValue = prevInput.datebox("getValue");*!/
                          /!*  var prevDate = date2number(prevValue);
                            var optDate = date2number(value);
                            if (!prevDate || !optDate){
                                return true;
                            }
                            return prevDate < optDate;*!/
                        },
                        message: '必须大需前一个时间'
                    }
                });*/
            }
        };
    </script>

</head>
<body>

<form id="whgff" class="whgff" method="post" action="${basePath}/admin/delivery/edit">
    <c:choose>
        <c:when test="${not empty targetShow}">
            <h2>查看配送信息</h2>
        </c:when>
        <c:otherwise>
            <h2>配送订单信息</h2>
        </c:otherwise>
    </c:choose>
    <input type="hidden" name="id" id="id" value="${id}"/>

   <div class="whgff-row">
        <div class="whgff-row-label">配送单号：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" disabled value="${cult.id}" style="width:500px; height:32px" ></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">名称：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" disabled value="${cult.name}" style="width:500px; height:32px" ></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">
            <c:choose>
                <c:when test="${clazz == 1}">
                    提供单位：
                </c:when>
                <c:otherwise>
                    申请配送单位：
                </c:otherwise>
            </c:choose>
        </div>
        <div class="whgff-row-input"><input class="easyui-textbox" disabled value="${cult.crtuser}" style="width:500px; height:32px" ></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">配送区域：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" disabled value="${cult.region}" style="width:500px; height:32px"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">配送日期：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" disabled value="${deliveryTime}" style="width:500px; height:32px" ></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">
            <c:choose>
                <c:when test="${clazz == 1}">
                    需求单位：
                </c:when>
                <c:otherwise>
                    提供配送单位：
                </c:otherwise>
            </c:choose>
        </div>
        <div class="whgff-row-input"><input class="easyui-textbox" disabled value="${cult.touser}" style="width:500px; height:32px" ></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">提交日期：</div>
        <div class="whgff-row-input">
            <input type="text" class="easyui-datetimebox"style="width: 240px; height: 32px;"  disabled value="<fmt:formatDate value='${cult.crtdate}' pattern="yyyy-MM-dd HH:mm"></fmt:formatDate>">
        </div>
    </div>
   <%-- <div class="whgff-row">
        <div class="whgff-row-label">附件：</div>
        <div class="whgff-row-input">
            <input  id="act_filepath1" name="filepath" value="${accultt.attachment }" data-options="required:true" style="width:600px;height:32px;" readonly="readonly">
        </div>
    </div>--%>

    <div class="whgff-row toedit">
        <div class="whgff-row-label">状态：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" style="width: 200px; height:32px" disabled  value="${deliveryState}"/>
        </div>
    </div>
    <div class="whgff-row toedit">
        <div class="whgff-row-label">附件：</div>
        <div class="whgff-row-input">
            <a href="${cult.attachment}" class="easyui-linkbutton whgff-but-clear">${cult.name}下载</a>
        </div>
    </div>
    <h2 class="toedit">审核信息</h2>
        <div class="whgff-row toedit">
            <div class="whgff-row-label"><i>*</i>结论：</div>
            <div class="whgff-row-input">
                <div class="radio radio-primary whg-js-data" name="result" id="result"
                     js-data='[{"id":"2","text":"同意"},{"id":"0","text":"驳回"}]'>
            </div>
            </div>
        </div>

    <div class="whgff-row toedit" id="pscity">
        <div class="whgff-row-label"><i>*</i>配送范围：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:30%" name="psprovince" id="__PROVINCE_ELE"
                   data-options="required:true, prompt:'请选择省', editable:false, limitToList:true, valueField:'name', textField:'name', data:__PROVINCE, onChange:changeProvince"/>
            <input class="easyui-combobox" style="width: 60%;" name="pscity" id="__CITY_ELE"
                   data-options="required:true, prompt:'请选择市(多选)', editable:false, multiple:true, valueField:'name', textField:'name'"/>
        </div>
    </div>

        <div class="whgff-row toedit">
            <div class="whgff-row-label"><i>*</i>配送日期：</div>
            <div class="whgff-row-input">
                    <div class="whgff-row-input"  id="timesUI">
                    </div>
            </div>
            </div>
        </div>

</form>

<div id="whgwin-add-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <div style="display: inline-block; margin: 0 auto">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="whgwin-add-btn-save">保 存</a>
<a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
</div>
</div>

<!-- script -->
<script type="text/javascript">

    $(function () {
        $("#__PROVINCE_ELE").combobox("setValue","广东省");
        $("#__CITY_ELE").combobox("setValues","${cult.region}");
       // timesUI.init();
        $.ajax({
            url:  '${basePath}/admin/delivery/getPstime?id=${cult.id}',
            type: "POST",
            success : function(data){
                $("#timesUI").html("");
                timesUI.init();
                if (!data) return;
                    if(data.data.times&&data.data.times!=""){
                        timesUI.setValue( JSON.parse(data.data.times) );
                        timesUIValues=timesUI.getValue();
                    }
                }});


      /* rongDatepicker.init({
           defaultDate: [[]],
            openDaysApi: [[]],
            id:
        });*/

       /* rongDatepicker.init({
            fstMonthApi: [["{basePath}/admin/delivery/firstOpenYMD"]],
            openDaysApi: [["{basePath}/admin/delivery/openDays"]],
            id: {cult.fkid}
        });*/
    });

    $(function () {

        $('#whgff').form({
            novalidate: true,
            url: "${basePath}/admin/delivery/edit",
            onSubmit : function(param) {
                var _valid = $(this).form('enableValidation').form('validate');
                var bool=false;
                if(_valid) {
                    var result=$('input:radio[name="result"]:checked').val();
                    var times = timesUI.getValue();
                    param.pstime = times;
                    if(result!=null&&result=="2"){//同意才验证时间是否冲突
                        $.ajax({
                            url:  '${basePath}/admin/delivery/checkDays?id=${cult.id}&fkid=${cult.fkid}&datestr='+times,
                            type: "POST",
                            async: false,
                            success : function(data){
                                if (!data) return;
                                if(data.success!="true"){
                                    $.messager.alert('提示', '操作失败:'+data.errormsg+'!', 'error');
                                    bool=true;
                                }
                            }});
                    }
                }
                if(bool){
                    _valid=false;
                }
                if (!_valid){
                    $.messager.progress('close');
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
                    $.messager.alert('提示', '操作失败:'+Json.errormsg+'!', 'error');
                    $('#whgwin-add-btn-save').off('click').one('click', submitFun);
                }
            }
        });
        //注册提交事件
        $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
    });



 //查看时的处理
    $(function () {
        var targetShow = '${targetShow}';
        if (targetShow){
            //取消表单验证
            $("#whgff").form("disableValidation");

            $('.easyui-textbox').textbox('readonly');
            $('.easyui-combobox').combobox('readonly');
            $('.toedit').hide();
            //不显示提交 button
            $('#whgwin-add-btn-save').hide();
            return;
        }

    });
    </script>
<!-- script END -->
</body>
</html>
