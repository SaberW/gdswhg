<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <c:choose>
        <c:when test="${reftype eq 40}">
            <c:set var="pageTitle2" value="展品"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle2" value="资源"></c:set>
        </c:otherwise>
    </c:choose>

    <c:choose>
        <c:when test="${pageType eq 'show'}">
            <c:set var="pageTitle" value="${pageTitle2}管理-查看"></c:set>
        </c:when>
        <c:when test="${pageType eq 'edit'}">
            <c:set var="pageTitle" value="${pageTitle2}管理-编辑"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="${pageTitle2}管理-添加"></c:set>
        </c:otherwise>
    </c:choose>
    <title>${pageTitle}</title>

    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>

    <script src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>

    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>

    <!-- 图片上传相关 -->
    <%--<script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>--%>
    <!-- 图片上传相关-END -->
    <%--<script type="text/javascript" src="${basePath}/static/plupload/upload-file.js"></script>--%>

    <script type="text/javascript" src="${basePath}/static/admin/js/mass-resource.js"></script>
</head>
<body class="body_add">

<form id="whgff" class="whgff" method="post">
    <h2>${pageTitle}</h2>

    <c:if test="${reftype eq '25'}">
        <div class="whgff-row">
            <div class="whgff-row-label"><i>*</i>艺术分类：</div>
            <div class="whgff-row-input">
            <div class="checkbox checkbox-primary whg-js-data" name="arttype" value="${info.arttype}"
                 js-data='getArtTypes'>
            </div>
            </div>
        </div>
    </c:if>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>${pageTitle2}类型：</div>
        <div class="whgff-row-input">
        <select class="easyui-combobox" id="enttype" panelHeight="auto" style="width:300px; height:32px"
                data-options="editable:false, limitToList:true,required:true,prompt:'请选择${pageTitle2}类型',value:'${info.enttype}'">
            <option value="1">图片</option>
            <option value="2">视频</option>
            <option value="3">音频</option>
            <option value="4">文档</option>
        </select>

        <a class="easyui-linkbutton" id="selectlinkbutton" style="background-color: green"
           data-options="onClick:getResourceItem">选择${pageTitle2}</a>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>${pageTitle2}名称：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" id="name" name="name" value="${info.name}" style="width:600px; height:32px"
                    data-options="required:true, validType:'length[1,60]'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i></i>添加时间：</div>
        <div class="whgff-row-input">
            <input class="easyui-datetimebox" id="crtdate" name="_crtdate" value="<fmt:formatDate value='${info.crtdate}' pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>"
                    data-options="editable:false,required:false"/>
        </div>
    </div>

    <div class="whgff-row vm-img" style="display: none">
        <div class="whgff-row-label"></div>
        <div class="whgff-row-input">
            <img width="500" height="300">
        </div>
    </div>
    <div class="whgff-row vm-src" style="display: none">
        <div class="whgff-row-label"></div>
        <div class="whgff-row-input">
            <input class="easyui-textbox vm-text-ui" style="width:600px; height:32px"
                   data-options="readonly:true">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">${pageTitle2}简介：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="summary" value="${info.summary}" style="width:500px; height:100px" multiline="true"
                   data-options="required:false,validType:['length[1,400]']"></div>
    </div>

    <c:if test="${reftype eq '54' or reftype eq '56'}">
        <%--群文届次,创作讲坛:装入主讲老师--%>
        <div class="whgff-row">
            <div class="whgff-row-label"><i></i>主讲老师：</div>
            <div class="whgff-row-input">
                <input class="easyui-textbox" name="extemcee" value="${info.extemcee}" style="width:600px; height:32px"
                       data-options="required:false, validType:'length[1,60]'">
            </div>
        </div>
    </c:if>

    <c:if test="${reftype eq '54' or reftype eq '52'}">
        <%--群文届次,人才: 装入是否获奖 --%>
        <div class="whgff-row">
            <div class="whgff-row-label"><i></i>是否获奖：</div>
            <div class="whgff-row-input">
                <div class="radio radio-primary whg-js-data" name="extisaward" value="${info.extisaward}"
                     js-data='[{"id":"0","text":"非获奖"},{"id":"1","text":"获奖"}]'>
                </div>
            </div>
        </div>
    </c:if>

    <input type="hidden" name="reftype" value="${not empty info.reftype?info.reftype:reftype}">
    <input type="hidden" name="refid" value="${not empty info.refid?info.refid:refid}">

    <input type="hidden" name="enttype" value="${info.enttype}">

    <%--${pageTitle2}库标识--%>
    <input type="hidden"  class="vm-refvalue" vm="libid" name="libid" value="${info.libid}">
    <%--${pageTitle2}标识--%>
    <input type="hidden"  class="vm-refvalue" vm="resid" name="resid" value="${info.resid}">
    <%--${pageTitle2}的地址--%>
    <input type="hidden"  class="vm-refvalue" vm="resurl" name="enturl" value="${info.enturl}">
    <%--视频类型相关封面图--%>
    <input type="hidden"  class="vm-refvalue" vm="respicture" name="deourl" value="${info.deourl}">

</form>

<div id="whgwin-add-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
</div>

<!-- script -->
<script type="text/javascript">

    function getArtTypes(){
        var artArray = [];
        $.ajax({
            url: '${basePath}/admin/yunwei/type/srchList',
            data: {type:1, cultid:managerCults[0].id},
            dataType: 'json',
            async: false,
            success: function(data){
                if ($.isArray(data)){
                    for (var i in data){
                        var row = data[i];
                        if (row.id){
                            artArray.push({id:row.id, text: row.name})
                        }
                    }
                }
            }
        });
        return artArray;
    }

    var entResTypes = {
        "1":"img",
        "2":"video",
        "3":"audio",
        "4":"file"
    };

    function getResourceItem(){
        var enttype = $("#enttype").combobox("getValue");
        if (!enttype || enttype == '') {
            return;
        }
        var restype = entResTypes[enttype];
        WhgMassResource.openResource({
            restype:restype,
            submitFn:function (row) {
                //{libid:'${pageTitle2}库标识', resid:'${pageTitle2}标识', restype:'${pageTitle2}类型', resname:'${pageTitle2}名称', respicture:'${pageTitle2}封面图片地址', resurl:'resurl'}
                row = row||{};

                $("#whgff .vm-refvalue").each(function(){
                    var vmkey = $(this).attr("vm");
                    $(this).val(row[vmkey]||'');
                });
                $("#name").textbox("setValue", row.resname||'');
                $("#whgff [name='enttype']").val(enttype);

                showResourc();
            }
        });
    }

    function showResourc(){
        var enttype = $("#whgff [name='enttype']").val();
        var enturl = $("#whgff [name='enturl']").val();
        var deourl = $("#whgff [name='deourl']").val();

        var vmimg = $("#whgff .vm-img");
        var vmsrc = $("#whgff .vm-src");
        vmimg.hide();
        vmsrc.hide();

        switch (enttype){
            case "1":
                vmimg.find(".whgff-row-label").text("图片 ： ");
                var imgurl = enturl;
                if (!/^http/.test(imgurl)){
                    imgurl = WhgComm.getImgServerAddr()+imgurl;
                }
                vmimg.find("img").attr("src", imgurl);
                vmimg.show();
                break;
            case "2":
                vmimg.find(".whgff-row-label").text("封面图片 ： ");
                var imgurl = deourl;
                if (!/^http/.test(imgurl)){
                    imgurl = WhgComm.getImgServerAddr()+imgurl;
                }
                vmimg.find("img").attr("src", imgurl);
                vmimg.show();
                vmsrc.find(".whgff-row-label").text("${pageTitle2}地址 ： ");
                vmsrc.find(".vm-text-ui").textbox("setText", enturl||'');
                vmsrc.show();
                break;
            case "3":
            case "4":
                vmsrc.find(".whgff-row-label").text("${pageTitle2}地址 ： ");
                vmsrc.find(".vm-text-ui").textbox("setText", enturl||'');
                vmsrc.show();
        }

    }



    $(function(){
        formTool.init();
    });

    var formTool = {
        pageType: '${pageType}',
        basePath: '${basePath}',
        modelPath: '/admin/mass/resmanage/',

        /** 入口*/
        init: function(){
            var me = this;

            var enttypes = "${enttypes}";
            if (!enttypes || enttypes=='') {
                enttypes = "1,2,3,4";
            }
            me.enttypes = enttypes.split(/\D+/);

            me.frm = $("#whgff");
            me.buttonDiv = $("div.whgff-but");
            this.initVal();

            var pageType = this.pageType;
            if (me[pageType] && $.type(me[pageType])==='function'){
                me[pageType].call(me);
            }
        },

        initVal: function(){
            var me = this;
            var enttypes = [{value:"1",text:"图片"},{value:"2",text:"视频"},{value:"3",text:"音频"},{value:"4",text:"文档"}];
            var types = [];
            var _enttype = me.frm.find("[name='enttype']").val();
            for(var i in enttypes){
                var v = enttypes[i].value;
                if ($.inArray(v, me.enttypes)!=-1){
                    types.push(enttypes[i]);
                }
            }
            $("#enttype").combobox("loadData", types);
            if (!_enttype || _enttype==''){
                _enttype = types.length>0? types[0].value: '';
            }
            $("#enttype").combobox("setValue", _enttype);

            showResourc();
        },

        __validata: function (param) {
            var me = this;
            var enturl = me.frm.find("[name='enturl']").val();
            if (!enturl|| $.trim(enturl) == '') {
                $.messager.alert("错误", '请选择${pageTitle2}', 'error');
                return false;
            }

            if (me.frm.find("div[name='arttype']").length) {
                var arttype = $("[name='arttype']:checkbox:checked");
                if (!arttype || !arttype.length) {
                    $.messager.alert("错误", '艺术分类不能为空', 'error');
                    return false;
                }
            }

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
            me.frm.find("#selectlinkbutton").hide();

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

            var id = '${id}';
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

    };

</script>
<!-- script END -->
</body>
</html>