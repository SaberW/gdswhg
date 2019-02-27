package com.creatoo.hn.dao.mapper;

import com.creatoo.hn.dao.model.WhgTraTeacher;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface WhgTraTeacherMapper extends Mapper<WhgTraTeacher> {
    /**
     * 分页查询关联老师
     * @return
     */
    List t_srchTeaList(@Param("mid")String mid, @Param("cultid")String cultid);
}