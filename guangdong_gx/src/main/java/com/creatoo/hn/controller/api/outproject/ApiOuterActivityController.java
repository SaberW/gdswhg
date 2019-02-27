package com.creatoo.hn.controller.api.outproject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.admin.activity.WhgActivityService;
import com.creatoo.hn.services.admin.gather.WhgGatherService;
import com.creatoo.hn.services.admin.resourse.WhgResourceService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.system.WhgSystemDeptService;
import com.creatoo.hn.services.admin.user.WhgBlackListService;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiWhppService;
import com.creatoo.hn.services.api.apioutside.activity.ApiActivityService;
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
 * 活动预定接口
 */
@CrossOrigin
@RestController
@RequestMapping("/api/outer/activity")
public class ApiOuterActivityController extends BaseController {
    /**
     * 短信公开服务类
     */
    @Autowired
    private WhgGatherService whgGatherService;

    @Autowired
    private ApiActivityService apiActivityService;

    @Autowired
    private WhgActivityService whgActivityService;

    @Autowired
    private WhgResourceService whgResourceService;
    @Autowired
    private WhgSystemDeptService deptService;

    @Autowired
    private WhgYunweiWhppService whgYunweiWhppService;
    /**
     * 文化馆服务
     */
    @Autowired
    private WhgSystemCultService whgSystemCultService;



    /**
     * 查询活动列表
     *
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/list", method = {RequestMethod.POST, RequestMethod.GET})
    public ApiResultBean actList(HttpServletRequest request, WebRequest webRequest) {
        ApiResultBean apiResultBean = new ApiResultBean();
        String type = getParam(request, "type", null);//活动类型
        String keywords = getParam(request, "keywords", null);//关键字
        String district = getParam(request, "district", null);//区域
        String province = getParam(request, "province", null);
        String city = getParam(request, "city", null);
        String area = getParam(request, "area", null);
        String sdate = getParam(request, "sdate", null);//排序值
        String index = getParam(request, "page", "1");//pageNo
        String size = getParam(request, "rows", "12");//pageSize
        String sort = getParam(request, "sort", "1");//排序
        String cultid = getParam(request, "cultid", null);//文化馆id
        String deptid = getParam(request, "deptid", null);//部门id
        String isrecommend = getParam(request, "isrecommend", null);//是否推荐
        String arttype = getParam(request, "arttype", null);
        String cultsite = getParam(request, "cultsite", null);
        try {
            Map param = new HashMap();
            WhgSysCult cult = whgSystemCultService.t_srchOneBySite(cultsite);
            if (cult != null) {
                param.put("cultids", Arrays.asList(cult.getId()));
            } else {
                apiResultBean.setCode(1);
                apiResultBean.setMsg("文化馆站点有误！");
                return apiResultBean;
            }
            if (null != type) {
                param.put("etype", type);
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
            if (null != area) {
                param.put("area", area);
            }
            if (null != district) {
                param.put("areaid", district);
            }
            if (null != sdate) {
                param.put("sdate", sdate);
            }
            if (null != keywords) {
                param.put("name", keywords);
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
            PageInfo pageInfo = apiActivityService.getActListForActFrontPage(index, size, param, EnumProject.PROJECT_WBGX.getValue());
            if (null == pageInfo) {
                apiResultBean.setCode(1);
                apiResultBean.setMsg("获取活动列表失败");
                return apiResultBean;
            }
            List<Map<String, Object>> list = pageInfo.getList();
            List actList = apiActivityService.judgeCanSign(list);
            apiResultBean.setPageInfo(pageInfo);
            apiResultBean.setRows(actList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiResultBean;
    }

    /**
     * 查询活动详情
     *
     */
    @CrossOrigin
    @RequestMapping(value = "/detail", method = {RequestMethod.POST, RequestMethod.GET})
    public ApiResultBean detail(String actId, @RequestParam(value = "cultsite", required = false, defaultValue = "") String cultsite) {
        ApiResultBean res = new ApiResultBean();
        Map<String, Object> param = new HashMap<>();
        try {
            if (actId == null || "".equals(actId)) {
                res.setCode(101);
                res.setMsg("活动Id不允许为空");//活动Id不允许为空
            } else {

                //活动详情
                WhgActivity actdetail1 = this.apiActivityService.getActDetail(actId);
                actdetail1 = FilterFontUtil.clearFont(actdetail1);
                //判断活动报名时间
                Date enterstrtime = actdetail1.getEnterstrtime();
                Date enterendtime = actdetail1.getEnterendtime();
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(actdetail1));
                //添加主办方-没有值的时候取所属文化馆
                String host = actdetail1.getHost();
                if (StringUtils.isEmpty(host) && StringUtils.isNotEmpty(actdetail1.getCultid())) {
                    WhgSysCult thisCult = this.whgSystemCultService.t_srchOne(actdetail1.getCultid());
                    host = thisCult.getName();
                }
                jsonObject.put("host", host);

                Integer canSign = apiActivityService.canSign(actdetail1, enterstrtime, enterendtime);
                if (canSign == 0) {//可报名:报名时间内
                    if (actdetail1.getSellticket() == 1) {
                        param.put("liststate", 3);    //直接前往：在报名时间内 又是不可预订类型 就直接前往
                    } else {
                        int ticketCounts = apiActivityService.getWhgActTicketCountsActId(actId);
                        int actCounts = apiActivityService.getActTicketList(actId);//该活动总票数
                        param.put("ticketAllnum", actCounts);
                        if (actCounts - ticketCounts <= 0) {
                            param.put("liststate", 2);//已订完
                        } else {
                            param.put("liststate", 1);
                            param.put("ticketnum", actCounts - ticketCounts);
                        }
                    }
                } else if (canSign == 104) {//即将开始：还未到报名时间
                    param.put("liststate", 5);
                } else if (canSign == 105) {//可报
                    param.put("liststate", 4);//已结束
                }
                List _list = new ArrayList();
                WhgSysCult cult = whgSystemCultService.t_srchOneBySite(cultsite);
                if (cult != null) {
                    _list = Arrays.asList(cult.getId());
                }
                List<WhgActivity> acttj = this.apiActivityService.acttjianfortz(actId, _list);
                List<Object> userList = new ArrayList<Object>();
                //活动场次信息
                List<String> dateList = apiActivityService.getActDate(actId);
                List myTimeList = apiActivityService.getTimesAct(dateList, actId);
                WhgGather whgGather = whgGatherService.getGatherByRefId(actId);
                if (whgGather != null) {
                    param.put("gatherid", whgGather.getId());
                }
                param.put("timeList", myTimeList);
                param.put("actdetail", jsonObject);
                param.put("acttj", acttj);
                param.put("date", new Date());
                res.setData(param);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 文化品牌列表
     *
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/hdpplist", method = RequestMethod.POST)
    public ApiResultBean pplist(HttpServletRequest request, WebRequest webRequest) throws Exception {
        ApiResultBean apiResultBean = new ApiResultBean();
        String cultid = getParam(request, "cultid", null);//文化馆id
        String province = getParam(request, "province", null);//文化馆id
        String city = getParam(request, "city", null);//文化馆id
        String area = getParam(request, "area", null);//文化馆id
        String index = getParam(request, "page", "1");//pageNo
        String size = getParam(request, "pageSize", "12");//pageSize
        String pptype = getParam(request, "pptype", null);//pptype
        String cultsite = getParam(request, "cultsite", null);
        try {
            Map map = new HashMap();
            if (null != index) {
                map.put("page", index);
            }
            if (null != size) {
                map.put("pageSize", size);
            }
            WhgSysCult cult = whgSystemCultService.t_srchOneBySite(cultsite);
            if (cult != null) {
                map.put("cultid", Arrays.asList(cult.getId()));
            }
            if (null != province) {
                map.put("province", province);
            }
            if (null != city) {
                map.put("city", city);
            }
            if (null != area) {
                map.put("area", area);
            }
            if (null != pptype && pptype.matches("\\d+")) {
                map.put("pptype", Integer.valueOf(pptype));
            }
            PageInfo<WhgYwiWhpp> list = whgYunweiWhppService.findBrand(map);
            if (null == list) {
                apiResultBean.setCode(1);
                apiResultBean.setMsg("获取文化品牌列表失败");
                return apiResultBean;
            }
            apiResultBean.setPageInfo(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiResultBean;
    }

    /**
     * 品牌详情
     *
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/ppdetail", method = RequestMethod.POST)
    public ApiResultBean phzs(HttpServletRequest request, WebRequest webRequest) throws Exception {
        ApiResultBean apiResultBean = new ApiResultBean();
        String id = getParam(request, "id", null);
        WhgYwiWhpp pageInfo = whgYunweiWhppService.t_srchOne(id);
        if (null == pageInfo) {
            apiResultBean.setCode(1);
            apiResultBean.setMsg("获取品牌详情失败");
            return apiResultBean;
        }
        apiResultBean.setData(pageInfo);
        return apiResultBean;
    }

    /**
     * 文化品牌活动列表
     *
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/ppActList", method = RequestMethod.POST)
    public ApiResultBean ppActList(HttpServletRequest request, WebRequest webRequest) throws Exception {
        ApiResultBean apiResultBean = new ApiResultBean();
        String cultid = getParam(request, "cultid", null);//文化馆id
        String id = getParam(request, "id", null);//品牌id
        String index = getParam(request, "index", "1");//pageNo
        String size = getParam(request, "size", "12");//pageSize
        Map map = new HashMap();
        if (null != id) {
            map.put("brandId", id);
        }
        if (null != cultid) {
            map.put("cultid", cultid);
        }
        if (null != index) {
            map.put("page", index);
        }
        if (null != size) {
            map.put("rows", size);
        }
        PageInfo<WhgActivity> list = whgActivityService.findActivityBrand(map);
        if (null == list) {
            apiResultBean.setCode(1);
            apiResultBean.setMsg("获取文化品牌活动列表失败");
            return apiResultBean;
        }
        List<WhgActivity> actList = list.getList();
        List totalList = apiActivityService.judgeCanSign2(actList);
        apiResultBean.setPageInfo(list);
        apiResultBean.setRows(totalList);
        return apiResultBean;
    }



    /***
     * 获取我的活动
     * @return
     */
    @SuppressWarnings("unused")
    @CrossOrigin
    @RequestMapping(value = "/getMyActList", method = RequestMethod.POST)
    public ApiResultBean activity(int index, int size, String type, String userId) {
        ApiResultBean rme = new ApiResultBean();
        if (null == userId) {
            rme.setCode(101);
            rme.setMsg("获取用户活动订单失败");
            return rme;
        }
        PageInfo pageInfo = this.apiActivityService.getOrderForCenter(index, size, Integer.valueOf(type), userId);
        Map<String, Object> param = new HashMap<>();
        rme.setPageInfo(pageInfo);
        rme.setRows(pageInfo.getList());
        param.put("time", new Date());
        rme.setData(param);
        return rme;
    }

    /**
     * 删除个人中心活动订单
     *
     * @param orderId 活动订单id
     * @return res
     */
    @CrossOrigin
    @RequestMapping(value = "/delMyAct", method = RequestMethod.POST)
    public ApiResultBean delMyAct(String orderId) {
        ApiResultBean rme = new ApiResultBean();
        rme.setCode(apiActivityService.delMyAct(orderId));
        if (0 != rme.getCode()) {
            rme.setMsg("删除失败");
        }
        return rme;
    }

    private String getParam(HttpServletRequest request, String paramName, String defaultValue) {
        String value = request.getParameter(paramName);
        if (null == value || value.trim().isEmpty()) {
            return defaultValue;
        }
        return value;
    }

    /**
     * 获取资源
     *
     * @param id      活动id
     * @param reftype 类型
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @CrossOrigin
    @RequestMapping(value = "/resource", method = RequestMethod.POST)
    public ApiResultBean loadResource(String id, String reftype) {
        ApiResultBean res = new ApiResultBean();
        Map<String, Object> rest = null;
        try {
            rest = new HashMap();
            if (id != null && !"".equals(id) && reftype != null && !"".equals(reftype)) {
                //活动图片
                List<WhgResource> tsource = this.whgResourceService.selectactSource(id, "1", reftype);
                //活动资源 音频
                List<WhgResource> ysource = this.whgResourceService.selectactSource(id, "3", reftype);
                //活动资源 视频
                List<WhgResource> ssource = this.whgResourceService.selectactSource(id, "2", reftype);
                rest.put("tsource", tsource);
                rest.put("tsource", tsource);
                rest.put("ysource", ysource);
                rest.put("ssource", ssource);
                rest.put("code", 0);
            } else {
                res.setMsg("参数错误");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        res.setData(rest);
        return res;
    }

}
