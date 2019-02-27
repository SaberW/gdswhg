package com.creatoo.hn.controller.admin.mass;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgMassBrand;
import com.creatoo.hn.dao.model.WhgMassBrandBatch;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.mass.WhgMassBrandService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumOptType;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.creatoo.hn.util.enums.EnumTypeClazz;
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
 * Created by rbg on 2017/10/31.
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping("/admin/mass/batch")
public class WhgMassBrandBatchController extends BaseController{

    @Autowired
    private WhgMassBrandService whgMassBrandService;

    @WhgOPT(optType = EnumOptType.MASS_BATCH,
            optDesc = {"访问文化专题届次编辑列表页", "访问文化专题届次审核列表页","访问文化专题届次发布列表页", "访问文化专题届次回收站页", "访问文化专题届次添加页", "访问文化专题届次编辑页", "查看文化专题届次信息"},
            valid = {"type=listedit", "type=listcheck", "type=listpublish", "type=listdel", "type=add", "type=edit", "type=show"})
    @RequestMapping("/view/{type}")
    public String view(@PathVariable("type")String type,
                       ModelMap mmp, WebRequest request){
        String view = "admin/mass/batch/";

        switch (type){
            case "show":
            case "edit":
                try {
                    String id = request.getParameter("id");
                    if (id != null && !id.isEmpty()) {
                        mmp.addAttribute("id", id);
                        WhgMassBrandBatch info = this.whgMassBrandService.srchOneBatch(id);
                        mmp.addAttribute("info", info);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            case "add":
                view += "view_edit";
                break;

            case "syslistpublish":
                view += "sys_view_list"; break;

            default:
                view += "view_list";
        }

        mmp.addAttribute("pageType", type);
        mmp.addAttribute("enumTypeClazz", EnumTypeClazz.TYPE_MASS_BATCH.getValue());

        return view;
    }

    @RequestMapping("/srchList4p")
    @ResponseBody
    public Object srchList4p(int page, int rows, HttpSession session,
                             WebRequest request, WhgMassBrandBatch recode){
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
            //审核列表，查 9待审核
            if ("listcheck".equalsIgnoreCase(pageType)){
                states = Arrays.asList(9,2,6,4);
            }
            //发布列表，查 2待发布 6已发布 4已下架
            if ("listpublish".equalsIgnoreCase(pageType)){
                states = Arrays.asList(9,2,6,4);
            }

            //删除列表，查已删除 否则查未删除的
            if ("listdel".equalsIgnoreCase(pageType)){
                recode.setDelstate(1);
            }else{
                recode.setDelstate(0);
            }

            PageInfo pageInfo = this.whgMassBrandService.srch4pBrandBatch(page,rows,recode,states,sort,order, sysUser.getId());
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
    public Object sysSrchList4p(int page, int rows, WebRequest request, WhgMassBrandBatch recode){
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
            PageInfo pageInfo = this.whgMassBrandService.sysSrch4pBrandBatch(page, rows, recode, sort, order, record);
            resb.setRows( (List)pageInfo.getList() );
            resb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.debug("查询数据失败", e);
            resb.setRows(new ArrayList());
            resb.setSuccess(ResponseBean.FAIL);
        }

        return resb;
    }

    @WhgOPT(optType = EnumOptType.MASS_BATCH, optDesc = {"添加文化专题届次"})
    @RequestMapping("/add")
    @ResponseBody
    public Object add(HttpSession session, WhgMassBrandBatch info,
                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date _batdate){
        ResponseBean rb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            info.setId(IDUtils.getID());
            info.setCrtdate(new Date());
            info.setCrtuser(sysUser.getId());
            info.setStatemdfdate(new Date());
            info.setStatemdfuser(sysUser.getId());
            info.setState(EnumBizState.STATE_CAN_EDIT.getValue());
            info.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
            info.setRecommend(0);

            if (_batdate != null) {
                info.setBatdate(_batdate);
            }

            this.whgMassBrandService.addMassBrandBatch(info);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("保存数据失败");
            log.error(e.getMessage(), e);
        }
        return rb;
    }

    @WhgOPT(optType = EnumOptType.MASS_BATCH, optDesc = {"编辑文化专题届次"})
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(HttpSession session, WhgMassBrandBatch info,
                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date _batdate){
        ResponseBean rb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            info.setStatemdfuser(sysUser.getId());
            info.setStatemdfdate(new Date());
            if (_batdate != null) {
                info.setBatdate(_batdate);
            }

            this.whgMassBrandService.editMassBrandBatch(info);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("信息保存失败");
            log.error(rb.getErrormsg(), e);
        }

        return rb;
    }

    @WhgOPT(optType = EnumOptType.MASS_BATCH, optDesc = {"取消推荐","推荐"}, valid = {"recommend=0","recommend=1"})
    @RequestMapping("/recommend")
    @ResponseBody
    public Object updateState(String id, int recommend){
        ResponseBean rb = new ResponseBean();
        try {
            if (id == null || id.isEmpty() || !Arrays.asList(0, 1).contains(recommend) ) {
                rb.setSuccess(ResponseBean.FAIL);
                rb.setErrormsg("参数错误");
                return rb;
            }
            WhgMassBrandBatch info = new WhgMassBrandBatch();
            info.setId(id);
            info.setRecommend(recommend);

            this.whgMassBrandService.editMassBrandBatch(info);

        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("信息保存失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    @WhgOPT(optType = EnumOptType.MASS_BATCH, optDesc = {"送审","审核","打回","发布","取消发布"},
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
            this.whgMassBrandService.t_updstateBrandBatch(ids, formstates, tostate, sysUser.getId(), optTime, reason, issms);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("信息保存失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    @WhgOPT(optType = EnumOptType.MASS_BATCH, optDesc = {"删除"})
    @RequestMapping("/del")
    @ResponseBody
    public Object del(String id){
        ResponseBean rb = new ResponseBean();
        try {
            this.whgMassBrandService.t_delBrandBatch(id);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("操作失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    @RequestMapping("/recovery")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.MASS_BATCH, optDesc = {"回收"})
    public Object recovery(String id){
        ResponseBean rb = new ResponseBean();
        try {
            WhgMassBrandBatch info = new WhgMassBrandBatch();
            info.setId(id);
            info.setDelstate(EnumStateDel.STATE_DEL_YES.getValue());

            this.whgMassBrandService.editMassBrandBatch(info);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("操作失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    @RequestMapping("/undel")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.MASS_BATCH, optDesc = {"还原"})
    public Object undel(String id){
        ResponseBean rb = new ResponseBean();
        try {
            WhgMassBrandBatch info = new WhgMassBrandBatch();
            info.setId(id);
            info.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
            info.setState(EnumBizState.STATE_CAN_EDIT.getValue());

            this.whgMassBrandService.editMassBrandBatch(info);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("操作失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }
}
