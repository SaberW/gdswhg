package com.creatoo.hn.controller.admin.venue;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgVenRoom;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.services.admin.venue.WhgVenroomService;
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

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 场馆管理-活动室控件器
 * Created by rbg on 2017/3/23.
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping("/admin/venue/room")
public class WhgVenroomController extends BaseController{

    @Autowired
    private WhgVenroomService whgVenroomService;

    @Autowired
    private WhgVenueService whgVenueService;

    @Autowired
    private WhgSystemUserService whgSystemUserService;


    @RequestMapping("/view/{type}")
    @WhgOPT(optType = EnumOptType.VEN,
            optDesc = {"访问活动室编辑列表页", "访问活动室审核列表页", "访问活动室发布列表页", "访问活动室回收站页", "访问活动室添加页", "访问活动室编辑页", "查看活动室信息"},
            valid = {"type=listedit", "type=listcheck", "type=listpublish", "type=listdel", "type=add", "type=edit", "type=show"})
    public String view(@PathVariable String type, ModelMap mmp, WebRequest request, HttpSession session){
        String view = "admin/venue/room/";
        switch (type){
            case "show" :
            case "edit" : {
                String id = request.getParameter("id");
                if (id!=null && !id.isEmpty()){
                    mmp.addAttribute("id", id);
                    try {
                        WhgVenRoom room = whgVenroomService.srchOne(id);
                        mmp.addAttribute("info", room);
                        if (room!=null && room.getVenid()!=null){
                            mmp.addAttribute("whgVen", this.whgVenueService.srchOne(room.getVenid()));
                        }
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

    /**
     * 分页查询活动室信息
     * @param page
     * @param rows
     * @param request
     * @return
     */
    @RequestMapping("/srchList4p")
    @ResponseBody
    public Object srchList4p(int page, int rows, WebRequest request, HttpSession session){
        ResponseBean resb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);

            Map<String, String[]> pmap = request.getParameterMap();
            Map record = new HashMap();
            for(Map.Entry<String, String[]> ent : pmap.entrySet()){
                if (ent.getValue().length == 1 && ent.getValue()[0]!=null && !ent.getValue()[0].isEmpty()){
                    record.put(ent.getKey(), ent.getValue()[0]);
                }
                if (ent.getValue().length > 1){
                    record.put(ent.getKey(), Arrays.asList( ent.getValue() ) );
                }
            }

            String pageType = request.getParameter("pageType");
            //编辑列表，查 1可编辑 5已撤消
            if ("listedit".equalsIgnoreCase(pageType)){
                //record.put("states", Arrays.asList(1) );
                record.put("crtuser", sysUser.getId());
            }
            //审核列表，查 9待审核
            if ("listcheck".equalsIgnoreCase(pageType)){
                //record.put("states", Arrays.asList(9) );
                record.put("states", Arrays.asList(9,2,6,4) );
            }
            //发布列表，查 2待发布 6已发布 4已下架
            if ("listpublish".equalsIgnoreCase(pageType)){
                //record.put("states", Arrays.asList(2,6,4) );
                record.put("states", Arrays.asList(9,2,6,4) );
            }

            //删除列表，查已删除 否则查未删除的
            if ("listdel".equalsIgnoreCase(pageType)){
                record.put("delstate", 1);
            }else{
                record.put("delstate", 0);
            }

            //record.put("sysUserId", sysUser.getId());
            if (record.get("cultid") == null || record.get("cultid").toString().isEmpty()) {
                try {
                    List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(sysUser.getId());
                    if (cultids!=null && cultids.size()>0){
                        record.put("cultids", cultids);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (record.get("deptid") == null || record.get("deptid").toString().isEmpty()) {
                List<String> deptids = this.whgSystemUserService.getAllDeptId4PMS(sysUser.getId());
                if (deptids != null && deptids.size() > 0) {
                    record.put("deptids", deptids);
                }
            }


            PageInfo pageInfo = this.whgVenroomService.srchList4p(page, rows, record);
            resb.setRows( (List)pageInfo.getList() );
            resb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.debug("活动室查询失败", e);
            resb.setRows(new ArrayList());
            resb.setSuccess(ResponseBean.FAIL);
        }

        return resb;
    }

    @RequestMapping("/sysSrchList4p")
    @ResponseBody
    public Object sysSrchList4p(int page, int rows, WhgVenRoom room, WebRequest request) {
        ResponseBean resb = new ResponseBean();
        /*String sort = request.getParameter("sort");
        String order = request.getParameter("order");

        String iscult = request.getParameter("iscult");
        String refid = request.getParameter("refid");
        String pcalevel = request.getParameter("pcalevel");
        String pcatext = request.getParameter("pcatext");

        Map record = new HashMap();
        record.put("iscult", iscult);
        record.put("refid", refid);
        record.put("pcalevel", pcalevel);
        record.put("pcatext", pcatext);
        record.put("sort", sort);
        record.put("order", order);*/

        Map<String, String[]> pmap = request.getParameterMap();
        Map record = new HashMap();
        for(Map.Entry<String, String[]> ent : pmap.entrySet()){
            if (ent.getValue().length == 1 && ent.getValue()[0]!=null && !ent.getValue()[0].isEmpty()){
                record.put(ent.getKey(), ent.getValue()[0]);
            }
            if (ent.getValue().length > 1){
                record.put(ent.getKey(), Arrays.asList( ent.getValue() ) );
            }
        }

        try {
            PageInfo pageInfo = this.whgVenroomService.sysSrchList4p(page, rows, record);
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
     * 查活动室列表
     * @return
     */
    @RequestMapping("/srchList")
    @ResponseBody
    public Object srchList(WhgVenRoom room, HttpSession session,
                           @RequestParam(required = false)String states,
                           @RequestParam(required = false)String sort,
                           @RequestParam(required = false)String order
    ){
        try {
            List stateslist = null;
            if (states!=null && !states.isEmpty()){
                stateslist = Arrays.asList(states.split("\\s*,\\s*"));
            }

            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);

            return this.whgVenroomService.srchList(room, stateslist, sort, order, sysUser.getId());
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return new ArrayList();
        }
    }

    @RequestMapping("/add")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.ROOM, optDesc = {"添加活动室"})
    public Object add(HttpSession session, WhgVenRoom room){
        ResponseBean rb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            rb = this.whgVenroomService.t_add(room, sysUser);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("活动室信息添加失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    @RequestMapping("/edit")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.ROOM, optDesc = {"编辑活动室"})
    public Object edit(HttpSession session, WhgVenRoom room){
        ResponseBean rb = new ResponseBean();
        try {
            if (room.getEkey()==null) room.setEkey("");
            if (room.getEtag()==null) room.setEtag("");
            if (room.getFacility()==null) room.setFacility("");
            if (room.getOpenweek()==null) room.setOpenweek("");
            if (room.getNoticetype()==null) room.setNoticetype("");

            this.whgVenroomService.t_edit(room);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("活动室信息保存失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    @RequestMapping("/recommend")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.ROOM, optDesc = {"取消推荐","推荐"}, valid = {"recommend=0","recommend=1"})
    public Object recommend(HttpSession session, WhgVenRoom room){
        ResponseBean rb = new ResponseBean();
        try {
            this.whgVenroomService.t_edit(room);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("活动室信息保存失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    @RequestMapping("/del")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.ROOM, optDesc = {"删除活动室"})
    public Object del(HttpSession session, String id){
        ResponseBean rb = new ResponseBean();
        try {
            this.whgVenroomService.t_del(id);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("活动室信息删除失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    /**
     * 回收活动室
     * @param id
     * @param session
     * @return
     */
    @RequestMapping("/recovery")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.ROOM, optDesc = {"回收活动室"})
    public Object recovery(String id, HttpSession session){
        ResponseBean rb = new ResponseBean();
        try {
            WhgVenRoom info = new WhgVenRoom();
            info.setId(id);
            info.setDelstate(EnumStateDel.STATE_DEL_YES.getValue());
            this.whgVenroomService.t_edit(info);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("活动室信息回收失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    /**
     * 还原删除
     * @param session
     * @param id
     * @return
     */
    @RequestMapping("/undel")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.ROOM, optDesc = {"还原活动室"})
    public Object undel(HttpSession session, String id){
        ResponseBean rb = new ResponseBean();
        try {
            /*WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            this.whgVenroomService.t_undel(id, sysUser);*/

            WhgVenRoom info = new WhgVenRoom();
            info.setId(id);
            info.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
            info.setState(EnumBizState.STATE_CAN_EDIT.getValue());
            this.whgVenroomService.t_edit(info);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("活动室信息还原失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    @RequestMapping("/updstate")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.ROOM, optDesc = {"送审","审核","打回","发布","取消发布"}, valid = {"tostate=9","tostate=2","tostate=1","tostate=6","tostate=4"})
    public Object updstate(HttpSession session, String ids, String formstates, int tostate,
                           @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date optTime,
                           @RequestParam(value = "reason", required = false)String reason,
                           @RequestParam(value = "issms", required = false, defaultValue = "0")int issms){
        ResponseBean rb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            rb = this.whgVenroomService.t_updstate(ids, formstates, tostate, sysUser, optTime, reason, issms);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("活动室状态更改失败");
            log.error(rb.getErrormsg()+" formstate: "+formstates+" tostate:"+tostate+" ids: "+ids, e);
        }
        return rb;
    }

    /**
     * 按场馆id 取可用活动室列表供选项值
     * @param venid
     * @return
     */
    @RequestMapping("/getRoomList4venid")
    @ResponseBody
    public Object getRoomList4venid(String venid){
        try {
            return this.whgVenroomService.selectRoomList4venid(venid);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ArrayList();
        }
    }
}
