package hn.creatoo.com.web.actions.web.pop;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**热门资源页面控制器
 * Created by caiyong on 2017/8/4.
 */
@Controller
@RequestMapping("/pop")
public class PopResourceAction {

    private static Logger logger = Logger.getLogger(PopResourceAction.class);

    @RequestMapping("/list")
    public ModelAndView list(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        String pageTitle = "文化在线-广东公共数字文化联盟-热门资源";
        modelAndView.addObject("title",pageTitle);
        request.setAttribute("qh",true);
        modelAndView.setViewName("/pop/resourcePage");
        return modelAndView;
    }

}
