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
     * @param traid
     * @param userid
     * @return
     */
    List<Map> selCourseAndEnrol(@Param("traid")String traid, @Param("userid")String userid);
}