package com.creatoo.hn.controller.api.apitrain;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.admin.gather.WhgGatherService;
import com.creatoo.hn.services.admin.information.WhgInfUploadService;
import com.creatoo.hn.services.admin.mylive.MyLiveService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.system.WhgSystemDeptService;
import com.creatoo.hn.services.admin.trainindex.TrainIndexService;
import com.creatoo.hn.services.api.apitrain.ApiTraService;
import com.creatoo.hn.util.FilterFontUtil;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumProject;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 培训的接口
 * Created by Administrator on 2017/10/9.
 */
@CrossOrigin
@Controller
@RequestMapping("/api/px")
public class ApiTraController extends BaseController{
    /**
     * 直播服务
     */
    @Autowired
    private MyLiveService myLiveService;

    @Autowired
    private ApiTraService apiTraService;
    @Autowired
    private WhgGatherService whgGatherService;
    @Autowired
    private WhgInfUploadService whgInfUploadService;
    /**
     * 培训指数统计服务
     */
    @Autowired
    private TrainIndexService trainIndexService;

    @Autowired
    private WhgSystemDeptService whgSystemDeptService;

    /**
     * 文化馆服务
     */
    @Autowired
    private WhgSystemCultService whgSystemCultService;


    /**
     * 培训列表页
     * @param page 当前页
     * @param pageSize  页列表数量
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/list",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean indexData(HttpServletRequest req,
                                   @RequestParam(value = "page", defaultValue = "1")int page,
                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        ApiResultBean rep = new ApiResultBean();
        String cultid=req.getParameter("cultid");
        Map recode = new HashMap();

        try {
            //区分全省、全市站
            if (cultid == null || cultid.isEmpty()){
                recode.put("allprovince", true);
            }
            if (cultid != null && cultid.contains(",")){
                recode.put("allcity", true);
            }

            recode.put("protype", EnumProject.PROJECT_WLPX.getValue());
            if(cultid!=null&&!cultid.equals("")){
                recode.put("cultid",  Arrays.asList(cultid.replaceAll(","," ").trim().split(" ")));
            }
            String deptid = req.getParameter("deptid");
            if(deptid !=null && !"".equals(deptid)){
                recode.put("deptid", whgSystemDeptService.srchDeptStrList(deptid));
            }
            recode.put("arttype", req.getParameter("arttype"));
            recode.put("etype", req.getParameter("etype"));
            recode.put("title", req.getParameter("title"));
            recode.put("userid", req.getParameter("userid"));
            recode.put("province", req.getParameter("province"));
            recode.put("city", req.getParameter("city"));
            recode.put("area", req.getParameter("area"));
            recode.put("sort", req.getParameter("sort"));
            recode.put("now", new Date());
            recode.put("title", req.getParameter("title"));

            PageInfo info = this.apiTraService.getTraList(page,pageSize,recode);
            rep.setData(new Date());
            rep.setPageInfo(info);
            rep.setRows(info.getList());
        } catch (Exception e) {
            rep.setCode(101);
            rep.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return rep;
    }


    /**
     * 培训详情
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/detail",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean traDetail(@RequestParam(value = "id",required = false)String id,@RequestParam(value = "userid",required = false)String userid){
        ApiResultBean arb = new ApiResultBean();
        if(null == id){
            arb.setCode(101);
            arb.setMsg("培训ID不能为空");
            return arb;
        }
        try {
            Map whgTra = (Map)apiTraService.getTraById(id,userid);
            WhgGather whgGather = whgGatherService.getGatherByRefId(id);
            Map<String,Object> param = new HashMap<>();
            if(whgGather!=null){
                param.put("gatherid", whgGather.getId());
            }
            //培训主办单位
            String host = (String) whgTra.get("host");
            String cultid = (String) whgTra.get("cultid");
            if(StringUtils.isEmpty(host) && StringUtils.isNotEmpty(cultid)){
                WhgSysCult thisCult = this.whgSystemCultService.t_srchOne(cultid);
                host = thisCult.getName();
            }
            whgTra.put("host", host);

            param.put("tra",FilterFontUtil.clearFont4Tra(whgTra));

            //培训组限制报名
            String groupid = (String) ((Map)whgTra).get("groupid");
            param.put("limitEnrolForTrainGroup", apiTraService.limitEnrolForTrainGroup(groupid, userid));

            //线下培训直播数据
            WhgLiveComm live = this.myLiveService.t_srchOneByTraid(id);
            Map<String, String> liveMap = this.myLiveService.findActOrTraLiveInfo(live);
            param.put("liveInfo", liveMap);

            List<WhgInfUpload> list = whgInfUploadService.selecup(id);//资料列表
            arb.setRows(list);
            arb.setData(param);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            arb.setCode(102);
            arb.setMsg("查询数据失败");
        }
        return arb;
    }

    /**
     * 资源
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/resource",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean getResource(@RequestParam(value = "id",required = false)String id,@RequestParam(value = "enttype",required = false)String enttype){
        ApiResultBean arb = new ApiResultBean();
        if(null == id){
            arb.setCode(101);
            arb.setMsg("培训ID不能为空");
            return arb;
        }
        try {
            List<Map> list = apiTraService.t_getResource(id,enttype);

            arb.setData(list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            arb.setCode(102);
            arb.setMsg("查询数据失败");
        }
        return arb;
    }

    /**
     *课程表
     * @param id
     * @return
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping("/course")
    public ApiResultBean getCourse(@RequestParam(value = "id",required = false)String id,
                                   @RequestParam(value = "page", defaultValue = "1")int page,
                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        ApiResultBean arb = new ApiResultBean();
        try {
            PageInfo list = this.apiTraService.getCourseByTraId(id,page,pageSize);
            arb.setPageInfo(list);
            arb.setRows(list.getList());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            arb.setCode(102);
            arb.setMsg("查询数据失败");
        }
        return arb;
    }

    /**
     *培训指数
     * @return
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping("/pxzs")
    public ApiResultBean pxzs(HttpServletRequest req){
        ApiResultBean arb = new ApiResultBean();
        String cultid=getParam(req,"cultid",null);
        Map recode = new HashMap();
        if (cultid != null && !cultid.equals("")) {
            recode.put("cultids", Arrays.asList(cultid.replaceAll(",", " ").trim().split(" ")));
        }
        try {
            Map<String, Integer> allTrainIndex = this.trainIndexService.countAllTrainIndex(recode);
            arb.setData(allTrainIndex);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            arb.setCode(102);
            arb.setMsg("查询数据失败");
        }
        return arb;
    }


    /**
     * 获取推荐培训
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/getRecommendTra",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean getRecommendTra(@RequestParam(value = "id",required = true)String id,
                                         @RequestParam(value = "cultid",required = false)String cultid,
                                         @RequestParam(value = "size",required = false)Integer size){
        ApiResultBean arb = new ApiResultBean();
        try {
            PageInfo pageInfo = apiTraService.getRecommendTra(id,cultid,size);
            arb.setRows(pageInfo.getList());
            arb.setPageInfo(pageInfo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            arb.setCode(101);
            arb.setMsg("查询数据失败");
        }
        return arb;
    }

    /**
     * 获取培训人员
     * @param id 培训id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/getEnrolPerson",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean getEnrolPerson(@RequestParam(value = "id",required = true)String id){
        ApiResultBean arb = new ApiResultBean();
        try {
            List<Map> list = apiTraService.t_getEnrolPerson(id);
            arb.setData(list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            arb.setCode(101);
            arb.setMsg("查询数据失败");
        }
        return arb;
    }

    /**
     * 检查报名
     * @param userid
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/checkEnrol",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean checkTrainApply(@RequestParam(value = "userid",required = false)String userid, @RequestParam(value = "id",required = true)String id){
        ApiResultBean arb = new ApiResultBean();
        try {
            arb = apiTraService.checkApplyTrain(id,userid);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            arb.setCode(101);
            arb.setMsg("数据查询失败");
        }
        return arb;
    }

    /**
     * 培训报名
     * @param userid
     * @param traid
     * @param name
     * @param birthday
     * @param idCard
     * @param mobile
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/enrol",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean saveTraEnrol(@RequestParam(value = "userid",required = true)String userid,
                                        @RequestParam(value = "traid",required = true)String traid,
                                        @RequestParam(value = "name",required = true)String name,
                                        @RequestParam(value = "birthday",required = true)String birthday,
                                        @RequestParam(value = "idCard",required = true)String idCard,
                                        @RequestParam(value = "mobile",required = true)String mobile){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ApiResultBean arb = new ApiResultBean();
        WhgTraEnrol enrol = new WhgTraEnrol();
        try {
            enrol.setBirthday(sdf.parse(birthday));
            enrol.setTraid(traid);
            enrol.setRealname(name);
            enrol.setCardno(idCard);
            enrol.setContactphone(mobile);
            arb = apiTraService.syncAddTranEnrol(enrol, userid);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            arb.setCode(101);
            arb.setMsg("保存失败");
        }
        return arb;
    }

    /**
     * 培训列表页的状态
     * @param userid
     * @param traid
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/checkState",method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResultBean checkState(@RequestParam(value = "userid",required = false)String userid,
                                   @RequestParam(value = "traid",required = true)String traid){
        ApiResultBean rsb = new ApiResultBean();
        try {
            rsb = this.apiTraService.t_checkState(userid,traid);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            rsb.setCode(101);
            rsb.setMsg("查询失败");
        }
        return rsb;
    }

    private String getParam(HttpServletRequest request,String paramName,String defaultValue){
        String value = request.getParameter(paramName);
        if(null == value || value.trim().isEmpty()){
            return defaultValue;
        }
        return value;
    }

    /**
     * 微信热门推荐
     * @param page 当前页
     * @param pageSize  页列表数量
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/hotRecom",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean hotRecommend(HttpServletRequest req,
                                   @RequestParam(value = "page", defaultValue = "1")int page,
                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        ApiResultBean rep = new ApiResultBean();
        String cultid=req.getParameter("cultid");
        Map recode = new HashMap();

        try {
            recode.put("protype", EnumProject.PROJECT_WLPX.getValue());
            if(cultid!=null&&!cultid.equals("")){
                recode.put("cultid",  Arrays.asList(cultid.replaceAll(","," ").trim().split(" ")));
            }
            String deptid = req.getParameter("deptid");
            if(deptid !=null && !"".equals(deptid)){
                recode.put("deptid", whgSystemDeptService.srchDeptStrList(deptid));
            }
            recode.put("arttype", req.getParameter("arttype"));
            recode.put("etype", req.getParameter("etype"));
            recode.put("title", req.getParameter("title"));
            recode.put("userid", req.getParameter("userid"));
            recode.put("province", req.getParameter("province"));
            recode.put("city", req.getParameter("city"));
            recode.put("area", req.getParameter("area"));
            recode.put("sort", req.getParameter("sort"));
            recode.put("title", req.getParameter("title"));
            recode.put("now", new Date());

            PageInfo info = this.apiTraService.t_hotRecommend(page,pageSize,recode);
            rep.setData(new Date());
            rep.setPageInfo(info);
            rep.setRows(info.getList());
        } catch (Exception e) {
            rep.setCode(101);
            rep.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return rep;
    }
}
