package com.creatoo.hn.controller.admin.supply;

import com.alibaba.fastjson.JSON;
import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSupplyTra;
import com.creatoo.hn.dao.model.WhgSupplyTraperson;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgYwiHardware;
import com.creatoo.hn.services.admin.supply.WhgSupplyTraPerService;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiYjpzservice;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/17.
 */
@RestController
@RequestMapping("/admin/supply/traper")
public class WhgSupplyTraPerController extends BaseController{
    /**
     * service
     */
    @Autowired
    private WhgSupplyTraPerService whgSupplyTraPerService;


    /**
     * 进入培训人员管理视图
     * type: 页面类型的参数（列表、添加、编辑）
     * classify : 培训人员的类型（）
     * @return
     */
    @RequestMapping("/view/{type}")
    public ModelAndView view(HttpServletRequest request,@PathVariable("type") String type){
        ModelAndView view = new ModelAndView();
        String entid = request.getParameter("entid");
        String id = request.getParameter("id");
        String targetShow = request.getParameter("targetShow");
        view.addObject("entid",entid);
        view.addObject("id",id);
        view.addObject("targetShow",targetShow);
        if("edit".equals(type)){
            if(id != null && !"".equals(id)){
                try {
                    WhgSupplyTraperson per = whgSupplyTraPerService.t_srchOne(id);
                    view.addObject("per",per);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
            view.setViewName("admin/supply/person/view_edit");
        }else {
            view.setViewName("admin/supply/person/view_list");
        }


        return view;
    }


    /**
     *  分页加载培训人员管理列表数据
     * @param request
     * @return
     */
    @RequestMapping("/srchList4p")
    public ResponseBean srchList4p(HttpServletRequest request, WhgSupplyTraperson per){
        ResponseBean res = new ResponseBean();
        int page = Integer.parseInt(request.getParameter("page"));
        int rows = Integer.parseInt(request.getParameter("rows"));
        try {
            PageInfo<WhgSupplyTraperson> pageInfo = this.whgSupplyTraPerService.t_srchList4p(page,rows,per);
            res.setRows(pageInfo.getList());
            res.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setRows(new ArrayList(0));
            res.setTotal(0);
            log.error(e.getMessage());
        }
        return res;
    }


    /**
     * 添加培训人员
     * @return
     */
    @RequestMapping("/add")
    @WhgOPT(optType = EnumOptType.KEY, optDesc = {"添加"})
    public ResponseBean add(HttpServletRequest request, WhgSupplyTraperson per){
        ResponseBean res = new ResponseBean();
        WhgSysUser sysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        if(per.getName() == null || "".equals(per.getName())){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("名称不能为空");
            return res;
        }
        try {
            this.whgSupplyTraPerService.t_add(per, sysUser);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }

        return res;
    }

    /**
     * 修改培训人员
     * @return
     */
    @RequestMapping("/edit")
    @WhgOPT(optType = EnumOptType.KEY, optDesc = {"编辑"})
    public ResponseBean edit(HttpServletRequest request,WhgSupplyTraperson per){
        ResponseBean res = new ResponseBean();
        WhgSysUser sysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        if(per.getId() == null || "".equals(per.getId())){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("主键丢失");
            return res;
        }
        try {
            this.whgSupplyTraPerService.t_edit(per,sysUser);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 删除培训人员
     * @return
     */
    @RequestMapping("/del")
    @WhgOPT(optType = EnumOptType.KEY, optDesc = {"删除"})
    public ResponseBean del(HttpServletRequest request){
        ResponseBean res = new ResponseBean();
        String id = request.getParameter("id");
        if(id == null || "".equals(id)){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("主键丢失");
            return res;
        }
        try {
            this.whgSupplyTraPerService.t_del(id);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }

}
