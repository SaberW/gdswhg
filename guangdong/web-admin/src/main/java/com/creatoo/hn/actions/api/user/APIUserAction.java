package com.creatoo.hn.actions.api.user;

import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.model.WhCode;
import com.creatoo.hn.model.WhUser_old;
import com.creatoo.hn.model.WhgYwiSms;
import com.creatoo.hn.services.api.user.ApiUserService;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.services.home.user.RegistService;
import com.creatoo.hn.utils.RegistRandomUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 用户个人中心修改
 * Created by wangxl on 2017/4/12.
 */
@RestController
@RequestMapping("/api/user")
public class APIUserAction {
    /**
     * 日志控制器
     */
    Logger log = Logger.getLogger(this.getClass().getName());

    /**
     * 用户接口服务
     */
    @Autowired
    private ApiUserService apiUserService;

    /**
     * 短信服务
     */
    @Autowired
    private SMSService smsService;

    /**
     * 公共服务
     */
    @Autowired
    private CommService commService;

    /**
     * 注册服务
     */
    @Autowired
    private RegistService regService;

    /**
     * 绑定微信用户绑定手机
     * 访问地址 /api/user/bind/{id}/{phone}
     * @param id 微信用户ID whg_usr_weixin.id
     * @param phone 手机号码
     * @return JSON : {
     *     "success" : "1"                             //1-成功； 其它失败
     *     "errormsg" : "100|101|102|103|104"          //100-绑定成功；101-手机格式不正确; 102-参数id无效； 103-手机号已经被其它账号绑定; 104-手机号已经被自己绑定  105--该账号已经绑定手机无法再次绑定
     *     "data" : "userid" //返回用户ID
     * }
     */
    @CrossOrigin
    @RequestMapping(value = "/bind/{id}/{phone}", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseBean bind(@PathVariable("id") String id, @PathVariable("phone") String phone){
        ResponseBean res = new ResponseBean();
        try{
            res = this.apiUserService.bindPhone(id, phone);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
        }
        return res;
    }

    /**
     * 取消绑定
     * 访问地址 /api/user/unbind/{id}/{phone}
     * @param id 微信用户ID whg_usr_weixin.id
     * @param phone 手机号码
     * @return JSON : {
     *     "success" : "1"                             //1-成功； 其它失败
     *     "errormsg" : "100|101|102|103|104"          //100-解绑成功；101-手机格式不正确; 102-参数id无效
     * }
     */
    @CrossOrigin
    @RequestMapping(value = "/unbind/{id}/{phone}", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseBean unbind(@PathVariable("id") String id, @PathVariable("phone") String phone){
        ResponseBean res = new ResponseBean();
        try{
            String validCode = this.apiUserService.unbindPhone(id, phone);
            if(!"0".equals(validCode)){
                res.setSuccess(ResponseBean.FAIL);
                res.setErrormsg(validCode);
            }
        } catch (Exception e){
            log.error(e.getMessage(), e);
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
        }
        return res;
    }

    /**
     * 微信注册
     * 访问地址 /api/user/register/{phone}/{password}
     * @param phone 手机号码
     * @param password  密码
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/register/{phone}/{password}", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseBean register(@PathVariable("phone") String phone, @PathVariable("password") String password){
        ResponseBean res = new ResponseBean();
        try {
            String validCode = this.apiUserService.register(phone, password);
            if(!"100".equals(validCode)){
                res.setSuccess(ResponseBean.FAIL);
                res.setErrormsg(validCode);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
        }
        return res;
    }



    /**
     * 发送验证码
     * 访问地址 /api/user/sendSMS/{phone}/{code}
     * @param phone 手机号码
     * @return JSON : {
     *     "success" : "1"                             //1-成功； 其它失败
     *     "errormsg" : "100|101|102"          //100-发送成功；101-手机格式不正确; 102-发生异常  103--一天只能发送六次验证码
     * }
     */
    @CrossOrigin
    @RequestMapping(value = "/sendSMS/{phone}", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseBean sendValidCode(@PathVariable("phone") String phone){
        ResponseBean res = new ResponseBean();
        try {
            //手机格式不正确
            if(phone == null || !phone.matches("^1[3|4|5|7|8][0-9]\\d{8}$")){
                res.setSuccess(ResponseBean.FAIL);
                res.setErrormsg("101");
            }else{

                List<WhCode> phoneCodeList = this.regService.getPhoneTime(phone,new Date());
                if(phoneCodeList != null && phoneCodeList.size() >= 6){
                    res.setSuccess(ResponseBean.FAIL);
                    res.setErrormsg("103");
                    return res;
                }
                Map<String, String> smsData = new HashMap<String, String>();
                String code = RegistRandomUtil.random();
                smsData.put("validCode", code);
                smsService.t_sendSMS(phone, "LOGIN_VALIDCODE", smsData, phone);

                //将数据保存至code表
                this.regService.delPhoneCode(phone);
                String cid = this.commService.getKey("whcode");
                WhCode whcode = new WhCode();
                whcode.setId(cid);
                whcode.setSessid("ssid");
                whcode.setMsgcontent(code);
                whcode.setMsgtime(new Date());
                whcode.setMsgphone(phone);
                this.regService.savePhone(whcode);
                res.setErrormsg("100");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
        }
        return res;
    }

    /**
     * 验证手机验证码
     * @param phone 手机号
     * @param code 验证码
     * @return JSON : {
     *     "success" : "1"                             //1-成功； 其它失败
     *     "errormsg" : "错误消息"
     * }
     */
    @CrossOrigin
    @RequestMapping(value = "/validSMSCode/{phone}/{code}", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseBean validSMSCode(@PathVariable("phone") String phone, @PathVariable("code")String code){
        ResponseBean res = new ResponseBean();
        try {
            boolean isok = this.regService.validPhoneCode4API(phone, code);
            if(isok){
                this.regService.delPhoneCode(phone);
            }else{
                res.setSuccess(ResponseBean.FAIL);
                res.setErrormsg("验证码错误");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
        }
        return res;
    }

    /**
     * 保存用户资料(POST提交，表单数据为需要修改的whusr表的字段)
     * 访问地址 /api/user/info/save
     * @param WhUser_old 需要修改的用户信息，参考whusr表结构, POST表单数据如：{
     *       "id": "userid,whuser.id的值",
     *       "name": "修改后的用户姓名",
     *       "nickname": "修改后的用户昵称"， ....更多whusr表的字段参数
     * }
     * @return JSON : {
     *     "success" : "1"                  //1-成功； 其它失败
     *     "errormsg" : "异常消息"          //失败时的异常消息
     * }
     */
    @CrossOrigin
    @RequestMapping(value = "/info/save", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseBean saveUserInfo(WhUser_old whUser){
        ResponseBean res = new ResponseBean();
        try {
            this.apiUserService.saveUserInfo(whUser);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("102");
        }
        return res;
    }

    /**
     *检查身份证号码
     * @param idcard
     * @param uid
     * @return JSON : {
     *     "success" : "1"                  //1-成功； 其它失败
     *     "errormsg" : "错误码"          //101:身份证已存在  102：查询失败
     * }
     */
    @CrossOrigin
    @RequestMapping(value = "/info/checkIdcard", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseBean checkIdcard(String idcard,String uid){
        ResponseBean res = new ResponseBean();
        try {
            int result = this.apiUserService.checkIdcard(idcard,uid);
            if(result > 0){
                res.setSuccess(ResponseBean.FAIL);
                res.setErrormsg("101");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("102");
        }
        return res;
    }

    /**
     *  100  提交成功
     * @param id  用户ID
     * @param name  名字
     * @param idcard  身份证号码
     * @param idcardface  身份证正面照
     * @param idcardback   身份证反面照
     * @param accesstoken   微信access_token
     * @return  "errormsg" : "101|102|103|104|105|106"
     *   101、用户不存在  102、已经实名 103、已存在相同身份证 104、信息保存失败 105、未找到用户 106、文件未找到
     */
    @CrossOrigin
    @RequestMapping(value = "/saveRealname", method = {RequestMethod.POST, RequestMethod.GET})
    public Object saveRealname(String id, String name, String idcard, String idcardface, String idcardback, String accesstoken, String sex,HttpServletRequest req){
        ResponseBean res = new ResponseBean();
        WhUser_old user = new WhUser_old();
        try {
            if(id == null || "".equals(id)){
                res.setSuccess(ResponseBean.FAIL);
                res.setErrormsg("101");
                return res;
            }
            user.setSex(sex);
            user.setId(id);
            user.setName(name);
            user.setIdcard(idcard);
            user.setIdcardface(idcardface);
            user.setIdcardback(idcardback);
            res = this.apiUserService.saveRealnameInfo(user,accesstoken,req);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if("1001".equals(e.getMessage())){
                res.setSuccess(ResponseBean.FAIL);
                res.setErrormsg("106");
            }else{
                res.setSuccess(ResponseBean.FAIL);
                res.setErrormsg("104");
            }
        }
        return res;
    }

    /**
     * 修改个人信息接口
     * 访问地址 /api/user/info/editInfo
     * param:  nickname-昵称  nation-名族  origo-籍贯  job-职业  company-工作单位  address-通讯地址
     * @return JSON : {
     *     "success" : "1"                  //1-成功； 其它失败
     *     "errormsg" : "异常消息"          //101-保存失败 102-昵称为空或长度过长  103-名族为空或过长 104-籍贯为空或者过长
     *     105-职业为空或过长 106-工作单位为空或过长   107-通讯地址为空或过长   201-ID不存在
     * }
     */
    @CrossOrigin
    @RequestMapping(value = "/info/editInfo",method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseBean editUserInfo(HttpServletRequest request){
        ResponseBean res = new ResponseBean();
        try {
            res = this.apiUserService.editUserInfo(request);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("101");
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 检查手机号码是否存在
     * 访问地址 /api/user/info/checkPhone
     * param:  phone-电话号码
     * @return JSON : {
     *     "success" : "1"                  //1-成功； 其它失败
     *     "errormsg" : "异常消息"          //101-操作失败 102-手机格式不正确  103-号码不存在
     * }
     */
    @CrossOrigin
    @RequestMapping(value = "/info/checkPhone", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseBean checkPhone(String phone){
        ResponseBean res = new ResponseBean();
        try {
            res = this.apiUserService.checkPhone(phone);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("101");
            log.error(e.getMessage());
        }
        return res;
    }


    /**
     * 检查手机号码是否已经绑定
     * 访问地址 /api/user/info/isbind
     * param:  phone-电话号码 id--用户ID
     * @return JSON : {
     *     "success" : "1"                  //1-成功； 其它失败
     *     "errormsg" : "异常消息"          //101-操作失败 103-手机号已经被其它微信用户绑定  104-手机号已经被自己绑定
     * }
     */
    @CrossOrigin
    @RequestMapping(value = "/info/isbind", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseBean isBindPhone(String phone,String id){
        ResponseBean res = new ResponseBean();
        try {
            res = this.apiUserService.t_isBindPhone(phone,id);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("101");
            log.error(e.getMessage());
        }
        return res;
    }
}
