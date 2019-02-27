package com.creatoo.hn.controller.admin.system;

import com.alibaba.fastjson.JSON;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.dao.model.WhgSysPms;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.system.WhgSystemPmsService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.util.MD5Util;
import com.creatoo.hn.util.RequestUtils;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumConsoleSystem;
import com.creatoo.hn.util.enums.EnumOptType;
import com.creatoo.hn.util.enums.EnumState;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 系统管理模块管理员管理控制器
 * Created by wangxl on 2017/3/16.
 */
@RestController
@RequestMapping("/admin/system/user")
public class WhgSystemUserController extends BaseController {
    /**
     * 管理员服务类
     */
    @Autowired
    private WhgSystemUserService service;

    /**
     * 文化馆服务类
     */
    @Autowired
    private WhgSystemCultService whgSystemCultService;

    /**
     * 权限组服务
     */
    @Autowired
    private WhgSystemPmsService whgSystemPmsService;

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
            //是否是添加或者编辑站点超级管理员
            boolean isSiteSuperMan = false;
            String cultid = null;

            if("add".equals(type)){
                String admintype = request.getParameter("admintype");//管理员类型
                String isbizmgr = request.getParameter("isbizmgr");//是否超级管理员
                if(EnumConsoleSystem.bizmgr.getValue().equals(admintype) && "1".equals(isbizmgr)){
                    isSiteSuperMan = true;
                    cultid = request.getParameter("cultid");
                }
            }

            if("edit".equals(type) || "info".equals(type)){
                String id = request.getParameter("id");
                WhgSysUser whgSysUser = this.service.t_srchOne(id);
                view.addObject("adminuser", whgSysUser);//账号信息
                view.addObject("adminjob", this.service.t_srchUserRole(id));//岗位

                if(EnumConsoleSystem.bizmgr.getValue().equals(whgSysUser.getAdmintype()) && "1".equals(whgSysUser.getIsbizmgr()+"")){
                    isSiteSuperMan = true;
                    cultid = whgSysUser.getCultid();
                }

                //部门
                List<String> deptids = this.service.getAllDeptId4PMS(id);
                view.addObject("admindept", StringUtils.join(deptids, ","));

                //权限文化馆(所属文化馆)名称
                if(StringUtils.isNotEmpty(whgSysUser.getCultid())){
                    WhgSysCult whgSysCult = this.whgSystemCultService.t_srchOne(whgSysUser.getCultid());
                    if(whgSysCult != null && StringUtils.isNotEmpty(whgSysCult.getName())){
                        view.addObject("cultname", whgSysCult.getName());
                    }
                }
            }

            // 权限分配
            if("powerallot_list".equals(type)){
                String userId = request.getParameter("id");
                String account = request.getParameter("account");
                view.addObject("userId", userId);
                view.addObject("account", account);
            }

            //如果添加或者编辑超级站点管理员时，查询文化馆权限
            if(isSiteSuperMan){
                WhgSysPms whgSysPms = new WhgSysPms();
                whgSysPms.setState(EnumState.STATE_YES.getValue());
                whgSysPms.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
                List<WhgSysPms> pmsList = this.whgSystemPmsService.t_srchList(whgSysPms, null, null, cultid);
                view.addObject("pmsList", JSON.toJSON(pmsList));
            }else{
                view.addObject("pmsList", "[]");
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
            Integer areaLevel = RequestUtils.getIntParameter(request, "areaLevel");
            String areaValue = RequestUtils.getParameter(request, "areaValue");
            WhgSysUser admin = RequestUtils.getAdmin(request);
            PageInfo<WhgSysUser> pageInfo = service.t_srchList4p(page, rows, sort, order, user, admin, areaLevel, areaValue);
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
            //管理员类型
            String admintype = RequestUtils.getParameter(request, "admintype");//sysmgr-区域管理员 bizmgr-站点管理员
            String cultid = RequestUtils.getParameter(request, "cultid");//站点管理员对应的文化馆标识
            String cultids = null;
            String[] cult_dept = null;
            String cult_role = null;
            if(EnumConsoleSystem.sysmgr.getValue().equals(admintype)){
                cult_role = RequestUtils.getParameter(request, "adminjob");//岗位-
            }else{
                user.setCultid(cultid);
                if(user.getIsbizmgr().intValue() != 1) {//普通站点管理员
                    cult_dept = request.getParameterValues("admindept");
                }
                cultids = cultid;//站点管理员权限馆和所属馆一致
                cult_role = RequestUtils.getParameter(request, "adminjob");//岗位-
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
            //管理员类型
            String admintype = RequestUtils.getParameter(request, "admintype");//sysmgr-区域管理员 bizmgr-站点管理员
            String cultid = RequestUtils.getParameter(request, "cultid");//站点管理员对应的文化馆标识
            String cultids = null;
            String[] cult_dept = null;
            String cult_role = null;
            if(EnumConsoleSystem.sysmgr.getValue().equals(admintype)){
                cult_role = RequestUtils.getParameter(request, "adminjob");//岗位-
            }else{
                user.setCultid(cultid);
                if(user.getIsbizmgr().intValue() != 1) {//普通站点管理员
                    cult_dept = request.getParameterValues("admindept");
                }
                cultids = cultid;//站点管理员权限馆和所属馆一致
                cult_role = RequestUtils.getParameter(request, "adminjob");//岗位-
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
