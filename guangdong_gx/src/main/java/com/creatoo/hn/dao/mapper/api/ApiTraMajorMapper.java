package com.creatoo.hn.dao.mapper.api;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/9.
 */
public interface ApiTraMajorMapper {

    /**
     * 查询首页的微专业
     * @param cultid
     * @return
     */
    List<Map> selindexTraMajorList(@Param("cultid")List cultid);


    /**
     * 微专业列表查询
     * @param recode
     * @return
     */
    List selectTraMajorList4page(Map recode);

    /**
     * 根据微专业id查询关联培训
     * @param id
     * @return
     */
    List selTra4mid(@Param("id")String id);

    /**
     * 根据微专业id查询关联老师
     * @param id
     * @return
     */
    List selTea4mid(@Param("id")String id);

    /**
     * 根据微专业id查询关联资源
     * @param id
     * @param etype
     * @return
     */
    List selDrsc4mid(@Param("id")String id, @Param("etype")String etype);
}
