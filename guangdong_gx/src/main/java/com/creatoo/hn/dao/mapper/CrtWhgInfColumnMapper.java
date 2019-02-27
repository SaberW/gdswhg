package com.creatoo.hn.dao.mapper;

import com.creatoo.hn.dao.model.WhgInfColumn;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface CrtWhgInfColumnMapper extends Mapper<WhgInfColumn> {

    List<WhgInfColumn> srchColumnTree(Map map);
}