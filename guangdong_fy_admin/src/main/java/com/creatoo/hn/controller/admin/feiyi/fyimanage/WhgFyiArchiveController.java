package com.creatoo.hn.controller.admin.feiyi.fyimanage;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.mapper.WhgFyiArchiveMapper;
import com.creatoo.hn.dao.model.WhgFyiArchive;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.feiyi.fyimanage.WhgFyiArchiveService;
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
 * 非遗公文档案管理控制器
 * Created by Administrator on 2017/11/1.
 */
@Controller
@RequestMapping("/admin/fyi/archive")
public class WhgFyiArchiveController extends BaseController{

    @Autowired
    private WhgFyiArchiveService whgFyiArchiveService;

    /**
     * 访问公文档案页面
     * @param request
     * @param type
     * @return
     */
    @RequestMapping("/view/{type}")
    public ModelAndView view(HttpServletRequest request,@PathVariable("type") String type){
        ModelAndView view = new ModelAndView("admin/fyimanage/archive/view_"+type);
        String id = request.getParameter("id");
        String show = request.getParameter("show");
        if(id != null && !"".equals(id)){
            view.addObject("id",id);
            view.addObject("show",show);
            view.addObject("archive",whgFyiArchiveService.t_srchOne(id));
        }
        return view;
    }

    /**
     * 分页查询公文档案
     * @param request
     * @param archive
     * @return
     */
    @RequestMapping("/srchList4p")
    @ResponseBody
    public ResponseBean srchList4p(HttpServletRequest request, WhgFyiArchive archive){
        ResponseBean rsb = new ResponseBean();

        try {
            int page = RequestUtils.getPageParameter(request);
            int rows = RequestUtils.getRowsParameter(request);
            PageInfo pageInfo = whgFyiArchiveService.t_srchlist4p(page,rows,archive);
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
     * 添加公文档案
     * @param session
     * @param archive
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseBean add(HttpSession session,WhgFyiArchive archive,HttpServletRequest request){
        ResponseBean rsb = new ResponseBean();
        try {
            String enturl = request.getParameter("doc_enturl");
            if(enturl != null && !"".equals(enturl)){
                archive.setEnturl(enturl);
            }
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            whgFyiArchiveService.t_add(archive,sysUser);
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
     * @param archive
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public ResponseBean edit(HttpSession session,WhgFyiArchive archive,HttpServletRequest request){
        ResponseBean rsb = new ResponseBean();
        if(archive.getId() == null || "".equals(archive.getId())){
            rsb.setSuccess(ResponseBean.FAIL);
            rsb.setErrormsg("主键丢失");
            return rsb;
        }
        String enturl = request.getParameter("doc_enturl");
        if(enturl != null && !"".equals(enturl)){
            archive.setEnturl(enturl);
        }
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            whgFyiArchiveService.t_edit(archive,sysUser);
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
            whgFyiArchiveService.t_del(id);
        } catch (Exception e) {
            rsb.setSuccess(ResponseBean.FAIL);
            rsb.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return rsb;
    }
}
