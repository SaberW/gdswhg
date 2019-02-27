package com.creatoo.hn.dao.mapper.api;

import com.creatoo.hn.dao.model.WhgActivityTime;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by rbg on 2017/8/9.
 */
public interface ApiActivityTimeMapper {
    public List<WhgActivityTime> findPlayDate4actId(Map params);

    public List<String> getActDate(@Param("actId")String actId);

    public List<Map> getActTimes(@Param("actId") String actId,@Param("actDate") String actDate);

    Integer getActTicketChecked(@Param("orderId") String orderId);
}
