<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>数字文化馆后台管理系统</title>
    <link href="${basePath }/static/easyui/themes/metro/easyui.css" rel="stylesheet">
    <link href="${basePath }/static/easyui/themes/icon.css" rel="stylesheet">
    <link href="${basePath }/static/admin/css/admin.css" rel="stylesheet">
    <style type="text/css">
        .topTt_area{
            background-color: #db433b;
        }
    </style>
    <script type="text/javascript" src="${basePath }/static/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath }/static/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${basePath }/static/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="${basePath }/static/admin/js/jquery-migrate-1.0.0.js"></script>
    <script type="text/javascript" src="${basePath }/static/admin/js/tipso.js"></script>
    <script type="text/javascript" src="${basePath }/static/common/js/jQuery.md5.js"></script>
    <script type="text/javascript" src="${basePath }/static/common/js/easyui.wh.tools.js"></script>
    <script type="text/javascript">
        $.ajaxSetup({
            dataType: "json",
            global: false
        });

        function genHref(href, id, opts) {
            if (opts) {
                opts = opts.replace(/\s*,\s*/g, ',');
            }
            if (href.indexOf('?') > -1) {
                href += '&rsid=' + id + "&opts=" + (opts || '');
            } else {
                href += '?rsid=' + id + "&opts=" + (opts || '');
            }
            return href;
        }

        function NavAction() {
            if (typeof NavAction.__initialization__ != 'undefined') return;
            NavAction.__initialization__ = true;

            //加载菜单数据(内容需要从数据库菜单及操作项表按当前会话用户角色权限对应取值，另外这里取值时若用ajax时用同步模式否则修改 init的显示菜单时机)
            NavAction.prototype.loadNavData = function () {
                var me = this;
                $.ajax({
                    url: "${basePath }/admin/loadMenus",
                    async: false,
                    cache: false,
                    success: function (navData) {
                        me.navData = navData || [];
                    }
                })
            };

            NavAction.prototype.showAdminHome= function (navParent) {
                var adminHome = $('<li class="parent p-13"><a href="javascript:void(0)" class="p-a" title="后台首页"><span>后台首页</span></a></li>');
                navParent.prepend(adminHome);
                adminHome.on("click", function () {
                    $("#cc").layout("hidden", "west");
                    $("#pageContentFrame").attr("src", "${basePath}/admin/admin_home");
                    closeAllPanel();
                });
                adminHome.click();
            };

            //织入一级菜单内容
            NavAction.prototype.showNavPanel_01 = function () {
                var me = this;
                if (!me.navData) return;
                var parentJQ = $(".leftPanel ul.outer");
                this.showAdminHome(parentJQ);
                for (var i in me.navData) {
                    var data = me.navData[i];
                    me._showNavPanel_01(data, parentJQ, 1);
                }
            };
            NavAction.prototype._showNavPanel_01 = function (data, parentJQ, addem, is_last) {
                var me = this;
                var nav_li = $('<li class="parent"></li>');
                if (data.iconcls) nav_li.addClass(data.iconcls);

                var _em = addem ? "<em></em>" : "";
                var nav_a = $('<a><span></span>' + _em + '</a>');
                nav_a.attr("href", "javascript:void(0)");
                nav_a.addClass(data.parent ? "s-a" : "p-a");
                nav_a.attr("title", data.text);
                nav_a.addClass(data.id);
                nav_li.attr("id", data.id);
                nav_a.find("span").text(data.text);
                if (is_last) {
                    nav_a.addClass("menu_child_last");
                }
                nav_li.append(nav_a);
                parentJQ.append(nav_li);

                //处理一级子菜单项的点击连接
                if (data.type == 1 && data.href) {
                    nav_a.on("click", function () {
                        $("#cc").layout("hidden", "west");
                        if (data.href) {
                            $("#pageContentFrame").attr("src", genHref("${basePath}" + data.href, data.id) || "", data.opts);
                        }
                    });
                }
                //处理一级子菜单分类的点击显示下级
                if (data.type == 0 && data.parent && data.children && data.children.length > 0) {
                    nav_a.on("click", function (event, type) {
                        open3Panel();
                        me.showNavPanel_02(type, data.children, data.text);
                    });
                }

                //加载主菜单的子菜单
                if (data.children && data.children.length > 0 && !data.parent) {
                    var _ul = nav_li.find("ul.inside");
                    if (_ul.size() == 0) {
                        _ul = $('<ul class="inside"></ul>');
                        nav_li.append(_ul);
                    }
                    for (var i in data.children) {
                        var is_last = data.children.length == (parseInt(i) + 1);
                        var addem = (data.children[i].children && data.children[i].children.length > 0)
                        me._showNavPanel_01(data.children[i], _ul, addem, is_last);
                    }
                }
            }
            //处理二级菜单内容
            NavAction.prototype.showNavPanel_02 = function (type, data, title) {
                var me = this;
                $("#cc .right-3-panel .tt3").text(title);
                var nav_ul = $("#cc .right-3-panel ul").empty();
                for (var i in data) {
                    var _data = data[i];

                    var _li = $('<li><a href="javascript:void(0)" class="s-a"></a></li>');

                    _li.find("a").text(_data.text);
                    nav_ul.append(_li);
                    _li.data("_href", _data.href);
                    _li.attr("_id", _data.id);
                    _li.data("_id", _data.id);
                    _li.attr("id", _data.id);
                    _li.data("_opts", _data.opts);
                    _li.on("click", function () {
                        nav_ul.find("li").removeClass("active");
                        $(this).addClass("active");

                        var _href = genHref($(this).data("_href"), $(this).data("_id"), $(this).data("_opts")) || "";
                        if (_href) {
                            $("#pageContentFrame").attr("src", "${basePath}" + _href);
                        }
                    })
                }
                if (type && type != "") {
                    //nav_ul.find("li[id='"+type+"']").click();
                    nav_ul.find("#" + type).click();
                } else {
                    nav_ul.find("li:eq(0)").click();
                }
            }

            NavAction.prototype.init = function () {
                var me = this;
                //加载菜单数据
                me.loadNavData();
                me.showNavPanel_01();
            }
        }

        function doOpenMenu(ppid, pid, id) {
            if (!$("#" + ppid).hasClass("active")) {
                $("." + ppid).trigger("click");
            }
            $("#" + ppid).find("." + pid).trigger("click", id);
        }

        //修改管理员密码
        function modifyPwd() {
            editWinform.setWinTitle("修改密码页面");
            editWinform.openWin();
            $('#ff').form('clear');
            var url = '${basePath }/admin/modipasManage';
            editWinform.setFoolterBut({
                onClick: function () {
                    $('#ff').form('submit', {
                        url: url,
                        onSubmit: function (param) {
                            var _valid = $(this).form('enableValidation').form('validate');

                            var a = $('#password1').textbox('getValue');
                            var b = $('#password2').textbox('getValue');
                            var c = $('#password3').textbox('getValue');
                            if (_valid && a == b) {
                                $.messager.alert("提示", "不能和原密码相同");
                                _valid = false;
                            }
                            if (_valid && b != c) {
                                $.messager.alert("提示", "确认密码和新密码不相同");
                                _valid = false;
                            }
                            if (!_valid){
                                //clearPassWordForm(a, b, c);
                            }else{
                                var pwd1 = $("#password1").textbox('getValue');
                                var pwd2 = $("#password2").textbox('getValue');
                                var pwd3 = $("#password3").textbox('getValue');
                                var pwdmd1 = $.md5(pwd1);
                                var pwdmd2 = $.md5(pwd2);
                                var pwdmd3 = $.md5(pwd3);
                                $("#password4").val(pwd2);
                                $("#password1").textbox('setValue', pwdmd1);
                                $("#password2").textbox('setValue', pwdmd2);
                                $("#password3").textbox('setValue', pwdmd3);
                            }
                            return _valid;
                        },
                        success: function (data) {
                            data = eval('(' + data + ')');
                            if (data && data.success == '0') {
                                $.messager.alert("提示", "密码修改成功,请重新登录", "info", function () {
                                    editWinform.closeWin();
                                    window.location.href = "${basePath}/admin/doLogout";
                                });

                            } else {
                                $.messager.alert('提示', '操作失败。原因：' + data.msg, 'error', function(){
                                    clearPassWordForm();
                                });
                            }
                        }
                    });
                }
            })
        }

        function clearPassWordForm(p1, p2, p3){
            p1= p1 || '';
            p2= p2 || '';
            p3= p3 || '';
            $("#password1").textbox('setValue', p1);
            $("#password2").textbox('setValue', p2);
            $("#password3").textbox('setValue', p3);
        }

        //密码加密
        /*function befpwd() {
            var pwd1 = $("input[name='password1']").val();
            var pwd2 = $("input[name='password2']").val();
            var pwd3 = $("input[name='password3']").val();
            var pwdmd1 = $.md5(pwd1);
            var pwdmd2 = $.md5(pwd2);
            var pwdmd3 = $.md5(pwd3);
            $("input[name='password1']").val(pwdmd1);
            $("input[name='password2']").val(pwdmd2);
            $("input[name='password3']").val(pwdmd3);

            return false;
        }*/

        //变量
        var editWinform;
        $(function () {
            var navAction = new NavAction();
            navAction.init();

            //初始化弹出层
            editWinform = new WhuiWinForm("#edit_win",{height:300,width:700});
            editWinform.init();
        })

        $.extend($.fn.validatebox.defaults.rules, {
            pwCheck:{
                validator: function (value, param) {
                    if (!/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$/.test(value)) {
                        $.fn.validatebox.defaults.rules.pwCheck.message = "密码强度弱，请输入不少于8位、字母数字组合密码";
                        return false;
                    }
                    return true;
                },
                message: ''
            }
        });
    </script>
    <script type="text/javascript" src="${basePath }/static/admin/js/creatoo.admin.js"></script>
</head>

<body class="easyui-layout body-main" data-options="fit:true,collapsible:false">
<div data-options="region:'north',border:false" class="topTt" id="adminTop" style="${sessionAdminUser.admintype == 'sysmgr'?'background-color: #da635d;':''}">
    <div class="logo"
         style="${sessionAdminUser.admintype == 'sysmgr'?'background: url(../../static/admin/img/logo2.jpg) repeat;':''}">
        <a href="/admin"></a></div>
    <div class="home"><a href="javascript:void(0)" onclick="location.reload()">
        <c:choose>
            <c:when test="${sessionAdminUser.admintype == 'bizmgr'}">文化馆业务管理系统<span id="cultShowName"></span></c:when>
            <c:otherwise>总分馆管理系统</c:otherwise>
        </c:choose>
    </a></div>

    <div class="userCont" style="${sessionAdminUser.admintype == 'sysmgr'?'border-left:#da635d solid 1px':''}">
        <div class="userName" style="${sessionAdminUser.admintype == 'sysmgr'?'background-color:#da635d':''}">
            <em></em>
            <span id="adminShowName"></span>
            <c:choose>
                <c:when test="${sessionAdminUser.account == 'administrator'}"></c:when>
                <c:otherwise><span><a href="javascript:void(0)" onclick="modifyPwd();">修改密码</a></span></c:otherwise>
            </c:choose>
            <span><a href="${basePath }/admin/doLogout">退出</a></span>
        </div>
    </div>
    <div class="msg none">
        <span>17</span>
    </div>
</div>
<div class="leftPanel" data-options="region:'west',border:false,collapsible:false">
    <div class="zoom in"></div>
    <div class="bar">
        <ul class="outer">

        </ul>
    </div>
</div>
<div data-options="region:'center',border:false" style="overflow:hidden;">
    <div id="cc" class="easyui-layout right-main" fit="true">
        <div data-options="region:'west',border:false" class="right-3-panel">
            <div class="layoutBtn addBtn"></div>
            <div class="tt3"><!-- 新闻公告 --></div>
            <ul>

            </ul>
        </div>
        <div data-options="region:'center',border:false" style="padding:5px;background:#fff;" class="center-main">
            <div class="layoutBtn subBtn"></div>
            <div class="easyui-panel" title="" style="width:100%;height:100%; overflow: hidden" data-options="fit:true,border:false">
                <iframe id="pageContentFrame" name="pageContentFrame" width="100%" height="100%"
                        frameborder="0"></iframe>
            </div>
            <%--<div id="ft" style="padding:5px; color:#999;"> 使用过程中遇到问题，请参看帮助文档 </div>--%>
        </div>
    </div>
</div>
<!--修改密码层 -->
<div id="edit_win" class="none">
    <form method="post" id="ff">
        <input type="hidden" id="password4" name="password4" value="">
        <!-- 隐藏作用域 -->
        <div class="main">
            <div class="row">
                <div><label>请输入原密码:</label></div>
                <div>
                    <input id="password1" name="password1" validType="length[6,32]" class="easyui-textbox"
                           style="width:90%;height:35px"
                           required="true" type="password"/>
                </div>
            </div>
            <div class="row">
                <div><label>请输入新密码:</label></div>
                <div>
                    <div>
                        <input id="password2" name="password2" validType="pwCheck" class="easyui-textbox"
                               style="width:90%;height:35px"
                               required="true" type="password"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div><label>确定新密码:</label></div>
                <div>
                    <div>
                        <input id="password3" name="password3" validType="length[6,32]" class="easyui-textbox"
                               style="width:90%;height:35px"
                               required="true" type="password"/>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript">
    $(function () {
        var managerCults = ${sessionAdminUserCults};
        var managerDepts = ${sessionAdminUserDepts};
        if('administrator' == '${sessionAdminUser.account}'){
            $('#adminShowName').text('${sessionAdminUser.account}');
        }else if('${sessionAdminUser.admintype}' == 'sysmgr'){//区域管理员
            if('${sessionAdminUser.adminlevel}' == '1'){
                $('#adminShowName').text('${sessionAdminUser.adminprovince}管理员：${sessionAdminUser.account}');
            }else if('${sessionAdminUser.adminlevel}' == '2'){
                $('#adminShowName').text('${sessionAdminUser.admincity}管理员：${sessionAdminUser.account}');
            }else{
                $('#adminShowName').text('${sessionAdminUser.adminarea}管理员：${sessionAdminUser.account}');
            }
        }else{//站点管理员
            if('${sessionAdminUser.isbizmgr}' == '1'){
                $('#cultShowName').text('--'+managerCults[0].text);
                $('#adminShowName').text('超级管理员：${sessionAdminUser.account}');
            }else{
                $('#cultShowName').text('--'+managerCults[0].text);
                $('#adminShowName').text('普通管理员：${sessionAdminUser.account}');
            }
        }
    });
</script>
</body>
</html>