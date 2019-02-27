package com.creatoo.hn.controller.admin.feiyi;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgFyiHistorical;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.feiyi.WhgFeiyiHistoricalService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by LENUVN on 2017/7/27.
 */
@RestController
@RequestMapping("/admin/fyiHistorical")
public class WhgFeiyiHistoricalController extends BaseController {

    /**
     * 文化遗产管理service
     */
    @Autowired
    private WhgFeiyiHistoricalService whgFeiyiHistoricalService;

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
            if ("add".equalsIgnoreCase(type)) {
                String id = request.getParameter("id");
                String targetShow = request.getParameter("targetShow");
                if (id != null) {
                    view.addObject("id", id);
                    view.addObject("targetShow", targetShow);
                    view.addObject("cult", whgFeiyiHistoricalService.t_srchOne(id));
                    view.setViewName("admin/feiyi/historical/view_edit");
                } else {
                    view.setViewName("admin/feiyi/historical/view_add");
                }
            } else {
                view.setViewName("admin/feiyi/historical/view_list");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return view;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return res
     */
    @RequestMapping(value = "/srchList4p")
    public ResponseBean srchList4p(HttpServletRequest request, WhgFyiHistorical historical) {
        ResponseBean res = new ResponseBean();
        try {
            WhgSysUser whgSysUser = (WhgSysUser)request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
            PageInfo<WhgFyiHistorical> pageInfo = whgFeiyiHistoricalService.t_srchList4p(request, historical,null);
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
     * 添加
     *
     * @return res
     */
    @RequestMapping("/add")
    public ResponseBean add(WhgFyiHistorical historical, HttpServletRequest request) {
        ResponseBean res = new ResponseBean();
        try {
            String newId = IDUtils.getID();
            historical.setId(newId);
            this.whgFeiyiHistoricalService.t_add(request, historical);
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
     * @param historical historical
     * @return res
     */
    @RequestMapping(value = "/edit")
    public ResponseBean edit(WhgFyiHistorical historical,HttpServletRequest request) {
        ResponseBean res = new ResponseBean();
        try {

            String branch = request.getParameter("branch");
            if(null != branch && !branch.trim().isEmpty()){
                this.whgFeiyiHistoricalService.t_edit(historical);
            }
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 删除
     *
     * @return
     */
    @RequestMapping("/del")
    public ResponseBean del(String id) {
        ResponseBean res = new ResponseBean();
        try {
            this.whgFeiyiHistoricalService.t_del(id);

        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("删除失败");
            log.error(res.getErrormsg(), e);
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
    public Object undel(String id) {
        ResponseBean rb = new ResponseBean();
        try {
            this.whgFeiyiHistoricalService.t_undel(id);
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
    public ResponseBean updstate(String ids, String formstates, int tostate, HttpSession session) {
        ResponseBean res = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            res = this.whgFeiyiHistoricalService.t_updstate(ids, formstates, tostate, sysUser);
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
    public ResponseBean updCommend(HttpServletRequest request, String ids, String fromState, String toState){
        ResponseBean res = new ResponseBean();
        try {
            int c = this.whgFeiyiHistoricalService.t_updCommend(ids, fromState, toState, (WhgSysUser)request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY));
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
