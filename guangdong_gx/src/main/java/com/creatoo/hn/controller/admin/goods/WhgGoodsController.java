package com.creatoo.hn.controller.admin.goods;

import com.alibaba.fastjson.JSON;
import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgGoods;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.goods.WhgGoodsService;
import com.creatoo.hn.services.admin.supply.WhgSupplyService;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.*;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by rbg on 2017/8/3.
 */

@SuppressWarnings("ALL")
@Controller
@RequestMapping("/admin/goods")
public class WhgGoodsController extends BaseController {

    @Autowired
    private WhgGoodsService whgGoodsService;

    @Autowired
    private WhgSupplyService whgSupplyService;

    /**
     * 访问管理页面
     */
    @RequestMapping("/view/{type}")
    @WhgOPT(optType = EnumOptType.GOODS,
            optDesc = {"访问文艺辅材列表页", "访问文艺辅材添加页", "访问文艺辅材编辑页", "查看文艺辅材信息"},
            valid = {"type=list", "type=add", "type=edit", "type=show"})
    public String view(@PathVariable String type, ModelMap mmp, HttpSession session, WebRequest request){
        String view = "admin/goods/";
        switch (type){
            case "show" :
            case "edit" :
                String id = request.getParameter("id");
                if (id!=null && !id.isEmpty()){
                    try {
                        mmp.addAttribute("id", id);
                        WhgGoods goods = this.whgGoodsService.srchOne(id);
                        Map _map = new HashMap();
                        _map.put("good",goods);
                        List timeList=whgSupplyService.selectTimes4Supply(id);
                        if(timeList!=null){
                            _map.put("times", JSON.toJSONString(timeList));
                        }
                        mmp.addAttribute("info", _map);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            case "add" :
                view+= "view_edit";
                break;
            case "syslistpublish":
                view += "sys_view_list";
                break;
            default: view+="view_list";
        }
        mmp.addAttribute("pageType", type);
        mmp.addAttribute("supplytype", EnumSupplyType.TYPE_WYPC.getValue());

        return view;
    }


    /**
     * 列表分页加载
     */
    @RequestMapping("/srchList4p")
    @ResponseBody
    public Object srchList4p(int page, int rows, WhgGoods goods, WebRequest request, HttpSession session){
        ResponseBean resb = new ResponseBean();

        String sort = request.getParameter("sort");
        String order = request.getParameter("order");
        String pageType = request.getParameter("pageType");

        String iscult = request.getParameter("iscult");
        String refid = request.getParameter("refid");
        String pcalevel = request.getParameter("pcalevel");
        String pcatext = request.getParameter("pcatext");

        Map<String, String> record = new HashMap();
        record.put("iscult", iscult);
        record.put("refid", refid);
        record.put("pcalevel", pcalevel);
        record.put("pcatext", pcatext);

        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);

            List states = null;
            if ("listedit".equalsIgnoreCase(pageType)){
                goods.setCrtuser(sysUser.getId());
            }
            if ("listcheck".equalsIgnoreCase(pageType) || "listpublish".equalsIgnoreCase(pageType)){
                states = Arrays.asList(9,2,6,4);
            }

            if ("syslistpublish".equalsIgnoreCase(pageType)) {
                states = Arrays.asList(6, 4);
            }

            if ("listdel".equalsIgnoreCase(pageType)){
                goods.setDelstate(EnumStateDel.STATE_DEL_YES.getValue());
            }else{
                goods.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
            }

            PageInfo pageInfo = this.whgGoodsService.srchlist4p(page, rows, goods, states, sort, order, sysUser.getId(), record);
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
    @WhgOPT(optType = EnumOptType.GOODS, optDesc = "添加")
    public Object add(HttpSession session, WhgGoods goods, HttpServletRequest request){
        ResponseBean resb = new ResponseBean();

        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            //String times = request.getParameter("times");
            resb = this.whgGoodsService.t_add(goods, sysUser/*,times*/);
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
    @WhgOPT(optType = EnumOptType.GOODS, optDesc = "编辑")
    public Object edit(WhgGoods goods,HttpServletRequest request){
        ResponseBean resb = new ResponseBean();
        if (goods == null || goods.getId()==null || goods.getId().isEmpty()){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("标识不能为空");
            return resb;
        }

        try {
            if (goods.getEtag()==null) goods.setEtag("");
            if (goods.getEkey()==null) goods.setEkey("");
            //String times = request.getParameter("times");
            resb = this.whgGoodsService.t_edit(goods/*,times*/);
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
    @WhgOPT(optType = EnumOptType.GOODS, optDesc = {"删除"})
    public Object del(String id){
        ResponseBean resb = new ResponseBean();
        if (id==null || id.isEmpty()){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("标识不能为空");
            return resb;
        }

        try {
            resb = this.whgGoodsService.t_del(id);
        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("信息删除失败");
            log.error(resb.getErrormsg(), e);
        }

        return resb;
    }


    @RequestMapping("/recovery")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.GOODS, optDesc = {"回收"})
    public Object recovery(String id, HttpSession session) {
        ResponseBean rb = new ResponseBean();

        try {
            WhgGoods info = new WhgGoods();
            info.setId(id);
            info.setDelstate(EnumStateDel.STATE_DEL_YES.getValue());

            this.whgGoodsService.t_edit(info/*, null*/);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("操作失败");
            log.error(rb.getErrormsg(), e);
        }

        return rb;
    }


    @RequestMapping("/undel")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.GOODS, optDesc = {"还原"})
    public Object undel(String id){
        ResponseBean rb = new ResponseBean();

        try {
            WhgGoods info = new WhgGoods();
            info.setId(id);
            info.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
            info.setState(EnumBizState.STATE_CAN_EDIT.getValue());

            this.whgGoodsService.t_edit(info/*, null*/);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("操作失败");
            log.error(rb.getErrormsg(), e);
        }

        return rb;
    }


    @RequestMapping("/updstate")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.GOODS,
            optDesc = {"送审","审核","打回","发布","取消发布"},
            valid = {"tostate=9","tostate=2","tostate=1","tostate=6","tostate=4"})
    public Object updstate(String ids, String formstates, int tostate, HttpSession session,
                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date optTime
            , @RequestParam(value = "reason", required = false) String reason,
                           @RequestParam(value = "issms", required = false, defaultValue = "0") int issms) {
        ResponseBean resb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            resb = this.whgGoodsService.t_updstate(ids, formstates, tostate, sysUser, optTime, reason, issms);
        }catch (Exception e){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("状态更改失败");
            log.error(resb.getErrormsg(), e);
        }
        return resb;
    }
}
