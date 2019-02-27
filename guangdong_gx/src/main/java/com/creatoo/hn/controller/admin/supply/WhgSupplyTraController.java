package com.creatoo.hn.controller.admin.supply;

import com.alibaba.fastjson.JSON;
import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgShowExh;
import com.creatoo.hn.dao.model.WhgSupplyTra;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.goods.WhgShowExhService;
import com.creatoo.hn.services.admin.supply.WhgSupplyService;
import com.creatoo.hn.services.admin.supply.WhgSupplyTraService;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.creatoo.hn.util.enums.EnumSupplyType;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 *供需培训
 * Created by Administrator on 2017/11/16.
 */
@RestController
@RequestMapping("/admin/supply/tra")
public class WhgSupplyTraController extends BaseController{

    @Autowired
    private WhgSupplyTraService whgSupplyTraService;

    @Autowired
    private WhgSupplyService whgSupplyService;

    @RequestMapping("/view/{type}")
    @WhgOPT(optType = EnumOptType.SUPTRA, optDesc = {"访问供需培训列表"})
    public ModelAndView view(@PathVariable("type") String type, ModelMap mmp, HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        String pageType = request.getParameter("pageType");
        mmp.addAttribute("type", type);
        if("add".equalsIgnoreCase(type)){
            String id = request.getParameter("id");
            if(id != null && !"".equals(id)){
                try {
                    mmp.addAttribute("id", id);
                    WhgSupplyTra tra = this.whgSupplyTraService.srchOne(id);
                    Map _map = new HashMap();
                    _map.put("tra",tra);
                    List timeList = whgSupplyService.selectTimes4Supply(id);
                    if(timeList!=null){
                        _map.put("times", JSON.toJSONString(timeList));
                    }
                    mmp.addAttribute("info", _map);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
            view.setViewName("admin/supply/supplytra/view_add");
        }else if ("syslistpublish".equalsIgnoreCase(type)){
            view.setViewName("admin/supply/supplytra/sys_view_list");
        }else{
            view.setViewName("admin/supply/supplytra/view_list");
        }
        mmp.addAttribute("pageType", pageType);
        mmp.addAttribute("supplytype", EnumSupplyType.TYPE_PXKC.getValue());
        return view;
    }

    /**
     * 分页查询
     * @param page
     * @param rows
     * @param tra
     * @param request
     * @return
     */
    @RequestMapping("/srchList4p")
    @ResponseBody
    public ResponseBean srchList4p(int page, int rows, WhgSupplyTra tra, WebRequest request,HttpSession session ){
        ResponseBean rsb = new ResponseBean();
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");
        String type = request.getParameter("type");
        WhgSysUser user = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
        try {
            PageInfo pageInfo = this.whgSupplyTraService.t_srchlist4p(type,page,rows,tra,sort,order,user.getId());
            rsb.setRows( (List) pageInfo.getList() );
            rsb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rsb.setRows(new ArrayList());
            rsb.setSuccess(ResponseBean.FAIL);
        }
        return rsb;
    }

    @RequestMapping("/sysSrchList4p")
    @ResponseBody
    public Object sysSrchList4p(int page, int rows, WhgSupplyTra room, WebRequest request) {
        ResponseBean resb = new ResponseBean();
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");

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
            PageInfo pageInfo = this.whgSupplyTraService.sysSrchList4p(page, rows, room, sort, order, record);
            resb.setRows( (List)pageInfo.getList() );
            resb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.debug("查询数据失败", e);
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
    @WhgOPT(optType = EnumOptType.SUPTRA, optDesc = {"添加供需培训"})
    public Object add(HttpSession session, WhgSupplyTra tra, HttpServletRequest request){
        ResponseBean resb = new ResponseBean();

        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            //String times = request.getParameter("times");
            resb = this.whgSupplyTraService.t_add(tra, sysUser/*,times*/);
        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("培训信息保存失败");
            log.error(resb.getErrormsg(), e);
        }
        return resb;
    }

    /**
     * 编辑
     */
    @RequestMapping("/edit")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.SUPTRA, optDesc = {"编辑供需培训"})
    public Object edit(WhgSupplyTra tra,HttpServletRequest request){
        ResponseBean resb = new ResponseBean();
        if (tra == null || tra.getId()==null || tra.getId().isEmpty()){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("培训标识不能为空");
            return resb;
        }

        try {
            //String times = request.getParameter("times");
            resb = this.whgSupplyTraService.t_edit(tra/*,times*/);
        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("培训信息保存失败");
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
    @WhgOPT(optType = EnumOptType.SUPTRA, optDesc = {"删除供需培训"})
    public Object del(String id){
        ResponseBean resb = new ResponseBean();
        if (id==null || id.isEmpty()){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("培训标识不能为空");
            return resb;
        }
        try {
            resb = this.whgSupplyTraService.t_del(id);
        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("培训信息删除失败");
            log.error(resb.getErrormsg(), e);
        }
        return resb;
    }

    /**
     * 还原删除
     * @param id
     * @param session
     * @return
     */
    @RequestMapping("/undel")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.SUPTRA, optDesc = {"还原供需培训"})
    public Object undel(String id, HttpSession session){
        ResponseBean rb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            this.whgSupplyTraService.t_undel(id, sysUser);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("培训信息还原失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    @RequestMapping("/updstate")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.SUPTRA, optDesc = {"送审","审核","打回","发布","取消发布"}, valid = {"tostate=9","tostate=2","tostate=1","tostate=6","tostate=4"})
    public Object updstate(String ids, String formstates, int tostate, HttpSession session,
                           @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date optTime,
                           @RequestParam(value = "reason", required = false)String reason,
                           @RequestParam(value = "issms", required = false, defaultValue = "0")int issms) {
        ResponseBean resb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            resb = this.whgSupplyTraService.t_updstate(ids, formstates, tostate, sysUser,optTime, reason, issms);
        }catch (Exception e){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("培训状态更改失败");
            log.error(resb.getErrormsg(), e);
        }
        return resb;
    }


}
