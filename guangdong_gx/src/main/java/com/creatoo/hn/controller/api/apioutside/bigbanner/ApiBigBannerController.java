package com.creatoo.hn.controller.api.apioutside.bigbanner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.admin.resourse.WhgResourceService;
import com.creatoo.hn.services.admin.system.WhgSystemDeptService;
import com.creatoo.hn.services.api.apioutside.activity.ApiActivityService;
import com.creatoo.hn.services.api.apioutside.venue.ApiVenueService;
import com.creatoo.hn.services.api.apioutside.whglm.ApiWhglmService;
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumProject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 大首页接口
 */
@CrossOrigin
@RestController
@RequestMapping("/api/bigbanner")
public class ApiBigBannerController extends BaseController {
    /**
     * 短信公开服务类
     */
    @Autowired
    private SMSService smsService;

    @Autowired
    private ApiActivityService apiActivityService;

    @Autowired
    private ApiWhglmService apiWhglmService;

    @Autowired
    private ApiVenueService apiVenueService;

    @Autowired
    private WhgResourceService whgResourceService;
    @Autowired
    private WhgSystemDeptService deptService;


    /**
     * 大首页活动推荐
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/bannerActList", method = RequestMethod.POST)
    public ApiResultBean actList(HttpServletRequest request,WebRequest webRequest){
        ApiResultBean apiResultBean = new ApiResultBean();
        String type = getParam(request,"type",null);//活动类型
        String keywords = getParam(request,"keywords",null);//关键字
        String district = getParam(request,"district",null);//区域
        String province= getParam(request,"province",null);
        String city= getParam(request,"city",null);
        String area= getParam(request,"area",null);
        String sdate = getParam(request,"sdate",null);//排序值
        String index = getParam(request,"index","1");//pageNo
        String size = getParam(request,"size","12");//pageSize
        String sort = getParam(request,"sort","3");//排序
        String iswechat = getParam(request, "iswechat", null);//是否是微信端请求
        String cultid = getParam(request, "cultid", null);//文化馆id
        String deptid = getParam(request, "deptid", null);//部门id
        Map param = new HashMap();
        if (null != iswechat) {
            if (null != cultid) {
                param.put("cultids", Arrays.asList(cultid.replaceAll(",", " ").trim().split(" ")));
            }
            if (null != deptid) {
                param.put("deptids", deptService.srchDeptStrList(deptid));
            }
        }
        if(null != type){
            param.put("etype", type);
        }
        if(null != province){
            param.put("province", province);
        }
        if(null != city){
            param.put("city", city);
        }
        if(null != area){
            param.put("area", area);
        }
        if(null != district){
            param.put("areaid", district);
        }
        if(null != sdate){
            param.put("sdate", sdate);
        }
        if(null != keywords){
            param.put("name", keywords);
        }
        if(null != sort){
            param.put("sort", Integer.parseInt(sort));
        }
        param.put("isbigbanner", 1);//推荐上大首页
        PageInfo pageInfo = apiActivityService.getActListForActFrontPage(index,size,param,EnumProject.PROJECT_WBGX.getValue());
        if(null == pageInfo){
            apiResultBean.setCode(1);
            apiResultBean.setMsg("获取活动列表失败");
            return apiResultBean;
        }
        List<Map<String,Object>> list = pageInfo.getList();
        List actList = apiActivityService.judgeCanSign(list);
        apiResultBean.setPageInfo(pageInfo);
        apiResultBean.setRows(actList);
        return apiResultBean;
    }

    /**
     * 排行榜指数
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/phbzs", method = RequestMethod.POST)
    public ApiResultBean phzs(HttpServletRequest request,WebRequest webRequest){
        ApiResultBean apiResultBean = new ApiResultBean();
        String type = getParam(request,"type",null);//活动类型
        String keywords = getParam(request,"keywords",null);//关键字
        String district = getParam(request,"district",null);//区域
        String province= getParam(request,"province",null);
        String city= getParam(request,"city",null);
        String area= getParam(request,"area",null);
        String index = getParam(request,"index","1");//pageNo
        String size = getParam(request,"size","12");//pageSize
        String sort = getParam(request,"sort","1");////排序 1 最新发布 2 最多参与 3 最多收藏 4 热门点击（点赞次数）
        String iswechat = getParam(request, "iswechat", null);//是否是微信端请求
        String cultid = getParam(request, "cultid", null);//文化馆id
        String deptid = getParam(request, "deptid", null);//部门id
        Map param = new HashMap();
        if (null != iswechat) {
            if (null != cultid) {
                param.put("cultids", Arrays.asList(cultid.replaceAll(",", " ").trim().split(" ")));
            }
            if (null != deptid) {
                param.put("deptids", deptService.srchDeptStrList(deptid));
            }
        }
        if(null != type){
            param.put("etype", type);
        }
        if(null != province){
            param.put("province", province);
        }
        if(null != city){
            param.put("city", city);
        }
        if(null != area){
            param.put("area", area);
        }
        if(null != district){
            param.put("areaid", district);
        }
        if(null != keywords){
            param.put("name", keywords);
        }
        if(null != sort){
            param.put("sort", sort);
        }
        PageInfo pageInfo = apiActivityService.getActListPhbFrontPage(index,size,param,EnumProject.PROJECT_WBGX.getValue());
        if(null == pageInfo){
            apiResultBean.setCode(1);
            apiResultBean.setMsg("获取活动列表失败");
            return apiResultBean;
        }
        List<Map<String,Object>> list = pageInfo.getList();
        List actList = apiActivityService.judgeCanSign(list);
        apiResultBean.setPageInfo(pageInfo);
        apiResultBean.setRows(actList);
        return apiResultBean;
    }


    /**
     * 热门场馆
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/hotVenue", method = RequestMethod.POST)
    public ApiResultBean hotVenue(HttpServletRequest request) throws Exception {
        ApiResultBean apiResultBean = new ApiResultBean();
        String type = getParam(request,"type",null);//活动类型
        String keywords = getParam(request,"keywords",null);//关键字
        String district = getParam(request,"district",null);//区域
        String province= getParam(request,"province",null);
        String city= getParam(request,"city",null);
        String area= getParam(request,"area",null);
        String index = getParam(request,"index","1");//pageNo
        String size = getParam(request,"size","12");//pageSize
        String iswechat = getParam(request, "iswechat", null);//是否是微信端请求
        String cultid = getParam(request, "cultid", null);//文化馆id
        String deptid = getParam(request, "deptid", null);//部门id
        Map param = new HashMap();
        if (null != iswechat) {
            if (null != cultid) {
                param.put("cultids", Arrays.asList(cultid.replaceAll(",", " ").trim().split(" ")));
            }
            if (null != deptid) {
                param.put("deptids", deptService.srchDeptStrList(deptid));
            }
        }
        if(null != type){
            param.put("etype", type);
        }
        if(null != province){
            param.put("province", province);
        }
        if(null != city){
            param.put("city", city);
        }
        if(null != area){
            param.put("area", area);
        }
        if(null != district){
            param.put("areaid", district);
        }
        if(null != keywords){
            param.put("name", keywords);
        }

        param.put("protype", EnumProject.PROJECT_WBGX.getValue());
        
        PageInfo pageInfo = apiVenueService.selectHotVenList4Page(Integer.parseInt(index),Integer.parseInt(size), param);
        apiResultBean.setPageInfo(pageInfo);
        apiResultBean.setRows(pageInfo.getList());

        return apiResultBean;
    }
    /**
     * 活跃分管
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/activeFG", method = {RequestMethod.POST, RequestMethod.GET})
    public ApiResultBean activeFG(HttpServletRequest request,WebRequest webRequest) throws Exception {
        ApiResultBean apiResultBean = new ApiResultBean();
        String type = getParam(request,"type",null);//活动类型
        String keywords = getParam(request,"keywords",null);//关键字
        String district = getParam(request,"district",null);//区域
        String province= getParam(request,"province",null);
        String city= getParam(request,"city",null);
        String area= getParam(request,"area",null);
        String index = getParam(request,"index","1");//pageNo
        String size = getParam(request,"size","12");//pageSize
        String iswechat = getParam(request, "iswechat", null);//是否是微信端请求
        String cultid = getParam(request, "cultid", null);//文化馆id
        String deptid = getParam(request, "deptid", null);//部门id
        Map param = new HashMap();
        /*if(null != iswechat){
            if(null != cultid){
                param.put("cultids", Arrays.asList(cultid.replaceAll(","," ").trim().split(" ")));
            }
            if(null != deptid){
                param.put("deptids", deptService.srchDeptStrList(deptid));
            }
        }*/
        if(null != type){
            param.put("etype", type);
        }
        if(null != province){
            param.put("province", province);
        }
        if(null != city){
            param.put("city", city);
        }
        if(null != area){
            param.put("area", area);
        }
        if(null != district){
            param.put("areaid", district);
        }
        if(null != keywords){
            param.put("name", keywords);
        }

        param.put("protype", EnumProject.PROJECT_WBGX.getValue());

        PageInfo pageInfo = apiWhglmService.getActiveFG(index,size,param,EnumProject.PROJECT_WBGX.getValue());
        apiResultBean.setPageInfo(pageInfo);
        apiResultBean.setRows(pageInfo.getList());

        return apiResultBean;
    }
    

    private String getParam(HttpServletRequest request,String paramName,String defaultValue){
        String value = request.getParameter(paramName);
        if(null == value || value.trim().isEmpty()){
            return defaultValue;
        }
        return value;
    }


}
