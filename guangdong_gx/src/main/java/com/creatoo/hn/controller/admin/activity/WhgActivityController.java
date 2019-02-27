package com.creatoo.hn.controller.admin.activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.admin.activity.*;
import com.creatoo.hn.services.admin.xj.WhgXjReasonService;
import com.creatoo.hn.services.api.apiinside.InsMessageService;
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.util.CommUtil;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.creatoo.hn.util.enums.EnumProject;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;


/**
 * 活动控制层
 * @author heyi
 *
 */
@Controller
@RequestMapping("/admin/activity")
public class WhgActivityController extends BaseController {

    /**
     * 活动服务类
     */
    @Autowired
    private WhgActivityService service;

    /**
     * 活动场次服务类
     */
    @Autowired
    private WhgActivityPlayService whgActivityPlayService;

    @Autowired
    private WhgActivitySeatService whgActivitySeatService;

    /**
     * 活动时间服务类
     */
    @Autowired
    private WhgActivityTimeService whgActivityTimeService;

    @Autowired
    private WhgActivityOrderService whgActivityOrderService;

    @Autowired
    private SMSService smsService;

    @Autowired
    private InsMessageService insMessageService;

    @Autowired
    private WhgXjReasonService whgXjReasonService;


    /**
     * 进入type(list|add|edit|view)视图
     * @param request 请求对象
     * @param type 视图类型(list|add|edit|view)
     * @return
     */
    @RequestMapping("/view/{type}")
    @WhgOPT(optType = EnumOptType.ACT, optDesc = {"访问活动列表页", "访问活动添加页", "访问活动编辑页"}, valid = {"type=list", "type=add", "type=edit"})
    public String view(HttpServletRequest request, ModelMap mmp, @PathVariable("type")String type){
        //模块页面前缀
        String view = "admin/activity/act/";
        try {
            mmp.addAttribute("viewType", type); //页面类型参数
            if ("add".equalsIgnoreCase(type)){
                view+="view_add";
            }else if("edit".equalsIgnoreCase(type)){
                String id = request.getParameter("id");
                String pageType = request.getParameter("pageType");
                String onlyShow = request.getParameter("onlyshow");
                WhgActivity act = service.t_srchOne(id);
                mmp.addAttribute("act", act);
                if (act != null && (act.getState() == 4 || act.getState() == 6)) {
                    mmp.addAttribute("NotEdit", "true");
                }
                if (pageType != null) {
                    mmp.addAttribute("pageType", pageType);
                }
                mmp.addAttribute("actSeatList",whgActivityPlayService.srchList4actId(id));
                List list = whgActivityTimeService.findActTimeListActId(id);
                if (list != null && list.size() > 0) {
                    mmp.addAttribute("timeList", JSON.toJSONString(list));
                }
                JSONObject seatMap = whgActivitySeatService.getActivitySeatInfo(id);
                mmp.addAttribute("whgSeat",seatMap);
                view += "view_edit";
            }else if("orderList".equalsIgnoreCase(type)){
                String id = request.getParameter("id");
                mmp.addAttribute("act", service.t_srchOne(id));
                view+="view_order_list";
            } else if("screenings".equalsIgnoreCase(type)){
                String id = request.getParameter("id");
                WhgActivity act = service.t_srchOne(id);
                WhgActivityTime WhgActivityTime = whgActivityTimeService.findWhgActivityTime4ActId(id);
                mmp.addAttribute("seats",WhgActivityTime.getSeats());
                mmp.addAttribute("act", act);
                view+="view_screenings";
            } else if ("list2".equalsIgnoreCase(type)) {
                view += "view_list2";
            }else{
                view+="view_list";
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        return view;
    }

    /**
     * 分页查询
     * @return
     */
/*   @SuppressWarnings({ "rawtypes", "unchecked" })*/
    @RequestMapping(value = "/srchList4p")
    @ResponseBody
    public Object srchList4p(int page, int rows, HttpServletRequest request){
        ResponseBean res = new ResponseBean();
        WhgSysUser whgSysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        try {
            Map<String, String[]> pmap = request.getParameterMap();
            Map param = new HashMap();
            for (Map.Entry<String, String[]> ent : pmap.entrySet()) {
                if (ent.getValue().length == 1 && ent.getValue()[0] != null && !ent.getValue()[0].isEmpty()) {
                    param.put(ent.getKey(), ent.getValue()[0]);
                }
                if (ent.getValue().length > 1) {
                    param.put(ent.getKey(), Arrays.asList(ent.getValue()));
                }
            }
            String pageType = request.getParameter("__pageType");
            String cultName = request.getParameter("cultname");
            String cultlevel = request.getParameter("cultlevel");
            //编辑列表，查 1可编辑 5已撤消
            if ("editList".equalsIgnoreCase(pageType)){
                //param.put("states", Arrays.asList(1,5) );
                param.put("crtuser", whgSysUser.getId());
            } else if ("list2".equalsIgnoreCase(pageType)) {
                param.put("states", Arrays.asList(6, 4));
            } else {
                if (param.get("state") == null || param.get("state") == "") {
                    param.put("states", Arrays.asList(9, 2, 6, 4));
                }
            }
            if (cultName != null) {
                param.put("cultname", cultName);
            }
            if (cultlevel != null) {
                if (cultlevel.equals("1")) {//省级 查询 省 市 区
                    param.put("cultlevels", Arrays.asList(1, 2, 3));
                } else if (cultlevel.equals("2")) {//市级 查询 市 区
                    param.put("cultlevels", Arrays.asList(2, 3));
                } else {
                    param.put("cultlevels", Arrays.asList(3));
                }
            }
            //删除列表
            if("del".equalsIgnoreCase(pageType)){
                param.put("delstate", 1);
            }
            PageInfo pageInfo = this.service.srchList4p(page, rows, param,whgSysUser);
            res.setRows( (List)pageInfo.getList() );
            res.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.debug("活动查询失败", e);
            res.setRows(new ArrayList());
            res.setSuccess(ResponseBean.FAIL);
        }
        return res;
    }

    /**
     * 添加场次信息
     * @param request
     * @param actId
     * @return
     */
    @RequestMapping(value = "/addScreenings")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.ACT, optDesc = {"添加场次"})
    public Object addScreenings(HttpServletRequest request, String actId){
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ResponseBean res = new ResponseBean();
        WhgActivityTime WhgActivityTime = new WhgActivityTime();
        Map<String,String> paramMap = CommUtil.getRequestParamByClass(request,WhgActivityTime.getClass());
        try{
            WhgActivity act = service.t_srchOne(actId);
            String playdate = paramMap.get("playdate");
            String playstime = paramMap.get("playstime");
            String playetime = paramMap.get("playetime");
            String optstimestr = playdate + " " + playstime;
            String optetimestr = playdate + " " + playetime;
            Date playDate = sdfDateTime.parse(paramMap.get("playdate")+ " " + "00:00:00");
            Date startDate = act.getStarttime();//活动开始时间
            Date endDate = act.getEndtime();//活动结束时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            Date optstarttime = sdfDateTime.parse(paramMap.get("playdate") + " " + playstime);
            Date optendtime = sdfDateTime.parse(paramMap.get("playdate") + " " + playetime);
            Date nowDate = df.parse(df.format(new Date()));// new Date()为获取当前系统时间
            Date strDate = df.parse(df.format(act.getEnterstrtime()));
            Date strendDate = df.parse(df.format(act.getEnterendtime()));
            PageInfo info = whgActivityTimeService.getActTimes(actId, optstimestr, optetimestr, playdate, null);
            if (info.getList().size() > 0) {
                res.setSuccess(ResponseBean.FAIL);
                res.setErrormsg("新增场次不能与该活动其他场次时间重合!");
                return res;
            }
            if (playDate.getTime() < strendDate.getTime()) {
                res.setSuccess(ResponseBean.FAIL);
                res.setErrormsg("新增场次活动日期不能小于活动预订结束时间!");
                return res;
            }
            if(strDate.getTime() < playDate.getTime() && playDate.getTime() > nowDate.getTime()  ){
                WhgActivityTime.setId(IDUtils.getID());
                WhgActivityTime.setActid(actId);
                WhgActivityTime.setPlaydate(sdf.parse(paramMap.get("playdate")));
                WhgActivityTime.setPlaystime(paramMap.get("playstime"));
                WhgActivityTime.setPlayetime(paramMap.get("playetime"));
                WhgActivityTime.setSeats(Integer.valueOf(paramMap.get("seats")));
                Date playStartTime = sdfDateTime.parse(paramMap.get("playdate") + " " + paramMap.get("playstime"));
                Date playEndTime = sdfDateTime.parse(paramMap.get("playdate") + " " + paramMap.get("playetime"));
                WhgActivityTime.setPlaystarttime(playStartTime);
                WhgActivityTime.setPlayendtime(playEndTime);
                WhgActivityTime.setState(1);
                whgActivityTimeService.addOne(WhgActivityTime);
                //判断新增的场次时间是否小于活动开始时间
                if(playDate.getTime() < startDate.getTime()){
                    act.setStarttime(sdf.parse(paramMap.get("playdate")));
                }
                //判断新增的场次时间是否大于活动结束时间
                if(playDate.getTime() > endDate.getTime() ){
                    act.setEndtime(sdf.parse(paramMap.get("playdate")));
                }
                WhgSysUser whgSysUser =  (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
                service.t_edit(act, whgSysUser);
            }else{
                res.setSuccess(ResponseBean.FAIL);
                res.setErrormsg("新增日期需大于报名开始时间且大于当前时间!");
            }
        }catch (Exception e){
            log.error(e.toString());
            res.setErrormsg("修改场次信息失败");
            res.setSuccess(ResponseBean.FAIL);
        }
        return res;
    }


    @RequestMapping(value = "/updateScreenings")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.ACT, optDesc = {"修改场次"})
    public Object updateScreenings(HttpServletRequest request) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        ResponseBean res = new ResponseBean();
        WhgActivityTime whgActivityTime = new WhgActivityTime();
        Map<String, String> paramMap = CommUtil.getRequestParamByClass(request, whgActivityTime.getClass());
        String actId = null;
        String playdate = paramMap.get("playdate");
        String playstime = paramMap.get("playstime");
        String playetime = paramMap.get("playetime");
        String optstimestr = playdate + " " + playstime;
        String optetimestr = playdate + " " + playetime;
        int state = Integer.valueOf(paramMap.get("state")); // 0 取消  1  启动
        try {

            Date optstarttime = sdfDateTime.parse(paramMap.get("playdate") + " " + playstime);
            Date optendtime = sdfDateTime.parse(paramMap.get("playdate") + " " + playetime);
            whgActivityTime = this.whgActivityTimeService.findWhgActivityTime4Id(paramMap.get("id"));
            actId = whgActivityTime.getActid();
            List<WhgActivityOrder> actOrderList = whgActivityOrderService.findWhgActivityOrder4EventId(paramMap.get("id"));
            if (state != 0) {
                WhgActivity act = service.t_srchOne(actId);
                Date strendDate = sdf1.parse(sdf1.format(act.getEnterendtime()));
                Date startDate = sdf1.parse(playdate);
                PageInfo info = whgActivityTimeService.getActTimes(actId, optstimestr, optetimestr, playdate, paramMap.get("id"));
                if (info.getList().size() > 0) {
                    res.setSuccess(ResponseBean.FAIL);
                    res.setErrormsg("修改场次不能与该活动其他场次时间重合!");
                    return res;
                }
                if (startDate.getTime() < strendDate.getTime()) {
                    res.setSuccess(ResponseBean.FAIL);
                    res.setErrormsg("修改场次活动日期不能小于活动预订结束时间!");
                    return res;
                }
            }
            if (actOrderList.size() < 1) {
                whgActivityTime.setPlaydate(sdf1.parse(paramMap.get("playdate")));
                Date playDate = sdfDateTime.parse(paramMap.get("playdate") + " " + "00:00:00");
                whgActivityTime.setPlaystime(paramMap.get("playstime"));
                whgActivityTime.setPlayetime(paramMap.get("playetime"));
                Date playStartTime = sdfDateTime.parse(paramMap.get("playdate") + " " + paramMap.get("playstime"));
                Date playEndTime = sdfDateTime.parse(paramMap.get("playdate") + " " + paramMap.get("playetime"));
                whgActivityTime.setSeats(Integer.valueOf(paramMap.get("seats")));
                whgActivityTime.setState(state);
                whgActivityTime.setPlaystarttime(playStartTime);
                whgActivityTime.setPlayendtime(playEndTime);
                whgActivityTimeService.updateOne(whgActivityTime);
            } else {
                res.setSuccess(ResponseBean.FAIL);
                res.setErrormsg("该场次已经产生订单，不允许修改!");
            }
        } catch (Exception e) {
            log.error(e.toString());
            res.setErrormsg("修改场次信息失败");
            res.setSuccess(ResponseBean.FAIL);
        }
        return res;
    }



    /**
     * 查询列表
     *  @param request 请求实体
     * @return 对象列表
     */
    @RequestMapping(value = "/getScreenings")
    @ResponseBody
    public Object getScreenings(HttpServletRequest request){
        ResponseBean res = new ResponseBean();
        try {
            String pageNo = request.getParameter("page");
            String pageSize = request.getParameter("rows");
            String activityId = request.getParameter("activityId");
            if(null == pageNo || !CommUtil.isInteger(pageNo)){
                pageNo = "1";
            }
            if(null == pageSize || !CommUtil.isInteger(pageSize)){
                pageSize = "10";
            }
            if(null == activityId){
                res.setSuccess(ResponseBean.FAIL);
                res.setErrormsg("activityId can't be null");
            }else{
                WhgActivityTime whgActivityTime = new WhgActivityTime();
                whgActivityTime.setActid(activityId);
                PageInfo pageInfo = whgActivityTimeService.getActivityScreenings(Integer.valueOf(pageNo), Integer.valueOf(pageSize), whgActivityTime);
                res.setRows((List)pageInfo.getList());
                res.setTotal(pageInfo.getTotal());
            }
        }catch (Exception e){
            log.error(e.toString());
        }
        return  res;
    }

    /**
     * 获取活动订单
     * @param request
     * @return
     */
    @RequestMapping(value = "/getActOrder")
    @ResponseBody
    public Object getActOrder(HttpServletRequest request){
        ResponseBean res = new ResponseBean();
        Map map=new HashMap();
        try{
            String activityId = request.getParameter("activityId");
            String pageNo = request.getParameter("page");
            String pageSize = request.getParameter("rows");
            String orderphoneno = request.getParameter("orderphoneno");
            String ticketstatus = request.getParameter("ticketstatus");
            String name = request.getParameter("name");
            if(null == pageNo || !CommUtil.isInteger(pageNo)){
                pageNo = "1";
            }
            if(null == pageSize || !CommUtil.isInteger(pageSize)){
                pageSize = "10";
            }
            if(orderphoneno!=null&&!orderphoneno.equals("")){
                map.put("orderphoneno", orderphoneno);
            }
            if(ticketstatus!=null&&!ticketstatus.equals("")){
                map.put("ticketstatus", ticketstatus);
            }
            if(name!=null&&!name.equals("")){
                map.put("name", name);
            }
            if(activityId!=null&&!activityId.equals("")){
                map.put("activityId", activityId);
            }
            PageInfo pageInfo = service.getActOrderForBackManager(map,Integer.valueOf(pageNo),Integer.valueOf(pageSize));
            res.setRows((List)pageInfo.getList());
            res.setTotal(pageInfo.getTotal());
        }catch (Exception e){
            log.error(e.toString());
        }
        return res;
    }

    /**
     * 查询列表
     * @param request 请求对象
     * @param act 条件对象
     * @return 对象列表
     */
    @RequestMapping(value = "/srchList")
    @ResponseBody
    public List<WhgActivity> srchList(HttpServletRequest request, WhgActivity act){
        List<WhgActivity> resList = new ArrayList<WhgActivity>();
        WhgSysUser whgSysUser =  (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        try {
            resList = service.t_srchActList(request, act);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return resList;
    }

    /**
     * 查询详情
     * @param request 请求对象
     * @param id 标识
     * @return 详情资料
     */
    @RequestMapping(value = "/srchOne")
    @ResponseBody
    public ResponseBean srchOne(HttpServletRequest request, String id){
        ResponseBean res = new ResponseBean();
        try {
            WhgActivity act = service.t_srchOne(id);
            res.setData(act);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 添加
     * @param request 请求对象
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.ACT, optDesc = {"添加活动"})
    public ResponseBean add(HttpServletRequest request, WhgActivity act){
        ResponseBean res = new ResponseBean();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        List<Map<String, String>> timePlayList = null;
        try {
            String seatJson = request.getParameter("seatjson");
            String timeJson = request.getParameter("activityTimeList");
            WhgSysUser sysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
            //添加活动
            act = service.t_add(act,null,sysUser);
            if (timeJson != null && timeJson != "") {
                //添加时间段模板
                timePlayList = whgActivityPlayService.setTimeTemp(timeJson);
            }
            //如果是在线选座，需要添加活动座位信息
            int ticketnum = 0;//
            if (seatJson != null && act.getSellticket()!=null &&act.getSellticket()==3) {
                List<Map<String, String>> seatList = whgActivitySeatService.setSeatList(seatJson);
                whgActivitySeatService.t_add(seatList, (WhgSysUser)request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY), act.getId());
                for (Map<String, String> _set : seatList) {
                    if ("1".equals(_set.get("seatstatus"))) {
                        ticketnum++;
                    }
                }
            }
            Date time = act.getStarttime();
            Date endDate = act.getEndtime();
            if (ticketnum == 0) {
                if (act.getTicketnum() != null) {
                    ticketnum = act.getTicketnum();
                }
            }
            if (timePlayList != null) {
                if (ticketnum != 0 && time != null && endDate != null) {
                    while (!service.isAfter(time, endDate)) {
                        whgActivityTimeService.t_add(timePlayList, act.getId(), time, ticketnum);
                        time = formatter.parse(service.getTomorrow(time));
                    }
                }
            }
            //res.setData(act);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }


    /**
     * 编辑
     * @param request 请求对象
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/edit")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.ACT, optDesc = {"编辑活动"})
    public ResponseBean edit(HttpServletRequest request,WhgActivity act){
        ResponseBean res = new ResponseBean();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        List<Map<String, String>> timePlayList = null;
        try {
            String seatJson = request.getParameter("seatjson");
            String timeJson = request.getParameter("activityTimeList");
            WhgSysUser whgSysUser =  (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
            if(null == whgSysUser){
                res.setSuccess(ResponseBean.FAIL);
                res.setErrormsg("未登录");
            }else {
                log.info(JSON.toJSONString(act));
                service.t_edit(act, whgSysUser);
                //如果是在线选座，需要添加活动座位信息
                int ticketnum = 0;//
                if (seatJson != null && act.getSellticket() != null && act.getSellticket() == 3) {
                    //删除座位信息
                    whgActivitySeatService.delActSeat4ActId(act.getId());
                    List<Map<String, String>> seatList = whgActivitySeatService.setSeatList(seatJson);
                    whgActivitySeatService.t_add(seatList, whgSysUser, act.getId());
                    for (Map<String, String> _set : seatList) {
                        if ("1".equals(_set.get("seatstatus"))) {
                            ticketnum++;
                        }
                    }
                }
                if (timeJson == null || timeJson.equals("")) {
                    timeJson = whgActivityTimeService.getTimeByAct(act.getId()); //数据查询场次信息
                }
                if (timeJson != null && timeJson != "") {//编辑的时候 不能编辑场次信息 不然 与场次管理 会有冲突 故此屏蔽此功能
                    WhgActivityTime actTime = new WhgActivityTime();
                    actTime.setActid(act.getId());
                    whgActivityTimeService.t_del(actTime); //删除场次信息
                    //添加时间段模板
                    timePlayList = whgActivityPlayService.setTimeTemp(timeJson);
                }
                Date time = act.getStarttime();
                Date endDate = act.getEndtime();
                if (ticketnum == 0) {
                    if (act.getTicketnum() != null) {
                        ticketnum = act.getTicketnum();
                    }
                }
                if (timePlayList != null) {//场次不可修改 但是 座位数有可能更新
                    if (ticketnum != 0 && time != null && endDate != null) {
                        while (!service.isAfter(time, endDate)) {
                            whgActivityTimeService.t_add(timePlayList, act.getId(), time, ticketnum);
                            time = formatter.parse(service.getTomorrow(time));
                        }
                    }
                } else {
                    if (ticketnum != 0) {
                        WhgActivityTime actTime = new WhgActivityTime();
                        actTime.setSeats(ticketnum);
                        whgActivityTimeService.updateList(act.getId(), actTime);
                    }
                }
            }
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.toString());
        }
        return res;
    }

    /**
     * 删除
     * @param request 请求对象
     * @param ids 用逗号分隔的多个ID
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/del")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.ACT, optDesc = {"删除活动"})
    public ResponseBean del(HttpServletRequest request, String ids, String delStatus){
        ResponseBean res = new ResponseBean();
        try {
            service.t_delstate(ids, delStatus, (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY));
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 删除
     *
     * @param request 请求对象
     * @param ids     用逗号分隔的多个ID
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/recovery")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.ACT, optDesc = {"回收活动"})
    public ResponseBean recovery(HttpServletRequest request, String ids, String delStatus) {
        ResponseBean res = new ResponseBean();
        try {
            service.t_updDelstate(ids, delStatus, (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY));
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 修改状态
     * @param request 请求对象
     * @param ids 用逗号分隔的多个ID
     * @param fromState 修改之前的状态
     * @param toState 修改后的状态
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/updstate")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.ACT, optDesc = {"送审","审核","打回","发布","取消发布"}, valid ={"state=9","state=2","state=1","state=6","state=4"})
    public ResponseBean updstate(String statemdfdate, HttpServletRequest request, String ids, String fromState, String toState, String publishdate) {
        ResponseBean res = new ResponseBean();
        String reason = request.getParameter("reason");
        String issms = request.getParameter("issms");
        WhgSysUser sysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        try {
            service.t_updstate(statemdfdate, ids, fromState, toState, sysUser, publishdate);
            if (reason != null && ids != null) {// 下架原因
                WhgActivity activity = service.t_srchOne(ids);
                WhgXjReason whgReason = new WhgXjReason();
                whgReason.setFkid(ids);
                if (activity != null && activity.getPublisher() != null) {
                    whgReason.setTouser(activity.getPublisher());
                }
                if (activity != null && activity.getName() != null) {
                    whgReason.setFktitile(activity.getName());
                }
                if (issms != null) {//是否发送短信
                    whgReason.setIssms(Integer.parseInt(issms));
                }
                whgReason.setCrtuser(sysUser.getId());
                whgReason.setReason(reason);
                whgReason.setFktype("活动");
                whgXjReasonService.t_add(whgReason);
            }
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 推荐状态修改
     * @param request 请求对象
     * @param ids 用逗号分隔的多个ID
     * @param fromState 修改之前的状态
     * @param toState 修改后的状态
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/updCommend")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.ACT, optDesc = {"推荐","取消推荐"}, valid ={"isrecommend=1","isrecommend=0"})
    public ResponseBean updCommend(HttpServletRequest request, String ids, String fromState, String toState){
        ResponseBean res = new ResponseBean();
        try {
            service.t_updCommend(ids, fromState, toState, (WhgSysUser)request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY));
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    @RequestMapping("setToprovince")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.ACT, optDesc = {"省级推荐","取消省级推荐"},valid = {"toprovince=1","toprovince=0"})
    public Object setToprovince(String id, int toprovince){
        ResponseBean rb = new ResponseBean();

        try {
            WhgActivity info = new WhgActivity();
            info.setId(id);
            info.setToprovince(toprovince);

            this.service.t_edit4Pk(info);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("设置省级推荐失败");
        }

        return rb;
    }

    @RequestMapping("setTocity")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.ACT, optDesc = {"市级推荐","取消市级推荐"},valid = {"tocity=1","tocity=0"})
    public Object setTocity(String id, int tocity){
        ResponseBean rb = new ResponseBean();

        try {
            WhgActivity info = new WhgActivity();
            info.setId(id);
            info.setTocity(tocity);

            this.service.t_edit4Pk(info);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("设置市级推荐失败");
        }

        return rb;
    }

    /**
     * 推荐上大首页状态修改
     * @param request 请求对象
     * @param ids 用逗号分隔的多个ID
     * @param fromState 修改之前的状态
     * @param toState 修改后的状态
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/updBanner")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.ACT, optDesc = {"推荐上大首页","取消推荐上大首页"}, valid ={"isbigbanner=1","isbigbanner=0"})
    public ResponseBean updBanner(HttpServletRequest request, String ids, String fromState, String toState){
        ResponseBean res = new ResponseBean();
        try {
            service.t_updBanner(ids, fromState, toState, (WhgSysUser)request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY));
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 还原删除状态
     * @param request 请求对象
     * @param ids 用逗号分隔的多个ID
     * @param delStatus 还原状态
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/reBack")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.ACT, optDesc = {"还原活动"})
    public ResponseBean reBack(HttpServletRequest request, String ids, String delStatus){
        ResponseBean res = new ResponseBean();
        try {
            service.reBack(ids, delStatus, (WhgSysUser)request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY));
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }


    /**
     * 订单重新发送短信
     * @param request
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/againSendSms")
    @ResponseBody
    public ResponseBean againSendSms(HttpServletRequest request, String orderId){
        ResponseBean res = new ResponseBean();
        try {
            WhgActivityOrder actOrder = whgActivityOrderService.findWhgActivityOrder4Id(orderId);
            WhgActivity WhgActivity = service.t_srchOne(actOrder.getActivityid());
            WhgActivityTime actTime = whgActivityTimeService.findWhgActivityTime4Id(actOrder.getEventid());
            //发送短信
            Map<String, String> smsData = new HashMap<String, String>();
            smsData.put("userName", actOrder.getOrdername());
            smsData.put("title", WhgActivity.getName());
            smsData.put("ticketCode", actOrder.getOrdernumber());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = actTime.getPlaydate();
            String dateStr = sdf.format(date);
            smsData.put("beginTime", dateStr +" "+ actTime.getPlaystime());
            int totalSeat = whgActivityOrderService.findWhgActivityTicket4OrderId(orderId);
            smsData.put("number", String.valueOf(totalSeat));
            if(WhgActivity.getBiz()!=null) {//众筹活动
                smsService.t_sendSMS(actOrder.getOrderphoneno(), "ZC_ACT_True", smsData, actOrder.getActivityid());
            }else{
                smsService.t_sendSMS(actOrder.getOrderphoneno(), "ACT_DUE", smsData, actOrder.getActivityid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    /**
     * 订单重新发送站内信
     * @param request
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/againSendZnx")
    @ResponseBody
    public ResponseBean againSendZnx(HttpServletRequest request, String orderId){
        ResponseBean res = new ResponseBean();
        WhgSysUser whgSysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        try {
            WhgActivityOrder actOrder = whgActivityOrderService.findWhgActivityOrder4Id(orderId);
            WhgActivity WhgActivity = service.t_srchOne(actOrder.getActivityid());
            WhgActivityTime actTime = whgActivityTimeService.findWhgActivityTime4Id(actOrder.getEventid());
            //发送短信
            Map<String, String> smsData = new HashMap<String, String>();
            smsData.put("userName", actOrder.getOrdername());
            smsData.put("title", WhgActivity.getName());
            smsData.put("ticketCode", actOrder.getOrdernumber());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = actTime.getPlaydate();
            String dateStr = sdf.format(date);
            smsData.put("beginTime", dateStr +" "+ actTime.getPlaystime());
            int totalSeat = whgActivityOrderService.findWhgActivityTicket4OrderId(orderId);
            smsData.put("number", String.valueOf(totalSeat));
            if(WhgActivity.getBiz()!=null) {//众筹活动
                insMessageService.t_sendZNX(actOrder.getUserid(),whgSysUser.getId(), "ZC_ACT_True", smsData, actOrder.getActivityid(), EnumProject.PROJECT_ZC.getValue());
            }else{
                insMessageService.t_sendZNX(actOrder.getUserid(),whgSysUser.getId(), "ACT_DUE", smsData, actOrder.getActivityid(), EnumProject.PROJECT_WBGX.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    /**
     * 取消订单
     * @return
     */
    @RequestMapping(value = "/cancelOrder")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.ACT, optDesc = {"取消订单"})
    public ResponseBean cancelOrder(HttpServletRequest request, String orderId){
        ResponseBean res = new ResponseBean();
        try {
            //更新订单信息
            int upCount = whgActivityOrderService.updateActOrder(orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }




    public static void main(String[] args) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date a = format.parse("2017-01-01");
        Date b = format.parse("2017-01-03");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(a);
        int strNum = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.setTime(b);
        int endNum = calendar.get(Calendar.DAY_OF_YEAR);
        System.out.println(endNum-strNum);
    }


}
