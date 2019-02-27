package com.creatoo.hn.actions.api.train;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.ext.emun.EnumBMState;
import com.creatoo.hn.ext.emun.EnumTypeClazz;
import com.creatoo.hn.model.WhgTraEnrol;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.services.home.agdpxyz.PxbmService;
import com.creatoo.hn.services.home.userCenter.MyEnrollService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import weibo4j.http.Response;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提供培训报名接口
 * Created by wangxl on 2017/4/12.
 */
@RestController
@RequestMapping("/api/tra")
public class APITrainAction {
    /**
     * 日志控制器
     */
    Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    public MyEnrollService enrollService;

    /**
     * 报名服务
     */
    @Autowired
    private PxbmService service;

    @Autowired
    private CommService commService;

    /**
     * 检查能否报名
     * 访问路径 /api/tra/check/{traId}/{userId}
     * @param traId 培训ID
     * @param userId 用户ID
     * @return JSON: {
     * "success" : "1"                        //1表示可以报名，其它失败
     * "errormsg" : "100|101|102|103|104"     //100-培训已失效; 101-培训报名已结束; 102-培训报名额已满; 103-已经报名不能重报; 104-未实名认证
     * }
     */
    @CrossOrigin
    @RequestMapping("/check")
    public ResponseBean check(String traId, @RequestParam(required = false) String userId){
        ResponseBean res = new ResponseBean();
        try{
            String validCode = this.service.checkApplyTrain(traId, userId);
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
     * 报名(POST提交数据)
     * 访问路径 /api/tra/enrol
     * @param        -培训ID(whg_tra.id)
     * @param      -真实姓名
     * @param      -出生年月日（yyyy-MM-dd）
     * @param           -性别(2-女； 1-男; 3-保密)
     * @param      -证件类型(1-身份证,2-户口本,其它)
     * @param        -证件号码
     * @param  -联系手机号码
     * @param userId        -报名的用户Id
     * @return JSON : {
     * "success" : "1"                        //1表示报名成功，其它失败
     * "errormsg" : "100|101|102|103|104|105|106|107"
     *   //100-培训已失效; 101-培训报名已结束; 102-培训报名额已满; 103-已经报名不能重报; 104-未实名认证; 105-报名失败 106-已经报了两场普及班 107-年龄段不符合 201:-请完善资料
     *   203-请填写真实姓名  205-未绑定手机
     * }
     */
    @CrossOrigin
    @RequestMapping(value = "/enrol", method = RequestMethod.POST)
    public ResponseBean enroll(WhgTraEnrol enrol, String userId){
        ResponseBean res = new ResponseBean();
        try{
            /*String validCode = this.service.checkApplyTrain(enrol.getTraid(), userId);
            if(!"0".equals(validCode)){
                res.setSuccess(ResponseBean.FAIL);
                res.setErrormsg(validCode);
            }else{
                String birthday = new java.text.SimpleDateFormat("yyyy-MM-dd").format(enrol.getBirthday());
                validCode = service.validAge(enrol, birthday, userId);
                if(!"0".equals(validCode)) {
                    res.setSuccess(ResponseBean.FAIL);
                    res.setErrormsg(validCode);
                }else{
                    service.addTranEnrol(enrol, "", userId);
                }
            }*/
//            String birthday = "";
//            if (enrol!=null && enrol.getBirthday()!=null){
//                birthday = new java.text.SimpleDateFormat("yyyy-MM-dd").format(enrol.getBirthday());
//            }
            String validCode = service.syncAddTranEnrol(enrol, userId);

            this.commService.addRepOrder(enrol.getTraid(), enrol.getId(), EnumTypeClazz.TYPE_TRAIN.getValue(), 1);
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
     *  培训订单号查询签名列表
     *  访问路径 /api/tra/signList/{orderId}
     * @param orderId 用户的培训订单号whg_tra_enrol.orderid
     * @return JSON : {
     * "success" : "1"                        //1表示请求成功，其它失败
     * "errormsg" : ""                        //请求失败时的异常信息
     *  "data": [
     *      {
     *          "traid":"培训ID",
     *          "enrolid":"报名ID",
     *          "courseid":"培训课程ID",
     *          "starttime":"课程开始时间yyyy-MM-dd HH:mm:ss",
     *          "endtime":"课程结束时间",
     *          "sign":"签到状态 0-未签到, 1-已签到"}
     *          "signtime":"签到时间yyyy-MM-dd HH:mm:ss",
     *          "userid":"签到会员ID"}
     *      ,...]                     //data=签名列表
     * }
     */
    @CrossOrigin
    @RequestMapping(value = "/signList/{orderId}")
    public ResponseBean signInfo(String orderId){
        ResponseBean res = new ResponseBean();
        try{
            List<Map<String, String>> eclist = this.service.queryEnrolCourseList(orderId);
            res.setData(eclist);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
        }
        return res;
    }

    /**
     * 培训课程签到  POST请求
     * 访问路径 /api/tra/sign
     * @param cardno 身份证号码
     * @return JSON ： {
     *  "success" : "1"             //1-签到成功, 其它签到失败
     *  "errormsg" : "100|101|102|103|109"（返回的只有这个状态码，具体提示文字需要前端来写）           // 100-签到成功; 101-没有报名信息; 102-没有课程信息; 103-课程已取消
     * }
     */
    @CrossOrigin
    @RequestMapping(value = "/sign",method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseBean signUp(String cardno){
        ResponseBean res = new ResponseBean();
        try{
            String code = this.service.signup(cardno);
            if(!"100".equals(code)){
                res.setSuccess(ResponseBean.FAIL);
            }
            res.setErrormsg(code);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("109");
        }
        return res;
    }


    /**
     * 所有日期类型使用指定格式转换
     * @param webDataBinder
     * @throws Exception
     */
    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class , new CustomDateEditor(simpleDateFormat , true));
    }

    /**
     * 访问地址  /api/tra/delmyenroll
     * 取消我的报名  （如果未取消会将状态改为已取消，如果是已取消状态将会对这条记录进行删除）
     * param  :  id-订单ID   state-订单状态
     * @return JSON ： {
     *  "success" : "1"             //1-操作成功, 其它失败
     *  "errormsg" : "101"         //101-操作失败  102-无法取消
     * }
     */
    @CrossOrigin
    @RequestMapping(value = "/delmyenroll", method = RequestMethod.POST)
    public ResponseBean delmyenroll(String id, String state){
        ResponseBean res = new ResponseBean();
        WhgTraEnrol enrol = new WhgTraEnrol();
//        String id = request.getParameter("id");
//        String state = request.getParameter("state");

        try {
            if(("1").equals(state)){
                enrol.setState(EnumBMState.BM_QXBM.getValue());
                enrol.setId(id);
                this.enrollService.delMyEnroll(enrol);
            }else{
                res.setSuccess(ResponseBean.FAIL);
                res.setErrormsg("102");  //无法取消
                return res;
            }
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("101");  //操作失败
            log.error(e.getMessage());
        }
        return res;
    }

    /** 访问地址：  /api/tra/saveSignData
     * 保存签到信息
     * @param name  姓名
     * @param sex   性别 （0、女 1、男）
     * @param idcard  身份证号码
     * @return  成功：success:1   失败：success:0   失败原因：errormsg：“”
     */
    @RequestMapping(value = "/saveSignData",method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    @CrossOrigin
    public ResponseBean saveSignData(String name,Integer sex,String idcard){
        ResponseBean rsb = new ResponseBean();
        try {
            if(idcard == null || name == null){
                rsb.setSuccess(ResponseBean.FAIL);
                rsb.setErrormsg("109");//缺少参数
                return rsb;
            }

            rsb = this.service.insertSignData(name,sex,idcard);
        } catch (Exception e) {
            rsb.setSuccess(ResponseBean.FAIL);
            rsb.setErrormsg("109");  //操作失败
            log.error(e.getMessage());
        }
        return rsb;
    }
}
