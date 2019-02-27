package com.creatoo.hn.controller.api.apioutside.whpp;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgActivity;
import com.creatoo.hn.dao.model.WhgResource;
import com.creatoo.hn.dao.model.WhgYwiWhpp;
import com.creatoo.hn.services.admin.activity.WhgActivityService;
import com.creatoo.hn.services.admin.resourse.WhgResourceService;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiWhppService;
import com.creatoo.hn.services.api.apioutside.activity.ApiActivityService;
import com.creatoo.hn.services.api.apioutside.venue.ApiVenueService;
import com.creatoo.hn.services.api.apioutside.whglm.ApiWhglmService;
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.util.FilterFontUtil;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumProject;
import com.creatoo.hn.util.enums.EnumTypeClazz;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.dsig.keyinfo.PGPData;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文化品牌
 */
@CrossOrigin
@RestController
@RequestMapping("/api/whpp")
public class ApiWhppController extends BaseController {
    /**
     * 文化品牌
     */
    @Autowired
    private WhgYunweiWhppService whgYunweiWhppService;

    /**
     * 文化活动
     */
    @Autowired
    private WhgActivityService whgActivityService;
    /**
     * 资源
     */
    @Autowired
    private WhgResourceService whgResourceService;

    @Autowired
    private ApiActivityService apiActivityService;


    /**
     * 文化品牌列表
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/pplist", method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResultBean pplist(HttpServletRequest request,WebRequest webRequest) throws Exception {
        ApiResultBean apiResultBean = new ApiResultBean();
        String cultid= getParam(request,"cultid",null);//文化馆id
        String province= getParam(request,"province",null);//文化馆id
        String city= getParam(request,"city",null);//文化馆id
        String area= getParam(request,"area",null);//文化馆id
        String index = getParam(request,"page","1");//pageNo
        String size = getParam(request,"pageSize","12");//pageSize
        String pptype = getParam(request,"pptype",null);//pptype
        String keywords = getParam(request,"keywords",null);//keywords
        Map map=new HashMap();
        if(null!=index){
            map.put("page",index);
        }
        if(null!=size){
            map.put("pageSize",size);
        }
        if(null!=cultid){
            map.put("cultid", Arrays.asList(cultid.replaceAll(","," ").trim().split(" ")));
        }
        if(null!=province){
            map.put("province",province);
        }
        if(null!=city){
            map.put("city",city);
        }
        if(null!=area){
            map.put("area",area);
        }
        if (null != pptype && pptype.matches("\\d+")) {
            map.put("pptype", Integer.valueOf(pptype));
        }
        if (null != keywords && !keywords.isEmpty()){
            map.put("keywords", keywords);
        }
        PageInfo<WhgYwiWhpp> list = whgYunweiWhppService.findBrand(map);
        if(null == list){
            apiResultBean.setCode(1);
            apiResultBean.setMsg("获取文化品牌列表失败");
            return apiResultBean;
        }
        apiResultBean.setPageInfo(list);
        return apiResultBean;
    }

    /**
     * 文化品牌活动列表
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/ppActList", method = RequestMethod.POST)
    public ApiResultBean ppActList(HttpServletRequest request,WebRequest webRequest) throws Exception {
        ApiResultBean apiResultBean = new ApiResultBean();
        String cultid= getParam(request,"cultid",null);//文化馆id
        String id= getParam(request,"id",null);//品牌id
        String index = getParam(request,"index","1");//pageNo
        String size = getParam(request,"size","12");//pageSize
        Map map=new HashMap();
        if(null!=id){
            map.put("brandId",id);
        }
        if(null!=cultid){
            map.put("cultid",cultid);
        }
        if(null!=index){
            map.put("page",index);
        }
        if(null!=size){
            map.put("rows",size);
        }
        PageInfo<WhgActivity> list = whgActivityService.findActivityBrand(map);
        if(null == list){
            apiResultBean.setCode(1);
            apiResultBean.setMsg("获取文化品牌活动列表失败");
            return apiResultBean;
        }
 /*       List<WhgActivity> actList = list.getList();
        List totalList = apiActivityService.judgeCanSign2(actList);
        apiResultBean.setPageInfo(list);
        apiResultBean.setRows(totalList);*/
        apiResultBean.setPageInfo(list);
        return apiResultBean;
    }

    /**
     * 文化品牌资源列表
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/ppResourceList", method = RequestMethod.POST)
    public ApiResultBean ppResourceList(HttpServletRequest request,WebRequest webRequest) throws Exception {
        ApiResultBean apiResultBean = new ApiResultBean();
        String cultid= getParam(request,"cultid",null);//文化馆id
        String id= getParam(request,"id",null);//品牌id
        String enttype= getParam(request,"type",null);//资源类型 1图片/2视频/3音频4/文档
        String index = getParam(request,"index",null);//pageNo
        String size = getParam(request,"size",null);//pageSize
        /*List<Map> list = whgResourceService.selectppSource(id,enttype, EnumTypeClazz.TYPE_ACTIVITY.getValue());
        if(null == list){
            apiResultBean.setCode(1);
            apiResultBean.setMsg("获取文化品牌活动列表失败");
            return apiResultBean;
        }
        apiResultBean.setPageInfo(new PageInfo(list));
        apiResultBean.setRows(list);
        return apiResultBean;*/

        try {
            int page = 0;
            int pageSize = 0;
            if (index!=null && index.matches("\\d+")){
                page = Integer.parseInt(index);
            }
            if (size!=null && size.matches("\\d+")){
                pageSize = Integer.parseInt(size);
            }
            String name = getParam(request,"name",null);

            PageInfo pageInfo = whgResourceService.selectPPandActSource(page, pageSize, id, enttype, name);
            apiResultBean.setPageInfo(pageInfo);
            apiResultBean.setRows(pageInfo.getList());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            apiResultBean.setCode(1);
            apiResultBean.setMsg("获取文化品牌活动列表失败");
        }
        return apiResultBean;
    }

    /**
     * 品牌详情
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/ppdetail", method = RequestMethod.POST)
    public ApiResultBean phzs(HttpServletRequest request,WebRequest webRequest) throws Exception {
        ApiResultBean apiResultBean = new ApiResultBean();
        String id = getParam(request,"id",null);
        WhgYwiWhpp pageInfo = whgYunweiWhppService.t_srchOne(id);
        if(null == pageInfo){
            apiResultBean.setCode(1);
            apiResultBean.setMsg("获取品牌详情失败");
            return apiResultBean;
        }
        apiResultBean.setData(FilterFontUtil.clearFont(pageInfo));
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
