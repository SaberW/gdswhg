package hn.creatoo.com.web.actions.web.act;

import hn.creatoo.com.web.util.ConfigUtils;
import hn.creatoo.com.web.util.SeoUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by zengrong on 2017/6/1.
 */
@Controller
@RequestMapping(value = "/act")
public class ActAction {

    @RequestMapping(value = "/list")
    public ModelAndView toList(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/act/list");
        SeoUtil.setCommcontent(modelAndView,"活动列表",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/detail")
    public ModelAndView toDetail(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/act/detail");
        SeoUtil.setCommcontent(modelAndView,"活动详情",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/step")
    public ModelAndView toActStep(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/act/step");
        SeoUtil.setCommcontent(modelAndView,"活动预定",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/actconfirm")
    public ModelAndView toActConfirm(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/act/actconfirm");
        SeoUtil.setCommcontent(modelAndView,"预订信息确认",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/readme")
    public ModelAndView toReadme(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/act/readme");
        SeoUtil.setCommcontent(modelAndView,"活动预订规则",null,null);
        return modelAndView;
    }

}
