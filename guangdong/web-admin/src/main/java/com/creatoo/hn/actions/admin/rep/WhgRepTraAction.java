package com.creatoo.hn.actions.admin.rep;

import com.creatoo.hn.ext.bean.ResponseBean;
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
 * Created by Administrator on 2017/6/26.
 */
@Controller
@RequestMapping("/admin/rep")
public class WhgRepTraAction {

    Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private WhgTraRepService whgTraRepService;

    /**
     * 通过培训类型进行统计
     * @return
     */
    @RequestMapping("/repTraEtype")
    @ResponseBody
    public ResponseBean srchTraByEtype(){
        ResponseBean res = new ResponseBean();
        try {
            res = whgTraRepService.t_srchTraByEtype();
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("查询失败");
            log.error(e.getMessage(),e);
        }
        return res;
    }

    /**
     * 通过培训区域进行统计
     * @return
     */
    @RequestMapping("/repTraArea")
    @ResponseBody
    public ResponseBean srchTraByArea(){
        ResponseBean res = new ResponseBean();
        try {
            res = whgTraRepService.t_srchTraByArea();
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("查询失败");
            log.error(e.getMessage(),e);
        }
        return res;
    }

    /**
     * 每月发布培训统计
     * @return
     */
    @RequestMapping("/srchTraByMonth")
    @ResponseBody
    public Map srchTraByMonth(){
        Map rest = new HashMap();
        try {
            rest = whgTraRepService.t_srchTraByMonth();
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
    @RequestMapping("/searchTraTop10")
    @ResponseBody
    public ResponseBean searchTraTop10(){
        ResponseBean res = new ResponseBean();
        try {
            res = whgTraRepService.t_searchTraTop10();
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("查询失败");
            log.error(e.getMessage(),e);
        }
        return res;
    }

    /**
     * 培训数据统计
     * @return
     */
    @RequestMapping("/reptra")
    @ResponseBody
    public ResponseBean reptra(HttpServletRequest req){
        ResponseBean res = new ResponseBean();
        try {
            res = whgTraRepService.t_reptra(req);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("查询失败");
            log.error(e.getMessage(),e);
        }
        return res;
    }
}
