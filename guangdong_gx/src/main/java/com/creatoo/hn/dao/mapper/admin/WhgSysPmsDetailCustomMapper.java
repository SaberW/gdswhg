package com.creatoo.hn.dao.mapper.admin;

import com.creatoo.hn.dao.model.WhgSysPmsDetail;
import com.creatoo.hn.dao.model.WhgSysPmsScope;
import com.creatoo.hn.dao.model.WhgSysRolePms;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface WhgSysPmsDetailCustomMapper extends Mapper<WhgSysPmsDetail> {
    int batchInsert(List<WhgSysPmsDetail> list);

    int batchInsertPmsScope(List<WhgSysPmsScope> list);
}