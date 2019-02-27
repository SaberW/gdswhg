package com.creatoo.hn.actions.admin;

import com.creatoo.hn.services.admin.sysuser.AdminService;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * Created by rbg on 2017/7/7.
 */

@Controller
@RequestMapping("/admin")
public class IndexAction {
    Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private AdminService adminService;

    /**进入后台首页
     * @return
     */
    @RequestMapping("")
    public ModelAndView index(){
        return new ModelAndView( "admin/main" );
    }

    /**处理管理员登录
     * @param session
     * @param request
     * @return
     */
    @RequestMapping("/logindo")
    public ModelAndView logindo(HttpSession session, WebRequest request){
        ModelAndView mav = new ModelAndView();

        try {
            String name = request.getParameter("name");
            String password = request.getParameter("password");
            Object admin = this.adminService.logindo(name, password);

            if (admin != null){
                session.setAttribute("user", admin);
                mav.setViewName("redirect:/admin");
            }else{
                mav.addObject("msg", "用户名或密码不正确");
                mav.setViewName("admin/login");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            mav.addObject("msg", "用户名或密码不正确");
            mav.setViewName("admin/login");
        }

        return mav;
    }


    /**处理管理员登出
     * @param session
     * @return
     */
    @RequestMapping("/loginout")
    public ModelAndView loginout(HttpSession session){
        ModelAndView mav = new ModelAndView();

        try {
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.getSession().removeAttribute("user");
            currentUser.logout();

            session.removeAttribute("user");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        mav.setViewName("admin/login");
        return mav;
    }


    /**加载菜单树型数据
     * @return
     */
    @RequestMapping("/loadMenus")
    @ResponseBody
    public Object getMenuData(String type){
        try {
            return this.adminService.getMenuList(type);
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return null;
        }
    }

    @RequestMapping("/admin_home")
    public String adminHome(){
        return "admin/admin_home";
    }
}
