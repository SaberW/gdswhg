package com.creatoo.hn.dao.mapper.api;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ApiInsGoodsMapper{

    /**
     * 查询商品列表
     * @param recode
     * @return
     */
    public List selectGoodsList(Map recode);

    /**
     * 查询商品
     * @param id
     * @param protype
     * @return
     */
    public Map findGoods(@Param("id")String id, @Param("protype")String protype);

    /**
     * 相关类型的商品列表
     * @param id
     * @param protype
     * @param cultid
     * @return
     */
    public List selectGoodsReflist(@Param("id")String id,
                                   @Param("protype")String protype,
                                   @Param("cultid")List<String> cultid,
                                   @Param("deptid")List<String> deptid);
}
