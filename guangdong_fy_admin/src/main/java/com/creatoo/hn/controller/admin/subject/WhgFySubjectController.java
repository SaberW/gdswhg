package com.creatoo.hn.controller.admin.subject;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgFyiSubject;
import com.creatoo.hn.services.admin.subject.WhgFySubjectService;
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
 * 非遗专题action
 *
 * @author luzhihuai
 *         Created by Administrator on 2017/10/31.
 */
@Controller
@RequestMapping("/admin/subject")
public class WhgFySubjectController extends BaseController {
    /**
     * log
     */
    private Logger log = Logger.getLogger(this.getClass());

    /**
     * 非遗展厅service
     */
    @Autowired
    private WhgFySubjectService subjectService;

    /**
     * 进入type(list|add|edit|view)视图
     *
     * @param request 请求对象
     * @param type    视图类型(list|add|edit|view)
     * @return 视图
     */
    @RequestMapping("/view/{type}")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.SUBJECT, optDesc = {"进入非遗专题列表页"})
    public ModelAndView view(HttpServletRequest request, @PathVariable("type") String type) {
        String edit = "edit";
        String look = "view";
        ModelAndView view = new ModelAndView("admin/subject/view_" + type);
        //专题id
        String id = request.getParameter("id");
        view.addObject("id", id);
        //查看
        String targetShow = request.getParameter("targetShow");
        view.addObject("targetShow", targetShow);
        try {
            if (edit.equals(type) || look.equals(type)) {
                view.addObject("subject", subjectService.searchOne(id));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return view;
    }

    /**
     * 列表查询
     * @param request  请求对象
     * @param subject 专题对象
     * @return 实体
     */
    @RequestMapping(value = "/subjectList")
    @ResponseBody
    public ResponseBean srchList4p(HttpServletRequest request, WhgFyiSubject subject) {
        ResponseBean res = new ResponseBean();
        try {
            PageInfo<WhgFyiSubject> pageInfo = subjectService.tSrchlist4p(request, subject);
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
     * @param subject 实体
     * @return 对象
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.SUBJECT, optDesc = {"添加非遗专题"})
    public ResponseBean add(HttpServletRequest request, WhgFyiSubject subject) {
        ResponseBean res = new ResponseBean();
        try {
            this.subjectService.add(subject, RequestUtils.getAdmin(request));
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
     * @param subject  专题对象
     * @return res
     */
    @RequestMapping(value = "/edit")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.SUBJECT, optDesc = {"编辑非遗专题"})
    public ResponseBean edit(HttpServletRequest request, WhgFyiSubject subject) {
        ResponseBean res = new ResponseBean();
        try {
            if (StringUtils.isEmpty(subject.getId())) {
                res.setErrormsg("id不能为空");
                res.setSuccess(ResponseBean.FAIL);
                return res;
            }
            this.subjectService.edit(RequestUtils.getAdmin(request), subject);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 删除
     * @param id 展厅id
     * @return 固定对象
     */
    @RequestMapping("/del")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.SUBJECT, optDesc = {"删除非遗专题"})
    public ResponseBean del(String id) {
        ResponseBean res = new ResponseBean();
        try {
            if (StringUtils.isEmpty(id)) {
                res.setErrormsg("id不能为空");
                res.setSuccess(ResponseBean.FAIL);
                return res;
            }
            this.subjectService.del(id);
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
            res = this.subjectService.updateState(ids, formstates, tostate, RequestUtils.getAdmin(request));
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("状态更改失败");
            log.error(res.getErrormsg() + " formstate: " + formstates + " tostate:" + tostate + " ids: " + ids, e);
        }
        return res;
    }


}
