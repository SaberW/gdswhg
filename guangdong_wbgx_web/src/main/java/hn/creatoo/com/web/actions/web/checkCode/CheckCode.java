package hn.creatoo.com.web.actions.web.checkCode;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/check")
public class CheckCode{
    @ResponseBody
    @RequestMapping(value = "/checkCode")
    public String checkCode(String checkCode, HttpServletRequest request) {
        String re="1";
        if (!checkCode.equalsIgnoreCase((String) request.getSession().getAttribute("randCheckCode"))) {
            re="0";
        }
        return re;
    }

}
