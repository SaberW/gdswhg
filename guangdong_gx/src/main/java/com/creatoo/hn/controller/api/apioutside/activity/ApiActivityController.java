package com.creatoo.hn.controller.api.apioutside.activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.admin.gather.WhgGatherService;
import com.creatoo.hn.services.admin.mylive.MyLiveService;
import com.creatoo.hn.services.admin.resourse.WhgResourceService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.system.WhgSystemDeptService;
import com.creatoo.hn.services.admin.user.WhgBlackListService;
import com.creatoo.hn.services.api.apioutside.activity.ApiActivityService;
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.util.FilterFontUtil;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumProject;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 活动预定接口
 */
@CrossOrigin
@RestController
@RequestMapping("/api/activity")
public class ApiActivityController extends BaseController {
    /**
     * 短信公开服务类
     */
    @Autowired
    private WhgGatherService whgGatherService;

    /**
     * 直播服务
     */
    @Autowired
    private MyLiveService myLiveService;

    @Autowired
    private ApiActivityService apiActivityService;

    @Autowired
    private WhgResourceService whgResourceService;

    @Autowired
    private WhgSystemDeptService deptService;

    /**
     * 文化馆服务
     */
    @Autowired
    private WhgSystemCultService whgSystemCultService;


    /**
     * 检查能否报名
     * 访问路径 /api/act/check/{actId}/{userId}
     * @param actId  活动Id
     * @param userId 用户Id
     * @return JSON: {
     * "success" : "1"             //1表示可以报名，其它失败
     * "errormsg" : "100|104"     //100-培训已失效;  104-未实名认证
     * }
     */
    @CrossOrigin
    @RequestMapping("/check/{actId}/{userId}")
    public ApiResultBean check(@PathVariable("actId")String actId, @PathVariable("userId")String userId){
        ApiResultBean res = new ApiResultBean();
        try{
           String validCode = this.apiActivityService.checkApplyAct(actId, userId);
            if(!"0".equals(validCode)){
                res.setCode(101);
                res.setMsg("培训已失效");
            }
        } catch (Exception e){
            log.error(e.getMessage(), e);
            res.setCode(105);
            res.setMsg("报名失败");
        }
        return res;
    }


    /**
     * 检查能否跳入预订页面
     * @param actId  活动Id
     * @param userId 用户Id
     * @return JSON: {
     * }
     */
    @CrossOrigin
    @RequestMapping(value = "/checkPreAct", method = RequestMethod.POST)
    public ApiResultBean checkAct(HttpServletRequest request, String userId, String actId) {
        ApiResultBean res = new ApiResultBean();
        if (null == userId || null == actId) {
            res.setCode(101);
            res.setMsg("获取活动信息失败");
            return res;
        }
        try {
            Map map = apiActivityService.checkPreActBook(actId, userId);
            if (map.get("code") != null && !map.get("code").equals("0")) {
                res.setCode((Integer) map.get("code"));
                res.setMsg((String) map.get("msg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * 活动预定验证
     * @param request
     * @param actId
     * @param seatStr
     * @param eventId
     * @param seats
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/checkActPublish", method = RequestMethod.POST)
    public ApiResultBean checkActPublish(HttpServletRequest request, String userId, String actId, String seatStr, String eventId,
                                         @RequestParam(value = "seats",required = false, defaultValue = "1") Integer seats) {
        ApiResultBean res = new ApiResultBean();
        String zcId=getParam(request,"zcId",null);//系统类型 众筹系统，总票数以其众筹数为主
        try {
            eventId = eventId == null ? "false": eventId;
            Map<String,Object> map = apiActivityService.checkActOrder(actId, eventId,zcId, userId, seatStr, seats);
            if(Integer.parseInt(String.valueOf(map.get("code"))) != 0){
                res.setCode(Integer.parseInt(String.valueOf(map.get("code"))));
                res.setMsg(String.valueOf(map.get("msg")));
            }else{
                res.setCode(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(res.getCode() == null){
            res.setCode(0);
        }
        return res;

    }

    /**
     * 检查验证码
     * @param request
     * @param checkCode
     * @param session
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/checkCode", method = RequestMethod.POST)
    public ApiResultBean checkCode(HttpServletRequest request, String checkCode,HttpSession session) {
        ApiResultBean res = new ApiResultBean();
        if (!checkCode.equalsIgnoreCase((String) session.getAttribute("randCheckCode"))) {
            res.setCode(1);
            res.setMsg("验证码有误！");
        }else{
            res.setCode(0);
        }
        return res;
    }

    /**
     * 预定活动
     * 访问路径 /api/act/bookingsave
     * @param actId  活动Id
     * @param eventId  订单信息 可参考whg_act_order(场次、预定人姓名、预定人手机号码)，POST的数据为此表的字段小写
     * @param userId  用户Id
     * @param seatStr  在线选座 座位编号：座位1,座位2
     * @param seats  自由选座 座位数
     * @return JSON : {
     * "success" : "1"        //1表示报名成功，其它失败
     * "errormsg" : "105"     //101-活动Id不允许为空;102-活动场次Id不允许为空;103-用户Id不允许为空;104-座位数必须大于0;105-报名失败
     * }
     */

    @SuppressWarnings("unused")
    @CrossOrigin
    @RequestMapping(value = "/bookingsave", method = RequestMethod.POST)
    public ApiResultBean bookingsave(String actId,String eventId,String zcId, String userId,String orderPhoneNo,String seatStr,int seats,String name ){
        ApiResultBean res = new ApiResultBean();
        WhgActivity whgActActivity = apiActivityService.getActDetail(actId);
        try {
            Map<String,Object> map = apiActivityService.checkActOrder(actId, eventId,zcId, userId, seatStr, seats);
            if(Integer.parseInt(String.valueOf(map.get("code"))) != 0){
                res.setCode(Integer.parseInt(String.valueOf(map.get("code"))));
                res.setMsg(String.valueOf(map.get("msg")));
            }else{
                res.setCode(0);
                String id = IDUtils.getID();
                apiActivityService.saveActOrder(id,actId, eventId, userId, orderPhoneNo, seatStr, seats, name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    /**
     * 查询活动列表
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ApiResultBean actList(HttpServletRequest request,WebRequest webRequest){
        ApiResultBean apiResultBean = new ApiResultBean();
        String type = getParam(request,"type",null);//活动类型
        String keywords = getParam(request,"keywords",null);//关键字
        String district = getParam(request,"district",null);//区域
        String province= getParam(request,"province",null);
        String city= getParam(request,"city",null);
        String area= getParam(request,"area",null);
        String sdate = getParam(request,"sdate",null);//排序值
        String index = getParam(request,"index","1");//pageNo
        String size = getParam(request,"size","12");//pageSize
        String sort = getParam(request,"sort","1");//排序
        String cultid = getParam(request,"cultid",null);//文化馆id
        String deptid = getParam(request,"deptid",null);//部门id
        String isrecommend = getParam(request,"isrecommend",null);//是否推荐
        String arttype = getParam(request, "arttype", null);
        String name = getParam(request, "name", null);
        Map param = new HashMap();

        //区分全省、全市站
        if (cultid == null || cultid.isEmpty()){
            param.put("allprovince", true);
        }
        if (cultid != null && cultid.contains(",")){
            param.put("allcity", true);
        }

        if(null != cultid){
            param.put("cultids", Arrays.asList(cultid.replaceAll(","," ").trim().split(" ")));
        }
        if(null != type){
            param.put("etype", type);
        }
        if(null != province){
            param.put("province", province);
        }
        if(null != city){
            param.put("city", city);
        }
        if(null != deptid){
            param.put("deptids", deptService.srchDeptStrList(deptid));
        }
        if(null != area){
            param.put("area", area);
        }
        if(null != district){
            param.put("areaid", district);
        }
        if(null != sdate){
            param.put("sdate", sdate);
        }
        if(null != keywords){
            param.put("name", keywords);
        }
        if(null != sort){
            param.put("sort", sort);
        }
        if(null != isrecommend){
            param.put("isrecommend", isrecommend);
        }
        if (null != arttype && !arttype.isEmpty()){
            param.put("arttype", arttype);
        }
        if (null != name && !name.isEmpty()){
            param.put("name", name);
        }
        PageInfo pageInfo = apiActivityService.getActListForActFrontPage(index,size,param,EnumProject.PROJECT_WBGX.getValue());
        if(null == pageInfo){
            apiResultBean.setCode(1);
            apiResultBean.setMsg("获取活动列表失败");
            return apiResultBean;
        }
        List<Map<String,Object>> list = pageInfo.getList();
        List actList = apiActivityService.judgeCanSign(list);
        apiResultBean.setPageInfo(pageInfo);
        apiResultBean.setRows(actList);
        return apiResultBean;
    }

    /**
     * 查询活动详情
     * @param actId
     * @param request
     * @return
     * @throws Exception
     * [{value: '110000',text: '北京市', children: [{value: '110101',text: '东城区' },{value: '2',text: '区' }]}]
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @CrossOrigin
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ApiResultBean  detail(String actId,String cultid,String userId,WebRequest request ){
        ApiResultBean  res = new ApiResultBean ();
        Map<String,Object> param = new HashMap<>();
        try {
            if(actId ==null || "".equals(actId)){
                res.setCode(101);
                res.setMsg("活动Id不允许为空");//活动Id不允许为空
            }else{

                //活动详情
               WhgActivity actdetail1 = this.apiActivityService.getActDetail(actId);
               actdetail1 = FilterFontUtil.clearFont(actdetail1);
                //判断活动报名时间
                Date enterstrtime = actdetail1.getEnterstrtime();
                Date enterendtime = actdetail1.getEnterendtime();
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(actdetail1));
                //添加主办方-没有值的时候取所属文化馆
                String host = actdetail1.getHost();
                if(StringUtils.isEmpty(host) && StringUtils.isNotEmpty(actdetail1.getCultid())){
                    WhgSysCult thisCult = this.whgSystemCultService.t_srchOne(actdetail1.getCultid());
                    host = thisCult.getName();
                }
                jsonObject.put("host", host);

                Integer canSign = apiActivityService.canSign(actdetail1,enterstrtime,enterendtime);
                int actCounts = apiActivityService.getActTicketList(actId);//该活动总票数
                int ticketCounts = apiActivityService.getWhgActTicketCountsActId(actId);
                param.put("ticketnum", actCounts - ticketCounts);
                param.put("ticketAllnum", actCounts);
                if(canSign==0){//可报名:报名时间内
                    if(actdetail1.getSellticket()==1){
                        param.put("liststate",3);    //直接前往：在报名时间内 又是不可预订类型 就直接前往
                    }else{
                        if (actCounts - ticketCounts <= 0) {
                            param.put("liststate",2);//已订完
                        }else{
                            param.put("liststate",1);
                        }
                    }
                }else if(canSign==104){//即将开始：还未到报名时间
                    param.put("liststate",5);
                }else if(canSign==105){//可报
                    param.put("liststate",4);//已结束
                }
                List _list = new ArrayList();
                if(null != cultid && !cultid.isEmpty()){
                    _list = Arrays.asList(cultid.replaceAll(","," ").trim().split(" "));
                }
                List<WhgActivity> acttj = this.apiActivityService.acttjianfortz(actId,_list);
                //活动场次信息
                List<String> dateList = apiActivityService.getActDate(actId);
                List myTimeList= apiActivityService.getTimesAct(dateList,actId);
                WhgGather whgGather = whgGatherService.getGatherByRefId(actId);
                if(whgGather!=null){
                    param.put("gatherid", whgGather.getId());
                }
                //活动直播数据
                WhgLiveComm live = this.myLiveService.t_srchOneByActid(actId);// "2018010810624793" actId
                Map<String, String> liveMap = this.myLiveService.findActOrTraLiveInfo(live);
                param.put("liveInfo", liveMap);

                param.put("timeList", myTimeList);
                param.put("actdetail", jsonObject);
                param.put("acttj", acttj);
                param.put("date", new Date());
                res.setData(param);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return res;
    }

    @CrossOrigin
    @RequestMapping(value = "/allEvents", method = RequestMethod.POST)
    public ApiResultBean getTimesAct(String actId) {
        ApiResultBean  res = new ApiResultBean ();
        //活动场次信息
        List<String> dateList = apiActivityService.getActDate(actId);
        try {
            List myTimeList = apiActivityService.getTimesAct(dateList, actId);
            res.setRows(myTimeList);
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return  res;
    }

    /***
     * 获取我的活动
     * @return
     */
    @SuppressWarnings("unused")
    @CrossOrigin
    @RequestMapping(value = "/getMyActList",method = RequestMethod.POST)
    public ApiResultBean activity(int index,int size,String type,String userId){
        ApiResultBean rme = new ApiResultBean();
        if(null == userId){
            rme.setCode(101);
            rme.setMsg("获取用户活动订单失败");
            return rme;
        }
        PageInfo pageInfo = this.apiActivityService.getOrderForCenter(index,size,Integer.valueOf(type),userId);
        Map<String,Object> param = new HashMap<>();
        rme.setPageInfo(pageInfo);
        rme.setRows(pageInfo.getList());
        param.put("time", new Date());
        rme.setData(param);
        return rme;
    }

    /**
     * 删除个人中心活动订单
     * @param orderId 活动订单id
     * @return res
     */
    @CrossOrigin
    @RequestMapping(value = "/delMyAct",method = RequestMethod.POST)
    public ApiResultBean delMyAct(String orderId){
        ApiResultBean rme = new ApiResultBean();
        rme.setCode(apiActivityService.delMyAct(orderId));
        if(0 != rme.getCode()){
            rme.setMsg("删除失败");
        }
        return rme;
    }




    /**
     * 查询座位信息
     * @param actId
     * @param userId
     * @param eventId
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/findSeat4ActId", method = RequestMethod.POST)
    public ApiResultBean findSeat4ActId(String actId,String userId,String eventId,WebRequest request){
        ApiResultBean res = new ApiResultBean();
        /**
         * 判断参数有效性
         */
        if(null == actId || null == userId || null == eventId){
            res.setCode(1);
            res.setMsg("参数不全");
            return res;
        }
        List<WhgActivitySeat> whgActSeatList = apiActivityService.getWhgActSeatListByActId(actId);
        if(null == whgActSeatList){
            res.setCode(1);
            res.setMsg("获取活动座位信息失败");
            return res;
        }
        List statusMap = apiActivityService.createStatusMap(whgActSeatList);//座位模板
        WhgActivityTime whgActTime = apiActivityService.getActTime(eventId);//场次信息
        WhgActivity whgActActivity = apiActivityService.getActDetail(actId);//活动信息
        List<WhgActivityTicket> whgActTicketList = apiActivityService.getWhgActTicket(actId, eventId);//本场 有效票务
        List mapType = apiActivityService.getMapType(statusMap, whgActSeatList, whgActTicketList);//活动 座位占用情况
        int[] count = apiActivityService.getSeatSize(mapType);
        int ticketCount = 0;
        List<WhgActivityTicket> whgActTicketList1 = apiActivityService.getUserTicket(userId,actId,eventId);
        ticketCount = (null != whgActTicketList1?whgActTicketList1.size():0);
        Map map = new HashMap();
        map.put("mapType",mapType);
        map.put("statusMap", statusMap);
        map.put("seatSize", count[0]);//已占用座位数
        map.put("totalSeatSize", whgActTime.getSeats());//count[1] 为所有座位数总数，但由于场次座位数 可以修改 那么这里就以场次座位数为主==场次座位数
        map.put("seatSizeUser", ticketCount);//该活动下的本场次 当前用户已经预订数
        map.put("act", whgActActivity);
        map.put("actTime", whgActTime);
        if (2 == whgActActivity.getSellticket()) {
            map.put("seatSize", whgActTicketList.size());//已卖票数
            map.put("totalSeatSize", whgActTime.getSeats());//总票数
        }
        res.setData(map);
        return res;
    }

    

    private String getParam(HttpServletRequest request,String paramName,String defaultValue){
        String value = request.getParameter(paramName);
        if(null == value || value.trim().isEmpty()){
            return defaultValue;
        }
        return value;
    }

    /**
     * 获取资源
     * @param id 活动id
     * @param reftype 类型
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @CrossOrigin
    @RequestMapping(value = "/resource", method = RequestMethod.POST)
    public ApiResultBean loadResource(String id, String reftype) {
        ApiResultBean res = new ApiResultBean();
        Map<String, Object> rest = null;
        try {
            rest = new HashMap();
            if (id != null && !"".equals(id) && reftype != null && !"".equals(reftype)) {
                //活动图片
                List<WhgResource> tsource = this.whgResourceService.selectactSource(id, "1", reftype);
                //活动资源 音频
                List<WhgResource> ysource = this.whgResourceService.selectactSource(id, "3", reftype);
                //活动资源 视频
                List<WhgResource> ssource = this.whgResourceService.selectactSource(id, "2", reftype);
                rest.put("tsource", tsource);
                rest.put("tsource", tsource);
                rest.put("ysource", ysource);
                rest.put("ssource", ssource);
                rest.put("code", 0);
            }else {
                res.setMsg("参数错误");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        res.setData(rest);
        return res;
    }

}
