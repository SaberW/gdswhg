package com.creatoo.hn.controller.admin.train;


import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgTra;
import com.creatoo.hn.services.admin.train.WhgTraInMajorService;
import com.creatoo.hn.services.admin.train.WhgTrainCourseService;
import com.creatoo.hn.services.admin.train.WhgTrainService;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 培训驿站action
 * @author wenjingqiang
 * @version 1-201703
 * Created by Administrator on 2017/3/21.
 */
@SuppressWarnings("ALL")
@RestController
@RequestMapping("/admin/train")
public class WhgTrainController extends BaseController{

    /**
     * 培训service
     */
    @Autowired
    private WhgTrainService whgTrainService;

    @Autowired
    private WhgTraInMajorService whgTraInMajorService;

    /**
     * 课程service
     */
    @Autowired
    private WhgTrainCourseService whgTrainCourseService;

    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    /**
     * 进入培训管理视图
     * @return
     */
    @RequestMapping("/view/{type}")
    @WhgOPT(optType = EnumOptType.TRA, optDesc = {"进入培训列表页"})
    public ModelAndView view(HttpServletRequest request, ModelMap mmp, @PathVariable("type") String type){
        ModelAndView view = new ModelAndView();
        String[] age = new String[2];

        try {
            mmp.addAttribute("type", type);
            if ("add".equalsIgnoreCase(type)){
                String id = request.getParameter("id");
                String targetShow = request.getParameter("targetShow");
                String mid = request.getParameter("mid");
                if(id != null){
                    mmp.addAttribute("id", id);
                    mmp.addAttribute("targetShow", targetShow);
                    String age1 = this.whgTrainService.srchOne(id).getAge();
                    if(!"".equals(age1) && age1 != null){
                        age = age1.split(",");
                        mmp.addAttribute("age1",age[0]);
                        mmp.addAttribute("age2",age[1]);
                    }
                    mmp.addAttribute("whgTra",this.whgTrainService.srchOne(id));
                    mmp.addAttribute("majorid",this.whgTraInMajorService.t_selMajorByTraid(id,1));
                    mmp.addAttribute("state",this.whgTrainService.srchOne(id).getState());
                    mmp.addAttribute("course",this.whgTrainCourseService.srchList(id));
                    view.setViewName("admin/train/base/view_edit");
                }else{
                    if(mid != null && !"".equals(mid)){
                        mmp.addAttribute("mid",mid);
                    }
                    view.setViewName("admin/train/base/view_add");
                }

            }else if("syspublish".equalsIgnoreCase(type)){
                view.setViewName("admin/train/base/sys_view_list");
            }else{
                view.setViewName("admin/train/base/view_list");
            }
        } catch (Exception e) {
            log.error("加载指定ID的培训信息失败", e);


        }
        return view;
    }



    /**
     *  分页加载培训列表数据
     * @param request
     * @return
     */
    @RequestMapping("/srchList4p")
    public ResponseBean srchList4p(HttpServletRequest request,WhgTra tra, String sort, String order,HttpSession session){
        ResponseBean resb = new ResponseBean();
        WhgTra whgTra = new WhgTra();
        try {
            int page = Integer.parseInt((String)request.getParameter("page"));
            int rows = Integer.parseInt((String)request.getParameter("rows"));
            String pageType = request.getParameter("type");
            WhgSysUser user = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
             String title = request.getParameter("title");
            if(title != null && !"".equals(title)) {
                whgTra.setTitle(title);
            }
            String cultid = request.getParameter("cultid");
            if(cultid != null && !"".equals(cultid)) {
                whgTra.setCultid(cultid);
            }
            String deptid = request.getParameter("deptid");
            if(deptid != null && !"".equals(deptid)) {
                whgTra.setDeptid(deptid);
            }

            if(request.getParameter("recommend") != null) {
                int recommend = Integer.parseInt((String) request.getParameter("recommend"));
                whgTra.setRecommend(recommend);
            }
            if(request.getParameter("state") != null) {
                int state = Integer.parseInt((String) request.getParameter("state"));
                whgTra.setState(state);
            }
            PageInfo pageInfo = this.whgTrainService.t_srchList4p(page,rows,pageType,whgTra,sort,order,user.getId());
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


    /**
     *  总分馆分页加载培训列表数据
     * @param request
     * @return
     */
    @RequestMapping("/srchSysList4p")
    public ResponseBean srchSysList4p(HttpServletRequest request, WhgTra tra){
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
            PageInfo pageInfo = this.whgTrainService.t_srchSysList4p(page, rows, tra, sort, order, param);
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

    /**
     * 普通列表查询
     * @return
     */
    @RequestMapping("/srchList")
    public Object srchList(WhgTra tra, @RequestParam(value = "bmEnd",required = false)String bmEnd)  {
        List list= null;
        try {
            list = this.whgTrainService.t_srchList(tra, bmEnd);
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return new ArrayList();
        }
        return list;
    }

    /**
     * 添加培训
     * @return
     */
    @RequestMapping("/add")
    @WhgOPT(optType = EnumOptType.TRA, optDesc = {"添加培训"})
    public ResponseBean add(WhgTra tra, HttpServletRequest request, HttpSession session) {
        ResponseBean res = new ResponseBean();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        WhgSysUser user = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
        try {
            String _age1 = request.getParameter("age1");
            String _age2 = request.getParameter("age2");
            String sin_starttime = request.getParameter("sin_starttime");
            String sin_endtime = request.getParameter("sin_endtime");
            String[] _starttime = request.getParameterValues("_starttime");
            String[] _endtime = request.getParameterValues("_endtime");
            String[] fixedweek = request.getParameterValues("fixedweek");
            String fixedstarttime = request.getParameter("fixedstarttime");
            String fixedendtime = request.getParameter("fixedendtime");
            String mid = request.getParameter("mid");
            String[] majorid = request.getParameterValues("majorid");

            res = this.whgTrainService.t_add(tra,user,_age1,_age2,sin_starttime,sin_endtime,_starttime,_endtime,fixedweek,fixedstarttime,fixedendtime,mid,majorid);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("培训保存失败");
            log.error(res.getErrormsg(), e);
        }
        return res;
    }

    /**
     * 修改培训
     * @return
     */
    @RequestMapping("/edit")
    @WhgOPT(optType = EnumOptType.TRA, optDesc = {"修改培训"})
    public ResponseBean edit(WhgTra tra, HttpSession session, HttpServletRequest request){
        ResponseBean res = new ResponseBean();
        if (tra.getId() == null){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("培训主键信息丢失");
            return res;
        }
        try {
            String _age1 = request.getParameter("age1");
            String _age2 = request.getParameter("age2");
            String sin_starttime = request.getParameter("sin_starttime");
            String sin_endtime = request.getParameter("sin_endtime");
            String[] _starttime = request.getParameterValues("_starttime");
            String[] _endtime = request.getParameterValues("_endtime");
            String[] fixedweek = request.getParameterValues("fixedweek");
            String fixedstarttime = request.getParameter("fixedstarttime");
            String fixedendtime = request.getParameter("fixedendtime");
            String starttime = request.getParameter("starttime");
            String endtime = request.getParameter("endtime");
            String[] majorid = request.getParameterValues("majorid");
            //
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            if("".equals(request.getParameter("age1")) && "".equals(request.getParameter("age2"))){
                tra.setAge("");
            }
            if(tra.getEbrand() == null ){
                tra.setEbrand("");
            }
            if(tra.getTeacherid() == null ){
                tra.setTeacherid("");
            }
            if(tra.getEkey() == null ){
                tra.setEkey("");
            }
            if(tra.getEtag() == null ){
                tra.setEtag("");
            }
            if(tra.getVenue() == null ){
                tra.setVenue("");
            }
            if(tra.getVenroom() == null ){
                tra.setVenroom("");
            }
            res = this.whgTrainService.t_edit(tra, sysUser,_age1,_age2,sin_starttime,sin_endtime,_starttime,_endtime,fixedweek,fixedstarttime,fixedendtime,starttime,endtime,majorid);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("培训信息保存失败");
            log.error(res.getErrormsg(), e);
        }
        return res;
    }

    /**
     * 删除培训
     * @return
     */
    @RequestMapping("/del")
    @WhgOPT(optType = EnumOptType.TRA, optDesc = {"删除培训"})
    public ResponseBean del(String id, HttpSession session) {
        ResponseBean res = new ResponseBean();
        try {
            this.whgTrainService.t_del(id);

        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("培训信息删除失败");
            log.error(res.getErrormsg(), e);
        }
        return res;
    }

    /**
     * 还原删除
     * @param id
     * @param session
     * @return
     */
    @RequestMapping("/undel")
    @WhgOPT(optType = EnumOptType.TRA, optDesc = {"还原培训"})
    public Object undel(String id, HttpSession session){
        ResponseBean rb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            this.whgTrainService.t_undel(id, sysUser);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("培训信息还原失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    /**
     * 修改状态
     * @param ids
     * @param formstates
     * @param tostate
     * @param session
     * @return
     */
    @RequestMapping("/updstate")
    @WhgOPT(optType = EnumOptType.TRA, optDesc = {"送审","审核","打回","发布","取消发布"}, valid = {"tostate=9","tostate=2","tostate=1","tostate=6","tostate=4"})
    public ResponseBean updstate(String ids, String formstates, int tostate, HttpSession session,
                                 @RequestParam(value = "statemdfdate", required = false)String statemdfdate,
                                 @RequestParam(value = "reason", required = false)String reason,
                                 @RequestParam(value = "issms", required = false, defaultValue = "0")int issms){
        ResponseBean res = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            res = this.whgTrainService.t_updstate(statemdfdate, ids, formstates, tostate, sysUser, reason, issms);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("培训状态更改失败");
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
    @WhgOPT(optType = EnumOptType.TRA, optDesc = {"推荐","取消推荐"}, valid = {"recommend=1","recommend=0"})
    public ResponseBean updrecommend(String ids, String formrecoms, int torecom){
        ResponseBean res = new ResponseBean();
        try {
            res = this.whgTrainService.t_updrecommend(ids,formrecoms,torecom);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("推荐失败！");
            log.error(res.getErrormsg()+" formrecoms: "+formrecoms+" torecom:"+torecom+" ids: "+ids, e);
        }
        return res;
    }
    /**
     * 是否上首页
     * @param ids
     * @param formupindex
     * @param toupindex
     * @return
     */
    @RequestMapping("/upindex")
    @WhgOPT(optType = EnumOptType.TRA, optDesc = {"上首页","取消上首页"}, valid = {"formupindex=1","formupindex=0"})
    public ResponseBean upindex(String ids, String formupindex, int toupindex){
        ResponseBean res = new ResponseBean();
        try {
            res = this.whgTrainService.t_upindex(ids,formupindex,toupindex);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("上首页失败！");
            log.error(res.getErrormsg()+" formupindex: "+formupindex+" toupindex:"+toupindex+" ids: "+ids, e);
        }
        return res;
    }

    @RequestMapping("setToprovince")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.TRA, optDesc = {"省级推荐","取消省级推荐"},valid = {"toprovince=1","toprovince=0"})
    public Object setToprovince(String id, int toprovince){
        ResponseBean rb = new ResponseBean();

        try {
            WhgTra info = new WhgTra();
            info.setId(id);
            info.setToprovince(toprovince);

            this.whgTrainService.t_editTra4id(info);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("设置省级推荐失败");
        }

        return rb;
    }

    @RequestMapping("setTocity")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.TRA, optDesc = {"市级推荐","取消市级推荐"},valid = {"tocity=1","tocity=0"})
    public Object setTocity(String id, int tocity){
        ResponseBean rb = new ResponseBean();

        try {
            WhgTra info = new WhgTra();
            info.setId(id);
            info.setTocity(tocity);

            this.whgTrainService.t_editTra4id(info);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("设置市级推荐失败");
        }

        return rb;
    }


    /**
     *  分页查询培训列表数据
     * @return
     */
    @RequestMapping("/srchTraList")
    @ResponseBody
    public ResponseBean srchTraList(int page,int rows,HttpServletRequest request,HttpSession session){
        ResponseBean resb = new ResponseBean();
        try {
            String mid = request.getParameter("mid");
            String cultid = " ";
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            if(sysUser != null && sysUser.getId().equals("2015121200000000")){
                cultid = "0000000000000000";
            }else{
                cultid = sysUser.getCultid();
            }

            PageInfo pageInfo = this.whgTrainService.t_srchTraList(page,rows,mid,cultid);
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
