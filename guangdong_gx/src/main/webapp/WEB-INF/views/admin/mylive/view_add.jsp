<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
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
    <!--YI@文件上传相关-->
    <script type="text/javascript" src="${basePath}/static/plupload/upload-file.js"></script>
    <!-- 根据地址取坐标 -->
    <script type="text/javascript" src="${basePath}/static/common/js/whg.maps.js"></script>
    <script type="text/javascript" src="${basePath}/static/common/js/area.js"></script>
    <!--YI@ 播放视频相关插件-->
    <link rel="stylesheet" href="http://g.alicdn.com/de/prismplayer/2.7.4/skins/default/aliplayer-min.css" />
    <script charset="utf-8" type="text/javascript" src="http://g.alicdn.com/de/prismplayer/2.7.4/aliplayer-min.js"></script>
    <c:choose>
        <c:when test="${reftype == '1'}"><c:set var="reftitle" value="活动"></c:set> </c:when>
        <c:when test="${reftype == '2'}"><c:set var="reftitle" value="培训"></c:set> </c:when>
        <c:otherwise><c:set var="reftitle" value="在线课程"></c:set></c:otherwise>
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
                        isValid = times > 1000*60*10 && times < 21600000;//[直播时间：10分钟到6个小时之间]
                        //必须大于当前时间
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
    <h2>直播管理-添加${reftitle}直播</h2>

    <input type="hidden" name="appname" value="gdswhg"/>
    <input type="hidden" id="limitStartTime" name="limitStartTime" value=""/>
    <input type="hidden" id="limitEndTime" name="limitEndTime" value=""/>
    <input type="hidden"  name="addtype" value="${addType}"/>


    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>关联${reftitle}：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="cultid" name="cultid" value="" />
            <input type="hidden" id="deptid" name="deptid" value="" />
            <input type="hidden" id="reftype" name="reftype" value="${reftype}" />
            <input type="hidden" id="refid" name="refid" value="" />
            <input type="hidden" id="courseid" name="courseid" value="" />
            <input class="easyui-textbox" style="width: 350px; height: 32px" id="refname" name="refname" value="" data-options="required:true, readonly:true" />
            <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-edit" onclick="chioceEnt('${reftype}')">选择${reftitle}</a>
            <span id="coursenameParent"><input class="easyui-textbox" style="width: 200px; height: 32px" id="coursename" name="coursename" value="" data-options="readonly:true" /></span>
            <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-edit" id="chioceCourseBtn" onclick="chioceCourse()">选择课时</a>
        </div>
    </div>
    <div class="whgff-row">
        <div class="whgff-row-label"><i>*</i>直播标题：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" id="name" name="name" style="width:500px; height:32px" data-options="required:true, prompt:'直播标题，最多30个字符', validType:'length[2,30]'">
        </div>
    </div>

            <div class="whgff-row">
                <div class="whgff-row-label"><i>*</i>直播时间：</div>
                <div class="whgff-row-input">
                    <input class="easyui-datetimebox" id="starttime" name="starttime" style="width:200px; height:32px" data-options="required:true, prompt:'直播开始时间',validType:'validLiveTime[\'starttime\',\'endtime\']'">至
                    <input class="easyui-datetimebox" id="endtime" name="endtime" style="width:200px; height:32px" data-options="required:true, prompt:'直播结束时间',validType:'validLiveTime[\'starttime\',\'endtime\']'">
                </div>
            </div>

    <c:choose>
        <c:when test="${addType == '1'}">
            <div class="whgff-row">
                <div class="whgff-row-label"><i>*</i>推流线路：</div>
                <div class="whgff-row-input">
                        <%--<input class="easyui-textbox" id="streamname" name="streamname" style="width: 500px; height: 32px" data-options="required:true,validType:['length[1,60]']" value="${whgLive.streamname}"/>--%>
                    <input class="easyui-combobox" id="streamname" name="streamname" value="live1" style="width: 500px; height: 32px" data-options="required:true, editable:false, valueField:'id', textField:'text', data:streamNameData(), onChange:changeLive"/>
                </div>
            </div>
    <div class="whgff-row">
        <div class="whgff-row-label">推流地址：</div>
        <div class="whgff-row-input">
            <input class="easyui-textbox" style="width: 500px; height: 32px" id="flowaddr" name="flowaddr" value="rtmp://video-center.alivecdn.com/gdswhg/live1?vhost=live.gdsqyg.com" data-options="readonly:true" />
            <%--<input class="easyui-combobox" name="flowaddr" id="flowaddr" panelHeight="auto" limitToList="true" style="width:500px; height:32px"
                                                               data-options="required:false, editable:false,multiple:false, mode:'remote', valueField:'id', textField:'name'"/>--%>
        </div>
    </div>
        </c:when>
    </c:choose>
    <%--<c:choose>后台播放小窗口预留位置
        <c:when test="${addType == '2'or addType=='3'}">
    <div class="whgff-row" id="video_location" hidden="hidden">
        <div class="whgff-row-input" style="margin-left: 200px">
            <div  class="prism-player" id="J_prismPlayer"></div>
        </div>
    </div>
         </c:when>
    </c:choose>--%>
    <c:choose>
        <c:when test="${addType == '1'}">
            <div class="whgff-row">
                <div class="whgff-row-label"><i>*</i>播放地址：</div>
                <div class="whgff-row-input">
                    <input class="easyui-textbox" style="width: 500px; height: 32px"  id="playaddr" name="playaddr" value="http://live.gdsqyg.com/gdswhg/live1.m3u8" data-options="readonly:true"  /><!--onChange='valueChange(this)'  style="width: 500px; height: 32px;border-radius: 3px;solid-color: #fffcf8;border: 1px solid gainsboro"-->
                        <%--<input class="easyui-combobox" name="playaddr" id="playaddr" panelHeight="auto" limitToList="true" style="width:500px; height:32px"
                               data-options="required:false, editable:false,multiple:false, mode:'remote', valueField:'id', textField:'name'"/>--%>
                   <%-- <a id="video_message" style="color: red;font-size: 12px" hidden="hidden">您输入的地址有误或不在直播时间范围内</a>--%>
                </div>
            </div>
        </c:when>
        <c:when test="${addType=='2'}">
            <div class="whgff-row">
                <div class="whgff-row-label"><i>*</i>播放地址：</div>
                <div class="whgff-row-input">
                    <input class="easyui-textbox" style="width: 500px; height: 32px"  id="playaddr" name="playaddr"  data-options="readonly:false"  /><!--onChange='valueChange(this)'  style="width: 500px; height: 32px;border-radius: 3px;solid-color: #fffcf8;border: 1px solid gainsboro"-->
                        <%--<input class="easyui-combobox" name="playaddr" id="playaddr" panelHeight="auto" limitToList="true" style="width:500px; height:32px"
                               data-options="required:false, editable:false,multiple:false, mode:'remote', valueField:'id', textField:'name'"/>--%>
                        <%-- <a id="video_message" style="color: red;font-size: 12px" hidden="hidden">您输入的地址有误或不在直播时间范围内</a>--%>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="whgff-row">
                <div class="whgff-row-label"><i>*</i>链接地址：</div>
                <div class="whgff-row-input">
                    <input  class="easyui-textbox" style="width: 500px; height: 32px" id="playaddr" name="playaddr" />
                        <%--<input class="easyui-combobox" name="playaddr" id="playaddr" panelHeight="auto" limitToList="true" style="width:500px; height:32px"
                               data-options="required:false, editable:false,multiple:false, mode:'remote', valueField:'id', textField:'name'"/>--%>
                    <%--<a id="video_link" class="easyui-linkbutton">链接跳转</a>--%>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
    <div class="whgff-row">
        <div class="whgff-row-label">上传预览视频：</div>
        <div class="whgff-row-input">
            <input type="hidden" id="whg_file_upload" name="advancevideoaddr">
            <input type="hidden" id="whg_file_upload_name" name="advancevideoname">
            <div class="whgff-row-input-fileview" id="whg_file_pload_view"></div>
            <div class="whgff-row-input-filefile">
                <i><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" id="fileUploadBtn2">选择视频</a></i>
            </div>
        </div>
    </div>
</form>

<div id="whgwin-add-btn" class="whgff-but" style="width: 400px; margin:20px 0px 50px 350px">
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-submit" iconCls="icon-ok" id="whgwin-add-btn-save">提 交</a>
    <a href="javascript:void(0)" class="easyui-linkbutton whgff-but-clear" iconCls="icon-undo" onclick="WhgComm.editDialogClose()">返 回</a>
</div>

<!-- script -->
<script type="text/javascript">
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
            url : getFullUrl('/admin/mylive/add'),
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
       /* $("#playaddr").textbox({
            onChange:function () {
                var _value = $("#playaddr").val();
                var player = new Aliplayer({
                    id: 'J_prismPlayer',
                    width: '350px',
                    height:'180px',
                    autoplay: true,
                    // isLive:true,
                    //支持播放地址播放,此播放优先级最高
                    source :_value
                },function(player){

                });
                player.on('error',function (e) {
                    $("#video_location").attr("hidden",true)
                    $("#video_message").attr("hidden",false)
                });
                player.on('playing',function () {
                    $("#video_location").attr("hidden",false);
                    $("#video_message").attr("hidden",true)
                })
            }

        })*/


       /* if('${addType}'=='2'||'${addType}'==3){
            $('#refname').textbox({
                required:false,
                readonly:false
            })

            $('#starttime').datetimebox({
                required:false
            })
            $('#endtime').datetimebox({
                required:false
            })
        }
*/

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
        WhgUploadFile.init({basePath: '${basePath}', uploadBtnId: 'fileUploadBtn2', hiddenFieldId: 'whg_file_upload',previewFileId:'whg_file_pload_view'});
        //初始表单
        initForm();
    });

</script>
<script>
    //YI@ 当播放地址值发生变化时，检测地址值的正确性，正确播放，不正确输出提示信息
    /*function valueChange(e) {
        var player = new Aliplayer({
            id: 'J_prismPlayer',
            width: '350px',
            height:'180px',
            autoplay: true,
            // isLive:true,
            //支持播放地址播放,此播放优先级最高
            source :e.value
        },function(player){

        });
        player.on('error',function (e) {
            $("#video_location").attr("hidden",true)
            $("#video_message").attr("hidden",false)
        });
        player.on('playing',function () {
            $("#video_location").attr("hidden",false);
            $("#video_message").attr("hidden",true)
        })

    }*/
</script>
<!-- script END -->
</body>
</html>