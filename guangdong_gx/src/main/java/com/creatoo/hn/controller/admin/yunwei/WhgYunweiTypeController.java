package com.creatoo.hn.controller.admin.yunwei;



import com.creatoo.hn.Constant;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgYwiType;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiTypeService;
import com.creatoo.hn.util.IDUtils;
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
 * 系统运营的分类action
 * @author wenjingqiang
 * @version 1-201703
 */
@RestController
@RequestMapping("/admin/yunwei/type")
public class WhgYunweiTypeController {
    /**
     * log
     */
    Logger log = Logger.getLogger(this.getClass());

    /**
     * services
     */
    @Autowired
    private WhgYunweiTypeService whgYunweiTypeService;

    @Autowired
    private WhgSystemCultService whgSystemCultService;

    /**
     * 进入分类管理视图
     * type: 页面类型的参数（列表、添加、编辑）
     * classify : 分类的类型（1、区域）
     * @return
     */
    @RequestMapping("/view/{type}/{classify}")
    @WhgOPT(optType = EnumOptType.TYPE
            ,optDesc = {"访问区域配置页面"}
            ,valid = {"classify=1&&type=list"})
    public ModelAndView view(HttpServletRequest request, @PathVariable("type") String type, @PathVariable("classify") String classify){
        ModelAndView view = new ModelAndView("admin/yunwei/type/view_"+type);
        view.addObject("classify", classify);
        return view;
    }



    /**
     *  分页加载分类管理列表数据
     * @param request
     * @return
     */
    @RequestMapping("/srchList4p")
    public ResponseBean srchList4p(HttpServletRequest request,WhgYwiType whgYwiType){
        ResponseBean res = new ResponseBean();
        int page = Integer.parseInt((String) request.getParameter("page"));
        int rows = Integer.parseInt((String) request.getParameter("rows"));
        try {
            PageInfo<WhgYwiType> pageInfo = this.whgYunweiTypeService.t_srchList4p(page,rows,whgYwiType);
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
    public List<WhgYwiType> srchList(String type, String cultid)throws Exception{
        List<WhgYwiType> list=null;
        list=this.whgYunweiTypeService.findAllYwiType(type, cultid);
        return list;
    }

    /**
     * 添加分类
     * @return
     */
    @RequestMapping("/add")
    @WhgOPT(optType = EnumOptType.TYPE, optDesc = {"添加"})
    public ResponseBean add(HttpServletRequest request, WhgYwiType whgYwiType){
        ResponseBean res = new ResponseBean();
        if(whgYwiType.getName() == null && "".equals(whgYwiType.getName())){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("名称不能为空");
            return res;
        }
        try {
            if(whgYwiType.getPid()!=null&&!whgYwiType.getPid().equals("")){
                whgYwiType.setId(whgYwiType.getPid()+ IDUtils.getID());//组合子分类：包含父分类
            }
            this.whgYunweiTypeService.t_add((WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY),whgYwiType);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 修改分类
     * @return
     */
    @RequestMapping("/edit")
    @WhgOPT(optType = EnumOptType.TYPE, optDesc = {"修改"})
    public ResponseBean edit(HttpServletRequest request,WhgYwiType whgYwiType){
        ResponseBean res = new ResponseBean();
        try {
            this.whgYunweiTypeService.t_edit(whgYwiType,(WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY));
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/del")
    @WhgOPT(optType = EnumOptType.TYPE, optDesc = {"删除"})
    public ResponseBean del(HttpServletRequest request){
        ResponseBean res = new ResponseBean();
        String id = request.getParameter("id");
        if(id == null && "".equals(id)){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("主键丢失");
            return res;
        }
        try {
            this.whgYunweiTypeService.t_del(id);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 修改type 开关状态
     * @return
     */
    @RequestMapping("/updateOpt")
    @WhgOPT(optType = EnumOptType.TYPE, optDesc = {"启用", "停用"}, valid = {"type=1", "type=0"})
    public ResponseBean updateOpt(HttpServletRequest request, String id, String cultid,String type){
        ResponseBean res = new ResponseBean();
        if(id == null && "".equals(id)){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("主键丢失");
            return res;
        }
        try {
            this.whgYunweiTypeService.t_updateOpt(id,cultid,type);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }

}
