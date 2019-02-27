package com.creatoo.hn.controller.admin.system;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.util.RequestUtils;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
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
@Api(tags = "文化馆管理")
public class WhgSystemCultController extends BaseController {
    /**
     * 文化馆服务类
     */
    @Autowired
    private WhgSystemCultService service;

    /**
     * 进入type(list|add|edit|view)视图
     * @param request 请求对象
     * @param type 视图类型(list|add|edit|view)
     * @return
     */
    @RequestMapping("/view/{type}")
    @WhgOPT(optType = EnumOptType.CULT, optDesc = {"访问分馆列表页", "访问分馆添加页", "访问分馆编辑页"}, valid = {"type=list", "type=add", "type=edit"})
    public ModelAndView listview(HttpServletRequest request, @PathVariable("type")String type){
        ModelAndView view = new ModelAndView("admin/system/cult/view_"+type);

        try {
            if("edit".equals(type) || "view".equals(type)){
                String id = request.getParameter("id");
                view.addObject("cult", service.t_srchOne(id));
            }
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
    public ResponseBean srchList4p(HttpServletRequest request, WhgSysCult cult){
        ResponseBean res = new ResponseBean();
        try {
            int page = RequestUtils.getPageParameter(request);
            int rows = RequestUtils.getRowsParameter(request);
            String sort = RequestUtils.getParameter(request, "sort");
            String order = RequestUtils.getParameter(request, "order");
            PageInfo<WhgSysCult> pageInfo = service.t_srchList4p(page, rows, sort, order, cult);
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
     * @param cult 条件对象
     * @return 对象列表
     */
    @RequestMapping(value = "/srchList")
    @ApiOperation("查询文化馆列表")
    public List<WhgSysCult> srchList(HttpServletRequest request, WhgSysCult cult){
        List<WhgSysCult> resList = new ArrayList<WhgSysCult>();
        try {
            String sort = RequestUtils.getParameter(request, "sort");
            String order = RequestUtils.getParameter(request, "order");
            resList = service.t_srchList(sort, order, cult);
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
    @ApiOperation("查询详情")
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
     * 分馆列表
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/branchList")
    public ResponseBean branchList(HttpServletRequest request, WhgSysCult cult){
        ResponseBean responseBean = new ResponseBean();
        int page = RequestUtils.getPageParameter(request);

        int rows = RequestUtils.getRowsParameter(request);
        String sort = RequestUtils.getParameter(request, "sort");
        String order = RequestUtils.getParameter(request, "order");
        try {
            PageInfo<WhgSysCult> myPage = service.t_srchList4p(page, rows, sort, order, cult);
            if(null == myPage){
                responseBean.setSuccess(ResponseBean.FAIL);
                responseBean.setErrormsg("获取分馆列表失败");
            }else {
                responseBean.setRows((List)myPage.getList());
                responseBean.setPage(myPage.getPageNum());
                responseBean.setPageSize(myPage.getPageSize());
                responseBean.setTotal(myPage.getTotal());
            }
        }catch (Exception e){
            log.error(e.toString());
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("获取分馆列表失败");
        }finally {
            return responseBean;
        }
    }

    /**
     * 添加
     * @param request 请求对象
     * @param cult 添加的资料
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/add")
    @WhgOPT(optType = EnumOptType.CULT, optDesc = {"添加"})
    public ResponseBean add(HttpServletRequest request, WhgSysCult cult){
        ResponseBean res = new ResponseBean();
        try {
            if(!(cult.getArea()!=null&&!cult.getArea().equals(""))){
                cult.setArea(null);
            }
            service.t_add(cult, RequestUtils.getAdmin(request));
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
     * @param cult 编辑的资料
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/edit")
    @WhgOPT(optType = EnumOptType.CULT, optDesc = {"编辑"})
    public ResponseBean edit(HttpServletRequest request, WhgSysCult cult){
        ResponseBean res = new ResponseBean();
        try {
            service.t_edit(cult, RequestUtils.getAdmin(request));
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
    @WhgOPT(optType = EnumOptType.CULT, optDesc = {"删除"})
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
    @WhgOPT(optType = EnumOptType.CULT, optDesc = {"启用", "停用"}, valid = {"toState=1", "toState=0"})
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


    /**
     * 修改状态
     * @param request 请求对象
     * @param ids 用逗号分隔的多个ID
     * @param fromState 修改之前的状态
     * @param toState 修改后的状态
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/updsqstate")
    @WhgOPT(optType = EnumOptType.CULT, optDesc = {"审核通过", "审核不通过"}, valid = {"toState=2", "toState=0"})
    public ResponseBean updsqstate(HttpServletRequest request, String ids, String fromState, String toState){
        ResponseBean res = new ResponseBean();
        try {
            service.t_updsqstate(ids, fromState, toState, RequestUtils.getAdmin(request));
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 排序
     * @param request 请求
     * @param id 排序的ID
     * @param type up|top|idx
     * @param val type=idx时表示直接设置排序值
     * @return
     */
    @RequestMapping(value = "/sort")
    @WhgOPT(optType = EnumOptType.CULT, optDesc = {"上移","置顶"}, valid = {"type=up", "type=top"})
    public ResponseBean sort(HttpServletRequest request, String id, String type, String val){
        ResponseBean res = new ResponseBean();
        try {
            service.t_sort(id, type, val, RequestUtils.getAdmin(request));
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
