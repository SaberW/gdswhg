package com.creatoo.hn.controller.api.apioutside.user;

import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgActivityOrder;
import com.creatoo.hn.dao.model.WhgActivityTime;
import com.creatoo.hn.dao.model.WhgUser;
import com.creatoo.hn.dao.model.WhgUserWeixin;
import com.creatoo.hn.services.api.apioutside.activity.ApiActivityService;
import com.creatoo.hn.services.api.apioutside.user.ApiUserService;
import com.creatoo.hn.services.api.apioutside.venue.ApiVenueService;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rbg on 2017/8/16.
 */
@SuppressWarnings("ALL")
@CrossOrigin
@Controller
@RequestMapping("/api")
public class ApiUserCenterController extends BaseController {

    @Autowired
    private ApiUserService apiUserService;

    @Autowired
    private ApiVenueService apiVenueService;

    @Autowired
    private ApiActivityService apiActivityService;

    /**
     * 用户基本信息
     * @param userId
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/user/detail", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object userDetail(String userId){
        ApiResultBean arb = new ApiResultBean();
        try {
            Object user = this.apiUserService.findWhgUser(userId);
            arb.setData(user);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询信息失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    @CrossOrigin
    @RequestMapping(value = "/user/info", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object getUserInfo(String userId, String openid){
        ApiResultBean arb = new ApiResultBean();
        try {
            Object user = this.apiUserService.findUserInfo(userId);
            Map<String, String> userMap = BeanUtils.describe(user);
            WhgUserWeixin wxUser = this.apiUserService.findWxUserInfo(userId, openid);
            if(wxUser != null){
                userMap.put("headimgurl", wxUser.getHeadimgurl());
            }
            arb.setData(userMap);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询信息失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 编译用户资料
     * @param id
     * @param nickname
     * @param sex
     * @param job
     * @param nation
     * @param origo
     * @param company
     * @param address
     * @param resume
     * @param actbrief
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/user/edit", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object userEdit(@RequestParam(value = "id", required = true)String id,
                           @RequestParam(value = "nickname", required = true)String nickname,
                           @RequestParam(value = "sex", required = false, defaultValue = "1") String sex,
                           @RequestParam(value = "job", required = true)String job,
                           @RequestParam(value = "nation", required = false)String nation,
                           @RequestParam(value = "origo", required = false)String origo,
                           @RequestParam(value = "company", required = false)String company,
                           @RequestParam(value = "address", required = false)String address,
                           @RequestParam(value = "resume", required = false)String resume,
                           @RequestParam(value = "actbrief", required = false)String actbrief){
        ApiResultBean arb = new ApiResultBean();

        try {
            WhgUser user = new WhgUser();
            user.setId(id);
            user.setNickname(nickname);
            user.setSex(sex);
            user.setJob(job);
            user.setNation(nation);
            user.setOrigo(origo);
            user.setCompany(company);
            user.setResume(resume);
            user.setActbrief(actbrief);
            arb = (ApiResultBean) this.apiUserService.editUser(user, false);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("保存数据失败");
        }

        return arb;
    }

    @CrossOrigin
    @RequestMapping(value = "/user/editinfo", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object editUserInfo(WhgUser user,
                               @RequestParam(value = "notValid", defaultValue = "1", required = false) int notValid,
                               @DateTimeFormat(pattern = "yyyy-MM-dd") Date birthdayStr) {
        ApiResultBean arb = new ApiResultBean();
        try {
            if (birthdayStr != null) {
                user.setBirthday(birthdayStr);
            }
            boolean notvalid = (notValid==1);
            arb = (ApiResultBean) this.apiUserService.editUser(user, notvalid);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("保存数据失败");
        }

        return arb;
    }


    /**
     * 用户中心我的场馆预定列表
     * @param userId
     * @param nowtype
     * @param page
     * @param pageSize
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/user/venue", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object venue(String userId,
                        @RequestParam(value = "nowtype", required = false, defaultValue = "now") String nowtype,
                        @RequestParam(value = "page",required = false,defaultValue = "1") int page,
                        @RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize){
        ApiResultBean arb = new ApiResultBean();
        try {
            PageInfo pageInfo = this.apiVenueService.selectOrder4User(page, pageSize, userId, nowtype);

//            ApiPageBean apb = new ApiPageBean();
//            apb.setIndex(pageInfo.getPageNum());
//            apb.setCount(pageInfo.getSize());
//            apb.setSize(pageInfo.getPageSize());
//            apb.setTotal(pageInfo.getTotal());

            Map data = new HashMap();
//            data.put("list", pageInfo.getList());
//            data.put("pager", apb);

            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_YEAR, 2);
            data.put("twoDayAgo", c.getTime());

            arb.setData(data);

            arb.setPageInfo(pageInfo);
            arb.setRows(pageInfo.getList());
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(),e);
        }
        return arb;
    }

    /**
     * 取消订单
     * @param request
     * @param request
     * @return
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/usercenter/cancel",method = RequestMethod.POST)
    public ApiResultBean cancel(String itemId, String userId, String type){
        ApiResultBean rme = new ApiResultBean();
        //type 订单类型 1. 活动 2. 场馆活动室 3. 培训课程
        try {
            if(itemId == null ||"".equals(itemId) ){
                rme.setCode(101);
                rme.setMsg("订单ID不允许为空!");
                return rme;
            }
            if(type.equals("1")){
                WhgActivityOrder actOrder = apiActivityService.findOrderDetail(itemId);
                int canCancel = canActCancel(actOrder);
                if(1 == canCancel){
                    rme.setCode(101);
                    rme.setMsg("该订单不存在");
                    return rme;
                }else if(2 == canCancel){
                    rme.setCode(101);
                    rme.setMsg("该订单不存在");
                    return rme;
                }else if(3 == canCancel){
                    rme.setCode(101);
                    rme.setMsg("已经有验过的票，不能取消");
                    return rme;
                }else {
                    actOrder.setTicketstatus(3);
                    //actOrder.setOrderisvalid(2);
                    apiActivityService.upActOrder(actOrder);
                    rme.setCode(0);
                    rme.setMsg("活动订单取消成功！");
                }
            }else if(type.equals("2")){
                rme=(ApiResultBean)this.apiVenueService.unOrder(itemId,userId);
                rme.setMsg("活动室订单取消成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rme;

    }

    /**
     *  判断活动订单能否取消
     * @param actOrder
     * @return：0:可以取消；1:该订单不存在；2:活动已经不能取消；3:已经有人验票
     */
    private int canActCancel(WhgActivityOrder actOrder){
        if(null == actOrder){
            return 1;
        }
        WhgActivityTime whgActTime = apiActivityService.getActTimeInfo(actOrder.getEventid());
        if(null == whgActTime){
            return 1;
        }
        LocalDateTime today =  LocalDateTime.now();
        today.plusDays(2);
        LocalDateTime startTime = date2LocalDateTime(whgActTime.getPlaystarttime());
        if(startTime.isBefore(today)){
            return 2;
        }
        if(0 < apiActivityService.getActTicketChecked(actOrder.getId())){
            return 3;
        }
        return 0;
    }

    private LocalDateTime date2LocalDateTime(Date date){
        try {
            Instant instant = date.toInstant();
            ZoneId zone = ZoneId.systemDefault();
            return LocalDateTime.ofInstant(instant, zone);
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }


}
