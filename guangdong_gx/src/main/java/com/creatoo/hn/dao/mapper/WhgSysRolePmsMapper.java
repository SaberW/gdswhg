package com.creatoo.hn.dao.mapper;

import com.creatoo.hn.dao.model.WhgSysRolePms;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface WhgSysRolePmsMapper extends Mapper<WhgSysRolePms> {

    int batchInsert(List<WhgSysRolePms> list);
    List<Map> srchRoleList(Map list);
}