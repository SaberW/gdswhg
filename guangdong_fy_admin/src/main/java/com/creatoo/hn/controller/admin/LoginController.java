package com.creatoo.hn.controller.admin;

import com.alibaba.fastjson.JSON;
import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.login.LoginService;
import com.creatoo.hn.services.admin.login.MenusService;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录控制器
 * Created by wangxl on 2017/7/13.
 */
@RestController
@RequestMapping("/admin")
public class LoginController extends BaseController {
    /**
     * 登录服务类
     */
    @Autowired
    private LoginService loginService;

    /**
     * 权限菜单服务
     */
    @Autowired
    private MenusService menusService;

    /**
     * 控制台首页
     * @return 控制台首页
     * @throws Exception
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @WhgOPT(optType = EnumOptType.CONSOLE_LOGIN, optDesc = {"访问控制台首页"})
    public ModelAndView index()throws Exception{
        return new ModelAndView("admin/main");
    }

    /**
     * 控制台登录视图
     * @return 控制台登录视图
     * @throws Exception
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @WhgOPT(optType = EnumOptType.CONSOLE_LOGIN, optDesc = "访问控制台登录页面")
    public ModelAndView login()throws Exception{
        return new ModelAndView("admin/login");
    }

    /**
     * 登录
     * @param username 登录账号
     * @param password md5(登录密码)
     * @return 登录成功的视图
     * @throws Exception
     */
    @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
    @WhgOPT(optType = EnumOptType.CONSOLE_LOGIN, optDesc = "登录控制台")
    public ModelAndView doLogin(String username, String password, HttpSession session)throws Exception{
        ModelAndView view = new ModelAndView();
        try {
            WhgSysUser user = this.loginService.doLogin(username, password);
            if (user != null){
                session.setAttribute(Constant.SESSION_ADMIN_CULT_AND_SON, JSON.toJSON(this.loginService.loadManagerCult(user.getId(),username)));//权限分馆及下级子馆
                session.setAttribute(Constant.SESSION_ADMIN_CULT, JSON.toJSON(this.loginService.loadManagerPMSCult(user.getId(),username)));//权限分馆JSON格式
                //session.setAttribute(Constant.SESSION_ADMIN_CULT_OBJ, this.loginService.loadManagerPMSCult(user.getId()));//权限分馆
                session.setAttribute(Constant.SESSION_ADMIN_DEPT, JSON.toJSON(this.loginService.loadManagerPMSDept(user.getId(),username)));//权限部门JSON格式
                //session.setAttribute(Constant.SESSION_ADMIN_DEPT_OBJ, this.loginService.loadManagerPMSDept(user.getId()));//权限部门
                session.setAttribute(Constant.SESSION_ADMIN_KEY, user);
                view.setViewName("redirect:/admin");
            }else{
                view.addObject("msg", "用户名或密码不正确");
                view.setViewName("admin/login");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return view;
    }

    /**
     * 登记站点资料
     * @return 登录成功的视图
     * @throws Exception
     */
    @RequestMapping(value = "/doReqistZd", method =RequestMethod.GET)
    @WhgOPT(optType = EnumOptType.CONSOLE_LOGIN, optDesc = "登记站点资料")
    public ModelAndView doReqistZd()throws Exception{
        return new ModelAndView("admin/registZd");
    }

    /**
     * 登出
     * @return 登录视图
     * @throws Exception
     */
    @RequestMapping(value = "/doLogout", method = {RequestMethod.POST, RequestMethod.GET})
    @WhgOPT(optType = EnumOptType.CONSOLE_LOGIN, optDesc = "登出控制台")
    public ModelAndView doLogout(HttpSession session)throws Exception{
        ModelAndView view = new ModelAndView();
        try{
            view.setViewName("redirect:/admin/login");
            session.removeAttribute(Constant.SESSION_ADMIN_KEY);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return view;
    }

    /**
     * 加载菜单树型数据
     * @return 菜单数据
     */
    @RequestMapping("/loadMenus")
    @ResponseBody
    public Object getMenuData(String type){
        try {
            return this.menusService.getMeunsTree4Auth(SecurityUtils.getSubject());
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 控制台首页
     * @return 返回控制台首页
     */
    @RequestMapping("/admin_home")
    public ModelAndView adminHome(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        try {
            view.setViewName("admin/admin_home");


        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return view;
    }

    @RequestMapping("/view/{type}/{abc}")
    @WhgOPT(optType = EnumOptType.CONSOLE_LOGIN, optDesc = "测试")
    public Object test(@PathVariable("type")String type, @PathVariable("abc")String abc){
        ResponseBean bean = new ResponseBean();
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("abc", abc);
        bean.setData(map);
        return bean;
    }
}
