package hn.creatoo.com.web.actions.web.resource;

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
@RequestMapping(value = "/resource")
public class ResourceAction {

    @RequestMapping(value = "/index")
    public ModelAndView toList(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/resource/index");
        SeoUtil.setCommcontent(modelAndView,"群文资源首页",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/whzt")
    public ModelAndView toWhzt(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/resource/whzt");
        SeoUtil.setCommcontent(modelAndView,"文化专题",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/whztdetail")
    public ModelAndView toWhztDetail(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/resource/whztdetail");
        SeoUtil.setCommcontent(modelAndView,"专题详情",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/ysrc")
    public ModelAndView toYsrc(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/resource/ysrc");
        SeoUtil.setCommcontent(modelAndView,"艺术人才",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/ysrcdetail")
    public ModelAndView toYsrcDetail(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/resource/ysrcdetail");
        SeoUtil.setCommcontent(modelAndView,"艺术人才详情",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/szzy")
    public ModelAndView toSzzy(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/resource/szzy");
        SeoUtil.setCommcontent(modelAndView,"数字资源",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/news")
    public ModelAndView toNews(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/resource/news");
        SeoUtil.setCommcontent(modelAndView,"资讯列表",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/newsdetail")
    public ModelAndView toNewsDetail(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/resource/newsdetail");
        SeoUtil.setCommcontent(modelAndView,"资讯详情",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/resourcelist")
    public ModelAndView toResList(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/resource/resourcelist");
        SeoUtil.setCommcontent(modelAndView,"资源列表",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/digitalaudio")
    public ModelAndView toAudioDetail(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/resource/digitalaudio");
        SeoUtil.setCommcontent(modelAndView,"音频详情",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/digitalvideo")
    public ModelAndView toVideoDetail(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/resource/digitalvideo");
        SeoUtil.setCommcontent(modelAndView,"视频详情",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/digitalimage")
    public ModelAndView toImageDetail(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/resource/digitalimage");
        SeoUtil.setCommcontent(modelAndView,"图片详情",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/digitalfile")
    public ModelAndView toFileDetail(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/resource/digitalfile");
        SeoUtil.setCommcontent(modelAndView,"文档详情",null,null);
        return modelAndView;
    }

}
