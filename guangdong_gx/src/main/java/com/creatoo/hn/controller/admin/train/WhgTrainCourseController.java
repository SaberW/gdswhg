package com.creatoo.hn.controller.admin.train;


import com.creatoo.hn.Constant;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgTra;
import com.creatoo.hn.dao.model.WhgTraCourse;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.services.admin.train.WhgTrainCourseService;
import com.creatoo.hn.services.admin.train.WhgTrainEnrolService;
import com.creatoo.hn.services.admin.train.WhgTrainService;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 培训课程管理action
 * @author wenjingqiang
 * @version 1-201703
 * Created by Administrator on 2017/3/28.
 */
@RestController
@RequestMapping("/admin/train/course")
public class WhgTrainCourseController {

    /**
     * 课程管理seivice
     */
    @Autowired
    private WhgTrainCourseService whgTrainCourseService;

    @Autowired
    private WhgTrainService whgTrainService;

    @Autowired
    private WhgTrainEnrolService whgTrainEnrolService;

    @Autowired
    private WhgSystemUserService whgSystemUserService;

    /**
     * 日志
     */
    Logger log = Logger.getLogger(this.getClass());

    @RequestMapping("/view/{type}")
    @WhgOPT(optType = EnumOptType.TRA, optDesc = {"进入培训课程列表页"})
    public ModelAndView view(HttpServletRequest request, ModelMap mmp, @PathVariable("type") String type){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        ModelAndView view = new ModelAndView();
        String id = request.getParameter("id");
        String starttime = request.getParameter("starttime");
        String endtime = request.getParameter("endtime");
        String islive = request.getParameter("islive");
        view.addObject("id",id);
        view.addObject("starttime",starttime);
        view.addObject("endtime",endtime);
        view.addObject("islive",islive);
        view.setViewName("admin/train/course/view_"+type);
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
            int page = Integer.parseInt(request.getParameter("page"));
            int rows = Integer.parseInt(request.getParameter("rows"));
            String id = request.getParameter("id");
            PageInfo pageInfo = this.whgTrainCourseService.t_srchList4p(page,rows,id);
            resb.setRows( (List)pageInfo.getList() );
            resb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.debug("课程查询失败", e);
            resb.setTotal(0);
            resb.setRows(new ArrayList());
            resb.setSuccess(ResponseBean.FAIL);
        }
        return resb;
    }

    /**
     * 根据培训id查询课程表和报名信息
     * @param request
     * @return
     */
    @RequestMapping("/srchLeaveList")
    @ResponseBody
    public ResponseBean srchLeaveList(HttpServletRequest request){
        ResponseBean rsb = new ResponseBean();
        Map param = new HashMap();
        try {
            WhgSysUser whgSysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
            List<String> cultids=new ArrayList<>();
            List<String> deptids=new ArrayList<>();
            String traid = request.getParameter("traid");
            String courseid = request.getParameter("courseid");
            String realname = request.getParameter("realname");
            String biz = request.getParameter("biz");
            String islive = request.getParameter("islive");
            param.put("traid", traid);
            param.put("courseid", courseid);
            param.put("realname", realname);
            param.put("biz", biz);
            param.put("islive", islive);


            if (request.getParameter("cultid") == null) {
                try {
                    cultids = this.whgSystemUserService.getAllCultId4PMS(whgSysUser.getId());
                    if (cultids!=null && cultids.size()>0){
                        param.put("cultids", cultids);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }else{
                cultids.add(request.getParameter("cultid"));
                param.put("cultids", cultids);
            }
            if (request.getParameter("deptid") == null) {
                try {
                    deptids= this.whgSystemUserService.getAllDeptId4PMS(whgSysUser.getId());
                    if (deptids != null && deptids.size() > 0) {
                        param.put("deptids", deptids);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }else{
                deptids.add(request.getParameter("deptid"));
                param.put("deptids", deptids);
            }
            Integer state = null;
            if(request.getParameter("state") != null){
                state = Integer.parseInt(request.getParameter("state"));
            }
            param.put("state", state);
            rsb = this.whgTrainCourseService.t_srchLeaveList(param);
        } catch (Exception e) {
            rsb.setSuccess(ResponseBean.FAIL);
            rsb.setErrormsg("查询失败");
            log.error(rsb.getErrormsg(), e);
        }
        return rsb;
    }

    /**
     * 根据培训id查询课程表和报名信息
     * @param traid
     * @return
     */
    @RequestMapping("/srchOne")
    @ResponseBody
    public ResponseBean srchOne(String traid,String courseid){
        ResponseBean rsb = new ResponseBean();
        try {
            rsb = this.whgTrainCourseService.t_srchOne4Courseid(traid,courseid);
        } catch (Exception e) {
            rsb.setSuccess(ResponseBean.FAIL);
            rsb.setErrormsg("课程信息保存失败");
            log.error(rsb.getErrormsg(), e);
        }
        return rsb;
    }


    /**
     * 根据培训id查询课程表和报名信息
     * @param request
     * @return
     */
    @RequestMapping("/srchTra4Leave")
    @ResponseBody
    public Object srchTra4Leave(HttpServletRequest request){
        ResponseBean rsb = new ResponseBean();
        try {
            rsb = this.whgTrainCourseService.t_srchTra4Leave();
        } catch (Exception e) {
            rsb.setSuccess(ResponseBean.FAIL);
            rsb.setErrormsg("查询失败");
            log.error(rsb.getErrormsg(), e);
        }
        return rsb.getRows();
    }

    /**
     * 编辑课程
     * @return
     */
    @RequestMapping("/edit")
    @WhgOPT(optType = EnumOptType.TRA, optDesc = {"编辑课程"})
    public ResponseBean edit(String traid, WhgTraCourse course, HttpSession session, HttpServletRequest request){
        ResponseBean res = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            if (course.getId() == null){
                res.setSuccess(ResponseBean.FAIL);
                res.setErrormsg("课程主键信息丢失");
                return res;
            }
            int count = this.whgTrainEnrolService.selCountEnroll(traid);
//            if(count > 0){
//                res.setSuccess(ResponseBean.FAIL);
//                res.setErrormsg("已经存在报名记录，不可编辑课程！");
//                course.setStarttime(null);
//                course.setEndtime(null);
//                //return res;
//            }

            this.whgTrainCourseService.t_edit(course, sysUser);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("课程信息保存失败");
            log.error(res.getErrormsg(), e);
        }
        return res;
    }

    /**
     * 添加课程
     * @param traid
     * @param course
     * @param session
     * @param request
     * @return
     */
    @RequestMapping("/addCourse")
    @WhgOPT(optType = EnumOptType.TRA, optDesc = {"编辑课程"})
    public ResponseBean addCourse(String traid, WhgTraCourse course, HttpSession session, HttpServletRequest request){
        ResponseBean res = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            int count = this.whgTrainEnrolService.selCountEnroll(traid);
//            if(count > 0){
//                res.setSuccess(ResponseBean.FAIL);
//                res.setErrormsg("已经存在报名记录，不可添加课程！");
//                course.setStarttime(null);
//                course.setEndtime(null);
//                return res;
//            }
            //单场
            WhgTra whgTra=this.whgTrainService.srchOne(traid);
            if(whgTra.getIsmultisite()==0){
                List<WhgTraCourse> whgTraCourse=this.whgTrainCourseService.srchCount(traid);
                if(whgTraCourse.size()>=1){
                    res.setSuccess(ResponseBean.FAIL);
                    res.setErrormsg("单场培训不能添加多场!");
                    return res;
                }
            }

            this.whgTrainCourseService.t_add(course, sysUser);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("课程信息保存失败");
            log.error(res.getErrormsg(), e);
        }
        return res;
    }



    /**
     * 删除课程
     * @return
     */
    @RequestMapping("/del")
    @WhgOPT(optType = EnumOptType.TRA, optDesc = {"删除课程"})
    public ResponseBean del(String id,String traid, HttpSession session) {
        ResponseBean res = new ResponseBean();
        try {
            WhgSysUser user = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);

            int count = this.whgTrainEnrolService.selCountEnroll(traid);
//            if(count > 0){
//                res.setSuccess(ResponseBean.FAIL);
//                res.setErrormsg("已经存在报名记录，不可删除课程！");
//                return res;
//            }

            this.whgTrainCourseService.t_del(id, user);

        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("课程信息删除失败");
            log.error(res.getErrormsg(), e);
        }
        return res;
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
    @WhgOPT(optType = EnumOptType.TRA, optDesc = {"启用","停用"}, valid = {"tostate=1","tostate=0"})
    public ResponseBean updstate(String ids, String formstates, int tostate, HttpSession session){
        ResponseBean res = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            res = this.whgTrainCourseService.t_updstate(ids, formstates, tostate, sysUser);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("课程状态更改失败");
            log.error(res.getErrormsg()+" formstate: "+formstates+" tostate:"+tostate+" ids: "+ids, e);
        }
        return res;
    }

    /**
     * 签到
     * @param traid
     * @param userid
     * @param courseid
     * @param starttime
     * @param endtime
     * @return
     */
    @RequestMapping("/signCourse")
    @ResponseBody
    public ResponseBean sign(String traid,String enrolid,String userid,String courseid,String starttime,String endtime){
        ResponseBean rsb = new ResponseBean();
        try {
            rsb = this.whgTrainCourseService.t_sign(traid,enrolid,userid,courseid,starttime,endtime);
        } catch (Exception e) {
            rsb.setSuccess(ResponseBean.FAIL);
            rsb.setErrormsg("签到失败");
            log.error(e.getMessage(), e);
        }
        return rsb;
    }
}
