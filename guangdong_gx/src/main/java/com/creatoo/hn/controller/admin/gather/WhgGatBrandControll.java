package com.creatoo.hn.controller.admin.gather;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgGatherBrand;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.gather.WhgGatherService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.util.IDUtils;
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
 * Created by rbg on 2017/9/22.
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping("/admin/brand")
public class WhgGatBrandControll extends BaseController {

    @Autowired
    private WhgGatherService whgGatherService;

    @Autowired
    private WhgSystemCultService whgSystemCultService;


    @RequestMapping("/view/{type}")
    @WhgOPT(optType = EnumOptType.GATHER,
            optDesc = {"访问众筹品牌编辑列表页", "访问众筹品牌审核列表页","访问众筹品牌发布列表页", "访问众筹品牌回收站页", "访问众筹品牌添加页", "访问众筹品牌编辑页", "查看众筹品牌信息"},
            valid = {"type=listedit", "type=listcheck", "type=listpublish", "type=listdel", "type=add", "type=edit", "type=show"})
    public String view(@PathVariable String type, ModelMap mmp, WebRequest request, HttpSession session){
        String view = "admin/gather/brand/";
        switch (type){
            case "show" :
            case "edit" : {
                String id = request.getParameter("id");
                if (id!=null && !id.isEmpty()){
                    mmp.addAttribute("id", id);
                    try {
                        Object info = this.whgGatherService.srchOneBrand(id);
                        mmp.addAttribute("info", info);
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

        return view;
    }


    /**
     * 添加众筹品牌
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.GATHER, optDesc = {"添加众筹品牌"})
    public Object add(HttpSession session, WhgGatherBrand info){
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

            /*String cultid = sysUser.getCultid();
            if (cultid == null || cultid.isEmpty()) {
                cultid = Constant.ROOT_SYS_CULT_ID;
            }
            info.setCultid(cultid);*/

            /*WhgSysCult cult = this.whgSystemCultService.t_srchOne(cultid);
            info.setProvince(cult.getProvince());
            info.setCity(cult.getCity());
            info.setArea(cult.getArea());*/

            info.setRecommend(0);

            this.whgGatherService.t_addBrand(info);

        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("信息保存失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    /**
     * 编辑众筹品牌
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.GATHER, optDesc = {"编辑众筹品牌"})
    public Object edit(HttpSession session, WhgGatherBrand info){
        ResponseBean rb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);

            info.setStatemdfuser(sysUser.getId());
            info.setStatemdfdate(new Date());

            this.whgGatherService.t_editBrand(info);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("信息保存失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    @RequestMapping("/recommend")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.GATHER, optDesc = {"取消推荐","推荐"}, valid = {"recommend=0","recommend=1"})
    public Object recommend(HttpSession session, WhgGatherBrand info){
        ResponseBean rb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            this.whgGatherService.t_editBrand(info);
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
            this.whgGatherService.t_updstateBrand(ids, formstates, tostate, sysUser.getId(), optTime, reason, issms);
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
    public Object del(String id){
        ResponseBean rb = new ResponseBean();
        try {
            boolean isUse = this.whgGatherService.isUseBrand(id);
            if (isUse){
                rb.setSuccess(ResponseBean.FAIL);
                rb.setErrormsg("品牌已被引用，不能删除！");
                return rb;
            }

            this.whgGatherService.t_delBrand(id);
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
            boolean isUse = this.whgGatherService.isUseBrand(id);
            if (isUse){
                rb.setSuccess(ResponseBean.FAIL);
                rb.setErrormsg("品牌已被引用，不能回收！");
                return rb;
            }

            this.whgGatherService.t_recoveryBrand(id);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("回收失败");
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
    @WhgOPT(optType = EnumOptType.GATHER, optDesc = {"还原"})
    public Object undel(String id){
        ResponseBean rb = new ResponseBean();
        try {
            this.whgGatherService.t_undelBrand(id);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("操作失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }





    /**
     * 众筹品牌管理后台分页查询
     * @param page
     * @param rows
     * @param request
     * @param recode
     * @return
     */
    @RequestMapping("/srchList")
    @ResponseBody
    public Object srchList(int page, int rows, WebRequest request, HttpSession session, WhgGatherBrand recode) {
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

            PageInfo pageInfo = this.whgGatherService.srch4pBrand(page, rows, recode, states, sort, order, sysUser.getId());
            resb.setRows( (List)pageInfo.getList() );
            resb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.debug("查询数据失败", e);
            resb.setRows(new ArrayList());
            resb.setSuccess(ResponseBean.FAIL);
        }
        return resb;
    }

    @RequestMapping("/sysSrchList4p")
    @ResponseBody
    public Object sysSrchList4p(int page, int rows, WhgGatherBrand gatherBrand, WebRequest request){
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
            PageInfo pageInfo = this.whgGatherService.sysSrch4pBrand(page, rows, gatherBrand, sort, order, record);
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
     * 品牌列表
     * @return
     */
    @RequestMapping("/getList")
    @ResponseBody
    public Object getList(HttpSession session, WebRequest request) {
        try {
            String cultid = request.getParameter("cultid");

            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            /*if (cultid!=null && !cultid.isEmpty()) {
                cultid = Constant.ROOT_SYS_CULT_ID;
                if (sysUser != null && sysUser.getCultid() != null) {
                    cultid = sysUser.getCultid();
                }
            }*/

            if (cultid == null || cultid.isEmpty()) {
                return new ArrayList();
            }

            return this.whgGatherService.srchListBrand(cultid, sysUser.getId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ArrayList();
        }
    }

}
