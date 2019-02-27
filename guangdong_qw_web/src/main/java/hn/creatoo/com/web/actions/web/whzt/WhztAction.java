package hn.creatoo.com.web.actions.web.whzt;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zengrong on 2017/10/27.
 */
@Controller
@RequestMapping(value = "/whzt")
public class WhztAction {
    //包装页面头部SEO信息
    private void setCommcontent(ModelAndView mv,String title,String desc,String key){
        String pageTitle = "文化在线-广东公共数字文化联盟-群众文化资源平台-文化专题";
        String addTitle =  title != null ? pageTitle+"-"+title : pageTitle;
        String addKey = key != null ? key : "文化在线-广东公共数字文化联盟、广东市民文化节、活动、场馆、免费活动、文化活动、文化场馆、活动预约、场馆预订、预订活动、预订场馆、群艺馆、博物馆、美术馆、陈列馆、消费、生活、生活消费、休闲、周末去哪儿、展览、演出、活动、旅行";
        String addDesc = desc != null ? desc : "广东市-一款聚焦文化领域，提供公众文化生活服务和文化消费的文化互联网平台，现汇聚全广东市22万场文化活动、5500余文化场馆资源，为用户提供便捷的文化品质生活服务。";
        mv.addObject("title",addTitle);
        mv.addObject("description",addDesc);
        mv.addObject("keywords",addKey);
    }


    @RequestMapping(value = "/recomment")
    public ModelAndView toWhztRecomment(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/whzt/recomment");
        request.setAttribute("qh",true);
        this.setCommcontent(modelAndView,"推荐",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/index")
    public ModelAndView toWhztIndex(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/whzt/index");
        this.setCommcontent(modelAndView,"首页",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/zpList")
    public ModelAndView toWhztZplist(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/whzt/zpList");
        this.setCommcontent(modelAndView,"作品库",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/rcList")
    public ModelAndView toWhztRcList(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/whzt/rcList");
        this.setCommcontent(modelAndView,"艺术人才",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/jtList")
    public ModelAndView toWhztJtlist(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/whzt/jtList");
        this.setCommcontent(modelAndView,"创作讲坛",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/history")
    public ModelAndView toWhztHistory(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/whzt/history");
        this.setCommcontent(modelAndView,"历届情况",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/winning")
    public ModelAndView toWhztWinning(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/whzt/winning");
        this.setCommcontent(modelAndView,"获奖作品",null,null);
        return modelAndView;
    }

}
