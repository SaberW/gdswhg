package com.creatoo.hn.controller.admin.system;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.util.MD5Util;
import com.creatoo.hn.util.RequestUtils;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统管理模块管理员管理控制器
 * Created by wangxl on 2017/3/16.
 */
@RestController
@RequestMapping("/admin/system/user")
public class WhgSystemUserController extends BaseController {
    /**
     * 日志控制器
     */
    Logger log = Logger.getLogger(this.getClass().getName());

    /**
     * 部门服务类
     */
    @Autowired
    private WhgSystemUserService service;

    /**
     * 进入type(list|add|edit|view)视图
     * @param request 请求对象
     * @param type 视图类型(list|add|edit|view)
     * @return
     */
    @RequestMapping("/view/{type}")
    @WhgOPT(optType = EnumOptType.ADMIN, optDesc = {"访问管理员列表页", "访问管理员添加页", "访问管理员编辑页"}, valid = {"type=list", "type=add", "type=edit"})
    public ModelAndView listview(HttpServletRequest request, @PathVariable("type")String type){
        ModelAndView view = new ModelAndView("admin/system/user/view_"+type);
        try{
            if("edit".equals(type)){
                String id = request.getParameter("id");
                view.addObject("adminuser", this.service.t_srchOne(id));
                view.addObject("adminuser_pms", this.service.t_srchUserCult_Role_Dept(id));
            }
        }catch (Exception e){

        }
        return view;
    }

    /**
     * 分页查询
     * @return
     */
    @RequestMapping(value = "/srchList4p")
    public ResponseBean srchList4p(HttpServletRequest request, WhgSysUser user){
        ResponseBean res = new ResponseBean();
        try {
            int page = RequestUtils.getPageParameter(request);
            int rows = RequestUtils.getRowsParameter(request);
            String sort = RequestUtils.getParameter(request, "sort");
            String order = RequestUtils.getParameter(request, "order");
            PageInfo<WhgSysUser> pageInfo = service.t_srchList4p(page, rows, sort, order, user);
            res.setRows(pageInfo.getList());
            res.setTotal(pageInfo.getTotal());
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 查询列表
     * @param request 请求对象
     * @param user 条件对象
     * @return 对象列表
     */
    @RequestMapping(value = "/srchList")
    public ResponseBean srchList(HttpServletRequest request, WhgSysUser user){
        ResponseBean res = new ResponseBean();
        try {
            String sort = RequestUtils.getParameter(request, "sort");
            String order = RequestUtils.getParameter(request, "order");
            List<WhgSysUser> list = service.t_srchList(sort, order, user);
            res.setRows(list);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 查询详情
     * @param request 请求对象
     * @param id 标识
     * @return 详情资料
     */
    @RequestMapping(value = "/srchOne")
    public ResponseBean srchOne(HttpServletRequest request, String id){
        ResponseBean res = new ResponseBean();
        try {
            WhgSysUser user = service.t_srchOne(id);
            res.setData(user);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 添加
     * @param request 请求对象
     * @param user 添加的资料
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/add")
    @WhgOPT(optType = EnumOptType.ADMIN, optDesc = {"添加"})
    public ResponseBean add(HttpServletRequest request, WhgSysUser user){
        ResponseBean res = new ResponseBean();
        try {
            String[] cultids = request.getParameterValues("pms_cultid");//权限分馆
            Map<String, String[]> cult_role = new HashMap<>();//权限分馆_角色
            Map<String, String[]> cult_dept = new HashMap<>();//权限分馆_部门
            if(cultids != null){
                for(String cultid: cultids){
                    String[] cult_roles = request.getParameterValues("pms_role_"+cultid);//权限分馆_角色
                    String[] cult_depts = request.getParameterValues("pms_dept_"+cultid);//权限分馆_部门
                    cult_role.put(cultid, cult_roles);
                    cult_dept.put(cultid, cult_depts);
                }
            }

            //保存密码到emps-此字段现在存密码明文base64加密后的字符串
            String password_user = request.getParameter("password2");
            user.setPasswordMemo(MD5Util.encode4Base64(password_user));

            service.t_add(user, cult_role, cult_dept, cultids, RequestUtils.getAdmin(request));
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 编辑
     * @param request 请求对象
     * @param user 编辑的资料
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/edit")
    @WhgOPT(optType = EnumOptType.ADMIN, optDesc = {"编辑"})
    public ResponseBean edit(HttpServletRequest request, WhgSysUser user){
        ResponseBean res = new ResponseBean();
        try {
            String[] cultids = request.getParameterValues("pms_cultid");//权限分馆
            Map<String, String[]> cult_role = new HashMap<>();//权限分馆_角色
            Map<String, String[]> cult_dept = new HashMap<>();//权限分馆_部门
            if(cultids != null){
                for(String cultid: cultids){
                    String[] cult_roles = request.getParameterValues("pms_role_"+cultid);//权限分馆_角色
                    String[] cult_depts = request.getParameterValues("pms_dept_"+cultid);//权限分馆_部门
                    cult_role.put(cultid, cult_roles);
                    cult_dept.put(cultid, cult_depts);
                }
            }

            //保存密码到emps-此字段现在存密码明文base64加密后的字符串
            String password_user = request.getParameter("password2");
            user.setPasswordMemo(MD5Util.encode4Base64(password_user));

            service.t_edit(user, cult_role, cult_dept, cultids, RequestUtils.getAdmin(request));
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 删除
     * @param request 请求对象
     * @param ids 用逗号分隔的多个ID
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/del")
    @WhgOPT(optType = EnumOptType.ADMIN, optDesc = {"删除"})
    public ResponseBean del(HttpServletRequest request, String ids){
        ResponseBean res = new ResponseBean();
        try {
            service.t_del(ids, RequestUtils.getAdmin(request));
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 修改状态
     * @param request 请求对象
     * @param ids 用逗号分隔的多个ID
     * @param fromState 修改之前的状态
     * @param toState 修改后的状态
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/updstate")
    @WhgOPT(optType = EnumOptType.ADMIN, optDesc = {"启用", "停用"}, valid = {"toState=1", "toState=0"})
    public ResponseBean updstate(HttpServletRequest request, String ids, String fromState, String toState){
        ResponseBean res = new ResponseBean();
        try {
            service.t_updstate(ids, fromState, toState, RequestUtils.getAdmin(request));
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }
}
