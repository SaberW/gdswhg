package com.creatoo.hn.controller.api.apioutside.information;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgInfColinfo;
import com.creatoo.hn.dao.model.WhgInfUpload;
import com.creatoo.hn.services.admin.information.WhgInfColInfoService;
import com.creatoo.hn.services.admin.information.WhgInfColumnService;
import com.creatoo.hn.services.admin.information.WhgInfUploadService;
import com.creatoo.hn.services.admin.resourse.WhgResourceService;
import com.creatoo.hn.services.admin.system.WhgSystemDeptService;
import com.creatoo.hn.services.api.apioutside.activity.ApiActivityService;
import com.creatoo.hn.services.api.apioutside.venue.ApiVenueService;
import com.creatoo.hn.services.api.apioutside.whglm.ApiWhglmService;
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.util.FilterFontUtil;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumProject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资讯接口
 */
@CrossOrigin
@RestController
@RequestMapping("/api/information")
public class ApiInformationController extends BaseController {



    @Autowired
    private ApiWhglmService apiWhglmService;

    @Autowired
    private ApiVenueService apiVenueService;

    @Autowired
    private WhgInfColInfoService whgInfColInfoService;
    @Autowired
    private WhgInfColumnService whgInfColumnService;

    @Autowired
    private WhgInfUploadService whgInfUploadService;


    @Autowired
    private WhgSystemDeptService deptService;
    /**
     * 资讯菜单栏
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/infcolumn", method = RequestMethod.POST)
    public ApiResultBean infcolumn(HttpServletRequest request,WebRequest webRequest) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        ApiResultBean apiResultBean = new ApiResultBean();
        String fromproject = getParam(request,"fromproject",EnumProject.PROJECT_WBGX.getValue());//所属项目
        List pageInfo = (List)whgInfColumnService.selectFrontColumn(null,fromproject);
        if(null == pageInfo){
            apiResultBean.setCode(1);
            apiResultBean.setMsg("获取资讯菜单栏失败");
            return apiResultBean;
        }
        apiResultBean.setRows(pageInfo);
        return apiResultBean;
    }

    /**
     * 资讯列表
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/infolist", method = RequestMethod.POST)
    public ApiResultBean phzs(HttpServletRequest request,WebRequest webRequest) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        ApiResultBean apiResultBean = new ApiResultBean();
        String clnftype = getParam(request,"type",null);//资讯类型
        String province= getParam(request,"province",null);
        String city= getParam(request,"city",null);
        String area= getParam(request,"area",null);
        String cultid = getParam(request,"cultid",null);//pageNo
        String deptid = getParam(request,"deptid",null);//部门id
        String index = getParam(request,"index","1");//pageNo
        String size = getParam(request,"size","12");//pageSize
        String relation = getParam(request,"relation",null);//相关推荐
        String searcheKey = getParam(request,"searcheKey",null);//搜索 关键字 标题 内容
        String fromproject = getParam(request,"fromproject",EnumProject.PROJECT_WBGX.getValue());//所属项目
        Map param = new HashMap();
        if(null != clnftype){
            param.put("clnftype", clnftype);
        }
        if(null != cultid){
            param.put("cultid", Arrays.asList(cultid.replaceAll(","," ").trim().split(" ")));
        }
        if(null != deptid){
            param.put("deptids",  deptService.srchDeptStrList(deptid));
        }
        if(null != searcheKey){
            param.put("searcheKey", searcheKey);
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
        if(null != relation){
            param.put("relation", relation);
        }
        param.put("page", index);
        param.put("rows", size);
        param.put("toproject",fromproject);
        param.put("delstate", 0);
        param.put("clnfstata", EnumBizState.STATE_PUB.getValue());
        PageInfo pageInfo = whgInfColInfoService.selfrontlist(param);
        if(null == pageInfo){
            apiResultBean.setCode(1);
            apiResultBean.setMsg("获取活动列表失败");
            return apiResultBean;
        }
        apiResultBean.setPageInfo(pageInfo);
        apiResultBean.setRows(pageInfo.getList());
        return apiResultBean;
    }


    /**
     * 资讯详情
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/infoldetail", method = RequestMethod.POST)
    public ApiResultBean infoldetail(HttpServletRequest request,WebRequest webRequest) throws Exception {
        ApiResultBean apiResultBean = new ApiResultBean();
        String type = getParam(request,"type",null);//活动类型
        String id = getParam(request,"id",null);//关键字
        WhgInfColinfo pageInfo = whgInfColInfoService.getInfo(id);
        if(pageInfo != null){//查询一次详情，增加一次资讯的访问量
            whgInfColInfoService.addViews(id);
        }
        List<WhgInfUpload> list = whgInfUploadService.selecup(id);
        apiResultBean.setData(FilterFontUtil.clearFont(pageInfo));
        apiResultBean.setRows(list);
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
