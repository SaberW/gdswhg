package com.creatoo.hn.controller.admin.system;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.dao.model.WhgSysDept;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.system.WhgSystemDeptService;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * 部门管理action
 * Created by Administrator on 2017/7/12.
 */
@Controller
@RequestMapping("/admin/system/dept")
public class WhgSystemDeptController extends BaseController{
    /**
     * 文化馆部门service
     */
    @Autowired
    private WhgSystemDeptService service;

    /**
     *进入部门管理视图
     * @return
     */
    @RequestMapping("/view/{type}")
    @WhgOPT(optType = EnumOptType.DEPT, optDesc = {"访问部门管理页面"}, valid = {"type=list"})
    public Object view(@PathVariable String type, ModelMap mmp){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("admin/system/dept/view_list");
        return mav;
    }

    /**
     * 查询列表
     * @param request 请求对象
     * @param dept 条件对象
     * @return 对象列表
     */
    @RequestMapping(value = "/srchList4p")
    @ResponseBody
    public ResponseBean srchList4p(HttpServletRequest request, WhgSysDept dept){
        ResponseBean res = new ResponseBean();
        try {
            List<WhgSysDept> list = service.t_srchList(dept);
            res.setRows(list);
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
     * @param dept 条件对象
     * @return 对象列表
     */
    @RequestMapping(value = "/srchList")
    @ResponseBody
    public List<WhgSysDept> srchList(HttpServletRequest request, WhgSysDept dept){
        List<WhgSysDept> list = new ArrayList<>();
        try {
            list = service.t_srchList(dept);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return list;
    }

    /**
     * 查询详情
     * @param request 请求对象
     * @param id 标识
     * @return 详情资料
     */
    @RequestMapping(value = "/srchOne")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.DEPT, optDesc = {"查询部门详情"})
    public ResponseBean srchOne(HttpServletRequest request, String id){
        ResponseBean res = new ResponseBean();
        try {
            WhgSysDept dept = service.t_srchOne(id);
            res.setData(dept);
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
     * @param dept 添加的资料
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.DEPT, optDesc = {"添加部门"})
    public ResponseBean add(HttpServletRequest request, WhgSysDept dept){
        ResponseBean res = new ResponseBean();
        try {
            WhgSysUser sysUser=(WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
            service.t_add(dept,sysUser);
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
     * @param dept 编辑的资料
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/edit")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.DEPT, optDesc = {"编辑部门资料"})
    public ResponseBean edit(HttpServletRequest request, WhgSysDept dept){
        ResponseBean res = new ResponseBean();
        try {
            WhgSysUser sysUser=(WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
            service.t_edit(dept,sysUser);
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
    @ResponseBody
    @WhgOPT(optType = EnumOptType.DEPT, optDesc = {"删除部门资料"})
    public ResponseBean del(HttpServletRequest request, String ids){
        ResponseBean res = new ResponseBean();
        try {
            service.t_del(ids, (WhgSysUser)request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY));
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
    @ResponseBody
    @WhgOPT(optType = EnumOptType.DEPT, optDesc = {"启用", "停用"}, valid = {"toState=1", "toState=0"})
    public ResponseBean updstate(HttpServletRequest request, String ids, String fromState, String toState){
        ResponseBean res = new ResponseBean();
        try {
            service.t_updstate(ids, fromState, toState, (WhgSysUser)request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY));
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }


}
