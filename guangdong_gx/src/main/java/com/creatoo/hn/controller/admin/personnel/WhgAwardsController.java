package com.creatoo.hn.controller.admin.personnel;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgPersonnelAwards;
import com.creatoo.hn.dao.model.WhgShowPlaybill;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.goods.WhgShowJmdService;
import com.creatoo.hn.services.admin.personnel.WhgAwardsService;
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

/**
 * Created by Administrator on 2017/11/28.
 */
@RestController
@RequestMapping("/admin/per/awards")
public class WhgAwardsController extends BaseController{

    /**
     * service
     */
    @Autowired
    private WhgAwardsService whgAwardsService;


    /**
     * 进入获奖情况管理视图
     * type: 页面类型的参数（列表、添加、编辑）
     * @return
     */
    @RequestMapping("/view/{type}")
    public ModelAndView view(HttpServletRequest request, @PathVariable("type") String type){
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
                    WhgPersonnelAwards awards = whgAwardsService.t_srchOne(id);
                    view.addObject("awards",awards);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
            view.setViewName("admin/personnel/awards/view_edit");
        }else {
            view.setViewName("admin/personnel/awards/view_list");
        }
        return view;
    }


    /**
     *  分页加载获奖情况管理列表数据
     * @param request
     * @return
     */
    @RequestMapping("/srchList4p")
    public ResponseBean srchList4p(HttpServletRequest request, WhgPersonnelAwards awards){
        ResponseBean res = new ResponseBean();
        int page = Integer.parseInt(request.getParameter("page"));
        int rows = Integer.parseInt(request.getParameter("rows"));
        try {
            PageInfo<WhgPersonnelAwards> pageInfo = this.whgAwardsService.t_srchList4p(page,rows,awards);
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
     * 添加获奖情况
     * @return
     */
    @RequestMapping("/add")
    public ResponseBean add(HttpServletRequest request, WhgPersonnelAwards awards){
        ResponseBean res = new ResponseBean();
        WhgSysUser sysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);

        try {
            this.whgAwardsService.t_add(awards, sysUser);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }

        return res;
    }

    /**
     * 修改获奖情况
     * @return
     */
    @RequestMapping("/edit")
    public ResponseBean edit(HttpServletRequest request,WhgPersonnelAwards awards){
        ResponseBean res = new ResponseBean();
        WhgSysUser sysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        if(awards.getId() == null || "".equals(awards.getId())){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("主键丢失");
            return res;
        }
        try {
            this.whgAwardsService.t_edit(awards,sysUser);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 删除获奖情况
     * @return
     */
    @RequestMapping("/del")
    public ResponseBean del(HttpServletRequest request){
        ResponseBean res = new ResponseBean();
        String id = request.getParameter("id");
        if(id == null || "".equals(id)){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("主键丢失");
            return res;
        }
        try {
            this.whgAwardsService.t_del(id);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }
}
