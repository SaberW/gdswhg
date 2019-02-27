package com.creatoo.hn.controller.admin.yunwei;


import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgYwiLbt;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiLbtService;
import com.creatoo.hn.util.ReqParamsUtil;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.github.pagehelper.PageInfo;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 系统管理的轮播图配置action
 *
 * @author luzhihuai
 * @version 1-201703
 *          Created by Administrator on 2017/3/20.
 */
@RestController
@RequestMapping("/admin/yunwei/lbt")
public class WhgSystemLbtController extends BaseController{
    /**
     * log
     */
    Logger log = Logger.getLogger(this.getClass());

    /**
     * 轮播图service
     */
    @Autowired
    private WhgYunweiLbtService whgYunweiLbtService;

    /**
     * 进入type(list|add|edit|view)视图
     *
     * @param request 请求对象
     * @param type    视图类型(list|add|edit|view)
     * @return 视图
     */
    @RequestMapping("/view/{type}/{clazz}")
    @WhgOPT(optType = EnumOptType.LBT
    ,optDesc = {"访问PC首页轮播图配置", "访问APP首页轮播图配置", "访问PC首页广告图配置", "访问APP首页广告图图配置", "访问PC培训首页轮播图配置", "访问PC志愿服务首页风采展示", "访问PC非遗首页广告图配置", "访问PC志愿服务首页广告图配置", "访问PC非遗首页轮播图配置"}
    ,valid = {"clazz=1&&type=list", "clazz=2&&type=list", "clazz=3&&type=list", "clazz=4&&type=list", "clazz=5&&type=list", "clazz=6&&type=list", "clazz=7&&type=list", "clazz=8&&type=list", "clazz=9&&type=list"})
    public ModelAndView listview(HttpServletRequest request, @PathVariable("type") String type, @PathVariable("clazz") String clazz) {
        ModelAndView view = new ModelAndView("admin/yunwei/lbt/view_" + type);

        String id = request.getParameter("id");
        view.addObject("id", id);
        try {
            view.addObject("lbt",whgYunweiLbtService.t_srchOne(id));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        view.addObject("clazz", clazz);

        return view;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return res
     */
    @RequestMapping(value = "/srchList4p")
    public ResponseBean srchList4p(HttpServletRequest request) {
        ResponseBean res = new ResponseBean();
        WhgSysUser user = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        try {
            Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
            //分页信息
            int page = Integer.parseInt((String) paramMap.get("page"));
            int rows = Integer.parseInt((String) paramMap.get("rows"));
            String name = (String) paramMap.get("name");
            PageInfo<WhgYwiLbt> pageInfo = whgYunweiLbtService.t_srchList4p(page,rows,name,user.getCultid());
            res.setRows(pageInfo.getList());
            res.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 区域管理员查询轮播图配置
     * @return
     */
    @RequestMapping("/srchList4p4area")
    public ResponseBean srchList4p4area(
            @SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser,
            @RequestParam(value="page", defaultValue="1") Integer page,
            @RequestParam(value="rows", defaultValue="10") Integer rows,
            @RequestParam(value="province", required=false) String province,
            @RequestParam(value="city", required=false) String city,
            @RequestParam(value="state", required=false) Integer state,
            @RequestParam(value="type") Integer type
            ){
        ResponseBean res = new ResponseBean();
        try {
            if(StringUtils.isEmpty(province)){
                province = null;
            }
            if(StringUtils.isEmpty(city)){
                city = null;
            }
            PageInfo<WhgYwiLbt> pageInfo = whgYunweiLbtService.t_srchList4p4area(page, rows, province, city, state, type);
            res.setRows(pageInfo.getList());
            res.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 区域管理员查询轮播图配置
     * @return
     */
    @RequestMapping("/srchList4p4cult")
    public ResponseBean srchList4p4cult(
            @SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser,
            @RequestParam(value="page", defaultValue="1") Integer page,
            @RequestParam(value="rows", defaultValue="10") Integer rows,
            @RequestParam(value="province", required=false) String province,
            @RequestParam(value="city", required=false) String city,
            @RequestParam(value="state", required=false) Integer state,
            @RequestParam(value="type") Integer type,
            @RequestParam(value="name", required=false) String name
    ){
        ResponseBean res = new ResponseBean();
        try {
            if(StringUtils.isEmpty(province)){
                province = null;
            }
            if(StringUtils.isEmpty(city)){
                city = null;
            }
            PageInfo<WhgYwiLbt> pageInfo = whgYunweiLbtService.t_srchList4p4cult(page, rows, province, city, type, name);
            res.setRows(pageInfo.getList());
            res.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }


    /**
     * 查询列表
     *
     * @return 。
     */
    @RequestMapping(value = "/srchList")
    public ResponseBean srchList(WhgYwiLbt whgYwiLbt,HttpServletRequest request) {
        ResponseBean res = new ResponseBean();
        WhgSysUser user = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        try {
            Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
            //分页信息
            int page = Integer.parseInt((String) paramMap.get("page"));
            int rows = Integer.parseInt((String) paramMap.get("rows"));
            PageInfo<WhgYwiLbt> list = this.whgYunweiLbtService.t_srchList(whgYwiLbt,page,rows,null);
            res.setRows(list.getList());
            res.setTotal(list.getTotal());
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 查询详情
     * @param id
     * @return
     */
    @RequestMapping(value = "/srchOne")
    public ResponseBean srchOne(String id){
        ResponseBean res = new ResponseBean();
        try {
            WhgYwiLbt whgYwiLbt = this.whgYunweiLbtService.t_srchOne(id);
            res.setData(whgYwiLbt);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 添加
     *
     * @param request
     * @param whgYwiLbt 实体
     * @return 对象
     */
    @RequestMapping(value = "/add")
    public ResponseBean add(HttpServletRequest request, WhgYwiLbt whgYwiLbt) {
        ResponseBean res = new ResponseBean();
        try {
            WhgSysUser user = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
            this.whgYunweiLbtService.t_add(user, whgYwiLbt);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 编辑
     *
     * @param request
     * @param whgYwiLbt
     * @return
     */
    @RequestMapping(value = "/edit")
    public ResponseBean edit(HttpServletRequest request, WhgYwiLbt whgYwiLbt) {
        ResponseBean res = new ResponseBean();
        try {
            Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
            WhgSysUser user = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
            String id = (String) paramMap.get("id");
            whgYunweiLbtService.t_edit(id, user, whgYwiLbt);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 修改状态
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/updstate")
    public ResponseBean updstate(HttpServletRequest request, int state) {
        ResponseBean res = new ResponseBean();
        try {
            Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
            String id = (String) paramMap.get("id");
            this.whgYunweiLbtService.t_updstate(id, state);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 删除标签
     *
     * @return
     */
    @RequestMapping("/del")
    public ResponseBean del(HttpServletRequest request) {
        ResponseBean res = new ResponseBean();
        try {
            Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
            String id = (String) paramMap.get("id");
            this.whgYunweiLbtService.t_del(id);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }

    @RequestMapping("/move")
    public ResponseBean move(String id, String type, String dataScope, String clazz) {
        ResponseBean res = new ResponseBean();
        try {
            this.whgYunweiLbtService.t_move(id, type, dataScope, clazz);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 推荐到省或者市
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/toarea")
    public ResponseBean toArea(HttpServletRequest request) {
        ResponseBean res = new ResponseBean();
        try {
            String id = request.getParameter("id");
            int type = Integer.parseInt(request.getParameter("type"));
            //String cultid = request.getParameter("cultid");
            res = this.whgYunweiLbtService.t_ToRecommend(id,type);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

}
