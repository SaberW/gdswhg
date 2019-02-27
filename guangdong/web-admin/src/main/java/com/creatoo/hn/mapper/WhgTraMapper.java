package com.creatoo.hn.mapper;

import com.creatoo.hn.model.WhgTra;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface WhgTraMapper extends Mapper<WhgTra> {
    /**
     * 根据类型进行统计
     * @return
     */
    List<Map> selTraByEtype();
    /**
     * 根据区域进行统计
     * @return
     */
    List<Map> selTraByArea();

    /**
     * 培训访问量TOP10统计
     * @return
     */
    List<Map> t_searchTraTop10();

    /**
     * 查询12个月每月培训发布量
     * @return
     */
    List<Map> srchTraByMonth(@Param("startdate")Date beginYear, @Param("enddate") Date endYear);

    /**
     *查询培训数据
     * @param params
     */
    List<Map> reptra(Map<String, Object> params);

    /**
     * 根据培训id查询课程时间
     * @param traid
     * @return
     */
    Map selCourseByTraid(@Param("traid")String traid);
}