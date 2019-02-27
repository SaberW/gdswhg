package com.creatoo.hn.actions.api.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.creatoo.hn.ext.emun.EnumTypeClazz;
import com.creatoo.hn.services.home.userCenter.UserCenterService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.ext.emun.EnumOrderType;
import com.creatoo.hn.model.WhgActActivity;
import com.creatoo.hn.model.WhgActOrder;
import com.creatoo.hn.model.WhgActSeat;
import com.creatoo.hn.model.WhgActTime;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.services.home.agdwhhd.WhhdService;

import javax.servlet.http.HttpServletRequest;

/**
 * 活动预定接口
 * Created by wangxl on 2017/4/12.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/act")
public class APIActivityAction {
    /**
     * 日志控制器
     */
    Logger log = Logger.getLogger(this.getClass().getName());
    
    /**
	 * 公用服务类
	 */
	@Autowired
	public CommService commservice;
	
	/**
	 * 短信公开服务类
	 */
	@Autowired
	private SMSService smsService;
    
    @Autowired
    private WhhdService  whhdService;

    @Autowired
    private UserCenterService userCenterService;
    
    
    /**
     * 检查能否报名
     * 访问路径 /api/act/check/{actId}/{userId}
     * @param actId  活动Id
     * @param userId 用户Id
     * @return JSON: {
     * "success" : "1"             //1表示可以报名，其它失败
     * "errormsg" : "100|104"     //100-培训已失效;  104-未实名认证
     * }
     */
    @CrossOrigin
    @RequestMapping(value="/check/{actId}/{userId}", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseBean check(@PathVariable("actId")String actId, @PathVariable("userId")String userId){
        ResponseBean res = new ResponseBean();
        try{
            String validCode = this.whhdService.checkApplyAct(actId, userId);
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
     * 预定活动
     * 访问路径 /api/act/reserveAct
     * @param actId  活动Id
     * @param actOrder  订单信息 可参考whg_act_order(场次、预定人姓名、预定人手机号码)，POST的数据为此表的字段小写
     * @param userId  用户Id 
     * @param seatStr  在线选座 座位编号：座位1,座位2
     * @param seats  自由选座 座位数
     * @return JSON : {
     * "success" : "1"        //1表示报名成功，其它失败
     * "errormsg" : "105"     //100-活动Id不允许为空;101-活动场次Id不允许为空;102-用户Id不允许为空;103-座位数必须大于0;105-报名失败;106-该活动已下架;120-该活动已开始
     * }
     */
    
    @CrossOrigin
    @RequestMapping(value = "/reserveAct", method = {RequestMethod.POST,RequestMethod.GET})
    public ResponseBean reserveAct(String actId,WhgActOrder actOrder, String userId,String seatStr,int seats){
        ResponseBean res = new ResponseBean();
        try{

            //valid
            /*Map<String, String> rtnmap = whhdService.hdCheck(actId, userId, actOrder,seatStr, seats);

            if(!"0".equals(rtnmap.get("code"))){
                res.setSuccess(ResponseBean.FAIL);
                res.setErrormsg(rtnmap.get("code"));
            }else{
                //验证通过，报名活动
                String id = this.commservice.getKey("WhgActOrder");
                this.whhdService.hdOrder(id, actId, actOrder, userId, seatStr, seats);
            }*/

            Map<String, String> rtnmap = whhdService.synchdOrder(actId, userId, actOrder,seatStr, seats);

            this.commservice.addRepOrder(actId, actOrder.getId(), EnumTypeClazz.TYPE_ACTIVITY.getValue(), 1);
            if(!"0".equals(rtnmap.get("code"))){
                res.setSuccess(ResponseBean.FAIL);
                res.setErrormsg(rtnmap.get("code"));
            }

        } catch (Exception e){
            log.error(e.getMessage(), e);
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
        }
        return res;
    }

    /**访问地址  ： /api/act/cancelActOrder
     * 取消订单
     * @param request
     * @param orderId   订单ID
     * @return       * "success" : "1"        //1表示报名成功，其它失败
     *                  "errormsg" : "错误信息"
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/cancelActOrder", method = {RequestMethod.POST,RequestMethod.GET})
    public ResponseBean cancelActOrder(HttpServletRequest request, String orderId){
        ResponseBean res = new ResponseBean();
        res.setErrormsg("操作成功");
        if(orderId == null || "".equals(orderId)){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("活动订单ID丢失");
            return res;
        }
        WhgActOrder actOrder = userCenterService.findOrderDetail(orderId);
        if(actOrder == null || "".equals(actOrder)){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("活动未找到");
            return res;
        }else{
            actOrder.setTicketstatus(3);
            actOrder.setOrderisvalid(2);
            userCenterService.upActOrder(actOrder);
        }

        //发送取消短信
        try {
            WhgActActivity act = whhdService.getActDetail(actOrder.getActivityid());
            Map<String, String> smsData = new HashMap<String, String>();
            smsData.put("userName", actOrder.getOrdername());
            smsData.put("activityName", act.getName());
            smsData.put("ordernumber", actOrder.getOrdernumber());
            smsService.t_sendSMS(actOrder.getOrderphoneno(), "ACT_UNORDER", smsData, act.getId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("取消活动订单短信发送失败");
        }

        return res;
    }

}
