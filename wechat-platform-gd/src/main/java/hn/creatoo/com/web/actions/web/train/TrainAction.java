package hn.creatoo.com.web.actions.web.train;

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
@RequestMapping(value = "/train")
public class TrainAction {


    @RequestMapping(value = "/index")
    public ModelAndView toIndex(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/train/index");
        SeoUtil.setCommcontent(modelAndView,"首页",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/list")
    public ModelAndView toList(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/train/list");
        SeoUtil.setCommcontent(modelAndView,"培训列表",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/liveList")
    public ModelAndView toLiveList(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/train/liveList");
        SeoUtil.setCommcontent(modelAndView,"在线课程",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/specialty")
    public ModelAndView toSpecialty(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/train/specialty");
        SeoUtil.setCommcontent(modelAndView,"微专业",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/microIndex")
    public ModelAndView toMicroIndex(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/train/microIndex");
        SeoUtil.setCommcontent(modelAndView,"微专业首页",null,null);
        return modelAndView;
    }


    @RequestMapping(value = "/detail")
    public ModelAndView toDetail(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/train/detail");
        SeoUtil.setCommcontent(modelAndView,"培训详情",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/liveDetail")
    public ModelAndView toLiveDetail(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/train/liveDetail");
        SeoUtil.setCommcontent(modelAndView,"在线课程",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/liveNow")
    public ModelAndView toLiveNow(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/train/live");
        SeoUtil.setCommcontent(modelAndView,"正在直播",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/liveBack")
    public ModelAndView liveBack(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/train/live");
        SeoUtil.setCommcontent(modelAndView,"查看回顾",null,null);
        return modelAndView;
    }

    /**
     * 活动直播和线下培训直播
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/liveInfo")
    public ModelAndView liveInfo(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/train/liveinfo");
        SeoUtil.setCommcontent(modelAndView,"直播",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/step")
    public ModelAndView toTrainStep(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/train/step");
        SeoUtil.setCommcontent(modelAndView,"培训预订",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/stepconfirm")
    public ModelAndView toTrainStepconfirm(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/train/stepconfirm");
        SeoUtil.setCommcontent(modelAndView,"培训预订",null,null);
        return modelAndView;
    }


    @RequestMapping(value = "/teachers")
    public ModelAndView toTeachers(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/train/teachers");
        SeoUtil.setCommcontent(modelAndView,"培训师资",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/teachersInfo")
    public ModelAndView toTeachersInfo(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/train/teachersInfo");
        SeoUtil.setCommcontent(modelAndView,"师资详情",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/exponent")
    public ModelAndView toExponent(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/train/exponent");
        SeoUtil.setCommcontent(modelAndView,"培训指数",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/specialtyGroup")
    public ModelAndView toSpecialtyGroup(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/train/specialtyGroup");
        SeoUtil.setCommcontent(modelAndView,"微专业详情",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/specialtyList")
    public ModelAndView toSpecialtyList(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/train/specialtyList");
        SeoUtil.setCommcontent(modelAndView,"微专业",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/resourceList")
    public ModelAndView toResourceList(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/train/resourcelist");
        SeoUtil.setCommcontent(modelAndView,"培训资源列表",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/digitalaudio")
    public ModelAndView toAudioDetail(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/train/digitalaudio");
        SeoUtil.setCommcontent(modelAndView,"音频详情",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/digitalvideo")
    public ModelAndView toVideoDetail(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/train/digitalvideo");
        SeoUtil.setCommcontent(modelAndView,"视频详情",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/digitalimage")
    public ModelAndView toImageDetail(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/train/digitalimage");
        SeoUtil.setCommcontent(modelAndView,"图片详情",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/digitalfile")
    public ModelAndView toFileDetail(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/train/digitalfile");
        SeoUtil.setCommcontent(modelAndView,"文档详情",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/ydgz")
    public ModelAndView toYdgz(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/train/ydgz");
        SeoUtil.setCommcontent(modelAndView,"培训预定规则",null,null);
        return modelAndView;
    }

}
