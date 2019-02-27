package com.creatoo.hn.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.login.MenusService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.util.*;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.enums.EnumConsoleSystem;
import com.creatoo.hn.util.enums.EnumOptType;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录控制器
 * Created by wangxl on 2017/7/13.
 */
@Controller
@RequestMapping("/admin")
public class LoginController extends BaseController {
    /**
     * 管理员服务
     */
    @Autowired
    private WhgSystemUserService whgSystemUserService;

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
    public ModelAndView login(HttpServletRequest request)throws Exception{
        ModelAndView view = new ModelAndView();
        try {
            HttpSession session = request.getSession();
            if(session != null){
                Object kickout2 = session.getAttribute("kickout2");
                if(kickout2 != null){
                    Integer _kickout = (Integer)kickout2;
                    if(_kickout.intValue() <= 1){
                        session.removeAttribute("kickout2");
                    }else{
                        session.setAttribute("kickout2", new Integer(_kickout-1));
                    }
                    view.addObject("kickout2", "kickout2");
                }
            }
            view.setViewName("admin/login");
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return view;
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
    public ModelAndView doLogin(String username, String password,String checkCode, HttpSession session)throws Exception{
        ModelAndView view = new ModelAndView();
        String sessionId = session.getId();

        try {
            sessionId = session.getId();
            if(isLock(sessionId)){
                view.addObject("isLock","0");
                view.addObject("msg", "连续登录失败多次，请15分钟后再登录！");
                view.setViewName("admin/login");
                return view;
            }


            WhgSysUser user = this.whgSystemUserService.doLogin(username, password);
            if("".equals(checkCode)){
//                view.addObject("msg", "验证码不能为空!");
//                view.setViewName("admin/login");
                if(setLogin(sessionId,false)){
                    view.addObject("isLock","0");
                    view.addObject("msg", "连续登录失败多次，请15分钟后再登录！");
                }else {
                    view.addObject("msg", "验证码不能为空!");
                }
                view.setViewName("admin/login");
                return view;
            }
            if (!checkCode.equalsIgnoreCase((String) session.getAttribute("randCheckCode"))) {
//                view.addObject("msg", "验证码错误!");
//                view.setViewName("admin/login");
                if(setLogin(sessionId,false)){
                    view.addObject("isLock","0");
                    view.addObject("msg", "连续登录失败多次，请15分钟后再登录！");
                }else {
                    view.addObject("msg", "验证码错误!");
                }
                view.setViewName("admin/login");
                return view;
            }
            if(user.getAdmintype()!=null && user.getAdmintype().equals(EnumConsoleSystem.masmgr.getValue())){
//                view.addObject("msg", "账号密码不正确!");
                if(setLogin(sessionId,false)){
                    view.addObject("isLock","0");
                    view.addObject("msg", "连续登录失败多次，请15分钟后再登录！");
                }else {
                    view.addObject("msg", "账号密码不正确!");
                }
                view.setViewName("admin/login");
                return view;
            }

            if (user != null){
                session.setAttribute(Constant.SESSION_ADMIN_CULT, JSON.toJSON(this.whgSystemUserService.loadManagerPMSCult(user.getId())));//权限分馆JSON格式
                session.setAttribute(Constant.SESSION_ADMIN_DEPT, JSON.toJSON(this.whgSystemUserService.loadManagerPMSDept(user.getId())));//权限部门JSON格式
                session.setAttribute(Constant.SESSION_ADMIN_KEY, user);
                view.addObject("isLock","1");
                setLogin(sessionId,true);
                view.setViewName("redirect:/admin");
            }else{
                if(setLogin(sessionId,false)){
                    view.addObject("isLock","0");
                    view.addObject("msg", "连续登录失败多次，请15分钟后再登录！");
                }else {
                    view.addObject("msg", "用户名或密码不正确");
                }
                view.setViewName("admin/login");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if(setLogin(sessionId,false)){
                view.addObject("isLock","0");
                view.addObject("msg", "连续登录失败多次，请15分钟后再登录！");
            }else {
                view.addObject("msg", e.getMessage());
            }
            //view.addObject("msg", e.getMessage());
            view.setViewName("admin/login");
        }
        return view;
    }
    /**
     * @return 刷新用户数据
     * @throws Exception
     */
    @RequestMapping(value = "/doRefresh", method = RequestMethod.POST)
    @ResponseBody
    @WhgOPT(optType = EnumOptType.CONSOLE_LOGIN, optDesc = "刷新用户数据")
    public Object doRefresh(HttpSession session)throws Exception{
        Map<String, Object> res = new HashMap<String, Object>();
        Map map=new HashMap();
        try {
            WhgSysUser user = (WhgSysUser)session.getAttribute(Constant.SESSION_ADMIN_KEY);
            if (user != null){
                Object str2= JSON.toJSON(this.whgSystemUserService.loadManagerPMSCult(user.getId()));
                Object str3= JSON.toJSON(this.whgSystemUserService.loadManagerPMSDept(user.getId()));

                session.setAttribute(Constant.SESSION_ADMIN_CULT,str2);//权限分馆JSON格式
                res.put(Constant.SESSION_ADMIN_CULT, str2);//权限分馆JSON格式

                session.setAttribute(Constant.SESSION_ADMIN_DEPT, str3);//权限部门JSON格式
                res.put(Constant.SESSION_ADMIN_DEPT, str3);//权限部门JSON格式
                session.setAttribute(Constant.SESSION_ADMIN_KEY, user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
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
            Subject currentUser = SecurityUtils.getSubject();
            if(currentUser != null){
                currentUser.logout();
            }
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
    public ModelAndView adminHome(@SessionAttribute(Constant.SESSION_ADMIN_KEY)WhgSysUser sysUser, HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        try {
            //主页上显示的时间
            view.addObject("curtDate", WeekDayUtil.getWeekStr(new Date()));

            if(EnumConsoleSystem.sysmgr.getValue().equals(sysUser.getAdmintype())){//区域管理员
                //view.setViewName("admin/admin_home_area");
                view.setViewName("redirect:/admin/tongji/bi/view/index");
            }else{//业务站点管理员
                //当前时间
                //本馆信息
                WhgSysCult cult = whgSystemUserService.t_srchUserCult(sysUser.getId());
                view.addObject("cultInfo", cult);
                view.addObject("imgPath", ENVUtils.env.getProperty("upload.local.server.addr"));

                if(cult!=null) {
                    view.addObject("cultid", cult.getId());
                }
                view.setViewName("admin/admin_home");
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return view;
    }

    /**
     * 修改密码
     */
    @ResponseBody
    @RequestMapping("/modipasManage")
    public Object upmodifyPwd(HttpSession session, String password1, String password2, HttpServletRequest request) {
        String success = "0";
        String errmasg = "";
        try {
            //得到session值 向下类型转换
            WhgSysUser user = RequestUtils.getAdmin(request);
            String account = user.getAccount();
            String pwd = user.getPassword();
            //判断用户不是超级管理员
            if (account != "administrator") {
                if (pwd.equals(password1)) {
                    String password4 = request.getParameter("password4");
                    this.whgSystemUserService.selectmagr(account, password2, password4);
                } else {
                    success = "1";
                    errmasg = "密码错误";
                }
            }
        } catch (Exception e) {
            success = "1";
            errmasg = e.getMessage();
        }
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("success", success);
        res.put("msg", errmasg);
        return res;
    }

    private boolean isLock(String sessionId){
        try {
            JSONObject jsonObject = (JSONObject) CacheUtil.getCache("myClientSessionLock",sessionId);
            Date date = jsonObject.getDate("sessionId");
            int saveSeconds = CompareTime.date2Seconds(date);
            saveSeconds += CompareTime.getSecondsByMin(15);//设置登录锁定时长
            int nowSeconds = CompareTime.date2Seconds(new Date());
            if(saveSeconds < nowSeconds){
                return false;
            }
            return true;
        }catch (Exception e){
            log.error(e.toString());
            return false;
        }
    }

    private boolean setLogin(String sessionId, boolean isSuccess){
        try {
            if(isSuccess){//登录成功，清除缓存
                CacheUtil.delCache("myClientSession",sessionId);
                return false;
            }
            Object myClientSession = CacheUtil.getCache("myClientSession",sessionId);
            if(null == myClientSession){//会话缓存数据，失败时记录
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("count",1);
                CacheUtil.putCache("myClientSession",sessionId, jsonObject,3600);
                return false;
            }
            Object cacheVal = CacheUtil.getCache("myClientSession",sessionId);
            JSONObject jsonObject = (JSONObject) cacheVal;
            int count = jsonObject.getInteger("count");//失败次数加1
            count++;
            if(count < 4){//3次以内, 记会话
                jsonObject.put("count",count);
                CacheUtil.putCache("myClientSession",sessionId,jsonObject,3600);
                return false;
            }else {//超过5次，锁住不让登录
                CacheUtil.delCache("myClientSession",sessionId);
                JSONObject lock = new JSONObject();
                lock.put("sessionId",new Date());
                CacheUtil.putCache("myClientSessionLock",sessionId,lock);
                return true;
            }
        }catch (Exception e){
            log.error(e.toString());
            return false;
        }
    }

}
