package com.creatoo.hn.services.admin.trainindex;

import com.alibaba.fastjson.JSONArray;
import com.aliyun.oss.model.LiveChannelGenericRequest;
import com.creatoo.hn.dao.mapper.admin.TrainIndexTongjiMapper;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 培训指数
 * Created by wangxl on 2017/10/22.
 */
@Service
public class TrainIndexService {
    /**
     * 文化馆服务
     */
    @Autowired
    private WhgSystemCultService whgSystemCultService;

    /**
     * 培训指数统计Mapper
     */
    @Autowired
    public TrainIndexTongjiMapper trainIndexTongjiMapper;

    /**
     * 统计全部培训指数
     * @param cultid 文化馆ID,可为空
     * @return Map<String, Integer>
     * @throws Exception
     */
    public Map<String, Integer> countAllTrainIndex(String cultid)throws Exception{
        Map paramMap = genParamMap(null, cultid);
        List<Map> list = this.trainIndexTongjiMapper.countAllTrainIndex(paramMap);
        Map<String, Integer> data = new HashMap<>();
        if(list != null){
            for(Map map : list){
                String name = (String)map.get("name");
                Integer cnt = ((Number)map.get("cnt")).intValue();
                data.put(name, cnt);
            }
        }
        return data;
    }

    /**
     * 统计全部培训指数
     *
     * @return Map<String, Integer>
     * @throws Exception
     */
    public Map<String, Integer> countAllTrainIndex(Map paramMap) throws Exception {
        List<Map> list = this.trainIndexTongjiMapper.countAllTrainIndex(paramMap);
        Map<String, Integer> data = new HashMap<>();
        if (list != null) {
            for (Map map : list) {
                String name = (String) map.get("name");
                Integer cnt = ((Number) map.get("cnt")).intValue();
                data.put(name, cnt);
            }
        }
        return data;
    }

    /**
     * 按区域统计培训发布量
     * @param islive 0-线下培训，1-在线课程
     * @param cultid 文化馆标识
     * @return Map<String, Integer>
     * @throws Exception
     */
    public JSONArray countTrainByArea(Integer islive, String cultid)throws Exception{
        Map paramMap = genParamMap(islive, cultid);
        WhgSysCult cult = whgSystemCultService.t_srchOne(cultid);
        paramMap.put("cultLevel", cult.getLevel());
        List<Map> list = this.trainIndexTongjiMapper.countXXTrainArea(paramMap);
        return change(list);
    }

    /**
     * 按区域统计微专业发布量
     * @param cultid 文化馆标识
     * @return Map<String, Integer>
     * @throws Exception
     */
    public JSONArray countMajorByArea(String cultid)throws Exception{
        Map paramMap = genParamMap(cultid);
        WhgSysCult cult = whgSystemCultService.t_srchOne(cultid);
        paramMap.put("cultLevel", cult.getLevel());
        List<Map> list = this.trainIndexTongjiMapper.countMajorArea(paramMap);
        return change(list);
    }

    /**
     * 按区域统计师资发布量
     * @param cultid 文化馆标识
     * @return Map<String, Integer>
     * @throws Exception
     */
    public JSONArray countTeacherByArea(String cultid)throws Exception{
        Map paramMap = genParamMap(cultid);
        WhgSysCult cult = whgSystemCultService.t_srchOne(cultid);
        paramMap.put("cultLevel", cult.getLevel());
        List<Map> list = this.trainIndexTongjiMapper.countTeacherArea(paramMap);
        return change(list);
    }

    /**
     * 按区域统计培训资源发布量
     * @param cultid 文化馆标识
     * @return Map<String, Integer>
     * @throws Exception
     */
    public JSONArray countDrscByArea(String cultid)throws Exception{
        Map paramMap = genParamMap(cultid);
        WhgSysCult cult = whgSystemCultService.t_srchOne(cultid);
        paramMap.put("cultLevel", cult.getLevel());
        List<Map> list = this.trainIndexTongjiMapper.countDrscArea(paramMap);
        return change(list);
    }

    /**
     * 按类型统计培训发布量
     * @param islive 0-线下培训，1-在线课程
     * @param cultid 文化馆标识
     * @return Map<String, Integer>
     * @throws Exception
     */
    public JSONArray countTrainByType(Integer islive, String cultid)throws Exception{
        Map paramMap = genParamMap(islive, cultid);
        List<Map> list = this.trainIndexTongjiMapper.countXXTrainType(paramMap);
        return change(list);
    }

    /**
     * 按月度统计培训发布量
     * @param islive 0-线下培训，1-在线课程
     * @param cultid 文化馆标识
     * @return Map<String, Integer>
     * @throws Exception
     */
    public JSONArray countTrainByMonth(Integer islive, String cultid)throws Exception{
        Map paramMap = genParamMap(islive, cultid);
        List<Map> list = this.trainIndexTongjiMapper.countXXTrainMonth(paramMap);
        return change(list);
    }

    /**
     * 按月度统计微专业发布量
     * @param cultid 文化馆标识
     * @return Map<String, Integer>
     * @throws Exception
     */
    public JSONArray countMajorByMonth(String cultid)throws Exception{
        Map paramMap = genParamMap(cultid);
        List<Map> list = this.trainIndexTongjiMapper.countMajorByYearAndCult(paramMap);
        return change(list);
    }

    /**
     * 按月度统计师资发布量
     * @param cultid 文化馆标识
     * @return Map<String, Integer>
     * @throws Exception
     */
    public JSONArray countTeacherByMonth(String cultid)throws Exception{
        Map paramMap = genParamMap(cultid);
        List<Map> list = this.trainIndexTongjiMapper.countTeacherByYearAndCult(paramMap);
        return change(list);
    }

    /**
     * 按月度统计资源发布量
     * @param cultid 文化馆标识
     * @return Map<String, Integer>
     * @throws Exception
     */
    public JSONArray countDrscByMonth(String cultid)throws Exception{
        Map paramMap = genParamMap(cultid);
        List<Map> list = this.trainIndexTongjiMapper.countDrscByYearAndCult(paramMap);
        return change(list);
    }

    /**
     * 统计培训访问量
     * @param page 每几页
     * @param rows 每页数
     * @param islive 0-线下培训，1-在线课程
     * @param cultid 文化馆标识
     * @return PageInfo<Map>
     * @throws Exception
     */
    public PageInfo<Map> countTrainVisit(int page, int rows, Integer islive, String cultid, String title, String sort, String order)throws Exception{
        Map paramMap = genParamMap(islive, cultid, sort, order, title);
        PageHelper.startPage(page, rows);
        List<Map> list = this.trainIndexTongjiMapper.countXXTrainVisit(paramMap);
        PageInfo<Map> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    /**
     * 统计培训点赞报名数据
     * @param page 每几页
     * @param rows 每页数
     * @param islive 0-线下培训，1-在线课程
     * @param cultid 文化馆标识
     * @return PageInfo<Map>
     * @throws Exception
     */
    public PageInfo<Map> countTrainData(int page, int rows, Integer islive, String cultid, String title, String sort, String order)throws Exception{
        Map paramMap = genParamMap(islive, cultid, sort, order, title);
        PageHelper.startPage(page, rows);
        List<Map> list = this.trainIndexTongjiMapper.countXXTrainData(paramMap);
        PageInfo<Map> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }


    private JSONArray change(List<Map> list)throws Exception{
        JSONArray data = new JSONArray();
        if(list != null){
            for(Map map : list){
                String name = (String)map.get("name");
                Integer cnt = ((Number)map.get("cnt")).intValue();

                JSONArray row = new JSONArray();
                row.add(name);
                row.add(cnt);
                data.add(row);
            }
        }
        return data;
    }

    private Map genParamMap(String cultid)throws Exception{
        return genParamMap(null, cultid, null, null, null);
    }

    private Map genParamMap(Integer islive, String cultid)throws Exception{
        return genParamMap(islive, cultid, null, null, null);
    }

    private Map genParamMap(Integer islive, String cultid, String sort, String order, String title)throws Exception{
        Map paramMap = new HashMap();
        if(islive != null){
            paramMap.put("islive", islive);
        }
        if(cultid!=null&&StringUtils.isNotEmpty(cultid)){
            paramMap.put("cultid", cultid);
        }
        if(StringUtils.isNotEmpty(sort)){
            paramMap.put("sort", sort);
        }
        if(StringUtils.isNotEmpty(order) && ("desc".equalsIgnoreCase(order) || "asc".equalsIgnoreCase(order))){
            paramMap.put("order", order);
        }else{
            paramMap.put("order", "desc");
        }
        if(StringUtils.isNotEmpty(title)){
            paramMap.put("title", "%"+title+"%");
        }
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy");
        paramMap.put("year", sdf.format(new Date()));
        return paramMap;
    }
}
