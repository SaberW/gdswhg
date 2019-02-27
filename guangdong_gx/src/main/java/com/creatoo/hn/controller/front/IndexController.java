package com.creatoo.hn.controller.front;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysRole;
import com.creatoo.hn.services.front.FrontIndexService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WEB 网站首页控制器
 * Created by wangXL on 2017/7/3.
 */
@RestController
public class IndexController extends BaseController {

    @Autowired
    private FrontIndexService frontIndexService;

    /**
     * 访问系统首页
     * @return 系统首页
     */
    /*@RequestMapping(value = "/", method = {RequestMethod.GET})
    public ModelAndView index(String aCurtPage, String aPageSize, String aSort, String aOrder, WhgSysRole role){
        ModelAndView view = new ModelAndView();
        try{
            //return new ModelAndView("admin/main");
            return new ModelAndView("redirect: /web");
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return view;
    }*/

    /**
     * 访问系统首页
     * @return 系统首页
     */
    @RequestMapping(value = "/home", method = {RequestMethod.GET})
    public ModelAndView home(){
        ModelAndView view = new ModelAndView();
        try{
            view.setViewName("front/index");
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        if(true)throw new RuntimeException("wxl");
        return view;
    }

    @RequestMapping(value = "/data", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getData(){
        Map<String, Object> map = new HashMap<>();
        map.put("name", "wangxl");
        map.put("demo", "wangxl-王新林");
        return map;
    }

    @RequestMapping(value = "/add", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView add(){
        ModelAndView view = new ModelAndView();
        List<WhgSysRole> roles = new ArrayList<>();
        try{
            view.setViewName("front/index");
            log.error("wxl error==add==env:"+this.env.getProperty("profile.val")+"=========================");

            WhgSysRole role = this.frontIndexService.add();
            roles.add(role);
            view.addObject("roles", roles);
            log.error("wxl error==add==role:"+role+"=========================");
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return view;
    }

    @RequestMapping(value = "/get", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView get(String id){
        ModelAndView view = new ModelAndView();
        try{
            WhgSysRole role = this.frontIndexService.get(id);
            List<WhgSysRole> roles = new ArrayList<>();
            roles.add(role);
            view.addObject("roles", roles);
            log.error("wxl error==get==role:"+role+"=========================");
            view.setViewName("front/index");
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return view;
    }


    @RequestMapping(value = "/edit", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView edit(String id, String name){
        ModelAndView view = new ModelAndView();
        try{
            WhgSysRole role = new WhgSysRole();
            role.setId(id);
            role.setName(name);
            role = this.frontIndexService.edit(role);
            List<WhgSysRole> roles = new ArrayList<>();
            roles.add(role);
            view.addObject("roles", roles);
            log.error("wxl error==edit==role:"+role+"=========================");
            view.setViewName("front/index");
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return view;
    }
}
