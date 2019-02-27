package com.creatoo.hn.dao.mapper;

import com.creatoo.hn.dao.model.WhgDrsc;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface WhgDrscMapper extends Mapper<WhgDrsc> {
    /**
     * 查询已发布资源列表
     * @return
     */
    List selDrscList(@Param("mid") String mid,@Param("cultid")String cultid);
}