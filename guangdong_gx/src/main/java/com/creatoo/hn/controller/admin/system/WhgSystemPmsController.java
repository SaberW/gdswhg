package com.creatoo.hn.controller.admin.system;

import com.alibaba.fastjson.JSON;
import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysPms;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.login.MenusService;
import com.creatoo.hn.services.admin.system.WhgSystemPmsService;
import com.creatoo.hn.util.RequestUtils;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumConsoleSystem;
import com.creatoo.hn.util.enums.EnumOptType;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.type.classreading.AnnotationMetadataReadingVisitor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限组控制器
 * Created by wangxl on 2018/1/16.
 */
@RestController
@RequestMapping("/admin/system/pms")
public class WhgSystemPmsController extends BaseController {
    /**
     * 权限组服务
     */
    @Autowired
    private WhgSystemPmsService whgSystemPmsService;

    /**
     * 菜单服务
     */
    @Autowired
    private MenusService menusService;

    /**
     * 权限组视图控制器
     * @param whgSysUser 管理员对象
     * @param type 视图名称
     * @return 指定视图
     */
    @RequestMapping("/view/{type}")
    @WhgOPT(optType=EnumOptType.ConsoleSystem_PermissionGroup,
            optDesc={"访问权限组管理页面","访问添加权限组页面","访问编辑权限组页面"},
            valid={"type=list","type=add", "type=edit"})
    public ModelAndView view(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser whgSysUser, @PathVariable String type, String sysflag, String id){
        ModelAndView view = new ModelAndView();
        try {
            if (StringUtils.isNotEmpty(sysflag)) {
                view.addObject("sysflag", sysflag);
            }
            view.addObject("isSuperMan", Constant.SUPER_USER_NAME.equals(whgSysUser.getAccount()));
            if ("add".equals(type) || "edit".equals(type)) {
                Map<String, String> optMap = this.menusService.getOptsList();
                view.addObject("optMap", JSON.toJSON(optMap));
            }
            if (StringUtils.isNotEmpty(id)) {
                view.addObject("pmsgroup", this.whgSystemPmsService.t_srchOne(id));//权限组分类
                view.addObject("rpms", this.whgSystemPmsService.parsePmsDetailJson(this.whgSystemPmsService.t_srchPmsDetail(id)));//权限组中的菜单权限
                view.addObject("choiceCults", JSON.toJSON(this.whgSystemPmsService.t_srchPmsScope(id)));//已分配到站点的权限组
            }
            view.setViewName("admin/system/pms/view_"+type);
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return view;
    }

    /**
     * 分页查询
     * @param request 请求对象
     * @param whgSysPms 条件对象
     * @return ResponseBean
     */
    @RequestMapping(value = "/srchList4p")
    public ResponseBean srchList4p(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser whgSysUser, HttpServletRequest request, WhgSysPms whgSysPms){
        ResponseBean res = new ResponseBean();
        try {
            int page = RequestUtils.getPageParameter(request);
            int rows = RequestUtils.getRowsParameter(request);
            String sort = RequestUtils.getParameter(request, "sort");
            String order = RequestUtils.getParameter(request, "order");
            String cultid = RequestUtils.getParameter(request, "cultid");
            //区域管理员：可以管理bizmgr类型的权限组; administrator:可以管理bizmgr和sysmgr类型的权限组
            /*if(!Constant.SUPER_USER_NAME.equals(whgSysUser.getAccount())){
                whgSysPms.setSysflag(EnumConsoleSystem.bizmgr.name());
            }*/
            PageInfo<WhgSysPms> pageInfo = this.whgSystemPmsService.t_srchList4p(whgSysPms, page, rows, sort, order, cultid);
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
     * 列表查询
     * @param whgSysPms 条件对象
     * @return
     */
    @RequestMapping(value = "/srchList")
    public List<WhgSysPms> srchList(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser whgSysUser, WhgSysPms whgSysPms){
        List<WhgSysPms> resList = new ArrayList<WhgSysPms>();
        try {
            //查询权限组: 站点管理员只站点权限组
            String cultid = null;
            if(Constant.SUPER_USER_NAME.equals(whgSysUser.getAccount()) || EnumConsoleSystem.sysmgr.getValue().equals(whgSysUser.getAdmintype())){
                whgSysPms.setSysflag(EnumConsoleSystem.sysmgr.getValue());
            }else{
                whgSysPms.setSysflag(EnumConsoleSystem.bizmgr.getValue());
                cultid = whgSysUser.getCultid();//站点管理员权限馆等于所属馆
            }
            resList = this.whgSystemPmsService.t_srchList(whgSysPms, null, null, cultid);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return resList;
    }

    /**
     * 查询权限组是否已经适应到区域
     * @param pmsid 权限组
     * @param areaLevel 级别
     * @param areaVal 区域名称
     * @return
     */
    @RequestMapping(value = "/srchScopeArea")
    public Object srchScopeArea(String pmsid, Integer areaLevel, String areaVal){
        ResponseBean res = new ResponseBean();
        try {
            boolean scope = this.whgSystemPmsService.t_countScopeArea(pmsid, areaLevel, areaVal);
            res.setData(scope);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 获取权限组的可用分组
     * @return
     */
    @RequestMapping(value = "/srchList4PmsType")
    public List<Map<String,String>> srchPmsGroupType(String sysflag){
        List<Map<String,String>> list = new ArrayList<>();
        try {
            WhgSysPms whgSysPms = new WhgSysPms();
            whgSysPms.setSysflag(sysflag);
            List<WhgSysPms> listPms = this.whgSystemPmsService.t_srchList(whgSysPms, null, null, null);
            if(listPms != null){
                Map<String, String> exists = new HashMap<>();
                for(WhgSysPms sysPms : listPms){
                    if(!exists.containsKey(sysPms.getType())){
                        exists.put(sysPms.getType(), sysPms.getType());
                        Map<String, String> map = new HashMap<>();
                        map.put("id", sysPms.getType());
                        map.put("text", sysPms.getType());
                        list.add(map);
                    }
                }
            }
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return list;
    }

    /**
     * 查询详情
     * @param id 权限组标识
     * @return 详情资料
     */
    @RequestMapping(value = "/srchOne")
    public ResponseBean srchOne(String id){
        ResponseBean res = new ResponseBean();
        try {
            WhgSysPms whgSysPms = this.whgSystemPmsService.t_srchOne(id);
            res.setData(whgSysPms);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 查询菜单及操作:根据系统标记取不同的菜单和操作
     * @param sysflag sysmrg-总分馆管理系统；bizmgr-业务管理系统
     * @return
     */
    @RequestMapping("/srchMenuTree")
    public ResponseBean srchMenuTree(String sysflag){
        ResponseBean res = new ResponseBean();
        try{
            //所有菜单
            List<Map> menuTree = this.menusService.getMeunsTree4Sysflag(sysflag);
            res.setRows(menuTree);
            res.setTotal(menuTree.size());
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 添加
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/add")
    @WhgOPT(optType=EnumOptType.ConsoleSystem_PermissionGroup, optDesc="添加权限组")
    public ResponseBean add(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser whgSysUser, WhgSysPms whgSysPms, String[] pms){
        ResponseBean res = new ResponseBean();
        try {
            if(pms == null || pms.length < 1){
                throw new Exception("功能配置未设值");
            }
            this.whgSystemPmsService.t_add(whgSysUser, whgSysPms, pms);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    @RequestMapping(value = "/saveScope")
    @WhgOPT(optType=EnumOptType.ConsoleSystem_PermissionGroup, optDesc="配置权限组适应站点")
    public ResponseBean saveScope(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser whgSysUser, String pmsid, String adds, String dels, String scopeArea){
        ResponseBean res = new ResponseBean();
        try {
            this.whgSystemPmsService.t_saveScope(pmsid, adds, dels, scopeArea);
            res.setData(this.whgSystemPmsService.t_srchPmsScope(pmsid));
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 编辑
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/edit")
    @WhgOPT(optType=EnumOptType.ConsoleSystem_PermissionGroup, optDesc="编辑权限组")
    public ResponseBean edit(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser whgSysUser, WhgSysPms whgSysPms, String[] pms){
        ResponseBean res = new ResponseBean();
        try {
            if(pms == null || pms.length < 1){
                throw new Exception("功能配置未设值");
            }
            this.whgSystemPmsService.t_edit(whgSysUser, whgSysPms, pms);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 修改状态
     * @param ids 用逗号分隔的多个ID
     * @param fromState 修改之前的状态
     * @param toState 修改后的状态
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/updstate")
    @WhgOPT(optType=EnumOptType.ConsoleSystem_PermissionGroup, optDesc={"启用", "停用"}, valid={"toState=1", "toState=0"})
    public ResponseBean updstate(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser whgSysUser, String ids, String fromState, String toState){
        ResponseBean res = new ResponseBean();
        try {
            this.whgSystemPmsService.t_updstate(ids, fromState, toState, whgSysUser);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }


    /**
     * 删除
     * @param ids 用逗号分隔的多个ID
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/del")
    @WhgOPT(optType=EnumOptType.ConsoleSystem_PermissionGroup, optDesc={"删除"})
    public ResponseBean del(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser whgSysUser, String ids){
        ResponseBean res = new ResponseBean();
        try {
            this.whgSystemPmsService.t_del(whgSysUser, ids);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }
}
