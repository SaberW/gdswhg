package com.creatoo.hn.controller.admin.gather;

import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgActivity;
import com.creatoo.hn.dao.model.WhgGather;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgTra;
import com.creatoo.hn.services.admin.activity.WhgActivityPlayService;
import com.creatoo.hn.services.admin.activity.WhgActivitySeatService;
import com.creatoo.hn.services.admin.gather.WhgGatherService;
import com.creatoo.hn.services.admin.train.WhgTrainCourseService;
import com.creatoo.hn.services.admin.train.WhgTrainService;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.creatoo.hn.util.enums.EnumTypeClazz;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanMap;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by rbg on 2017/9/22.
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping("/admin/gather")
public class WhgGatherController extends BaseController {

    @Autowired
    private WhgGatherService whgGatherService;

    @Autowired
    private WhgActivityPlayService whgActivityPlayService;
    @Autowired
    private WhgActivitySeatService whgActivitySeatService;

    @Autowired
    private WhgTrainService whgTrainService;
    @Autowired
    private WhgTrainCourseService whgTrainCourseService;


    @RequestMapping("/view/{type}")
    @WhgOPT(optType = EnumOptType.GATHER,
            optDesc = {"访问众筹编辑列表页", "访问众筹审核列表页","访问众筹发布列表页", "访问众筹回收站页", "访问众筹添加页", "访问众筹编辑页", "查看众筹信息"},
            valid = {"type=listedit", "type=listcheck", "type=listpublish", "type=listdel", "type=add", "type=edit", "type=show"})
    public String view(@PathVariable String type, ModelMap mmp, WebRequest request, HttpSession session){
        String view = "admin/gather/";
        switch (type){
            case "show" :
            case "edit" : {
                String id = request.getParameter("id");
                if (id!=null && !id.isEmpty()){
                    mmp.addAttribute("id", id);
                    try {
                        this.setInfo2model(id, mmp);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
            case "add" :
                view += "view_edit"; break;

            case "syslistpublish":
                view += "sys_view_list"; break;

            case "listdel":
            case "listedit":
            case "listcheck":
            case "listpublish":
                view += "view_list"; break;
        }

        mmp.addAttribute("pageType", type);

        String brandshow = request.getParameter("brandshow");
        String brandid = request.getParameter("brandid");
        if (brandshow != null && !brandshow.isEmpty()) {
            mmp.addAttribute("brandshow", brandshow);
        }
        if (brandid != null && !brandid.isEmpty()) {
            mmp.addAttribute("brandid", brandid);
        }

        WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
        if (sysUser!=null && sysUser.getCultid()!=null){
            mmp.addAttribute("userCultid", sysUser.getCultid());
        }else{
            mmp.addAttribute("userCultid", Constant.ROOT_SYS_CULT_ID);
        }

        return view;
    }


    /**
     * 装入信息到 mmp
     * @param id
     * @param mmp
     * @throws Exception
     */
    private void setInfo2model(String id, ModelMap mmp) throws Exception{
        Map info = this.whgGatherService.srchOneGather(id);
        mmp.addAttribute("info", info);
        if (info == null) {
            return;
        }
        String gat_etype = (String) info.get("gat_etype");
        String gat_refid = (String) info.get("gat_refid");
        if (gat_refid == null || gat_refid.isEmpty() || gat_etype == null || gat_etype.isEmpty() || "0".equals(gat_etype)) {
            return;
        }
        if (gat_etype.equals(EnumTypeClazz.TYPE_ACTIVITY.getValue())) {
            mmp.addAttribute("actSeatList",whgActivityPlayService.srchList4actId(gat_refid));
            JSONObject seatMap = whgActivitySeatService.getActivitySeatInfo(gat_refid);
            mmp.addAttribute("whgSeat",seatMap);
        }
        if (gat_etype.equals(EnumTypeClazz.TYPE_TRAIN.getValue())) {
            String[] age = new String[2];
            String age1 = this.whgTrainService.srchOne(gat_refid).getAge();
            if(!"".equals(age1) && age1 != null){
                age = age1.split(",");
                mmp.addAttribute("age1",age[0]);
                mmp.addAttribute("age2",age[1]);
            }
            mmp.addAttribute("state",this.whgTrainService.srchOne(gat_refid).getState());
            mmp.addAttribute("course",this.whgTrainCourseService.srchList(gat_refid));
        }
    }


    @RequestMapping("/add")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.GATHER, optDesc = {"添加众筹项目"})
    public Object add(HttpSession session, HttpServletRequest request, WhgActivity act, WhgTra tra) {
        ResponseBean rb = new ResponseBean();
        WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);

        try {

            WhgGather gather = this.getGather4Request(request);

            rb = this.whgGatherService.t_addGather(request, sysUser, gather, act, tra);

        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }

        return rb;
    }

    private WhgGather getGather4Request(HttpServletRequest request) throws Exception{
        Map<String, String[]> reqMap = request.getParameterMap();
        Map<String, Object> beanMap = new HashMap();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(Map.Entry<String, String[]> ent : reqMap.entrySet()){
            String key = ent.getKey();
            if (key!=null && key.startsWith("gat_")){
                String[] vals = ent.getValue();
                if (vals == null || vals.length == 0 || vals[0]==null) {
                    continue;
                }

                Object val = vals[0];
                if (vals.length == 1){
                    if (key.startsWith("gat_time")){
                        val = sdf.parse((String) val);
                    }
                }else {
                    StringBuffer sb = new StringBuffer();
                    for (String v : vals) {
                        if (v==null || v.isEmpty()) continue;
                        if (sb.length() > 0) sb.append(",");
                        sb.append(v);
                    }
                    val = sb.toString();
                }


                String prop = key.replace("gat_", "");
                beanMap.put(prop, val);
            }
        }
        WhgGather wg = new WhgGather();
        BeanMap bm = new BeanMap();
        bm.setBean(wg);
        bm.putAll(beanMap);
        wg = (WhgGather) bm.getBean();
        return wg;
    }

    @RequestMapping("/edit")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.GATHER, optDesc = {"编辑众筹项目"})
    public Object edit(HttpSession session, HttpServletRequest request, WhgActivity act, WhgTra tra) {
        ResponseBean rb = new ResponseBean();
        WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);

        try {

            WhgGather gather = this.getGather4Request(request);

            if (gather.getNoticetype() == null) gather.setNoticetype("");
            rb = this.whgGatherService.t_editGather(request, sysUser, gather, act, tra);

        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }

        return rb;
    }

    @RequestMapping("/recommend")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.GATHER, optDesc = {"取消推荐","推荐"}, valid = {"recommend=0","recommend=1"})
    public Object recommend(HttpSession session, WhgGather info){
        ResponseBean rb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            WhgGather whgGather=this.whgGatherService.t_srchOneGather(info.getId());
            whgGather.setRecommend(info.getRecommend());
            this.whgGatherService.t_editGather(whgGather);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("信息保存失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    @RequestMapping("/updstate")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.GATHER, optDesc = {"送审","审核","打回","发布","取消发布"},
            valid = {"tostate=9","tostate=2","tostate=1","tostate=6","tostate=4"})
    public Object updstate(String ids, String formstates, int tostate, HttpSession session,
                           @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date optTime,
                           @RequestParam(value = "reason", required = false)String reason,
                           @RequestParam(value = "issms", required = false, defaultValue = "0")int issms){
        ResponseBean rb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            this.whgGatherService.t_updateGatherState(ids,formstates,tostate,sysUser,optTime, reason, issms);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("信息保存失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    /**
     * 删除
     * @param id
     * @param session
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.GATHER, optDesc = {"删除"})
    public Object del(String id,String delstate){
        ResponseBean rb = new ResponseBean();
        try {
           this.whgGatherService.t_delGather(id);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("操作失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    /**
     * 回收
     * @param id
     * @param session
     * @return
     */
    @RequestMapping("/recovery")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.GATHER, optDesc = {"回收"})
    public Object recovery(String id, HttpSession session){
        ResponseBean rb = new ResponseBean();
        try {
            this.whgGatherService.t_recoveryGather(id);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("回收失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    /**
     * 还原
     * @param id
     * @param session
     * @return
     */
    @RequestMapping("/undel")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.GATHER, optDesc = {"还原"})
    public Object undel(String id){
        ResponseBean rb = new ResponseBean();
        try {
            this.whgGatherService.t_undelGather(id);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("操作失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }


    @RequestMapping("/srchList")
    @ResponseBody
    public Object srchList(int page, int rows, WebRequest request, HttpSession session, WhgGather recode) {
        String pageType = request.getParameter("pageType");
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");

        ResponseBean resb = new ResponseBean();
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

            PageInfo pageInfo = this.whgGatherService.srch4pGather(page, rows, recode, states, sort, order, sysUser.getId());
            resb.setRows( (List)pageInfo.getList() );
            resb.setTotal(pageInfo.getTotal());

            Map data = new HashMap();
            data.put("now", new Date());
            resb.setData(data);
        } catch (Exception e) {
            log.debug("查询数据失败", e);
            resb.setRows(new ArrayList());
            resb.setSuccess(ResponseBean.FAIL);
        }
        return resb;
    }

    @RequestMapping("/sysSrchList4p")
    @ResponseBody
    public Object sysSrchList4p(int page, int rows, WhgGather gather, WebRequest request) {
        ResponseBean resb = new ResponseBean();
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");

        String iscult = request.getParameter("iscult");
        String refid = request.getParameter("refTreeId");
        String pcalevel = request.getParameter("pcalevel");
        String pcatext = request.getParameter("pcatext");

        Map<String, String> record = new HashMap();
        record.put("iscult", iscult);
        record.put("refid", refid);
        record.put("pcalevel", pcalevel);
        record.put("pcatext", pcatext);

        try {
            PageInfo pageInfo = this.whgGatherService.sysSrch4pGather(page, rows, gather, sort, order, record);
            resb.setRows( (List)pageInfo.getList() );
            resb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.debug("查询数据失败", e);
            resb.setRows(new ArrayList());
            resb.setSuccess(ResponseBean.FAIL);
        }

        return resb;
    }

}
