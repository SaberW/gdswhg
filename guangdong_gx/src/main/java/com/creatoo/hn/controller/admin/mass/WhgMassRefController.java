package com.creatoo.hn.controller.admin.mass;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.mass.CrtWhgMassService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.util.bean.ResponseBean;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rbg on 2017/11/4.
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping("/admin/mass/ref")
public class WhgMassRefController extends BaseController{

    @Autowired
    private CrtWhgMassService crtWhgMassService;

    @Autowired
    private WhgSystemUserService whgSystemUserService;


    //TODO 资讯关联

    /**
     * 资讯关联界面
     * @param request
     * @param mmp
     * @return
     */
    @RequestMapping("/view/zxlist")
    public String view(WebRequest request, ModelMap mmp){
        String view = "admin/mass/zx/view_list";

        mmp.addAttribute("mid", request.getParameter("mid"));
        mmp.addAttribute("mtype", request.getParameter("mtype"));

        return view;
    }

    /**
     * 获取已关联的资讯列表
     * @param page
     * @param rows
     * @param request
     * @return
     */
    @RequestMapping("/getrefzxlist")
    @ResponseBody
    public Object getRefzxList(int page, int rows, WebRequest request){
        ResponseBean rb = new ResponseBean();

        Map recode = new HashMap();
        recode.put("mid", request.getParameter("mid"));
        recode.put("mtype", request.getParameter("mtype"));
        recode.put("clnvenueid", request.getParameter("cultid"));
        recode.put("deptid", request.getParameter("deptid"));
        recode.put("clnftltle", request.getParameter("clnftltle"));

        recode.put("sort", request.getParameter("sort"));
        recode.put("order", request.getParameter("order"));
        try {
            PageInfo pageInfo = this.crtWhgMassService.selectMassRefZxList(page, rows, recode);
            rb.setRows(pageInfo.getList());
            rb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("查询数据失败");
            log.error(e.getMessage(), e);
        }

        return rb;
    }

    /**
     * 获取群文系统的资讯列表
     * @param page
     * @param rows
     * @param request
     * @return
     */
    @RequestMapping("/getzxlist")
    @ResponseBody
    public Object getZxList(int page, int rows, WebRequest request, HttpSession session){
        ResponseBean rb = new ResponseBean();

        Map recode = new HashMap();
        recode.put("mid", request.getParameter("mid"));
        recode.put("mtype", request.getParameter("mtype"));
        //recode.put("clnvenueid", request.getParameter("cultid"));
        recode.put("clnftltle", request.getParameter("clnftltle"));

        recode.put("sort", request.getParameter("sort"));
        recode.put("order", request.getParameter("order"));
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            String cultid = request.getParameter("cultid");
            List<String> cultids = null;
            if (cultid==null || cultid.isEmpty()) {
                cultids = this.whgSystemUserService.getAllCultId4PMS(sysUser.getId());
            }else {
                cultids = Arrays.asList(cultid);
            }
            recode.put("clnvenueid", cultids);

            String deptid = request.getParameter("deptid");
            List<String> deptids = null;
            if (deptid==null || deptid.isEmpty()){
                deptids = this.whgSystemUserService.getAllDeptId4PMS(sysUser.getId());
            }else{
                deptids = Arrays.asList(deptid);
            }
            recode.put("deptid", deptids);

            PageInfo pageInfo = this.crtWhgMassService.selectZxList4Mass(page, rows, recode);
            rb.setRows(pageInfo.getList());
            rb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return rb;
    }

    /**
     * 添加群文资讯到发届次的关联
     * @param clnfids
     * @param mid
     * @param mtype
     * @return
     */
    @RequestMapping("/addZx")
    @ResponseBody
    public Object addZx(String clnfids, String mid, String mtype){
        ResponseBean rb = new ResponseBean();

        try {
            rb = this.crtWhgMassService.addMassRefZxs(clnfids, mid, mtype);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("保存数据失败");
            log.error(e.getMessage(), e);
        }
        return rb;
    }

    /**
     * 移除群文资讯到届次的关联
     * @param clnfids
     * @param mid
     * @param mtype
     * @return
     */
    @RequestMapping("/removeZx")
    @ResponseBody
    public Object removeZx(String clnfids, String mid, String mtype){
        ResponseBean rb = new ResponseBean();

        try {
            rb = this.crtWhgMassService.removeMassRefZxs(clnfids, mid, mtype);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("保存数据失败");
            log.error(e.getMessage(), e);
        }
        return rb;
    }


    //TODO 艺术人才关联

    /**
     * 人才关联界面
     * @param request
     * @param mmp
     * @return
     */
    @RequestMapping("/view/rclist")
    public String viewrc(WebRequest request, ModelMap mmp){
        String view = "admin/mass/batch/rc/view_list";

        mmp.addAttribute("mid", request.getParameter("mid"));
        mmp.addAttribute("mtype", request.getParameter("mtype"));

        return view;
    }

    /**
     * 关联的艺术人才
     * @param page
     * @param rows
     * @param request
     * @return
     */
    @RequestMapping("/getrefrclist")
    @ResponseBody
    public Object getRefrcList(int page, int rows, WebRequest request){
        ResponseBean rb = new ResponseBean();

        Map recode = new HashMap();
        recode.put("mid", request.getParameter("mid"));
        recode.put("cultid", request.getParameter("cultid"));
        recode.put("deptid", request.getParameter("deptid"));
        recode.put("name", request.getParameter("name"));
        recode.put("feattype", request.getParameter("feattype"));

        recode.put("sort", request.getParameter("sort"));
        recode.put("order", request.getParameter("order"));
        try {
            PageInfo pageInfo = this.crtWhgMassService.selectMassRefRcList(page, rows, recode);
            rb.setRows(pageInfo.getList());
            rb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("查询数据失败");
            log.error(e.getMessage(), e);
        }

        return rb;
    }

    /**
     * 可选的艺术人才
     * @param page
     * @param rows
     * @param request
     * @return
     */
    @RequestMapping("/getrclist")
    @ResponseBody
    public Object getRcList(int page, int rows, WebRequest request, HttpSession session){
        ResponseBean rb = new ResponseBean();

        Map recode = new HashMap();
        recode.put("mid", request.getParameter("mid"));
        //recode.put("cultid", request.getParameter("cultid"));
        recode.put("name", request.getParameter("name"));
        recode.put("feattype", request.getParameter("feattype"));

        recode.put("sort", request.getParameter("sort"));
        recode.put("order", request.getParameter("order"));
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            String cultid = request.getParameter("cultid");
            List<String> cultids = null;
            if (cultid==null || cultid.isEmpty()) {
                cultids = this.whgSystemUserService.getAllCultId4PMS(sysUser.getId());
            }else {
                cultids = Arrays.asList(cultid);
            }
            recode.put("cultid", cultids);

            String deptid = request.getParameter("deptid");
            List<String> deptids = null;
            if (deptid==null || deptid.isEmpty()){
                deptids = this.whgSystemUserService.getAllDeptId4PMS(sysUser.getId());
            }else{
                deptids = Arrays.asList(deptid);
            }
            recode.put("deptid", deptids);

            PageInfo pageInfo = this.crtWhgMassService.selectRcList4Mass(page, rows, recode);
            rb.setRows(pageInfo.getList());
            rb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return rb;
    }

    @RequestMapping("/addRefRc")
    @ResponseBody
    public Object addRefRc(String ids, String mid, String mtype){
        ResponseBean rb = new ResponseBean();

        try {
            rb = this.crtWhgMassService.addMassRefRcs(ids, mid, mtype);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("保存数据失败");
            log.error(e.getMessage(), e);
        }
        return rb;
    }

    @RequestMapping("/removeRefRc")
    @ResponseBody
    public Object removeRefRc(String ids, String mid, String mtype){
        ResponseBean rb = new ResponseBean();

        try {
            rb = this.crtWhgMassService.removeMassRefRcs(ids, mid, mtype);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("保存数据失败");
            log.error(e.getMessage(), e);
        }
        return rb;
    }


    //TODO 艺术团队关联

    /**
     * 团队关联界面
     * @param request
     * @param mmp
     * @return
     */
    @RequestMapping("/view/tdlist")
    public String viewtd(WebRequest request, ModelMap mmp){
        String view = "admin/mass/batch/td/view_list";

        mmp.addAttribute("mid", request.getParameter("mid"));
        mmp.addAttribute("mtype", request.getParameter("mtype"));

        return view;
    }

    /**
     * 关联的艺术团队
     * @param page
     * @param rows
     * @param request
     * @return
     */
    @RequestMapping("/getreftdlist")
    @ResponseBody
    public Object getReftdList(int page, int rows, WebRequest request){
        ResponseBean rb = new ResponseBean();

        Map recode = new HashMap();
        recode.put("mid", request.getParameter("mid"));
        recode.put("cultid", request.getParameter("cultid"));
        recode.put("deptid", request.getParameter("deptid"));
        recode.put("name", request.getParameter("name"));
        recode.put("feattype", request.getParameter("feattype"));

        recode.put("sort", request.getParameter("sort"));
        recode.put("order", request.getParameter("order"));
        try {
            PageInfo pageInfo = this.crtWhgMassService.selectMassRefTdList(page, rows, recode);
            rb.setRows(pageInfo.getList());
            rb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("查询数据失败");
            log.error(e.getMessage(), e);
        }

        return rb;
    }

    /**
     * 可选的艺术团队
     * @param page
     * @param rows
     * @param request
     * @return
     */
    @RequestMapping("/gettdlist")
    @ResponseBody
    public Object getTdList(int page, int rows, WebRequest request, HttpSession session){
        ResponseBean rb = new ResponseBean();

        Map recode = new HashMap();
        recode.put("mid", request.getParameter("mid"));
        //recode.put("cultid", request.getParameter("cultid"));
        recode.put("name", request.getParameter("name"));
        recode.put("feattype", request.getParameter("feattype"));

        recode.put("sort", request.getParameter("sort"));
        recode.put("order", request.getParameter("order"));
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            String cultid = request.getParameter("cultid");
            List<String> cultids = null;
            if (cultid==null || cultid.isEmpty()) {
                cultids = this.whgSystemUserService.getAllCultId4PMS(sysUser.getId());
            }else {
                cultids = Arrays.asList(cultid);
            }
            recode.put("cultid", cultids);

            String deptid = request.getParameter("deptid");
            List<String> deptids = null;
            if (deptid==null || deptid.isEmpty()){
                deptids = this.whgSystemUserService.getAllDeptId4PMS(sysUser.getId());
            }else{
                deptids = Arrays.asList(deptid);
            }
            recode.put("deptid", deptids);

            PageInfo pageInfo = this.crtWhgMassService.selectTdList4Mass(page, rows, recode);
            rb.setRows(pageInfo.getList());
            rb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return rb;
    }

    @RequestMapping("/addRefTd")
    @ResponseBody
    public Object addRefTd(String ids, String mid, String mtype){
        ResponseBean rb = new ResponseBean();

        try {
            rb = this.crtWhgMassService.addMassRefTds(ids, mid, mtype);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("保存数据失败");
            log.error(e.getMessage(), e);
        }
        return rb;
    }

    @RequestMapping("/removeRefTd")
    @ResponseBody
    public Object removeRefTds(String ids, String mid, String mtype){
        ResponseBean rb = new ResponseBean();

        try {
            rb = this.crtWhgMassService.removeMassRefTds(ids, mid, mtype);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("保存数据失败");
            log.error(e.getMessage(), e);
        }
        return rb;
    }

}
