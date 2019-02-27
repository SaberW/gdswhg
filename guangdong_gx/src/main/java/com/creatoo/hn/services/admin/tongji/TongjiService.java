package com.creatoo.hn.services.admin.tongji;

import com.creatoo.hn.dao.mapper.admin.TongjiMapper;
import com.creatoo.hn.dao.model.WhgActivity;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.activity.WhgActivityService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 总分馆统计服务
 */
@Service
public class TongjiService extends BaseService {
    /**
     * 活动服务
     */
    @Autowired
    private WhgActivityService whgActivityService;

    /**
     * 文化馆服务
     */
    @Autowired
    private WhgSystemCultService whgSystemCultService;

    /**
     * 统计Mapper类
     */
    @Autowired
    private TongjiMapper tongjiMapper;

    /**
     * 解析参数
     * @param sort 排序字段
     * @param order 排序方式
     * @param province 省
     * @param city 市
     * @param area 区
     * @param cultId 文化馆标识
     * @param actName 搜索名称
     * @param timeScope 时间范围
     * @param startTime 开发时间
     * @param endTime 结束时间
     * @return Map<String, Object>
     * @throws Exception
     */
    private Map<String, Object> getParamMap(String sort, String order,
                                        String province, String city, String area,
                                        String cultId,
                                        String actName,
                                        String timeScope, String startTime, String endTime,
                                        String cultLevel
    )throws Exception{
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if(StringUtils.isNotEmpty(cultId)){
            paramMap.put("cultid", cultId);
        }
        if(StringUtils.isNotEmpty(province)){
            paramMap.put("province", province);
        }
        if(StringUtils.isNotEmpty(city)){
            paramMap.put("city", city);
        }
        if(StringUtils.isNotEmpty(province)){
            paramMap.put("area", area);
        }
        if(StringUtils.isNotEmpty(actName)){
            paramMap.put("name", "%"+actName+"%");
        }
        if(StringUtils.isNotEmpty(cultLevel)){
            paramMap.put("level", cultLevel);
        }
        if(StringUtils.isNotEmpty(timeScope)){
            paramMap.put("timeScope", timeScope);
            if("3".equals(timeScope)){
                if(StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)){
                    paramMap.put("stime", startTime);
                    paramMap.put("etime", endTime);
                }else{
                    paramMap.remove("timeScope");
                }
            }
        }
        if(StringUtils.isNotEmpty(sort)){
            paramMap.put("sort", sort);
            if(StringUtils.isNotEmpty(order)){
                paramMap.put("order", order);
            }
        }
        return paramMap;
    }

    /**
     * 活动排行
     * @throws Exception
     */
    public PageInfo<Map> hdph(int page, int rows, String sort, String order,
                     String province, String city, String area,
                     String cultId,
                     String actName,
                     String timeScope, String startTime, String endTime
    )throws Exception{
        //参数
        Map<String, Object> paramMap = getParamMap(sort, order, province, city, area, cultId, actName, timeScope, startTime, endTime, null);

        //分页查询
        PageHelper.startPage(page, rows);
        List<Map> list = this.tongjiMapper.hdph(paramMap);
        PageInfo<Map> pageInfo = new PageInfo<Map>(list);

        //数据处理
        if(pageInfo.getTotal() > 0){
            for(Map row : pageInfo.getList()){
                BigDecimal bd_ratebook = (BigDecimal)row.get("ratebook");
                BigDecimal bd_rateseat = (BigDecimal)row.get("rateseat");
                row.remove("ratebook");
                row.remove("rateseat");
                row.put("ratebook", bd_ratebook.toString());//订票率
                row.put("rateseat", bd_rateseat.toString());//上座率
            }
        }
        return pageInfo;
    }

    /**
     * 培训排行
     * @throws Exception
     */
    public PageInfo<Map> pxph(int page, int rows, String sort, String order,
                              String province, String city, String area,
                              String cultId,
                              String traName,
                              String timeScope, String startTime, String endTime
    )throws Exception{
        //参数
        Map<String, Object> paramMap = getParamMap(sort, order, province, city, area, cultId, traName, timeScope, startTime, endTime, null);

        //分页查询
        PageHelper.startPage(page, rows);
        List<Map> list = this.tongjiMapper.pxph(paramMap);
        PageInfo<Map> pageInfo = new PageInfo<Map>(list);

        //数据处理
        if(pageInfo.getTotal() > 0){
            for(Map row : pageInfo.getList()){
                BigDecimal bd_ratebook = (BigDecimal)row.get("ratebook");
                BigDecimal bd_ratesign = (BigDecimal)row.get("ratesign");
                row.remove("ratebook");
                row.remove("ratesign");
                row.put("ratebook", bd_ratebook.toString());//报名率
                row.put("ratesign", bd_ratesign.toString());//签到率
            }
        }
        return pageInfo;
    }

    /**
     * 站点活动
     * @throws Exception
     */
    public PageInfo<Map> zdhd(int page, int rows, String sort, String order,
                              String province, String city, String area,
                              String cultId, String cultName, String cultLevel,
                              String timeScope, String startTime, String endTime
    )throws Exception{
        //参数
        Map<String, Object> paramMap = getParamMap(sort, order, province, city, area, cultId, cultName, timeScope, startTime, endTime, cultLevel);

        //分页查询
        PageHelper.startPage(page, rows);
        List<Map> list = this.tongjiMapper.zdhd(paramMap);
        PageInfo<Map> pageInfo = new PageInfo<Map>(list);

        //数据处理
        if(pageInfo.getTotal() > 0){
            for(Map row : pageInfo.getList()){
                BigDecimal bd_ratebook = (BigDecimal)row.get("ratebook");
                BigDecimal bd_rateseat = (BigDecimal)row.get("rateseat");
                row.remove("ratebook");
                row.remove("rateseat");
                row.put("ratebook", bd_ratebook.toString());//预订率
                row.put("rateseat", bd_rateseat.toString());//上座率
            }
        }
        return pageInfo;
    }


    /**
     * 站点培训
     * @return
     * @throws Exception
     */
    public PageInfo<Map> zdpx(int page, int rows, String sort, String order,
                              String province, String city, String area,
                              String cultId, String cultName, String cultLevel,
                              String timeScope, String startTime, String endTime
    )throws Exception{
        //参数
        Map<String, Object> paramMap = getParamMap(sort, order, province, city, area, cultId, cultName, timeScope, startTime, endTime, cultLevel);

        //分页查询
        PageHelper.startPage(page, rows);
        List<Map> list = this.tongjiMapper.zdpx(paramMap);
        PageInfo<Map> pageInfo = new PageInfo<Map>(list);

        //数据处理
        if(pageInfo.getTotal() > 0){
            for(Map row : pageInfo.getList()){
                BigDecimal bd_ratebook = (BigDecimal)row.get("ratebook");
                BigDecimal bd_rateseat = (BigDecimal)row.get("rateseat");
                row.remove("ratebook");
                row.remove("rateseat");
                row.put("ratebook", bd_ratebook.toString());//报名率
                row.put("rateseat", bd_rateseat.toString());//签到率
            }
        }
        return pageInfo;
    }

    /**
     * 站点场馆
     * @return
     * @throws Exception
     */
    public PageInfo<Map> zdcg(int page, int rows, String sort, String order,
                              String province, String city, String area,
                              String cultId, String cultName, String cultLevel,
                              String timeScope, String startTime, String endTime
    )throws Exception{
        //参数
        Map<String, Object> paramMap = getParamMap(sort, order, province, city, area, cultId, cultName, timeScope, startTime, endTime, cultLevel);

        //分页查询
        PageHelper.startPage(page, rows);
        List<Map> list = this.tongjiMapper.zdcg(paramMap);
        PageInfo<Map> pageInfo = new PageInfo<Map>(list);

        //数据处理
        if(pageInfo.getTotal() > 0){
            for(Map row : pageInfo.getList()){
                Number bd_ratebook = (Number)row.get("ratebook");
                row.remove("ratebook");
                row.put("ratebook", bd_ratebook.toString());//使用率
            }
        }
        return pageInfo;
    }

    /**
     * 站点资讯
     * @return
     * @throws Exception
     */
    public PageInfo<Map> zdzx(int page, int rows, String sort, String order,
                              String province, String city, String area,
                              String cultId, String cultName, String cultLevel,
                              String timeScope, String startTime, String endTime
    )throws Exception{
        //参数
        Map<String, Object> paramMap = getParamMap(sort, order, province, city, area, cultId, cultName, timeScope, startTime, endTime, cultLevel);

        //分页查询
        PageHelper.startPage(page, rows);
        List<Map> list = this.tongjiMapper.zdzx(paramMap);
        PageInfo<Map> pageInfo = new PageInfo<Map>(list);
        return pageInfo;
    }

    /**
     * 首页统计数据
     * @throws Exception
     */
    public Map<String, Object> indexTongji()throws Exception{
        Map<String, Object> returnMap = new HashMap<>();

        //注册用户, 活跃用户, PV, UV
        List<Map> list = this.tongjiMapper.tjptsj();
        if(list != null && list.size() > 0) {
            Map map = list.get(0);
            returnMap.put("reg_user_total", map.get("reg_user_total"));
            returnMap.put("reg_user_month", map.get("reg_user_month"));
            returnMap.put("reg_user_day", map.get("reg_user_month"));
            returnMap.put("hot_user_total", map.get("hot_user_total"));
            returnMap.put("hot_user_month", map.get("hot_user_month"));
            returnMap.put("hot_user_day", map.get("hot_user_day"));
            returnMap.put("pv_total", map.get("pv_total"));
            returnMap.put("pv_month", map.get("pv_month"));
            returnMap.put("pv_day", map.get("pv_day"));
            returnMap.put("uv_total", map.get("uv_total"));
            returnMap.put("uv_month", map.get("uv_month"));
            returnMap.put("uv_day", map.get("uv_day"));
        }

        //年度场馆排行-查询6个
        Integer venTotal = this.tongjiMapper.venActTotal();
        returnMap.put("act_times_total", venTotal);
        PageHelper.startPage(1,6);
        List<Map> venList = this.tongjiMapper.venActPaihang();
        PageInfo<Map> pageInfo = new PageInfo<Map>(venList);
        if(pageInfo.getTotal() > 0){
            for(int i=0; i<pageInfo.getList().size(); i++){
                Map map = pageInfo.getList().get(i);
                returnMap.put("act_times_"+(i+1)+"_name", map.get("name"));
                returnMap.put("act_times_"+(i+1), map.get("acts"));
            }
        }

        //年度最热活动
        Map<String, Object> param = new HashMap<>();
        param.put("fmt", "%Y");
        PageHelper.startPage(1,1);
        List<Map> list_act_year = this.tongjiMapper.hotAct(param);
        PageInfo<Map> pageInfo_act_year = new PageInfo<Map>(list_act_year);
        if(pageInfo_act_year.getList().size() > 0){
            Map map = pageInfo_act_year.getList().get(0);
            String id = (String) map.get("id");
            String cultId = (String) map.get("cultid");
            Object orders = (Object) map.get("orders");
            Object likes = (Object) map.get("likes");
            Object hots = (Object) map.get("hots");

            WhgActivity activity = this.whgActivityService.t_srchOne(id);
            Map<String, Object> act_year = new HashMap<>();
            act_year.put("name", activity.getName());
            act_year.put("imgurl", activity.getImgurl());
            act_year.put("starttime", formatDate(activity.getStarttime()));
            act_year.put("endtime", formatDate(activity.getEndtime()));
            act_year.put("bookTickets", orders);
            act_year.put("likes", likes);
            act_year.put("hots", hots);
            String memo = filterHtml(activity.getRemark());
            if(memo.length() > 50){
                memo = memo.substring(0,50)+"...";
            }
            act_year.put("memo", memo);
            WhgSysCult cult = this.whgSystemCultService.t_srchOne(cultId);
            act_year.put("cultName", cult.getName());

            returnMap.put("act_year", act_year);
        }

        //本月最热活动
        param.clear();
        param.put("fmt", "%Y-%m");
        PageHelper.startPage(1,1);
        List<Map> list_act_month = this.tongjiMapper.hotAct(param);
        PageInfo<Map> pageInfo_act_month = new PageInfo<Map>(list_act_month);
        if(pageInfo_act_month.getList().size() > 0){
            Map map = pageInfo_act_month.getList().get(0);
            String id = (String) map.get("id");
            String cultId = (String) map.get("cultid");
            Object orders = (Object) map.get("orders");
            Object likes = (Object) map.get("likes");
            Object hots = (Object) map.get("hots");

            WhgActivity activity = this.whgActivityService.t_srchOne(id);
            Map<String, Object> act_year = new HashMap<>();
            act_year.put("name", activity.getName());
            act_year.put("imgurl", activity.getImgurl());
            act_year.put("starttime", formatDate(activity.getStarttime()));
            act_year.put("endtime", formatDate(activity.getEndtime()));
            act_year.put("bookTickets", orders);
            act_year.put("likes", likes);
            act_year.put("hots", hots);
            String memo = filterHtml(activity.getRemark());
            if(memo.length() > 50){
                memo = memo.substring(0,50)+"...";
            }
            act_year.put("memo", memo);
            WhgSysCult cult = this.whgSystemCultService.t_srchOne(cultId);
            act_year.put("cultName", cult.getName());

            returnMap.put("act_month", act_year);
        }

        //文化服务趋势-活动-最近六个月
        //最近六个月
        List<String> monthList = getMonth();
        param.clear();
        for(int i=0; i<6; i++){
            returnMap.put("month"+(i+1), monthList.get(i));
            param.put("month"+(i+1), monthList.get(i));
        }
        List<Map> actS = this.tongjiMapper.actSearch(param);
        for(Map map : actS){
            returnMap.put("act_"+(String)map.get("month"), map.get("num"));
        }

        //文化服务趋势-活动-最近六个月
        List<Map> traS = this.tongjiMapper.traSearch(param);
        for(Map map : traS){
            returnMap.put("tra_"+(String)map.get("month"), map.get("num"));
        }

        //返回
        return returnMap;
    }

    /**
     * 获得最近六个月月份
     * @return
     * @throws Exception
     */
    public List<String> getMonth()throws Exception{
        List<String> monthList = new ArrayList<String>();

        Date now = new Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM");
        monthList.add(sdf.format(now));

        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(now);
        for(int i=0; i<5; i++){
            c.add(Calendar.MONTH, -1);
            monthList.add(sdf.format(c.getTime()));
        }
        return monthList;
    }

    private String formatDate(Date date){
        String dStr = "";
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            dStr = sdf.format(date);
        }catch (Exception e){}
        return dStr;
    }

    public static String filterHtml(String str) {
        StringBuffer sb = new StringBuffer();
        if(StringUtils.isNotEmpty(str)) {
            Pattern pattern = Pattern.compile("<([^>]*)>");
            Matcher matcher = pattern.matcher(str);
            boolean result1 = matcher.find();
            while (result1) {
                matcher.appendReplacement(sb, "");
                result1 = matcher.find();
            }
            matcher.appendTail(sb);
        }
        return sb.toString();
    }
}
