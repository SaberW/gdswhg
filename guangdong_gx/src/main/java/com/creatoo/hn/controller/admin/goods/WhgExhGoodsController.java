package com.creatoo.hn.controller.admin.goods;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgExhGoods;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.goods.WhgExhGoodsService;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumStateDel;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 展品controller
 * Created by Administrator on 2017/9/1.
 */
@Controller
@RequestMapping("/admin/exhGoods")
public class WhgExhGoodsController extends BaseController{

    @Autowired
    private WhgExhGoodsService whgExhGoodsService;

    @RequestMapping("/view/{type}")
    public ModelAndView view(@PathVariable("type") String type, ModelMap mmp, HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        //String pageType = request.getParameter("pageType");
        mmp.addAttribute("pageType", type);
        if ("add".equalsIgnoreCase(type)){
            view.setViewName("admin/goods/exhGoods/view_edit");
        }else if("edit".equalsIgnoreCase(type) || "show".equalsIgnoreCase(type)){
            /*if ("show".equalsIgnoreCase(type)){
                mmp.addAttribute("pageType", "2");
            }else{
                mmp.addAttribute("pageType", "1");
            }*/
            String id = request.getParameter("id");
            if(id != null && !"".equals(id)){
                mmp.addAttribute("id", id);
                mmp.addAttribute("info", this.whgExhGoodsService.srchOne(id));
            }
            view.setViewName("admin/goods/exhGoods/view_edit");
        }else{
            view.setViewName("admin/goods/exhGoods/view_list");
        }

        return view;
    }

    /**
     * 分页查询展品列表
     * @param page
     * @param rows
     * @param exhGoods
     * @param request
     * @return
     */
    @RequestMapping("/srchList4p")
    @ResponseBody
    public ResponseBean srchList4p(int page, int rows, WhgExhGoods exhGoods,HttpSession session, WebRequest request){
        ResponseBean rsb = new ResponseBean();
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");
        String pageType = request.getParameter("pageType");
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            List states = null;
            if ("listedit".equalsIgnoreCase(pageType)){
                exhGoods.setCrtuser(sysUser.getId());
            }
            if ("listcheck".equalsIgnoreCase(pageType) || "listpublish".equalsIgnoreCase(pageType)){
                states = Arrays.asList(9,2,6,4);
            }

            if ("listdel".equalsIgnoreCase(pageType)){
                exhGoods.setDelstate(EnumStateDel.STATE_DEL_YES.getValue());
            }else{
                exhGoods.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
            }

            PageInfo pageInfo = this.whgExhGoodsService.t_srchlist4p(page,rows,sysUser.getId(),exhGoods, states, sort,order);
            rsb.setRows(pageInfo.getList() );
            rsb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rsb.setRows(new ArrayList());
            rsb.setSuccess(ResponseBean.FAIL);
        }
        return rsb;
    }

    /**
     * 查询展品列表
     * @param request
     * @return
     */
    @RequestMapping("/srchList")
    @ResponseBody
    public Object srchList(HttpServletRequest request,HttpSession session){
        List<WhgExhGoods> list = new ArrayList<>();
        WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
        String cultid = request.getParameter("cultid");
        try {
            list = this.whgExhGoodsService.t_srchList(cultid,sysUser);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
           return new ArrayList();
        }
        return list;
    }

    /**
     * 添加展品
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public Object add(HttpSession session, WhgExhGoods exhGoods){
        ResponseBean resb = new ResponseBean();

        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            this.whgExhGoodsService.t_add(exhGoods, sysUser);
        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("信息保存失败");
            log.error(resb.getErrormsg(), e);
        }
        return resb;
    }

    /**
     * 编辑
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(WhgExhGoods exhGoods){
        ResponseBean resb = new ResponseBean();
        if (exhGoods == null || exhGoods.getId()==null || exhGoods.getId().isEmpty()){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("信息标识不能为空");
            return resb;
        }

        try {
            this.whgExhGoodsService.t_edit(exhGoods);
        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("信息保存失败");
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
            resb.setErrormsg("信息标识不能为空");
            return resb;
        }
        try {
            this.whgExhGoodsService.t_del(id);
        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("信息删除失败");
            log.error(resb.getErrormsg(), e);
        }

        return resb;
    }

    @RequestMapping("/recovery")
    @ResponseBody
    public Object recovery(String id, HttpSession session) {
        ResponseBean rb = new ResponseBean();

        try {
            WhgExhGoods info = new WhgExhGoods();
            info.setId(id);
            info.setDelstate(EnumStateDel.STATE_DEL_YES.getValue());

            this.whgExhGoodsService.t_edit(info);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("操作失败");
            log.error(e.getMessage(), e);
        }

        return rb;
    }

    @RequestMapping("/undel")
    @ResponseBody
    public Object undel(String id){
        ResponseBean rb = new ResponseBean();

        try {
            WhgExhGoods info = new WhgExhGoods();
            info.setId(id);
            info.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
            info.setState(EnumBizState.STATE_CAN_EDIT.getValue());

            this.whgExhGoodsService.t_edit(info);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("操作失败");
            log.error(e.getMessage(), e);
        }

        return rb;
    }

    @RequestMapping("/updstate")
    @ResponseBody
    public Object updstate(String ids, String formstates, int tostate, HttpSession session, @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date optTime) {
        ResponseBean resb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            resb = this.whgExhGoodsService.t_updstate(ids, formstates, tostate, sysUser,optTime);
        }catch (Exception e){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("商品状态更改失败");
            log.error(resb.getErrormsg(), e);
        }
        return resb;
    }

}
