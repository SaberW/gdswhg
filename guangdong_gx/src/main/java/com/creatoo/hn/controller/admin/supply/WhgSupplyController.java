package com.creatoo.hn.controller.admin.supply;

import com.alibaba.fastjson.JSON;
import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgPersonnel;
import com.creatoo.hn.dao.model.WhgSupply;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.supply.WhgSupplyService;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by rbg on 2017/8/22.
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping("/admin/supply")
public class WhgSupplyController extends BaseController {

    @Autowired
    private WhgSupplyService whgSupplyService;

    @RequestMapping("/view/{type}")
    public ModelAndView view(@PathVariable(value = "type") String type, WebRequest request, ModelMap mmp) {
        ModelAndView view = new ModelAndView();
        try {
            view.addObject("type", type);
            if ("add".equalsIgnoreCase(type) || "show".equalsIgnoreCase(type) || "edit".equalsIgnoreCase(type)) {
                String id = request.getParameter("id");
                String targetShow = request.getParameter("targetShow");
                if (id != null) {
                    view.addObject("id", id);
                    view.addObject("info", this.whgSupplyService.srchOne(id));
                    view.setViewName("admin/supply/view_edit");
                } else {
                    view.setViewName("admin/supply/view_edit");
                }
            } else if ("syslistpublish".equalsIgnoreCase(type)) {
                view.setViewName("admin/supply/sys_view_list");
            } else {
                view.setViewName("admin/supply/view_list");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 分页列表查询
     * @param page
     * @param rows
     * @param goods
     * @param request
     * @return
     */
    @RequestMapping("/srchList4p")
    @ResponseBody
    public Object srchList4p(int page, int rows, WhgSupply supply, WebRequest request, HttpSession session){
        ResponseBean resb = new ResponseBean();

        String sort = request.getParameter("sort");
        String order = request.getParameter("order");
        String type = request.getParameter("type");

        String iscult = request.getParameter("iscult");
        String refid = request.getParameter("refid");
        String pcalevel = request.getParameter("pcalevel");
        String pcatext = request.getParameter("pcatext");

        Map<String, String> record = new HashMap();
        record.put("iscult", iscult);
        record.put("refid", refid);
        record.put("pcalevel", pcalevel);
        record.put("pcatext", pcatext);

        try {

            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            PageInfo pageInfo = (PageInfo) this.whgSupplyService.srchList4p(type, page, rows, supply, sort, order, sysUser.getId(), record);
            resb.setRows( (List)pageInfo.getList() );
            resb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resb.setRows(new ArrayList());
            resb.setSuccess(ResponseBean.FAIL);
        }

        return resb;
    }


    /**
     * 添加
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.SUPPLY, optDesc = "添加供需信息")
    public Object add(HttpSession session, WhgSupply info, String times){
        ResponseBean resb = new ResponseBean();

        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            resb = this.whgSupplyService.t_add(sysUser, info, times);
        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("添加供需信息失败");
            log.error(resb.getErrormsg(), e);
        }

        return resb;
    }

    /**
     * 编辑
     */
    @RequestMapping("/edit")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.SUPPLY, optDesc = "编辑供需信息")
    public Object edit(HttpSession session, WhgSupply info, String times) {
        ResponseBean resb = new ResponseBean();

        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            resb = this.whgSupplyService.t_edit(sysUser, info, times);
        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("编辑供需信息");
            log.error(resb.getErrormsg(), e);
        }

        return resb;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.SUPPLY, optDesc = {"删除供需信息"})
    public Object del(String id){
        ResponseBean resb = new ResponseBean();

        try {
            resb = this.whgSupplyService.t_del(id);
        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("商品信息删除失败");
            log.error(resb.getErrormsg(), e);
        }

        return resb;
    }

    /**
     * 还原删除
     *
     * @param id id
     * @return res
     */
    @RequestMapping("/undel")
    @ResponseBody
    public Object undel(String id) {
        ResponseBean rb = new ResponseBean();
        try {
            this.whgSupplyService.t_undel(id);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("还原失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }


    /**
     * 回收
     *
     * @param request 请求对象
     * @param ids     用逗号分隔的多个ID
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/recovery")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.ACT, optDesc = {"回收需求"})
    public ResponseBean recovery(HttpServletRequest request, String id, String delStatus) {
        ResponseBean res = new ResponseBean();
        try {
            this.whgSupplyService.t_updDelstate(id);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    @RequestMapping("/updstate")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.GOODS,
            optDesc = {"送审","审核","打回","发布","取消发布"},
            valid = {"tostate=9","tostate=2","tostate=1","tostate=6","tostate=4"})
    public Object updstate(String ids, String formstates, int tostate, HttpSession session,
                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date optTime,
                           @RequestParam(value = "reason", required = false) String reason,
                           @RequestParam(value = "issms", required = false, defaultValue = "0") int issms) {
        ResponseBean resb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            resb = this.whgSupplyService.t_updstate(ids, formstates, tostate, sysUser, optTime, reason, issms);
        }catch (Exception e){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("需求状态更改失败");
            log.error(resb.getErrormsg(), e);
        }
        return resb;
    }
}
