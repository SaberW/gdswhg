package hn.creatoo.com.web.actions.web.qwzy;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zengrong on 2017/10/27.
 */
@Controller
@RequestMapping(value = "/qwzy")
public class QwzyAction {

    //包装页面头部SEO信息
    private void setCommcontent(ModelAndView mv,String title,String desc,String key){
        String pageTitle = "文化在线-广东公共数字文化联盟-群众文化资源平台-群文资源";
        String addTitle =  title != null ? pageTitle+"-"+title : pageTitle;
        String addKey = key != null ? key : "文化在线-广东公共数字文化联盟、广东市民文化节、活动、场馆、免费活动、文化活动、文化场馆、活动预约、场馆预订、预订活动、预订场馆、群艺馆、博物馆、美术馆、陈列馆、消费、生活、生活消费、休闲、周末去哪儿、展览、演出、活动、旅行";
        String addDesc = desc != null ? desc : "广东市-一款聚焦文化领域，提供公众文化生活服务和文化消费的文化互联网平台，现汇聚全广东市22万场文化活动、5500余文化场馆资源，为用户提供便捷的文化品质生活服务。";
        mv.addObject("title",addTitle);
        mv.addObject("description",addDesc);
        mv.addObject("keywords",addKey);
    }
    

    @RequestMapping(value = "/index")
    public ModelAndView toQwzyIndex(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/qwzy/index");
        request.setAttribute("qh",true);
        this.setCommcontent(modelAndView,"首页",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/list")
    public ModelAndView toQwzyList(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/qwzy/list");
        request.setAttribute("qh",true);
        modelAndView.addObject("srchkey", request.getParameter("srchkey"));
        this.setCommcontent(modelAndView,"资源列表",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/liblist")
    public ModelAndView toLibQwzyList(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/qwzy/lib_res_list");
        request.setAttribute("qh",true);

        modelAndView.addObject("libname", request.getParameter("libname"));
        modelAndView.addObject("srchkey", request.getParameter("srchkey") == null ? "" : request.getParameter("srchkey"));
        modelAndView.addObject("resourcetype", request.getParameter("resourcetype") == null ? "" : request.getParameter("resourcetype"));
        modelAndView.addObject("arttype", request.getParameter("arttype") == null ? "" : request.getParameter("arttype"));
        modelAndView.addObject("libid", request.getParameter("libid") == null ? "" : request.getParameter("libid"));


        this.setCommcontent(modelAndView,"资源列表",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/detail")
    public ModelAndView toQwzyDetail(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/qwzy/detail");
        modelAndView.addObject("srchkey", request.getParameter("srchkey") == null ? "" : request.getParameter("srchkey"));
        modelAndView.addObject("resclazz", request.getParameter("resclazz") == null ? "" : request.getParameter("resclazz"));
        modelAndView.addObject("type", request.getParameter("type") == null ? "" : request.getParameter("type"));
        modelAndView.addObject("libid", request.getParameter("libid") == null ? "" : request.getParameter("libid"));
        modelAndView.addObject("resid", request.getParameter("resid") == null ? "" : request.getParameter("resid"));
        this.setCommcontent(modelAndView,"详情",null,null);
        return modelAndView;
    }

}
