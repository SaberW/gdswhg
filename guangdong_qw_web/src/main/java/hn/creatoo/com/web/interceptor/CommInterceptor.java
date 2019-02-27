package hn.creatoo.com.web.interceptor;

import hn.creatoo.com.web.config.ApiUrlsConfig;
import hn.creatoo.com.web.mode.WhUser;
import hn.creatoo.com.web.util.ConfigUtils;
import hn.creatoo.com.web.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 会话拦截
 * Created by wangxl on 2017/6/1.
 */
@PropertySource("classpath:application.properties")
public class CommInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String staticUrl = ConfigUtils.getApiConfig().getStaticPath();
        String rootUrl = ConfigUtils.getApiConfig().getRoot();

        //获取绝对路径
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
        if(80 == request.getServerPort()){
            basePath = request.getScheme()+"://"+request.getServerName()+request.getContextPath();
        }
        request.setAttribute( "basePath", basePath);
        request.setAttribute( "imgPath",staticUrl);
        request.setAttribute( "rootUrl",rootUrl);
        //设置接口API
        Map<String, Object> jsonObject = new HashMap<String, Object>();

        /*jsonObject.put("hasPhone",rootUrl + "api/user/isPhone"); //手机号是否存在 POST
        jsonObject.put("sendCode",rootUrl + "user/sendPhone"); //发送验证码 POST
        jsonObject.put("hasCode",rootUrl + "api/user/isPhoneCode"); //验证验证码 POST

        jsonObject.put("login",rootUrl + "api/user/doLogin"); //用户登录 POST

        jsonObject.put("regist1",rootUrl + "user/saveRegist"); //用户注册step1 POST
        jsonObject.put("regist2",rootUrl + "user/saveRegist2"); //用户注册step2 POST

        jsonObject.put("hasNickName",rootUrl + "api/user/isNickname"); //验证昵称是否重复 POST

        jsonObject.put("userInfo",rootUrl + "api/user/detail"); //获取用户信息 POST
        jsonObject.put("userInfoSave",rootUrl + "api/user/info/save"); //保存用户信息 POST

        jsonObject.put("actCate",rootUrl + "api/comm/getTypes"); //分类查询 POST
        jsonObject.put("gxTypes",rootUrl + "api/comm/getEnumValues/EnumSupplyType"); //分类查询 POST
        jsonObject.put("actList",rootUrl + "api/activity/list"); //活动列表 POST
        jsonObject.put("actDetail",rootUrl + "api/activity/detail"); //v活动详情 POST
        jsonObject.put("actBooking",rootUrl + "api/activity/findSeat4ActId"); //活动预约 POST
        jsonObject.put("actBookSave",rootUrl + "api/activity/bookingsave"); //活动订单提交 POST
        jsonObject.put("checkActPublish",rootUrl + "api/activity/checkActPublish"); //活动订单检查 POST
        jsonObject.put("venueListList",rootUrl + "api/venue/list"); //场馆列表 POST
        jsonObject.put("venueDetail",rootUrl + "api/venue/detail"); //场馆详情 POST
        jsonObject.put("recommendVenList",rootUrl + "api/venue/recommendVenList"); //推荐场馆列表POST
        jsonObject.put("roomlist",rootUrl + "api/venue/roomlist"); //活动室列表POST
        jsonObject.put("refRoomList",rootUrl + "api/venue/refRoomList"); //相关活动室列表POST
        jsonObject.put("roomTimeMaxMinDay",rootUrl + "api/venue/roomTimeMaxMinDay"); //获取活动室含系统当前天之后，有开放的最大和最小天的时间值
        jsonObject.put("roomApplyAll",rootUrl + "api/venue/roomApplyAll"); //查询时段内活动室的开放预订时间列表
        jsonObject.put("roomdetail",rootUrl + "api/venue/roomDetail"); //活动室详情 POST
        jsonObject.put("venueOrderCheck",rootUrl + "api/venue/orderCheck"); //场馆预约检查 POST
        jsonObject.put("orderApply",rootUrl + "api/venue/orderApply"); //场馆预约提交 POST

        jsonObject.put("dictionaries",rootUrl + "api/user/dict"); //查询分类字典 POST
        jsonObject.put("tagdict",rootUrl + "api/user/tagdict"); //查询标签字典 POST

        jsonObject.put("myAct",rootUrl + "api/activity/getMyActList");  //获取我的活动 POST
        jsonObject.put("myVenue",rootUrl + "api/user/venue");  //获取我的场馆 POST
        jsonObject.put("venueUnOrder",rootUrl + "api/venue/unOrder");  //取消场馆订单 POST

        jsonObject.put("myFavorite",rootUrl + "api/collection/collectList"); //获取我的收藏 POST
        jsonObject.put("myComment",rootUrl + "api/user/getComment"); //获取我的评论 POST

        jsonObject.put("delOrder",rootUrl + "/api/usercenter/cancel"); //取消订单 POST
        jsonObject.put("deleteCollect",rootUrl + "api/collection/removeColle"); //删除收藏 POST
        jsonObject.put("removeContent",rootUrl + "api/user/removeContent"); //删除评论 POST

        jsonObject.put("hasPwd",rootUrl + "api/user/isPassWord");  //验证密码 POST
        jsonObject.put("rePwd",rootUrl + "api/user/modifyPassword"); //修改密码 POST
        jsonObject.put("findPwd",rootUrl + "api/user/setPasswd");  //找回密码 POST

        jsonObject.put("readList",rootUrl + "reading/szreading"); //数字阅读 POST
        jsonObject.put("readDetail",rootUrl + "exhibitionhall/getHallList");  //数字展馆 POST
        jsonObject.put("getExhibitList",rootUrl + "exhibitionhall/getExhibitList");  //数字展馆详情 POST
        jsonObject.put("commentList",rootUrl + "api/user/showComment");  //评论列表 POST
        jsonObject.put("commentHuifu",rootUrl + "api/user/commentHuifu");  //评论回复列表 POST

        jsonObject.put("addComment",rootUrl + "api/user/addComment");  //评论 POST

        jsonObject.put("voteCount",rootUrl + "api/comm/isLightenGood");  //查询点赞 POST
        jsonObject.put("voteup",rootUrl + "api/comm/addGood");  //点赞 POST

        jsonObject.put("bindPhone",rootUrl + "api/user/bindPhone");  //绑定手机号 POST

        jsonObject.put("getUserInfo",rootUrl + "api/user/detail");  //获取用户信息 POST
        jsonObject.put("saveCardInfo",rootUrl + "api/user/saveRealInfo");  //保存身份证信息 POST
        jsonObject.put("uploadCardPic",rootUrl + "api/user/uploadIdcard"); //上传身份证照片 POST

        jsonObject.put("initFav",rootUrl + "api/comm/isLightenColle"); //查询收藏  POST
        jsonObject.put("addFav",rootUrl + "api/collection/addMyColle"); //添加收藏  POST
        jsonObject.put("delFav",rootUrl + "api/collection/removeColle"); //取消用户收藏  POST
        jsonObject.put("delListFav",rootUrl + "api/collection/remColles"); //取消列表收藏  POST

        jsonObject.put("cultList",rootUrl + "api/feiyi/cultList"); //遗产列表  POST
        jsonObject.put("cultDetail",rootUrl + "api/feiyi/cultDetail"); //遗产详情  POST
        jsonObject.put("talentsList",rootUrl + "api/feiyi/talentsList"); //人才列表  POST
        jsonObject.put("talentsDetail",rootUrl + "api/feiyi/talentsDetail"); //人才详情  POST
        jsonObject.put("historicalList",rootUrl + "api/feiyi/historicalList"); //文物列表  POST
        jsonObject.put("historicalDetail",rootUrl + "api/feiyi/historicalDetail"); //文物详情  POST

        jsonObject.put("didType",rootUrl + "api/feiyi/selectType"); //非遗类型

        jsonObject.put("getTypes",rootUrl + "api/comm/getTypes"); //培训筛选
        jsonObject.put("getTags",rootUrl + "api/comm/getTags"); //培训筛选
        jsonObject.put("trainIndex",rootUrl + "api/tra/indexData"); //培训首页
        jsonObject.put("trainList",rootUrl + "api/tra/list"); //普通培训列表
        jsonObject.put("traListCulMarket",rootUrl + "api/tra/traListCulMarket"); //文化超市培训列表
        jsonObject.put("traDetail",rootUrl + "api/tra/detail"); //培训详情
        jsonObject.put("bookingvalid",rootUrl + "api/tra/bookingvalid"); //培训详情
        jsonObject.put("getRecommendTra",rootUrl + "api/tra/getRecommendTra"); //推荐培训
        jsonObject.put("getSyllabus",rootUrl + "api/tra/traCourseList"); //获取课程表
        jsonObject.put("getMyTraEnrol",rootUrl + "api/tra/getMyTraEnrol"); //获取我的培训
        jsonObject.put("cancelTraEnrol",rootUrl + "api/tra/myenroll/delmyenroll"); //取消用户已报名的培训

        jsonObject.put("traSign",rootUrl + "api/tra/booking"); //培训报名 POST
        jsonObject.put("findTraEnrol",rootUrl + "api/tra/findTraEnrol"); //培训验证 POST

        jsonObject.put("delAct",rootUrl + "/api/activity/delMyAct"); //删除过期活动订单 POST
        jsonObject.put("delVen",rootUrl + "api/venue/delOrder"); //删除过期场馆订单 POST

        jsonObject.put("search",rootUrl + "api/global/search"); //智能查询 POST

        jsonObject.put("Kulturbund",rootUrl + "api/kulturbund/indexData"); //文化联盟首页 POST
        jsonObject.put("KulturbundList",rootUrl + "/api/kulturbund/cultureZx4p"); //文化联盟资讯 POST
        jsonObject.put("unionDetail",rootUrl + "/api/kulturbund/unionDetail"); //文化联盟详情 POST

        jsonObject.put("cityStyle",rootUrl+"api/showStyle/searchCityStyle"); //市民风采列表 POST
        jsonObject.put("cityStyleDetail",rootUrl+"api/showStyle/showStyleDetail"); //市民风采详情 POST
        jsonObject.put("resList",rootUrl+"api/showStyle/showStyleList"); //特色资源列表 POST
        jsonObject.put("adList",rootUrl+"api/showStyle/showStylePic"); //秀我风采广告列表 POST
        jsonObject.put("myUpload",rootUrl+"api/showStyle/searchPersonStyle"); //秀我风采-我的上传 POST

        jsonObject.put("cityStyleUpload",rootUrl+"api/showStyle/upload"); //市民风采上传 POST
        jsonObject.put("cityStyleAdd",rootUrl+"api/showStyle/add");//市民风采表单提交 POST
        jsonObject.put("myUploadDel",rootUrl+"api/showStyle/delUpload"); //市民风采删除 POST

        jsonObject.put("pickUp",rootUrl+"api/ticket/pickUp"); //取票 POST
        jsonObject.put("completePickUp",rootUrl+"api/ticket/completePickUp"); //取票改状态 POST

        //api/ticket/completePickUp
        jsonObject.put("liveIndex",rootUrl+"api/live/indexData"); //直播首页 POST
        jsonObject.put("liveList",rootUrl+"api/live/list4p"); //直播列表页 POST
        jsonObject.put("liveGetCondition",rootUrl+"api/live/getCondition"); //直播列表筛选条件 POST
        jsonObject.put("liveDetail",rootUrl+"api/live/getDetail"); //直播详情 POST
        jsonObject.put("liveGetRecommend",rootUrl+"api/live/getRecommend"); //直播详情推荐 POST

        jsonObject.put("addClubMember",rootUrl+"api/cultclub/addClubMember"); //团体报名 POST
        jsonObject.put("selectClubList",rootUrl+"api/cultclub/selectClubList"); //团体列表 POST
        jsonObject.put("clubDetail",rootUrl+"api/cultclub/clubDetail"); //团体详情 POST
        jsonObject.put("countClub",rootUrl+"api/cultclub/countClub"); //我创建的团体 POST
        jsonObject.put("selectJoinClub",rootUrl+"api/cultclub/selectJoinClub"); //我加入的团体 POST
        jsonObject.put("clubMemberListByClubId",rootUrl+"api/cultclub/clubMemberListByClubId"); //团体成员列表 POST

        jsonObject.put("createClub",rootUrl+"api/cultclub/addClub"); //创建团队名称 POST
        jsonObject.put("hasClub",rootUrl+"api/cultclub/countClub"); //判断是否有团队 POST
        jsonObject.put("clubMember",rootUrl+"api/cultclub/countClubMember"); //判断团队信息是否完善 POST
        jsonObject.put("clubEdit",rootUrl+"api/cultclub/editClub");//管理社团 POST
        jsonObject.put("countMember",rootUrl+"api/cultclub/countMember");//当前可招募人数最大值 POST

        jsonObject.put("modifyUser",rootUrl+"user/modifyUser"); //修改用户基本信息 POST

        jsonObject.put("selectMyClub",rootUrl+"api/cultclub/selectMyClub"); //查询我的团队 POST
        jsonObject.put("clubUserList",rootUrl+"api/cultclub/clubMemberListNoPass"); //团队管理用户列表 POST
        jsonObject.put("clubUserDetail",rootUrl+"api/cultclub/clubMemberDetail"); //团队管理用户个人详情 POST
        jsonObject.put("clubUserCheck",rootUrl+"api/cultclub/doCheckMember");  //团队管理审核用户 POST
        jsonObject.put("addStyle",rootUrl+"api/cultclub/addStyle");  //团队管理添加风采 POST
        jsonObject.put("clubStyleList",rootUrl+"api/cultclub/teamStyleList");  //团队风采列表 POST
        jsonObject.put("delStyle",rootUrl+"api/cultclub/delClubFeature");  //删除风采 POST
        jsonObject.put("countClubName",rootUrl+"api/cultclub/countClubName");  //团队名称是否同名 POST
        jsonObject.put("uploadCut",rootUrl+"api/showStyle/uploadCut");  //图片裁剪 POST

        jsonObject.put("selectVoluntTeamList",rootUrl+"api/volunt/selectVoluntTeamList");  //志愿者列表 POST
        jsonObject.put("voluntTeamDetail",rootUrl+"api/volunt/voluntTeamDetail");  //志愿者详情 POST
        jsonObject.put("addVoluntTeamMember",rootUrl+"api/volunt/addVoluntTeamMember");  //添加志愿者团队成员接口 POST
        jsonObject.put("searchPersonStyle",rootUrl+"api/volunt/voluntTeamList");  //添加志愿者下拉框接口 POST
        jsonObject.put("hasTeamMember",rootUrl+"api/volunt/hasTeamMember");  //添加志愿者下拉框接口 POST

        jsonObject.put("popSource",rootUrl+"api/index/popSource");  //首页热门资源 POST
        jsonObject.put("showAdver",rootUrl+"api/basic/lbts");  //公共轮播图 POST

        jsonObject.put("filmIndexBanner",rootUrl+"api/film/getIndexInfo");  //周二影院轮播图 POST
        jsonObject.put("filmGetList",rootUrl+"api/film/getList");  //周二影院列表 POST
        jsonObject.put("filmGetDetail",rootUrl+"api/film/getDetail");  //周二影院详情 POST
        jsonObject.put("currentMonth",rootUrl+"api/film/currentMonth");  //周二影院本月列表 POST
        jsonObject.put("nextMonth",rootUrl+"api/film/nextMonth");  //周二影院下月列表 POST

        jsonObject.put("getCategory",rootUrl+"api/pop/getCategory");//获取资源分类
        jsonObject.put("searchPop",rootUrl+"api/pop/search4List");//获取热门资源
        jsonObject.put("activityList",rootUrl+"api/activity/list");//获取热门活动
        jsonObject.put("venueList",rootUrl+"api/venue/indexList");//获取热门场馆


        jsonObject.put("sendSmsCode",rootUrl+"api/user/sendSmsCode");//发送验证码  NEW
        jsonObject.put("setPassword",rootUrl+"api/user/setPassword");//修改密码  NEW
        jsonObject.put("relationMobile",rootUrl+"api/user/relationMobile");//修改手机  NEW
        jsonObject.put("userEdit",rootUrl+"api/user/edit");//修改用户资料  NEW

        jsonObject.put("findPassword",rootUrl+"api/user/setPasswd");//找回密码 NEW
        jsonObject.put("userRegister",rootUrl+"api/user/register");//新用户注册 NEW
        jsonObject.put("userRegister2",rootUrl+"api/user/register2");//新用户注册 NEW

        jsonObject.put("getCategory",rootUrl+"/api/pop/getCategory");//获取资源分类
        jsonObject.put("searchPop",rootUrl+"/api/pop/search4List");//获取热门资源
        jsonObject.put("activityList",rootUrl+"api/activity/list");//获取热门活动
        jsonObject.put("venueList",rootUrl+"api/venue/indexList");//获取热门场馆
        jsonObject.put("switch",rootUrl+"api/comm/getCult4city");//获取资源分类
        jsonObject.put("query",rootUrl+"api/comm/getCult");//获取资源分类

        //供需平台内部接口
        jsonObject.put("resCgList",rootUrl+"api/inside/venue/list");//获取场馆列表
        jsonObject.put("resPxList",rootUrl+"api/inside/tra/list");//获取培训列表
        jsonObject.put("resPxInfo",rootUrl+"api/inside/tra/detail");//获取培训列表
        jsonObject.put("resRcList",rootUrl+"api/inside/person/list");//获取人才列表
        jsonObject.put("resRcInfo",rootUrl+"api/inside/person/info");//获取人才详情
        jsonObject.put("personRecommends",rootUrl+"api/inside/person/recommends");//推荐人才列表
        jsonObject.put("resSsList",rootUrl+"api/inside/goods/list");//获取设施列表
        jsonObject.put("resSsInfo",rootUrl+"api/inside/goods/info");//获取设施详情
        jsonObject.put("goodsRefList",rootUrl+"api/inside/goods/reflist");//推荐设施列表
        jsonObject.put("resJmList",rootUrl+"api/inside/showGoods/list");//获取节目列表
        jsonObject.put("resJmInfo",rootUrl+"api/inside/showGoods/detail");//获取节目详情
        jsonObject.put("resZlList",rootUrl+"api/inside/showExh/list");//获取展览列表
        jsonObject.put("resZlInfo",rootUrl+"api/inside/showExh/detail");//获取展览详情
        jsonObject.put("exhGoodsList",rootUrl+"api/inside/showExh/exhGoodsList");//获取展览展品列表
        jsonObject.put("exhGoodsDetail",rootUrl+"api/inside/showExh/exhGoodsDetail");//获取展览展品详情
        jsonObject.put("recommendTraList",rootUrl+"api/inside/tra/recommendTraList");//获取培训推荐
        jsonObject.put("recommendExhList",rootUrl+"api/inside/showExh/recommendExhList");//获取展览推荐
        jsonObject.put("recommendShowList",rootUrl+"api/inside/showGoods/recommendShowList");//获取节目推荐

        jsonObject.put("resCgInfo",rootUrl+"api/inside/venue/info");//获取场馆
        jsonObject.put("resCgRoomList",rootUrl+"api/inside/venue/rooms");//获取场馆活动室列表
        jsonObject.put("resCgReVenList",rootUrl+"api/inside/venue/recommendVenList");//获取场馆推荐
        jsonObject.put("resCgRoomInfo",rootUrl+"api/inside/venue/roominfo");//获取场馆活动室
        jsonObject.put("resCgRefRoomList",rootUrl+"api/inside/venue/refrooms");//获取相关活动室

        jsonObject.put("openDays",rootUrl+"api/inside/supply/openDays");//供需插件(天数集)
        jsonObject.put("firstOpenYMD",rootUrl+"api/inside/supply/firstOpenYMD");//供需插件(最近的年月日)
        jsonObject.put("getgxcity",rootUrl+"api/inside/supply/getgxcity");//可配送的范围
        jsonObject.put("getSupplyInfo",rootUrl+"api/inside/supply/info");//配送基本信息
        jsonObject.put("addDelivery",rootUrl+"api/inside/delivery/addDelivery");//申请配送


        jsonObject.put("validLogin",rootUrl+"api/inside/validLogin");//内部供需登录接口
        jsonObject.put("supplyList",rootUrl+"api/inside/supply/list");//供需列表查询
        jsonObject.put("supplyInfo",rootUrl+"api/inside/supply/info");//供需列表详情
        jsonObject.put("supplyTimeList",rootUrl+"api/inside/supply/timeList");//供需配送时间列表
        jsonObject.put("supplyInfoMatchList",rootUrl+"api/inside/supply/infoMatchList");//供需推荐列表
        jsonObject.put("supplyGetGxCity",rootUrl+"api/inside/supply/getgxcity");//供需配送范围
        jsonObject.put("canApply",rootUrl+"api/inside/supply/canApply");//检测能否申请

        jsonObject.put("getCitys4resource",rootUrl+"api/inside/comm/getCitys4resource");//资源供需配送范围

        jsonObject.put("supplyUserEdit",rootUrl+"api/inside/user/edit");//资料编辑
        jsonObject.put("supplyUserInfo",rootUrl+"api/inside/user/info");//资料获取
        jsonObject.put("supplyMsgList",rootUrl+"api/inside/msg/list");//我的消息
        jsonObject.put("msgDel",rootUrl+"api/inside/msg/remove");//删除消息
        jsonObject.put("myDelivery",rootUrl+"api/inside/delivery/myDelivery");//我的配送
        jsonObject.put("myFavoriteList",rootUrl+"api/inside/collection/list");//我的收藏

        jsonObject.put("addCollection",rootUrl+"api/inside/collection/addCollection");//添加收藏
        jsonObject.put("isLightenColle",rootUrl+"api/inside/collection/isLightenColle");//判断收藏
        jsonObject.put("removeColle",rootUrl+"api/inside/collection/removeColle");//删除收藏

        jsonObject.put("supplyGetTypes",rootUrl+"api/comm/getTypes");//获取类型

        //api/inside/collection/list
        //api/inside/user/info
        ///api/inside/msg/remove
        //api/inside/delivery/myDelivery
        //api/inside/msg/list
        //api/inside/user/edit
        //api/inside/supply/getgxcity
        //api/inside/supply/firstOpenYMD
        //api/inside/delivery/addDelivery
*/

        Map<String, String> urls = ConfigUtils.getApiUrls();
        //System.out.println("==========>>>>>>"+urls);
        for (Map.Entry<String, String> ent : urls.entrySet()) {
            jsonObject.put(ent.getKey(), rootUrl + ent.getValue());
        }
        jsonObject.put("province","广东省");

        //System.out.println("aaaaaaaaa======>>>>>>>"+jsonObject);

        request.setAttribute("apiPath",jsonObject);
        return super.preHandle(request, response, handler);
    }
}
