package hn.creatoo.com.web.actions;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by rbg on 2018/1/24.
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping("/utils")
public class UtilsAction {

    @RequestMapping("/callpage")
    public String formCallPage(HttpServletRequest request, ModelMap mmp){
        return "/formcallpage";
    }
}
