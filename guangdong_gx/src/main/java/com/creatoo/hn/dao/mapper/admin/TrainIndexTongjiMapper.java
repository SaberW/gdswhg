package com.creatoo.hn.dao.mapper.admin;

import java.util.List;
import java.util.Map;

/**
 * 培训指数统计
 * Created by wangxl on 2017/10/20.
 */
public interface TrainIndexTongjiMapper {
    /**
     * 根据文化馆标识统计线下培训发布数量-截止到当前日期。不带cultid参数，就是统计全部
     * @param param
     * @return
     */
    List<Map> countAllTrainIndex(Map param);

    /**
     * 根据文化馆标识统计线下培训发布数量-按区域。不带cultid参数，就是统计全部
     * @param param islive=0表示线下培训 islive=1表示在线课程
     * @return
     */
    List<Map> countXXTrainArea(Map param);

    /**
     * 根据文化馆标识统计发布数量-微专业-按区域
     * @param param
     * @return
     */
    List<Map> countMajorArea(Map param);

    /**
     * 根据文化馆标识统计发布数量-师资-按区域
     * @param param
     * @return
     */
    List<Map> countTeacherArea(Map param);

    /**
     * 根据文化馆标识统计发布数量-培训资源-按区域
     * @param param
     * @return
     */
    List<Map> countDrscArea(Map param);

    /**
     * 根据文化馆标识统计线下培训发布数量-按类型
     * @param param
     * @return
     */
    List<Map> countXXTrainType(Map param);

    /**
     * 根据文化馆标识统计线下培训发布数量-按年查询月份
     * @param param
     * @return
     */
    List<Map> countXXTrainMonth(Map param);

    /**
     * 根据文化馆标识统计线下培训访问量
     * @param param
     * @return
     */
    List<Map> countXXTrainVisit(Map param);

    /**
     * 数据统计
     * @param param
     * @return
     */
    List<Map> countXXTrainData(Map param);

    /**
     * 根据文化馆标识统计发布数量-微专业-按年查询月份
     * @param param
     * @return
     */
    List<Map> countMajorByYearAndCult(Map param);

    /**
     * 根据文化馆标识统计发布数量-师资-按年查询月份
     * @param param
     * @return
     */
    List<Map> countTeacherByYearAndCult(Map param);

    /**
     * 根据文化馆标识统计发布数量-培训资源-按年查询月份
     * @param param
     * @return
     */
    List<Map> countDrscByYearAndCult(Map param);
}
