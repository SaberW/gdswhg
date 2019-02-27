package com.creatoo.hn.controller.admin.yunwei;


import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgYwiKey;
import com.creatoo.hn.dao.model.WhgYwiTag;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiKeyService;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 系统运营的关键字action
 * @author wenjingqiang
 * @version 1-201703
 * Created by Administrator on 2017/3/17.
 */
@RestController
@RequestMapping("/admin/yunwei/key")
public class WhgYunweiKeyController extends BaseController{
    /**
     * log
     */
    Logger log = Logger.getLogger(this.getClass());

    /**
     * service
     */
    @Autowired
    private WhgYunweiKeyService whgYunweiKeyService;

    @Autowired
    private WhgSystemCultService whgSystemCultService;

    /**
     * 进入关键字管理视图
     * type: 页面类型的参数（列表、添加、编辑）
     * classify : 关键字的类型（1、场馆关键字 2、活动关键字 3、活动室关键字 4、培训关键字 5、资讯关键字）
     * @return
     */
    @RequestMapping("/view/{type}/{classify}")
    @WhgOPT(optType = EnumOptType.KEY,
    optDesc = {"访问场馆关键字页面", "访问活动室关键字页面", "访问活动关键字页面", "访问培训关键字页面", "访问资讯关键字页面"}
    ,valid = {"classify=1&&type=list", "classify=2&&type=list", "classify=3&&type=list", "classify=4&&type=list", "classify=5&&type=list"})
    public ModelAndView view(HttpServletRequest request, @PathVariable("type") String type, @PathVariable("classify") String classify){
        ModelAndView view = new ModelAndView("admin/yunwei/key/view_"+type);
        view.addObject("classify", classify);
        return view;
    }



    /**
     *  分页加载关键字管理列表数据
     * @param request
     * @return
     */
    @RequestMapping("/srchList4p")
    public ResponseBean srchList4p(HttpServletRequest request,WhgYwiKey whgYwiKey){
        ResponseBean res = new ResponseBean();
        int page = Integer.parseInt(request.getParameter("page"));
        int rows = Integer.parseInt(request.getParameter("rows"));
        try {
            PageInfo<WhgYwiKey> pageInfo = this.whgYunweiKeyService.t_srchList4p(page,rows,whgYwiKey);
            res.setRows(pageInfo.getList());
            res.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setRows(new ArrayList(0));
            res.setTotal(0);
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 列表查询
     * @return
     */
    @RequestMapping("/srchList")
    public List<WhgYwiKey> srchList(String type, String cultid)throws Exception{
        return this.whgYunweiKeyService.findAllYwiKey(type, cultid);
    }

    /**
     * 添加标签
     * @return
     */
    @RequestMapping("/add")
    @WhgOPT(optType = EnumOptType.KEY, optDesc = {"添加"})
    public ResponseBean add(HttpServletRequest request, WhgYwiKey whgYwiKey){
        ResponseBean res = new ResponseBean();
        WhgSysUser sysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        if(whgYwiKey.getName() == null || "".equals(whgYwiKey.getName())){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("名称不能为空");
            return res;
        }
        try {
            this.whgYunweiKeyService.t_add(whgYwiKey, sysUser);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }

        return res;
    }

    /**
     * 修改标签
     * @return
     */
    @RequestMapping("/edit")
    @WhgOPT(optType = EnumOptType.KEY, optDesc = {"编辑"})
    public ResponseBean edit(HttpServletRequest request,WhgYwiKey whgYwiKey){
        ResponseBean res = new ResponseBean();
        WhgSysUser sysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        if(whgYwiKey.getId() == null || "".equals(whgYwiKey.getId())){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("主键丢失");
            return res;
        }
        try {
            this.whgYunweiKeyService.t_edit(whgYwiKey,sysUser);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 删除标签
     * @return
     */
    @RequestMapping("/del")
    @WhgOPT(optType = EnumOptType.KEY, optDesc = {"删除"})
    public ResponseBean del(HttpServletRequest request){
        ResponseBean res = new ResponseBean();
        String id = request.getParameter("id");
        if(id == null || "".equals(id)){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("主键丢失");
            return res;
        }
        try {
            this.whgYunweiKeyService.t_del(id);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 关键字排序
     */
    @RequestMapping("/sort")
    @WhgOPT(optType = EnumOptType.KEY, optDesc = {"排序"})
    public ResponseBean sort(HttpServletRequest request){
        ResponseBean res = new ResponseBean();
        int idx = Integer.parseInt(request.getParameter("idx"));
        String id = request.getParameter("id");
        if(id == null && "".equals(id)){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("主键丢失");
            return res;
        }
        try {
            this.whgYunweiKeyService.t_sort(idx,id);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }
}
