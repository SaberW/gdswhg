package com.creatoo.hn.controller.admin.goods;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgGoods;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.goods.WhgGoodsService;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rbg on 2017/8/3.
 */

@SuppressWarnings("ALL")
@Controller
@RequestMapping("/admin/goods")
public class WhgGoodsController extends BaseController {

    @Autowired
    private WhgGoodsService whgGoodsService;

    /**
     * 访问管理页面
     */
    @RequestMapping("/view/{type}")
    @WhgOPT(optType = EnumOptType.GOODS,
            optDesc = {"访问商品列表页", "访问商品添加页", "访问商品编辑页", "查看商品信息"},
            valid = {"type=list", "type=add", "type=edit", "type=show"})
    public String view(@PathVariable String type, ModelMap mmp, HttpSession session, WebRequest request){
        String view = "admin/goods/";
        switch (type){
            case "show" :
            case "edit" :
                String id = request.getParameter("id");
                if (id!=null && !id.isEmpty()){
                    mmp.addAttribute("id", id);
                    mmp.addAttribute("info", this.whgGoodsService.srchOne(id));
                }
            case "add" :
                view+= "view_edit";
                break;
            default: view+="view_list";
        }
        mmp.addAttribute("pageType", type);

        return view;
    }


    /**
     * 列表分页加载
     */
    @RequestMapping("/srchList4p")
    @ResponseBody
    public Object srchList4p(int page, int rows, WhgGoods goods, WebRequest request){
        ResponseBean resb = new ResponseBean();

        String sort = request.getParameter("sort");
        String order = request.getParameter("order");

        try {
            PageInfo pageInfo = this.whgGoodsService.srchlist4p(page,rows,goods,sort,order);
            resb.setRows( (List)pageInfo.getList() );
            resb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resb.setRows(new ArrayList());
            resb.setSuccess(ResponseBean.FAIL);
        }

        return resb;
    }


    /**
     * 添加
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.GOODS, optDesc = "添加商品信息")
    public Object add(HttpSession session, WhgGoods goods){
        ResponseBean resb = new ResponseBean();

        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);

            resb = this.whgGoodsService.t_add(goods, sysUser);
        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("商品信息保存失败");
            log.error(resb.getErrormsg(), e);
        }

        return resb;
    }

    /**
     * 编辑
     */
    @RequestMapping("/edit")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.GOODS, optDesc = "编辑商品信息")
    public Object edit(WhgGoods goods){
        ResponseBean resb = new ResponseBean();
        if (goods == null || goods.getId()==null || goods.getId().isEmpty()){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("商品标识不能为空");
            return resb;
        }

        try {
            if (goods.getEtag()==null) goods.setEtag("");
            if (goods.getEkey()==null) goods.setEkey("");
            resb = this.whgGoodsService.t_edit(goods);
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
    @WhgOPT(optType = EnumOptType.GOODS, optDesc = {"删除商品信息"})
    public Object del(String id){
        ResponseBean resb = new ResponseBean();
        if (id==null || id.isEmpty()){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("商品标识不能为空");
            return resb;
        }

        try {
            resb = this.whgGoodsService.t_del(id);
        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("商品信息删除失败");
            log.error(resb.getErrormsg(), e);
        }

        return resb;
    }

    @RequestMapping("/updstate")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.GOODS,
            optDesc = {"送审","审核","打回","发布","取消发布"},
            valid = {"tostate=9","tostate=2","tostate=1","tostate=6","tostate=4"})
    public Object updstate(String ids, String formstates, int tostate, HttpSession session,
                           @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date optTime) {
        ResponseBean resb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            resb = this.whgGoodsService.t_updstate(ids, formstates, tostate, sysUser, optTime);
        }catch (Exception e){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("商品状态更改失败");
            log.error(resb.getErrormsg(), e);
        }
        return resb;
    }
}
