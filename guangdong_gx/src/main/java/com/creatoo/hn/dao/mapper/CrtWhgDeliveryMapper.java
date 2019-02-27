package com.creatoo.hn.dao.mapper;

import com.creatoo.hn.dao.model.WhgDelivery;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface CrtWhgDeliveryMapper extends Mapper<WhgDelivery> {


    List<Map> srchListDelivery(Map delivery);
}