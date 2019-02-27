package com.creatoo.hn.dao.mapper;


import com.creatoo.hn.dao.model.WhgTraEnrol;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface WhgTraEnrolMapper extends Mapper<WhgTraEnrol> {


    /**
     * 查询上课时间是否有交叉
     * @param starttime
     * @param endtime
     * @param userId
     * @return
     */
    int selCount(@Param("starttime") Date starttime, @Param("endtime") Date endtime, @Param("userid") String userId, @Param("now") Date now);


    /**
     * 查询报名以及课程信息
     * @return
     */
    List<Map> selCourseAndEnrol(Map param);

    /**
     * 查询请假的培训名称
     * @return
     */
    List<Map> t_srchTra4Leave();

    /**
     * 查询报名以及课程信息
     * @param traid
     * @param courseid
     * @return
     */
    List<Map> t_srchOne4Courseid(@Param("traid")String traid, @Param("courseid")String courseid);

    List<Map> t_srchUserTraList4p(@Param("traid") String traid, @Param("emap") Map emap);

    List<Map> srchListOrders(Map record);

    int selectOrdersCount(Map record);
}