package com.creatoo.hn.controller.admin.goods;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgShowPlaybill;
import com.creatoo.hn.dao.model.WhgSupplyTraperson;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.goods.WhgShowJmdService;
import com.creatoo.hn.services.admin.supply.WhgSupplyTraPerService;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/23.
 */
@RestController
@RequestMapping("/admin/show/jmd")
public class WhgShowJmdController extends BaseController{
    /**
     * service
     */
    @Autowired
    private WhgShowJmdService whgShowJmdService;


    /**
     * 进入节目单管理视图
     * type: 页面类型的参数（列表、添加、编辑）
     * @return
     */
    @RequestMapping("/view/{type}")
    public ModelAndView view(HttpServletRequest request, @PathVariable("type") String type){
        ModelAndView view = new ModelAndView();
        String entid = request.getParameter("entid");
        String id = request.getParameter("id");
        String targetShow = request.getParameter("targetShow");
        view.addObject("entid",entid);
        view.addObject("id",id);
        view.addObject("targetShow",targetShow);
        if("edit".equals(type)){
            if(id != null && !"".equals(id)){
                try {
                    WhgShowPlaybill play = whgShowJmdService.t_srchOne(id);
                    view.addObject("play",play);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
            view.setViewName("admin/goods/playbill/view_edit");
        }else {
            view.setViewName("admin/goods/playbill/view_list");
        }
        return view;
    }


    /**
     *  分页加载节目单管理列表数据
     * @param request
     * @return
     */
    @RequestMapping("/srchList4p")
    public ResponseBean srchList4p(HttpServletRequest request, WhgShowPlaybill play){
        ResponseBean res = new ResponseBean();
        int page = Integer.parseInt(request.getParameter("page"));
        int rows = Integer.parseInt(request.getParameter("rows"));
        try {
            PageInfo<WhgShowPlaybill> pageInfo = this.whgShowJmdService.t_srchList4p(page,rows,play);
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
     * 添加节目单
     * @return
     */
    @RequestMapping("/add")
    @WhgOPT(optType = EnumOptType.KEY, optDesc = {"添加"})
    public ResponseBean add(HttpServletRequest request, WhgShowPlaybill play){
        ResponseBean res = new ResponseBean();
        WhgSysUser sysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);

        try {
            this.whgShowJmdService.t_add(play, sysUser);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }

        return res;
    }

    /**
     * 修改节目单
     * @return
     */
    @RequestMapping("/edit")
    @WhgOPT(optType = EnumOptType.KEY, optDesc = {"编辑"})
    public ResponseBean edit(HttpServletRequest request,WhgShowPlaybill play){
        ResponseBean res = new ResponseBean();
        WhgSysUser sysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        if(play.getId() == null || "".equals(play.getId())){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("主键丢失");
            return res;
        }
        try {
            this.whgShowJmdService.t_edit(play,sysUser);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 删除节目单
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
            this.whgShowJmdService.t_del(id);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }


}
