package com.creatoo.hn.dao.mapper.admin;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 自定义查询轮播图Mapper
 * Created by wangxl on 2018/1/29.
 */
public interface WhgYunweiLbtCustomMapper {

    List<Map> queryList(@Param("type")Integer type, @Param("province") String province, @Param("city") String city, @Param("state") Integer state);

    List<Map> queryList4Province(@Param("type")Integer type, @Param("province") String province, @Param("name") String name);

    List<Map> queryList4City(@Param("type")Integer type, @Param("city") String city, @Param("name") String name);
}
