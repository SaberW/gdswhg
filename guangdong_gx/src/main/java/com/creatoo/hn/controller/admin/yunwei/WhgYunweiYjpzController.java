package com.creatoo.hn.controller.admin.yunwei;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgYwiHardware;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiYjpzservice;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 硬件配置控制器
 * Created by Administrator on 2017/11/14.
 */
@RestController
@RequestMapping("/admin/yunwei/yjpz")
public class WhgYunweiYjpzController extends BaseController{

    /**
     * service
     */
    @Autowired
    private WhgYunweiYjpzservice whgYunweiYjpzservice;


    /**
     * 进入硬件配置管理视图
     * type: 页面类型的参数（列表、添加、编辑）
     * classify : 硬件配置的类型（）
     * @return
     */
    @RequestMapping("/view/{type}")
    @WhgOPT(optType = EnumOptType.KEY, optDesc = {"访问硬件配置页面"})
    public ModelAndView view(HttpServletRequest request){
        ModelAndView view = new ModelAndView("admin/yunwei/yjpz/view_list");
        Integer _type = Integer.parseInt(request.getParameter("type"));
        String entid = request.getParameter("entid");
        view.addObject("type",_type);
        view.addObject("entid",entid);
        return view;
    }


    /**
     *  分页加载硬件配置管理列表数据
     * @param request
     * @return
     */
    @RequestMapping("/srchList4p")
    public ResponseBean srchList4p(HttpServletRequest request, WhgYwiHardware yjpz){
        ResponseBean res = new ResponseBean();
        int page = Integer.parseInt(request.getParameter("page"));
        int rows = Integer.parseInt(request.getParameter("rows"));
        try {
            PageInfo<WhgYwiHardware> pageInfo = this.whgYunweiYjpzservice.t_srchList4p(page,rows,yjpz);
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
    public List<WhgYwiHardware> srchList(String type, String cultid){
        List<WhgYwiHardware> list = new ArrayList<>();
        try {
            list = this.whgYunweiYjpzservice.findAllYjpz(type, cultid);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ArrayList();
        }
        return list;
    }

    /**
     * 添加硬件配置
     * @return
     */
    @RequestMapping("/add")
    @WhgOPT(optType = EnumOptType.KEY, optDesc = {"添加"})
    public ResponseBean add(HttpServletRequest request, WhgYwiHardware yjpz){
        ResponseBean res = new ResponseBean();
        WhgSysUser sysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        if(yjpz.getName() == null || "".equals(yjpz.getName())){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("名称不能为空");
            return res;
        }
        try {
            this.whgYunweiYjpzservice.t_add(yjpz, sysUser);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }

        return res;
    }

    /**
     * 修改硬件配置
     * @return
     */
    @RequestMapping("/edit")
    @WhgOPT(optType = EnumOptType.KEY, optDesc = {"编辑"})
    public ResponseBean edit(HttpServletRequest request,WhgYwiHardware yjpz){
        ResponseBean res = new ResponseBean();
        WhgSysUser sysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        if(yjpz.getId() == null || "".equals(yjpz.getId())){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("主键丢失");
            return res;
        }
        try {
            this.whgYunweiYjpzservice.t_edit(yjpz,sysUser);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 删除硬件配置
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
            this.whgYunweiYjpzservice.t_del(id);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }


    /**
     * 列表查询
     * @return
     */
    @RequestMapping("/srchListByExhId")
    public List<WhgYwiHardware> srchListByExhId(String ExhId){
        List<WhgYwiHardware> list = new ArrayList<>();
        try {
            list = this.whgYunweiYjpzservice.findHardwareByExhId(ExhId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ArrayList();
        }
        return list;
    }

}
