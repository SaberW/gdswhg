package com.creatoo.hn.mapper;

import com.creatoo.hn.model.WhgTraEnrol;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface WhgTraEnrolMapper extends Mapper<WhgTraEnrol> {

    /**
     * 根据用户Id获取用户报名订单/历史订单
     * @param params
     * @return
     */
    public List<Map<String,Object>> getTraEnrolListByUserId(Map<String,Object> params);

    /**
     * 查询报名数量
     * @return
     */
    int selEnrolCount(@Param("userId")String userId, @Param("now")Date now,@Param("beginYear")Date beginYear,@Param("endYear")Date endYear);

    /**
     * 查询上课时间是否有交叉
     * @param starttime
     * @param endtime
     * @param userId
     * @return
     */
    int selCount(@Param("starttime")Date starttime, @Param("endtime")Date endtime, @Param("userid")String userId,@Param("now")Date now);

    /**
     * 查询报名以及课程信息
     * @param traid
     * @param userid
     * @return
     */
    List<Map> selCourseAndEnrol(@Param("traid")String traid, @Param("userid")String userid);

    /**
     * 秋季班报两个
     * @param userId
     * @param now
     * @param myDate
     * @return
     */
    int selQjEnrolCount(@Param("userId")String userId, @Param("myDate")Date myDate);
}