<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<% request.setAttribute("resourceid", request.getParameter("rsid")); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/comm/admin/header.jsp"%>

    <c:choose>
        <c:when test="${pageType eq 'listdel'}">
            <c:set var="pageTitle" value="回收站"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="pageTitle" value="众筹项目列表"></c:set>
        </c:otherwise>
    </c:choose>

    <title>${pageTitle}</title>

    <script src="${basePath}/static/admin/js/whgmodule-gridopts.js"></script>
</head>
<body>

<table id="whgdg" class="easyui-datagrid" title="${pageTitle}" style="display: none;"
       toolbar="#tb" pagination=true
       data-options="fit:true, striped:true, rownumbers:true, fitColumns:false, singleSelect:true, checkOnSelect:true, selectOnCheck:true">
    <thead>
    <tr>
        <th data-options="sortable:false, field:'title' ">众筹名称</th>
        <th data-options="sortable: true, field:'etype', formatter:function(v){
            if (v=='4') return '活动众筹'
            else if (v=='5') return '培训众筹'
            else return '其它众筹'
        } ">众筹类型</th>
        <th data-options="sortable: true, field:'timestart', formatter:WhgComm.FMTDateTime ">众筹开始时间</th>
        <th data-options="sortable: true, field:'timeend', formatter:WhgComm.FMTDateTime ">众筹结束时间</th>

        <c:if test="${pageType eq 'listpublish'}">
            <th data-options="sortable: true, field:'issuccess', formatter:fmtGatIssuccess ">进度</th>
            <th data-options="sortable:true, field:'recommend', formatter:function(v){
                return (v && v==1)? '是':'否'
            }">是否推荐</th>
        </c:if>

        <th data-options="field:'crtuser',width:120, formatter:WhgComm.FMTUserName">编辑者</th>
        <th data-options="field:'checkor',width:120, formatter:WhgComm.FMTUserName">审核者</th>
        <th data-options="field:'publisher',width:120, formatter:WhgComm.FMTUserName">发布者</th>
        <th data-options="field:'statemdfdate',width:130, formatter:WhgComm.FMTDateTime, sortable:true">操作时间</th>
        <th data-options="field:'state',width:50, formatter:WhgComm.FMTBizState" >状态</th>

        <th data-options="field:'id', formatter:WhgComm.FMTOpt, optDivId:'whgdg-opt'">操作</th>
    </tr>
    </thead>
</table>

<div id="tb" style="display: none;">
    <c:if test="${pageType eq 'listedit' or (not empty brandid and not empty brandshow)}">
    <div class="whgd-gtb-btn">
    <c:if test="${empty brandid and empty brandshow}">
        <shiro:hasPermission name="${resourceid}:add">
            <a class="easyui-linkbutton" iconCls="icon-add" onclick="whgListTool.add()">添 加</a>
        </shiro:hasPermission>
    </c:if>
    <c:if test="${not empty brandid and not empty brandshow}">
        <a class="easyui-linkbutton" iconCls="icon-back" onclick="WhgComm.editDialogClose()">返回</a>
    </c:if>
    </div>
    </c:if>

    <div class="whgdg-tb-srch">
        <c:if test="${not empty brandid and not empty brandshow}">
        <input type="hidden" name="brandid" value="${brandid}">
        </c:if>

        <input class="easyui-combobox" name="cultid" id="cultid" prompt="请选择文化馆" data-options="editable:false,width:150" />
        <input class="easyui-combobox" name="deptid" id="deptid" prompt="请选择部门" data-options="editable:false,width:150"/>

        <select class="easyui-combobox" name="etype" prompt="请选择类型" panelHeight="auto" limitToList="true"
                data-options="editable:false,width:120, value:'', valueField:'id', textField:'text', data:getGatEtypes()">
        </select>

        <c:if test="${pageType ne 'listdel'}">
            <input class="easyui-combobox" name="state" id="state" prompt="请选择状态" data-options="editable:false,width:110, panelHeight:'auto', limitToList:true"/>
            <input class="easyui-combobox" name="issuccess" prompt="进度" panelHeight="auto" limitToList="true"
                    data-options="editable:false,width:100, value:'', data:[{'value':'1','text':'成功'},{'value':'2','text':'失败'}]"/>
            </input>
        </c:if>

        <input class="easyui-textbox" name="title" prompt="请输入名称" data-options="width:200">

        <c:if test="${pageType eq 'listpublish'}">
        <select class="easyui-combobox" name="recommend" prompt="是否推荐" panelHeight="auto" limitToList="true"
                data-options="editable:false,width:120, value:'', valueField:'id', textField:'text', data:[{id:'1',text:'推荐'},{id:'0',text:'非推荐'}]">
        </select>
        </c:if>

        <a class="easyui-linkbutton" iconCls="icon-search" onclick="WhgComm.search('#whgdg', '#tb');">查 询</a>
    </div>
</div>


<!-- 操作按钮 -->
<div id="whgdg-opt" style="display: none;">

    <a plain="true" method="whgListTool.view">查看</a>

<c:if test="${empty brandshow}">

    <shiro:hasPermission name="${resourceid}:edit">
        <a plain="true" method="whgListTool.edit" validFun="whgListTool.optValid4EditState">编辑</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:ziyuan">
        <a plain="true" method="whgListTool.resource" validFun="whgListTool.optValid4PageTypeState">资源管理</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:info">
        <a plain="true" method="whgListTool.info" validFun="whgListTool.optValid4PageTypeState">资讯管理</a>
    </shiro:hasPermission>

    <shiro:hasPermission name="${resourceid}:checkgo">
        <a plain="true" method="whgListTool.checkgo" validFun="whgListTool.optValid4EditState">提交审核</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkon">
        <a plain="true" method="whgListTool.checkon" validFun="whgListTool.optValid4EditState">审核通过</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:checkoff">
        <a plain="true" method="whgListTool.checkoff" validFun="whgListTool.optValid4EditState">审核打回</a>
    </shiro:hasPermission>

    <shiro:hasPermission name="${resourceid}:publish">
        <a plain="true" method="whgListTool.publish" validFun="whgListTool.optValid4EditState">发布</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:publishoff">
        <a plain="true" method="whgListTool.publishoff" validKey="state" validVal="6">撤销发布</a>
    </shiro:hasPermission>

    <shiro:hasPermission name="${resourceid}:reserve">
        <a plain="true" method="whgListTool.reserve" validKey="state" validVal="4,6">预定管理</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:kecheng">
        <a plain="true" method="whgListTool.kecheng" validFun="whgListTool.kechengVfun">课程管理</a>
    </shiro:hasPermission>

    <shiro:hasPermission name="${resourceid}:recommend">
        <a plain="true" method="whgListTool.recommend" validFun="whgListTool.recommendVfun">推荐</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:recommendoff">
        <a plain="true" method="whgListTool.recommendoff" validFun="whgListTool.recommendOffVfun">撤销推荐</a>
    </shiro:hasPermission>

    <shiro:hasPermission name="${resourceid}:recovery">
        <a plain="true" method="whgListTool.recovery" validFun="whgListTool.optValid4RecoveryState">回收</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:undel">
        <a plain="true" method="whgListTool.undel" validKey="delstate" validVal="1">撤销回收</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="${resourceid}:del">
        <a plain="true" method="whgListTool.del" validFun="whgListTool.optValid4Del">删除</a>
    </shiro:hasPermission>

</c:if>
</div>
<!-- 操作按钮-END -->


<script>

    function getGatEtypes(){
        return [{"id":"4","text":"活动众筹"},{"id":"5","text":"培训众筹"},{"id":"0","text":"其它众筹"}];
    }

    function fmtGatIssuccess(v,r,i){
        var now = whgListTool.__now;
        var timestart = r.timestart;
        var timeend = r.timeend;

        if (!now || !timestart || !timeend){
            if (v == 0){
                return "";
            }else if (v == 1) {
                return "众筹成功";
            }else {
                return "众筹失败";
            }
        }

        if (now < timestart) {
            return "未开始";
        }else if (now >= timestart && now <= timeend){
            return "众筹中";
        }else{
            if (v == 0){
                return "已结束";
            }else if (v == 1) {
                return "众筹成功";
            }else {
                return "众筹失败";
            }
        }
    }

    $(function(){
        WhgComm.initPMS({
            basePath:'${basePath}',
            cultEid:'cultid', cultValue:'', allcult:true,
            deptEid:'deptid', deptValue:'', alldept:true
        });

        var brandshow = '${brandshow}';
        $("#state").combobox({
            valueField:'id',
            textField:'text',
            value: brandshow!=''? '':whgListTool.getDefaultState4PageType(),
            data: whgListTool.getDataState4PageType()
        });

        WhgComm.search('#whgdg', '#tb', '${basePath}/admin/gather/srchList?pageType=${pageType}');
    });

    var whgListTool = new Gridopts({pageType: "${pageType}"});


    Gridopts.prototype.resource = function(idx) {
        var row = this.__getGridRow(idx);
        //var cultid = $('#cultid').combobox('getValue');
        //WhgComm.editDialog('${basePath}/admin/resource/view/list?reftype=28&refid=' + row.id+'&cultid='+cultid);
        WhgComm.editDialog('${basePath}/admin/mass/resmanage/view/list?refid=' + row.id+'&reftype=28');
    };

    Gridopts.prototype.reserve = function(idx){
        var row = this.__getGridRow(idx);

        switch (row.etype){
            case "4": //活动
                WhgComm.editDialog('${basePath}/admin/activity/view/orderList?reftype=2&id=' + row.refid);
                break;
            case "5": //培训
                WhgComm.editDialog('${basePath}/admin/train/enrol/view/list?id='+row.refid+'&gatherid='+row.id);
                break;
            default:
                WhgComm.editDialog( '${basePath}/admin/gather/order/view/list?gatherid='+row.id+'&title='+ encodeURI(row.title) );
        }
    };

    Gridopts.prototype.kecheng = function(idx){
        var row = this.__getGridRow(idx);
        if (row.etype !=5 || !row.refInfo){
            return;
        }
        WhgComm.editDialog('${basePath}/admin/train/course/view/list?id='+row.refid+'&starttime='+row.refInfo.starttime+'&endtime='+row.refInfo.endtime);
    };

    Gridopts.prototype.kechengVfun = function(idx){
        var row = this.__getGridRow(idx);
        if (row.etype != 5){
            return false;
        }
        return this.optValid4PageTypeState(idx);
    };

    //资讯公告管理
    Gridopts.prototype.info = function(idx){
        var row = this.__getGridRow(idx);
        WhgComm.editDialog('${basePath}/admin/info/view/list?entityid='+row.id+'&entity=4&cultid='+row.cultid+'&deptid='+row.deptid);
    }

</script>
</body>
</html>
