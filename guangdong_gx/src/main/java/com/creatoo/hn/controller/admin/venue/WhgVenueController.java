package com.creatoo.hn.controller.admin.venue;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgVen;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.services.admin.venue.WhgVenueService;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumOptType;
import com.creatoo.hn.util.enums.EnumStateDel;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by rbg on 2017/7/25.
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping("/admin/venue")
public class WhgVenueController extends BaseController{

    @Autowired
    private WhgVenueService whgVenueService;

    @RequestMapping("/view/{type}")
    @WhgOPT(optType = EnumOptType.VEN,
            optDesc = {"访问场馆编辑列表页", "访问场馆审核列表页","访问场馆发布列表页", "访问场馆回收站页", "访问场馆添加页", "访问场馆编辑页", "查看场馆信息"},
            valid = {"type=listedit", "type=listcheck", "type=listpublish", "type=listdel", "type=add", "type=edit", "type=show"})
    public String view(@PathVariable String type, ModelMap mmp, WebRequest request, HttpSession session){
        String view = "admin/venue/ven/";
        switch (type){
            case "show" :
            case "edit" : {
                String id = request.getParameter("id");
                if (id!=null && !id.isEmpty()){
                    mmp.addAttribute("id", id);
                    try {
                        Object whgVen = this.whgVenueService.srchOne(id);
                        mmp.addAttribute("info", whgVen);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
            case "add" :
                view += "view_add";
                break;

            case "syslistpublish":
                view += "sys_view_list";
                break;

            case "listdel":
            case "listedit":
            case "listcheck":
            case "listpublish":
                view += "view_list";
                break;
        }

        mmp.addAttribute("pageType", type);

        return view;
    }


    @RequestMapping("/srchList4p")
    @ResponseBody
    public Object srchList4p(int page, int rows, WhgVen ven, WebRequest request, HttpSession session){
        ResponseBean resb = new ResponseBean();
        try {
            String pageType = request.getParameter("pageType");
            String sort = request.getParameter("sort");
            String order = request.getParameter("order");

            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            List states = null;
            //编辑列表
            if ("listedit".equalsIgnoreCase(pageType)){
                //states = Arrays.asList(1);
                ven.setCrtuser(sysUser.getId());
            }
            //审核列表，查 9待审核
            if ("listcheck".equalsIgnoreCase(pageType)){
                //states = Arrays.asList(9);
                states = Arrays.asList(9,2,6,4);
            }
            //发布列表，查 2待发布 6已发布 4已下架
            if ("listpublish".equalsIgnoreCase(pageType)){
                //states = Arrays.asList(2,6,4);
                states = Arrays.asList(9,2,6,4);
            }

            //删除列表，查已删除 否则查未删除的
            if ("listdel".equalsIgnoreCase(pageType)){
                ven.setDelstate(1);
            }else{
                ven.setDelstate(0);
            }

            PageInfo pageInfo = this.whgVenueService.srchList4p(page, rows, ven, states, sort, order, sysUser.getId());
            resb.setRows( (List)pageInfo.getList() );
            resb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.debug("场馆查询失败", e);
            resb.setRows(new ArrayList());
            resb.setSuccess(ResponseBean.FAIL);
        }

        return resb;
    }

    @RequestMapping("/sysSrchList4p")
    @ResponseBody
    public Object sysSrchList4p(int page, int rows, WhgVen ven, WebRequest request) {
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
            PageInfo pageInfo = this.whgVenueService.sysSrchList4p(page, rows, ven, sort, order, record);
            resb.setRows( (List)pageInfo.getList() );
            resb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.debug("场馆查询失败", e);
            resb.setRows(new ArrayList());
            resb.setSuccess(ResponseBean.FAIL);
        }

        return resb;
    }

    /**
     * 供于其他模块关联场馆查询列表  根据实际业务 取全站所有的
     *
     * @return
     */
    @RequestMapping("/srchList")
    @ResponseBody
    public Object srchList(WhgVen ven, HttpServletRequest request,
                           @RequestParam(required = false)String states,
                           @RequestParam(required = false, defaultValue = "true")boolean more,
                           @RequestParam(required = false)String sort,
                           @RequestParam(required = false)String order ){
        try {
            //处理状态
            if (ven.getState() == null){
                ven.setState(EnumBizState.STATE_PUB.getValue());
            }
            if (ven.getDelstate() == null){
                ven.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
            }

            //处理数据查询查标
            if (more) {
                return this.whgVenueService.srchListMore(ven);
            }else {
                List stateslist = new ArrayList();
                if (states!=null && !states.isEmpty()){
                    stateslist = Arrays.asList(states.split("\\s*,\\s*"));
                  ven.setState(null);
                }

                WhgSysUser sysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
                return this.whgVenueService.srchList(ven, stateslist, sort, order,sysUser.getId());
            }
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return new ArrayList();
        }
    }


    /**
     * 处理添加场馆
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.VEN, optDesc = {"添加场馆"})
    public Object add(WhgVen ven, HttpSession session,
                      @RequestParam(value = "datebuild_str", required = false)
                      @DateTimeFormat(pattern="yyyy-MM-dd") Date datebuild_str){
        ResponseBean rb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            if (datebuild_str!=null){
                ven.setDatebuild(datebuild_str);
            }
            this.whgVenueService.t_add(ven, sysUser);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("场馆保存失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    /**
     * 获取场馆经纬度，供活动所需
     * @return
     */
    @RequestMapping(value= "/changeVen")
    @ResponseBody
    public Object changeVen(HttpServletRequest request, String venueId){
        Map<String,Object> res = new HashMap<String, Object>();
        try {
            WhgVen whgVen = whgVenueService.srchOne(venueId);
            res.put("address", whgVen.getAddress());
            res.put("longitude", whgVen.getLongitude());//坐标经度
            res.put("latitude", whgVen.getLatitude());//坐标纬度
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 处理编辑场馆
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.VEN, optDesc = {"编辑场馆"})
    public Object edit(WhgVen ven, HttpSession session,
                       @RequestParam(value = "datebuild_str", required = false)
                       @DateTimeFormat(pattern="yyyy-MM-dd") Date datebuild_str){
        ResponseBean rb = new ResponseBean();
        if (ven.getId() == null){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("场馆主键信息丢失");
            return rb;
        }
        try {
            if (datebuild_str!=null){
                ven.setDatebuild(datebuild_str);
            }
            if (ven.getArttype()==null) ven.setArttype("");
            if (ven.getEtag()==null) ven.setEtag("");
            if (ven.getEkey()==null) ven.setEkey("");
            this.whgVenueService.t_edit(ven);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("场馆信息保存失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    @RequestMapping("/recommend")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.VEN, optDesc = {"取消推荐","推荐"}, valid = {"recommend=0","recommend=1"})
    public Object recommend(WhgVen ven, HttpSession session){
        ResponseBean rb = new ResponseBean();
        if (ven.getId() == null){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("场馆主键信息丢失");
            return rb;
        }
        try {
            this.whgVenueService.t_edit(ven);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("场馆信息保存失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    /**
     * 删除场馆
     * @param id
     * @param session
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.VEN, optDesc = {"删除场馆"})
    public Object del(String id, HttpSession session){
        ResponseBean rb = new ResponseBean();
        try {
            if (this.whgVenueService.isUsrVenue(id)){
                rb.setSuccess(ResponseBean.FAIL);
                rb.setErrormsg("场馆有活动室信息存在，请先删除场馆下的活动室");
                return rb;
            }
            this.whgVenueService.t_del(id);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("场馆信息删除失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    /**
     * 回收场馆
     * @param id
     * @param session
     * @return
     */
    @RequestMapping("/recovery")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.VEN, optDesc = {"回收场馆"})
    public Object recovery(String id, HttpSession session){
        ResponseBean rb = new ResponseBean();
        try {
            WhgVen info = new WhgVen();
            info.setId(id);
            info.setDelstate(EnumStateDel.STATE_DEL_YES.getValue());
            this.whgVenueService.t_edit(info);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("场馆信息回收失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    /**
     * 还原删除
     * @param id
     * @param session
     * @return
     */
    @RequestMapping("/undel")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.VEN, optDesc = {"还原场馆"})
    public Object undel(String id, HttpSession session){
        ResponseBean rb = new ResponseBean();
        try {
            //WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            //this.whgVenueService.t_undel(id, sysUser);

            WhgVen info = new WhgVen();
            info.setId(id);
            info.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
            info.setState(EnumBizState.STATE_CAN_EDIT.getValue());
            this.whgVenueService.t_edit(info);

        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("场馆信息还原失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    /**
     * 修改场馆状态
     * @param ids
     * @param formstates
     * @param tostate
     * @param session
     * @return
     */
    @RequestMapping("/updstate")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.VEN, optDesc = {"送审","审核","打回","发布","取消发布"}, valid = {"tostate=9","tostate=2","tostate=1","tostate=6","tostate=4"})
    public Object updstate(String ids, String formstates, int tostate, HttpSession session,
                           @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date optTime,
                           @RequestParam(value = "reason", required = false)String reason,
                           @RequestParam(value = "issms", required = false, defaultValue = "0")int issms){
        ResponseBean rb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            rb = this.whgVenueService.t_updstate(ids, formstates, tostate, sysUser, optTime, reason, issms);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("场馆状态更改失败");
            log.error(rb.getErrormsg()+" formstate: "+formstates+" tostate:"+tostate+" ids: "+ids, e);
        }
        return rb;
    }

    @RequestMapping("setToprovince")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.VEN, optDesc = {"省级推荐","取消省级推荐"},valid = {"toprovince=1","toprovince=0"})
    public Object setToprovince(String id, int toprovince){
        ResponseBean rb = new ResponseBean();

        try {
            WhgVen info = new WhgVen();
            info.setId(id);
            info.setToprovince(toprovince);

            this.whgVenueService.t_edit(info);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("设置省级推荐失败");
        }

        return rb;
    }

    @RequestMapping("setTocity")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.VEN, optDesc = {"市级推荐","取消市级推荐"},valid = {"tocity=1","tocity=0"})
    public Object setTocity(String id, int tocity){
        ResponseBean rb = new ResponseBean();

        try {
            WhgVen info = new WhgVen();
            info.setId(id);
            info.setTocity(tocity);

            this.whgVenueService.t_edit(info);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("设置市级推荐失败");
        }

        return rb;
    }



    /**
     * 以会话中权限分馆查询可使用的场馆，以供场馆选项数据加载
     * @param session
     * @return
     */
    @RequestMapping("/ajaxVenList4session")
    @ResponseBody
    public Object ajaxVenList4Session(HttpSession session){
        JSONArray cults = (JSONArray) session.getAttribute(Constant.SESSION_ADMIN_CULT);

        List cultids = new ArrayList();
        for(Object obj : cults){
            JSONObject json = (JSONObject) obj;
            cultids.add(json.get("id"));
        }
        return this.whgVenueService.selectVenList4Cultids(cultids);
    }
}
