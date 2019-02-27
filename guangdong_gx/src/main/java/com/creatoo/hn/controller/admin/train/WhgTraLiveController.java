package com.creatoo.hn.controller.admin.train;

import com.creatoo.hn.Constant;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgTra;
import com.creatoo.hn.dao.model.WhgTraLive;
import com.creatoo.hn.services.admin.train.WhgTraInMajorService;
import com.creatoo.hn.services.admin.train.WhgTraLiveService;
import com.creatoo.hn.services.admin.train.WhgTrainCourseService;
import com.creatoo.hn.services.admin.train.WhgTrainService;
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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/27.
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping("admin/traLive")
public class WhgTraLiveController {

    @Autowired
    private WhgTraLiveService whgTraLiveService;

    @Autowired
    private WhgTraInMajorService whgTraInMajorService;

    @Autowired
    private WhgTrainService whgTrainService;

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
    @WhgOPT(optType = EnumOptType.TRALIVE, optDesc = {"进入直播列表页"})
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
                    String age1 = this.whgTraLiveService.srchOne(id).getAge();
                    if(!"".equals(age1) && age1 != null){
                        age = age1.split(",");
                        mmp.addAttribute("age1",age[0]);
                        mmp.addAttribute("age2",age[1]);
                    }
                    mmp.addAttribute("whgTra",this.whgTraLiveService.srchOne(id));
                    mmp.addAttribute("majorid",this.whgTraInMajorService.t_selMajorByTraid(id,1));
                    mmp.addAttribute("state",this.whgTraLiveService.srchOne(id).getState());
                    mmp.addAttribute("course",this.whgTrainCourseService.srchList(id));
                    view.setViewName("admin/train/tralive/view_edit");
                }else{
                    if(mid != null && !"".equals(mid)){
                        mmp.addAttribute("mid",mid);
                    }
                    view.setViewName("admin/train/tralive/view_add");
                }

            }else if("syspublish".equalsIgnoreCase(type)){
                view.setViewName("admin/train/tralive/sys_view_list");
            }else{
                view.setViewName("admin/train/tralive/view_list");
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
    @ResponseBody
    public ResponseBean srchList4p(HttpServletRequest request, String sort, String order,HttpSession session){
        ResponseBean resb = new ResponseBean();
        WhgTra whgTra = new WhgTra();
        try {
            int page = Integer.parseInt((String)request.getParameter("page"));
            int rows = Integer.parseInt((String)request.getParameter("rows"));
            String title = request.getParameter("title");
            String pageType = request.getParameter("type");
            WhgSysUser user = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);

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
            PageInfo pageInfo = this.whgTraLiveService.t_srchList4p(page,rows,pageType,whgTra,sort,order,user.getId());
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
     * 列表查询
     * @return
     */
    @RequestMapping("/srchList")
    public ResponseBean srchList(){return new ResponseBean();}

    /**
     * 添加培训
     * @return
     */
    @RequestMapping("/add")
    @WhgOPT(optType = EnumOptType.TRALIVE, optDesc = {"添加培训直播"})
    @ResponseBody
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

            res = this.whgTraLiveService.t_add(tra,user,_age1,_age2,sin_starttime,sin_endtime,_starttime,_endtime,fixedweek,fixedstarttime,fixedendtime,mid,majorid);
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
    @ResponseBody
    @WhgOPT(optType = EnumOptType.TRALIVE, optDesc = {"修改培训直播"})
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
            res = this.whgTraLiveService.t_edit(tra, sysUser,_age1,_age2,sin_starttime,sin_endtime,_starttime,_endtime,fixedweek,fixedstarttime,fixedendtime,starttime,endtime,majorid);
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
    @ResponseBody
    @WhgOPT(optType = EnumOptType.TRALIVE, optDesc = {"删除培训直播"})
    public ResponseBean del(String id, HttpSession session) {
        ResponseBean res = new ResponseBean();
        try {
            this.whgTraLiveService.t_del(id);

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
    @ResponseBody
    @WhgOPT(optType = EnumOptType.TRALIVE, optDesc = {"还原培训直播"})
    public Object undel(String id, HttpSession session){
        ResponseBean rb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            this.whgTraLiveService.t_undel(id, sysUser);
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
    @ResponseBody
    @WhgOPT(optType = EnumOptType.TRALIVE, optDesc = {"送审","审核","打回","发布","取消发布"}, valid = {"tostate=9","tostate=2","tostate=1","tostate=6","tostate=4"})
    public ResponseBean updstate(String ids, String formstates, int tostate, HttpSession session,
                                 @RequestParam(value = "statemdfdate", required = false)String statemdfdate,
                                 @RequestParam(value = "reason", required = false)String reason,
                                 @RequestParam(value = "issms", required = false, defaultValue = "0")int issms){
        ResponseBean res = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            res = this.whgTraLiveService.t_updstate(statemdfdate, ids, formstates, tostate, sysUser, reason, issms);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("培训状态更改失败");
            log.error(res.getErrormsg()+" formstate: "+formstates+" tostate:"+tostate+" ids: "+ids, e);
        }
        return res;
    }




    /**
     *  分页查询培训列表数据
     * @return
     */
    @RequestMapping("/srchTraList")
    @ResponseBody
    public ResponseBean srchTraList(int page,int rows){
        ResponseBean resb = new ResponseBean();
        try {
            PageInfo pageInfo = this.whgTraLiveService.t_srchTraList(page,rows);
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
     * 查询课程推流地址
     * @return
     */
    @RequestMapping("/srchTraLive")
    @ResponseBody
    public ResponseBean srchTraLive(HttpServletRequest request){
        ResponseBean rsb = new ResponseBean();
        String traid = request.getParameter("traid");
        String courseid = request.getParameter("courseid");
        try {
            rsb = this.whgTraLiveService.t_srchTraLive(traid,courseid);
        } catch (Exception e) {
            rsb.setSuccess(ResponseBean.FAIL);
            rsb.setErrormsg("上首页失败！");
            log.error(rsb.getErrormsg(), e);
        }
        return rsb;
    }

    /**
     * 添加培训直播
     * @return
     */
    @RequestMapping("/addLive")
    @WhgOPT(optType = EnumOptType.TRALIVE, optDesc = {"添加培训直播"})
    @ResponseBody
    public ResponseBean addLive(WhgTraLive traLive, HttpServletRequest request, HttpSession session) {
        ResponseBean res = new ResponseBean();
        try {
            String traid = request.getParameter("traid");
            String courseid = request.getParameter("courseid");
            if(traid != null && !"".equals(traid)){
                traLive.setTraid(traid);
            }
            if(courseid != null && !"".equals(courseid)){
                traLive.setCourseid(courseid);
            }
            this.whgTraLiveService.t_addLive(traLive);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("培训直播保存失败");
            log.error(res.getErrormsg(), e);
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
    @WhgOPT(optType = EnumOptType.TRALIVE, optDesc = {"推荐","取消推荐"}, valid = {"recommend=1","recommend=0"})
    public ResponseBean updrecommend(String ids, String formrecoms, int torecom){
        ResponseBean res = new ResponseBean();
        try {
            res = this.whgTraLiveService.t_updrecommend(ids,formrecoms,torecom);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("推荐失败！");
            log.error(res.getErrormsg()+" formrecoms: "+formrecoms+" torecom:"+torecom+" ids: "+ids, e);
        }
        return res;
    }

    @RequestMapping("setToprovince")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.TRALIVE, optDesc = {"省级推荐","取消省级推荐"},valid = {"toprovince=1","toprovince=0"})
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
    @WhgOPT(optType = EnumOptType.TRALIVE, optDesc = {"市级推荐","取消市级推荐"},valid = {"tocity=1","tocity=0"})
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
}
