package com.creatoo.hn.dao.mapper.api;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ApiInsPersonnelMapper {

    /**
     * 人才列表
     * @param recode
     * @return
     */
    public List selectPersonList(Map recode);

    /**
     * 人才详情
     * @param id
     * @param protype
     * @return
     */
    public Map findPerson(@Param("id")String id, @Param("protype")String protype);

    /**
     * 相关的推荐查询
     * @param id
     * @param protype
     * @return
     */
    public List selectRecommonends(@Param("id")String id,
                                   @Param("protype")String protype,
                                   @Param("cultid")List cultid);
}
