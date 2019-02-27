package com.creatoo.hn.controller.api.apiwechart;


import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.services.api.apioutside.user.ApiUserService;
import com.creatoo.hn.util.bean.ApiResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通过微信接口登录网站
 */
@CrossOrigin
@RestController
@RequestMapping("/api/user/wechat")
public class APIWebLoginFromWechatController extends BaseController {
    /**
     * 会员服务
     */
    @Autowired
    private ApiUserService apiUserService;

    /**
     * 通过微信接口登录网站
     * @return
     */
    @RequestMapping("/doLoginFromWX")
    public ApiResultBean doLoginFromWX(
            String unionid,
            String openid,
            String nickname,
            String sex,
            String province,
            String city,
            String country,
            String headimgurl
    ){
        ApiResultBean retMobileEntity = new ApiResultBean();
        try {
            //业务逻辑：
            // 1: whg_usr_weixin如果存在unionid的记录，表示微信用户已经绑定了系统用户，直接返回whg_user中对应在系统用户
            // 2: whg_usr_weixin如果不存在unionid记录，保存记录到whg_usr_weixin表，并保存一每条用户记录whg_user表。


            // 是否已绑定账号和手机，如果是直接告诉登录成功，如果没有，应该跳转到绑定手机和设置密码界面
        }catch (Exception e){
            log.error(e.getMessage(), e);
            retMobileEntity.setCode(1);
            retMobileEntity.setMsg("通过微信登录系统失败");
        }
        return retMobileEntity;
    }
}
