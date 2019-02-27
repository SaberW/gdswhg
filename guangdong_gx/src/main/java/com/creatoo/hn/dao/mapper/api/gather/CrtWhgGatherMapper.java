package com.creatoo.hn.dao.mapper.api.gather;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * Created by rbg on 2017/9/26.
 */
public interface CrtWhgGatherMapper {

    /**
     * 查询品牌列表
     * @return
     */
    @SelectProvider(type = CrtWhgGatherProvider.class, method="selectBrands")
    List<Map> selectBrands(@Param("cultid") List<String> cultid,
                           @Param("province")String province,
                           @Param("city")String city,
                           @Param("area")String area,
                           @Param("deptid") List<String> deptid);

    /**
     * 查询指定品牌
     * @param id
     * @return
     */
    @SelectProvider(type = CrtWhgGatherProvider.class, method = "findBrand")
    Map findBrand(@Param("id") String id);

    /**
     * 查询品牌的项目列表
     * @param recode
     * @return
     */
    @SelectProvider(type = CrtWhgGatherProvider.class, method="selectBrandGathers")
    List<Map> selectBrandGathers(Map recode);

    /**
     * 查询品牌的资源信息列表
     * @param recode
     * @return
     */
    @SelectProvider(type = CrtWhgGatherProvider.class, method="selectBrandResources")
    List<Map> selectBrandResources(Map recode);


    /**
     * 查询众筹项目列表
     * @param recode
     * @return
     */
    @SelectProvider(type = CrtWhgGatherProvider.class, method="selectGathers")
    List<Map> selectGathers(Map recode);

    /**
     * 查询众筹项目资源
     * @param recode
     * @return
     */
    @SelectProvider(type = CrtWhgGatherProvider.class, method="selectGatherResources")
    List<Map> selectGatherResources(Map recode);

    /**
     * 查用户订单相关的众筹列表
     * @param recode
     * @return
     */
    @SelectProvider(type = CrtWhgGatherProvider.class, method="selectUserOrderGathers")
    List<Map> selectUserOrderGathers(Map recode);

    /**
     * 查用户收藏表相应的众筹列表
     * @param recode
     * @return
     */
    @SelectProvider(type = CrtWhgGatherProvider.class, method="selectGathers4UserColle")
    List<Map> selectGathers4UserColle(Map recode);

    @SelectProvider(type = CrtWhgGatherProvider.class, method = "findGatherOrder")
    Map findGatherOrder(@Param("id") String id);
}
