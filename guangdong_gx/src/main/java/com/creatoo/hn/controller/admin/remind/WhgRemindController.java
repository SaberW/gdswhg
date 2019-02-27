package com.creatoo.hn.controller.admin.remind;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.remind.WhgRemindService;
import com.creatoo.hn.services.admin.system.WhgSystemRoleService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.util.RequestUtils;
import com.creatoo.hn.util.WeekDayUtil;
import com.creatoo.hn.util.bean.ResponseBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/1.
 */
@RestController
@RequestMapping("/admin/remind")
public class WhgRemindController extends BaseController{

    @Autowired
    private WhgRemindService whgRemindService;

    @Autowired
    private WhgSystemUserService WhgSystemUserService;

    /**
     * 进入消息提醒统计视图
     * @return
     */
    @RequestMapping("/view")
    public ModelAndView view(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        String cultid = RequestUtils.getParameter(request, "cultid");

        if(StringUtils.isEmpty(cultid)){
            JSONArray jsonArray = (JSONArray) request.getSession().getAttribute(Constant.SESSION_ADMIN_CULT);//权限分馆JSON格式
            if(jsonArray != null && jsonArray.size() > 0){
                JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                cultid = (String) jsonObject.get("id");
            }
        }
        view.addObject("curtDate", WeekDayUtil.getWeekStr(new Date()));
        view.addObject("cultid", cultid);
        view.setViewName("admin/remind/remind");
        return view;
    }



    /**
     *  加载内部供需数据
     * @param request
     * @return
     */
    @RequestMapping("/nbgxsrchList")
    public ResponseBean nbgxsrchList(HttpServletRequest request){
        ResponseBean resb = new ResponseBean();
        WhgSysUser whgSysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);

        try {
            List list = whgRemindService.t_nbgxsrchList(whgSysUser);
            resb.setData(list);

        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("查询失败");
            log.error(e.getMessage(), e);
        }
        return resb;
    }


    /**
     *  加载内部供需数据
     * @param request
     * @return
     */
    @RequestMapping("/wbgxsrchList")
    public ResponseBean wbgxsrchList(HttpServletRequest request){
        ResponseBean resb = new ResponseBean();
        try {
            WhgSysUser whgSysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
            List list = whgRemindService.t_wbgxsrchList(whgSysUser);
            resb.setData(list);

        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("查询失败");
            log.error(e.getMessage(), e);
        }
        return resb;
    }

    /**
     *  加载网络培训数据
     * @param request
     * @return
     */
    @RequestMapping("/wlpxsrchList")
    public ResponseBean wlpxsrchList(HttpServletRequest request){
        ResponseBean resb = new ResponseBean();
        try {
            WhgSysUser whgSysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
            List list = whgRemindService.t_wlpxsrchList(whgSysUser);
            resb.setData(list);

        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("查询失败");
            log.error(e.getMessage(), e);
        }
        return resb;
    }

    /**
     *  加载用户权限list
     * @param request
     * @return
     */
    @RequestMapping("/getPmsList")
    public ResponseBean getPmsList(HttpServletRequest request){
        ResponseBean resb = new ResponseBean();
        String id = request.getParameter("id");
        try {
            List list = WhgSystemUserService.getUserAllPmsStr(id);
            resb.setData(list);

        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("查询失败");
            log.error(e.getMessage(), e);
        }
        return resb;
    }


    /**
     * 群文待办数据
     * @param request
     * @return
     */
    @RequestMapping("/massSrchList")
    public ResponseBean massSrchList(HttpServletRequest request){
        ResponseBean resb = new ResponseBean();
        try {
            WhgSysUser whgSysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
            Map rest = whgRemindService.t_massSrchList(whgSysUser);
            resb.setData(rest);

        } catch (Exception e) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("查询失败");
            resb.setData(new HashMap());
            log.error(e.getMessage(), e);
        }
        return resb;
    }
}
