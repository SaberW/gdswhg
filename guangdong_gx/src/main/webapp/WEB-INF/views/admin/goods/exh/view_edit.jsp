<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">

    <c:choose>
        <c:when test="${pageType eq '2'}">
            <c:set var="pageTitle" value="展览展示管理-查看展览"></c:set>
        </c:when>
        <c:when test="${pageType eq '1'}">
            <c:set var="pageTitle" value="展览展示管理-编辑展览"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="展览展示管理-添加展览"></c:set>
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
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <!-- 图片上传相关-END -->

    <script type="text/javascript" src="${basePath}/static/common/js/area.js"></script>

</head>

<body class="body_add">

<form id="whgff" method="post" class="whgff">
    <h2>${pageTitle}</h2>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>展览名称：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="title" value="${info.exh.title}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,100]'], prompt:'请输入展览名称'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>图片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture1" name="image" value="${info.exh.image}">
            <div class="whgff-row-input-imgview" id="previewImg1"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>供需文化馆：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:245px; height:32px" name="cultid" id="cultid" data-options="required:true" />
            <input class="easyui-combobox" style="width:245px; height:32px" name="deptid" id="deptid" data-options="required:true" />
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>区域：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:150px; height:32px" id="province" name="province" data-options="required:true, readonly:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name', data:__PROVINCE, onChange:myChangeProvince"/>
            <input class="easyui-combobox" style="width:150px; height:32px" id="__CITY_ELE" name="city" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name', data:[], onChange:myChangeCity"/>
            <input class="easyui-combobox" style="width:179px; height:32px" id="__AREA_ELE" name="area" data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>展览类型：</div>
        <div class="whgff-row-input">
            <div class="checkbox checkbox-primary" id="etype" name="etype" data-options="required:true"></div>
            </div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>艺术分类：</div>
        <div class="whgff-row-input">
            <div class="checkbox checkbox-primary" id="arttype" name="arttype" data-options="required:true"></div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>关键字：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" id="ekey" style="width:500px; height:32px" validType="notQuotes" data-options="required:true, multiple:true, editable:true, prompt:'请填写关键字'
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
        <div class="whgff-row-label"><label style="color: red">*</label>展品：</div>
        <div class="whgff-row-input">
            <select class="easyui-combobox select-cultid" id="exhgoods" name="exhgoods" style="width:500px; height:32px" prompt="请选择展品"
                    data-options="required:true, multiple:true,editable:false, value:'${info.exh.exhgoods}'
                    "></select>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>展览幅数：</div>
        <div class="whgff-row-input">
            <input class="easyui-numberbox" name="exhnum" value="${info.exh.exhnum}" style="width:300px; height:32px"
                   data-options="required:true,min:0,max:1000000, prompt:'请输入展览幅数'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>特装：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="special" value="${info.exh.special}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,100]'], prompt:''">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>展期：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="exhtime" value="${info.exh.exhtime}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,100]'], prompt:'请输入展期'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>布展时间：</div>
        <div class="whgff-row-input">
            <input class="easyui-numberbox" name="arrayexhtime" value="${info.exh.arrayexhtime}" style="width:300px; height:32px"
                   data-options="required:true,min:0,max:1000000, prompt:'请输入布展时间'"> 天
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>配套活动：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="elseact" value="${info.exh.elseact}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,100]'], prompt:'请输入配套活动'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>合适人群：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="fitcrowd" value="${info.exh.fitcrowd}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,100]'], prompt:'请输入合适人群'">
        </div>
    </div>

    <%--<div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>是否收费：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="ismoney" value="${info.exh.ismoney}"
                 js-data='[{"id":"0","text":"不收费"},{"id":"1","text":"收费"}]'>
            </div>
        </div>
    </div>--%>

   <%-- <div class="whgff-row">
        <div class="whgff-row-label">展览机构：</div>
        <div class="whgff-row-input">
            <select class="easyui-combobox select-cultid" id="exhorgan" name="exhorgan" value="${info.exh.exhorgan}" style="width:500px; height:32px" prompt="请选择所属机构"
                    data-options="editable:false,value:'${info.exh.exhorgan}'"></select>
        </div>
    </div>--%>

    <div class="whgff-row">
        <div class="whgff-row-label"><label style="color: red">*</label>联系人：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="exhcontacts" value="${info.exh.exhcontacts}" style="width:500px; height:32px"
                   data-options="required:true,validType:['length[1,100]'], prompt:'请输入联系人'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>联系电话：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="exhphone" value="${info.exh.exhphone}" style="width:500px; height:32px" data-options="required:true,validType:'isPhone[\'exhphone\']',prompt:'请填写联系方式'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>邮箱：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="email" value="${info.exh.email}" style="width:500px; height:32px"
                   data-options="required:true,validType:['length[1,20]', 'isEmail'], prompt:'请输入邮箱'">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">所属单位：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="workplace" value="${info.exh.workplace}" style="width:300px; height:32px"
                   data-options="required:false, prompt:'请输入所属单位'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">固定电话：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="telephone" value="${info.exh.telephone}" style="width:300px; height:32px"
                   data-options="required:false,validType:'isTel', prompt:'请输入固定电话'">
        </div>
    </div>

    <%--<div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>配送范围：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:150px; height:32px" name="psprovince" id="__PSPROVINCE_ELE"
                   data-options="readonly:true, required:true, prompt:'请选择省', editable:false, limitToList:true, valueField:'name', textField:'name' "/>
            <input class="easyui-combobox" style="width:400px; height:32px" name="pscity" id="__PSCITY_ELE"
                   data-options="required:true, prompt:'请选择市(多选)', editable:false, multiple:true, valueField:'name', textField:'name'"/>
        </div>
    </div>--%>


    <%--<div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>配送时间：</div>
        <div class="whgff-row-input" style="width:500px" id="timesUI">
        </div>
    </div>--%>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>说明：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="exhexplain" value="${not empty info.exh.exhexplain? info.exh.exhexplain :'一旦达成意向，经费参照国家和地方相关标准执行，相关具体事宜由派出方与承接方相互协商而定。'}" style="width:500px; height:60px"
                   data-options="multiline:true,required:true,validType:['length[1,500]'], prompt:'请输入展览说明'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>展览简介：</div>
        <div class="whgff-row-input">
            <div id="exhdesc" name="exhdesc" type="text/plain" style="width:800px; height:200px;"></div>
            <textarea id="value_exhdesc" style="display: none;">${info.exh.exhdesc}</textarea>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">备注：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="exhcomment" value="${info.exh.exhcomment}" style="width:500px; height:32px"
                   data-options="required:false,validType:['length[1,100]'], prompt:'备注'">
        </div>
    </div>
    <a name="firstAnchor01"></a>
    <div class="whgff-row">
        <div class="whgd-gtb-btn"   style="float:left;margin-left:120px">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="add();">硬件设施配置添加</a>
        </div>
    </div>
    <div class="whgff-row" id="show_yjpz">

        <div style="float:left;margin-left:180px">
            <table style="width: 500px;text-align: center"   >
                <thead>
                <tr >
                    <td style="text-align: center">硬件设施配置名称</td>
                    <td style="text-align: center">规格要求</td>
                    <td style="text-align: center">操作</td>
                </tr>
                </thead>
                <tbody id="yjpz_tbody"></tbody>
            </table>



            <%--<div style="float: left"><label style="color: red">*</label>xxxxx：</div>
            <div  style="float:left">
                <input class="easyui-textbox" name="special" value="${info.exh.special}" style="width:180px; height:32px"
                       data-options="required:true,validType:['length[1,100]'], prompt:''">
            </div>
            <div  style="float:left"><label style="color: red">*</label>yyyyy：</div>
            <div  style="float:left">
                <input class="easyui-textbox" name="special" value="${info.exh.special}" style="width:180px; height:32px"
                       data-options="required:true,validType:['length[1,100]'], prompt:''">
            </div>--%>
        </div>
    </div>

    <div class="whgff-row" style="display: none" id="showReason">
        <div class="whgff-row-label">
            下架原因：
        </div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" id="reason" readonly="true" value="" multiline="true"
                   style="width:550px;height: 150px;">
        </div>
    </div>
</form>

<div class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <a class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok">提 交</a>
    <a class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose();">返 回</a>
</div>
<!-- 添加表单 -->
<div id="whgwin-add" style="display: none">
    <form id="whgff01" class="whgff" method="post">
        <div class="whgff-row">
            <span id="error_msg" style="color: rgba(255,39,42,0.91)">名称和规格要求都必须填写</span>
        </div>
        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 25%"><i>*</i>名称：</div>
            <div class="whgff-row-input" style="width: 75%"><input  name="name" id="yjpzName"  style="width:90%; height:32px;border-radius: 4px;border: 1px solid gainsboro" data-options="required:true,validType:'length[0,20]'"></div>
        </div>

        <div class="whgff-row">
            <div class="whgff-row-label" style="width: 25%"><i>*</i>规格要求：</div>
            <div class="whgff-row-input" style="width: 75%"><input  name="detail" id="yjpzDetail" style="width:90%; height:60px;border-radius: 4px;border: 1px solid gainsboro" data-options="required:true,validType:'length[0,200]'"></div>
        </div>
    </form>
</div>
<div id="whgwin-add-btn" style="text-align: center; display: none">
    <div style="display: inline-block; margin: 0 auto">
        <a href="#" class="easyui-linkbutton" iconCls="icon-save" id="btn">保 存</a>
        <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#whgwin-add').dialog('close')">关 闭</a>
    </div>
</div>
<!-- 添加表单 END -->
<script>
    var TEMP=0;
    var quan;
    $(function(){
        $.ajax
        ({
            cache: false,
            async: false,
            dataType: 'json', type: 'get',
            url: "/admin/yunwei/yjpz/srchListByExhId?ExhId=${info.exh.id}",
            success: function (data){
                if(data[0]){
                    var yjpz_tbody = document.getElementById("yjpz_tbody");
                    for(var i=0;i<data.length;i++){
                        show_yjpz(data[i],yjpz_tbody);
                    }
                }else{
                    $("#show_yjpz").hide();
                }

            }
        });
        $('#btn').click(function (e) {
            if(yjpz_Rule()){
                $("#error_msg").show();
                return false;
            }
            if(TEMP==1){
                save();
                $("#show_yjpz").show();
                var iframes = document.getElementsByTagName("iframe");
                iframes[0].contentDocument.location.href="#firstAnchor01";
            }else{
                var tbody_tr = quan.parentNode.parentNode;
                var childNodes = tbody_tr.childNodes;
                var yjpzName_value = document.getElementById("yjpzName");
                var yjpzDetail_value = document.getElementById("yjpzDetail");
                childNodes[0].childNodes[0].value=yjpzName_value.value;
                childNodes[1].childNodes[0].value=yjpzDetail_value.value;
                $("#show_yjpz").show();
                $('#whgff01').form("clear");
                $('#whgwin-add').dialog('close')
                var iframes = document.getElementsByTagName("iframe");
                iframes[0].contentDocument.location.href="#firstAnchor01";
            }
        })
    });
    //校验硬件配置和规格都存在
    function yjpz_Rule() {
        var yjpzName_value = document.getElementById("yjpzName").value;
        var yjpzDetail_value = document.getElementById("yjpzDetail").value;
        if(!yjpzName_value||!yjpzDetail_value){
            return true;
        }
    }



    function edit(obj) {
        //首先将数据展现在表单中
        var tbody_tr = obj.parentNode.parentNode;
        var childNodes = tbody_tr.childNodes;
        var yjpzName_value = document.getElementById("yjpzName");
        var yjpzDetail_value = document.getElementById("yjpzDetail");
        var value01=childNodes[0].childNodes[0].value;
        yjpzName_value.value=value01;
        var value02= childNodes[1].childNodes[0].value;
        yjpzDetail_value.value=value02;

        //将修改好的表单数据再向原位置保存

    }

    //YI@ 将相关链的硬件配置放入页面表格
    function show_yjpz(yjpz,yjpz_tbody) {
        var c_tr = document.createElement("tr");

        var td01 = document.createElement("td");
        var td01_put=document.createElement("input");
        td01_put.setAttribute("style","border-radius: 4px;border: 1px solid gainsboro")
        td01_put.setAttribute("readonly","readonly");
        td01_put.setAttribute("name","yjpzName");//readonly="readonly"
        td01_put.setAttribute("value",yjpz.name);
        td01.appendChild(td01_put);
        c_tr.appendChild(td01);

        var td02 = document.createElement("td");
        var td02_put=document.createElement("input");
        td02_put.setAttribute("style","border-radius: 4px;border: 1px solid gainsboro")
        td02_put.setAttribute("readonly","readonly");
        td02_put.setAttribute("name","yjpzDetail");
        td02_put.setAttribute("type","text");
        td02_put.setAttribute("value",yjpz.detail);
        td02.appendChild(td02_put);
        c_tr.appendChild(td02);

        var td03 = document.createElement("td");
        var td03_a01=document.createElement("a")
        td03_a01.setAttribute("href","javascript:void(0);");
        td03_a01.setAttribute("onclick","add(this);");
        var td03_aText01 = document.createTextNode("编辑");
        td03_a01.appendChild(td03_aText01);
        td03.appendChild(td03_a01);



        var td03_a02=document.createElement("a")
        td03_a02.setAttribute("href","javascript:void(0);");
        td03_a02.setAttribute("style","margin-left: 5px");
        td03_a02.setAttribute("onclick","dele(this);");
        var td03_aText02 = document.createTextNode("删除");
        td03_a02.appendChild(td03_aText02);
        td03.appendChild(td03_a02);

        c_tr.appendChild(td03);
        yjpz_tbody.appendChild(c_tr);

    }


    function save() {
        var c_tr = document.createElement("tr");
        var yjpzName_value = document.getElementById("yjpzName");
        var yjpzDetail_value = document.getElementById("yjpzDetail");


        var td01 = document.createElement("td");
        var td01_put=document.createElement("input");
        td01_put.setAttribute("style","border-radius: 4px;border: 1px solid gainsboro")
        td01_put.setAttribute("readonly","readonly");
        td01_put.setAttribute("name","yjpzName");
        td01_put.setAttribute("value",yjpzName_value.value);
        td01.appendChild(td01_put);
        c_tr.appendChild(td01);

        var td02 = document.createElement("td");
        var td02_put=document.createElement("input");
        td02_put.setAttribute("style","border-radius: 4px;border: 1px solid gainsboro")
        td02_put.setAttribute("readonly","readonly");
        td02_put.setAttribute("name","yjpzDetail");
        td02_put.setAttribute("type","text");
        td02_put.setAttribute("value",yjpzDetail_value.value);
        td02.appendChild(td02_put);
        c_tr.appendChild(td02);

        var td03 = document.createElement("td");
        var td03_a01=document.createElement("a")
        td03_a01.setAttribute("href","javascript:void(0);");
        td03_a01.setAttribute("onclick","add(this)");
        var td03_aText01 = document.createTextNode("编辑");
        td03_a01.appendChild(td03_aText01);
        td03.appendChild(td03_a01);


        var td03_a02=document.createElement("a")
        td03_a02.setAttribute("href","javascript:void(0);");
        td03_a02.setAttribute("style","margin-left: 5px");
        td03_a02.setAttribute("onclick","dele(this);");
        var td03_aText02 = document.createTextNode("删除");
        td03_a02.appendChild(td03_aText02);
        td03.appendChild(td03_a02);

        c_tr.appendChild(td03);


        document.getElementById("yjpz_tbody").appendChild(c_tr);
        $('#whgff01').form("clear");


        $('#whgwin-add').dialog('close')

    }

    function dele(obj) {
        var table_node = obj.parentNode.parentNode.parentNode;
        table_node.removeChild(obj.parentNode.parentNode)
        if($("#yjpz_tbody").html()==""){
            $("#show_yjpz").hide();
        }
    }



    var province = '${info.exh.province}';
    var city = '${info.exh.city}';


    //省市区
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
                $('#__AREA_ELE').combobox('setValue', '${info.exh.area}');
                window.__init_area = true;
            }
        });
    }  //省市区------END


    //图片初始化
    var __WhgUploadImg = WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1'});

    //处理UE
    var ueConfig = {
        scaleEnabled:true,
        autoFloatEnabled: false,
        elementPathEnabled:false,
        readonly: '${pageType}' == 2? true: false
    };
    var exhdesc = UE.getEditor('exhdesc', ueConfig);

    //UE 设置值
    exhdesc.ready(function(){  exhdesc.setContent($("#value_exhdesc").val()) });




    $(function () {

        var state = '${info.exh.state}';
        if (state && state == 4) {
            showXjReason('${info.exh.id}');
        }
        /*var aa = '${info.times}';
        $("#timesUI").html("");
        timesUI.init();
        if(aa){
            timesUI.setValue( JSON.parse(aa) );
        }*/


        WhgComm.initPMS({
            basePath:'${basePath}',
            deptEid:'deptid', deptValue:'${info.exh.deptid}',
            cultEid:'cultid', cultValue:'${info.exh.cultid}', cultOnChange: function (newVal, oldVal) {
                $("#exhgoods").combobox({
                    url:'${basePath}/admin/exhGoods/srchList?cultid='+newVal,
                    valueField:'id',
                    textField:'title'
                })
                $("#exhorgan").combobox({
                    url:'${basePath}/admin/showOrgan/srchList?cultid='+newVal,
                    valueField:'id',
                    textField:'title'
                })

            },
            ywiTypeEid:'etype', ywiTypeValue:'${info.exh.etype}', ywiTypeClass:23,
            ywiArtTypeEid:'arttype', ywiArtTypeValue:'${info.exh.arttype}',
            ywiKeyEid:'ekey', ywiKeyValue:'${info.exh.ekey}', ywiKeyClass:23
            /*,psprovinceEid:'__PSPROVINCE_ELE', psprovinceValue:'${info.exh.psprovince}',
            pscityEid:'__PSCITY_ELE', pscityValue:'${info.exh.pscity}'*/
        });

        $("#ekey").combobox("setValue","${info.exh.ekey}");
        //初始省值
        //$('#province').combobox('setValue', '${info.exh.province}');

        if (!province || province==''){
            province = WhgComm.getProvince()?WhgComm.getProvince():'广东省';
        }
        $('#province').combobox('setValue', province);

        var city = '${info.exh.city}';
        if (!city || city==''){
            city = WhgComm.getCity()?WhgComm.getCity():"";
        }
        $("#__CITY_ELE").combobox('setValue', city); //初始省值


        var id = '${id}';
        var pageType = '${pageType}';
        var frm = $("#whgff");
        var buts = $("div.whgff-but");

        //查看时的处理
        if (pageType == 2){
            //取消表单验证
            frm.form("disableValidation");

            //组件设为只读
            $('.easyui-textbox').textbox('readonly');
            $('.easyui-combobox').combobox('readonly');
            $('.easyui-datebox').combobox('readonly');
            $('.easyui-numberspinner').numberspinner('readonly');
            $('.easyui-datetimebox').combobox('readonly');
            $("#getXYPointBtn").hide();
            $("#imgUploadBtn1").hide();
            //处理选项点击不生效
            frm.find("input[type='checkbox'], input[type='radio']").on('click', function(){return false});

            //不显示提交 button
            buts.find("a.whgff-but-submit").hide();
            return;
        }


        //定义表单提交
        var url = id ? "${basePath}/admin/showExh/edit" : "${basePath}/admin/showExh/add";
        frm.form({
            url: url,
            novalidate: true,
            onSubmit: function (param) {
                if (id){
                    param.id = id;
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
                     if (!arttype){
                         $.messager.alert("错误", '艺术分类不能为空！', 'error');
                         isValid = false;
                     }
                    //展览类型不能为空
                    var etype = $("#whgff").find("input[name='etype']:checked").val();
                    if (!etype){
                        $.messager.alert("错误", '展览类型不能为空！', 'error');
                        isValid = false;
                    }
                    //课程详情不能为空
                    if (!exhdesc.hasContents()){
                        $.messager.alert("错误", '展览简介不能为空！', 'error');
                        isValid = false;
                    }
                }
                if (!isValid){
                    $.messager.progress('close');
                    buts.find("a.whgff-but-submit").off('click').one('click', function () {
                        frm.submit();
                    });
                }
                /*var times = timesUI.getValue();
                param.times = JSON.stringify(times);*/
                param.ekey = $("#ekey").combobox("getText");
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


                WhgComm.editDialogClose();
                window.parent.$('#whgdg').datagrid('reload');

            }
        });
        buts.find("a.whgff-but-submit").off('click').one('click', function () {
            frm.submit();
        });
    })


    /**
     * 时间项处理
     * */
    /*var timesUI = {
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
    };*/
    /** 添加关键字 */
    function add(obj){
        $('#whgwin-add').dialog({
            title: '硬件管理-添加硬件',
            cache: false,
            modal: true,
            width: '400',
            height: '250',
            maximizable: true,
            resizable: true,
            buttons: '#whgwin-add-btn',
            onOpen: function (){
                if(obj==undefined){
                    $("#error_msg").hide();
                    TEMP=1;
                    $('#whgff01').form("clear");
                }else {
                    $("#error_msg").hide();
                    TEMP=2;
                    edit(obj);
                    quan=obj;
                };

            }
            /*onOpen: function () {
                $('#whgff').form({
                    url : '${basePath}/admin/yunwei/yjpz/add?type='+type+'&entid='+entid,
                    onSubmit : function(param) {
                        var isValid = $(this).form('enableValidation').form('validate');
                        if(isValid){
                            $.messager.progress();
                        }else{
                            $("#btn").off("click").one("click",function () { $('#whgff').submit(); });
                        }
                        return isValid
                    },
                    success : function(data) {
                        $.messager.progress('close');
                        var Json = $.parseJSON(data);
                        if(Json.success == "1"){
                            $('#whgdg').datagrid('reload');
                            $('#whgwin-add').dialog('close');
                        }else{
                            $.messager.alert("提示", Json.errormsg);
                            $("#btn").off("click").on("click",function () {$('#whgff').submit();});
                        }

                    }
                });
                WhgComm.initPMS({cultEid:'cultid2'});
                $('#whgff').form("clear");
                WhgComm.initPMS({cultEid:'cultid2'});
                $("#btn").off("click").one("click",function () { $('#whgff').submit(); });
            }*/
        });
    }

    /** 更新关键字方法 */
    /*function edit(idx){
        $('#whgwin-add').dialog({
            title: '硬件管理-编辑硬件',
            cache: false,
            modal: true,
            width: '400',
            height: '250',
            maximizable: true,
            resizable: true,
            buttons: '#whgwin-add-btn',
            onOpen : function () {
                var rows = $("#whgdg").datagrid('getRows');
                var row = rows[idx];
                var id = row.id;
                if (row){
                    $('#whgff01').form('load', row);
                    $('#whgff01').form({
                        url : '${basePath}/admin/yunwei/yjpz/edit',
                        onSubmit : function(param) {
                            var isValid = $(this).form('enableValidation').form('validate');
                            if(isValid){
                                $.messager.progress();
                            }else{
                                $("#btn").off("click").one("click",function () { $('#whgff01').submit(); });
                            }
                            return isValid
                        },
                        success : function(data) {
                            $.messager.progress('close');
                            var Json = $.parseJSON(data);
                            if(Json.success == "1"){
                                $('#whgdg01').datagrid('reload');
                                $('#whgwin-add').dialog('close');
                            }else{
                                $.messager.alert("提示", Json.errormsg);
                            }
                        }
                    });
                }
                $("#btn").off("click").on("click",function () {$('#whgff01').submit();});
                WhgComm.initPMS({cultEid:'cultid2', cultValue:row.cultid});
            }
        });
    }
*/
    /*
     * 删除硬件配置 */
  /*  function del(idx){
        var rows = $("#whgdg").datagrid('getRows');
        var row = rows[idx];
        var id = row.id;
        $.messager.confirm('确认对话框', '您确认要删除吗？', function(r){
            if (r){
                $.ajax({
                    type: "POST",
                    url: '${basePath}/admin/yunwei/yjpz/del',
                    data: {id : id},
                    success: function(data){
                        if(data.success == "1"){
                            $('#whgdg').datagrid('reload');
                        }else{
                            $.messager.alert("提示", data.errormsg);
                        }

                    }
                });
            }
        });
    }*/

</script>

</body>
</html>
