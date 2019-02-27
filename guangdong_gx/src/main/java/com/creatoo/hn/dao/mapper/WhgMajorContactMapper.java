package com.creatoo.hn.dao.mapper;

import com.creatoo.hn.dao.model.WhgMajorContact;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface WhgMajorContactMapper extends Mapper<WhgMajorContact> {

    /**
     * 查询课程列表
     * @return
     */
    List<Map> selTraInMajor(@Param("mid") String mid);

    /**
     * 查询师资列表
     * @param mid
     * @return
     */
    List<Map> selTeaInMajor(@Param("mid") String mid);

    /**
     * 查询关联资源
     * @param mid
     * @return
     */
    List<Map> selDrscInMajor(@Param("mid")String mid);


    /**
     *  根据培训ID查询相关微专业信息
     * @param id
     * @return
     */
    List<Map> t_selMajorByTraid(@Param("id")String id,@Param("type")int type);
}