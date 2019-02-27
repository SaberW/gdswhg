package hn.creatoo.com.web.actions.web.gather;

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
@RequestMapping(value = "/gather")
public class GatherAction {

    @RequestMapping(value = "/index")
    public ModelAndView toIndex(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/gather/index");
        SeoUtil.setCommcontent(modelAndView,"众筹首页",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/list")
    public ModelAndView toList(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/gather/list");
        SeoUtil.setCommcontent(modelAndView,"众筹列表",null,null);
        return modelAndView;
    }


    @RequestMapping(value = "/actdetail")
    public ModelAndView toActDetail(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/gather/actdetail");
        SeoUtil.setCommcontent(modelAndView,"活动众筹",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/traindetail")
    public ModelAndView toTrainDetail(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/gather/traindetail");
        SeoUtil.setCommcontent(modelAndView,"培训众筹",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/otherdetail")
    public ModelAndView toOtherDetail(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/gather/otherdetail");
        SeoUtil.setCommcontent(modelAndView,"众筹详情",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/actliucheng")
    public ModelAndView toActLiuCheng(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/gather/step");
        SeoUtil.setCommcontent(modelAndView,"众筹项目报名流程",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/pxliucheng")
    public ModelAndView toPxLiuCheng(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/gather/step2");
        SeoUtil.setCommcontent(modelAndView,"众筹项目报名流程",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/gatherconfirm")
    public ModelAndView togatherconfirm(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/gather/gatherconfirm");
        SeoUtil.setCommcontent(modelAndView,"预订信息确认",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/brand")
    public ModelAndView toBrand(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/gather/brand");
        SeoUtil.setCommcontent(modelAndView,"品牌列表",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/branddetail")
    public ModelAndView toBrandDetail(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/gather/branddetail");
        SeoUtil.setCommcontent(modelAndView,"品牌详情",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/news")
    public ModelAndView toNews(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/gather/news");
        SeoUtil.setCommcontent(modelAndView,"众筹资讯",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/fagui")
    public ModelAndView toFaGui(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/gather/fagui");
        SeoUtil.setCommcontent(modelAndView,"众筹法规",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/newsdetail")
    public ModelAndView toNewsDetail(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/gather/newsdetail");
        SeoUtil.setCommcontent(modelAndView,"众筹资讯",null,null);
        return modelAndView;
    }

}
