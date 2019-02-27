package hn.creatoo.com.web.actions.web.usercenter;

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
@RequestMapping(value = "/usercenter")
public class UserCenterAction {

    @RequestMapping(value = "/index")
    public ModelAndView toList(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/usercenter/index");
        SeoUtil.setCommcontent(modelAndView,"个人中心",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/myact")
    public ModelAndView toMyActivity(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/usercenter/avtivity");
        SeoUtil.setCommcontent(modelAndView,"我的活动",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/myvenue")
    public ModelAndView toMyVenue(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/usercenter/venue");
        SeoUtil.setCommcontent(modelAndView,"我的场馆",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/mytrain")
    public ModelAndView toMyTrain(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/usercenter/train");
        SeoUtil.setCommcontent(modelAndView,"我的培训",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/mytraintable")
    public ModelAndView toMyTrainTable(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/usercenter/classTable");
        SeoUtil.setCommcontent(modelAndView,"我的课程表",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/myfeiyi")
    public ModelAndView toFeiYi(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/usercenter/feiyi");
        SeoUtil.setCommcontent(modelAndView,"我的非遗",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/myfavorite")
    public ModelAndView toFavorite(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/usercenter/favorite");
        SeoUtil.setCommcontent(modelAndView,"我的收藏",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/mymessage")
    public ModelAndView toMessage(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/usercenter/message");
        SeoUtil.setCommcontent(modelAndView,"我的消息",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/myGather")
    public ModelAndView toGather(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/usercenter/gather");
        SeoUtil.setCommcontent(modelAndView,"我的众筹",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/myuserinfo")
    public ModelAndView toUserInfo(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/usercenter/userinfo");
        SeoUtil.setCommcontent(modelAndView,"个人信息",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/myusersecurity")
    public ModelAndView toUserSecurity(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/usercenter/security");
        SeoUtil.setCommcontent(modelAndView,"安全中心",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/myusersecurityphone")
    public ModelAndView toUserSecurityPhone(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/usercenter/securityPhone");
        SeoUtil.setCommcontent(modelAndView,"修改绑定手机",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/myusersecurityreal")
    public ModelAndView toUserSecurityReal(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/usercenter/securityUserReal");
        SeoUtil.setCommcontent(modelAndView,"实名认证",null,null);
        return modelAndView;
    }

}
