package com.creatoo.hn.controller.admin.yunwei;


import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgYwiQuota;
import com.creatoo.hn.dao.vo.WhgYwiQuotaVO;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiQuotaService;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 群文配额管理action
 * @author liunima
 * Created by liunima on 2019/2/19.
 */
@RestController
@RequestMapping("/admin/yunwei/quota")
public class WhgYunweiQuotaController extends BaseController{
    /**
     * log
     */
    Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private WhgYunweiQuotaService quotaService;

    /**
     * service
     */


    /**
     * 进入type(list|add|edit|view)视图
     * @param type 视图类型(list|add|edit|view)
     * @return
     */
    @RequestMapping("/view/{type}")
    @WhgOPT(optType = EnumOptType.CULT, optDesc = {"群文配额管理"}, valid = {"type=list"})
    public ModelAndView view(@PathVariable("type")String type){
        ModelAndView view = new ModelAndView("admin/yunwei/quota/view_"+type);
        if(type.equals("set")){
            WhgYwiQuota quota = quotaService.findDefaultSet();
            view.addObject("quota",quota);
        }
        return view;
    }

    @RequestMapping("/noticetype")
    public ModelAndView noticetype(){
        return  new ModelAndView("admin/system/noticetype/noticetype");
    }

    /**
     * 修改配额
     * @return
     */
    @RequestMapping("/querynoticetype")
    @WhgOPT(optType = EnumOptType.KEY, optDesc = {"查询"})
    public ResponseBean querynoticetype(HttpServletRequest request, WhgYwiQuota quota){
        ResponseBean res = new ResponseBean();
        try {
            res.setData(this.quotaService.queryNoticetype(quota));
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }

    @RequestMapping("/set_default")
    @WhgOPT(optType = EnumOptType.KEY, optDesc = {"修改默认设定"})
    public ResponseBean set_default(HttpServletRequest request, WhgYwiQuota quota){
        ResponseBean res = new ResponseBean();
        WhgSysUser sysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        try {
            this.quotaService.set_default(quota,sysUser);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }
    /**
     * 修改配额
     * @return
     */
    @RequestMapping("/edit")
    @WhgOPT(optType = EnumOptType.KEY, optDesc = {"编辑"})
    public ResponseBean edit(HttpServletRequest request, WhgYwiQuota quota){
        ResponseBean res = new ResponseBean();
        WhgSysUser sysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        try {
            this.quotaService.t_edit(quota,sysUser);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }

    @RequestMapping("/batchEdit")
    @WhgOPT(optType = EnumOptType.KEY, optDesc = {"批量修改"})
    public ResponseBean batchEdit(HttpServletRequest request, WhgYwiQuota quota,String cultids){
        ResponseBean res = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
            res = this.quotaService.batchEdit(quota,sysUser,cultids);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 修改配额
     * @return
     */
    @RequestMapping("/edit_noticetype")
    @WhgOPT(optType = EnumOptType.KEY, optDesc = {"编辑"})
    public ResponseBean editnoticetype(HttpServletRequest request, WhgYwiQuota quota){
        ResponseBean res = new ResponseBean();
        WhgSysUser sysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        try {
            this.quotaService.editNoticetype(quota,sysUser);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }



    /**
     * 分页查询
     * @return
     */
    @RequestMapping(value = "/srchList4p")
    public ResponseBean srchList4p(
            @SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser,
            WhgYwiQuotaVO quota, Integer page, Integer rows){
        ResponseBean res = new ResponseBean();
        try {
            PageInfo<WhgYwiQuotaVO> pageInfo = quotaService.t_srchList4p(page, rows,quota,sysUser.getId());
            res.setRows(pageInfo.getList());
            res.setTotal(pageInfo.getTotal());
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }


}
