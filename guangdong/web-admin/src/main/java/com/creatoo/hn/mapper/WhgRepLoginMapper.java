package com.creatoo.hn.mapper;

import com.creatoo.hn.model.WhgRepLogin;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.common.Mapper;

public interface WhgRepLoginMapper extends Mapper<WhgRepLogin> {
}