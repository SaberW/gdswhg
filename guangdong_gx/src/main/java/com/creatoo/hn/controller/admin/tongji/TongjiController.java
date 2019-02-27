package com.creatoo.hn.controller.admin.tongji;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.services.admin.tongji.TongjiService;
import com.creatoo.hn.util.WeekDayUtil;
import com.creatoo.hn.util.bean.ResponseBean;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.Map;

/**
 * 总分馆业务统计功能
 * @author wangxl 2018-03-26
 */
@RestController
@RequestMapping("/admin/tongji/bi")
public class TongjiController extends BaseController {
    /**
     * 统计服务
     */
    @Autowired
    private TongjiService tongjiService;

    /**
     * 首页统计
     * @return
     */
    @RequestMapping("/view/index")
    public ModelAndView index() {
        ModelAndView view = new ModelAndView("admin/tongji/bi/view_index");
        try {
            view.addObject("curtDate", WeekDayUtil.getWeekStr(new Date()));

            Map<String, Object> tjMap = this.tongjiService.indexTongji();
            view.addAllObjects(tjMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 活动排行
     * @return
     */
    @RequestMapping("/view/hdph")
    public ModelAndView hdph() {
        ModelAndView view = new ModelAndView();
        try {
            view.setViewName("admin/tongji/bi/view_hdph");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 站点活动
     * @return
     */
    @RequestMapping("/view/zdhd")
    public ModelAndView zdhd() {
        ModelAndView view = new ModelAndView();
        try {
            view.setViewName("admin/tongji/bi/view_zdhd");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 培训排行
     * @return
     */
    @RequestMapping("/view/pxph")
    public ModelAndView pxph() {
        ModelAndView view = new ModelAndView();
        try {
            view.setViewName("admin/tongji/bi/view_pxph");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 站点培训
     * @return
     */
    @RequestMapping("/view/zdpx")
    public ModelAndView zdpx() {
        ModelAndView view = new ModelAndView();
        try {
            view.setViewName("admin/tongji/bi/view_zdpx");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 站点场馆
     * @return
     */
    @RequestMapping("/view/zdcg")
    public ModelAndView zdcg() {
        ModelAndView view = new ModelAndView();
        try {
            view.setViewName("admin/tongji/bi/view_zdcg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 站点资讯
     * @return
     */
    @RequestMapping("/view/zdzx")
    public ModelAndView zdzx() {
        ModelAndView view = new ModelAndView();
        try {
            view.setViewName("admin/tongji/bi/view_zdzx");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 活动排行
     * @return
     */
    @RequestMapping(value = "/hdph")
    public ResponseBean srchList4p(
            Integer page, Integer rows, String sort, String order,
            String province, String city, String area,
            String cultId,
            String actName,
            String timeScope, String startTime, String endTime
    ) {
        ResponseBean res = new ResponseBean();
        try {
            PageInfo<Map> pageInfo = tongjiService.hdph(page, rows, sort, order, province, city, area, cultId, actName, timeScope, startTime, endTime);
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
     * 培训排行
     * @return
     */
    @RequestMapping(value = "/pxph")
    public ResponseBean pxph(
            Integer page, Integer rows, String sort, String order,
            String province, String city, String area,
            String cultId,
            String traName,
            String timeScope, String startTime, String endTime
    ) {
        ResponseBean res = new ResponseBean();
        try {
            PageInfo<Map> pageInfo = tongjiService.pxph(page, rows, sort, order, province, city, area, cultId, traName, timeScope, startTime, endTime);
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
     * 站点活动
     * @return
     */
    @RequestMapping(value = "/zdhd")
    public ResponseBean zdhd(
            Integer page, Integer rows, String sort, String order,
            String province, String city, String area,
            String cultId, String cultName, String cultLevel,
            String timeScope, String startTime, String endTime
    ) {
        ResponseBean res = new ResponseBean();
        try {
            PageInfo<Map> pageInfo = tongjiService.zdhd(page, rows, sort, order, province, city, area, cultId, cultName, cultLevel, timeScope, startTime, endTime);
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
     * 站点培训
     * @return
     */
    @RequestMapping(value = "/zdpx")
    public ResponseBean zdpx(
            Integer page, Integer rows, String sort, String order,
            String province, String city, String area,
            String cultId, String cultName, String cultLevel,
            String timeScope, String startTime, String endTime
    ) {
        ResponseBean res = new ResponseBean();
        try {
            PageInfo<Map> pageInfo = tongjiService.zdpx(page, rows, sort, order, province, city, area, cultId, cultName, cultLevel, timeScope, startTime, endTime);
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
     * 站点场馆
     * @return
     */
    @RequestMapping(value = "/zdcg")
    public ResponseBean zdcg(
            Integer page, Integer rows, String sort, String order,
            String province, String city, String area,
            String cultId, String cultName, String cultLevel,
            String timeScope, String startTime, String endTime
    ) {
        ResponseBean res = new ResponseBean();
        try {
            PageInfo<Map> pageInfo = tongjiService.zdcg(page, rows, sort, order, province, city, area, cultId, cultName, cultLevel, timeScope, startTime, endTime);
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
     * 站点资讯
     * @return
     */
    @RequestMapping(value = "/zdzx")
    public ResponseBean zdzx(
            Integer page, Integer rows, String sort, String order,
            String province, String city, String area,
            String cultId, String cultName, String cultLevel,
            String timeScope, String startTime, String endTime
    ) {
        ResponseBean res = new ResponseBean();
        try {
            PageInfo<Map> pageInfo = tongjiService.zdzx(page, rows, sort, order, province, city, area, cultId, cultName, cultLevel, timeScope, startTime, endTime);
            res.setRows(pageInfo.getList());
            res.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }
}
