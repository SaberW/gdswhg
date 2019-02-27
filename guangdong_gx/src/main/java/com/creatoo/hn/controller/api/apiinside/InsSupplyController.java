package com.creatoo.hn.controller.api.apiinside;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.services.admin.supply.WhgSupplyService;
import com.creatoo.hn.services.admin.system.WhgSystemDeptService;
import com.creatoo.hn.services.api.apiinside.InsSupplyService;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumSupplyGxType;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 内部供需-供需列表
 */
@SuppressWarnings("ALL")
@Controller
@CrossOrigin
@RequestMapping("/api/inside/supply")
public class InsSupplyController extends BaseController{

    @Autowired
    private InsSupplyService insSupplyService;

    @Autowired
    private WhgSupplyService whgSupplyService;

    @Autowired
    private WhgSystemDeptService whgSystemDeptService;


    private Map getParams2Map(HttpServletRequest req, Date beginDay, Date endDay){
        Map recode = new HashMap();
        //recode.put("cultid", req.getParameter("cultid"));
        String cultid = req.getParameter("cultid");
        if (cultid != null && !cultid.isEmpty()) {
            recode.put("cultid", Arrays.asList(cultid.split("\\s*,\\s*")));
        }

        String deptid = req.getParameter("deptid");
        if (deptid!=null && !deptid.isEmpty()){
            try {
                recode.put("deptid", this.whgSystemDeptService.srchDeptStrList(deptid));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        recode.put("srchkey", req.getParameter("srchkey"));
        recode.put("gxtype", req.getParameter("gxtype"));
        recode.put("etype", req.getParameter("etype"));
        recode.put("province", req.getParameter("province"));
        recode.put("city", req.getParameter("city"));
        recode.put("area", req.getParameter("area"));
        recode.put("sortType", req.getParameter("sortType"));
        recode.put("psprovince", req.getParameter("psprovince"));
        recode.put("pscity", req.getParameter("pscity"));

        recode.put("beginDay", beginDay);
        if (endDay != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(endDay);
            c.add(Calendar.DAY_OF_YEAR, 1);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            endDay = c.getTime();
        }
        recode.put("endDay", endDay);

        return recode;
    }


    /**
     * 供需需求列表查询
     * @param page 分页
     * @param pageSize 分页
     * @param etype 类型
     * @param city 区域
     * @param sortType 排序类型值
     * @param beginDay 起始天
     * @param endDay 结束天
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/list", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object list(HttpServletRequest req,
                       @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                       @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                       @DateTimeFormat(pattern = "yyyy-MM-dd")Date beginDay,
                       @DateTimeFormat(pattern = "yyyy-MM-dd")Date endDay){
        ApiResultBean arb = new ApiResultBean();
        try {
            Map recode = this.getParams2Map(req, beginDay, endDay);

            String gxtype = (String) recode.get("gxtype");
            if (gxtype==null || gxtype.isEmpty()){
                gxtype = EnumSupplyGxType.TYPE_G.getValue();
            }

            PageInfo pageInfo = null;
            Map gxcount = new HashMap();
            gxcount.put("gxtype", gxtype);
            if (EnumSupplyGxType.TYPE_G.getValue().equals(gxtype)){
                pageInfo =  this.insSupplyService.srchFkProjectList4p(page, pageSize, recode);
                gxcount.put("gcount", pageInfo.getTotal());
                gxcount.put("xcount", this.insSupplyService.countSupplys(recode));
            }else{
                pageInfo =  this.insSupplyService.srchList4p(page, pageSize, recode);
                gxcount.put("xcount", pageInfo.getTotal());
                gxcount.put("gcount", this.insSupplyService.countFkProjects(recode));
            }

            arb.setPageInfo(pageInfo);
            arb.setRows(pageInfo.getList());

            arb.setData(gxcount);

        } catch (Exception e) {
            arb.setMsg("101");
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 供需需求信息记数
     * @param req
     * @param beginDay
     * @param endDay
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/listCount", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object listCount(HttpServletRequest req,
                       @DateTimeFormat(pattern = "yyyy-MM-dd")Date beginDay,
                       @DateTimeFormat(pattern = "yyyy-MM-dd")Date endDay){
        ApiResultBean arb = new ApiResultBean();
        try {
            Map recode = this.getParams2Map(req, beginDay, endDay);

            String gxtype = (String) recode.get("gxtype");
            if (gxtype==null || gxtype.isEmpty()){
                gxtype = EnumSupplyGxType.TYPE_G.getValue();
            }

            Map gxcount = new HashMap();
            gxcount.put("gcount", this.insSupplyService.countFkProjects(recode));
            gxcount.put("xcount", this.insSupplyService.countSupplys(recode));

            arb.setData(gxcount);
        } catch (Exception e) {
            arb.setMsg("101");
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 供需需求详情
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/info", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object info(String id) {
        ApiResultBean arb = new ApiResultBean();
        try {
            arb = (ApiResultBean) this.insSupplyService.srchOne(id);
        } catch (Exception e) {
            arb.setMsg("101");
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 取时间段列表
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/timeList", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object timeList(String id,
                           @RequestParam(value = "page", required = false) Integer page,
                           @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        ApiResultBean arb = new ApiResultBean();
        try {
            if (page!=null && pageSize!=null){
                PageInfo pageInfo = (PageInfo) this.whgSupplyService.selectTimes4Supply(id, page, pageSize);
                arb.setRows(pageInfo.getList());
                arb.setPageInfo(pageInfo);
            }else {
                List times = this.whgSupplyService.selectTimes4Supply(id);
                arb.setRows(times);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        Map data = new HashMap();
        data.put("now", new Date());
        arb.setData(data);
        return arb;
    }

    /**
     * 供需信息匹配列表
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/infoMatchList", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object infoMatchList(String id,
                                @RequestParam(value = "size", required = false)Integer size){
        ApiResultBean arb = new ApiResultBean();

        try {
            arb = (ApiResultBean) this.insSupplyService.selectMatchList(id, size);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
        }
        return arb;
    }

    /**
     * 获取供需可用年月的日期集
     * @param id
     * @param year
     * @param month
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/openDays", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object getOpenDays(String id, String year, String month,
                              @RequestParam(value = "timeid", required = false) String timeid){
        ApiResultBean arb = new ApiResultBean();
        try {
            Object rest = this.insSupplyService.getGxtimeDays(id, year, month, timeid);
            if (rest != null) {
                arb.setData(rest);
            }else {
                Map unrest = new HashMap();
                unrest.put("year", year);
                unrest.put("month", month);
                unrest.put("days", new ArrayList());
                unrest.put("useDays", new ArrayList());
            }
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(),e);
        }

        return arb;
    }

    /**
     * 供需最近的开放年月值
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/firstOpenYMD", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object firstOpenTime(String id, @RequestParam(value = "timeid", required = false) String timeid){
        ApiResultBean arb = new ApiResultBean();

        try {
            Object rest = this.insSupplyService.firstOpenTime(id, timeid);
            arb.setData(rest);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
        }

        return arb;
    }


    /**
     * 取供需配送的市
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/getgxcity", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object getGXcity(String id){
        ApiResultBean arb = new ApiResultBean();
        try {
            Object info = this.insSupplyService.getGXcity(id);
            arb.setData(info);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }



    /**
     * 检测供需信息的可预订时间
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/canApply", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object canApply(String id){
        ApiResultBean arb = new ApiResultBean();
        try {
            boolean canapply = this.insSupplyService.canApply4time(id);
            arb.setData(canapply);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }
}
