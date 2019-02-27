package com.creatoo.hn.dao.mapper.api;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApiInsCollectionMapper {

    /**
     * 内部供需收藏查询
     * @param userId
     * @return
     */
    List selectCollections(@Param("userId")String userId,@Param("systype")String systype);

}
