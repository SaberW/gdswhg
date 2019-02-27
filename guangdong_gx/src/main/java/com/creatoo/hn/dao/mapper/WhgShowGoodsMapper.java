package com.creatoo.hn.dao.mapper;

import com.creatoo.hn.dao.model.WhgShowGoods;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface WhgShowGoodsMapper extends Mapper<WhgShowGoods> {

    /**
     * 查询展演类商品列表
     * @param cultid
     * @param type
     * @param title
     * @return
     */
    List selInsShowGoodsList(@Param("cultid")List cultid,@Param("deptid")List deptid, @Param("type")String type, @Param("title")String title, @Param("protype")String protype, @Param("province")String province, @Param("city")String city, @Param("area")String area, @Param("psprovince")String psprovince, @Param("pscity")String pscity);

    /**
     * 查询展演类商品详情
     * @param id
     * @param protype
     * @return
     */
    Map selectShowGoodsById(@Param("id")String id, @Param("protype")String protype);

    /**
     * 相似表演
     * @param protype
     * @param cultid
     * @param id
     * @return
     */
    List selectShowRecommend(@Param("protype")String protype, @Param("cultid")List cultid, @Param("id")String id);
}