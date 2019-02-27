package hn.creatoo.com.web.actions.train;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainsiteErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request){
        //获取statusCode:401,404,500
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if(statusCode == 400){
            return "pages/400";
        }else if(statusCode == 404){
            return "pages/404";
        }else if(statusCode == 405){
            return "pages/405";
        }else{
            return "pages/500";
        }

    }
    @Override
    public String getErrorPath() {
        return "/error";
    }

}
