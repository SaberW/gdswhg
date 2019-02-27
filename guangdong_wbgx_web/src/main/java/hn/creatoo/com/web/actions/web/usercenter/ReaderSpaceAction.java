package hn.creatoo.com.web.actions.web.usercenter;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/8/18.
 */
@Controller
@RequestMapping("/{cultsite}/center/reader")
public class ReaderSpaceAction {

    private static Logger logger = Logger.getLogger(ReaderSpaceAction.class);

    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request,@PathVariable("cultsite") String cultsite){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("title","读者空间");
        modelAndView.setViewName("guangdong/userCenter/readerSpace");
        request.setAttribute("cultsite",cultsite);
        return modelAndView;
    }

    @RequestMapping("/detail")
    public ModelAndView detail(HttpServletRequest request,@PathVariable("cultsite") String cultsite){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("title","读者空间");
        request.setAttribute("cultsite",cultsite);

        modelAndView.setViewName("guangdong/userCenter/reader");
        return modelAndView;
    }

}
