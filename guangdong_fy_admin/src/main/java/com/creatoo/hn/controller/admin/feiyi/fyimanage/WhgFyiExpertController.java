package com.creatoo.hn.controller.admin.feiyi.fyimanage;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgFyiExpert;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.feiyi.fyimanage.WhgFyiExpertService;
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
 * 非遗专家库controller
 * Created by Administrator on 2017/10/31.
 */
@Controller
@RequestMapping("admin/fyi/expert")
public class WhgFyiExpertController extends BaseController{

    @Autowired
    private WhgFyiExpertService whgFyiExpertService;

    /**
     * 访问专家库视图
     * @param request
     * @param type
     * @return
     */
    @RequestMapping("/view/{type}")
    public ModelAndView listView(HttpServletRequest request,@PathVariable("type") String type){
        ModelAndView view = new ModelAndView("admin/fyimanage/expert/view_"+type);
        try {
            String id = request.getParameter("id");
            if(id != null && !"".equals(id)){
                String show = request.getParameter("show");
                view.addObject("show", show);
                view.addObject("id", id);
                view.addObject("expert", whgFyiExpertService.t_srchOne(id));
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return view;
    }

    /**
     * 分页查询列表
     * @param request
     * @param expert
     * @return
     */
    @RequestMapping("/srchList4p")
    @ResponseBody
    public ResponseBean srchList4p(HttpServletRequest request, WhgFyiExpert expert){
        ResponseBean rsb = new ResponseBean();
        try {
            int page = RequestUtils.getPageParameter(request);
            int rows = RequestUtils.getRowsParameter(request);
            PageInfo pageInfo = whgFyiExpertService.t_srchlist4p(page,rows,expert);
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
     * 添加专家
     * @param session
     * @param expert
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseBean add(HttpServletRequest session,WhgFyiExpert expert){
        ResponseBean rsb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            whgFyiExpertService.t_add(expert,sysUser);
        } catch (Exception e) {
            rsb.setSuccess(ResponseBean.FAIL);
            rsb.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return rsb;
    }

    /**
     * 编辑专家
     * @param expert
     * @param session
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public ResponseBean edit(WhgFyiExpert expert, HttpSession session){
        ResponseBean rsb = new ResponseBean();
        if(expert.getId() == null || "".equals(expert.getId())){
            rsb.setSuccess(ResponseBean.FAIL);
            rsb.setErrormsg("主键丢失");
        }
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            whgFyiExpertService.t_edit(expert,sysUser);
        } catch (Exception e) {
            rsb.setSuccess(ResponseBean.FAIL);
            rsb.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return rsb;
    }

    /**
     * 删除专家
     * @param id
     * @return
     */
    @RequestMapping("del")
    @ResponseBody
    public ResponseBean del(String id){
        ResponseBean rsb = new ResponseBean();
        try {
            whgFyiExpertService.t_del(id);
        } catch (Exception e) {
            rsb.setSuccess(ResponseBean.FAIL);
            rsb.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return rsb;
    }

}
