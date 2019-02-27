package com.creatoo.hn.controller.api.apitrain;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.services.api.apitrain.ApiTraIndexService;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 培训首页接口
 * Created by Administrator on 2017/10/9.
 */
@Controller
@CrossOrigin
@RequestMapping("/api/px")
public class ApiTraIndexController extends BaseController{

    @Autowired
    private ApiTraIndexService apiTraIndexService;


    /**
     * 查询首页的培训
     * @param cultid
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/indexTraList", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ApiResultBean getindexTraList(@RequestParam(value = "cultid", required = false)String cultid,@RequestParam(required = false, value = "size") Integer size){
        ApiResultBean arb = new ApiResultBean();
        List list = new ArrayList();
        String protype = EnumProject.PROJECT_WLPX.getValue();
        try {
            list = apiTraIndexService.t_getindexTraList(cultid,size,protype);
            arb.setData(list);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            arb.setData(new ArrayList());
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 查询首页的培训
     * @param cultid
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/indexTraliveList", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ApiResultBean getindexTraliveList(@RequestParam(value = "cultid", required = false)String cultid,@RequestParam(required = false, value = "size") Integer size){
        ApiResultBean arb = new ApiResultBean();
        List list = new ArrayList();
        String protype = EnumProject.PROJECT_WLPX.getValue();
        try {
            list = apiTraIndexService.t_getindexTraliveList(cultid,size,protype);
            arb.setData(list);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            arb.setData(new ArrayList());
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 查询首页的微专业
     * @param cultid
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/indexTraMajorList", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ApiResultBean getindexTraMajorList(@RequestParam(value = "cultid", required = false)String cultid,@RequestParam(required = false, value = "size") Integer size){
        ApiResultBean arb = new ApiResultBean();
        List list = new ArrayList();
        try {
            list = apiTraIndexService.t_getindexTraMajorList(cultid,size);
            arb.setData(list);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            arb.setData(new ArrayList());
            log.error(e.getMessage(), e);
        }
        return arb;
    }
}
