package com.creatoo.hn.controller.api.apioutside.venue;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.services.admin.system.WhgSystemDeptService;
import com.creatoo.hn.services.api.apioutside.venue.ApiVenueService;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumProject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

/**
 * Created by rbg on 2017/8/7.
 */

@SuppressWarnings("ALL")
@Controller
@CrossOrigin
@RequestMapping("/api")
public class ApiVenueController extends BaseController {

    @Autowired
    private ApiVenueService apiVenueService;

    @Autowired
    private WhgSystemDeptService whgSystemDeptService;


    /**
     * 取首页显示的场馆列表
     * @param cultid
     * @param pageSize
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/venue/indexList", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object getVenList2Index(@RequestParam(value = "cultid", required = false) String cultid,
                                   @RequestParam(value = "deptid", required = false) String deptid,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize){
        ApiResultBean arb = new ApiResultBean();
        try {
            List cultids = null;
            if (cultid != null && !cultid.isEmpty()) {
                cultids = Arrays.asList(cultid.replaceAll(","," ").trim().split(" "));
            }
            List deptids = null;
            if (deptid != null && !deptid.isEmpty()) {
                deptids = this.whgSystemDeptService.srchDeptStrList(deptid);
            }
            PageInfo pageInfo = (PageInfo) this.apiVenueService.selectVenList2IndexPage(cultids, deptids, pageSize);
            arb.setRows(pageInfo.getList());
            arb.setPageInfo(pageInfo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return arb;
    }


    /**
     *
     * @param page  页码*
     * @param pageSize  页记录数*
     * @param cultid   所属文化馆ID
     * @param type  场馆类型
     * @param tag   场馆标签
     * @param key   场馆关键字
     * @param isuse 是否可预订：“1”可预订
     * @param content   内容搜标题或简介
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/venue/list", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object getVenuelist(WebRequest request
            ,@RequestParam(value = "page", defaultValue = "1")int page
            ,@RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        ApiResultBean arb = new ApiResultBean();

        Map recode = new HashMap();
        //recode.put("cultid", request.getParameter("cultid"));
        String cultid = request.getParameter("cultid");

        //区分全省、全市站
        if (cultid == null || cultid.isEmpty()){
            recode.put("allprovince", true);
        }
        if (cultid != null && cultid.contains(",")){
            recode.put("allcity", true);
        }

        if (cultid != null && !cultid.isEmpty()) {
            recode.put("cultid", Arrays.asList(cultid.replaceAll(","," ").trim().split(" ")));
        }
        String deptid = request.getParameter("deptid");
        if (deptid!=null && !deptid.isEmpty()){
            try {
                recode.put("deptid", this.whgSystemDeptService.srchDeptStrList(deptid));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        recode.put("province", request.getParameter("province"));
        recode.put("city", request.getParameter("city"));
        recode.put("area", request.getParameter("area"));
        recode.put("type", request.getParameter("type"));
        recode.put("tag", request.getParameter("tag"));
        recode.put("key", request.getParameter("key"));
        recode.put("isUse", request.getParameter("isUse"));
        recode.put("content", request.getParameter("content"));

        Calendar c = Calendar.getInstance();
        /*c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);*/
        recode.put("now", c.getTime());
        recode.put("protype", EnumProject.PROJECT_WBGX.getValue());

        try {
            PageInfo pageInfo = this.apiVenueService.selectVen4Page(page, pageSize, recode);

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
     * 场馆详情
     * @param itemId
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/venue/detail", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object getVenInfo(String itemId){
        ApiResultBean arb = new ApiResultBean();

        try {
            Object venInfo = this.apiVenueService.selectVen4id(itemId, EnumProject.PROJECT_WBGX.getValue());
            if (venInfo==null){
                arb.setCode(102);
                arb.setMsg("查询无数据");
                return arb;
            }
            arb.setData(venInfo);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(),e);
        }
        return arb;
    }

    /**
     * 查推荐场馆列表，
     * @param exVenid   例外的场馆ID
     * @param cultid  分馆id
     * @param size 条数
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/venue/recommendVenList", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object getRecommendVenList(@RequestParam(required = false, value = "exVenid") String exVenid,
                                      @RequestParam(required = false, value = "cultid") String cultid,
                                      @RequestParam(required = false, value = "deptid") String deptid,
                                      @RequestParam(required = false, value = "size") Integer size){
        ApiResultBean arb = new ApiResultBean();

        try {
            List<String> cultids = null;
            if (cultid != null && !cultid.isEmpty()) {
                cultids = Arrays.asList(cultid.replaceAll(","," ").trim().split(" "));
            }
            List<String> deptids = null;
            if (deptid != null && !deptid.isEmpty()) {
                deptids = this.whgSystemDeptService.srchDeptStrList(deptid);
            }

            List list = this.apiVenueService.selectRecommendVenList(exVenid, cultids,deptids, size, EnumProject.PROJECT_WBGX.getValue());
            arb.setRows(list);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            arb.setData(new ArrayList());
            log.error(e.getMessage(), e);
        }

        return arb;
    }









    /**
     * 活动室详情
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/venue/roomDetail", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object getVenRoomInfo(String id){
        ApiResultBean arb = new ApiResultBean();

        try {
            Object info = this.apiVenueService.selectVenRoom4id(id);
            if (info==null){
                arb.setCode(102);
                arb.setMsg("查询无数据");
                return arb;
            }
            arb.setData(info);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(),e);
        }
        return arb;
    }


    /**
     * 查询活动室的相关活动室
     * @param roomid
     * @param size
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/venue/refRoomList", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object getRefRoomList(String roomid,
                                 @RequestParam(required = false) Integer size){
        ApiResultBean arb = new ApiResultBean();

        try {
            List list = this.apiVenueService.selectRefRoomList(roomid, size, EnumProject.PROJECT_WBGX.getValue());
//            arb.setData(list);
            arb.setRows(list);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            arb.setData(new ArrayList());
            log.error(e.getMessage(),e);
        }

        return arb;
    }

    /**
     * 获取指定活动室可预订的最大和最小的两个天数时间
     * @param roomid
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/venue/roomTimeMaxMinDay", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object getRoomTimeMinMaxDay(String roomid){
        ApiResultBean arb = new ApiResultBean();

        try {
            Map maxMinDay = this.apiVenueService.getRoomTimeMaxMinDay(roomid);
            arb.setData(maxMinDay);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(),e);
        }

        return arb;
    }

    /**
     * 查询时段内活动室的开放预订
     * @param roomid
     * @param startDay
     * @param endDay
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/venue/roomTimes", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object getVenRoomTimes(String roomid,
                                  @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDay,
                                  @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDay){
        ApiResultBean arb = new ApiResultBean();
        try {
            List list = this.apiVenueService.selectRoomTimeList(roomid, startDay, endDay);
//            arb.setData(list);
            arb.setRows(list);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            arb.setData(new ArrayList());
            log.error(e.getMessage(),e);
        }
        return arb;
    }

    /**
     * 查询时段内活动室成功预订信息
     * @param roomid
     * @param startDay
     * @param endDay
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/venue/roomOrderSuccess", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object getVenRoomOrderSuccess(String roomid,
                                     @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDay,
                                     @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDay){
        ApiResultBean arb = new ApiResultBean();
        try {
            List list = this.apiVenueService.selectRoomOrderSuccess(roomid, startDay, endDay);
//            arb.setData(list);
            arb.setRows(list);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            arb.setData(new ArrayList());
            log.error(e.getMessage(),e);
        }
        return arb;
    }

    /**
     * 查时段内的活动室 用户ID 的预订申请
     * @param roomid
     * @param userId
     * @param startDay
     * @param endDay
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/venue/roomOrderApply4userid", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object getVenRoomOrderApply4userid(String roomid, String userId,
                                     @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDay,
                                     @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDay){
        ApiResultBean arb = new ApiResultBean();
        try {
            List list = this.apiVenueService.selectRoomOrderApply4userId(roomid, userId, startDay, endDay);
//            arb.setData(list);
            arb.setRows(list);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            arb.setData(new ArrayList());
            log.error(e.getMessage(),e);
        }
        return arb;
    }

    /**
     * 合并取活动室开放预定信息
     * @param roomid
     * @param userId
     * @param startDay
     * @param endDay
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/venue/roomApplyAll", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object getRoomApplyListAll(String roomid,
                                  @RequestParam(value = "userId", required = false) String userId,
                                  @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDay,
                                  @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDay){
        ApiResultBean arb = new ApiResultBean();
        try {
            Map rest = new HashMap();

            List timeList = this.apiVenueService.selectRoomTimeList(roomid, startDay, endDay);
            List successList = this.apiVenueService.selectRoomOrderSuccess(roomid, startDay, endDay);
            rest.put("timeList", timeList);
            rest.put("successList", successList);

            if (userId != null && !userId.isEmpty()) {
                List applyList = this.apiVenueService.selectRoomOrderApply4userId(roomid, userId, startDay, endDay);
                rest.put("applyList", applyList);
            }else {
                rest.put("applyList", new ArrayList());
            }
            arb.setData(rest);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            arb.setData(new ArrayList());
            log.error(e.getMessage(),e);
        }
        return arb;
    }


    /**
     * 场馆活动室预订检查
     * @param roomTimeId 开放时段Id
     * @param userId 预定用户Id
     * @return
     * code:
     * 101: 预定检查失败
     * 102: 预订用户不能为空
     * 103: 预订用户不存在
     * 104: 您还未绑定手机，请先绑定手机！
     * 105: 非常抱歉！您的操作行为已被列入黑名单，如需了解详细情况，请与管理员联系！
     * 106: 申请的活动室所选开放时段已关闭预订
     * 107: 申请的活动室不存在
     * 108: 申请的活动室所属场馆状态不可用
     * 109: 申请的活动室开放时间已过时
     * 110: 申请的活动室已被其他用户预定
     * 111: 重复申请了相同活动室开放时段，请查看已申请的预订
     */
    @CrossOrigin
    @RequestMapping(value = "/venue/orderCheck", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object testOrderCheck(String roomTimeId, String userId){
        ApiResultBean arb = new ApiResultBean();

        try {
            arb = (ApiResultBean) this.apiVenueService.testUserRoomTime(userId, roomTimeId, null, null, null);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("预定检查失败");
            log.error(e.getMessage(),e);
        }
        return arb;
    }

    /**
     * 场馆活动室预订
     * @param roomTimeId 开放时段Id
     * @param userId    预定用户Id
     * @param phone  手机
     * @param username  姓名
     * @param purpose   预定用途
     * @return
     * code:
     * 101: 预定检查失败
     * 102: 预订用户不能为空
     * 103: 预订用户不存在
     * 104: 您还未绑定手机，请先绑定手机！
     * 105: 非常抱歉！您的操作行为已被列入黑名单，如需了解详细情况，请与管理员联系！
     * 106: 申请的活动室所选开放时段已关闭预订
     * 107: 申请的活动室不存在
     * 108: 申请的活动室所属场馆状态不可用
     * 109: 申请的活动室开放时间已过时
     * 110: 申请的活动室已被其他用户预定
     * 111: 重复申请了相同活动室开放时段，请查看已申请的预订
     * 131: 手机号码格式不正确
     * 132: 预订人为空或输入过长
     * 133: 预订用途输入过长
     */
    @CrossOrigin
    @RequestMapping(value = "/venue/orderApply", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object orderApply(String roomTimeId, String userId, String phone, String username, String purpose){
        ApiResultBean arb = new ApiResultBean();
        try {
            arb = (ApiResultBean) this.apiVenueService.saveUserRoomOrder(userId, roomTimeId, phone, username, purpose);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("预定失败");
            log.error(e.getMessage(),e);
        }
        return arb;
    }

    /**
     * 取消活动室预定申请
     * @param orderId
     * @param userId
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/venue/unOrder", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object unOrder(String orderId, String userId){
        ApiResultBean arb = new ApiResultBean();

        try {
            if (orderId == null) {
                throw new Exception("param is null");
            }
            arb = (ApiResultBean) this.apiVenueService.unOrder(orderId, userId);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("取消预订失败");
            log.error(e.getMessage(), e);
        }

        return arb;
    }

    /**
     * 删除取消的预订
     * @param orderId
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/venue/delOrder", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object delOrder(String orderId){
        ApiResultBean arb = new ApiResultBean();

        try {
            arb = (ApiResultBean) this.apiVenueService.delOrder(orderId);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("操作失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }
}
