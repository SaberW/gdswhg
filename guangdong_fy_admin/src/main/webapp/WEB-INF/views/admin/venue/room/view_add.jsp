<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">

    <c:choose>
        <c:when test="${pageType eq 'show'}">
            <c:set var="pageTitle" value="活动室管理-查看活动室"></c:set>
        </c:when>
        <c:when test="${pageType eq 'edit'}">
            <c:set var="pageTitle" value="活动室管理-编辑活动室"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="活动室管理-添加活动室"></c:set>
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
    <script src="${basePath}/static/admin/js/whgmodule-weektimes.js"></script>
    <script src="${basePath}/static/admin/js/whgmodule-venseatmaps.js"></script>

    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <!-- 图片上传相关-END -->

</head>
<body class="body_add">
<form id="whgff" method="post" class="whgff">
    <h2>${pageTitle}</h2>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>名称：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="title" value="${info.title}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,10]'], prompt:'请输入活动室名称'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>图片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture1" name="imgurl" value="${info.imgurl}">
            <div class="whgff-row-input-imgview" id="previewImg1"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>

    <%--<div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>所属场馆：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" name="venid" panelHeight="auto" limitToList="true" prompt="请选择所属场馆" style="width:600px; height:32px"
                   data-options="required:true, editable:false,multiple:false, mode:'remote', panelMaxHeight:300,
                   valueField:'id', textField:'title',
                   &lt;%&ndash;url:'${basePath}/admin/venue/srchList?state=6&delstate=0',&ndash;%&gt;
                   url:'${basePath}/admin/venue/ajaxVenList4session',
                   onLoadSuccess: function(){$(this).combobox('setValue','${info.venid}')},
                   groupField:'cultname' ">
        </div>
    </div>--%>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>所属场馆：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:245px; height:32px" name="cultid" id="cultid" data-options="editable:false,required:true" />
            <input class="easyui-combobox" style="width:245px; height:32px" name="venid" id="venid" data-options="editable:false,required:true" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>活动室分类：</div>
        <div class="whgff-row-input">
            <%--<div class="radio radio-primary whg-js-data" name="etype" value="${info.etype}"
                 js-data="WhgComm.getRoomType">
            </div>--%>
            <div class="radio radio-primary" id="etype" name="etype"></div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>是否收费：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="hasfees" value="${info.hasfees}"
                 js-data='[{"id":"0","text":"不收费"},{"id":"1","text":"收费"}]'>
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">
            <label style="color: red">*</label>提醒方式：
        </div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="noticetype" value="${info.noticetype}"
                 js-data='[{"id":"SMS","text":"短信"},{"id":"ZNX","text":"站内信"}]'>
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">活动室标签：</div>
        <div class="whgff-row-input">
            <%--<div class="checkbox checkbox-primary whg-js-data" name="etag" value="${info.etag}"
                 js-data="WhgComm.getRoomTag">
            </div>--%>
            <div class="checkbox checkbox-primary" id="etag" name="etag"></div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">活动室关键字：</div>
        <div class="whgff-row-input">
            <%--<select class="easyui-combobox" id="ekey" style="width:600px; height:32px" validType="notQuotes"
                    data-options="multiple:true, valueField:'text', textField:'text', data:WhgComm.getRoomKey()" ></select>
                (多于2个关键字之间用半角逗号分隔)--%>
            <input class="easyui-combobox" id="ekey" style="width:500px; height:32px" validType="notQuotes"
                   data-options="multiple:true, editable:true, prompt:'请填写关键字'
                   ,onChange: function (val, oldval) {
                        if (val.length>1 && val[0]==''){
                            val.shift();
                            $(this).combobox('setValues', val);
                        }
                    }"/>
            <span>（如需手动输入，请用英文逗号隔开！）</span>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">活动室设施：</div>
        <div class="whgff-row-input">
            <%--<div class="checkbox checkbox-primary whg-js-data" name="facility" value="${info.facility}"
                 js-data="WhgComm.getSBType" >
            </div>--%>
            <div class="checkbox checkbox-primary" id="facility" name="facility"></div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">开放时间：</div>
        <div class="whgff-row-input">
            <div class="checkbox checkbox-primary whg-js-data" name="openweek" value="${info.openweek}"
                 js-data='[{"id":"2","text":"周一"},{"id":"3","text":"周二"},{"id":"4","text":"周三"},{"id":"5","text":"周四"},{"id":"6","text":"周五"},{"id":"7","text":"周六"},{"id":"1","text":"周日"}]'>
            </div>

            <div class="whg-week-cont whgmodule-weektimes" whgmodule-options="{refWeekCheckbox:'input[name=\'openweek\']'}">
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">座位模板：</div>
        <div class="whgff-row-input">
            <input class="easyui-numberbox" name="seatrows" value="${info.seatrows}" style="width:100px; height:32px"
                   data-options="required:false,min:0,max:99, prompt:'行数'">
            -
            <input class="easyui-numberbox" name="seatcols" value="${info.seatcols}" style="width:100px; height:32px"
                   data-options="required:false,min:0,max:99, prompt:'列数'">
            <a class="easyui-linkbutton js-test" text="设置行列数"></a>
        </div>
        <div class="whgff-row-map whgmodule-venseatmaps"
             whgmodule-options="{
             type: '${targetShow}'?'show':'edit',
             rowNum:'${info.seatrows}',
             colNum:'${info.seatcols}',
             onInit:initSeatValue}"></div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>地址：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="location" value="${info.location}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,60]'], prompt:'请输入位置信息，如：1栋108室'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>面积(m<sup>2</sup>)：</div>
        <div class="whgff-row-input">
            <input class="easyui-numberbox" name="sizearea" value="${info.sizearea}" style="width:300px; height:32px"
                   data-options="required:true,precision:3,min:0,max:1000000, prompt:'请输入面积'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>可容人数：</div>
        <div class="whgff-row-input">
            <input class="easyui-numberbox" name="sizepeople" value="${info.sizepeople}" style="width:300px; height:32px"
                   data-options="required:true,min:0,max:1000000, prompt:'请输入可容人数'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>场地简介：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="summary" value="${info.summary}" multiline="true" style="width:600px;height: 100px;"
                   data-options="required:true,validType:['length[1,400]']">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>场地描述：</div>
        <div class="whgff-row-input">
            <div id="description" name="_description" type="text/plain" style="width:600px; height:200px;"></div>
            <textarea id="value_description" style="display: none;">${info.description}</textarea>
        </div>
    </div>

</form>

<div class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <%--<a class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok">提 交</a>
    <a class="easyui-linkbutton whgff-but-clear" iconCls="icon-no">清 空</a>--%>
</div>

<script>


    /** 初始座位值*/
    function initSeatValue(){
        var seatjson = '${info.seatjson}';
        if (seatjson){
            $(this).whgVenseatmaps('setValue', JSON.parse(seatjson));
        }
    };

    $(function(){
        formTool.init();
    });

    var formTool = {
        /** 入口*/
        init: function () {
            var pageType = this.getPageType();

            this.__whgUEInit();
            this.__whgImgInit();

            var me = this;

            me.frm = $("#whgff");
            me.buttonDiv = $("div.whgff-but");
            me.seatmap = $("div.whgff-row-map");

            //时段设置
            var weektimejson = '${info.weektimejson}';
            if (weektimejson){
                $(".whgmodule-weektimes").whgWeektimes("setValue", JSON.parse(weektimejson));
            }

            if (me[pageType] && $.type(me[pageType])==='function'){
                me[pageType].call(me);
            }

            WhgComm.initPMS({
                basePath:'${basePath}',
                cultEid:'cultid', cultValue:'${whgVen.cultid}',
                venEid:'venid', venValue:'${info.venid}',
                ywiTypeEid:['etype', 'facility'], ywiTypeValue:['${info.etype}','${info.facility}'], ywiTypeClass:[3,7],
                ywiKeyEid:'ekey', ywiKeyValue:'${info.ekey}', ywiKeyClass:3,
                ywiTagEid:'etag', ywiTagValue:'${info.etag}', ywiTagClass:3
            });
        },

        /** 页面类型*/
        getPageType: function(){
            return '${pageType}';
        },

        /** 查看*/
        show : function(){
            var me = this;
            $("#ekey").combobox('setValue', "${info.ekey}");

            //取消表单验证
            me.frm.form("disableValidation");
            var undoBut = me.__appendButton("返 回", 'icon-undo', function(){ me.__closeForm(); });

            //组件设为只读
            $('.easyui-textbox').textbox('readonly');
            $('.easyui-combobox').combobox('readonly');
            $('.easyui-datebox').combobox('readonly');
            $('.easyui-numberbox').combobox('readonly');
            $(".whgmodule-weektimes").whgWeektimes("setIsShow");
            //处理选项点击不生效
            //me.frm.find("input[type='checkbox']").on('click', function(){return false});
            me.frm.find("input[type='radio']").attr('disabled', true);
            $("div.radio").on("DOMNodeInserted", function(e){
                $(e.target).attr('disabled', true);
            });
            me.frm.on('click', "input[type='checkbox']", function(){return false});

            $("#imgUploadBtn1").hide();
            $('.js-test').hide();
            me.seatmap.whgVenseatmaps("setModuleType", "show");
        },

        /** 编辑*/
        edit : function () {
            var me = this;
            $("#ekey").combobox('setValue', "${info.ekey}");

            var okBut = me.__appendButton("提 交", 'icon-ok');
            var undoBut = me.__appendButton("返 回", 'icon-undo', function(){ me.__closeForm(); });

            me.__onRCNumClick();

            var id = '${id}';
            me.__submitForm(okBut, '${basePath}/admin/venue/room/edit', id);
        },

        /** 添加*/
        add : function() {
            var me = this;

            var okBut = me.__appendButton("提 交", 'icon-ok');
            //var noBut = me.__appendButton("清 空", 'icon-no', function(){ me.__clearForm(); });
            var undoBut = me.__appendButton("返 回", 'icon-undo', function(){ me.__closeForm(); });

            me.__onRCNumClick();

            me.__submitForm(okBut, '${basePath}/admin/venue/room/add');
        },

        /** 表单提交*/
        __submitForm: function (okBut, url, id) {
            var me = this;
            var pageType = me.getPageType();

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

                    //时段相关值处理 START
                    var weekvalida = $(".whgmodule-weektimes").whgWeektimes("validaValue");
                    if (weekvalida){
                        var weekTimes = $(".whgmodule-weektimes").whgWeektimes("getValue");
                        param.weektimejson = JSON.stringify(weekTimes);
                    }else{
                        $("input[name='openweek']:checked:eq(0)").focus();
                        isValid = weekvalida;
                    }
                    //时段相关值处理 END

                    if (isValid){
                        isValid = me.__validata(param);
                    }

                    if (!isValid){
                        $.messager.progress('close');
                        oneSubmit();
                    }else{
                        if (id){ param.id = id; }
                        param.ekey = $("#ekey").combobox('getText');

                        //坐位相关值处理 START
                        var seatsValue = me.seatmap.whgVenseatmaps('getValue');
                        param.seatjson = JSON.stringify(seatsValue);
                        var options= me.seatmap.whgVenseatmaps('options');
                        $('input[name="seatrows"]').val(options.rowNum);
                        $('input[name="seatcols"]').val(options.colNum);
                        //坐位相关值处理 END
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
                                    /*if (pageType=='edit'){*/
                                        me.__closeForm();
                                    /*}else{
                                        me.__clearForm();
                                    }*/
                                }
                        );
                    }else{
                        $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                    }
                }
            })
        },

        __validata: function (param) {
            var me = this;
            var picture1 = $("#cult_picture1").val();
            if (!picture1){
                $.messager.alert("错误", '图片不能为空', 'error');
                return false;
            }

            var etype = $("[name='etype']:radio:checked").val();
            if (!etype){
                $.messager.alert("错误", '活动室分类不能为空', 'error');
                return false;
            }

            if (!me.ue_description.hasContents()){
                $.messager.alert("错误", '活动室描述不能为空', 'error');
                return false;
            } else{
                param.description = me.ue_description.getContent();
            }

            var notNumCount = $(".whgmodule-venseatmaps li.type-0:empty").length;
            if (notNumCount){
                $.messager.alert("错误", notNumCount+'个座位没有设置座位号，请双击设置', 'error');
                return false;
            }

            return true;
        },

        /** 设置行列点击*/
        __onRCNumClick: function(){
            var me = this;
            $('.js-test').on("click", function(){
                var rownum = $('input[name="seatrows"]').val();
                var colnum = $('input[name="seatcols"]').val();
                me.seatmap.whgVenseatmaps('setSeatSize', rownum, colnum);
            });
        },

        /** 表单清空*/
        __clearForm: function () {
            var me = this;

            me.frm.form("disableValidation");
            me.frm.form('clear');
            me.whgImg.clear();
            //第一个单选又点上
            me.frm.find("div.radio").find(':radio:eq(0)').click();
            me.ue_description.setContent('');
            me.seatmap.whgVenseatmaps('setSeatSize', 0, 0);
            $(".whgmodule-weektimes").whgWeektimes("clear");
        },

        /** 关闭返回*/
        __closeForm: function () {
            window.parent.whgListTool.reload();
            WhgComm.editDialogClose();
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


        __whgImgInit: function(){
            this.whgImg = WhgUploadImg.init({
                basePath: '${basePath}',
                uploadBtnId: 'imgUploadBtn1',
                hiddenFieldId: 'cult_picture1',
                previewImgId: 'previewImg1'
            });
        },

        /** 初始UE 组件*/
        __whgUEInit: function () {
            var pageType = this.getPageType();
            var ueConfig = {
                scaleEnabled: false,
                autoFloatEnabled: false,
                elementPathEnabled:false,
                readonly: pageType=='show'
            };
            var ue_description = UE.getEditor('description', ueConfig);
            ue_description.ready(function(){  ue_description.setContent($("#value_description").val()) });

            this.ue_description = ue_description;
        }
    };


</script>

</body>
</html>
