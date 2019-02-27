package com.creatoo.hn.controller.api.apiinside;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgActivity;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.api.apiinside.InsPersonnelService;
import com.creatoo.hn.services.api.apioutside.activity.ApiActivityService;
import com.creatoo.hn.util.FilterFontUtil;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumProject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
@Controller
@CrossOrigin
@RequestMapping("/api/inside")
public class InsActivityController extends BaseController{

    @Autowired
    private InsPersonnelService insPersonnelService;

    @Autowired
    private ApiActivityService apiActivityService;
    @Autowired
    private WhgSystemCultService whgSystemCultService;
    /**
     * 查询活动列表
     * @param req
     * @param page
     * @param pageSize
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/activity/list", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object activityList(HttpServletRequest request,
                             @RequestParam(value = "page", defaultValue = "1", required = false)int page,
                             @RequestParam(value = "pageSize", defaultValue = "10", required = false)int pageSize){
        ApiResultBean arb = new ApiResultBean();
        try {
            Map recode = new HashMap();

            String type = getParam(request,"type",null);//活动类型
            String arttype = getParam(request,"arttype",null);//活动类型
            String province= getParam(request,"province",null);
            String psprovince= getParam(request,"psprovince",null);
            String pscity= getParam(request,"pscity",null);
            String city= getParam(request,"city",null);
            String area= getParam(request,"area",null);
            String index = getParam(request,"index","1");//pageNo
            String size = getParam(request,"size","12");//pageSize
            String sort = getParam(request,"sort","1");//排序
            String cultid = getParam(request,"cultid",null);//文化馆id

            if(cultid!=null){
                recode.put("cultid", Arrays.asList(cultid.replaceAll(","," ").trim().split(" ")));
            }
            if(psprovince!=null){
                recode.put("psprovince",psprovince);//配送区域
            }
            if(pscity!=null){
                recode.put("pscity",pscity);//配送区域
            }
            if(area!=null){
                recode.put("area",area);
            }
            if(city!=null){
                recode.put("city",city);
            }
            if(province!=null){
                recode.put("province",province);
            }
            if(type!=null){
                recode.put("etype", type);
            }
            if(arttype!=null){
                recode.put("arttype", arttype);
            }
            recode.put("sort",1);
            PageInfo pageInfo = this.apiActivityService.getActListForActFrontPage(page+"",pageSize+"",recode,EnumProject.PROJECT_NBGX.getValue());
            arb.setPageInfo(pageInfo);
            arb.setRows(pageInfo.getList());
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }

        return arb;
    }


    /**
     * 查询活动信息
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/activity/info", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object getActivity(String id){
        ApiResultBean arb = new ApiResultBean();
        try {
            WhgActivity info = this.apiActivityService.getActDetail(id);
            if(info.getCultid()!=null){
                WhgSysCult cult=whgSystemCultService.t_srchOne(info.getCultid());
                if(cult!=null){
                    info.setDeptid(cult.getName());// 借部门id存储文化馆名称 供前端展示
                }
            }
            arb.setData(FilterFontUtil.clearFont(info));
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(),e);
        }
        return arb;
    }

    /**
     * 相关人才的推荐列表
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/activity/recommends", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object getRecommends(HttpServletRequest req,String id, @RequestParam(value = "page", defaultValue = "1", required = false)int page,
                                @RequestParam(value = "pageSize", defaultValue = "10", required = false)int pageSize,
                                @RequestParam(value = "cultid", required = false)String cultid){
        ApiResultBean arb = new ApiResultBean();
        try {
            Map recode = new HashMap();
            if(req.getParameter("cultid")!=null&&!req.getParameter("cultid").equals("")){
                recode.put("cultid", req.getParameter("cultid"));
            }
            String protype = EnumProject.PROJECT_NBGX.getValue();
            recode.put("sort",1);
            recode.put("isrecommend",1);
            PageInfo pageInfo = this.apiActivityService.getActListForActFrontPage(page+"",pageSize+"",recode,EnumProject.PROJECT_NBGX.getValue());
            arb.setPageInfo(pageInfo);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
        }
        return arb;
    }

    private String getParam(HttpServletRequest request,String paramName,String defaultValue){
        String value = request.getParameter(paramName);
        if(null == value || value.trim().isEmpty()){
            return defaultValue;
        }
        return value;
    }
}
