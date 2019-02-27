package com.creatoo.hn.actions.admin.rep;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.services.admin.rep.RepVenService;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

/**
 * Created by rbg on 2017/6/26.
 */

@Controller
@RequestMapping("/admin/rep/ven")
public class WhgRepVenAction {
    Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private RepVenService repVenService;

    /**
     * 按类型统计发布数量
     * @return
     */
    @RequestMapping("/chart4type")
    @ResponseBody
    public Object chart4type(){
        Map rest = new HashMap();

        try {
            rest = this.repVenService.count4Type();
        } catch (Exception e) {
            rest.put("categories", new ArrayList<>());
            rest.put("data", new ArrayList<>());
            log.error(e.getMessage(), e);
        }

        return rest;
    }

    /**
     * 按区域统计发布数量
     * @return
     */
    @RequestMapping("/chart4area")
    @ResponseBody
    public Object chart4area(){
        Map rest = new HashMap();

        try {
            rest = this.repVenService.count4Area();
        } catch (Exception e) {
            rest.put("categories", new ArrayList<>());
            rest.put("data", new ArrayList<>());
            log.error(e.getMessage(), e);
        }

        return rest;
    }

    /**
     * 年的月份统计发布数量
     * @return
     */
    @RequestMapping("/chart4year")
    @ResponseBody
    public Object chart4yrea(){
        Map rest = new HashMap();

        try {
            rest = this.repVenService.count4Year();
        } catch (Exception e) {
            rest.put("categories", new ArrayList<>());
            rest.put("data", new ArrayList<>());
            log.error(e.getMessage(), e);
        }

        return rest;
    }

    /**
     * 访问量TOP10
     * @return
     */
    @RequestMapping("/listTop10")
    @ResponseBody
    public Object listTop10(){
        try {
            return this.repVenService.selectCount4Top10();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * 场馆开放率统计
     * @param request
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/listTimeOpen")
    @ResponseBody
    public Object listTimeOpen(WebRequest request, int page, int rows){
        ResponseBean resb = new ResponseBean();
        try{
            String title = request.getParameter("title");
            PageInfo pageInfo = this.repVenService.selectTime4RoomOpen(page, rows, title);
            resb.setRows( pageInfo.getList() );
            resb.setTotal( pageInfo.getTotal() );
        }catch (Exception e){
            log.error(e.getMessage(), e);
            resb.setRows( new ArrayList() );
            resb.setSuccess(ResponseBean.FAIL);
        }
        return resb;
    }

    /**
     * 场馆使用率统计
     * @param request
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/listTimeOrder")
    @ResponseBody
    public Object listTimeOrder(WebRequest request, int page, int rows){
        ResponseBean resb = new ResponseBean();
        try{
            String title = request.getParameter("title");
            PageInfo pageInfo = this.repVenService.selectTime4RoomOrder(page, rows, title);
            resb.setRows( pageInfo.getList() );
            resb.setTotal( pageInfo.getTotal() );
        }catch (Exception e){
            log.error(e.getMessage(), e);
            resb.setRows( new ArrayList() );
            resb.setSuccess(ResponseBean.FAIL);
        }
        return resb;
    }
}
