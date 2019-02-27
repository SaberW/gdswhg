package com.creatoo.hn.dao.mapper.api;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by rbg on 2017/8/9.
 */
public interface ApiActivityTicketMapper {


    /**
     * 根据活动订单ID查询活动订单
     * @param orderId
     * @return
     */
    Map queryActOrder(@Param("orderId") String orderId);

    /**
     * 获取活动订单票信息
     * @param orderId
     * @return
     */
    List<Map> queryActTicket(@Param("orderId") String orderId);

    /**
     * 根据座位ID和场次ID验证某场次的某个座位是否已经被预定
     * @param seatId
     * @param eventId
     * @return
     */
    Map queryTicket4Id(@Param("seatId") String seatId,@Param("eventId") String eventId );

    /**
     * 根据座活动ID获得所有被预定票数
     * @return
     */
    Map queryTicketBookCount(@Param("actId") String actId);

    /**
     * 获取场馆信息
     * @param orderId
     * @return
     */
    Map queryVenOrder(@Param("orderId") String orderId);

    void updateActOrderPrintTimes(@Param("orderId") String orderId,@Param("times") Integer times);

    void updateVenOrderPrintTimes(@Param("orderId") String orderId,@Param("times") Integer times);
}
