package hn.creatoo.com.web.actions.web.index;

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
public class IndexAction {

    @RequestMapping(value = "/index")
    public ModelAndView toIndex(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("/guangdong/index");
        modelAndView.setViewName("/guangdong/index_");
        SeoUtil.setCommcontent(modelAndView,"首页",null,null);
        System.out.println("========api.root:"+ ConfigUtils.getApiConfig().getRoot()+"==============================================");
        return modelAndView;
    }

    @RequestMapping(value = "/search")
    public ModelAndView toSearch(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/guangdong/search");
        SeoUtil.setCommcontent(modelAndView,"搜索",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/", method = {RequestMethod.GET})
    public ModelAndView toIndex2(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/index");
        return modelAndView;
    }

    @RequestMapping(value = "/changecity", method = {RequestMethod.GET})
    public ModelAndView toChangeCity(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/guangdong/changecity");
        SeoUtil.setCommcontent(modelAndView,"切换城市",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/complete", method = {RequestMethod.GET})
    public ModelAndView toComlpete(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/guangdong/complete");
        SeoUtil.setCommcontent(modelAndView,"预定结果",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/about", method = {RequestMethod.GET})
    public ModelAndView toAbout(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/guangdong/about");
        SeoUtil.setCommcontent(modelAndView,"关于我们",null,null);
        return modelAndView;
    }

}
