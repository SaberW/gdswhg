package com.creatoo.hn.controller.admin.yunwei;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgYwiGroup;
import com.creatoo.hn.dao.model.WhgYwiKey;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiGroupService;
import com.creatoo.hn.util.RequestUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 培训组
 * Created by wangxl on 2018/1/9.
 */
@RestController
@RequestMapping("/admin/yunwei/group")
public class WhgYunweiGroupController extends BaseController {
    /**
     * 培训组服务
     */
    @Autowired
    private WhgYunweiGroupService whgYunweiGroupService;

    @GetMapping("/view/{type}")
    public ModelAndView view(HttpServletRequest request, @PathVariable("type") String type){
        ModelAndView view = new ModelAndView("admin/yunwei/group/view_"+type);
        return view;
    }

    @RequestMapping("/srchList4p")
    public ResponseBean srchList4p(HttpServletRequest request, WhgYwiGroup whgYwiGroup){
        ResponseBean res = new ResponseBean();
        try {
            int page = RequestUtils.getPageParameter(request);
            int rows = RequestUtils.getRowsParameter(request);
            String sort = RequestUtils.getParameter(request, "sort");
            String order = RequestUtils.getParameter(request, "order");
            WhgSysUser sysUser = RequestUtils.getAdmin(request);
            PageInfo<WhgYwiGroup> pageInfo = this.whgYunweiGroupService.t_srchList4p(page,rows,sort, order,whgYwiGroup, sysUser.getId());
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
     * 列表查询
     * @return
     */
    @RequestMapping("/srchList")
    public List<WhgYwiGroup> srchList(HttpServletRequest request, WhgYwiGroup whgYwiGroup)throws Exception{
        WhgSysUser sysUser = RequestUtils.getAdmin(request);
        return this.whgYunweiGroupService.find(whgYwiGroup, sysUser.getId());
    }

    /**
     * 添加
     * @return
     */
    @RequestMapping("/add")
    public ResponseBean add(HttpServletRequest request, WhgYwiGroup whgYwiGroup){
        ResponseBean res = new ResponseBean();
        try {
            this.whgYunweiGroupService.t_add(whgYwiGroup, RequestUtils.getAdmin(request));
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }

        return res;
    }

    /**
     * 修改标签
     * @return
     */
    @RequestMapping("/edit")
    public ResponseBean edit(HttpServletRequest request,WhgYwiGroup whgYwiGroup){
        ResponseBean res = new ResponseBean();
        try {
            WhgSysUser sysUser = RequestUtils.getAdmin(request);
            this.whgYunweiGroupService.t_edit(whgYwiGroup,sysUser);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }

    @RequestMapping("on")
    public ResponseBean on(HttpServletRequest request, String id){
        ResponseBean res = new ResponseBean();
        try {
            WhgSysUser sysUser = RequestUtils.getAdmin(request);
            this.whgYunweiGroupService.t_on(id, sysUser.getId());
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }

    @RequestMapping("off")
    public ResponseBean off(HttpServletRequest request, String id){
        ResponseBean res = new ResponseBean();
        try {
            WhgSysUser sysUser = RequestUtils.getAdmin(request);
            this.whgYunweiGroupService.t_off(id, sysUser.getId());
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/del")
    public ResponseBean del(HttpServletRequest request, String id){
        ResponseBean res = new ResponseBean();
        try {
            this.whgYunweiGroupService.t_del(id);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }
}
