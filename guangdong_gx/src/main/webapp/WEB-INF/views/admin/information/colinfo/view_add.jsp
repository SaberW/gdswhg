<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>栏目内容管理</title>

    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>

    <script src="${basePath}/static/ueditor/ueditor.config.js"></script>
    <script src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
    <script src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
    <!-- 实例化编辑器 -->
    <script type="text/javascript">
        var ue = UE.getEditor('clnfdetail');
    </script>

    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
    <script src="${basePath}/static/admin/js/whgmodule-venseatmaps.js"></script>
    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <!-- 图片上传相关-END -->
    <script type="text/javascript" src="${basePath}/static/common/js/area.js"></script>
    <script type="text/javascript" src="${basePath}/static/admin/js/mass-resource.js"></script>

</head>
<body class="body_add">
<input id="supplement" type="hidden" value="0">
<form id="whgff" method="post" class="whgff">
    <c:if test="${empty offH2}">
    <h2>栏目内容管理</h2>
    </c:if>
    <div class="whgff-row" refField="clnftltle">
        <div class="whgff-row-label"><i>*</i>标题：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="clnftltle" value="${info.clnftltle}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,60]'], prompt:'请输入标题'">
        </div>
    </div>

    <div class="whgff-row" refField="clnfsource">
        <div class="whgff-row-label"><i>*</i>来源：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="clnfsource" value="${info.clnfsource}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,60]'], prompt:'请输入来源'">
        </div>
    </div>

    <div class="whgff-row" refField="clnfauthor">
        <div class="whgff-row-label"><i>*</i>作者：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="clnfauthor" value="${info.clnfauthor}" style="width:600px; height:32px"
                   data-options="required:true,validType:['length[1,60]'], prompt:'请输入作者'">
        </div>
    </div>

    <div class="whgff-row" refField="clnvenueid">
        <div class="whgff-row-label"><i>*</i>所属单位：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:245px; height:32px" name="clnvenueid" id="clnvenueid" data-options="required:true" />
            <input class="easyui-combobox" style="width:245px; height:32px" name="deptid" id="deptid" data-options="required:true" />
        </div>
    </div>

    <div class="whgff-row" refField="clnfkey">
        <div class="whgff-row-label">关键字：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" id="clnfkey" name="clnfkey" style="width:500px; height:32px" validType="notQuotes"
                   data-options="multiple:true, editable:true, prompt:'请填写关键字'
                   ,onChange: function (val, oldval) {
                        if (val.length>1 && val[0]==''){
                            val.shift();
                            $(this).combobox('setValues', val);
                        }
                    }"/>
            <span>（如需手动输入，请用英文逗号隔开！）</span>
        </div>
        <%--
        <div class="whgff-row-input">
                  <div class="checkbox checkbox-primary" id="clnfkey" name="clnfkey" style=""></div>
                </div>--%>
    </div>

    <div class="whgff-row" refField="clnfbigpic">
        <div class="whgff-row-label">图片：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture1" name="clnfbigpic" value="${info.clnfbigpic}">
            <div class="whgff-row-input-imgview" id="previewImg1"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 1920x335 省馆介绍：1200x455，大小为2MB以内</i>
            </div>
        </div>
    </div>
    <%-- <div class="whgff-row"  refField="toproject">
         <div class="whgff-row-label"><i>*</i>所属系统：</div>
         <div class="whgff-row-input" data-check="true" target="toproject" err-msg="请至少选择一个系统">
             <div class="checkbox checkbox-primary whg-js-data" name="toproject" value="${info.toproject}"   js-data="WhgComm.getFromProject">
             </div>
         </div>
     </div>--%>

    <div class="whgff-row"  refField="area">
        <div class="whgff-row-label">
            <label style="color: red">*</label>区域：
        </div>
        <input class="easyui-combobox" style="width:150px; height:32px" id="province" name="province" data-options="required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name'<%--, data:__PROVINCE, onChange:myChangeProvince--%>"/>
        <input class="easyui-combobox" style="width:150px; height:32px" id="__CITY_ELE" name="city" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name'<%--, data:[], onChange:myChangeCity--%>"/>
        <input class="easyui-combobox" style="width:179px; height:32px" id="__AREA_ELE" name="area" data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
    </div>


    <div class="whgff-row" refField="clnfcrttime">
        <div class="whgff-row-label"><label style="color: red">*</label>创立时间：</div>
        <div class="whgff-row-input">
            <input type="text" class="easyui-datetimebox" style="width:300px;height: 32px;" name="clnfcrttime_str"
                   value="<fmt:formatDate value='${info.clnfcrttime}' pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>" data-options="required:true,editable:false,prompt:'请选择'"/>
        </div>
    </div>

    <div class="whgff-row" refField="clnfidx" style="display:none">
        <div class="whgff-row-label">排序：</div>
        <div class="whgff-row-input">
            <input class="easyui-numberspinner" name="clnfidx" value="${info.clnfidx}" style="width:300px;height:32px"
                   data-options="increment:1, min:1, editable:true"/>
        </div>
    </div>

    <div class="whgff-row" refField="clnfintroduce">
        <div class="whgff-row-label">简介：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="clnfintroduce" value="${info.clnfintroduce}" multiline="true" style="width:600px;height: 100px;"
                   data-options="required:false,validType:['length[1,400]']">
        </div>
    </div>

    <div class="whgff-row" refField="clnfdetail">
        <div class="whgff-row-label">详细介绍：</div>
        <div class="whgff-row-input">
            <a class="easyui-linkbutton" id="selectlinkbutton" >插入群文图片</a>
            <script id="clnfdetail" name="clnfdetail" type="text/plain" style="width:600px; height:200px;"></script>
            <textarea id="value_catalog" style="display: none;">${info.clnfdetail}</textarea>
                </div>
                </div>

                <%--群文图片的地址--%>
                <input type="hidden"  class="vm-refvalue" vm="resurl" name="enturl" value="${infos.enturl}">
                </form>

                <div class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
                <a class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok">提 交</a>
            <a class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo">返 回</a>
            </div>

            <script>

            //省市区
            /*var province = WhgComm.getProvince()?WhgComm.getProvince():'广东省';
            var city = WhgComm.getCity()?WhgComm.getCity():"";
            function myChangeProvince(newVal, oldVal) {
                changeProvince(newVal, oldVal, function(){
                    if(typeof(window.__init_city) == 'undefined'){
                        $('#__CITY_ELE').combobox('setValue', '${info.city}');
                window.__init_city = true;
            }
        });
    }

    function myChangeCity(newVal, oldVal) {
        changeCity(newVal, oldVal, function(){
            if(typeof(window.__init_area) == 'undefined'){
                $('#__AREA_ELE').combobox('setValue', '${info.area}');
                window.__init_area = true;
            }
        });
    }*/  //省市区------END
            //处理UE
            var ueConfig = {
                scaleEnabled: false,
                autoFloatEnabled: false,
                elementPathEnabled:false,
                readonly: '${targetShow}'? true: false
            };

            $(function(){
                //$('#province').combobox('setValue', province);
                WhgComm.initPMS({
                    basePath:'${basePath}',
                    deptEid:'deptid', deptValue:'${info.deptid}',
                    cultEid:'clnvenueid', cultValue:'${info.clnvenueid}',
                    ywiKeyEid:'clnfkey', ywiKeyValue:'${info.clnfkey}', ywiKeyClass:25
                    ,provinceEid:'province', provinceValue:'${info.province}',
                    cityEid:'__CITY_ELE', cityValue:'${info.city}',
                    areaEid:'__AREA_ELE', areaValue:'${info.area}'
                });
                /* $("#clnfkey").combobox("setValue","${info.clnfkey}");*/
                var id = '${id}';
                var targetShow = '${targetShow}';
                var frm = $("#whgff");
                var buts = $("div.whgff-but");
                var clnftype = '${not empty clnftype? clnftype: info.clnftype}';

                //返回
                buts.find("a.whgff-but-clear").linkbutton({
                    onClick: function(){
                        if (!targetShow){
                            window.parent.$('#whgdg').datagrid('reload');
                        }
                        WhgComm.editDialogClose();
                    }
                });

                //选择群文资源
                $('#selectlinkbutton').click(function () {
                    var enttype = 1;
                    var restype = 'img';
                    WhgMassResource.openResource({
                        restype:restype,
                        submitFn:function (row) {
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

                })

                function showResourc(){
                    var enturl = $("#whgff [name='enturl']").val();
                    var imgurl = enturl;
                    if (!/^http/.test(imgurl)){
                        imgurl = WhgComm.getImgServerAddr()+imgurl;
                    }
                    var imgHtml='<img src="'+imgurl+'" _src="'+imgurl+'">';
                    ue.ready(function() {
                        //获取html内容
                        var html = ue.getContent();
                        if(html!="" && html!=null){
                            //拼接
                            imgHtml=html+imgHtml+"</p>";
                        }
                        //设置编辑器的内容
                        ue.setContent(imgHtml);
                        //获取纯文本内容
                        //var txt = ue.getContentTxt();
                    });

                }

                lockEditItem(frm, clnftype);

                //查看处理
                if (targetShow){
                    //取消表单验证
                    frm.form("disableValidation");
                    buts.find("a.whgff-but-submit").hide();
                    lockShowItem(frm);
                    return;
                }

                //表单提交处理
                var url = id ? "${basePath}/admin/information/edit" : "${basePath}/admin/information/add";
                frm.form({
                    url: url,
                    novalidate: true,
                    onSubmit: function (param) {
                        if (id){
                            param.clnfid = id;
                        }else{
                            param.clnfstata = 0;
                            param.clnfghp = 0;
                            param.clnfidx = 1;
                            param.clnftype = clnftype;	//当前的栏目ID
                            //param.clnvenueid = venueid;	//所属文化馆标识
                        }
                        if($("input[name='clnfbigpic']").val() != "" || $("input[name='clnfbigpic']").val() != null){
                            param.clnfpic = $("input[name='clnfbigpic']").val();
                        };


                        $(this).form("enableValidation");
                        var isValid = $(this).form('validate');

                        if (isValid)
                            isValid = supplement();

                        if (!isValid){
                            $.messager.progress('close');
                            oneSubmit();
                        }

                        return isValid;
                    },
                    success: function (data) {
                        data = $.parseJSON(data);
                        $.messager.progress('close');
                        oneSubmit();

                        if (!data.success || data.success != "1"){
                            $.messager.alert("错误", data.msg||'操作失败', 'error');
                            return;
                        }
                        window.parent.$('#whgdg').datagrid('reload');
                        WhgComm.editDialogClose();
                    }
                });

                //处理表单提交
                function submitForm(){
                    $.messager.progress();
                    frm.submit();
                }

                //处理重复点击提交
                function oneSubmit(){
                    buts.find("a.whgff-but-submit").off('click').one('click', submitForm);
                }
                oneSubmit();

                function supplement() {
                    //验证项目
                    /*var toproject = $("input[name='toproject']:checked").val();
                     if(null == toproject || "" == toproject){
                         $.messager.alert("错误", '请选择所属系统', 'error');
                         return false;
                     }*/
                    return true;
                }
            });



            //查看锁项
            function lockShowItem(frm){
                $('.easyui-textbox').textbox('readonly');
                $('.easyui-combobox').combobox('readonly');
                $('.easyui-datetimebox').combobox('readonly');
                $('.easyui-numberspinner').combobox('readonly');

                frm.find("input[type='checkbox']").on('click', function(){return false});
                frm.find("input[type='radio']").attr('disabled', true);
                $("#imgUploadBtn1").hide();
            }

            function lockEditItem(frm, clnftype){
                var idsKey = 'ids_default';
                for(var k in cfgColids){
                    var arridx = $.inArray(clnftype, cfgColids[k]);
                    if (arridx != -1){
                        idsKey = k;
                        break;
                    }
                }

                var fields = cfgRefFields[idsKey];
                frm.find(".whgff-row").each(function(){
                    var refField = $(this).attr("refField");
                    var arridx = $.inArray(refField, fields);
                    if (arridx == -1){
                        $(this).remove();
                        return true;
                    }

                    if (refField == 'clnfdetail'){
                        var ue_description = UE.getEditor('clnfdetail', ueConfig);
                        ue_description.ready(function(){  ue_description.setContent($("#value_catalog").val()); });
                    }
                    if (refField == 'clnfbigpic'){
                        var whgImg = WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1', needCut:false,isSingleSy:true});
                    }
                });


            }

            var cfgColids = {
                //只要 标题、详情图、内容 相关的栏目集
                ids_0 : ['2016111900000006'],
                ids_1 : ['2016111900000010'],
                ids_2 : ['2016111900000011'],
                ids_3 : ['2016111900000007','2016111900000008'],
                ids_4 : ['2016111900000012','2016111900000018',
                    '2016111900000020','2016111900000021',
                    '2016112200000004','2016112200000005','2016112200000006','2016112200000007',
                    '2016112200000008','2016112200000009','2016112200000010','2016112200000011','2016112200000012',
                    '2016112400000001','2016112400000002'],
                ids_5 : ['2016111900000014','2016111900000015', '2016111900000016','2016111900000017'],
                ids_6 : ['2017060200000003']
            };
            var cfgRefFields = {
                ids_0 : ["clnftltle", "clnfbigpic", "clnfdetail","clnfcrttime","clnvenueid","toproject","area"],
                ids_1 : ["clnftltle", "clnfintroduce","clnfidx","clnfcrttime","clnvenueid","toproject","area"],
                ids_2 : ["clnftltle", "clnfintroduce","clnfidx","clnfcrttime","clnvenueid","toproject","area"],
                ids_3 : ["clnftltle", "clnfintroduce","clnfauthor","clnfbigpic","clnfsource","toproject","area","clnfdetail","clnfkey","clnfcrttime","clnvenueid"],
                ids_4 : ["clnftltle", "clnfintroduce","clnfauthor","clnfsource","clnfdetail","toproject","area","clnfkey","clnfcrttime","clnvenueid"],
                ids_5 : ["clnftltle", "clnfintroduce","clnfauthor","clnfsource","clnfdetail","toproject","area","clnfkey","clnfcrttime","clnvenueid"],
                ids_6 : ["clnftltle", "clnfdetail","clnvenueid","toproject","area"],
                ids_default : [
                    "clnftltle", //栏目内容标题
                    "clnfsource", //栏目内容来源
                    "clnfauthor", //作者
                    //"colinfopic_up", //列表图片
                    "clnfbigpic", //详情图片
                    "clnvenueid",
                    "clnfintroduce",  //栏目内容简介
                    "clnfdetail", //栏目内容详细介绍
                    "clnfkey", // 关键字
                    "toproject",//所属项目
                    "area",//区域
                    "clnfidx", //排序
                    "clnfcrttime"//创立时间
                ]
            };

            </script>

</body>
</html>
