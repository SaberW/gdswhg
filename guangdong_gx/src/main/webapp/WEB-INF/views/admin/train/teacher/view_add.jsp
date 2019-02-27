<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <c:choose>
        <c:when test="${not empty id and not empty targetShow}">
            <c:set var="pageTitle" value="查看培训师资"></c:set>
        </c:when>
        <c:when test="${not empty id}">
            <c:set var="pageTitle" value="编辑培训师资"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="添加培训师资"></c:set>
        </c:otherwise>
    </c:choose>
    <title>${pageTitle}</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>

    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>

    <script src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>

    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>

    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <!-- 图片上传相关-END -->

    <script type="text/javascript" src="${basePath}/static/common/js/area.js"></script>

    <style>
        .slider-h{margin-left: 10px}
    </style>
</head>
<body>
<form id="whgff" method="post" class="whgff">
    <h2>${pageTitle}</h2>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>图　　片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture1" name="teacherpic" value="${tea.teacherpic}">
            <div class="whgff-row-input-imgview" id="previewImg1"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>老师名称：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="name" value="${tea.name}" style="width:500px; height:32px"
                   data-options="required:true,validType:['length[1,60]'],prompt:'请输入老师名称'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>所属单位：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:245px; height:32px" name="cultid" id="cultid" data-options="required:true" />
            <input class="easyui-combobox" style="width:245px; height:32px" name="deptid" id="deptid" data-options="required:true" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>区　　域：</div>
        <input class="easyui-combobox" style="width:150px; height:32px" id="province" name="province" data-options="required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name', data:__PROVINCE, onChange:myChangeProvince"/>
        <input class="easyui-combobox" style="width:150px; height:32px" id="__CITY_ELE" name="city" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name', data:[], onChange:myChangeCity"/>
        <input class="easyui-combobox" style="width:179px; height:32px" id="__AREA_ELE" name="area" data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>微专业　：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" id="majorid" name="majorid" value="${majorid}" style="width:500px; height:32px" data-options="multiple:true,editable:true "/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>艺术类型：</div>
        <div class="whgff-row-input">
            <div class="checkbox checkbox-primary" id="arttype" name="arttype"></div>
            <%--<div class="checkbox checkbox-primary whg-js-data" value="${tea.arttype}" name="arttype" js-data="WhgComm.getArtType"></div>--%>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>专长类型：</div>
        <div class="whgff-row-input">
           <div class="checkbox checkbox-primary" id="teachertype" name="teachertype"></div>
            <%--<div class="checkbox checkbox-primary whg-js-data" value="${tea.teachertype}" name="teachertype" js-data="WhgComm.getTEAType"></div>--%>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>固定号码：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="fixednumber" value="${tea.fixednumber}" style="width:500px; height:32px"
                   data-options="required:true,validType:['length[1,20]', 'isTel'],prompt:'请输入固定号码,如：010-88888888'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>注册日期：</div>
        <div class="whgff-row-input">
            <input class="easyui-datetimebox" name="crtdate" value="<fmt:formatDate value="${tea.crtdate}" pattern="yyyy-MM-dd HH:mm:ss"/>" style="width:200px; height:32px" data-options="required:true">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>老师简介：</div>
        <div class="whgff-row-input">
            <textarea id="textarea_teacherdesc" style="display: none">${tea.teacherdesc}</textarea>
            <input class="easyui-textbox" name="teacherdesc" id="teacherdesc" value="" style="width:500px; height:128px" data-options="multiline:true, required:true, validType:['length[1,1000]'], prompt:'请填写老师简介'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">性　　别：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="sex" value="${tea.sex}" js-data="getSexData">
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">政治面貌：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="politicalstatus" value="${tea.politicalstatus}" js-data="getPoliticalStatusData"></div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">职　　业：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="occupation" value="${tea.occupation}" js-data="getOccupationData"></div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">学　　历：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="education" value="${tea.education}" js-data="getEducationData"></div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">出生日期：</div>
        <div class="whgff-row-input">
            <input class="easyui-datebox" name="birthday" value="<fmt:formatDate value="${tea.birthday}" pattern="yyyy-MM-dd"/>" style="width:200px; height:32px" data-options="" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">工作单位：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="workunit" value="${tea.workunit}" style="width:500px; height:32px" data-options="validType:['length[1,60]'],prompt:'请输入工作单位名称'" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">手机号码：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="phonenumber" value="${tea.phonenumber}" style="width:500px; height:32px" data-options="validType:'isPhone', prompt:'请输入手机号码，如：13688888888'" />
        </div>
    </div>



    <div class="whgff-row">
        <div class="whgff-row-label">兴趣爱好：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="hobby" value="${tea.hobby}" style="width:500px; height:32px" data-options="validType:'length[1,64]',prompt:'请输入培训师资的兴趣爱好'" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">所在地　：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="location" value="${tea.location}" style="width:500px; height:32px" data-options="validType:'length[1,64]',prompt:'请输入培训师资的所在地'" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">职　　称：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="titles" value="${tea.titles}" style="width:500px; height:32px" data-options="validType:'length[1,64]',prompt:'请输入培训师资的资质'" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">资　　质：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="aptitude" value="${tea.aptitude}" style="width:500px; height:32px" data-options="validType:'length[1,64]',prompt:'请输入培训师资的资质'" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">荣　　誉：</div>
        <div class="whgff-row-input">
            <textarea id="textarea_honor" style="display: none">${tea.honor}</textarea>
            <input class="easyui-textbox" name="honor" id="honor" value="" style="width:500px; height:128px" data-options="multiline:true, validType:'length[1,1000]',prompt:'请输入培训师资的荣誉信息'" />
        </div>
    </div>

    <%--<div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>标签：</div>
        <div class="whgff-row-input">
            <div class="checkbox checkbox-primary" id="etag" name="etag"></div>
            &lt;%&ndash;<div class="checkbox checkbox-primary whg-js-data" value="${tea.etag}" name="etag" js-data="WhgComm.getPxszTag"></div>&ndash;%&gt;
        </div>
    </div>--%>
    <%--<div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>关键字：</div>
        <div class="whgff-row-input">
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
    </div>--%>





    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red"></label>专长介绍：</div>
        <div class="whgff-row-input">
            <textarea id="textarea_teacherexpdesc" style="display: none">${tea.teacherexpdesc}</textarea>
            <input class="easyui-textbox" name="teacherexpdesc" id="teacherexpdesc" value="" style="width:500px; height:128px" data-options="multiline:true, validType:['length[1,400]'],prompt:'请填写专长介绍'">
        </div>
    </div>
    <div class="whgff-row" style="display: none" id="showReason">
        <div class="whgff-row-label">
            下架原因：
        </div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" id="reason" readonly="true" value="" multiline="true"
                   style="width:350px;height: 150px;">
        </div>
    </div>

</form>

<div class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <a class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok">提 交</a>
    <a class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo">返 回</a>
</div>

<script>
    /** 性别  */
    function getSexData() {
        return [{"id":"0", "text":"女"}, {"id":"1", "text":"男"}];
    }

    //政治面貌 中共党员,民主党派,群众,其他
    function getPoliticalStatusData() {
        return [{"id":"中共党员", "text":"中共党员"}, {"id":"民主党派", "text":"民主党派"}, {"id":"群众", "text":"群众"}, {"id":"其他", "text":"其他"}];
    }

    //职业:国家公务员,专业技术人员,职员,企业管理人员,工人,农民,学生,教师,现役军人,自由职业者,个体经营者,无业人员,退（离）休人员,其他
    function getOccupationData() {
        return [
            {"id":"国家公务员", "text":"国家公务员"}, {"id":"专业技术人员", "text":"专业技术人员"}, {"id":"职员", "text":"职员"}, {"id":"企业管理人员", "text":"企业管理人员"}
            ,{"id":"工人", "text":"工人"},{"id":"农民", "text":"农民"},{"id":"学生", "text":"学生"},{"id":"教师", "text":"教师"},{"id":"现役军人", "text":"现役军人"}
            ,{"id":"自由职业者", "text":"自由职业者"},{"id":"个体经营者", "text":"个体经营者"},{"id":"无业人员", "text":"无业人员"}
            ,{"id":"退（离）休人员", "text":"退（离）休人员"},{"id":"其他", "text":"其他"}
        ];
    }

    //学历:小学,初中,高中,专科,本科,硕士,博士,其他
    function getEducationData(){
        return [
            {"id":"小学", "text":"小学"}, {"id":"初中", "text":"初中"}, {"id":"高中", "text":"高中"}, {"id":"专科", "text":"专科"}
            ,{"id":"本科", "text":"本科"},{"id":"硕士", "text":"硕士"},{"id":"博士", "text":"博士"},{"id":"其他", "text":"其他"}
        ];
    }

    //省市区
    var province = '${tea.province}';
    var city = '${tea.city}';
    function myChangeProvince(newVal, oldVal) {
        changeProvince(newVal, oldVal, function(){
            if(typeof(window.__init_city) == 'undefined'){
                if(!city || city==''){
                    city = WhgComm.getCity()?WhgComm.getCity():"";
                }
                $('#__CITY_ELE').combobox('setValue', city);
                window.__init_city = true;
            }
        });
    }

    function myChangeCity(newVal, oldVal) {
        changeCity(newVal, oldVal, function(){
            if(typeof(window.__init_area) == 'undefined'){
                $('#__AREA_ELE').combobox('setValue', '${tea.area}');
                window.__init_area = true;
            }
        });
    }  //省市区------END

    $(function () {
        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'${tea.cultid}',cultOnChange: function (newVal, oldVal) {
                $("#majorid").combobox({
                    url:'${basePath}/admin/tra/major/srchList?cultid='+newVal,
                    valueField:'id',
                    textField:'name'
                })
            },
            deptEid:'deptid', deptValue:'${tea.deptid}',
            ywiArtTypeEid:'arttype', ywiArtTypeValue:'${tea.arttype}',
            ywiTypeEid:'teachertype', ywiTypeValue:'${tea.teachertype}', ywiTypeClass:11/*,
            ywiKeyEid:'ekey', ywiKeyValue:'$ {tea.ekey}', ywiKeyClass:27,
            ywiTagEid:'etag', ywiTagValue:'$ {tea.etag}', ywiTagClass:27*/
        });


        var mid = '${mid}';
        //初始省值
       // $('#province').combobox('setValue', province);
        if (!province || province==''){
            province = WhgComm.getProvince()?WhgComm.getProvince():'广东省';
        }
        $('#province').combobox('setValue', province);

        var city = '${info.city}';
        if (!city || city==''){
            city = WhgComm.getCity()?WhgComm.getCity():"";
        }
        $("#__CITY_ELE").combobox('setValue', city); //初始省值

        var id = '${id}';
        var infostate = '${tea.state}';
        if (infostate && infostate == "4") {
            showXjReason(id);
        }
        //图片初始化
        var __WhgUploadImg = WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1'});

        var buts = $("div.whgff-but");

        var targetShow = '${targetShow}';
        var frm = $("#whgff");
        //处理返回
        buts.find("a.whgff-but-clear").linkbutton({
            text: '返 回',
            iconCls: 'icon-undo',
            onClick: function(){
                if (!targetShow){
                    window.parent.$('#whgdg').datagrid('reload');
                }
                WhgComm.editDialogClose();
            }
        });

        //初始多行文本的值
        $('#teacherdesc').textbox('setValue', $('#textarea_teacherdesc').val());
        $('#honor').textbox('setValue', $('#textarea_honor').val());
        $('#teacherexpdesc').textbox('setValue', $('#textarea_teacherexpdesc').val());

        //查看时的处理
        if (targetShow){
            //取消表单验证
            frm.form("disableValidation");

            //组件设为只读
            $('.easyui-textbox').textbox('readonly');
            $('.easyui-combobox').combobox('readonly');
            $('.easyui-datetimebox').combobox('readonly');

            $("#imgUploadBtn1").hide();
            //处理选项点击不生效
            frm.find("input[type='checkbox'], input[type='radio']").on('click', function(){return false});

            //不显示提交 button
            buts.find("a.whgff-but-submit").hide();
            return;
        }

        //定义表单提交
        var url = id ? "${basePath}/admin/train/tea/edit" : "${basePath}/admin/train/tea/add";
        frm.form({
            url: url,
            novalidate: true,
            onSubmit: function (param) {
                if (id){
                    param.id = id;
                }
                if (mid){
                    param.mid = mid;
                }
                $(this).form("enableValidation");
                var isValid = $(this).form('validate');
                if(isValid){
                    //图片不能为空
                    var picture1 = $("#cult_picture1").val();
                    if (!picture1){
                        $.messager.alert("错误", '图片不能为空！', 'error');
                        isValid = false;
                    }
                    //艺术分类不能为空
                    var arttype = $("#whgff").find("input[name='arttype']:checked").val();
                    if (isValid && !arttype){
                        $.messager.alert("错误", '艺术类型不能为空！', 'error');
                        isValid = false;
                    }
                    //专长类型不能为空
                    var teachertype = $("#whgff").find("input[name='teachertype']:checked").val();
                    if (isValid && !teachertype){
                        $.messager.alert("错误", '专长类型不能为空！', 'error');
                        isValid = false;
                    }
                }
                if (!isValid){
                    $.messager.progress('close');
                    buts.find("a.whgff-but-submit").off('click').one('click', function () {
                        frm.submit();
                    });
                }
                //param.ekey = $("#ekey").combobox("getText");
                return isValid;

            },
            success : function (data) {
                data = $.parseJSON(data);
                $.messager.progress('close');
                buts.find("a.whgff-but-submit").off('click').one('click', function () {
                    frm.submit();
                });
                if (!data.success || data.success != "1"){
                    $.messager.alert("错误", data.errormsg||'操作失败', 'error');
                    return;
                }
                if(mid){
                    WhgComm.editDialogClose();
                    window.parent.$('#whgdg').datagrid('reload');
                }

                WhgComm.editDialogClose();
                window.parent.$('#whgdg').datagrid('reload');

            }
        });
        buts.find("a.whgff-but-submit").off('click').one('click', function () {
            frm.submit();
        });
    });
</script>
</body>
</html>
