package com.creatoo.hn.controller.admin.system;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统管理模块子馆管理控制器
 * Created by wangxl on 2017/3/16.
 */
@RestController
@RequestMapping("/admin/system/cult")
public class WhgSystemCultController extends BaseController {
    /**
     * 文化馆服务类
     */
    @Autowired
    private WhgSystemCultService service;

    /**
     * 进入type(list|add|edit|view)视图
     * @param type 视图类型(list|add|edit|view)
     * @return
     */
    @RequestMapping("/view/{type}")
    @WhgOPT(optType = EnumOptType.CULT, optDesc = {"访问分馆列表页", "访问分馆添加页", "访问分馆编辑页"}, valid = {"type=list", "type=add", "type=edit"})
    public ModelAndView listview(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser whgSysUser, @PathVariable("type")String type, String id, String pt){
        ModelAndView view = new ModelAndView("admin/system/cult/view_"+type);
        try {
            if(StringUtils.isNotEmpty(id)){
                view.addObject("cult", service.t_srchOne(id));
            }
            if(StringUtils.isNotEmpty(pt)){
                view.addObject("pageType", pt);
            }
            view.setViewName("admin/system/cult/view_"+type);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }

        return view;
    }

    /**
     * 分页查询
     * @return
     */
    @RequestMapping(value = "/srchList4p")
    public ResponseBean srchList4p(
            @SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser,
            WhgSysCult cult,Integer page, Integer rows, String sort, String order,
            String inStates, String inLevel){
        ResponseBean res = new ResponseBean();
        try {
            if(cult.getLevel() != null && cult.getLevel().intValue() != 0){
                inLevel = null;
            }
            PageInfo<WhgSysCult> pageInfo = service.t_srchList4p(sysUser, cult, inStates, inLevel, page, rows, sort, order);
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
     * @param cult 条件对象
     * @return 对象列表
     */
    @RequestMapping(value = "/srchList")
    public List<WhgSysCult> srchList(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser, WhgSysCult cult, String sort, String order, String cultLevel){
        List<WhgSysCult> resList = new ArrayList<WhgSysCult>();
        try {
            resList = this.service.t_srchList(sort, order, cult, cultLevel);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return resList;
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
            WhgSysCult cult = service.t_srchOne(id);
            res.setData(cult);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 添加
     * @param cult 添加的资料
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/add")
    @WhgOPT(optType = EnumOptType.CULT, optDesc = {"添加"})
    public ResponseBean add(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser, WhgSysCult cult){
        ResponseBean res = new ResponseBean();
        try {
            service.t_add(cult, sysUser);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 编辑
     * @param cult 编辑的资料
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/edit")
    @WhgOPT(optType = EnumOptType.CULT, optDesc = {"编辑"})
    public ResponseBean edit(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser, WhgSysCult cult){
        ResponseBean res = new ResponseBean();
        try {
            service.t_edit(cult, sysUser);
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
    @WhgOPT(optType = EnumOptType.CULT, optDesc = {"删除"})
    public ResponseBean del(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser, String ids){
        ResponseBean res = new ResponseBean();
        try {
            service.t_del(ids, sysUser);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    @RequestMapping(value = "/undel")
    @WhgOPT(optType = EnumOptType.CULT, optDesc = {"还原"})
    public ResponseBean undel(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser, String ids){
        ResponseBean res = new ResponseBean();
        try {
            service.t_undel(ids);
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
    @WhgOPT(optType = EnumOptType.CULT, optDesc = {"启用", "停用"}, valid = {"toState=1", "toState=0"})
    public ResponseBean updstate(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser, String ids, String fromState, String toState){
        ResponseBean res = new ResponseBean();
        try {
            service.t_updstate(ids, fromState, toState, sysUser);
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
    @RequestMapping(value = "/updsqstate")
    @WhgOPT(optType = EnumOptType.CULT, optDesc = {"审核通过", "审核不通过"}, valid = {"toState=2", "toState=0"})
    public ResponseBean updsqstate(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser, String ids, String fromState, String toState){
        ResponseBean res = new ResponseBean();
        try {
            service.t_updsqstate(ids, fromState, toState, sysUser);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 排序
     * @param id 排序的ID
     * @param type up|top|idx
     * @param val type=idx时表示直接设置排序值
     * @return
     */
    @RequestMapping(value = "/sort")
    @WhgOPT(optType = EnumOptType.CULT, optDesc = {"上移","置顶"}, valid = {"type=up", "type=top"})
    public ResponseBean sort(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser, String id, String type, String val){
        ResponseBean res = new ResponseBean();
        try {
            service.t_sort(id, type, val, sysUser);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 是否上首页
     * @param ids
     * @param formupindex
     * @param toupindex
     * @return
     */
    @RequestMapping("/upindex")
    @WhgOPT(optType = EnumOptType.CULT, optDesc = {"上首页","取消上首页"}, valid = {"formupindex=1","formupindex=0"})
    public ResponseBean upindex(String ids, String formupindex, int toupindex){
        ResponseBean res = new ResponseBean();
        try {
            this.service.t_upindex(ids, formupindex, toupindex);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("上首页失败！");
            log.error(res.getErrormsg()+" formupindex: "+formupindex+" toupindex:"+toupindex+" ids: "+ids, e);
        }
        return res;
    }
}
