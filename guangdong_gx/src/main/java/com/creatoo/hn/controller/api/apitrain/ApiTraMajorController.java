package com.creatoo.hn.controller.api.apitrain;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgTraMajor;
import com.creatoo.hn.services.admin.system.WhgSystemDeptService;
import com.creatoo.hn.services.api.apitrain.ApiTraMajorService;
import com.creatoo.hn.util.FilterFontUtil;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 培训微专业接口
 * Created by Administrator on 2017/10/11.
 */
@CrossOrigin
@Controller
@RequestMapping("/api/px/major")
public class ApiTraMajorController extends BaseController{

    @Autowired
    private ApiTraMajorService apiTraMajorService;

    @Autowired
    private WhgSystemDeptService whgSystemDeptService;

    /**
     * 微专业列表页
     * @param page 当前页
     * @param pageSize  页列表数量
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/list",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean indexData(HttpServletRequest req,
                                   @RequestParam(value = "page", defaultValue = "1")int page,
                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        ApiResultBean rep = new ApiResultBean();
        Map recode = new HashMap();

        try {
            String  cultid=req.getParameter("cultid");
            //区分全省、全市站
            if (cultid == null || cultid.isEmpty()){
                recode.put("allprovince", true);
            }
            if (cultid != null && cultid.contains(",")){
                recode.put("allcity", true);
            }

            if(cultid!=null&&!cultid.equals("")){
                recode.put("cultid",Arrays.asList(cultid.replaceAll(","," ").trim().split(" ")));
            }
            String deptid = req.getParameter("deptid");
            if(deptid !=null && !"".equals(deptid)){
                recode.put("deptid",whgSystemDeptService.srchDeptStrList(deptid));
            }
            recode.put("etype", req.getParameter("etype"));
            recode.put("province", req.getParameter("province"));
            recode.put("city", req.getParameter("city"));
            recode.put("area", req.getParameter("area"));
            recode.put("name", req.getParameter("name"));

            PageInfo info = this.apiTraMajorService.getTraMajorList(page,pageSize,recode);
            rep.setPageInfo(info);
            rep.setRows(info.getList());
        } catch (Exception e) {
            rep.setCode(101);
            rep.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return rep;
    }

    /**
     * 根据微专业id查询培训
     * @param id
     * @param pageSize
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping("/getTra")
    public ApiResultBean getTra(@RequestParam(value = "id",required = true)String id, @RequestParam(value = "page", defaultValue = "1")int page,
    @RequestParam(value = "pageSize", defaultValue = "4") int pageSize){
        ApiResultBean arb = new ApiResultBean();
        try {
            PageInfo info = this.apiTraMajorService.getTra4mid(id,page,pageSize);
            arb.setPageInfo(info);
            arb.setRows(info.getList());

            arb.setData(new Date());
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 微专业详情
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/detail",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean majorDetail(@RequestParam(value = "id",required = false)String id){
        ApiResultBean arb = new ApiResultBean();
        if(null == id){
            arb.setCode(101);
            arb.setMsg("微专业ID不能为空");
            return arb;
        }
        try {
            WhgTraMajor major = apiTraMajorService.getMajorById(id);
            arb.setData(FilterFontUtil.clearFont(major));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            arb.setCode(102);
            arb.setMsg("查询数据失败");
        }
        return arb;
    }

    /**
     * 根据微专业id查询师资
     * @param id
     * @param page
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping("/getTea")
    public ApiResultBean getTea(@RequestParam(value = "id",required = true)String id,@RequestParam(value = "page", defaultValue = "1")int page,
                                @RequestParam(value = "pageSize", defaultValue = "4") int pageSize){
        ApiResultBean arb = new ApiResultBean();
        try {
            PageInfo info = this.apiTraMajorService.getTea4mid(id,page,pageSize);
            arb.setPageInfo(info);
            arb.setRows(info.getList());
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 根据微专业id查询师资
     * @param id
     * @param page
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping("/getDrsc")
    public ApiResultBean getDrsc(@RequestParam(value = "id",required = true)String id,
                                 @RequestParam(value = "page", defaultValue = "1")int page,
                                 @RequestParam(value = "pageSize", defaultValue = "4") int pageSize,
                                 @RequestParam(value = "etype",required = true)String etype){
        ApiResultBean arb = new ApiResultBean();
        try {
            PageInfo info = this.apiTraMajorService.getDrsc4mid(id,page,pageSize,etype);
            arb.setPageInfo(info);
            arb.setRows(info.getList());
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }
}
