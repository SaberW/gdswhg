package com.creatoo.hn.controller.admin.train;


import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.comm.ExcelTransformer;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.admin.gather.WhgGatherService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.services.admin.train.WhgTrainEnrolService;
import com.creatoo.hn.services.admin.train.WhgTrainService;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.TransformerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * * 培训报名管理action
 * @author wenjingqiang
 * @version 1-201703
 * Created by Administrator on 2017/4/1.
 */
@SuppressWarnings("ALL")
@RestController
@RequestMapping("/admin/train/enrol")
public class WhgTrainEnrolController {

    /**
     * 报名service
     */
    @Autowired
    private WhgTrainEnrolService whgTrainEnrolService;

    @Autowired
    private WhgTrainService whgTrainService;

    @Autowired
    private WhgGatherService whgGatherService;

    @Autowired
    private WhgSystemUserService whgSystemUserService;

    /**
     * 日志
     */
    Logger log = Logger.getLogger(this.getClass());

    @RequestMapping("/order/list")
    public ModelAndView orderList(@RequestParam(value = "biz", required = false, defaultValue = "PT") String biz){
        ModelAndView mav = new ModelAndView();
        mav.addObject("biz", biz);
        mav.setViewName("admin/train/enrol/view_list_order");
        return mav;
    }

    @RequestMapping("/order/yaohaolist")
    public ModelAndView orderList2(@RequestParam(value = "biz", required = false, defaultValue = "PT") String biz) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("biz", biz);
        mav.setViewName("admin/train/enrol/view_list_yaohao");
        return mav;
    }

    @RequestMapping("/order/addorderlist")
    public ModelAndView addorderlist(@RequestParam(value = "biz", required = false, defaultValue = "PT") String biz) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("biz", biz);
        mav.setViewName("admin/train/enrol/view_list_add_order");
        return mav;
    }

    @RequestMapping("/yhaoList")
    public Object yaohaoListt(int page, int rows, @RequestParam(value = "ids", required = false, defaultValue = "") String ids, @RequestParam(value = "number", required = false, defaultValue = "0") Integer number, HttpServletRequest request) {
        ResponseBean resb = new ResponseBean();
        if (ids.equals("")) {
            resb.setErrormsg("信息不完整");
            resb.setSuccess("0");
            return resb;
        }
        try {
            WhgTra tra = whgTrainService.srchOne(ids);
            if (tra != null && tra.getIsyaohao()!=null && tra.getIsyaohao().intValue() == 1) {
                resb.setErrormsg("该培训已摇号，请重新选择!");
                resb.setSuccess("0");
                return resb;
            }
            PageInfo pageInfo = this.whgTrainEnrolService.ramEnroll2(ids, number, page, rows);
            resb.setRows(pageInfo.getList());
            resb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.debug("培训报名信息查询失败", e);
            resb.setTotal(0);
            resb.setRows(new ArrayList());
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("培训报名信息查询失败");
        }

        return resb;
    }

    @RequestMapping("/srchList4pOrders")
    public Object srchList4pOrders(int page, int rows, HttpServletRequest request, HttpSession session){
        ResponseBean resb = new ResponseBean();

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
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
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
            String type = (String) record.get("type");
            if (type!=null && "0".equals(type)){
                record.put("isbasicclass", 0);
                record.put("states", Arrays.asList(1,2,3));
            }
            record.remove("type");

            PageInfo pageInfo = this.whgTrainEnrolService.t_srchList4pOrders(page, rows, record);

            resb.setRows(pageInfo.getList());
            resb.setTotal(pageInfo.getTotal());

            if (type!=null && "1".equals(type)){
                //总报名数
                int count = this.whgTrainEnrolService.t_selCountOrder(1, record);
                //有效的报名数
                int goodCount = this.whgTrainEnrolService.t_selCountOrder(2, record);
                //未处理的报名数
                int unCheckCount = this.whgTrainEnrolService.t_selCountOrder(3, record);
                //面试总人数
                int viewCount = this.whgTrainEnrolService.t_selCountOrder(4, record);
                //成功录取人数
                int passCount = this.whgTrainEnrolService.t_selCountOrder(5, record);
                Map data = new HashMap();
                data.put("count", count);
                data.put("goodCount", goodCount);
                data.put("unCheckCount", unCheckCount);
                data.put("viewCount", viewCount);
                data.put("passCount", passCount);
                resb.setData(data);
            }

        } catch (Exception e) {
            log.debug("培训报名信息查询失败", e);
            resb.setTotal(0);
            resb.setRows(new ArrayList());
            resb.setSuccess(ResponseBean.FAIL);
        }

        return resb;
    }

    @RequestMapping("/params/tralist")
    public Object paramsTraList(HttpSession session, HttpServletRequest request, String biz) {
        String isbasicclass = request.getParameter("isbasicclass");
        String state = request.getParameter("state");
        String islive = request.getParameter("islive");
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(sysUser.getId());
            List<String> deptids = this.whgSystemUserService.getAllDeptId4PMS(sysUser.getId());
            return this.whgTrainEnrolService.selectTraList4Params(cultids, deptids, biz, isbasicclass, state, islive);
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return new ArrayList();
        }
    }


    @RequestMapping("/view/{type}")
    @WhgOPT(optType = EnumOptType.TRA, optDesc = {"进入培训报名列表页"})
    public ModelAndView view(HttpServletRequest request, ModelMap mmp, @PathVariable("type") String type){
        ModelAndView view = new ModelAndView();
        String id = request.getParameter("id");
        String gatherid = request.getParameter("gatherid");
        //总报名数
        int count = this.whgTrainEnrolService.t_selCount(id,1);
        //有效的报名数
        int goodCount = this.whgTrainEnrolService.t_selCount(id,2);
        //未处理的报名数
        int unCheckCount = this.whgTrainEnrolService.t_selCount(id,3);
        //面试总人数
        int viewCount = this.whgTrainEnrolService.t_selCount(id,4);
        //成功录取人数
        int passCount = this.whgTrainEnrolService.t_selCount(id,5);
        String isbasicclass = request.getParameter("isbasicclass");

        try {
            WhgTra tra = whgTrainService.srchOne(id);
            view.addObject("traInfo", tra);
            /*view.addObject("nowDate", System.currentTimeMillis());
            view.addObject("enrollendtime", tra.getEnrollendtime().getTime());*/
            String issuccess = "";
            if (gatherid!=null && !gatherid.isEmpty()){
                WhgGather gather = this.whgGatherService.t_srchOneGather(gatherid);
                issuccess = gather.getIssuccess().toString();
            }
            view.addObject("issuccess", issuccess);

            if(isbasicclass != null && !"".equals(isbasicclass)){
                view.addObject("isbasicclass",isbasicclass);
            }else {
                //WhgTra tra = whgTrainService.srchOne(id);
                view.addObject("isbasicclass",tra.getIsbasicclass());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        view.addObject("id",id);
        view.addObject("count",count);
        view.addObject("goodCount",goodCount);
        view.addObject("unCheckCount",unCheckCount);
        view.addObject("viewCount",viewCount);
        view.addObject("passCount",passCount);
        view.setViewName("admin/train/enrol/view_"+type);
        return view;
    }

    /**
     *  分页加载培训列表数据
     * @param request
     * @return
     */
    @RequestMapping("/srchList4p")
    public ResponseBean srchList4p(HttpServletRequest request){
        ResponseBean resb = new ResponseBean();
        try {
            int page = Integer.parseInt((String)request.getParameter("page"));
            int rows = Integer.parseInt((String)request.getParameter("rows"));
            String traid = request.getParameter("traid");
            int type = Integer.parseInt((String)request.getParameter("type"));
            int tab = Integer.parseInt((String)request.getParameter("tab"));
            String state = request.getParameter("state");
            String contactphone = request.getParameter("contactphone");

            PageInfo pageInfo = this.whgTrainEnrolService.t_srchList4p(page,rows,traid,type,tab,state,contactphone);
            resb.setRows( (List)pageInfo.getList() );
            resb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.debug("培训报名信息查询失败", e);
            resb.setTotal(0);
            resb.setRows(new ArrayList());
            resb.setSuccess(ResponseBean.FAIL);
        }
        return resb;
    }


    /**
     * 分页加载培训列表数据
     *
     * @param request
     * @return
     */
    @RequestMapping("/userTraList")
    public Object userTraList(HttpServletRequest request, WhgUser user) {
        ResponseBean resb = new ResponseBean();
        try {
            int page = Integer.parseInt((String) request.getParameter("page"));
            int rows = Integer.parseInt((String) request.getParameter("rows"));
            String traid = request.getParameter("traid");
            PageInfo pageInfo = this.whgTrainEnrolService.t_srchUserTraList4p(page, rows, traid, user);
            resb.setRows((List) pageInfo.getList());
            resb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.debug("培训报名信息查询失败", e);
            resb.setTotal(0);
            resb.setRows(new ArrayList());
            resb.setSuccess(ResponseBean.FAIL);
        }
        return resb;
    }

    /**
     * 改变状态
     * @param ids
     * @param fromstate
     * @param tostate
     * @param session
     * @return
     */
    @RequestMapping("/updstate")
    @WhgOPT(optType = EnumOptType.TRA, optDesc = {"审核面试","审核失败","报名成功","面试失败"}, valid = {"tostate=4","tostate=3","tostate=6","tostate=5"})
    public ResponseBean updstate(String statedesc, String ids, String fromstate, int tostate, HttpSession session, String viewtime, String viewaddress){
        ResponseBean res = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            res = this.whgTrainEnrolService.t_updstate(statedesc, ids, fromstate, tostate, sysUser, viewtime, viewaddress);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("培训报名状态更改失败");
            log.error(res.getErrormsg()+" formstate: "+fromstate+" tostate:"+tostate+" ids: "+ids, e);
        }
        return res;
    }

    @RequestMapping("/updstateyaohao")
    public ResponseBean updstate(String ids, String fromstate, int tostate, HttpSession session) {
        ResponseBean res = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            res = this.whgTrainEnrolService.ramEnrolList(ids, fromstate, tostate, sysUser);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("培训报名状态更改失败");
            log.error(res.getErrormsg() + " formstate: " + fromstate + " tostate:" + tostate + " ids: " + ids, e);
        }
        return res;
    }

    @RequestMapping("/addorder")
    public ResponseBean addorder(String ids, String traid, HttpSession session) {
        ResponseBean res = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            res = this.whgTrainEnrolService.addorder(ids, traid, sysUser);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("新增培训报名订单失败");
            log.error(res.getErrormsg() + " 新增培训报名订单失败", e);
        }
        return res;
    }

    /**
     * 随机录取
     * @param ids
     * @param fromstate
     * @param tostate
     * @return
     */
    @RequestMapping("/ramEnroll")
    @WhgOPT(optType = EnumOptType.TRA, optDesc = {"随机录取"})
    public ResponseBean ramEnroll(String ids, String fromstate, int tostate, HttpSession session){
        ResponseBean res = new ResponseBean();
        if (ids == null){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("培训报名信息主键丢失");
            return res;
        }
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            res = this.whgTrainEnrolService.ramEnroll(ids,fromstate,tostate,sysUser);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("随机录取操作失败");
            log.error(res.getErrormsg()+" formstate: "+fromstate+" tostate:"+tostate+" ids: "+ids, e);
        }
        return res;
    }

    /**
     * 培训报名管理导出
     * @param
     * @return
     */
    @RequestMapping("/exportExcel")
    public ResponseBean exportExcel(HttpServletRequest request, HttpServletResponse response, String tab) {
        ResponseBean res = new ResponseBean();
        ExcelTransformer excelTransformer = new ExcelTransformer();
        try {
            if (tab != null && "0".equals(tab)) {
                excelTransformer.preProcessing(response, "面试通知列表清单");
            } else {
                excelTransformer.preProcessing(response, "录取列表清单");
            }
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("template" + File.separator + "报名管理清单.xls");
            OutputStream os = response.getOutputStream();
            Transformer transformer = TransformerFactory.createTransformer(is, os);
            JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();
            Map<String, Object> functionMap = new HashMap<>();
            functionMap.put("et", excelTransformer);
            evaluator.getJexlEngine().setFunctions(functionMap);
            List<WhgTraEnrolExcel> detailList = whgTrainEnrolService.serch(request);
            Context context = new Context();
            context.putVar("detailList", detailList);
            AreaBuilder areaBuilder = new XlsCommentAreaBuilder(transformer);
            List<Area> xlsAreaList = areaBuilder.build();
            Area xlsArea = xlsAreaList.get(0);
            xlsArea.applyAt(new CellRef("结果!A1"), context);
            transformer.write();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    @RequestMapping("/sendMsg")
    @WhgOPT(optType = EnumOptType.TRA, optDesc = {"重发短信"})
    public ResponseBean sendMsg(String id, String viewtime, String viewaddress){
        ResponseBean res = new ResponseBean();
        if (id == null){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("培训报名信息主键丢失");
            return res;
        }
        try {
            this.whgTrainEnrolService.t_sendMsg(id,viewtime,viewaddress);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("重发短信操作失败");
            log.error(res.getErrormsg(), e);
        }
        return res;
    }

    @RequestMapping("/leaveView")
    @WhgOPT(optType = EnumOptType.TRA, optDesc = {"进入培训课程列表页"})
    public ModelAndView toLeaveView(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/train/enrol/leave/view_list");
        return view;
    }


    /**
     * 查询请假信息
     * @param request
     * @return
     */
    @RequestMapping("/leaveview")
    @ResponseBody
    public ModelAndView leaveView(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        ResponseBean rsb = new ResponseBean();
        try {
            String id = request.getParameter("id");
            String userid = request.getParameter("userid");
            String canEdit = request.getParameter("canEdit");
            if(canEdit != null && canEdit != ""){
                view.addObject("canEdit",canEdit);
            }
            rsb = this.whgTrainEnrolService.selLeave(id,userid);
            if(rsb.getData() != null){
                view.addObject("leave",rsb.getData());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        view.setViewName("admin/train/enrol/leave/view_edit");
        return view;
    }


    /**
     * 审核请假条
     * @return
     */
    @RequestMapping("/editLeave")
    @ResponseBody
    public ResponseBean editLeave(WhgTraleave leave, HttpSession session, HttpServletRequest request){
        ResponseBean res = new ResponseBean();
        try {
            res = this.whgTrainEnrolService.t_editLeave(leave);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("课程信息保存失败");
            log.error(res.getErrormsg(), e);
        }
        return res;
    }
}
