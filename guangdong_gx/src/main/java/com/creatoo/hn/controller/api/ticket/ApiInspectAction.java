package com.creatoo.hn.controller.api.ticket;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.services.api.ticket.ApiInspectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rbg on 2017/4/12.
 */
@SuppressWarnings("ALL")
@CrossOrigin
@RestController
@RequestMapping("/ticket")
public class ApiInspectAction extends BaseController{

    @Autowired
    private ApiInspectService inspectService;

    /**
     * 用户登录
     * @param userAccount  管理员帐号
     * @param userPassword  md5密码文
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/checkTicket/loginCheckSysUser", method = {RequestMethod.POST, RequestMethod.GET})
    public Object loginCheckSysUser(String userAccount, String userPassword){
        Map<String, Object> rest = new HashMap();
        try {
            rest = this.inspectService.loginCheckSysUser(userAccount, userPassword);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rest.put("status", "RESULT_ERROR_CODE_99999");
            rest.put("msg", e.getMessage());
        }
        return rest;
    }



    /**
     * 验证活动
     * @param userId
     * @param orderValidateCode  活动订单号
     * @param userAccount
     * @param seats    活动订单座位seatcode
     * @param orderPayStatus
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/checkTicket/activityCode", method = {RequestMethod.POST, RequestMethod.GET})
    public Object activityCode(String userId, String orderValidateCode, String userAccount,
                               String seats) {
        Map<String, Object> rest = new HashMap();
        try {
            rest = this.inspectService.activityCode(userId, orderValidateCode, userAccount, seats);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rest.put("status", "RESULT_ERROR_CODE_99999");
            rest.put("msg", e.getMessage());
        }
        return rest;
    }

    @CrossOrigin
    @RequestMapping(value = "/checkTicket/orderActivityCode", method = {RequestMethod.POST, RequestMethod.GET})
    public Object orderActivityCode(String orderValidateCode){
        Map<String, Object> rest = new HashMap();
        try {
            rest = this.inspectService.orderActivityCode(orderValidateCode);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rest.put("status", "RESULT_ERROR_CODE_99999");
            rest.put("msg", e.getMessage());
        }
        return rest;
    }


    /**
     * 验证场馆
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/checkTicket/roomCode", method = {RequestMethod.POST, RequestMethod.GET})
    public Object roomCode(String userId, String orderValidateCode, String userAccount,
                           String roomOderId, String bookStatus){
        Map<String, Object> rest = new HashMap();
        try {
            rest = this.inspectService.roomCode(userId, orderValidateCode, userAccount, roomOderId, bookStatus);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rest.put("status", "RESULT_ERROR_CODE_99999");
            rest.put("msg", e.getMessage());
        }
        return rest;
    }

    @CrossOrigin
    @RequestMapping(value = "/checkTicket/orderRoomCode", method = {RequestMethod.POST, RequestMethod.GET})
    public Object orderRoomCode(String orderValidateCode){
        Map<String, Object> rest = new HashMap();
        try {
            rest = this.inspectService.orderRoomCode( orderValidateCode);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rest.put("status", "RESULT_ERROR_CODE_99999");
            rest.put("msg", e.getMessage());
        }
        return rest;
    }
}
