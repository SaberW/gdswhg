package com.creatoo.hn.controller.front;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysRole;
import com.creatoo.hn.services.front.FrontIndexService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WEB 网站首页控制器
 * Created by wangXL on 2017/7/3.
 */
@RestController
public class IndexController extends BaseController {

    @Autowired
    private FrontIndexService frontIndexService;

    /**
     * 访问系统首页
     * @return 系统首页
     */
    @RequestMapping(value = "/", method = {RequestMethod.GET})
    public ModelAndView index(String aCurtPage, String aPageSize, String aSort, String aOrder, WhgSysRole role){
        ModelAndView view = new ModelAndView();
        try{
            return new ModelAndView("admin/main");
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return view;
    }
}
