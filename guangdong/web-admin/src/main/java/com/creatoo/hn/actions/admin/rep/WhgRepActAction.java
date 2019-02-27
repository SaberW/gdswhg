package com.creatoo.hn.actions.admin.rep;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.services.admin.rep.WhgRepActService;
import com.creatoo.hn.services.admin.rep.WhgTraRepService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/28.
 */
@Controller
@RequestMapping("/admin/rep/act")
public class WhgRepActAction {


    Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private WhgRepActService whgRepActService;

    /**
     * 通过活动类型进行统计
     * @return
     */
    @RequestMapping("/repActEtype")
    @ResponseBody
    public ResponseBean srchActByEtype(){
        ResponseBean res = new ResponseBean();
        try {
            res = whgRepActService.t_srchActByEtype();
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("查询失败");
            log.error(e.getMessage(),e);
        }
        return res;
    }

    /**
     * 通过活动区域进行统计
     * @return
     */
    @RequestMapping("/repActArea")
    @ResponseBody
    public ResponseBean srchActByArea(){
        ResponseBean res = new ResponseBean();
        try {
            res = whgRepActService.t_srchActByArea();
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("查询失败");
            log.error(e.getMessage(),e);
        }
        return res;
    }

    /**
     * 每月发布活动统计
     * @return
     */
    @RequestMapping("/srchActByMonth")
    @ResponseBody
    public Map srchActByMonth(){
        Map rest = new HashMap();
        try {
            rest = whgRepActService.t_srchActByMonth();
        } catch (Exception e) {
            rest.put("categories", new ArrayList<>());
            rest.put("data", new ArrayList<>());
            log.error(e.getMessage(),e);
        }
        return rest;
    }

    /**
     * 查询top10
     * @return
     */
    @RequestMapping("/searchActTop10")
    @ResponseBody
    public ResponseBean searchActTop10(){
        ResponseBean res = new ResponseBean();
        try {
            res = whgRepActService.t_searchActTop10();
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("查询失败");
            log.error(e.getMessage(),e);
        }
        return res;
    }

    /**
     * 活动数据统计
     * @return
     */
    @RequestMapping("/repAct")
    @ResponseBody
    public ResponseBean repAct(HttpServletRequest req){
        ResponseBean res = new ResponseBean();
        try {
            res = whgRepActService.t_repAct(req);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("查询失败");
            log.error(e.getMessage(),e);
        }
        return res;
    }
}
