package com.creatoo.hn.controller.admin.mylive;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgLiveComm;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.mylive.MyLiveService;
import com.creatoo.hn.util.bean.ResponseBean;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用直播管理模块
 */
@RestController
@RequestMapping("/admin/mylive")
public class MyLiveController extends BaseController {
    /**
     * 直播管理服务
     */
    @Autowired
    private MyLiveService myLiveService;

    /**
     * 视图访问控制：管理系统-直播管理-列表页(添加页/编辑页)
     * @param type
     * @return
     */
    @RequestMapping("/view/{type}")
    public ModelAndView view(@PathVariable("type")String type, String reftype, String id){
        ModelAndView view = new ModelAndView();
        try{
            //编辑和查看详情页有ID参数
            if(StringUtils.isNotEmpty(id)){
                WhgLiveComm live = this.myLiveService.t_srchOne(id);
                view.addObject("reftype", live.getReftype()+"");
                view.addObject("live", live);

                //reftitle
                Map<String, String> params = new HashMap<>();
                String reftitle = this.myLiveService.getRefTitle(live, params);
                view.addObject("refname", reftitle);
                view.addAllObjects(params);

                //coursename
                String courseTitle = this.myLiveService.getCourseTitle(live);
                view.addObject("coursename", courseTitle);
            }

            //根据type|reftype定位视图
            String viewName = "admin/mylive/view_"+type;
            if(StringUtils.isNotEmpty(reftype)){
                view.addObject("reftype", reftype);
            }
            view.setViewName(viewName);
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return view;
    }



    /**
     * YI@ 视图访问控制：管理系统-直播管理-列表页(添加页/编辑页) 编辑类型 1-推流地址和播放地址添加方式 2-直接添加播放地址方式 3-添加链接跳转方式
     * @param type
     * @return
     */
    @RequestMapping("/view/{type}/{addType}")
    public ModelAndView view(@PathVariable("type")String type, String reftype, String id,@PathVariable("addType")String addType){
        ModelAndView view = new ModelAndView();
        try{
            //编辑和查看详情页有ID参数
            if(StringUtils.isNotEmpty(id)){
                WhgLiveComm live = this.myLiveService.t_srchOne(id);
                view.addObject("reftype", live.getReftype()+"");
                view.addObject("live", live);

                //reftitle
                Map<String, String> params = new HashMap<>();
                String reftitle = this.myLiveService.getRefTitle(live, params);
                view.addObject("refname", reftitle);
                view.addAllObjects(params);

                //coursename
                String courseTitle = this.myLiveService.getCourseTitle(live);
                view.addObject("coursename", courseTitle);
            }

            //根据type|reftype/addType定位视图
            String viewName = "admin/mylive/view_"+type;
            if(StringUtils.isNotEmpty(reftype)){
                view.addObject("reftype", reftype);
            }
            if(StringUtils.isNotEmpty(addType)){
                view.addObject("addType", addType);
            }
            view.setViewName(viewName);
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return view;
    }

    /**
     * 分页查询直播管理
     * @return
     */
    @RequestMapping(value = "/srchList4p")
    public ResponseBean srchList4p(
            @SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser,
            WhgLiveComm live, Integer page, Integer rows, String sort, String order){
        ResponseBean res = new ResponseBean();
        try {
            //区域管理员-查询区域下文化馆所有部门的数据
            //站点管理员-查询站点下指定部门的数据

            PageInfo<WhgLiveComm> pageInfo = this.myLiveService.t_srchList4p(sysUser, live, page, rows, sort, order);
            res.setRows(this.myLiveService.changeListToMap(pageInfo.getList()));
            res.setTotal(pageInfo.getTotal());
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 添加
     * @param sysUser
     * @param live
     * @return
     */
    @RequestMapping(value = "/add")
    public ResponseBean add(
            @SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser,
            WhgLiveComm live){//
        ResponseBean rb = new ResponseBean();
        try{
            this.myLiveService.add(sysUser, live);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return rb;
    }

    /**
     * 编辑
     * @param sysUser
     * @param live
     * @return
     */
    @RequestMapping(value = "/edit")
    public ResponseBean edit(
            @SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser,
            WhgLiveComm live, String editVod){
        ResponseBean rb = new ResponseBean();
        try{
            this.myLiveService.edit(sysUser, live, editVod);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return rb;
    }

    /**
     * 启用停用
     * @param sysUser
     * @param id
     * @param state
     * @return
     */
    @RequestMapping(value = "/updateState")
    public ResponseBean updateState(
            @SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser,
            String id, String state){
        ResponseBean rb = new ResponseBean();
        try{
            this.myLiveService.updateState(sysUser, id, state);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return rb;
    }

    /**
     * 删除
     * @param sysUser
     * @param id
     * @return
     */
    @RequestMapping(value = "/del")
    public ResponseBean delete(
            @SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser,
            String id){
        ResponseBean rb = new ResponseBean();
        try{
            this.myLiveService.delete(sysUser, id);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return rb;
    }
}
