package com.creatoo.hn.controller.admin.supply;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSupplyRoom;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.supply.WhgSupplyRoomService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumOptType;
import com.creatoo.hn.util.enums.EnumStateDel;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by rbg on 2017/11/16.
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping("/admin/supply/room")
public class WhgSupplyRoomController extends BaseController {
    @Autowired
    private WhgSupplyRoomService whgSupplyRoomService;

    @WhgOPT(optType = EnumOptType.SUPPLY,
            optDesc = {"访问供需活动室编辑列表页", "访问供需活动室审核列表页","访问供需活动室发布列表页", "访问供需活动室回收站页", "访问供需活动室添加页", "访问供需活动室编辑页", "查看供需活动室信息"},
            valid = {"type=listedit", "type=listcheck", "type=listpublish", "type=listdel", "type=add", "type=edit", "type=show"})
    @RequestMapping("/view/{type}")
    public String view(@PathVariable("type")String type, ModelMap mmp, HttpServletRequest request){
        String view = "admin/supply/room/";

        switch (type){
            case "show":
            case "edit":
                try {
                    String id = request.getParameter("id");
                    if (id != null && !id.isEmpty()) {
                        mmp.addAttribute("id", id);
                        Object info = this.whgSupplyRoomService.srchOne(id);
                        mmp.addAttribute("info", info);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            case "add":
                view += "view_edit";
                break;

            case "syslistpublish":
                view += "sys_view_list";
                break;

            default:
                view += "view_list";
        }

        mmp.addAttribute("pageType", type);
        mmp.addAttribute("supplytype", EnumSupplyType.TYPE_CG.getValue());

        return view;
    }

    @RequestMapping("/srchList4p")
    @ResponseBody
    public Object srchList4p(int page, int rows, HttpSession session, HttpServletRequest request, WhgSupplyRoom recode) {
        ResponseBean rb = new ResponseBean();

        String pageType = request.getParameter("pageType");
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");

        try{
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            List states = null;
            //编辑列表，查 1可编辑 5已撤消
            if ("listedit".equalsIgnoreCase(pageType)){
                //states = Arrays.asList(1);
                recode.setCrtuser(sysUser.getId());
            }
            /*//审核列表，查 9待审核
            if ("listcheck".equalsIgnoreCase(pageType)){
                states = Arrays.asList(9);
            }
            //发布列表，查 2待发布 6已发布 4已下架
            if ("listpublish".equalsIgnoreCase(pageType)){
                states = Arrays.asList(2,6,4);
            }*/
            if ("listpublish".equalsIgnoreCase(pageType) || "listcheck".equalsIgnoreCase(pageType)){
                states = Arrays.asList(9,2,6,4);
            }

            //删除列表，查已删除 否则查未删除的
            if ("listdel".equalsIgnoreCase(pageType)){
                recode.setDelstate(1);
            }else{
                recode.setDelstate(0);
            }

            PageInfo pageInfo = this.whgSupplyRoomService.srch4p(page,rows,recode,states,sort,order, sysUser.getId());
            rb.setRows( pageInfo.getList() );
            rb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.debug("查询数据失败", e);
            rb.setRows(new ArrayList());
            rb.setSuccess(ResponseBean.FAIL);
        }

        return rb;
    }

    @RequestMapping("/sysSrchList4p")
    @ResponseBody
    public Object sysSrchList4p(int page, int rows, WhgSupplyRoom room, WebRequest request) {
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
            PageInfo pageInfo = this.whgSupplyRoomService.sysSrchList4p(page, rows, room, sort, order, record);
            resb.setRows( (List)pageInfo.getList() );
            resb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.debug("查询数据失败", e);
            resb.setRows(new ArrayList());
            resb.setSuccess(ResponseBean.FAIL);
        }

        return resb;
    }


    @WhgOPT(optType = EnumOptType.SUPPLY, optDesc = {"添加供需活动室"})
    @RequestMapping("/add")
    @ResponseBody
    public Object add(HttpSession session, WhgSupplyRoom info, /*String times,*/
                      @DateTimeFormat(pattern="yyyy-MM-dd") Date pstimestart_date,
                      @DateTimeFormat(pattern="yyyy-MM-dd") Date pstimeend_date){
        ResponseBean rb = new ResponseBean();

        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);

            info.setId(IDUtils.getID());
            info.setCrtuser(sysUser.getId());
            info.setCrtdate(new Date());
            info.setStatemdfuser(sysUser.getId());
            info.setStatemdfdate(new Date());
            info.setState(EnumBizState.STATE_CAN_EDIT.getValue());
            info.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
            info.setRecommend(0);

            if (pstimeend_date != null) {
                info.setPstimeend(pstimeend_date);
            }
            if (pstimestart_date != null) {
                info.setPstimestart(pstimestart_date);
            }

            this.whgSupplyRoomService.t_add(info/*, times*/);

        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("信息保存失败");
            log.error(rb.getErrormsg(), e);
        }

        return rb;
    }

    @WhgOPT(optType = EnumOptType.SUPPLY, optDesc = {"编辑供需活动室"})
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(HttpSession session, WhgSupplyRoom info, /*String times,*/
                       @DateTimeFormat(pattern="yyyy-MM-dd") Date pstimestart_date,
                       @DateTimeFormat(pattern="yyyy-MM-dd") Date pstimeend_date){
        ResponseBean rb = new ResponseBean();

        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);

            info.setStatemdfuser(sysUser.getId());
            info.setStatemdfdate(new Date());

            if (pstimeend_date != null) {
                info.setPstimeend(pstimeend_date);
            }
            if (pstimestart_date != null) {
                info.setPstimestart(pstimestart_date);
            }

            if (info.getFacility()==null){
                info.setFacility("");
            }

            this.whgSupplyRoomService.t_edit(info/*, times*/);

        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("信息保存失败");
            log.error(rb.getErrormsg(), e);
        }

        return rb;
    }

    @WhgOPT(optType = EnumOptType.SUPPLY, optDesc = {"取消推荐","推荐"}, valid = {"recommend=0","recommend=1"})
    @RequestMapping("/recommend")
    @ResponseBody
    public Object recommend(String id, int recommend){
        ResponseBean rb = new ResponseBean();
        try {
            if (id == null || id.isEmpty() || !Arrays.asList(0, 1).contains(recommend) ) {
                rb.setSuccess(ResponseBean.FAIL);
                rb.setErrormsg("参数错误");
                return rb;
            }

            WhgSupplyRoom recode = new WhgSupplyRoom();
            recode.setId(id);
            recode.setRecommend(recommend);

            this.whgSupplyRoomService.t_edit(recode/*, null*/);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("信息保存失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    @WhgOPT(optType = EnumOptType.SUPPLY, optDesc = {"送审","审核","打回","发布","取消发布"},
            valid = {"tostate=9","tostate=2","tostate=1","tostate=6","tostate=4"})
    @RequestMapping("/updstate")
    @ResponseBody
    public Object updstate(String ids, String formstates, int tostate, HttpSession session,
                           @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date optTime,
                           @RequestParam(value = "reason", required = false)String reason,
                           @RequestParam(value = "issms", required = false, defaultValue = "0")int issms){
        ResponseBean rb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            this.whgSupplyRoomService.t_updstate(ids, formstates, tostate, sysUser.getId(), optTime, reason, issms);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("信息保存失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    @WhgOPT(optType = EnumOptType.SUPPLY, optDesc = {"删除"})
    @RequestMapping("/del")
    @ResponseBody
    public Object del(String id){
        ResponseBean rb = new ResponseBean();
        try {

            this.whgSupplyRoomService.t_del(id);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("操作失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    @RequestMapping("/undel")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.SUPPLY, optDesc = {"还原"})
    public Object undel(String id){
        ResponseBean rb = new ResponseBean();
        try {
            this.whgSupplyRoomService.t_undel(id);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("操作失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    @RequestMapping("/recovery")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.SUPPLY, optDesc = {"回收"})
    public Object recovery(String id, HttpSession session){
        ResponseBean rb = new ResponseBean();
        try {
            WhgSupplyRoom info = new WhgSupplyRoom();
            info.setId(id);
            info.setDelstate(EnumStateDel.STATE_DEL_YES.getValue());
            this.whgSupplyRoomService.t_edit(info/*, null*/);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("操作失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }
}
