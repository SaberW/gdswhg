package com.creatoo.hn.controller.admin.traindex;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.mapper.admin.TrainIndexTongjiMapper;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.trainindex.TrainIndexService;
import com.creatoo.hn.util.RequestUtils;
import com.creatoo.hn.util.WeekDayUtil;
import com.creatoo.hn.util.bean.ResponseBean;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 培训指数控制器
 * Created by wangxl on 2017/10/19.
 */
@RestController
@RequestMapping("/admin/traindex")
public class TrainIndexController extends BaseController {
    /**
     * 培训指数统计服务
     */
    @Autowired
    private TrainIndexService trainIndexService;

    /**
     * 培训指数视图
     * @param type xx-线下培训 zb-在线课程 wz-微专业 sz-师资 zy-资源
     * @return 对应的视图
     */
    @RequestMapping("/view/{type}")
    public ModelAndView view(HttpServletRequest request, @PathVariable("type") String type){
        ModelAndView view = new ModelAndView("admin/traindex/view_"+type);
        try {
            view.addObject("type", type);
            String cultid = RequestUtils.getParameter(request, "cultid");

            if(StringUtils.isEmpty(cultid)){
                JSONArray jsonArray = (JSONArray) request.getSession().getAttribute(Constant.SESSION_ADMIN_CULT);//权限分馆JSON格式
                if(jsonArray != null && jsonArray.size() > 0){
                    JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                    cultid = (String) jsonObject.get("id");
                }
            }

            Map<String, Integer> allTrainIndex = this.trainIndexService.countAllTrainIndex(cultid);
            view.addObject("cultid", cultid);
            view.addObject("allTrainIndex", allTrainIndex);
            view.addObject("curtDate", WeekDayUtil.getWeekStr(new Date()));

            //线下培训 //在线课程
            if("xx".equals(type) || "zb".equals(type)){
               Integer islive = "xx".equals(type) ? 0 : 1;
                //区域
                JSONArray areaData = this.trainIndexService.countTrainByArea(islive, cultid);
                view.addObject("areaData", areaData);
                //类型
                JSONArray typeData = this.trainIndexService.countTrainByType(islive, cultid);
                view.addObject("typeData", typeData);
                //月份
                JSONArray monthData = this.trainIndexService.countTrainByMonth(islive, cultid);
                view.addObject("monthData", monthData);
            }else if("sz".equals(type)){//培训师资
                //区域
                JSONArray areaData = this.trainIndexService.countTeacherByArea(cultid);
                view.addObject("areaData", areaData);
                //月份
                JSONArray monthData = this.trainIndexService.countTeacherByMonth(cultid);
                view.addObject("monthData", monthData);
            }else if("wz".equals(type)){//微专业
                //区域
                JSONArray areaData = this.trainIndexService.countMajorByArea(cultid);
                view.addObject("areaData", areaData);
                //月份
                JSONArray monthData = this.trainIndexService.countMajorByMonth(cultid);
                view.addObject("monthData", monthData);
            }else if("zy".equals(type)){//培训资源
                //区域
                JSONArray areaData = this.trainIndexService.countDrscByArea(cultid);
                view.addObject("areaData", areaData);
                //月份
                JSONArray monthData = this.trainIndexService.countDrscByMonth(cultid);
                view.addObject("monthData", monthData);
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return view;
    }

    /**
     * 统计培训访问量
     * @param request 请求对象
     * @return ResponseBean
     */
    @RequestMapping(value = "/srchList4p4visit")
    public ResponseBean srchList4p4visit(HttpServletRequest request){
        ResponseBean res = new ResponseBean();
        try {
            int page = RequestUtils.getPageParameter(request);
            int rows = RequestUtils.getRowsParameter(request);
            String cultid = RequestUtils.getParameter(request, "cultid");
            String title = RequestUtils.getParameter(request, "title");
            Integer islive = RequestUtils.getIntParameter(request, "islive");
            String sort = RequestUtils.getParameter(request, "sort");
            String order = RequestUtils.getParameter(request, "order");
            PageInfo<Map> pageInfo = this.trainIndexService.countTrainVisit(page, rows, islive, cultid, title, sort, order);
            res.setRows(pageInfo.getList());
            res.setTotal(pageInfo.getTotal());
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 统计培训报名数据
     * @param request 请求对象
     * @return ResponseBean
     */
    @RequestMapping(value = "/srchList4p4bm")
    public ResponseBean srchList4p4bm(HttpServletRequest request){
        ResponseBean res = new ResponseBean();
        try {
            int page = RequestUtils.getPageParameter(request);
            int rows = RequestUtils.getRowsParameter(request);
            String cultid = RequestUtils.getParameter(request, "cultid");
            String title = RequestUtils.getParameter(request, "title");
            Integer islive = RequestUtils.getIntParameter(request, "islive");
            String sort = RequestUtils.getParameter(request, "sort");
            String order = RequestUtils.getParameter(request, "order");
            PageInfo<Map> pageInfo = this.trainIndexService.countTrainData(page, rows, islive, cultid, title, sort, order);
            res.setRows(pageInfo.getList());
            res.setTotal(pageInfo.getTotal());
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }
}
