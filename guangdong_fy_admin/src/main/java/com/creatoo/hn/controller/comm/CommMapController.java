package com.creatoo.hn.controller.comm;

import com.creatoo.hn.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by rbg on 2017/7/12.
 */
@Controller
@RequestMapping("/comm")
public class CommMapController extends BaseController {

    /**
     * 跳转到取坐标界面
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/gomap")
    public ModelAndView goMap(HttpServletRequest request, HttpServletResponse response)throws Exception{
        ModelAndView view = new ModelAndView("/comm/admin/gomap");
        return view;
    }

    /**
     * 这个是给前端用的地图
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/gomap2")
    public ModelAndView goMap2(HttpServletRequest request, HttpServletResponse response)throws Exception{
        ModelAndView view = new ModelAndView("/comm/admin/gomap2");
        return view;
    }
}
