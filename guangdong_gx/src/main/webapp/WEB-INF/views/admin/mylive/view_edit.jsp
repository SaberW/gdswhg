<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>直播管理-添加直播</title>
    <%@include file="/WEB-INF/views/comm/admin/header.jsp" %>
    <!-- 编辑表单 -->
    <link rel="stylesheet" href="${basePath}/static/Font-Awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/bootstrap.css"/>
    <link rel="stylesheet" href="${basePath}/static/admin/css/build.css"/>
    <script src="${basePath}/static/admin/js/whgtoolmodule.js"></script>
    <!-- 图片上传相关 -->
    <script type="text/javascript" src="${basePath}/static/plupload/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
    <script type="text/javascript" src="${basePath}/static/plupload/upload-img.js"></script>
    <!-- 根据地址取坐标 -->
    <script type="text/javascript" src="${basePath}/static/common/js/whg.maps.js"></script>
    <script type="text/javascript" src="${basePath}/static/common/js/area.js"></script>
    <script type="text/javascript" src="${basePath }/static/admin/js/mass-resource.js"></script>
    <c:choose>
        <c:when test="${reftype == '1'}"><c:set var="reftitle" value="活动"></c:set> </c:when>
        <c:when test="${reftype == '2'}"><c:set var="reftitle" value="培训"></c:set> </c:when>
        <c:otherwise><c:set var="reftitle" value="在线课程"></c:set></c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${param.onlyshow == '1'}"><c:set var="viewtitle" value="查看"></c:set> </c:when>
        <c:otherwise><c:set var="viewtitle" value="编辑"></c:set></c:otherwise>
    </c:choose>
    <style>
        #WhgDialog4Edit{
            padding: 2px;
        }
    </style>
    <script type="text/javascript">
        $.extend($.fn.validatebox.defaults.rules, {
            validLiveTime: {
                validator: function(value, param){
                    //直播时间必须是同一天，不能超过6个小时
                    var isValid = false;
                    var sdVal1 = $('#'+param[0]).datetimebox('getValue');//startTime
                    var sdVal2 = $('#'+param[1]).datetimebox('getValue');//startTime
                    if(sdVal1.substring(0,10) == sdVal2.substring(0,10)){
                        var d1 = WhgComm.parseDateTime(sdVal1);//$.fn.datebox.defaults.parser(sdVal);
                        var d2 = WhgComm.parseDateTime(sdVal2);//$.fn.datebox.defaults.parser(value);
                        var d3 = new Date();
                        var times = d2.getTime()-d1.getTime();
                        isValid = times > 1000*60*10 && times < 21600000;
                        //必须大于当前时间2个小时
                        if(isValid){
                            isValid = (d1.getTime()-d3.getTime()) > 0;
                            if(!isValid)$.fn.validatebox.defaults.rules.validLiveTime.message = '直播开始时间必须大于当前时间';
                        }
                        if(isValid){
                            var lst = $('#limitStartTime').val();
                            var let = $('#limitEndTime').val();
                            if(lst != '' && let != ''){//开始结束时间必须限制指定的时间之内
                                var dlst = WhgComm.parseDateTime(lst);
                                var dlet = WhgComm.parseDateTime(let);
                                isValid = d1.getTime() >= dlst.getTime() && d2.getTime() <= dlet.getTime();
                                if(!isValid)$.fn.validatebox.defaults.rules.validLiveTime.message = '直播时间必须限制在【'+lst+'】至【'+let+'】之内';
                            }
                        }
                    }
                    return isValid;
                },
                message: '直播时间必须在同一天，直播时间必须介于10分钟到6个小时之间.'
            }
        });
    </script>
</head>
<body>

<form id="whgff" class="whgff" method="post">
    <h2>直播管理-${viewtitle}${reftitle}直播</h2>

    <input type="hidden" name="appname" value="gdswhg"/>
    <input type="hidden" name="id" value="${live.id}"/>
    <input type="hidden" id="onlyshow" name="onlyshow" value="${param.onlyshow}"/>
    <input type="hidden" id="editVod" name="editVod" value="${param.editVod}"/>
    <input type="hidden" id="limitStartTime" name="limitStartTime" value="${limitStartTime}"/>
    <input type="hidden" id="limitEndTime" name="limitEndTime" value="${limitEndTime}"/>

    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>关联${reftitle}：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cultid" name="cultid" value="${live.cultid}" />
            <input type="hidden" id="deptid" name="deptid" value="${live.deptid}" />
            <input type="hidden" id="reftype" name="reftype" value="${reftype}" />
            <input type="hidden" id="refid" name="refid" value="${live.refid}" />
            <input type="hidden" id="courseid" name="courseid" value="${live.courseid}" />
            <input class="easyui-textbox" style="width: 350px; height: 32px" id="refname" name="refname" value="${refname}" data-options="required:true, readonly:true" />
            <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-edit" id="chioceEntBtn" onclick="chioceEnt('${reftype}')">选择${reftitle}</a>
            <span id="coursenameParent"><input class="easyui-textbox" style="width: 200px; height: 32px" id="coursename" name="coursename" value="${coursename}" data-options="readonly:true" /></span>
            <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-edit" id="chioceCourseBtn" onclick="chioceCourse()">选择课时</a>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>直播标题：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" id="name" name="name" value="${live.name}" style="width:500px; height:32px" data-options="required:true, prompt:'直播标题，最多30个字符', validType:'length[2,30]'">
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>直播时间：</div>
        <div class="whgff-row-input">
            <c:choose>
                <c:when test="${param.editVod == '1' or param.onlyshow == '1'}">
                    <fmt:formatDate value="${live.starttime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate> 至
                    <fmt:formatDate value="${live.endtime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
                </c:when>
                <c:otherwise>
                    <input class="easyui-datetimebox" id="starttime" name="starttime" value="" style="width:200px; height:32px" data-options="required:true, prompt:'直播开始时间',validType:'validLiveTime[\'starttime\',\'endtime\']'">至
                    <input class="easyui-datetimebox" id="endtime" name="endtime" value="" style="width:200px; height:32px" data-options="required:true, prompt:'直播结束时间',validType:'validLiveTime[\'starttime\',\'endtime\']'">
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>推流线路：</div>
        <div class="whgff-row-input">
            <%--<input class="easyui-textbox" id="streamname" name="streamname" style="width: 500px; height: 32px" data-options="required:true,validType:['length[1,60]']" value="${whgLive.streamname}"/>--%>
            <input class="easyui-combobox" id="streamname" name="streamname" value="${live.streamname}" style="width: 500px; height: 32px" data-options="required:true, editable:false, valueField:'id', textField:'text', data:streamNameData(), onChange:changeLive"/>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">推流地址：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" style="width: 500px; height: 32px" id="flowaddr" name="flowaddr" value="rtmp://video-center.alivecdn.com/gdswhg/${live.streamname}?vhost=live.gdsqyg.com" data-options="readonly:true" />
            <%--<input class="easyui-combobox" name="flowaddr" id="flowaddr" panelHeight="auto" limitToList="true" style="width:500px; height:32px"
                                                               data-options="required:false, editable:false,multiple:false, mode:'remote', valueField:'id', textField:'name'"/>--%>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label">播放地址：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" style="width: 500px; height: 32px" id="playaddr" name="playaddr" value="http://live.gdsqyg.com/gdswhg/${live.streamname}.m3u8" data-options="readonly:true" />
            <c:if test="${param.onlyshow == '1'}">
            <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" onclick="lookVod('http://live.gdsqyg.com/gdswhg/${live.streamname}.flv')">查看</a>
            </c:if>
        </div>
    </div>

    <c:if test="${param.onlyshow == '1'}">
    <div class="whgff-row">
        <div class="whgff-row-label">录制视频：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="liveaddr" name="liveaddr" value="${live.liveaddr}"/>
            <c:choose>
                <c:when test="${empty live.liveaddr}">未录制完成</c:when>
                <c:otherwise>
                    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" onclick="lookVod('${live.liveaddr}')">查看</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    </c:if>

    <c:if test="${param.onlyshow == '1'}">
    <div class="whgff-row">
        <div class="whgff-row-label">回看视频：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="enturl" name="enturl" value="${live.enturl}"/>
            <c:choose>
                <c:when test="${empty live.enturl}">
                    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" id="enturlBtn" data-options="disabled:true">未配置</a>
                </c:when>
                <c:otherwise>
                    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" id="enturlBtn" onclick="lookVod('${live.enturl}');">查看</a>
                </c:otherwise>
            </c:choose>
            <c:if test="${param.editVod == '1'}">
            <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-edit" onclick="choiceBackLive();">选择视频</a>
            </c:if>
        </div>
    </div>
    </c:if>
</form>

<div id="whgwin-add-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" style="display: none" iconCls="icon-ok" id="whgwin-add-btn-save">提 交</a>
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
</div>

<!-- script -->
<script type="text/javascript">
    /** 查看直播或者视频 **/
    function lookVod(url) {
        var url = '${basePath}/admin/mylive/view/vod?resurl='+url+'&respicture='+encodeURIComponent('')+'&t='+new Date().getTime();
        WhgComm.openDialog4size('查看', url, 680, 500, { hideSubmitBtn: true, cancelBtnText: '关  闭', closeFun: function () {

        } });
    }

    /** 选择视频资源保存为回看视频 */
    function choiceBackLive() {
        WhgMassResource.openResource({restype:'video', submitFn: function (data) {
            $('#enturl').val(data.resurl);
            $('#enturlBtn').linkbutton({disabled:false, text:'查看'}).on('click', function () {
                lookVod(data.resurl);
            });
        }});
    }

    /** 可选直播流 **/
    function streamNameData(){
        return [
            {"id":"live1", "text":"live1"},
            {"id":"live2", "text":"live2"},
            {"id":"live3", "text":"live3"},
            {"id":"live4", "text":"live4"},
            {"id":"live5", "text":"live5"},
            {"id":"live6", "text":"live6"},
            {"id":"live7", "text":"live7"},
            {"id":"live8", "text":"live8"},
            {"id":"live9", "text":"live9"},
            {"id":"live10", "text":"live10"},
        ];
    }

    /**修改流名称时处理*/
    function changeLive(newVal, oldVal){
        newVal = newVal || '{流名}';
        $('#flowaddr').textbox('setValue', 'rtmp://video-center.alivecdn.com/gdswhg/'+newVal+'?vhost=live.gdsqyg.com');
        $('#playaddr').textbox('setValue', 'http://live.gdsqyg.com/gdswhg/'+newVal+'.m3u8');
    }

    /** 选择一个活动|一个线下培训|一个在线课程的课时 */
    function chioceEnt(reftype) {
        if(reftype == '1'){//选择一个活动
            WhgComm.openDialog4size('选择活动', '${basePath}/admin/mylive/view/list_act', 800, 600, {
                submitBtnText: '确  认'
            });
        }else if(reftype == '2'){//选择一个线下培训
            WhgComm.openDialog4size('选择线下培训', '${basePath}/admin/mylive/view/list_tra', 800, 600, {
                submitBtnText: '确  认'
            });
        }else if(reftype == '3'){//选择一个在线课程的课时
            WhgComm.openDialog4size('选择在线课程', '${basePath}/admin/mylive/view/list_zxk', 800, 600, {
                submitBtnText: '确  认'
            });
        }
    }

    /** 选择在线课程的课时 */
    function chioceCourse(){
        if($('#reftype').val() == '3' && $('#refid').val() != ''){
            //弹出选择在线课程-课时的窗口
            WhgComm.openDialog4size('选择课时', '${basePath}/admin/mylive/view/list_course?traid='+$('#refid').val(), 800, 600, {
                submitBtnText: '确  认'
            });
        }else{
            $.messager.alert('警告', '请先选择一个在线课程再进行此操作！', 'warning');
        }
    }

    /** 初始表单 */
    function initForm(){
        $('#whgff').form({
            novalidate: true,
            url : getFullUrl('/admin/mylive/edit'),
            onSubmit : function(param) {
                var _valid = $(this).form('enableValidation').form('validate');
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
        //根据直播类型隐藏可选择关联对象
        if('${reftype}' == '1' || '${reftype}' == '2'){
            $('#chioceCourseBtn').hide();
            $('#coursenameParent').hide();
        }else{
            $('#name').textbox('readonly', true);
            $('#coursename').textbox({required:true});
            $('#starttime').datetimebox('readonly', true);//在线课程的课时直播只能用课时的时间,不能修改
            $('#endtime').datetimebox('readonly', true);
        }

        //设置直播时间
        $('#starttime').datetimebox('setValue', '<fmt:formatDate value="${live.starttime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>');
        $('#endtime').datetimebox('setValue', '<fmt:formatDate value="${live.endtime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>');

        //查看
        var onlyshow = $('#onlyshow').val();
        if(onlyshow == '1'){
            $('.easyui-textbox').textbox('readonly', true);
            $('.easyui-datetimebox').datetimebox('readonly', true);
            $('.easyui-combobox').combobox('readonly', true);
            $('#chioceEntBtn').hide();
            $('#chioceCourseBtn').hide();

        }else{
            $('#whgwin-add-btn-save').show();
        }
        if('${param.editVod}' == '1'){
            $('#whgwin-add-btn-save').show();
        }


        //初始表单
        initForm();
    });
</script>
<!-- script END -->
</body>
</html>