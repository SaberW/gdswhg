package com.creatoo.hn.mapper;

import com.creatoo.hn.model.WhgActActivity;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface WhgActActivityMapper extends Mapper<WhgActActivity> {

    /**
     * 根据类型统计
     * @return
     */
    List<Map> selActByEtype();

    /**
     * 根据区域统计
     * @return
     */
    List<Map> selActByArea();

    /**
     * 活动访问量top10统计
     * @return
     */
    List<Map> t_searchActTop10();

    /**
     * 1-12月发布活动统计
     * @return
     */
    List<Map> srchActByMonth(@Param("startdate")Date beginYear, @Param("enddate") Date endYear);

    /**
     * 活动相关数据统计
     * @return
     */
    List<Map> repAct(Map<String, Object> params);
}