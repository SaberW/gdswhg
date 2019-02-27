package com.creatoo.hn.controller.api.apiwechart;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgRepLogin;
import com.creatoo.hn.dao.model.WhgUsrWeixin;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.api.wechat.EnvironService;
import com.creatoo.hn.services.api.wechat.WechatService;
import com.creatoo.hn.services.comm.CommUploadService;
import com.creatoo.hn.services.wechat.*;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.ReqParamsUtil;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.bean.UploadFileBean;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sun.security.krb5.internal.Ticket;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 微信控制器
 * Created by caiyong on 2017/5/3.
 */
@CrossOrigin
@Controller
@RequestMapping("/api/wechat")
public class APIWechatController  extends BaseController {
    @Autowired
    public WechatService wechatService;
    @Autowired
    public EnvironService environService;

    @Autowired
    private CommUploadService commUploadService;

    /**
     * 基础授权，不会弹出授权界面
     * @param req
     * @return
     */
    @RequestMapping("login")
    public Object wechatLogin(HttpServletRequest req){
        ModelAndView mav = new ModelAndView() ;
        String url = this.wechatService.getBaseAuthUrl(req);
        mav.setViewName(String.format("redirect:%s", url));
        return mav ;
    }

    /**
     * 微信基础授权响应界面
     * @return 验证结果
     */
    @RequestMapping("/baseauth")
    public ModelAndView baseAuth(HttpServletRequest req, HttpServletResponse resp){
        ModelAndView mav = new ModelAndView();
        try {
            //获取请求参数
            Map<String, Object> paramMap = ReqParamsUtil.parseRequest(req);
            String code = paramMap.get("code").toString();
            String state = paramMap.get("state").toString();
            state = java.net.URLDecoder.decode(state, "UTF-8");//截码
            if (!"authdeny".equals(code)) {
                // 1.获取网页授权access_token
                WeixinOauth2Token weixinOauth2Token = AdvancedUtil.getOauth2AccessToken(wechatService.getWechartAppId(), wechatService.getWechartAppsecret(), code);
                if(null == weixinOauth2Token){//无法获取token，重新深度授权，此逻辑需要考虑，影响不大
                    String url = this.wechatService.getUserAuthUrl(req,state);
                    mav.setViewName(String.format("redirect:%s", url));
                    return mav;
                }
                String accessToken = weixinOauth2Token.getAccessToken();//access_token
                String openId = weixinOauth2Token.getOpenId();// 用户标识

                // 2.获取用户信息
                SNSUserInfo snsUserInfo = AdvancedUtil.getSNSUserInfo(accessToken, openId);
                if(null == snsUserInfo){//无法获取用户信息的处理
                    String url = this.wechatService.getUserAuthUrl(req,state);
                    mav.setViewName(String.format("redirect:%s", url));
                    return mav;
                }

                // 3.如果用户未注册(绑定手机号)，跳转到注册页面，否则跳转到首页
                String userid = this.wechatService.wxAccess(snsUserInfo);

                // 3.1 通过openid，有账号并且已绑定微信,跳转到微信首页
                if(StringUtils.isNotEmpty(userid)){
                    String path = getPageForState(req,resp,"doLogin",openId);
                    path += "?t="+new Date().getTime();

                    //设置回传地址
                    path += "&userId="+userid;

                    //设置用户标识
                    if(StringUtils.isNotEmpty(state) && !"init".equals(state)) {
                        path += "&to="+state;
                    }
                    mav.setViewName(path);
                }else{// 3.2 通过openid, 不是上面的情况 //根路径/bingPhone asf
                    String path = getPageForState(req,resp,"bindPhone",openId);
                    path += "?t="+new Date().getTime();
                    //设置回传地址
                    if(StringUtils.isNotEmpty(state) && !"init".equals(state)) {
                        path += "&to="+state;
                    }
                    //openid
                    path += "&openid="+openId;
                    mav.setViewName(path);
                }
                return mav;
            }else {
                state = "error";
                String path = getPageForState(req,resp,state);
                mav.setViewName(path);
                return mav;
            }
        } catch (Exception e) {
            log.error(e.toString());
            String path = getPageForState(req, resp,"login");
            mav.setViewName(path);
        }
        return mav;
    }

    /**
     * 确定绑定状态
     * @return
     */
    private String getState(String openId){
        WhgUsrWeixin whgUsrWeixin = wechatService.getSessionByOpenId(openId);
        if(null == whgUsrWeixin.getUserid()||whgUsrWeixin.getUserid().isEmpty()){
            return "bind";
        }
        return "user";
    }

    /**
     * 添加或修改微信用户
     * @param snsUserInfo
     * @throws Exception
     */
    private void insertWxUser(SNSUserInfo snsUserInfo) throws Exception{
        String openId = snsUserInfo.getOpenId();
        WhgUsrWeixin whgUsrWeixin = wechatService.getSessionByOpenId(openId);
        if (null == whgUsrWeixin) {//数据库是否有openid-没有
            whgUsrWeixin = new WhgUsrWeixin();
            whgUsrWeixin.setUnionid(snsUserInfo.getUnionid());
            whgUsrWeixin.setOpenid(openId);
            whgUsrWeixin.setId(IDUtils.getID());
            whgUsrWeixin.setCity(snsUserInfo.getCity());
            whgUsrWeixin.setCountry(snsUserInfo.getCountry());
            whgUsrWeixin.setCrtdate(new Date());
            whgUsrWeixin.setNickname(filterExpression(snsUserInfo.getNickname()));
            whgUsrWeixin.setHeadimgurl(snsUserInfo.getHeadImgUrl());
            whgUsrWeixin.setProvince(snsUserInfo.getProvince());
            whgUsrWeixin.setSex(snsUserInfo.getSex());
            whgUsrWeixin.setCrtdate(new Date());
            //应对测试环境没有unionid的情况
            if(null == whgUsrWeixin.getUnionid()){
                whgUsrWeixin.setUnionid(whgUsrWeixin.getOpenid());
            }
            wechatService.addSession(whgUsrWeixin);
        } else {//数据库是否有openid-有
            Date now = new Date();
            whgUsrWeixin.setOpenid(snsUserInfo.getOpenId());
            whgUsrWeixin.setCity(snsUserInfo.getCity());
            whgUsrWeixin.setCountry(snsUserInfo.getCountry());
            whgUsrWeixin.setCrtdate(new Date());
            whgUsrWeixin.setNickname(filterExpression(snsUserInfo.getNickname()));
            whgUsrWeixin.setHeadimgurl(snsUserInfo.getHeadImgUrl());
            whgUsrWeixin.setProvince(snsUserInfo.getProvince());
            whgUsrWeixin.setSex(snsUserInfo.getSex());
            //应对测试环境没有unionid的情况
            if(null == whgUsrWeixin.getUnionid()){
                whgUsrWeixin.setUnionid(whgUsrWeixin.getOpenid());
            }
            wechatService.updateSession(whgUsrWeixin);
        }
    }

    /**
     * 微信验证接口
     * @return 验证结果
     */
    @RequestMapping("/userauth")
    public Object userAuth(HttpServletRequest req, HttpServletResponse resp){
        //获取请求参数
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(req);
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        String baseUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() ;
        ModelAndView mav = new ModelAndView();
          String code = paramMap.get("code").toString();
        String state = paramMap.get("state").toString();
        String path = null;
        try {
            if (!"authdeny".equals(code)) {
                // 获取网页授权access_token
                WeixinOauth2Token weixinOauth2Token = AdvancedUtil.getOauth2AccessToken(wechatService.getWechartAppId(),wechatService.getWechartAppsecret(), code);
//                WeixinOauth2Token weixinOauth2Token = AdvancedUtil.getOauth2AccessToken("wxd8c81cbf81ab3361", "3ebeef6c65619496d39fa848d363f166", code);
                // 网页授权接口访问凭证
                String accessToken = weixinOauth2Token.getAccessToken();
                // 用户标识
                String openId = weixinOauth2Token.getOpenId();
                // 获取用户信息
                SNSUserInfo snsUserInfo = AdvancedUtil.getSNSUserInfo(accessToken, openId);
                if(null == snsUserInfo){
                    state = "login";
                    path = getPageForState(req,resp,state,openId);
                    mav.setViewName(path);
                    return mav;
                }
                //开始存入授权信息
                insertWxUser(snsUserInfo);
                environService.getJsTicket();
                state = getState(snsUserInfo.getOpenId());
                path = getPageForState(req,resp,state,openId);
                mav.setViewName(path);
                //插入用户登录时间信息
                try {
                    if(snsUserInfo.getOpenId() != null){
                        List<WhgUsrWeixin> wxUser = this.wechatService.selUser(snsUserInfo.getOpenId());
                        if(wxUser != null && wxUser.size() > 0){
                            String logintimeId = IDUtils.getID();
                            WhgRepLogin whgRepLogin = new WhgRepLogin();
                            whgRepLogin.setId(logintimeId);
                            whgRepLogin.setDevtype(0);
                            whgRepLogin.setLogintime(new Date());
                            whgRepLogin.setUserid(wxUser.get(0).getUserid());
                            this.wechatService.insertLoginTime(whgRepLogin);
                        }

                    }
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
                return mav;
                //resp.sendRedirect(baseUrl+path);
            }else {
                state = "login";
                path = getPageForState(req,resp,state);
                mav.setViewName(path);
                return mav;
            }

        } catch (Exception e) {
            e.printStackTrace();
            rtnMap.put("exception", e.getMessage());
            state = "login";
            path = getPageForState(req,resp,state);
            mav.setViewName(path);
            return mav;
        }
    }

    /**
     * 过滤表情符
     * @param nickName
     * @return
     */
    private String filterExpression(String nickName){
        Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher emojiMatcher = emoji.matcher(nickName);
        if (emojiMatcher.find()) {
            nickName = emojiMatcher.replaceAll("*");
        }
        return nickName;
    }

    /**
     * 微信验证接口
     * @return 验证结果
     */
    @RequestMapping("/userid")
    @ResponseBody
    public Object userId(HttpServletRequest req, HttpServletResponse resp){
        //获取请求参数
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(req);
        Map<String, Object> rtnMap = new HashMap<String, Object>();

        try {
//    		String openId = req.getSession().getAttribute("WechatOpenId").toString();
//    		rtnMap.put("WechatOpenId", openId); 1·

//        	WhWechat wechat = new WhWechat();
//        	wechat.setOpenid("test");
//        	wechatService.addSession(wechat);
            String openId = req.getSession().getAttribute("WechatOpenId").toString();
            WhgUsrWeixin wechat = wechatService.getSessionByOpenId(openId);
            rtnMap.put("openid", wechat.getOpenid());
//        	wechat.setSex("1");
//        	wechatService.updateSession(wechat);

        } catch (Exception e) {
            rtnMap.put("exception", e.getMessage());
            e.printStackTrace();
        }

        return rtnMap;
    }

    /**
     * 微信验证接口
     * @return 验证结果
     */
    @RequestMapping("/userinfo")
    @ResponseBody
    public Object userinfo(HttpServletRequest req, HttpServletResponse resp){
        Map<String, Object> rtnMap = new HashMap<String, Object>();

        try {
            String openId = req.getSession().getAttribute("WechatOpenId").toString();
            WhgUsrWeixin wechat = wechatService.getSessionByOpenId(openId);

            Map<String, Object> userMap = new HashMap<String, Object>();
            rtnMap.put("code", 0);
            rtnMap.put("msg", "");
            userMap.put("openId", wechat.getOpenid());
            userMap.put("nickName", wechat.getNickname());
            userMap.put("avatarUrl", wechat.getHeadimgurl());
            userMap.put("newCourse", 0);
            userMap.put("newActivity", 0);
            rtnMap.put("data", userMap);

        } catch (Exception e) {
            rtnMap.put("msg", e.getMessage());
            rtnMap.put("code", -1);
            e.printStackTrace();
        }

        return rtnMap;
    }



    /**
     * 微信验证接口
     * @return 验证结果
     */
    @CrossOrigin
    @RequestMapping(value = "/jsticket",method = RequestMethod.POST)
    @ResponseBody
    public ApiResultBean jsticket(HttpServletRequest req, HttpServletResponse resp){
        ApiResultBean retMobileEntity = new ApiResultBean();
        try {
            // 需要缓存7200秒,改为缓存到数据库中
            System.out.println("appid===>"+wechatService.getWechartAppId());
            String accessToken = wechatService.getGlobalToken4Cache(wechatService.getWechartAppId(),wechatService.getWechartAppsecret());
            //String jsticket = AdvancedUtil.getAccessToken4Cache(wechatService.getWechartAppId(),wechatService.getWechartAppsecret(),accessToken);
            String jsticket = this.wechatService.getGlobalJsTicket4Cache(wechatService.getWechartAppId(),wechatService.getWechartAppsecret(),accessToken);
            String noncestr = AdvancedUtil.create_nonce_str(); //必填，生成签名的随机串
            Long timestamp = System.currentTimeMillis() / 1000;//必填，生成签名的时间戳
            String url = req.getHeader("Referer");
            String[] arrUrl = url.split("#");
            url = arrUrl[0];
            String signature = AdvancedUtil.wechatSignature(jsticket, noncestr, timestamp, url); //必填，签名，见附录1

            Map<String, Object> map = new HashMap();
            map.put("nonceStr", noncestr);
            map.put("timestamp", timestamp);
            map.put("signature", signature);
            map.put("appId",wechatService.getWechartAppId());
            retMobileEntity.setData(map);
        }catch (Exception e){
            log.error(e.toString());
            retMobileEntity.setCode(1);
            retMobileEntity.setMsg("获取JsTicket失败");
        }
        return retMobileEntity;
    }

    /**
     * 微信端上传接口
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/getResource",method = RequestMethod.POST)
    @ResponseBody
    public ApiResultBean getResource(HttpServletRequest request){
        ApiResultBean retMobileEntity = new ApiResultBean();
        String fileCount = request.getParameter("fileCount");
        if(null == fileCount || fileCount.trim().isEmpty()){
            retMobileEntity.setCode(1);
            retMobileEntity.setMsg("参数不全");
            return retMobileEntity;
        }

        List res = new ArrayList();
        try {
            //获取全局accessToken
            String accessToken = wechatService.getGlobalToken4Cache(wechatService.getWechartAppId(),wechatService.getWechartAppsecret());

            int max= Integer.valueOf(fileCount);
            for(int i = 1; i <= max;i++){
                String resourceId = request.getParameter("imageName"+i);
                if(null == resourceId || resourceId.trim().isEmpty()){
                    continue;
                }
                com.squareup.okhttp.Response response = AdvancedUtil.getResourceUrl(resourceId, accessToken);
                if(null == response){
                    continue;
                }
                String filename = response.header("Content-disposition");
                String oldFileName = AdvancedUtil.analysisFileName(filename);
                com.squareup.okhttp.ResponseBody body = response.body();
                InputStream inputStream = body.byteStream();
                if(null == inputStream){
                    continue;
                }
               UploadFileBean uploadFileBean = commUploadService.uploadFile(inputStream,oldFileName);
                String url = uploadFileBean.getUrl();
                url = url.replace("\"","");
                Map map = new HashMap();
                map.put("resourceId",resourceId);
                map.put("url",url);
                res.add(map);
            }
            retMobileEntity.setData(res);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            retMobileEntity.setCode(1);
            retMobileEntity.setMsg("获取资源下载地址失败");
            return retMobileEntity;
        }
        retMobileEntity.setCode(0);
        return retMobileEntity;
    }

    /**
     * 微信WAP首页
     * @return 验证结果
     */
    @RequestMapping("/index")
    public Object index(HttpServletRequest req, HttpServletResponse resp){

        //获取请求参数
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(req);
        Map<String, Object> rtnMap = new HashMap<String, Object>();

        ModelAndView mav = new ModelAndView();

        try {
            if (req.getSession().getAttribute("WechatOpenId") == null) {
                mav.setViewName("redirect:/userauth");
            }
            else {
                mav.setViewName("redirect:/pages/wap/wechat/index.html");
            }
        } catch (Exception e) {
            rtnMap.put("exception", e.getMessage());
            e.printStackTrace();
        }

        return mav;
    }

    /**
     * 微信开发者平台验证接口
     * @return 验证结果
     */
    @RequestMapping("/validate")
    @ResponseBody
    public Object validate(HttpServletRequest req, HttpServletResponse resp){
        //获取请求参数
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(req);
        Map<String, Object> rtnMap = new HashMap<String, Object>();



        return rtnMap;
    }

    /**
     * 不存储openId，只获得返回页面
     * @param req
     * @param res
     * @param state
     * @return
     */
    public String getPageForState(HttpServletRequest req, HttpServletResponse res, String state){
        String baseUrl = req.getScheme() + "://" + req.getServerName();// + ":" + req.getServerPort() ;
        String base = baseUrl+"/tzy/";
        String page = "index.html";
        if ("init".equals(state)) {
            page = "index.html";
        }
        else if ("checkin".equals(state)) {
            page = "page/checkin.html";
        }
        else if ("bind".equals(state)) {
            page = "page/userbind.html";
        }
        else if ("act".equals(state)) {
            page = "page/activity.html";
        }
        else if ("ven".equals(state)) {
            page = "page/venue.html";
        }
        else if ("tra".equals(state)) {
            page = "page/train.html";
        }
        else if ("user".equals(state)) {
            page = "page/user.html";
        }
        else if ("login".equals(state)) {
            page = "page/authlogin.html";
        }
        else if ("reg".equals(state)) {
            page = "page/authreg.html";
        }
        page = String.format("redirect:%s%s", base, page);
        return page;
    }

    /**
     * 获取返回页面并存储openId
     * @param req
     * @param res
     * @param state
     * @param openId
     * @return
     * @throws UnsupportedEncodingException
     */
    public String getPageForState(HttpServletRequest req, HttpServletResponse res, String state, String openId) throws UnsupportedEncodingException {
        String baseUrl = req.getScheme() + "://" + req.getServerName();// + ":" + req.getServerPort() ;
        String base = baseUrl+"/wechat/";  //这里固定了微信端的路径 ContextPath 为 wechat
        String page = "index.html";
        if("bindPhone".equals(state)){
            page = "bindPhone";
        }else if("index".equals(state)){
            page = "index";
        }else if ("init".equals(state)) {
            page = "index.html";
        }else if("doLogin".equals(state)){
            page = "doLogin";
        }
        Cookie wxoid = new Cookie("wxoid", URLEncoder.encode(openId, "UTF-8"));
        Cookie flag = new Cookie("dgflag", "1");
        flag.setPath("/");
        wxoid.setPath("/");
        res.addCookie(wxoid);
        res.addCookie(flag);
        page = String.format("redirect:%s%s", base, page);
        return page;
    }
}
