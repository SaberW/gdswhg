package com.creatoo.hn.controller.api.apitrain;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgDrsc;
import com.creatoo.hn.services.admin.system.WhgSystemDeptService;
import com.creatoo.hn.services.api.apitrain.ApiTraResourceService;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumProject;
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
 * Created by Administrator on 2017/10/10.
 */
@CrossOrigin
@Controller
@RequestMapping("/api/px/drsc")
public class ApiTraResourceController extends BaseController{

    @Autowired
    private ApiTraResourceService apiTraResourceService;

    @Autowired
    private WhgSystemDeptService whgSystemDeptService;
    /**
     * 培训资源列表
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list",method = {RequestMethod.POST,RequestMethod.GET})
    @CrossOrigin
    @ResponseBody
    public ApiResultBean descList(HttpServletRequest req,
                                  @RequestParam(value = "page", defaultValue = "1")int page,
                                  @RequestParam(value = "pageSize", defaultValue = "10")int pageSize){
        ApiResultBean arb = new ApiResultBean();
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
                recode.put("cultid", Arrays.asList(cultid.replaceAll(","," ").trim().split(" ")));
            }
            String deptid = req.getParameter("deptid");
            if(deptid != null && !"".equals(deptid)){
                recode.put("deptid", whgSystemDeptService.srchDeptStrList(deptid));
            }
            recode.put("arttype", req.getParameter("arttype"));
            recode.put("etype", req.getParameter("etype"));
            recode.put("title", req.getParameter("title"));
            recode.put("province", req.getParameter("province"));
            recode.put("city", req.getParameter("city"));
            recode.put("area", req.getParameter("area"));
            recode.put("sort", req.getParameter("sort"));

            PageInfo info = this.apiTraResourceService.findList(page,pageSize,recode);
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
     * 培训资源详情
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value="/detail",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean descDetail(String id){
        ApiResultBean arb = new ApiResultBean();
        try {
            WhgDrsc desc = this.apiTraResourceService.selDescById(id);
            arb.setData(desc);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            arb.setCode(101);
            arb.setMsg("查询失败");
        }
        return arb;
    }

    /**
     * 获取推荐培训资源
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
            PageInfo pageInfo = apiTraResourceService.getRecommendDesc(id,cultid,size);
            arb.setRows(pageInfo.getList());
            arb.setPageInfo(pageInfo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            arb.setCode(101);
            arb.setMsg("查询数据失败");
        }
        return arb;
    }
}
