package hn.creatoo.com.web.actions.web.Intangibleheritage;

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
@RequestMapping(value = "/intangibleheritage")
public class IntangiAction {

    @RequestMapping(value = "/index")
    public ModelAndView toIntangiIndex(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/intangibleheritage/index");
        SeoUtil.setCommcontent(modelAndView,"非遗中心",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/creativeSpace")
    public ModelAndView toCreativeSpace(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/intangibleheritage/creativeSpace");
        SeoUtil.setCommcontent(modelAndView,"创意空间",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/customized")
    public ModelAndView toCustomized(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/intangibleheritage/customized");
        SeoUtil.setCommcontent(modelAndView,"非遗定制",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/productDetail")
    public ModelAndView toProductDetail(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/intangibleheritage/productDetail");
        SeoUtil.setCommcontent(modelAndView,"新品详情",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/rostrum")
    public ModelAndView toRostrum(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/intangibleheritage/rostrum");
        SeoUtil.setCommcontent(modelAndView,"非遗大讲坛",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/rostrumDetail")
    public ModelAndView toRostrumDetail(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/intangibleheritage/rostrumDetail");
        SeoUtil.setCommcontent(modelAndView,"非遗大讲坛详情",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/interaction")
    public ModelAndView toInteraction(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/intangibleheritage/interaction");
        SeoUtil.setCommcontent(modelAndView,"非遗互动",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/interactionDetail")
    public ModelAndView toInteractionDetail(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/intangibleheritage/interactionDetail");
        SeoUtil.setCommcontent(modelAndView,"非遗互动详情",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/magazine")
    public ModelAndView toMagazine(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/intangibleheritage/magazine");
        SeoUtil.setCommcontent(modelAndView,"非遗期刊",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/magazineDetail")
    public ModelAndView toMagazineDetail(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/intangibleheritage/magazineDetail");
        SeoUtil.setCommcontent(modelAndView,"非遗期刊详情",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/digitalResources")
    public ModelAndView toDigitalResources(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/intangibleheritage/digitalResources");
        SeoUtil.setCommcontent(modelAndView,"非遗数字资源",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/digitalAudio")
    public ModelAndView toDigitalAudio(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/intangibleheritage/digitalAudio");
        SeoUtil.setCommcontent(modelAndView,"非遗数字音频",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/digitalVideo")
    public ModelAndView toDigitalVideo(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/intangibleheritage/digitalVideo");
        SeoUtil.setCommcontent(modelAndView,"非遗数字视频",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/project")
    public ModelAndView toProject(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/intangibleheritage/project");
        SeoUtil.setCommcontent(modelAndView,"非遗项目",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/projectDetail")
    public ModelAndView toProjectDetail(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/intangibleheritage/projectDetail");
        SeoUtil.setCommcontent(modelAndView,"非遗项目详情",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/subject")
    public ModelAndView toSubject(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/intangibleheritage/subject");
        SeoUtil.setCommcontent(modelAndView,"非遗专题",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/subjectName")
    public ModelAndView toSubjectName(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/intangibleheritage/subjectName");
        SeoUtil.setCommcontent(modelAndView,"非遗专题名称",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/subjectMien")
    public ModelAndView toSubjectMien(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/intangibleheritage/subjectMien");
        SeoUtil.setCommcontent(modelAndView,"非遗专题风采",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/subjectMienAll")
    public ModelAndView toSubjectMienAll(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/intangibleheritage/subjectMienAll");
        SeoUtil.setCommcontent(modelAndView,"非遗专题风采全部",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/subjectActivity")
    public ModelAndView toSubjectActivity(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/intangibleheritage/subjectActivity");
        SeoUtil.setCommcontent(modelAndView,"非遗专题活动",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/digitalPavilion")
    public ModelAndView toDigitalPavilion(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/intangibleheritage/digitalPavilion");
        SeoUtil.setCommcontent(modelAndView,"非遗数字展馆",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/digitalPavilionName")
    public ModelAndView toDigitalPavilionName(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/intangibleheritage/digitalPavilionName");
        SeoUtil.setCommcontent(modelAndView,"非遗数字展馆名称",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/informationTrends")
    public ModelAndView toInformationTrends(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/intangibleheritage/informationTrends");
        SeoUtil.setCommcontent(modelAndView,"资讯动态",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/informationDetail")
    public ModelAndView toInformationDetail(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/intangibleheritage/informationDetail");
        SeoUtil.setCommcontent(modelAndView,"资讯详情",null,null);
        return modelAndView;
    }
}
