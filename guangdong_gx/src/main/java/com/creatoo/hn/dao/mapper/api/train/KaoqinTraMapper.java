package com.creatoo.hn.dao.mapper.api.train;

import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

public interface KaoqinTraMapper {

    @SelectProvider(type = KaoqinTraProvider.class, method="selectTraKaoqin")
    List<Map> selectTraKaoqin(Map record);
}
