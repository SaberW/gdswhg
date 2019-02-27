package com.creatoo.hn.controller.admin.supply;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSupply;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.supply.WhgSupplyService;
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
 * Created by rbg on 2017/8/22.
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping("/admin/supply")
public class WhgSupplyController extends BaseController {

    @Autowired
    private WhgSupplyService whgSupplyService;

    @RequestMapping("/view/{type}")
    @WhgOPT(optType = EnumOptType.SUPPLY,
            optDesc = {"访问供需列表页", "访问供需添加页", "访问供需编辑页", "查看供需信息"},
            valid = {"type=list", "type=add", "type=edit", "type=show"})
    public String view(@PathVariable(value = "type") String type, WebRequest request, ModelMap mmp){
        String view = "admin/supply/";
        switch (type) {
            case "show":
            case "edit":
                String id = request.getParameter("id");
                if (id != null && !id.isEmpty()) {
                    mmp.addAttribute("id", id);
                    try {
                        mmp.addAttribute("info", this.whgSupplyService.srchOne(id));
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            case "add":
                view += "view_edit";
                break;
            case "list":
            default:
                type = "list";
                view += "view_list";
        }

        mmp.addAttribute("pageType", type);
        return view;
    }

    /**
     * 分页列表查询
     * @param page
     * @param rows
     * @param goods
     * @param request
     * @return
     */
    @RequestMapping("/srchList4p")
    @ResponseBody
    public Object srchList4p(int page, int rows, WhgSupply supply, WebRequest request, HttpSession session){
        ResponseBean resb = new ResponseBean();

        String sort = request.getParameter("sort");
        String order = request.getParameter("order");

        try {
            /*WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            String cultid = sysUser.getCultid();
            if (cultid==null){
                cultid = Constant.ROOT_SYS_CULT_ID;
            }
            supply.setCultid(sysUser.getCultid());*/

            PageInfo pageInfo = (PageInfo) this.whgSupplyService.srchList4p(page,rows,supply,sort,order);
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
    @WhgOPT(optType = EnumOptType.SUPPLY, optDesc = "添加供需信息")
    public Object add(HttpSession session, WhgSupply info, String times){
        ResponseBean resb = new ResponseBean();

        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            resb = this.whgSupplyService.t_add(sysUser, info, times);
        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("添加供需信息失败");
            log.error(resb.getErrormsg(), e);
        }

        return resb;
    }

    /**
     * 编辑
     */
    @RequestMapping("/edit")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.SUPPLY, optDesc = "编辑供需信息")
    public Object edit(HttpSession session, WhgSupply info, String times) {
        ResponseBean resb = new ResponseBean();

        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            resb = this.whgSupplyService.t_edit(sysUser, info, times);
        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("编辑供需信息");
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
    @WhgOPT(optType = EnumOptType.SUPPLY, optDesc = {"删除供需信息"})
    public Object del(String id){
        ResponseBean resb = new ResponseBean();

        try {
            resb = this.whgSupplyService.t_del(id);
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
            resb = this.whgSupplyService.t_updstate(ids, formstates, tostate, sysUser, optTime);
        }catch (Exception e){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("商品状态更改失败");
            log.error(resb.getErrormsg(), e);
        }
        return resb;
    }
}
