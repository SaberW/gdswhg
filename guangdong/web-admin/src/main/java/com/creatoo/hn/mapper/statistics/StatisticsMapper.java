package com.creatoo.hn.mapper.statistics;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 数据统计的DAO
 * Created by wangxl on 2017/6/23.
 */
public interface StatisticsMapper {
    /**
     * 统计控制台PV/UV/IP等
     * @return PV|UV|IP统计数据
     */
    List<Map> selectSYS_PV_UV_IP();

    /**
     * 查询某年用户新增数据
     * @param params
     * @return
     */
    List<Map> selectYearAddUser(Map params);

    /**
     * 查询某月用户新增数据
     * @param params
     * @return
     */
    List<Map> selectMonthAddUser(Map params);

    /**
     * 按类型查询活动室发布数量
     * @return
     */
    List<Map> selectRepCount4RoomType();

    /**
     * 按区域查询活动室发布数量
     * @return
     */
    List<Map> selectRepCount4RoomArea();

    /**
     * 查询年的月份活动室发布数量，参数示例 2017-01-01 00:00:00 - 2018-01-01 00:00:00
     * @param beginYear
     * @param endYear
     * @return
     */
    List<Map> selectRepCount4RoomYear(@Param("beginYear")Date beginYear, @Param("endYear") Date endYear);

    /**
     * 查询活动室访问top10
     * @return
     */
    List<Map> selectRepCount4RoomTop10();

    /**
     * 查询活动室开放时间
     * @param title
     * @return
     */
    List<Map> selectRepTime4RoomOpen(@Param("title") String title);

    /**
     * 查询活动室使用时间
     * @param title
     * @return
     */
    List<Map> selectRepTime4RoomOrder(@Param("title") String title);

    /**
     * 查询指定到起始月的活动室预订短信发送 参数示例 2017-06-01 00:00:00 - 2017-07-01 00:00:00
     * @param beginMonth
     * @param endMonth
     * @return
     */
    List<Map> selectRepSmsCount4Room(@Param("beginMonth")Date beginMonth, @Param("endMonth") Date endMonth);
    /**
     * 查询指定到起始月的培训预订短信发送 参数示例 2017-06-01 00:00:00 - 2017-07-01 00:00:00
     * @param beginMonth
     * @param endMonth
     * @return
     */
    List<Map> selectRepSmsCount4Tra(@Param("beginMonth")Date beginMonth, @Param("endMonth") Date endMonth);
    /**
     * 查询指定到起始月的活动预订短信发送 参数示例 2017-06-01 00:00:00 - 2017-07-01 00:00:00
     * @param beginMonth
     * @param endMonth
     * @return
     */
    List<Map> selectRepSmsCount4Act(@Param("beginMonth")Date beginMonth, @Param("endMonth") Date endMonth);
    /**
     * 查询资讯访问信息 参数示例 2017-06-01 00:00:00 - 2017-07-01 00:00:00
     * @param beginMonth
     * @param endMonth
     * @return
     */
    List<Map> selectRepZxCount(@Param("beginMonth")Date beginMonth, @Param("endMonth") Date endMonth);

    /**
     * 查询活跃用户
     * @param params
     * @return
     */
    List<Map> selectActiveUser(Map params);

    /**
     * 查询某年用户粘度
     * @param params
     * @return
     */
    List<Map> selectActiveUserForYear(Map params);

    /**
     * 用户参加培训的情况
     * @param params
     * @return
     */
    List<Map> selectUserJoinTra(Map params);

    /**
     * 用户参加活动的情况
     * @param params
     * @return
     */
    List<Map> selectUserJoinAct(Map params);
}
