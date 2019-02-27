/*
package hn.creatoo.com.web.actions.web.usercenter;

import com.google.gson.JsonObject;
import hn.creatoo.com.web.interceptor.LoginInterceptor;
import hn.creatoo.com.web.mode.WhUser;
import hn.creatoo.com.web.util.JsonUtils;
import hn.creatoo.com.web.util.RestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

*/
/**
 * 登录处理会话
 * Created by wangxl on 2017/6/1.
 *//*

@RestController
public class LoginAction {
    */
/**
     * Restful调用接口工具
     *//*

    @Autowired
    private RestTemplate restTemplate;

    */
/**
     * 跳转到登录页面
     *
     * @return
     *//*

    @RequestMapping("/login")
    public ModelAndView toLogin(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView();
        String url = request.getHeader("Referer");
        view.addObject("temp_url",url);
        view.setViewName("guangdong/regist/login");
        return view;
    }

    */
/**
     * 登录
     *
     * @return
     *//*

    @RequestMapping("/doLogin")
    public ModelAndView doLogin(HttpServletRequest request, HttpServletResponse response,RedirectAttributes model) {
        ModelAndView view = new ModelAndView();
        try {
            //params
            String phoneNum = request.getParameter("phoneNum");
            String password = request.getParameter("password");
            Map<String,String> params = new HashMap<String,String>();
            params.put("userName", phoneNum);
            params.put("password", password);

            //调用登录接口
            String a = RestUtils.post(restTemplate, "api/user/doLogin", params);//restTemplate.postForObject("http://localhost:8080/api/doLogin", reqJsonStr, String.class);
            //System.out.println("====================a:" + a + "============================");

            //获取返回值
            JsonObject json = JsonUtils.toJsonObj(a);//gson.fromJson(a, JsonObject.class);
            String code = json.get("code").getAsString();
            if("0".equals(code)){//success
                //System.out.println("====================data:" + json.get("data").toString() + "============================");
                //登录成功设置会话
                WhUser user = JsonUtils.toJavaObj(json.get("data").toString(), WhUser.class);
                request.getSession().setAttribute(LoginInterceptor.sessionUserKey, user);

                //跳转到之前的页面
                String url = request.getParameter("history_url");
                if(url == null || StringUtils.isEmpty(url) || url.endsWith("login")){
                    if(request.getServerPort() == 80){
                        url = request.getScheme() + "://" + request.getServerName() + request.getContextPath()+"/index";
                    }else{
                        url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/index";
                    }
                }
                view.setViewName("redirect:"+url);
            }else{//fail
                String errMsg = json.get("msg").toString();
                //view.addObject("msg", errMsg);
                //request.getSession().setAttribute("loginErrMsg", errMsg);
                //view.addObject("loginErrMsg", errMsg);
                //request.setAttribute("loginErrMsg", errMsg);
                model.addFlashAttribute("loginErrMsg", errMsg);
                view.setViewName("redirect:/login");
            }
        } catch (Exception e) {
            */
/*rb.setSuccess(ResponseBean.FAIL);
            rb.setErrMsg(e.getMessage());
            rb.setErrCode(ResponseBean.SYS_ERR_CODE);*//*

            Logger.getLogger(LoginAction.class).error(e.getMessage(), e);
        }
        return view;
    }

    */
/**
     * 登出并跳转到登录界面
     * @return
     *//*

    @RequestMapping("/doLogout")
    public ModelAndView doLogout(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView();
        try {
            request.getSession().removeAttribute(LoginInterceptor.sessionUserKey);
            view.setViewName("redirect:/login");//登录界面
        } catch (Exception e) {
            Logger.getLogger(LoginAction.class).error(e.getMessage(), e);
        }
        return view;
    }

    public static void main(String[] args) throws Exception {


    }

    public static String getRandom(int len) {
        Integer[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Random rand = new Random();
        for (int i = 10; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = array[index];
            array[index] = array[i - 1];
            array[i - 1] = tmp;
        }
        int result = 0;
        for (int i = 0; i < len; i++) {
            result = result * 10 + array[i];
        }
        return "" + result;
    }
}
*/
