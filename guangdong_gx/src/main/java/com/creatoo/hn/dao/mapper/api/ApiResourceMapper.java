package com.creatoo.hn.dao.mapper.api;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * Created by rbg on 2017/8/10.
 */
public interface ApiResourceMapper {

    /**
     * 查询资源列表
     * @param reftype 对象类型
     * @param refid 实体ID
     * @param enttype   资源类型
     * @return
     */
    @Select("SELECT * FROM whg_resource res " +
            "WHERE res.reftype = #{reftype} " +
            "AND res.refid = #{refid} " +
            "AND res.enttype = #{enttype} " +
            "ORDER BY res.crtdate desc")
    public List<Map> selectResource(@Param("reftype") String reftype, @Param("refid") String refid, @Param("enttype") String enttype);

    /**
     * 查询资源列表
     *
     * @param reftype 对象类型
     * @param refid   实体ID
     * @param enttype 资源类型
     * @return
     */
    @Select("SELECT * FROM whg_resource res " +
            "WHERE res.reftype = #{reftype} " +
            "AND res.refid = #{refid} " +
            "AND res.enttype = #{enttype} " +
            "ORDER BY res.crtdate asc")
    public List<Map> selectResource2(@Param("reftype") String reftype, @Param("refid") String refid, @Param("enttype") String enttype);

    /**
     * 查询品牌活动资源
     * @param id
     * @param enttype
     * @param reftype
     * @return
     */
    List<Map> t_selppSource(@Param("id")String id,@Param("enttype")String enttype, @Param("reftype")String reftype, @Param("name")String name);

    /**
     * 整合查询品牌与关连活动的资源
     * @param id
     * @param enttype
     * @return
     */
    List<Map> t_selppAndtarSource(@Param("id")String id,@Param("enttype")String enttype, @Param("name")String name);

    /**
     *
     * @param refid
     * @param enttype
     * @return
     */
    List<Map> t_selMajorSource(@Param("refid")String refid, @Param("enttype")String enttype, @Param("name")String name);
}
