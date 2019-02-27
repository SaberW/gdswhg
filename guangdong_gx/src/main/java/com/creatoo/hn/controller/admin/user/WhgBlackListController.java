package com.creatoo.hn.controller.admin.user;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgUserBlacklist;
import com.creatoo.hn.services.admin.user.WhgBlackListService;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 黑名单action
 * @author wenjingqiang
 * @version 1-201703
 * Created by Administrator on 2017/4/26.
 */
@Controller
@RequestMapping("/admin/user/black")
public class WhgBlackListController extends BaseController {

    /**
     * 黑名单service
     */
    @Autowired
    private WhgBlackListService whgBlackListService;

    @RequestMapping("/view/list")
    @WhgOPT(optType = EnumOptType.MEMBER, optDesc = {"查看黑名单列表"})
    public ModelAndView viewList(){
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/user/blacklist/view_list");
        return view;
    }

    /**
     *  分页加载黑名单列表数据
     * @param request
     * @return
     */
    @RequestMapping("/srchList4p")
    @ResponseBody
    public ResponseBean srchList4p(HttpServletRequest request, int page, int rows, WhgUserBlacklist record){
        ResponseBean res = new ResponseBean();
        try {
            String sort = request.getParameter("sort");
            String order = request.getParameter("order");

            PageInfo pageInfo = this.whgBlackListService.srchList4p(page, rows, record, sort, order);
            res.setRows(pageInfo.getList());
            res.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 取消黑名单
     * @return
     */
    @RequestMapping("/cancel")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.MEMBER, optDesc = {"取消黑名单"})
    public ResponseBean cancel(String userid) {
        ResponseBean res = new ResponseBean();
        try {
            this.whgBlackListService.unBlackList4uid(userid);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("操作失败");
            log.error(res.getErrormsg(), e);
        }
        return res;
    }
}
