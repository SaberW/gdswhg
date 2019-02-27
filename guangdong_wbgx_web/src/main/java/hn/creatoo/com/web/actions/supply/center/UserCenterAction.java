package hn.creatoo.com.web.actions.supply.center;

        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.servlet.ModelAndView;

        import javax.servlet.http.HttpServletRequest;

/**
 * Created by zengrong on 2017/6/1.
 */
@Controller
@RequestMapping(value = "/res-center")
public class UserCenterAction {
    //包装页面头部SEO信息
    private void setCommcontent(ModelAndView mv,String title,String desc,String key){
        String pageTitle = "文化在线-广东公共数字文化联盟-内部供需-个人中心";
        String addTitle =  title != null ? pageTitle+"-"+title : pageTitle;
        String addKey = key != null ? key : "文化在线-广东公共数字文化联盟、广东市民文化节、活动、场馆、免费活动、文化活动、文化场馆、活动预约、场馆预订、预订活动、预订场馆、群艺馆、博物馆、美术馆、陈列馆、消费、生活、生活消费、休闲、周末去哪儿、展览、演出、活动、旅行";
        String addDesc = desc != null ? desc : "广东市-一款聚焦文化领域，提供公众文化生活服务和文化消费的文化互联网平台，现汇聚全广东市22万场文化活动、5500余文化场馆资源，为用户提供便捷的文化品质生活服务。";
        mv.addObject("title",addTitle);
        mv.addObject("description",addDesc);
        mv.addObject("keywords",addKey);
    }

    @RequestMapping(value = "/mySupplyList")
    public ModelAndView toMySupplyList(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong_gx/center/mySupplyList");
        this.setCommcontent(modelAndView,"我发布的配送",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/myApplyList")
    public ModelAndView toMyApplyList(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong_gx/center/myApplyList");
        this.setCommcontent(modelAndView,"我申请的配送",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/stepOpenDay")
    public ModelAndView toStepOpenDay(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong_gx/center/stepOpenDay");
        this.setCommcontent(modelAndView,"我申请的配送",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/myMessage")
    public ModelAndView toMyMessage(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong_gx/center/message");
        this.setCommcontent(modelAndView,"我的消息",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/editUserInfo")
    public ModelAndView toEditUserInfo(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong_gx/center/editUserInfo");
        this.setCommcontent(modelAndView,"编辑资料",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/myFavorite")
    public ModelAndView toMyFavorite(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong_gx/center/myFavoriteList");
        this.setCommcontent(modelAndView,"我的收藏",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/index")
    public ModelAndView toCenterIndex(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong_gx/center/index");
        this.setCommcontent(modelAndView,"首页",null,null);
        return modelAndView;
    }
}
