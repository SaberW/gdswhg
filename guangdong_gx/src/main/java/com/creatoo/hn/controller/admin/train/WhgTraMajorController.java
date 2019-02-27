package com.creatoo.hn.controller.admin.train;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgTraMajor;
import com.creatoo.hn.services.admin.train.WhgTraMajorService;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信专业管理
 * Created by Administrator on 2017/9/22.
 */
@Controller
@RequestMapping("admin/tra/major")
public class WhgTraMajorController extends BaseController{

    @Autowired
    private WhgTraMajorService whgTraMajorService;

    /**
     * 进入微专业视图
     * @return
     */
    @RequestMapping("/view/{type}")
    @WhgOPT(optType = EnumOptType.MAJOR, optDesc = {"进入微专业列表"})
    public ModelAndView view(HttpServletRequest request, ModelMap mmp, @PathVariable("type") String type){
        ModelAndView view = new ModelAndView();
        try {
            mmp.addAttribute("type", type);
            if ("add".equalsIgnoreCase(type)){
                String id = request.getParameter("id");
                String targetShow = request.getParameter("targetShow");
                if(id != null){
                    mmp.addAttribute("id", id);
                    mmp.addAttribute("targetShow", targetShow);
                    mmp.addAttribute("major",this.whgTraMajorService.srchOne(id));
                }
                view.setViewName("admin/train/major/view_add");
            }else if("syspublish".equalsIgnoreCase(type)){
                view.setViewName("admin/train/major/sys_view_list");
            }else{
                view.setViewName("admin/train/major/view_list");
            }
        } catch (Exception e) {
            log.error("加载指定ID的微专业信息失败", e);
        }
        return view;
    }

    /**
     * 分页查询微专业列表
     * @param request
     * @return
     */
    @RequestMapping("/srchList")
    @ResponseBody
    public Object srchList4p(HttpServletRequest request){
        List<WhgTraMajor> list = new ArrayList<>();
        String cultid = request.getParameter("cultid");
        try {
            list = this.whgTraMajorService.t_srchList(cultid);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return list;
        }
        return list;
    }

    /**
     *  总分馆分页加载培训列表数据
     * @param request
     * @return
     */
    @RequestMapping("/srchSysList4p")
    @ResponseBody
    public ResponseBean srchSysList4p(HttpServletRequest request, WhgTraMajor major){
        ResponseBean resb = new ResponseBean();
        try {
            int page = Integer.parseInt((String)request.getParameter("page"));
            int rows = Integer.parseInt((String)request.getParameter("rows"));
            String sort = request.getParameter("sort");
            String order = request.getParameter("order");

            String iscult = request.getParameter("iscult");
            String syscultid = request.getParameter("syscultid");
            String level = request.getParameter("level");
            String cultname = request.getParameter("cultname");

            Map<String, String> param = new HashMap();
            param.put("iscult", iscult);
            param.put("syscultid", syscultid);
            param.put("level", level);
            param.put("cultname", cultname);
            PageInfo pageInfo = this.whgTraMajorService.t_srchSysList4p(page, rows, major, sort, order, param);
            resb.setRows( (List)pageInfo.getList() );
            resb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.debug("微专业查询失败", e);
            resb.setTotal(0);
            resb.setRows(new ArrayList());
            resb.setSuccess(ResponseBean.FAIL);
        }
        return resb;
    }

    /**
     * 查询微专业列表
     * @param page
     * @param rows
     * @param major
     * @param request
     * @return
     */
    @RequestMapping("/srchList4p")
    @ResponseBody
    public ResponseBean srchList4p(int page, int rows, WhgTraMajor major, WebRequest request, HttpSession session, String defaultState){
        ResponseBean rsb = new ResponseBean();
        try {
            String sort = request.getParameter("sort");
            String order = request.getParameter("order");
            WhgSysUser user = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            PageInfo pageInfo = this.whgTraMajorService.t_srchlist4p(page,rows,major,sort,order,user.getId(), defaultState);
            rsb.setRows(pageInfo.getList() );
            rsb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rsb.setRows(new ArrayList());
            rsb.setSuccess(ResponseBean.FAIL);
        }
        return rsb;
    }

    /**
     * 添加微专业
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.MAJOR, optDesc = {"添加微专业"})
    public ResponseBean add(WhgTraMajor major, HttpServletRequest request, HttpSession session) {
        ResponseBean res = new ResponseBean();
        WhgSysUser user = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
        try {
            this.whgTraMajorService.t_add(major,user);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("微专业保存失败");
            log.error(res.getErrormsg(), e);
        }
        return res;
    }

    /**
     * 修改微专业
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.MAJOR, optDesc = {"编辑微专业"})
    public ResponseBean edit(WhgTraMajor major){
        ResponseBean res = new ResponseBean();
        if (major.getId() == null){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("微专业主键信息丢失");
            return res;
        }
        try {
            this.whgTraMajorService.t_edit(major);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("微专业保存失败");
            log.error(res.getErrormsg(), e);
        }
        return res;
    }

    /**
     *  删除微专业
     * @param req
     * @param
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.MAJOR, optDesc = {"删除微专业"})
    public Object del(HttpServletRequest req, String id){
        ResponseBean res = new ResponseBean();
        try {
            this.whgTraMajorService.t_del(id);

        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("微专业删除失败");
            log.error(res.getErrormsg(), e);
        }
        return res;
    }

    /**
     * 回收微专业
     * @param req
     * @param id
     * @return
     */
    @RequestMapping("/recycle")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.MAJOR, optDesc = {"回收微专业"})
    public Object recycle(HttpServletRequest req, String id){
        ResponseBean res = new ResponseBean();
        try {
            this.whgTraMajorService.t_recycle(id, 1);

        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("微专业删除失败");
            log.error(res.getErrormsg(), e);
        }
        return res;
    }

    /**
     * 撤销回收微专业
     * @param req
     * @param id
     * @return
     */
    @RequestMapping("/unrecycle")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.MAJOR, optDesc = {"撤销回收微专业"})
    public Object unRecycle(HttpServletRequest req, String id){
        ResponseBean res = new ResponseBean();
        try {
            this.whgTraMajorService.t_recycle(id, 0);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("微专业删除失败");
            log.error(res.getErrormsg(), e);
        }
        return res;
    }

    /**
     * 修改状态
     * @param ids
     * @param formstates
     * @param tostate
     * @return
     */
    @RequestMapping("/updstate")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.MAJOR, optDesc = {"审核","打回","发布","取消发布"}, valid = {"tostate=2","tostate=1","tostate=3","tostate=2"})
    public ResponseBean updstate(String ids, String formstates, int tostate, HttpSession session,
                                 @RequestParam(value = "reason", required = false)String reason,
                                 @RequestParam(value = "issms", required = false, defaultValue = "0")int issms){
        ResponseBean res = new ResponseBean();
        try {
            WhgSysUser user = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            res = this.whgTraMajorService.t_updstate(ids, formstates, tostate, user, reason, issms);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("培训师资状态更改失败");
            log.error(res.getErrormsg()+" formstate: "+formstates+" tostate:"+tostate+" ids: "+ids, e);
        }
        return res;
    }

    /**
     * 是否推荐
     * @param ids
     * @param formrecoms
     * @param torecom
     * @return
     */
    @RequestMapping("/updrecommend")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.MAJOR, optDesc = {"推荐","取消推荐"}, valid = {"recommend=1","recommend=0"})
    public ResponseBean updrecommend(String ids, String formrecoms, int torecom){
        ResponseBean res = new ResponseBean();
        try {
            res = this.whgTraMajorService.t_updrecommend(ids,formrecoms,torecom);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("推荐失败！");
            log.error(res.getErrormsg()+" formrecoms: "+formrecoms+" torecom:"+torecom+" ids: "+ids, e);
        }
        return res;
    }

    @RequestMapping("setToprovince")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.MAJOR, optDesc = {"省级推荐","取消省级推荐"},valid = {"toprovince=1","toprovince=0"})
    public Object setToprovince(String id, int toprovince){
        ResponseBean rb = new ResponseBean();

        try {
            WhgTraMajor info = new WhgTraMajor();
            info.setId(id);
            info.setToprovince(toprovince);

            this.whgTraMajorService.t_edit(info);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("设置省级推荐失败");
        }

        return rb;
    }

    @RequestMapping("setTocity")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.MAJOR, optDesc = {"市级推荐","取消市级推荐"},valid = {"tocity=1","tocity=0"})
    public Object setTocity(String id, int tocity){
        ResponseBean rb = new ResponseBean();

        try {
            WhgTraMajor info = new WhgTraMajor();
            info.setId(id);
            info.setTocity(tocity);

            this.whgTraMajorService.t_edit(info);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("设置市级推荐失败");
        }

        return rb;
    }
}
