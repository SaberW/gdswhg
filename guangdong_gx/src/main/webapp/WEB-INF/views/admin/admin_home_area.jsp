<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<% request.setAttribute("basePath", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()); %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>数字文化馆后台管理系统</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${basePath}/static/bootstrap2.3.2/css/mian.css" rel="stylesheet" media="screen">
    <link href="${basePath}/static/bootstrap2.3.2/css/bootstrap.css" rel="stylesheet" media="screen">
    <script type="text/javascript" src="${basePath }/static/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath }/static/bootstrap2.3.2/js/bootstrap.min.js"></script>
    <style>
        .box{
            border: 1px solid #d9d9d9;
            margin-top: 10px;
        }
        .box .box-header{
             height: 35px;
             line-height: 35px;
             background-color: #ddd;
         }
        .box .box-header span{
            margin-left: 10px;
            font-size: 14px;
            font-weight: 800;
        }
        .box .box-header i{
            color:red;
            font-size: 18px;
            font-weight: 800;
        }
        span.num{
            color:red;
            font-size: 18px;
            font-weight: 400;
        }
    </style>
</head>
<body style="overflow-x: hidden;">

<div class="container-fluid">
    <div class="crumbs" style="border: 1px solid #d9d9d9;">
        <ul id="breadcrumbs" class="breadcrumb">
            <li><i class="icon-home"></i><a href="javascript:void(0);">欢迎您进入总分馆管理系统！</a></li>
        </ul>
        <ul class="crumb-buttons">
            <li class="range">
                <a href="#">
                    <i class="icon-calendar"></i>
                    <span>${curtDate}</span>
                </a>
            </li>
        </ul>
    </div>
</div>

<div class="row-fluid" style="margin-top: 20px;">
    <div class="span12" style="text-align: center;">
        <img src="http://119.23.73.71:8080/whgstatic//shop/picture/2017/201704/20170414/2017041400000005.jpg" style="margin: 0 auto;" width="1200" height="455">
    </div>
</div>

<div class="row-fluid">
    <div class="span12">
        <div class="page-detail">
            <%--<h1>广东省文化馆总分馆管理系统</h1>--%>

            <!-- 广东省文化馆的详情介绍 -->
            <p style="margin-top: 0px; margin-bottom: 1em; padding: 0px; border: 0px; outline: 0px; font-size: 14px; font-family: &quot;Microsoft YaHei&quot;, &quot;Hiragino Sans GB&quot;, Helvetica, Arial, &quot;Lucida Grande&quot;, sans-serif; vertical-align: baseline; color: rgb(51, 51, 51); line-height: 24px; white-space: normal; background-color: rgb(255, 255, 255);"><span style="margin: 0px; padding: 0px; border: 0px; outline: 0px; font-weight: inherit; font-style: inherit; font-size: 16px; font-family: 微软雅黑; vertical-align: baseline;"><span style="color: rgb(51, 51, 51); font-family: 微软雅黑; background-color: rgb(255, 255, 255);">&nbsp; &nbsp; &nbsp; &nbsp;</span>广东省文化馆（广东省非物质文化遗产保护中心）成立于1956年，是广东省人民政府设立的专门从事群众文化工作和非物质文化传承保护工作的正处级公益一类事业单位。</span></p><p style="margin-top: 0px; margin-bottom: 1em; padding: 0px; border: 0px; outline: 0px; font-size: 14px; font-family: &quot;Microsoft YaHei&quot;, &quot;Hiragino Sans GB&quot;, Helvetica, Arial, &quot;Lucida Grande&quot;, sans-serif; vertical-align: baseline; color: rgb(51, 51, 51); line-height: 24px; white-space: normal; background-color: rgb(255, 255, 255);"><span style="margin: 0px; padding: 0px; border: 0px; outline: 0px; font-weight: inherit; font-style: inherit; font-size: 16px; font-family: 微软雅黑; vertical-align: baseline;"><span style="margin: 0px; padding: 0px; border: 0px; outline: 0px; font-weight: inherit; font-style: inherit; vertical-align: baseline;">&nbsp; &nbsp; &nbsp; &nbsp;</span>其主要职责任务有：组织开展具有导向性、示范性的群众文化艺术活动；辅导农村、社区、企业等开展群众文化活动，辅导、培训辖区内文化馆、站业余干部及文艺活动业务骨干，组织、指导、研究群众性文艺创作活动；组织开展群众文艺理论研究，搜集、整理、保护民族民间文化艺术遗产；负责广东省文化志愿者总队及全省文化志愿者队伍建设、管理、培训工作和文化志愿服务开展；执行全省非物质文化遗产保护的规划、计划和工作规范，组织实施全省非物质文化遗产的普查、认定、申报、保护和交流传播工作。</span></p><p style="margin-top: 0px; margin-bottom: 1em; padding: 0px; border: 0px; outline: 0px; font-size: 14px; font-family: &quot;Microsoft YaHei&quot;, &quot;Hiragino Sans GB&quot;, Helvetica, Arial, &quot;Lucida Grande&quot;, sans-serif; vertical-align: baseline; color: rgb(51, 51, 51); line-height: 24px; white-space: normal; background-color: rgb(255, 255, 255);"><span style="margin: 0px; padding: 0px; border: 0px; outline: 0px; font-weight: inherit; font-style: inherit; font-size: 16px; font-family: 微软雅黑; vertical-align: baseline;"><span style="margin: 0px; padding: 0px; border: 0px; outline: 0px; font-weight: inherit; font-style: inherit; vertical-align: baseline;">&nbsp; &nbsp; &nbsp; &nbsp;</span>馆内设有办公室、活动部、培训部、创作部、信息部、团队部、拓展部、省非物质文化遗产保护中心办公室共八个部室。</span></p><p style="margin-top: 0px; margin-bottom: 1em; padding: 0px; border: 0px; outline: 0px; font-size: 14px; font-family: &quot;Microsoft YaHei&quot;, &quot;Hiragino Sans GB&quot;, Helvetica, Arial, &quot;Lucida Grande&quot;, sans-serif; vertical-align: baseline; color: rgb(51, 51, 51); line-height: 24px; white-space: normal; background-color: rgb(255, 255, 255);"><span style="margin: 0px; padding: 0px; border: 0px; outline: 0px; font-weight: inherit; font-style: inherit; font-size: 16px; font-family: 微软雅黑; vertical-align: baseline;"><span style="margin: 0px; padding: 0px; border: 0px; outline: 0px; font-weight: inherit; font-style: inherit; vertical-align: baseline;">&nbsp; &nbsp; &nbsp; &nbsp;</span>作为我省现代公共文化服务体系建设和公共文化服务的重要参与者、提供者，广东省文化馆始终坚持在省委、省政府和省文化厅的领导下，围绕党和政府的中心工作，以满足群众文化需求为立足点，以改善群众文化生活为目标，充分发挥省馆龙头示范作用，不断完善和创新现代公共文化服务，努力实现好、维护好、保障好广大人民群众的基本公共文化权益；以高度的历史责任感和使命感，着力推进现代公共文化服务体系建设，为我省建设文化强省和幸福广东，实现“三个定位、两个率先”的总目标做出应有的贡献。</span></p><p><br></p><p></p>
            <!-- 广东省文化馆的详情介绍 -END-->
        </div>
    </div>
</div>


</body>
</html>