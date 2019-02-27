package com.creatoo.hn.controller.api.apioutside.digitalExhibition;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.admin.gather.WhgGatherService;
import com.creatoo.hn.services.admin.mylive.MyLiveService;
import com.creatoo.hn.services.admin.resourse.WhgResourceService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.system.WhgSystemDeptService;
import com.creatoo.hn.services.api.apioutside.activity.ApiActivityService;
import com.creatoo.hn.services.api.apioutside.digitalExhibition.ApiDigitalExhibitionService;
import com.creatoo.hn.util.FilterFontUtil;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumProject;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 数字展厅接口
 */
@CrossOrigin
@RestController
@RequestMapping("/api/digitalExhibition")
public class ApiDigitalExhController extends BaseController {

    @Autowired
    private ApiDigitalExhibitionService apiDigitalExhibitionService;

    @Autowired
    private WhgResourceService whgResourceService;

    @Autowired
    private WhgSystemDeptService deptService;


    /**
     * 查询数字展厅列表
     *
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResultBean exhList(HttpServletRequest request, WebRequest webRequest) {
        ApiResultBean apiResultBean = new ApiResultBean();
        String province = getParam(request, "province", null);
        String keywords = getParam(request, "keywords", null);//关键字
        String city = getParam(request, "city", null);
        String area = getParam(request, "area", null);
        String sdate = getParam(request, "sdate", null);//排序值
        String index = getParam(request, "index", "1");//pageNo
        String size = getParam(request, "size", "12");//pageSize
        String sort = getParam(request, "sort", "1");//排序
        String cultid = getParam(request, "cultid", null);//文化馆id
        String deptid = getParam(request, "deptid", null);//部门id
        String isrecommend = getParam(request, "isrecommend", null);//是否推荐
        String arttype = getParam(request, "arttype", null);
        Map param = new HashMap();
        if (null != cultid) {
            param.put("cultids", Arrays.asList(cultid.replaceAll(",", " ").trim().split(" ")));
        }
        if (null != province) {
            param.put("province", province);
        }
        if (null != city) {
            param.put("city", city);
        }
        if (null != deptid) {
            param.put("deptids", deptService.srchDeptStrList(deptid));
        }
        if (null != keywords) {
            param.put("keywords", keywords);
        }
        if (null != area) {
            param.put("area", area);
        }
        if (null != sort) {
            param.put("sort", sort);
        }
        if (null != isrecommend) {
            param.put("isrecommend", isrecommend);
        }
        if (null != arttype && !arttype.isEmpty()) {
            param.put("arttype", arttype);
        }
        PageInfo pageInfo = apiDigitalExhibitionService.getDigitalExhList(index, size, param);
        if (null == pageInfo) {
            apiResultBean.setCode(1);
            apiResultBean.setMsg("获取数据失败");
            return apiResultBean;
        }
        apiResultBean.setPageInfo(pageInfo);
        return apiResultBean;
    }

    /**
     * 查询活动详情
     *
     * @param request
     * @return
     * @throws Exception [{value: '110000',text: '北京市', children: [{value: '110101',text: '东城区' },{value: '2',text: '区' }]}]
     */
    @CrossOrigin
    @RequestMapping(value = "/detail", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResultBean detail(String id, WebRequest request) {
        ApiResultBean res = new ApiResultBean();
        Map<String, Object> param = new HashMap<>();
        try {
            if (id == null || "".equals(id)) {
                res.setCode(101);
                res.setMsg("活动Id不允许为空");//活动Id不允许为空
            } else {
                WhgDigitalExhibition actdetail1 = this.apiDigitalExhibitionService.getExhDetail(id);
                res.setData(FilterFontUtil.clearFont(actdetail1));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return res;
    }

    private String getParam(HttpServletRequest request, String paramName, String defaultValue) {
        String value = request.getParameter(paramName);
        if (null == value || value.trim().isEmpty()) {
            return defaultValue;
        }
        return value;
    }


}
