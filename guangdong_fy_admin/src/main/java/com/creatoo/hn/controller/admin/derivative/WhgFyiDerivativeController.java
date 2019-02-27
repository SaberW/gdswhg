package com.creatoo.hn.controller.admin.derivative;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgFyiDerivative;
import com.creatoo.hn.services.admin.derivative.WhgFyDerivativeService;
import com.creatoo.hn.util.RequestUtils;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 非遗衍生品action
 *
 * @author luzhihuai
 *         Created by Administrator on 2017/11/1.
 */
@Controller
@RequestMapping("/admin/derivative")
public class WhgFyiDerivativeController extends BaseController {
    /**
     * log
     */
    private Logger log = Logger.getLogger(this.getClass());

    /**
     * 非遗衍生品service
     */
    @Autowired
    private WhgFyDerivativeService derivativeService;

    /**
     * 进入type(list|add|edit|view)视图
     *
     * @param request 请求对象
     * @param type    视图类型(list|add|edit|view)
     * @return 视图
     */
    @RequestMapping("/view/{type}")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.DERIVATIVE, optDesc = {"进入非遗衍生品列表页"})
    public ModelAndView view(HttpServletRequest request, @PathVariable("type") String type) {
        String edit = "edit";
        String look = "view";
        ModelAndView view = new ModelAndView("admin/derivative/view_" + type);
        String id = request.getParameter("id");
        view.addObject("id", id);
        //查看
        String targetShow = request.getParameter("targetShow");
        view.addObject("targetShow", targetShow);
        try {
            if (edit.equals(type) || look.equals(type)) {
                view.addObject("derivative", derivativeService.searchOne(id));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return view;
    }

    /**
     * 列表查询
     * @param request  请求对象
     * @param derivative 衍生品对象
     * @return 实体
     */
    @RequestMapping(value = "/derivativeList")
    @ResponseBody
    public ResponseBean srchList4p(HttpServletRequest request, WhgFyiDerivative derivative) {
        ResponseBean res = new ResponseBean();
        try {
            PageInfo<WhgFyiDerivative> pageInfo = derivativeService.tSrchlist4p(request, derivative);
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
     * @param derivative bean
     * @return 对象
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.DERIVATIVE, optDesc = {"添加非遗衍生品"})
    public ResponseBean add(HttpServletRequest request, WhgFyiDerivative derivative) {
        ResponseBean res = new ResponseBean();
        try {
            this.derivativeService.add(derivative, RequestUtils.getAdmin(request));
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 编辑
     *
     * @param request  request对象
     * @param derivative whgComResource
     * @return res
     */
    @RequestMapping(value = "/edit")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.DERIVATIVE, optDesc = {"编辑非遗衍生品"})
    public ResponseBean edit(HttpServletRequest request, WhgFyiDerivative derivative) {
        ResponseBean res = new ResponseBean();
        try {
            if (StringUtils.isEmpty(derivative.getId())) {
                res.setErrormsg("id不能为空");
                res.setSuccess(ResponseBean.FAIL);
                return res;
            }
            this.derivativeService.edit(RequestUtils.getAdmin(request), derivative);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 删除
     * @param id id
     * @return 固定对象
     */
    @RequestMapping("/del")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.DERIVATIVE, optDesc = {"删除非遗衍生品"})
    public ResponseBean del(String id) {
        ResponseBean res = new ResponseBean();
        try {
            if (StringUtils.isEmpty(id)) {
                res.setErrormsg("id不能为空");
                res.setSuccess(ResponseBean.FAIL);
                return res;
            }
            this.derivativeService.del(id);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 修改状态
     *
     * @param ids        id
     * @param formstates 当前状态
     * @param tostate    要改变的状态
     * @param request    request
     * @return res
     */
    @RequestMapping("/updstate")
    @ResponseBody
    public ResponseBean updstate(String ids, String formstates, int tostate, HttpServletRequest request) {
        ResponseBean res = new ResponseBean();
        try {
            res = this.derivativeService.updateState(ids, formstates, tostate, RequestUtils.getAdmin(request));
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
     * @param ids 主键id
     * @param toState 修改后的状态
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/updCommend")
    @ResponseBody
    public ResponseBean updCommend(HttpServletRequest request, String ids, String toState) {
        ResponseBean res = new ResponseBean();
        try {
            if (StringUtils.isEmpty(ids) && StringUtils.isEmpty(toState)) {
                res.setErrormsg("id不能为空");
                res.setSuccess(ResponseBean.FAIL);
                return res;
            }
            int c = this.derivativeService.updateCommend(ids, toState, RequestUtils.getAdmin(request));
            if (c != 1) {
                res.setErrormsg("推荐失败");
            }
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

}
