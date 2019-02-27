package com.creatoo.hn.controller.admin.personnel;

import com.alibaba.fastjson.JSON;
import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgPersonnel;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.personnel.WhgPersonnelService;
import com.creatoo.hn.services.admin.supply.WhgSupplyService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.creatoo.hn.util.enums.EnumSupplyType;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LENUVN on 2017/7/27.
 */
@Controller
@RequestMapping("/admin/personnel")
public class WhgPersonnelController extends BaseController {


    @Autowired
    private WhgPersonnelService whgPersonnelService;

    @Autowired
    private WhgSupplyService whgSupplyService;
    /**
     * 进入type(list|add|edit|view)视图
     *
     * @param request 请求对象
     * @param type    视图类型(list|add|edit|view)
     * @return 视图
     */
    @RequestMapping("/view/{type}")
    public ModelAndView listview(HttpServletRequest request, @PathVariable("type") String type) {
        ModelAndView view = new ModelAndView();
        try {
            view.addObject("type", type);
            if ("add".equalsIgnoreCase(type) || "show".equalsIgnoreCase(type)) {
                String id = request.getParameter("id");
                String targetShow = request.getParameter("targetShow");
                if ("show".equalsIgnoreCase(type)) {//查看
                    targetShow = "1";
                }
                if (id != null) {
                    view.addObject("id", id);
                    view.addObject("targetShow", targetShow);
                    WhgPersonnel per = whgPersonnelService.t_srchOne(id);
                    Map _map = new HashMap();
                    _map.put("per",per);
                    List timeList = whgSupplyService.selectTimes4Supply(id);
                    if(timeList!=null){
                        _map.put("times", JSON.toJSONString(timeList));
                    }
                    view.addObject("cult", _map);
                    view.setViewName("admin/personnel/view_edit");
                } else {
                    view.setViewName("admin/personnel/view_add");
                }
            } else if ("syslistpublish".equalsIgnoreCase(type)) {
                view.setViewName("admin/personnel/sys_view_list");
            } else {
                view.setViewName("admin/personnel/view_list");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        view.addObject("supplytype", EnumSupplyType.TYPE_WYZJ.getValue());
        return view;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return res
     */
    @RequestMapping(value = "/srchList4p")
    @ResponseBody
    public ResponseBean srchList4p(HttpServletRequest request, WhgPersonnel cultHeritage) {
        ResponseBean res = new ResponseBean();
        String type = request.getParameter("type");
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
            WhgSysUser whgSysUser = (WhgSysUser)request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
            PageInfo<WhgPersonnel> pageInfo = whgPersonnelService.t_srchList4p(request, cultHeritage, type, whgSysUser, record);
            res.setRows(pageInfo.getList());
            res.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 查询权限分馆下的人才列表
     * @return
     */
    @RequestMapping("/srchList")
    @ResponseBody
    public Object srchList(HttpServletRequest request){
        List<WhgPersonnel> list = new ArrayList<>();
//        JSONArray cults = (JSONArray) session.getAttribute(Constant.SESSION_ADMIN_CULT);
//
//        List cultids = new ArrayList();
//        for(Object obj : cults){
//            JSONObject json = (JSONObject) obj;
//            cultids.add(json.get("id"));
//        }
        String cultid = request.getParameter("cultid");
        try {
            list = this.whgPersonnelService.t_srchList(cultid);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ArrayList();
        }
        return list;
    }

    /**
     * 添加
     *
     * @return res
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseBean add(WhgPersonnel whgPersonnel, HttpServletRequest request) {
        ResponseBean res = new ResponseBean();
        try {
            String newId = IDUtils.getID();
            whgPersonnel.setId(newId);
            //String times = request.getParameter("times");
            this.whgPersonnelService.t_add(request, whgPersonnel/*,times*/);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("保存失败");
            log.error(res.getErrormsg(), e);
        }
        return res;
    }

    /**
     * 编辑
     *
     * @param cultHeritage cultHeritage
     * @return res
     */
    @RequestMapping(value = "/edit")
    @ResponseBody
    public ResponseBean edit(WhgPersonnel cultHeritage,HttpServletRequest request) {
        ResponseBean res = new ResponseBean();
        try {
            //String times = request.getParameter("times");
            this.whgPersonnelService.t_edit(cultHeritage/*,times*/);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 物理删除
     *
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public ResponseBean del(String id) {
        ResponseBean res = new ResponseBean();
        try {
            this.whgPersonnelService.t_del(id);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("删除失败");
            log.error(res.getErrormsg(), e);
        }
        return res;
    }

    /**
     * 删除
     *
     * @param request 请求对象
     * @param ids     用逗号分隔的多个ID
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/recovery")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.ACT, optDesc = {"回收人才"})
    public ResponseBean recovery(HttpServletRequest request, String ids, String delStatus) {
        ResponseBean res = new ResponseBean();
        try {
            this.whgPersonnelService.t_updDelstate(ids);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 还原删除
     *
     * @param id id
     * @return res
     */
    @RequestMapping("/undel")
    @ResponseBody
    public Object undel(String id) {
        ResponseBean rb = new ResponseBean();
        try {
            this.whgPersonnelService.t_undel(id);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("还原失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    /**
     * 修改状态
     *
     * @param ids        id
     * @param formstates 当前状态
     * @param tostate    要改变的状态
     * @param session    session
     * @return res
     */
    @RequestMapping("/updstate")
    @ResponseBody
    public ResponseBean updstate(String ids, String formstates, int tostate, HttpSession session,
                                 @RequestParam(value = "reason", required = false) String reason,
                                 @RequestParam(value = "issms", required = false, defaultValue = "0") int issms) {
        ResponseBean res = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            res = this.whgPersonnelService.t_updstate(ids, formstates, tostate, sysUser, reason, issms);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("状态更改失败");
            log.error(res.getErrormsg() + " formstate: " + formstates + " tostate:" + tostate + " ids: " + ids, e);
        }
        return res;
    }

    /**
     * 推荐状态修改
     * @param request 请求对象
     * @param ids 用逗号分隔的多个ID
     * @param fromState 修改之前的状态
     * @param toState 修改后的状态
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/updCommend")
    @ResponseBody
    public ResponseBean updCommend(HttpServletRequest request, String ids, String fromState, String toState){
        ResponseBean res = new ResponseBean();
        try {
            int c = this.whgPersonnelService.t_updCommend(ids, fromState, toState, (WhgSysUser)request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY));
            if(c != 1){
                res.setErrormsg("推荐失败");
            }
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

}
