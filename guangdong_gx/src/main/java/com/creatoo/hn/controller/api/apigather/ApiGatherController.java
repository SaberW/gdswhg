package com.creatoo.hn.controller.api.apigather;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgGatherOrder;
import com.creatoo.hn.services.admin.gather.WhgGatherService;
import com.creatoo.hn.services.admin.system.WhgSystemDeptService;
import com.creatoo.hn.util.FilterFontUtil;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

/**
 * Created by rbg on 2017/10/10.
 */
@SuppressWarnings("ALL")
@Controller
@CrossOrigin
@RequestMapping("api/gather")
public class ApiGatherController extends BaseController{

    @Autowired
    private WhgGatherService whgGatherService;

    @Autowired
    private WhgSystemDeptService whgSystemDeptService;

    /**
     * 众筹品牌列表
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/brand/list", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object getGatherBrands(WebRequest request,
                                  @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                  @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){
        ApiResultBean arb = new ApiResultBean();
        try {
            //String cultid = request.getParameter("cultid");
            String cultid = request.getParameter("cultid");
            List cultids = null;
            if (cultid != null && !cultid.isEmpty()) {
                cultids = Arrays.asList(cultid.split("\\s*,\\s*"));
            }

            String province = request.getParameter("province");
            String city = request.getParameter("city");
            String area = request.getParameter("area");

            String deptid = request.getParameter("deptid");
            List deptids = null;
            if (deptid!=null && !deptid.isEmpty()){
                deptids = this.whgSystemDeptService.srchDeptStrList(deptid);
            }

            PageInfo pageInfo = this.whgGatherService.select4pBrands(page, pageSize, cultids,province,city,area, deptids);
            arb.setPageInfo(pageInfo);
            arb.setRows(pageInfo.getList());
        } catch (Exception e) {
            arb.setMsg("查询数据失败");
            arb.setCode(101);
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 品牌详情
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/brand/info", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object getGatherBrand(String id){
        ApiResultBean arb = new ApiResultBean();
        try {
            Object info = this.whgGatherService.findBrand(id);
            arb.setData(info);
        }catch (Exception e){
            arb.setMsg("查询数据失败");
            arb.setCode(101);
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 品牌项目列表
     * @param request
     * @param brandid
     * @param page
     * @param pageSize
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/brand/gatherlist", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object getGatherList4Brand(WebRequest request, String brandid,
                                      @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){
        ApiResultBean arb = new ApiResultBean();
        try {
            Map recode = new HashMap();
            //recode.put("cultid", request.getParameter("cultid"));
            String cultid = request.getParameter("cultid");
            if (cultid != null && !cultid.isEmpty()) {
                recode.put("cultid", Arrays.asList(cultid.split("\\s*,\\s*")));
            }

            recode.put("filter", request.getParameter("filter"));
            recode.put("brandid", brandid);

            String deptid = request.getParameter("deptid");
            if (deptid!=null && !deptid.isEmpty()){
                recode.put("deptid", this.whgSystemDeptService.srchDeptStrList(deptid));
            }

            PageInfo pageInfo = this.whgGatherService.selectGathers4Brands(page, pageSize, recode);
            arb.setPageInfo(pageInfo);
            arb.setRows(pageInfo.getList());

            Map data = new HashMap();
            data.put("now", new Date());
            arb.setData(data);
        } catch (Exception e) {
            arb.setMsg("查询数据失败");
            arb.setCode(101);
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 品牌资源列表
     * @param request
     * @param brandid
     * @param page
     * @param pageSize
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/brand/resourcelist", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object getGatherBrandResourceList(WebRequest request, String brandid,
                                             @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                             @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){
        ApiResultBean arb = new ApiResultBean();
        try {
            Map recode = new HashMap();
            recode.put("refid", brandid);
            recode.put("enttype", request.getParameter("enttype"));

            PageInfo pageInfo = this.whgGatherService.selectBrandResources(page, pageSize, recode);
            arb.setPageInfo(pageInfo);
            arb.setRows(pageInfo.getList());
        } catch (Exception e) {
            arb.setMsg("查询数据失败");
            arb.setCode(101);
            log.error(e.getMessage(), e);
        }
        return arb;
    }


    /**
     * 众筹项目列表
     * @param request
     * @param page
     * @param pageSize
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/list", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object getGatherList(WebRequest request,
                                @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){
        ApiResultBean arb = new ApiResultBean();
        try {
            Map recode = new HashMap();
            //recode.put("cultid", request.getParameter("cultid"));
            String cultid = request.getParameter("cultid");
            if (cultid != null && !cultid.isEmpty()) {
                recode.put("cultid", Arrays.asList(cultid.split("\\s*,\\s*")));
            }

            String deptid = request.getParameter("deptid");
            if (deptid!=null && !deptid.isEmpty()){
                recode.put("deptid", this.whgSystemDeptService.srchDeptStrList(deptid));
            }

            recode.put("province", request.getParameter("province"));
            recode.put("city", request.getParameter("city"));
            recode.put("area", request.getParameter("area"));
            recode.put("etype", request.getParameter("etype")); // 0其他，4活动，5培训
            recode.put("jd", request.getParameter("jd")); //1即将开始，2进行中，3众筹结，4众筹成功
            recode.put("content", request.getParameter("content")); //内容

            recode.put("px", request.getParameter("px")); //0推荐排序，1最新上线

            PageInfo pageInfo = this.whgGatherService.selectGatherList(page, pageSize, recode);
            arb.setPageInfo(pageInfo);
            arb.setRows(pageInfo.getList());

            Map data = new HashMap();
            data.put("now", new Date());
            arb.setData(data);
        } catch (Exception e) {
            arb.setMsg("查询数据失败");
            arb.setCode(101);
            log.error(e.getMessage(), e);
        }
        return arb;
    }


    /**
     * 查询众筹详情
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/info",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object getGatherInfo(String id){
        ApiResultBean arb = new ApiResultBean();

        try {
            Map info = this.whgGatherService.findGather(id);
            info.put("now", new Date());

            arb.setData(FilterFontUtil.clearFont(info, new String[]{"descriptions"}));
        } catch (Exception e) {
            arb.setMsg("查询数据失败");
            arb.setCode(101);
            log.error(e.getMessage(), e);
        }
        return arb;
    }


    /**
     * 查询众筹资源
     * @param request
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/resourcelist", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object getGatherResourceList(WebRequest request, String id,
                                        @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){
        ApiResultBean arb = new ApiResultBean();
        try {
            Map recode = new HashMap();
            recode.put("refid", id);
            recode.put("enttype", request.getParameter("enttype"));

            PageInfo pageInfo = this.whgGatherService.selectGatherResources(page, pageSize, recode);
            arb.setPageInfo(pageInfo);
            arb.setRows(pageInfo.getList());
        } catch (Exception e) {
            arb.setMsg("查询数据失败");
            arb.setCode(101);
            log.error(e.getMessage(), e);
        }
        return arb;
    }



    /**
     * 申请检查 其它众筹
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/applychecking", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object applyGatherChecking(WebRequest request){
        ApiResultBean arb = new ApiResultBean();
        String id = request.getParameter("id");
        String userid = request.getParameter("userid");
        String etype = request.getParameter("etype");

        try {
            arb = this.whgGatherService.applyCheckGather(id, userid, etype);
        } catch (Exception e) {
            arb.setMsg("申请检查失败");
            arb.setCode(101);
            log.error(e.getMessage(), e);
        }

        return arb;
    }

    /**
     * 申请 其它众筹
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/apply", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object applyGather(WhgGatherOrder info, String gatherid, String userid){
        ApiResultBean arb = new ApiResultBean();

        try {
            arb = this.whgGatherService.applyGather4other(info, gatherid, userid);
        } catch (Exception e) {
            arb.setMsg("操作失败");
            arb.setCode(101);
            log.error(e.getMessage(), e);
        }

        return arb;
    }

    /**
     * 删除申请 其它众筹
     * @param id
     * @param userid
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/unapply", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object unApplyGather(String id, String userid){
        ApiResultBean arb = new ApiResultBean();

        try {
            arb = this.whgGatherService.unApplyGather(id, userid);
        } catch (Exception e) {
            arb.setMsg("操作失败");
            arb.setCode(101);
            log.error(e.getMessage(), e);
        }

        return arb;

    }

    /**
     * 取消申请 其它众筹
     * @param id
     * @param userid
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/offapply", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object offApplyGather(String id, String userid){
        ApiResultBean arb = new ApiResultBean();

        try {
            arb = this.whgGatherService.offApplyGather(id, userid);
        } catch (Exception e) {
            arb.setMsg("操作失败");
            arb.setCode(101);
            log.error(e.getMessage(), e);
        }

        return arb;

    }


    /**
     * 我的众筹列表
     * filter : 1进行中, 2众筹成功,3众筹失败,  4历史
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/usergatherlist", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object getUserGathers(String userid, WebRequest request,
                                 @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                 @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){
        ApiResultBean arb = new ApiResultBean();
        try {
            Map recode = new HashMap();
            recode.put("userid", userid);
            recode.put("filter", request.getParameter("filter"));

            PageInfo pageInfo = this.whgGatherService.selectUserGatherList(page, pageSize, recode);

            arb.setPageInfo(pageInfo);
            arb.setRows(pageInfo.getList());

            Map data = new HashMap();
            data.put("now", new Date());
            arb.setData(data);
        } catch (Exception e) {
            arb.setMsg("查询数据失败");
            arb.setCode(101);
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 我的收藏众筹列表
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/usergatherlist4colle", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object getUserColleGathers(String userid, WebRequest request,
                                      @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){
        ApiResultBean arb = new ApiResultBean();
        try {
            Map recode = new HashMap();
            recode.put("userid", userid);
            //recode.put("cultid", request.getParameter("cultid"));
            String cultid = request.getParameter("cultid");
            if (cultid != null && !cultid.isEmpty()) {
                recode.put("cultid", Arrays.asList(cultid.split("\\s*,\\s*")));
            }

            String deptid = request.getParameter("deptid");
            if (deptid!=null && !deptid.isEmpty()){
                recode.put("deptid", this.whgSystemDeptService.srchDeptStrList(deptid));
            }

            recode.put("province", request.getParameter("province"));
            recode.put("city", request.getParameter("city"));
            recode.put("area", request.getParameter("area"));
            recode.put("etype", request.getParameter("etype")); // 0其他，4活动，5培训

            PageInfo pageInfo = this.whgGatherService.selectGatherList4colle(page, pageSize, recode);

            arb.setPageInfo(pageInfo);
            arb.setRows(pageInfo.getList());

            Map data = new HashMap();
            data.put("now", new Date());
            arb.setData(data);
        } catch (Exception e) {
            arb.setMsg("查询数据失败");
            arb.setCode(101);
            log.error(e.getMessage(), e);
        }
        return arb;
    }

}
