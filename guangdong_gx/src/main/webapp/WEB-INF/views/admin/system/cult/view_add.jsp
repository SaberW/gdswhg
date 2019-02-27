<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>添加文化馆</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>

    <!-- 编辑表单 -->
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>
    <!-- 编辑表单-END -->
    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <!-- 图片上传相关-END -->

    <!-- 根据地址取坐标 -->
    <script type="text/javascript" src="${basePath}/static/common/js/whg.maps.js"></script>
    <script type="text/javascript" src="${basePath}/static/common/js/area.js"></script>
    <style>
        .ul-list{
            margin: 0;
            padding: 0;
            list-style: none;
            font-size: 14px;
        }
        .ul-list>dl{
            margin-top: 15px;
            margin-bottom: 0;
        }
        .ul-list>dl>dd>label{
            font-weight: normal;
            width:120px;
        }
        .ul-list>dl:first-child{
            margin-top: 0;
        }
        .ul-list>dl>dd>label{
            margin-right: 15px;
        }
        .red{
            color:red;
        }
    </style>
</head>
<body>

<form id="whgff" class="whgff" method="post">
    <h2>添加文化馆</h2>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>文化馆名称：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="name" style="width:500px; height:32px" data-options="required:true, prompt:'子馆名称，最多30个字符', validType:'length[3,30]'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>文化馆站点标识：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="cultsite" style="width:500px; height:32px"
                   data-options="required:true, prompt:'站点标识，最多10个小写英文及数字字符组合', validType:['length[3,10]','english', 'xyz']">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>区　　域　：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:150px; height:32px;" id="province" name="province" data-options="required:true, prompt:'请选择省', limitToList:true, valueField:'name', textField:'name', data:__PROVINCE, onChange:myChangeProvince"/>
            <input class="easyui-combobox" style="width:150px; height:32px" name="city" id="__CITY_ELE" data-options="required:true, prompt:'请选择市', limitToList:true, valueField:'name', textField:'name', data:[], onChange:myChangeCity"/>
            <input class="easyui-combobox" style="width:179px; height:32px" name="area" id="__AREA_ELE" data-options="required:true, prompt:'请选择区', limitToList:true, valueField:'name', textField:'name', data:[]"/>
            <span style="color: #999;font-size: 12px;display: block;margin-top: 10px;font-style: normal;">温馨提示：此处是文化馆（系统建设方）所在的省市区，与省级馆，市级馆，区级馆没关系。</span>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>文化馆地址：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" id="address" name="address" style="width:500px; height:32px" data-options="prompt:'请输入文化馆地址', required:true, validType:['length[2,35]']">
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>文化馆坐标：</div>
        <div class="whgff-row-input">
            <input class="easyui-numberbox" id="longitude" name="longitude" style="width:150px; height:32px" data-options="required:true, precision:6, readonly:true, prompt:'经度'">
            <input class="easyui-numberbox" id="latitude" name="latitude" style="width:150px; height:32px" data-options="required:true, precision:6, readonly:true, prompt:'纬度'">
            <a class="easyui-linkbutton" id="getXYPointBtn" text="选择坐标"></a>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>文化馆级别：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="level" js-data="getLevelData"></div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>开通站点显示：</div>
        <div class="whgff-row-input">
            <div class="radio radio-primary whg-js-data" name="isshow" js-data='[{"id":"1", "text":"是"},{"id":"0", "text":"否"}]'></div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label">上级文化馆：</div>
        <div class="whgff-row-input">
            <input class="easyui-combobox" style="width:500px; height:32px" name="pid" id="pid" data-options="prompt:'请选择上级文化馆',editable:false" />
        </div>
    </div>

    <div class="whgff-row" style="display: none">
        <div class="whgff-row-label"><i>*</i>ＬＯＧＯ　：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture3" name="logopicture" >
            <div class="whgff-row-input-imgview" id="previewImg3" style="height: 70px;"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn3">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 166*35，大小为2MB以内</i>
            </div>
        </div>
    </div>
    <div class="whgff-row" style="display: none">
        <div class="whgff-row-label">水印图片　：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture4" name="sypicture" >
            <div class="whgff-row-input-imgview" id="previewImg4" style="height: 70px;"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn4">选择图片</a></i>
                <i>图片格式为png，建议图片尺寸 166*35，大小为2MB以内</i>
            </div>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>封面图片　：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_picture1" name="picture" >
            <div class="whgff-row-input-imgview" id="previewImg1" style="height: 200px;"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgUploadBtn1">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>联系人　　：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="contact" style="width:500px; height:32px" data-options="prompt:'请输入联系人姓名',required:true,validType:['length[2,20]']"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>联系方式　：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="contactnum" style="width:500px; height:32px" data-options="prompt:'请输入联系人手机', required:true, validType:'isPhone'"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>固定电话　：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="telephone" style="width:500px; height:32px" data-options="prompt:'请输入固定电话', required:true, validType:'isTel'"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>身份证号码：</div>
        <div class="whgff-row-input"><input class="easyui-textbox" name="idcard" style="width:500px; height:32px" data-options="prompt:'请输入身份证号码', required:true, validType:'isIdcard'"></div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>身份证正面：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_idcardface" name="idcardface" >
            <div class="whgff-row-input-imgview" id="idcardface" style="height: 200px;"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgidcardface">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>身份证反面：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cult_idcardback" name="idcardback" >
            <div class="whgff-row-input-imgview" id="idcardback" style="height: 200px;"></div>
            <div class="whgff-row-input-imgfile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="imgidcardback">选择图片</a></i>
                <i>图片格式为jpg、png、gif，建议图片尺寸 750*500，大小为2MB以内</i>
            </div>
        </div>
    </div>
    <%--<div class="whgff-row">
        <div class="whgff-row-label">文化馆站点：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="siteurl" style="width:500px; height:32px" data-options="prompt:'文化馆站点地址，如:http://yoursite.com/', validType:['url', 'length[2,512]']">
        </div>
    </div>--%>

    <div class="whgff-row" >
        <div class="whgff-row-label"><i>*</i>网站栏目　：</div>
        <div class="whgff-row-input">
            <div class="ul-list"></div>
        </div>
    </div>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>文化馆简介：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" name="introduction" style="width:600px; height:250px" data-options="required:true, multiline:true, validType:['length[1,500]', 'isText']">
        </div>
    </div>
</form>

<div id="whgwin-add-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok" id="whgwin-add-btn-save">提 交</a>
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
</div>

<!-- script -->
<script type="text/javascript">
    /** 文化馆级别数据 */
    function getLevelData(){
        return WhgSysData.getStateData("EnumCultLevel");
    }

    /** 必须选择至少一个前端菜单 */
    function supplement() {
        var frontmenu = $("input[name='frontmenu']:checked").val();
        if(null == frontmenu || "" == frontmenu){
            $.messager.alert("错误", '请至少选择一个网站栏目', 'error');
            return false;
        }
        return true;
    }

    /** 切换省时赋值城市的值 */
    function myChangeProvince(newVal, oldVal){
        changeProvince(newVal, oldVal, function(){
            if(typeof(window.__init_city) == 'undefined'){
                $('#__CITY_ELE').combobox('setValue', '${param.city}');
                window.__init_city = true;
            }
            setParentCultData('', $('input[name="level"]:checked').val());
        });
    }

    /** 切换城市时赋区域的值 */
    function myChangeCity(newVal, oldVal){
        changeCity(newVal, oldVal, function(){
            if(typeof(window.__init_area) == 'undefined'){
                $('#__AREA_ELE').combobox('setValue', '${param.area}');
                window.__init_area = true;
            }
            setParentCultData(newVal, $('input[name="level"]:checked').val());
        });
    }

    /** 根据省市和级别确认上级文化馆 */
    function setParentCultData(city, level){
        var param = level == "1" ? "pid=0" : (level == "2" ? "level=1" : "level=2&city="+encodeURIComponent(city));
        if(level == '3' && city == ''){
            $('#pid').combobox('loadData', []);
        }else{
            param += '&state=6&delstate=0';
            $('#pid').combobox({valueField:'id',textField:'name',url:'${basePath}/admin/system/cult/srchList?'+param});
        }
    }

    /** 初始时设置级别，省市区 */
    function initLevelAndArea(province, city, area, level){
        //级别确定上级文化馆
        $('input[name="level"]').click(function () {
            setParentCultData($('#__CITY_ELE').combobox('getValue'), $(this).val());
        });
        //设置区域
        $('#province').combobox('setValue', province);

        //根据参数设置级别
        if(level){
            $('input[name="level"][value="'+level+'"]').prop("checked", true);
            $('input[name="level"][value="'+level+'"]').click();
        }else if(area != ''){
            $('input[name="level"][value="3"]').prop("checked", true);
            $('input[name="level"][value="3"]').click();
        }else if(city != ''){
            $('input[name="level"][value="2"]').prop("checked", true);
            $('input[name="level"][value="2"]').click();
        }else{
            $('input[name="level"][value="1"]').prop("checked", true);
            $('input[name="level"][value="1"]').click();
        }
    }

    /** 初始前端菜单 */
    function initFrontMenu(initValue){
        var initValueArr = typeof(initValue) != 'undefined' ? initValue.split(",") : [];
        function isChecked(val){
            var isChecked = false;
            for(var i=0; i<initValueArr.length; i++){
                isChecked = initValueArr[i] == val
                if(isChecked) break;
            }
            return isChecked;
        }
        function setRed(input){
            var curtChecked = input.is(':checked') ? 'checked' : '';
            if(curtChecked == input.attr('defaultCheck')){
                input.parent('label').removeClass("red")
            }else{
                input.parent('label').addClass("red");
            }
        }
        var menus = WhgComm.getFrontMenu();
        if($.isArray(menus)){
            var menuObj = {};//{"分类":[]}
            for(var i=0; i<menus.length; i++){
                var menuName = menus[i].text;
                var menuType = menuName.split("-")[0];
                if(typeof(menuObj[menuType]) == 'undefined'){
                    menuObj[menuType] = [];
                }
                menuObj[menuType].push(menus[i]);
            }
            var html = '';
            for(var menuType in menuObj){
                html += '<dl>';
                html += '<dt><label><input type="checkbox" name="frontmenuAll" value="all" />全选-'+menuType+'</label></dt><dd>';
                for(var i=0; i<menuObj[menuType].length; i++){
                    var checkedStr = isChecked(menuObj[menuType][i].id)?"checked":"";
                    html += '<label><input type="checkbox" name="frontmenu" value="'+menuObj[menuType][i].id+'" defaultCheck="'+checkedStr+'" '+checkedStr+' />'+menuObj[menuType][i].text+'</label>';
                }
                html += '</dd></dl>';
            }
            $('.ul-list').html(html);
            $('.ul-list input[name="frontmenu"]').change(function(){
                setRed($(this));
            });
            $('input[name="frontmenuAll"]').change(function () {
                var _checked = $(this).is(":checked");
                $(this).parents("dl").find('input[name="frontmenu"]').each(function () {
                    $(this).prop("checked", _checked);
                    setRed($(this));
                });
            });
        }
    }

    /** 初始表单 */
    function initForm(){
        $('#whgff').form({
            novalidate: true,
            url : getFullUrl('/admin/system/cult/add'),
            onSubmit : function(param) {
                var _valid = $(this).form('enableValidation').form('validate');
                //图片必填
                if (_valid) {//应产品要求  logo及水印 都默认为 文化馆联盟
                    /*  if($('#cult_picture3').val() == ""){
                        _valid = false;
                        $.messager.alert('提示', '请选择文化馆的LOGO图片');
                     }else*/
                    if ($('#cult_picture1').val() == "") {
                        _valid = false;
                        $.messager.alert('提示', '请选择文化馆的封面图片');
                    }else if($('#cult_idcardface').val() == ""){
                         _valid = false;
                         $.messager.alert('提示', '请选择身份证正面图片');
                    }else if($('#cult_idcardback').val() == ""){
                        _valid = false;
                        $.messager.alert('提示', '请选择身份证反面图片');
                    }
                }
                if (_valid) {
                    _valid = supplement();
                }
                if(_valid){
                    $.messager.progress();
                }else{
                    //失败时再注册提交事件
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
                    $.messager.alert('提示', '操作失败: '+Json.errormsg+'！', 'error');
                    $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
                }
            }
        });
        //注册提交事件
        $('#whgwin-add-btn-save').off('click').one('click', function () { $('#whgff').submit(); });
    }

    $(function () {
        //图片初始化
        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn1', hiddenFieldId: 'cult_picture1', previewImgId: 'previewImg1'});
        /* WhgUploadImg.init({basePath: '
        ${basePath}', uploadBtnId: 'imgUploadBtn3', hiddenFieldId: 'cult_picture3', previewImgId: 'previewImg3', needCut:true, cutWidth:166, cutHeight:35,isSingleSy:false});
        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgUploadBtn4', hiddenFieldId: 'cult_picture4', previewImgId: 'previewImg4', needCut:true, cutWidth:166, cutHeight:35,isSingleSy:false});
         */
        WhgUploadImg.init({
            basePath: '${basePath}',
            uploadBtnId: 'imgidcardface',
            hiddenFieldId: 'cult_idcardface',
            previewImgId: 'idcardface',
            needCut: false
        });
        WhgUploadImg.init({basePath: '${basePath}', uploadBtnId: 'imgidcardback', hiddenFieldId: 'cult_idcardback', previewImgId: 'idcardback', needCut:false});
        //根据地址取坐标
        WhgMap.init({
            basePath:'${basePath}', addrFieldId:'address', xpointFieldId:'longitude', ypointFieldId:'latitude', getPointBtnId:'getXYPointBtn'
        });

        //初始级别和区域
        initLevelAndArea('${param.province}', '${param.city}', '${param.area}');

        //初始前端菜单
        initFrontMenu();

        //初始表单
        initForm();
    });
</script>
<!-- script END -->
</body>
</html>