package com.creatoo.hn.controller.admin.goods;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgShowGoods;
import com.creatoo.hn.dao.model.WhgShowOrgan;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.goods.WhgShowOrganService;
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
 * Created by Administrator on 2017/8/31.
 */
@Controller
@RequestMapping("/admin/showOrgan")
public class WhgShowOrganController extends BaseController{

    @Autowired
    private WhgShowOrganService whgShowOrganService;


    @RequestMapping("/view/{type}")
    public ModelAndView view(@PathVariable("type") String type, ModelMap mmp, HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        String pageType = request.getParameter("pageType");
        if("edit".equalsIgnoreCase(type)){
            String id = request.getParameter("id");
            if(id != null && !"".equals(id)){
                mmp.addAttribute("id", id);
                mmp.addAttribute("info", this.whgShowOrganService.srchOne(id));
            }
            view.setViewName("admin/goods/organ/view_edit");
        }else{
            view.setViewName("admin/goods/organ/view_list");
        }
        mmp.addAttribute("pageType", pageType);
        return view;
    }

    /**
     * 分页查询展演类组织机构列表
     * @param page
     * @param rows
     * @param organ
     * @param request
     * @return
     */
    @RequestMapping("/srchList4p")
    @ResponseBody
    public ResponseBean srchList4p(int page, int rows, WhgShowOrgan organ, WebRequest request){
        ResponseBean rsb = new ResponseBean();
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");

        try {
            PageInfo pageInfo = this.whgShowOrganService.t_srchlist4p(page,rows,organ,sort,order);
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
     * 查询权限分馆下的组织机构列表
     * @param  session
     * @return
     */
    @RequestMapping("/srchList")
    @ResponseBody
    public Object srchList(HttpServletRequest request){
        List<WhgShowOrgan> list = new ArrayList<>();
//        JSONArray cults = (JSONArray) session.getAttribute(Constant.SESSION_ADMIN_CULT);
//
//        List cultids = new ArrayList();
//        for(Object obj : cults){
//            JSONObject json = (JSONObject) obj;
//            cultids.add(json.get("id"));
//        }
        String cultid = request.getParameter("cultid");
        try {
            list = this.whgShowOrganService.t_srchList(cultid);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ArrayList();
        }
        return list;
    }

    /**
     * 添加组织机构
     * @param organ
     * @param session
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseBean add(WhgShowOrgan organ, HttpSession session){
        ResponseBean resb = new ResponseBean();

        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            resb = this.whgShowOrganService.t_add(organ, sysUser);
        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("组织机构信息保存失败");
            log.error(resb.getErrormsg(), e);
        }
        return resb;
    }

    /**
     * 编辑
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(WhgShowOrgan organ){
        ResponseBean resb = new ResponseBean();
        if (organ == null || organ.getId()==null || organ.getId().isEmpty()){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("组织机构标识不能为空");
            return resb;
        }

        try {
            resb = this.whgShowOrganService.t_edit(organ);
        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("组织机构信息保存失败");
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
            resb.setErrormsg("组织机构标识不能为空");
            return resb;
        }

        try {
            resb = this.whgShowOrganService.t_del(id);
        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("组织机构信息删除失败");
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
            resb = this.whgShowOrganService.t_updstate(ids, formstates, tostate, sysUser,optTime);
        }catch (Exception e){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("组织机构状态更改失败");
            log.error(resb.getErrormsg(), e);
        }
        return resb;
    }

}