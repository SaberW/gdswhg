package hn.creatoo.com.web.actions.web.login;

import com.google.gson.JsonObject;
import hn.creatoo.com.web.interceptor.LoginInterceptor;
import hn.creatoo.com.web.mode.WhUser;
import hn.creatoo.com.web.util.JsonUtils;
import hn.creatoo.com.web.util.RestUtils;
import hn.creatoo.com.web.util.social.wechat.AdvancedUtil;
import hn.creatoo.com.web.util.social.wechat.SNSUserInfo;
import hn.creatoo.com.web.util.social.wechat.WeixinOauth2Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信登录控制器
 */
@RestController
public class WxLoginAction {
    //日志
    private Logger log = LoggerFactory.getLogger(WxLoginAction.class);

    /**
     * Restful调用接口工具
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 运行环境
     */
    /**
     * 当前环境的配置信息:application[-][test|dev].properties
     * 如：env.getProperty("spring.datasource.password")
     */
    @Autowired
    protected Environment env;

    /**
     * Wechat跳转至授权页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/{cultsite}/user/authorizeWx")
    public ModelAndView authorizePage(HttpServletRequest request, HttpServletResponse response,@PathVariable("cultsite") String cultsite){
        ModelAndView view = new ModelAndView();
        try {
            //回调地址
            String redirectUrl = String.format("%suser/afterWxLogin", getWebBaseUrl(request));

            //授权URL
            String url = getQrAuthUrl(this.env.getProperty("wx.appId"), redirectUrl, cultsite);

            //跳转到授权URL
            view.setViewName("redirect:"+url);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            view.setViewName("redirect:/login");
        }
        return view;
    }

    /**
     * Wechat授权登录后页面的跳转
     * @return
     */
    @RequestMapping("/user/afterWxLogin")
    public ModelAndView afterLoginRedirect(String code, String state, HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        try {
            if(StringUtils.isEmpty(code)){//拒绝授权-没有code参数时为拒绝授权
                view.setViewName("redirect:/"+state+"/login");
            }else{//授权成功
                // 获取网页授权access_token
                WeixinOauth2Token weixinOauth2Token = AdvancedUtil.getOauth2AccessToken(this.env.getProperty("wx.appId"), this.env.getProperty("wx.appSecret"), code);

                //通过access_token获取用户信息
                SNSUserInfo userInfo = AdvancedUtil.getSNSUserInfo(weixinOauth2Token.getAccessToken(), weixinOauth2Token.getOpenId());

                //微信用户信息
                Map<String,String> params = new HashMap<String,String>();
                params.put("unionid", userInfo.getUnionid());
                params.put("openid", userInfo.getOpenId());
                params.put("nickname", userInfo.getNickname());
                params.put("sex", userInfo.getSex()+"");
                params.put("province", userInfo.getProvince());
                params.put("city", userInfo.getCity());
                params.put("country", userInfo.getCountry());
                params.put("headimgurl", userInfo.getHeadImgUrl());

                //调用登录接口
                String restRes = RestUtils.post(restTemplate, "api/user/wechat/doLoginFromWX", params);

                //获取返回值
                JsonObject json = JsonUtils.toJsonObj(restRes);
                String resCode = json.get("code").getAsString();
                if("0".equals(resCode)){//success
                    //登录成功设置会话
                    WhUser user = JsonUtils.toJavaObj(json.get("data").toString(), WhUser.class);
                    HttpSession session = request.getSession();
                    session.setAttribute(LoginInterceptor.sessionUserKey, user);
                    session.setAttribute("userData", json.get("data").toString());
                    ServletContext ContextA = session .getServletContext();
                    ContextA.setAttribute("wbgx_session", session);
                }else{//fail
                    view.setViewName("redirect:/"+state+"/login");
                }
                view.setViewName("redirect:/center/userInfo");
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return view;
    }

    /**
     * 获取应用访问前缀
     * @param req
     * @return
     */
    private String getWebBaseUrl(HttpServletRequest req) {
        String url = req.getRequestURL().toString();
        String baseURL = url.substring(0, url.length() - req.getRequestURI().length()) + req.getContextPath() + "/";
        return baseURL;
    }

    /**
     * 获取微信授权URL
     * @param appId
     * @param redirectUrl
     * @param state
     * @return
     */
    private String getQrAuthUrl(String appId, String redirectUrl, String state) {
        // 拼接请求地址
        String requestUrl = "https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECTURL&response_type=code&scope=snsapi_login&state=STATE#wechat_redirect";
        requestUrl = requestUrl.replace("APPID", urlEncodeUTF8(appId));
        requestUrl = requestUrl.replace("REDIRECTURL", urlEncodeUTF8(redirectUrl));
        requestUrl = requestUrl.replace("STATE", urlEncodeUTF8(state));
        return requestUrl;
    }

    /**
     * 对请求参数进行URL编码
     * @param source
     * @return
     */
    private String urlEncodeUTF8(String source) {
        String result = source;
        try {
            result = java.net.URLEncoder.encode(source, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

}
