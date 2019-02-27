package hn.creatoo.com.web.actions.train.xxpx;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zengrong on 2017/6/1.
 */
@Controller
@RequestMapping(value = "/{cultsite}/xxpx")
public class XxpxAction {

    //包装页面头部SEO信息
    private void setCommcontent(ModelAndView mv,String title,String desc,String key){
        String pageTitle = "文化在线-广东公共数字文化联盟-线下培训";
        String addTitle =  title != null ? pageTitle+"-"+title : pageTitle;
        String addKey = key != null ? key : "文化在线-广东公共数字文化联盟、广东市民文化节、活动、场馆、免费活动、文化活动、文化场馆、活动预约、场馆预订、预订活动、预订场馆、群艺馆、博物馆、美术馆、陈列馆、消费、生活、生活消费、休闲、周末去哪儿、展览、演出、活动、旅行";
        String addDesc = desc != null ? desc : "广东市-一款聚焦文化领域，提供公众文化生活服务和文化消费的文化互联网平台，现汇聚全广东市22万场文化活动、5500余文化场馆资源，为用户提供便捷的文化品质生活服务。";
        mv.addObject("title",addTitle);
        mv.addObject("description",addDesc);
        mv.addObject("keywords",addKey);
    }

    @RequestMapping(value = "/list")
    public ModelAndView toList(HttpServletRequest request,@PathVariable("cultsite") String cultsite){
        request.setAttribute("cultsite",cultsite);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong_gx/xxpx/list");
        request.setAttribute("qh",true);
        this.setCommcontent(modelAndView,"线下培训列表",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/detail")
    public ModelAndView toDetail(HttpServletRequest request,@PathVariable("cultsite") String cultsite){
        request.setAttribute("cultsite",cultsite);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong_gx/xxpx/detail");
        this.setCommcontent(modelAndView,"线下培训详情",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/step")
    public ModelAndView toStep(HttpServletRequest request,@PathVariable("cultsite") String cultsite){
        request.setAttribute("cultsite",cultsite);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong_gx/xxpx/step");
        this.setCommcontent(modelAndView,"在线报名",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/live")
    public ModelAndView live(HttpServletRequest request,@PathVariable("cultsite") String cultsite){
        request.setAttribute("cultsite",cultsite);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong_gx/xxpx/live");
        request.setAttribute("id", request.getParameter("id"));//培训标识
        this.setCommcontent(modelAndView,"培训直播",null,null);
        return modelAndView;
    }
}
