package com.creatoo.hn.controller.admin.resource;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgResource;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.ReqParamsUtil;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.github.pagehelper.PageInfo;
import com.creatoo.hn.services.admin.resourse.WhgResourceService;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 资源管理action
 *
 * @author luzhihuai
 * @version 1-201703
 *          Created by Administrator on 2017/3/29.
 */
@Controller
@RequestMapping("/admin/resource")
public class WhgResourceController extends BaseController {
    /**
     * log
     */
    private Logger log = Logger.getLogger(this.getClass());

    /**
     * 资源管理service
     */
    @Autowired
    private WhgResourceService whgResourceService;
    /**
     * 进入type(list|add|edit|view)视图
     *
     * @param request 请求对象
     * @param type    视图类型(list|add|edit|view)
     * @return 视图
     */
    @RequestMapping("/view/{type}")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.TRA, optDesc = {"进入资源列表页"})
    public ModelAndView listview(HttpServletRequest request, @PathVariable("type") String type) {
        ModelAndView view = new ModelAndView("admin/resource/view_" + type);
        String refid = request.getParameter("refid");//关联id
        view.addObject("refid", refid);
        String id = request.getParameter("id");//资源id
        view.addObject("id",id);
        String reftype = request.getParameter("reftype");
        view.addObject("reftype", reftype);
        try {
            if("edit".equals(type)){
                view.addObject("wcr", whgResourceService.t_srchOne(id));
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }

        return view;
    }

    /**
     * 分页查询
     *
     * @param request .
     * @return res
     */
    @RequestMapping(value = "/srchList4p")
    @ResponseBody
    public ResponseBean srchList4p(HttpServletRequest request, WhgResource whgComResource) {
        ResponseBean res = new ResponseBean();
        try {
            PageInfo<WhgResource> pageInfo = whgResourceService.t_srchList4p(request,whgComResource);
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
     * 查询详情
     * @param id id
     * @return res
     */
    @RequestMapping(value = "/srchOne")
    @ResponseBody
    public ResponseBean srchOne(String id){
        ResponseBean res = new ResponseBean();
        try {
            WhgResource whgComResource = this.whgResourceService.t_srchOne(id);
            res.setData(whgComResource);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 添加
     *
     * @param whgComResource 实体
     * @return 对象
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.TRA, optDesc = {"添加资源"})
    public ResponseBean add(HttpServletRequest request, WhgResource whgComResource) {
        ResponseBean res = new ResponseBean();
        try {
            this.whgResourceService.t_add(request,whgComResource);
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
     * @param request .
     * @param whgComResource whgComResource
     * @return res
     */
    @RequestMapping(value = "/edit")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.TRA, optDesc = {"编辑资源"})
    public ResponseBean edit(HttpServletRequest request, WhgResource whgComResource) {
        ResponseBean res = new ResponseBean();
        try {
            Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
            this.whgResourceService.t_edit(paramMap, whgComResource);
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
     * @return res
     */
    @RequestMapping("/del")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.TRA, optDesc = {"删除资源"})
    public ResponseBean del(HttpServletRequest request) {
        ResponseBean res = new ResponseBean();
        try {
            this.whgResourceService.t_del(request);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }


}
