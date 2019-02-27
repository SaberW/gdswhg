package com.creatoo.hn.controller.api.apioutside.train;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.api.apioutside.train.ApiTraDrscService;
import com.creatoo.hn.services.api.apioutside.train.ApiTraTeacherService;
import com.creatoo.hn.services.api.apioutside.train.ApiTrainService;
import com.creatoo.hn.util.FilterFontUtil;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumBMState;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/7.
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping("/api/tra")
@CrossOrigin
public class ApiTrainController extends BaseController{

    @Autowired
    private ApiTrainService service;

    @Autowired
    private ApiTraTeacherService apiTraTeacherService;

    @Autowired
    private ApiTraDrscService apiTraDrscService;

    /**
     * 培训列表页
     * @param page 当前页
     * @param pageSize  页列表数量
     * @param cultid  文化馆id
     * @param arttype  艺术分类
     * @param type  培训类型
     * @param sort  排序（1-即将开始  2-即将结束）
     * @param title  搜索标题
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/list",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean indexData(@RequestParam(value = "page", defaultValue = "1")int page,
                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                   @RequestParam(value = "cultid",required = false) String cultid,
                                  @RequestParam(value = "arttype",required = false)String arttype,
                                  @RequestParam(value = "type",required = false)String type,
                                  @RequestParam(value = "sort",required = false)String sort,
                                  @RequestParam(value = "title",required = false)String title){
        ApiResultBean rep = new ApiResultBean();
        Map data = new HashMap();
        try {
            PageInfo info = this.service.getTraList(page,pageSize,cultid,arttype,type,sort,title);
//            ApiPageBean apb = new ApiPageBean();
//            apb.setIndex(info.getPageNum());
//            apb.setCount(info.getSize());
//            apb.setSize(info.getPageSize());
//            apb.setTotal(info.getTotal());
//            data.put("list",info.getList());
//            data.put("pager", apb);
//            rep.setData(data);
            rep.setPageInfo(info);
            rep.setRows(info.getList());
        } catch (Exception e) {
            rep.setCode(1);
            rep.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return rep;
    }

    /**
     * 培训详情
     * @param id
     * @param userid
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/detail",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean traDetail(@RequestParam(value = "itemId",required = false)String id,@RequestParam(value = "userId",required = false)String userid){
        ApiResultBean arb = new ApiResultBean();
        if(null == id){
            arb.setCode(101);
            arb.setMsg("培训ID不能为空");
            return arb;
        }
        List<WhgTraCourse> whgTraCourseList = new ArrayList<>();
        ApiResultBean canSign = new ApiResultBean();
        try {
            Object whgTra = service.getOneTra(id,userid);
            if(null == whgTra){
                arb.setCode(102);
                arb.setMsg("查询培训数据失败");
                return arb;
            }
            canSign = service.canSign(userid,id);
            whgTraCourseList = service.getCourseByTraId(id);
            arb.setData(whgTra);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            arb.setCode(102);
            arb.setMsg("查询数据失败");
        }



        return arb;
    }

    /**
     * 获取推荐培训
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/getRecommendTra",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean getRecommendTra(@RequestParam(value = "id",required = true)String id,
                                         @RequestParam(value = "cultid",required = false)String cultid,
                                         @RequestParam(value = "size",required = false)Integer size){
        ApiResultBean arb = new ApiResultBean();
        try {
            PageInfo pageInfo = service.getRecommendTra(id,cultid,size);
            arb.setRows(pageInfo.getList());
            arb.setPageInfo(pageInfo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            arb.setCode(101);
            arb.setMsg("查询数据失败");
        }
        return arb;
    }

    /**
     * 检查报名
     * @param userid
     * @param trainId
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/bookingvalid",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean checkTrainApply(@RequestParam(value = "userId",required = false)String userid,
                                         @RequestParam(value = "itemId",required = true)String trainId,
                                         @RequestParam(value = "gatherid",required = false)String gatherid){
        ApiResultBean arb = new ApiResultBean();
        try {
            arb = service.checkApplyTrain(trainId,userid,gatherid);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            arb.setCode(101);
            arb.setMsg("数据查询失败");
        }
        return arb;
    }

    /**
     * 培训报名
     * @param userid
     * @param enrol
     * @param enrolBirthdayStr
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/booking",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean saveTrainEnrol(@RequestParam(value = "userId",required = true)String userId,
                                        @RequestParam(value = "itemId",required = true)String itemId,
                                        @RequestParam(value = "name",required = true)String name,
                                        @RequestParam(value = "birthday",required = true)String birthday,
                                        @RequestParam(value = "caednumber",required = true)String caednumber,
                                        @RequestParam(value = "phone",required = true)String phone){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ApiResultBean arb = new ApiResultBean();
        WhgTraEnrol enrol = new WhgTraEnrol();
        try {
            enrol.setBirthday(sdf.parse(birthday));
            enrol.setTraid(itemId);
            enrol.setRealname(name);
           // enrol.setSex(sex);
            enrol.setCardno(caednumber);
            enrol.setContactphone(phone);
            arb = service.syncAddTranEnrol(enrol, userId);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            arb.setCode(101);
            arb.setMsg("保存失败");
        }
        return arb;
    }

    /**
     * 查询培训老师列表
     * @param page
     * @param pageSize
     * @param cultid
     * @param arttype
     * @param title
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/teaList",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean teaList(@RequestParam(value = "page", defaultValue = "1")int page,
                                 @RequestParam(value = "pageSize", defaultValue = "10")int pageSize,
                                 @RequestParam(value = "cultid",required = false) String cultid,
                                 @RequestParam(value = "arttype",required = false)String arttype,
                                 @RequestParam(value = "title",required = false)String title){
        ApiResultBean arb = new ApiResultBean();
        Map data = new HashMap();
        try {

            PageInfo info = this.apiTraTeacherService.getTeaList(page,pageSize,cultid,arttype,title);
//            ApiPageBean apb = new ApiPageBean();
//            apb.setIndex(info.getPageNum());
//            apb.setCount(info.getSize());
//            apb.setSize(info.getPageSize());
//            apb.setTotal(info.getTotal());
//            data.put("list",info.getList());
//            data.put("pager", apb);
//            arb.setData(data);
            arb.setRows(info.getList());
            arb.setPageInfo(info);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            arb.setCode(101);
            arb.setMsg("查询失败");
        }
        return arb;
    }

    /**
     * 培训老师详情
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/teaDetail",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean teaDetail(String id){
        ApiResultBean arb = new ApiResultBean();
        try {
            WhgTraTeacher teacher = this.apiTraTeacherService.selTeaById(id);
            arb.setData(teacher);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            arb.setCode(101);
            arb.setMsg("查询失败");
        }
        return arb;
    }

    /**
     * 获取老师相关培训
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/getRecommendTea",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean getRecommendTea(@RequestParam(value = "id",required = true)String id,
                                          @RequestParam(value = "cultid",required = false)String cultid,
                                          @RequestParam(value = "size",required = false)Integer size){
        ApiResultBean arb = new ApiResultBean();
        try {
            Map data = new HashMap();
            PageInfo pageInfo = this.service.getTraByTea(id,cultid,size);
//            ApiPageBean apb = new ApiPageBean();
//            apb.setIndex(pageInfo.getPageNum());
//            apb.setCount(pageInfo.getSize());
//            apb.setSize(pageInfo.getPageSize());
//            apb.setTotal(pageInfo.getTotal());
//            data.put("list",pageInfo.getList());
//            data.put("pager", apb);
//            arb.setData(data);
            arb.setRows(pageInfo.getList());
            arb.setPageInfo(pageInfo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            arb.setCode(101);
            arb.setMsg("查询数据失败");
        }
        return arb;
    }

    /**
     * 在线点播列表
     * @param page
     * @param pageSize
     * @param cultid
     * @param drscarttyp
     * @param title
     * @param order
     * @return
     */
    @RequestMapping(value = "/drscList",method = {RequestMethod.POST,RequestMethod.GET})
    @CrossOrigin
    @ResponseBody
    public ApiResultBean descList(@RequestParam(value = "page", defaultValue = "1")int page,
                                  @RequestParam(value = "pageSize", defaultValue = "10")int pageSize, @RequestParam(value = "cultid",required = false)String cultid,
                                  @RequestParam(value = "drscarttyp",required = false)String drscarttyp,
                                  @RequestParam(value = "title",required = false)String title,
                                  @RequestParam(value = "sort",required = false)String sort){
        ApiResultBean arb = new ApiResultBean();
        Map data = new HashMap();
        try {
            PageInfo info = this.apiTraDrscService.findList(page,pageSize,cultid,drscarttyp,title,sort);
//            ApiPageBean apb = new ApiPageBean();
//            apb.setIndex(info.getPageNum());
//            apb.setCount(info.getSize());
//            apb.setSize(info.getPageSize());
//            apb.setTotal(info.getTotal());
//            data.put("list",info.getList());
//            data.put("pager", apb);
//            arb.setData(data);
            arb.setRows(info.getList());
            arb.setPageInfo(info);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            arb.setCode(101);
            arb.setMsg("查询失败");
        }
        return arb;
    }

    /**
     * 在线点播详情
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value="/descDetail",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean descDetail(String id){
        ApiResultBean arb = new ApiResultBean();
        try {
            WhgDrsc desc = this.apiTraDrscService.selDescById(id);
            arb.setData(desc);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            arb.setCode(101);
            arb.setMsg("查询失败");
        }
        return arb;
    }

    /**
     * 获取推荐在线点播
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/getRecommendDesc",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean getRecommendDesc(@RequestParam(value = "id",required = true)String id,
                                         @RequestParam(value = "cultid",required = false)String cultid,
                                         @RequestParam(value = "size",required = false)Integer size){
        ApiResultBean arb = new ApiResultBean();
        try {
            PageInfo pageInfo = apiTraDrscService.getRecommendDesc(id,cultid,size);
            arb.setRows(pageInfo.getList());
            arb.setPageInfo(pageInfo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            arb.setCode(101);
            arb.setMsg("查询数据失败");
        }
        return arb;
    }

    /**
     * 获取用户培训订单
     * @param request
     * @return
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/getMyTraEnrol",method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResultBean getMyTraEnrol(@RequestParam(value = "page", defaultValue = "1")int page,
                                       @RequestParam(value = "pageSize", defaultValue = "10")int pageSize,
                                       @RequestParam(value = "userId",required = true)String userId,
                                       @RequestParam(value = "sdate",required = true)Integer sdate){
        ApiResultBean arb = new ApiResultBean();
        Map data = new HashMap();
        if(null == userId){
            arb.setCode(101);
            arb.setMsg("用户ID不能为空");
            return arb;
        }
        PageInfo info = null;
        try {
            info = service.getUserTraOrder(page,pageSize,userId,sdate);
//            ApiPageBean apb = new ApiPageBean();
//            apb.setIndex(info.getPageNum());
//            apb.setCount(info.getSize());
//            apb.setSize(info.getPageSize());
//            apb.setTotal(info.getTotal());
//            data.put("list",info.getList());
//            data.put("pager", apb);
//            arb.setData(data);
            arb.setRows(info.getList());
            arb.setPageInfo(info);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            arb.setCode(102);
            arb.setMsg("查询失败");
        }
        return arb;
    }

    /**
     * 删除我的报名
     * @param id
     * @return
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping("/myenroll/delmyenroll")
    public ApiResultBean delmyenroll(@RequestParam(value = "orderId",required = true)String orderId,
                                     @RequestParam(value = "userId",required = true)String userId,
                                     @RequestParam(value = "state",required = false)Integer state){
        ApiResultBean arb = new ApiResultBean();
        WhgTraEnrol enrol = new WhgTraEnrol();
        String success = "0";
        String errmsg = "";
        if(userId == null && "".equals(userId)){
            arb.setCode(103);
            arb.setMsg("用户ID不能为空");
            return arb;
        }
        if(userId == null && "".equals(userId)){
            arb.setCode(104);
            arb.setMsg("订单ID不能为空");
            return arb;
        }
        if(state != null && state != 2){
            enrol.setState(EnumBMState.BM_QXBM.getValue());
            enrol.setId(orderId);
            try {
                this.service.updateMyEnroll(enrol);
                //清除相关报名的临时上传目录
            } catch (Exception e) {
                arb.setCode(101);
                arb.setMsg("取消失败");
                log.error(e.getMessage(),e);
            }
        }else{
            try {
                this.service.delEnroll(orderId);
                //清除相关报名的临时上传目录
            } catch (Exception e) {
                arb.setCode(102);
                arb.setMsg("删除失败");
                log.error(e.getMessage(),e);
            }
        }
        return arb;
    }
}
