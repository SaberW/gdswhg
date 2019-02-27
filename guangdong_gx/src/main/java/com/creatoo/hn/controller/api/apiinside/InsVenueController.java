package com.creatoo.hn.controller.api.apiinside;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSupplyRoom;
import com.creatoo.hn.dao.model.WhgSupplyVen;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.services.admin.supply.WhgSupplyRoomService;
import com.creatoo.hn.services.admin.supply.WhgSupplyVenService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.system.WhgSystemDeptService;
import com.creatoo.hn.services.api.apioutside.venue.ApiVenueService;
import com.creatoo.hn.services.comm.ResetRefidService;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumProject;
import com.creatoo.hn.util.enums.EnumTypeClazz;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@SuppressWarnings("ALL")
@Controller
@CrossOrigin
@RequestMapping("/api/inside")
public class InsVenueController extends BaseController {

    @Autowired
    private ApiVenueService apiVenueService;

    @Autowired
    private WhgSupplyRoomService whgSupplyRoomService;

    @Autowired
    private WhgSupplyVenService whgSupplyVenService;

    @Autowired
    private ResetRefidService resetRefidService;

    @Autowired
    private WhgSystemDeptService whgSystemDeptService;

    @Autowired
    private WhgSystemCultService whgSystemCultService;

    /**
     * 查场馆列表
     * @param req
     * @param page
     * @param pageSize
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/venue/list", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object venueList(HttpServletRequest req,
                            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){
        ApiResultBean arb = new ApiResultBean();
        try {
            Map recode = new HashMap();
            recode.put("protype", EnumProject.PROJECT_NBGX.getValue());
            //recode.put("cultid", req.getParameter("cultid"));
            String cultid = req.getParameter("cultid");
            if (cultid != null && !cultid.isEmpty()) {
                recode.put("cultid",  Arrays.asList(cultid.replaceAll(","," ").trim().split(" ")));
            }

            String deptid = req.getParameter("deptid");
            if (deptid!=null && !deptid.isEmpty()){
                recode.put("deptid", this.whgSystemDeptService.srchDeptStrList(deptid));
            }

            recode.put("etype", req.getParameter("etype"));
            recode.put("area", req.getParameter("area"));
            recode.put("etag", req.getParameter("etag"));
            recode.put("ekey", req.getParameter("ekey"));
            recode.put("content", req.getParameter("content"));
            recode.put("province", req.getParameter("province"));
            recode.put("city", req.getParameter("city"));
            recode.put("area", req.getParameter("area"));
            recode.put("pscity", req.getParameter("pscity"));//配送区域
            recode.put("psprovince", req.getParameter("psprovince"));//配送区域


            PageInfo pageInfo = this.apiVenueService.selectVenList4Page(page, pageSize, recode);

            arb.setPageInfo(pageInfo);
            arb.setRows(pageInfo.getList());
        } catch (Exception e) {
            arb.setMsg("查询数据失败");
            arb.setCode(101);
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 查场馆信息
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/venue/info", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object getVenue(String id){
        ApiResultBean arb = new ApiResultBean();
        try {
            Object info = this.apiVenueService.findVenInfo(id, EnumProject.PROJECT_NBGX.getValue());
            arb.setData(info);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(),e);
        }
        return arb;
    }

    /**
     * 场馆活动室列表
     * @param venid
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/venue/rooms", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object getVenueRooms(String venid){
        ApiResultBean arb = new ApiResultBean();
        try {
            List list = this.apiVenueService.selectRooms4ven(venid, EnumProject.PROJECT_NBGX.getValue());
            arb.setRows(list);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(),e);
        }
        return arb;
    }

    /**
     * 查推荐场馆列表，
     * @param exVenid   例外的场馆ID
     * @param cultid  分馆id
     * @param size 条数
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/venue/recommendVenList", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object getRecommendVenList(@RequestParam(required = false, value = "exVenid") String exVenid,
                                      @RequestParam(required = false, value = "cultid") String cultid,
                                      @RequestParam(required = false, value = "deptid") String deptid,
                                      @RequestParam(required = false, value = "size") Integer size){
        ApiResultBean arb = new ApiResultBean();

        try {
            List<String> cultids = null;
            if (cultid != null && !cultid.isEmpty()) {
                cultids = Arrays.asList(cultid.replaceAll(","," ").trim().split(" "));
            }
            List<String> deptids = null;
            if (deptid != null && !deptid.isEmpty()) {
                deptids = this.whgSystemDeptService.srchDeptStrList(deptid);
            }



            List list = this.apiVenueService.selectRecommendVenList4supply(exVenid, cultids,deptids, size, EnumProject.PROJECT_NBGX.getValue());
            arb.setRows(list);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            arb.setData(new ArrayList());
            log.error(e.getMessage(), e);
        }

        return arb;
    }

    /**
     * 查询活动室详情
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/venue/roominfo", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object getVenueRoomInfo(String id){
        ApiResultBean arb = new ApiResultBean();
        try {
            Map room = this.whgSupplyRoomService.srchOne(id); //this.apiVenueService.selectVenRoom4id(id);
            if (room==null){
                throw new Exception("not find data");
            }

            /*Map room = new HashMap();
            BeanMap bm = new BeanMap(info);
            room.putAll(bm);*/
            //类型枚举值
            room.put("enumTypeClazz", EnumTypeClazz.TYPE_ROOM.getValue());

            this.resetRefidService.resetRefid4type(room, "etype", "facility");
            this.resetRefidService.resetRefid4tag(room, "etag");

            WhgSupplyVen ven = this.whgSupplyVenService.srchOne(room.get("venid").toString());
            if (ven!=null) {
                room.put("venTitle", ven.getTitle());
                room.put("address", ven.getAddress());

                WhgSysCult cult = this.whgSystemCultService.t_srchOne(ven.getCultid());
                if (cult!=null){
                    room.put("venCultTitle", cult.getName());
                }
            }

            arb.setData(room);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 相关活动室
     * @param id
     * @param size
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/venue/refrooms", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object getRefRooms(String id,
                              @RequestParam(value = "size",required = false)Integer size){
        ApiResultBean arb = new ApiResultBean();
        try {
            List list = this.whgSupplyRoomService.selectRefRoomList(id, size, EnumProject.PROJECT_NBGX.getValue());
            arb.setRows(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arb;
    }

}
