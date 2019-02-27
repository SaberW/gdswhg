<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <c:choose>
        <c:when test="${pageType eq 'show'}">
            <c:set var="pageTitle" value="短信模板管理-查看"></c:set>
        </c:when>
        <c:when test="${pageType eq 'edit'}">
            <c:set var="pageTitle" value="短信模板管理-编辑"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="短信模板管理-添加"></c:set>
        </c:otherwise>
    </c:choose>
    <title>${pageTitle}</title>

    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>

    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
</head>
<body class="body_add">
<form id="whgff" method="post" class="whgff">
    <h2>${pageTitle}</h2>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>模板code：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="tpcode" value="${info.tpcode}" style="width:600px;" prompt="请输入内容，字数限制在100个字符内"
                   data-options="multiline:false, required:true, validType:['length[1,100]'] "/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>模板名称：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="tpname" value="${info.tpname}" style="width:600px;" prompt="请输入内容，字数限制在100个字符内"
                   data-options="multiline:false, required:true, validType:['length[1,100]'] "/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>模板说明：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="tpdesc" value="${info.tpdesc}" style="width:600px;" prompt="请输入内容，字数限制在500个字符内"
                   data-options="multiline:false, required:true, validType:['length[1,500]'] "/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>模板内容：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="tpcontent" value="${info.tpcontent}" style="width:600px; height:180px" prompt="请输入内容，字数限制在1000个字符内"
                   data-options="multiline:true, required:true, validType:['length[1,1000]'] "/>
        </div>
    </div>
</form>

<div class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px"></div>

<script>

    $(function(){
        formTool.init();
    });


    var formTool = {
        pageType: '${pageType}',
        basePath: '${basePath}',
        modelPath: '/admin/yunwei/aliysmscode/',
        entid: '${info.id}',

        /** 入口*/
        init: function(){
            var pageType = this.pageType;

            this.initVal();

            var me = this;

            me.frm = $("#whgff");
            me.buttonDiv = $("div.whgff-but");

            if (me[pageType] && $.type(me[pageType])==='function'){
                me[pageType].call(me);
            }
        },

        initVal: function(){
            var me = this;
        },

        __validata: function (param) {
            var me = this;

            return true;
        },

        /** 查看*/
        show : function(){
            var me = this;

            //取消表单验证
            me.frm.form("disableValidation");
            var undoBut = me.__appendButton("返 回", 'icon-undo', function(){ me.__closeForm(); });

            //组件设为只读
            me.frm.find(".whgff-row-input>:input[class*='easyui-']").each(function(){
                var _class = $(this).attr("class");
                _class = String(_class).match(/easyui-\w+/);
                if (!_class || _class.length ==0){
                    return true;
                }
                _class = _class[0].toLowerCase();
                var method = _class.replace("easyui-", "");
                if (method!=''){
                    var obj = $(this);
                    obj[method].call(obj, 'readonly');
                }
            });

            me.frm.find(".whgff-row-input-imgfile a").hide();

            //处理选项点击不生效
            me.frm.find("input[type='radio']").attr('disabled', true);
            $("div.radio").on("DOMNodeInserted", function(e){
                $(e.target).attr('disabled', true);
            });
            me.frm.on('click', "input[type='checkbox']", function(){return false});
        },

        /** 编辑*/
        edit : function () {
            var me = this;

            var okBut = me.__appendButton("提 交", 'icon-ok');
            var undoBut = me.__appendButton("返 回", 'icon-undo', function(){ me.__closeForm(); });

            var id = me.entid;
            me.__submitForm(okBut, me.basePath+me.modelPath+me.pageType, id);
        },

        /** 添加*/
        add : function() {
            var me = this;

            var okBut = me.__appendButton("提 交", 'icon-ok');
            var undoBut = me.__appendButton("返 回", 'icon-undo', function(){ me.__closeForm(); });

            me.__submitForm(okBut, me.basePath+me.modelPath+me.pageType);
        },

        /** 表单提交*/
        __submitForm: function (okBut, url, id) {
            var me = this;

            function oneSubmit(){
                okBut.off("click").one("click", function(){
                    $.messager.progress();
                    me.frm.submit();
                })
            }
            oneSubmit();

            me.frm.form({
                url: url,
                novalidate: true,
                onSubmit: function (param) {
                    $(this).form("enableValidation");
                    var isValid = $(this).form('validate');

                    if (isValid){
                        isValid = me.__validata(param);
                    }

                    if (!isValid){
                        $.messager.progress('close');
                        oneSubmit();
                    }else{
                        if (id && id!=''){ param.id = id; }
                    }
                    return isValid;
                },
                success: function(data){
                    $.messager.progress('close');
                    oneSubmit();
                    data = $.parseJSON(data);
                    if (data.success && data.success=="1"){
                        $.messager.alert("提示", '信息提交成功', 'info',
                            function(){
                                me.__closeForm();
                            }
                        );
                    }else{
                        $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                    }
                }
            })
        },


        /** 插入按钮*/
        __appendButton: function (text, iconCls, onClick) {
            var button = $('<a style="margin-right: 5px"></a>');
            this.buttonDiv.append(button);

            var cfg = {};
            cfg.text = text;
            if (iconCls) cfg.iconCls = iconCls;
            if (onClick) cfg.onClick = onClick;

            button.linkbutton( cfg );
            return button;
        },

        /** 关闭返回*/
        __closeForm: function () {
            window.parent.whgListTool.reload();
            WhgComm.editDialogClose();
        }

    }
</script>
</body>
</html>
