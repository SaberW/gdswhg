/*
package hn.creatoo.com.web.actions.web.usercenter;

import com.google.gson.JsonObject;
import hn.creatoo.com.web.interceptor.LoginInterceptor;
import hn.creatoo.com.web.mode.WhUser;
import hn.creatoo.com.web.util.JsonUtils;
import hn.creatoo.com.web.util.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

*/
/**
 * Created by zengrong on 2017/6/1.
 *//*

@Controller
public class Login {
    */
/**
     * Restful调用接口工具
     *//*

    @Autowired
    private RestTemplate restTemplate;

    //包装页面头部SEO信息
    private void setCommcontent(ModelAndView mv, String title,String desc,String key){
        String pageTitle = "广东省文化馆联盟-";
        String addTitle =  title != null ? pageTitle+"-"+title : pageTitle;
        String addKey = key != null ? key : "广东省文化馆联盟、广东市民文化节、活动、场馆、免费活动、文化活动、文化场馆、活动预约、场馆预订、预订活动、预订场馆、群艺馆、博物馆、美术馆、陈列馆、消费、生活、生活消费、休闲、周末去哪儿、展览、演出、活动、旅行";
        String addDesc = desc != null ? desc : "广东市-一款聚焦文化领域，提供公众文化生活服务和文化消费的文化互联网平台，现汇聚全广东市22万场文化活动、5500余文化场馆资源，为用户提供便捷的文化品质生活服务。";
        mv.addObject("title",addTitle);
        mv.addObject("description",addDesc);
        mv.addObject("keywords",addKey);
    }
    private void setCommcontent(RedirectAttributes mv, String title,String desc,String key){
        String pageTitle = "广东省文化馆联盟-";
        String addTitle =  title != null ? pageTitle+"-"+title : pageTitle;
        String addKey = key != null ? key : "广东省文化馆联盟、广东市民文化节、活动、场馆、免费活动、文化活动、文化场馆、活动预约、场馆预订、预订活动、预订场馆、群艺馆、博物馆、美术馆、陈列馆、消费、生活、生活消费、休闲、周末去哪儿、展览、演出、活动、旅行";
        String addDesc = desc != null ? desc : "广东市-一款聚焦文化领域，提供公众文化生活服务和文化消费的文化互联网平台，现汇聚全广东市22万场文化活动、5500余文化场馆资源，为用户提供便捷的文化品质生活服务。";
        mv.addFlashAttribute("title",addTitle);
        mv.addFlashAttribute("description",addDesc);
        mv.addFlashAttribute("keywords",addKey);
    }

    */
/*@RequestMapping(value = "/login")
    public ModelAndView toLogin(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("regist/login");
        this.setCommcontent(modelAndView,"登录",null,null);
        return modelAndView;
    }*//*


    @RequestMapping(value = "/readme")
    public ModelAndView toReadme(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/regist/readme");
        this.setCommcontent(modelAndView,"注册-会员注册条款",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/regist")
    public ModelAndView toRegist(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/regist/regist");
        this.setCommcontent(modelAndView,"注册-填写资料",null,null);
        return modelAndView;
    }

    @RequestMapping(value = "/regist-2")
    public ModelAndView toRegistI(HttpServletRequest request, RedirectAttributes model){
        ModelAndView modelAndView = new ModelAndView("guangdong/regist/regist-2");
        String id = request.getParameter("temp_id");
        if(id == null || StringUtils.isEmpty(id)){
            modelAndView.setViewName("redirect:regist");
            this.setCommcontent(model,"注册-填写资料",null,null);
        }else{
            Map<String,String> params = new HashMap<String,String>();
            params.put("userId", id);
            String a = RestUtils.post(restTemplate, "api/user/detail", params);
            //获取返回值
            JsonObject json = JsonUtils.toJsonObj(a);//gson.fromJson(a, JsonObject.class);
            String code = json.get("code").getAsString();
            if("0".equals(code)) {
                WhUser user = JsonUtils.toJavaObj(json.get("data").toString(), WhUser.class);
                request.getSession().setAttribute(LoginInterceptor.sessionUserKey, user);
            }
            this.setCommcontent(modelAndView,"注册-完善资料",null,null);
            modelAndView.addObject("temp_id",id);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/regist-3")
    public ModelAndView toRegistII(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("guangdong/regist/regist-3");
        this.setCommcontent(modelAndView,"注册-注册成功",null,null);
        return modelAndView;
    }
}
*/
