package com.creatoo.hn.controller.api.apioutside.whglm;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.services.admin.resourse.WhgResourceService;
import com.creatoo.hn.services.api.apioutside.activity.ApiActivityService;
import com.creatoo.hn.services.api.apioutside.venue.ApiVenueService;
import com.creatoo.hn.services.api.apioutside.whglm.ApiWhglmService;
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumProject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文化馆联盟接口
 */
@CrossOrigin
@RestController
@RequestMapping("/api/whglm")
public class ApiWhglmController extends BaseController {


    @Autowired
    private ApiWhglmService  apiWhglmService;

    /**
     * 大首页活动推荐
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/whglmList", method = RequestMethod.POST)
    public ApiResultBean actList(HttpServletRequest request,WebRequest webRequest){
        ApiResultBean apiResultBean = new ApiResultBean();
        String type = getParam(request,"type",null);//活动类型
        String keywords = getParam(request,"keywords",null);//关键字
        String district = getParam(request,"district",null);//区域
        String province= getParam(request,"province",null);
        String city= getParam(request,"city",null);
        String area= getParam(request,"area",null);
        String sdate = getParam(request,"sdate",null);//排序值
        String cultid = getParam(request,"cultid",null);//默认 省馆 文化馆id
        String index = getParam(request,"index","1");//pageNo
        String size = getParam(request,"size","12");//pageSize
        String sort = getParam(request,"sort","2");//排序
        Map param = new HashMap();
        if(null != type){
            param.put("etype", type);
        }
        if(null != province){
            param.put("province", province);
        }
        if(null != cultid){
            List<String> cultList=Arrays.asList(cultid.replaceAll(","," ").trim().split(" "));
            if(cultList!=null&&cultList.size()==1){
                param.put("cultid",cultList.get(0));
            }
            param.put("cultids",cultList);
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
            param.put("sort", sort);
        }
        PageInfo pageInfo = this.apiWhglmService.getWhglm(index,size,param,EnumProject.PROJECT_WBGX.getValue());
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
     * 文化馆详情
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/whgdetail", method = RequestMethod.POST)
    public ApiResultBean whgdetail(HttpServletRequest request,WebRequest webRequest){
        ApiResultBean apiResultBean = new ApiResultBean();
        String cultid = getParam(request,"cultid",null);//文化馆id
        WhgSysCult sysCult = null;
        if (cultid != null) {
            sysCult = (WhgSysCult) apiWhglmService.getWhgMenu(cultid);
        }
        if(null == sysCult){
            apiResultBean.setCode(1);
            apiResultBean.setMsg("获取文化馆详情失败");
            return apiResultBean;
        }
        apiResultBean.setData(sysCult);
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
