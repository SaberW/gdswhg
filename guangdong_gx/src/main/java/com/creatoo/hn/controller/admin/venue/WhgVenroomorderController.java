package com.creatoo.hn.controller.admin.venue;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgVenRoom;
import com.creatoo.hn.dao.model.WhgVenRoomOrder;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.services.admin.venue.WhgVenroomService;
import com.creatoo.hn.services.admin.venue.WhgVenroomorderService;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by rbg on 2017/3/30.
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping("/admin/venue/roomorder")
public class WhgVenroomorderController extends BaseController{

    @Autowired
    private WhgVenroomorderService whgVenroomorderService;
    @Autowired
    private WhgVenroomService whgVenroomService;

    @Autowired
    private WhgSystemUserService whgSystemUserService;

    @RequestMapping("/view")
    @WhgOPT(optType = EnumOptType.ROOM, optDesc = {"进入活动室订单列表"})
    public String view(ModelMap mmp, String roomid, WebRequest request){
        mmp.addAttribute("roomid", roomid);
        mmp.addAttribute("roomtitle", request.getParameter("roomtitle"));
        return "admin/venue/roomorder/view_list";
    }

    @RequestMapping("/view/add")
    public String viewShow(ModelMap mmp, String id, WebRequest request){
        try {
            WhgVenRoomOrder order = this.whgVenroomorderService.srchOne(id);
            WhgVenRoom room = this.whgVenroomService.srchOne(order.getRoomid());
            mmp.addAttribute("order", order);
            mmp.addAttribute("room", room);
        } catch (Exception e) {
            log.debug("活动室订单信息查询失败", e);
        }
        return "admin/venue/roomorder/view_add";
    }

    @RequestMapping("/srchList4p")
    @ResponseBody
    public Object srchList4p(int page, int rows, WhgVenRoomOrder roomOrder, WebRequest request,
                             @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDay,
                             @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDay){
        ResponseBean resb = new ResponseBean();
        try {
            String sort = request.getParameter("sort");
            String order = request.getParameter("order");

            PageInfo pageInfo = this.whgVenroomorderService.srchList4p(page, rows, roomOrder, sort, order, startDay, endDay);
            resb.setRows( pageInfo.getList() );
            resb.setTotal( pageInfo.getTotal() );
        } catch (Exception e) {
            log.debug("活动室预订查询失败", e);
            resb.setRows(new ArrayList());
            resb.setSuccess(ResponseBean.FAIL);
        }

        return resb;
    }


    @RequestMapping("/updstate")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.ROOM, optDesc = {"审核通过","审核拒绝"}, valid = {"tostate=3","tostate=2"})
    public Object updstate(HttpSession session, WhgVenRoomOrder roomOrder, String formstates, int tostate) throws Exception{
        ResponseBean rb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            rb = this.whgVenroomorderService.t_updstate(roomOrder, formstates, tostate, sysUser);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("预定状态更改失败");
            log.error(rb.getErrormsg()+" formstate: "+formstates+" tostate:"+tostate+" ids: "+roomOrder.getId(), e);
        }
        return rb;
    }

    @RequestMapping("/view/list")
    @WhgOPT(optType = EnumOptType.ROOM, optDesc = {"访问活动室订单列表"})
    public String viewlist(ModelMap mmp, WebRequest request){
        return "admin/venue/roomorder/view_list_order";
    }

    @RequestMapping("/srchList4pOrder")
    @ResponseBody
    public Object srchList4pOrder(int page, int rows, WebRequest request, HttpSession session,
                             @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDay,
                             @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDay) {
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
            if (startDay!=null){
                record.put("startday", startDay);
            }
            if (endDay!=null){
                record.put("endday", endDay);
            }

            /*String sort = request.getParameter("sort");
            String order = request.getParameter("order");
            String roomTitle = request.getParameter("roomTitle");
            PageInfo pageInfo = this.whgVenroomorderService.srchList4pOrders(page, rows, roomOrder, sort, order, roomTitle, startDay, endDay);
            */
            PageInfo pageInfo = this.whgVenroomorderService.srchList4pOrders(page, rows, record);
            resb.setRows( pageInfo.getList() );
            resb.setTotal( pageInfo.getTotal() );
        } catch (Exception e) {
            log.debug("活动室预订查询失败", e);
            resb.setRows(new ArrayList());
            resb.setSuccess(ResponseBean.FAIL);
        }

        return resb;
    }

}
