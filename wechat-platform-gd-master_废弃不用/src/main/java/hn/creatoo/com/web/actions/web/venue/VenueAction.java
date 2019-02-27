package hn.creatoo.com.web.actions.web.venue;

import hn.creatoo.com.web.util.SeoUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by zengrong on 2017/6/1.
 */
@Controller
@RequestMapping(value = "/venue")
public class VenueAction {

    @RequestMapping(value = "/list")
    public ModelAndView toList(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/venue/list");
        SeoUtil.setCommcontent(modelAndView,"场馆列表",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/detail")
    public ModelAndView toDetail(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/venue/detail");
        SeoUtil.setCommcontent(modelAndView,"场馆详情",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/readme")
    public ModelAndView toReadme(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/venue/readme");
        SeoUtil.setCommcontent(modelAndView,"场馆预订规则",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/roomList")
    public ModelAndView toRoomList(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/venue/roomlist");
        SeoUtil.setCommcontent(modelAndView,"活动室列表",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/roomDetail")
    public ModelAndView toRoomDetail(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/venue/roomDetail");
        SeoUtil.setCommcontent(modelAndView,"活动室详情",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/step")
    public ModelAndView toVenueStep(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/venue/step");
        SeoUtil.setCommcontent(modelAndView,"活动室预订",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/stepconfirm")
    public ModelAndView toStepConfirm(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/venue/stepconfirm");
        SeoUtil.setCommcontent(modelAndView,"活动室预订",null,null);
        return modelAndView;
    }

}
