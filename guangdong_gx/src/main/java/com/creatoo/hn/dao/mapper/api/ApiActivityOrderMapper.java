package com.creatoo.hn.dao.mapper.api;

import com.creatoo.hn.dao.model.WhgActivityTime;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by rbg on 2017/8/9.
 */
public interface ApiActivityOrderMapper {
    public List<Map> findSeat4EventId(Map params);


    /**
     * 获取过期的用户订单
     * @param userid
     * @return
     */
    public List<Map> getUserActOrderTimeOut(@Param("userid")String userid);

    /**
     * 获取未过期的用户订单
     * @param userid
     * @return
     */
    public List<Map> getUserActOrderNotTimeOut(@Param("userid")String userid);


    /**
     * 获取用户所有订单
     * @param userid
     * @returngetUserActOrderAll
     */
    public List<Map> getUserActOrderAll(@Param("userid")String userid);
}
