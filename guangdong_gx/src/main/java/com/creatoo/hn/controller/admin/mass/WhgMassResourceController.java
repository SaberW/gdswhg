package com.creatoo.hn.controller.admin.mass;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgMassResource;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.mass.WhgMassResourceService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by rbg on 2017/10/30.
 * 群文资源
 */
@Controller
@RequestMapping("/admin/mass/resource")
public class WhgMassResourceController extends BaseController {
    
    @Autowired
    private WhgMassResourceService whgMassResourceService;


    @WhgOPT(optType = EnumOptType.MASS_ARTIST,
            optDesc = {"访问群文资源编辑列表页", "访问群文资源审核列表页","访问群文资源发布列表页", "访问群文资源回收站页", "访问群文资源添加页", "访问群文资源编辑页", "查看群文资源信息"},
            valid = {"type=listedit", "type=listcheck", "type=listpublish", "type=listdel", "type=add", "type=edit", "type=show"})
    @RequestMapping("/view/{type}")
    public String view(@PathVariable("type")String type, ModelMap mmp, WebRequest request){
        String view = "admin/mass/resource/";

        switch (type){
            case "show":
            case "edit":
                try {
                    String id = request.getParameter("id");
                    if (id != null && !id.isEmpty()) {
                        mmp.addAttribute("id", id);
                        Object info = this.whgMassResourceService.srchOne(id);
                        mmp.addAttribute("info", info);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            case "add":
                view += "view_edit";
                break;
            default:
                view += "view_list";
        }

        mmp.addAttribute("pageType", type);

        return view;
    }

    @RequestMapping("/srchList4p")
    @ResponseBody
    public Object srchList4p(int page, int rows, WebRequest request, WhgMassResource recode) {
        ResponseBean rb = new ResponseBean();

        String pageType = request.getParameter("pageType");
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");
        List states = null;
        //编辑列表，查 1可编辑 5已撤消
        if ("listedit".equalsIgnoreCase(pageType)){
            states = Arrays.asList(1);
        }
        //审核列表，查 9待审核
        if ("listcheck".equalsIgnoreCase(pageType)){
            states = Arrays.asList(9);
        }
        //发布列表，查 2待发布 6已发布 4已下架
        if ("listpublish".equalsIgnoreCase(pageType)){
            states = Arrays.asList(2,6,4);
        }

        //删除列表，查已删除 否则查未删除的
        if ("listdel".equalsIgnoreCase(pageType)){
            recode.setDelstate(1);
        }else{
            recode.setDelstate(0);
        }

        try{
            PageInfo pageInfo = this.whgMassResourceService.srch4p(page,rows,recode,states,sort,order);
            rb.setRows( pageInfo.getList() );
            rb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.debug("查询数据失败", e);
            rb.setRows(new ArrayList());
            rb.setSuccess(ResponseBean.FAIL);
        }

        return rb;
    }


    @WhgOPT(optType = EnumOptType.MASS_ARTIST, optDesc = {"添加群文资源"})
    @RequestMapping("/add")
    @ResponseBody
    public Object add(HttpSession session, WhgMassResource info){
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

            this.whgMassResourceService.t_add(info);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("保存数据失败");
            log.error(e.getMessage(), e);
        }
        return rb;
    }

    @WhgOPT(optType = EnumOptType.MASS_ARTIST, optDesc = {"编辑群文资源"})
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(HttpSession session, WhgMassResource info){
        ResponseBean rb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            info.setStatemdfuser(sysUser.getId());
            info.setStatemdfdate(new Date());

            this.whgMassResourceService.t_edit(info);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("保存数据失败");
            log.error(e.getMessage(), e);
        }
        return rb;
    }


    @WhgOPT(optType = EnumOptType.MASS_ARTIST, optDesc = {"取消推荐","推荐"}, valid = {"recommend=0","recommend=1"})
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
            WhgMassResource info = new WhgMassResource();
            info.setId(id);
            info.setRecommend(recommend);

            this.whgMassResourceService.t_edit(info);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("信息保存失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    @WhgOPT(optType = EnumOptType.MASS_ARTIST, optDesc = {"送审","审核","打回","发布","取消发布"},
            valid = {"tostate=9","tostate=2","tostate=1","tostate=6","tostate=4"})
    @RequestMapping("/updstate")
    @ResponseBody
    public Object updstate(String ids, String formstates, int tostate, HttpSession session,
                           @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date optTime){
        ResponseBean rb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            this.whgMassResourceService.t_updstate(ids, formstates, tostate, sysUser.getId(), optTime);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("信息保存失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    @WhgOPT(optType = EnumOptType.MASS_ARTIST, optDesc = {"删除"})
    @RequestMapping("/del")
    @ResponseBody
    public Object del(String id){
        ResponseBean rb = new ResponseBean();
        try {
            this.whgMassResourceService.t_del(id);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("操作失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    @RequestMapping("/undel")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.MASS_ARTIST, optDesc = {"还原"})
    public Object undel(String id){
        ResponseBean rb = new ResponseBean();
        try {
            this.whgMassResourceService.t_undel(id);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("操作失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }
}
