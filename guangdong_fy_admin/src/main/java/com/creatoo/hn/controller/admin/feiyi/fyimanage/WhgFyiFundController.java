package com.creatoo.hn.controller.admin.feiyi.fyimanage;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgFyiFund;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.feiyi.fyimanage.WhgFyiFundService;
import com.creatoo.hn.util.RequestUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 非遗专项资金表
 * Created by Administrator on 2017/11/3.
 */
@Controller
@RequestMapping("/admin/fyi/fund")
public class WhgFyiFundController extends BaseController{

    @Autowired
    private WhgFyiFundService whgFyiFundService;


    /**
     * 访问专项资金页面
     * @param request
     * @param type
     * @return
     */
    @RequestMapping("/view/{type}")
    public ModelAndView view(HttpServletRequest request,@PathVariable("type") String type){
        ModelAndView view = new ModelAndView("admin/fyimanage/fund/view_"+type);
        String id = request.getParameter("id");
        if(id != null && !"".equals(id)){
            String show = request.getParameter("show");
            view.addObject("show",show);
            view.addObject("id",id);
            view.addObject("fund",whgFyiFundService.srchOne(id));
        }
        return view;
    }

    /**
     * 分页查询专项资金信息
     * @param request
     * @param fund
     * @return
     */
    @RequestMapping("/srchList4p")
    @ResponseBody
    public ResponseBean srchList4p(HttpServletRequest request, WhgFyiFund fund){
        ResponseBean rsb = new ResponseBean();
        try {
            int page = RequestUtils.getPageParameter(request);
            int rows = RequestUtils.getRowsParameter(request);
            PageInfo pageInfo = whgFyiFundService.t_srchlist4p(page,rows,fund);
            rsb.setRows(pageInfo.getList() );
            rsb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            rsb.setSuccess(ResponseBean.FAIL);
            rsb.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return rsb;
    }

    /**
     * 添加专项资金
     * @param session
     * @param fund
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseBean add(HttpSession session,WhgFyiFund fund){
        ResponseBean rsb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            whgFyiFundService.t_add(fund,sysUser);
        } catch (Exception e) {
            rsb.setSuccess(ResponseBean.FAIL);
            rsb.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return rsb;
    }

    /**
     * 编辑公文档案
     * @param session
     * @param fund
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public ResponseBean edit(HttpSession session, WhgFyiFund fund, HttpServletRequest request){
        ResponseBean rsb = new ResponseBean();
        if(fund.getId() == null || "".equals(fund.getId())){
            rsb.setSuccess(ResponseBean.FAIL);
            rsb.setErrormsg("主键丢失");
            return rsb;
        }
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            whgFyiFundService.t_edit(fund,sysUser);
        } catch (Exception e) {
            rsb.setSuccess(ResponseBean.FAIL);
            rsb.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return rsb;
    }

    /**
     * 删除公文档案
     * @param id
     * @return
     */
    @RequestMapping("del")
    @ResponseBody
    public ResponseBean del(String id){
        ResponseBean rsb = new ResponseBean();
        try {
            whgFyiFundService.t_del(id);
        } catch (Exception e) {
            rsb.setSuccess(ResponseBean.FAIL);
            rsb.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return rsb;
    }
}
