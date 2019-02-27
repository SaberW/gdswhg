package com.creatoo.hn.dao.mapper.api;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApiGlobalSearchMapper {

    /**
     * 外部供需全局搜索
     * @param srchkey
     * @return
     */
    public List globalSearch(@Param("srchkey") String srchkey);
}
