package com.creatoo.hn.controller.admin.train;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgTraTeacher;
import com.creatoo.hn.services.admin.train.WhgTraInMajorService;
import com.creatoo.hn.services.admin.train.WhgTrainTeacherService;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 培训师资管理action
 * @author wenjingqiang
 * @version 1-201703
 * Created by Administrator on 2017/4/20.
 */
@RestController
@RequestMapping("/admin/train/tea")
public class WhgTrainTeacherAction extends BaseController{
    /**
     * 培训师资service
     */
    @Autowired
    private WhgTrainTeacherService whgTrainTeacherService;

    @Autowired
    private WhgTraInMajorService whgTraInMajorService;

    /**
     * 日志
     */
    Logger log = org.apache.log4j.Logger.getLogger(this.getClass());

    /**
     * 进入培训师资管理视图
     * @return
     */
    @RequestMapping("/view/{type}")
    @WhgOPT(optType = EnumOptType.TRATEA, optDesc = {"进入培训师资列表"})
    public ModelAndView view(HttpServletRequest request, ModelMap mmp, @PathVariable("type") String type){
        ModelAndView view = new ModelAndView();
        try {
            mmp.addAttribute("type", type);
            if ("add".equalsIgnoreCase(type)){
                String teacherid = request.getParameter("id");
                String targetShow = request.getParameter("targetShow");
                String mid = request.getParameter("mid");
                if(teacherid != null){
                    mmp.addAttribute("id", teacherid);
                    mmp.addAttribute("targetShow", targetShow);
                    mmp.addAttribute("tea",this.whgTrainTeacherService.srchOne(teacherid));
                    mmp.addAttribute("majorid",this.whgTraInMajorService.t_selMajorByTraid(teacherid,2));
                }
                if(mid != null && !"".equals(mid)){
                    mmp.addAttribute("mid",mid);
                }
                view.setViewName("admin/train/teacher/view_add");
            }else if("syspublish".equalsIgnoreCase(type)){
                view.setViewName("admin/train/teacher/sys_view_list");
            }else{
                view.setViewName("admin/train/teacher/view_list");
            }
        } catch (Exception e) {
            log.error("加载指定ID的培训师资信息失败", e);
        }
        return view;
    }

    /**
     *  分页加载培训师资列表数据
     * @param request
     * @return
     */
    @RequestMapping("/srchList4p")
    public ResponseBean srchList4p(HttpServletRequest request,WhgTraTeacher tea,String sort, String order,HttpSession session, String defaultState){
        //获取请求参数
        ResponseBean resb = new ResponseBean();
        try {
            int page = Integer.parseInt((String)request.getParameter("page"));
            int rows = Integer.parseInt((String)request.getParameter("rows"));
            WhgSysUser user = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            PageInfo pageInfo = this.whgTrainTeacherService.t_srchList4p(page,rows,tea,sort,order,user.getId(), defaultState);
            resb.setRows(pageInfo.getList() );
            resb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.debug("培训师资查询失败", e);
            resb.setTotal(0);
            resb.setRows(new ArrayList());
            resb.setSuccess(ResponseBean.FAIL);
        }
        return resb;
    }


    /**
     *  总分馆分页加载培训列表数据
     * @param request
     * @return
     */
    @RequestMapping("/srchSysList4p")
    @ResponseBody
    public ResponseBean srchSysList4p(HttpServletRequest request, WhgTraTeacher teacher){
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
            PageInfo pageInfo = this.whgTrainTeacherService.t_srchSysList4p(page, rows, teacher, sort, order, param);
            resb.setRows( (List)pageInfo.getList() );
            resb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.debug("培训师资查询失败", e);
            resb.setTotal(0);
            resb.setRows(new ArrayList());
            resb.setSuccess(ResponseBean.FAIL);
        }
        return resb;
    }

    /**
     * 查询老师列表
     * @param request
     * @return
     */
    @RequestMapping("/srchList")
    @ResponseBody
    public Object srchList(HttpServletRequest request){
        List<WhgTraTeacher> list = new ArrayList<>();
//        JSONArray cults = (JSONArray) session.getAttribute(Constant.SESSION_ADMIN_CULT);
//
//        List cultids = new ArrayList();
//        for(Object obj : cults){
//            JSONObject json = (JSONObject) obj;
//            cultids.add(json.get("id"));
//        }
        String cultid = request.getParameter("cultid");
        try {
            list = this.whgTrainTeacherService.t_srchList(cultid);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ArrayList();
        }
        return list;
    }

    /**
     * 添加培训师资
     * @return
     */
    @RequestMapping("/add")
    @WhgOPT(optType = EnumOptType.TRATEA, optDesc = {"添加培训师资"})
    public ResponseBean add(WhgTraTeacher tea, HttpServletRequest request, HttpSession session) {
        ResponseBean res = new ResponseBean();
        WhgSysUser user = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
        try {
            String mid = request.getParameter("mid");
            String[] majorid = request.getParameterValues("majorid");
            this.whgTrainTeacherService.t_add(tea,user,mid,majorid);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("培训师资保存失败");
            log.error(res.getErrormsg(), e);
        }
        return res;
    }

    /**
     * 修改培训师资
     * @return
     */
    @RequestMapping("/edit")
    @WhgOPT(optType = EnumOptType.TRATEA, optDesc = {"编辑培训师资"})
    public ResponseBean edit(HttpServletRequest request,WhgTraTeacher tea){
        ResponseBean res = new ResponseBean();
        if (tea.getId() == null){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("培训师资主键信息丢失");
            return res;
        }
        try {
            String[] majorid = request.getParameterValues("majorid");
            this.whgTrainTeacherService.t_edit(tea,majorid);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("培训师资信息保存失败");
            log.error(res.getErrormsg(), e);
        }
        return res;
    }

    /**
     *  删除培训师资数据
     * @param req
     * @param
     * @return
     */
    @RequestMapping("/del")
    @WhgOPT(optType = EnumOptType.TRATEA, optDesc = {"删除培训师资"})
    public Object saveTraintpl(HttpServletRequest req, String id){
        ResponseBean res = new ResponseBean();
        try {
            this.whgTrainTeacherService.t_del(id);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("培训师资信息删除失败");
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
    @WhgOPT(optType = EnumOptType.MAJOR, optDesc = {"回收培训师资"})
    public Object recycle(HttpServletRequest req, String id){
        ResponseBean res = new ResponseBean();
        try {
            this.whgTrainTeacherService.t_recycle(id, 1);

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
    @WhgOPT(optType = EnumOptType.MAJOR, optDesc = {"撤销回收培训师资"})
    public Object unRecycle(HttpServletRequest req, String id){
        ResponseBean res = new ResponseBean();
        try {
            this.whgTrainTeacherService.t_recycle(id, 0);
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
    @WhgOPT(optType = EnumOptType.TRATEA, optDesc = {"审核","打回","发布","取消发布"}, valid = {"tostate=2","tostate=1","tostate=3","tostate=2"})
    public ResponseBean updstate(String ids, String formstates, int tostate, HttpSession session,
                                 @RequestParam(value = "reason", required = false)String reason,
                                 @RequestParam(value = "issms", required = false, defaultValue = "0")int issms){
        ResponseBean res = new ResponseBean();
        try {
            WhgSysUser user = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            res = this.whgTrainTeacherService.t_updstate(ids, formstates, tostate, user, reason, issms);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("培训师资状态更改失败");
            log.error(res.getErrormsg()+" formstate: "+formstates+" tostate:"+tostate+" ids: "+ids, e);
        }
        return res;
    }

    @RequestMapping("setToprovince")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.TRATEA, optDesc = {"省级推荐","取消省级推荐"},valid = {"toprovince=1","toprovince=0"})
    public Object setToprovince(String id, int toprovince){
        ResponseBean rb = new ResponseBean();

        try {
            WhgTraTeacher info = new WhgTraTeacher();
            info.setId(id);
            info.setToprovince(toprovince);

            this.whgTrainTeacherService.t_edit4Pk(info);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("设置省级推荐失败");
        }

        return rb;
    }

    @RequestMapping("setTocity")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.TRATEA, optDesc = {"市级推荐","取消市级推荐"},valid = {"tocity=1","tocity=0"})
    public Object setTocity(String id, int tocity){
        ResponseBean rb = new ResponseBean();

        try {
            WhgTraTeacher info = new WhgTraTeacher();
            info.setId(id);
            info.setTocity(tocity);

            this.whgTrainTeacherService.t_edit4Pk(info);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("设置市级推荐失败");
        }

        return rb;
    }

    /**
     *  分页查询培训老师列表数据
     * @return
     */
    @RequestMapping("/srchTeaList")
    @ResponseBody
    public ResponseBean srchTraList(int page,int rows,HttpServletRequest request,HttpSession session){
        ResponseBean resb = new ResponseBean();
        try {
            String mid = request.getParameter("mid");
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            String cultid = "";
            if(sysUser != null && sysUser.getId().equals("2015121200000000")){
                cultid = "0000000000000000";
            }else{
                cultid = sysUser.getCultid();
            }
            PageInfo pageInfo = this.whgTrainTeacherService.t_srchTeaList(page,rows,mid,cultid);
            resb.setRows( (List)pageInfo.getList() );
            resb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.debug("培训查询失败", e);
            resb.setTotal(0);
            resb.setRows(new ArrayList());
            resb.setSuccess(ResponseBean.FAIL);
        }
        return resb;
    }

}
