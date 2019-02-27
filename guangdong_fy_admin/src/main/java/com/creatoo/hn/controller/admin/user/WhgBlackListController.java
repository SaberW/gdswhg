package com.creatoo.hn.controller.admin.user;

import com.creatoo.hn.dao.model.WhgUserBlacklist;
import com.creatoo.hn.util.CommUtil;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.services.admin.user.WhgBlackListService;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

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

    @RequestMapping("/view/{type}")
    @WhgOPT(optType = EnumOptType.MEMBER, optDesc = {"进入黑名单列表"})
    public ModelAndView view(HttpServletRequest request, ModelMap mmp, @PathVariable("type") String type){
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/user/blacklist/view_"+type);
        return view;
    }

    /**
     *  分页加载黑名单列表数据
     * @param request
     * @return
     */
    @RequestMapping("/srchList4p")
    @ResponseBody
    public ResponseBean srchList4p(HttpServletRequest request){
        ResponseBean res = new ResponseBean();
        try {
            PageInfo<WhgUserBlacklist> pageInfo = this.whgBlackListService.t_srchList4p(request);
            res.setRows(pageInfo.getList());
            res.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setRows(new ArrayList(0));
            res.setTotal(0);
            log.error(e.getMessage());
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
    public ResponseBean cancel(String id, String userid, HttpSession session) {
        ResponseBean res = new ResponseBean();
        try {
            this.whgBlackListService.t_cancel(id,userid);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("取消黑名单失败");
            log.error(res.getErrormsg(), e);
        }
        return res;
    }
}
