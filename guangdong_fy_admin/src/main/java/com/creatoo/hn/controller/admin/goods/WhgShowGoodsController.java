package com.creatoo.hn.controller.admin.goods;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgGoods;
import com.creatoo.hn.dao.model.WhgShowGoods;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.goods.WhgShowGoodsService;
import com.creatoo.hn.util.bean.ResponseBean;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 展演类商品管理action
 * Created by Administrator on 2017/8/25.
 */
@Controller
@RequestMapping("/admin/showGoods")
public class WhgShowGoodsController extends BaseController{

    @Autowired
    private WhgShowGoodsService whgShowGoodsService;

    @RequestMapping("/view/{type}")
    public ModelAndView view(@PathVariable("type") String type, ModelMap mmp, HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        String pageType = request.getParameter("pageType");
        if("edit".equalsIgnoreCase(type)){
            String id = request.getParameter("id");
            if(id != null && !"".equals(id)){
                mmp.addAttribute("id", id);
                mmp.addAttribute("info", this.whgShowGoodsService.srchOne(id));
            }
            view.setViewName("admin/goods/show/view_edit");
        }else{
            view.setViewName("admin/goods/show/view_list");
        }
        mmp.addAttribute("pageType", pageType);
        return view;
    }

    /**
     * 分页查询展演类商品列表
     * @param page
     * @param rows
     * @param show
     * @param request
     * @return
     */
    @RequestMapping("/srchList4p")
    @ResponseBody
    public ResponseBean srchList4p(int page, int rows, WhgShowGoods show, WebRequest request){
        ResponseBean rsb = new ResponseBean();
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");

        try {
            PageInfo pageInfo = this.whgShowGoodsService.t_srchlist4p(page,rows,show,sort,order);
            rsb.setRows( (List)pageInfo.getList() );
            rsb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rsb.setRows(new ArrayList());
            rsb.setSuccess(ResponseBean.FAIL);
        }
        return rsb;
    }

    /**
     * 添加展演类商品
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public Object add(HttpSession session, WhgShowGoods show){
        ResponseBean resb = new ResponseBean();

        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            resb = this.whgShowGoodsService.t_add(show, sysUser);
        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("展演商品信息保存失败");
            log.error(resb.getErrormsg(), e);
        }
        return resb;
    }

    /**
     * 编辑
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(WhgShowGoods show){
        ResponseBean resb = new ResponseBean();
        if (show == null || show.getId()==null || show.getId().isEmpty()){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("商品标识不能为空");
            return resb;
        }

        try {
            resb = this.whgShowGoodsService.t_edit(show);
        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("商品信息保存失败");
            log.error(resb.getErrormsg(), e);
        }

        return resb;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public Object del(String id){
        ResponseBean resb = new ResponseBean();
        if (id==null || id.isEmpty()){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("商品标识不能为空");
            return resb;
        }
        try {
            resb = this.whgShowGoodsService.t_del(id);
        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("商品信息删除失败");
            log.error(resb.getErrormsg(), e);
        }

        return resb;
    }

    @RequestMapping("/updstate")
    @ResponseBody
    public Object updstate(String ids, String formstates, int tostate, HttpSession session, @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date optTime) {
        ResponseBean resb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            resb = this.whgShowGoodsService.t_updstate(ids, formstates, tostate, sysUser,optTime);
        }catch (Exception e){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("商品状态更改失败");
            log.error(resb.getErrormsg(), e);
        }
        return resb;
    }

}
