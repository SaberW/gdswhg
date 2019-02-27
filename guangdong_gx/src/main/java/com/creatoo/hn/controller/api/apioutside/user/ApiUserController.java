package com.creatoo.hn.controller.api.apioutside.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgRepLogin;
import com.creatoo.hn.dao.model.WhgUser;
import com.creatoo.hn.services.api.apioutside.user.ApiUserService;
import com.creatoo.hn.services.api.wechat.UserRealService;
import com.creatoo.hn.services.api.wechat.WechatService;
import com.creatoo.hn.services.wechat.AdvancedUtil;
import com.creatoo.hn.services.wechat.WeixinOauth2Token;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.ReqParamsUtil;
import com.creatoo.hn.util.bean.ApiResultBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rbg on 2017/8/7.
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping("/api")
@CrossOrigin
public class ApiUserController extends BaseController{

    @Autowired
    private ApiUserService apiUserService;

    @Autowired
    public WechatService wechatService;
    @Autowired
    private UserRealService realService;
    /**
     * 用户注册
     * @param mobile *手机号
     * @param password  *MD5密码
     * @param code  *短信验证码
     * @param sex   性别
     * @param birthday 生日
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/user/register", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Object userRegister(String mobile, String password, String code,
                               @RequestParam(required = false) Integer sex,
                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")Date birthday) {
        WhgUser whgUser = new WhgUser();
        whgUser.setPhone(mobile.trim());
        whgUser.setPassword(password);
        if (sex!=null) {
            String _sex = sex.intValue() == 1 ? "1" : "0";
            whgUser.setSex(_sex);
        }
        if (birthday!=null){
            whgUser.setBirthday(birthday);
        }

        ApiResultBean arb = new ApiResultBean();
        try {
            arb = (ApiResultBean) this.apiUserService.userRegister(whgUser, code);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("注册失败");
            log.error(e.getMessage(), e);
        }

        return arb;
    }

    /**
     * 注册第2步-完善资料
     * @param id
     * @param sex
     * @param nickname
     * @param job
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/user/register2", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Object userRegister2(String id, String sex, String nickname, String job, String qq, String wx) {
        ApiResultBean arb = new ApiResultBean();
        try {
            WhgUser whgUser = new WhgUser();
            whgUser.setSex(sex);
            whgUser.setNickname(nickname);
            whgUser.setJob(job);
            whgUser.setQq(qq);
            whgUser.setWx(wx);

            arb = (ApiResultBean) this.apiUserService.userRegister2(id, whgUser);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("操作失败");
            log.error(e.getMessage(), e);
        }

        return arb;
    }

    /**
     * 是否是用户表中的手机号
     * @param phone
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/user/isPhone", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object isPhone(String phone){
        ApiResultBean arb = new ApiResultBean();
        try {
            if (phone == null || phone.isEmpty()) {
                throw new Exception("params is null");
            }
            boolean isOk = this.apiUserService.isPhone(phone);
            arb.setData( isOk ? 1 : 0 );
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
        }
        return arb;
    }

    /**
     * 昵称是否在用
     * @param nickName
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/user/isNickname", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object isNickName(String nickName, String id){
        ApiResultBean arb = new ApiResultBean();
        try {
            if (nickName == null || nickName.isEmpty() || id==null || id.isEmpty()) {
                throw new Exception("params is null");
            }
            boolean isOk = this.apiUserService.isNickname(nickName, id);
            arb.setData( isOk? 1: 0 );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arb;
    }

    /**
     * 指定手机发送短信验证码
     * @param phone
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/user/sendSmsCode", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Object userCode(String phone){
        ApiResultBean arb = new ApiResultBean();

        try {
            arb = (ApiResultBean) this.apiUserService.sendSmsCode(phone);
        } catch (Exception e) {
            arb.setCode(250);
            arb.setMsg("发送短信验证码失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 手机验证码是否有效
     * @param phone
     * @param code
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/user/isPhoneCode", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object isPhoneCode(String phone, String code) {
        ApiResultBean arb = new ApiResultBean();
        try {
            if (phone == null || phone.isEmpty() || code == null || code.isEmpty()) {
                throw new Exception("params is null");
            }
            boolean isOk = this.apiUserService.validSmsCode(phone,code);
            arb.setData( isOk ? 1 : 0 );
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
        }
        return arb;
    }


    /**
     * 前端通过接口登录验证
     * @param userName
     * @param password
     * @param visitType 访问类型  1 外网访问登录
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/user/doLogin", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Object doLogin4user(String userName, String password, @RequestParam(value = "visitType", required = false, defaultValue = "") String visitType) {
        ApiResultBean arb = new ApiResultBean();

        try {
            arb = (ApiResultBean) this.apiUserService.userLogin(userName, password, visitType);
        } catch (Exception e) {
            arb.setCode(102);
            arb.setMsg("登录失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }


    /**
     * 用户登录（手机号登录）
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value="/user/wechart/doLogin",method = RequestMethod.POST)
    public ApiResultBean doLogin(HttpServletRequest request){
        ApiResultBean responseBean = new ApiResultBean();
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String clientSession = getParam(request,"clientSession",null);
       /* if(null != clientSession){
            boolean isLock = isLock(clientSession);
            if(isLock){
                responseBean.setCode(1);
                responseBean.setMsg("用户账号被锁定中");
                return responseBean;
            }
        }*/
        if(null == userName || null == password){
            responseBean.setCode(1);
            responseBean.setMsg("用户名或密码错误");
            return responseBean;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        WhgUser whUser = new WhgUser();
        whUser.setPhone(userName);
        //whUser.setPassword(password);
        Map findResult = null;
        try {
           /* findResult = apiUserService.getUserDetail(whUser);
            if(null == findResult){
                responseBean.setCode(1);
                responseBean.setMsg("用户名或密码错误");
                return responseBean;
            }*/
            if(userName.equals(findResult.get("phone")) && password.equals(findResult.get("password"))){
                Map map = new HashMap();
                map.put("userId",findResult.get("id"));
                map.put("userName",findResult.get("name"));
                map.put("mobile",findResult.get("phone"));
                map.put("sex",findResult.get("sex"));
                map.put("birthday",findResult.get("birthday"));
                map.put("nickName",findResult.get("nickname"));
                map.put("authState",findResult.get("isrealname"));
                map.put("userHeadImgUrl",findResult.get("headurl"));
                map.put("staticServerUrl",env.getProperty("upload.local.server.addr"));
                responseBean.setData(map);
                //插入用户登录时间信息
                try {
                    String logintimeId = IDUtils.getID();
                    WhgRepLogin whgRepLogin = new WhgRepLogin();
                    whgRepLogin.setId(logintimeId);
                    whgRepLogin.setDevtype(0);
                    whgRepLogin.setLogintime(new Date());
                    whgRepLogin.setUserid((String)findResult.get("id"));
                    this.wechatService.insertLoginTime(whgRepLogin);
                    setLogin(clientSession,true);
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            } else {
                responseBean.setCode(1);
                responseBean.setMsg("用户名或密码错误");
            }
            return responseBean;
        }catch (Exception e){
            log.error(e.toString());
            responseBean.setCode(2);
            responseBean.setMsg("登录失败");
            return responseBean;
        }
    }


    private boolean setLogin(String sessionId,boolean isSuccess){
        try {
            String myClientSession = (String)wechatService.getCache("myClientSession",sessionId);

            if(isSuccess){
                wechatService.delCache("myClientSession",sessionId);
                return false;
            }
            if(null == myClientSession){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("count",0);
                wechatService.addCache("myClientSession",sessionId,jsonObject);
                return false;
            }
            JSONObject jsonObject = JSON.parseObject(myClientSession);
            int count = jsonObject.getInteger("count");
            if(count < 1){
                jsonObject.put("count",++count);
                wechatService.addCache("myClientSession",sessionId,jsonObject);
                return false;
            }else {
                wechatService.delCache("myClientSession",sessionId);
                JSONObject lock = new JSONObject();
                lock.put("sessionId",new Date());
                wechatService.addCache("myClientSessionLock",sessionId,lock);
                return true;
            }
        }catch (Exception e){
            log.error(e.toString());
            return false;
        }
    }

    /*private boolean isLock(String sessionId){
        try {
            String lock = (String) wechatService.getCache("myClientSessionLock",sessionId);
            JSONObject jsonObject = JSON.parseObject(lock);
            Date date = jsonObject.getDate("sessionId");
            int saveSeconds = CompareTime.date2Seconds(date);
            saveSeconds += CompareTime.getSecondsByMin(5);
            int nowSeconds = CompareTime.date2Seconds(new Date());
            if(saveSeconds < nowSeconds){
                return false;
            }
            return true;
        }catch (Exception e){
            log.error(e.toString());
            return false;
        }
    }*/
    
    
    /**
     * 获取微信用户资料
     * @param openId
     * @return
     *//*
    @CrossOrigin
    @RequestMapping(value = "/user/wechatLogin", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object wechatLogin(String openId){
        ApiResultBean arb = new ApiResultBean();

        try {
            arb = (ApiResultBean) this.apiUserService.wechatLogin(openId);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("登录失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }*/

    /**
     * 微信绑定手机
     * @param userId
     * @param phone
     * @return
     */
    @CrossOrigin
    @PostMapping("/user/wechart/relationMobile")
    @ResponseBody
    public Object relationMobile(String openid, String phone, String code, String pwd){
        ApiResultBean arb = new ApiResultBean();
        try {
            arb = (ApiResultBean) this.apiUserService.relationMobile(openid, phone, code, pwd);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("微信绑定手机号失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 关联手机
     *
     * @param userId
     * @param phone
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/user/relationMobile", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object relationMobile(String userId, String phone, String code) {
        ApiResultBean arb = new ApiResultBean();

        try {
            arb = (ApiResultBean) this.apiUserService.relationMobile(userId, phone, code);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("绑定失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 微信绑定手机时验证手机号是否存在系统账号
     * @param phone 手机号
     */
    @CrossOrigin
    @PostMapping("/user/wechart/existPhoneInSystem")
    @ResponseBody
    public ApiResultBean existPhoneInSystem(String phone,String openid){
        ApiResultBean arb = new ApiResultBean();
        try {
            String userid = this.apiUserService.existPhoneInSystem(phone);
            if(StringUtils.isNotEmpty(userid)){
                boolean isBind = this.apiUserService.bindWeixin(userid,openid);
                if(isBind){
                    arb.setCode(2);
                    arb.setMsg("此手机号已经绑定微信，不能再次绑定");
                }else{
                    arb.setCode(0);
                    arb.setMsg("此手机号已经注册成为系统账号");
                }
            }else{
                arb.setCode(1);
                arb.setMsg("此手机号没有注册成为系统账号");
            }
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("绑定失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 找回密码
     * @param mobile
     * @param code
     * @param password
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/user/setPasswd", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object setPasswd(String mobile, String code, String password){
        ApiResultBean arb = new ApiResultBean();
        try {
            arb = (ApiResultBean) this.apiUserService.setPasswd(mobile, code, password);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("重置密码失败");
            log.error(e.getMessage(),e);
        }
        return arb;
    }

    /**
     * 修改密码
     * @param userId
     * @param passWord
     * @param newPwdMd5
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/user/setPassword", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object setPassword(String userId, String passWord, String newPwdMd5){
        ApiResultBean arb = new ApiResultBean();

        try {
            arb = (ApiResultBean) this.apiUserService.setPassword(userId, passWord, newPwdMd5);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("修改密码失败");
            log.error(e.getMessage(), e);
        }

        return arb;
    }



    /**
     * 实名认证（微信端实名认证接口）
     * @param request
     * @returnSS
     */
    @CrossOrigin
    @RequestMapping(value = "/user/wechart/realNameApprove", method = RequestMethod.POST)
    @ResponseBody
    public Object realNameApprove(WhgUser whUser, HttpServletRequest request,
                                  @RequestParam(value = "_birthday", required = false) Date _birthday) {
        ApiResultBean arb = new ApiResultBean();
        try {
            if (_birthday!=null){
                whUser.setBirthday(_birthday);
            }
            arb = (ApiResultBean)this.realService.realNameApprove(whUser);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            arb.setCode(1);
            arb.setMsg(e.getMessage());
        }
        return arb;
    }


    /**
     * 实名认证步骤A
     * @param userId
     * @param fullName
     * @param idCard
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/auth/verifya", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object authVerifyA(String userId, String fullName, String idCard){
        ApiResultBean arb = new ApiResultBean();

        try {
            arb = (ApiResultBean) this.apiUserService.authVerifyA(userId, fullName, idCard);
        } catch (Exception e) {
            arb.setCode(103);
            arb.setMsg("实名认证信息提交失败");
            log.error(e.getMessage(),e);
        }

        return arb;
    }

    /**
     * 实名认证步骤B
     * @param userId
     * @param frontId
     * @param backId
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/auth/verifyb", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object authVerifyB(String userId, String accesstoken, String frontId, String backId){
        ApiResultBean arb = new ApiResultBean();

        try {
            arb = (ApiResultBean) this.apiUserService.authVerifyB(userId, accesstoken, frontId, backId);
        } catch (Exception e) {
            arb.setCode(103);
            arb.setMsg("实名认证信息提交失败");
            log.error(e.getMessage(),e);
        }

        return arb;
    }


    /**
     * 微信修改图像
     *
     * @param frontId
     * @param backId
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/user/updateUserHeadImg", method = RequestMethod.POST)
    @ResponseBody
    public Object updateUserHeadImg(HttpServletRequest request) {
        ApiResultBean retMobileEntity = new ApiResultBean();
        String userId = getParam(request, "userId", null);
        String headUrl = getParam(request, "headUrl", null);
        System.out.println("userId:" + userId + "---headUrl:" + headUrl);
        if (null == userId || null == headUrl) {
            retMobileEntity.setCode(1);
            retMobileEntity.setMsg("修改头像失败，参数不全");
            return retMobileEntity;
        }
        if (0 != apiUserService.updateUserHeadUrl(userId, headUrl)) {
            retMobileEntity.setCode(1);
            retMobileEntity.setMsg("修改头像失败");
            return retMobileEntity;
        }
        WhgUser whUser = apiUserService.getUserDetail(userId);
        retMobileEntity.setCode(0);
        retMobileEntity.setMsg("修改头像成功");
        if (null != whUser) {
            retMobileEntity.setData(whUser);
        }
        return retMobileEntity;
    }


    /**
     * web实名认证 - 身份证上传图片
     * @param userId
     * @param file
     * @param filemake idcardback || idcardface
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/user/uploadIdcard", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Object uploadIdCard(String userId, String filemake, MultipartFile file, HttpServletRequest request) {
        ApiResultBean arb = new ApiResultBean();
        try {
            arb = (ApiResultBean) this.apiUserService.uploadIdcard(userId, filemake, file, request);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("操作失败");
            log.error(e.getMessage(), e);
        }
        /*String restStr = "";
        if (arb.getCode().intValue() != 0 && arb.getMsg()!=null&& !arb.getMsg().isEmpty()){
            restStr = arb.getMsg();
        }*/
        /*JSONObject json = (JSONObject) JSON.toJSON(arb);
        return json.toJSONString();*/

        return arb;
    }


    /**
     * web实名认证-保存信息
     * @param userId
     * @param name
     * @param idcard
     * @param sex
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/user/saveRealInfo", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object saveRealInfo(String userId, String name, String idcard,
                               @RequestParam(value = "idcardtype", required = false, defaultValue = "1") Integer idcardtype,
                               @RequestParam(value = "birthday", required = false) Date birthday,
                               @RequestParam(value = "sex", required = false) String sex) {
        ApiResultBean arb = new ApiResultBean();

        try {
            arb = (ApiResultBean) this.apiUserService.saveRealInfo(userId, name, idcard, sex, idcardtype, birthday);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("操作失败");
        }
        return arb;
    }


    /**
     * 获取参数
     * @param request
     * @param paramName
     * @return
     */
    private String getParam(HttpServletRequest request,String paramName){
        String value = request.getParameter(paramName);
        if(null != value && !value.isEmpty()){
            return value;
        }
        return null;
    }

    /**
     * 获取参数，带默认值
     * @param request
     * @param paramName
     * @param myDefault
     * @return
     */
    private String getParam(HttpServletRequest request,String paramName,String myDefault){
        String value = request.getParameter(paramName);
        if(null != value && !value.isEmpty()){
            return value;
        }
        return myDefault;
    }

}
