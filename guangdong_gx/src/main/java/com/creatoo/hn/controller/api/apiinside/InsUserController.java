package com.creatoo.hn.controller.api.apiinside;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.api.apiinside.InsUserService;
import com.creatoo.hn.util.MD5Util;
import com.creatoo.hn.util.bean.ApiResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 内部供需用户
 */
@SuppressWarnings("ALL")
@CrossOrigin
@Controller
@RequestMapping("/api/inside")
public class InsUserController extends BaseController {

    @Autowired
    private InsUserService insUserService;

    /**
     * 处理登录验证
     *
     * @param account
     * @param password
     * @return code
     * 101：登录失败
     * 102：帐号或密码不能为空
     * 103：帐号或密码不正确
     */
    @CrossOrigin
    @RequestMapping(value = "/validLogin", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object login(String account, String password) {
        ApiResultBean arb = new ApiResultBean();

        try {
            arb = (ApiResultBean) this.insUserService.login(account, password);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("登录失败");
            log.error(e.getMessage(), e);
        }

        return arb;
    }


    /**
     * 查询用户资料
     *
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/user/info", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object getUserInfo(String id) {
        ApiResultBean arb = new ApiResultBean();
        try {
            Object info = this.insUserService.findWhgSysUser(id);
            arb.setData(info);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 处理编辑资料
     * @param request
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/user/edit", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object userEdit(HttpServletRequest request,
                           @RequestParam(value = "id", required = true)String id){
        ApiResultBean arb = new ApiResultBean();

        try {
            String contact = request.getParameter("contact");
            String contactnum = request.getParameter("contactnum");
            String password = request.getParameter("password");//md5加密
            String password2 = request.getParameter("password2");//明文

            WhgSysUser recode = new WhgSysUser();
            recode.setContact(contact);
            recode.setContactnum(contactnum);
            recode.setPassword(password);
            recode.setPasswordMemo(MD5Util.encode4Base64(password2));
            arb = this.insUserService.editUser(id, recode);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("操作失败");
            log.error(e.getMessage(),e);
        }

        return arb;
    }

}
