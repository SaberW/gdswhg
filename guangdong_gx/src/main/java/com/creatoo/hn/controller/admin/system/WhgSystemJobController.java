package com.creatoo.hn.controller.admin.system;

import com.alibaba.fastjson.JSON;
import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysJob;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.system.WhgSystemJobService;
import com.creatoo.hn.util.RequestUtils;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumConsoleSystem;
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
 * 岗位控制器
 * Created by wangxl on 2018/1/17.
 */
@RestController
@RequestMapping("/admin/system/job")
public class WhgSystemJobController extends BaseController {
    /**
     * 岗位服务
     */
    @Autowired
    private WhgSystemJobService whgSystemJobService;

    /**
     * 视图控制器
     * @param whgSysUser 管理员对象
     * @param type 视图名称
     * @return 指定视图
     */
    @RequestMapping("/view/{type}")
    @WhgOPT(optType= EnumOptType.POST, optDesc={"访问岗位管理页面","访问添加页面","访问岗位页面"}, valid={"type=list","type=add", "type=edit"})
    public ModelAndView view(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser whgSysUser, @PathVariable String type, String id){
        ModelAndView view = new ModelAndView();
        try {
            view.addObject("isSuperMan", Constant.SUPER_USER_NAME.equals(whgSysUser.getAccount()));
            if (StringUtils.isNotEmpty(id)) {
                view.addObject("job", this.whgSystemJobService.t_srchOne(id));
                view.addObject("job_pms", JSON.toJSON(this.whgSystemJobService.t_srchJobPms(id)));
            }else{
                view.addObject("job_pms", JSON.toJSON( new ArrayList() ));
            }

            //根据管理员获得sysflag
            WhgSysJob whgSysJob = new WhgSysJob();
            this.whgSystemJobService.getSysflag(whgSysUser, whgSysJob);
            view.addObject("sysflag", whgSysJob.getSysflag());

            view.setViewName("admin/system/job/view_"+type);
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return view;
    }

    /**
     * 分页查询
     * @param request 请求对象
     * @param whgSysJob 条件对象
     * @return ResponseBean
     */
    @RequestMapping(value = "/srchList4p")
    public ResponseBean srchList4p(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser whgSysUser, HttpServletRequest request, WhgSysJob whgSysJob){
        ResponseBean res = new ResponseBean();
        try {
            int page = RequestUtils.getPageParameter(request);
            int rows = RequestUtils.getRowsParameter(request);
            String sort = RequestUtils.getParameter(request, "sort");
            String order = RequestUtils.getParameter(request, "order");
            PageInfo<WhgSysJob> pageInfo = this.whgSystemJobService.t_srchList4p(whgSysUser, whgSysJob, page, rows, sort, order);
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
     * @param request 请求对象
     * @param whgSysJob 条件对象
     * @return
     */
    @RequestMapping(value = "/srchList")
    public List<WhgSysJob> srchList(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser whgSysUser, HttpServletRequest request, WhgSysJob whgSysJob){
        List<WhgSysJob> resList = new ArrayList<>();
        try {
            String sort = RequestUtils.getParameter(request, "sort");
            String order = RequestUtils.getParameter(request, "order");
            resList = this.whgSystemJobService.t_srchList(whgSysUser, whgSysJob, sort, order);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return resList;
    }

    /**
     * 查询详情
     * @param id 岗位标识
     * @return 详情资料
     */
    @RequestMapping(value = "/srchOne")
    public ResponseBean srchOne(String id){
        ResponseBean res = new ResponseBean();
        try {
            WhgSysJob whgSysJob = this.whgSystemJobService.t_srchOne(id);
            res.setData(whgSysJob);
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
    @WhgOPT(optType=EnumOptType.POST, optDesc="添加")
    public ResponseBean add(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser whgSysUser, WhgSysJob whgSysJob, String[] pms){
        ResponseBean res = new ResponseBean();
        try {
            if(pms == null || pms.length < 1){
                throw new Exception("未配置权限组");
            }
            this.whgSystemJobService.t_add(whgSysUser, whgSysJob, pms);
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
    @WhgOPT(optType=EnumOptType.POST, optDesc="编辑")
    public ResponseBean edit(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser whgSysUser, WhgSysJob whgSysJob, String[] pms){
        ResponseBean res = new ResponseBean();
        try {
            if(pms == null || pms.length < 1){
                throw new Exception("未配置权限组");
            }
            this.whgSystemJobService.t_edit(whgSysUser, whgSysJob, pms);
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
    @WhgOPT(optType=EnumOptType.POST, optDesc={"启用", "停用"}, valid={"toState=1", "toState=0"})
    public ResponseBean updstate(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser whgSysUser, String ids, String fromState, String toState){
        ResponseBean res = new ResponseBean();
        try {
            this.whgSystemJobService.t_updstate(ids, fromState, toState, whgSysUser);
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
    @WhgOPT(optType=EnumOptType.POST, optDesc={"删除"})
    public ResponseBean del(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser whgSysUser, String ids){
        ResponseBean res = new ResponseBean();
        try {
            this.whgSystemJobService.t_del(whgSysUser, ids);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }
}
