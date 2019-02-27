package com.creatoo.hn.dao.mapper;

import com.creatoo.hn.dao.model.WhgVisitData;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;

public interface WhgVisitDataMapper extends Mapper<WhgVisitData> {

    @Select("select sum(visit_count) from whg_visit_data where visit_page like #{urlfag}")
    public BigDecimal selectSumCount4Url(@Param("urlfag")String urlfag);
}