package com.creatoo.hn.controller.api.apitrain;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgTraTeacher;
import com.creatoo.hn.services.admin.system.WhgSystemDeptService;
import com.creatoo.hn.services.api.apitrain.ApiTeacherService;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 培训老师的接口
 * Created by Administrator on 2017/10/10.
 */
@CrossOrigin
@Controller
@RequestMapping("/api/px/tea")
public class ApiTraTeacherController extends BaseController{

    @Autowired
    private ApiTeacherService apiTeacherService;

    @Autowired
    private WhgSystemDeptService whgSystemDeptService;

    /**
     * 查询培训老师列表
     * @param page
     * @param pageSize
     * @param req
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/list",method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ApiResultBean teaList(HttpServletRequest req,
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
            String  deptid = req.getParameter("deptid");
            if(deptid != null && !"".equals(deptid)){
                recode.put("deptid", whgSystemDeptService.srchDeptStrList(deptid));
            }
            recode.put("arttype", req.getParameter("arttype"));
            recode.put("title", req.getParameter("title"));
            recode.put("province", req.getParameter("province"));
            recode.put("city", req.getParameter("city"));
            recode.put("area", req.getParameter("area"));

            PageInfo info = this.apiTeacherService.getTeaList(page,pageSize,recode);
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
    @RequestMapping(value = "/detail",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean teaDetail(String id){
        ApiResultBean arb = new ApiResultBean();
        try {
            WhgTraTeacher teacher = this.apiTeacherService.selTeaById(id);
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
    public ApiResultBean getRecommendTea(@RequestParam(value = "name",required = true)String name,
                                         @RequestParam(value = "cultid",required = false)String cultid,
                                         @RequestParam(value = "size",required = false)Integer size){
        ApiResultBean arb = new ApiResultBean();
        try {
            Map data = new HashMap();
            PageInfo pageInfo = this.apiTeacherService.getTraByTea(name,cultid,size);
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
