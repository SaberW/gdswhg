package com.creatoo.hn.actions.api.user;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.model.WhUser_old;
import com.creatoo.hn.model.WhgRepLogin;
import com.creatoo.hn.model.WhgUsrWeixin;
import com.creatoo.hn.services.api.user.APIWechatLoginService;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.services.home.user.RegistService;
import com.creatoo.hn.utils.ReqParamsUtil;
import com.creatoo.hn.utils.WhConstance;
import com.creatoo.hn.utils.social.wechat.AdvancedUtil;
import com.creatoo.hn.utils.social.wechat.SNSUserInfo;
import com.creatoo.hn.utils.social.wechat.WeixinOauth2Token;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/15.
 * 微信端三方登录接口
 */
@RestController
@RequestMapping("/api/user")
public class APIWechatLoginAction {

    Logger log = Logger.getLogger(this.getClass());

    @Autowired
    public CommService commService;

    @Autowired
    public APIWechatLoginService apiWechatLoginService;


    /**
     *  访问地址：  /api/user/wxLogin
     *  param: openid/unionid/city/country/nickname/headimgurl/province/sex
     * Wechat授权登录接口
     * @return JSON : {
     *     "success" : "1"  //1-成功； 其它失败
     *     "errormsg" :   //101-操作失败；102:openid或者unionid为空
     *     "data" : 返回有 userid 的微信用户信息
     * }
     */
    @CrossOrigin
    @RequestMapping(value = "/wxLogin", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseBean afterLoginRedirect(HttpServletRequest request){
        ResponseBean res = new ResponseBean();
        WhgUsrWeixin wxUser = new WhgUsrWeixin();
        try {
            wxUser.setOpenid(request.getParameter("openid"));
            wxUser.setUnionid(request.getParameter("unionid"));
            if(request.getParameter("city") != null && !"".equals(request.getParameter("city").trim())){
                wxUser.setCity(request.getParameter("city"));
            }
            if(request.getParameter("country") != null && !"".equals(request.getParameter("country").trim())){
                wxUser.setCountry(request.getParameter("country"));
            }
            if(request.getParameter("nickname") != null && !"".equals(request.getParameter("nickname").trim())){
                wxUser.setNickname(request.getParameter("nickname"));
            }
            if(request.getParameter("headimgurl") != null && !"".equals(request.getParameter("headimgurl").trim())){
                wxUser.setHeadimgurl(request.getParameter("headimgurl"));
            }
            if(request.getParameter("province") != null && !"".equals(request.getParameter("province").trim())){
                wxUser.setProvince(request.getParameter("province"));
            }
            if(request.getParameter("sex") != null && !"".equals(request.getParameter("sex").trim())){
                wxUser.setSex(Integer.parseInt(request.getParameter("sex")));
            }

            res = this.apiWechatLoginService.saveWxUser(wxUser);

            //插入用户登录时间信息
            try {
                WhUser_old user = (WhUser_old) res.getData();
                String logintimeId = this.commService.getKey("whg_rep_login");
                WhgRepLogin whgRepLogin = new WhgRepLogin();
                whgRepLogin.setId(logintimeId);
                whgRepLogin.setDevtype(1);
                whgRepLogin.setLogintime(new Date());
                whgRepLogin.setUserid(user.getId());
                this.apiWechatLoginService.insertLoginTime(whgRepLogin);
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("101");
            log.error(e.getMessage(),e);
        }
        return res;
    }

}
