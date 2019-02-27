package com.creatoo.hn.controller.admin.yunwei;


import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgLiveComm;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgYwiLbt;
import com.creatoo.hn.services.admin.mylive.MyLiveService;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiLbtService;
import com.creatoo.hn.util.ReqParamsUtil;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 系统管理的轮播图配置action
 *
 * @author luzhihuai
 * @version 1-201703
 *          Created by Administrator on 2017/3/20.
 */
@RestController
@RequestMapping("/admin/yunwei/config")
public class WhgSystemConfigController extends BaseController{
    /**
     * 直播管理
     */
    @Autowired
    private MyLiveService myLiveService;

    /**
     * 轮播图service
     */
    @Autowired
    private WhgYunweiLbtService whgYunweiLbtService;

    /**
     * 进入type(list|add|edit|view)视图
     * @param request 请求对象
     * @return 视图
     */
    @RequestMapping("/{page}")
    public ModelAndView confSystem(HttpServletRequest request, @PathVariable("page") String page) {
        ModelAndView view = new ModelAndView();
        try {
            WhgLiveComm live = this.myLiveService.getYTJLive();
            if(live != null){
                String url = live.getEnturl();
                String name = live.getName();
                view.addObject("resurl", url);
                view.addObject("resname", name);
                view.addObject("respicture", live.getPlayaddr());//封面图片
            }
            //view.addObject("lbt",whgYunweiLbtService.t_srchOne(id));
            view.setViewName("admin/yunwei/config/"+page);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return view;
    }

    /**
     * 保存一体机首页视频配置
     * @param resurl 视频地址
     * @return
     */
    @RequestMapping("/save")
    public ResponseBean save(String resurl, String resname, String respicture){
        ResponseBean res = new ResponseBean();
        try {
            this.myLiveService.setYTJLive(resurl, resname, respicture);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

}
