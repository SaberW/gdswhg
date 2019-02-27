package com.creatoo.hn.dao.mapper.api;

import org.apache.ibatis.annotations.Param;

/**
 * Created by LENUVN on 2017/9/6.
 */
public interface ExtendDeliveryMapper {

    int selDeliveryTimeCount(@Param("date") String date,@Param("supplyId") String supplyId) ;

    int selHaveDeliveryTimeCount(@Param("date")String date,
                                 @Param("supplyId") String supplyId,
                                 @Param("userId") String userId);
}
