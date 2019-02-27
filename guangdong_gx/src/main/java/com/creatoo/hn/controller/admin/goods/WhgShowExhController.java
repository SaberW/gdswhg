package com.creatoo.hn.controller.admin.goods;

import com.alibaba.fastjson.JSON;
import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgShowExh;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgYwiHardware;
import com.creatoo.hn.services.admin.goods.WhgShowExhService;
import com.creatoo.hn.services.admin.supply.WhgSupplyService;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiYjpzservice;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.creatoo.hn.util.enums.EnumSupplyType;
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
 * Created by Administrator on 2017/9/1.
 */
@Controller
@RequestMapping("/admin/showExh")
public class WhgShowExhController extends BaseController{

    @Autowired
    private WhgShowExhService whgShowExhService;

    @Autowired
    private WhgSupplyService whgSupplyService;
    @Autowired
    private WhgYunweiYjpzservice whgYunweiYjpzservice;

    @RequestMapping("/view/{type}")
    @WhgOPT(optType = EnumOptType.EXH, optDesc = {"进入展览展示列表"})
    public ModelAndView view(@PathVariable("type") String type, ModelMap mmp, HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        String pageType = request.getParameter("pageType");
        mmp.addAttribute("type", type);
        if ("show".equalsIgnoreCase(type)) {
            pageType = "2";//查看
        }
        if ("add".equalsIgnoreCase(type) || "show".equalsIgnoreCase(type)) {
            mmp.addAttribute("type", "add");
            String id = request.getParameter("id");
            if(id != null && !"".equals(id)){

                try {
                    mmp.addAttribute("id", id);
                    WhgShowExh exh = this.whgShowExhService.srchOne(id);
                    //YI@ 把展览展示对应的硬件配置查询出来，然后放在map中：
                   // List<WhgYwiHardware> hardware_ist = whgYunweiYjpzservice.findHardwareByExhId(id);


                    Map _map = new HashMap();
                    _map.put("exh",exh);
                   // _map.put("hardware_ist",hardware_ist);
                    List timeList = whgSupplyService.selectTimes4Supply(id);
                    if(timeList!=null){
                        _map.put("times", JSON.toJSONString(timeList));
                    }
                    mmp.addAttribute("info", _map);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }

            }
            view.setViewName("admin/goods/exh/view_edit");
        } else if ("syslistpublish".equalsIgnoreCase(type)) {
            view.setViewName("admin/goods/exh/sys_view_list");
        }else{
            view.setViewName("admin/goods/exh/view_list");
        }
        mmp.addAttribute("pageType", pageType);
        mmp.addAttribute("supplytype", EnumSupplyType.TYPE_ZL.getValue());
        return view;
    }

    /**
     * 分页查询展演类商品列表
     * @param page
     * @param rows
     * @param exh
     * @param request
     * @return
     */
    @RequestMapping("/srchList4p")
    @ResponseBody
    public ResponseBean srchList4p(int page, int rows, WhgShowExh exh, WebRequest request,HttpSession session){
        ResponseBean rsb = new ResponseBean();
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");
        String type = request.getParameter("type");
        WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
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
            PageInfo pageInfo = this.whgShowExhService.t_srchlist4p(type, page, rows, sysUser.getId(), exh, sort, order, record);
            rsb.setRows( (List) pageInfo.getList() );
            rsb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rsb.setRows(new ArrayList());
            rsb.setSuccess(ResponseBean.FAIL);
        }
        return rsb;
    }

    /**
     * 添加展演类商品
     * @return
     */
  /*  @RequestMapping("/add")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.EXH, optDesc = {"添加展览展示"})
    public Object add(HttpSession session, WhgShowExh exh,HttpServletRequest request){
        ResponseBean resb = new ResponseBean();

        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            //String times = request.getParameter("times");
            resb = this.whgShowExhService.t_add(exh, sysUser*//*,times*//*);
        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("展演商品信息保存失败");
            log.error(resb.getErrormsg(), e);
        }
        return resb;
    }*/

    /**
     * 添加展演类商品
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.EXH, optDesc = {"添加展览展示"})
    public Object add(HttpSession session, WhgShowExh exh,HttpServletRequest request,String[] yjpzName,String[] yjpzDetail){
        ResponseBean resb = new ResponseBean();

        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            //String times = request.getParameter("times");
            resb = this.whgShowExhService.t_add(exh, sysUser/*,times*/,yjpzName,yjpzDetail);
        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("展演商品信息保存失败");
            log.error(resb.getErrormsg(), e);
        }
        return resb;
    }

    /**
     * 编辑
     *//*
    @RequestMapping("/edit")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.EXH, optDesc = {"编辑展览展示"})
    public Object edit(WhgShowExh exh,HttpServletRequest request){
        ResponseBean resb = new ResponseBean();
        if (exh == null || exh.getId()==null || exh.getId().isEmpty()){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("商品标识不能为空");
            return resb;
        }

        try {
            //String times = request.getParameter("times");
            resb = this.whgShowExhService.t_edit(exh*//*,times*//*);
        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("商品信息保存失败");
            log.error(resb.getErrormsg(), e);
        }

        return resb;
    }*/
    /**
     * 编辑
     */
    @RequestMapping("/edit")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.EXH, optDesc = {"编辑展览展示"})
    public Object edit(HttpSession session,WhgShowExh exh,HttpServletRequest request,String[] yjpzName,String[] yjpzDetail){
        ResponseBean resb = new ResponseBean();
        if (exh == null || exh.getId()==null || exh.getId().isEmpty()){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("商品标识不能为空");
            return resb;
        }

        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            //String times = request.getParameter("times");
            resb = this.whgShowExhService.t_edit(exh/*,times*/,sysUser,yjpzName,yjpzDetail);
        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("商品信息保存失败");
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
    @WhgOPT(optType = EnumOptType.EXH, optDesc = {"删除展览展示"})
    public Object del(String id){
        ResponseBean resb = new ResponseBean();
        if (id==null || id.isEmpty()){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("商品标识不能为空");
            return resb;
        }
        try {
            resb = this.whgShowExhService.t_del(id);
        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("商品信息删除失败");
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
    @WhgOPT(optType = EnumOptType.EXH, optDesc = {"还原展览展示"})
    public Object undel(String id, HttpSession session){
        ResponseBean rb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            this.whgShowExhService.t_undel(id, sysUser);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("培训信息还原失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }


    @RequestMapping("/updstate")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.EXH, optDesc = {"送审","审核","打回","发布","取消发布"}, valid = {"tostate=9","tostate=2","tostate=1","tostate=6","tostate=4"})
    public Object updstate(String ids, String formstates, int tostate, HttpSession session,
                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date optTime,
                           @RequestParam(value = "reason", required = false) String reason,
                           @RequestParam(value = "issms", required = false, defaultValue = "0") int issms) {
        ResponseBean resb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            resb = this.whgShowExhService.t_updstate(ids, formstates, tostate, sysUser, optTime, reason, issms);
        }catch (Exception e){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("商品状态更改失败");
            log.error(resb.getErrormsg(), e);
        }
        return resb;
    }
}
